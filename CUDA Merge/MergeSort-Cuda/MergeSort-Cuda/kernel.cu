#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include <cuda.h>
#include <cuda_runtime.h>
#include <device_functions.h>
#include <device_launch_parameters.h>

#include "win-gettimeofday.h"

#define ELEMENTS 10000000
#define RANGE 1000
#define NUM_THREADS 1024
#define min(a,b) (a<b?a:b)


// Returns an integer representing the threads global index
//
// This function calculates the global index of the current thread
__device__ int getIdx()
{
	return blockIdx.x * blockDim.x + threadIdx.x;
}

// Takes in a pointer to the array that needs checking
// Returns a boolean indicating whether or not the array is sorted.
//
// Function will iterate over the array and for element in the array it will 
// check that it is not less thatn the element before it. If it is, then it will return false
// and if it makes it to the end of the array, it will return true.
bool succesfullySorted(float* sorted)
{
	for (int i = 1; i < ELEMENTS; i++)
		if (sorted[i] < sorted[i - 1])
			return false;
	return true;
}

// Takes in a pointer to an array
// Returns nothing, bur modifies the array that was passed in.
//
// This function iterates over the array that has been passed in up until the ELEMENTS limit which is defined at the top of this file.
// For each element in the array it will insert a random float value between and the number defined in RANGE.
void genRandomArray(float* arr)
{
	printf("Generating array \n");
	for (int i = 0; i < ELEMENTS; i++)
	{
		arr[i] = ((float)rand() / (float)RAND_MAX) * RANGE;	//Gen random floating point numbers up to 10
	}
	printf("Array Made \n\n");
}

// Takes in two array pointers and two integer values. The pointer d_array is the array that needs to be sorted, and the pointer d_temp
// is the array that the values are stored in after sorting. The integer value width is the current width of the sorted arrays and numSegments
// is the number of segments the array is currently split into.
// Returns nothing, but modifies d_temp. 
//
// This function will assign each thread 2 arrays to merge, and then that thread will merge them into one larger sorted array in d_temp.
// 
// This function does not copy the merge results back to d_array from d_temp. The pointers to the arrays need to be swapped before launching the next kernel.
__global__
void mergeCuda(float* d_array, float* d_temp, int width, int numSegments)
{
	int globalIndex = getIdx();

	if (globalIndex > numSegments)
		return;

	int start = globalIndex * width;
	int end = min(start + width - 1, ELEMENTS - 1);
	int mid = min(start + (width / 2) - 1, ELEMENTS - 1);

	int startPoint = start;
	int midPoint = mid + 1;
	int index = start;

	while (startPoint <= mid && midPoint <= end)
	{
		if (d_array[startPoint] < d_array[midPoint])
			d_temp[index++] = d_array[startPoint++];
		else
			d_temp[index++] = d_array[midPoint++];
	}
	while (startPoint <= mid)
		d_temp[index++] = d_array[startPoint++];
	while (midPoint <= end)
		d_temp[index++] = d_array[midPoint++];
}

// Takes in a pointer to the array that needs to be sorted.
// Returns nothing, but modifies the array that has been passed in.
//
// This function acts as a wrapper for all of the cuda specific parts of the merge sort, such as 
// allocating/copying memory and freeing the memory on the device. As well as determing grid and block sizes and launching the kernels.
void cudaMergeSort(float* arr)
{
	int arrayMemorySize = ELEMENTS * sizeof(float);
	float* d_temp, * d_array;
	cudaMalloc(&d_temp, arrayMemorySize);
	cudaMalloc(&d_array, arrayMemorySize);
	cudaMemcpy(d_array, arr, arrayMemorySize, cudaMemcpyHostToDevice);

	for (int width = 2; width < 2 * ELEMENTS; width *= 2)
	{
		int numSegments = ceil((double)ELEMENTS / (double)width);
		int numBlocks = ceil((double)numSegments / (double)NUM_THREADS);
		dim3 gridDim(numBlocks, 1, 1);
		dim3 dimBlock(NUM_THREADS, 1, 1);
		mergeCuda << <gridDim, dimBlock >> > (d_array, d_temp, width, numSegments);

		float* swap = d_array;
		d_array = d_temp;
		d_temp = swap;

	}

	cudaMemcpy(arr, d_array, arrayMemorySize, cudaMemcpyDeviceToHost);

	cudaDeviceSynchronize();
	cudaFree(d_temp);
	cudaFree(d_array);
}

int main()
{


	float* h_array;
	h_array = (float*)malloc(ELEMENTS * sizeof(float));
	genRandomArray(h_array);

	//Print the initial array
	//for (int i = 0; i < ELEMENTS; i++)
	//	printf("%f \n", h_array[i]);

	//Beginning of timed section
	float timeStart, timeEnd, timeTotal;
	timeStart = get_current_time();
	cudaMergeSort(h_array);
	timeEnd = get_current_time();
	timeTotal = timeEnd - timeStart;
	//Ending of timed section

	//Print the array
	//for (int i = 0; i < ELEMENTS; i++)
	//	printf("%d: %f \n", i, h_array[i]);

	printf("Time taken: %f \n", timeTotal);

	if (succesfullySorted(h_array))
		printf("Good sort \n");
	else
		printf("Bad sort \n");

	cudaDeviceSynchronize();
	free(h_array);
	return 0;
}
