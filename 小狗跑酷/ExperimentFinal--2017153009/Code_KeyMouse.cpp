#include "Code_KeyMouse.h"
#include "Angel.h"
#include "Const_Head.h"

GLfloat dr = 4 * DegreesToRadians;

extern int dogDirection;
extern int dogAngleY;
extern int dogAxisZ;
//狗是的转头方向监听
void KeyMouse::specialKey(int key, int x, int y) {
	if (key == GLUT_KEY_UP
		&& dogDirection == 0
		&& dogAxisZ == 0) {
		dogDirection = -1;
	}
	else if (key == GLUT_KEY_DOWN
		&& dogDirection == 0
		&& dogAxisZ == topTrackZ) {
		dogDirection = 1;
	}
}

extern GLfloat theta[NumJointAngles];
extern GLint angle;
extern double ZOOM;
extern GLfloat radius;extern GLfloat thera;extern GLfloat phi;
extern GLfloat zNear;extern GLfloat zFar;
extern GLfloat lef;extern GLfloat rig;
extern GLfloat bottom;extern GLfloat top;
extern int score;
extern int game_start;
//键盘监听
void KeyMouse::keyboard(unsigned char key, int x, int y)
{
	switch (key) {
	case 033: // Escape Key
	case 'q':
	case 'Q':
		exit(EXIT_SUCCESS);
		break;

	case 's':game_start = game_start == 0 ? 1 : 0; break;
	case 'd':
		if(ZOOM< 1)		//狗的缩放比
			ZOOM += 0.1; break;
	case 'D':
		if(ZOOM> 0.2)
			ZOOM -= 0.1; break;
	case 'r':
		if (radius < 2)
			radius *= 1.2;		//改变观察点的r值
		break;
	case 'R': 
		if(radius> 0.1)
			radius *= 0.5;
		break;
	case 'o': thera += dr;		//改变观察点的thera值
		break;
	case 'O':thera -= dr;
		break;
	case 'p': phi += dr;	//改变观察点的phi值
		break;
	case 'P': phi -= dr;
		break;

	case 'x':lef *= 1.1; rig *= 1.1;
		break;
	case 'X':lef *= 0.9; rig *= 0.9;
		break;
	case 'y': bottom *= 1.1; top *= 1.1;
		break;
	case 'Y':bottom *= 0.9; top *= 0.9;
		break;
	case 'z': zNear *= 1.1; zFar *= 1.1;
		break;
	case 'Z':zNear *= 0.9; zFar *= 0.9;
		break;
	case 't':				//重置r、thera和phi的值
		radius = 1;
		thera = M_PI / 6;
		phi = M_PI / 2;

		ZOOM = 0.6;
		//score = 0;
	}

		glutPostRedisplay();
}

//鼠标监听
void KeyMouse::mouse(int button, int state, int x, int y)
{
	if (button == GLUT_LEFT_BUTTON) {
		theta[angle] += 5.0;
		if (theta[angle] > 360.0) { theta[angle] -= 360.0; }
	}

	if (button == GLUT_RIGHT_BUTTON) {
		theta[angle] -= 5.0;
		if (theta[angle] < 0.0) { theta[angle] += 360.0; }
	}

	if (!game_start)
		glutPostRedisplay();
}

//退出程序
void KeyMouse::menu(int option)
{
	if (option == Quit) {
		exit(EXIT_SUCCESS);
	}

	angle = option;
}

