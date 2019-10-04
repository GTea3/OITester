// TODO: Terminate after time limit timeout: spawn on separate thread?

package com.samsung.tester;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class Tester {
    public static void Test(String name, Consumer<String[]> f) throws IOException {
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
            correct += Test(f, testName, Paths.get(path.toString(), "in", inputFile), Paths.get(path.toString(), "out", inputFile.replace(".in", ".out")), timeLimitMs) ? 1 : 0;
            ++total;
        }
        long runTimeMs = System.currentTimeMillis() - startTimeMs;
        String summary = correct + "/" + total + " tests passed (total " +  runTimeMs + "ms)";
        System.out.println(new String(new char[summary.length()]).replace("\0", "-") + "\n" + summary + "\n");
    }

    private static boolean Test(Consumer<String[]> f, String testName, Path input, Path output, long timeLimitMs) throws IOException {
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
