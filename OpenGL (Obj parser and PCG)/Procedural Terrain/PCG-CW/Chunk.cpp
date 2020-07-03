#include "Chunk.h"

Chunk::~Chunk()
{
	glDeleteVertexArrays(1, &VAO);
	glDeleteBuffers(1, &VBO);
	glDeleteBuffers(1, &EBO);
}

Chunk::Chunk(GLint x, GLint z, ChunkController* controller) : position(x, z)
{
	this->controller = controller;
	genChunk();
}

void Chunk::genChunk()
{
	for (int row = 0; row < 16; row++)
		for (int col = 0; col < 16; col++)
		{
			int x = position.x * 16 + row;
			int y = position.y * 16 + col;
			int height = controller->getHeightValue(x, y);
			int biome = controller->getBiomeValue(x, y);
			heightMap[row][col] = height;
			biomeMap[row][col] = biome;
			shouldAddTree(row, col);

			highestBlock = (height > highestBlock) ? height : highestBlock;
		}


	for (int i = 0; i <= highestBlock; i++)
	{
		ChunkLayer layer;
		layers.push_back(layer);
		for (int j = 0; j < 16; j++)
			for (int k = 0; k < 16; k++)
			{
				int height = heightMap[j][k];
				if (height < i)
				{
					layers[i].blocks[j][k] = BlockType::AIR;
				}
				else if (i < height - 3)
				{
					layers[i].blocks[j][k] = BlockType::STONE;
				}
				else if (i >= height - 3 && i < height)
				{
					if (biomeMap[j][k] >= 21)
						layers[i].blocks[j][k] = BlockType::DIRT;
					else
						layers[i].blocks[j][k] = BlockType::SAND;
				}
				else if (i >= height)
				{
					if (biomeMap[j][k] >= 21)
						layers[i].blocks[j][k] = BlockType::GRASS;
					else
						layers[i].blocks[j][k] = BlockType::SAND;
				}
			}
	}

	for (int row = 0; row < 16; row++)
		for (int col = 0; col < 16; col++)
		{
			if (treeMap[row][col])
			{
				addTree(row, col, heightMap[row][col]);
			}	
		}

	genMesh();
	genVao();
}

void Chunk::genMesh()
{
	AdjacentBlocks neighbours;

	vertices.clear();
	indices.clear();

	for (int y = 0; y <= highestBlock; y++)
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++)
			{
				neighbours.updatePostions(x, y, z);
				BlockType type = layers[y].blocks[x][z];
				if (type != BlockType::AIR)
				{
					BlockData data = Block::getData(type);
					addFace(faceData::frontFace, data.sideTex, x, y, z, neighbours.front);
					addFace(faceData::backFace, data.sideTex, x, y, z, neighbours.back);
					addFace(faceData::leftFace, data.sideTex, x, y, z, neighbours.left);
					addFace(faceData::rightFace, data.sideTex, x, y, z, neighbours.right);
					addFace(faceData::topFace, data.topTex, x, y, z, neighbours.up);
					addFace(faceData::bottomFace, data.bottomTex, x, y, z, neighbours.down);
				}
			}

	if (VAO != 0)
	{
		glBindVertexArray(VAO);
		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(Vertex), &vertices[0], GL_STATIC_DRAW);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size() * sizeof(GLuint), &indices[0], GL_STATIC_DRAW);

		glBindVertexArray(0);
	}

}

void Chunk::genVao()
{  
	glGenVertexArrays(1, &VAO);
	glGenBuffers(1, &VBO);
	glGenBuffers(1, &EBO);

	glBindVertexArray(VAO);
	glBindBuffer(GL_ARRAY_BUFFER, VBO);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
	glBufferData(GL_ARRAY_BUFFER, vertices.size() * sizeof(Vertex), &vertices[0], GL_STATIC_DRAW);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size() * sizeof(GLuint), &indices[0], GL_STATIC_DRAW);

	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)0);
	glEnableVertexAttribArray(0);
	glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)offsetof(Vertex, Normal));
	glEnableVertexAttribArray(1);
	glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (void*)offsetof(Vertex, TexCoords));
	glEnableVertexAttribArray(2);
	glBindVertexArray(0);
}

void Chunk::draw()
{
	glBindVertexArray(VAO);
	glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
	glBindVertexArray(0);
}

BlockType Chunk::getBlock(int x, int y, int z)
{
	if (y > highestBlock) 
		return BlockType::AIR;
	else
		return layers[y].blocks[x][z];
}

void Chunk::addFace(std::array<glm::vec3, 5> blockData, FaceType tex, int x, int y, int z, glm::ivec3 neighbour)
{
	TextureAtlas* atlas = TextureAtlas::getInstance();
	int currIndex = vertices.size();
	auto textureData = atlas->getTexture(tex);
	glm::vec3 location(16 * position.x + x, y , 16 * position.y + z);

	//If its within the bounds of the chunk, directly  it
	if (neighbour.y <= highestBlock && neighbour.x < 16 && neighbour.z < 16 && neighbour.y >= 0 && neighbour.x >= 0 && neighbour.z >= 0)
		if(layers[neighbour.y].blocks[neighbour.x][neighbour.z] != BlockType::AIR)
			return;


	//If not, check through the controller
	int findX = position.x * 16 + neighbour.x;
	int findZ = position.y * 16 + neighbour.z;
	if (neighbour.y >= 0)
		if (controller->getBlock(findX, neighbour.y, findZ) != BlockType::AIR)
			return;

	Vertex v;
	v.Position = blockData[0] + location;
	v.TexCoords = textureData[0];
	v.Normal = blockData[4];
	vertices.push_back(v);
	v.Position = blockData[1] + location;
	v.TexCoords = textureData[1];
	vertices.push_back(v);
	v.Position = blockData[2] + location;
	v.TexCoords = textureData[2];
	vertices.push_back(v);
	v.Position = blockData[3] + location;
	v.TexCoords = textureData[3];
	vertices.push_back(v);

	indices.push_back(currIndex);
	indices.push_back(currIndex + 1);
	indices.push_back(currIndex + 2);
	indices.push_back(currIndex);
	indices.push_back(currIndex + 2);
	indices.push_back(currIndex + 3);
	
}

void Chunk::shouldAddTree(int row, int col)
{
	if (row > 0 && col > 0 && row < 15 && col < 15) //No trees on chunk edge block
	{
		if (!treeMap[row + 1][col] && !treeMap[row - 1][col]
			&& !treeMap[row][col + 1] && !treeMap[row][col - 1]
			&& !treeMap[row + 1][col + 1] && !treeMap[row - 1][col - 1]
			&& !treeMap[row + 1][col - 1] && !treeMap[row - 1][col + 1])
		{
			double random = (double)rand() / RAND_MAX;
			if (random > 0.99 && biomeMap[row][col] >= 21)
			{
				treeMap[row][col] = true;
				return;
			}
		}
	}
	treeMap[row][col] = false;
}

void Chunk::addTree(int row, int col, int height)
{
	int layersLeft = highestBlock - (height + 6); //Adding trees may cause the heighest block in the chunk to change, meaning new layers need to be added.
	if (layersLeft < 0)
	{
		for (int i = 0; i >= layersLeft; i--)
		{
			ChunkLayer layer;
			layers.push_back(layer);
			initLayer(layers.size() - 1); //Set all blocks on the new layer to air 
		}
	}

	highestBlock = layers.size() - 1;

	//Make a tree
	layers[height + 1].blocks[row][col] = BlockType::LOG;
	layers[height + 2].blocks[row][col] = BlockType::LOG;
	layers[height + 3].blocks[row][col] = BlockType::LOG;
	layers[height + 4].blocks[row][col] = BlockType::LOG;
	layers[height + 4].blocks[row + 1][col] = BlockType::LEAF;
	layers[height + 4].blocks[row - 1][col] = BlockType::LEAF;
	layers[height + 4].blocks[row][col + 1] = BlockType::LEAF;
	layers[height + 4].blocks[row][col - 1] = BlockType::LEAF;
	layers[height + 4].blocks[row + 1][col + 1] = BlockType::LEAF;
	layers[height + 4].blocks[row - 1][col - 1] = BlockType::LEAF;
	layers[height + 4].blocks[row + 1][col - 1] = BlockType::LEAF;
	layers[height + 4].blocks[row - 1][col + 1] = BlockType::LEAF;
	layers[height + 5].blocks[row][col] = BlockType::LOG;
	layers[height + 5].blocks[row + 1][col] = BlockType::LEAF;
	layers[height + 5].blocks[row - 1][col] = BlockType::LEAF;
	layers[height + 5].blocks[row][col + 1] = BlockType::LEAF;
	layers[height + 5].blocks[row][col - 1] = BlockType::LEAF;
	layers[height + 5].blocks[row + 1][col + 1] = BlockType::LEAF;
	layers[height + 5].blocks[row - 1][col - 1] = BlockType::LEAF;
	layers[height + 5].blocks[row + 1][col - 1] = BlockType::LEAF;
	layers[height + 5].blocks[row - 1][col + 1] = BlockType::LEAF;
	layers[height + 6].blocks[row][col] = BlockType::LEAF;
}

void Chunk::initLayer(int height) 
{
	for (int row = 0; row < 16; row++)
	{
		layers[height].blocks[row][0] = BlockType::AIR;
		layers[height].blocks[row][1] = BlockType::AIR;
		layers[height].blocks[row][2] = BlockType::AIR;
		layers[height].blocks[row][3] = BlockType::AIR;
		layers[height].blocks[row][4] = BlockType::AIR;
		layers[height].blocks[row][5] = BlockType::AIR;
		layers[height].blocks[row][6] = BlockType::AIR;
		layers[height].blocks[row][7] = BlockType::AIR;
		layers[height].blocks[row][8] = BlockType::AIR;
		layers[height].blocks[row][9] = BlockType::AIR;
		layers[height].blocks[row][10] = BlockType::AIR;
		layers[height].blocks[row][11] = BlockType::AIR;
		layers[height].blocks[row][12] = BlockType::AIR;
		layers[height].blocks[row][13] = BlockType::AIR;
		layers[height].blocks[row][14] = BlockType::AIR;
		layers[height].blocks[row][15] = BlockType::AIR;
	}
}
