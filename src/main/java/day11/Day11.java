package day11;

import utils.MyUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Day11 {
    // TODO: fill dictionary in run ?


    private static String[] INITIAL_STAGE;
    private static int INITIAL_AMOUNT;
    private static int INITIAL_AMOUNT_IN_DIC;
    private static Map<Long, long[]> DICTIONARY = new HashMap<>(); // array=amount of stones after number of blinks
    private static BigInteger BIG_INT_2024 = new BigInteger("2024");


    private static List<String> goOneBlink(String stone) {
        List<String> resultList = new ArrayList<>();
        if (stone.equals("0")) {
            resultList.add("1");
            return resultList;
        }
        if (stone.length() % 2 == 0) {
            int half = stone.length() / 2;
            String leftStone = stone.substring(0, half);
            String rightStone = stone.substring(half);
            int left = Integer.parseInt(leftStone);
            int right = Integer.parseInt(rightStone);
            resultList.add(String.valueOf(left));
            resultList.add(String.valueOf(right));
            return resultList;
        }

        BigInteger resultStone = new BigInteger(stone).multiply(BIG_INT_2024);
        resultList.add(resultStone.toString());
        return resultList;
    }

    public static long goNumberOfBlinks(int blinksLeft, int part) {
        long counter = 0;
        List<String> resultList;
        for (int i = 0; i < INITIAL_AMOUNT; i++) {

            resultList = new ArrayList<>(List.of(INITIAL_STAGE));


            for (; blinksLeft >= 1; blinksLeft--) {


                List<String> fromOneBlink = new ArrayList<>();
                for (String currentStone : resultList) {

                    long stoneAsInt = Long.parseLong(currentStone);
                    if (DICTIONARY.containsKey(stoneAsInt) && blinksLeft <= INITIAL_AMOUNT_IN_DIC) {
                        counter += DICTIONARY.get(stoneAsInt)[blinksLeft];
                    } else {
                        List<String> resultFromOneStone = goOneBlink(currentStone);
                        fromOneBlink.addAll(resultFromOneStone);
                    }
                }
                resultList = new ArrayList<>(fromOneBlink);

                System.out.println("Part " + part + " - blinksLeft = " + blinksLeft);
                if (blinksLeft == 1) {
                    counter += fromOneBlink.size();
                }
            }
        }
        resultList = new ArrayList<>();
        return counter;
    }

    public static void partOne(int blinks) {
        System.out.println("START PART I.");
        System.out.println("PART I: " + goNumberOfBlinks(blinks,1));
    }

    public static void partTwo(int blinks) {
        System.out.println("START PART II.");
        System.out.println("PART II: " + goNumberOfBlinks(blinks,2));
    }

    private static void prepareData(final List<String> inputLines, int initialBlinksForDictionary, int forValues) {
        INITIAL_STAGE = inputLines.get(0).strip().split(" ");
        INITIAL_AMOUNT = INITIAL_STAGE.length;
        System.out.println("INITIAL_STAGE = " + Arrays.toString(INITIAL_STAGE));
        INITIAL_AMOUNT_IN_DIC = initialBlinksForDictionary;


        System.out.println("Start preparing DICTIONARY for values 0 -> " + forValues + ". Blinks from 0 to " + initialBlinksForDictionary);
        for (int i = 0; i <= forValues; i++) {
            long[] array = new long[initialBlinksForDictionary + 1];
            array[0] = 1;
            DICTIONARY.put((long) i, array);
        }

        List<String> resultList;
        for (int v = 0; v <= forValues; v++) {
            resultList = new ArrayList<>(List.of(String.valueOf(v)));

            for (int i = 1; i <= initialBlinksForDictionary; i++) {
                List<String> fromOneBlink = new ArrayList<>();
                for (String s : resultList) {
                    List<String> resultFromOneStone = goOneBlink(s);
                    fromOneBlink.addAll(resultFromOneStone);
                }
                resultList = new ArrayList<>(fromOneBlink);
                long[] currentArray = DICTIONARY.get((long) v);
                currentArray[i] = resultList.size();
                DICTIONARY.put((long) v, currentArray);
            }
            System.out.println("DICTIONARY for value: " + v + " is prepared.");

        }

        System.out.println("ALL DICTIONARY:");
        for (Map.Entry<Long, long[]> element : DICTIONARY.entrySet()) {
            System.out.print(element.getKey() + "->" + Arrays.toString(element.getValue()) + ", \n");
        }

    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day11/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines, 38, 9);
        partOne(25);
        partTwo(75);
    }
}
