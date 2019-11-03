package com.samsung;

import com.samsung.tester.Tester;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // What tests do you want to run?
        Tester.Tests test = Tester.Tests.All;

        // Do you want to see first received token that did not match expected one?
        Boolean verbose = true;

        // Do you want to stop testing as soon as a test fails?
        Boolean stopOnFirstFail = true;

        // Time limit multiplier (2.0f should be fine for Java)
        float timeLimitMultiplier = 3.0f;

        Tester.Test(test, verbose, stopOnFirstFail, timeLimitMultiplier);
    }
}
