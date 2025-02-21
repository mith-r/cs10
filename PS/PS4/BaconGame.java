/**
 * Kevin Bacon Game Simulation
 * Dartmouth CS 10, Winter 2025
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */

import java.io.*;
import java.util.*;

public class BaconGame {
    private static final String MOVIES = "inputs/movies.txt";
    private static final String MOVIE_ACTORS = "inputs/movie-actors.txt";
    private static final String ACTORS = "inputs/actors.txt";

    private Map<String, Set<String>> actorMovies; //map actors to all the movies they've acted in
    private Graph<String, Set<String>> coStarGraph; // graph that represents co-starring relationships
    private Graph<String, Set<String>> tree; // BFS tree that starts with the actor that's currently in the center

    private String currentCenter; //current center actor for the game

    public BaconGame() {
        actorMovies = new HashMap<>();
        coStarGraph = new AdjacencyMapGraph<>();

        try{
            createActorMoviesMap(); //initialize the actor-movie relationships
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        createGraph(); //make a graph with the co starring relationships
        currentCenter = "Kevin Bacon"; //sets default center as Kevin Bacon
        tree = GraphLibrary.bfs(coStarGraph, currentCenter); // creates a BFS tree starting from Kevin Bacon
    }

    //read movies file and makes a map that maps movie IDs to movie titles
    private Map<Integer, String> createMovieMap() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES))) {
            Map<Integer, String> res = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split("\\|");
                res.put(Integer.parseInt(movie[0]), movie[1]);
            }
            return res;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    //reads actors file and makes a map that maps actor IDs to their names
    private Map<Integer, String> createActorMap() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACTORS))) {
            Map<Integer,String> res = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] movie = line.split("\\|");
                res.put(Integer.parseInt(movie[0]), movie[1]);
            }
            return res;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    //reads movie actors file and makes a map that maps actors to the movies they've been in
    private void createActorMoviesMap() throws IOException {
        try (BufferedReader reader= new BufferedReader(new FileReader(MOVIE_ACTORS))){
            Map<Integer, String> movies = createMovieMap();
            Map<Integer, String> actors = createActorMap();
            actorMovies = new HashMap<>();

            String line;

            while ((line = reader.readLine()) != null){
                String[] info = line.split("\\|");

                String movie = movies.get(Integer.parseInt(info[0]));
                String actor = actors.get(Integer.parseInt(info[1]));

                if (!actorMovies.containsKey(actor)){
                    actorMovies.put(actor, new HashSet<>());
                }
                actorMovies.get(actor).add(movie);
            }

        } catch (IOException e){
            throw new IOException(e);
        }
    }

    // creates a co-star graph in which actors are connected to each other if they've acted in the same movie together
    private void createGraph(){
        List <String> actorList = new ArrayList<>(actorMovies.keySet());

        coStarGraph.insertVertex(actorList.get(0)); //put first actor into the graph

        for (int i = 0; i < actorList.size() - 1; i ++){ //loop through all the combinations of actors
            for (int j = i + 1; j < actorList.size(); j ++){
                String actor1 = actorList.get(i);
                String actor2 = actorList.get(j);

                coStarGraph.insertVertex(actor2);

                //make a set and store all the common movies between to actors in it
                Set<String> common = new HashSet<>(actorMovies.get(actor1));
                common.retainAll(actorMovies.get(actor2));

                if (!common.isEmpty()) {
                    coStarGraph.insertUndirected(actor2, actor1, common);
                }
            }

        }
    }

    //print command line instructions for playing the actual game
    private void printInstructions(){
        System.out.println("Commands:");
        System.out.println("c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation");
        System.out.println("d <low> <high>: list actors sorted by degree, with degree between low and high");
        System.out.println("i: list actors with infinite separation from the current center");
        System.out.println("p <name>: find path from <name> to current center of the universe");
        System.out.println("s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high");
        System.out.println("u <name>: make <name> the center of the universe");
        System.out.println("q: quit game");
        System.out.println("NOTE: MUST ADD SPACE BETWEEN COMMAND AND INPUT");
        System.out.println();
    }


    //prints the total number of connections and the average separation of the new center of the universe
    private void printNumberConnections(){
        //we do -1 here so that we don't include the current actor in the total number of possible connections
        System.out.println(currentCenter + " is now the center of the acting universe, connected to " + (tree.numVertices()-1) + "/" +
                (coStarGraph.numVertices() - 1) + " actors with average separation " + GraphLibrary.averageSeparation(tree, currentCenter));
        System.out.println();
    }

    //prints the current center of the universe
    private void printCurrentCenter(){
        System.out.println(currentCenter + " game > ");
    }

    //creates a list of top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation
    private List<String> pressC(String input){
        String[] inputArray = input.split("\\s+");
        int num = Integer.parseInt(inputArray[1].trim());

        Map<String, Double> mapOfAverage = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>((s1,s2) -> Double.compare(mapOfAverage.get(s1), mapOfAverage.get(s2)));

        //make a set of the actors that aren't in the BFS tree
        Set<String> missing = GraphLibrary.missingVertices(coStarGraph, tree);

        //find the average separation for each actor and store it in a priority queue
        for (String actor : coStarGraph.vertices()){
            if (!missing.contains(actor)) { //don't include the actors that aren't connected to the BFS tree
                double average = GraphLibrary.averageSeparation(GraphLibrary.bfs(coStarGraph, actor), actor);
                mapOfAverage.put(actor, average);
                pq.add(actor);
            }
        }

        //extract the actors in sorted order
        List<String> inOrder = new ArrayList<>();
        while (!pq.isEmpty()){
            inOrder.add(pq.poll());
        }

        //return top or bottom of the pq depending on the input
        if (num > 0){
            return inOrder.subList(0,num);
        }
        if (num < 0){
            return inOrder.subList(inOrder.size()+num, inOrder.size());
        }

        return inOrder;
    }

    // update "center of the universe" and makes a new BFS tree based on the new root
    private void pressU(String input){
        String[] inputArray = input.split("\\s+", 2);
        String result = inputArray[1].trim();

        currentCenter = result;
        tree = GraphLibrary.bfs(coStarGraph, currentCenter);

        printNumberConnections();
    }

    //prints path from any actor to the center of the universe
    private void pressP(String input){
        String[] inputArray = input.split("\\s+", 2);
        String current = inputArray[1].trim();

        List<String> path = GraphLibrary.getPath(tree, current);

        //Print Bacon number of the actor
        System.out.println(current + "'s number is " + (GraphLibrary.getPath(tree, current).size() - 1));

        for (int i = 0; i < path.size() - 1; i++) {
            String actor1 = path.get(i);
            String actor2 = path.get(i + 1);
            System.out.println(actor1 + " appeared in " + tree.getLabel(actor2, actor1) + " with " + actor2);
        }
    }

    //creates a list of actors sorted by non-infinite separation from the current center, with separation between low and high
    private Map<String, Integer> pressS(String input){
        String[] inputArray = input.split("\\s+");
        int low = Integer.parseInt(inputArray[1]);
        int high = Integer.parseInt(inputArray[2]);
        Map<String, Integer> mapOfDistance = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>((s1, s2) -> Integer.compare(mapOfDistance.get(s1), mapOfDistance.get(s2)));

        //compute distances for all connected actors
        for (String actor : tree.vertices()) {
            int distance = GraphLibrary.getPath(tree, actor).size() - 1;
            if (distance >= low && distance <= high) {
                mapOfDistance.put(actor, distance);
                pq.add(actor);
            }
        }

        Map<String, Integer> separation = new HashMap<>();
        while (!pq.isEmpty()) {
            String actor = pq.poll();
            separation.put(actor, mapOfDistance.get(actor));
        }

        return separation;
    }

    // creates a map of actors and their degree that's sorted by degree, with degree between low and high
    private Map<String, Integer> pressD(String input){
        String[] inputArray = input.split("\\s+");
        int low = Integer.parseInt(inputArray[1]);
        int high = Integer.parseInt(inputArray[2]);
        Map<String, Integer> mapOfDegrees = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>((s1, s2) -> Integer.compare(mapOfDegrees.get(s2), mapOfDegrees.get(s1)));

        //calculate degrees for all actors
        for (String actor : coStarGraph.vertices()) {
            int degree = coStarGraph.outDegree(actor);
            if (degree >= low && degree <= high) {
                mapOfDegrees.put(actor, degree);
                pq.add(actor);
            }
        }

        Map<String, Integer> separation = new HashMap<>();
        while (!pq.isEmpty()) {
            String actor = pq.poll();
            separation.put(actor, mapOfDegrees.get(actor));
        }

        return separation;
    }

    //run the interactive game in a loop
    public void runGame() throws Exception {
        printInstructions();
        printNumberConnections();

        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                printCurrentCenter();
                String input = scanner.nextLine();

                //handle all the different commands
                if (input.startsWith("u")) {
                    pressU(input);
                } else if (input.startsWith("p")) {
                    pressP(input);
                } else if (input.startsWith("i")) {
                    System.out.println(GraphLibrary.missingVertices(coStarGraph, tree));
                } else if (input.startsWith("s")) {
                    System.out.println(pressS(input));
                } else if (input.equals("q")) {
                    break;
                } else if (input.startsWith("d")) {
                    System.out.println(pressD(input));
                } else if (input.startsWith("c")) {
                    System.out.println(pressC(input));
                } else {
                    System.err.println("Unknown Command. Try again.");
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Invalid Input. Check index/spacing and try again.");
            } catch (NullPointerException e) {
                System.err.println("Invalid Input. Check actor name and try again.");
            } catch (NumberFormatException e){
                System.err.println("Invalid Input. Check input type and try again.");
            }
        }
    }

    //Run the game!
    public static void main(String[] args) throws Exception {
        BaconGame game = new BaconGame();
        game.runGame();
    }
}