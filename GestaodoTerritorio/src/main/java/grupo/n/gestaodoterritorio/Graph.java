package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.util.List;
import java.util.Map;

public class Graph {
    /*Grafo que armazena:
    * - Property, como vertices (nós)
    * - DefaultEdge, como arestas (adjacências)*/
    private SimpleGraph<Property, DefaultEdge> graph;

    public Graph() {
        this.graph = new SimpleGraph<>(DefaultEdge.class);
    }

    /* Constrói o grafo a partir do mapa de propriedades lido do ficheiro CSV em Properties Loader */
    public void createGraph(Map<String, Property> properties) {
        //Adiciona os nós (propiedades)
        for (Property property : properties.values()) {
            graph.addVertex(property);
        }

        //Adiciona as arestas (vizinhança/adjacencias)
        for(Property p1 : properties.values()) {
            for(Property p2 : properties.values()) {
                //Certifica-se que são propriedades diferentes
                if(!p1.getObjectId().equals(p2.getObjectId())) {
                    //Guarda em objetos Geometry os dados do parametro geometry de cada propriedade
                    Geometry g1 = p1.getGeometry();
                    Geometry g2 = p2.getGeometry();

                    //Comparamos geometry das duas propriedades (intersetam-se?)
                    if(g1.intersects(g2))
                        graph.addEdge(p1, p2); //no caso de intersetarem, cria a vizinhança
                }
            }
        }
    }

    public void printGraphStats() {
        System.out.println("Sobre o Grafo:");
        System.out.println("- " + graph.vertexSet().size() + " nós (propriedades)");
        System.out.println("- " + graph.edgeSet().size() + " arestas (vizinhancas)");
    }

    public SimpleGraph<Property, DefaultEdge> getGraph() {
        return graph;
    }
}
