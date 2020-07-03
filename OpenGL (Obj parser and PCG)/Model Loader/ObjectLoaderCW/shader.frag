#version 400 core
out vec4 FragColor;

struct Lighting
{
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
	float shininess;
	vec3 lightPos;
	float transparency;
};

uniform Lighting lighting;
uniform vec3 cameraPosition;

uniform sampler2D diffuseTex;
uniform sampler2D ambientTex;

uniform bool hasTex;

in vec3 normal;
in vec2 tex;
in vec3 fragmentPosition;
in vec4 ourColour;

vec3 matAndTex()
{
	vec3 amb = texture(diffuseTex, tex).rgb;
	vec3 norm = normalize(normal);
	vec3 lightDir = normalize(lighting.lightPos - fragmentPosition);
	float diffScale = max(0.0, dot(norm, lightDir));
	vec3 diff = lighting.diffuse * diffScale * texture(diffuseTex, tex).rgb;

	vec3 result = amb + diff;
	return result;
}

vec3 mat()
{
	vec3 amb = 0.1 * lighting.ambient;
	vec3 norm = normalize(normal);
	vec3 lightDir = normalize(lighting.lightPos - fragmentPosition);
	float diffScale = max(0.0, dot(norm, lightDir));
	vec3 diff = lighting.diffuse * diffScale;

	vec3 result = amb + diff;
	return result;
}

void main()
{
	vec3 result;
	if (hasTex) 
	{
		result = matAndTex();
	}
	else 
	{
		result = mat();
	}

	FragColor = vec4(result, lighting.transparency);
	
	
}