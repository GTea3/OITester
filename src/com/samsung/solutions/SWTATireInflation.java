package com.samsung.solutions;

import java.util.Scanner;

public class SWTATireInflation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // number of tests
        int k = scanner.nextInt(); // maximum tire pressure
        int[] inflate = new int[n];
        for(int i = 0; i < n; ++i)
            inflate[i] = scanner.nextInt(); // tire pressure increase in test #i
        int[] deflate = new int[n];
        for(int i = 0; i < n; ++i)
            deflate[i] = scanner.nextInt(); // tire pressure decrease in test #i
        int solution = solve(inflate, deflate, new int[] {}, n, k); // find lowest peak pressure for any test order
        System.out.println(solution != Integer.MAX_VALUE ? solution : -1);
    }

    private static int solve(int[] inflate, int[] deflate, int[] seq, int n, int k) {
        if(seq.length == n) // Is sequence complete? Let's compute it's peak pressure
            return compute(inflate, deflate, seq, k);
        int best = Integer.MAX_VALUE;
        for(int i = 0; i < n; ++i) // Generate all possible subsequences given current subsequence by adding another element
            if(!contains(seq, i)) // don't extend the subsequence with i if the subsequence already contains i
                best = Math.min(best, solve(inflate, deflate, extendSubsequenceWith(seq, i), n, k)); // find lowest peak pressure for this subsequence
        return best;
    }

    private static boolean contains(int[] seq, int i) { // check if seq array contains element i
        for(int e : seq)
            if(e == i)
                return true;
        return false;
    }

    private static int[] extendSubsequenceWith(int[] seq, int i) { // make a copy of seq array with i integer appended at the end
        int[] newSeq = new int[seq.length + 1];
        for(int j = 0; j < seq.length; ++j)
            newSeq[j] = seq[j];
        newSeq[seq.length] = i;
        return newSeq;
    }

    private static int compute(int[] inflate, int[] deflate, int[] seq, int k) { // compute peak pressure for given test sequence
        int currentPressure = 0, minPressure = 0, maxPressure = 0; // starting pressure is 0 (let's assume for now it can go to negative values)
        for(int test : seq) { // for every test
            maxPressure = Math.max(maxPressure, currentPressure += inflate[test]); // inflate tire
            minPressure = Math.min(minPressure, currentPressure -= deflate[test]); // deflate tire
        }
        if(maxPressure - minPressure > k) // let's check if corrected maxPressure does not exceed limit (minPressure <= 0)
            return Integer.MAX_VALUE; // let's return MAX_VALUE as indication that maximum pressure has been exceeded
        return Math.abs(minPressure); // return corrected minPressure
    }

    // ---------------------------------------------------------------
    // additional solutions (different permutation generation methods)
    // ---------------------------------------------------------------

    private static int solveIterativeHeapsAlgorithm(int[] inflate, int[] deflate, int n, int k) {
        int[] seq = new int[n];
        for(int i = 0; i < n; ++i)
            seq[i] = i;

        int[] c = new int[n]; // c is an encoding of the stack state. c[k] encodes the for-loop counter for when generate(k+1, A) is called
        for(int i = 0; i < n; ++i)
            c[i] = 0;

        int best = compute(inflate, deflate, seq, k);
        for(int i = 0; i < n;) { // i acts similarly to the stack pointer
            if(c[i] < i) {
                if(i % 2 == 0)
                    swap(seq, 0, i);
                else
                    swap(seq, c[i], i);
                best = Math.min(best, compute(inflate, deflate, seq, k)); // find lowest peak pressure for this subsequence
                ++c[i]; // Swap has occurred ending the for-loop. Simulate the increment of the for-loop counter
                i = 0; // Simulate recursive call reaching the base case by bringing the pointer to the base case analog in the array
            }
            else {
                c[i++] = 0; // Calling generate(i+1, A) has ended as the for-loop terminated. Reset the state and simulate popping the stack by incrementing the pointer.
            }
        }
        return best;
    }

    private static int solveRecursiveHeapsAlgorithm(int[] inflate, int[] deflate, int n, int k) {
        int[] seq = new int[n];
        for(int i = 0; i < n; ++i)
            seq[i] = i;
        return recursiveHeapsAlgorithmHelper(inflate, deflate, n, k, seq);
    }

    private static int recursiveHeapsAlgorithmHelper(int[] inflate, int[] deflate, int n, int k, int seq[]) {
        if(n == 1)
            return compute(inflate, deflate, seq, k); // find lowest peak pressure for this subsequence
        int best = Integer.MAX_VALUE;
        for(int i = 0; i < n; ++i) {
            best = Math.min(best, recursiveHeapsAlgorithmHelper(inflate, deflate, n - 1, k, seq));
            if(n % 2 == 0)
                swap(seq, i, n - 1);
            else
                swap(seq, 0, n - 1);
        }
        return best;
    }

    private static int solveIterativeLexicographical(int[] inflate, int[] deflate, int n, int k) {
        int best = Integer.MAX_VALUE;
        int[] seq = new int[n];
        for(int i = 0; i < n; ++i)
            seq[i] = i;
        while(nextLexicographicalPermutation(seq)) // for every permutation
                best = Math.min(best, compute(inflate, deflate, seq, k)); // find lowest peak pressure for this order of tests
        return best;
    }

    private static boolean nextLexicographicalPermutation(int[] seq) {
        int i = seq.length - 2;
        while(i >= 0 && seq[i + 1] <= seq[i])
            --i;
        if(i >= 0) {
            int j = seq.length - 1;
            while(j >= 0 && seq[j] <= seq[i])
                --j;
            swap(seq, i, j);
        }
        reverse(seq, i + 1);

        return i >= 0;
    }

    private static void swap(int[] seq, int a, int b) {
        int tmp = seq[a];
        seq[a] = seq[b];
        seq[b] = tmp;
    }

    private static void reverse(int[] seq, int start) {
        for(int end = seq.length - 1; start < end; ++start, --end)
            swap(seq, start, end);
    }
}
