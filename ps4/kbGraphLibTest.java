import java.util.HashSet;
import java.util.Set;

/**
 * @author Jack Chou
 */
public class kbGraphLibTest {
    public static void main(String[] args) throws Exception {
        //Create a new graph
        Graph<String, Set<String>> testGraph = new AdjacencyMapGraph<>();
        //Insert vertices into the graph
        testGraph.insertVertex("Kevin Bacon");
        testGraph.insertVertex("Alice");
        testGraph.insertVertex("Bob");
        testGraph.insertVertex("Charlie");
        testGraph.insertVertex("Dartmouth (Earl thereof)");
        testGraph.insertVertex("Nobody");
        testGraph.insertVertex("Nobody's Friend");
        //Insert edges into the graph
        testGraph.insertUndirected("Kevin Bacon", "Alice", new HashSet<>());
        testGraph.getLabel("Kevin Bacon", "Alice").add("A movie");
        testGraph.getLabel("Kevin Bacon", "Alice").add("E movie");

        testGraph.insertUndirected("Kevin Bacon", "Bob", new HashSet<>());
        testGraph.getLabel("Kevin Bacon", "Bob").add("A movie");

        testGraph.insertUndirected("Alice", "Bob", new HashSet<>());
        testGraph.getLabel("Alice", "Bob").add("A movie");

        testGraph.insertUndirected("Alice", "Charlie", new HashSet<>());
        testGraph.getLabel("Alice", "Charlie").add("D movie");

        testGraph.insertUndirected("Bob", "Charlie", new HashSet<>());
        testGraph.getLabel("Bob", "Charlie").add("C movie");

        testGraph.insertUndirected("Charlie", "Dartmouth (Earl thereof)", new HashSet<>());
        testGraph.getLabel("Charlie", "Dartmouth (Earl thereof)").add("B movie");

        testGraph.insertUndirected("Nobody", "Nobody's Friend", new HashSet<>());
        testGraph.getLabel("Nobody", "Nobody's Friend").add("F movie");


        graphCreate test = new graphCreate();
        test.buildMap();
        AdjacencyMapGraph<String, Set<String>> map = test.getMap();
        System.out.println(map);


        //Test Case 1
        Graph<String, Set<String>> current = kbGraphLib.bfs(testGraph, "Dartmouth (Earl thereof)");
        System.out.println("Get Path from Dartmouth (Earl thereof) to Alice: ");
        System.out.println(kbGraphLib.getPath(current, "Alice"));
        System.out.println("Missing vertices: ");
        System.out.println(kbGraphLib.missingVertices(map, current));
        System.out.println("Average separation from Dartmouth (Earl thereof): ");
        System.out.println(kbGraphLib.averageSeparation(current, "Dartmouth Earl thereof"));




    }
}
