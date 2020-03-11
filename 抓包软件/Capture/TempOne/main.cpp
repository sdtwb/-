#include "Analysis.h"
#include "Main_Head.h"
#include "pcap.h"
#include <iostream>
#include <iostream>
#include<time.h>
#include <fstream>
#include <string>
#include <cstring>
#include <string.h>
#include <WinSock2.h>
using namespace std;

string DATA_PATH = "./Data/";

Analysis* analysis = new Analysis;

void getDev();
int  capture(int choice);

int main() {

	int choice= 0;
	cout << "���������¹��ܣ�\n 0--ץȡ���ݰ� \n 1--��ѯ�������ݰ�" << endl;
	cout << "�����룺 ";
	cin >> choice;

	switch (choice) {
	case 0:
		getDev();
		cout << "Please input your choice:" << endl;
		cin >> choice;
		cout << "Result:" << endl;
		capture(choice);
		break;
	case 1:
		analysis->check();
		break;
	}

}


int  capture(int choice)
{
	pcap_if* alldev;
	pcap_if* dev;
	char errbuf[PCAP_ERRBUF_SIZE];

	int devCount = 0;
	if (pcap_findalldevs(&alldev, errbuf) == -1)
		return -1;
	for (dev = alldev; dev; dev = dev->next) {
		devCount++;
	}

	dev = alldev;
	pcap_t* adhandle;
	int count = 0;
	for (; count < choice; count++)
		dev = dev->next;

	// �豸�� �������ݰ����� ����ģʽ (��0��ζ���ǻ���ģʽ) ����ʱ���� ������Ϣ
	if ((adhandle = pcap_open_live(dev->name, 65536, 1, 1000, errbuf)) == NULL) {
		cout << "�޷��򿪽ӿ�: " << dev->description << endl;
	}

	int res, nItem;
	struct tm* ltime;
	string timestr, buf, srcMac, destMac;
	time_t local_tv_sec;
	struct pcap_pkthdr* header;									  //���ݰ�ͷ
	const u_char* pkt_data = NULL, * pData = NULL;     //�������յ����ֽ�������
	u_char* ppkt_data;
	struct pktcount npacket;
	int num_pkg = 0;



	while ((res = pcap_next_ex(adhandle, &header, &pkt_data)) >= 0)
	{

		if (res == 0)				//��ʱ
			continue;

		struct datapkt* data = (struct datapkt*)malloc(sizeof(struct datapkt));

		if (NULL == data) {
			cout << "�ռ��������޷������µ����ݰ�" << endl;
			return -1;
		}
		else {
			memset(data, 0, sizeof(struct datapkt));
		}



		//������������������ݰ����ڴ���Χ��
		if (analysis->analyze_frame(pkt_data, data, &(npacket)) < 0)
			continue;


		/*Ԥ�������ʱ�䡢����*/
		data->len = header->len;
		local_tv_sec = header->ts.tv_sec;
		ltime = localtime(&local_tv_sec);

		data->time[0] = ltime->tm_year + 1900;
		data->time[1] = ltime->tm_mon + 1;
		data->time[2] = ltime->tm_mday;
		data->time[3] = ltime->tm_hour;
		data->time[4] = ltime->tm_min;
		data->time[5] = ltime->tm_sec;

		string time;
		char temp[1024 * 20];
		/*��ʾʱ���*/
		sprintf(temp, "%d-%d-%d %d-%d-%d", data->time[0], data->time[1], data->time[2], data->time[3], data->time[4], data->time[5]);
		time = temp;

		//ԴMAC
		string smac;
		sprintf(temp, "%02X-%02X-%02X-%02X-%02X-%02X", data->ethh->src[0], data->ethh->src[1],
			data->ethh->src[2], data->ethh->src[3], data->ethh->src[4], data->ethh->src[5]);
		smac = temp;


		//Ŀ��MAC
		string dmac;
		sprintf(temp, "%02X-%02X-%02X-%02X-%02X-%02X", data->ethh->dest[0],
			data->ethh->dest[1], data->ethh->dest[2], data->ethh->dest[3], data->ethh->dest[4], data->ethh->dest[5]);
		dmac = temp;


		string sip;
		/*���ԴIP*/
		if (0x0806 == data->ethh->type)
		{
			sprintf(temp, "%d.%d.%d.%d", data->arph->ar_srcip[0],
				data->arph->ar_srcip[1], data->arph->ar_srcip[2], data->arph->ar_srcip[3]);
			sip = temp;
		}
		else if (0x0800 == data->ethh->type) {
			struct  in_addr in;
			in.S_un.S_addr = data->iph->saddr;
			buf = string(inet_ntoa(in));
			sip = buf;
		}
		else if (0x86dd == data->ethh->type) {
			int n;
			for (n = 0; n < 8; n++)
			{
				if (n <= 6) {
					sprintf(temp, "%02x:", data->iph6->saddr[n]);
					sip += temp;
				}
				else {
					sprintf(temp, "%02x", data->iph6->saddr[n]);
					sip += temp;
				}

			}
		}

		string dip;
		//Ŀ��IP
		if (0x0806 == data->ethh->type)  //arp
		{
			sprintf(temp, "%d.%d.%d.%d", data->arph->ar_destip[0],
				data->arph->ar_destip[1], data->arph->ar_destip[2], data->arph->ar_destip[3]);
			dip = temp;
		}
		else if (0x0800 == data->ethh->type) {  //ip
			struct  in_addr in;
			in.S_un.S_addr = data->iph->daddr;
			buf = string(inet_ntoa(in));
			dip = buf;
		}
		else if (0x86dd == data->ethh->type) {
			int n;
			for (n = 0; n < 8; n++)
			{
				if (n <= 6) {
					sprintf(temp, "%02x:", data->iph6->daddr[n]);
					dip += temp;
				}
				else {
					sprintf(temp, "%02x", data->iph6->daddr[n]);
					dip += temp;
				}

			}
		}

		//���ݰ�����
		sprintf(temp, "%s", pkt_data);
		string data_pkg = temp;


		string file_name = string(data->pktType) + "_" + time + "_" + to_string(num_pkg) + ".txt";
		string data_str = data_pkg;

		ofstream out_file;
		out_file.open(DATA_PATH + file_name, std::fstream::in | std::fstream::out | std::fstream::app);
		out_file << data_str << endl;
		out_file.close();

		cout << string(data->pktType) << endl;
		cout << num_pkg << " " << data->pktType << " " << time << " " << sip << " " << dip << " " << data->len << endl;

		num_pkg++;
	}
	return 1;
}

//��ȡ������
void getDev() {
	system("cls");
	cout << "��ĵ�����һ����������ѡ������һ��" << endl;
	pcap_if* alldev;
	pcap_if* dev;
	char errbuf[PCAP_ERRBUF_SIZE];

	int devCount = 0;
	if (pcap_findalldevs(&alldev, errbuf) == -1)
		return;
	for (dev = alldev; dev; dev = dev->next) {
		cout << devCount << " " << dev->description << endl;
		devCount++;
	}

}