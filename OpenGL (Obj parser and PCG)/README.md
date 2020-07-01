# Procedural Minecraft Terrain (PCG-CW) #

## Software and Library versions ##
* Visual Studio 2019 Community
* OpenGl 4.0 Core  
  * Nupengl.core 0.1.0.1  
* GLM 9.9.6
* STB Image  2.23

## How to use ##
1) Start the program
    * There is a folder called Executable, in there run PCG-CW.exe to run the program.
    * Or open the .sln and run build and run through visual studio.
2) Wait for the initial map to load
3) Once it has loaded you may: 
    * Move around using <kbd>W</kbd><kbd>A</kbd><kbd>S</kbd><kbd>D</kbd> and mouse movement
    * Switch between normal and wireframe view by pressing <kbd>P</kbd>
    * Close the program by pressing <kbd>Esc</kbd>

## Initial idea and what I started with ##

The idea for the project came from an interest in procedural content systems (and Minecraft in general) and wanting to explore how to implement these myself. The project was started mostly from scratch apart from the Camera and Shader classes, which were re-used from the Model Loader project.

I started this project by looking around at other implementations of procedural terrain generators, both minecraft styled and normal and looking at their implementations, seeing what I liked, what I didn't like and how I could use this to aid in my own generation system. From this research I found out about using a noise generator (I opted to use a value noise generator, although perlin is commonly used as well) to generate smooth height maps and other elements of the map, and got a few ideas on how to implement my own chunk loading system.

## Code overview ##

### General code overview ###

The overall code structure is as follows. Main is located in `source.cpp` and is the entry point for the application. Inside this file the glfw context is created and glew is initialised, along with the actual game object as well. The game object (`game.cpp/.h`) holds most of the running logic for the application, handling user inputs, creating the world, initialising the camera and setting up the shaders to make sure everything is rendering properly.

The world class (`world.cpp/.h`) sits inside the game object. This has two main functions which are called by the game object, `render()` and `update()`. The update function is called first, and handles checking the current camera positions during this frame and seeing if any new chunks need to be generated depending on where the user is located. The render function is called after and will call the chunk controller's `drawChunks()` function.

The chunk controller (`ChunkController.cpp/.h`) handles management and rendering of all of the chunks in the application. When the world class calls its drawChunks function it will iterate through the map of chunks and draw the ones that are in range of the player, while removing chunks that are out of the render range. When `addChunk()` is called it will create a new chunk object, starting the generation of the chunk's data and mesh. The chunk controller also holds the reference to the noise generators used by the chunks when they are generating their meshes, and provides functions to get the noise values from them.

The chunk class (`Chunk.cpp/.h`) handles storage of the chunks data, vao's and meshes. More detail on chunk generation, as well as the storage and access of chunks, will be given below.

### Specifics ###

### Noise Generation ###

As mentioned above, the noise generator (based off of [this](http://www.arendpeter.com/Perlin_Noise.html) article) is stored within the chunk controller and is accessed through the functions provided by the controller. In the case of the height generator, the noise generator takes the X and Z position of a chunk and will return an integer representing how many blocks hight that part of the chunk should be, this will always return the same value for the same X and Z coordinates to ensure that nothing gets changed when chunks are regenerated.

For each point the noise generator will get a random number, it will then get random numbers for the 8 surrounding points as well. With these points it adds the corner points together and divides them by 8, it then does the same to the sides and divides them by 4, and then it will take the original center point and divide it by 2 before adding the 3 results together to get a new value. This new value is a smoother version of the initial random number that just helps the match look a lot less rigid.
 
You then need to repeat this process for a few different points around the main point, and then perform interpolation between  them all to get a value for the point that is closer to what the surrounding blocks will be. This whole processes is then looped over a few times and averaged out (Based on the number of octaves) to produce a smooth noise map.

### Chunk Generation and Management ###
As the player moves around the world new chunks will be generated in a square around the player (8 chunks in each direction) and any chunks outside of the square will be deleted to conserve memory. Chunks are stored in an unordered map which uses a `glm::ivec2` of the chunks x and z coordinate as a key. Although it has a slightly higher memory overhead than a vector or an array due to the fact it needs to store the hash table as well, it has the advantage of having a best case time complexity of *O(1)* for reading a specific element by its coordinate as long as the hash function is good (GLM provides hash functions for its data types) and there are no collisions, whereas if I was using a vector I would have to iterate through the list to find the one with a specific coordinate. This fast access time is advantageous as chunks need to check what block is in a neighbouring chunk when they are creating their meshes and also being able to access them by their coordinate is good for knowing which chunks to render/create and which ones to discard when rendering and updating the word.

Chunk generation and rendering all happens in the `Chunk.cpp/.h` file. When a new chunk is created it starts by generating the height map, biome map and tree map. The height map and biome map are generated using a value noise generator and tree placement is decided through a few constraints and a random number. Once the maps have been generated it will generate the chunk data (Stored in a vector of 2D 16x16 arrays, with each array representing one layer of blocks) and assign a block type depending on the height of the block, what biome it is in, and whether there is supposed to be a tree there or not. After that, the chunk mesh will be generated. This function will loop through all of the blocks in the chunk and if it has no block or an air block next to it it will add a face. Once it has finished generating its mesh it will update all of the neighbouring chunks so that they can remove their side faces if they have any. This allows for much lower memory usage by the mesh and much faster render times as only the block faces that will be seen by the user are rendered (back face culling is also used).

### Memory Management ###

As the map is infinitely generating, memory management is especially important. Chunks need to be cleared when they are no longer being shown to the user and its important that they don't leave anything behind in memory. Because of this, the map of chunks stores a pointer to the chunks location on the heap, and when that pointer is removed from the map, delete is called on the chunk so that it is removed from the heap. 

As the chunk also generates other objects that are not just contained within it's object, a custom destructor is also used to clear up the VAO and associated buffers (Vertex and Element buffers). This makes sure that nothing is left in memory and stops any memory leaks from occurring, meaning the map can go on for as long as the user wants without having to worry about running out of memory (though integer overflows are still a problem and could cause issues, the "Far Lands" in Minecraft are an example of this).

### Textures ###
All of the textures for the program are stored in a texture atlas image. This has the advantage of only needing to generate and bind a texture once for the entire runtime of the program which also means less data needs to be sent in to the shader through uniforms. Once the image has been loaded block faces can get the correct data by working out the texture coordinates of their texture on the atlas image (An enum of different face types is used, which when converted to an int and multiplied by the texture size, gives the minimum x coordinate of the texture).

## Conclusions ##

In conclusion, I believe I have made a good procedural terrain generator. It can infinitely generate terrain that moves between different biomes and even adds trees to the landscape if the biome is correct. It adds and removes chunks to make sure there are no memory leaks or that the program just doesn't get too large during runtime. It also efficiently generates meshes to avoid rendering most unnecessary faces (as well as saving memory) and stores data in a way that would make it easy to regenerate chunk meshes if a feature like block breaking were to be added in to the project. 

If I were to go back and re-do this again, or just add more features to it. I would add in multi-threaded chunk generation so that the application doesn't stutter when a new chunk is being loaded. I would also like to look into combining noise functions to generate more interesting terrain and landscapes, for example, Hills and cliffs that have overhangs or rives running through some biomes. I would also like to pay more attention to C++ best practices and other technologies, such as the Rule of Three/Five and Smart Pointers, that I am more aware of now than I was when I started this module.


## [Video Report](https://www.youtube.com/watch?v=UHQrYPKpA14&feature=youtu.be) ## 

Normal link to report:  https://www.youtube.com/watch?v=UHQrYPKpA14&feature=youtu.be

Link to noise generation article: http://www.arendpeter.com/Perlin_Noise.html



# Model Loader #

## Software and Library Versions ##
* Visual Studio 2019 Community
* OpenGl 4.0 Core  
  * Nupengl.core 0.1.0.1  
* GLM 9.9.6


## How to use ##
1) Start the program
2) Enter the name of the obj or dae file that you wish to load  
   * All model (including .mtl) files must be inside the main project directory (with source.cpp, modelloader.cpp. mesh.cpp etc.)
3) Once the file has loaded you may: 
   * Move the model around using the arrow keys. <kbd>&#8592;</kbd><kbd>&#8593;</kbd><kbd>&#8594;</kbd><kbd>&#8595;</kbd> 
   * Move the camera around using <kbd>W</kbd><kbd>A</kbd><kbd>S</kbd><kbd>D</kbd> and mouse movement
   * Clear the model from the scene by pressing <kbd>C</kbd>
   * Load a new model by pressing <kbd>N</kbd>
   * Close the program by pressing <kbd>Esc</kbd>
   * If more than one model has been loaded you may change the currently selected model by pressing <kbd>Tab</kbd>
    
## Code details ##  

I tried to keep the code as "Object-Oriented" as possible, creating specialised classes for each purpose to make maintenance and extension of the project easier. I also used OOP and other considerations to avoid the use of global variables within the program.

#### Source.cpp ####

Main file of the program. Initial set up occurs here such as Window settings, OpenGl settings and also callbacks. An instance of `World` is instantiated here to handle storage of all the models and other scene related objects. Once the initial model has been loaded all of the application logic, such as calling `world.render()` or clearing/swapping the buffers, takes place inside the main while loop until `glfwWindowShouldClose()` becomes false (this happens when escape is pressed or a broken file is used).

#### World.cpp/.h ####

This class handles the storage and rendering of the models that have been loaded, as well as the performing any actions on the selected model and the camera depending on the user input. It also holds a reference to the shader and camera objects that are being used within the scene so that the models and the window can use them during rendering. 

The `render()` function handles all delta time calculations, recalculating the projection matrix when and also calling the draw function of all the models that are currently loaded. This function is called from the main program loop in source.cpp along with `processInput()` which handles all the movement related keypresses and moves the camera or models depending on what key was pressed.

#### ModelLoader.cpp/.h ####

This class handles loading the models and parsing the files that are associated with it. Its main function is `public Model loadModel(std::string path)` which returns a Model object containing all of the meshes and materials that were parsed from the file. This function will check the file extension and call the relevant parsing function (`parseObj()` or `parseDae()`), or if it is not supported, will print out that the file type isn't supported and return a blank model to the user.

This class is instantiated in the `newModel()` function in source.cpp where user input is taken and `loadModel()` is called with the input value as the parameter.

#### Model.cpp/.h and Mesh.cpp/.h ####

These two classes handle the storage of vertex/material data for each object as well as rendering the models that have been loaded in and generating their VAO's and buffers.

The model class stores a `std::vector` of meshes, as well as the model matrix of the overall object. When its draw function is called it sets the uniform values of the shader that it is able to and iterates over the mesh vector, calling the draw function for each mesh.

The mesh class stores all of the vertex data that it is made of as well as the ID's of its VAO and buffers that it generate when it is initialised. When its draw function is called it will set its shader uniform values based on the materials it uses and then bind its VAO and call `glDrawElements()` before unbinding its VAO again so the next mesh can draw.

#### Camera.cpp/.h #### 

This class handles details relating to the scenes camera. As a starting point the [LearnOpenGl](http://www.LearnOpenGl.com) camera tutorial was used to help me create it, and then it was adapted to fit the needs of the project (Removing some unnecessary parts, improving encapsulation and splitting into a header and cpp file) allowing the user to view the models from any angle. 

#### Shader.cpp/.h ####

This class handles loading, binding and creation of the shader programs. This also used [LearnOpenGl](http://www.LearnOpenGl.com) tutorials as a starting point. In the constructor all of the shader code is read in from the .frag and .vert files and then compiled into shaders and linked into a shader program, the ID of which is then stored within the object. It also features functions for setting uniform values of various types.
