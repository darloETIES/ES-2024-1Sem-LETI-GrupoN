package grupo.n.gestaodoterritorio;

import java.util.List;

public class Main {

    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle.csv"; // Caminho para o ficheiro CSV
        LoadRusticProperties propLoader = new LoadRusticProperties(file);
        List<Propriedade> propriedades= propLoader.getPropriedades();
        Grafo Graphprop = new Grafo();
        Graphprop.construirGrafo(propriedades);
    }
}
