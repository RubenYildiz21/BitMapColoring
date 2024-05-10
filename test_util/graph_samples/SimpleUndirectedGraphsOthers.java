package graph_samples;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class SimpleUndirectedGraphsOthers {
	/**
	 * @return A connected undirected graph with Double vertices
	 */
	public static SimpleGraph<Double, DefaultEdge> graphWithDoubleVertices() {
		SimpleGraph<Double, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (int v = 101; v <= 106; v++) {
			g.addVertex((double) v);
		}

		// Add edges
		g.addEdge(101.0, 102.0);
		g.addEdge(101.0, 104.0);
		g.addEdge(101.0, 105.0);
		g.addEdge(102.0, 103.0);
		g.addEdge(102.0, 105.0);
		g.addEdge(103.0, 105.0);
		g.addEdge(103.0, 106.0);
		g.addEdge(104.0, 105.0);
		g.addEdge(105.0, 106.0);

		return g;
	}

	/**
	 * @return A connected undirected graph with String vertices
	 */
	public static SimpleGraph<String, DefaultEdge> graphWithStringVertices() {
		SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (char c : "ABCDEF".toCharArray()) graph.addVertex("Vertex " + c);
		
		// Add edges
		graph.addEdge("Vertex A", "Vertex B");
		graph.addEdge("Vertex A", "Vertex C");
		graph.addEdge("Vertex A", "Vertex D");
		graph.addEdge("Vertex B", "Vertex C");
		graph.addEdge("Vertex B", "Vertex E");
		graph.addEdge("Vertex C", "Vertex F");
		graph.addEdge("Vertex D", "Vertex E");
		graph.addEdge("Vertex E", "Vertex F");
		return graph;
	}
	

}
