package com.algorithms3.book.chapter10;

import java.util.Arrays;

public class VectorTest {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6};
//        remove(arr, 4);
//        System.out.println(Arrays.toString(arr));

        int a = 3;
        int b = 3;
//        System.out.println(arr[a--]);// 结果：4，说明是a[3]
//        System.out.println(a); // 2
//        System.out.println(arr[--b]);// 结果：3，说明是a[2]
//        System.out.println(b); // 2


        int initialCapacity = 100;


//        initialCapacity = initialCapacity | (initialCapacity >>>  1);

//        initialCapacity |= (initialCapacity >>>  1);
//        System.out.println(initialCapacity);
//        initialCapacity |= (initialCapacity >>>  2);
//        System.out.println(initialCapacity);
//        initialCapacity |= (initialCapacity >>>  4);
//        System.out.println(initialCapacity);
//        initialCapacity |= (initialCapacity >>>  8);
//        System.out.println(initialCapacity);
        initialCapacity |= (initialCapacity >>> 16);
        System.out.println(initialCapacity);

    }

    public static void remove(int[] arr, int index) {
        // index后面的元素向前移动一位
        for (int i = index; i < arr.length; i++) {
            arr[i - 1] = arr[i];
        }
        arr[arr.length - 1] = 0;
    }

}
