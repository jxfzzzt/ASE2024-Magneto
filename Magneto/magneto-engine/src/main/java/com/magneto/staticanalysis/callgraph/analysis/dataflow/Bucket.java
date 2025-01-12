package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import java.util.Arrays;

public class Bucket {

    private final int[] bound;
    private final int[] arr;
    private boolean isFirst;


    public Bucket(int size, int[] bound) {
        arr = new int[size];
        Arrays.fill(arr, 0);
        this.bound = bound;
        isFirst = true;
    }

    public boolean hasNext() {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != bound[i]) {
                return true;
            }
        }
        return false;
    }

    public int[] next() {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != bound[i]) {
                if (isFirst) {
                    isFirst = false;
                    break;
                }
                arr[i] += 1;
                break;
            }
        }
        return arr;
    }


}
