#include "Mesh.h"
#include <iostream>

Mesh::Mesh()
{

}

Mesh::Mesh(std::vector<Vertex> vertices, std::vector<std::vector<unsigned int>> indices, std::vector<Material> materials)
{
	this->vertices = vertices;
	this->indices = indices;
	this->materials = materials;
	initMesh();
}

void Mesh::Draw(Shader shader)
{
	for (int i = 0; i < indices.size(); i++) 
	{
		glBindVertexArray(VAO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices[i].size() * sizeof(unsigned int), &indices.at(i)[0], GL_STATIC_DRAW);
		Material mat;
		if (materials.size() > 0)
		{
			mat = materials[i];
		}
		glm::vec3 lightPos(600.0f, 600.0f, 0.0f);
		//std::cout << indices[i].size() << std::endl;
		shader.use();
		shader.setVector3("lighting.lightPos", lightPos);
		shader.setVector3("lighting.ambient", mat.ambient);
		shader.setVector3("lighting.diffuse", mat.diffuse);
		shader.setVector3("lighting.specular", mat.specular);
		shader.setFloat("lighting.shininess", mat.shininess);
		shader.setFloat("lighting.transparency", mat.transparency);
		shader.setBool("hasTex", !mat.textures.empty());

		for (int i = 0; i < mat.textures.size(); i++)
		{
			Texture tex = mat.textures[i];
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, tex.id);
			shader.setInt(tex.type, i);
		}

		glDrawElements(GL_TRIANGLES, indices[i].size(), GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}
}

void Mesh::initMesh()
{
	glGenVertexArrays(1, &VAO);
	glGenBuffers(1, &VBO);
	glGenBuffers(1, &EBO);

	glBindVertexArray(VAO);
	glBindBuffer(GL_ARRAY_BUFFER, VBO);
	try 
	{
		glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(Vertex), &vertices.at(0), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)offsetof(Vertex, Normal));
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)offsetof(Vertex, TexCoords));
		glBindVertexArray(0);
	}
	catch (std::out_of_range e)
	{
		std::cout << "Something went wrong" << std::endl;
	}
	
}

