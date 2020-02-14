package com.samsung;

import com.samsung.tester.Tester;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // What tests do you want to run?
        Tester.Tests test = Tester.Tests.SWTAAirplaneGame;

        // If you want to run just one single test, supply it's name. If not, leave the string empty.
        String singleTestName = "";

        // Do you want to see first received token that did not match expected one?
        Boolean verbose = true;

        // Do you want to see full output of your solution?
        Boolean fullOutput = false;

        // Do you want to stop testing as soon as a test fails?
        Boolean stopOnFirstFail = true;

        // Time limit multiplier (2.0f should be fine for Java)
        float timeLimitMultiplier = 3.0f;

        Tester.Test(test, singleTestName, verbose, fullOutput, stopOnFirstFail, timeLimitMultiplier);
    }
}
