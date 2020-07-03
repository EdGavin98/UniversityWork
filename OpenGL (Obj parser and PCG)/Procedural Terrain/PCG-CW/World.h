#pragma once
#include "ChunkController.h"
#include "Camera.h"
class World
{

public: 
	World(Camera* camera);
	void render();
	void update();
private:
	ChunkController* chunkController;
	Camera* player;
	bool isFirstLoad;
};

