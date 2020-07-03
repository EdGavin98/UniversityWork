#include "NoiseGenerator.h"

NoiseGenerator::NoiseGenerator(int seed, float amp, int oct, float rough)
{
	this->seed = seed;
	this->amplitude = amp;
	this->octaves = oct;
	this->roughness = rough;
}

float NoiseGenerator::getValue(int x, int z)
{
	float total = 0.0f;
	float d = (float)pow(2, octaves - 1);
	for (int i = 0; i < octaves; i++)
	{
		float frequency = (float)(pow(2, i) / d);
		float amp = (float)pow(roughness, i) * amplitude;
		total += lerpNoise(x * frequency, z * frequency) * amp;
	}
	return total;
}

float NoiseGenerator::getNoise(int x, int z)
{
	srand(x * 1026 + z * 5342 + seed); //Need to ensure same value for each x/z coord, big number * cause close seeds were giving similar values
	float noise = ((float)rand() / (float)RAND_MAX); //Random int between 0 and 1
	return noise;
}

float NoiseGenerator::smoothNoise(int x, int z)
{
	float sides = getNoise(x + 1, z + 1) + getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1);
	float corners = getNoise(x + 1, z) + getNoise(x - 1, z) + getNoise(x, z - 1) + getNoise(x, z + 1);
	float center = getNoise(x, z);

	sides /= 8.0f;
	corners /= 4.0f;
	center /= 2.0f;

	float noise = sides + corners + center;

	return noise;
}

float NoiseGenerator::lerp(float a, float b, float blend)
{
	float f = (float)(1.0f - cos(blend * 3.141592)) * 0.5f;
	return a * (1.0f - f) + b * f;
}

float NoiseGenerator::lerpNoise(float x, float z)
{
	int xFloor = floor(x);
	int zFloor = floor(z);

	float v1 = smoothNoise(xFloor, zFloor);
	float v2 = smoothNoise(xFloor + 1, zFloor);
	float v3 = smoothNoise(xFloor, zFloor + 1);
	float v4 = smoothNoise(xFloor + 1, zFloor + 1);
	float i1 = lerp(v1, v2, x - xFloor);
	float i2 = lerp(v3, v4, x - xFloor);

	return lerp(i1, i2, z - zFloor);
}
