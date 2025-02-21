/**
 * Graph Library Class
 * Dartmouth CS 10, Winter 2025
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */

import java.util.*;

public class GraphLibrary {
    /**
     *Complete a BFS on a graph starting from a given vertex.
     * @param g is the input graph
     * @param source is the starting vertex
     * @return a BFS tree
     */
    public static <V, E> Graph<V, E> bfs(Graph<V, E> g, V source) {
        Graph<V, E> tree = new AdjacencyMapGraph<>(); //create a new tree that will represent our BFS tree
        Queue<V> queue = new LinkedList<>(); //Create a queue for the BFS traversal
        Set<V> visited = new HashSet<>(); //Create a set to track the vertices we've visited

        tree.insertVertex(source); //add source vertex to the BFS tree
        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            V u = queue.remove(); // Dequeue vertex
            for (V v : g.outNeighbors(u)) { //go through all the neighbors
                if (!visited.contains(v)) { //only look at the ones that haven't been visited
                    visited.add(v);
                    queue.add(v);
                    tree.insertVertex(v); //add to the tree
                    tree.insertDirected(u, v, g.getLabel(u,v)); //add a directed edge between two vertices
                }
            }
        }

        return tree;
    }

    /**
     * Get the path from the root to a given vertex in the BFS tree
     * @param tree is the BFS tree
     * @param v is the target vertex
     * @return a list for the path from the root to the vertex, v
     */
    public static <V, E> List<V> getPath(Graph<V, E> tree, V v) {
        if (!tree.hasVertex(v)) return null; //if the vertex doesn't exist, return null

        List<V> shortestPath = new ArrayList<>();
        shortestPath.add(v);

        while (tree.inDegree(v) > 0) { //traverse through parents
            for (V node : tree.inNeighbors(v)) {
                v = node;
                shortestPath.add(v);
                break; //go to next parent
            }
        }

        return shortestPath;
    }

    /**
     * Find a set of the vertices that are in an original graph, but not our subgraph
     * @param graph is the original graph
     * @param subgraph is a subgraph that we're comparing to the original graph
     * @return a Set containing all  the vertices that are in graph, but aren't in subgraph
     */
    public static <V, E> Set<V> missingVertices(Graph<V, E> graph, Graph<V, E> subgraph) {
        Set<V> missing = new HashSet<>();
        for (V v : graph.vertices()) {
            missing.add(v); //add all the vertices in graph
        }

        for (V v : subgraph.vertices()) {
            missing.remove(v); //remove vertices that are in subgraph
        }

        return missing;
    }

    /**
     * Helper to get the sum of the "levels" of every node in the tree
     * @param tree is the BFS tree
     * @param vertex is the current vertex
     * @param level is the current depth level
     * @return the sum of the levels of all the nodes
     */
    private static <V, E> int helperLevelSum(Graph<V, E> tree, V vertex, int level) {
        int sum = level;
        for (V child : tree.outNeighbors(vertex)) {
            sum += helperLevelSum(tree, child, level + 1); //recursively call the next level
        }
        return sum;
    }

    /**
     * Helper that calculates the totle number of nodes in the tree
     * @param tree is the BFS tree
     * @param vertex is the current vertex
     * @return the total number of nodes in the tree
     */
    private static <V, E> int helperNodeSum(Graph<V, E> tree, V vertex) {
        int count = 1;
        for (V child : tree.outNeighbors(vertex)) {
            count += helperNodeSum(tree, child); //recursively add all the nodes
        }
        return count;
    }

    /**
     * Compute the average distance between a root and all the other nodes in the BFS tree
     * @param tree is the BFS tree
     * @param root is the root vertex
     * @return a double the represents the average separation
     */
    public static <V, E> double averageSeparation(Graph<V, E> tree, V root) {
        int totalSum = helperLevelSum(tree, root, 0); //sum of the levels of all the nodes
        int totalCount = helperNodeSum(tree, root); //number of nodes in the tree
        return (double) (totalSum) / (totalCount-1); //calculate the average separation, not including the root
    }

    public static void main(String[] args) {
        Graph<String,String> g = new AdjacencyMapGraph<>();

        // Insert Vertexxes
        g.insertVertex("Mithun");
        g.insertVertex("Ashok");
        g.insertVertex("Zac");
        g.insertVertex("Ava");
        g.insertVertex("Pierson");
        g.insertVertex("Sleep");
        g.insertVertex("Pheobe");

        //Insert Edges
        g.insertUndirected("Mithun", "Ashok", "Partners");
        g.insertUndirected("Mithun", "Ava", "TA");
        g.insertUndirected("Ashok", "Zac", "TA");
        g.insertUndirected("Mithun", "Pierson", null);
        g.insertUndirected("Ashok", "Pierson", null);
        g.insertUndirected("Zac", "Pierson", null);
        g.insertUndirected("Ava", "Pierson", null);
        g.insertUndirected("Zac", "Pheobe", null);

        //Insert Edges
        System.out.println(g);
        System.out.println();
        Graph<String, String> tree = GraphLibrary.bfs(g, "Mithun");
        System.out.println(GraphLibrary.getPath(tree, "Pheobe"));
        System.out.println();
        System.out.println("Missing Vertices" + GraphLibrary.missingVertices(g,tree));
        System.out.println();
        System.out.println("Mithun's Average Separation " + averageSeparation(tree, "Mithun"));
        System.out.println("Pierson's Average Separation " + averageSeparation(GraphLibrary.bfs(g, "Pierson"), "Pierson"));
        System.out.println("Pheobe's Average Separation " + averageSeparation(GraphLibrary.bfs(g, "Pheobe"), "Pheobe"));
    }
}
