package com.samsung.solutions;

import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/noPY-IL0vsAi2TiXF-v2f5Br/site/
public class Tetris {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = 50000; // maximum number of colors
        int M = 1000000; // maximum answer size

        int n = scanner.nextInt(); // input size
        int[] d = new int[N]; int di = 0; // stack for elements that has been processed
        int[] s = new int[2 * N]; int si = 2 * n; // stack for elements that has not been processed
        int[] c = new int[N]; // seen, max N
        int[] a = new int[M]; // answers, max M
        int m = 0;
        for(int i = 0; i < 2 * n; ++i)
            s[2 * n - 1 - i] = scanner.nextInt() - 1;

        while (si > 0) {
            int x = s[--si];
            if(c[x] == 0) {
                d[di++] = x;
                c[x] = 1;
            }
            else {
                int j = di;
                int y = d[--di];
                c[y] = 0;
                if(x != y) {
                    a[m++] = j; // we're swapping j and j+1
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
