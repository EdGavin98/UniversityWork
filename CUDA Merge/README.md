# Overview #
## Final Grade - 62% ##

This project was undertaken for a parallel computation module. It involved implementing the common Merge Sort algorithm on a GPU using CUDA. The project was incredibly interesting and a lot was learned about how to write efficient and fast code for a GPU.

There are 3 folders in this section:
1) MergeSort-CUDA
    * This was the first implementation of the merge sort in CUDA, it was much, much slower than the serial implementation due to inefficient usage of the GPU's manycore SIMT architecture, and improvements were later made.
2) MergeSort-Serial
    * This was the serial implementation of the merge sort algorithm, it's an iterative bottom-up merge sort as opposed to a recursive one.
3) MergeSortCoRank
    * This was the final implementation of the merge sort in CUDA, it achieved a speedup of 8x at 30 million elements when compared to the serial implementation and was much, much faster than the original CUDA version. It uses a binary search based co-ranking algorithm in order to split up the merge section of the algorithm among different threads in order to achieve speedup in the later stages of sorting when there are fewer arrays to merge.
    
As well as the code, the final report for this project can be seen within the repository.
