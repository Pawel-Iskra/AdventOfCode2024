package day06;

import utils.MyUtils;

import java.io.IOException;
import java.util.List;

public class Day06 {

    enum Dir {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private static char[][] MAP;
    private static int ROWS;
    private static int COLS;
    private static Character OBSTACLE = '#';
    private static Character AIR = '.';
    private static Character GUARD = '^';

    private static int GUARD_CURRENT_ROW;
    private static int GUARD_START_ROW;
    private static int GUARD_CURRENT_COL;
    private static int GUARD_START_COL;
    private static Dir GUARD_CURRENT_DIR = Dir.UP;


    public static void partOne(List<String> inputLines) {
        while (doesMapStillContainsGuard()) {
            goOneStep();
        }

        int counter = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (MAP[i][j] == 'X') {
                    counter++;
                }
            }
        }

        System.out.println("\nFinal MAP:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(MAP[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Part I - counter = " + counter);
    }

    private static void goOneStep() {
        if (GUARD_CURRENT_DIR == Dir.UP) {
            if (GUARD_CURRENT_ROW > 0) {
                char next = MAP[GUARD_CURRENT_ROW - 1][GUARD_CURRENT_COL];
                if (next != OBSTACLE) {
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                    GUARD_CURRENT_ROW = GUARD_CURRENT_ROW - 1;
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = GUARD;
                } else {
                    GUARD_CURRENT_DIR = Dir.RIGHT;
                }
            } else if (GUARD_CURRENT_ROW == 0) {
                MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                GUARD_CURRENT_ROW = GUARD_CURRENT_ROW - 1;
            }
        } else if (GUARD_CURRENT_DIR == Dir.DOWN) {
            if (GUARD_CURRENT_ROW < (ROWS - 1)) {
                char next = MAP[GUARD_CURRENT_ROW + 1][GUARD_CURRENT_COL];
                if (next != OBSTACLE) {
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                    GUARD_CURRENT_ROW = GUARD_CURRENT_ROW + 1;
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = GUARD;
                } else {
                    GUARD_CURRENT_DIR = Dir.LEFT;
                }
            } else if (GUARD_CURRENT_ROW == (ROWS - 1)) {
                MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                GUARD_CURRENT_ROW = GUARD_CURRENT_ROW + 1;
            }

        } else if (GUARD_CURRENT_DIR == Dir.LEFT) {
            if (GUARD_CURRENT_COL > 0) {
                char next = MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL - 1];
                if (next != OBSTACLE) {
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                    GUARD_CURRENT_COL = GUARD_CURRENT_COL - 1;
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = GUARD;
                } else {
                    GUARD_CURRENT_DIR = Dir.UP;
                }
            } else if (GUARD_CURRENT_COL == 0) {
                MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                GUARD_CURRENT_COL = GUARD_CURRENT_COL - 1;
            }

        } else if (GUARD_CURRENT_DIR == Dir.RIGHT) {
            if (GUARD_CURRENT_COL < (COLS - 1)) {
                char next = MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL + 1];
                if (next != OBSTACLE) {
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                    GUARD_CURRENT_COL += 1;
                    MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = GUARD;
                } else {
                    GUARD_CURRENT_DIR = Dir.DOWN;
                }
            } else if (GUARD_CURRENT_COL == (COLS - 1)) {
                MAP[GUARD_CURRENT_ROW][GUARD_CURRENT_COL] = 'X';
                GUARD_CURRENT_COL += 1;
            }
        }
    }

    private static boolean doesMapStillContainsGuard() {
//        for (int i = 0; i < ROWS; i++) {
//            for (int j = 0; j < COLS; j++) {
//                if (MAP[i][j] == '^') {
//                    return true;
//                }
//            }
//        }
//        return false;
        if (GUARD_CURRENT_ROW >= ROWS) return false;
        if (GUARD_CURRENT_ROW < 0) return false;
        if (GUARD_CURRENT_COL >= COLS) return false;
        if (GUARD_CURRENT_COL < 0) return false;
        return true;
    }


    public static void partTwo(List<String> inputLines) {
        int counter = 0;
        int rounds = ROWS * COLS * 10;

        int currentRound = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                GUARD_CURRENT_ROW = GUARD_START_ROW;
                GUARD_CURRENT_COL = GUARD_START_COL;
                GUARD_CURRENT_DIR = Dir.UP;

                if (i == GUARD_START_ROW && j == GUARD_START_COL) continue;
                if (MAP[i][j] == OBSTACLE) continue;
                MAP[i][j] = OBSTACLE;


                boolean isGuard = true;
                for (int k = 0; k < rounds; k++) {
                    goOneStep();
                    if (!doesMapStillContainsGuard()) {
                        isGuard = false;
                        break;
                    }
                }
                if (isGuard) counter++;

//                System.out.println("MAP:");
//                for (int n = 0; n < ROWS; n++) {
//                    for (int m = 0; m < COLS; m++) {
//                        System.out.print(MAP[n][m] + " ");
//                    }
//                    System.out.println();
//                }

//                if (doesMapStillContainsGuard()) {
//                    System.out.println("MAP that contains guard after all rounds:");
//                    for (int n = 0; n < ROWS; n++) {
//                        for (int m = 0; m < COLS; m++) {
//                            System.out.print(MAP[n][m] + " ");
//                        }
//                        System.out.println();
//                    }
//                    counter++;
//                }


                MAP[i][j] = AIR;
            }
        }
        System.out.println("Part II - counter = " + counter);
    }

    // dla rounds = 1 690 000 -> counter = 15 193 ?
    // 4647 - too high
    // 2323 - too high
    // 1000 - to low

//    GUARD_START_ROW = 47
//    GUARD_START_COL = 42
//    ROWS = 130
//    COLS = 130
//    countObstacles = 807

    private static void prepareData(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLS = inputLines.get(0).length();
        MAP = new char[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                MAP[i][j] = inputLines.get(i).charAt(j);
                if (MAP[i][j] == '^') {
                    GUARD_CURRENT_ROW = i;
                    GUARD_START_ROW = i;
                    GUARD_CURRENT_COL = j;
                    GUARD_START_COL = j;
                }
            }
        }

        int countObstacles = 0;
        System.out.println("MAP:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (MAP[i][j] == OBSTACLE) countObstacles++;
                System.out.print(MAP[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("GUARD_START_ROW = " + GUARD_CURRENT_ROW);
        System.out.println("GUARD_START_COL = " + GUARD_CURRENT_COL);
        System.out.println("ROWS = " + ROWS);
        System.out.println("COLS = " + COLS);
        System.out.println("countObstacles = " + countObstacles);

    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day06/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne(inputLines);
        partTwo(inputLines);
    }
}
