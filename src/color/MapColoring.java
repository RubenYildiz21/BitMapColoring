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
		this.componentMap = new HashMap<>();
		createGraphFromImage();
		identifyComponents();
		createZoneGraph();
		applyColorsToImage();
	}


	/**
	 * Color the map areas with the least possible colors in such a way that
	 * two adjacent areas have different colors.
	 */
	public void colorMap() {
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
		if (zoneColorMap.isEmpty()) {
			logger.warning("No coloring available. Zone graph might be empty or improperly connected.");
			return new VertexColoringAlgorithm.ColoringImpl<>(new HashMap<>(), 0);
		}
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
				if (!zone1.equals(zone2) && areComponentsAdjacent(componentMap.get(zone1), componentMap.get(zone2), 4)) {
					if (zoneGraph.containsVertex(zone1) && zoneGraph.containsVertex(zone2)) {
						zoneGraph.addEdge(zone1, zone2);
						//logger.info("Edge successfully added between " + zone1 + " and " + zone2);
					} else {
						logger.warning("Attempted to add edge between non-existent vertices: " + zone1 + " and " + zone2);
					}
				}
			}
		}
	}


	private boolean areComponentsAdjacent(Set<Integer> pixels1, Set<Integer> pixels2, int maxDistance) {
		int width = image.getWidth();
		// Définir la couleur des frontières pour la détection
		int borderColor = Color.WHITE.getRGB(); // Exemple : supposons que les frontières sont blanches

		// Utiliser un Map pour stocker les points de chaque zone par leur coordonnée x ou y pour faciliter les comparaisons
		Map<Integer, List<Integer>> xCoords1 = new HashMap<>();
		Map<Integer, List<Integer>> yCoords1 = new HashMap<>();
		Map<Integer, List<Integer>> xCoords2 = new HashMap<>();
		Map<Integer, List<Integer>> yCoords2 = new HashMap<>();

		// Remplir les maps pour les deux ensembles de pixels
		for (Integer p1 : pixels1) {
			int x1 = p1 % width;
			int y1 = p1 / width;
			xCoords1.computeIfAbsent(x1, k -> new ArrayList<>()).add(y1);
			yCoords1.computeIfAbsent(y1, k -> new ArrayList<>()).add(x1);
		}
		for (Integer p2 : pixels2) {
			int x2 = p2 % width;
			int y2 = p2 / width;
			xCoords2.computeIfAbsent(x2, k -> new ArrayList<>()).add(y2);
			yCoords2.computeIfAbsent(y2, k -> new ArrayList<>()).add(x2);
		}

		// Vérifier l'adjacence verticale et horizontale avec un intervalle maximal
		for (int x1 : xCoords1.keySet()) {
			for (int x2 : xCoords2.keySet()) {
				if (Math.abs(x1 - x2) <= maxDistance) {
					for (int y1 : xCoords1.get(x1)) {
						for (int y2 : xCoords2.get(x2)) {
							if (Math.abs(y1 - y2) == 1 && image.getRGB(x1, Math.min(y1, y2)) == borderColor) {
								return true;
							}
						}
					}
				}
			}
		}

		for (int y1 : yCoords1.keySet()) {
			for (int y2 : yCoords2.keySet()) {
				if (Math.abs(y1 - y2) <= maxDistance) {
					for (int x1 : yCoords1.get(y1)) {
						for (int x2 : yCoords2.get(y2)) {
							if (Math.abs(x1 - x2) == 1 && image.getRGB(Math.min(x1, x2), y1) == borderColor) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}


	public void applyColorsToImage() {
		if (zoneGraph.vertexSet().isEmpty()) {
			logger.warning("Zone graph is empty, no coloring possible.");
			return;
		}
		SaturationDegreeColoring<Integer, DefaultEdge> coloringAlg = new SaturationDegreeColoring<>(zoneGraph);
		Coloring<Integer> coloring = coloringAlg.getColoring();
		zoneColorMap = coloring.getColors();
		if (zoneColorMap == null || zoneColorMap.isEmpty()) {
			logger.warning("No colors were assigned to any zone.");
			return;
		}


		// Additional check to ensure no adjacent zones share the same color
		for (DefaultEdge edge : zoneGraph.edgeSet()) {
			Integer source = zoneGraph.getEdgeSource(edge);
			Integer target = zoneGraph.getEdgeTarget(edge);
			if (zoneColorMap.get(source).equals(zoneColorMap.get(target))) {
				logger.severe(String.format("Adjacent zones %d and %d have the same color %d", source, target, zoneColorMap.get(source)));
				// Implement a fallback or corrective action here
			}
		}
		//logger.info("Coloring result: " + zoneColorMap);
	}


	private Integer findZoneId(int pixelId) {
		for (Map.Entry<Integer, Set<Integer>> entry : componentMap.entrySet()) {
			if (entry.getValue().contains(pixelId)) {
				return entry.getKey();
			}
		}
		return null;
	}


}
