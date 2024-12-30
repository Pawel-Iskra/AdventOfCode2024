package day03;

import utils.MyUtils;

import java.io.IOException;
import java.util.List;

public class Day03 {

    private static String MUL_EXPR_START = "mul(";
    private static String DONT = "don't";

    public static void partOne(List<String> input) {
        int length = input.get(0).length();
        String inputLine = input.get(0);

        long sum = 0;
        for (int i = 0; i < length - 7; i++) {
            String currentStart = inputLine.substring(i, i + 4);
//            System.out.println("currentStart = " + currentStart);
            if (currentStart.equals(MUL_EXPR_START)) {
                int last = Math.min((length - i), 12);
                String currentMul = inputLine.substring(i, i + last);
//                System.out.println("currentMul = " + currentMul);
                if (!currentMul.contains(")")) continue;
                if (!currentMul.contains(",")) continue;

                currentMul = currentMul.substring(currentMul.indexOf("(") +1, currentMul.indexOf(")"));
                System.out.println("currentMul = " + currentMul);
                String[] current = currentMul.split(",");
                int firstInt = Integer.parseInt(current[0]);
                int secondInt = Integer.parseInt(current[1]);
                System.out.println("firstInt = " + firstInt);
                System.out.println("secondInt = " + secondInt);

                sum += ((long) firstInt * secondInt);
            }
        }
        System.out.println("sum = " + sum);
    }
    // 31748124 - too low
    // 173419328 -- OK


    public static void partTwo(List<String> input) {

        int length = input.get(0).length();
        String inputLine = input.get(0);

        long sum = 0;
        boolean isDo = true;
        boolean isDont = false;
        for (int i = 0; i < length - 7; i++) {
            if(inputLine.substring(i,i+5).contains(DONT)){
                isDont = true;
                isDo = false;
            } else if(inputLine.substring(i,i+2).contains("do")){
                isDo = true;
                isDont = false;
            }
            String currentStart = inputLine.substring(i, i + 4);
//            System.out.println("currentStart = " + currentStart);
            if (currentStart.equals(MUL_EXPR_START)) {
                int last = Math.min((length - i), 12);
                String currentMul = inputLine.substring(i, i + last);
//                System.out.println("currentMul = " + currentMul);
                if (!currentMul.contains(")")) continue;
                if (!currentMul.contains(",")) continue;

                currentMul = currentMul.substring(currentMul.indexOf("(") +1, currentMul.indexOf(")"));
//                System.out.println("currentMul = " + currentMul);
                String[] current = currentMul.split(",");
                int firstInt = Integer.parseInt(current[0]);
                int secondInt = Integer.parseInt(current[1]);
//                System.out.println("firstInt = " + firstInt);
//                System.out.println("secondInt = " + secondInt);
                if(!isDont && isDo){
                    sum += ((long) firstInt * secondInt);
                }
            }
        }
        System.out.println("sum = " + sum);
    }
    // part II: 15537330 - too low


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day03/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        partOne(inputLines);
        partTwo(inputLines);
    }
}
