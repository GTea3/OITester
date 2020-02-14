package com.samsung.solutions;

import java.util.Scanner;

// https://szkopul.edu.pl/problemset/problem/Z1C91LB8rGYMxy6wRLBmbXba/site/
public class Browar {
    private static int n; // number of cities
    private static int[] distances; // distance between city i and i+1
    private static int[] orders; // order for city i

    private static int furthestRightIndex; // index of furthest city we're delivering to going right (clockwise)
    private static int furthestLeftIndex; // index of furthest city we're delivering to going left (counter-clockwise)
    private static int distanceRight; // distance to rightIndex city going right
    private static int distanceLeft; // distance to leftIndex city going left
    private static long ordersRight; // sum of orders for cities we're delivering to going right
    private static long ordersLeft; // sum of orders for cities we're delivering to going left
    private static long totalCost; // minimum total cost of transporting beer from currently considered city

    private static void CorrectLeftRightDivision() {
        while(distanceRight + distances[furthestRightIndex] < distanceLeft) { // let's move the left/right division right until distance to the right is not lower
            totalCost = totalCost + orders[furthestLeftIndex] * (distanceRight + distances[furthestRightIndex]) - orders[furthestLeftIndex] * distanceLeft;
            ordersRight += orders[furthestLeftIndex];
            ordersLeft -= orders[furthestLeftIndex];
            distanceRight += distances[furthestRightIndex];
            distanceLeft -= distances[furthestLeftIndex];
            furthestRightIndex = (furthestRightIndex + 1) % n;
            furthestLeftIndex = (furthestLeftIndex + 1) % n;
        }
    }

    public static void main(String[] args) {

        // Read input
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        distances = new int[n];
        orders = new int[n];
        for(int i = 0; i < n; ++i) {
            orders[i] = scanner.nextInt();
            distances[i] = scanner.nextInt();
        }

        furthestRightIndex = 0;
        furthestLeftIndex = 1;
        distanceRight = 0;
        distanceLeft = 0;
        ordersRight = 0;
        ordersLeft = 0;
        totalCost = 0;

        // Calculate initial cost for town 0
        for(int i = n - 1; i >= 1; --i) { // we start from the furthest city to the left, go left (counter-clockwise) and end in city 1 (the one left to city 0 because cost for current city is 0)
            distanceLeft += distances[i];
            ordersLeft += orders[i];
            totalCost += distanceLeft * orders[i];
        }
        CorrectLeftRightDivision();
        long minimumTotalCost = totalCost;

        // Calculate steps from i-1 to i and do corrections
        for(int i = 1; i < n; ++i) {
            int distance = distances[i - 1];
            totalCost = totalCost - ordersRight * distance + (ordersLeft + orders[i - 1]) * distance;
            ordersRight -= orders[i];
            ordersLeft += orders[i - 1];
            distanceRight -= distance;
            distanceLeft += distance;
            CorrectLeftRightDivision();
            minimumTotalCost = Math.min(minimumTotalCost, totalCost);
        }

        // Print best totalCost
        System.out.println(minimumTotalCost);
    }
}