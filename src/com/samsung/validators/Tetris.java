package com.samsung.validators;

import java.util.ArrayList;
import java.util.Scanner;

public class Tetris {

    private static int N = 50000; // maximum number of colors
    private static int M = 1000000; // maximum answer size

    public static boolean validate(Scanner answer, Scanner input) throws Exception {
        int n = input.nextInt();
        ArrayList<Integer> inputStack = new ArrayList<Integer>();
        for(int i = 0; i < 2 * n; ++i)
            inputStack.add(input.nextInt());
        int correctM = solve(inputStack);
        int m = answer.nextInt();
        if(m != correctM)
            throw new Exception("Expected " + correctM + " required swaps, got " + m + ".");
        for(int i = 0; i < m; ++i) {
            if(!answer.hasNextInt())
                throw new Exception("Input ended too early.");
            int j = answer.nextInt() - 1;
            swapJAndJPlusOne(inputStack, j);
            removeMatchingNeighbours(inputStack, j);
        }

        if(inputStack.size() > 0)
            throw new Exception("This strategy doesn't remove the elements.");
        return true;
    }

    private static void removeMatchingNeighbours(ArrayList<Integer> inputStack, int j) throws Exception {
        if(j + 1 >= 0 && j + 2 < inputStack.size() && inputStack.get(j + 1).equals(inputStack.get(j + 2))) { // remove lower pair
            inputStack.remove(j + 1);
            inputStack.remove(j + 1);
        }
        for(--j; j >= 0 && j + 1 < inputStack.size() && inputStack.get(j).equals(inputStack.get(j + 1)); --j) { // now remove upper pair and all that were separated before
            inputStack.remove(j);
            inputStack.remove(j);
        }
    }

    private static void swapJAndJPlusOne(ArrayList<Integer> inputStack, int j) throws Exception {
        if(j < 0 || j > inputStack.size() - 2) // swap j and j+1 for indices in range [0, 2n - 1]
            throw new Exception("Index out of range.");
        int tmp = inputStack.get(j);
        inputStack.set(j, inputStack.get(j + 1));
        inputStack.set(j + 1, tmp);
    }

    // Since I just need number of required swaps and not indices of consecutive swaps I could do it
    // in O(nlogn) using interval tree, but I don't have time for implementing that right now.
    private static int solve(ArrayList<Integer> inputStack) {
        int n = inputStack.size() / 2; // input size
        int[] d = new int[N]; int di = 0; // stack for elements that has been processed
        int[] s = new int[2 * N]; int si = 0; // stack for elements that has not been processed
        int[] c = new int[N]; // seen, max N
        int[] a = new int[M]; // answers, max M
        int m = 0;
        for(int i = 0; i < 2 * n; ++i)
            s[2 * n - 1 - i] = inputStack.get(i) - 1;
        si = 2 * n;

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

        return m;
    }
}
