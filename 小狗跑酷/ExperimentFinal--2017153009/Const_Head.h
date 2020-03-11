#pragma once
#ifndef _Const_Head_
#define  _Const_Head_
#include "Angel.h"
#include <cstring>

const int NumVertices = 36; //(6 faces)(2 triangles/face)(3 vertices/triangle)
const int NumJointAngles = 13;
const int NumFrame = 13 * 6;

const double topTrackZ = -10;  //最上面那条道的Z轴坐标
const double perTurnAngle = 4;  //狗转头的角度步幅


const vec4 BLACK(0.0, 0.0, 0.0, 1.0);	//black
const vec4 Red(1.0, 0.0, 0.0, 1.0);	//red
const vec4 White(1.0, 1.0, 1.0, 1.0);  // White

const vec3 lightPos(-5, 40, 5);  //光源的初始位置

const GLfloat distTL = 0.01;	//默认光源到物体的距离

const int limitDit = 4;

const float dogMoveFrame[NumFrame] = {
	5, 40, 315, 200, 310, 220, 315, 170, 305, 155, 330, 260, 110,//每帧13个角度
	0, 40, 315, 165, 315, 195, 320, 165, 335, 180, 345, 260, 110,
	 355, 40, 320, 160, 315, 170, 335, 180, 335, 195, 345, 270, 100,
	 360, 60, 305, 145, 340, 155, 335, 200, 335, 210, 345, 270, 90,
	 5, 60, 305, 180, 340, 155, 335, 200, 345, 210, 345, 280, 80,
	 10, 60, 305, 195, 340, 155, 335, 205, 345, 215, 345, 290, 65
};

typedef Angel::vec4 point4;
typedef Angel::vec4 color4;

// RGBA colors
const color4 vertex_colors[8] = {
	color4(0.0, 0.0, 0.0, 1.0),  // black
	color4(1.0, 0.0, 0.0, 1.0),  // red
	color4(1.0, 1.0, 0.0, 1.0),  // yellow
	color4(0.0, 1.0, 0.0, 1.0),  // green
	color4(0.0, 0.0, 1.0, 1.0),  // blue
	color4(1.0, 0.0, 1.0, 1.0),  // magenta
	color4(1.0, 1.0, 1.0, 1.0),  // white
	color4(0.0, 1.0, 1.0, 1.0)   // cyan
};

const point4 vertices[8] = {
	point4(-0.5, -0.5, 0.5, 1.0),
	point4(-0.5, 0.5, 0.5, 1.0),
	point4(0.5, 0.5, 0.5, 1.0),
	point4(0.5, -0.5, 0.5, 1.0),
	point4(-0.5, -0.5, -0.5, 1.0),
	point4(-0.5, 0.5, -0.5, 1.0),
	point4(0.5, 0.5, -0.5, 1.0),
	point4(0.5, -0.5, -0.5, 1.0)
};

enum {
	Torso,
	Neck,
	Head,
	RightUpperArm,
	RightLowerArm,
	LeftUpperArm,
	LeftLowerArm,
	RightUpperLeg,
	RightLowerLeg,
	LeftUpperLeg,
	LeftLowerLeg,
	LeftEar,
	RightEar,
	Quit
};

#endif
