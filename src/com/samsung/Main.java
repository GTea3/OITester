package com.samsung;

import com.samsung.tester.Tester;
import com.samsung.solutions.Plakatowanie;
import com.samsung.solutions.Krazki;
import com.samsung.solutions.Trojkaty;
import com.samsung.solutions.Lizak;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Boolean verbose = false;
        Tester.Test("Plakatowanie", Plakatowanie::main, verbose);
        Tester.Test("Krazki", Krazki::main, verbose);
        Tester.Test("Trojkaty", Trojkaty::main, verbose);
        Tester.Test("Lizak", Lizak::main, verbose);
    }
}
