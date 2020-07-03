#pragma once
#include <glm/glm.hpp>
#include <Vector>
#include <Memory>
#include <GL\glew.h>
#include "BlockData.h"
#include "TextureAtlas.h"
#include "Block.h"
#include "NoiseGenerator.h"
#include "ChunkController.h"

class ChunkController;

struct Vertex
{
	glm::vec3 Position;
	glm::vec3 Normal = glm::vec3(0.0f);
	glm::vec2 TexCoords;
};


struct AdjacentBlocks
{
	glm::ivec3 up;
	glm::ivec3 down;
	glm::ivec3 left;
	glm::ivec3 right;
	glm::ivec3 front;
	glm::ivec3 back;

	void updatePostions(int x, int y, int z)
	{
		up = glm::ivec3(x, y + 1, z);
		down = glm::ivec3(x, y - 1, z);
		left = glm::ivec3(x - 1, y, z);
		right = glm::ivec3(x + 1, y, z);
		front = glm::ivec3(x, y, z + 1);
		back = glm::ivec3(x, y, z - 1);
	}
};

//Wrapper for a 2D array so that it can be put inside an std::vector
//This allows for infinite height chunks that are still 16 x 16 on the X and Z axis
struct ChunkLayer
{
	BlockType blocks[16][16];
};

class Chunk
{

public:
	~Chunk();
	Chunk(GLint x, GLint z, ChunkController* controller);
	void genChunk();
	void genMesh();
	void genVao();
	void draw();
	BlockType getBlock(int x, int y, int z);
	glm::ivec2 position;


private:

	void addFace(std::array<glm::vec3, 5> blockData, FaceType tex,int x, int y, int z, glm::ivec3 neighbour);
	void shouldAddTree(int row, int col);
	void addTree(int row, int col, int height);
	void initLayer(int height);
	std::vector<Vertex> vertices;
	std::vector<GLuint> indices;

	std::vector<ChunkLayer> layers;

	int heightMap[16][16];
	int biomeMap[16][16];
	bool treeMap[16][16];
	int highestBlock;

	ChunkController* controller;

	GLuint VAO;
	GLuint VBO;
	GLuint EBO;
};

