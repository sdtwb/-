#include <vector>
#include <math.h>
#include "Code_Texture.h"
#include "Mesh_Painter.h"
#include "mesh.h"
#include "Const_Head.h"


//初始化纹理
extern Mesh_Painter* mp_;
void Texture::initTexture() {

	My_Mesh* background = new My_Mesh;
	background->generate_rectangle();
	background->set_texture_file("texture/grassdown.jpg");
	mp_->add_mesh(background);

	background = new My_Mesh;
	background->generate_rectangle();
	background->set_texture_file("texture/sand_floor.jpg");
	mp_->add_mesh(background);

	background = new My_Mesh;
	background->generate_rectangle();
	background->set_texture_file("texture/grass.jpg");
	mp_->add_mesh(background);

	/*My_Mesh* box_top = new My_Mesh;
	box_top->generate_rectangle();
	box_top->set_texture_file("texture/box_top.jpg");
	mp_->add_mesh(box_top);

	My_Mesh* box_left = new My_Mesh;
	box_left->generate_rectangle();
	box_left->set_texture_file("texture/box_left.jpg");
	mp_->add_mesh(box_left);

	My_Mesh* box_font = new My_Mesh;
	box_font->generate_rectangle();
	box_font->set_texture_file("texture/box_font.jpg");
	mp_->add_mesh(box_font);

	My_Mesh* box_right = new My_Mesh;
	box_right->generate_rectangle();
	box_right->set_texture_file("texture/box_right.jpg");
	mp_->add_mesh(box_right);

	My_Mesh* box_back = new My_Mesh;
	box_back->generate_rectangle();
	box_back->set_texture_file("texture/box_back.jpg");
	mp_->add_mesh(box_back);*/

	mp_->init_shaders("vshader82.glsl", "fshader82.glsl");
	mp_->update_vertex_buffer();
	mp_->update_texture();

}


//显示纹理
extern mat4 view;
extern mat4 projection;
extern std::vector<int> freePosition;
extern int game_start;
extern int dogAxisX;extern int dogAxisZ;
void Texture::displayTexture() {

	//grass_down
	int i = 0;
	mp_->m_my_meshes_[i]->viewMatrix = view;
	mp_->m_my_meshes_[i]->modelMatrix = Translate(0, 0, 60) * Scale(50, 50, 50);
	mp_->m_my_meshes_[i]->proMatrix = projection;

	//sand_floor
	i++;
	mp_->m_my_meshes_[i]->viewMatrix = view;
	mp_->m_my_meshes_[i]->modelMatrix = Translate(0, -0.001, 0) * Scale(50, 50, 50);
	mp_->m_my_meshes_[i]->proMatrix = projection;

	//grass_top
	i++;
	mp_->m_my_meshes_[i]->viewMatrix = view;
	mp_->m_my_meshes_[i]->modelMatrix = Translate(0, 0, -70) * Scale(50, 50, 50);
	mp_->m_my_meshes_[i]->proMatrix = projection;

	////box_top
	//i++;
	//mp_->m_my_meshes_[i]->viewMatrix = view;
	//mp_->m_my_meshes_[i]->modelMatrix = Translate(0, 50, 0) * Scale(50, 50, 50);
	//mp_->m_my_meshes_[i]->proMatrix = projection;

	////box_left
	//i++;
	//mp_->m_my_meshes_[i]->viewMatrix = view;
	//mp_->m_my_meshes_[i]->modelMatrix = Translate(-50, 50, 0) * RotateY(-90)* RotateX(90)* Scale(50, 50, 50);
	//mp_->m_my_meshes_[i]->proMatrix = projection;

	////box_font
	//i++;
	//mp_->m_my_meshes_[i]->viewMatrix = view;
	//mp_->m_my_meshes_[i]->modelMatrix = Translate(0, 50, -50) * RotateX(90) * Scale(50, 50, 50);
	//mp_->m_my_meshes_[i]->proMatrix = projection;

	////box_right
	//i++;
	//mp_->m_my_meshes_[i]->viewMatrix = view;
	//mp_->m_my_meshes_[i]->modelMatrix = Translate(50, 50, 0) * RotateY(90)*RotateX(90) * Scale(50, 50, 50);
	//mp_->m_my_meshes_[i]->proMatrix = projection;

	////box_back
	//i++;
	//mp_->m_my_meshes_[i]->viewMatrix = view;
	//mp_->m_my_meshes_[i]->modelMatrix = Translate(0, 50, 50) * RotateY(180) * RotateX(90) * Scale(50, 50, 50);
	//mp_->m_my_meshes_[i]->proMatrix = projection;

	int size = mp_->m_my_meshes_.size();
	for (i++; i < size; i++) {
		double x = mp_->m_my_meshes_[i]->xAxis;
		if(game_start)
			x = mp_->m_my_meshes_[i]->xAxis++;

		double z = mp_->m_my_meshes_[i]->zAxis;
		mat4 moMatrix = mp_->m_my_meshes_[i]->moMatrix;

		mp_->m_my_meshes_[i]->viewMatrix = view;
		mp_->m_my_meshes_[i]->modelMatrix = Translate(x, 1, z) * moMatrix;
		mp_->m_my_meshes_[i]->proMatrix = projection;

		if (x > 40) {
			freePosition.push_back(i);
		}

		//狗撞到了障碍物，游戏结束
		if (abs(dogAxisX - x) < limitDit&& dogAxisZ== z)
			game_start = 0;

	}

	mp_->draw_meshes();
}