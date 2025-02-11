/**
 * Test case for GraphLib
 * @author Mithun Rameshkumar
 */

public class GraphLibTest{
    public static void main(String[] args) {
        Graph<Character, String> test = new AdjacencyMapGraph<>();

        test.insertVertex('A');
        test.insertVertex('B');
        test.insertVertex('C');
        test.insertVertex('D');
        test.insertVertex('E');

        test.insertDirected('A','B',"");
        test.insertDirected('A','C',"");
        test.insertDirected('A','D',"");
        test.insertDirected('A','E',"");

        test.insertDirected('B','A',"");
        test.insertDirected('B','C',"");

        test.insertDirected('C','A',"");
        test.insertDirected('C','B',"");
        test.insertDirected('C','D',"");

        test.insertDirected('E','B',"");
        test.insertDirected('E','C',"");

        System.out.println(test);
        System.out.println();

        System.out.println(GraphLib.randomWalk(test,'B',0));
        System.out.println(GraphLib.randomWalk(test, 'A', 3));
        System.out.println(GraphLib.randomWalk(test,'A',3));
        System.out.println(GraphLib.randomWalk(test,'C',7));

        System.out.println();
        System.out.println("Vertices in decreasing popularity: " + GraphLib.verticesByInDegree(test));
    }
}
