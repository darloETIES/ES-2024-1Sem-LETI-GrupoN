package grupo.n.gestaodoterritorio;

import grupo.n.gestaodoterritorio.models.Graph;
import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.models.PropertiesLoader;
import grupo.n.gestaodoterritorio.models.Property;

import java.util.Map;



public class Main {

    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle-1.1.csv";

        try{
            //ler os dados do CSV
            PropertiesLoader ploader = new PropertiesLoader(file);
            System.out.println("Ler propriedades");
            Map<String, Property> properties = ploader.readProperties();
            System.out.println("Ler proprietários");
            Map<String, Owner> owners = ploader.readOwners();


            //Criacao do grafo
            // Grafo de propriedades
            Graph<Property> propertyGraph = new Graph<>();
            propertyGraph.createGraph(properties);
            propertyGraph.printGraphStats();
            propertyGraph.drawGraph("Properties");

            // Grafo de proprietários
            Graph<Owner> ownerGraph = new Graph<>();
            ownerGraph.createGraph(owners);
            ownerGraph.printGraphStats();
            ownerGraph.drawGraph("Owners");

            //Calculo da area geografica selecionada pelo utilizador
            String parish = "Arco da Calheta";
            String county = "Calheta";
            String district = "Ilha da Madeira (Madeira)";
            String ownerID = "5";

            double averageAreaProp = ploader.averageAreaProp(parish, county, district);
            System.out.println("Average Area from " + parish + ", " + county + ", " + district + " -> " + averageAreaProp);

            double averageAreaOwners = ploader.averagePropAreaByOwner(parish, county, district, ownerID);
            System.out.println("Average Area from " + parish + ", " + county + ", " + district + " (OWNERID = "+ ownerID + ") -> " + averageAreaOwners);


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}