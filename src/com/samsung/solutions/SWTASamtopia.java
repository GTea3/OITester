package com.samsung.solutions;

import java.util.Scanner;

public class SWTASamtopia {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] votes = new int[n][n];
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                votes[i][j] = scanner.nextInt();
        System.out.println(solve(n, votes));
    }

    private static int solve(int n, int[][] votes) {
        // we'll be indexing with values from the array, so lets move them to <0, n-1> range
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                --votes[i][j];
        // let's check if we have a winner and if not, remove losers
        // we'll be doing it max n times, since after no more than n times everyone will be removed
        for(int i = 0; i < n; ++i) {
            int winner = getWinner(n, votes);
            if(winner > -1)
                return winner + 1;
            removeLoser(n, votes);
        }
        return -1;
    }

    private static int getWinner(int n, int[][] votes) {
        // count first votes (after skipping removed candidates)
        int count[] = new int[n];
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                if(votes[i][j] != -1) {
                    ++count[votes[i][j]];
                    break;
                }
        // find possible winner
        int most = 0;
        int winner = -1;
        for(int i = 0; i < n; ++i)
            if(count[i] > most) {
                most = count[i];
                winner = i;
            }
        // check if winning conditions are met
        if(most * 2 > n)
            return winner;
        return -1;
    }

    private static void removeLoser(int n, int[][] votes) {
        // count last votes (after skipping removed candidates)
        int count[] = new int[n];
        for(int i = 0; i < n; ++i)
            for(int j = n - 1; j >= 0; --j)
                if(votes[i][j] != -1) {
                    ++count[votes[i][j]];
                    break;
                }
        // find highest value
        int most = 0;
        for(int i = 0; i < n; ++i)
            if(count[i] > most)
                most = count[i];
        // remove (mark as -1) all candidates with highest value
        for(int i = 0; i < n; ++i)
            if(count[i] == most) {
                for (int j = 0; j < n; ++j)
                    for(int k = 0; k < n; ++k)
                    if (votes[j][k] == i)
                        votes[j][k] = -1;
            }
    }
}
