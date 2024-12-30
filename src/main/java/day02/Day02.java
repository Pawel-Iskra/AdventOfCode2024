package day02;

import utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day02 {

    public static void partOne(List<String> input) {
        List<Integer> lefts = new ArrayList<>();
        List<Integer> rights = new ArrayList<>();

        int amount = input.size();
        int cols;
        int safeNumbers = 0;
        for (String line : input) {
            String[] lineArr = line.split(" ");
            cols = lineArr.length;

            boolean isSafe = true;
            boolean isIncreasing = true;
            int veryfirst = Integer.parseInt(lineArr[0]);
            int verysecond = Integer.parseInt(lineArr[1]);
            isIncreasing = (verysecond - veryfirst) > 0;

            for (int i = 0; i < (cols - 1); i++) {
                int first = Integer.parseInt(lineArr[i]);
                int second = Integer.parseInt(lineArr[i + 1]);
                int diff = second - first;
                if (isIncreasing != (diff > 0)) {
                    isSafe = false;
                    break;
                }
                int diffAbs = Math.abs(diff);
                if (diffAbs < 1 || diffAbs > 3) {
                    isSafe = false;
                    break;
                }
            }
            if (isSafe) safeNumbers++;
        }
        System.out.println("safeNumbers = " + safeNumbers);
    }

    public static void partTwo(List<String> input) {

        int safeNumbers = 0;
        for (String line : input) {
            String[] lineArr = line.split(" ");
            int cols = lineArr.length;

            boolean isLineSafe = isSafe(lineArr);
            if (isLineSafe) {
                safeNumbers++;
            } else {
                String[] newLine = new String[cols - 1];
                for (int i = 0; i < cols; i++) {
                    int index = 0;
                    for (int j = 0; j < cols; j++) {
                        if (j == i) {
                            continue;
                        } else {
                            newLine[index] = lineArr[j];
                            index++;
                        }
                    }

                    isLineSafe = isSafe(newLine);
                    if (isLineSafe) {
                        safeNumbers++;
                        break;
                    }
                }
            }
        }
        System.out.println("safeNumbers = " + safeNumbers);


    }

    public static boolean isSafe(String[] line) {
        int cols = line.length;
        boolean isSafe = true;
        boolean isIncreasing = true;

        int veryfirst = Integer.parseInt(line[0]);
        int verysecond = Integer.parseInt(line[1]);
        isIncreasing = (verysecond - veryfirst) > 0;

        for (int i = 0; i < (cols - 1); i++) {
            int first = Integer.parseInt(line[i]);
            int second = Integer.parseInt(line[i + 1]);
            int diff = second - first;
            if (isIncreasing != (diff > 0)) {
                isSafe = false;
                break;
            }
            int diffAbs = Math.abs(diff);
            if (diffAbs < 1 || diffAbs > 3) {
                isSafe = false;
                break;
            }
        }


        return isSafe;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day02/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        partOne(inputLines);
        partTwo(inputLines);
    }
}
