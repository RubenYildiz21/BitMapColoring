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
	private BufferedImage image;
	private Color borderColor;
	private SimpleGraph<Integer, DefaultEdge> adjacencyGraph;
	private Map<Integer, Integer> regionMap;
	private Coloring<Integer> coloring;

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
		if (image == null || borderColor == null) {
			throw new NullPointerException("Image and borderColor cannot be null");
		}
		this.image = image;
		this.borderColor = borderColor;
	}

	/**
	 * Color the map areas with the least possible colors so that two adjacent areas
	 * have different colors.
	 */
	public void colorMap() {
		// Construction du graphe d'adjacence
		this.adjacencyGraph = buildAdjacencyGraph();

		// Inspecter les composantes connexes
		UndirectedConnectivityInspector<Integer, DefaultEdge> inspector = new UndirectedConnectivityInspector<>(
				adjacencyGraph);
		List<Set<Integer>> connectedComponents = inspector.connectedSets();

		// Créer un mappage entre chaque nœud et sa région
		regionMap = new HashMap<>();
		int index = 0;
		for (Set<Integer> component : connectedComponents) {
			for (Integer node : component) {
				regionMap.put(node, index);
			}
			index++;
		}

		// Imprimer les régions associées
		for (Map.Entry<Integer, Integer> entry : regionMap.entrySet()) {
			System.out.printf("Nœud %d appartient à la région %d%n", entry.getKey(), entry.getValue());
		}

		// Construire le graphe d'adjacence des régions
		SimpleGraph<Integer, DefaultEdge> regionGraph = new SimpleGraph<>(DefaultEdge.class);
		for (int i = 0; i < index; i++) {
			regionGraph.addVertex(i);
		}

		// Ajouter des arêtes entre les régions adjacentes
		for (DefaultEdge edge : adjacencyGraph.edgeSet()) {
			Integer source = adjacencyGraph.getEdgeSource(edge);
			Integer target = adjacencyGraph.getEdgeTarget(edge);
			int region1 = regionMap.get(source);
			int region2 = regionMap.get(target);

			if (region1 != region2) {
				regionGraph.addEdge(region1, region2);
				System.out.printf("Arête ajoutée entre les régions %d et %d%n", region1, region2);
			}
		}

		// Appliquer l'algorithme de coloration DSatur
		DSaturColoring<Integer, DefaultEdge> dsatur = new DSaturColoring<>(regionGraph);
		var coloring = dsatur.getColoring();

		System.out.printf("Nombre total de couleurs utilisées: %d%n", coloring.getNumberColors());

		// Colorier les régions
		applyColorsToMap(coloring.getColors());
	}

	/**
	 * Return the adjacency graph of the image's areas.
	 * 
	 * @return the adjacency graph of the image's areas
	 */
	public SimpleGraph<Integer, DefaultEdge> adjacencyGraph() {
		if (adjacencyGraph == null) {
			colorMap();
		}
		return adjacencyGraph;
	}

	/**
	 * Return the coloring of the adjacency graph.
	 * 
	 * @return the coloring of the adjacency graph
	 */
	public Coloring<Integer> getColoring() {
		if (coloring == null) {
			colorMap();
		}
		return coloring;
	}

	/**
	 * Builds the graph representing the adjacency relationships between different
	 * regions in the image.
	 * 
	 * @return the constructed graph.
	 */
	private SimpleGraph<Integer, DefaultEdge> buildAdjacencyGraph() {
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        int width = image.getWidth();
        int height = image.getHeight();
        Map<Integer, Integer> pixelToNodeMap = new HashMap<>();
        int nodeCounter = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isNotBorder(x, y)) {
                    int nodeId = nodeCounter++;
                    pixelToNodeMap.put(y * width + x, nodeId);
                    graph.addVertex(nodeId);
                }
            }
        }

        // Ajout des arêtes entre les nœuds adjacents
        for (Map.Entry<Integer, Integer> entry : pixelToNodeMap.entrySet()) {
            int position = entry.getKey();
            int nodeId = entry.getValue();
            int x = position % width;
            int y = position / width;

            addEdgeIfValid(graph, nodeId, x + 1, y, pixelToNodeMap, width);
            addEdgeIfValid(graph, nodeId, x - 1, y, pixelToNodeMap, width);
            addEdgeIfValid(graph, nodeId, x, y + 1, pixelToNodeMap, width);
            addEdgeIfValid(graph, nodeId, x, y - 1, pixelToNodeMap, width);
        }

        return graph;
    }

	/**
	 * Apply colors to the regions of the map based on the calculated coloring.
	 */
	private void applyColorsToMap(Map<Integer, Integer> regionColors) {
        int width = image.getWidth();
        System.out.println("Coloration des régions:");
        for (var entry : regionColors.entrySet()) {
            int region = entry.getKey();
            int colorIndex = entry.getValue();
            int color = rgbColors[colorIndex % rgbColors.length];
            System.out.printf("Région %d colorée avec la couleur %d%n", region, colorIndex);
            for (Map.Entry<Integer, Integer> pixelEntry : regionMap.entrySet()) {
                if (pixelEntry.getValue() == region) {
                    int position = pixelEntry.getKey();
                    int x = position % width;
                    int y = position / width;
                    image.setRGB(x, y, color);
                }
            }
        }
    }

	/**
	 * Determines if the pixel at (x, y) is not part of a border.
	 * 
	 * @param x the x-coordinate of the pixel.
	 * @param y the y-coordinate of the pixel.
	 * @return true if the pixel is not part of a border.
	 */
	private boolean isNotBorder(int x, int y) {
		int pixelColor = image.getRGB(x, y);
		boolean result = !isColorWithinTolerance(pixelColor, borderColor.getRGB(), 30);
		System.out.printf("Pixel [%d, %d] - Border: %b%n", x, y, result);
		return result;
	}

	private boolean isColorWithinTolerance(int color1, int color2, int tolerance) {
		int r1 = (color1 >> 16) & 0xFF;
		int g1 = (color1 >> 8) & 0xFF;
		int b1 = color1 & 0xFF;

		int r2 = (color2 >> 16) & 0xFF;
		int g2 = (color2 >> 8) & 0xFF;
		int b2 = color2 & 0xFF;

		return Math.abs(r1 - r2) <= tolerance && Math.abs(g1 - g2) <= tolerance && Math.abs(b1 - b2) <= tolerance;
	}

	/**
	 * Adds an edge between the source node and the pixel at (x, y) if it's not a
	 * border.
	 * 
	 * @param graph          the graph to which the edge should be added.
	 * @param sourceNode     the source node.
	 * @param x              the x-coordinate of the neighboring pixel.
	 * @param y              the y-coordinate of the neighboring pixel.
	 * @param pixelToNodeMap the map linking pixels to graph nodes.
	 * @param width          the width of the image.
	 */
	private void addEdgeIfValid(SimpleGraph<Integer, DefaultEdge> graph, int sourceNode, int x, int y,
			Map<Integer, Integer> pixelToNodeMap, int width) {
		if (x >= 0 && x < width && y >= 0 && y < image.getHeight()) {
			int position = y * width + x;
			if (pixelToNodeMap.containsKey(position)) {
				int targetNode = pixelToNodeMap.get(position);
				System.out.printf("Tentative d'ajout d'une arête entre %d et %d%n", sourceNode, targetNode);
				graph.addEdge(sourceNode, targetNode);
			}
		}
	}

}
