import java.util.*;

/**
 * Library for graph analysis
 *
 * @author Jack Chou
 * @source Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * 
 */
public class GraphLib {
	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		// TODO: your code here
		// Check if the start vertex is in the graph
		if (!g.hasVertex(start)) {
			return null;
		}
		List <V> path = new ArrayList<V>(); // Create a list to store the path
		path.add(start); // Add the start vertex to the path

		Random rand = new Random(); // Create a random number generator
		// Loop through the number of steps given by the user
		for (int i = 0; i < steps; i++) {
			V current = path.get(path.size() - 1); // Get the current vertex
			List<V> outNeighbors = new ArrayList<>(); // Create a list to store the out-neighbors of the current vertex
			// Loop through the out-neighbors of the current vertex and add them to the list
			for (V neighbor : g.outNeighbors(current)) {
				outNeighbors.add(neighbor);
			}
			// If there are no out-neighbors, break the loop
			if (outNeighbors.isEmpty()) {
				break;
			}
			// Select a random out-neighbor and add it to the path
			int randomIndex = rand.nextInt(outNeighbors.size());
			path.add(outNeighbors.get(randomIndex));
		}
		return path;
	}
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		// TODO: your code here
		if (g.numVertices() == 0) {
			return null;
		}
		// Create a list of pairs of in-degrees and vertices
		List<Map.Entry<Integer, V>> degreeList = new ArrayList<>();
		for (V vertex : g.vertices()) {
			int inDegree = g.inDegree(vertex);
			degreeList.add(new AbstractMap.SimpleEntry<>(inDegree, vertex));
		}
		// Sort the list by in-degree
		degreeList.sort((o1, o2) -> o2.getKey() - o1.getKey());

		// Create a list of vertices sorted by in-degree
		List<V> sortedVertices = new ArrayList<>();
		for (Map.Entry<Integer, V> entry : degreeList) {
			sortedVertices.add(entry.getValue());
		}
		return sortedVertices;
	}
}
