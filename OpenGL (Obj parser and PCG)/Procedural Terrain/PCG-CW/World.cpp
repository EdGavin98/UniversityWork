#include "World.h"

World::World(Camera* camera)
{
	this->chunkController = new ChunkController();
	this->player = camera;
}

void World::render()
{
	chunkController->drawChunks(player->getPosition());
}

void World::update()
{
	int xLoc = player->getPosition().x / 16; //Translate block coord to chunk coord
	int zLoc = player->getPosition().z / 16;
	for (int i = 0; i < 8; i++)
	{
		//int xStart = std::max(xLoc - i, 0); 
		//int zStart = std::max(zLoc - i, 0);
		int xStart = xLoc - i;				//Allows chunk rendering of chunks with coordinates below 0;
		int zStart = zLoc - i;				//Works fine but some interior faces still load due to errors converting chunk and block coords below 0;
		int xDist = xLoc + i;
		int zDist = zLoc + i;

		for (int x = xStart; x <= xDist; x++)
			for (int z = zStart; z <= zDist; z++)
				if (!chunkController->checkChunkExists(x, z))
				{
					chunkController->addChunk(x, z);
					chunkController->updateNeighbours(x, z);	//Notify the neighbouring chunks that there is a new chunk there.
				}	
	}
}