package grupo.n.gestaodoterritorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle.csv";

        try{
            //ler os dados do CSV
            PropertiesLoader ploader = new PropertiesLoader(file);
            Map<String, Property> properties = ploader.readProperties();

            //Criacao do grafo
            Graph graph = new Graph();
            graph.createGraph(properties);
            graph.printGraphStats();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
