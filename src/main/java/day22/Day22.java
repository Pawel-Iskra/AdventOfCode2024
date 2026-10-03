package day22;

import utils.MyUtils;

import java.util.List;

public class Day22 {


    private static final int TRANSFORMATIONS = 2000;
    private static final int FIRST_MULTIPLY_BY = 64;
    private static final int SECOND_MULTIPLY_BY = 2048;
    private static final int DIVIDE_BY = 32;
    private static final int MODULO_BY = 16777216;
    private static List<Long> INITIAL_SECRET_NUMBERS;


    private static long mix(long secretNumber, long value) {
        return secretNumber ^ value;
    }

    private static long prune(long secretNumber) {
        return secretNumber % MODULO_BY;
    }

    //Calculation = 1. *64 -> mix -> prune
    //              2. /32 -> mix -> prune
    //              3. *2048 -> mix -> prune
    private static long oneTransformation(long secretNumber) {
        secretNumber = prune(mix(secretNumber, secretNumber * FIRST_MULTIPLY_BY));
        secretNumber = prune(mix(secretNumber, secretNumber / DIVIDE_BY));
        secretNumber = prune(mix(secretNumber, secretNumber * SECOND_MULTIPLY_BY));
        return secretNumber;
    }

    private static void prepareData(List<String> inputLines) {
        INITIAL_SECRET_NUMBERS = inputLines.stream()
                .map(Long::parseLong)
                .toList();
    }

    private static void partOne() {
        long sumTwoThousandThSecretNumbers = 0;
        for (long currentSecretNumber : INITIAL_SECRET_NUMBERS) {
            long nextSecret = currentSecretNumber;
            for (int i = 0; i < TRANSFORMATIONS; i++) {
                nextSecret = oneTransformation(nextSecret);
            }
            sumTwoThousandThSecretNumbers += nextSecret;
        }
        System.out.println("PART I = " + sumTwoThousandThSecretNumbers);
    }


    private static void partTwo() {
        long counter = 0;

        System.out.println("PART II = " + counter);
    }


    static void main() {
        String pathToFile = "src/main/resources/2024.day22/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
