#include "TextureAtlas.h"
TextureAtlas::TextureAtlas()
{
	this->imageSize = 144;
	this->textureSize = 16;
	loadImage("TextureAtlas.png");
}

TextureAtlas* TextureAtlas::getInstance()
{
	if (instance == nullptr)
	{
		std::cout << "Hello";
		instance = new TextureAtlas();
	}
	return instance;
}

std::array<glm::vec2, 4> TextureAtlas::getTexture(FaceType type)
{
	const GLfloat PIXEL_SIZE = 1.0f / (float)imageSize;
	const GLfloat NUM_TEX = imageSize / textureSize;
	const GLfloat TEX_SIZE = 1.0f / NUM_TEX;

	GLfloat minX = (type * TEX_SIZE) + PIXEL_SIZE;
	GLfloat maxX = minX + TEX_SIZE - PIXEL_SIZE;
	GLfloat maxY = 1.0f;
	GLfloat minY = 0.0f;
	return
	{
		glm::vec2(maxX, maxY),
		glm::vec2(minX, maxY),
		glm::vec2(minX, minY),
		glm::vec2(maxX, minY)
	};
}

void TextureAtlas::loadImage(std::string path)
{
	unsigned int textureID;
	glGenTextures(1, &textureID);
	//stbi_set_flip_vertically_on_load(true);
	int width, height, components;
	unsigned char* data = stbi_load(path.c_str(), &width, &height, &components, 0);
	if (data)
	{
		GLenum format = GL_RGBA;

		//Bind and set parameters for the texture
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	}
	else
	{
		std::cout << "Failed to load texture";
	}
	stbi_image_free(data);

	this->texId = textureID;
}

TextureAtlas* TextureAtlas::instance = nullptr;