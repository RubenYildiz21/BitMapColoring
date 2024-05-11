package color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.logging.Logger;

import org.jgrapht.alg.color.SaturationDegreeColoring;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
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
	private static final Logger logger = Logger.getLogger(MapColoring.class.getName());
	@SuppressWarnings("unused")
	private static final int[] rgbColors = { Color.BLUE.getRGB(), Color.RED.getRGB(), Color.GREEN.getRGB(),
			Color.YELLOW.getRGB(), Color.CYAN.getRGB(), Color.PINK.getRGB(), Color.ORANGE.getRGB(),
			Color.MAGENTA.getRGB() };
	private BufferedImage image;
	private Color borderColor;
	private SimpleGraph<Integer, DefaultEdge> pixelGraph;
	private SimpleGraph<Integer, DefaultEdge> zoneGraph;
	private Map<Integer, Integer> zoneColorMap;
	private Map<Integer, Set<Integer>> componentMap;

	public SimpleGraph<Integer, DefaultEdge> getAdjacencyGraph() {
		return zoneGraph;
	}

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
		this.pixelGraph = new SimpleGraph<>(DefaultEdge.class);
		this.zoneGraph = new SimpleGraph<>(DefaultEdge.class);
		this.componentMap = new HashMap<>();  // Initialisation de componentMap
		createGraphFromImage();
		identifyComponents();
		createZoneGraph();
		applyColoring();
		applyColorsToImage();
	}


	/**
	 * Color the map areas with the least possible colors in such a way that
	 * two adjacent areas have different colors.
	 */
	public void colorMap() {
		if (zoneGraph == null || zoneGraph.vertexSet().isEmpty()) {
			logger.info("No vertices to color in the graph.");
			return;
		}
		DSaturColoring<Integer, DefaultEdge> coloringAlg = new DSaturColoring<>(zoneGraph);
		Coloring<Integer> coloring = coloringAlg.getColoring();
		Map<Integer, Integer> colorMap = coloring.getColors();
		logger.info("Coloring result: " + colorMap);
	}

	/**
	 * Return the adjacency graph of the image's areas.
	 * @return the adjacency graph of the image's areas
	 */
	public SimpleGraph<Integer, DefaultEdge> adjacencyGraph() {
		return pixelGraph;
	}

	/**
	 * Return the coloring the adjacency graph.
	 * @return the coloring of the adjacency graph
	 */
	public Coloring<Integer> getColoring() {
		return new VertexColoringAlgorithm.ColoringImpl<>(zoneColorMap, zoneColorMap.size());
	}

	public void createGraphFromImage() {
		int width = image.getWidth();
		int height = image.getHeight();
		Map<Integer, Integer> pixelToVertexId = new HashMap<>();

		// Parcourir chaque pixel pour créer des sommets
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				if (rgb != borderColor.getRGB()) {  // Si ce n'est pas une bordure
					int vertexId = y * width + x;
					pixelGraph.addVertex(vertexId);
					pixelToVertexId.put(vertexId, vertexId);

					// Connecter avec le pixel à gauche s'il n'est pas une bordure
					if (x > 0 && image.getRGB(x - 1, y) != borderColor.getRGB()) {
						pixelGraph.addEdge(vertexId, pixelToVertexId.get(y * width + (x - 1)));
					}
					// Connecter avec le pixel au-dessus s'il n'est pas une bordure
					if (y > 0 && image.getRGB(x, y - 1) != borderColor.getRGB()) {
						pixelGraph.addEdge(vertexId, pixelToVertexId.get((y - 1) * width + x));
					}
				}
			}
		}
	}


	private void identifyComponents() {
		ConnectivityInspector<Integer, DefaultEdge> ci = new ConnectivityInspector<>(pixelGraph);
		List<Set<Integer>> connectedComponents = ci.connectedSets();
		int zoneId = 0;
		componentMap.clear();
		for (Set<Integer> component : connectedComponents) {
			componentMap.put(zoneId, new HashSet<>(component));
			zoneGraph.addVertex(zoneId);
			//logger.info("Zone " + zoneId + " has " + component.size() + " pixels");  // Log the size of each zone
			zoneId++;
		}
		logger.info("Total zones identified: " + zoneId);  // Log the total number of identified zones
	}

	private void createZoneGraph() {
		for (Integer zone1 : componentMap.keySet()) {
			for (Integer zone2 : componentMap.keySet()) {
				if (!zone1.equals(zone2) && areComponentsAdjacent(componentMap.get(zone1), componentMap.get(zone2))) {
					if (zoneGraph.containsVertex(zone1) && zoneGraph.containsVertex(zone2)) {
						zoneGraph.addEdge(zone1, zone2);
						logger.info("Edge successfully added between " + zone1 + " and " + zone2);
					} else {
						logger.warning("Attempted to add edge between non-existent vertices: " + zone1 + " and " + zone2);
					}
				}
			}
		}
	}



	private boolean areComponentsAdjacent(Set<Integer> pixels1, Set<Integer> pixels2) {
		int width = image.getWidth();
		int height = image.getHeight();
		int blackRGB = Color.BLACK.getRGB();
		// Définir des variables pour stocker les extrémités de chaque composant
		int x1Min = width, x1Max = 0, y1Min = height, y1Max = 0;
		int x2Min = width, x2Max = 0, y2Min = height, y2Max = 0;

		// Calculer les limites de chaque composant
		for (Integer p1 : pixels1) {
			int x1 = p1 % width;
			int y1 = p1 / width;
			if (x1 < x1Min) x1Min = x1;
			if (x1 > x1Max) x1Max = x1;
			if (y1 < y1Min) y1Min = y1;
			if (y1 > y1Max) y1Max = y1;
		}

		for (Integer p2 : pixels2) {
			int x2 = p2 % width;
			int y2 = p2 / width;
			if (x2 < x2Min) x2Min = x2;
			if (x2 > x2Max) x2Max = x2;
			if (y2 < y2Min) y2Min = y2;
			if (y2 > y2Max) y2Max = y2;
		}
		// Vérifier l'alignement horizontal ou vertical et la proximité des limites
		return ((y1Min == y2Min && y1Max == y2Max && (Math.abs(x1Max - x2Min) <= 4 || Math.abs(x2Max - x1Min) <= 4))
				|| (x1Min == x2Min && x1Max == x2Max && (Math.abs(y1Max - y2Min) <= 4 || Math.abs(y2Max - y1Min) <= 4)))
				&& image.getRGB(x1Max, y1Min) != blackRGB && image.getRGB(x2Min, y2Min) != blackRGB;
	}


	private void applyColoring() {
		if (zoneGraph.vertexSet().isEmpty()) {
			logger.warning("Zone graph is empty, no coloring possible.");
			zoneColorMap = new HashMap<>(); // Initialize map to prevent NullPointerException
			return;
		}

		DSaturColoring<Integer, DefaultEdge> coloringAlg = new DSaturColoring<>(zoneGraph);
		Coloring<Integer> coloring = coloringAlg.getColoring();
		zoneColorMap = coloring.getColors();

		if (zoneColorMap == null || zoneColorMap.isEmpty()) {
			logger.warning("No colors were assigned to any zone. Initializing empty map.");
			zoneColorMap = new HashMap<>();
			// Potentially fill with default values if necessary
			for (Integer zone : zoneGraph.vertexSet()) {
				zoneColorMap.put(zone, 0); // Assign a default color if none assigned
			}
		}  else {
			logger.info("Colors assigned to zones: " + zoneColorMap);
		}
	}



	public void applyColorsToImage() {
		int width = image.getWidth();
		int height = image.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int vertexId = y * width + x;
				Integer zoneId = findZoneId(vertexId);
				if (zoneId != null && zoneColorMap.containsKey(zoneId)) {
					int colorIndex = zoneColorMap.get(zoneId);
					Color color = new Color(rgbColors[colorIndex % rgbColors.length]);
					image.setRGB(x, y, color.getRGB());
				}
			}
		}
	}


	private Integer findZoneId(int pixelId) {
		for (Map.Entry<Integer, Set<Integer>> entry : componentMap.entrySet()) {
			if (entry.getValue().contains(pixelId)) {
				//logger.info("Pixel " + pixelId + " found in zone " + entry.getKey()); // Debugging info
				return entry.getKey();
			}
		}
		return null;
	}


}
