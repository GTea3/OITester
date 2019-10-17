package com.samsung;

import com.samsung.solutions.Plakatowanie;
import com.samsung.solutions.Krazki;
import com.samsung.solutions.Trojkaty;
import com.samsung.tester.Tester;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Tester.Test("Plakatowanie", Plakatowanie::main);
        Tester.Test("Krazki", Krazki::main);
        Tester.Test("Trojkaty", Trojkaty::main);
    }
}
