package com.book.study.ArtOfJavaConcurrencyProgramming.chapter05;

public class MainTest {

    public static void main(String[] args) {

        int a = 1;
        int b = a << 16;
        int c = b -1;
        System.out.println(a + " " + b + " " + c);

        long w = 999999999;
        System.out.println(w >>> 16);

        long x =2;

        for (int i = 1; i < 16; i++) {

            x = x*2;
        }
        System.out.println(x);


//        int state = 851969;
//
//        System.out.println("二进制："+Integer.toBinaryString(state));
//
//        System.out.println("十六进制："+Integer.toHexString(state));


    }
}
