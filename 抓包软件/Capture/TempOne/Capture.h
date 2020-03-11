#pragma once
#include "Main_Head.h"
#include <iostream>
#include<time.h>
#include <fstream>
#include <string>
#include <cstring>
#include <string.h>
#include <WinSock2.h>

#include "Analysis.h"


extern string DATA_PATH;
extern Analysis* analysis;

class Capture {
public:
	int  capture(int choice);

	void getDev();

};