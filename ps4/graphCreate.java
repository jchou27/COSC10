import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author Jack Chou
 */
public class graphCreate {
    /**
     * Declare the file paths
     */
    static String actorPath = "";
    static String moviePath = "";
    static String actorMoviePath = "";

    /**
     * Creating a graph that connects an actor to other actors with the movies they have in common
     */
    AdjacencyMapGraph<String, Set<String>> myMap = new AdjacencyMapGraph<>();

    /**
     * Default constructor
     * If not given parameters, use the test files
     */
    public graphCreate() {
        actorPath = "ps4/data/actorsTest.txt";
        moviePath = "ps4/data/moviesTest.txt";
        actorMoviePath = "ps4/data/movie-actorsTest.txt";
    }

    /**
     * Constructor that takes in the file paths
     * @param actorPath
     * @param moviePath
     * @param actorMoviePath
     */
    public graphCreate(String actorPath, String moviePath, String actorMoviePath) {
            this.actorPath = actorPath;
            this.moviePath = moviePath;
            this.actorMoviePath = actorMoviePath;
    }

    /**
     * load the actor data into the graph
     */
    public static Map <String, String> loadActors(String pathname) throws Exception {
        Map <String, String> actorIDs = new HashMap<>(); //Create a new map to store the actors
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathname)); //Create a new buffered reader to read the file
            String line; //Create a string to store the line
            //While there are lines to read
            while ((line = br.readLine()) != null) {
                String[] actor = line.split("\\|"); //Split the line by the pipe delimiter
                actorIDs.put(actor[0], actor[1]); //Add the actor to the map
            }
            br.close(); //Close the buffered reader
        } catch (Exception e) {
            System.out.println("Actor file is not loading properly.");
        }
        return actorIDs;
    }

    /**
     * load the movie data into the graph
     */
    public static Map <String, String> loadMovies(String pathname) throws Exception {
        Map<String, String> movieIDs = new HashMap<>(); //Create a new map to store the movies
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathname)); //Create a new buffered reader to read the file
            String line; //Create a string to store the line
            //While there are lines to read
            while ((line = br.readLine())!= null) {
                String[] movie = line.split("\\|"); //Split the line by the pipe delimiter
                movieIDs.put(movie[0], movie[1]); //Add the movie to the map
            }
            br.close(); //Close the buffered reader
        } catch (Exception e) {
            System.out.println("Movie file is not loading properly.");
        }
        return movieIDs;
    }

    /**
     * load the actor-movie data into the graph
     */
    public static Map <String, ArrayList<String>> loadActorMovies(String pathname) throws Exception {
        Map<String, ArrayList<String>> actorMovieMap = new HashMap<>(); //Create a new map to store the actors and movies
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathname)); //Create a new buffered reader to read the file
            String line; //Create a string to store the line
            //While there are lines to read
            while ((line = br.readLine()) != null) {
                String[] actorMovie = line.split("\\|"); //Split the line by the pipe delimiter
                ArrayList<String> actors; //Declare a new array list to store the actors
                //If the actor is not in the map
                if (!actorMovieMap.containsKey(actorMovie[0])) {
                    actors = new ArrayList<>(); //Create a new array list to store the actors
                } else {
                    actors = actorMovieMap.get(actorMovie[0]); //Otherwise, get the actors from the map
                }
                actors.add(actorMovie[1]); //Add the actor to the list of actors
                actorMovieMap.put(actorMovie[0], actors); //Add the list of actors to the map
            }
            br.close(); //Close the buffered reader
        } catch (Exception e) {
            System.out.println("Actor-Movie file is not loading properly.");
        }
        return actorMovieMap;
    }

    /**
     * create the adjacency map graph with actors as vertex and movies as edges
     */

    public void buildMap() throws Exception {
        //Load the actor, movie, and actor-movie data into the graph
        Map<String, String> actorMap = loadActors(actorPath);
        Map<String, String> movieMap = loadMovies(moviePath);
        Map<String, ArrayList<String>> actorMovieMap = loadActorMovies(actorMoviePath);



        //For each actor in the actor map
        for (String actorID : actorMap.keySet()) {
            myMap.insertVertex(actorMap.get(actorID)); //Insert the actor into the graph
        }

        // For each movie in the key set of the actor-movie map
        for (String movie : actorMovieMap.keySet()) {
            ArrayList<String> allActors = actorMovieMap.get(movie); //Get the list of actors for the movie
            //For each actor in the list of actors
            for (String actor : allActors) {
                for (int i = 0; i < allActors.size(); i++) {
                    if (!actor.equals(allActors.get(i))) {
                        Set<String> movies = new HashSet<>(); //Create a new set to store the movies
                        if (myMap.hasEdge(actorMap.get(actor), actorMap.get(allActors.get(i)))) {
                            movies = myMap.getLabel(actorMap.get(actor), actorMap.get(allActors.get(i))); //Get the movies from the graph
                        }
                        movies.add(movieMap.get(movie)); //Add the movie to the set of movies
                        myMap.insertUndirected(actorMap.get(actor), actorMap.get(allActors.get(i)), movies); //Insert the actor and the movie into the graph
                    }
                }
            }
        }
    }

    /**
     * get the graph
     * @return
     */
    public AdjacencyMapGraph<String, Set<String>> getMap() {
        return myMap;
    }

    /**
     * main method
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        graphCreate graph = new graphCreate();
        graph.buildMap();
        System.out.println(graph.getMap());
    }
}
