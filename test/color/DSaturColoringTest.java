package color;

import static org.junit.jupiter.api.Assertions.*;

import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.Test;

public class DSaturColoringTest {

	@Test
	void nullGraph() {
		DSaturColoring<Integer, DefaultEdge> dsc = null;
		try {
			dsc = new DSaturColoring<>(null);
			fail();
		} catch(Exception e) {
			assertNull(dsc);
		}
	}

	@Test
	void emptyGraph() {
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
		VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();
		
		assertEquals(0, coloring.getNumberColors());
		assertEquals(0, coloring.getColors().size());
	}

	@Test
	void oneVertex() {
		SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		graph.addVertex(42);
		DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
		VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();
		
		assertEquals(1, coloring.getNumberColors());
		assertEquals(1, coloring.getColors().size());
	}
	
	@Test
    void twoConnectedVertices() {
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);
        DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
        VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();

        assertEquals(2, coloring.getNumberColors());
        assertEquals(2, coloring.getColors().size());
        assertNotEquals(coloring.getColors().get(1), coloring.getColors().get(2));
    }

    @Test
    void simpleCycle() {
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
        VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();

        assertEquals(3, coloring.getNumberColors());
        assertEquals(3, coloring.getColors().size());
        assertNotEquals(coloring.getColors().get(1), coloring.getColors().get(2));
        assertNotEquals(coloring.getColors().get(2), coloring.getColors().get(3));
        assertNotEquals(coloring.getColors().get(1), coloring.getColors().get(3));
    }

    @Test
    void completeGraph() {
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (int i = 1; i <= 4; i++) {
            graph.addVertex(i);
        }
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);

        DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
        VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();

        assertEquals(4, coloring.getNumberColors());
        assertEquals(4, coloring.getColors().size());
    }

    @Test
    void bipartiteGraph() {
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (int i = 1; i <= 4; i++) {
            graph.addVertex(i);
        }
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);

        DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
        VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();

        assertEquals(2, coloring.getNumberColors());
        assertEquals(4, coloring.getColors().size());
        assertNotEquals(coloring.getColors().get(1), coloring.getColors().get(3));
        assertNotEquals(coloring.getColors().get(2), coloring.getColors().get(4));
    }


    @Test
    void fiveVertex() {
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);

        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(4, 5);

        DSaturColoring<Integer, DefaultEdge> dsc = new DSaturColoring<>(graph);
        VertexColoringAlgorithm.Coloring<Integer> coloring = dsc.getColoring();

        assertEquals(2, coloring.getNumberColors());
        assertEquals(5, coloring.getColors().size());
    }

}
