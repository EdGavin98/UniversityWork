#include "ChunkController.h"

ChunkController::ChunkController() : noiseGen(15212, 80.0f, 6, 0.3), biomeGen(13464, 20.0f, 9, 0.3)
{
}

bool ChunkController::checkChunkExists(int x, int z)
{
	return chunks.find(glm::vec2(x, z)) != chunks.end();
}

BlockType ChunkController::getBlock(int x, int y, int z)
{
	int chunkX = x / 16;
	int chunkZ = z / 16;

	if (checkChunkExists(chunkX, chunkZ)) 
	{
		int blockX = x % 16;
		int blockZ = z % 16;
		auto chunk = chunks.at(glm::ivec2(chunkX, chunkZ));

		if (blockX < 0 || blockZ < 0)
			return BlockType::AIR;

		return chunk->getBlock(blockX, y, blockZ);
	}
	else 
	{
		return BlockType::AIR;
	}

	return BlockType::AIR;
}

void ChunkController::updateNeighbours(int x, int z)
{
	glm::ivec2 left = glm::ivec2(x + 1, z);
	glm::ivec2 right = glm::ivec2(x - 1, z);
	glm::ivec2 front = glm::ivec2(x, z + 1);
	glm::ivec2 back = glm::ivec2(x, z - 1);
	if (checkChunkExists(x + 1, z)) 
		chunks.at(left)->genMesh();

	if (checkChunkExists(x - 1, z) && x >= 0)
		chunks.at(right)->genMesh();

	if (checkChunkExists(x, z + 1))
		chunks.at(front)->genMesh();

	if (checkChunkExists(x, z - 1) && z >= 0)
		chunks.at(back)->genMesh();
}

int ChunkController::getBiomeValue(int x, int z)
{
	return this->biomeGen.getValue(x, z);
}

int ChunkController::getHeightValue(int x, int z)
{
	return this->noiseGen.getValue(x, z);
}

void ChunkController::drawChunks(glm::vec3 pos)
{
	int minRenderX = (pos.x / 16) - 8;
	int maxRenderX = (pos.x / 16) + 8;
	int minRenderZ = (pos.z / 16) - 8;
	int maxRenderZ = (pos.z / 16) + 8;

	for (std::pair<glm::ivec2, Chunk*> chunk : chunks)
	{
		glm::ivec2 chunkPos = chunk.first;
		Chunk* currentChunk = chunk.second;
		

		if (minRenderX > chunkPos.x || minRenderZ > chunkPos.y || maxRenderX < chunkPos.x || maxRenderZ < chunkPos.y)
		{
			chunks.erase(chunkPos);
			delete currentChunk;
		}
		else
		{
			currentChunk->draw();
		}
	}
}

void ChunkController::addChunk(int x, int z)
{
	Chunk* chunk = new Chunk(x, z, this);
	chunks.insert({ glm::ivec2(x,z), chunk});
}




