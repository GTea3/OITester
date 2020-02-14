package com.samsung.solutions;

import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/fYXVXOreVxlXTRoHZJXyXF2l/site/
public class Krazki {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[] r = new int[300000];
        r[0] = scanner.nextInt();
        for(int i = 1; i < n; ++i)
            r[i] = Math.min(scanner.nextInt(), r[i - 1]);
        int h = n;
        for(int i = 0; i < m; ++i) {
            int k = scanner.nextInt();
            while(--h >= 0 && r[h] < k)
                continue;
        }
        System.out.println(h > -1 ? h + 1 : 0);
    }
}
