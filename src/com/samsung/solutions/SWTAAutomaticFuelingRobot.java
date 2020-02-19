package com.samsung.solutions;

import java.util.Scanner;

public class SWTAAutomaticFuelingRobot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] cars = new int[n + 2];
        for(int i = 0; i < n; ++i)
            cars[i + 1] = scanner.nextInt();
        int gasCars = 0;
        int dieselCars = 0;
        for(int car : cars)
            if(car == 1)
                ++gasCars;
            else if(car == 2)
                ++dieselCars;
        int initialCost = gasCars > 0 ? 0 : n + 1;
        int initialPosition = gasCars > 0 ? 0 : n + 1;
        System.out.println(initialCost + solve(n, cars, initialPosition, gasCars, dieselCars));
    }

    // Proposed solution - it seems to be correct, but I'm not sure - TODO: PROVE CORRECTNESS
    // invariant: we're at one of the stations and there are cars of this station's fuel type that need fueling
    // option 1: from station jump to closest one fuel-matching (fuel it) and return
    // option 2: from station jump to furthest one fuel-matching (fuel this one and 2nd furthest one) and branch between returning to both stations (making sense)
    // option 3: from station jump to 2nd closest one fuel-matching (fuel this one and 2nd furthest one) and branch between returning to both stations (making sense)

    private static int solve(int n, int[] cars, int startingPosition, int gasCars, int dieselCars) {
        if (gasCars == 0 && dieselCars == 0)
            return 0;

        // find closest car and 2 furthest cars
        int[] furthest = new int[] { -1, -1 };
        int[] closest = new int[] { -1, -1 };
        for (int p = startingPosition; p >= 0 && p < cars.length; p = startingPosition == 0 ? p + 1 : p - 1) {
            if ((startingPosition == 0 && cars[p] == 1) || (startingPosition == n + 1 && cars[p] == 2)) {
                if (closest[0] == -1)
                    closest[0] = p;
                else if(closest[1] == -1) {
                    closest[1] = p;
                }
                furthest[1] = furthest[0];
                furthest[0] = p;
            }
        }

        int best = Integer.MAX_VALUE;

        // go to closest
        {
            int[] cars2 = cars.clone();
            cars2[closest[0]] = 0;
            int gasCars2 = startingPosition == 0 ? gasCars - 1 : gasCars;
            int dieselCars2 = startingPosition == n + 1 ? dieselCars - 1 : dieselCars;
            int cost = Math.abs(startingPosition - closest[0]);

            // we're done
            if(gasCars2 == 0 && dieselCars2 == 0)
                return cost;

            // return
            if((startingPosition == 0 && gasCars2 > 0) || (startingPosition == n + 1 && dieselCars2 > 0))
                best = Math.min(best, 2 * cost + solve(n, cars2, startingPosition, gasCars2, dieselCars2));

            // go to other station
            if((startingPosition == n + 1 && gasCars2 > 0) || (startingPosition == 0 && dieselCars2 > 0))
                best = Math.min(best, (n + 1) + solve(n, cars2, startingPosition == 0 ? n + 1 : 0, gasCars2, dieselCars2));
        }

        // go to 2nd closest
        if(closest[1] != -1) {
            int[] cars2 = cars.clone();
            cars2[closest[0]] = 0;
            cars2[closest[1]] = 0;
            int gasCars2 = startingPosition == 0 ? gasCars - 2 : gasCars;
            int dieselCars2 = startingPosition == n + 1 ? dieselCars - 2 : dieselCars;
            int cost = Math.abs(startingPosition - closest[1]);

            // we're done
            if(gasCars2 == 0 && dieselCars2 == 0)
                return cost;

            // return
            if((startingPosition == 0 && gasCars2 > 0) || (startingPosition == n + 1 && dieselCars2 > 0))
                best = Math.min(best, 2 * cost + solve(n, cars2, startingPosition, gasCars2, dieselCars2));

            // go to other station
            if((startingPosition == n + 1 && gasCars2 > 0) || (startingPosition == 0 && dieselCars2 > 0))
                best = Math.min(best, (n + 1) + solve(n, cars2, startingPosition == 0 ? n + 1 : 0, gasCars2, dieselCars2));
        }

        // go to furthest
        if(furthest[1] != -1) {
            int[] cars2 = cars.clone();
            cars2[furthest[0]] = 0;
            cars2[furthest[1]] = 0;
            int gasCars2 = startingPosition == 0 ? gasCars - 2 : gasCars;
            int dieselCars2 = startingPosition == n + 1 ? dieselCars - 2 : dieselCars;
            int cost = Math.abs(startingPosition - furthest[0]);

            // we're done
            if(gasCars2 == 0 && dieselCars2 == 0)
                return cost;

            // return
            if((startingPosition == 0 && gasCars2 > 0) || (startingPosition == n + 1 && dieselCars2 > 0))
                best = Math.min(best, 2 * cost + solve(n, cars2, startingPosition, gasCars2, dieselCars2));

            // go to other station
            if((startingPosition == n + 1 && gasCars2 > 0) || (startingPosition == 0 && dieselCars2 > 0))
                best = Math.min(best, (n + 1) + solve(n, cars2, startingPosition == 0 ? n + 1 : 0, gasCars2, dieselCars2));
        }

        if(best == Integer.MAX_VALUE) {
            System.out.print(startingPosition + ":");
            for(int i = 1; i < n + 1; ++i)
                System.out.print(cars[i]);
            System.out.print(":" + closest + "." + furthest[0] + "." + furthest[1] + ":" + gasCars + "." + dieselCars + "\n");
        }
        return best;
    }
}
