#pragma once

#include "Shader.h"
#include "World.h"
#include "Camera.h"
#include <GLFW\glfw3.h>


class Game
{
public:
	Game();

	void run();
	void processInput();
	Camera* getCamera();
	float mouseX = 0.0f, mouseY = 0.0f;
	void toggleWireframe();
	bool getWireframe();


private:
	Shader shader;
	World world;
	Camera camera;
	GLFWwindow* window;
	float deltaTime = 0.0f;
	float lastFrame = 0.0f;
	bool isWireframe = false;
};

