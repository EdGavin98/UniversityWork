#pragma once

#include <glm\glm.hpp>
#include <GL/glew.h>
#include <GLFW\glfw3.h>
#include "Mesh.h"
#include "Model.h"
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <stb_image.h>
#include <regex>

//Stores details about each face element
struct Face {
	std::vector<int> vertIdx;
	std::vector<int> texIdx;
	std::vector<int> normIdx;
};

class ModelLoader
{
public:
	ModelLoader();

	//Load a model from the specified file, returns that model;
	Model loadModel(std::string path);
private:

	GLFWwindow* window;
	std::ifstream modelFile;

	//Vertices and other data to add to the mesh currently being created
	std::vector<Vertex> currentVertices;
	std::vector<Material> currentMaterials;
	std::vector<std::vector<unsigned int>> currentIndices;
	
	//Data stored during parsing of the file
	std::vector<glm::vec3> vertices;
	std::vector<glm::vec2> textures;
	std::vector<glm::vec3> normals;
	std::map<std::string, Material> materials;
	std::vector<unsigned int> indices;

	//All the meshes that will make up the object
	std::vector<Mesh> meshes;

	//Methods for parsing an obj file
	void parseObj();
	void createFace(Face face);
	void parseMtl(std::string path);

	//Load a texture
	Texture loadTexture(std::string path);

	//Method for parsing a dae file
	void parseDae();


};

