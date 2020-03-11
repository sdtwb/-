#include "Number.h"
#include "Angel.h"
#include "Const_Head.h"
#include "mat.h"
#include<vector>
using namespace std;

Number::Number() {

}

//初始化数字图案与着色器的绑定
extern point4 points[NumVertices];
extern color4 colors[NumVertices];
extern point4 ns[NumVertices];
void Number::init_shader() {
	// Create a vertex array object
	glGenVertexArrays(1, &this->vao_numbers);
	glBindVertexArray(this->vao_numbers);

	// Create and initialize a buffer object

	glGenBuffers(1, &this->buffer_numbers);
	glBindBuffer(GL_ARRAY_BUFFER, this->buffer_numbers);
	glBufferData(GL_ARRAY_BUFFER, sizeof(points) + sizeof(colors) + sizeof(ns),
		NULL, GL_DYNAMIC_DRAW);
	glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(points), points);
	glBufferSubData(GL_ARRAY_BUFFER, sizeof(points), sizeof(colors),
		colors);
	glBufferSubData(GL_ARRAY_BUFFER, sizeof(points) + sizeof(colors), sizeof(ns), ns);

	// Load shaders and use the resulting shader program
	this->program_numbers = InitShader("vshader82.glsl", "fshader82.glsl");
	glUseProgram(this->program_numbers);

	GLuint vPosition = glGetAttribLocation(this->program_numbers, "vPosition");
	glEnableVertexAttribArray(vPosition);
	glVertexAttribPointer(vPosition, 4, GL_FLOAT, GL_FALSE, 0,
		BUFFER_OFFSET(0));

	GLuint vColor = glGetAttribLocation(this->program_numbers, "vColor");
	glEnableVertexAttribArray(vColor);
	glVertexAttribPointer(vColor, 4, GL_FLOAT, GL_FALSE, 0,
		BUFFER_OFFSET(sizeof(points)));

	GLuint vNormal = glGetAttribLocation(this->program_numbers, "vNormal");
	glEnableVertexAttribArray(vNormal);
	glVertexAttribPointer(vNormal, 4, GL_FLOAT, GL_FALSE, 0,
		BUFFER_OFFSET(sizeof(points) + sizeof(vColor)));

	this->viewMatrixID = glGetUniformLocation(this->program_numbers, "viewMatrix");
	this->modelMatrixID = glGetUniformLocation(this->program_numbers, "ModelView");
	this->proMatrixID = glGetUniformLocation(this->program_numbers, "Projection");
	this->drawColorID = glGetUniformLocation(this->program_numbers, "draw_color");

}

//初始化数字坐标
void Number::initNumber() {

	Number::init_shader();

	for (int i = 0; i < 10; i++) {
		std::vector<int> v;
		numbers.push_back(v);
	}

	numbers[0].push_back(1); numbers[0].push_back(1);
	numbers[0].push_back(1); numbers[0].push_back(2);
	numbers[0].push_back(1); numbers[0].push_back(3);
	
	numbers[1].push_back(0); numbers[1].push_back(1);
	numbers[1].push_back(0); numbers[1].push_back(2);
	numbers[1].push_back(0 ); numbers[1].push_back( 4);
	numbers[1].push_back(2 ); numbers[1].push_back( 1);
	numbers[1].push_back(2 ); numbers[1].push_back( 2);
	numbers[1].push_back(2 ); numbers[1].push_back( 3);
	numbers[1].push_back(2 ); numbers[1].push_back( 4);

	numbers[2].push_back(0 ); numbers[2].push_back( 3);
	numbers[2].push_back(1 ); numbers[2].push_back( 3);
	numbers[2].push_back(1 ); numbers[2].push_back( 1);
	numbers[2].push_back(2 ); numbers[2].push_back( 1);

	numbers[3].push_back(0 ); numbers[3].push_back( 3);
	numbers[3].push_back(1 ); numbers[3].push_back( 3);
	numbers[3].push_back(0 ); numbers[3].push_back( 1);
	numbers[3].push_back(1 ); numbers[3].push_back( 1);

	numbers[4].push_back(0 ); numbers[4].push_back( 0);
	numbers[4].push_back(1 ); numbers[4].push_back( 0);
	numbers[4].push_back(0 ); numbers[4].push_back( 1);
	numbers[4].push_back(1 ); numbers[4].push_back( 1);
	numbers[4].push_back(1 ); numbers[4].push_back( 3);
	numbers[4].push_back(1 ); numbers[4].push_back( 4);

	numbers[5].push_back(0 ); numbers[5].push_back( 1);
	numbers[5].push_back(1 ); numbers[5].push_back( 1);
	numbers[5].push_back(1 ); numbers[5].push_back( 3);
	numbers[5].push_back(2 ); numbers[5].push_back( 3);

	numbers[6].push_back(1 ); numbers[6].push_back( 3);
	numbers[6].push_back(2 ); numbers[6].push_back( 3);
	numbers[6].push_back(1 ); numbers[6].push_back( 1);

	numbers[7].push_back(0 ); numbers[7].push_back( 0);
	numbers[7].push_back(0 ); numbers[7].push_back( 1);
	numbers[7].push_back(0 ); numbers[7].push_back( 2);
	numbers[7].push_back(0 ); numbers[7].push_back( 3);
	numbers[7].push_back(1 ); numbers[7].push_back( 0);
	numbers[7].push_back(1 ); numbers[7].push_back( 1);
	numbers[7].push_back(1 ); numbers[7].push_back( 2);
	numbers[7].push_back(1 ); numbers[7].push_back( 3);

	numbers[8].push_back(1 ); numbers[8].push_back( 1);
	numbers[8].push_back(1 ); numbers[8].push_back( 3);

	numbers[9].push_back(1 ); numbers[9].push_back( 3);
	numbers[9].push_back(0 ); numbers[9].push_back( 1);
	numbers[9].push_back(1 ); numbers[9].push_back( 1);
}

//显示数字
void Number::displayNumber(int numb) {

	glUseProgram(this->program_numbers);
	glBindVertexArray(this->vao_numbers);

	int row = 5;
	int line = 3;

	int** flag = new int*[row];
	for (int i = 0; i < row; i++)
		flag[i] = new int[line];

	for (int i = 0; i < row; i++)
		for (int j = 0; j < line; j++)
			flag[i][j] = 1;

	int size = numbers[numb].size();
	for (int i = 0; i < size; i += 2) {
		int x = numbers[numb][i];
		int y = numbers[numb][i + 1];
		
		//坐标与数组下标刚好反过来
		flag[y][x] = 0;
	}

	int Y = 0;
	for (int i = 0; i < row; i++) {
		int X = 0;
		for (int j = 0; j < line; j++) {
			if (flag[i][j]) {
				mat4 instance = Translate(X, Y, -20) * Scale(2, 2, 2);

				GLuint is_texture = glGetUniformLocation(this->program_numbers, "is_texture");
				glUniform1i(is_texture, 0);

				glUniformMatrix4fv(this->viewMatrixID, 1, GL_TRUE, this->viewMatrix);
				glUniformMatrix4fv(this->proMatrixID, 1, GL_TRUE, this->proMatrix);
				glUniformMatrix4fv(this->modelMatrixID, 1, GL_TRUE, this->modelMatrix * instance);
				glUniform4fv(this->drawColorID, 1, White);
				glDrawArrays(GL_TRIANGLES, 0, NumVertices);
			}
			X+= this->distance_cube;
		}
		Y += this->distance_cube;
	}

	for (int i = 0; i < row; i++)
		delete[] flag[i];
	delete[] flag;

}