package grupo.n.gestaodoterritorio;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.List;

public class Grafo {
    private DefaultUndirectedGraph<Propriedade, DefaultEdge> grafo;

    public Grafo() {
        this.grafo = new DefaultUndirectedGraph<>(DefaultEdge.class);
    }

    public void adicionarPropriedade(Propriedade propriedade) {
        grafo.addVertex(propriedade);
    }

    public void conectarPropriedadesAdjacentes(Propriedade prop1, Propriedade prop2) {

        if (prop1.isAdjacentTo(prop2)) { // Apenas conecta se forem adjacentes
            grafo.addEdge(prop1, prop2);
        }
    }
    public void construirGrafo(List<Propriedade> propriedades) {
        // Adiciona todas as propriedades como nós no grafo
        for (Propriedade propriedade : propriedades) {
            adicionarPropriedade(propriedade);
        }

        // Verifica cada par de propriedades para determinar adjacências
        for (int i = 0; i < propriedades.size(); i++) {
            for (int j = i + 1; j < propriedades.size(); j++) {
                Propriedade prop1 = propriedades.get(i);
                Propriedade prop2 = propriedades.get(j);
                conectarPropriedadesAdjacentes(prop1, prop2); // Conecta se adjacentes
            }
        }
    }
}

