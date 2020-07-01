#include "Model.h"

Model::Model(std::vector<Mesh> meshes)
{
	this->meshes = meshes;
	this->modelMatrix = glm::mat4(1.0f);
}

void Model::Draw(Shader shader)
{
	for (int i = 0; i < meshes.size(); i++) {
		meshes[i].Draw(shader);
	}
}

void Model::translate(glm::vec3 amount)
{

	modelMatrix = glm::translate(modelMatrix, amount);
	std::cout << "Translated: " << amount.x << std::endl;
	
}

glm::mat4 Model::getModelMatrix()
{
	return this->modelMatrix;
}

