#include "Game.h"

Game::Game() : shader("chunkshader.vert", "chunkshader.frag"), camera(glm::vec3(0.0f, 140.0f, 0.0f)), world(&camera)
{
	this->window = glfwGetCurrentContext();
	shader.use();
	shader.setInt("texMap", 0); //Set this here as it doesnt need to be set more than once.
}

void Game::run()
{
	float currentFrame = glfwGetTime();
	deltaTime = currentFrame - lastFrame;
	lastFrame = currentFrame;
	
	processInput();
	world.update();

	glm::mat4 projection = glm::perspective(glm::radians(camera.getZoom()), (float)1366 / (float)768, 0.1f, 10000.0f);
	glm::mat4 view = camera.GetViewMatrix();
	glm::mat4 model(1.0f);
	shader.use();
	glm::mat4 mvp = projection * view * model;
	shader.setMatrix("mvp", mvp);
	shader.setMatrix("model", model);

	world.render();
}

void Game::processInput()
{
	if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
		glfwSetWindowShouldClose(window, true);
	if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
		camera.ProcessKeyboard(FORWARD, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
		camera.ProcessKeyboard(BACKWARD, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
		camera.ProcessKeyboard(LEFT, deltaTime);
	if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
		camera.ProcessKeyboard(RIGHT, deltaTime);
}

Camera* Game::getCamera()
{
	return &camera;
}

void Game::toggleWireframe()
{
	isWireframe = !isWireframe;
}

bool Game::getWireframe()
{
	return this->isWireframe;
}
