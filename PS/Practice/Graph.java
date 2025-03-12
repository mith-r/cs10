import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public void DFS(AdjacencyMapGraph<V,E> G, V start) {
    backTrack = new HashMap<V,V>();
    backTrack.put(start, null);
    Set<V> visited = new HashSet<V>();
    Stack<V> stack = new Stack<V>();

    stack.push(start);
    while (!stack.isEmpty()) {
        V u = stack.pop();
        if (!visited.contains(u)) {
            visited.add(u);
            for (V v : G.outNeighbors(u)) {
                if (!visited.contains(v)) {
                    stack.push(v);
                    backTrack.put(v, u);
                }}}}}

public void BFS(AdjacencyMapGraph<V,E> G, V start) {
    backTrack = new HashMap<V,V>();
    backTrack.put(start, null);
    Set<V> visited = new HashSet<V>();
    Queue<V> queue = new LinkedList<V>();

    queue.add(start);
    visited.add(start);
    while (!queue.isEmpty()) {
        V u = queue.remove();
        for (V v : G.outNeighbors(u)) {
            if (!visited.contains(v)) {
                visited.add(v);
                queue.add(v);
                backTrack.put(v, u);
            }}}}

public List<V> findPath(V start, V end) {
    if (backTrack.isEmpty() || !backTrack.containsKey(start) ||
            (backTrack.containsKey(start) &&
                    backTrack.get(start) != null)) {
        return new ArrayList<V>();}
    if (!backTrack.containsKey(end)) {
        return new ArrayList<V>();}
    List<V> path = new LinkedList<V>();
    V current = end;
    while (current != null) {
        path.add(0,current);
        current = backTrack.get(current);
    System.out.println(path);
    return path;}}

void dijkstra(s) {
    queue = new PriorityQueue<Vertex>();
    for (each vertex v)
}
