package color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;

/**
 * This class implements the D-Satur coloring algorithm.
 * 
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 * 
 * @author François Schumacker
 *
 */
public class DSaturColoring<V, E> implements VertexColoringAlgorithm<V> {
    private static final java.util.logging.Logger logger = Logger.getLogger(DSaturColoring.class.getName());
	public final Graph<V, E> graph; 
	
    /**
     * Construct a new DSatur algorithm.
     * 
     * @param graph the input graph
     * @throws NullPointerException if the input graph is null
     */
	public DSaturColoring(Graph<V,E> graph) {
		if(graph == null) throw new NullPointerException("This graph cannot be null");
		this.graph = graph;
	}

	/**
	 * Computes a vertex coloring.
	 * @return a vertex coloring
	 */
	public Coloring<V> getColoring() {
	    Map<V, Integer> colors = new HashMap<>();
	    Map<V, Set<Integer>> usedColors = new HashMap<>();

	    // Initialize usedColors for each vertex
	    for (V vertex : graph.vertexSet()) {
	        usedColors.put(vertex, new HashSet<>());
	    }

	    // If there are no vertices, return a coloring with zero colors
	    if (graph.vertexSet().isEmpty()) {
	        return new ColoringImpl<>(colors, 0);
	    }

	    // Sort vertices by degree for proper ordering
	    List<V> vertices = new ArrayList<>(graph.vertexSet());
	    vertices.sort((v1, v2) -> graph.degreeOf(v2) - graph.degreeOf(v1));

        for (V vertex : vertices) {
            if (!colors.containsKey(vertex)) {
                Set<Integer> adjacentColors = getAdjacentColors(vertex, colors);
                int color = getSmallestAvailableColor(adjacentColors);
                colors.put(vertex, color);
                //updateUsedColors(vertex, color, usedColors);
                logger.info(String.format("Vertex %s colored with %d using colors adjacent %s", vertex.toString(), color, adjacentColors.toString()));
            }
        }

	    return new ColoringImpl<>(colors, Collections.max(colors.values()) + 1);
	}

    
    /**
     * Gets colors that are already used by the neighbors of a vertex.
     *
     * @param vertex the vertex to check
     * @param colors map of current colors
     * @return a set of used colors
     */
    private Set<Integer> getAdjacentColors(V vertex, Map<V, Integer> colors) {
        Set<Integer> adjacentColors = new HashSet<>();
        for (E edge : graph.edgesOf(vertex)) {
            V source = graph.getEdgeSource(edge);
            V target = graph.getEdgeTarget(edge);
            V opposite = source.equals(vertex) ? target : source;

            if (colors.containsKey(opposite)) {
                adjacentColors.add(colors.get(opposite));
            }
        }
        return adjacentColors;
    }
    
    /**
     * Finds the lowest unused color.
     *
     * @param usedColors colors that cannot be used
     * @return the lowest available color
     */
    private int getSmallestAvailableColor(Set<Integer> usedColors) {
        int color = 0;
        while (usedColors.contains(color)) {
            color++;
        }
        return color;
    }
    
    
    /**
     * Updates the sets of used colors for all neighbors of a colored vertex.
     *
     * @param vertex the recently colored vertex
     * @param color the color used for the vertex
     * @param usedColors map of vertex to colors used by its neighbors
     */
    private void updateUsedColors(V vertex, int color, Map<V, Set<Integer>> usedColors) {
        for (E edge : graph.edgesOf(vertex)) {
            V source = graph.getEdgeSource(edge);
            V target = graph.getEdgeTarget(edge);
            V opposite = source.equals(vertex) ? target : source;

            if (usedColors.containsKey(opposite)) {
                usedColors.get(opposite).add(color);
                logger.info(String.format("Updating used colors for vertex %s: added color %d", opposite.toString(), color));
            }
        }
    }

}
