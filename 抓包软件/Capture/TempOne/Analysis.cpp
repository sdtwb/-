#include "Analysis.h"
using namespace std;

//���캯��
Analysis::Analysis() {

}

/*��������㣺ARP*/
int Analysis::analyze_arp(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	int i;
	struct arphdr* arph = (struct arphdr*)pkt;
	data->arph = (struct arphdr*)malloc(sizeof(struct arphdr));

	if (NULL == data->arph)
		return -1;

	//����IP��MAC
	for (i = 0; i < 6; i++)
	{
		if (i < 4)
		{
			data->arph->ar_destip[i] = arph->ar_destip[i];
			data->arph->ar_srcip[i] = arph->ar_srcip[i];
		}
		data->arph->ar_destmac[i] = arph->ar_destmac[i];
		data->arph->ar_srcmac[i] = arph->ar_srcmac[i];
	}

	data->arph->ar_hln = arph->ar_hln;
	data->arph->ar_hrd = ntohs(arph->ar_hrd);
	data->arph->ar_op = ntohs(arph->ar_op);
	data->arph->ar_pln = arph->ar_pln;
	data->arph->ar_pro = ntohs(arph->ar_pro);

	strcpy(data->pktType, "ARP");
	npacket->n_arp++;
	return 1;
}


/*��������㣺ICMP*/
int Analysis::analyze_icmp(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	struct icmphdr* icmph = (struct icmphdr*)pkt;
	data->icmph = (struct icmphdr*)malloc(sizeof(struct icmphdr));

	if (NULL == data->icmph)
		return -1;

	data->icmph->chksum = icmph->chksum;
	data->icmph->code = icmph->code;
	data->icmph->seq = icmph->seq;
	data->icmph->type = icmph->type;
	strcpy(data->pktType, "ICMP");
	npacket->n_icmp++;
	return 1;
}

/*��������㣺ICMPv6*/
int Analysis:: analyze_icmp6(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	int i;
	struct icmphdr6* icmph6 = (struct icmphdr6*)pkt;
	data->icmph6 = (struct icmphdr6*)malloc(sizeof(struct icmphdr6));

	if (NULL == data->icmph6)
		return -1;

	data->icmph6->chksum = icmph6->chksum;
	data->icmph6->code = icmph6->code;
	data->icmph6->seq = icmph6->seq;
	data->icmph6->type = icmph6->type;
	data->icmph6->op_len = icmph6->op_len;
	data->icmph6->op_type = icmph6->op_type;
	for (i = 0; i < 6; i++)
	{
		data->icmph6->op_ethaddr[i] = icmph6->op_ethaddr[i];
	}
	strcpy(data->pktType, "ICMPv6");
	npacket->n_icmp6++;
	return 1;
}

/*��������㣺TCP*/
int Analysis::analyze_tcp(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	struct tcphdr* tcph = (struct tcphdr*)pkt;
	data->tcph = (struct tcphdr*)malloc(sizeof(struct tcphdr));
	if (NULL == data->tcph)
		return -1;

	data->tcph->ack_seq = tcph->ack_seq;
	data->tcph->check = tcph->check;

	data->tcph->doff = tcph->doff;
	data->tcph->res1 = tcph->res1;
	data->tcph->cwr = tcph->cwr;
	data->tcph->ece = tcph->ece;
	data->tcph->urg = tcph->urg;
	data->tcph->ack = tcph->ack;
	data->tcph->psh = tcph->psh;
	data->tcph->rst = tcph->rst;
	data->tcph->syn = tcph->syn;
	data->tcph->fin = tcph->fin;
	//data->tcph->doff_flag = tcph->doff_flag;

	data->tcph->dport = ntohs(tcph->dport);
	data->tcph->seq = tcph->seq;
	data->tcph->sport = ntohs(tcph->sport);
	data->tcph->urg_ptr = tcph->urg_ptr;
	data->tcph->window = tcph->window;
	data->tcph->opt = tcph->opt;

	/////////////////////*��Ҫ����http��֧*/////////////////////////
	if (ntohs(tcph->dport) == 80 || ntohs(tcph->sport) == 80)
	{
		npacket->n_http++;
		strcpy(data->pktType, "HTTP");
	}
	else {
		npacket->n_tcp++;
		strcpy(data->pktType, "TCP");
	}
	return 1;
}

/*��������㣺UDP*/
int Analysis::analyze_udp(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	struct udphdr* udph = (struct udphdr*)pkt;
	data->udph = (struct udphdr*)malloc(sizeof(struct udphdr));
	if (NULL == data->udph)
		return -1;

	data->udph->check = udph->check;
	data->udph->dport = ntohs(udph->dport);
	data->udph->len = ntohs(udph->len);
	data->udph->sport = ntohs(udph->sport);

	strcpy(data->pktType, "UDP");
	npacket->n_udp++;
	return 1;
}


/*��������㣺IP*/
int Analysis::analyze_ip(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	//int i;
	struct iphdr* iph = (struct iphdr*)pkt;
	data->iph = (struct iphdr*)malloc(sizeof(struct iphdr));

	if (NULL == data->iph)
		return -1;
	data->iph->check = iph->check;
	npacket->n_ip++;

	data->iph->saddr = iph->saddr;
	data->iph->daddr = iph->daddr;

	data->iph->frag_off = iph->frag_off;
	data->iph->id = iph->id;
	data->iph->proto = iph->proto;
	data->iph->tlen = ntohs(iph->tlen);
	data->iph->tos = iph->tos;
	data->iph->ttl = iph->ttl;
	data->iph->ihl = iph->ihl;
	data->iph->version = iph->version;
	//data->iph->ver_ihl= iph->ver_ihl;
	data->iph->op_pad = iph->op_pad;

	int iplen = iph->ihl * 4;							//ipͷ����
	switch (iph->proto)
	{
	case PROTO_ICMP:
		return analyze_icmp((u_char*)iph + iplen, data, npacket);
		break;
	case PROTO_TCP:
		return analyze_tcp((u_char*)iph + iplen, data, npacket);
		break;
	case PROTO_UDP:
		return analyze_udp((u_char*)iph + iplen, data, npacket);
		break;
	default:
		return-1;
		break;
	}
	return 1;
}

/*��������㣺IPV6*/
int Analysis::analyze_ip6(const u_char* pkt, datapkt* data, struct pktcount* npacket)
{
	int i;
	struct iphdr6* iph6 = (struct iphdr6*)pkt;
	data->iph6 = (struct iphdr6*)malloc(sizeof(struct iphdr6)+ 5);

	if (NULL == data->iph6)
		return -1;

	npacket->n_ip6++;

	data->iph6->version = iph6->version;
	data->iph6->flowtype = iph6->flowtype;
	data->iph6->flowid = iph6->flowid;
	data->iph6->plen = ntohs(iph6->plen);
	data->iph6->nh = iph6->nh;
	data->iph6->hlim = iph6->hlim;

	for (i = 0; i < 16; i++)
	{
		data->iph6->saddr[i] = iph6->saddr[i];
		data->iph6->daddr[i] = iph6->daddr[i];
	}

	switch (iph6->nh)
	{
	case 0x3a:
		return analyze_icmp6((u_char*)iph6 + 40, data, npacket);
		break;
	case 0x06:
		return analyze_tcp((u_char*)iph6 + 40, data, npacket);
		break;
	case 0x11:
		return analyze_udp((u_char*)iph6 + 40, data, npacket);
		break;
	default:
		return-1;
		break;
	}
	//npacket->n_ip6++;
	//strcpy(data->pktType,"IPV6");
	return 1;
}

/*������·��*/
int Analysis::analyze_frame(const u_char* pkt, struct datapkt* data, struct pktcount* npacket)
{
	int i;
	struct ethhdr* ethh = (struct ethhdr*)pkt;
	data->ethh = (struct ethhdr*)malloc(sizeof(struct ethhdr));
	if (NULL == data->ethh)
		return -1;

	for (i = 0; i < 6; i++)
	{
		data->ethh->dest[i] = ethh->dest[i];
		data->ethh->src[i] = ethh->src[i];
	}

	npacket->n_sum++;

	/*���������ֽ�˳��ԭ����Ҫ��*/
	data->ethh->type = ntohs(ethh->type);

	//����ARP����IP����
	switch (data->ethh->type)
	{
	case 0x0806:
		return analyze_arp((u_char*)pkt + 14, data, npacket);      //mac ͷ��СΪ14
		break;
	case 0x0800:
		return analyze_ip((u_char*)pkt + 14, data, npacket);
		break;
	case 0x86dd:
		return analyze_ip6((u_char*)pkt + 14, data, npacket);
		return -1;
		break;
	default:
		npacket->n_other++;
		return -1;
		break;
	}
	return 1;
}


void Analysis::getAllFiles(string path, vector<string>& files, string format)
{
	long  hFile = 0;//�ļ����  64λ��long ��Ϊ intptr_t
	struct _finddata_t fileinfo;//�ļ���Ϣ 
	string p;
	if ((hFile = _findfirst(p.assign(path).append("\\*" + format).c_str(), &fileinfo)) != -1) //�ļ�����
	{
		do
		{
			if ((fileinfo.attrib & _A_SUBDIR))//�ж��Ƿ�Ϊ�ļ���
			{
				if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0)//�ļ������в���"."��".."
				{
					files.push_back(p.assign(path).append("\\").append(fileinfo.name)); //�����ļ�����
					getAllFiles(p.assign(path).append("\\").append(fileinfo.name), files, format); //�ݹ�����ļ���
				}
			}
			else
			{
				files.push_back(fileinfo.name);//��������ļ��У������ļ���
			}
		} while (_findnext(hFile, &fileinfo) == 0);
		_findclose(hFile);
	}
}

extern string DATA_PATH;
void Analysis::check()
{


	//��ȡ�����ļ�
	string filePath = ".\\Data";
	vector<string> files;
	string format = "";
	getAllFiles(filePath, files, format);

	//��ѯ������
	string pty;


	while (true) {
		cout << "֧�ֲ�ѯ��" << endl;
		cout << "TCP UDP HTTP" << endl;
		cout << "���������ݰ�����(�˳�����Q)��" << endl;

		cin >> pty;

		if (pty == "Q") {
			system("cls");
			break;
		}

		vector<string> checkFiles;

		//��ȡ���˺���ļ���
		for (int i = 0; i < files.size(); i++)
		{
			if (files[i].substr(0, pty.size()) == pty) {
				checkFiles.push_back(files[i]);
			}
		}



		if (checkFiles.size() == 0) {
			cout << "�Ѳ����" + pty + "���ݰ�Ϊ��" << endl;
			system("pause");
			system("cls");
			continue;
		}

		int page = 100;

		cout << "�Ѳ����" + pty + "���ݰ��б����£�" << endl;
		for (int i = 0; i != checkFiles.size(); i++) {
			cout << "ID��" << i << " Package: " << checkFiles[i] << endl;

			//ÿ���100��ѯ��һ��
			if ((i + 1) % page == 0 || i == checkFiles.size() - 1) {

				while (true) {
					cout << "�˳�������Q" << endl;
					cout << "��ѯ������C" << endl;
					cout << "�����룺";
					string choice;
					cin >> choice;
					if (choice == "Q") {
						system("cls");
						break;
					}
					else if(choice== "C"||choice== "c")
						break;

					//�ǲ�ѯ���˳�������������
				}

				int ID;
				cout << "���������ݰ�ID" << endl;
				cin >> ID;

				//��ȡ�ļ�����
				fstream in(DATA_PATH + checkFiles[ID]);
				string input;
				in >> input;
				in.close();

				// ����������
				struct pktcount* npacket = (struct pktcount*)malloc(sizeof(struct pktcount));
				struct datapkt* data = (struct datapkt*)malloc(sizeof(struct datapkt));
				u_char* p = (u_char*)input.c_str();

				//���
				if (pty == "UDP") {
					analyze_udp(p, data, npacket);
					struct udphdr* udp = data->udph;
					cout << "Դ�˿ںţ�" << udp->sport << " "
						<< "Ŀ�Ķ˿ڣ�" << udp->dport << " "
						<< "У��ͣ� " << udp->check << " "
						<< "���ݰ����ȣ�" << udp->len << endl;
				}
				else if (pty == "TCP" || pty == "HTTP") {
					analyze_tcp(p, data, npacket);
					struct tcphdr* tcph = data->tcph;
					cout << "Դ�˿ںţ�" << tcph->sport << " "
						<< "Ŀ�Ķ˿ڣ�" << tcph->dport << " "
						<< "���кţ�" << tcph->seq << " "
						<< "ȷ�����кţ�" << tcph->ack_seq << " "
						<< "res1:" << tcph->res1 << " "
						<< "doff:" << tcph->doff << " "
						<< "fin:" << tcph->fin << " "
						<< "syn:" << tcph->syn << " "
						<< "rst:" << tcph->rst << " "
						<< "psh:" << tcph->psh << " "
						<< "ack:" << tcph->ack << " "
						<< "urg:" << tcph->urg << " "
						<< "ece:" << tcph->ece << " "
						<< "cwr:" << tcph->cwr << " "
						<< "���ڴ�С��" << tcph->window << " "
						<< "У��ͣ�" << tcph->check << " "
						<< "����ָ�룺" << tcph->urg_ptr << " "
						<< "ѡ�" << tcph->opt << endl;
				}
				else if (pty == "ICMP") {
					analyze_icmp(p, data, npacket);
					cout << string(data->pktType) << endl;
					system("pause");

					struct icmphdr* icmph = data->icmph;
					cout << "���ͣ�" << icmph->type << " "
						<< "���룺" << icmph->code << " "
						<< "���кţ�" << icmph->seq << " "
						<< "У��ͣ�" << icmph->chksum << endl;
				}
				else if (string(data->pktType) == "") {

				}

				free(npacket);
				free(data);
				system("pause");
				if (i == checkFiles.size() - 1) {
					system("cls");
				}
			}

		}



	}


}