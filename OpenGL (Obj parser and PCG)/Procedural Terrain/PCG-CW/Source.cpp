#include <GL\glew.h>
#include <GL\freeglut.h>
#include <GLFW\glfw3.h>
#include "Game.h"

#pragma region glfwFunctions
void framebufferSize(GLFWwindow* window, int width, int height)
{
	glViewport(0, 0, width, height);
}

void mouseMovement(GLFWwindow* window, double x, double y)
{
	Game* g = (Game*)glfwGetWindowUserPointer(window);

	float xOffset = x - g->mouseX;
	float yOffset = g->mouseY - y; // reversed since y-coordinates go from bottom to top
	g->mouseX = x;
	g->mouseY = y;
	g->getCamera()->ProcessMouseMovement(xOffset, yOffset);
}

void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	Game* g = (Game*)glfwGetWindowUserPointer(window);
	if (key == GLFW_KEY_P && action == GLFW_PRESS)
	{
		g->toggleWireframe();

		if (g->getWireframe())
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		else
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}

GLFWwindow* init(int height, int width)
{
	//Set up GLFW and Window
	glfwInit();
	glfwWindowHint(GL_MAJOR_VERSION, 4);
	glfwWindowHint(GL_MINOR_VERSION, 0);
	GLFWwindow* window = glfwCreateWindow(width, height, "Minceraft", NULL, NULL);
	glfwMakeContextCurrent(window);
	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	glViewport(0, 0, 1366, 768);
	glfwSetFramebufferSizeCallback(window, framebufferSize);

	//Get modern OpenGL functions
	glewExperimental = GL_TRUE;
	glewInit();

	//Enable glOptions
	glEnable(GL_TEXTURE_2D);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);
	glCullFace(GL_CCW);

	return window;
}
#pragma endregion



int main()
{
	GLFWwindow* window = init(768, 1366);
	Game game;

	glfwSetWindowUserPointer(window, (void*)&game);
	glfwSetCursorPosCallback(window, mouseMovement);
	glfwSetKeyCallback(window, keyCallback);

	while (!glfwWindowShouldClose(window))
	{
		glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		game.run();

		glfwSwapBuffers(window);
		glfwPollEvents();
	}

}