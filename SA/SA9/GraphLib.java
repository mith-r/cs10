import java.util.Comparator;
import java.util.*;

/**
 * Library for graph analysis
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * @author Tim Pierson, Dartmouth CS10, provided for Winter 2025
 * @author Mithun Rameshkumar
 */
public class GraphLib {
    /**
     * Takes a random walk from a vertex, up to a given number of steps
     * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
     * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
     * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
     * @param g		graph to walk on
     * @param start	initial vertex (assumed to be in graph)
     * @param steps	max number of steps
     * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
     * 			    null if start isn't in graph
     */
    public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
        // TODO: your code here
        V current = start;
        List<V> res = new LinkedList<>();

        for (int i = 0; i <= steps; i++){
            res.add(current);
            Iterator<V> iterator = g.outNeighbors(current).iterator();

            if (g.outDegree(current) == 0){
                return res;
            }

            int random = (int) (Math.random() * (g.outDegree(current))-1);

            for (int j = 0; j <= random; j++){
                current = iterator.next();
            }
        }

        return res;
    }

    /**
     * Orders vertices in decreasing order by their in-degree
     * @param g		graph
     * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
     */
    public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
        // TODO: your code here
        List<V> res = new ArrayList<>();
        for (V vertex: g.vertices()){
            res.add(vertex);
        }

        res.sort(new VertexComparator<V,E>(g));
        return res;
    }


    private static class VertexComparator<V,E> implements Comparator<V> {
        private Graph <V,E> g;

        public VertexComparator(Graph<V,E> g){
            this.g = g;
        }

        @Override
        public int compare(V o1, V o2) {
            int i1 = g.inDegree(o1);
            int i2 = g.inDegree(o2);

            return Integer.compare(i2,i1);
        }
    }
}