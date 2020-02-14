package com.samsung.solutions;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.lang.Math.max;
import static java.lang.Math.min;

// SW Test Advanced 2020-01
//
// Wejscie:
// - W pierwszej linii znajduje sie K, liczba potrzebnych mineralow, a nastepnie K identyfikatorow liczbowych
//   kolejnych potrzebnych mineralow. 5 <= K <= 30; 1 <= Ki <= 30.
// - W drugiej linii znajduje sie N, liczba dostepnych kopaln. 5 <= N <= 15.
// - W kolejnych N liniach znajduje sie opis kolejnych kopaln skladajacy sie z ceny zakupu kopalni, 5 <= Pi <= 50,
//   liczby mineralow dostepnych w tej kopalni, 1 <= Mi <= 15 oraz Mi identyfikatorow kolejnych mineralow
//   1 <= Mi,j <= 30, w kolejnosci rosnacej.
// Wyjscie:
// - Pojedyncza liczba oznaczajaca minimalny koszt zakupu kopaln, ktore pokryja zbior wymaganych mineralow
//   lub -1 w przypadku, gdy pozyskanie wszystkich mineralow jest niemozliwe.

public class SWTAKopalnie {
    private static class Mine {
        public int price;
        public int minerals;
        public int[] mineral;
    }

    // it would be easier to do with a set, but let's pretend we don't have it and don't want to write it
    private static boolean meetsRequirements(int[] required, int[] bought, Mine[] mines) {
        int N = 31;
        int[] missing = new int[N];

        // set required mineral ids to 1
        for (int r : required)
            missing[r] = 1;

        // set mineral ids we have to 0
        for (int b : bought)
            for (int j = 0; j < mines[b].mineral.length; ++j)
                missing[mines[b].mineral[j]] = 0;

        // check if any required mineral is missing
        for(int i = 0; i < N; ++i)
            if(missing[i] != 0)
                return false;
        return true;
    }

    private static int getMinimumPrice(int[] required, int[] owned, Mine[] mines, int spent, int mineId) {
        // check if we've found a solution
        if(meetsRequirements(required, owned, mines))
            return spent; // since we're done buying anything more won't lower the cost

        // check if we've run out of mines to buy
        if(mineId >= mines.length)
            return -1;

        // get best result for both cases: when buying and when not buying currently considered mine
        // --> optimization possibility: check if buying currently considered mine adds any lacking but required mineral
        // --> optimization possibility: pre-compute which mines that are essential (contain a mineral that is required
        //                               and no other mine has it) and always buy them
        int[] newOwned = Arrays.copyOf(owned, owned.length + 1); // we'll make a copy since we change it
        newOwned[owned.length] = mineId;
        int whenBought = getMinimumPrice(required, newOwned, mines, spent + mines[mineId].price, mineId + 1);
        int whenNotBought = getMinimumPrice(required, owned, mines, spent, mineId + 1);

        // return best result
        if(whenBought == -1 || whenNotBought == -1)
            return max(whenBought, whenNotBought);
        return min(whenBought, whenNotBought);
    }

    public static void main(String[] args) {

        // read input
        Scanner scanner = new Scanner(System.in);
        int Required = scanner.nextInt();
        int[] required = new int[Required];
        for (int i = 0; i < Required; ++i)
            required[i] = scanner.nextInt();
        int Mines = scanner.nextInt();
        Mine[] mine = new Mine[Mines];
        for (int i = 0; i < Mines; ++i) {
            mine[i] = new Mine();
            mine[i].price = scanner.nextInt();
            mine[i].minerals = scanner.nextInt();
            mine[i].mineral = new int[mine[i].minerals];
            for (int j = 0; j < mine[i].minerals; ++j)
                mine[i].mineral[j] = scanner.nextInt();
        }

        // solve
        if(!meetsRequirements(required, IntStream.rangeClosed(0, Mines - 1).toArray(), mine)) // is it even possible?
            System.out.println(-1);
        else // find solution
            System.out.println(getMinimumPrice(required, new int[0], mine, 0, 0));
    }
}