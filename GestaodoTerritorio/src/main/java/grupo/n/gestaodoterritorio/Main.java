package grupo.n.gestaodoterritorio;

public class Main {

    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle.csv"; // Caminho para o ficheiro CSV
        LoadRusticProperties propLoader = new LoadRusticProperties(file);
    }
}
