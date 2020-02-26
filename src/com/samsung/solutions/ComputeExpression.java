package com.samsung.solutions;

// values are greater than 0, no plus sign in front, no spaces
// operators: +, -, *, /, ^, (, )

import java.util.*;

public class ComputeExpression {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        int result = new Solution().solve(expression);
        System.out.print(result);
    }
}

class Solution {
    public int solve(String expression) {
        Token[] infix = tokenize(expression);
        Token[] postfix = infixToPostfix(infix);
        int result = evaluateRPN(postfix);
        return result;
    }

    public enum Type { Number, Mul, Div, Sub, Add, PaL, PaR };
    static HashMap<Type, Integer> priorityMap = new HashMap<Type, Integer>() {
        {
            put(Type.Div, 2);
            put(Type.Mul, 2);
            put(Type.Sub, 1);
            put(Type.Add, 1);
            put(Type.PaL, 0);
            put(Type.PaR, 0);
        }
    };
    static HashMap<Character, Type> operatorMap = new HashMap<Character, Type>() {
        {
            put('/', Type.Div);
            put('*', Type.Mul);
            put('-', Type.Sub);
            put('+', Type.Add);
            put('(', Type.PaL);
            put(')', Type.PaR);
        }
    };
    class Token {
        public Type type;
        public int value;

        Token(int n) {
            type = Type.Number;
            value = n;
        }

        Token(char op) {
            type = operatorMap.get(op);
        }
    }

    Token[] tokenize(String expression) {
        ArrayList<Token> out = new ArrayList<Token>();
        for(int i = 0; i < expression.length(); ++i) {
            char c = expression.charAt(i);
            if(c == ' ')
                continue;

            if(Character.isDigit(c)) {
                int value = 0;
                while(i < expression.length() && Character.isDigit(expression.charAt(i)))
                    value = value * 10 + (expression.charAt(i++) - '0');
                --i;
                out.add(new Token(value));
            }
            else
                out.add(new Token(c));
        }
        return out.toArray(new Token[out.size()]);
    }

    Token[] infixToPostfix(Token[] infix) {
        ArrayList<Token> out = new ArrayList<Token>();
        Stack<Token> s = new Stack<Token>();

        for (Token token : infix) {
            if(token.type == Type.Number) // a number - just push on stack
                out.add(token);
            else {
                if(token.type == Type.PaL) { // '(' - just push on stack
                    s.push(token);
                }
                else if(token.type == Type.PaR) { // ')' - push to output everything from stack till matching '('
                    while(s.peek().type != Type.PaL)
                        out.add(s.pop());
                    s.pop(); // pop matching '('
                }
                else { // arithmetic operator - push to output everything from stack till lower priority operator is found
                    while(!s.empty() && priorityMap.get(token.type) <= priorityMap.get(s.peek().type))
                        out.add(s.pop());
                    s.push(token); // push the operator to stack
                }
            }
        }
        while(!s.empty()) // move everything left on stack to output
            out.add(s.pop());

        return out.toArray(new Token[out.size()]);
    }

    int evaluateRPN(Token[] postfix) {
        Stack<Integer> s = new Stack<Integer>();
        for(Token token : postfix) {
            if(token.type == Type.Number)
                s.push(token.value);
            else {
                int b = s.pop();
                int a = s.pop();
                int c;
                switch (token.type) {
                    case Add:
                        c = a + b;
                        break;
                    case Sub:
                        c = a - b;
                        break;
                    case Mul:
                        c = a * b;
                        break;
                    case Div:
                        c = a / b;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + token.type);
                }
                s.push(c);
            }
        }
        return s.peek();
    }
}

