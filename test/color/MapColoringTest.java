package color;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.Test;

import graphics.Image;

public class MapColoringTest {

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
    void mediumMapColoring() {
        BufferedImage image = Image.loadImage("img/maps/three_colors_tiling.png");

        MapColoring mc = new MapColoring(image, Color.BLACK);
        assertNotNull(mc);
        mc.colorMap();

        SimpleGraph<Integer, DefaultEdge> graph = mc.adjacencyGraph();
        assertNotNull(graph);
        assertEquals(25, graph.vertexSet().size());
        assertEquals(52, graph.edgeSet().size());

        VertexColoringAlgorithm.Coloring<Integer> coloring = mc.getColoring();
        assertNotNull(coloring);
        assertEquals(5, coloring.getNumberColors());
    }

    @Test
    void largeMapColoring() {
        BufferedImage image = Image.loadImage("img/maps/conformally-regular-pentagonal-tiling.png");

        MapColoring mc = new MapColoring(image, Color.BLACK);
        assertNotNull(mc);
        mc.colorMap();

        SimpleGraph<Integer, DefaultEdge> graph = mc.adjacencyGraph();
        assertNotNull(graph);
        assertEquals(50, graph.vertexSet().size());
        assertEquals(122, graph.edgeSet().size());

        VertexColoringAlgorithm.Coloring<Integer> coloring = mc.getColoring();
        assertNotNull(coloring);
        assertEquals(7, coloring.getNumberColors());
    }

}
