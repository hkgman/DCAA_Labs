package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelMergeSort1 {

    public SortResult sort (int[] array,int countThread) throws Exception {
        long startTime = System.currentTimeMillis();
        int[] result = parallelMergeSort(array,countThread);
        long elapsedTime = System.currentTimeMillis() - startTime;
        SortResult sortResult = new SortResult(result,elapsedTime);
        return  sortResult;

    }
    public int[] parallelMergeSort(int[] array,int countThread) throws Exception
    {
        if(countThread==1)
        {
            return mergeSort(array);
        }
        int mid = array.length/2;
        int[] left = new int[mid];
        int [] right = new int[array.length-mid];
        System.arraycopy(array,0,left,0,mid);
        System.arraycopy(array,mid,right,0,array.length-mid);
        int remainThread = countThread / 2;
        ExecutorService executor = Executors.newFixedThreadPool(remainThread);


        Future<int[]> leftFuture = executor.submit(() -> parallelMergeSort(left, remainThread));
        Future<int[]> rightFuture = executor.submit(() -> parallelMergeSort(right, remainThread));

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        int[] leftSorted = leftFuture.get();
        int[] rightSorted = rightFuture.get();

        return merge(leftSorted, rightSorted);
    }
    public int[] mergeSort(int[] array)
    {
        if (array.length <= 1) {
            return array;
        }
        int mid = array.length/2;
        int[] left = new int[mid];
        int [] right = new int[array.length-mid];

        System.arraycopy(array,0,left,0,mid);
        System.arraycopy(array,mid,right,0,array.length-mid);

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left,right);
    }

    public int[] merge (int[] left,int[] right)
    {
        int i=0,k=0,j =0;
        int[] result = new int[left.length+right.length];
        while (i<left.length & j<right.length)
        {
            if(left[i]<right[j])
            {
                result[k] = left[i];
                k++;
                i++;
            }else{
                result[k]=right[j];
                k++;
                j++;
            }
        }
        while(i<left.length)
        {
            result[k]=left[i];
            i++;
            k++;
        }
        while(j<right.length)
        {
            result[k] = right[j];
            j++;
            k++;
        }
        return result;
    }
}
