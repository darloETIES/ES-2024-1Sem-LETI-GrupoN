package grupo.n.gestaodoterritorio.services;

import grupo.n.gestaodoterritorio.models.*;
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

    /**
     * Método para obter a instância única
     * @return Instância única (singleton)
     */
    public static PropertiesService getInstance() {
        if (instance == null) {
            instance = new PropertiesService();
        }
        return instance;
    }

    /**
     * Método para carregar os dados do CSV (vindo de PropertiesLoader)
     * @param filePath Caminho para o ficheiro CSV
     * @throws Exception
     */
    public void loadProperties(String filePath) throws Exception {
        propertiesLoader = new PropertiesLoader(filePath);
        properties = propertiesLoader.readProperties();
        owners = propertiesLoader.readOwners();
    }

    /**
     * Método para obter o cálculo da área média (vindo de PropertiesLoader)
     * @param parish Freguesia da área geográfica
     * @param county Concelho da área geeográfica
     * @param district Distrito da área geográfica
     * @return Área média das propriedades numa área geográfica
     */
    public double getAverageArea(String parish, String county, String district) {
        if (propertiesLoader == null) {
            throw new IllegalStateException("PropertiesLoader não foi inicializado. Necessário carregar os dados primeiro.");
        }
        return propertiesLoader.averageAreaProp(parish, county, district);
    }


    /**
     * Método para calcular a área média, de um proprietário (vindo de PropertiesLoader)
     * @param parish Freguesia da área geográfica
     * @param county Concelho da área geeográfica
     * @param district Distrito da área geográfica
     * @param owner Proprietário escolhido
     * @return Área média das propriedades numa área geográfica do proprietário escolhido
     */
    public double getAveragePropAreaByOwner(String parish, String county, String district, String owner) {
        if (propertiesLoader == null) {
            throw new IllegalStateException("PropertiesLoader não foi inicializado. Necessário carregar os dados primeiro.");
        }
        return propertiesLoader.averagePropAreaByOwner(parish, county, district, owner);
    }

    /**
     * Método para obter todas as propriedades
     * @return Mapa com as propriedades e respetivo ID
     */
    public Map<String, Property> getProperties() {
        if (properties == null) {
            throw new IllegalStateException("Dados não foram carregados. Certifique-se de carregar os dados antes.");
        }
        return properties;
    }


    /**
     * Método para obter todos os proprietarios
     * @return Mapa com os proprietários e respetivo ID
     */
    public Map<String, Owner> getOwners() {
        if (owners == null) {
            throw new IllegalStateException("Dados não foram carregados. Certifique-se de carregar os dados antes.");
        }
        return owners;
    }

    /**
     * Obtém lista de sugestões de troca
     * @param graph Grafo sobre o qual se vai obter a lista de sugestões de troca
     * @return Lista com sugestões de trocas de propriedades
     */
    public List<Proposal> getExchSuggestions(SimpleGraph<Owner, DefaultEdge> graph){
        if(propertiesLoader == null){
            throw new IllegalStateException("Dados não foram carregados. Certifique-se de carregar os dados antes.");
        }
        return propertiesLoader.exchSuggestions(graph);
    }


    //fazer e obter o grafo dos owners para as trocas

    /**
     * Faz o grafo dos proprietários
     * @param owners Mapa com os proprietários e respetivos ID
     */
    public void generateOwnersGraph(Map<String, Owner> owners) {
        graphOwners = new Graph<>();
        graphOwners.createGraph(owners);
    }

    /**
     *  Obtém o grafo dos proprietários
     * @return Grafo de proprietários
     */
    public SimpleGraph<Owner, DefaultEdge> getGraphOwners() {
        return graphOwners.getGraph();
    }
}
