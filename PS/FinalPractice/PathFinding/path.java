import net.datastructures.AdjacencyListGraph;
import net.datastructures.Graph;
import net.datastructures.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class path<V,E> {

    private AdjacencyMapGraph<String, Integer> cities;

    public path(){
        cities = new AdjacencyMapGraph<>();
    }

    public void createGraph(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] splitLine = line.split("|");
                String vertex1 = splitLine[0].strip();
                String vertex2 = splitLine[1].strip();
                Integer distance = Integer.parseInt(splitLine[2].strip());

                if (!cities.hasVertex(vertex1)){
                    cities.insertVertex(vertex1);
                }

                if (!cities.hasVertex(vertex2)){
                    cities.insertVertex(vertex2);
                }

                cities.insertUndirected(vertex1,vertex2,distance);
            }

        } catch (Exception e){
            System.err.println(e);
        }
    }

    public AdjacencyMapGraph<String,Integer> bfs(String source){
        AdjacencyMapGraph<String, Integer> result = new AdjacencyMapGraph<>();
        result.insertVertex(source);
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(source);
        visited.add(source);
        while(!queue.isEmpty()){
            String u = queue.remove();
            for (String v: cities.outNeighbors(source)){
                if (!visited.contains(v)){
                    visited.add(v);
                    queue.add(v);
                    result.insertVertex(u);
                    result.insertDirected(u,v,null);
                }
            }
        }

        return result;
    }

    public List<String> findPath(String city, AdjacencyMapGraph<String,Integer> backTrack){
        if (!backTrack.hasVertex(city)){
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        String current = city;
        result.add(current);
        while (backTrack.outDegree(current) > 0){
            result.add(0,current);
            for (String u : backTrack.outNeighbors(current)) {
                result.add(u);
                current = u;
            }
        }
        return result;
        }

    }




}
