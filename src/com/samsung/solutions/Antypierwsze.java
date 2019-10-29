package com.samsung.solutions;

import com.samsung.utils.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/AB6xXa9zVukjNRe7nvPqxbVQ/site/
public class Antypierwsze {
    public static void main(String[] args) {
        System.out.println(generated((new Scanner(System.in)).nextInt()));
        //System.out.println(arrayed((new Scanner(System.in)).nextInt()));
    }

    private static int generated(int n) {
        int[] primes = new int[] {1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29}; // first 11 prime numbers
        List<Pair<Integer, Integer>> candidates = new ArrayList<Pair<Integer, Integer>>() {
        };
        generate(1, 31, 1, 1, primes, n, candidates);
        candidates.sort(Comparator.comparing(Pair<Integer, Integer>::getValue).thenComparing(Pair<Integer, Integer>::getKey));
        Pair<Integer, Integer> best = new Pair<Integer, Integer>(1, 1);
        for(Pair<Integer, Integer> candidate : candidates)
            if(candidate.getKey() > n)
                break;
            else if(candidate.getValue() > best.getValue())
                best = candidate;
        return best.getKey();
    }

    private static void generate(int nextPrimeIndex, int maxNextPrimePower, int currentCandidate, int currentCandidateDivisorCount, int[] primes, int n, List<Pair<Integer, Integer>> candidates) {
        candidates.add(new Pair<Integer, Integer>(currentCandidate, currentCandidateDivisorCount));
        for(int i = 1; i <= maxNextPrimePower; ++i) {
            if(primes[nextPrimeIndex] > Math.floor(n / currentCandidate)) // "primes[nextPrimeIndex] * currentCandidate > n"
                return;
            currentCandidate *= primes[nextPrimeIndex];
            generate(nextPrimeIndex + 1, i, currentCandidate, currentCandidateDivisorCount * (i + 1), primes, n, candidates);
        }
    }

    private static int arrayed(int n) { // https://oeis.org/A002182
        int[] antiprimes = new int[] {1, 2, 4, 6, 12, 24, 36, 48, 60, 120, 180, 240, 360, 720, 840, 1260, 1680, 2520, 5040, 7560, 10080, 15120, 20160, 25200, 27720, 45360, 50400, 55440, 83160, 110880, 166320, 221760, 277200, 332640, 498960, 554400, 665280, 720720, 1081080, 1441440, 2162160, 2882880, 3603600, 4324320, 6486480, 7207200, 8648640, 10810800, 14414400, 17297280, 21621600, 32432400, 36756720, 43243200, 61261200, 73513440, 110270160, 122522400, 147026880, 183783600, 245044800, 294053760, 367567200, 551350800, 698377680, 735134400, 1102701600, 1396755360, };
        for(int i = antiprimes.length - 1; i >= 0; --i)
            if(antiprimes[i] <= n)
                return antiprimes[i];
        return 0;
    }
}
