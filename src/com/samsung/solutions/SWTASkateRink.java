package com.samsung.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Maybe it could be optimized a bit to pass last test, but it's NP-Hard.
// Here's NP-Hardness proof for use-all-segments version based on partitioning problem:
// https://stackoverflow.com/questions/7294548/construct-the-largest-possible-rectangle-out-of-line-segments-of-given-lengths

// Improvement over assigning every segment to every one of 5 sets (4 sets for 4 edges and 1 for not using the segment),
// which is 5^10 ~= 10^7 possibilities: generate all partitions of set of segments into 5 sets, which is
// SterlingS2(10, 5) = 42525 possibilities.
// https://mathworld.wolfram.com/StirlingNumberoftheSecondKind.html
// https://en.wikipedia.org/wiki/Stirling_numbers_of_the_second_kind

// https://stackoverflow.com/questions/20530128/how-to-find-all-partitions-of-a-set/20530130

public class SWTASkateRink {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] beams = new int[n];
        for(int i = 0; i < n; ++i)
            beams[i] = scanner.nextInt();
        System.out.println(solve(beams));
    }

    private static int solve(int[] beams) {
        return solve(beams, 0, new int[4]);
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

    // Approach #2: Partition given set into 4 nonempty and 1 possibly-empty subset

    // partition given set into two sets but to avoid duplication, always place 1st element into 1st set
    private static void partition(int[] set) {
        if(set.length == 0)
            return;

        ArrayList<Integer> set1 = new ArrayList<Integer>();
        ArrayList<Integer> set2 = new ArrayList<Integer>();

        set1.add(set[0]);

    }
}
