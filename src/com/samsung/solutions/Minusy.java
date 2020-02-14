package com.samsung.solutions;

import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/POAyCWzUB990_g4_MA4GF9Jw/site/
public class Minusy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        char lastSign = scanner.next("[+-]\\n?").charAt(0); // first sign must be '-'
        System.out.print(lastSign);
        for(int i = 1; i < n - 1; ++i) {
            char newSign = scanner.next("[+-]\\n?").charAt(0);
            if(lastSign != newSign) {
                if(lastSign == '-') // we're starting new block of '+'s
                    System.out.print('(');
                else
                    System.out.print(')'); // we're ending block of '+'s
            }
            System.out.print('-');
            lastSign = newSign;
        }
        if(lastSign == '+') // if '+'s block is ending expression, we must close the block
            System.out.print(')');
        System.out.print('\n');
    }
}