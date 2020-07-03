#version 400 core

out vec4 fragColour;

in vec2 texCoords;
in vec3 normals;
in vec3 fragpos;

uniform sampler2D texMap;

void main() 
{
	vec4 ambient = vec4(0.8);
	vec3 norm = normalize(normals);
	vec3 lightDir = normalize(vec3(10.0, 5.0, 7.0));
	float diff = max(dot(norm, lightDir), 0.0);

	fragColour = (diff + ambient) * texture(texMap, texCoords);
}