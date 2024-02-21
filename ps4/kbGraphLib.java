import java.util.*;

/**
 * @author Jack Chou
 */
public class kbGraphLib {
    /**
     * Create a bfs method that takes in a graph and a start vertex
     * returns a bfs tree of the graph
     * @param g
     * @param start
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V start) {
        Graph<V,E> backTrack = new AdjacencyMapGraph<>(); //Create a new graph to store the BFS tree
        backTrack.insertVertex(start); //Add the start vertex to the BFS tree
        Set<V> visited = new HashSet<>(); //Create a set to store the visited vertices
        Queue<V> bfsQueue = new LinkedList<>(); //Create a queue to store the vertices to visit

        bfsQueue.add(start); //Add the start vertex to the
        visited.add(start); //Add the start vertex to the queue and the set of visited vertices
        while (!bfsQueue.isEmpty()) {
            V vertex = bfsQueue.remove(); //Remove the vertex from the queue
            for (V neighbor : g.outNeighbors(vertex)) { //For each out neighbor of the vertex
                if (!visited.contains(neighbor)) { //If the neighbor has not been visited
                    visited.add(neighbor); //Add the neighbor to the set of visited vertices
                    bfsQueue.add(neighbor); //Add the neighbor to the queue
                    backTrack.insertVertex(neighbor); //Add the neighbor to the backTrack tree
                    backTrack.insertDirected(neighbor, vertex, null); //Add the edge to the backTrack tree
                }
            }
        }
        return backTrack;
    }

    /**
     * Create a getPath method that takes in a graph and a start vertex
     * returns a list of verticies that represent the shortest path from the start vertex to the root in visited order
     * @param tree
     * @param v
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
        if (tree.numVertices() == 0 || !tree.hasVertex(v) ) { //if the tree does not have the vertex, return null
            return new ArrayList<>();
        }
        List<V> path = new ArrayList<>(); //create a new linked list that gets the shortest path
        V current = v; //set the current vertex to the start vertex
        path.add(0, current); //add the current vertex to the path
        while (tree.outDegree(current) != 0) { //while the out degree of the vertex is not 0
           for (V neighbor : tree.outNeighbors(current)) { //for each out neighbor of the current vertex
                    current = neighbor; //set the current vertex to the neighbor
           }
           path.add(0, current); //add the current vertex to the path
        }
        return path;
    }

    /**
     * Create a missingVertices method that takes in a graph and a subgraph
     * Compares the vertices of the graph and the subgraph and returns a set of missing vertices
     * @param graph
     * @param subgraph
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        Set<V> missingVerticesSet = new HashSet<>(); //create a new set to store the missing vertices
        //for each vertex in the graph
        for (V vertex : graph.vertices()) {
            //if the subgraph does not have the vertex
            if (!subgraph.hasVertex(vertex)) {
                missingVerticesSet.add(vertex); //add the vertex to the set of missing vertices
            }
        }
        return missingVerticesSet;
    }

    /**
     * Create a method that takes in a graph and a root vertex
     * returns the average separation of the graph
     * @param tree
     * @param root
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
        double sum = 0;

        for (V vertex : tree.vertices()) {
            if (vertex != root) {
                List<V> path = getPath(tree, vertex);
                sum += path.size();
            }
        }
        return sum / tree.numVertices();
    }
}
