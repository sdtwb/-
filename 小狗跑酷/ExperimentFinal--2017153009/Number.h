#pragma once

#include "Angel.h"
#include <vector>

class Number {
private:
	std::vector<std::vector<int> > numbers;
	int distance_cube = 2;
public:

	GLuint program_numbers;
	GLuint vao_numbers;
	GLuint buffer_numbers;

	GLuint viewMatrixID;
	GLuint modelMatrixID;
	GLuint proMatrixID;
	GLuint drawColorID;

	mat4 viewMatrix;
	mat4 modelMatrix;
	mat4 proMatrix;

	Number();

	void init_shader();

	void initNumber();

	void displayNumber(int num);
};