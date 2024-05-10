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

import org.jgrapht.Graph;
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
	void testCreateGraphFromImage3x3() {
		// Création d'une image 3x3 où le centre est noir et tous les autres pixels sont blancs
		BufferedImage testImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
		int white = Color.WHITE.getRGB();
		int black = Color.BLACK.getRGB();

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (x == 1 && y == 1) {
					testImage.setRGB(x, y, black);
				} else {
					testImage.setRGB(x, y, white);
				}
			}
		}

		MapColoring mc = new MapColoring(testImage, Color.BLACK);
		Graph<Integer, DefaultEdge> graph = mc.adjacencyGraph();

		assertNotNull(graph);
		assertEquals(8, graph.vertexSet().size(), "Should have 8 vertices for white pixels");

		// Test des connexions, chaque pixel blanc devrait être connecté seulement avec ses voisins directs non noirs
		int expectedEdges = 8;
		assertEquals(expectedEdges, graph.edgeSet().size(), "Should have correct number of edges based on adjacency in image");
	}

	@Test
	void testCreateGraphFromImage5x5() {
		// Création d'une image 3x3 où le centre est noir et tous les autres pixels sont blancs
		BufferedImage testImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
		int white = Color.WHITE.getRGB();
		int black = Color.BLACK.getRGB();

		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				if (x == 2 && y == 2) {
					testImage.setRGB(x, y, black);
				} else {
					testImage.setRGB(x, y, white);
				}
			}
		}

		MapColoring mc = new MapColoring(testImage, Color.BLACK);
		Graph<Integer, DefaultEdge> graph = mc.adjacencyGraph();

		assertNotNull(graph);
		assertEquals(24, graph.vertexSet().size(), "le graph doit avoir 24 sommets pour les pixels blanc");

		// Test des connexions, chaque pixel blanc devrait être connecté seulement avec ses voisins directs non noirs
		int expectedEdges = 36;
		assertEquals(expectedEdges, graph.edgeSet().size(), "le graph doit avoir 36 aretes");
	}

	@Test
	void testConnectedComponents() {
		// Création d'une image 5x5
		BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
		int white = Color.WHITE.getRGB();
		int black = Color.BLACK.getRGB();

		// Dessiner des pixels (laissant des bordures et un carré central noir pour simuler des zones)
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				if (x == 2 && y == 2) {
					image.setRGB(x, y, black); // Centre noir
				} else {
					image.setRGB(x, y, white); // Autres pixels blancs
				}
			}
		}

		// Définir la couleur de bordure comme noire
		MapColoring mc = new MapColoring(image, Color.BLACK);

		// Récupérer les composantes connexes
		List<Set<Integer>> components = mc.getConnectedComponents();

		// Assertions pour vérifier les résultats
		assertNotNull(components, "La liste des composantes ne doit pas être nulle");
		assertEquals(1, components.size(), "Il doit y avoir exactement une composante connexe de couleur blanche");
		assertEquals(24, components.get(0).size(), "La composante connexe doit contenir 24 sommets (tous sauf le centre)");
	}

	@Test
	void testAdjacencyGraphCreationAlone() {
		// Créer une image test simple avec deux zones adjacentes
		BufferedImage image = new BufferedImage(3, 2, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, Color.WHITE.getRGB());
		image.setRGB(1, 0, Color.WHITE.getRGB());
		image.setRGB(2, 0, Color.BLACK.getRGB());
		image.setRGB(0, 1, Color.BLACK.getRGB());
		image.setRGB(1, 1, Color.WHITE.getRGB());
		image.setRGB(2, 1, Color.WHITE.getRGB());

		MapColoring mc = new MapColoring(image, Color.BLACK);
		Graph<Integer, DefaultEdge> adjacencyGraph = mc.getAdjacencyGraph();

		assertNotNull(adjacencyGraph);
		assertEquals(1, adjacencyGraph.vertexSet().size(), "Should have 2 distinct components");
		assertTrue(adjacencyGraph.containsEdge(0, 1), "Should have an edge between the two components");
	}

	@Test
	void testAdjacencyGraphCreation2Zone() {
		// Créer une image test avec deux zones blanches séparées par des bandes noires
		BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);
		// Définir les pixels pour la première zone blanche
		image.setRGB(0, 0, Color.WHITE.getRGB());
		image.setRGB(0, 1, Color.WHITE.getRGB());
		image.setRGB(0, 2, Color.WHITE.getRGB());
		image.setRGB(0, 3, Color.WHITE.getRGB());

		// Définir une bande noire pour séparer les zones blanches
		for (int i = 1; i <= 3; i++) {
			for (int j = 0; j < 4; j++) {
				image.setRGB(i, j, Color.BLACK.getRGB());
			}
		}

		// Définir les pixels pour la deuxième zone blanche
		image.setRGB(3, 0, Color.WHITE.getRGB());
		image.setRGB(3, 1, Color.WHITE.getRGB());
		image.setRGB(3, 2, Color.WHITE.getRGB());
		image.setRGB(3, 3, Color.WHITE.getRGB());

		MapColoring mc = new MapColoring(image, Color.BLACK);
		Graph<Integer, DefaultEdge> adjacencyGraph = mc.getAdjacencyGraph();

		assertNotNull(adjacencyGraph, "Adjacency graph should not be null");
		assertEquals(2, adjacencyGraph.vertexSet().size(), "Should have 2 distinct components");

		// Vérifiez qu'il n'y a pas d'arêtes entre les deux zones, car elles sont isolées par des pixels noirs
		assertEquals(0, adjacencyGraph.edgeSet().size(), "There should be no edges between the two components");
	}

}
