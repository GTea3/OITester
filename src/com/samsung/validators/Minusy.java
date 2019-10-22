package com.samsung.validators;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class Minusy {

    private enum State {
        Start,
        Minus,
        Closing,
        Opening,
        End
    }

    private static class Context {
        Scanner answer;
        Scanner input;
        State state;
        String s;
        int n;
        int i;
        boolean verbose;
        int variablesProcessed;
        int parenthesesLevel;
        char requestedSign;
        char currentSign;
        Stack<Character> signStack;

        public Context(Scanner answer, Scanner input, boolean verbose) throws Exception {
            this.verbose = true;
            this.answer = answer;
            this.input = input;
            this.state = State.Start;
            this.n = input.nextInt();
            if(!answer.hasNext()) {
                if(verbose)
                    System.out.println("\nNo answer provided.");
                throw new Exception();
            }
            this.s = answer.next();
            this.i = 0;
            this.variablesProcessed = 0;
            this.parenthesesLevel = 0;
            this.requestedSign = '+'; // first variable is positive
            this.currentSign = '+';
            this.signStack = new Stack<Character>();
        }

        public void nextRequested() {
            try {
                ++this.variablesProcessed;
                this.requestedSign = input.next("[+-]\\n?").charAt(0);
            } catch (NoSuchElementException e) {
                ; // Input ended. It's visible in this.variablesProcessed now should equal to this.n.
            }
        }

        public char GetCombinedSign() {
            if(signStack.empty())
                return currentSign;
            return currentSign == signStack.peek() ? '+' : '-';
        }

    }

    public static boolean validate(Scanner answer, Scanner input, boolean verbose) {
        Context context = null;
        try {
            context = new Context(answer, input, verbose);
        } catch (Exception e) {
            return false;
        }
        while(true) {
            switch (context.state) {
                case Start:
                    if(!Start(context))
                        return false;
                    break;
                case Minus:
                    if(!Minus(context))
                        return false;
                    break;
                case Opening:
                    if(!Opening(context))
                        return false;
                    break;
                case Closing:
                    if(!Closing(context))
                        return false;
                    break;
                case End:
                    return End(context);
            }
        }
    }

    private static boolean VerifyRequest(Context context) {
        if(context.variablesProcessed >= context.n) {
            if(context.verbose)
                System.out.println("\nProvided expression contains more implicit variables than expected.");
            return false;
        }
        if(context.requestedSign != context.GetCombinedSign()) {
            if(context.verbose)
                System.out.println("\nExpected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
            return false;
        }
        context.nextRequested();
        return true;
    }

    private static boolean Start(Context context) {
        if(context.i >= context.s.length()) {
            if(context.verbose)
                System.out.println("\nInvalid state transition.");
            return false;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = '-';
                context.state = State.Minus;
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                context.state = State.Opening;
                break;
            default:
                if(context.verbose)
                    System.out.println("\nInvalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean Minus(Context context) {
        if(context.i >= context.s.length()) {
            if(!VerifyRequest(context))
                return false;
            context.state = State.End;
            return true;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = '-';
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                context.state = State.Opening;
                break;
            case ')':
                if(context.parenthesesLevel <= 0) {
                    if (context.verbose)
                        System.out.println("\nParentheses closed incorrectly.");
                    return false;
                }
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                context.state = State.Closing;
                break;
            default:
                if(context.verbose)
                    System.out.println("\nInvalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean Opening(Context context) {
        if(context.i >= context.s.length()) {
            if(context.verbose)
                System.out.println("\nInvalid state transition.");
            return false;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = '-';
                context.state = State.Minus;
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                break;
            default:
                if(context.verbose)
                    System.out.println("\nInvalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean Closing(Context context) {
        if(context.i >= context.s.length()) {
            context.state = State.End;
            return true;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                context.currentSign = '-';
                context.state = State.Minus;
                break;
            case ')':
                if(context.parenthesesLevel <= 0) {
                    if (context.verbose)
                        System.out.println("\nParentheses closed incorrectly.");
                    return false;
                }
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                break;
            default:
                if(context.verbose)
                    System.out.println("\nInvalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean End(Context context) {
        if(context.parenthesesLevel != 0) {
            if (context.verbose)
                System.out.println("\nParentheses not closed correctly.");
            return false;
        }
        if(context.n != context.variablesProcessed) {
            if (context.verbose)
                System.out.println("\nNot all variables had been taken into account.");
            return false;
        }
        ++context.i;
        return true;
    }
}
