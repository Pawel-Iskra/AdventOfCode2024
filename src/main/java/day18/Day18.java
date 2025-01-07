package day18;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;

public class Day18 {

//    private static int ROWS = 7;
//    private static int COLS = 7;
    private static int NUMBER_OF_FALLEN_BYTES = 1024;
    private static int ROWS = 71;
    private static int COLS = 71;
    private static char[][] MEMORY_AS_MATRIX = new char[ROWS][COLS];
    private static DirectedGraph MEMORY_AS_GRAPH;

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


        public List<Integer> breadthFirstSearch(int vertex) {
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
                    if (!visited[currentNeighbour]) {
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

    static class CurrentDistanceToStart {
        private int from;
        private int to;
        private long dist;

        public CurrentDistanceToStart(int from, int to, long dist) {
            this.from = from;
            this.to = to;
            this.dist = dist;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public long getDist() {
            return dist;
        }
    }


    static class DijkstraShortestPath {
        private DirectedGraph directedGraph;
        private int vertices;
        private long[] shortestPaths;
        private int[] previous;
        private boolean[] visited;

        public DijkstraShortestPath(DirectedGraph directedGraph) {
            this.directedGraph = directedGraph;
            this.vertices = directedGraph.getVertices();
            this.visited = new boolean[vertices];
            this.shortestPaths = new long[vertices];
            this.previous = new int[vertices];
        }

        private void clearArraysBeforeDijkstraAlgorithmStart(int start) {
            visited = new boolean[vertices];
            previous = new int[vertices];
            previous[start] = start;
            for (int i = 0; i < vertices; i++) {
                shortestPaths[i] = Long.MAX_VALUE;
            }
            shortestPaths[start] = 0;
        }

        public void runModifiedDijkstraAlgorithm(int start) {
            clearArraysBeforeDijkstraAlgorithmStart(start);

            Queue<CurrentDistanceToStart> currentDistancesQueue =
                    new PriorityQueue<>(vertices, Comparator.comparingLong(CurrentDistanceToStart::getDist));
            currentDistancesQueue.add(new CurrentDistanceToStart(start, start, 0));

            while (!currentDistancesQueue.isEmpty()) {
                visited[start] = true;
                start = currentDistancesQueue.poll().getTo();

                List<DirectedEdge> directedEdgesFromNode = directedGraph.getDirectedEdgesFromNode(start);
                long currDistToSrc = shortestPaths[start];

                int numOfEdges = directedEdgesFromNode.size();
                for (int i = 0; i < numOfEdges; i++) {
                    DirectedEdge currentEdge = directedEdgesFromNode.get(i);
                    long calcDist = currDistToSrc + 1;
                    int to = currentEdge.getTo();
                    if (calcDist < shortestPaths[to]) {
                        shortestPaths[to] = calcDist;
                        previous[to] = start;
                        if (!visited[to]) {
                            currentDistancesQueue.add(
                                    new CurrentDistanceToStart(currentEdge.getFrom(), currentEdge.getTo(), calcDist));
                        }
                    }
                }
            }
        }

        public long[] getShortestPaths() {
            return shortestPaths;
        }

        public int[] getPrevious() {
            return previous;
        }

        public boolean[] getVisited() {
            return visited;
        }

        public long getShortestPathFromStartToGivenVertex(int interestingVertex) {
            return shortestPaths[interestingVertex];
        }
    }

    public static void partOne() {
        int startVertex = 0;
        int endVertex = ROWS*COLS -1;
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(MEMORY_AS_GRAPH);
        dijkstraShortestPath.runModifiedDijkstraAlgorithm(startVertex);
        System.out.println("Part 1 = " + dijkstraShortestPath.getShortestPathFromStartToGivenVertex(endVertex));
    }

    public static void partTwo(List<String> inputLines) {
        System.out.println("\nPART II:");
        int size = inputLines.size();
        for (int i = 1024; i < size; i++) {
            System.out.println("i = " + i);
            System.out.println("inputLines.get(i) = " + inputLines.get(i));
            prepareData(inputLines, i);
            int startVertex = 0;
            int endVertex = ROWS*COLS -1;
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(MEMORY_AS_GRAPH);
            dijkstraShortestPath.runModifiedDijkstraAlgorithm(startVertex);
            System.out.println("Part 1 = " + dijkstraShortestPath.getShortestPathFromStartToGivenVertex(endVertex));

        }

    }
    // 2,27 - NO


    private static void prepareData(final List<String> inputLines, int nrOfFallenBytes) {
//        ROWS = inputLines.size();
//        COLS = inputLines.get(0).length();
//        MEMORY_AS_MATRIX = new char[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                MEMORY_AS_MATRIX[i][j] = '.';
            }
        }

        for (int i = 0; i < nrOfFallenBytes; i++) {
            String[] currentLine = inputLines.get(i).split(",");
            int x = Integer.parseInt(currentLine[0]);
            int y = Integer.parseInt(currentLine[1]);
            MEMORY_AS_MATRIX[y][x] = '#';
        }


        int vertices = ROWS * COLS;
        MEMORY_AS_GRAPH = new DirectedGraph(vertices);
        int weight = 1;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int currentVertex = i * ROWS + j;

                if (MEMORY_AS_MATRIX[i][j] == '#') continue;

                if (i > 0 && (MEMORY_AS_MATRIX[i - 1][j] != '#')) { // go up
                    MEMORY_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - COLS, weight));
                }
                if (i < (ROWS - 1) && (MEMORY_AS_MATRIX[i + 1][j] != '#')) { // go down
                    MEMORY_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + COLS, weight));
                }
                if (j > 0 && (MEMORY_AS_MATRIX[i][j - 1] != '#')) { // go left
                    MEMORY_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, weight));
                }
                if (j < (COLS - 1) && (MEMORY_AS_MATRIX[i][j + 1] != '#')) { // go right
                    MEMORY_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, weight));
                }
            }
        }

//        System.out.println("MEMORY_AS_GRAPH as edges from node:");
//        for (int i = 0; i < vertices; i++) {
//            List<DirectedEdge> edgesFromNode = MEMORY_AS_GRAPH.getDirectedEdgesFromNode(i);
//            System.out.println(i + ": " + edgesFromNode.toString());
//        }
//
//        System.out.println("MEMORY_AS_GRAPH as adjacency nodes:");
//        for (int i = 0; i < vertices; i++) {
//            List<Integer> adjacencyList = MEMORY_AS_GRAPH.getAdjacencyList(i);
//            System.out.println(i + ": " + adjacencyList.toString());
//        }
//
//        System.out.println("MEMORY_AS_MATRIX");
//        for (int i = 0; i < ROWS; i++) {
//            for (int j = 0; j < COLS; j++) {
//                System.out.print(MEMORY_AS_MATRIX[i][j] + " ");
//            }
//            System.out.println();
//        }

    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day18/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines, 1024);
        partOne();
        partTwo(inputLines);
    }
}
