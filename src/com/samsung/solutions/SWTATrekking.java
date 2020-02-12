package com.samsung.solutions;

import java.util.Scanner;

public class SWTATrekking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int map[][] = new int[n][n];
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                map[i][j] = scanner.nextInt();
        System.out.println(iterativeDP(map));
    }

    private static int cost(int from, int to) {
        return 1 + Math.max(to - from, 0);
    }

    private static int recursiveNaive(int map[][], int r, int c) {
        if(r == map.length - 1 && c == map.length - 1)
            return 0;
        int down = 1000;
        if(r + 1 < map.length)
            down = cost(map[r][c], map[r + 1][c]) + recursiveNaive(map, r + 1, c);
        int right = 1000;
        if(c + 1 < map.length)
            right = cost(map[r][c], map[r][c + 1]) + recursiveNaive(map, r, c + 1);
        return Math.min(down, right);
    }

    private static int recursiveDP(int map[][]) {
        int mem[][] = new int[map.length][map.length];
        for(int i = 0; i < map.length; ++i)
            for(int j = 0; j < map.length; ++j)
                mem[i][j] = -1;
        mem[map.length - 1][map.length - 1] = 0;
        return recursiveDPHelper(map, mem, 0, 0);
    }

    private static int recursiveDPHelper(int map[][], int mem[][], int r, int c) {
        if(mem[r][c] > -1)
            return mem[r][c];
        int down = 1000;
        if(r + 1 < map.length)
            down = cost(map[r][c], map[r + 1][c]) + recursiveDPHelper(map, mem, r + 1, c);
        int right = 1000;
        if(c + 1 < map.length)
            right = cost(map[r][c], map[r][c + 1]) + recursiveDPHelper(map, mem, r, c + 1);
        mem[r][c] = Math.min(down, right);
        return mem[r][c];
    }

    private static int iterativeDP(int map[][]) {
        int mem[][] = new int[map.length][map.length];
        mem[map.length - 1][map.length - 1] = 0;
        for(int i = map.length - 2; i >= 0; --i) {
            mem[i][map.length - 1] = cost(map[i][map.length - 1], map[i + 1][map.length - 1]) + mem[i + 1][map.length - 1];
            mem[map.length - 1][i] = cost(map[map.length - 1][i], map[map.length - 1][i + 1]) + mem[map.length - 1][i + 1];
        }
        for(int r = map.length - 2; r >= 0; --r)
            for(int c = map.length - 2; c >= 0; --c)
                mem[r][c] = Math.min(cost(map[r][c], map[r + 1][c]) + mem[r + 1][c], cost(map[r][c], map[r][c + 1]) + mem[r][c + 1]);
        return mem[0][0];
    }
}
