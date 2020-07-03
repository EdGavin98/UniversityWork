#include "ModelLoader.h"

ModelLoader::ModelLoader()
{
	window = glfwGetCurrentContext();
}

Model ModelLoader::loadModel(std::string path)
{
	modelFile.open(path);

	if (!modelFile.is_open())
	{
		std::cout << "Error with file";
		Model model(meshes);
		return model;
	}

	if (path.substr(path.length() - 4) == ".obj")
	{
		parseObj();
	}
	else if (path.substr(path.length() - 4) == ".dae")
	{
		parseDae();
	}
	else
	{
		std::cout << "Not supported";
	}

	std::cout << "File loaded" << std::endl;
	Model model(meshes);
	return model;
}

void ModelLoader::parseObj()
{
	bool firstObject = true;
	bool firstMtl = true;

	std::string line;
	std::string word;

	while (getline(modelFile, line))
	{
		std::istringstream current(line);
		std::string token;
		if (!line.empty())
		{
			current >> token;
			if (token == "v")
			{
				glm::vec3 vertex(0.0f);
				current >> vertex.x >> vertex.y >> vertex.z;
				vertices.push_back(vertex);
			}
			else if (token == "vt")
			{
				glm::vec2 texCoord(0.0f);
				current >> texCoord.x >> texCoord.y;
				textures.push_back(texCoord);
			}
			else if (token == "vn")
			{
				glm::vec3 normal(0.0f);
				current >> normal.x >> normal.y >> normal.z;
				normals.push_back(normal);
			}
			else if (token == "f")
			{
				Face face;
				std::string val;
				std::string temp;
				while (!current.eof())
				{
					current >> temp;
					if (!temp.empty())
					{
						std::istringstream item(temp);
						getline(item, val, '/');
						face.vertIdx.push_back(std::stoi(val) - 1);
						getline(item, val, '/');
						if (!val.empty())							//Making sure the face has texture coords (Possible to have X//Z
						{
							face.texIdx.push_back(std::stoi(val) - 1);
						}
						if (getline(item, val, '/'))				//Checking that face has normals (Possible for just X/Y)
						{
							face.normIdx.push_back(std::stoi(val) - 1);
						}
					}
				}
				createFace(face);
			}
			else if (token == "o")
			{
				if (firstObject)
				{
					firstObject = false;
				}
				else
				{
					currentIndices.push_back(indices);
					indices.clear();
					indices.resize(0);
					Mesh mesh(currentVertices, currentIndices, currentMaterials);
					currentVertices.clear();
					currentIndices.clear();
					currentMaterials.clear();
					meshes.push_back(mesh);
					firstMtl = true;
				}
			}
			else if (token == "mtllib")
			{
				std::string path;
				current >> path;
				parseMtl(path);
			}
			else if (token == "usemtl")
			{
				std::string material;
				current >> material;
				currentMaterials.push_back(materials[material]);
				if (firstMtl)
				{
					firstMtl = false;
				}
				else
				{
					currentIndices.push_back(indices);
					indices.clear();
					indices.resize(0);
				}

			}
		}
	}

	currentIndices.push_back(indices);
	indices.clear();
	indices.resize(0);
	Mesh mesh(currentVertices, currentIndices, currentMaterials);
	meshes.push_back(mesh);

	//None of these are needed anymore, so de-allocate any memory they are using
	//Could be done just when the model is deleted, but nearly doubles ram usage during rendering if its left till then.
	modelFile.close();
	std::vector<Vertex>().swap(currentVertices);
	std::vector<std::vector<unsigned int>>().swap(currentIndices);
	std::vector<unsigned int>().swap(indices);
	std::vector<glm::vec3>().swap(vertices);
	std::vector<glm::vec2>().swap(textures);
	std::vector<glm::vec3>().swap(normals);
}

void ModelLoader::createFace(Face face)
{
	Vertex vertex;
	unsigned short numVertices = face.vertIdx.size();

	if (numVertices == 4)
	{
		int idx1, idx2;
		try
		{
			//Triangle 1
			vertex.Position = vertices.at(face.vertIdx[0]);					//using .at() instead of [] operator as it performs a bounds check
			vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[0]) : glm::vec2(0.0f);  //Set these to 0 if they dont have textures or normals
			vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[0]) : glm::vec3(0.0f);
			idx1 = currentVertices.size();
			indices.push_back(idx1);
			currentVertices.push_back(vertex);

			vertex.Position = vertices.at(face.vertIdx[1]);
			vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[1]) : glm::vec2(0.0f);
			vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[1]) : glm::vec3(0.0f);
			indices.push_back(currentVertices.size());
			currentVertices.push_back(vertex);

			vertex.Position = vertices.at(face.vertIdx[2]);
			vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[2]) : glm::vec2(0.0f);
			vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[2]) : glm::vec3(0.0f);
			idx2 = currentVertices.size();
			indices.push_back(idx2);
			currentVertices.push_back(vertex);

			//Triangles 2
			indices.push_back(idx1);
			indices.push_back(idx2);
			vertex.Position = vertices.at(face.vertIdx[3]);
			vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[3]) : glm::vec2(0.0f);
			vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[3]) : glm::vec3(0.0f);
			indices.push_back(currentVertices.size());
			currentVertices.push_back(vertex);
		}
		catch (std::out_of_range e)				//Throw this if no vertex position exists at the index specified, could occur if file was modified
		{
			std::cout << "Something went wrong" << std::endl;
			glfwSetWindowShouldClose(window, GL_TRUE);			//Set this so it will skip the render loop and close if the model is invalid.
		}

	}
	else
	{
		for (int i = 1; i < numVertices - 1; i++) //Generate multiple triangles for faces with 3 or more than 4
		{
			try
			{
				vertex.Position = vertices.at(face.vertIdx[0]);
				vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[0]) : glm::vec2(0.0f);  //Set these to 0 if they dont have textures or normals
				vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[0]) : glm::vec3(0.0f);
				indices.push_back(currentVertices.size());
				currentVertices.push_back(vertex);

				vertex.Position = vertices.at(face.vertIdx[i]);
				vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[i]) : glm::vec2(0.0f);
				vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[i]) : glm::vec3(0.0f);
				indices.push_back(currentVertices.size());
				currentVertices.push_back(vertex);

				vertex.Position = vertices.at(face.vertIdx[i + 1]);
				vertex.TexCoords = !face.texIdx.empty() ? textures.at(face.texIdx[i + 1]) : glm::vec2(0.0f);
				vertex.Normal = !face.normIdx.empty() ? normals.at(face.normIdx[i + 1]) : glm::vec3(0.0f);
				indices.push_back(currentVertices.size());
				currentVertices.push_back(vertex);
			}
			catch  (std::out_of_range e)
			{
				std::cout << "Something went wrong" << std::endl;
				glfwSetWindowShouldClose(window, GL_TRUE);
			}
			

		}
	}
}

void ModelLoader::parseMtl(std::string path)
{
	std::ifstream mtlFile(path);

	if (!mtlFile.is_open())
	{
		std::cout << "Error parsing material";
	}

	std::string line;
	Material material{};
	std::string currentMtl;

	while (getline(mtlFile, line))
	{
		std::istringstream current(line);
		if (!line.empty())
		{
			std::string token;
			current >> token;
			if (token == "newmtl")
			{
				current >> currentMtl;
				materials.insert({ currentMtl, material });
			}
			else if (token == "Ka")
			{
				current >> materials[currentMtl].ambient.x >> materials[currentMtl].ambient.y >> materials[currentMtl].ambient.z;
			}
			else if (token == "Kd")
			{
				current >> materials[currentMtl].diffuse.x >> materials[currentMtl].diffuse.y >> materials[currentMtl].diffuse.z;
			}
			else if (token == "Ks")
			{
				current >> materials[currentMtl].specular.x >> materials[currentMtl].specular.y >> materials[currentMtl].specular.z;
			}
			else if (token == "Ke")
			{
				current >> materials[currentMtl].emission.x >> materials[currentMtl].emission.y >> materials[currentMtl].emission.z;
			}
			else if (token == "Tr")
			{
				float opacity;
				current >> opacity;
				materials[currentMtl].transparency = 1.0f - opacity;
			}
			else if (token == "d")
			{
				current >> materials[currentMtl].transparency;
			}
			else if (token == "Ni")
			{
				current >> materials[currentMtl].density;
			}
			else if (token == "Ns")
			{
				current >> materials[currentMtl].shininess;
			}
			else if (token == "illum")
			{
				current >> materials[currentMtl].illuminationModel;
			}
			else if (token == "map_Ka")
			{
				std::string temp;
				current >> temp;
				Texture tex = loadTexture(temp);
				tex.type = "ambient";
				materials[currentMtl].textures.push_back(tex);
			}
			else if (token == "map_Kd")
			{
				std::string temp;
				current >> temp;
				Texture tex = loadTexture(temp);
				tex.type = "diffuse";
				materials[currentMtl].textures.push_back(tex);
			}
			else if (token == "map_d")
			{
				std::string temp;
				current >> temp;
				Texture tex = loadTexture(temp);
				tex.type = "transparency";
				materials[currentMtl].textures.push_back(tex);
			}

		}
	}
}

Texture ModelLoader::loadTexture(std::string path) {
	unsigned int textureID;
	glGenTextures(1, &textureID);
	stbi_set_flip_vertically_on_load(true);
	int width, height, components;
	unsigned char* data = stbi_load(path.c_str(), &width, &height, &components, 0);
	if (data)
	{

		//Check if the file is Jpg or png (Or other 3/4 channel file formats)
		GLenum format;
		if (components == 3)
			format = GL_RGB;
		else if (components == 4)
			format = GL_RGBA;

		//Bind and set parameters for the texture
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}
	else
	{
		std::cout << "Failed to load texture";
	}

	stbi_image_free(data);

	//Create a texture structure with the Id, tex.type will be set by the material parser
	Texture tex;
	tex.id = textureID;
	tex.path = path;

	return tex;
}

void ModelLoader::parseDae()
{
	std::string line;
	std::map<std::string, Material> tempMaterials;

	while (getline(modelFile, line))
	{

		//Regular Expressions for specific tag types
		std::regex effectReg("\\s*<\\s*effect.*>");
		std::regex effectEndReg("\\s*</\\s*effect\\s*>");
		std::regex meshReg("\\s*<\\s*mesh\\s*>");
		std::regex meshEndReg("\\s*</\\s*mesh\\s*>");
		std::regex materialReg("\\s*<\\s*material\\s*id=\"(.*)\".*name=.*");
		std::regex imageReg("\\s*<\\s*image\\s*.*>");
		std::regex tagContentsReg("\\s*<([a-zA-Z]*).*>");
		std::regex contentsReg(">(.*)</");
		std::regex posArrayReg("\\s*<float_array.*positions.*");
		std::regex normArrayReg("\\s*<float_array.*normals.*");
		std::regex texArrayReg("\\s*<float_array.*map.*");
		std::regex faceArrayReg("\\s*<p>.*");
		std::regex trianglesReg("\\s*<triangles\\s*material=\"(.*)\"\\s*count.*");
		std::regex texReg("\\s<.* />");
		std::regex urlReg(".*url=\"(.*)\".*");
		std::smatch match;
		if (std::regex_match(line, effectReg))
		{
			std::regex idReg("id=\"(.*)\"");
			std::regex_search(line, match, idReg);

			Material mat;
			std::string matName = match[1];
			tempMaterials.insert({matName, mat});
			while (!std::regex_match(line, effectEndReg))
			{
				getline(modelFile, line);
				if (std::regex_search(line, match, tagContentsReg))
				{
					std::string type = match[1];
					if (type == "diffuse")
					{
						getline(modelFile, line);
						if (std::regex_search(line, match, contentsReg))
						{
							std::string current = match[1];
							std::istringstream vals(current);
							vals >> tempMaterials[matName].diffuse.x >> tempMaterials[matName].diffuse.y >> tempMaterials[matName].diffuse.z;
						} 
						else if (std::regex_match(line, texReg))
						{
							std::cout << "Textures not supported";
						}

					}
					else if (type == "transparent")
					{
						getline(modelFile, line);
						if (std::regex_search(line, match, contentsReg))
						{
							std::string current = match[1];
							std::istringstream vals(current);
							std::string temp;
							vals >> temp >> temp >> temp >> tempMaterials[matName].transparency;
						}
					}
				}
			}
		}
		else if (std::regex_match(line, imageReg))
		{
			getline(modelFile, line);
			if (std::regex_search(line, match, contentsReg))
			{
				std::string file = match[1];
				Texture tex = loadTexture(file);
				tex.type = "diffuse";

				for (std::pair<std::string, Material> mat : tempMaterials)
				{
					tempMaterials[mat.first].textures.push_back(tex);
				}
			}
		}
		else if (std::regex_match(line, meshReg))
		{
			while (!std::regex_match(line, meshEndReg))
			{
				getline(modelFile, line);
				std::string searchLine = line.substr(0, 160);
				if (std::regex_match(line.substr(0, 160), posArrayReg))
				{
					std::regex_search(line, match, contentsReg);
					std::string current = match[1];
					std::istringstream str(current);

					while (!str.eof())
					{
						glm::vec3 vec;
						str >> vec.x >> vec.y >> vec.z;
						vertices.push_back(vec);
					}
				}
				else if (std::regex_match(line.substr(0,160), normArrayReg))
				{
					std::regex_search(line, match, contentsReg);
					std::string current = match[1];
					std::istringstream str(current);
					while (!str.eof())
					{
						glm::vec3 vec;
						str >> vec.x >> vec.y >> vec.z;
						normals.push_back(vec);
					}
				}
				else if (std::regex_match(line.substr(0, 160), texArrayReg))
				{
					std::regex_search(line, match, contentsReg);
					std::string current = match[1];
					std::istringstream str(current);

					while (!str.eof())
					{
						glm::vec2 vec;
						str >> vec.x >> vec.y;
						textures.push_back(vec);
					}
				}
				else if (std::regex_search(searchLine, match, trianglesReg))
				{
					std::string mat = match[1];
					currentMaterials.push_back(materials[mat]);
				}
				else if (std::regex_match(line.substr(0, 30), faceArrayReg))
				{
					std::regex_search(line, match, contentsReg);
					std::string current = match[1];
					std::istringstream str(current);
					while (!str.eof())
					{
						Vertex v;
						int index;
						try
						{
							str >> index;
							v.Position = vertices.at(index);
							str >> index;
							v.Normal = normals.at(index);
							str >> index;
							v.TexCoords = textures.at(index);
						}
						catch (std::out_of_range e)
						{
							std::cout << "Something went wrong: " << std::endl;
							glfwSetWindowShouldClose(window, GL_TRUE);
						}
						
						

						indices.push_back(currentVertices.size());
						currentVertices.push_back(v);
					}
					currentIndices.push_back(indices);
					indices.clear();
				}
			}
			Mesh mesh(currentVertices, currentIndices, currentMaterials);
			meshes.push_back(mesh);
			vertices.clear();
			textures.clear();
			normals.clear();
			currentMaterials.clear();
			currentIndices.clear();
			currentVertices.clear();
		}
		else if (std::regex_search(line, match, materialReg))
		{
			std::string id = match[1];
			getline(modelFile, line);
			if (std::regex_search(line, match, urlReg))
			{
				std::string effect = match[1];
				materials.insert({ id, tempMaterials[effect.substr(1)] });
			}
		}
	}

	modelFile.close();
	std::vector<Vertex>().swap(currentVertices);
	std::vector<std::vector<unsigned int>>().swap(currentIndices);
	std::vector<unsigned int>().swap(indices);
	std::vector<glm::vec3>().swap(vertices);
	std::vector<glm::vec2>().swap(textures);
	std::vector<glm::vec3>().swap(normals);

}