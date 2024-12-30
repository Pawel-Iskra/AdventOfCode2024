package day07;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;

public class Day07 {
    // TODO: PERMUTATIONS

    private static int ROWS;
    private static Map<String, List<Long>> LINES_AS_MAP = new HashMap();
    private static List<Long> RESULTS = new ArrayList<>();
    private static List<List<Long>> NUMBERS = new ArrayList<>();
    private static String MUL = "*";
    private static String ADD = "+";
    private static String CONCAT = "||";

    public static void partOne(List<String> inputLines) {
        long sum = 0;
//        for (String key : LINES_AS_MAP.keySet()) {
//            if (checkIfEquationIsPossible(key, LINES_AS_MAP.get(key))) {
//                sum += Long.parseLong(key);
//            }
//        }

        for (int i = 0; i < RESULTS.size(); i++) {
            long result = RESULTS.get(i);
            List<Long> numbers = NUMBERS.get(i);
            if (checkIfEquationIsPossible(result, numbers)) {
                sum += result;
            }
        }

        System.out.println("sum = " + sum);
    }
    // 1522666695216 - too low
    // 2650123272335 - too low
    // 2664459966384
    // 2664459966384
    // 2664459966384 - too low - dla i=700000
    // 2664459966384 dla i = 800000
    // 2664459966384 dla i = 900000
    //                2664459966384
    // i = 1000000 => 2664459966384
    // i = 1500000 => 2664459966384
    // i = 4000000 => 2664459966384
    // i = 5000000 =>

    // solution based on lists :P
    // i = 10000 ===> 2664460013123 -> OK

    private static boolean checkIfEquationIsPossible(long result, List<Long> numbers) {
        int amount = numbers.size();
        long currentResult = numbers.get(0);
//        long resultLong = Long.parseLong(result);

        Random ran = new Random();
        String operator;
        for (int j = 0; j < 1000000; j++) {
            currentResult = numbers.get(0);
            for (int i = 1; i < amount; i++) {
                int randomNum = ran.nextInt(10);
                if (randomNum % 3 == 0) { // part 1 -> %2   , for part2 -> %3
                    operator = MUL;
                } else if (randomNum % 3 == 1) {
                    operator = ADD;
                } else {
                    operator = CONCAT;
                }
//                System.out.println("currentResult = " + currentResult);
                currentResult = getResult(currentResult, numbers.get(i), operator);

                if (currentResult > result) break;
            }
            if (currentResult == result) {
                return true;
            }
        }


        return false;
    }

    private static long getResult(final long currentResult, final long number, final String operator) {
        if (operator.equals(MUL)) return currentResult * number;
        if (operator.equals(ADD)) return currentResult + number;
        if (operator.equals(CONCAT)) {
            StringBuilder strb = new StringBuilder();
            strb.append(currentResult).append(number);
            return Long.parseLong(strb.toString());
        }
        return currentResult;
    }


    public static void partTwo(List<String> inputLines) {

    }
    // i=1000   -> 34199709573518 - too low
    // i=10000  -> 88352315457451 - too low
    // i=500000 -> 419883253475755 - too low
    // i=1000000-> 426214131924213 -> OK!

    private static void prepareData(final List<String> inputLines) {
        ROWS = inputLines.size();
        for (int i = 0; i < ROWS; i++) {
            String[] line = inputLines.get(i).split(":");
            String result = line[0];
            String[] numbers = line[1].strip().split(" ");
            List<Long> numbersAsList = new ArrayList<>();
            for (int j = 0; j < numbers.length; j++) {
                numbersAsList.add(Long.parseLong(numbers[j]));
            }
            RESULTS.add(Long.valueOf(result));
            NUMBERS.add(numbersAsList);
            LINES_AS_MAP.put(result, numbersAsList);
        }
        System.out.println("LINES_AS_MAP.size() = " + LINES_AS_MAP.size());
        System.out.println("MAP:");
        System.out.println("LINES_AS_MAP = " + LINES_AS_MAP);
    }


    public static void main(String[] args) throws IOException {
        // TODO: PERMUTATIONS
        String pathToInputFile = "src/main/resources/2024.day07/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne(inputLines);
        partTwo(inputLines);
    }
}
