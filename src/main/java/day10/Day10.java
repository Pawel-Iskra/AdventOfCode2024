package day10;

import utils.MyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10 {

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
            this.reachedTimes = new int[vertices];
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

        public int countAchievedNines() {
            int counter = 0;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (MAP[i][j] == 9 && visited[i * COLS + j]) counter++;
                }
            }
            return counter;
        }


        public int countHowManyTimesNinesWereReached() {
            int counter = 0;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    int vertexNr = i * COLS + j;
                    if (MAP[i][j] == 9) counter += reachedTimes[vertexNr];
                }
            }
            return counter;
        }

        public void depthFirstSearch(int vertex, int part) {
            strb.append(vertex).append(" ");
            reachedTimes[vertex]++;
            visited[vertex] = true;
            int row = vertex / COLS;
            int col = vertex - row * COLS;
            if (part == 2 && MAP[row][col] == 9) clearVisited();
            for (int currentVertexFromAdjacency : getAdjacencyList(vertex)) {
                int heightFromTo = getHeightFromNodeToNode(vertex, currentVertexFromAdjacency);
                if (!visited[currentVertexFromAdjacency] && (heightFromTo == 1)) {
                    depthFirstSearch(currentVertexFromAdjacency, part);
                }
            }
        }

        public String getStrb() {
            return strb.toString();
        }

        public int[] getReachedTimes() {
            return this.reachedTimes;
        }

        public void refreshStrb() {
            strb = new StringBuilder();
        }

        public void clearVisited() {
            this.visited = new boolean[vertices];
        }

        public void clearReachedTimes() {
            this.reachedTimes = new int[vertices];
        }

        private int getHeightFromNodeToNode(final int from, final int to) {
            List<DirectedEdge> edgesList = getDirectedEdgesFromNode(from);
            return edgesList.stream()
                    .filter(edge -> edge.getTo() == to)
                    .findFirst()
                    .map(DirectedEdge::getHighToClimb)
                    .orElse(0);
        }
    }


    private static int ROWS;
    private static int COLS;
    private static int[][] MAP;
    private static DirectedGraph GRAPH;


    public static void partOne() {
        int counter = 0;
        System.out.println("\nPART 1: \n");
        for (int i = 0; i < GRAPH.getVertices(); i++) {
            int row = i / COLS;
            int col = i - row * COLS;
            if (MAP[row][col] != 0) continue;

            GRAPH.depthFirstSearch(i, 1);

            System.out.println("GRAPH.countAchievedNines() = " + GRAPH.countAchievedNines());
            System.out.println("from vertex:" + i + "->" + GRAPH.getStrb());

            counter += GRAPH.countAchievedNines();
            GRAPH.refreshStrb();
            GRAPH.clearVisited();
        }
        System.out.println("FINAL counter = " + counter);
    } // 501 -> OK


    public static void partTwo() {
        GRAPH.refreshStrb();
        GRAPH.clearVisited();
        GRAPH.clearReachedTimes();
        int counter = 0;
        System.out.println("\nPART 2: \n");
        for (int i = 0; i < GRAPH.getVertices(); i++) {
            int row = i / COLS;
            int col = i - row * COLS;
            if (MAP[row][col] != 0) continue;


            GRAPH.depthFirstSearch(i, 2);
            System.out.println("GRAPH.countHowManyTimesNinesWereReached() = " + GRAPH.countHowManyTimesNinesWereReached());
            System.out.println("from vertex:" + i + "->" + GRAPH.getStrb());
            counter += GRAPH.countHowManyTimesNinesWereReached();

            int[] reachedTimes = GRAPH.getReachedTimes();
//            System.out.println("reachedTimes = " + Arrays.toString(reachedTimes));

            GRAPH.refreshStrb();
            GRAPH.clearVisited();
            GRAPH.clearReachedTimes();

        }
        System.out.println("FINAL counter = " + counter);
    } // 1017 - OK

    private static void prepareData(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLS = inputLines.get(0).length();
        MAP = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            String currentLine = inputLines.get(i);
            for (int j = 0; j < COLS; j++) {
                MAP[i][j] = currentLine.charAt(j) - 48;
            }
        }

        System.out.println("MAP:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(MAP[i][j] + ", ");
            }
            System.out.println();
        }

        int vertices = ROWS * COLS;
        GRAPH = new DirectedGraph(vertices);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int currentVertex = i * ROWS + j;

                if (i > 0) { // go up
                    int height = MAP[i - 1][j] - MAP[i][j];
                    GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - COLS, height));
                }
                if (i < (ROWS - 1)) { // go down
                    int height = MAP[i + 1][j] - MAP[i][j];
                    GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + COLS, height));
                }
                if (j > 0) { // go left
                    int height = MAP[i][j - 1] - MAP[i][j];
                    GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, height));
                }
                if (j < (COLS - 1)) { // go right
                    int height = MAP[i][j + 1] - MAP[i][j];
                    GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, height));
                }
            }
        }

        System.out.println("GRAPH as edges from node:");
        for (int i = 0; i < vertices; i++) {
            List<DirectedEdge> edgesFromNode = GRAPH.getDirectedEdgesFromNode(i);
            System.out.println(i + ": " + edgesFromNode.toString());
        }

        System.out.println("GRAPH as adjacency nodes:");
        for (int i = 0; i < vertices; i++) {
            List<Integer> adjacencyList = GRAPH.getAdjacencyList(i);
            System.out.println(i + ": " + adjacencyList.toString());
        }
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day10/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
