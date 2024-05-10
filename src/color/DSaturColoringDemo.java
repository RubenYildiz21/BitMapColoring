package color;

import java.util.Objects;
import org.jgrapht.Graph;
import org.jgrapht.alg.color.SaturationDegreeColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;

import graph_samples.*;

/**
 * DSaturColoring examples.
 * 
 * @author François Schumacker
 *
 */
public class DSaturColoringDemo<V, E> {

	protected final Graph<V, E> graph;

	public DSaturColoringDemo(Graph<V, E> graph) {
		this.graph = Objects.requireNonNull(graph, "Graph cannot be null");
	}

	public void run() {
		Runtime.getRuntime().gc();
		long time = System.currentTimeMillis();
		Coloring<V> coloring = new DSaturColoring<>(graph).getColoring();
		double customDuration = (System.currentTimeMillis() - time) / 1000.0;
		System.out.printf("[Custom] colors : %d\n", coloring.getNumberColors());
		if (customDuration > 0.05)
			System.out.printf("[Custom] duration : %.2f seconds\n", customDuration);

		Runtime.getRuntime().gc();
		time = System.currentTimeMillis();
		coloring = new SaturationDegreeColoring<>(graph).getColoring();
		double jgraphtDuration = (System.currentTimeMillis() - time) / 1000.0;
		System.out.printf("[JGraphT] colors : %d\n", coloring.getNumberColors());
		if (customDuration > 0.05)
			System.out.printf("[JGraphT] duration : %.2f seconds\n", jgraphtDuration);

		if (customDuration > 0.05 && jgraphtDuration > 0.05) {
			double cttRatio = customDuration / jgraphtDuration;
			if (cttRatio >= 1.0) {
				System.out.printf("=> %.2f times slower\n", cttRatio);
			} else {
				System.out.printf("=> %.2f times faster\n", 1.0 / cttRatio);
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		communesAjoie();
		horaireExamens();
		grapheComplet();
		grapheBipartiComplet();
	}

	/*
	 * Communes d'Ajoie [MULLER EX 51]
	 */
	private static void communesAjoie() {
		System.out.println(">>> Communes d'Ajoie [MULLER EX 51]");
		new DSaturColoringDemo<>(ColoringExamples.mullerEx51()).run();
	}

	/*
	 * Horaire d'examens [MULLER EX 45]
	 */
	private static void horaireExamens() {
		System.out.println(">>> Horaire d'examens [MULLER EX 45]");
		new DSaturColoringDemo<>(ColoringExamples.mullerEx45()).run();
	}

	/*
	 * Graphe complet Kn
	 */
	private static void grapheComplet() {
		System.out.println(">>> Graphe complet Kn");
		new DSaturColoringDemo<>(SimpleUndirectedGraphsInteger.completeGraph(2000)).run();
	}

	/*
	 * Graphe biparti complet Kn,m
	 */
	private static void grapheBipartiComplet() {
		System.out.println(">>> Graphe biparti complet NxM");
		new DSaturColoringDemo<>(SimpleUndirectedGraphsInteger.bipartiCompleteGraph(1000, 500)).run();
	}

}
