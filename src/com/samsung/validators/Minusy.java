package com.samsung.validators;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class Minusy {

    private static boolean debug = false; // TODO: remove

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

    private static boolean VerifyRequest(Context context) {
        if(context.variablesProcessed >= context.n) {
            if(context.verbose)
                System.out.println("Provided expression contains more implicit variables than expected.");
            return false;
        }
        if(context.requestedSign != context.GetCombinedSign()) {
            if(context.verbose)
                System.out.println("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
            return false;
        }
        if(debug) System.out.println("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
        context.nextRequested();
        return true;
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
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = '-';
                context.state = State.Minus;
                if(debug) System.out.println("->Minus");
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                context.state = State.Opening;
                if(debug) System.out.println("->Opening");
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
            if(!VerifyRequest(context))
                return false;
            context.state = State.End;
            if(debug) System.out.println("->End");
            return true;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = '-';
                if(debug) System.out.println("->Minus");
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                context.state = State.Opening;
                if(debug) System.out.println("->Opening");
                break;
            case ')':
                if(context.parenthesesLevel <= 0) {
                    if (context.verbose)
                        System.out.println("Parentheses closed incorrectly.");
                    return false;
                }
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                context.state = State.Closing;
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

    private static boolean Opening(Context context) {
        if(debug) System.out.println("Opening");
        if(context.i >= context.s.length()) {
            if(context.verbose)
                System.out.println("Invalid state transition.");
            return false;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                if(!VerifyRequest(context))
                    return false;
                context.currentSign = '-';
                context.state = State.Minus;
                if(debug) System.out.println("->Minus");
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
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
            if(debug) System.out.println("->End");
            return true;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                context.currentSign = '-';
                context.state = State.Minus;
                if(debug) System.out.println("->Minus");
                break;
            case ')':
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
