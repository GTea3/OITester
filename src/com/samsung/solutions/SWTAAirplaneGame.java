package com.samsung.solutions;

import java.util.Scanner;

public class SWTAAirplaneGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] map = new int[n][5];
        for(int r = n - 1; r >= 0; --r)
            for(int c = 0; c < 5; ++c)
                map[r][c] = scanner.nextInt();
        System.out.println(Math.max(branch(map, -1, 2, -1, 0, true),
                                    branch(map, -1, 2, -1, 0, false)));
    }

    private static int solve(int[][] map, int row, int col, int bomb, int score) {
        if(row == map.length)
            return score;

        if (map[row][col] == 1)
            ++score;
        if (map[row][col] == 2 && !(bomb != -1 && row < bomb + 5))
            --score;
        if (score < 0)
            return -1;

        int best = -1;
        if(bomb == -1)
            best = Math.max(best, branch(map, row, col, bomb, score, true));
        best = Math.max(best, branch(map, row, col, bomb, score, false));
        return best;
    }

    private static int branch(int[][] map, int row, int col, int bomb, int score, boolean useBomb) {
        int best = -1;
        for(int j = -1; j < 2; ++j)
            if(col + j >= 0 && col + j < 5)
                best = Math.max(best, solve(map, row + 1, col + j, useBomb ? row + 1 : bomb, score));
        return best;
    }
}
