#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "win-gettimeofday.h"

#define ELEMENTS 30000000
#define RANGE 1000


// Takes in a pointer to the array that needs checking
// Returns a boolean indicating whether or not the array is sorted.
//
// Function will iterate over the array and for element in the array it will 
// check that it is not less thatn the element before it. If it is, then it will return false
// and if it makes it to the end of the array, it will return true.
bool succesfullySorted(float* sorted)
{
	for (int i = 1; i < ELEMENTS; i++)
	{
		if (sorted[i] < sorted[i - 1])
			return false;
	}
	return true;
}

// Takes in an array of values, a temporary array to store values while it is sorting. As well
// as three pointers to the start, middle and end of the array section you want to merge.
// Returns nothing, but will modify the two arrays that were passed in.
//
// This function will merge two sorted "arrays" (First array from start to mid, second array is
// from mid + 1 to end).
void merge(float* values, float* temp, int start, int mid, int end)
{

	int startPoint = start;
	int midPoint = mid + 1;
	int index = start;

	while (startPoint <= mid && midPoint <= end)
	{
		if (values[startPoint] < values[midPoint])
		{
			temp[index++]= values[startPoint++];
		}
		else
		{
			temp[index++] = values[midPoint++];
		}
	}
	while (startPoint <= mid)
	{
		temp[index++] = values[startPoint++];
	}

	while (midPoint <= end)
	{
		temp[index++] = values[midPoint++];
	}

	for (int i = start; i <= end; i++)
	{
		values[i] = temp[i];
	}
}

// Takes in a pointer to the array (arr) and the length of that array (n)
// Returns nothing, but modifies the array that was passed in.
//
// This is a bottom up merge sort, it will iteratively merge arrays of size 1 to get sorted 
// arrays of size 2 then merge arrays of size 2 to get sorted arrays of size 4 etc.
void mergeSort(float* arr, int n)
{
	int size;
	int start;
	n--;

	float* temp;
	temp = (float*)malloc(ELEMENTS * sizeof(float));
	
	for (size = 1; size < n * 2; size *= 2)
	{
		for (start = 0; start <= n; start += (size * 2))
		{
			int mid = fmin(start + size - 1, n);
			int end = fmin(start + (2 * size) - 1, n);
			merge(arr, temp, start, mid, end);
		}
	}

	free(temp);
}



int main()
{
	float* h_array;
	h_array = (float*)malloc(ELEMENTS * sizeof(float));

	//Generate an array of random floats
	printf("Generating array \n");
	for (int i = 0; i < ELEMENTS; i++)
	{
		h_array[i] = ((float)rand() / RAND_MAX) * RANGE;	//Gen random floating point numbers up to RANGE
	}
	printf("Array Made \n\n");

	float start, end, total;
	printf("Starting sort \n");

	//Start of timed section
	start = get_current_time();
	mergeSort(h_array, ELEMENTS);
	end = get_current_time();
	total = end - start;
	//End of timed section

	printf("Sort complete. Time taken: %f  \n\n\n\n\n\n\n", total);

	//for (int i = 0; i < ELEMENTS; i++)
	//	printf("%d: %f \n", i, h_array[i]);

	if (succesfullySorted(h_array))
		printf("Good sort \n");
	else
		printf("Bad sort \n");

	free(h_array);
	return 0;
}