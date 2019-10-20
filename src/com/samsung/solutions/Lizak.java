package com.samsung.solutions;

import java.util.Scanner;

public class Lizak {
    private static int N = 1000000;
    private static int NIL = -1;

    private static int[] lollipop = new int[N];
    private static int[][] range = new int[2 * N + 1][2];

    private static void computeRanges(int left, int right, int sum) {

        // save ranges for current sum
        range[sum][0] = left;
        range[sum][1] = right;

        // compute subranges
        if(sum >= 3) {
            if(lollipop[left] == 2)
                computeRanges(left + 1, right, sum - 2);
            else if(lollipop[right] == 2)
                computeRanges(left, right - 1, sum - 2);
            else
                computeRanges(left + 1, right - 1, sum - 2);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int sum = 0;

        // read input, put segment costs into lollipop array
        // and compute sum of values of all segments
        String s = scanner.next(".*(T|W)+");
        for(int i = 0; i < n; ++i) {
            char a = s.charAt(i);
            lollipop[i] = a == 'W' ? 1 : 2;
            sum += lollipop[i];
        }

        // compute ranges for all possible even values if sum is even or odd values if sum is odd
        for(int i = 0; i < 2 * n + 1; ++i)
            range[i][0] = NIL; // a "range not existing" value
        computeRanges(0, n - 1, sum);

        // find first and last 1s
        int firstOne = NIL, lastOne = NIL;
        for(int i = 0; i < n; ++i) {
            if(lollipop[i] == 1) {
                if(firstOne == NIL)
                    firstOne = i;
                lastOne = i;
            }
        }

        // compute ranges for all possible odd values is sum is even or even values if sum is odd
        if(firstOne != NIL && lastOne < n - firstOne - 1) // cut out segments until after first 1 if it's closer to left edge than last 1 to right edge
            computeRanges(firstOne + 1, n - 1, sum - 2 * firstOne - 1);
        else if(lastOne != NIL) // cut out segment from last 1 till the end otherwise
            computeRanges(0, lastOne - 1, sum - 2 * (n - lastOne - 1) - 1);

        // answer queries
        for(int i = 0; i < m; ++i) {
            int q = scanner.nextInt();
            if(q > sum || range[q][0] == NIL)
                System.out.println("NIE");
            else
                System.out.println((range[q][0] + 1) + " " + (range[q][1] + 1));
        }
    }
}

