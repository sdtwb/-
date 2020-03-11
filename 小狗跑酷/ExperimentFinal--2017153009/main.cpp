#pragma once
#include <assert.h>
#include <cstring>
#include <vector>
#include <assert.h>
#include <time.h>
#include <stdlib.h>
#include <iostream>
#include <windows.h> 
#include <MMSystem.h>
#include <process.h>

#include "Angel.h"
#include "mesh.h"
#include "FreeImage.h"
#include "Mesh_Painter.h"
#include "Const_Head.h"
#include "MatrixStack.h"
#include "Code_KeyMouse.h"
#include "Code_Dog.h"
#include "Code_Texture.h"
#include "Code_Rules.h"
#include "Code_Functions.h"
#include "Number.h"

using namespace std;
#pragma comment(lib, "glew32.lib")
#pragma comment(lib, "FreeImage.lib")
#pragma comment(lib, "Winmm.lib")

//----------------------------------------------------------------------------
//全局变量
MatrixStack  mvstack;
Mesh_Painter* mp_ ;
Number* number_;
vector<int> freePosition;
mat4 view;
mat4 projection;

GLint angle = Torso;

GLfloat lef = -15.0, rig = 15.0;
GLfloat bottom = -15, top = 25;
GLfloat zNear = -60, zFar = 100;

GLfloat radius = 1;
GLfloat thera = M_PI / 6;
GLfloat phi = M_PI / 2;

GLuint program;

int game_start = 0;
int score= 0;
int digit_score = 4;
//----------------------------------------
//初始化视角
void initEye() {
	vec4 eye = vec4(radius * sin(thera) * cos(phi),
		radius * sin(thera) * sin(phi),
		radius * cos(thera),
		1.0);
	vec4 at = vec4(0, 0, 0, 1);
	vec4 up = vec4(0, 1, 0, 0);

	view = LookAt(eye, at, up);

	projection = Ortho(lef, rig, bottom, top, zNear, zFar);

	//projection = Perspective(atan(top/zNear), rig/top, zNear, zFar) * Scale(-1, 1, 1);
	
}

//初始化
void init(void)
{

	Functions::printUsage();

	mp_ = new Mesh_Painter;

	number_ = new Number;

	Functions::colorcube();

	Texture::initTexture();

	number_->initNumber();

	glEnable(GL_DEPTH_TEST);
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	glClearColor(0.7, 0.7, 0.7, 1.0);

}

//游戏的动作
void move(int id) {

	Rules::actions();
	glutPostRedisplay();
	glutTimerFunc(100, move, 1);
}

//显示分数
void displayScore() {
	int score_temp = score;
	int axisX = 10;
	for (int i = 0; i < digit_score; i++) {
		int num = score_temp % 10;
		score_temp /= 10;

		number_->viewMatrix = view;
		number_->proMatrix= projection;
		number_->modelMatrix = Translate(axisX, 5, 5);
		number_->displayNumber(num);
		axisX -= 10;
	}
}

//显示狗，纹理和分数的函数
void display(){

    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

	initEye();
	
	Texture::displayTexture();

	Dog::displayDog();

	displayScore();

    glutSwapBuffers();
}

void reshape( int width, int height )
{
	glViewport(0, 0, width, height);

	GLfloat aspect = GLfloat(width) / height;

	if (aspect > 1.0) {
		lef *= aspect;
		rig *= aspect;
	}
	else {
		bottom /= aspect;
		top /= aspect;
	}

}

//----------------------------------------------------------------------------

//////////////////////////////////////////////////////////////////////////////
//音乐线程的方法
unsigned __stdcall ThreadPlayMusic(LPVOID lpParameter)
{
	while (true) {
		mciSendString("play ./music/runBackgroundMusic.wav", NULL, 0, NULL);
	}

	return 0;
}

//创建菜单
void createMenu() {

	glutCreateMenu(KeyMouse::menu);
	glutAddMenuEntry("torso", Torso);
	glutAddMenuEntry("neck", Neck);
	glutAddMenuEntry("head", Head);
	glutAddMenuEntry("right_upper_arm", RightUpperArm);
	glutAddMenuEntry("right_lower_arm", RightLowerArm);
	glutAddMenuEntry("left_upper_arm", LeftUpperArm);
	glutAddMenuEntry("left_lower_arm", LeftLowerArm);
	glutAddMenuEntry("right_upper_leg", RightUpperLeg);
	glutAddMenuEntry("right_lower_leg", RightLowerLeg);
	glutAddMenuEntry("left_upper_leg", LeftUpperLeg);
	glutAddMenuEntry("left_lower_leg", LeftLowerLeg);
	glutAddMenuEntry("left_ear", LeftEar);
	glutAddMenuEntry("right_ear", RightEar);
	glutAddMenuEntry("quit", Quit);
	glutAttachMenu(GLUT_MIDDLE_BUTTON);

}

//主函数
int main( int argc, char **argv )
{
    glutInit( &argc, argv );
    glutInitDisplayMode( GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH );
    glutInitWindowSize( 500, 500 );
    glutInitContextVersion( 3, 2 );
    glutInitContextProfile( GLUT_CORE_PROFILE );
    glutCreateWindow( "2017153009_谭文彪_期末大作业" );

	glewExperimental = GL_TRUE;
    glewInit();

    init();
	createMenu();

    glutDisplayFunc( display );
    glutReshapeFunc( reshape );
    glutKeyboardFunc( KeyMouse::keyboard );
	glutSpecialFunc( KeyMouse::specialKey );
    glutMouseFunc( KeyMouse::mouse );
	glutTimerFunc(100, move, 1);
	glutTimerFunc(1000, Rules::addBarrier, 1);

	volatile bool g_bEndMusicThread = false;	//初始化子线程标志位
	HANDLE hThread = (HANDLE)_beginthreadex(NULL, 0, ThreadPlayMusic, NULL, 0, NULL);	//创建线程

    glutMainLoop();

	g_bEndMusicThread = true;	//通知子线程退出
	WaitForSingleObject(hThread, INFINITE);		//线程结束之后再释放资源

    return 0;
}

