#include "Head_Dog.h"
#include "Code_Dog.h"
#include "MatrixStack.h"

extern MatrixStack mvstack;
extern GLuint program;

void Dog::left_upper_arm() {
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * UPPER_ARM_HEIGHT, 0) *
		Scale(UPPER_ARM_WIDTH, UPPER_ARM_HEIGHT, UPPER_ARM_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();
}

void Dog::left_lower_arm()
{
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * LOWER_ARM_HEIGHT, 0) *
		Scale(LOWER_ARM_WIDTH, LOWER_ARM_HEIGHT, LOWER_ARM_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

void Dog::right_upper_arm()
{
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * UPPER_ARM_HEIGHT, 0) *
		Scale(UPPER_ARM_WIDTH, UPPER_ARM_HEIGHT, UPPER_ARM_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

void Dog::right_lower_arm()
{
	mvstack.push(model_view);

	mat4 instance = (Translate(0, 0.5 * LOWER_ARM_HEIGHT, 0) *
		Scale(LOWER_ARM_WIDTH, LOWER_ARM_HEIGHT, LOWER_ARM_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();
}

void Dog::left_upper_leg()
{
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * UPPER_LEG_HEIGHT, 0) *
		Scale(UPPER_LEG_WIDTH, UPPER_LEG_HEIGHT, UPPER_LEG_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

void Dog::left_lower_leg()
{
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * LOWER_LEG_HEIGHT, 0) *
		Scale(LOWER_LEG_WIDTH, LOWER_LEG_HEIGHT, LOWER_LEG_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

void Dog::right_upper_leg()
{
	mvstack.push(model_view);

	mat4 instance = (Translate(0, 0.5 * UPPER_LEG_HEIGHT, 0) *
		Scale(UPPER_LEG_WIDTH, UPPER_LEG_HEIGHT, UPPER_LEG_WIDTH));
	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

void Dog::right_lower_leg()
{
	mvstack.push(model_view);

	mat4 instance = (Translate(0, 0.5 * LOWER_LEG_HEIGHT, 0) *
		Scale(LOWER_LEG_WIDTH, LOWER_LEG_HEIGHT, LOWER_LEG_WIDTH));


	model_view = model_view * instance;
	//绘制光照和阴影
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();
}

void Dog::head()
{
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0, 0.5 * HEAD_LONG - 0.5 * NECK_WIDTH) *
		Scale(HEAD_WIDTH, HEAD_HEIGHT, HEAD_LONG));

	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();
}

void Dog::torso()
{
	mvstack.push(model_view);//保存父节点矩阵

	mat4 instance = (Translate(0.0, 0.5 * TORSO_HEIGHT + Body_Offset, 0.0) *
		Scale(TORSO_WIDTH, TORSO_HEIGHT, TORSO_LONG));//本节点局部变换矩阵

	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);//父节点矩阵*本节点局部变换矩阵
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();//恢复父节点矩阵
}

void Dog::neck() {
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * NECK_HEIGHT, 0) *
		Scale(NECK_WIDTH, NECK_HEIGHT, NECK_WIDTH));

	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();
}

void Dog::tail() {
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5*TAIL_HEIGHT, 0) *
		Scale(TAIL_WIDTH, TAIL_HEIGHT, TAIL_WIDTH));
	model_view = model_view * instance;
	drawLight;

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);

	model_view = mvstack.pop();
}

void Dog:: left_ear() {
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * EAR_HEIGHT, 0) *
		Scale(EAR_WIDTH, EAR_HEIGHT, EAR_LONG));

	model_view = model_view * instance;
	drawLight();

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

void Dog::right_ear() {
	mvstack.push(model_view);
	mat4 instance = (Translate(0, 0.5 * EAR_HEIGHT, 0) *
		Scale(EAR_WIDTH, EAR_HEIGHT, EAR_LONG));

	model_view = model_view * instance;
	drawLight();
	glUniformMatrix4fv(ModelView, 1, GL_TRUE, model_view);
	glUniform4fv(draw_color, 1, colorDog);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	model_view = mvstack.pop();
}

extern point4 points[NumVertices];
extern color4 colors[NumVertices];
extern point4 ns[NumVertices];
void Dog:: initDog() {

	// Create a vertex array object
	GLuint vao;
	glGenVertexArrays(1, &vao);
	glBindVertexArray(vao);

	// Create and initialize a buffer object
	GLuint buffer;
	glGenBuffers(1, &buffer);
	glBindBuffer(GL_ARRAY_BUFFER, buffer);
	glBufferData(GL_ARRAY_BUFFER, sizeof(points) + sizeof(colors) + sizeof(ns),
		NULL, GL_DYNAMIC_DRAW);
	glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(points), points);
	glBufferSubData(GL_ARRAY_BUFFER, sizeof(points), sizeof(colors),
		colors);
	glBufferSubData(GL_ARRAY_BUFFER, sizeof(points) + sizeof(colors), sizeof(ns), ns);

	// Load shaders and use the resulting shader program
	program = InitShader("vshader82.glsl", "fshader82.glsl");
	glUseProgram(program);

	GLuint vPosition = glGetAttribLocation(program, "vPosition");
	glEnableVertexAttribArray(vPosition);
	glVertexAttribPointer(vPosition, 4, GL_FLOAT, GL_FALSE, 0,
		BUFFER_OFFSET(0));

	GLuint vColor = glGetAttribLocation(program, "vColor");
	glEnableVertexAttribArray(vColor);
	glVertexAttribPointer(vColor, 4, GL_FLOAT, GL_FALSE, 0,
		BUFFER_OFFSET(sizeof(points)));

	GLuint vNormal = glGetAttribLocation(program, "vNormal");
	glEnableVertexAttribArray(vNormal);
	glVertexAttribPointer(vNormal, 4, GL_FLOAT, GL_FALSE, 0,
		BUFFER_OFFSET(sizeof(points) + sizeof(vColor)));

	ModelView = glGetUniformLocation(program, "ModelView");
	Projection = glGetUniformLocation(program, "Projection");
	draw_color = glGetUniformLocation(program, "draw_color");

	//modelMatrix = glGetUniformLocation(program, "modelMatrix");
	viewMatrix = glGetUniformLocation(program, "viewMatrix");

}

extern GLfloat theta[NumJointAngles];
extern double ZOOM;
extern mat4 projection;
void Dog::displayDog() {

	initDog();
	//不是纹理
	GLuint is_texture = glGetUniformLocation(program, "is_texture");
	glUniform1i(is_texture, 0);

	mat4 proMatrix = projection;
	glUniformMatrix4fv(Projection, 1, GL_TRUE, proMatrix);


	model_view = Translate(dogAxisX, 0, dogAxisZ) *
		RotateY(dogAngleY) * RotateX(theta[Torso]) * Scale(ZOOM, ZOOM, ZOOM);//躯干变换矩阵
	Dog::torso();//躯干绘制

	//绘制狗身体以上的部分
	mvstack.push(model_view);
	model_view *= (Translate(0, TORSO_HEIGHT + Body_Offset, 0.5 * TORSO_LONG) *
		RotateX(theta[Neck]));
	Dog::neck();
	model_view *= (Translate(0, NECK_HEIGHT + 0.5 * HEAD_HEIGHT, 0) *
		RotateX(theta[Head]));
	Dog::head();

	mvstack.push(model_view);
	model_view *= (Translate(-0.5 * HEAD_WIDTH, 0.5 * HEAD_HEIGHT, 0) *
		RotateZ(theta[RightEar]));
	Dog::right_ear();
	model_view = mvstack.pop();

	mvstack.push(model_view);
	model_view *= (Translate(0.5 * HEAD_WIDTH, 0.5 * HEAD_HEIGHT, 0) *
		RotateZ(theta[LeftEar]));
	Dog::left_ear();
	model_view = mvstack.pop();

	model_view = mvstack.pop();

	//绘制狗的尾巴
	mvstack.push(model_view);
	model_view *= (Translate(0, TORSO_HEIGHT+ Body_Offset, -0.5 * TORSO_LONG) *
		RotateX(-120));
	Dog::tail();
	model_view = mvstack.pop();


	//绘制狗的两只前脚
	mvstack.push(model_view);
	model_view *= (Translate(0.5 * TORSO_WIDTH, Body_Offset, 0.5 * TORSO_LONG) *
		RotateX(theta[LeftUpperArm]));
	Dog::left_upper_arm();
	model_view *= (Translate(0, UPPER_ARM_HEIGHT, 0) *
		RotateX(theta[LeftLowerArm]));
	Dog::left_lower_arm();
	model_view = mvstack.pop();

	mvstack.push(model_view);
	model_view *= (Translate(-0.5 * TORSO_WIDTH, Body_Offset, 0.5 * TORSO_LONG) *
		RotateX(theta[RightUpperArm]));
	Dog::right_upper_arm();
	model_view *= (Translate(0, UPPER_ARM_HEIGHT, 0) *
		RotateX(theta[RightLowerArm]));
	Dog::right_lower_arm();
	model_view = mvstack.pop();

	//绘制狗的两只后脚
	mvstack.push(model_view);
	model_view *= (Translate(0.5 * TORSO_WIDTH, Body_Offset, -0.5 * TORSO_LONG) *
		RotateX(theta[LeftUpperLeg]));
	Dog::left_upper_leg();
	model_view *= (Translate(0, UPPER_LEG_HEIGHT, 0) *
		RotateX(theta[LeftLowerLeg]));
	Dog::left_lower_leg();
	model_view = mvstack.pop();

	mvstack.push(model_view);
	model_view *= (Translate(-0.5 * TORSO_WIDTH, Body_Offset, -0.5 * TORSO_LONG) *
		RotateX(theta[RightUpperLeg]));
	Dog::right_upper_leg();
	model_view *= (Translate(0, UPPER_LEG_HEIGHT, 0) *
		RotateX(theta[RightLowerLeg]));
	Dog::right_lower_leg();

	model_view = mvstack.pop();
}

extern mat4 view;
void Dog::drawLight() {

	//计算投影矩阵
	float lx = lightPos[0];
	float ly = lightPos[1];
	float lz = lightPos[2];
	mat4 shadowMatrix = mat4(-ly, 0, 0, 0,
		lx, 0, lz, 1,
		0, 0, -ly, 0,
		0, 0, 0, -ly);

	// 请阅读顶点着色器的内容，对照实验1的异同
	mat4 shadowPMatrix = shadowMatrix;

	GLuint ModelView = glGetUniformLocation(program, "ModelView");
	GLuint Projection = glGetUniformLocation(program, "Projection");
	GLuint draw_color = glGetUniformLocation(program, "draw_color");

	glUniformMatrix4fv(ModelView, 1, GL_TRUE, shadowPMatrix * model_view);
	glUniformMatrix4fv(viewMatrix, 1, GL_TRUE, view);

	mat4 proMatrix = projection;
	glUniformMatrix4fv(Projection, 1, GL_TRUE, proMatrix);
	// 将光源位置传入顶点着色器

	GLuint lightPosID = glGetUniformLocation(program, "lightPos");
	glUniform3fv(lightPosID, 1, lightPos);
	//将距离传入顶点着色器

	GLuint distToLightID = glGetUniformLocation(program, "distToLight");
	glUniform1f(distToLightID, distTL);

	glUniform4fv(draw_color, 1, BLACK);
	glDrawArrays(GL_TRIANGLES, 0, NumVertices);
}

