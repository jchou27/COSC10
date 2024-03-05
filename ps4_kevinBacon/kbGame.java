import java.util.*;

/**
 * @Author: Jack Chou
 * @Purpose: This class is the main class for the Kevin Bacon Game
 */

public class kbGame {

    /**
     * Prints out the commands for the user to input
     */
    public static void printCommands() {
        System.out.println("Commands");
        System.out.println("c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation");
        System.out.println("d <low> <high>: list actors sorted by degree, with degree between low and high");
        System.out.println("i: list actors with infinite separation from the current center");
        System.out.println("p <name>: find path from <name> to current center of the universe");
        System.out.println("s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high");
        System.out.println("u <name>: make <name> the center of the universe");
        System.out.println("q: quit game" + "\n");
    }

    /**
     * Resets the universe center to the inputted actor
     * @param graph
     * @param centerUniv
     * @return
     */
    public static Graph<String, Set<String>> resetUniverseCenter(Graph<String, Set<String>> graph, String centerUniv){
        Graph<String, Set<String>> pathTree = new AdjacencyMapGraph<>(); // create new graph

        // if the actor is in the graph
        if (graph.hasVertex(centerUniv)){
            pathTree = kbGraphLib.bfs(graph, centerUniv); // create new tree

            double avg = kbGraphLib.averageSeparation(pathTree, centerUniv); // calculate average separation
            int n_actors = pathTree.numVertices() - 1; // number of actors not including the center
            int total_actors = graph.numVertices(); // total number of actors

            System.out.println(centerUniv + " is now the center of the acting universe, connected to "
                    + n_actors + "/" + total_actors + " actors with average separation " + avg);

            System.out.println("\n" + centerUniv + " game >");
        }
        else {
            System.err.println("Invalid actor"); // if the actor is not in the graph
        }
        return pathTree;
    }

    /**
     * Displays the path from the center of the universe to the inputted actor
     * @param pathTree
     * @param centerUniv
     * @param name
     */
    public static void displayPath(Graph<String, Set<String>> pathTree, String centerUniv, String name){
        List<String> path = kbGraphLib.getPath(pathTree, name);
        // if the actor is the center of the universe
        if (name.equals(centerUniv)) {
            System.out.println(name + "'s number is 0");
        }
        // if the actor is not in the graph
        else if (path.size() == 0) {
            System.out.println(name + "'s number is infinite");
        }
        // if the actor is in the graph
        else {
            System.out.println(name + "'s number is " + (path.size() - 1));
        }

        String actor_1 = name; // first actor
        // loop through the path and print out the actors and movies that actor 1 and actor 2 were in
        for (String actor_2 : path){
            // if the actors are not the same
            if (!actor_1.equals(actor_2)) {
                Set<String> movies = pathTree.getLabel(actor_1, actor_2); // get the movies
                System.out.println(actor_1 + " appeared in " + movies.toString() + " with " + actor_2); // print out the actors and movie
                actor_1 = actor_2; // set actor 1 to actor 2 for the next iteration
            }
        }
        System.out.println("\n" + centerUniv + " game >");
    }

    /**
     * Ranks actors by their separation from the center of the universe
     * @param pathTree
     * @param centerUniv
     * @param low
     * @param high
     * @return
     */
    public static Map<Integer, Set<String>> rankBySeparation(Graph<String, Set<String>> pathTree, String centerUniv, int low, int high) {
        Map<Integer, Set<String>> rank = new HashMap<>();
        // loop through the vertices in the path tree
        for (String actor: pathTree.vertices()) {
            // if the actor is the center of the universe, skip
            if (actor.equals(centerUniv)) {
                continue;
            }
            int separation = kbGraphLib.getPath(pathTree, actor).size() - 1; // get the separation
            // if the separation is between low and high
            if (separation >= low && separation <= high){
                // if the separation is already in the map, add the actor to the set
                if (rank.containsKey(separation)) {
                    rank.get(separation).add(actor);
                }
                // if the separation is not in the map, create a new set and add the actor to it
                else {
                    Set<String> actorsSet = new HashSet<>();
                    actorsSet.add(actor);
                    rank.put(separation, actorsSet);
                }
            }
        }
        return rank;
    }

    public static void avgSep(Graph<String, Set<String>> graph, int num) {
        if (num == 0) {
            System.out.println("Please enter non-zero number");
        }
        Map<String, Double> avgSep = new HashMap<>(); // map to store the average separations

        // loop through the vertices in the graph
        for (String actor : graph.vertices()) {
            Graph<String, Set<String>> pathTree = kbGraphLib.bfs(graph, actor);
            double avg = kbGraphLib.averageSeparation(pathTree, actor);
            avgSep.put(actor, avg);
        }

        // create a priority queue to store the actors sorted by their average separation
        Queue<String> sortedActorsPQ;
        // if the number is positive, create a min heap
        if (num > 0) {
            sortedActorsPQ = new PriorityQueue<>(Comparator.comparingDouble(avgSep::get)); // min heap
        }
        // if the number is negative, create a max heap
        else {
            sortedActorsPQ = new PriorityQueue<>(Comparator.comparingDouble(avgSep::get).reversed()); // max heap
        }

        // loop through the actors and add them to the priority queue
        for (String actor : avgSep.keySet()) {
            sortedActorsPQ.add(actor);
            // if the number of actors in the priority queue is greater than the absolute value of the input number, remove the actor with the smallest or largest average separation
            if (sortedActorsPQ.size() > Math.abs(num)) {
                sortedActorsPQ.remove();
            }
        }

        // create a list to store the actors in the priority queue
        List<String> sortedListAL = new ArrayList<>(sortedActorsPQ);
        // loop through the actors in the list and print out the actors and their average separations
        for (int i = Math.abs(num); i > 0; i--) {
            String actor = sortedListAL.get(i - 1);
            System.out.println("Top average separation " + avgSep.get(actor) + ", " + actor);
        }
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {

        String pathName1 = "data/actors.txt";
        String pathName2 = "data/movies.txt";
        String pathName3 = "data/movie-actors.txt";
        Graph<String, Set<String>> graph = kbBuildGraph.makeGraph(pathName1, pathName2, pathName3);

        printCommands();

        String centerUniv = "Kevin Bacon"; //Initial center of the universe
        Graph<String, Set<String>> pathTree = resetUniverseCenter(graph, centerUniv); //Initial tree

        Scanner input = new Scanner(System.in);
        while (true) {
            String line = input.nextLine();
            String[] split = line.split(" ");

            // if no input
            if (line.isEmpty() || split.length == 0) {
                System.out.println("Please enter a command with its associated parameters");
            }
            // list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation input commands
            else if (split[0].equals("c")){
                if (split.length != 2) {
                    System.out.println("Incorrect Parameters");
                    continue;
                }

                int n = Integer.parseInt(split[1]);
                avgSep(pathTree, n);
            }
            // list of actors sorted by degree between low and high input commands
            else if (split[0].equals("d")) {
                if (split.length != 3) {
                    System.out.println("Incorrect Parameters");
                    continue;
                }

                int low = Integer.parseInt(split[1]);
                int high = Integer.parseInt(split[2]);

                Map<Integer, Set<String>> rank = kbGraphLib.verticesByInDegree(graph); // get the rank
                // loop through the rank and print out the actors and their degrees
                for (Integer key : rank.keySet()) {
                    if (key >= low && key <= high) {
                        Set<String> actors = rank.get(key);
                        System.out.println(actors.size() + " actors with degree " + key + " " + actors);
                    }
                }
            }
            // infinite input commands
            else if (split[0].equals("i")){
                Set<String> alone = kbGraphLib.missingVertices(graph, pathTree);
                System.out.println(alone.size() + " actors have infinite separation with " + centerUniv);
                System.out.println(alone);
            }
            // find name path input commands
            else if (split[0].equals("p")){
                if (split.length == 1){
                    System.out.println("Incorrect Parameters");
                    continue;
                }
                int idx = line.indexOf('p');
                String fullName = line.substring(idx + 2);
                if (!graph.hasVertex(fullName))
                    System.out.println("Unknown Actor");
                else
                    displayPath(pathTree, centerUniv, fullName);
            }
            // list of actors sorted by non-infinite separation from the current center, with separation between low and high input commands
            else if (split[0].equals("s")) {
                if (split.length != 3) {
                    System.out.println("Incorrect Parameters");
                    continue;
                }
                int low = Integer.parseInt(split[1]);
                int high = Integer.parseInt(split[2]);

                var rank = rankBySeparation(pathTree, centerUniv, low, high);
                for (Integer key : rank.keySet()) {
                    Set<String> actors = rank.get(key);
                    System.out.println(actors.size() + " actors with separation " + key + " " + actors);
                }
            }
            // name input commands
            else if (split[0].equals("u")){
                if (split.length == 1) {
                    System.out.println("Incorrect Parameters");
                    continue;
                }
                int idx = line.indexOf('u');
                String full_name = line.substring(idx + 2);
                if (! graph.hasVertex(full_name))
                    System.out.println("Unknown Actor");
                else {
                    centerUniv = full_name;
                    pathTree = resetUniverseCenter(graph, centerUniv);
                }
            }
            // quit input commands
            else if (split[0].equals("q")) {
                System.exit(0);
            }
        }
    }
}

