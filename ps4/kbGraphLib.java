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
        Graph<V,E> bfsTree = new AdjacencyMapGraph<>(); //Create a new graph to store the BFS tree
        Queue<V> toVisit = new LinkedList<>(); //Create a queue to store the vertices to be visited
        Set<V> visited = new HashSet<>(); //Create a set to store the visited vertices
        toVisit.add(start); //Add the start vertex to the queue and the set of visited vertices
        bfsTree.insertVertex(start); //Add the start vertex to the BFS tree
        visited.add(start); //Mark the start vertex as visited
        //While there are vertices to visit
        while (!toVisit.isEmpty()) {
            V current = toVisit.remove();  //Dequeue the vertex at the front of the toVisit queue
            //For each out-neighbor of the current vertex
            for (V neighbor : g.outNeighbors(current)) {
                //If the neighbor has not been visited
                if (!visited.contains(neighbor)) {
                    toVisit.add(neighbor); //Add the neighbor to the queue and the set of visited vertices
                    bfsTree.insertVertex(neighbor); //Add the neighbor to the BFS tree
                    //Add an edge from the current vertex to the neighbor in the BFS tree
                    bfsTree.insertDirected(current, neighbor, g.getLabel(current, neighbor));
                    visited.add(neighbor); //Mark the neighbor as visited
                }
            }
        }
        return bfsTree;
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
        if (!tree.hasVertex(v)) {
            return null;
        }
        List<V> shortestPath = new ArrayList<>(); //create a new linked list that gets the shortest path
        shortestPath.add(v); //add the vertex to the linked list
        V current = v; //set the current vertex to the vertex
        //while the current vertex is not null
        while (current != null) {
            // get the in-neighbors of the current vertex
            for (V neighbor : tree.inNeighbors(current)) {
                // if there are no in-degree neighbors, set the current vertex to null
                if (tree.inDegree(neighbor) == 0) {
                    current = null;
                } else { // if there are in-degree neighbors
                    shortestPath.add(neighbor); // add the in-neighbors to the linked list
                    current = neighbor; // update the current node to the in-neighbor
                }
            }
        }
        return shortestPath;
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
        Set<V> missingVertices = new HashSet<>(); //create a new set to store the missing vertices
        //for each vertex in the graph
        for (V vertex : graph.vertices()) {
            //if the subgraph does not have the vertex
            if (!subgraph.hasVertex()) {
                missingVertices.add(vertex); //add the vertex to the set of missing vertices
            }
        }
        return missingVertices;
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
        double tDistance = totalDistance(tree, root, 0); //total distance
        int tVertices = tree.numVertices(); //total vertices
        return tDistance / tVertices; //return the average separation
    }

    /**
     * Helper method that takes in a graph, a vertex, and a distance from the root
     * returns the total distance from the root
     * @param tree
     * @param currVert
     * @param distanceFromRoot
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> double totalDistance(Graph<V,E> tree, V currVert, double distanceFromRoot) {
        //if the root has no out-degree, return the distance from the root
        if (tree.outDegree(currVert) == 0) {
            return distanceFromRoot;
        }
        double totalDistance = 0; //create a variable to store the total distance
        //for each out-neighbor of the root
        for (V neighbor : tree.outNeighbors(currVert)) {
            //add the total distance from the root to the out-neighbor to the total distance
            totalDistance += totalDistance(tree, neighbor, distanceFromRoot + 1);
        }
        return totalDistance;
    }
}
