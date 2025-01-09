package day19;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Day19 {


    private static List<String> TOWEL_LIST;
    private static List<String> DESIGN_LIST;


    public static void partOne() {
        int counter = 0;
        for (String currentDesign : DESIGN_LIST) {
            if (checkIfDesignIsPossible(currentDesign)) counter++;
        }
        System.out.println("PART I counter = " + counter);
    }
    // 258 - OK


    private static boolean checkIfDesignIsPossible(String currentDesign) {
        Set<String> remainingList = Set.of(currentDesign);

        while (true) {

            Set<String> partialRemainingList;
            Set<String> resultRemainingList = new HashSet<>();
            for (String design : remainingList) {
                partialRemainingList = new HashSet<>();
                for (String currentTowel : TOWEL_LIST) {
                    int towelSize = currentTowel.length();
                    if (towelSize > design.length()) continue;
                    if (design.substring(0, towelSize).equals(currentTowel)) {
                        if (design.length() == currentTowel.length()) return true;
                        partialRemainingList.add(design.substring(towelSize));
                    }
                }
                resultRemainingList.addAll(partialRemainingList);
            }
            remainingList = new HashSet<>(resultRemainingList);
            if (remainingList.isEmpty()) return false;

        }
    }


    private static void prepareData(final List<String> inputLines) {
        TOWEL_LIST = Arrays.stream(inputLines.get(0).split(", ")).collect(Collectors.toList());

        DESIGN_LIST = new ArrayList<>();
        for (int i = 2; i < inputLines.size(); i++) {
            DESIGN_LIST.add(inputLines.get(i).strip());
        }

        TOWEL_LIST.sort(Comparator.comparingInt(String::length).reversed());

        System.out.println("TOWEL_LIST = " + TOWEL_LIST);
        System.out.println("TOWEL_LIST.size() = " + TOWEL_LIST.size());
        System.out.println("DESIGN_LIST = " + DESIGN_LIST);
        System.out.println("DESIGN_LIST.size() = " + DESIGN_LIST.size());
    }

    // PART II : 19660 - too low

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day19/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();

    }
}
