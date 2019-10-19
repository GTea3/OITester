// TODO: Terminate after time limit timeout: spawn on separate thread?

package com.samsung.tester;

import com.samsung.solutions.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Consumer;

public class Tester {
    public enum Tests {
        All,
        Lizak,
        //Minusy,
        Trojkaty,
        //Antypierwsze,
        Krazki,
        //Browar,
        Plakatowanie,
        //Tetris,
        Rezerwacja
    }

    public static void Test(Tests test, Boolean verbose) throws IOException {
        Map<Tests, Consumer<String[]>> tests = GetTests();
        if(test == Tests.All) {
            for (Map.Entry<Tests, Consumer<String[]>> entry : tests.entrySet()) {
                Tester.Test(entry.getKey().name(), entry.getValue(), verbose);
            }
        }
        else {
            Tester.Test(test.name(), tests.get(test), verbose);
        }
    }

    private static Map<Tests, Consumer<String[]>> GetTests() {
        return new TreeMap<Tests, Consumer<String[]>>() {
            {
                for(Tests test : Tests.values()) {
                    if(test == Tests.All)
                        continue;

                    put(test, new Consumer<String[]>() {
                        @Override
                        public void accept(String[] strings) {
                            try {
                                Class.forName("com.samsung.solutions." + test.name()).getMethod("main", String[].class).invoke(null, (Object) strings);
                            } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        };
    }

    public static void Test(String name, Consumer<String[]> f) throws IOException {
        Test(name, f, false);
    }

    private static void Test(String name, Consumer<String[]> f, Boolean verbose) throws IOException {
        System.out.println(name + "\n" + new String(new char[name.length()]).replace("\0", "-"));
        int correct = 0;
        int total = 0;
        Path path = Paths.get("resources", name);
        int longestTestNameLength = 0;
        for(String inputFile : Objects.requireNonNull((new File(Paths.get(path.toString(),"in").toString())).list()))
            longestTestNameLength = Math.max(longestTestNameLength, inputFile.length());
        longestTestNameLength -= ".in".length();
        long startTimeMs = System.currentTimeMillis();
        for(String inputFile : Objects.requireNonNull((new File(Paths.get(path.toString(),"in").toString())).list())) {
            String testName = String.format("%1$-" + (longestTestNameLength + 1) + "s", inputFile.replace(".in", ""));
            Scanner s = new Scanner(new File(Paths.get(path.toString(), "limits", inputFile.replace(".in", ".limit")).toString()));
            long timeLimitMs = s.nextInt() * 2;
            s.close();
            correct += Test(f, testName, Paths.get(path.toString(), "in", inputFile), Paths.get(path.toString(), "out", inputFile.replace(".in", ".out")), timeLimitMs, verbose) ? 1 : 0;
            ++total;
        }
        long runTimeMs = System.currentTimeMillis() - startTimeMs;
        String summary = correct + "/" + total + " tests passed (total " +  runTimeMs + "ms)";
        System.out.println(new String(new char[summary.length()]).replace("\0", "-") + "\n" + summary + "\n");
    }

    private static boolean Test(Consumer<String[]> f, String testName, Path input, Path output, long timeLimitMs, Boolean verbose) throws IOException {
        PrintStream defaultSystemOut = System.out;
        InputStream defaultSystemIn = System.in;

        System.setIn(new InputStream() {
            FileReader fileReader = new FileReader(input.toString());
            @Override
            public int read() throws IOException {
                return fileReader.read();
            }
        });
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);

        long startTimeMs = System.currentTimeMillis();
        f.accept(new String[0]);
        long runTimeMs = System.currentTimeMillis() - startTimeMs;

        System.setOut(defaultSystemOut);
        System.setIn(defaultSystemIn);

        Scanner answer = new Scanner(byteArrayOutputStream.toString());
        Scanner correct = new Scanner(new File(output.toString()));
        boolean timeLimitExceeded = runTimeMs > timeLimitMs;
        boolean correctAnswer = true;
        while(answer.hasNext() && correct.hasNext()) {
            var received = answer.next();
            var expected = correct.next();
            if(!received.equals(expected)) {
                if(verbose)
                    System.out.println("\nReceived: " + received + "\nExpected: " + expected);
                correctAnswer = false;
                break;
            }
        }
        if(answer.hasNext() || correct.hasNext()) {
            if(verbose)
                System.out.println("\nIncorrect answer size.");
            correctAnswer = false;
        }

        System.out.println(testName + ": " + (correctAnswer ? "OK" : "wrong answer") + " (" + runTimeMs + "/" + timeLimitMs + "ms)" + (timeLimitExceeded ? " [time limit exceeded]" : ""));

        return correctAnswer && !timeLimitExceeded;
    }
}
