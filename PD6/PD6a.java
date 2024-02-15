import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Find all connected groups in a graph of cities.
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2024, based on idea from Pratim Chowdhary
 */
public class PD6a {
    /**
     * Find all groups (sometimes called cliques) of cities connected to each other
     * @param graph AdjacencyMapGraph of cities as String vertices
     * @return List where each entry is a List of connected cities.
     */
    public static List<ArrayList<String>> findGroups(Graph<String,String> graph) {
        List<ArrayList<String>> groups = new ArrayList<ArrayList<String>>(); //each group is a clique
        Set<String> visited = new HashSet<String>(); //track cities visited
        //TODO: Your code here
        //loop over all vertices in graph
        for (String City : graph.vertices()) {
            //skip if vertex already visited
            //if found unvisted vertex, create new list of connected cities
            if (!visited.contains(City)) {
                ArrayList<String> connected = new ArrayList<>();
                //create list of vertices toVisit, initial just this one
                List<String> toVisit = new ArrayList<>();
                toVisit.add(City);
                //loop until no vertices toVisit
                while (!toVisit.isEmpty()) {
                    //remove vertex to visit, mark visited, add to list of connected cities
                    String toVisitCity = toVisit.remove(0);
                    if (!toVisit.contains(toVisitCity)) {
                        visited.add(toVisitCity);
                        connected.add(toVisitCity);
                        //loop over neighbors
                        for (String neighbor : graph.outNeighbors(toVisitCity)) {
                            //add unvisited neighbors toVisit
                            if (!visited.contains(neighbor)) {
                                toVisit.add(neighbor);
                            }
                        }
                    }
                }
                //add list to groups
                groups.add(connected);
            }
        }
        return groups;
    }

    public static void main(String [] args) {
        Graph<String,String> g = new AdjacencyMapGraph<String, String>();

        //add vertices
        g.insertVertex("Hanover"); g.insertVertex("Boston");
        g.insertVertex("Washington D.C."); g.insertVertex("Los Angeles");
        g.insertVertex("Paris"); g.insertVertex("Berlin");
        g.insertVertex("Sydney"); g.insertVertex("Melbourne");
        g.insertVertex("Honolulu");

        //add edges (not using edge lables in this graph)
        g.insertUndirected("Hanover", "Boston", null);
        g.insertUndirected("Boston", "Washington D.C.", null);
        g.insertUndirected("Washington D.C.", "Los Angeles", null);
        g.insertUndirected("Los Angeles", "Hanover", null);
        g.insertUndirected("Paris", "Berlin", null);
        g.insertUndirected("Sydney", "Melbourne", null);

        //find connected groups
        System.out.println(findGroups(g));
    }
}

