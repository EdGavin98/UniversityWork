#include "GL\glew.h"
#include "GLFW\glfw3.h"
#include "GL\freeglut.h"
#include "Model.h"
#include "ModelLoader.h"
#include "World.h"

GLFWwindow* initWindow(int width, int height) {
	glfwInit();
	glfwWindowHint(GL_MAJOR_VERSION, 4);
	glfwWindowHint(GL_MINOR_VERSION, 0);
	GLFWwindow* window = glfwCreateWindow(width, height, "Test", NULL, NULL);
	glfwMakeContextCurrent(window);
	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	glewExperimental = GL_TRUE;
	glViewport(0, 0, 1366, 768);
	glewInit();
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_BLEND); 
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	return window;
}

void mouseMovement(GLFWwindow* window, double x, double y)
{
	World* w = (World*)glfwGetWindowUserPointer(window);

	float xOffset = x - w->getMouseX();
	float yOffset = w->getMouseY() - y; // reversed since y-coordinates go from bottom to top

	w->setMouseXY(x, y);
	w->updateMouseMovement(xOffset, yOffset);
}

void newModel(World* world)
{
	ModelLoader loader;
	std::string modelPath;
	std::cout << "Please enter the file name (including extension) of the model you wish to load: ";
	std::cin >> modelPath;
	Model model = loader.loadModel(modelPath);
	world->addModel(model);
	world->setSelectedModel();
}

void singleInput(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	World* w = (World *)glfwGetWindowUserPointer(window);
	if (glfwGetKey(window, GLFW_KEY_TAB) == GLFW_PRESS)
		w->setSelectedModel();
	if (glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS)
		w->removeModel();
	if (glfwGetKey(window, GLFW_KEY_N) == GLFW_PRESS)
		newModel(w);
}

int main() {
	GLFWwindow* window = initWindow(1366, 768);
	World world;
	//Set a glfw window pointer to the world to avoid globals;
	glfwSetWindowUserPointer(window, (void *)&world);
	glfwSetKeyCallback(window, singleInput);
	glfwSetCursorPosCallback(window, mouseMovement);
	newModel(&world);

	while (!glfwWindowShouldClose(window)) {
		


		//Update the inputs
		world.processInput(window);

		//Render the world
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.2f, 0.4f, 0.2f, 1.0f);
		world.render();

		glfwSwapBuffers(window);
		glfwPollEvents();
	}
}