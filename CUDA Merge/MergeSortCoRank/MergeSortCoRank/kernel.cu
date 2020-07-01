#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include <cuda.h>
#include <cuda_runtime.h>
#include <device_functions.h>
#include <device_launch_parameters.h>
#include "win-gettimeofday.h"

#define ELEMENTS 100000
#define RANGE 1000
#define NUM_THREADS 1024
#define TILE_SIZE 4096
#define min(a,b) (a < b ? a : b)
#define max(a,b) (a > b ? a : b)

// Takes in a pointer to the array the needs checking.
// Returns an integer value indicating if it has been sorted or not (-1 if sorted, if not sorted it returns the index of the first unsorted element).
//
// Function will iterate over the array and for each element in the array it will
// check that it is not less than the element before it. If it is, then it will return false
// and if it makes it to the end of the array then it will return true.
int succesfullySorted(float* sorted)
{
	for (int i = 1; i < ELEMENTS; i++)
	{
		if (sorted[i] < sorted[i - 1])
			return i;
	}
	return -1;
}

// Takes in a pointer to an array
// Returns nothing, but modifies the array that was passed in.
//
// This function iterates over the array that has been passed in up till the ELEMENTS limit, defined at the top of this file.
// For each element in the array it will insert a random float value between 0 and the number defined in RANGE.
void genRandomArray(float* arr)
{
	printf("Generating array \n");
	for (int i = 0; i < ELEMENTS; i++)
	{
		arr[i] = ((float)rand() / (float)RAND_MAX) * RANGE;	//Gen random floating point numbers up to RANGE
	}
	printf("Array Made \n\n");
}


// Takes in three pointers to arrays and two integer values. Pointers A and B are the two arrays that need to
// merged together and C is the array that they shall be merged in to. Integers m and n are the lengths of 
// A and B respectively, length of C is assumed to be m + n.
//
// Returns nothing, but modifies C.
//
// This function performs a merge operation on arrays A and B, and merges them into C.
__device__
void merge(const float* A, int m, const float* B, int n, float* C) {
	int aStart = 0;
	int bStart = 0;
	int outputIndex = 0;
	while ((aStart < m) && (bStart < n))
	{
		if (A[aStart] <= B[bStart])
			C[outputIndex++] = A[aStart++];
		else
			C[outputIndex++] = B[bStart++];
	}
	if (aStart == m)
		for (; bStart < n; bStart++)
			C[outputIndex++] = B[bStart];
	else
		for (; aStart < m; aStart++)
			C[outputIndex++] = A[aStart];
}

// Takes in 3 integers and 2 array pointers. A and B are the arrays that need to be searched, and integers m 
// and n are their lengths. Integer i is the index at which the current thread is going to begin or end it's output.
// Returns an integer 'j' specifying the co rank of one of the elements in the array.
//
// This function uses the input index provide to work out the co ranks of this index in arrays A and B using a binary search.
// It will then return the indexs co rank in array A, and the co rank in array B can then be worked out by doing i - j;
__device__
int coRank(int i, float* A, int m, float* B, int n)
{
	int j = min(i, m);
	int k = i - j;
	int j_low = max(0, i - n);
	int k_low;
	int delta;
	while (true)
	{
		if (j > 0 && k < n && A[j - 1] > B[k])
		{
			delta = ((j - j_low + 1) >> 1);
			k_low = k;
			j = j - delta;
			k = k + delta;
		}
		else if (k > 0 && j < m && B[k - 1] >= A[j])
		{
			delta = ((k - k_low + 1) >> 1);
			j_low = j;
			j = j + delta;
			k = k - delta;
		}
		else
		{
			break;
		}
	}
	return j;
}

// Takes in 3 array pointers, A, B and C and two integers representing the length of A and B (Length of C is m+n)
// Returns nothing, but modifies the array C.
//
// This function will use the co_rank function to divide up the array amongst all of the threads within the block.
// It will then call the merge function so that each thread can merge its section of the array into the output array C.
__device__
void coRankMerge(float* A, int m, float* B, int n, float* C)
{
	int sectionSize = ceil((double)(m + n) / blockDim.x); 
	int outputStart = min(threadIdx.x * sectionSize, m + n);
	int outputEnd = min((threadIdx.x + 1) * sectionSize, m + n);

	int aThreadStart = coRank(outputStart, A, m, B, n);
	int aThreadEnd = coRank(outputEnd, A, m, B, n);

	int bThreadStart = outputStart - aThreadStart;
	int bThreadEnd = outputEnd - aThreadEnd;

	merge(&A[aThreadStart], aThreadEnd - aThreadStart, &B[bThreadStart], bThreadEnd - bThreadStart, &C[outputStart]);
}

// Takes in 2 array pointers d_array and d_temp, and the current width of the sorted arrays.
// Returns nothing, but modifies the arrays that are passed in.
//
// This function will determine the start and end index of each pair of arrays that needs to be merged and assign it to a thread block
// it will then call the coRankMerge function so that they can begin the merge operation.
// This function does not copy the sorted arrays back to d_array, it will be necessary to swap the pointers before the next kernel launch.
__global__
void mergeCuda(float* d_array, float* d_temp, int width)
{
	int start = blockIdx.x * width;
	int end = min(start + width - 1, ELEMENTS - 1);
	int mid = min(start + (width >> 1), ELEMENTS - 1);

	coRankMerge(&d_array[start], mid - start, &d_array[mid], end - mid + 1, &d_temp[start]);
}


// Takes in three array pointers for the Input arrays A and B, and the output array C. As well as 
// integer values m and n, representing the length of A and B respectively. 
// Returns nothing, but modifies the output array C.
//
// This function uses the co ranking algorithm to split up the array into blocks that can fit inside
// shared memory, it will then split up each of those tiles among the threads in the block so that they can be merged.
__device__
void coRankTiledMerge(float* A, int m, float* B, int n, float* C, int blocksPerArray)
{
	__shared__ float sharedAB[TILE_SIZE << 1];
	float* sharedA = &sharedAB[0];
	float* sharedB = &sharedAB[TILE_SIZE];

	int segSize = ceil(((double)(m + n) / blocksPerArray));

	int outBlockStart = min((blockIdx.x % blocksPerArray) * segSize, m + n);
	int outBlockEnd = min(((blockIdx.x % blocksPerArray) + 1) * segSize, m + n);

	//Calculate the co rank values for the whole block
	//Only need one thread as it can be put in shared memory
	if (threadIdx.x == 0)
	{
		sharedA[0] = coRank(outBlockStart, A, m, B, n);
		sharedA[1] = coRank(outBlockEnd, A, m, B, n);
	}

	__syncthreads();
													 
	int aBlockStart = sharedA[0];							 
	int aBlockEnd = sharedA[1];								 
																 
	int bBlockStart = outBlockStart - aBlockStart;			 
	int bBlockEnd = outBlockEnd - aBlockEnd;				 
																 
	__syncthreads();											 
																 
	int aChunkSize = aBlockEnd - aBlockStart;			 
	int bChunkSize = bBlockEnd - bBlockStart;					 
	int cChunkSize = outBlockEnd - outBlockStart;				

	//Load values into shared memory
	for (int i = 0; i < TILE_SIZE; i += blockDim.x) //Increase i by blockDim.x so all values are next to each other when being loaded, allowing for memory coalescing
	{
		int index = i + threadIdx.x;
		if (index < aChunkSize)
			sharedA[index] = A[aBlockStart + index];
		if (index < bChunkSize)
			sharedB[index] = B[bBlockStart + index];
	}

	__syncthreads();

	int outputThreadStart = min(threadIdx.x * (TILE_SIZE / blockDim.x), cChunkSize);
	int outputThreadEnd = min((threadIdx.x + 1) * (TILE_SIZE / blockDim.x), cChunkSize);

	int aThreadStart = coRank(outputThreadStart, sharedA, min(TILE_SIZE, aChunkSize), sharedB, min(TILE_SIZE, bChunkSize));
	int aThreadEnd = coRank(outputThreadEnd, sharedA, min(TILE_SIZE, aChunkSize), sharedB, min(TILE_SIZE, bChunkSize));

	int bThreadStart = outputThreadStart - aThreadStart;
	int bThreadEnd = outputThreadEnd - aThreadEnd;

	merge(&sharedA[aThreadStart], aThreadEnd - aThreadStart, &sharedB[bThreadStart], bThreadEnd - bThreadStart, &C[outBlockStart + outputThreadStart]);

}

// Takes in two array pointers, d_array and d_temp, the integer value width, representing the current width of the sorted arrays
// and also the integer value blocksPerArray, representing the number of blocks that will be tiled across the arrays.
// Returns nothing, but modifies the array d_temp.
//
// This function will determine the start and end index for each array pair that needs to be merged, and will then assign each block to
// an array depending on its index.
// 
// This function does not copy the sorted arrays back to d_array, it is necessary to swap the pointers before the next kernel launch.
__global__
void mergeCudaTiled(float* d_array, float* d_temp, int width, int blocksPerArray)
{
	int arrayNumber = blockIdx.x / blocksPerArray;
	int start = arrayNumber * width;
	int end = min(start + width - 1, ELEMENTS - 1);
	int mid = min(start + (width / 2) - 1, ELEMENTS - 1);

	coRankTiledMerge(&d_array[start], (mid - start) + 1, &d_array[mid + 1], end - mid, &d_temp[start], blocksPerArray);
}

// Takes in the array that needs to be sorted
// Returns nothing, but modifies the array that was passed in.
//
// This function acts as a wrapper for all of the cuda functionality, such as allocating, copying and freeing memory on the device, as well as
// handling all of block and grid size calculations and launching the kernel.
void cudaMergeSortTiled(float* arr)
{
	int arrayMemorySize = ELEMENTS * sizeof(float);
	float* d_temp, * d_array;
	cudaMalloc(&d_temp, arrayMemorySize);
	cudaMalloc(&d_array, arrayMemorySize);
	cudaMemcpy(d_array, arr, arrayMemorySize, cudaMemcpyHostToDevice);

	for (int width = 2; width < 2 * ELEMENTS; width *= 2)
	{
		int numBlocks = 0;
		int numBlocksPerArray = 1;

		if (width > TILE_SIZE)
		{
			int numSegments = ceil((double)ELEMENTS / width);
			numBlocksPerArray = width / TILE_SIZE;
			numBlocks = numBlocksPerArray * numSegments;
		}
		else
		{
			numBlocks = ceil((double)ELEMENTS / (double)width);
		}

		int threads = min(width, NUM_THREADS);

		dim3 gridDim(numBlocks, 1, 1);
		dim3 blockDim(threads, 1, 1);

		//Time spent allocating and storing into shared memory is long when there are large numbers of arrays as each needs at least 1 block
		//so use global for the first few iterations
		if (width <= 128)
			mergeCuda<<<gridDim, blockDim>>>(d_array, d_temp, width);
		else
			mergeCudaTiled<<<gridDim, blockDim >>>(d_array, d_temp, width, numBlocksPerArray);

		float* swap = d_array;
		d_array = d_temp;
		d_temp = swap;
	}
	cudaMemcpy(arr, d_array, arrayMemorySize, cudaMemcpyDeviceToHost);
	cudaDeviceSynchronize();
	cudaFree(d_temp);
	cudaFree(d_array);
}

// Takes in the array that needs to be sorted
// Returns nothing, but modifies the array that was passed in.
//
// This function acts as a wrapper for all of the cuda functionality, such as allocating, copying and freeing memory on the device, as well as
// handling all of block and grid size calculations and launching the kernel.
void cudaMergeSort(float* arr)
{
	int arrayMemorySize = ELEMENTS * sizeof(float);
	float* d_temp, * d_array;
	cudaMalloc(&d_temp, arrayMemorySize);
	cudaMalloc(&d_array, arrayMemorySize);
	cudaMemcpy(d_array, arr, arrayMemorySize, cudaMemcpyHostToDevice);

	for (int width = 2; width < 2 * ELEMENTS; width *= 2)
	{
		
		int numBlocks = ceil((double)ELEMENTS / (double)width);
		int threads = min(width, NUM_THREADS);
		dim3 gridDim(numBlocks, 1, 1);
		dim3 blockDim(threads, 1, 1);

		mergeCuda << <gridDim, blockDim >> > (d_array, d_temp, width);

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
	cudaMergeSortTiled(h_array); //Sort with tiling
	//cudaMergeSort(h_array);    //Sort without tiling
	timeEnd = get_current_time();
	timeTotal = timeEnd - timeStart;
	printf("Time taken: %f \n", timeTotal);
	//End of timed section

	//Print the sorted array
	//for (int i = 0; i < ELEMENTS; i++)
	//	printf("%d: %f \n", i, h_array[i]);

	int isSorted = succesfullySorted(h_array);
	if (isSorted == -1)
		printf("Good sort \n");
	else
		printf("Bad sort: %d \n", isSorted);

	free(h_array);
	
	return 0;
}