package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        int[] array = {3, 2, 6, 9, 2, 3, 5, 8, 2, 1, 11};

        // Сортировка с использованием обычной Merge Sort
        ParallelMergeSort mergeSort = new ParallelMergeSort();
        long startTime = System.currentTimeMillis();
        int[] sortedArray = mergeSort.mergeSort(array.clone()); // Используем clone, чтобы не изменять исходный массив
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println("Sorted with mergeSort: " + Arrays.toString(sortedArray));
        System.out.println("Time taken: " + elapsedTime + " ms");

        // Сортировка с использованием Parallel Merge Sort
        int countThreads = 5; // Пример количества потоков
        SortResult result = mergeSort.sort(array.clone(), countThreads);
        System.out.println("Sorted with ParallelMergeSort: " + Arrays.toString(result.result));
        System.out.println("Time taken: " + result.timeResult + " ms");
    }
}