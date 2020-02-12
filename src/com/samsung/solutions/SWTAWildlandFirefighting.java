package com.samsung.solutions;

import java.util.Scanner;

public class SWTAWildlandFirefighting {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int map[][] = new int[N][N];
        for(int r = 0; r < N; ++r)
            for(int c = 0; c < N; ++c)
                map[r][c] = scanner.nextInt(); // 0 - empty, 1 - tree, 2 - fire
        System.out.println(solve(map, N, M));
    }

    // to avoid duplicating sets of positions, pass last set position and start looking for next tree from there
    private static int solve(int map[][], int N, int M) {
        // no more trees to cut - let it burn
        if(M == 0)
            return calculate(map);

        // don't cut a tree this time, so we have solutions for less than M cut trees
        int best = solve(clone(map), N, M - 1);

        // find a tree, cut it, pass it recursively to be solved
        for(int r = 0; r < N; ++r)
            for(int c = 0; c < N; ++c)
                if(map[r][c] == 1) {
                    int newMap[][] = clone(map);
                    newMap[r][c] = 0;
                    best = Math.max(best, solve(newMap, N, M - 1));
                }
        return best;
    }

    private static int solveAlt(int map[][], int N, int M) {
        // 0 trees to cut
        int best = calculate(clone(map));

        // 1 tree to cut
        if(M > 0)
            for(int r = 0; r < N; ++r)
                for(int c = 0; c < N; ++c)
                    if(map[r][c] == 1) {
                        int newMap[][] = clone(map);
                        newMap[r][c] = 0;
                        best = Math.max(best, calculate(newMap));
                    }

        // 2 trees to cut
        if(M > 1)
            for(int r = 0; r < N; ++r)
                for(int c = 0; c < N; ++c)
                    if(map[r][c] == 1)
                        for(int r2 = r; r2 < N; ++r2)
                            for(int c2 = r2 == r ? c + 1 : 0; c2 < N; ++c2)
                                if(map[r2][c2] == 1) {
                                    int newMap[][] = clone(map);
                                    newMap[r][c] = 0;
                                    newMap[r2][c2] = 0;
                                    best = Math.max(best, calculate(newMap));
                                }

        return best;
    }

    private static int calculate(int map[][]) {
        for(int r = 0; r < map.length; ++r)
            for(int c = 0; c < map.length; ++c)
                if(map[r][c] == 2)
                    burn(map, r, c);
        int saved = 0;
        for(int r = 0; r < map.length; ++r)
            for(int c = 0; c < map.length; ++c)
                if(map[r][c] == 1)
                    ++saved;
        return saved;
    }

    private static void burn(int map[][], int r, int c) {
        if(r < 0 || r >= map.length || c < 0 || c >= map.length)
            return;
        if(map[r][c] > 0) {
            map[r][c] = 0;
            burn(map, r - 1, c);
            burn(map, r + 1, c);
            burn(map, r, c - 1);
            burn(map, r, c + 1);
        }
    }

    private static int[][] clone(int map[][]) {
        int newMap[][] = new int[map.length][map.length];
        for(int i = 0; i < map.length; ++i)
            newMap[i] = map[i].clone();
        return newMap;
    }
}
