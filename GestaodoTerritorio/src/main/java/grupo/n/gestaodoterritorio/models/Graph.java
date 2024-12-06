package grupo.n.gestaodoterritorio.models;


import com.mxgraph.layout.*;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class Graph<V> { // Classe genérica com <V> representando os vértices
    private final SimpleGraph<V, DefaultEdge> graph;

    /**
     * Construtor que inicializa o grafo vazio.
     */
    public Graph() {
        this.graph = new SimpleGraph<>(DefaultEdge.class);
    }

    /**
     * Constrói o grafo a partir de um mapa de vértices.
     * @param elements Mapa contendo os vértices (Property ou Owner).
     */
    public void createGraph(Map<String, V> elements) {
        System.out.println("\nCreating graph:");

        // Adiciona os nós
        for (V element : elements.values()) {
            graph.addVertex(element);
        }

        // Adiciona as arestas com base na interseção
        for (V v1 : elements.values()) {
            for (V v2 : elements.values()) {
                if (!v1.equals(v2) && hasIntersection(v1, v2)) {
                    graph.addEdge(v1, v2);
                }
            }
        }
    }

    /**
     * Verifica se dois vértices possuem interseção.
     * Este método é adaptado para diferentes tipos de vértices.
     */
    private boolean hasIntersection(V v1, V v2) {
        if (v1 instanceof Property p1 && v2 instanceof Property p2) {
            return p1.getGeometry().intersects(p2.getGeometry());
        } else if (v1 instanceof Owner o1 && v2 instanceof Owner o2) {

            // Verifica interseções entre as propriedades dos proprietários
            for (Property p1 : o1.getOwnerPropertyList()) {
                for (Property p2 : o2.getOwnerPropertyList()) {
                    if (p1.getGeometry().intersects(p2.getGeometry())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Exibe as estatísticas do grafo.
     */
    public void printGraphStats() {
        System.out.println("Sobre o Grafo:");
        System.out.println("- " + graph.vertexSet().size() + " nós.");
        System.out.println("- " + graph.edgeSet().size() + " arestas.");
    }

    /**
     * Desenha o grafo como uma imagem e salva no disco.
     * @param imgName Nome do arquivo de saída.
     */
    public void drawGraph(String imgName) {
        try {
            // Cria o adaptador para JGraphX
            JGraphXAdapter<V, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

            graphAdapter.getStylesheet().getDefaultVertexStyle().put("fontSize", 10);
            graphAdapter.getStylesheet().getDefaultVertexStyle().put("autosize", true);
            graphAdapter.getStylesheet().getDefaultVertexStyle().put("spacing", 10);

            mxFastOrganicLayout layout = new mxFastOrganicLayout(graphAdapter);
            // Configura layout circular
            //mxCircleLayout layout = new mxCircleLayout(graphAdapter);
            //layout.setRadius(300);
            layout.execute(graphAdapter.getDefaultParent());

            // Renderiza o grafo como imagem
            BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 1, null, true, null);

            // Limitar dimensões
            //if (image.getWidth() > 10000 || image.getHeight() > 10000) {
            //    System.err.println("Dimensões da imagem excedem o limite permitido.");
            //    return;
            //}

            // Salva a imagem no disco
            File outputFile = new File("GestaodoTerritorio/src/main/resources/images/grafo" + imgName + ".png");
            outputFile.getParentFile().mkdirs();
            ImageIO.write(image, "PNG", outputFile);

            System.out.println("Graph saved as image: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to create graph image.");
        }
    }

    /**
     * Retorna o grafo.
     * @return O grafo como SimpleGraph.
     */
    public SimpleGraph<V, DefaultEdge> getGraph() {
        return graph;
    }
}