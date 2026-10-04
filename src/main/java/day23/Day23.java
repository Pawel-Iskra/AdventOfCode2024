package day23;

import utils.MyUtils;

import java.util.*;

public class Day23 {


    private static final String CONNECTION_SYMBOL = "-";
    private static final int NUMBER_IN_SET = 3;
    private static final String LETTER_REQUIRED = "t";

    private static List<String> COMP_LIST;
    private static Graph CONNECTION_GRAPH;


    static class Graph {
        private final int vertices;
        private final Set<Integer>[] adjacencySets;

        public Graph(int vertices) {
            this.vertices = vertices;
            adjacencySets = new Set[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencySets[i] = new HashSet<>();
            }
        }

        public Set<Integer> getAdjacencySet(int vertex) {
            return adjacencySets[vertex];
        }

        public void addEdge(int from, int to) {
            adjacencySets[from].add(to);
            adjacencySets[to].add(from);
        }

        // set of three comps where each comp is connected to each other
        public Set<List<Integer>> getThreeComputersConnectionSets() {
            Set<List<Integer>> resultListSets = new HashSet<>();
            for (int vertex = 0; vertex < vertices; vertex++) {
                List<Integer> neighbours = new ArrayList<>(getAdjacencySet(vertex));
                int neighboursSize = neighbours.size();
                for (int i = 0; i < neighboursSize; i++) {
                    for (int j = i + 1; j < neighboursSize; j++) {
                        int firstNeighbour = neighbours.get(i);
                        int secondNeighbour = neighbours.get(j);
                        if (getAdjacencySet(firstNeighbour).contains(secondNeighbour)) {
                            List<Integer> resultThreeCompList = new ArrayList<>(List.of(vertex, firstNeighbour, secondNeighbour));
                            Collections.sort(resultThreeCompList);
                            resultListSets.add(resultThreeCompList);
                        }
                    }
                }
            }
            return resultListSets;
        }

        @Override
        public String toString() {
            StringBuilder strb = new StringBuilder();
            strb.append("Graph = ").append("vertices=").append(vertices);
            for (int i = 0; i < vertices; i++) {
                strb.append("\n").append(i).append("->").append(getAdjacencySet(i));
            }
            return strb.toString();
        }
    }


    private static void prepareData(List<String> inputLines) {
        Set<String> computers = new HashSet<>();
        for (String line : inputLines) {
            String[] currentComps = line.split(CONNECTION_SYMBOL);
            computers.add(currentComps[0]);
            computers.add(currentComps[1]);
        }
        COMP_LIST = new ArrayList<>(computers);
        CONNECTION_GRAPH = new Graph(COMP_LIST.size());
        for (String line : inputLines) {
            String[] currentComps = line.split(CONNECTION_SYMBOL);
            String firstComp = currentComps[0];
            String secondComp = currentComps[1];
            CONNECTION_GRAPH.addEdge(COMP_LIST.indexOf(firstComp), COMP_LIST.indexOf(secondComp));
        }
//        System.out.println("COMP_LIST = " + COMP_LIST);
//        System.out.println("CONNECTION_GRAPH = " + CONNECTION_GRAPH);
    }


    private static void partOne() {
        int counter = 0;
        Set<List<Integer>> threeCompConnSetLists = CONNECTION_GRAPH.getThreeComputersConnectionSets();
        for (List<Integer> list : threeCompConnSetLists) {
            for (int currentComp : list) {
                if (COMP_LIST.get(currentComp).startsWith(LETTER_REQUIRED)) {
                    counter++;
                    break;
                }
            }
        }
        System.out.println("Part I = " + counter);
    }


    private static void partTwo() {

    }


    static void main() {
        String pathToFile = "src/main/resources/2024.day23/input.txt";
        List<String> inputLines = MyUtils.getInputLines(pathToFile);

        prepareData(inputLines);
        partOne();
        partTwo();
    }

}
