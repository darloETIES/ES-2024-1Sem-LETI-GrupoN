package grupo.n.gestaodoterritorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Main {

    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle-1.1.csv";

        try{
            //ler os dados do CSV
            PropertiesLoader ploader = new PropertiesLoader(file);
            Map<String, Property> properties = ploader.readProperties();
            double v = ploader.averageAreaProp("Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)");
            System.out.println("here is the averageArea of a proprotie given the parish,county and district :" + v);
            //Criacao do grafo
            Graph graph = new Graph();
            graph.createGraph(properties);
            graph.printGraphStats();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
