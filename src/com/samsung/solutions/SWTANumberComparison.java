package com.samsung.solutions;

import java.util.Scanner;

public class SWTANumberComparison {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] b = new int[n];
        int[] c = new int[n];
        int[] d = new int[n];
        for(int i = 0; i < n; ++i) {
            b[i] = scanner.nextInt();
            c[i] = scanner.nextInt();
            d[i] = scanner.nextInt();
        }
        System.out.println(solve(n, b, c, d));
    }

    private static int solve(int n, int[] b, int[] c, int[] d) {
        int count = 0;
        for(int a = 10000; a <= 99999; ++a)
            if(correct(n, b, c, d, a))
                ++count;
        return count;
    }

    private static boolean correct(int n, int[] b, int[] c, int[] d, int a) {
        for(int i = 0; i < n; ++i)
            if(!correct(a, b[i], c[i], d[i]))
                return false;
        return true;
    }

    private static boolean correct(int a, int b, int c, int d) {
        return matchingPlaces(a, b) == c && matchingDigits(a, b) - c == d;
    }

    private static int matchingPlaces(int a, int b) {
        int count = 0;
        while(a > 0 || b > 0) {
            if(a % 10 == b % 10)
                ++count;
            a /= 10;
            b /= 10;
        }
        return count;
    }

    private static int matchingDigits(int a, int b) {
        int[] d = new int[10];
        while(a > 0) {
            ++d[a % 10];
            a /= 10;
        }
        int count = 0;
        while(b > 0) {
            if(d[b % 10] > 0) {
                --d[b % 10];
                ++count;
            }
            b /= 10;
        }
        return count;
    }
}
