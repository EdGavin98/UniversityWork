#pragma once

#include <GL\glew.h>
#include "GL\freeglut.h"
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>
#include <glm\glm.hpp>
#include <glm\gtc\type_ptr.hpp>

//Abstract class for shaders
class Shader
{

public:
	//Constructor to create and bind the shader
	Shader(const GLchar* vertexPath, const GLchar* fragPath);

	//Call glUseProgram for this shader
	void use();

	//Set shader uniforms with various data types
	void setBool(const std::string& name, bool value);
	void setInt(const std::string& name, int value);
	void setFloat(const std::string& name, float value);
	void setMatrix(const std::string& name, glm::mat4 value);
	void setVector3(const std::string& name, glm::vec3 value);

private:
	unsigned int ID;
};

