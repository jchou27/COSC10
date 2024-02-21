import java.util.*;


/**
 * @author Jack Chou
 * @Purpose Kevin Bacon Game
 */

public class kevinBaconGame{

    /**
     * Kevin Bacon Game Interface
     * @param map Graph of Actors
     * @param <V> Vertex
     * @param <E> Edge
     */
    public static <V,E> void getList (Graph<V,E> map) {
        Graph<V, E> universe = null;
        System.out.println(
                "Commands:\n" +
                        "c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation\n" +
                        "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
                        "i: list actors with infinite separation from the current center\n" +
                        "p <name>: find path from <name> to current center of the universe\n" +
                        "s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high\n" +
                        "u <name>: make <name> the center of the universe\n" +
                        "q: quit game");
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print(">");
            String commandInput = in.nextLine();
            String[] key = commandInput.split(" ");
            if (key[0].equals("c")) {
                System.out.println("Enter an integer: ");
                int input = Integer.parseInt(in.nextLine());
                rankActors(map, input);
            } else if (key[0].equals("d")) {
                System.out.println("Enter 'low' or 'high': ");
                String input = in.nextLine();
                degree(map, input);
            } else if (key[0].equals("i")) {
                infiniteSeparation(map, universe);
            } else if (key[0].equals("p")) {
                System.out.println("Enter the name of the actor: ");
                String input = in.nextLine();
                V actor = (V) input;
                path(map, universe, actor);
            } else if (key[0].equals("s")) {
                System.out.println("Enter 'low' or 'high': ");
                String input = in.nextLine();
                separation(map, input);
            } else if (key[0].equals("u")) {
                System.out.println("Enter the name of the actor: ");
                String input = in.nextLine();
                V actor = (V) input;
                universe = center(map, actor);
            } else if (key[0].equals("q")) {
                quit();
            }
        }
    }
    /**
     * Rank the actors based on their average separation from the start actor
     * @param map  graph to search
     * @param numOfActors (How many actors to rank, positive for top, negative for bottom
     * @param <V> vertex
     * @param <E> edge
     */
    public static <V,E> void rankActors (Graph<V,E> map, int numOfActors) {
        ArrayList<V> allActors = new ArrayList<>();
        for (V actor : map.vertices()) {
            allActors.add(actor);
        }
        allActors.sort( (a, b) -> (int) kbGraphLib.averageSeparation(kbGraphLib.bfs(map,b),b)- (int) kbGraphLib.averageSeparation(kbGraphLib.bfs(map,a),a));
        if (numOfActors > 0) {
            System.out.println("Top " + numOfActors + " actors by average separation:");
            for (int i = 0; i < numOfActors; i++) {
                System.out.println(allActors.get(i));
            }
        } else {
            System.out.println("Bottom " + Math.abs(numOfActors) + " actors by average separation:");
            allActors.sort(Comparator.comparingInt(a -> (int) kbGraphLib.averageSeparation(kbGraphLib.bfs(map,a),a)));
            for (int i = 0; i > numOfActors; i--) {
                System.out.println(allActors.get(i));
            }
        }
    }

    /**
     * Ranks by the degree of the actor
     * @param map graph to search
     * @param degree (low or high)
     * @param <V> vertex
     * @param <E> edge
     */
    public static <V,E> void degree(Graph<V,E> map, String degree) {
        ArrayList<V> allActors = new ArrayList<>();
        for (V vertex : map.vertices()) {
            allActors.add(vertex);
        }
        if (degree.equals("low")) {
            allActors.sort(Comparator.comparingInt(map::inDegree));
            System.out.println("Actors with the lowest degree:");
            System.out.println(allActors);
        } else if (degree.equals("high")) {
            allActors.sort((a,b) -> map.inDegree(b) - map.inDegree(a));
            System.out.println("Actors with the highest degree:");
            System.out.println(allActors);
        }
    }

    /**
     * List actors with infinite separation from the start actor
     * @param map       graph that represents the universe
     * @param center    graph that represents the center
     * @param <V>       vertex
     * @param <E>       edge
     */
    public static <V,E> void infiniteSeparation(Graph<V,E> map, Graph<V,E> center) {
        if (center == null) {
            System.out.println("No center found");
        } else {
            System.out.println("Actors with infinite separation from the center:");
            Set<V> missingVertices = kbGraphLib.missingVertices(map, center);
            System.out.println(missingVertices);
            System.out.println("Number of actors with infinite separation: " + missingVertices.size());
        }
    }

    /**
     * List the path from the start actor to the end actor
     * @param universe      graph of the universe with all the actors and movies
     * @param map           subgraph of the universe with other actors that can reach the starting actor
     * @param startingActor the starting actor
     */
    public static <V, E> void path(Graph<V,E> universe , Graph<V,E> map, V startingActor) {
        if(universe==null) {
            System.out.println("Set center first by using the command u");
        }
        else {
            ArrayList<V> thisPath = (ArrayList<V>) kbGraphLib.getPath(universe,startingActor);
            System.out.println(startingActor + "'s number is "+(thisPath.size()-1));
            for(int x = 0; x < thisPath.size()-1 ; x++){
                V actor = thisPath.get(x);
                V nextActor = thisPath.get(x+1);
                System.out.println(actor + " appeared in " + map.getLabel(actor, nextActor) + " with " + nextActor);
            }
        }
    }

    /**
     * Set the center actor and return a tree
     * @param map           the original map
     * @param centerActor   the center actor
     * @return              a new map with new center actor
     * @param <V>           vertex
     * @param <E>           edge
     */
    public static <V,E> Graph<V,E> center(Graph<V,E> map, V centerActor) {
        Graph<V,E> newMap = null;
        if (map.hasVertex(centerActor)) {
            newMap = kbGraphLib.bfs(map, centerActor);
            System.out.println("Center is set to " + centerActor);
            System.out.println("There are " + (newMap.numVertices()-1) + "/9235 actors with an average separation " + kbGraphLib.averageSeparation(newMap, centerActor));
        } else {
            System.out.println("Invalid center actor");
        }
        return newMap;
    }

    /**
     * Calculate the average separation of the graph
     * @param map
     * @param input
     * @param <V>
     * @param <E>
     */
    public static <V,E> void separation(Graph<V,E> map, String input) {
        Map<V, Double> averageSeparationMap = new HashMap<>();
        if (map == null) {
            System.out.println("No center found");
            return;
        }
        for (V vertex : map.vertices()) {
            averageSeparationMap.put(vertex, kbGraphLib.averageSeparation(kbGraphLib.bfs(map, vertex), vertex));
        }
        ArrayList<V> listOfVert = new ArrayList<>(averageSeparationMap.keySet());

        if (input.equals("low")){
            listOfVert.sort(Comparator.comparingInt(a -> (int) kbGraphLib.averageSeparation(kbGraphLib.bfs(map,a),a)));
        } else if (input.equals("high")) {
            listOfVert.sort((a,b) -> (int) kbGraphLib.averageSeparation(kbGraphLib.bfs(map,b),b) - (int) kbGraphLib.averageSeparation(kbGraphLib.bfs(map,a),a));
        }
        System.out.println(averageSeparationMap);
    }

    public static void quit(){
        System.exit(0);
    }
}

