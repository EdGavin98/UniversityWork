#pragma once

#include "GL/glew.h";
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>

//Abstractions for camera movement
enum CameraMovement {
	FORWARD,
	BACKWARD,
	LEFT,
	RIGHT
};

class Camera
{
public:

	// Constructor with vectors
	Camera(glm::vec3 position = glm::vec3(0.0f, 140.0f, 0.0f), glm::vec3 up = glm::vec3(0.0f, 1.0f, 0.0f), float yaw = -90.0f, float pitch = 0.0f);
	// Returns the view matrix calculated using Euler Angles and the LookAt Matrix
	glm::mat4 GetViewMatrix();

	//Process the keyboard and mouse movement
	void ProcessKeyboard(CameraMovement direction, float deltaTime);
	void ProcessMouseMovement(float xoffset, float yoffset);

	glm::vec3 getPosition();
	float getZoom();

private:
	// Calculates the new front vector based on euler angles.
	void updateCameraVectors();

	//Attributes and Euler Angles
	glm::vec3 Position;
	glm::vec3 Front;
	glm::vec3 Up;
	glm::vec3 Right;
	glm::vec3 WorldUp;
	float Yaw;
	float Pitch;
	//Options
	float MovementSpeed;
	float MouseSensitivity;
	float Zoom;

};
