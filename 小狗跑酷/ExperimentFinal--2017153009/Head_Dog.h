#pragma once
#ifndef _Head_Dog_
#define _Head_Dog_
#include "Const_Head.h"
#include "Angel.h"

// Joint angles with initial values
GLfloat theta[NumJointAngles] = {
	0.0,    // Torso
	0.0,    // Neck
	0.0,    // Head2
	200.0,    // RightUpperArm
	300.0,    // RightLowerArm
	200.0,    // LeftUpperArm
	300.0,    // LeftLowerArm
	200.0,  // RightUpperLeg
	300.0,    // RightLowerLeg
	200.0,  // LeftUpperLeg
	300.0 ,    // LeftLowerLeg
	210.0,	//LeftEar
	150		//RightEar
};

color4 colorDog = vec4(0.8, 0.8, 0.8, 1.0);

//狗身体的各部分长度
double TORSO_HEIGHT = 2.0; double TORSO_WIDTH = 3.0; double TORSO_LONG = 6;
double UPPER_ARM_HEIGHT = 2.0; double UPPER_ARM_WIDTH = 0.5;
double LOWER_ARM_HEIGHT = 3.0; double LOWER_ARM_WIDTH = 0.5;
double UPPER_LEG_HEIGHT = 2.0; double UPPER_LEG_WIDTH = 0.5;
double LOWER_LEG_HEIGHT = 3.0; double LOWER_LEG_WIDTH = 0.5;
double HEAD_HEIGHT = 1.5; double HEAD_WIDTH = 1.5; double HEAD_LONG = 2.5;
double NECK_HEIGHT = 2; double NECK_WIDTH = 1;
double EAR_HEIGHT = 2; double EAR_WIDTH = 0.5; double EAR_LONG = 0.5;
double TAIL_HEIGHT = 1; double TAIL_WIDTH = 0.5;

double Body_Offset = UPPER_LEG_HEIGHT + LOWER_LEG_HEIGHT;

GLuint ModelView, Projection;
GLuint draw_color;
GLuint modelMatrix;
GLuint viewMatrix;

mat4 model_view;

//狗身体的缩放比
double ZOOM = 0.6;

//狗方向和位置的初始
int dogDirection = 0;
int dogAngleY = 270;
int dogAxisZ = 0;
int dogAxisX = 6;

#endif
