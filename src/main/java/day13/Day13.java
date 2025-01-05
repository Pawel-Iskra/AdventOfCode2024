package day13;

import utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {

    static class Data {
        int aX;
        int aY;
        int bX;
        int bY;
        int priceX;
        int priceY;

        Data(final int aX, final int aY, final int bX, final int bY, final int priceX, final int priceY) {
            this.aX = aX;
            this.aY = aY;
            this.bX = bX;
            this.bY = bY;
            this.priceX = priceX;
            this.priceY = priceY;
        }

        int getaX() {
            return aX;
        }

        int getaY() {
            return aY;
        }

        int getbX() {
            return bX;
        }

        int getbY() {
            return bY;
        }

        int getPriceX() {
            return priceX;
        }

        int getPriceY() {
            return priceY;
        }

        @Override
        public String toString() {
            return "\nData{" +
                    "aX=" + aX +
                    ", aY=" + aY +
                    ", bX=" + bX +
                    ", bY=" + bY +
                    ", priceX=" + priceX +
                    ", pricey=" + priceY +
                    '}';
        }
    }


    private static List<Data> DATA_LIST;
    private static int NUMBER_OF_SETS;
    private static int A_TOKENS = 3;
    private static int B_TOKENS = 1;
    private static int MAX_BUTTON_PUSHES = 100;
    //    private static BigInteger HIGHER = new BigInteger("10000000000000");
    private static long MORE = 10000000000000L;


    public static void partOne() {
        System.out.println("\nPART I:");
        List<Integer>[] prizes = new ArrayList[NUMBER_OF_SETS];

        int[] lowestPrizes = new int[NUMBER_OF_SETS];
        Arrays.fill(lowestPrizes, Integer.MAX_VALUE);

        for (int k = 0; k < NUMBER_OF_SETS; k++) {
            Data currentDataSet = DATA_LIST.get(k);
            int currentPrizeX = currentDataSet.getPriceX();
            int currentPrizeY = currentDataSet.getPriceY();
            int currentAx = currentDataSet.getaX();
            int currentAy = currentDataSet.getaY();
            int currentBx = currentDataSet.getbX();
            int currentBy = currentDataSet.getbY();
            List<Integer> currentPrizeList = new ArrayList<>();
            for (int i = 1; i <= MAX_BUTTON_PUSHES; i++) {
                for (int j = 1; j <= MAX_BUTTON_PUSHES; j++) {
                    int currentLowestPrize = lowestPrizes[k];
                    int currentMoveX = currentAx * i + currentBx * j;
                    int currentMoveY = currentAy * i + currentBy * j;
                    if (currentPrizeX == currentMoveX && currentPrizeY == currentMoveY) {
                        int currentPrize = i * A_TOKENS + j * B_TOKENS;
                        if (currentPrize < currentLowestPrize) {
                            lowestPrizes[k] = currentPrize;
                        }
                        currentPrizeList.add(currentPrize);
                    }

                }
            }
            prizes[k] = currentPrizeList;
        }

        System.out.println("prizes = " + Arrays.toString(prizes));
        System.out.println("lowestPrizes = " + Arrays.toString(lowestPrizes));
        int sumOfLowestPrizes = 0;
        for (int i = 0; i < lowestPrizes.length; i++) {
            int currentLowestPrize = lowestPrizes[i];
            if (currentLowestPrize != Integer.MAX_VALUE) {
                sumOfLowestPrizes += currentLowestPrize;
            }
        }
        System.out.println("sumOfLowestPrizes = " + sumOfLowestPrizes);
    }

    public static void partTwo() {
        System.out.println("\n\nPART II:");
        List<Long>[] prizes = new ArrayList[NUMBER_OF_SETS];

        long[] lowestPrizes = new long[NUMBER_OF_SETS];
        Arrays.fill(lowestPrizes, Long.MAX_VALUE);

        for (int k = 0; k < NUMBER_OF_SETS; k++) {
            List<Long> currentPrizeList = new ArrayList<>();

            Data currentDataSet = DATA_LIST.get(k);
            long currentPrizeX = currentDataSet.getPriceX() + MORE;
            long currentPrizeY = currentDataSet.getPriceY() + MORE;
            long currentAx = currentDataSet.getaX();
            long currentAy = currentDataSet.getaY();
            long currentBx = currentDataSet.getbX();
            long currentBy = currentDataSet.getbY();

            long a = (currentPrizeY * currentBx - currentBy * currentPrizeX) / (currentAy * currentBx - currentBy * currentAx);
            long b = (currentPrizeX - currentAx * a) / currentBx;

            boolean isRightX = currentPrizeX == currentAx * a + currentBx * b;
            boolean isRightY = currentPrizeY == currentAy * a + currentBy * b;


            if (!isRightX || !isRightY) continue;


            long currentLowestPrize = lowestPrizes[k];
            long currentPrize = a * A_TOKENS + b * B_TOKENS;
            if (currentPrize < currentLowestPrize) {
                lowestPrizes[k] = currentPrize;
            }
            currentPrizeList.add(currentPrize);
            prizes[k] = currentPrizeList;
        }


        System.out.println("prizes = " + Arrays.toString(prizes));
        System.out.println("lowestPrizes = " + Arrays.toString(lowestPrizes));
        long sumOfLowestPrizes = 0;
        for (int i = 0; i < lowestPrizes.length; i++) {
            long currentLowestPrize = lowestPrizes[i];
            if (currentLowestPrize != Long.MAX_VALUE) {
                sumOfLowestPrizes += currentLowestPrize;
            }
        }
        System.out.println("sumOfLowestPrizes = " + sumOfLowestPrizes);
    }

    private static void prepareData(final List<String> inputLines) {
        NUMBER_OF_SETS = (inputLines.size() + 1) / 4;
        DATA_LIST = new ArrayList();
        for (int i = 0; i < NUMBER_OF_SETS; i++) {

            String currentButtonA = (inputLines.get(i * 4));
            String currentButtonB = inputLines.get(i * 4 + 1);
            String currentPrize = inputLines.get(i * 4 + 2);

            String[] currentAasArray = currentButtonA.substring(currentButtonA.indexOf(":")).split(",");
            String[] currentBasArray = currentButtonB.substring(currentButtonB.indexOf(":")).split(",");
            String[] currentPrizeAsArray = currentPrize.substring(currentPrize.indexOf(":")).split(",");

            int aX = Integer.parseInt(currentAasArray[0].replaceAll("[^\\d.]", ""));
            int aY = Integer.parseInt(currentAasArray[1].replaceAll("[^\\d.]", ""));
            int bX = Integer.parseInt(currentBasArray[0].replaceAll("[^\\d.]", ""));
            int bY = Integer.parseInt(currentBasArray[1].replaceAll("[^\\d.]", ""));

            int prizeX = Integer.parseInt(currentPrizeAsArray[0].replaceAll("[^\\d.]", ""));
            int prizeY = Integer.parseInt(currentPrizeAsArray[1].replaceAll("[^\\d.]", ""));

            DATA_LIST.add(new Data(aX, aY, bX, bY, prizeX, prizeY));
        }
        System.out.println("DATA_LIST = " + DATA_LIST);
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day13/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
