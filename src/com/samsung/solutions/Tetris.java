package com.samsung.solutions;

import java.util.Arrays;
import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/noPY-IL0vsAi2TiXF-v2f5Br/site/
public class Tetris {
    public static void main(String[] args) {
        final int N = 50000;  // maxiumum number of colors
        final int M = 1000000; // maximum answer size (also no higher than N * (N - 1))

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int d[] = new int[n]; int di = 0; // stack for elements that has been processed; won't contain duplicates; max size N
        int s[] = new int[2 * n]; int si = 0; // stack for elements that has not been processed; initially all elements; max size 2 * N
        boolean c[] = new boolean[n]; Arrays.fill(c, false); // set containing elements that are on stack d
        int a[] = new int[M]; int m = 0; // vector for answers (consecutive swaps)

        for(int i = 0; i < 2 * n; ++i) {
            s[2 * n - 1 - i] = scanner.nextInt();
            --s[2 * n - 1 - i];
        }
        si = 2 * n;

        // minimum number of swaps is number of inversions in primary set of data
        // an inversion is a pair of colors ordered in following way: ...x...y...x...y...
        // stack d will contain upper part with unique colors (no duplicates)
        // we will swap top element from stack d with top element from stack s (first duplicate)
        // and either remove now consecutive j and j+1 elements if they're the same color
        // or put them on stack s otherwise
        while(si > 0) {
            int x = s[--si]; // x is next element we'll be processing
            if(!c[x]) { // there is no element of color x on upper stack s, so let's put it there
                d[di++] = x;
                c[x] = true;
            } else { // x is a duplicate of an element on stack d
                int j = di; // we'll be swapping x (j+1-th element) with y, last element of d, so with j-th element
                int y = d[--di];
                c[y] = false;
                if(x != y) { // they're not of the same color so we can't get rid of them yet, let's put them back on s
                    a[m++] = j; // we're swapping j and j+1, so let's add j to the answer
                    s[si++] = y;
                    s[si++] = x;
                }
            }
        }

        System.out.println(m);
        for(int i = 0; i < m; ++i)
            System.out.println(a[i]);
    }
}
