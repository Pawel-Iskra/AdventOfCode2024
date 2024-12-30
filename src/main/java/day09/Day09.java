package day09;

import utils.MyUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day09 {

    // OK:0099811188827773336446555566..............
    //    0099811188827773336446555566
    //    009981118882777333644655556666667775888899

    private static String DISK_MAP;
    private static int[] BLOCK_FILES_AS_ARRAY;
    private static int[] BLOCKS_OF_FREE_SPACES_ARRAY;
    private static int[] REARRANGED_BLOCK_FILES_AS_ARRAY;
    private static int[] REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO;
    private static int DISK_MAP_LENGTH;
    private static int DECODED_DISK_MAP_LENGTH;
    private static int INDEX_OF_LAST_BLOCK_FILE;
    private static int NUMBER_OF_FREE_SPACES_TO_FILL;
    private static String DECODED_DISK_MAP;
    private static int[] DECODED_DISK_MAP_AS_ARRAY;
    private static Map<Integer, int[]> DECODED_DISK_MAP_AS_MAP = new HashMap();
    private static String FREE_SPACE = ".";


    // PART 1: 85795637992 - too low
    //         6331212425418 - OK

    public static void partOne(List<String> inputLines) {
        StringBuilder strb = new StringBuilder();
        int indexOfRearranged = 0;


        for (int i = 0; i < DISK_MAP_LENGTH; i++) {
            if (Arrays.stream(BLOCK_FILES_AS_ARRAY).count() == 0) break;
            int currentBlocksToFill = Integer.parseInt(String.valueOf(DISK_MAP.charAt(i)));
            if (i % 2 == 0) {
                if (i / 2 == INDEX_OF_LAST_BLOCK_FILE) {
                    int howManyInLastNonZero = BLOCK_FILES_AS_ARRAY[INDEX_OF_LAST_BLOCK_FILE];
                    for (int j = 0; j < howManyInLastNonZero; j++) {
                        strb.append(INDEX_OF_LAST_BLOCK_FILE);
                        REARRANGED_BLOCK_FILES_AS_ARRAY[indexOfRearranged] = INDEX_OF_LAST_BLOCK_FILE;
                        indexOfRearranged++;
                    }
                    BLOCK_FILES_AS_ARRAY[INDEX_OF_LAST_BLOCK_FILE] = 0;
                    break;
                }
                for (int j = 0; j < currentBlocksToFill; j++) {
                    strb.append(i / 2);
                    REARRANGED_BLOCK_FILES_AS_ARRAY[indexOfRearranged] = i / 2;
                    indexOfRearranged++;
                }
                BLOCK_FILES_AS_ARRAY[i / 2] = 0;
            } else {
                if (INDEX_OF_LAST_BLOCK_FILE < 0) {
//                    strb.append(FREE_SPACE);
                    continue;
                }
                while (currentBlocksToFill > 0) {
                    int howManyInLastNonZero = BLOCK_FILES_AS_ARRAY[INDEX_OF_LAST_BLOCK_FILE];


                    if (currentBlocksToFill >= howManyInLastNonZero) {
                        for (int j = 0; j < howManyInLastNonZero; j++) {
                            strb.append(INDEX_OF_LAST_BLOCK_FILE);
                            REARRANGED_BLOCK_FILES_AS_ARRAY[indexOfRearranged] = INDEX_OF_LAST_BLOCK_FILE;
                            indexOfRearranged++;
                        }
                        BLOCK_FILES_AS_ARRAY[INDEX_OF_LAST_BLOCK_FILE] = 0;
                        currentBlocksToFill = currentBlocksToFill - howManyInLastNonZero;
                        INDEX_OF_LAST_BLOCK_FILE--;
                    } else {
                        for (int j = 0; j < currentBlocksToFill; j++) {
                            strb.append(INDEX_OF_LAST_BLOCK_FILE);
                            REARRANGED_BLOCK_FILES_AS_ARRAY[indexOfRearranged] = INDEX_OF_LAST_BLOCK_FILE;
                            indexOfRearranged++;
                        }
                        BLOCK_FILES_AS_ARRAY[INDEX_OF_LAST_BLOCK_FILE] = howManyInLastNonZero - currentBlocksToFill;
                        currentBlocksToFill = currentBlocksToFill - howManyInLastNonZero;
                    }
                }
            }

        }

//        System.out.println("strb.toString() = " + strb.toString());

//        System.out.println("REARRANGED_BLOCK_FILES_AS_ARRAY = " + Arrays.toString(REARRANGED_BLOCK_FILES_AS_ARRAY));

        long countChecksum = 0;
//        for (int i = 0; i < strb.length(); i++) {
//            countChecksum += (long) i * Integer.parseInt(String.valueOf(strb.charAt(i)));
//        }

        for (int i = 0; i < DECODED_DISK_MAP_LENGTH; i++) {
            countChecksum += (long) i * REARRANGED_BLOCK_FILES_AS_ARRAY[i];
        }
        System.out.println("countChecksum = " + countChecksum);

    }


    public static void partTwo(List<String> inputLines) {
//        int nrOfBlockFiles = DISK_MAP_LENGTH % 2 == 0 ? DISK_MAP_LENGTH / 2 : DISK_MAP_LENGTH / 2 + 1;
//        INDEX_OF_LAST_BLOCK_FILE = nrOfBlockFiles - 1;
//        int[] checkSumValuesForIndex = new int[DECODED_DISK_MAP_LENGTH];
//        Map<Integer, List<Integer>> freeSpaceBlocksAfterOperations = new HashMap<>();
//        for (int i = 0; i < nrOfBlockFiles; i++) {
//            BLOCK_FILES_AS_ARRAY[i] = Integer.parseInt(String.valueOf(DISK_MAP.charAt(i * 2)));
//        }
//
//        for (int i = 0; i < DISK_MAP_LENGTH; i++) {
//            REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO[i] = DECODED_DISK_MAP_AS_ARRAY[i];
//        }
//
//        System.out.println("\nREARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO at start = " + Arrays.toString(REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO));
//
//        BLOCKS_OF_FREE_SPACES_ARRAY = new int[NUMBER_OF_FREE_SPACES_TO_FILL];
//
//        for (int i = REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO.length - 1; i >= 2; i = i - 2) {
//            int currentFileBlockSize = REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO[i];
//
//            int indexFromStart = 1;
//            while (indexFromStart < REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO.length) {
//                int currentFreeSpaceBlockSize = REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO[indexFromStart];
//                if (currentFreeSpaceBlockSize >= currentFileBlockSize) {
//
//                    break;
//                }
//
//
//                indexFromStart += 2;
//            }
//
//
//        }


        System.out.println("\nPART II:");
        for (int i = DECODED_DISK_MAP_AS_MAP.size() - 1; i > 0; i = i - 2) {

            int currentFileBlockSize = DECODED_DISK_MAP_AS_MAP.get(i).length;
            int value = i / 2;

            for (int j = 1; j < DECODED_DISK_MAP_AS_MAP.size(); j = j + 2) {
                int currentFreeSpaceBlockSize = (int) Arrays.stream(DECODED_DISK_MAP_AS_MAP.get(j)).filter(val -> val == 0).count();

                if (j >= i) break;

                boolean isMoved = false;
                if (currentFreeSpaceBlockSize >= currentFileBlockSize) {
                    int[] currentFreeBlock = DECODED_DISK_MAP_AS_MAP.get(j);
                    int counter = 0;
                    for (int k = 0; k < currentFreeBlock.length; k++) {
                        if (currentFreeBlock[k] == 0) {
                            currentFreeBlock[k] = value;
                            counter++;
                        }
                        if (counter == currentFileBlockSize) {
                            isMoved = true;
                            break;
                        }

                    }


                    DECODED_DISK_MAP_AS_MAP.put(j, currentFreeBlock);
                    DECODED_DISK_MAP_AS_MAP.put(i, new int[currentFileBlockSize]);

                    if (isMoved) break;
                }

            }
        }

//        System.out.println("DECODED_DISK_MAP_AS_MAP = ");
//        for (Map.Entry<Integer, int[]> element : DECODED_DISK_MAP_AS_MAP.entrySet()) {
//            System.out.print(element.getKey() + " : [" + Arrays.toString(element.getValue()) + "], ");
//        }


        int numberOfBlocks = DISK_MAP.chars().map(c -> c - 48).sum();

        DECODED_DISK_MAP_AS_ARRAY = new int[numberOfBlocks];
        int index = 0;
        for (int i = 0; i < DECODED_DISK_MAP_AS_MAP.size(); i++) {
            int[] currentBlocks = DECODED_DISK_MAP_AS_MAP.get(i);

            for (int j = 0; j < currentBlocks.length; j++) {
                DECODED_DISK_MAP_AS_ARRAY[index] = currentBlocks[j];
                index++;
            }
        }

        System.out.println("\nDECODED_DISK_MAP_AS_ARRAY = " + Arrays.toString(DECODED_DISK_MAP_AS_ARRAY));

        long checkSum = 0;
        for (int i = 0; i < DECODED_DISK_MAP_AS_ARRAY.length; i++) {
            checkSum += (long) i * DECODED_DISK_MAP_AS_ARRAY[i];
        }
        System.out.println("checkSum = " + checkSum);
    }
    // PART I : 6331212425418
    // PART II: 8592266602739 - too high
    //          8592266602739
    //          6363268339304 -- OK


    private static void prepareData(final List<String> inputLines) {
        DISK_MAP = inputLines.get(0);
        DISK_MAP_LENGTH = DISK_MAP.length();
        System.out.println("DISK_MAP_LENGTH = " + DISK_MAP_LENGTH);
//        System.out.println("DISK_MAP = " + DISK_MAP);
        int howManyDecoded = 0;

        int nrOfBlockFiles = DISK_MAP_LENGTH % 2 == 0 ? DISK_MAP_LENGTH / 2 : DISK_MAP_LENGTH / 2 + 1;
        BLOCK_FILES_AS_ARRAY = new int[nrOfBlockFiles];
        BLOCKS_OF_FREE_SPACES_ARRAY = new int[DISK_MAP_LENGTH / 2];
        for (int i = 0; i < nrOfBlockFiles; i++) {
            BLOCK_FILES_AS_ARRAY[i] = Integer.parseInt(String.valueOf(DISK_MAP.charAt(i * 2)));
        }
        INDEX_OF_LAST_BLOCK_FILE = nrOfBlockFiles - 1;
//        System.out.println("BLOCK_FILES_AS_ARRAY = " + Arrays.toString(BLOCK_FILES_AS_ARRAY));
        System.out.println("INDEX_OF_LAST_BLOCK_FILE = " + INDEX_OF_LAST_BLOCK_FILE);

        DECODED_DISK_MAP_AS_ARRAY = new int[DISK_MAP_LENGTH];

        StringBuilder strb = new StringBuilder();
        int firstDigit = Integer.parseInt(String.valueOf(DISK_MAP.charAt(0)));
        DECODED_DISK_MAP_AS_ARRAY[0] = firstDigit;
        for (int i = 0; i < firstDigit; i++) {
            strb.append("0");
            howManyDecoded++;
        }
        for (int i = 1; i < DISK_MAP_LENGTH; i++) {
            int digit = Integer.parseInt(String.valueOf(DISK_MAP.charAt(i)));
            if (i % 2 == 1) {
                DECODED_DISK_MAP_AS_ARRAY[i] = digit;
                for (int j = 0; j < digit; j++) {
                    strb.append(FREE_SPACE);
                    howManyDecoded++;
                    BLOCKS_OF_FREE_SPACES_ARRAY[i / 2] = digit;
                }
            } else {
                int value = i / 2;
                DECODED_DISK_MAP_AS_ARRAY[i] = digit;
                for (int j = 0; j < digit; j++) {
                    strb.append(value);
                    howManyDecoded++;
                }
            }
        }
        DECODED_DISK_MAP_LENGTH = howManyDecoded;
        System.out.println("DECODED_DISK_MAP_LENGTH = " + DECODED_DISK_MAP_LENGTH);
        DECODED_DISK_MAP = strb.toString();
        NUMBER_OF_FREE_SPACES_TO_FILL = (int) DECODED_DISK_MAP.chars().filter(c -> c == '.').count();
        System.out.println("NUMBER_OF_FREE_SPACES_TO_FILL = " + NUMBER_OF_FREE_SPACES_TO_FILL);
//        System.out.println("DECODED_DISK_MAP = " + DECODED_DISK_MAP);

//        System.out.println("BLOCKS_OF_FREE_SPACES_ARRAY = " + Arrays.toString(BLOCKS_OF_FREE_SPACES_ARRAY));

        REARRANGED_BLOCK_FILES_AS_ARRAY = new int[DECODED_DISK_MAP_LENGTH];
        REARRANGED_BLOCK_FILES_AS_ARRAY_PART_TWO = new int[DISK_MAP_LENGTH];

        System.out.println("DECODED_DISK_MAP_AS_ARRAY = " + Arrays.toString(DECODED_DISK_MAP_AS_ARRAY));

        for (int i = 0; i < DECODED_DISK_MAP_AS_ARRAY.length; i++) {
            int numberOfCurrentFreeSpacesOrFiles = DECODED_DISK_MAP_AS_ARRAY[i];
            int[] currentBlock = new int[numberOfCurrentFreeSpacesOrFiles];
            int value = i / 2;
            if (i % 2 == 0) {
                for (int j = 0; j < numberOfCurrentFreeSpacesOrFiles; j++) {
                    currentBlock[j] = value;
                }
            } else {
                for (int j = 0; j < numberOfCurrentFreeSpacesOrFiles; j++) {
                    currentBlock[j] = 0;
                }
            }
            DECODED_DISK_MAP_AS_MAP.put(i, currentBlock);
        }


//        System.out.println("DECODED_DISK_MAP_AS_MAP = ");
//        for (Map.Entry<Integer, int[]> element : DECODED_DISK_MAP_AS_MAP.entrySet()) {
//            System.out.print(element.getKey() + " : [" + Arrays.toString(element.getValue()) + "], ");
//        }
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day09/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne(inputLines);
        partTwo(inputLines);
    }
}
