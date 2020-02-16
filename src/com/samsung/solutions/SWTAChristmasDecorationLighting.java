package com.samsung.solutions;

import java.util.Scanner;

public class SWTAChristmasDecorationLighting {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] bulbs = new int[3];
        for(int i = 0; i < 3; ++i)
            bulbs[i] = scanner.nextInt();
        int[][] models = new int[n][3];
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < 3; ++j)
                models[i][j] = scanner.nextInt();
        System.out.println(solve(bulbs, models, 0));
    }

    private static int solve(int[] bulbs, int[][] models, int model) {
        if(model == models.length)
            return 0;
        int best = 0;
        for(int pieces = 0; pieces <= 3 && canBeMade(bulbs, models[model], pieces); ++pieces) {
            int[] newBulbs = bulbs.clone();
            for(int i = 0; i < 3; ++i)
                newBulbs[i] -= models[model][i] * pieces;
            best = Math.max(best, pieces + solve(newBulbs, models, model + 1));
        }
        return best;
    }

    private static boolean canBeMade(int[] bulbs, int[] model, int pieces) {
        for(int i = 0; i < 3; ++i)
            if(bulbs[i] < model[i] * pieces)
                return false;
        return true;
    }
}
