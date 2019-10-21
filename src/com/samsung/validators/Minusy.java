package com.samsung.validators;

import java.util.Scanner;

public class Minusy {
    private static boolean check(char requestedSign, char currentSign, char externalSign, boolean verbose) {
        char compoundSign = externalSign == currentSign ? currentSign : '-';
        if(requestedSign != compoundSign) {
            if (verbose)
                System.out.println("Expected " + (requestedSign == '+' ? "positive" : "negative") + " variable, got " + (compoundSign == '+' ? "positive" : "negative") + ".");
            return false;
        }
        return true;
    }

    private static char flip(char sign) {
        return sign == '-' ? '+' : '-';
    }

    public static boolean validate(Scanner answer, Scanner input, boolean verbose) {
        verbose = true;
        int n = input.nextInt();
        String s = answer.next();
        int variablesProcessed = 0;
        char requestedSign = '+'; // first variable is positive
        char externalSign = '+'; // sign applied to current partial expression inside current parentheses (or whole expression when not in any parentheses)
        char internalSign = '+'; // current sign applicable to next virtual token; expression begins with a variable without any sign, so it's positive
        int parenthesesLevel = 0;
        for(int i = 0; i < s.length(); ++i) {
            System.out.println(s.charAt(i));
            if(s.charAt(i) == '(') {
                // TODO: parentheses cannot be opened right after one's been closed
                externalSign = flip(externalSign);
                internalSign = '+';
                ++parenthesesLevel;
            }
            else if(s.charAt(i) == ')') {
                if(!check(requestedSign, internalSign, externalSign, verbose))
                    return false;
                requestedSign = input.next("[+-]\\n?").charAt(0);
                internalSign = externalSign;
                externalSign = flip(externalSign);
                if(--parenthesesLevel < 0) {
                    if(verbose)
                        System.out.println("Parentheses not matching correctly.");
                    return false;
                }
            }
            else if(s.charAt(i) == '-') {
                if(!check(requestedSign, internalSign, externalSign, verbose))
                    return false;
                requestedSign = input.next("[+-]\\n?").charAt(0);
                internalSign = '-';
            }
            else {
                if(verbose)
                    System.out.println("Invalid character: '" + s.charAt(i) + "'");
                return false;
            }
        }
        if(variablesProcessed != n - 1) {
            if(verbose)
                System.out.println("Not all variables had been taken into account.");
            return false;
        }
        return true;
    }
}
