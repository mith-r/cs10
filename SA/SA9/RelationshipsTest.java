/**
 * Test program for graph interface
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2025, based on prior term code
 */
public class RelationshipsTest {
	public static void main(String [] args) {
		Graph<String, String> relationships = new AdjacencyMapGraph<String, String>();

		relationships.insertVertex("Alice");
		relationships.insertVertex("Bob");
		relationships.insertVertex("Charlie");
		relationships.insertVertex("Dartmouth");
		relationships.insertVertex("Elvis");
		relationships.insertDirected("Alice", "Dartmouth", "follower");
		relationships.insertDirected("Bob", "Dartmouth", "follower");
		relationships.insertDirected("Charlie", "Dartmouth", "follower");
		relationships.insertDirected("Elvis", "Dartmouth", "follower");
		relationships.insertUndirected("Alice", "Bob", "friend"); // symmetric, undirected edge
		relationships.insertDirected("Alice", "Elvis", "friend"); // not symmetric, directed edge!
		relationships.insertDirected("Charlie", "Elvis", "follower");

		System.out.println(relationships);
		System.out.println();

		System.out.println(GraphLib.randomWalk(relationships, "Alice", 1));
		System.out.println();
		System.out.println(GraphLib.verticesByInDegree(relationships));
	}
}
