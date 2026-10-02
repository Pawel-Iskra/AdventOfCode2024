package day16;

import utils.MyUtils;

import java.util.*;

public class Day16 {


    private static final Direction START_DIRECTION = Direction.EAST;
    private static final Character START_TILE = 'S';
    private static final Character END_TILE = 'E';
    private static final Character ROAD_TILE = '.';
    private static final Character WALL = '#';
    private static final int NINETY_DEGREE_ROTATION_COST = 1000;
    private static final int ONE_TILE_COST = 1;
    private static final int[][] MATRIX_NEIGHBOURS = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
    };

    private static int START_NODE;
    private static int EMD_NODE;
    private static int ROWS;
    private static int COLS;
    private static Graph MAP_AS_GRAPH;
    private static char[][] MAP_AS_MATRIX;


    record Edge(int from, int to) {

    }

    enum Direction {
        SOUTH, NORTH, EAST, WEST

    }


    private static class Graph {
        private final int vertices;
        private final Set<Integer>[] adjacencyListAsSet;

        public Graph(int vertices) {
            this.vertices = vertices;
            adjacencyListAsSet = new Set[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencyListAsSet[i] = new HashSet<>();
            }
        }

        void addEdge(int from, int to) {
            Edge edge = new Edge(from, to);
            adjacencyListAsSet[from].add(to);
            adjacencyListAsSet[to].add(from);
        }

        private Set<Integer> getAdjacencySet(int node) {
            return adjacencyListAsSet[node];
        }

        public List<List<Integer>> getAllPathsPossibleFromTo(int from, int to) {
            List<List<Integer>> pathsFromTo = new ArrayList<>();

            Queue<List<Integer>> pathsQueue = new ArrayDeque<>();
            pathsQueue.add(List.of(from));
            while (!pathsQueue.isEmpty()) {
                List<Integer> currentPath = pathsQueue.poll();
                int currentNode = currentPath.getLast();
                if (currentNode == to) {
                    pathsFromTo.add(currentPath);
                    continue;
                }
                for (Integer nextNode : getAdjacencySet(currentNode)) {
                    if (!currentPath.contains(nextNode)) {
                        List<Integer> newPath = new ArrayList<>(currentPath);
                        newPath.add(nextNode);
                        pathsQueue.add(newPath);
                    }
                }
            }
            return pathsFromTo;
        } // tests OK; part 1 -> OutOfMemoryError: Java heap space


        @Override
        public String toString() {
            StringBuilder strb = new StringBuilder();
            strb.append("Graph: \nvertices=").append(vertices).append("\nadjacencyList:");
            for (int i = 0; i < adjacencyListAsSet.length; i++) {
                strb.append("\n").append(i).append(adjacencyListAsSet[i]);
            }
            return strb.toString();
        }
    }


    private static void printOutPreparedData() {
        System.out.println("MAP_AS_GRAPH = " + MAP_AS_GRAPH);
        System.out.println("MAP_AS_MATRIX = ");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(MAP_AS_MATRIX[i][j]);
            }
            System.out.println();
        }
        System.out.println("EMD_NODE = " + EMD_NODE);
        System.out.println("START_NODE = " + START_NODE);
    }

    private static void prepareData(List<String> inputLines) {
        ROWS = inputLines.size();
        COLS = inputLines.getFirst().length();
        MAP_AS_GRAPH = new Graph(ROWS * COLS);
        MAP_AS_MATRIX = new char[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                char currentTile = inputLines.get(i).charAt(j);
                MAP_AS_MATRIX[i][j] = currentTile;
                if (currentTile == WALL) continue;

                int currentIndex = i * COLS + j;
                if (currentTile == START_TILE) START_NODE = currentIndex;
                if (currentTile == END_TILE) EMD_NODE = currentIndex;

                // mapping up/down/right/left connections in the matrix
                for (int[] direction : MATRIX_NEIGHBOURS) {
                    int row = i + direction[0];
                    int col = j + direction[1];
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        char nextChar = inputLines.get(row).charAt(col);
                        if (nextChar == WALL) continue;
                        MAP_AS_GRAPH.addEdge(currentIndex, row * COLS + col);
                    }
                }
            }
        }
//        printOutPreparedData();
    }


    private static int getLowestPointsToGetFromStartToEnd(List<List<Integer>> pathsFromStartToEnd) {
        int lowestPoints = Integer.MAX_VALUE;
        for (List<Integer> path : pathsFromStartToEnd) {
            int pointsFromPath = getPointsFromPath(path);
//            System.out.println("pointsFromPath = " + pointsFromPath);
            lowestPoints = Math.min(lowestPoints, pointsFromPath);
        }
        return lowestPoints;
    }

    private static Direction getNextDirection(int diff) {
        if (diff == 1) return Direction.EAST;
        if (diff == -1) return Direction.WEST;
        if (diff == COLS) return Direction.SOUTH;
        if (diff == -COLS) return Direction.NORTH;
        throw new IllegalArgumentException("Invalid move: " + diff);
    }

    private static int getPointsFromPath(List<Integer> path) {
        int points = 0;
        Direction currentDirection = START_DIRECTION;
        for (int i = 0; i < path.size() - 1; i++) {
            int currentNode = path.get(i);
            int nextNode = path.get(i + 1);
            Direction nextDirection = getNextDirection(nextNode - currentNode);

            if (currentDirection != nextDirection) points += NINETY_DEGREE_ROTATION_COST;
            points += ONE_TILE_COST;
            currentDirection = nextDirection;
        }
        return points;
    }


    private static void partOne() {
        System.out.println("PART I:");
        List<List<Integer>> pathsFromStartToEnd = MAP_AS_GRAPH.getAllPathsPossibleFromTo(START_NODE, EMD_NODE);
//        System.out.println("pathsFromStartToEnd = ");
//        for (List<Integer> path : pathsFromStartToEnd) {
//            for (int i = 0; i < path.size(); i++) {
//                System.out.print(path.get(i));
//                if (i < path.size() - 1) System.out.print(" -> ");
//            }
//            System.out.println();
//        }
        System.out.println("pathsFromStartToEnd.size() = " + pathsFromStartToEnd.size());
        int result = getLowestPointsToGetFromStartToEnd(pathsFromStartToEnd);
        System.out.println("result = " + result);
    }


    static void main() {
        String pathToInputFile = "src/main/resources/2024.day16/input1.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
//        partTwo();
    }

}
