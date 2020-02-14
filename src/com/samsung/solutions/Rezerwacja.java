package com.samsung.solutions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/ex8U04OQav3BWcaH7wCtK-_b/site/
public class Rezerwacja {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int a[][] = new int[n][2];
        for(int i = 0; i < n; ++i) {
            a[i][0] = scanner.nextInt();
            a[i][1] = scanner.nextInt();
        }

        Arrays.sort(a, Comparator.comparingInt(o -> o[1]));
        System.out.println(solve2(a, n));
    }

    // O(nlogn) solution
    static int solve2(int a[][], int n) {
        int c[] = new int[n];
        c[0] = a[0][1] - a[0][0];
        for(int i = 1; i < n; ++i)
            c[i] = Math.max(c[i - 1], a[i][1] - a[i][0] + findBest2(a, n, c, i));
        return c[n - 1];
    }

    // O(logn) binary search
    static int findBest2(int a[][], int n, int c[], int I) {
        int l = 0, r = I, best = 0;
        while(l < r) {
            int m = (l + r) / 2;
            if(a[m][1] > a[I][0])
                r = m;
            else
                l = m + 1;
        }
        return l > 0 ? c[l - 1] : 0;
    }

    // O(n) search
    static int findBest1(int a[][], int n, int c[], int I) {
        for(int i = I - 1; i >= 0; --i)
            if(a[i][1] <= a[I][0])
                return c[i];
            return 0;
    }

    // O(n^2) solution
    static int solve1(int a[][], int n) {
        int c[] = new int[n];
        int thebest = 0;
        for(int i = 0; i < n; ++i) {
            int best = 0;
            for(int j = 0; j < n; ++j)
                if(a[j][1] <= a[i][0])
                    best = Math.max(best, c[j]);
            c[i] = a[i][1] - a[i][0] + best;
            thebest = Math.max(thebest, c[i]);
        }
        return thebest;
    }
}
