package day16;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;

public class Day16 {


    enum Dir {
        E,
        W,
        S,
        N
    }

    static class DirectedEdge {
        private int from;
        private int to;
        private int weight;

        public DirectedEdge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "DirectedEdge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }


    static class DirectedGraph {
        private int vertices;
        private int edges;
        private boolean[] visited;
        private boolean[] dfsVisited;
        private List<Integer>[] adjacencyList;
        private List<DirectedEdge>[] edgeListFromNode;
        private long[] shortestPaths;
        private Dir[] dirsArray;
        private Set<Integer> set = new HashSet<>();

        public DirectedGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            this.dfsVisited = new boolean[vertices];
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

        public void setShortestPaths(long[] array) {
            this.shortestPaths = array;
        }

        public void setDirsArray(Dir[] array) {
            this.dirsArray = array;
        }


        public void depthFirstSearch(int vertex) {
            set.add(vertex);
            dfsVisited[vertex] = true;
            for (int currentVertexFromAdjacency : getAdjacencyList(vertex)) {
                if (!dfsVisited[currentVertexFromAdjacency]) {
                    depthFirstSearch(currentVertexFromAdjacency);
                }
            }
        }

//        public void depthFirstSearch(int vertex) {
//            dfsVisited[vertex] = true;
//            set.add(vertex);
//            System.out.println("vertex = " + vertex);
////            long shortestPath = getAdjacencyList(vertex).stream().filter(v -> !dfsVisited[v]).map(v -> shortestPaths[v]).mapToLong(path -> path).min().getAsLong();
////            List<Integer> adjacencyListWithShortestPath = getAdjacencyListWithShortestPath(getAdjacencyList(vertex), shortestPath, vertex);
//            for (int currentVertexFromAdjacency : adjacencyListWithShortestPath) {
//                if (!dfsVisited[currentVertexFromAdjacency]) {
//                    depthFirstSearch(currentVertexFromAdjacency);
//                }
//            }
//        }

        private List<Integer> getAdjacencyListWithShortestPath(final List<Integer> adjacencyList, long shortestPath, int currentVertex) {
            List<Integer> resultList = new ArrayList<>();
            Dir currentVertexDir = dirsArray[currentVertex];
            for (int currentNeigbhour : adjacencyList) {
                if (shortestPath == shortestPaths[currentNeigbhour]) {
                    resultList.add(currentNeigbhour);
                } else {
                    Dir currentNeighbourDir = dirsArray[currentNeigbhour];

                    long currentNeighbourPath = shortestPaths[currentNeigbhour];
                    if (currentNeighbourPath - ROTATE_VALUE == shortestPath) {
                        resultList.add(currentNeigbhour);

                    }
                }
            }

            return resultList;
        }


        public Set<Integer> getSet() {
            return set;
        }

        public List<Integer> getAdjacencyList(int vertex) {
            return adjacencyList[vertex];
        }

        public List<DirectedEdge> getDirectedEdgesFromNode(int vertex) {
            return edgeListFromNode[vertex];
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
        private Dir[] dirsArray;
        private int[] previous;
        private boolean[] visited;
        private List<Integer>[] adjacencyList;

        Dir[] getDirsArray() {
            return dirsArray;
        }

        public DijkstraShortestPath(DirectedGraph directedGraph) {
            this.directedGraph = directedGraph;
            this.vertices = directedGraph.getVertices();
            this.visited = new boolean[vertices];
            this.shortestPaths = new long[vertices];
            this.previous = new int[vertices];
            this.dirsArray = new Dir[vertices];
            this.adjacencyList = (List<Integer>[]) new List[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencyList[i] = new ArrayList<Integer>();
            }
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
            dirsArray[START_ROW * COLS + START_COL] = Dir.E;
            clearArraysBeforeDijkstraAlgorithmStart(start);

            Queue<CurrentDistanceToStart> currentDistancesQueue =
                    new PriorityQueue<>(vertices, Comparator.comparingLong(CurrentDistanceToStart::getDist));
            currentDistancesQueue.add(new CurrentDistanceToStart(start, start, 0));

            while (!currentDistancesQueue.isEmpty()) {
                visited[start] = true;
                start = currentDistancesQueue.poll().getTo();

                List<DirectedEdge> directedEdgesFromNode = directedGraph.getDirectedEdgesFromNode(start);
                long currDistToSrc = shortestPaths[start];
                Dir currDirFromStart = dirsArray[start];

                int numOfEdges = directedEdgesFromNode.size();
                for (int i = 0; i < numOfEdges; i++) {
                    DirectedEdge currentEdge = directedEdgesFromNode.get(i);
                    long calcDist = currDistToSrc + 1;
                    int to = currentEdge.getTo();
                    int pointsFromRotation = getPointsFromRotation(start, to, currDirFromStart);
                    calcDist += pointsFromRotation;
                    if (calcDist < shortestPaths[to]) {
                        shortestPaths[to] = calcDist;
                        previous[to] = start;
                        dirsArray[to] = getCurrentDirFromTo(start, to, currDirFromStart);
                        if (!visited[to]) {
                            currentDistancesQueue.add(
                                    new CurrentDistanceToStart(currentEdge.getFrom(), currentEdge.getTo(), calcDist));
                        }
                    }
                }
            }
        }


        private Dir getCurrentDirFromTo(int from, int to, Dir dirFrom) {
            Dir resultDir = dirFrom;
            int diff = to - from;
            if (diff == 1) {
                switch (dirFrom) {
                    case N, S -> resultDir = Dir.E;
                }
            } else if (diff == -1) {
                switch (dirFrom) {
                    case N, S -> resultDir = Dir.W;
                }
            } else if (diff == COLS) {
                switch (dirFrom) {
                    case E, W -> resultDir = Dir.S;
                }
            } else if (diff == -COLS) {
                switch (dirFrom) {
                    case E, W -> resultDir = Dir.N;
                }
            }
            return resultDir;
        }


        private int getPointsFromRotation(int from, int to, Dir currentDir) {
            int diff = to - from;
            int value = 0;
            if (diff == 1) {
                switch (currentDir) {
                    case N, S -> value = ROTATE_VALUE;
                }
            } else if (diff == -1) {
                switch (currentDir) {
                    case N, S -> value = ROTATE_VALUE;
                }
            } else if (diff == COLS) {
                switch (currentDir) {
                    case E, W -> value = ROTATE_VALUE;
                }
            } else if (diff == -COLS) {
                switch (currentDir) {
                    case E, W -> value = ROTATE_VALUE;
                }
            }
            return value;
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

        public List<Integer> getAdjacencyList(int vertex) {
            return adjacencyList[vertex];
        }

        public long getShortestPathFromStartToGivenVertex(int interestingVertex) {
            return shortestPaths[interestingVertex];
        }
    }


    private static int ROTATE_VALUE = 1000;
    private static int ROWS;
    private static int COLS;
    private static char[][] MAZE_AS_MATRIX;
    private static char WALL = '#';
    private static char END = 'E';
    private static char START = 'S';
    private static DirectedGraph MAZE_AS_GRAPH;

    private static int END_ROW;
    private static int END_COL;
    private static int START_ROW;
    private static int START_COL;
    private static List<Set<Integer>> ADJ = new ArrayList<>();


    static void find_paths(List<List<Integer>> paths, ArrayList<Integer> path, ArrayList<ArrayList<Integer>> parent, int n, int u) {
        // Base Case
        if (u == -1) {
            paths.add(new ArrayList<>(path));
            return;
        }

        // Loop for all the parents
        // of the given vertex
        for (int par : parent.get(u)) {

            // Insert the current
            // vertex in path
            path.add(u);

            // Recursive call for its parent
            find_paths(paths, path, parent, n, par);

            // Remove the current vertex
            path.remove(path.size() - 1);
        }
    }

    static void print_paths(List<Set<Integer>> adj, int n, int start, int end) {
        Set<Integer> resultSet = new HashSet<>();
        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        ArrayList<ArrayList<Integer>> parent = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            parent.add(new ArrayList<>());
        }

        // Function call to bfs
        bfs(adj, parent, n, start);

        // Function call to find_paths
        find_paths(paths, path, parent, n, end);

        for (ArrayList<Integer> v : paths) {

            // Since paths contain each
            // path in reverse order,
            // so reverse it
            Collections.reverse(v);

            // Print node for the current path
            for (int u : v) {
                System.out.print(u + " ");
                resultSet.add(u);
            }
            System.out.println();
        }
        System.out.println("resultSet.size() = " + resultSet.size());
        System.out.println("resultSet = " + resultSet);
    }

    static void bfs(List<Set<Integer>> adj, ArrayList<ArrayList<Integer>> parent, int n, int start) {

        // dist will contain shortest distance
        // from start to every other vertex
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);

        Queue<Integer> q = new LinkedList<>();

        // Insert source vertex in queue and make
        // its parent -1 and distance 0
        q.offer(start);

        parent.get(start).clear();
        parent.get(start).add(-1);

        dist[start] = 0;

        // Until Queue is empty
        while (!q.isEmpty()) {
            int u = q.poll();

            for (int v : adj.get(u)) {
                if (dist[v] > dist[u] + 1) {

                    // A shorter distance is found
                    // So erase all the previous parents
                    // and insert new parent u in parent[v]
                    dist[v] = dist[u] + 1;
                    q.offer(v);
                    parent.get(v).clear();
                    parent.get(v).add(u);
                } else if (dist[v] == dist[u] + 1) {

                    // Another candidate parent for
                    // shortes path found
                    parent.get(v).add(u);
                }
            }
        }
    }

    static void add_edge(List<Set<Integer>> adj, int src, int dest) {
        adj.get(src).add(dest);
        adj.get(dest).add(src);
    }

    // Function which finds all the paths
    // and stores it in paths array
    static void find_paths(ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> path,
                           ArrayList<ArrayList<Integer>> parent, int n, int u) {
        // Base Case
        if (u == -1) {
            paths.add(new ArrayList<>(path));
            return;
        }

        // Loop for all the parents
        // of the given vertex
        for (int par : parent.get(u)) {

            // Insert the current
            // vertex in path
            path.add(u);

            // Recursive call for its parent
            find_paths(paths, path, parent, n, par);

            // Remove the current vertex
            path.remove(path.size() - 1);
        }
    }


    public static void partOne() {

        /////////////////////////////////
        // Number of vertices
        int n = COLS*ROWS;

        // array of vectors is used
        // to store the graph
        // in the form of an adjacency list
//        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            adj.add(new ArrayList<>());
//        }
//
//        // Given Graph
//        add_edge(adj, 0, 1);
//        add_edge(adj, 0, 2);
//        add_edge(adj, 1, 3);
//        add_edge(adj, 1, 4);
//        add_edge(adj, 2, 3);
//        add_edge(adj, 3, 5);
//        add_edge(adj, 4, 5);


        int startVertex = START_ROW * COLS + START_COL;
        int endVertex = END_ROW * COLS + END_COL;


        // Given source and destination
        int src = startVertex;
        int dest = endVertex;

        // Function Call
        System.out.println("\nSTART PRINT PATHS:");
        print_paths(ADJ, n, src, dest);
        System.out.println("\nEND PRINT PATHS.");
        ///////////////////////////////////////////////////////

        System.out.println("startVertex = " + startVertex);
        System.out.println("endVertex = " + endVertex);

        DijkstraShortestPath dijkstra = new DijkstraShortestPath(MAZE_AS_GRAPH);
        dijkstra.runModifiedDijkstraAlgorithm(startVertex);
        System.out.println("Part 1 = " + dijkstra.getShortestPathFromStartToGivenVertex(endVertex));

        System.out.println("\nPART II:");


        int[] previousArray = dijkstra.getPrevious();
        System.out.println("previousArray = " + Arrays.toString(previousArray));
        List<Integer> listOfPrevious = new ArrayList<>();
        listOfPrevious.add(endVertex);
        int current = previousArray[endVertex];
        while (current != startVertex) {
            listOfPrevious.add(current);
            current = previousArray[current];
        }
        listOfPrevious.add(startVertex);
        System.out.println("listOfPrevious = " + listOfPrevious);

//        MAZE_AS_GRAPH.setShortestPaths(dijkstra.getShortestPaths());
//        MAZE_AS_GRAPH.setDirsArray(dijkstra.getDirsArray());
//        System.out.println("DFS");
//        MAZE_AS_GRAPH.depthFirstSearch(startVertex);
//        System.out.println("MAZE_AS_GRAPH.getSet() = " + MAZE_AS_GRAPH.getSet());
//        System.out.println("MAZE_AS_GRAPH.getSet().size() = " + MAZE_AS_GRAPH.getSet().size());

        System.out.println("listOfPrevious.size() = " + listOfPrevious.size());
        Set<Integer> verticesSet = new HashSet<>();

        TreeSet<Integer> waitingList = new TreeSet<>();
        waitingList.add(listOfPrevious.get(1));

        long[] shortestPaths = dijkstra.getShortestPaths();
        boolean[] visited = new boolean[ROWS * COLS];

        int currVer = waitingList.pollFirst();
        for (int j = 0; j < previousArray.length; j++) {
            if (previousArray[j] == currVer) {
                waitingList.add(j);
            }
        }

        while (!waitingList.isEmpty()) {

            currVer = waitingList.first();
            waitingList.remove(currVer);
//
//            for (int j = 0; j < previousArray.length; j++) {
//                if (previousArray[j] == currVer) {
//                    if (!visited[j]) waitingList.add(j);
//                }
//            }

            visited[currVer] = true;
            verticesSet.add(currVer);

            while (currVer != startVertex) {

                for (int j = 0; j < previousArray.length; j++) {
                    if (previousArray[j] == currVer) {
                        if (!visited[j]) waitingList.add(j);
                    }
                }

                currVer = previousArray[currVer];
                visited[currVer] = true;
                verticesSet.add(currVer);

            }

        }

        System.out.println("verticesSet = " + verticesSet);
        System.out.println("verticesSet.size() = " + verticesSet.size());

        for (int ver : verticesSet) {
            MAZE_AS_MATRIX[ver / COLS][ver % COLS] = 'X';
        }
        System.out.println("MAZE:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(MAZE_AS_MATRIX[i][j] + " ");
            }
            System.out.println();
        }


    }
    // PART I:
    // 156440 - too high
    // 155440 - too high
    //  10129 - too low
    // 101440 - NO
    //  91464 - OK


    public static void partTwo() {
//        System.out.println("\n\nPART II:");
//        int startVertex = START_ROW * COLS + START_COL;
//        System.out.println("startVertex = " + startVertex);
//        int endVertex = END_ROW * COLS + END_COL;
//        System.out.println("endVertex = " + endVertex);
//
//        DijkstraShortestPath dijkstra = new DijkstraShortestPath(MAZE_AS_GRAPH);
//        dijkstra.runModifiedDijkstraAlgorithm(startVertex);
//        System.out.println("Part 1 = " + dijkstra.getShortestPathFromStartToGivenVertex(endVertex));
//
//        int[] previous = dijkstra.getPrevious();
//        System.out.println("previous = " + Arrays.toString(previous));
//
//        List<Integer> listOfPrevious = new ArrayList<>();
//        listOfPrevious.add(endVertex);
//        int current = previous[endVertex];
//        while (current != startVertex) {
//            listOfPrevious.add(current);
//            current = previous[current];
//        }
//        listOfPrevious.add(startVertex);
//        System.out.println("listOfPrevious = " + listOfPrevious);
//
//        MAZE_AS_GRAPH.setShortestPaths(dijkstra.getShortestPaths());
//        System.out.println("DFS");
//        MAZE_AS_GRAPH.depthFirstSearch(endVertex);
//        System.out.println("MAZE_AS_GRAPH.getCounter() = " + MAZE_AS_GRAPH.getCounter());

    }


    private static void prepareData(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLS = inputLines.get(0).length();
        MAZE_AS_MATRIX = new char[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                char currentChar = inputLines.get(i).charAt(j);
                MAZE_AS_MATRIX[i][j] = currentChar;
                if (currentChar == START) {
                    START_ROW = i;
                    START_COL = j;
                }
                if (currentChar == END) {
                    END_ROW = i;
                    END_COL = j;
                }
            }
        }

        System.out.println("MAZE:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(MAZE_AS_MATRIX[i][j] + " ");
            }
            System.out.println();
        }

        int vertices = ROWS * COLS;
        MAZE_AS_GRAPH = new DirectedGraph(vertices);

        ///////////////////////////////////////////////////////
        ADJ = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            ADJ.add(new HashSet<>());
        }

        // Given Graph
//        add_edge(adj, 0, 1);
//        add_edge(adj, 0, 2);
//        add_edge(adj, 1, 3);
//        add_edge(adj, 1, 4);
//        add_edge(adj, 2, 3);
//        add_edge(adj, 3, 5);
//        add_edge(adj, 4, 5);
        //////////////////////////////////////////////

        int weight = 1;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (MAZE_AS_MATRIX[i][j] == WALL) continue;

                int currentVertex = i * ROWS + j;
                if ((i > 0) && (MAZE_AS_MATRIX[i - 1][j] != WALL)) { // go up
                    MAZE_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - COLS, weight));
                    add_edge(ADJ, currentVertex, currentVertex - COLS);
                }
                if ((i < (ROWS - 1)) && (MAZE_AS_MATRIX[i + 1][j] != WALL)) { // go down
                    MAZE_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + COLS, weight));
                    add_edge(ADJ, currentVertex, currentVertex + COLS);
                }
                if (j > 0 && (MAZE_AS_MATRIX[i][j - 1] != WALL)) { // go left
                    MAZE_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, weight));
                    add_edge(ADJ, currentVertex, currentVertex - 1);
                }
                if (j < (COLS - 1) && (MAZE_AS_MATRIX[i][j + 1] != WALL)) { // go right
                    MAZE_AS_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, weight));
                    add_edge(ADJ, currentVertex, currentVertex + 1);
                }
            }
        }

        System.out.println("MAZE_AS_GRAPH.adjacencyList = " + Arrays.toString(MAZE_AS_GRAPH.adjacencyList));
        System.out.println("ADJ = " + ADJ);
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day16/input1.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
