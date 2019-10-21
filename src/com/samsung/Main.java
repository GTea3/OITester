package com.samsung;

import com.samsung.tester.Tester;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // What tests do you want to run?
        Tester.Tests test = Tester.Tests.All;

        // Do you want to see first received token that did not match expected one?
        Boolean verbose = false;

        // Do you want to stop testing as soon as a test fails?
        Boolean stopOnFirstFail = true;

        Tester.Test(test, verbose, stopOnFirstFail);
    }
}
