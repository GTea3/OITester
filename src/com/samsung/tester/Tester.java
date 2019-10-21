// TODO: Terminate after time limit timeout: spawn on separate thread?

package com.samsung.tester;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public class Tester {
    public enum Tests {
        // Wszystkie
        All,

        // Rozgrzewka
        Lizak,
        //Minusy,
        Trojkaty,
        //Antypierwsze,

        // Koszt zamortyzowany
        Krazki,
        //Browar,

        // Stos
        Plakatowanie,
        //Tetris,

        // Przeszukiwanie grafow
        //Rownanie,
        //Jedynki,
        //Agenci,

        // Algorytmy zachlanne
        //Szeregowanie,
        //Rozklad,

        // Programowanie dynamiczne I
        Rezerwacja,
        //Roznica,
        //Zajakniecia,

        // Drzewa
        //Dostawca,
        //Luk,
        //Wielokat,

        // Algorytmy grafowe I
        //Odleglosc,
        //Zawody,
        //Dziuple,
        //Zabka,
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
            if(IsCustomValidated(name))
                correct += Test(f, testName, Paths.get(path.toString(), "in", inputFile), name, timeLimitMs, verbose) ? 1 : 0;
            else
                correct += Test(f, testName, Paths.get(path.toString(), "in", inputFile), Paths.get(path.toString(), "out", inputFile.replace(".in", ".out")), timeLimitMs, verbose) ? 1 : 0;
            ++total;
        }
        long runTimeMs = System.currentTimeMillis() - startTimeMs;
        String summary = correct + "/" + total + " tests passed (total " +  runTimeMs + "ms)";
        System.out.println(new String(new char[summary.length()]).replace("\0", "-") + "\n" + summary + "\n");
    }

    private static boolean IsCustomValidated(String name) {
        try {
            Class.forName("com.samsung.validators." + name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static List<Object> TestConsumer(Consumer<String[]> f, Path input)  throws IOException {
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

        return new ArrayList<Object>() {
            {
                add(byteArrayOutputStream);
                add(runTimeMs);
            }
        };
    }

    // validate answer with custom validator
    private static boolean Test(Consumer<String[]> f, String testName, Path input, String name, long timeLimitMs, Boolean verbose) throws IOException {
        List<Object> results = TestConsumer(f, input);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) results.get(0);
        long runTimeMs = (long) results.get(1);

        boolean timeLimitExceeded = runTimeMs > timeLimitMs;
        Scanner answer = new Scanner(byteArrayOutputStream.toString());
        Scanner inputContents = new Scanner(new File(input.toString()));
        boolean correctAnswer = false;
        try {
            correctAnswer = (Boolean) Class.forName("com.samsung.validators." + name).getMethod("validate", Scanner.class, Scanner.class, boolean.class).invoke(null, answer, inputContents, verbose);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(testName + ": " + (correctAnswer ? "OK" : "wrong answer") + " (" + runTimeMs + "/" + timeLimitMs + "ms)" + (timeLimitExceeded ? " [time limit exceeded]" : ""));

        return correctAnswer && !timeLimitExceeded;
    }

    // check against correct answer in output file
    private static boolean Test(Consumer<String[]> f, String testName, Path input, Path output, long timeLimitMs, Boolean verbose) throws IOException {
        List<Object> results = TestConsumer(f, input);
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) results.get(0);
        long runTimeMs = (long) results.get(1);

        boolean timeLimitExceeded = runTimeMs > timeLimitMs;
        Scanner answer = new Scanner(byteArrayOutputStream.toString());
        Scanner correct = new Scanner(new File(output.toString()));
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
