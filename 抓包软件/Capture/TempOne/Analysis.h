#pragma once
#include "Main_Head.h"
#include<vector>
#include <iostream>
#include <string>
#include <WinSock2.h>
#include<fstream>
#include <cstring>

#pragma comment(lib, "Wsock32.lib")

class Analysis {

public:
	Analysis();

	int analyze_arp(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_icmp(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_icmp6(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_tcp(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_udp(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_ip(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_ip6(const u_char* pkt, datapkt* data, struct pktcount* npacket);

	int analyze_frame(const u_char* pkt, struct datapkt* data, struct pktcount* npacket);

	void getAllFiles(std::string path, std::vector<std::string>& files, std::string format);

	void check();
};
