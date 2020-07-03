#include "World.h"

World::World() : shader("shader.vert", "shader.frag"), camera(glm::vec3(0.0f, 0.0f, 3.0f))
{
	selectedModel = -1;
	deltaTime = 0.0f;
	lastFrame = 0.0f;
}

void World::render()
{
	float currentFrame = glfwGetTime();
	deltaTime = currentFrame - lastFrame;
	lastFrame = currentFrame;
	glm::mat4 projection = glm::perspective(glm::radians(camera.getZoom()), (float)1366 / (float)768, 0.1f, 10000.0f);
	glm::mat4 view = camera.GetViewMatrix();

	


	for (Model model : models)
	{
		shader.use();
		glm::mat4 mvp = projection * view * model.getModelMatrix();
		shader.setMatrix("mvp", mvp);
		shader.setMatrix("m", model.getModelMatrix());
		shader.setVector3("cameraPosition", camera.getPosition());
		model.Draw(shader);
	}
}

void World::addModel(Model model)
{
	this->models.push_back(model);
}

void World::removeModel()
{
	if (selectedModel != -1)
	{
		models.erase(models.begin() + selectedModel);
		setSelectedModel();
	}
}

void World::setSelectedModel()
{
	selectedModel++;

	if (selectedModel >= models.size())
	{
		selectedModel = 0;
	}
	if (models.empty() || models.size() == 0)
	{
		selectedModel = -1;
	}
}



void World::processInput(GLFWwindow *window)
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
	if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS && selectedModel != -1)
		models[selectedModel].translate(glm::vec3(5.0f * deltaTime, 0.0f, 0.0f));
	if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS && selectedModel != -1)
		models[selectedModel].translate(glm::vec3(-5.0f * deltaTime, 0.0f, 0.0f));
	if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS && selectedModel != -1)
		models[selectedModel].translate(glm::vec3(0.0f, 0.0f, -5.0f * deltaTime));
	if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS && selectedModel != -1)
		models[selectedModel].translate(glm::vec3(0.0f, 0.0f, 5.0f * deltaTime));
}

void World::updateMouseMovement(double x, double y)
{
	camera.ProcessMouseMovement(x, y);
}

float World::getMouseX()
{
	return this->mouseX;
}

float World::getMouseY()
{
	return this->mouseY;
}

void World::setMouseXY(float x, float y)
{
	this->mouseX = x;
	this->mouseY = y;
}



