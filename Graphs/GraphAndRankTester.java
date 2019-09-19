
/*  Student information for assignment:
 *
 *  On my honor, <Pranav Teja Varanasi>, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  UTEID: ptv247
 *  email address: varanasipranav@gmail.com
 *  Grader name: Jacob Szwejbka
 *  Number of slip days I am using: 0
 */

/*
 * Question. The assignment presents three ways to rank teams using graphs.
 * The results, especially for the last two methods are reasonable.
 * However if all results from all college football teams are included
 * some unexpected results occur. Explain the unexpected results:
 * 
 * The unexpected results were that non-division 1 teams were ranked higher than
 * division 1 teams as the ranking algorithms sort data according to win/loss percentages and 
 * don't properly take into account which division the teams are from. In addition, this failure to take into account
 * division and proper weightage results in unusually high root-mean-square values.
 * 
 * Suggest another way of method of ranking teams using the results
 * from the graph. Thoroughly explain your method. The method can build
 * on one of the three existing algorithms.
 * 
 * Since the error in calculating rankings comes from the usage of the unweightedShortestPath method,
 * we can choose not to use the unweighted lengths and instead choose to create another ranking algorithm
 * based on groupings of weightedPaths. My idea would be to partition the average weighted path lengths
 * into subsets with similar weighted path lengths. I would create these partitions by using dijkstra's method
 * to create minimum-spanning-trees for each subset. I would then average the weights of each tree 
 * to determine a ranking order from which to properly position the teams according to division.
 * 
 */

public class GraphAndRankTester {

    /**
     * Runs tests on Graph classes and FootballRanker class.
     * Experiments involve results from college football
     * teams. Central nodes in the graph are compared to
     * human rankings of the teams.
     * @param args None expected.
     */
    public static void main(String[] args)  {
    	
    	//studentTests();   	
        //graphTests();

        String actual = "2008ap_poll.txt";
        String gameResults = "div12008.txt";

        FootballRanker ranker = new FootballRanker(gameResults, actual);

        ranker.doUnweighted(true);
        ranker.doWeighted(true);
        ranker.doWeightedAndWinPercentAdjusted(true);

        System.out.println();
        doRankTests(ranker);

        System.out.println();

    }
    
    private static void studentTests() {
    	System.out.println("PERFORMING STUDENT TESTS\n");
    	
    	
    	// Graph #1
    	String [][] g1Edges =  {{"A", "C", "20"},
                 {"C", "D", "20"},
                 {"B", "D", "50"},
                 {"A", "B", "10"},};
    	
    	Graph g1 = getGraph(g1Edges, false);
    	g1.dijkstra("A");
    	
    	String actualPath = g1.findPath("D").toString();
    	String expected = "[A, C, D]";
    	if (actualPath.equals(expected)) {
    		System.out.println("Passed dijkstra student path test graph 1.");
    	} else {
    		System.out.println("Failed dijkstra student path test graph 1. Expected: " + expected + " actual " + actualPath);
    	}
    	
    	// Graph #2
    	 String [][] g2Edges =  {{"A", "C", "20"},
                 {"A", "I", "12"},
                 {"C", "I", "93"},
                 {"C", "D", "20"},
                 {"C", "H", "46"},
                 {"I", "D", "59"},
                 {"D", "F", "2"},
                 {"F", "H", "89"},
                 {"C", "H", "46"}};
    	
    	Graph g2 = getGraph(g2Edges, false);
    	g2.dijkstra("A");
    	
    	String actualPath2 = g2.findPath("F").toString();
    	String expected2 = "[A, C, D, F]";
    	if (actualPath2.equals(expected2)) {
    		System.out.println("Passed dijkstra student path test graph 2.");
    	} else {
    		System.out.println("Failed dijkstra student path test graph 2. Expected: " + expected2 + " actual " + actualPath2);
    	}
    	
    	// Graph #3
        String [][] g3Edges =  {{"H", "I", "1"},                       
                        {"I", "K", "12"},
                        {"H", "N", "7"},
                        {"K", "M", "4"},
                        {"K", "N", "5"},
                        {"K", "L", "6"}};
        Graph g3 = getGraph(g1Edges, false);
        
        String[] expectedPaths3 = {"Name: D                    cost per path: 1.3333, num paths: 6",
                "Name: B                    cost per path: 1.5000, num paths: 6",
                "Name: F                    cost per path: 1.8333, num paths: 6",
                "Name: G                    cost per path: 1.8333, num paths: 6",
                "Name: A                    cost per path: 2.0000, num paths: 6",
                "Name: E                    cost per path: 2.1667, num paths: 6"};
        doAllPathsTests("Graph 3", g1, false, 3, 2.0, expectedPaths3);

        
        // Graph #4
        // now do all paths weighted
        expectedPaths3 = new String[] {  "Name: F                    cost per path: 6.5000, num paths: 6",
                "Name: C                    cost per path: 6.8333, num paths: 6",
                "Name: D                    cost per path: 7.1667, num paths: 6",
                "Name: B                    cost per path: 7.3333, num paths: 6",
                "Name: A                    cost per path: 7.8333, num paths: 6",
                "Name: G                    cost per path: 8.5000, num paths: 6",
                "Name: E                    cost per path: 12.1667, num paths: 6"};
        doAllPathsTests("Graph 3", g1, true, 5, 13.0, expectedPaths3);
    	
        String [][] g4Edges =
            {{"A", "B", "13"},
                            {"A", "C", "10"},
                            {"A", "D", "2"},
                            {"B", "E", "5"},
                            {"C", "B", "1"},
                            {"G", "F", "2"},
                            {"H", "I", "10"},
                            {"H", "J", "5"},
                            {"H", "K", "22"},
                            {"I", "K", "3"},
                            {"I", "J", "1"},
                            {"J", "L", "8"}};
       
        Graph g4 = getGraph(g4Edges, true);

        // do all paths weighted
        String[] expectedPaths4 = {"Name: A                    cost per path: 10.0000, num paths: 6",
                "Name: D                    cost per path: 9.6000, num paths: 5",
                "Name: F                    cost per path: 3.0000, num paths: 4",
                "Name: E                    cost per path: 4.2500, num paths: 4",
                "Name: G                    cost per path: 4.2500, num paths: 4",
                "Name: C                    cost per path: 5.7500, num paths: 4",
                "Name: B                    cost per path: 7.5000, num paths: 4",
                "Name: H                    cost per path: 10.2500, num paths: 4",
                "Name: I                    cost per path: 4.3333, num paths: 3",
                "Name: J                    cost per path: 8.0000, num paths: 1"};
        doAllPathsTests("Graph 4", g4, true, 6, 12.0, expectedPaths4);
        
    }

    // tests on various simple Graphs
    private static void graphTests() {
        System.out.println("PERFORMING TESTS ON SIMPLE GRAPHS\n");
        graph1Tests();
        graph2Tests();
        graph3Tests();
    }

    private static void graph1Tests() {
        System.out.println("Graph #1 Tests:");
        // first a simple path test
        // Graph #1
        String [][] g1Edges =  {{"A", "B", "1"},
                        {"B", "C", "3"},
                        {"B", "D", "12"},
                        {"C", "F", "3"},
                        {"A", "G", "7"},
                        {"D", "F", "4"},
                        {"D", "G", "5"},
                        {"D", "E", "6"}};
        Graph g1 = getGraph(g1Edges, false);

        g1.dijkstra("A");
        String actualPath = g1.findPath("E").toString();
        String expected = "[A, B, C, F, D, E]";
        if (actualPath.equals(expected)) {
            System.out.println("Passed dijkstra path test graph 1.");
        } else {
            System.out.println("Failed dijkstra path test graph 1. Expected: " + expected + " actual " + actualPath);
        }

        // now do all paths unweighted
        String[] expectedPaths = {"Name: D                    cost per path: 1.3333, num paths: 6",
                        "Name: B                    cost per path: 1.5000, num paths: 6",
                        "Name: F                    cost per path: 1.8333, num paths: 6",
                        "Name: G                    cost per path: 1.8333, num paths: 6",
                        "Name: A                    cost per path: 2.0000, num paths: 6",
                        "Name: C                    cost per path: 2.0000, num paths: 6",
                        "Name: E                    cost per path: 2.1667, num paths: 6"};
        doAllPathsTests("Graph 1", g1, false, 3, 3.0, expectedPaths);

        // now do all paths weighted
        expectedPaths = new String[] {  "Name: F                    cost per path: 6.5000, num paths: 6",
                        "Name: C                    cost per path: 6.8333, num paths: 6",
                        "Name: D                    cost per path: 7.1667, num paths: 6",
                        "Name: B                    cost per path: 7.3333, num paths: 6",
                        "Name: A                    cost per path: 7.8333, num paths: 6",
                        "Name: G                    cost per path: 8.5000, num paths: 6",
                        "Name: E                    cost per path: 12.1667, num paths: 6"};
        doAllPathsTests("Graph 1", g1, true, 5, 17.0, expectedPaths);
    }

    private static void graph2Tests() {
        System.out.println("Graph #2 Tests:");
        // first a simple path test
        // Graph #1
        String[][] g2Edges = {{"E", "G", "9.6"},
                        {"G", "E", "19.2"},
                        {"D", "F", "4.0"},
                        {"F", "D", "8.0"},
                        {"E", "B", "8.0"},
                        {"B", "E", "16.0"},
                        {"F", "A", "6.0"},
                        {"A", "F", "12.0"},
                        {"F", "C", "4.0"},
                        {"C", "F", "8.0"},
                        {"C", "E", "6.9"},
                        {"E", "C", "13.8"},
                        {"D", "G", "8.0"},
                        {"G", "D", "16.0"},
                        {"E", "A", "5.7"},
                        {"A", "E", "11.4"},
                        {"C", "A", "0.4"},
                        {"A", "C", "0.8"},
                        {"D", "A", "6.1"},
                        {"A", "D", "12.2"},
                        {"D", "B", "7.9"},
                        {"B", "D", "15.8"},
                        {"C", "G", "5.4"},
                        {"G", "C", "10.8"},
                        {"A", "B", "7.1"},
                        {"B", "A", "14.2"},
                        {"E", "F", "4.4"},
                        {"F", "E", "8.8"}};
        Graph g2 = getGraph(g2Edges, true);



        // do all paths weighted
        String[] expectedPaths = new String[] { "Name: C                    cost per path: 6.8000, num paths: 6",
                        "Name: A                    cost per path: 7.1333, num paths: 6",
                        "Name: D                    cost per path: 7.6167, num paths: 6",
                        "Name: F                    cost per path: 7.6833, num paths: 6",
                        "Name: E                    cost per path: 7.7667, num paths: 6",
                        "Name: G                    cost per path: 15.4667, num paths: 6",
                        "Name: B                    cost per path: 16.8667, num paths: 6"};
        doAllPathsTests("Graph 2", g2, true, 3, 20.4, expectedPaths);
    }

    // Graph 3 is an unconnected Graph
    private static void graph3Tests() {
        System.out.println("Graph 3 Tests. Graph 3 is not fully connected. ");
        String [][] g3Edges =
                    {{"A", "B", "13"},
                                    {"A", "C", "10"},
                                    {"A", "D", "2"},
                                    {"B", "E", "5"},
                                    {"C", "B", "1"},
                                    {"D", "C", "5"},
                                    {"E", "G", "1"},
                                    {"E", "F", "4"},
                                    {"F", "C", "3"},
                                    {"F", "E", "2"},
                                    {"G", "F", "2"},
                                    {"H", "I", "10"},
                                    {"H", "J", "5"},
                                    {"H", "K", "22"},
                                    {"I", "K", "3"},
                                    {"I", "J", "1"},
                                    {"J", "L", "8"}};
        Graph g3 = getGraph(g3Edges, true);

        // do all paths weighted
        String[] expectedPaths = {"Name: A                    cost per path: 10.0000, num paths: 6",
                        "Name: D                    cost per path: 9.6000, num paths: 5",
                        "Name: F                    cost per path: 3.0000, num paths: 4",
                        "Name: E                    cost per path: 4.2500, num paths: 4",
                        "Name: G                    cost per path: 4.2500, num paths: 4",
                        "Name: C                    cost per path: 5.7500, num paths: 4",
                        "Name: B                    cost per path: 7.5000, num paths: 4",
                        "Name: H                    cost per path: 10.2500, num paths: 4",
                        "Name: I                    cost per path: 4.3333, num paths: 3",
                        "Name: J                    cost per path: 8.0000, num paths: 1"};
        doAllPathsTests("Graph 3", g3, true, 6, 16.0, expectedPaths);
    }

    // return a Graph based on the given edges
    private static Graph getGraph(String[][] edges, boolean directed) {
        Graph result = new Graph();
        for (String[] edge : edges) {
            result.addEdge(edge[0], edge[1], Double.parseDouble(edge[2]));
            // If edges are for an undirected graph add edge in other direction too.
            if (!directed) {
                result.addEdge(edge[1], edge[0], Double.parseDouble(edge[2]));
            }
        }
        return result;
    }

    // Tests the all paths method. Run each set of tests twice to ensure the Graph
    // is correctly reseting each time
    private static void doAllPathsTests(String graphNumber, Graph g, boolean weighted,
                    int expectedDiameter, double expectedCostOfLongestShortestPath,
                    String[] expectedPaths) {

        System.out.println("\nTESTING ALL PATHS INFO ON " + graphNumber + ". ");
        for (int i = 0; i < 2; i++) {
            System.out.println("Test run = " + (i + 1));
            System.out.println("Find all paths weighted = " + weighted);
            g.findAllPaths(weighted);
            int actualDiameter = g.getDiameter();
            double actualCostOfLongestShortesPath = g.costOfLongestShortestPath();
            if (actualDiameter == expectedDiameter) {
                System.out.println("Passed diameter test.");
            } else {
                System.out.println("FAILED diameter test. "
                                + "Expected = "  + expectedDiameter +
                                " Actual = " + actualDiameter);
            }
            if (actualCostOfLongestShortesPath == expectedCostOfLongestShortestPath) {
                System.out.println("Passed cost of longest shortest path. test.");
            } else {
                System.out.println("FAILED cost of longest shortest path. "
                                + "Expected = "  + expectedCostOfLongestShortestPath +
                                " Actual = " + actualCostOfLongestShortesPath);
            }
            testAllPathsInfo(g, expectedPaths);
            System.out.println();
        }

    }

    // Compare results of all paths info on Graph to expected results.
    private static void testAllPathsInfo(Graph g, String[] expectedPaths) {
        int index = 0;

        for (AllPathsInfo api : g.getAllPaths()) {
            if (expectedPaths[index].equals(api.toString())) {
                System.out.println(expectedPaths[index] + " is correct!!");
            } else {
                System.out.println("ERROR IN ALL PATHS INFO: ");
                System.out.println("index: " + index);
                System.out.println("EXPECTED: " + expectedPaths[index]);
                System.out.println("ACTUAL: " + api.toString());
                System.out.println();
            }
            index++;
        }
        System.out.println();
    }

    // Test the FootballRanker on the given file.
    private static void doRankTests(FootballRanker ranker) {
        System.out.println("\nTESTS ON FOOTBALL TEAM GRAPH WITH FootBallRanker CLASS: \n");
        double actualError = ranker.doUnweighted(false);
        if (actualError == 13.7) {
            System.out.println("Passed unweighted test");
        } else {
            System.out.println("FAILED UNWEIGHTED ROOT MEAN SQUARE ERROR TEST. Expected 13.7, actual: " + actualError);
        }

        actualError = ranker.doWeighted(false);
        if (actualError == 12.6) {
            System.out.println("Passed weigthed test");
        } else {
            System.out.println("FAILED WEIGHTED ROOT MEAN SQUARE ERROR TEST. Expected 12.6, actual: " + actualError);
        }


        actualError = ranker.doWeightedAndWinPercentAdjusted(false);
        if (actualError == 6.3) {
            System.out.println("Passed unweighted win percent test");
        } else {
            System.out.println("FAILED WEIGHTED  AND WIN PERCENT ROOT MEAN SQUARE ERROR TEST. Expected 6.3, actual: " + actualError);
        }
    }
}