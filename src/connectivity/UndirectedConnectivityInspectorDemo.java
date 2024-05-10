package connectivity;

import java.util.List;
import java.util.Set;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import graph_samples.*;

/**
 * UndirectedConnectivityInspector examples.
 * 
 * @author François Schumacker
 *
 */
public class UndirectedConnectivityInspectorDemo {

	public static void main(String[] args) {
		smallConnectedGraph();
		smallDisconnectedGraph();
		bigConnectedGraph();
		bigDisconnectedGraph();
	}

	private static void smallConnectedGraph() {
		System.out.println(">>> Connected undirected graph");
		SimpleGraph<Integer, DefaultEdge> connectedGraph = SimpleUndirectedGraphsInteger.connectedUndirectedGraph();
		System.out.println(connectedGraph.toString());

		ConnectivityInspector<Integer, DefaultEdge> ci = new ConnectivityInspector<>(connectedGraph);
		List<Set<Integer>> cs = ci.connectedSets();
		System.out.println("[JGraphT] isConnected ? " + ci.isConnected());
		System.out.println("[JGraphT] Connected sets => " + cs.size() + " CS -> " + cs.toString());

		UndirectedConnectivityInspector<Integer, DefaultEdge> uci = new UndirectedConnectivityInspector<>(
				connectedGraph);
		cs = uci.connectedSets();
		System.out.println("[Custom] isConnected ? " + uci.isConnected());
		System.out.println("[Custom] Connected sets => " + cs.size() + " CS -> " + cs.toString());
		System.out.println();
	}

	private static void smallDisconnectedGraph() {
		System.out.println(">>> Disconnected undirected graph");
		SimpleGraph<Integer, DefaultEdge> disconnectedGraph = SimpleUndirectedGraphsInteger
				.disconnectedUndirectedGraph();
		System.out.println(disconnectedGraph.toString());

		ConnectivityInspector<Integer, DefaultEdge> ci = new ConnectivityInspector<>(disconnectedGraph);
		List<Set<Integer>> cs = ci.connectedSets();
		System.out.println("[JGraphT] isConnected ? " + ci.isConnected());
		System.out.println("[JGraphT] Connected sets => " + cs.size() + " CS -> " + cs.toString());

		UndirectedConnectivityInspector<Integer, DefaultEdge> uci = new UndirectedConnectivityInspector<>(
				disconnectedGraph);
		cs = uci.connectedSets();
		System.out.println("[Custom] isConnected ? " + uci.isConnected());
		System.out.println("[Custom] Connected sets => " + cs.size() + " CS -> " + cs.toString());
		System.out.println();

	}

	private static void bigConnectedGraph() {
		System.out.println(">>> PERFORMANCE TEST : connected undirected graph");
		SimpleGraph<Integer, DefaultEdge> connectedGraph = SimpleUndirectedGraphsInteger.connectedSquaresGraph(2000000);
		System.out.println("DEBUG : graph created");

		Runtime.getRuntime().gc();
		long time = System.currentTimeMillis();
		ConnectivityInspector<Integer, DefaultEdge> ci = new ConnectivityInspector<>(connectedGraph);
		List<Set<Integer>> cs = ci.connectedSets();
		double jgraphtDuration = (System.currentTimeMillis() - time) / 1000.0;
		System.out.println("[JGraphT] isConnected ? " + ci.isConnected());
		System.out.println("[JGraphT] Connected sets => " + cs.size() + " CS");
		System.out.println("[JGraphT] Duration => " + jgraphtDuration);

		Runtime.getRuntime().gc();
		time = System.currentTimeMillis();
		UndirectedConnectivityInspector<Integer, DefaultEdge> uci = new UndirectedConnectivityInspector<>(
				connectedGraph);
		cs = uci.connectedSets();
		double customDuration = (System.currentTimeMillis() - time) / 1000.0;
		System.out.println("[Custom] isConnected ? " + uci.isConnected());
		System.out.println("[Custom] Connected sets => " + cs.size() + " CS");
		System.out.println("[Custom] Duration => " + customDuration);
		System.out.println();

		double cttRatio = customDuration / jgraphtDuration;
		if (cttRatio >= 1.0) {
			System.out.printf("=> %.2f times slower\n", cttRatio);
		} else {
			System.out.printf("=> %.2f times faster\n", 1.0 / cttRatio);
		}
		System.out.println();
	}

	private static void bigDisconnectedGraph() {
		System.out.println(">>> PERFORMANCE TEST : disconnected undirected graph");
		SimpleGraph<Integer, DefaultEdge> connectedGraph = SimpleUndirectedGraphsInteger
				.disconnectedSquaresGraph(2000000);
		System.out.println("DEBUG : graph created");

		Runtime.getRuntime().gc();
		long time = System.currentTimeMillis();
		ConnectivityInspector<Integer, DefaultEdge> ci = new ConnectivityInspector<>(connectedGraph);
		List<Set<Integer>> cs = ci.connectedSets();
		double jgraphtDuration = (System.currentTimeMillis() - time) / 1000.0;
		System.out.println("[JGraphT] isConnected ? " + ci.isConnected());
		System.out.println("[JGraphT] Connected sets => " + cs.size() + " CS");
		System.out.println("[JGraphT] Duration => " + jgraphtDuration);
		ci = null;

		Runtime.getRuntime().gc();
		time = System.currentTimeMillis();
		UndirectedConnectivityInspector<Integer, DefaultEdge> uci = new UndirectedConnectivityInspector<>(
				connectedGraph);
		cs = uci.connectedSets();
		double customDuration = (System.currentTimeMillis() - time) / 1000.0;
		System.out.println("[Custom] isConnected ? " + uci.isConnected());
		System.out.println("[Custom] Connected sets => " + cs.size() + " CS");
		System.out.println("[Custom] Duration => " + customDuration);
		System.out.println();

		double cttRatio = customDuration / jgraphtDuration;
		if (cttRatio >= 1.0) {
			System.out.printf("=> %.2f times slower\n", cttRatio);
		} else {
			System.out.printf("=> %.2f times faster\n", 1.0 / cttRatio);
		}
		System.out.println();
	}

}
