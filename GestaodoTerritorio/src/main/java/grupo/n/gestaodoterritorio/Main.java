package grupo.n.gestaodoterritorio;

import java.util.Map;



public class Main {

    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle-1.1.csv";

        try{
            //ler os dados do CSV
            PropertiesLoader ploader = new PropertiesLoader(file);
            Map<String, Property> properties = ploader.readProperties();

            //Criacao do grafo
            Graph graph = new Graph();
            graph.createGraph(properties);
            graph.printGraphStats();

            //Calculo da area geografica selecionada pelo utilizador
            String parish = "Arco da Calheta";
            String county = "Calheta";
            String district = "Ilha da Madeira (Madeira)";
            double averageAreaProp = ploader.averageAreaProp(parish, county, district);
            System.out.println("Average Area from " + parish + ", " + county + ", " + district + " -> " + averageAreaProp);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
