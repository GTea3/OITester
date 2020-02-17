package com.samsung.solutions;

import java.util.Scanner;

public class SWTASamsungBonusPoint { // TODO: overall rewrite is quite needed
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for(int i = 0; i < n; ++i)
            a[i] = scanner.nextInt();
        System.out.println(solve(n, a, 0, new int[n]));
    }

    private static int solve(int n, int[] a, int step, int[] seq) {
        if(step == n) {
            if(!valid(seq))
                return 0;
            return value(seq);
        }
        int best = 0;
        for(int i = 0; i < n; ++i) // TODO: better permutation generation
            if(seq[i] == 0) {
                int[] seq2 = seq.clone();
                seq2[i] = a[step];
                best = Math.max(best, solve(n, a, step + 1, seq2));
            }
        return best;
    }

    private static boolean valid(int[] seq) {
        if(seq[0] < 0 || seq[seq.length - 1] < 0)
            return false;
        for(int i = 1; i < seq.length - 1; ++i)
            if(seq[i] < 0 && seq[i - 1] < 0)
                return false;
        return true;
    }

    private static int value(int[] seq) {
        int[] tokens = tokenize(seq);
        int[] rpn = infixToPostfix(tokens);
        return computeRPN(rpn);
    }

    private static int[] tokenize(int[] seq) { // TODO: write it to be clearer
        int[] tokens = new int[seq.length];
        int n = 0;
        int value = 0;
        for(int i = 0; i < seq.length; ++i) {
            if (seq[i] < 0) {
                tokens[n++] = value;
                value = 0;
                tokens[n++] = seq[i];
            } else {
                value = value * 10 + seq[i];
            }
        }
        tokens[n++] = value;

        if(n < tokens.length) {
            int[] tokens2 = new int[n];
            for(int i = 0; i < n; ++i)
                tokens2[i] = tokens[i];
                tokens = tokens2;
        }
        return tokens;
    }

    private static int[] infixToPostfix(int[] tokens) { // TODO: write it to be clearer
        int[] rpn = new int[tokens.length];
        int rpni = 0;
        int[] stack = new int[tokens.length];
        int si = 0;
        for(int i = 0; i < tokens.length; ++i) {
            if(tokens[i] > 0)
                rpn[rpni++] = tokens[i];
            else {
                // we can put an operand on the stack if stack is empty or it's precedence is higher than last operand on the stack
                while(si != 0 && !isHigherPrecedence(tokens[i], stack[si - 1]))
                    rpn[rpni++] = stack[--si];
                stack[si++] = tokens[i];
            }
        }
        while(si > 0)
            rpn[rpni++] = stack[--si];
        return rpn;
    }

    private static boolean isHigherPrecedence(int a, int b) {
        return (a == -3 || a == -4) && (b == -1 || b == -2); // a has higher precedence than b
    }

    private static int computeRPN(int[] rpn) { // TODO: write it to be clearer
        int[] stack = new int[rpn.length];
        int si = 0;
        for (int i = 0; i < rpn.length; ++i) {
            if (rpn[i] > 0)
                stack[si++] = rpn[i];
            else {
                int b = stack[--si];
                int a = stack[--si];
                switch (rpn[i]) {
                    case -1: // +
                        stack[si++] = a + b;
                        break;
                    case -2: // -
                        stack[si++] = a - b;
                        break;
                    case -3: // *
                        stack[si++] = a * b;
                        break;
                    case -4: // /
                        stack[si++] = a / b;
                        break;
                }
            }
        }
        return stack[0];
    }
}
