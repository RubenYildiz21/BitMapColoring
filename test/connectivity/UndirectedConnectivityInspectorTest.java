package connectivity;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UndirectedConnectivityInspectorTest {

    private Graph<Integer, DefaultEdge> graph;
    private UndirectedConnectivityInspector<Integer, DefaultEdge> inspector;

    @BeforeEach
    void setUp() {
        graph = new SimpleGraph<>(DefaultEdge.class);
    }

    @Test
    void nullGraph() {
        assertThrows(NullPointerException.class, () -> new UndirectedConnectivityInspector<>(null));
    }

    @Test
    void emptyGraph() {
        inspector = new UndirectedConnectivityInspector<>(graph);
        assertTrue(inspector.isConnected(), "Empty graph should be considered connected");
        assertEquals(0, inspector.connectedSets().size(), "Empty graph should have 0 connected sets");
    }

    @Test
    void oneNodeGraph() {
        graph.addVertex(10);
        inspector = new UndirectedConnectivityInspector<>(graph);
        assertTrue(inspector.isConnected(), "Graph with one node should be considered connected");
        assertEquals(1, inspector.connectedSets().size(), "Graph with one node should have 1 connected set");
        assertEquals(1, inspector.connectedSets().get(0).size(), "Connected set should have exactly one node");
        assertTrue(inspector.connectedSets().get(0).contains(10), "Connected set should contain the node 10");
    }

    @Test
    void testIsConnected() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        inspector = new UndirectedConnectivityInspector<>(graph);
        assertTrue(inspector.isConnected(), "Graph should be considered connected");
    }

    @Test
    void testIsNotConnected() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);

        inspector = new UndirectedConnectivityInspector<>(graph);
        assertFalse(inspector.isConnected(), "Graph should not be considered connected");
    }

    @Test
    void testConnectedSets() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        
        inspector = new UndirectedConnectivityInspector<>(graph);
        assertEquals(2, inspector.connectedSets().size(), "Graph should have 2 connected sets");
    }
}

