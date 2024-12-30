package day08;

import utils.MyUtils;

import java.io.IOException;
import java.util.List;

public class Day08 {

    private static int ROWS;
    private static int COLS;
    private static char AIR = '.';
    private static char ANTINODE = '#';
    private static char[][] ANTENNA_MAP;
    private static char[][] ANTINODES_MAP;

    public static void partOne(List<String> inputLines) {


        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (ANTENNA_MAP[i][j] != AIR) {
                    char currentAntenna = ANTENNA_MAP[i][j];
                    int row = i;
                    int col = j;
                    analyzeMapForGivenAntenna(currentAntenna, row, col);
                }
            }
        }


        System.out.println("ANTINODE MAP:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(ANTINODES_MAP[i][j] + " ");
            }
            System.out.println();
        }

        int counter = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (ANTINODES_MAP[i][j] != AIR) counter++;
            }
        }

        System.out.println("counter = " + counter);
    }

    private static void analyzeMapForGivenAntenna(final char currentAntenna, final int antennaRow, final int antennaCol) {
        int startCol = antennaCol == (COLS - 1) ? 0 : antennaCol + 1;
        int startRow = antennaCol == antennaCol + 1 ? antennaRow : antennaRow + 1;
        for (int i = startRow; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (ANTENNA_MAP[i][j] == currentAntenna) {
                    int row = i;
                    int col = j;

                    int rowDiff = row - antennaRow;
                    int colDiff = col - antennaCol;



                    for (int k = 1; k < (ROWS + COLS); k++) {


                        int lowerAntinodeRow = row + rowDiff *k;
                        int higherAntinodeRow = antennaRow - rowDiff*k;

                        int lowerAntinodeCol = col + colDiff*k;
                        int higherAntinodeCol = antennaCol - colDiff*k;

                        if (lowerAntinodeRow < ROWS && lowerAntinodeCol >= 0 && lowerAntinodeCol < COLS) {
                            ANTINODES_MAP[lowerAntinodeRow][lowerAntinodeCol] = ANTINODE;
                        }

                        if (higherAntinodeRow >= 0 && higherAntinodeCol >= 0 && higherAntinodeCol < COLS) {
                            ANTINODES_MAP[higherAntinodeRow][higherAntinodeCol] = ANTINODE;
                        }

                    }



                }
            }
        }


    }


    public static void partTwo(List<String> inputLines) {

    }


    private static void prepareData(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLS = inputLines.get(0).length();
        ANTENNA_MAP = new char[ROWS][COLS];
        ANTINODES_MAP = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                ANTENNA_MAP[i][j] = inputLines.get(i).charAt(j);
            }
        }

        System.out.println("MAP:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(ANTENNA_MAP[i][j] + " ");
            }
            System.out.println();
        }


        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
//                ANTINODES_MAP[i][j] = AIR;
                ANTINODES_MAP[i][j] = ANTENNA_MAP[i][j];
            }
        }


    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day08/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne(inputLines);
        partTwo(inputLines);
    }
}
