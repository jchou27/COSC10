import java.util.*;

/**
 * Test program for graph interface
 *
 * @author Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024, based on prior term code
 */
public class RelationshipsTest extends GraphLib{
	public static void main(String [] args) {
		Graph<String, String> relationships = new AdjacencyMapGraph<String, String>();

		relationships.insertVertex("A");
		relationships.insertVertex("B");
		relationships.insertVertex("C");
		relationships.insertVertex("D");
		relationships.insertVertex("E");
		relationships.insertDirected("A", "D", "");
		relationships.insertDirected("A", "C", "");
		relationships.insertDirected("A", "B", "");
		relationships.insertDirected("A", "E", "");
		relationships.insertUndirected("B", "A", "");
		relationships.insertDirected("B", "C", "");
		relationships.insertDirected("C", "A", "");
		relationships.insertDirected("C", "D", "");
		relationships.insertDirected("C", "B", "");
		relationships.insertDirected("E", "C", "");
		relationships.insertDirected("E", "B", "");

		System.out.println("The Graph: \n" + relationships);

		List<String> verticesByInDegree = verticesByInDegree(relationships);
		System.out.println("Vertices by in degree: " + verticesByInDegree);

		List<String> randomWalk = randomWalk(relationships, "A", 5);
		System.out.println("Random walk: " + randomWalk);

	}
}
