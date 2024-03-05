import java.io.*;
import java.util.*;

/**
 * @Author: Jack Chou
 * @Purpose: This class reads the actor, movie, and actor-movie relationship files and builds a graph
 */
public class kbBuildGraph {
    /**
     * Reads the actor or movie files and builds a map
     * @param in_file
     * @return
     */
    public static Map<Integer, String> readFile(String in_file){
        Map<Integer, String> newMap = new HashMap<>();
        try{
            BufferedReader input = new BufferedReader(new FileReader(in_file));
            String line = input.readLine();
            while (line != null) {
                String[] split = line.split("\\|");
                newMap.put(Integer.parseInt(split[0]), split[1]);
                line = input.readLine();
            }
            input.close();
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
        return newMap;
    }

    /**
     * Reads the actor-movie relationship file and builds a map
     * @param in_file
     * @return
     */
    public static Map<Integer, Set<Integer>> readRelationshipFile(String in_file){
        Map<Integer, Set<Integer>> relationships = new HashMap<>();
        try {
            BufferedReader input = new BufferedReader(new FileReader(in_file));
            String line = input.readLine();
            while (line != null){
                String [] split = line.split("\\|");
                Integer movieID = Integer.parseInt(split[0]);
                Integer actorID = Integer.parseInt(split[1]);
                if (relationships.containsKey(movieID)){
                    relationships.get(movieID).add(actorID);
                }
                else {
                    Set<Integer> actors = new HashSet<>();
                    actors.add(actorID);
                    relationships.put(movieID, actors);
                }

                line = input.readLine();
            }
            input.close();
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
        return relationships;
    }

    /**
     * Builds a graph from the actor, movie, and actor-movie relationship files
     * @param actor_file
     * @param movie_file
     * @param actormovie_file
     * @return
     */
    public static Graph<String,Set<String>> makeGraph(String actor_file, String movie_file, String actormovie_file){
        Graph<String, Set<String>> finalGraph = new AdjacencyMapGraph<>();
        Map<Integer, String> actorsMap = readFile(actor_file);
        Map<Integer, String> moviesMap = readFile(movie_file);
        Map<Integer, Set<Integer>> relationsMap = readRelationshipFile(actormovie_file);

        // insert vertices of actors
        for (Integer key : actorsMap.keySet()){
            if (actorsMap.get(key) != null){
                finalGraph.insertVertex(actorsMap.get(key));
            }
        }

        // insert edges of movies
        for (Integer movieID : relationsMap.keySet()) { // for each movie
            String movie_name = moviesMap.get(movieID);
            for (Integer key1 : relationsMap.get(movieID)){ // for each actor in the movie
                for (Integer key2 : relationsMap.get(movieID)){ // for each actor in the movie
                    String actor1 = actorsMap.get(key1);
                    String actor2 = actorsMap.get(key2);

                    if (!actor1.equals(actor2)){ // if the actors are not the same
                        if (finalGraph.hasEdge(actor1, actor2)) { // if the edge already exists
                            finalGraph.getLabel(actor1, actor2).add(movie_name); // add the movie to the edge
                        }
                        else if (!finalGraph.hasEdge(actor1, actor2)) { // if the edge does not exist
                            Set<String> edge = new HashSet<>(); // create a new edge
                            edge.add(movie_name); // add the movie to the edge
                            finalGraph.insertUndirected(actor1, actor2, edge); // insert the edge
                        }
                    }
                }
            }
        }
        return finalGraph;
    }

    /**
     * Test to see if the graph is made properly
     */

    public static void main(String[] args) {
        String pathName1 = "data/actorsTest.txt";
        String pathName2 = "data/moviesTest.txt";
        String pathName3 = "data/movie-actorsTest.txt";
        System.out.println(readFile(pathName1));
        System.out.println(readFile(pathName2));
        System.out.println(readRelationshipFile(pathName3));
        System.out.println(makeGraph(pathName1, pathName2, pathName3));
    }
}
