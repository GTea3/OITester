package com.samsung.solutions;

import java.util.Scanner;

public class SWTAAirplaneGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] map = new int[n][5];
        for(int r = 0; r < n; ++r)
            for(int c = 0; c < 5; ++c)
                map[r][c] = scanner.nextInt();
        System.out.println(solve(map, n, 2, -1, 0));
    }

    private static int solve(int[][] map, int row, int col, int bomb, int score) {
        if(row < 0)
            return score;

        if(row < map.length) {
            if (map[row][col] == 1)
                ++score;
            if (map[row][col] == 2 && !(bomb != -1 && row > bomb - 5))
                --score;
            if (score < 0)
                return -1;
        }

        int best = -1;
        for(int i = 0; i < 2; ++i) {
            boolean useBomb = i > 0;
            if(useBomb && bomb > -1)
                continue;
            for(int j = -1; j < 2; ++j) {
                if(col + j < 0 || col + j >= 5)
                    continue;
                best = Math.max(best, solve(map, row - 1, col + j, useBomb ? row - 1 : bomb, score));
            }
        }
        return best;
    }
}
