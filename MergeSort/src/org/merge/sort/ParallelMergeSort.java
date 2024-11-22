package org.merge.sort;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSort {

    public SortResult sort(int[] array, int countThread) throws Exception {
        long startTime = System.currentTimeMillis();
        int[] result = parallelMergeSort(array, countThread);
        long elapsedTime = System.currentTimeMillis() - startTime;
        return new SortResult(result, elapsedTime);
    }

    public int[] parallelMergeSort(int[] array, int countThread) throws Exception {
        if (array.length <= 1 || countThread == 1) {
            return mergeSort(array);
        }

        ForkJoinPool pool = new ForkJoinPool(countThread);
        MergeSortTask task = new MergeSortTask(array,countThread);
        return pool.invoke(task);
    }

    private static class MergeSortTask extends RecursiveTask<int[]> {
        private final int[] array;
        private final int countThread;

        public MergeSortTask(int[] array, int countThread) {
            this.array = array;
            this.countThread = countThread;
        }

        @Override
        protected int[] compute() {
            if (array.length <= 1) {
                return array;
            }

            // Печать текущего состояния массива

            int mid = array.length / 2;
            int[] left = new int[mid];
            int[] right = new int[array.length - mid];

            System.arraycopy(array, 0, left, 0, mid);
            System.arraycopy(array, mid, right, 0, array.length - mid);

            MergeSortTask leftTask = new MergeSortTask(left, countThread > 1 ? countThread / 2 : 1);
            MergeSortTask rightTask = new MergeSortTask(right, countThread > 1 ? countThread / 2 : 1);

            leftTask.fork();
            rightTask.fork();

            int[] rightSorted = rightTask.join();
            int[] leftSorted = leftTask.join();


            return merge(leftSorted, rightSorted);
        }
    }

    public int[] mergeSort(int[] array) {
        if (array.length <= 1) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];

        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    public static int[] merge(int[] left, int[] right) {
        int i = 0, k = 0, j = 0;
        int[] result = new int[left.length + right.length];
        while (i < left.length & j < right.length) {
            if (left[i] < right[j]) {
                result[k] = left[i];
                k++;
                i++;
            } else {
                result[k] = right[j];
                k++;
                j++;
            }
        }
        while (i < left.length) {
            result[k] = left[i];
            i++;
            k++;
        }
        while (j < right.length) {
            result[k] = right[j];
            j++;
            k++;
        }
        return result;
    }
}