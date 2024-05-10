package connectivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

/**
 * A more verbose connectivity inspector algorithm for undirected graphs.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class UndirectedConnectivityInspector<V, E> {

    private final Graph<V, E> graph;

    public UndirectedConnectivityInspector(Graph<V, E> graph) {
        if (graph == null) throw new NullPointerException("The input graph cannot be null");
        this.graph = graph;
    }

    public boolean isConnected() {
        if (graph.vertexSet().isEmpty()) return true;

        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();
        
        
        V startVertex = graph.vertexSet().iterator().next();
        queue.add(startVertex);
        visited.add(startVertex);

        while (!queue.isEmpty()) {
            V vertex = queue.poll();
            for (V neighbor : Graphs.neighborListOf(graph, vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return visited.size() == graph.vertexSet().size();
    }

    public List<Set<V>> connectedSets() {
        List<Set<V>> connectedSets = new ArrayList<>();
        Set<V> visited = new HashSet<>();

        for (V vertex : graph.vertexSet()) {
            if (!visited.contains(vertex)) {
                Set<V> component = new HashSet<>();
                Queue<V> queue = new LinkedList<>();
                
                queue.add(vertex);
                visited.add(vertex);
                component.add(vertex);

                while (!queue.isEmpty()) {
                    V current = queue.poll();
                    for (V neighbor : Graphs.neighborListOf(graph, current)) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            component.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
                
                connectedSets.add(component);
            }
        }

        return connectedSets;
    }
}
