#pragma once
#ifndef _Code_KeyMouse_H_
#define  _Code_KeyMouse_H_
namespace KeyMouse {

	void specialKey(int key, int x, int y);

	void keyboard(unsigned char key, int x, int y);

	void mouse(int button, int state, int x, int y);

	void menu(int option);
}
#endif
