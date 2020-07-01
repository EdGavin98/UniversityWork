#pragma once

#include <string>
#include <gl\glew.h>
#include <glm\glm.hpp>
#include <vector>
#include "Shader.h"

struct Vertex 
{
	glm::vec3 Position;
	glm::vec3 Normal;
	glm::vec2 TexCoords;
};

struct Texture
{
	unsigned int id;
	std::string type;
	std::string path;
};

struct Material
{
	glm::vec3 ambient = glm::vec3(1.0f);
	glm::vec3 diffuse = glm::vec3(0.4f);
	glm::vec3 specular;
	glm::vec3 emission;
	float transparency = 1.0f;
	float shininess;
	float density;
	unsigned short illuminationModel;
	std::vector<Texture> textures;
};

//Class to store information about each mesh of a model and the materials associated with it.
class Mesh
{
public:
	Mesh();
	Mesh(std::vector<Vertex> vertices, 
		std::vector<std::vector<unsigned int>> indices, 
		std::vector<Material> materials);

	//Bind the mesh and draw it
	void Draw(Shader shader);
private:
	std::vector<Vertex> vertices;
	std::vector<std::vector<unsigned int>> indices;
	std::vector<Material> materials;
	
	GLuint VAO, VBO, EBO;

	void initMesh();
};



