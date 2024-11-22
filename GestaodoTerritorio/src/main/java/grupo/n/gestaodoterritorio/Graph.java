package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    /* Grafo que armazena:
     * - Property, como vértices (nós)
     * - DefaultEdge, como arestas (adjacências) */
    private SimpleGraph<String, DefaultEdge> graph;

    public Graph() {
        // Criando o grafo com DefaultEdge (arestas simples) e String como vértice (proprietários)
        this.graph = new SimpleGraph<>(DefaultEdge.class);
    }
    /* Constrói o grafo a partir do mapa de propriedades lido do ficheiro CSV em Properties Loader */
    public void createGraph(Map<String, Property> properties) {

        System.out.println("\nCreating graph:");
        //Adiciona os nós (propiedades)
        for (Property property : properties.values()) {
            graph.addVertex(String.valueOf(property));
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
                        graph.addEdge(String.valueOf(p1), String.valueOf(p2));
                    }
                }
            }
        }

    }

    /* Constrói o grafo a partir do mapa de propriedades lido do ficheiro CSV em PropertiesLoader */
    public void createGraphOwners(Map<String, Property> properties) {
        System.out.println("\nCreating graph:");

        // Usando um Set para garantir que os proprietários não sejam duplicados
        Set<String> owners = new HashSet<>();

        // Adiciona os proprietários ao conjunto
        for (Property property : properties.values()) {
            String owner = property.getOwner();
            owners.add(owner); // Adiciona o proprietário ao Set
        }

        // Adiciona os nós (proprietários) ao grafo
        for (String owner : owners) {
            graph.addVertex(owner);
        }

        // Adiciona as arestas (relações de vizinhança entre os proprietários)
        for (Property p1 : properties.values()) {
            for (Property p2 : properties.values()) {
                if (!p1.getOwner().equals(p2.getOwner())) { // Garante que são proprietários diferentes
                    // Verifica se as geometrias são válidas e se há interseção entre as propriedades
                    if (p1.getGeometry() != null && p2.getGeometry() != null &&
                            p1.getGeometry().isValid() && p2.getGeometry().isValid() &&
                            p1.getGeometry().intersects(p2.getGeometry())) {
                        System.out.println("Owners " + p1.getOwner() + " and " + p2.getOwner() + " are neighbors.");
                        // Adiciona aresta entre os proprietários se suas propriedades se intersectam
                        graph.addEdge(p1.getOwner(), p2.getOwner());
                    }
                }
            }
        }
    }

    /* Imprime estatísticas do grafo */
    public void printGraphStats() {
        System.out.println("Sobre o Grafo:");
        System.out.println("- " + graph.vertexSet().size() + " nós (proprietários)");
        System.out.println("- " + graph.edgeSet().size() + " arestas (vizinhancas)");
    }

    public SimpleGraph<String, DefaultEdge> getGraph() {
        return graph;
    }
}
