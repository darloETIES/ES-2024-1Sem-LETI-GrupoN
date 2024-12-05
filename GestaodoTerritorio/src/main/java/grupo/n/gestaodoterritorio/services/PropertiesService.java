package grupo.n.gestaodoterritorio.services;

import grupo.n.gestaodoterritorio.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.List;
import java.util.Map;

public class PropertiesService {

    private static PropertiesService instance; // Instância única
    private PropertiesLoader propertiesLoader;
    private Graph<Owner> graphOwners;
    private Map<String, Property> properties;
    private Map<String, Owner> owners;

    private PropertiesService() {
    }

    // Método para obter a instância única
    public static PropertiesService getInstance() {
        if (instance == null) {
            instance = new PropertiesService();
        }
        return instance;
    }

    // Método para carregar os dados do CSV (vindo de PropertiesLoader)
    public void loadProperties(String filePath) throws Exception {
        propertiesLoader = new PropertiesLoader(filePath);
        properties = propertiesLoader.readProperties();
        owners = propertiesLoader.readOwners();

        //gerar de inicio o grafo dos owners (para as trocas)
        generateOwnersGraph(owners);
    }


    // Método para calcular a área média (vindo de PropertiesLoader)
    public double getAverageArea(String parish, String county, String district) {
        if (propertiesLoader == null) {
            throw new IllegalStateException("PropertiesLoader não foi inicializado. Necessário carregar os dados primeiro.");
        }
        return propertiesLoader.averageAreaProp(parish, county, district);
    }

    // Método para calcular a área média, de um proprietário (vindo de PropertiesLoader)
    public double getAveragePropAreaByOwner(String parish, String county, String district, String owner) {
        if (propertiesLoader == null) {
            throw new IllegalStateException("PropertiesLoader não foi inicializado. Necessário carregar os dados primeiro.");
        }
        return propertiesLoader.averagePropAreaByOwner(parish, county, district, owner);
    }

    // Método para obter todas as propriedades
    public Map<String, Property> getProperties() {
        if (properties == null) {
            throw new IllegalStateException("Dados não foram carregados. Certifique-se de carregar os dados antes.");
        }
        return properties;
    }

    // Método para obter todos os proprietarios
    public Map<String, Owner> getOwners() {
        if (owners == null) {
            throw new IllegalStateException("Dados não foram carregados. Certifique-se de carregar os dados antes.");
        }
        return owners;
    }

    public List<Proposal> getExchSuggestions(SimpleGraph<Owner, DefaultEdge> graph){
        if(propertiesLoader == null){
            throw new IllegalStateException("Dados não foram carregados. Certifique-se de carregar os dados antes.");
        }
        return propertiesLoader.exchSuggestions(graph);
    }


    //fazer e obter o grafo dos owners para as trocas
    public void generateOwnersGraph(Map<String, Owner> owners) {
        graphOwners = new Graph();
        graphOwners.createGraph(owners);
    }

    public SimpleGraph<Owner, DefaultEdge> getGraphOwners() {
        return graphOwners.getGraph();
    }
}
