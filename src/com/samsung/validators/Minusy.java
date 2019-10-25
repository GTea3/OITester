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
        int variablesProcessed;
        int parenthesesLevel;
        char requestedSign;
        char currentSign;
        Stack<Character> signStack;

        Context(Scanner answer, Scanner input) throws Exception {
            this.answer = answer;
            this.input = input;
            this.state = State.Start;
            this.n = input.nextInt();
            if(!answer.hasNext())
                throw new Exception("No answer provided.");
            this.s = answer.next();
            this.i = 0;
            this.variablesProcessed = 0;
            this.parenthesesLevel = 0;
            this.requestedSign = '+'; // first variable is always positive; it's not listed in requests
            this.currentSign = '+'; // we start with neutral (positive-signed) context
            this.signStack = new Stack<Character>();
        }

        void nextRequested() {
            try {
                ++this.variablesProcessed;
                this.requestedSign = input.next("[+-]\\n?").charAt(0);
            } catch (NoSuchElementException ignored) {
            } // Input ended; this.variablesProcessed should now be equal or greater than this.n.
        }

        char GetCombinedSign() {
            if(signStack.empty())
                return currentSign;
            return currentSign == signStack.peek() ? '+' : '-';
        }
    }

    public static boolean validate(Scanner answer, Scanner input) throws Exception {
        Context context = new Context(answer, input);
        while(true) {
            switch (context.state) {
                case Start:
                    Start(context);
                    break;
                case Minus:
                    Minus(context);
                    break;
                case Opening:
                    Opening(context);
                    break;
                case Closing:
                    Closing(context);
                    break;
                case End:
                    End(context);
                    return true;
            }
        }
    }

    private static void VerifyRequest(Context context) throws Exception {
        if(context.variablesProcessed >= context.n)
            throw new Exception("Provided expression contains more implicit variables than expected.");
        if(context.requestedSign != context.GetCombinedSign())
            throw new Exception("Expected " + (context.requestedSign == '-' ? "negative" : "positive") + " variable, got " + (context.GetCombinedSign() == '-' ? "negative" : "positive") + ".");
        context.nextRequested();
    }

    private static void Start(Context context) throws Exception {
        if(context.i >= context.s.length())
            throw new Exception("Invalid state transition.");
        switch (context.s.charAt(context.i)) {
            case '-':
                VerifyRequest(context);
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
                throw new Exception("Invalid state transition.");
        }
        ++context.i;
    }

    private static void Minus(Context context) throws Exception {
        if(context.i >= context.s.length()) {
            VerifyRequest(context);
            context.state = State.End;
            return;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                VerifyRequest(context);
                context.currentSign = '-';
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                context.state = State.Opening;
                break;
            case ')':
                if(context.parenthesesLevel <= 0)
                    throw new Exception("Parentheses closed incorrectly.");
                VerifyRequest(context);
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                context.state = State.Closing;
                break;
            default:
                throw new Exception("Invalid state transition.");
        }
        ++context.i;
    }

    private static void Opening(Context context) throws Exception {
        if(context.i >= context.s.length())
            throw new Exception("Invalid state transition.");
        switch (context.s.charAt(context.i)) {
            case '-':
                VerifyRequest(context);
                context.currentSign = '-';
                context.state = State.Minus;
                break;
            case '(':
                context.signStack.push(context.GetCombinedSign());
                context.currentSign = '+';
                ++context.parenthesesLevel;
                break;
            default:
                throw new Exception("Invalid state transition.");
        }
        ++context.i;
    }

    private static void Closing(Context context) throws Exception {
        if(context.i >= context.s.length()) {
            context.state = State.End;
            return;
        }
        switch (context.s.charAt(context.i)) {
            case '-':
                context.currentSign = '-';
                context.state = State.Minus;
                break;
            case ')':
                if(context.parenthesesLevel <= 0)
                    throw new Exception("Parentheses closed incorrectly.");
                context.currentSign = context.signStack.pop();
                --context.parenthesesLevel;
                break;
            default:
                throw new Exception("Invalid state transition.");
        }
        ++context.i;
    }

    private static void End(Context context) throws Exception {
        if(context.parenthesesLevel != 0)
            throw new Exception("Parentheses not closed correctly.");
        if(context.n != context.variablesProcessed)
            throw new Exception("Not all variables had been taken into account.");
        ++context.i;
    }
}
