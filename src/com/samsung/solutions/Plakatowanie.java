package com.samsung.solutions;

import java.util.Scanner;
import java.util.Stack;

public class Plakatowanie {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        Stack<Integer> s = new Stack<>();
        int result = 0;

        while(n-- > 0) {
            scanner.nextInt();
            int h = scanner.nextInt();
            if(s.empty() || h > s.peek())
                s.push(h);
            else if(h < s.peek()) {
                while(!s.empty() && h < s.peek()) {
                    s.pop();
                    ++result;
                }
                if(s.empty() || h > s.peek())
                    s.push(h);
            }
        }
        result += s.size();

        System.out.println(result);
    }
}
