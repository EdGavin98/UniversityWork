#pragma once
#include <glm\glm.hpp>
#include "Mesh.h"
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <stb_image.h>
#include <glm/glm.hpp>
#include <glm\gtc\matrix_transform.hpp>




/* Model Class
*  Handles reading the files and storing data of vertices, textures, material etc.
*/
class Model
{

public:
	Model(std::vector<Mesh> meshes);

	//Call the draw method for each mesh in the model
	void Draw(Shader shader);

	//Move the position of the model
	void translate(glm::vec3 amount);
	glm::mat4 getModelMatrix();

private:
	std::vector<Mesh> meshes;
	glm::mat4 modelMatrix;

};



