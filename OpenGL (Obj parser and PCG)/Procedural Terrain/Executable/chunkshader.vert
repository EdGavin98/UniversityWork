#version 400 core
layout (location = 0) in vec3 vertPos;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 tex;

uniform mat4 mvp;
uniform mat4 model;

out vec2 texCoords;
out vec3 normals;

void main()
{
	gl_Position = mvp * vec4(vertPos, 1.0);
	texCoords = tex;
	normals = normal;
}