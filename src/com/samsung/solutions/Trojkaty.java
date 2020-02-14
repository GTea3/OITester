package com.samsung.solutions;

import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/UU2Uj-barjiONnRxd9aEVoDj/site/
public class Trojkaty {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int d[] = new int[n];

        // d[i - 1] will contain number of grey edges coming out of vertex i
        for(int i = 0; i < m; ++i) {
            ++d[scanner.nextInt() - 1];
            ++d[scanner.nextInt() - 1];
        }

        // s will contain number of varicoloured triangles
        int s = 0;
        for(int i = 0; i < n; ++i)
            s += d[i] * (n - 1 - d[i]);
        s /= 2;

        // if we substract number of varicoloured traingles from number of all triangles
        // we get number of monochrome triangles which is our answer
        System.out.println((n * (n - 1) * (n - 2)) / 6 - s);
    }
}
