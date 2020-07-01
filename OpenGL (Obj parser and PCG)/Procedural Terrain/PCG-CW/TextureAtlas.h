#pragma once
#include <GL/glew.h>
#include <glm\glm.hpp>
#include <array>
#include <iostream>

#include "stb_image.h"

enum FaceType
{
	GRASS_SIDE,
	DIRT,
	GRASS_TOP,
	STONE,
	WATER,
	SAND,
	LOG_SIDE,
	LOG_TOP,
	LEAF
};

class TextureAtlas
{

public:
	static TextureAtlas* getInstance();
	std::array<glm::vec2, 4> getTexture(FaceType type);
	static TextureAtlas* instance;


private:
	TextureAtlas();
	GLuint texId;
	GLint imageSize;
	GLint textureSize;
	void loadImage(std::string path);

};

