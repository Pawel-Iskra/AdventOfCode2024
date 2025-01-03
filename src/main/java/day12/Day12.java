package day12;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;

public class Day12 {

    static class DirectedEdge {
        private int from;
        private int to;
        private int highToClimb;

        public DirectedEdge(int from, int to, int highToClimb) {
            this.from = from;
            this.to = to;
            this.highToClimb = highToClimb;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getHighToClimb() {
            return highToClimb;
        }

        @Override
        public String toString() {
            return "DirectedEdge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", highToClimb=" + highToClimb +
                    '}';
        }
    }


    static class DirectedGraph {
        private StringBuilder strb;
        private int vertices;
        private int[] reachedTimes;
        private int edges;
        private boolean[] visited;
        private List<Integer>[] adjacencyList;
        private List<DirectedEdge>[] edgeListFromNode;

        public DirectedGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            this.strb = new StringBuilder();
            this.visited = new boolean[vertices];
            this.edgeListFromNode = new List[vertices];
            this.adjacencyList = (List<Integer>[]) new List[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencyList[i] = new ArrayList<Integer>();
            }
            for (int i = 0; i < vertices; i++) {
                edgeListFromNode[i] = new ArrayList<>();
            }
        }

        public int getVertices() {
            return vertices;
        }

        public void addEdge(DirectedEdge directedEdge) {
            edgeListFromNode[directedEdge.getFrom()].add(directedEdge);
            adjacencyList[directedEdge.getFrom()].add(directedEdge.getTo());
            edges++;
        }

        public List<Integer> getAdjacencyList(int vertex) {
            return adjacencyList[vertex];
        }

        public List<DirectedEdge> getDirectedEdgesFromNode(int vertex) {
            return edgeListFromNode[vertex];
        }


        public void depthFirstSearch(int vertex) {
            strb.append(vertex).append(" ");
            visited[vertex] = true;
            for (int currentVertexFromAdjacency : getAdjacencyList(vertex)) {
                if (!visited[currentVertexFromAdjacency]) {
                    depthFirstSearch(currentVertexFromAdjacency);
                }
            }
        }

        public List<Integer>[] goBfsForAllVertices() {
            List<Integer>[] result = new List[vertices];

            for (int i = 0; i < vertices; i++) {
                int row = i / COLS;
                int col = i % COLS;
                char currentChar = GARDEN_AS_MATRIX[row][col];
                result[i] = breadthFirstSearch(i, currentChar);
            }

            return result;
        }


        public List<Integer> breadthFirstSearch(int vertex, int currentChar) {
            List<Integer> resultList = new ArrayList<>();
            resultList.add(vertex);
//            strb.append(vertex).append(" ");
            clearVisited();
            LinkedList<Integer> queue = new LinkedList<Integer>();
            visited[vertex] = true;
            queue.add(vertex);
            while (queue.size() != 0) {
                vertex = queue.poll();
                for (int currentNeighbour : adjacencyList[vertex]) {
                    if (!visited[currentNeighbour] && (GARDEN_AS_MATRIX[currentNeighbour / COLS][currentNeighbour % COLS] == currentChar)) {
//                        strb.append(currentNeighbour).append(" ");
                        resultList.add(currentNeighbour);
                        visited[currentNeighbour] = true;
                        queue.add(currentNeighbour);
                    }
                }
            }
            Collections.sort(resultList);
            return resultList;
        }

        public String getStrb() {
            return strb.toString();
        }

        public void refreshStrb() {
            strb = new StringBuilder();
        }

        public void clearVisited() {
            this.visited = new boolean[vertices];
        }

    }


    private static int ROWS;
    private static int COLS;
    private static char[][] GARDEN_AS_MATRIX;
    private static DirectedGraph GARDEN_AS_GRAPH;


    public static void partOne() {
        System.out.println("PART I:");
        List<Integer>[] lists = GARDEN_AS_GRAPH.goBfsForAllVertices();
        Set<List<Integer>> set = new HashSet<>(Arrays.asList(lists).subList(0, GARDEN_AS_GRAPH.getVertices()));
        System.out.println("set = " + set);

        int summaryPrice = 0;
        for (List<Integer> currentList : set) {
            int region = currentList.size();


            int howManyOneDiff = 0;
            int howManyRowDiff = 0;
            for (int i = 0; i < region; i++) {
                int currentVertex = currentList.get(i);
                int currentVertexRow = currentVertex / COLS;
                int currentVertexCol = currentVertex % COLS;


                for (int j = i; j < region; j++) {
                    int currentOne = currentList.get(j);
                    int row = currentOne / COLS;
                    int col = currentOne % COLS;

                    if(currentVertexRow == row && Math.abs(currentVertex - currentOne) == 1) howManyOneDiff++;
                    if(currentVertexCol == col && Math.abs(currentVertex - currentOne) == COLS) howManyRowDiff++;

                }
            }
            System.out.println("howManyOneDiff = " + howManyOneDiff);
            System.out.println("howManyRowDiff = " + howManyRowDiff);

            int perimeter = region * 4 - howManyOneDiff * 2 - howManyRowDiff * 2;
            int price = region * perimeter;
            System.out.println("currentList = " + currentList);
            System.out.println("region = " + region);
            System.out.println("perimeter = " + perimeter);

            summaryPrice += price;
            System.out.println("price = " + price);
        }
        System.out.println("PART I: summaryPrice = " + summaryPrice);

    }

    public static void partTwo() {

    }

    private static void prepareData(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLS = inputLines.get(0).length();
        GARDEN_AS_MATRIX = new char[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            String currentLine = inputLines.get(i);
            for (int j = 0; j < COLS; j++) {
                GARDEN_AS_MATRIX[i][j] = currentLine.charAt(j);
            }
        }

        int vertices = ROWS * COLS;
        GARDEN_AS_GRAPH = new DirectedGraph(vertices);
        int weight = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int currentVertex = i * ROWS + j;

                if (i > 0) { // go up
                    GARDEN_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - COLS, weight));
                }
                if (i < (ROWS - 1)) { // go down
                    GARDEN_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + COLS, weight));
                }
                if (j > 0) { // go left
                    GARDEN_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, weight));
                }
                if (j < (COLS - 1)) { // go right
                    GARDEN_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, weight));
                }
            }
        }

        System.out.println("GARDEN AS GRAPH as edges from node:");
        for (int i = 0; i < vertices; i++) {
            List<DirectedEdge> edgesFromNode = GARDEN_AS_GRAPH.getDirectedEdgesFromNode(i);
            System.out.println(i + ": " + edgesFromNode.toString());
        }

        System.out.println("GARDEN AS GRAPH as adjacency nodes:");
        for (int i = 0; i < vertices; i++) {
            List<Integer> adjacencyList = GARDEN_AS_GRAPH.getAdjacencyList(i);
            System.out.println(i + ": " + adjacencyList.toString());
        }

        System.out.println("GARDEN AS MATRIX:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(GARDEN_AS_MATRIX[i][j] + " ");
            }
            System.out.println();
        }


        List<Integer>[] lists = GARDEN_AS_GRAPH.goBfsForAllVertices();
        System.out.println("lists = " + Arrays.toString(lists));
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day12/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
