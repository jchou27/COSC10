import java.util.*;

/**
 * @Author: Jack Chou
 * @Purpose: This class contains methods for graph operations
 */
public class kbGraphLib {
    /**
     * Breadth-first search
     * @param g
     * @param source
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V, E> Graph<V, E> bfs(Graph<V, E> g, V source) {
        Graph<V, E> pathTree = new AdjacencyMapGraph<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        pathTree.insertVertex(source); // Insert the source vertex into the pathTree
        queue.add(source); // Add the source vertex to the queue
        visited.add(source);// Add the source vertex to the visited set

        // Loop through the queue
        while (!queue.isEmpty()) {
            V u = queue.remove(); // Remove the first vertex from the queue
            for (V v : g.outNeighbors(u)) { // Loop through the out-neighbors of the vertex
                if (!visited.contains(v)) { // If the out-neighbor has not been visited
                    visited.add(v); // Add the out-neighbor to the visited set
                    queue.add(v); // Add the out-neighbor to the queue
                    pathTree.insertVertex(v); // Add the out-neighbor to the pathTree
                    pathTree.insertDirected(v, u, g.getLabel(u, v)); // Add the edge from the out-neighbor to the vertex to the pathTree
                }
            }
        }
        return pathTree;
    }

    /**
     * Get the path from the root to a vertex
     * @param tree
     * @param v
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
        List<V> path = new ArrayList<>();

        // If the vertex is not in the tree, return an empty path
        if(!tree.hasVertex(v)) {
            return path;
        }

        V current = v; // Set the current vertex to the input vertex
        path.add(current); // Add the current vertex to the path

        // Loop through the out-neighbors of the current vertex
        while (tree.outDegree(current) > 0){
            current = tree.outNeighbors(current).iterator().next(); // Set the current vertex to the first out-neighbor
            path.add(current); // Add the current vertex to the path
        }
        return path;
    }

    /**
     * Get the vertices that are in the graph but not in the subgraph, aka missing vertices
     * @param graph
     * @param subgraph
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
        Set<V> missing = new HashSet<>();

        // Loop through the vertices in the graph
        for (V v : graph.vertices()){
            // If the vertex is not in the subgraph, add it to the missing set
            if (!subgraph.hasVertex(v)){
                missing.add(v);
            }
        }
        return missing;
    }

    /**
     * Get the average separation of all vertices from the root
     * @param tree
     * @param root
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
        double sum = 0;

        // Loop through the vertices in the tree
        for (V vertex : tree.vertices()) {
            // If the vertex is not the root
            if (vertex != root) {
                List<V> path = getPath(tree, vertex); // Get the path from the root to the vertex
                sum += path.size(); // Add the length of the path to the sum
            }
        }
        return (sum / tree.numVertices() - 1) ; // subtract 1 to account for the root
    }

    /**
     * Get the vertices in the graph ordered by in-degree using a map
     * @param g
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> Map<Integer, Set<V>> verticesByInDegree(Graph<V, E> g) {
        Map<Integer, Set<V>> rankMap = new HashMap<>();
        // Loop through the vertices in the graph
        for(V v: g.vertices()) {
            Integer n = g.inDegree(v); // Get the in-degree of the vertex
            // If the in-degree is already in the map, add the vertex to the set
            if (rankMap.containsKey(n)) {
                rankMap.get(n).add(v);
            }
            // If the in-degree is not in the map, create a new set and add the vertex to it
            else {
                Set<V> mySet = new HashSet<>();
                mySet.add(v);
                rankMap.put(n, mySet);
            }
        }
        return rankMap;
    }

    /**
     * Tester
     * @param args
     */
    public static void main(String[] args) {
        Graph<String, Set<String>> test = new AdjacencyMapGraph<>();
        String k = "Kevin Bacon";
        String a = "Alice";
        String c = "Charlie";
        String b = "Bob";
        String d = "Dartmouth (Earl thereof)";
        String n = "Nobody";
        String nf = "Nobody's Friend";

        // Insert vertices
        test.insertVertex(k);
        test.insertVertex(a);
        test.insertVertex(c);
        test.insertVertex(b);
        test.insertVertex(d);
        test.insertVertex(n);
        test.insertVertex(nf);

        // Insert edges, used HashSet to put multiple movies in one edge
        Set<String> kA = new HashSet<>();
        kA.add("A Movie");
        kA.add("E Movie");

        Set<String> kB = new HashSet<>();
        kB.add("A Movie");

        Set<String> aB = new HashSet<>();
        aB.add("A Movie");

        Set<String> aC = new HashSet<>();
        aC.add("D Movie");

        Set<String> bC = new HashSet<>();
        bC.add("C Movie");

        Set<String> cD = new HashSet<>();
        cD.add("B Movie");

        Set<String> nNf = new HashSet<>();
        nNf.add("F Movie");

        test.insertUndirected(k, a, kA);
        test.insertUndirected(k, b, kB);
        test.insertUndirected(a, b, aB);
        test.insertUndirected(a, c, aC);
        test.insertUndirected(b, c, bC);
        test.insertUndirected(c, d, cD);
        test.insertUndirected(n, nf, nNf);

        //Test Output
        System.out.println((test));

        Graph<String, Set<String>> bfs_result = bfs(test, k);
        System.out.println(bfs_result);

        System.out.println(getPath(bfs_result, c));
        System.out.println(missingVertices(test, bfs_result));
        System.out.println(averageSeparation(bfs_result, k));
    }

}