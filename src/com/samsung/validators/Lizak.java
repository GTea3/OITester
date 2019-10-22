package com.samsung.validators;

import java.util.Scanner;

public class Lizak {
    public static boolean validate(Scanner answer, Scanner input, boolean verbose) {
        int n = input.nextInt();
        int m = input.nextInt();
        String s = input.next(".*(T|W)+");
        solve(n, s);
        int[] a = new int[n + 1];
        a[0] = 0;
        for(int i = 0; i < n; ++i)
            a[i + 1] = a[i] + (s.charAt(i) == 'T' ? 2 : 1);
        for(int i = 0; i < m; ++i) {
            int query = input.nextInt();
            if(answer.hasNextInt()) {
                int l = answer.nextInt();
                int r = answer.nextInt();
                int rangeValue = a[r] - a[l - 1];
                if(rangeValue != query) {
                    if(verbose)
                        System.out.println("\nSum for range (" + l + ", " + r + ") is equal " + rangeValue + ", not " + query + ".");
                    return false;
                }
                if(answer.hasNextLine()) // get rid of '\n'
                    answer.nextLine();
            }
            else {
                if(!answer.hasNextLine()) {
                    if(verbose)
                        System.out.println("\nNot enough query results.");
                    return false;
                }
                var ss = answer.nextLine();
                if(!ss.equals("NIE")) {
                    if(verbose)
                        System.out.println("\nUnrecognized answer: \"" + ss + "\"");
                    return false;
                }
                if (query <= sum && range[query][0] != NIL) {
                    if(verbose)
                        System.out.println("\nReturned \"NIE\" for query with existing answer.");
                    return false;
                }
            }
        }
        return true;
    }

    private static int N = 1000000;
    private static int NIL = -1;

    private static int[] lollipop = new int[N];
    private static int[][] range = new int[2 * N + 1][2];
    private static int sum;

    private static void computeRanges(int left, int right, int sum) {
        // save ranges for current sum
        range[sum][0] = left;
        range[sum][1] = right;

        // compute subranges
        while(sum >= 3) {
            // compute subranges
            if(lollipop[left] == 2) {
                left = left + 1;
            }
            else if(lollipop[right] == 2) {
                right = right - 1;
            }
            else {
                left = left + 1;
                right = right - 1;
            }
            sum = sum - 2;

            // save ranges for current sum
            range[sum][0] = left;
            range[sum][1] = right;
        }
    }

    private static void solve(int n, String s) {
        // read input, put segment costs into lollipop array
        // and compute sum of values of all segments
        sum = 0;
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
    }
}
