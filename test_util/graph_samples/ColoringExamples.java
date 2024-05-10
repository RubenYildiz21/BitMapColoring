package graph_samples;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ColoringExamples {

	/**
	 * Coloration exemple 1 - 3 couleurs
	 * @return a 3-colored graph
	 */
	public static SimpleGraph<Character, DefaultEdge> coloringExample1() {
		SimpleGraph<Character, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (char c : "ABCDEF".toCharArray()) graph.addVertex(c);
		
		// Add edges
		graph.addEdge('A', 'B');
		graph.addEdge('A', 'C');
		graph.addEdge('A', 'D');
		graph.addEdge('B', 'C');
		graph.addEdge('B', 'E');
		graph.addEdge('C', 'F');
		graph.addEdge('D', 'E');
		graph.addEdge('E', 'F');
		return graph;
	}
	
	/**
	 * Coloration exemple 2 - graphe biparti
	 * @return a biparti graph
	 */
	public static SimpleGraph<Integer, DefaultEdge> coloringExample2() {
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

		// Add vertices
		for (int i=1; i<=9; i++) graph.addVertex(i);
		
		// Add edges
		for (int i=1; i<9; i++) graph.addEdge(i, i+1);
		graph.addEdge(1, 8);
		graph.addEdge(9, 2);
		graph.addEdge(9, 4);
		graph.addEdge(9, 6);
		return graph;
	}

	/**
	 * MULLER exercice 51 - Communes d'Ajoie (4 couleurs)
	 * @return a 4-colored graph
	 */
	public static SimpleGraph<Integer, DefaultEdge> mullerEx51() {
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

		// Add vertices
		for (int n = 1; n <= 23; n++) {
			graph.addVertex(n);
		}

		// Add edges for adjacent areas
		graph.addEdge(1, 2);

		graph.addEdge(2, 3);
		graph.addEdge(2, 5);
		graph.addEdge(2, 7);
		graph.addEdge(2, 9);
		graph.addEdge(2, 10);

		graph.addEdge(3, 4);
		graph.addEdge(3, 5);

		graph.addEdge(4, 5);
		graph.addEdge(4, 6);

		graph.addEdge(5, 6);
		graph.addEdge(5, 7);
		graph.addEdge(5, 8);

		graph.addEdge(6, 8);

		graph.addEdge(7, 8);
		graph.addEdge(7, 10);
		graph.addEdge(7, 11);
		graph.addEdge(7, 12);

		graph.addEdge(8, 12);
		graph.addEdge(8, 13);

		graph.addEdge(9, 10);
		graph.addEdge(9, 11);
		graph.addEdge(9, 14);
		graph.addEdge(9, 15);
		graph.addEdge(9, 18);

		graph.addEdge(10, 11);

		graph.addEdge(11, 12);
		graph.addEdge(11, 15);
		graph.addEdge(11, 19);
		graph.addEdge(11, 20);
		graph.addEdge(11, 21);

		graph.addEdge(12, 13);
		graph.addEdge(12, 21);
		graph.addEdge(12, 22);

		graph.addEdge(13, 22);
		graph.addEdge(13, 23);

		graph.addEdge(14, 16);
		graph.addEdge(14, 17);
		graph.addEdge(14, 18);

		graph.addEdge(15, 18);
		graph.addEdge(15, 19);

		graph.addEdge(16, 17);
		graph.addEdge(16, 18);

		graph.addEdge(17, 18);

		graph.addEdge(18, 19);

		graph.addEdge(19, 20);
		graph.addEdge(19, 23);

		graph.addEdge(20, 21);
		graph.addEdge(20, 23);

		graph.addEdge(21, 22);
		graph.addEdge(21, 23);

		return graph;
	}

	/**
	 * MULLER exercice 45 - Horaire d'examens (4 couleurs)
	 * @return a 4-colored graph
	 */
	public static SimpleGraph<Integer, DefaultEdge> mullerEx45() {
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

		// Add vertices
		for (int n = 1; n <= 7; n++) {
			graph.addVertex(n);
		}
		
		// Add edges
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(1, 4);
		graph.addEdge(1, 7);
		graph.addEdge(2, 3);
		graph.addEdge(2, 4);
		graph.addEdge(2, 5);
		graph.addEdge(2, 7);
		graph.addEdge(3, 4);
		graph.addEdge(3, 6);
		graph.addEdge(3, 7);
		graph.addEdge(4, 5);
		graph.addEdge(4, 6);
		graph.addEdge(5, 6);
		graph.addEdge(5, 7);
		graph.addEdge(6, 7);
		return graph;
	}

	/**
	 * A wheel graph is 3-colored if n is even, 4-colored if n is odd.
	 * @param n number of peripheral vertices
	 * @return a wheel graph with 1 central vertex and n peripheral vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> wheelGraph(int n) {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

		// Add vertices
		g.addVertex(0);
		for (int i = 1; i <= n; i++) {
			g.addVertex(i);

			// Connect them in a loop
			g.addEdge(0, i);
			if (i > 1) g.addEdge(i, i - 1);
		}
		if (n > 2) g.addEdge(n, 1);
		return g;
	}

	/**
	 * The Petersen graph is 3-colored.
	 * @return a Petersen graph
	 */
	public static SimpleGraph<Integer, DefaultEdge> petersenGraph() {
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

		// Add vertices
		for (int n = 1; n <= 10; n++) {
			graph.addVertex(n);
		}
		
		// Add edges
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 4);
		graph.addEdge(4, 5);
		graph.addEdge(5, 1);
		
		graph.addEdge(1, 6);
		graph.addEdge(2, 7);
		graph.addEdge(3, 8);
		graph.addEdge(4, 9);
		graph.addEdge(5, 10);
		
		graph.addEdge(6, 8);
		graph.addEdge(6, 9);
		graph.addEdge(7, 9);
		graph.addEdge(7, 10);
		graph.addEdge(8, 10);
		return graph;
	}

	/**
	 * If n>=2, a linear graph is 2-colored.
	 * @return an linear graph with N connected vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> linearGraph(int n) {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		
		g.addVertex(0);
		for (int i=1; i<n; i++) {
			g.addVertex(i);
			g.addEdge(i, i-1);
		}
		return g;
	}

	/**
	 * If n>=2, a loop graph is 2-colored if n is even, 3-colored if n is odd.
	 * @return an loop graph with N connected vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> loopGraph(int n) {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		
		g.addVertex(0);
		for (int i=1; i<n; i++) {
			g.addVertex(i);
			g.addEdge(i, i-1);
		}
		g.addEdge(0, n-1);
		return g;
	}
}
