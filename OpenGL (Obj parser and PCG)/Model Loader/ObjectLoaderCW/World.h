#pragma once
#include "Shader.h"
#include <GLFW\glfw3.h>
#include "Camera.h"
#include "Model.h"
#include <vector>

#include <glm\glm.hpp>
#include <glm\gtc\matrix_transform.hpp>
#include <glm\gtc\type_ptr.hpp>

//Class to store all rendering and interaction based logic for the scene
class World
{
public:
	Camera camera;
	Shader shader;

	World();
	//Render the objects currently stored in the vector
	void render();

	void addModel(Model model);
	void removeModel();

	//Set which model is currently being selected for movement/deletion
	void setSelectedModel();

	void processInput(GLFWwindow* window);
	void updateMouseMovement(double x, double y);

	float getMouseX();
	float getMouseY();
	void setMouseXY(float x, float y);

private:
	std::vector<Model> models;
	int selectedModel;
	float deltaTime;
	float lastFrame;

	float mouseX;
	float mouseY;

};

