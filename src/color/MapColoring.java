package color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * Coloring of a Map image.
 *
 * @author François Schumacker
 *
 */
public class MapColoring {

	@SuppressWarnings("unused")
	private static final int[] rgbColors = { Color.BLUE.getRGB(), Color.RED.getRGB(), Color.GREEN.getRGB(),
			Color.YELLOW.getRGB(), Color.CYAN.getRGB(), Color.PINK.getRGB(), Color.ORANGE.getRGB(),
			Color.MAGENTA.getRGB() };
	private BufferedImage image;
	private Color borderColor;
	private SimpleGraph<Integer, DefaultEdge> graph;
	private List<Set<Integer>> connectedComponents;

	public SimpleGraph<Integer, DefaultEdge> getAdjacencyGraph() {
		return adjacencyGraph;
	}

	private SimpleGraph<Integer, DefaultEdge> adjacencyGraph;
	private Map<Integer, Set<Integer>> componentMap;

	/**
	 * Constructor
	 *
	 * @param image	a bitmap image representing a map to color
	 * @param borderColor the color of the lines delimiting the map areas
	 * @throws NullPointerException if any input parameter is null
	 */
	public MapColoring(BufferedImage image, Color borderColor) {
		if (image == null || borderColor == null) {
			throw new NullPointerException("Image and borderColor cannot be null");
		}
		this.image = image;
		this.borderColor = borderColor;
		this.graph = new SimpleGraph<>(DefaultEdge.class);
		createGraphFromImage();
		identifyComponents();
		createAdjacencyGraph();
	}

	/**
	 * Color the map areas with the least possible colors in such a way that
	 * two adjacent areas have different colors.
	 */
	public void colorMap() {
		// TODO
	}

	/**
	 * Return the adjacency graph of the image's areas.
	 * @return the adjacency graph of the image's areas
	 */
	public SimpleGraph<Integer, DefaultEdge> adjacencyGraph() {
		return graph;
	}

	/**
	 * Return the coloring the adjacency graph.
	 * @return the coloring of the adjacency graph
	 */
	public Coloring<Integer> getColoring() {
		return null; // TODO
	}

	private void createGraphFromImage() {
		int width = image.getWidth();
		int height = image.getHeight();
		int vertexId = 0; // We will use this to assign unique IDs to vertices.
		Map<Integer, Integer> pixelToVertexId = new HashMap<>(); // Maps pixel positions to vertex IDs.

		// Scan each pixel and create vertices if they are not of the border color.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int color = image.getRGB(x, y);
				if (color != borderColor.getRGB()) {
					int currentId = vertexId++;
					pixelToVertexId.put(y * width + x, currentId);
					graph.addVertex(currentId);

					// Connect this vertex with the vertex on the left if it exists and is within the image boundary.
					if (x > 0 && pixelToVertexId.containsKey(y * width + (x - 1))) {
						graph.addEdge(currentId, pixelToVertexId.get(y * width + (x - 1)));
					}

					// Connect this vertex with the vertex above if it exists and is within the image boundary.
					if (y > 0 && pixelToVertexId.containsKey((y - 1) * width + x)) {
						graph.addEdge(currentId, pixelToVertexId.get((y - 1) * width + x));
					}
				}
			}
		}
	}

	private void identifyComponents() {
		ConnectivityInspector<Integer, DefaultEdge> ci = new ConnectivityInspector<>(graph);
		this.connectedComponents = ci.connectedSets();
	}

	public List<Set<Integer>> getConnectedComponents() {
		return connectedComponents;
	}

	private void createAdjacencyGraph() {
		adjacencyGraph = new SimpleGraph<>(DefaultEdge.class);
		componentMap = new HashMap<>();
		int componentId = 0;

		// Assign each pixel to a component
		for (Set<Integer> component : connectedComponents) {
			adjacencyGraph.addVertex(componentId);
			componentMap.put(componentId, component);
			componentId++;
		}

		// Check adjacency between each pair of components
		for (Integer comp1 : componentMap.keySet()) {
			Set<Integer> pixels1 = componentMap.get(comp1);
			for (Integer comp2 : componentMap.keySet()) {
				if (!comp1.equals(comp2)) {
					if (areComponentsAdjacent(pixels1, componentMap.get(comp2))) {
						adjacencyGraph.addEdge(comp1, comp2);
					}
				}
			}
		}
	}

	private boolean areComponentsAdjacent(Set<Integer> pixels1, Set<Integer> pixels2) {
		int width = image.getWidth();
		for (Integer pixel1 : pixels1) {
			int x1 = pixel1 % width;
			int y1 = pixel1 / width;

			// Check neighbors
			int[] dx = {-1, 1, 0, 0};  // Left, Right, Up, Down
			int[] dy = {0, 0, -1, 1};

			for (int i = 0; i < 4; i++) {
				int nx = x1 + dx[i];
				int ny = y1 + dy[i];
				int neighborIndex = ny * width + nx;
				if (nx >= 0 && nx < width && ny >= 0 && ny < image.getHeight()) {
					if (pixels2.contains(neighborIndex)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
