package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.util.List;
import java.util.Map;

public class Graph {
    /**Grafo que armazena:
    * - Property, como vertices (nós)
    * - DefaultEdge, como arestas (adjacências)*/
    private SimpleGraph<Property, DefaultEdge> graph;

    public Graph() {
        this.graph = new SimpleGraph<>(DefaultEdge.class);
    }

    /** Constrói o grafo a partir do mapa de propriedades lido do ficheiro CSV em Properties Loader */
    public void createGraph(Map<String, Property> properties) {

        System.out.println("\nCreating graph:");
        //Adiciona os nós (propiedades)
        for (Property property : properties.values()) {
            graph.addVertex(property);
        }

        //Adiciona as arestas (vizinhança/adjacencias)
        for(Property p1 : properties.values()) {
            for(Property p2 : properties.values()) {
                if (!p1.getObjectId().equals(p2.getObjectId())) {
                    Geometry g1 = p1.getGeometry();
                    Geometry g2 = p2.getGeometry();
                    boolean intersects = g1.intersects(g2);
                    if (intersects) {
                        System.out.println("Comparing " + p1.getObjectId() + " and " + p2.getObjectId() + ": " + intersects);
                        graph.addEdge(p1, p2);
                    }
                }
            }
        }

    }

    /**
     * Informações sobre o grafo
     */
    public void printGraphStats() {
        System.out.println("Sobre o Grafo:");
        System.out.println("- " + graph.vertexSet().size() + " nós (propriedades)");
        System.out.println("- " + graph.edgeSet().size() + " arestas (vizinhancas)");
    }

    /**
     * Obter grafo
     * @return
     */
    public SimpleGraph<Property, DefaultEdge> getGraph() {
        return graph;
    }
}
