package day01;

import utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 {

    public static void partOne(List<String> input) {
        List<Integer> lefts = new ArrayList<>();
        List<Integer> rights = new ArrayList<>();

        int amount = input.size();
        for (String line : input) {
            String[] lineAsArr = line.split("   ");
            int left = Integer.parseInt(lineAsArr[0].strip());
            int right = Integer.parseInt(lineAsArr[1].strip());
            lefts.add(left);
            rights.add(right);
        }

        Collections.sort(lefts);
        Collections.sort(rights);

        int sum = 0;
        for (int i = 0; i < amount; i++) {
            sum += Math.abs(lefts.get(i) - rights.get(i));
        }

        System.out.println("sum = " + sum);
    }

    public static void partTwo(List<String> input) {
        List<Integer> lefts = new ArrayList<>();
        List<Integer> rights = new ArrayList<>();

        int amount = input.size();
        for (String line : input) {
            String[] lineAsArr = line.split("   ");
            int left = Integer.parseInt(lineAsArr[0].strip());
            int right = Integer.parseInt(lineAsArr[1].strip());
            lefts.add(left);
            rights.add(right);
        }

        int sum = 0;
        for (int i = 0; i < amount; i++) {
            int leftOne = lefts.get(i);

            int currentSum = 0;
            for (int j = 0; j < amount; j++) {
                if (leftOne == rights.get(j)) {
                    currentSum++;
                }
            }
//            System.out.println("(currentSum*leftOne) = " + (currentSum * leftOne));
            sum += currentSum * leftOne;
        }
        System.out.println("sum = " + sum);
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day01/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        partOne(inputLines);
        partTwo(inputLines);
    }
}
