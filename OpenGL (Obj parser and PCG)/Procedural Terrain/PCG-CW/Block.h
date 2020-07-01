#pragma once
#include <glm/glm.hpp>
#include <GL\glew.h>
#include "TextureAtlas.h"

enum class BlockType
{
	GRASS,
	STONE,
	DIRT,
	WATER,
	SAND,
	LOG,
	LEAF,
	AIR
};

struct BlockData
{
	FaceType topTex;
	FaceType sideTex;
	FaceType bottomTex;
};

class Block
{
public:
	static BlockData getData(BlockType type);
};

