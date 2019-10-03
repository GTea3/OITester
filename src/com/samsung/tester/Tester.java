package com.samsung.tester;

import com.samsung.solutions.Plakatowanie;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class Tester {
    public static void Test(String name, Consumer<String[]> f) throws IOException {
        System.out.println(name + "\n" + new String(new char[name.length()]).replace("\0", "-"));
        int correct = 0;
        int total = 0;
        String path = "resources/" + name;
        int longestTestNameLength = 0;
        for(String inputFile : Objects.requireNonNull((new File(path + "/in")).list()))
            longestTestNameLength = Math.max(longestTestNameLength, inputFile.length());
        longestTestNameLength -= ".in".length();
        long startTimeMs = System.currentTimeMillis();
        for(String inputFile : Objects.requireNonNull((new File(path + "/in")).list())) {
            String testName = String.format("%1$-" + (longestTestNameLength + 1) + "s", inputFile.replace(".in", ""));
            Scanner s = new Scanner(new File(path + "/limits/" + inputFile.replace(".in", ".limit")));
            long timeLimitMs = s.nextInt();
            s.close();
            correct += Test(f, testName, path + "/in/" + inputFile, path + "/out/" + inputFile.replace(".in", ".out"), timeLimitMs) ? 1 : 0;
            ++total;
        }
        long runTimeMs = System.currentTimeMillis() - startTimeMs;
        String summary = correct + "/" + total + " tests passed (total " +  runTimeMs + "ms)";
        System.out.println(new String(new char[summary.length()]).replace("\0", "-") + "\n" + summary + "\n");
    }

    private static boolean Test(Consumer<String[]> f, String testName, String input, String output, long timeLimitMs) throws IOException {
        PrintStream defaultSystemOut = System.out;
        InputStream defaultSystemIn = System.in;

        System.setIn(new InputStream() {
            FileReader fileReader = new FileReader(input);
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
        Scanner correct = new Scanner(new File(output));
        boolean timeLimitExceeded = runTimeMs > timeLimitMs;
        boolean correctAnswer = true;
        while(answer.hasNext() && correct.hasNext()) {
            if(answer.nextInt() != correct.nextInt()) {
                correctAnswer = false;
                break;
            }
        }
        if(answer.hasNext() || correct.hasNext())
            correctAnswer = false;

        System.out.println(testName + ": " + (correctAnswer ? "OK" : "wrong answer") + " (" + runTimeMs + "/" + timeLimitMs + "ms)" + (timeLimitExceeded ? " [time limit exceeded]" : ""));

        return correctAnswer && !timeLimitExceeded;
    }
}
