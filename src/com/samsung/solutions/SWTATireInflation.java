package com.samsung.solutions;

import java.util.ArrayList;
import java.util.Scanner;

public class SWTATireInflation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[] inflate = new int[n];
        for(int i = 0; i < n; ++i)
            inflate[i] = scanner.nextInt();
        int[] deflate = new int[n];
        for(int i = 0; i < n; ++i)
            deflate[i] = scanner.nextInt();
        System.out.println(solve(inflate, deflate, new ArrayList<Integer>(), n, k));
    }

    private static int solve(int[] inflate, int[] deflate, ArrayList<Integer> seq, int n, int k) {
        if(seq.size() == n)
            return compute(inflate, deflate, seq, k);
        int best = Integer.MAX_VALUE;
        for(int i = 0; i < n; ++i)
            if(!seq.contains(i)) {
                ArrayList<Integer> newSeq = (ArrayList<Integer>) seq.clone();
                newSeq.add(i);
                int candidate = solve(inflate, deflate, newSeq, n, k);
                if(candidate > -1)
                    best = Math.min(best, candidate);
            }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    private static int compute(int[] inflate, int[] deflate, ArrayList<Integer> seq, int k) {
        int pressure = 0;
        int maxPressure = 0;
        int minPressure = 0;
        for(Integer test : seq) {
            pressure += inflate[test];
            maxPressure = Math.max(maxPressure, pressure);
            pressure -= deflate[test];
            minPressure = Math.min(minPressure, pressure);
        }
        minPressure = Math.abs(minPressure);
        maxPressure += minPressure;
        if(maxPressure > k)
            return -1;
        return minPressure;
    }
}
