package color;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.Test;

import graphics.Image;

public class MapColoringTest {

	private static final Logger logger = Logger.getLogger(MapColoringTest.class.getName());
	@Test
	void nullImage() {
		MapColoring mc = null;
		try {
			mc = new MapColoring(null, Color.BLACK);
			fail();
		} catch(Exception e) {
			assertNull(mc);
		}
	}


	@Test
	void nullColor() {
		MapColoring mc = null;
		try {
			BufferedImage image = Image.loadImage("img/maps/small_map.png");
			mc = new MapColoring(image, null);
			fail();
		} catch(Exception e) {
			assertNull(mc);
		}
	}

	@Test
	void smallMapColoring() {
		BufferedImage image = Image.loadImage("img/maps/small_map.png");
		
		MapColoring mc = new MapColoring(image, Color.BLACK);
		assertNotNull(mc);
		mc.colorMap();
		
		SimpleGraph<Integer, DefaultEdge> graph = mc.adjacencyGraph();
		assertNotNull(graph);
		assertEquals(11, graph.vertexSet().size());
		assertEquals(21, graph.edgeSet().size());
		
		VertexColoringAlgorithm.Coloring<Integer> coloring = mc.getColoring();
		assertNotNull(coloring);
		assertEquals(4, coloring.getNumberColors());
	}

	@Test
	void testAdjacencyGraphSimpleImage() {
		// Create a simple test image
		int width = 5, height = 5;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Fill the image with all black (border color)
		Color borderColor = Color.BLACK;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image.setRGB(x, y, borderColor.getRGB());
			}
		}

		// Draw a white square in the middle
		Color zoneColor = Color.WHITE;
		image.setRGB(2, 1, zoneColor.getRGB());
		image.setRGB(2, 2, zoneColor.getRGB());
		image.setRGB(1, 2, zoneColor.getRGB());
		image.setRGB(3, 2, zoneColor.getRGB());
		image.setRGB(2, 3, zoneColor.getRGB());

		// Initialize MapColoring with the created image
		MapColoring mapColoring = new MapColoring(image, borderColor);
		SimpleGraph<Integer, DefaultEdge> graph = mapColoring.adjacencyGraph();

		// Check if the graph contains the expected number of vertices (5)
		assertEquals(5, graph.vertexSet().size(), "Graph should contain 5 vertices");

		// Check if the graph contains the expected number of edges (4)
		assertEquals(4, graph.edgeSet().size(), "Graph should contain 4 edges");

		// Check if specific edges are present (verify adjacency)
		assertTrue(graph.containsEdge(0, 2) || graph.containsEdge(1, 0), "Edge between vertices 0 and 1 should exist");
		assertTrue(graph.containsEdge(1, 2) || graph.containsEdge(2, 0), "Edge between vertices 0 and 2 should exist");
		assertTrue(graph.containsEdge(2, 3) || graph.containsEdge(3, 0), "Edge between vertices 0 and 3 should exist");
		assertTrue(graph.containsEdge(2, 4) || graph.containsEdge(4, 0), "Edge between vertices 0 and 4 should exist");
	}

	@Test
	void testIdentifyZonesSimpleImage() {
		// Create a simple test image with multiple zones
		int width = 5, height = 5;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Fill the entire image with border color (black)
		Color borderColor = Color.BLACK;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image.setRGB(x, y, borderColor.getRGB());
			}
		}

		// Create a zone (Z1) in the top-left corner
		Color zoneColor1 = Color.WHITE;
		image.setRGB(0, 0, zoneColor1.getRGB());
		image.setRGB(1, 0, zoneColor1.getRGB());
		image.setRGB(0, 1, zoneColor1.getRGB());
		image.setRGB(1, 1, zoneColor1.getRGB());

		// Create a separate zone (Z2) in the bottom-right corner
		Color zoneColor2 = Color.GREEN;
		image.setRGB(3, 3, zoneColor2.getRGB());
		image.setRGB(4, 3, zoneColor2.getRGB());
		image.setRGB(3, 4, zoneColor2.getRGB());
		image.setRGB(4, 4, zoneColor2.getRGB());

		// Initialize MapColoring with the created image
		MapColoring mapColoring = new MapColoring(image, borderColor);
		List<Set<Integer>> zones = mapColoring.identifyZone();

		// Check the number of identified zones
		assertEquals(2, zones.size(), "The graph should contain 2 separate zones.");

		// Verify that each zone contains the expected number of vertices
		boolean foundZone1 = false;
		boolean foundZone2 = false;
		logger.info(String.format("ZoneS : %s", zones));
		for (Set<Integer> zone : zones) {
			logger.info(String.format("Found zone size: %d", zone.size()));
			if (zone.size() == 4) {
				logger.info(String.format("Found zone : %s", zone));
				if (zone.contains(0) && zone.contains(1) && zone.contains(2) && zone.contains(3)) {
					foundZone1 = true;
				} else if (zone.contains(4) && zone.contains(5) && zone.contains(6) && zone.contains(7)) {
					foundZone2 = true;
				}
			}
		}

		assertTrue(foundZone1, "Zone 1 should be identified correctly.");
		assertTrue(foundZone2, "Zone 2 should be identified correctly.");
	}

	@Test
	void testCreateZoneAdjacencyGraph() {
		// Crée une image de test
		int width = 7, height = 7;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Remplit l'image avec une couleur de bordure (noir)
		Color borderColor = Color.BLACK;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image.setRGB(x, y, borderColor.getRGB());
			}
		}

		// Dessine plusieurs zones distinctes pour les tests
		// Zone 0 : rectangle blanc 3x3 en haut à gauche
		Color zoneColor1 = Color.WHITE;
		for (int y = 1; y <= 3; y++) {
			for (int x = 1; x <= 3; x++) {
				image.setRGB(x, y, zoneColor1.getRGB());
			}
		}

		// Zone 1 : rectangle cyan 2x3 en haut à droite
		Color zoneColor2 = Color.CYAN;
		for (int y = 1; y <= 3; y++) {
			for (int x = 5; x <= 6; x++) {
				image.setRGB(x, y, zoneColor2.getRGB());
			}
		}

		// Zone 2 : rectangle vert 2x2 en bas
		Color zoneColor3 = Color.GREEN;
		for (int y = 5; y <= 6; y++) {
			for (int x = 3; x <= 4; x++) {
				image.setRGB(x, y, zoneColor3.getRGB());
			}
		}

		// Zone 3 : rectangle jaune 2x1 adjacent à la Zone 2
		Color zoneColor4 = Color.YELLOW;
		image.setRGB(6, 5, zoneColor4.getRGB());
		image.setRGB(6, 6, zoneColor4.getRGB());

		// Crée une instance de MapColoring
		MapColoring mapColoring = new MapColoring(image, borderColor);

		// Crée le graphe d'adjacence des zones
		SimpleGraph<Integer, DefaultEdge> graph = mapColoring.createZoneAdjacencyGraph();

		// Vérifie que le graphe contient les sommets attendus (indices de 0 à 3)
		Set<Integer> expectedVertices = Set.of(0, 1, 2, 3);
		assertEquals(expectedVertices, graph.vertexSet(), "Le graphe doit contenir les sommets 0, 1, 2 et 3");

		// Vérifie les arêtes entre les zones adjacentes
		assertTrue(graph.containsEdge(2, 3) || graph.containsEdge(3, 2), "Les zones 2 et 3 doivent être adjacentes");

		// Vérifie que les zones non adjacentes ne sont pas connectées
		assertFalse(graph.containsEdge(0, 1), "Les zones 0 et 1 ne doivent pas être adjacentes");
		assertFalse(graph.containsEdge(0, 2), "Les zones 0 et 2 ne doivent pas être adjacentes");
		assertFalse(graph.containsEdge(0, 3), "Les zones 0 et 3 ne doivent pas être adjacentes");
		assertFalse(graph.containsEdge(1, 2), "Les zones 1 et 2 ne doivent pas être adjacentes");
		assertFalse(graph.containsEdge(1, 3), "Les zones 1 et 3 ne doivent pas être adjacentes");
	}
}
