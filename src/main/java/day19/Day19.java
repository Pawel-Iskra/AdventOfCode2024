package day19;

import utils.MyUtils;

import java.util.Arrays;
import java.util.List;

public class Day19 {


    private static List<String> TOWEL_PATTERNS;
    private static List<String> TOWEL_DESIGNS;


    private static long countPossibleWaysToBuildDesign(String design) {
        int designLength = design.length();
        long[] waysToBuild = new long[designLength + 1];
        waysToBuild[0] = 1;
        for (int i = 0; i < designLength; i++) {
            if (waysToBuild[i] == 0) continue;
            for (String currentPattern : TOWEL_PATTERNS) {
                if (design.startsWith(currentPattern, i)) {
                    waysToBuild[i + currentPattern.length()] += waysToBuild[i];
                }
            }
        }
        return waysToBuild[designLength];
    }

    private static boolean checkIfPossibleToBuild(String design) {
        int designLength = design.length();
        boolean[] designPossibility = new boolean[designLength + 1];
        designPossibility[0] = true;
        for (int i = 0; i < designLength; i++) {
            if (!designPossibility[i]) continue;
            for (String pattern : TOWEL_PATTERNS) {
                if (design.startsWith(pattern, i)) {
                    designPossibility[i + pattern.length()] = true;
                }
            }
        }
        return designPossibility[designLength];
    }

    private static void prepareData(List<String> inputLines) {
        TOWEL_PATTERNS = Arrays.stream(inputLines.getFirst().split(", ")).toList();
        TOWEL_DESIGNS = inputLines.stream().skip(2).toList();
    }


    private static void partOne() {
        int counter = 0;
        for (String currentDesign : TOWEL_DESIGNS) {
            if (checkIfPossibleToBuild(currentDesign)) counter++;
        }
        System.out.println("PART I = " + counter);
    }


    private static void partTwo() {
        long counter = 0;
        for (String currentDesign : TOWEL_DESIGNS) {
            counter += countPossibleWaysToBuildDesign(currentDesign);
        }
        System.out.println("PART II = " + counter);
    }

    static void main() {
        String pathToFile = "src/main/resources/2024.day19/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
