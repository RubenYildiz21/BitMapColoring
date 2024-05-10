package color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import connectivity.UndirectedConnectivityInspector;

/**
 * Coloring of a Map image.
 * 
 * @author François Schumacker
 *
 */
public class MapColoring {

	// Tableau de couleurs prédéfini
	@SuppressWarnings("unused")
	private static final int[] rgbColors = { Color.BLUE.getRGB(), Color.RED.getRGB(), Color.GREEN.getRGB(),
			Color.YELLOW.getRGB(), Color.CYAN.getRGB(), Color.PINK.getRGB(), Color.ORANGE.getRGB(),
			Color.MAGENTA.getRGB() };

	/**
	 * Constructor
	 * 
	 * @param image       a bitmap image representing a map to color
	 * @param borderColor the color of the lines delimiting the map areas
	 * @throws NullPointerException if any input parameter is null
	 */
	public MapColoring(BufferedImage image, Color borderColor) {
		//todo
	}

	/**
	 * Color the map areas with the least possible colors so that two adjacent areas
	 * have different colors.
	 */
	public void colorMap() {
		//todo
	}

	/**
	 * Return the adjacency graph of the image's areas.
	 * 
	 * @return the adjacency graph of the image's areas
	 */
	public SimpleGraph<Integer, DefaultEdge> adjacencyGraph() {
		return null; //todo
	}

	/**
	 * Return the coloring of the adjacency graph.
	 * 
	 * @return the coloring of the adjacency graph
	 */
	public Coloring<Integer> getColoring() {
		return null; //todo
	}

}
