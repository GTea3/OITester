package com.samsung.validators;

import java.util.Scanner;
import java.util.Stack;

public class Minusy {

    private static boolean debug = true; // TODO: remove

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

        public Context(Scanner answer, Scanner input, boolean verbose) {
            this.verbose = true;
            this.answer = answer;
            this.input = input;
            this.state = State.Start;
            this.n = input.nextInt();
            this.s = answer.next();
            this.i = 0;
            this.variablesProcessed = 0;
            this.parenthesesLevel = 0;
            this.requestedSign = '+'; // first variable is positive
            this.currentSign = '+';
            this.signStack = new Stack<Character>();
            if(debug) System.out.println(s);
        }

        public char nextRequested() {
            this.requestedSign = input.next("[+-]\\n?").charAt(0);
            return  requestedSign;
        }

        public char GetCombinedSign() {
            if(signStack.empty())
                return currentSign;
            return currentSign == signStack.peek() ? currentSign : (currentSign == '-' ? '+' : '-');
        }

    }

    public static boolean validate(Scanner answer, Scanner input, boolean verbose) {
        Context context = new Context(answer, input, verbose);
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

    private static boolean Start(Context context) {
        if(debug) System.out.println("Start");
        if(context.i >= context.s.length()) {
            if(context.verbose)
                System.out.println("Invalid state transition.");
            return false;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                // TODO: implement missing logic
                if(context.requestedSign != context.GetCombinedSign()) {
                    if(context.verbose)
                        System.out.println("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
                    return false;
                }
                context.currentSign = '-';
                if(debug) System.out.println("->Minus");
                context.state = State.Minus;
                break;
            case '(':
                // TODO: implement missing logic
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                // TODO: implement missing logic
                ++context.parenthesesLevel;
                if(debug) System.out.println("->Opening");
                context.state = State.Opening;
                break;
            default:
                if(context.verbose)
                    System.out.println("Invalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean Minus(Context context) {
        if(debug) System.out.println("Minus");
        if(context.i >= context.s.length()) {
            context.state = State.End;
            return true;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                // TODO: implement missing logic
                if(context.requestedSign != context.GetCombinedSign()) {
                    if(context.verbose)
                        System.out.println("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
                    return false;
                }
                context.currentSign = '-';
                if(debug) System.out.println("->Minus");
                break;
            case '(':
                // TODO: implement missing logic
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                // TODO: implement missing logic
                ++context.parenthesesLevel;
                if(debug) System.out.println("->Opening");
                context.state = State.Opening;
                break;
            case ')':
                // TODO: implement missing logic
                if(context.parenthesesLevel <= 0) {
                    if (context.verbose)
                        System.out.println("Parentheses closed incorrectly.");
                    return false;
                }
                if(context.requestedSign != context.GetCombinedSign()) {
                    if(context.verbose)
                        System.out.println("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
                    return false;
                }
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                if(debug) System.out.println("->Closing");
                context.state = State.Closing;
                break;
            default:
                if(context.verbose)
                    System.out.println("Invalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean Opening(Context context) {
        if(debug) System.out.println("Opening");
        if(context.i >= context.s.length()) {
            if(context.verbose)
                System.out.println("Invalid state transition.");
            return false;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                if(context.requestedSign != context.GetCombinedSign()) {
                    if(context.verbose)
                        System.out.println("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
                    return false;
                }
                context.currentSign = '-';
                // TODO: implement missing logic
                if(debug) System.out.println("->Minus");
                context.state = State.Minus;
                break;
            case '(':
                // TODO: implement missing logic
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                // TODO: implement missing logic
                if(debug) System.out.println("->Opening");
                ++context.parenthesesLevel;
                break;
            default:
                if(context.verbose)
                    System.out.println("Invalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean Closing(Context context) {
        if(debug) System.out.println("Closing");
        if(context.i >= context.s.length()) {
            context.state = State.End;
            return true;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                context.currentSign = '-';
                // TODO: implement missing logic
                if(debug) System.out.println("->Minus");
                context.state = State.Minus;
                break;
            case ')':
                // TODO: implement missing logic
                if(context.parenthesesLevel <= 0) {
                    if (context.verbose)
                        System.out.println("Parentheses closed incorrectly.");
                    return false;
                }
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                if(debug) System.out.println("->Closing");
                break;
            default:
                if(context.verbose)
                    System.out.println("Invalid state transition.");
                return false;
        }
        ++context.i;
        return true;
    }

    private static boolean End(Context context) {
        if(debug) System.out.println("End");

        context.variablesProcessed = context.n; // TODO: remove this
        if(debug) System.out.println("pLvl=" + context.parenthesesLevel);

        if(context.parenthesesLevel != 0) {
            if (context.verbose)
                System.out.println("Parentheses not closed correctly.");
            return false;
        }
        if(context.n != context.variablesProcessed) {
            if (context.verbose)
                System.out.println("Not all variables had been taken into account.");
            return false;
        }
        ++context.i;
        return true;
    }
}
