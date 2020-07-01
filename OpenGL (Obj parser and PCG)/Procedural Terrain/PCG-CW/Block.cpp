#include "Block.h"


BlockData Block::getData(BlockType type)
{
	BlockData data;
	switch (type)
	{
	case BlockType::DIRT:
		data.bottomTex = DIRT;
		data.sideTex = DIRT;
		data.topTex = DIRT;
		break;
	case BlockType::STONE:
		data.bottomTex = STONE;
		data.sideTex = STONE;
		data.topTex = STONE;
		break;
	case BlockType::GRASS:
		data.bottomTex = DIRT;
		data.sideTex = GRASS_SIDE;
		data.topTex = GRASS_TOP;
		break;
	case BlockType::SAND: 
		data.bottomTex = SAND;
		data.sideTex = SAND;
		data.topTex = SAND;
		break;
	case BlockType::LOG:
		data.bottomTex = LOG_TOP;
		data.sideTex = LOG_SIDE;
		data.topTex = LOG_TOP; 
		break;
	case BlockType::LEAF:
		data.bottomTex = LEAF;
		data.sideTex = LEAF;
		data.topTex = LEAF;
		break;
	}

	return data;
}
