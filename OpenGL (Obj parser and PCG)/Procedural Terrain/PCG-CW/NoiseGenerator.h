#pragma once
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

class NoiseGenerator
{
public:
	NoiseGenerator(int seed, float amp, int oct, float rough);
	float getValue(int x, int z);
private:
	float getNoise(int x, int z);
	float smoothNoise(int x, int z);
	float lerp(float a, float b, float blend);
	float lerpNoise(float x, float z);

	int seed;
	int octaves;
	int roughness;
	int amplitude;

};

