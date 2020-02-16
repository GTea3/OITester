package com.samsung.solutions;

import java.util.Scanner;

public class SWTASamthello {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] board = new int[n];
        for(int i = 0; i < n; ++i)
            board[i] = scanner.nextInt();
        System.out.println(solve(board, 0));
    }

    private static int solve(int[] board, int turn) {
        if(turn == 3)
            return score(board);
        int best = 0;
        for(int i = 0; i < board.length; ++i)
            if(board[i] == 0) {
                int[] newBoard = board.clone();
                newBoard[i] = 1;
                propagate(newBoard, i);
                whiteMoves(newBoard);
                best = Math.max(best, solve(newBoard, turn + 1));
            }
        return best;
    }

    private static int score(int[] board) {
        int blacks = 0;
        for(int i = 0; i < board.length; ++i)
            if(board[i] == 1)
                ++blacks;
        return blacks;
    }

    private static void whiteMoves(int[] board) {
        for(int i = 0; i < board.length; ++i)
            if(board[i] == 0) {
                board[i] = 2;
                propagate(board, i);
                break;
            }
    }

    private static void propagate(int[] board, int index) {
        int color = board[index];
        int opponent = color % 2 + 1;
        int i;

        // go left
        i = index - 1;
        while(i >= 0 && board[i] == opponent)
            --i;
        if(i < 0 || board[i] == color) {
            while(++i < index)
                board[i] = color;
        }

        // go right
        i = index + 1;
        while( i < board.length && board[i] == opponent)
            ++i;
        if(i >= board.length || board[i] == color) {
            while(--i > index)
                board[i] = color;
        }
    }
}
