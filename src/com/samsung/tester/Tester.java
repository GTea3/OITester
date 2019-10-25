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
        Minusy,
        Trojkaty,
        Antypierwsze, // TODO: correct time limits

        // Koszt zamortyzowany
        Krazki,
        //Browar, // not implemented

        // Stos
        Plakatowanie,
        //Tetris, // not implemented

        // Przeszukiwanie grafow
        //Rownanie, // not implemented
        //Jedynki, // not implemented
        //Agenci, // not implemented

        // Algorytmy zachlanne
        //Szeregowanie, // not implemented
        //Rozklad, // not implemented

        // Programowanie dynamiczne I
        //Rezerwacja, // implemented, not yet available
        //Roznica, // not implemented
        //Zajakniecia, // not implemented

        // Drzewa
        //Dostawca, // not implemented
        //Luk, // not implemented
        //Wielokat, // not implemented

        // Algorytmy grafowe I
        //Odleglosc, // not implemented
        //Zawody, // not implemented
        //Dziuple, // not implemented
        //Zabka, // not implemented
    }

    public static void Test(Tests test, Boolean verbose, Boolean stopOnFirstFail, float timeLimitMultiplier) throws IOException {
        Map<Tests, Consumer<String[]>> tests = GetTests();
        if(test == Tests.All) {
            for (Map.Entry<Tests, Consumer<String[]>> entry : tests.entrySet()) {
                Tester.Test(entry.getKey().name(), entry.getValue(), verbose, stopOnFirstFail, timeLimitMultiplier);
            }
        }
        else {
            Tester.Test(test.name(), tests.get(test), verbose, stopOnFirstFail, timeLimitMultiplier);
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

    private static void Test(String name, Consumer<String[]> f, Boolean verbose, Boolean stopOnFirstFail, float timeLimitMultiplier) throws IOException {
        System.out.println(name + "\n" + new String(new char[name.length()]).replace("\0", "-"));
        int correct = 0;
        int total = 0;
        Path path = Paths.get("resources", name);
        int longestTestNameLength = 0;
        String[] inputFiles1 = Objects.requireNonNull((new File(Paths.get(path.toString(),"in").toString())).list());
        List<String> inputFiles = Arrays.asList(inputFiles1);
        Collections.sort(inputFiles, new Comparator<String>() {
            public int compare(String o1, String o2) {
                int o1int = extractInt(o1);
                int o2int = extractInt(o2);
                if(o1int != o2int)
                    return extractInt(o1) - extractInt(o2);
                return o1.compareTo(o2);
            }
            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num); // return 0 if no digits found
            }
        });
        for(String inputFile : inputFiles)
            longestTestNameLength = Math.max(longestTestNameLength, inputFile.length());
        longestTestNameLength -= ".in".length();
        long startTimeMs = System.currentTimeMillis();
        for(String inputFile : inputFiles) {
            String testName = String.format("%1$-" + (longestTestNameLength + 1) + "s", inputFile.replace(".in", ""));
            Scanner s = new Scanner(new File(Paths.get(path.toString(), "limits", inputFile.replace(".in", ".limit")).toString()));
            long timeLimitMs = (long)(s.nextInt() * timeLimitMultiplier);
            s.close();
            if(IsCustomValidated(name))
                correct += Test(f, testName, Paths.get(path.toString(), "in", inputFile), name, timeLimitMs, verbose) ? 1 : 0;
            else
                correct += Test(f, testName, Paths.get(path.toString(), "in", inputFile), Paths.get(path.toString(), "out", inputFile.replace(".in", ".out")), timeLimitMs, verbose) ? 1 : 0;
            ++total;
            if(stopOnFirstFail && correct < total)
                break;
        }
        long runTimeMs = System.currentTimeMillis() - startTimeMs;
        String summary = correct + "/" + total + " tests passed (total " +  runTimeMs + "ms)" + (stopOnFirstFail && correct < total ? (", executed " + total + "/" + inputFiles.size() + " tests before first fail") : "");
        System.out.println(new String(new char[summary.length()]).replace("\0", "-"));
        System.out.println(summary + "\n");
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
            correctAnswer = (Boolean) Class.forName("com.samsung.validators." + name).getMethod("validate", Scanner.class, Scanner.class).invoke(null, answer, inputContents);
        } catch (Exception e) {
            if(verbose)
                System.out.println("\n" + e.getCause().toString().replace("java.lang.Exception: ", ""));
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
            String received = answer.next();
            String expected = correct.next();
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
