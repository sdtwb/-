#include "Code_Functions.h"
#include "Const_Head.h"

point4 points[NumVertices];
color4 colors[NumVertices];
point4 ns[NumVertices];


int Index = 0;
void Functions::quad(int a, int b, int c, int d)
{
	colors[Index] = vertex_colors[a]; points[Index] = vertices[a]; ns[Index] = (vertices[a] - vec4(0, 0, 0, 1)); Index++;
	colors[Index] = vertex_colors[a]; points[Index] = vertices[b]; ns[Index] = (vertices[b] - vec4(0, 0, 0, 1)); Index++;
	colors[Index] = vertex_colors[a]; points[Index] = vertices[c]; ns[Index] = (vertices[c] - vec4(0, 0, 0, 1)); Index++;
	colors[Index] = vertex_colors[a]; points[Index] = vertices[a]; ns[Index] = (vertices[a] - vec4(0, 0, 0, 1)); Index++;
	colors[Index] = vertex_colors[a]; points[Index] = vertices[c]; ns[Index] = (vertices[c] - vec4(0, 0, 0, 1)); Index++;
	colors[Index] = vertex_colors[a]; points[Index] = vertices[d]; ns[Index] = (vertices[d] - vec4(0, 0, 0, 1)); Index++;
}

void Functions::colorcube(void)
{
	Index = 0;
	quad(1, 0, 3, 2);
	quad(2, 3, 7, 6);
	quad(3, 0, 4, 7);
	quad(6, 5, 1, 2);
	quad(4, 5, 6, 7);
	quad(5, 4, 0, 1);
}

void Functions::printUsage() {

	std::cout << "Q、q:退出程序\n s:开始游戏或暂停游戏\nd:将狗放大\nD:将狗缩小\nr:增大视点的半径\nR:减小视点的半径\no:增大视点的thera值\nO:减小视点的thera值\np;增大视点的phi值\nP:减小视点的phi值\nx : 增大视景框的left和right值\nX : 减小视景框的left和right值\ny : 增大视景框的bottom和top值\nY : 减小视景框的bottom和top值\nz : 增大视景框的near和far值\nZ : 减小视景框的near和far值\nt : 重置radius、thera、phi和狗的缩放比" << std::endl;

	std::cout << "向上方向键:控制狗向上移动\n向下方向键:控制狗向下移动" << std::endl;

	std::cout << "鼠标中键：唤出菜单\n鼠标左键：增大角度\n鼠标右键：减小角度" << std::endl;
}