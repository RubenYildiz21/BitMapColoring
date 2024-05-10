package color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import connectivity.UndirectedConnectivityInspector;
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

	// Tableau de couleurs prédéfini
	@SuppressWarnings("unused")
	private static final int[] rgbColors = { Color.BLUE.getRGB(), Color.RED.getRGB(), Color.GREEN.getRGB(),
			Color.YELLOW.getRGB(), Color.CYAN.getRGB(), Color.PINK.getRGB(), Color.ORANGE.getRGB(),
			Color.MAGENTA.getRGB() };

	private final BufferedImage image;
	private final Color borderColor;
	private SimpleGraph<Integer, DefaultEdge> zoneAdjacencyGraph;
	private List<Set<Integer>> zones;

	/**
	 * Constructor
	 * 
	 * @param image       a bitmap image representing a map to color
	 * @param borderColor the color of the lines delimiting the map areas
	 * @throws NullPointerException if any input parameter is null
	 */
	public MapColoring(BufferedImage image, Color borderColor) {
		if (image == null || borderColor == null){
			throw new NullPointerException("Input image or border color cannot be null.");
		}
		this.image = image;
		this.borderColor = borderColor;
	}

	/**
	 * Color the map areas with the least possible colors so that two adjacent areas
	 * have different colors.
	 */
	public void colorMap() {
		// Obtenez la coloration du graphe d'adjacence
		Coloring<Integer> coloring = getColoring();

		if (coloring != null) {
			Map<Integer, Integer> zoneColors = coloring.getColors();

			// Récupérez les ensembles de zones identifiés
			zones = identifyZone();

			// Appliquez les couleurs appropriées à chaque point dans chaque zone
			for (int zoneIndex = 0; zoneIndex < zones.size(); zoneIndex++) {
				Set<Integer> zonePoints = zones.get(zoneIndex);
				int colorIndex = zoneColors.get(zoneIndex);
				int rgbColor = rgbColors[colorIndex % rgbColors.length]; // Évitez un dépassement d'index

				// Coloriez chaque point de la zone
				for (int pointIndex : zonePoints) {
					int x = pointIndex % image.getWidth();
					int y = pointIndex / image.getWidth();
					image.setRGB(x, y, rgbColor);
				}
			}

			logger.info("Carte colorée avec succès !");
		}
	}

	/**
	 * Return the adjacency graph of the image's areas.
	 *
	 * @return the adjacency graph of the image's areas
	 */
	public SimpleGraph<Integer, DefaultEdge> adjacencyGraph() {
		SimpleGraph<Integer, DefaultEdge> adjacencyGraph = new SimpleGraph<>(DefaultEdge.class);
		Map<Integer, Integer> pointToVertex = new HashMap<>();
		int width = image.getWidth();
		int height = image.getHeight();
		int vertexId = 0;

		// Créez des sommets pour chaque pixel non-bordure
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				if (rgb != borderColor.getRGB()) {
					int index = y * width + x;
					pointToVertex.put(index, vertexId);
					adjacencyGraph.addVertex(vertexId);
					vertexId++;
				}
			}
		}

		// Créez les arêtes entre les pixels voisins non-bordure
		int[][] neighborOffsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		for (Map.Entry<Integer, Integer> entry : pointToVertex.entrySet()) {
			int index = entry.getKey();
			int vertex = entry.getValue();
			int x = index % width;
			int y = index / width;

			for (int[] offset : neighborOffsets) {
				int neighborX = x + offset[0];
				int neighborY = y + offset[1];
				if (neighborX >= 0 && neighborX < width && neighborY >= 0 && neighborY < height) {
					int neighborIndex = neighborY * width + neighborX;
					if (pointToVertex.containsKey(neighborIndex)) {
						int neighborVertex = pointToVertex.get(neighborIndex);
						if (!adjacencyGraph.containsEdge(vertex, neighborVertex)) {
							adjacencyGraph.addEdge(vertex, neighborVertex);
						}
					}
				}
			}
		}
		return adjacencyGraph;
	}


	/**
	 * Return the coloring of the adjacency graph.
	 * 
	 * @return the coloring of the adjacency graph
	 */
	public Coloring<Integer> getColoring() {
		SimpleGraph<Integer, DefaultEdge> graph = createZoneAdjacencyGraph();
		DSaturColoring<Integer, DefaultEdge> dsatur = new DSaturColoring<>(graph);
		return dsatur.getColoring();
	}


	public List<Set<Integer>> identifyZone() {
		// Utilisez un graphe d'adjacence pour identifier les zones.
		SimpleGraph<Integer, DefaultEdge> adjacencyGraph = adjacencyGraph();
		return new UndirectedConnectivityInspector<>(adjacencyGraph).connectedSets();
	}

	public SimpleGraph<Integer, DefaultEdge> createZoneAdjacencyGraph() {
		if (zoneAdjacencyGraph == null) {
			zoneAdjacencyGraph = new SimpleGraph<>(DefaultEdge.class);
			zones = identifyZone();
			logger.info(String.format("Identified %d zones for the adjacency graph.", zones.size()));

			// Add vertices
			for (int i = 0; i < zones.size(); i++) {
				zoneAdjacencyGraph.addVertex(i);
				logger.info(String.format("Vertex %s added to the adjacency graph.", i));
			}

			// Add edges only if two zones share a border
			for (int i = 0; i < zones.size(); i++) {
				Set<Integer> zoneA = zones.get(i);
				String zoneAName = "Z" + i;

				for (int j = i + 1 ; j < zones.size(); j++) {
					Set<Integer> zoneB = zones.get(j);
					String zoneBName = "Z" + j;

					if (hasSharedBorder(zoneA, zoneB)) {
						zoneAdjacencyGraph.addEdge(i, j);
						logger.info(String.format("Edge added between %s and %s.", zoneAName, zoneBName));
					} else {
						logger.info(String.format("No shared border between %s and %s.", zoneAName, zoneBName));
					}
				}
			}

			logger.info(String.format("Zone adjacency graph created with %d vertices and %d edges.",
					zoneAdjacencyGraph.vertexSet().size(), zoneAdjacencyGraph.edgeSet().size()));
		}
		return zoneAdjacencyGraph;
	}


	private boolean hasSharedBorder(Set<Integer> zoneA, Set<Integer> zoneB) {
		int width = image.getWidth();
		int[] horizontalOffsets = {-1, 1};
		int[] verticalOffsets = {-width, width};

		for (int pointA : zoneA) {
			int xA = pointA % width;
			int yA = pointA / width;

			// Vérifiez les voisins horizontaux (gauche/droite)
			for (int hOffset : horizontalOffsets) {
				for (int distance = 1; distance <= 4; distance++) {
					int neighbor = pointA + hOffset * distance;
					int xNeighbor = neighbor % width;
					int yNeighbor = neighbor / width;

					// Vérifiez si le voisin est sur la même ligne
					if (zoneB.contains(neighbor) && yA == yNeighbor) {
						logger.info(String.format(
								"Horizontal adjacency: Point %d (%d, %d) in ZoneA is adjacent to Point %d (%d, %d) in ZoneB.",
								pointA, xA, yA, neighbor, xNeighbor, yNeighbor));
						return true;
					}
				}
			}

			// Vérifiez les voisins verticaux (haut/bas)
			for (int vOffset : verticalOffsets) {
				for (int distance = 1; distance <= 4; distance++) {
					int neighbor = pointA + vOffset * distance;
					int xNeighbor = neighbor % width;
					int yNeighbor = neighbor / width;

					// Vérifiez si le voisin est dans la même colonne
					if (zoneB.contains(neighbor) && xA == xNeighbor) {
						logger.info(String.format(
								"Vertical adjacency: Point %d (%d, %d) in ZoneA is adjacent to Point %d (%d, %d) in ZoneB.",
								pointA, xA, yA, neighbor, xNeighbor, yNeighbor));
						return true;
					}
				}
			}
		}

		logger.info("No shared border found between zones.");
		return false;
	}

}
