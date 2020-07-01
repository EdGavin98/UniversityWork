#version 400 core
layout (location = 0) in vec3 vPosition;
layout (location = 1) in vec3 vNormal;
layout (location = 2) in vec2 vTex;

out vec3 normal;
out vec2 tex;
out vec3 fragmentPosition;
out vec4 ourColour;

uniform mat4 mvp;
uniform mat4 m;

void main()
{
	gl_Position = mvp * vec4(vPosition, 1.0);
	normal = vNormal;
	tex = vTex;
	fragmentPosition = vec3(m * vec4(vPosition, 1.0));
	ourColour = vec4(0.2f, 0.2f, 0.2f, 1.0f);

}