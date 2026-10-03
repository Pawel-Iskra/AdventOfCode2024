package day18;

import utils.MyUtils;

import java.util.*;

public class Day18 {

    // test data
//    private static final int ROWS = 7;
//    private static final int COLS = 7;
//    private static final int NUMBER_OF_FALLEN_BYTES = 12;

    private static final int ROWS = 71;
    private static final int COLS = 71;
    private static final int NUMBER_OF_FALLEN_BYTES = 1024;

    private static final int ONE_MOVE_COST = 1;
    private static final int START_NODE = 0;
    private static final int END_NODE = ROWS * COLS - 1;
    private static final int[][] MATRIX_NEIGHBOURS = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
    };

    private static Graph MEMORY_AS_GRAPH;


    record Node(int nodeIndex, int distanceFromStart) {
    }

    static class Graph {
        private final int vertices;
        private final Set<Integer>[] adjacencySets;

        public Graph(int vertices) {
            this.vertices = vertices;
            this.adjacencySets = new Set[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencySets[i] = new HashSet<>();
            }
        }

        public void addEdge(int from, int to) {
            adjacencySets[from].add(to);
            adjacencySets[to].add(from);
        }

        public Set<Integer> getAdjaccencySet(int vertex) {
            return adjacencySets[vertex];
        }

        public void removeNodeFromAdjacency(int vertex) {
            for (int[] direction : MATRIX_NEIGHBOURS) {
                int neighbourRow = vertex / COLS + direction[0];
                int neighbourCol = vertex % COLS + direction[1];
                if (neighbourRow >= 0 && neighbourRow < ROWS && neighbourCol >= 0 && neighbourCol < COLS) {
                    adjacencySets[neighbourRow * COLS + neighbourCol].remove(vertex);
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder strb = new StringBuilder();
            strb.append("Graph: \nvertices=").append(vertices).append("\nadjacencyList:");
            for (int i = 0; i < adjacencySets.length; i++) {
                strb.append("\n").append(i).append(adjacencySets[i]);
            }
            return strb.toString();
        }
    }


    private static void prepareData(List<String> inputLines) {
        int vertices = ROWS * COLS;
        MEMORY_AS_GRAPH = new Graph(vertices);
        for (int i = 0; i < vertices; i++) {
            for (int[] neighbour : MATRIX_NEIGHBOURS) {
                int neighbourRow = i / COLS + neighbour[0];
                int neighbourCol = i % COLS + neighbour[1];
                if (neighbourRow >= 0 && neighbourRow < ROWS && neighbourCol >= 0 && neighbourCol < COLS) {
                    MEMORY_AS_GRAPH.addEdge(i, neighbourRow * COLS + neighbourCol);
                }
            }
        }

        int counterOfFallenBytes = 0;
        for (String line : inputLines) {
            String[] coords = line.split(",");
            counterOfFallenBytes++;
            int coordCol = Integer.parseInt(coords[0]);
            int coordRow = Integer.parseInt(coords[1]);
            int vertexIndex = coordRow * COLS + coordCol;
            MEMORY_AS_GRAPH.removeNodeFromAdjacency(vertexIndex);
            if (counterOfFallenBytes == NUMBER_OF_FALLEN_BYTES) break;
        }
//        System.out.println("MEMORY_AS_GRAPH = " + MEMORY_AS_GRAPH);
    }

    // start with the least expensive node and using that value -> reduce (if possible) neighbours' costs
    private static int getShortestPathWithDijkstra() {
        Map<Integer, Integer> distancesMap = new HashMap<>();
        distancesMap.put(START_NODE, 0);
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>(Comparator.comparing(Node::distanceFromStart));
        nodeQueue.add(new Node(START_NODE, 0));

        while (!nodeQueue.isEmpty()) {
            Node currentNode = nodeQueue.poll();
            int currentNodeDist = currentNode.distanceFromStart();

            for (int currentNeighbour : MEMORY_AS_GRAPH.getAdjaccencySet(currentNode.nodeIndex())) {
                int newDistForNeighbour = currentNodeDist + ONE_MOVE_COST;
                int oldDistForNeighbour = distancesMap.getOrDefault(currentNeighbour, Integer.MAX_VALUE);

                if (newDistForNeighbour < oldDistForNeighbour) {
                    distancesMap.put(currentNeighbour, newDistForNeighbour);
                    nodeQueue.add(new Node(currentNeighbour, newDistForNeighbour));
                }

            }
        }
        return distancesMap.get(END_NODE);
    }

    private static void partOne() {
        int shortestPathSteps = getShortestPathWithDijkstra();
        System.out.println("PART I: " + shortestPathSteps);
    }


    static void main() {
        String pathToFile = "src/main/resources/2024.day18/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToFile);

        prepareData(inputLines);
        partOne();
    }

}
