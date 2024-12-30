package day05;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;

public class Day05 {

    private static Map<Integer, Set<Integer>> RULE_IS_BEFORE_KEY = new HashMap<>();
    private static Map<Integer, Set<Integer>> RULE_IS_AFTER_KEY = new HashMap<>();
    private static List<String> PAGES_LIST = new ArrayList<>();


    public static void partOne(List<String> input) {

        int sumOfMiddleNumberCorrectLines = 0;
        int sumOfMiddleNumberCorrectedLines = 0;
        int nrOfUpdates = PAGES_LIST.size();
        for (int j = 0; j < nrOfUpdates; j++) {
            String[] currentUpdate = PAGES_LIST.get(j).split(",");
            int nrOfPagesInUpdate = currentUpdate.length;
            boolean isOK = true;
            for (int k = 0; k < nrOfPagesInUpdate; k++) {
                int currentPage = Integer.parseInt(currentUpdate[k]);
                List<Integer> beforeCurrentPage = new ArrayList<>();
                List<Integer> afterCurrentPage = new ArrayList<>();

                for (int l = 0; l < k; l++) {
                    beforeCurrentPage.add(Integer.valueOf(currentUpdate[l]));
                }
                for (int l = k + 1; l < nrOfPagesInUpdate; l++) {
                    afterCurrentPage.add(Integer.valueOf(currentUpdate[l]));
                }

//                System.out.println("currentPage = " + currentPage);
//                System.out.println("beforeCurrentPage = " + beforeCurrentPage);
//                System.out.println("afterCurrentPage = " + afterCurrentPage);

                int nrOfBefore = beforeCurrentPage.size();
                for (int l = 0; l < nrOfBefore; l++) {
                    if (!RULE_IS_AFTER_KEY.containsKey(currentPage)) continue;
                    if (RULE_IS_AFTER_KEY.get(currentPage).contains(beforeCurrentPage.get(l))) {
                        isOK = false;
                        break;
                    }
                }

                int nrOfAfter = afterCurrentPage.size();
                for (int l = 0; l < nrOfAfter; l++) {
                    if (!RULE_IS_BEFORE_KEY.containsKey(currentPage)) continue;
                    if (RULE_IS_BEFORE_KEY.get(currentPage).contains(afterCurrentPage.get(l))) {
                        isOK = false;
                        break;
                    }
                }


            }

            if (isOK) {
                sumOfMiddleNumberCorrectLines += Integer.parseInt(currentUpdate[currentUpdate.length / 2]);
            } else {
                List<Integer> correctedLine = correctWrongOrderInSingleUpdate(currentUpdate);
                System.out.println("correctedLine = " + correctedLine);
                sumOfMiddleNumberCorrectedLines+=correctedLine.get(correctedLine.size()/2);
            }


            System.out.println("sumOfMiddleNumberCorrectLines = " + sumOfMiddleNumberCorrectLines);
            System.out.println("sumOfMiddleNumberCorrectedLines = " + sumOfMiddleNumberCorrectedLines);
        }


    }

    private static List<Integer> correctWrongOrderInSingleUpdate(final String[] currentUpdate) {

        List<Integer> result = new LinkedList<>();
        List<Integer> updateAsList = new ArrayList<>();
        List<Integer> currentResultAsList = new ArrayList<>();


        int amount = currentUpdate.length;
        for (int i = 0; i < amount; i++) {
            updateAsList.add(Integer.parseInt(currentUpdate[i]));
        }


        for (int m = 0; m < currentUpdate.length; m++) {
            result = new ArrayList<>();
            result.add(updateAsList.get(0));
            currentResultAsList = new ArrayList<>();
            currentResultAsList.add(Integer.valueOf(currentUpdate[0]));
            for (int i = 1; i < amount; i++) {
                int currentPage = updateAsList.get(i);
                int previousPage = updateAsList.get(i - 1);
                Set<Integer> pagesBeforePrevious = RULE_IS_BEFORE_KEY.get(previousPage);
                if (pagesBeforePrevious == null) {
                    result.add(currentPage);
                    continue;
                }

                if (pagesBeforePrevious.contains(currentPage)) {
                    result.add((i - 1), currentPage);
                } else {
                    result.add(currentPage);
                }

                currentResultAsList = new ArrayList<>(result);
//                System.out.println("currentResultAsList = " + currentResultAsList);
            }
            updateAsList = new ArrayList<>(result);

        }

        return updateAsList;
    }


    public static void partTwo(List<String> input) {


    }

    private static void prepareData(final List<String> inputLines) {
        int size = inputLines.size();
        int i = 0;
        for (i = 0; i < size; i++) {
            if (inputLines.get(i).strip().isBlank()) break;
            String[] line = inputLines.get(i).split("\\|");
            int left = (Integer.parseInt(line[0].strip()));
            int right = (Integer.parseInt(line[1].strip()));

            if (RULE_IS_BEFORE_KEY.containsKey(right)) {
                Set<Integer> currentList = new HashSet<>(RULE_IS_BEFORE_KEY.get(right));
                currentList.add(left);
                RULE_IS_BEFORE_KEY.put(right, currentList);
            } else {
                RULE_IS_BEFORE_KEY.put(right, Set.of(left));
            }


            if (RULE_IS_AFTER_KEY.containsKey(left)) {
                Set<Integer> currentList = new HashSet<>(RULE_IS_AFTER_KEY.get(left));
                currentList.add(right);
                RULE_IS_AFTER_KEY.put(left, currentList);
            } else {
                RULE_IS_AFTER_KEY.put(left, Set.of(right));
            }
        }

        for (int j = i + 1; j < size; j++) {
            PAGES_LIST.add(inputLines.get(j));
        }

        System.out.println("pagesList = " + PAGES_LIST);
        System.out.println("ruleIsBeforeKey = " + RULE_IS_BEFORE_KEY);
        System.out.println("ruleIsAfterKey = " + RULE_IS_AFTER_KEY);

    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day05/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);
        prepareData(inputLines);

        partOne(inputLines);
        partTwo(inputLines);
    }
}
