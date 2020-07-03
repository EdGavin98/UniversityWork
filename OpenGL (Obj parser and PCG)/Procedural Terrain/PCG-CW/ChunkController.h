#pragma once
#include <GL\glew.h>
#include <glm\glm.hpp>
#define GLM_ENABLE_EXPERIMENTAL
#include <glm\gtx\hash.hpp>

#include "Chunk.h"
#include "Shader.h"
#include "NoiseGenerator.h"

#include <unordered_map>

class Chunk;

class ChunkController
{
public:
	ChunkController();
	void drawChunks(glm::vec3 pos);
	void addChunk(int x, int z);
	bool checkChunkExists(int x, int z);
	BlockType getBlock(int x, int y, int z);
	void updateNeighbours(int x, int z);
	int getBiomeValue(int x, int z);
	int getHeightValue(int x, int z);

private:
	std::unordered_map<glm::ivec2, Chunk*> chunks;
	NoiseGenerator noiseGen;
	NoiseGenerator biomeGen;
};

