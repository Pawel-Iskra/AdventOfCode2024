package day04;

import utils.MyUtils;

import java.io.IOException;
import java.util.List;

public class Day04 {

    public static char[][] PUZZLE;
    public static int ROWS;
    public static int COLS;
    public static Character X = 'X';
    public static Character M = 'M';
    public static Character A = 'A';
    public static Character S = 'S';


    public static void partOne(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();

        int counter = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (checkToRight(i, j)) counter++;
                if (checkToLeft(i, j)) counter++;
                if (checkToUp(i, j)) counter++;
                if (checkToDown(i, j)) counter++;
                if (checkUpLeft(i, j)) counter++;
                if (checkUpRight(i, j)) counter++;
                if (checkDownLeft(i, j)) counter++;
                if (checkDownRight(i, j)) counter++;
            }
        }
        System.out.println("counter = " + counter);

    }

    private static boolean checkDownRight(final int row, final int col) {
        if ((ROWS - row) < 4) return false;
        if ((COLS - col) < 4) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row + 1][col + 1];
        char third = PUZZLE[row + 2][col + 2];
        char fourth = PUZZLE[row + 3][col + 3];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }


    private static boolean checkDownLeft(final int row, final int col) {
        if ((ROWS - row) < 4) return false;
        if (col < 3) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row + 1][col - 1];
        char third = PUZZLE[row + 2][col - 2];
        char fourth = PUZZLE[row + 3][col - 3];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }

    private static boolean checkUpRight(final int row, final int col) {
        if (row < 3) return false;
        if ((COLS - col) < 4) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row - 1][col + 1];
        char third = PUZZLE[row - 2][col + 2];
        char fourth = PUZZLE[row - 3][col + 3];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }

    private static boolean checkUpLeft(final int row, final int col) {
        if (row < 3) return false;
        if (col < 3) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row - 1][col - 1];
        char third = PUZZLE[row - 2][col - 2];
        char fourth = PUZZLE[row - 3][col - 3];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }

    private static boolean checkToDown(int row, int col) {
        if (ROWS - row < 4) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row + 1][col];
        char third = PUZZLE[row + 2][col];
        char fourth = PUZZLE[row + 3][col];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }

    private static boolean checkToUp(int row, int col) {
        if (row < 3) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row - 1][col];
        char third = PUZZLE[row - 2][col];
        char fourth = PUZZLE[row - 3][col];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }


    private static boolean checkToLeft(int row, int col) {
        if (col < 3) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row][col - 1];
        char third = PUZZLE[row][col - 2];
        char fourth = PUZZLE[row][col - 3];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }

    private static boolean checkToRight(int row, int col) {
        if (col > (COLS - 4)) return false;
        char first = PUZZLE[row][col];
        char sec = PUZZLE[row][col + 1];
        char third = PUZZLE[row][col + 2];
        char fourth = PUZZLE[row][col + 3];
        if (first != X) return false;
        if (sec != M) return false;
        if (third != A) return false;
        if (fourth != S) return false;
        return true;
    }


    public static void partTwo(List<String> input) {
        int counter = 0;

        for (int i = 1; i < (ROWS - 1); i++) {
            for (int j = 1; j < (COLS - 1); j++) {
                if (PUZZLE[i][j] != A) continue;

                if (checkIfxMas(i, j)) counter++;
            }
        }
        System.out.println("counter = " + counter);

    }

    private static boolean checkIfxMas(int row, int col) {
        char leftUp = PUZZLE[row - 1][col - 1];
        char rightDown = PUZZLE[row + 1][col + 1];
        char leftDown = PUZZLE[row + 1][col - 1];
        char rightUp = PUZZLE[row - 1][col + 1];

        List<Character> lUrD =  List.of(leftUp, rightDown);
        if(!(lUrD.contains(M) && lUrD.contains(S))) return false;

        List<Character> lDrU =  List.of(leftDown, rightUp);
        if(!(lDrU.contains(M) && lDrU.contains(S))) return false;

        return true;
    }

    public static void preparePuzzle(List<String> input) {
        ROWS = input.size();
        COLS = input.get(0).length();

        PUZZLE = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                PUZZLE[i][j] = input.get(i).charAt(j);
            }
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(PUZZLE[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day04/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        preparePuzzle(inputLines);
        partOne(inputLines);
        partTwo(inputLines);
    }
}
