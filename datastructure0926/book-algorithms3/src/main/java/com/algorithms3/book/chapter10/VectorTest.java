package com.algorithms3.book.chapter10;

import java.util.Arrays;

public class VectorTest {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6};
        remove(arr, 4);
        System.out.println(Arrays.toString(arr));

    }

    public static void remove(int[] arr, int index) {
        // index后面的元素向前移动一位
        for (int i = index; i < arr.length; i++) {
            arr[i - 1] = arr[i];
        }
        arr[arr.length - 1] = 0;
    }

}
