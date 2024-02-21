import java.util.Set;
/**
 * @author Jack Chou
 */

public class startKbGame {
    public static void main(String[] args) throws Exception {
        String actorPath = "data/actors.txt";
        String moviePath = "data/movies.txt";
        String actorMoviePath = "data/movie-actors.txt";

        graphCreate myGraph = new graphCreate(actorPath, moviePath, actorMoviePath);
        myGraph.buildMap();
        AdjacencyMapGraph<String, Set<String>> map = myGraph.getMap();

        kevinBaconGame.getList(map);

    }
}
