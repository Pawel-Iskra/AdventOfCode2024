package day14;

import utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day14 {

    static class RobotData {
        private int posX;
        private int posY;
        private int velX;
        private int velY;

        RobotData(final int posX, final int posY, final int velX, final int velY) {
            this.posX = posX;
            this.posY = posY;
            this.velX = velX;
            this.velY = velY;
        }


        void setPosX(final int posX) {
            this.posX = posX;
        }

        void setPosY(final int posY) {
            this.posY = posY;
        }

        int getPosX() {
            return posX;
        }

        int getPosY() {
            return posY;
        }

        int getVelX() {
            return velX;
        }

        int getVelY() {
            return velY;
        }

        @Override
        public String toString() {
            return "\nRobotData{" +
                    "posX=" + posX +
                    ", posY=" + posY +
                    ", velX=" + velX +
                    ", velY=" + velY +
                    '}';
        }
    }

    //    private static int ROWS = 7;
//    private static int COLS = 11;
    private static int COLS = 101;
    private static int ROWS = 103;
    private static int NUMBER_OF_ROBOTS;
    private static int[][] SPACE;
    private static List<RobotData> ROBOT_DATA_LIST = new ArrayList<>();

    /**
     * So, a velocity of v=1,-2 means that each second, the robot moves 1 tile to the right and 2 tiles up.
     * they can teleport. When a robot would run into an edge of the space they're in,
     * they instead teleport to the other side, effectively wrapping around the edges. Here is what robot p=2,4 v=2,-3
     */

    public static void partOne() { // Where will the robots be after 100 seconds?

        for (int i = 0; i < 100; i++) {
            goOneSecond();
        }

        SPACE = new int[ROWS][COLS];
        for (int i = 0; i < NUMBER_OF_ROBOTS; i++) {
            RobotData currentRobot = ROBOT_DATA_LIST.get(i);
            int posX = currentRobot.getPosX();
            int posY = currentRobot.getPosY();
            SPACE[posY][posX]++;
        }


        System.out.println("SPACE: ");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(SPACE[i][j] + " ");
            }
            System.out.println();
        }

        int safetyFactor = 1;
        int halfRow = ROWS / 2;
        int halfCol = COLS / 2;

        int counter = 0;
        for (int i = 0; i < halfRow; i++) {
            for (int j = 0; j < halfCol; j++) {
                counter += SPACE[i][j];
            }
        }
        safetyFactor *= counter;

        System.out.println("counter = " + counter);
        counter = 0;
        for (int i = 0; i < halfRow; i++) {
            for (int j = halfCol + 1; j < COLS; j++) {
                counter += SPACE[i][j];
            }
        }
        safetyFactor *= counter;

        System.out.println("counter = " + counter);
        counter = 0;
        for (int i = halfRow + 1; i < ROWS; i++) {
            for (int j = 0; j < halfCol; j++) {
                counter += SPACE[i][j];
            }
        }
        safetyFactor *= counter;

        System.out.println("counter = " + counter);
        counter = 0;
        for (int i = halfRow + 1; i < ROWS; i++) {
            for (int j = halfCol + 1; j < COLS; j++) {
                counter += SPACE[i][j];
            }
        }
        System.out.println("counter = " + counter);
        safetyFactor *= counter;

        System.out.println("safetyFactor = " + safetyFactor);
    }

    private static void goOneSecond() {
        for (int i = 0; i < NUMBER_OF_ROBOTS; i++) {
            RobotData currentRobot = ROBOT_DATA_LIST.get(i);
            int posX = currentRobot.getPosX();
            int posY = currentRobot.getPosY();
            int velX = currentRobot.getVelX();
            int velY = currentRobot.getVelY();

            int nextX = (posX + velX) % COLS;
            int nextY = (posY + velY) % ROWS;

            if (nextX < 0) {
                nextX = COLS + nextX;
            } else if (nextX >= COLS) {
                nextX = COLS - nextX;
            }
            if (nextY < 0) {
                nextY = ROWS + nextY;
            } else if (nextY >= ROWS) {
                nextY = ROWS - nextY;
            }

            currentRobot.setPosX(nextX);
            currentRobot.setPosY(nextY);

        }

    }

    public static void partTwo() {
        System.out.println("\n\nPART II:");
        SPACE = new int[ROWS][COLS];

        for (int b = 1; b < 7000; b++) {
            goOneSecond();


            SPACE = new int[ROWS][COLS];
            int[] cols = new int[ROWS];
            for (int a = 0; a < NUMBER_OF_ROBOTS; a++) {
                RobotData currentRobot = ROBOT_DATA_LIST.get(a);
                int posX = currentRobot.getPosX();
                int posY = currentRobot.getPosY();
                cols[posX]++;
                SPACE[posY][posX]++;
            }

            if (b < 6000) continue;

            for (int i = 15; i < cols.length - 16; i++) {


                int col0 = cols[i - 14];
                int col1 = cols[i];
                int col2 = cols[i + 1];
                int col3 = cols[i + 2];
                int col4 = cols[i + 16];

                if (col1 > 21 && col2 > 21 && col3 > 21 && col0 > 31 && col4 > 31) {
                    System.out.println("b = " + b);
                    System.out.println("SPACE: ");
                    for (int h = 0; h < ROWS; h++) {
                        for (int j = 0; j < COLS; j++) {
                            char current = SPACE[h][j] == 0 ? ' ' : (char) (SPACE[h][j] + 48);
                            System.out.print(current + " ");
                        }
                        System.out.println();
                    }
                }
            }
        }

    }

    // 6567 - too low
    // 6568 - too low
    // 6569 - too low
    // 6570 - NO
    // 6668 - OK

    private static void prepareData(final List<String> inputLines) {
        ROBOT_DATA_LIST = new ArrayList<>();
        SPACE = new int[ROWS][COLS];
        NUMBER_OF_ROBOTS = inputLines.size();
        for (int i = 0; i < NUMBER_OF_ROBOTS; i++) {
            String[] currentData = inputLines.get(i).split(" ");
            String[] position = currentData[0].split(",");
            String[] velocity = currentData[1].substring(currentData[1].indexOf("=") + 1).split(",");

            int posX = Integer.parseInt(position[0].replaceAll("[^\\d.]", ""));
            int posY = Integer.parseInt(position[1].replaceAll("[^\\d.]", ""));
            int velX = Integer.parseInt(velocity[0]);
            int velY = Integer.parseInt(velocity[1]);

            ROBOT_DATA_LIST.add(new RobotData(posX, posY, velX, velY));
        }

        System.out.println("ROBOT_DATA_LIST = " + ROBOT_DATA_LIST);

        for (int i = 0; i < NUMBER_OF_ROBOTS; i++) {
            RobotData currentRobot = ROBOT_DATA_LIST.get(i);
            int posX = currentRobot.getPosX();
            int posY = currentRobot.getPosY();
            SPACE[posY][posX]++;
        }

        System.out.println("SPACE: ");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(SPACE[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day14/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
        prepareData(inputLines);
        partTwo();
    }
}
