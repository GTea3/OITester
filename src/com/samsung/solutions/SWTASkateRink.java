package com.samsung.solutions;

import java.util.Scanner;

public class SWTASkateRink {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] beams = new int[n];
        for(int i = 0; i < n; ++i)
            beams[i] = scanner.nextInt();
        System.out.println(solve(beams, 0, new int[4]));
    }

    private static int solve(int[] beams, int beam, int[] edges) {
        if(beam == beams.length)
            return compute(edges);
        int best = 0;
        for(int i = 0; i < 4; ++i) {
            int[] newEdges = edges.clone();
            newEdges[i] += beams[beam];
            best = Math.max(best, solve(beams, beam + 1, newEdges));
        }
        best = Math.max(best, solve(beams, beam + 1, edges));
        return best;
    }

    private static int compute(int[] edge) {
        if(edge[0] == edge[2] && edge[1] == edge[3])
            return edge[0] * edge[1];
        return 0;
    }
}
