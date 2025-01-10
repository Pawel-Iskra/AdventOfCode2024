package day23;

import utils.MyUtils;

import java.io.IOException;
import java.util.*;

public class Day23 {


    private static DirectedGraph COMPS_AS_GRAPH;
    private static Set<String> COMP_SET;
    private static List<String> COMP_LIST;


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
            adjacencyList[directedEdge.getTo()].add(directedEdge.getFrom());
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
//            Collections.sort(resultList);
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
        Set<List<String>> threes = new HashSet<>();
        int compAmount = COMP_LIST.size();
        for (int i = 0; i < compAmount; i++) {
            String firstComp = COMP_LIST.get(i);
            List<Integer> firstCompAdjacencyList = COMPS_AS_GRAPH.getAdjacencyList(i);
            for (int j = 0; j < firstCompAdjacencyList.size(); j++) {
                int secondComp = firstCompAdjacencyList.get(j);
                List<Integer> secondCompAdjacencyList = COMPS_AS_GRAPH.getAdjacencyList(secondComp);
                for (int k = 0; k < secondCompAdjacencyList.size(); k++) {
                    int thirdComp = firstCompAdjacencyList.get(k);
                    List<Integer> thirdCompAdjacencyList = COMPS_AS_GRAPH.getAdjacencyList(thirdComp);
                    if (thirdCompAdjacencyList.contains(secondComp) && thirdCompAdjacencyList.contains(i)) {
                        List<String> currentList = new ArrayList<>();
                        currentList.add(firstComp);
                        currentList.add(COMP_LIST.get(secondComp));
                        currentList.add(COMP_LIST.get(thirdComp));
                        Collections.sort(currentList);
                        threes.add(currentList);
                    }
                }
            }
        }

        System.out.println("threes = " + threes);
        System.out.println("threes.size() = " + threes.size());

        int counter = 0;
        for (List<String> currentThree : threes) {
            for (String currentComp : currentThree) {
                if (currentComp.charAt(0) == 't') {
                    counter++;
                    break;
                }
            }
        }
        System.out.println("Part I counter = " + counter);
    }


    public static void partTwo() {
        System.out.println("\n\nPART II:");
        Set<Set<String>> twoConnectedComps = new HashSet<>();

        int compAmount = COMP_LIST.size();
        for (int i = 0; i < compAmount; i++) {
            String firstComp = COMP_LIST.get(i);
            List<Integer> firstCompAdjacencyList = COMPS_AS_GRAPH.getAdjacencyList(i);
            for (int j = 0; j < firstCompAdjacencyList.size(); j++) {
                List<String> currentList = new ArrayList<>();
                currentList.add(firstComp);
                int secondComp = firstCompAdjacencyList.get(j);
                currentList.add(COMP_LIST.get(secondComp));
                Collections.sort(currentList);
                Set<String> listAsSet = new HashSet<>();
                for (String current : currentList) {
                    listAsSet.add(current);
                }
                twoConnectedComps.add(listAsSet);
            }
        }
        System.out.println("twoConnectedComps = " + twoConnectedComps);
        System.out.println("twoConnectedComps.size() = " + twoConnectedComps.size());

        Set<String>[] connectedComps = new HashSet[twoConnectedComps.size()];
        int index = 0;
        for (Set<String> currentSet : twoConnectedComps) {
            connectedComps[index] = currentSet;
            index++;
        }
        System.out.println("BEFORE connectedComps = " + Arrays.toString(connectedComps));


        // TODO: make it faster
        int vertices = COMPS_AS_GRAPH.getVertices();
        System.out.println("vertices = " + vertices);
        for (int i = 0; i < vertices; i++) {

            for (int j = 0; j < vertices; j++) {
                List<Integer> adjacencyList = COMPS_AS_GRAPH.getAdjacencyList(j);
                for (int k = 0; k < connectedComps.length; k++) {
                    Set<String> currentSet = connectedComps[k];
                    if (checkIfAdjacencyListOfNextCompContainAllPreviousComps(adjacencyList, currentSet)) {
                        currentSet.add(COMP_LIST.get(j));
                    }
                }
            }
            System.out.println("i = " + i);
        }
        System.out.println("AFTER connectedComps = " + Arrays.toString(connectedComps));

        Set<String> longest = connectedComps[0];
        for (int i = 1; i < connectedComps.length; i++) {
            if (connectedComps[i].size() > longest.size()) {
                longest = connectedComps[i];
            }
        }

        List<String> longestLan = new ArrayList<>(longest);
        Collections.sort(longestLan);
        System.out.println("longestLan = " + longestLan);
        System.out.print("PART II = ");
        for (String current : longestLan) {
            System.out.print(current + ",");
        }

    }

    private static boolean checkIfAdjacencyListOfNextCompContainAllPreviousComps(final List<Integer> nextCompAdjacencyList, final Set<String> currentList) {
        for (String currentComp : currentList) {
            int currentCompFromList = COMP_LIST.indexOf(currentComp);
            if (!nextCompAdjacencyList.contains(currentCompFromList)) return false;
        }
        return true;
    }


    private static void prepareData(final List<String> inputLines) {
        int size = inputLines.size();
        COMP_SET = new HashSet<>();
        COMP_LIST = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String[] currentConn = inputLines.get(i).split("-");
            COMP_SET.add(currentConn[0]);
            COMP_SET.add(currentConn[1]);
        }
        int vertices = COMP_SET.size();
        System.out.println("compSet.size() = " + COMP_SET.size());
        COMP_LIST = new ArrayList<>(COMP_SET);
        COMPS_AS_GRAPH = new DirectedGraph(COMP_SET.size());

        for (int i = 0; i < size; i++) {
            String[] currentConn = inputLines.get(i).split("-");
            String from = (currentConn[0]);
            String to = (currentConn[1]);

            int weight = 1;
            COMPS_AS_GRAPH.addEdge(new DirectedEdge(COMP_LIST.indexOf(from), COMP_LIST.indexOf(to), weight));
        }


//        System.out.println("COMPUTERS_AS_GRAPH as edges from node:");
//        for (int i = 0; i < vertices; i++) {
//            List<DirectedEdge> edgesFromNode = COMPUTERS_AS_GRAPH.getDirectedEdgesFromNode(i);
//            System.out.println(i + ": " + edgesFromNode.toString());
//        }

        System.out.println("COMPUTERS_AS_GRAPH as adjacency nodes:");
        for (int i = 0; i < vertices; i++) {
            List<Integer> adjacencyList = COMPS_AS_GRAPH.getAdjacencyList(i);
            System.out.print(i + "=" + COMP_LIST.get(i) + " : " + adjacencyList.toString());
            System.out.print(" = [");
            for (int j = 0; j < adjacencyList.size(); j++) {
                System.out.print(COMP_LIST.get(adjacencyList.get(j)));
                if (j < adjacencyList.size() - 1) System.out.print(", ");
            }
            System.out.print("]");
            System.out.println();
        }

    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/2024.day23/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToInputFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }
}
