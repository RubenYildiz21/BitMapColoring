package graph_samples;

import java.util.function.Supplier;

import org.jgrapht.generate.CompleteBipartiteGraphGenerator;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.generate.PlantedPartitionGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.util.SupplierUtil;

public class SimpleUndirectedGraphsInteger {
	/**
	 * @return An undirected graph with 1 vertex
	 */
	public static SimpleGraph<Integer, DefaultEdge> oneVertex() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
		g.addVertex(123);
		return g;
	}

	/**
	 * @return An undirected graph with 2 disconnected vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> twoDisconnectedVertices() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
		g.addVertex(123);
		g.addVertex(456);
		return g;
	}

	/**
	 * @return An undirected graph with N disconnected vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> disconnectedVertices(int n) {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
		for (int i = 0; i < n; i++) {
			g.addVertex(i * 42);
		}
		return g;
	}

	/**
	 * @return An undirected graph with 2 connected vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> twoConnectedVertices() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
		g.addVertex(123);
		g.addVertex(456);
		g.addEdge(123, 456);
		return g;
	}

	/**
	 * @return A connected undirected graph [MULLER example p.20]
	 */
	public static SimpleGraph<Integer, DefaultEdge> connectedUndirectedGraph() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (int v = 1; v <= 6; v++) {
			g.addVertex(v);
		}

		// Add edges
		g.addEdge(1, 2);
		g.addEdge(1, 4);
		g.addEdge(1, 5);
		g.addEdge(2, 3);
		g.addEdge(2, 5);
		g.addEdge(3, 5);
		g.addEdge(3, 6);
		g.addEdge(4, 5);
		g.addEdge(5, 6);

		return g;
	}

	/**
	 * @return A disconnected undirected graph [based on MULLER example p.20]
	 */
	public static SimpleGraph<Integer, DefaultEdge> disconnectedUndirectedGraph() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (int n = 1; n <= 6; n++) {
			g.addVertex(n);
		}

		// Add edges
		g.addEdge(1, 2);
		g.addEdge(1, 4);
		g.addEdge(1, 5);
		g.addEdge(2, 5);
		g.addEdge(3, 6);
		g.addEdge(4, 5);

		return g;
	}

	/**
	 * @return A complete graph Kn
	 */
	public static SimpleGraph<Integer, DefaultEdge> completeGraph(int n) {
		Supplier<Integer> vSupplier = new Supplier<Integer>() {
			private int id = 0;

			@Override
			public Integer get() {
				return id++;
			}
		};
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(),
				false);
		CompleteGraphGenerator<Integer, DefaultEdge> completeGen = new CompleteGraphGenerator<>(n);
		completeGen.generateGraph(graph);
		return graph;
	}

	/**
	 * @return A biparti complete graph Kn,m
	 */
	public static SimpleGraph<Integer, DefaultEdge> bipartiCompleteGraph(int n, int m) {
		Supplier<Integer> vSupplier = new Supplier<Integer>() {
			private int id = 0;

			@Override
			public Integer get() {
				return id++;
			}
		};
		SimpleGraph<Integer, DefaultEdge> bipartiGraph = new SimpleGraph<>(vSupplier,
				SupplierUtil.createDefaultEdgeSupplier(), false);
		CompleteBipartiteGraphGenerator<Integer, DefaultEdge> bipartiGen = new CompleteBipartiteGraphGenerator<>(n, m);
		bipartiGen.generateGraph(bipartiGraph);
		return bipartiGraph;
	}

	/**
	 * Creates a connected undirected graph with N vertices consisting of N/4
	 * connected square cycles
	 * 
	 * @return connected undirected graph
	 */
	public static SimpleGraph<Integer, DefaultEdge> connectedSquaresGraph(int n) {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (int i = 0; i < n; i += 4) {
			// Add 4 vertices
			g.addVertex(i);
			g.addVertex(i + 1);
			g.addVertex(i + 2);
			g.addVertex(i + 3);

			// Connect them in a loop
			g.addEdge(i, i + 1);
			g.addEdge(i + 1, i + 2);
			g.addEdge(i + 2, i + 3);
			g.addEdge(i + 3, i);

			// Connect previous bloc to current bloc
			if (i > 0)
				g.addEdge(i - 3, i);
		}
		return g;
	}

	/**
	 * Creates a disconnected undirected graph with N vertices consisting of N/4
	 * disconnected square cycles
	 * 
	 * @return disconnected undirected graph
	 */
	public static SimpleGraph<Integer, DefaultEdge> disconnectedSquaresGraph(int n) {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

		// Add vertices
		for (int i = 0; i < n; i += 4) {
			// Add 4 vertices
			g.addVertex(i);
			g.addVertex(i + 1);
			g.addVertex(i + 2);
			g.addVertex(i + 3);

			// Connect them in a loop
			g.addEdge(i, i + 1);
			g.addEdge(i + 1, i + 2);
			g.addEdge(i + 2, i + 3);
			g.addEdge(i + 3, i);

		}
		return g;
	}

	/**
	 * @return a tree with 100 vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> treeWith100Vertices() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		// Add vertices
		for (int n = 1; n <= 100; n++) {
			g.addVertex(n);
		}

		// Add edges
		g.addEdge(5, 16);
		g.addEdge(6, 23);
		g.addEdge(10, 78);
		g.addEdge(14, 62);
		g.addEdge(15, 17);
		g.addEdge(17, 53);
		g.addEdge(19, 97);
		g.addEdge(20, 43);
		g.addEdge(21, 37);
		g.addEdge(24, 42);
		g.addEdge(30, 2);
		g.addEdge(2, 95);
		g.addEdge(31, 90);
		g.addEdge(35, 22);
		g.addEdge(38, 26);
		g.addEdge(39, 7);
		g.addEdge(7, 4);
		g.addEdge(40, 11);
		g.addEdge(41, 29);
		g.addEdge(29, 97);
		g.addEdge(45, 47);
		g.addEdge(46, 51);
		g.addEdge(47, 82);
		g.addEdge(50, 84);
		g.addEdge(52, 66);
		g.addEdge(53, 68);
		g.addEdge(55, 54);
		g.addEdge(57, 85);
		g.addEdge(61, 27);
		g.addEdge(62, 60);
		g.addEdge(60, 87);
		g.addEdge(63, 90);
		g.addEdge(65, 33);
		g.addEdge(66, 11);
		g.addEdge(11, 44);
		g.addEdge(69, 56);
		g.addEdge(70, 26);
		g.addEdge(26, 72);
		g.addEdge(71, 98);
		g.addEdge(72, 22);
		g.addEdge(22, 100);
		g.addEdge(79, 85);
		g.addEdge(80, 75);
		g.addEdge(82, 49);
		g.addEdge(49, 9);
		g.addEdge(83, 87);
		g.addEdge(84, 42);
		g.addEdge(42, 51);
		g.addEdge(86, 73);
		g.addEdge(73, 12);
		g.addEdge(12, 27);
		g.addEdge(27, 89);
		g.addEdge(87, 64);
		g.addEdge(64, 78);
		g.addEdge(78, 88);
		g.addEdge(88, 18);
		g.addEdge(18, 81);
		g.addEdge(90, 94);
		g.addEdge(91, 16);
		g.addEdge(92, 54);
		g.addEdge(93, 44);
		g.addEdge(44, 89);
		g.addEdge(89, 97);
		g.addEdge(94, 75);
		g.addEdge(75, 23);
		g.addEdge(23, 48);
		g.addEdge(48, 81);
		g.addEdge(81, 67);
		g.addEdge(67, 3);
		g.addEdge(3, 74);
		g.addEdge(74, 34);
		g.addEdge(95, 9);
		g.addEdge(9, 96);
		g.addEdge(96, 54);
		g.addEdge(54, 68);
		g.addEdge(68, 25);
		g.addEdge(25, 33);
		g.addEdge(97, 13);
		g.addEdge(13, 33);
		g.addEdge(33, 34);
		g.addEdge(98, 85);
		g.addEdge(85, 28);
		g.addEdge(28, 59);
		g.addEdge(59, 4);
		g.addEdge(4, 58);
		g.addEdge(58, 76);
		g.addEdge(76, 37);
		g.addEdge(37, 51);
		g.addEdge(51, 16);
		g.addEdge(99, 56);
		g.addEdge(56, 43);
		g.addEdge(43, 8);
		g.addEdge(8, 36);
		g.addEdge(36, 32);
		g.addEdge(32, 77);
		g.addEdge(77, 1);
		g.addEdge(1, 16);
		g.addEdge(16, 34);
		g.addEdge(34, 100);
		return g;
	}

	/**
	 * @return a forest with 3 trees
	 */
	public static SimpleGraph<Integer, DefaultEdge> forestWith3Trees() {
		SimpleGraph<Integer, DefaultEdge> g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		// Add vertices
		for (int n = 1; n <= 6; n++) {
			g.addVertex(n);
		}

		// Add edges
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(3, 6);

		// Add vertices
		for (int n = 11; n <= 16; n++) {
			g.addVertex(n);
		}

		// Add edges
		g.addEdge(11, 12);
		g.addEdge(12, 13);
		g.addEdge(13, 14);
		g.addEdge(13, 15);
		g.addEdge(13, 16);

		// Add vertices
		for (int n = 21; n <= 26; n++) {
			g.addVertex(n);
		}

		// Add edges
		g.addEdge(21, 22);
		g.addEdge(22, 23);
		g.addEdge(23, 24);
		g.addEdge(23, 25);
		g.addEdge(23, 26);

		return g;
	}

	/**
	 * @return a random connected graph with N vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> randomConnectedGraph(int n) {
		Supplier<Integer> vSupplier = new Supplier<Integer>() {
			private int id = 0;

			@Override
			public Integer get() {
				return id++;
			}
		};
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(),
				false);
		PlantedPartitionGraphGenerator<Integer, DefaultEdge> completeGen = new PlantedPartitionGraphGenerator<>(1, n, 0.5, 1.0);
		completeGen.generateGraph(graph);
		return graph;
	}

	/**
	 * @return a random disconnected graph with P partitions of N vertices
	 */
	public static SimpleGraph<Integer, DefaultEdge> randomDisconnectedGraph(int p, int n) {
		Supplier<Integer> vSupplier = new Supplier<Integer>() {
			private int id = 0;

			@Override
			public Integer get() {
				return id++;
			}
		};
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(vSupplier, SupplierUtil.createDefaultEdgeSupplier(),
				false);
		PlantedPartitionGraphGenerator<Integer, DefaultEdge> completeGen = new PlantedPartitionGraphGenerator<>(p, n, 0.5, 0.0);
		completeGen.generateGraph(graph);
		return graph;
	}


}
