package com.samsung.solutions;

import java.util.Arrays;
import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/A7ZI0Wwn6tTiCJoYblblTAqz/site/
public class Rownanie {

    // It's hurriedly ported from C++, so it's not exactly clean Java code. ;)
    // Also, it's work in progress; TODO: implement multimap, set and stack

    // Every bit in equation must be equal to at last one other bit.
    // We create graph of "equality" for bits and compute number
    // of strongly connected components that don't contain 0 nor 1
    // (let's call them variable strongly connected components; vscc).
    // Result is equal to pow(2, count(vscc)), because every such bit can be 0 or 1.
    // There is no solution if bitlenghts of expressions are not equal
    // or if there is 0 and 1 together in any strongly connected component.
    public static void main(String[] args) {/*
        final int N = 10000; // maximum expression bitlength

        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        while(x-- > 0) {
            // let's read and pre-process the input
            int k = scanner.nextInt(); // number of variables
            int c[] = new int[26]; Arrays.fill(c, 0); // variables' bitlengths
            for(int i = 0; i < k; ++i)
                c[i] = scanner.nextInt();

            int cc[] = new int[26]; Arrays.fill(cc, 0); // variables' bitoffsets
            for(int i = 1; i < k; ++i)
                cc[i] = cc[i - 1] + c[i - 1];

            int l = scanner.nextInt(); // left expression length
            char lw[] = new char[N]; Arrays.fill(lw, (char)0); // left expression
            lw = scanner.nextLine().toCharArray();

            int r = scanner.nextInt(); // right expression length
            char rw[] = new char[N]; Arrays.fill(rw, (char)0); // left expression
            rw = scanner.nextLine().toCharArray();

            int ll = 0; // left expression length in bits
            for(int i = 0; i < l; ++i)
                ll += lw[i] < 'a' ? 1 : c[lw[i] - 'a'];

            int rl = 0; // right expression length in bits
            for(int i = 0; i < r; ++i)
                rl += rw[i] < 'a' ? 1 : c[rw[i] - 'a'];

            // let's make sure a solution exists
            if(ll != rl) { // bitlengths don't match - it's unsolvable
                System.out.println(0);
                continue; // let's get to next query
            }

            // let's construct the graph
            std::unordered_multimap<int, int> g; // a multimap for our graph
            std::set<std::pair<int, int>> s; // we're using multimap, so we'll use this set to make sure we don't insert edges multiple times
            for(int i = 0, li = 0, lbi = 0, ri = 0, rbi = 0; i < ll; ++i) { // for every bit
                char lc = lw[li]; // left character
                char rc = rw[ri]; // right character
                int lcl = lc < 'a' ? 1 : c[lc - 'a']; // bitlength or left element
                int rcl = rc < 'a' ? 1 : c[rc - 'a']; // bitlength of right element
                int lp = (lc < 'a' ? lc - '0' : cc[lc - 'a'] + 2) + lbi; // unique id for left element bit
                int rp = (rc < 'a' ? rc - '0' : cc[rc - 'a'] + 2) + rbi; // unique id for right element bit

                // let's add this edge to our graph (representation is directed, so we must add both directed edges)
                // we're adding lp (lbi-th bit of li-th element (lc)) <-> rp (rbi-th bit of ri-th element (rc)) edge
                if(!s.count({lp, rp})) {
                    s.insert({lp, rp});
                    s.insert({rp, lp});
                    g.insert({lp, rp});
                    g.insert({rp, lp});
                }

                if(++lbi == lcl) { // go to next left character if we're dont with current one
                    lbi = 0;
                    ++li;
                }
                if(++rbi == rcl) { // go to next right character if we're dont with current one
                    rbi = 0;
                    ++ri;
                }
            }

            // let's find number of variable strongly connected components (containing no 0 nor 1)
            // and make sure no strongly connected component contains both 0 and 1
            int vscc = 0;
            int bitvariables = cc[k - 1] + c[k - 1] + 2; // +2 for '0' and '1'
            boolean visited[bitvariables]; // visited check for every variable bit
            for(int i = 0; i < bitvariables; ++i) // visited is dynamically sized, so let's initialize it manually for older compilers
                visited[i] = false;
            boolean gotoNext = false;
            for(int i = 0; i < bitvariables; ++i) { // for every node
                if(visited[i])
                    continue;
                boolean constants[2] = { false };
                std::stack<int> s; // DFS stack
                s.push(i);
                while(!s.empty()) {
                    int j = s.top(); s.pop();
                    if(j < 2) // it's either 0 or 1
                        constants[j] = true;
                    visited[j] = true; // we're visiting this node now, let's write it down
                    auto range = g.equal_range(j); // let's get all nodes leading from node on top of the dfs stack
                    for(auto it = range.first; it != range.second; ++it)
                        if(!visited[it->second]) // we haven't visited this one yet - let's add it to the dfs stack
                            s.push(it->second);
                }

                // let's make sure a solution exists
                if(constants[0] && constants[1]) { // we've found both 0 and 1 in a single strongly connected component
                    printf("0\n");
                    gotoNext = true;
                    break; // let's go to next query
                }

                // let's increment number of variable bits we need for our answer
                if(!constants[0] && !constants[1]) // we haven't found 0 nor 1, so it's a variable bit
                    ++vscc;

            }
            if(gotoNext)
                continue; // let's go to next query

            // let's print the answer
            // it's up to 2^N, so it's gonna be much bigger than what basic types can handle
            final int M = 3011; // 2^10000 has 3011 digits and that's our maximum answer
            char digits[M + 1] = { 0 }; // +1 for '\0' terminator
            int size = 1;
            digits[M - 2] = 1; // we're start with 2^0=1

            // multiply till we're done
            for(int i = 0; i < vscc; ++i) { // iterate until we reach required power
                int surplus = 0;
                for(int j = 0; j < size; ++j) { // for every digit in current value
                    digits[M - 2 - j] <<= 1; // multiply by two
                    digits[M - 2 - j] += surplus; // add surplus
                    surplus = digits[M - 2 - j] / 10; // save new surplus
                    digits[M - 2 - j] %= 10; // leave just units digit
                }
                if(surplus > 0) // we've got to increase the size
                    digits[M - 2 - size++] = surplus; // and initialize new digit
            }

            // now convert values to ascii and print the string
            for(int i = 0; i < size; ++i)
                digits[M - size + i - 1] += '0';
            printf("%s\n", (digits + M - size - 1));
        }
    */}
}
