package com.samsung.validators;

import java.util.Scanner;

public class Lizak {
    public static boolean validate(Scanner answer, Scanner input, boolean verbose) {
        int n = input.nextInt();
        int m = input.nextInt();
        String s = input.next(".*(T|W)+");
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
                        System.out.println("Sum for range (" + l + ", " + r + ") is equal " + rangeValue + ", not " + query + ".");
                    return false;
                }
            }
            else {
                answer.nextLine();
                if(!answer.hasNextLine()) {
                    if(verbose)
                        System.out.println("Not enough query results.");
                    return false;
                }
                var ss = answer.nextLine();
                if(!ss.equals("NIE")) {
                    System.out.println("Unrecognized answer: \"" + ss + "\"");
                    return false;
                }
                // TODO: Implement check for "NIE"
            }
        }
        return true;
    }
}
