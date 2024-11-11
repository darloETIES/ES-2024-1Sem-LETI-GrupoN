package grupo.n.gestaodoterritorio;

import org.apache.commons.csv.CSVFormat;

public class LoadRusticProperties
{
    public static void main(String[] args){
        String file = "GestaodoTerritorio/src/main/resources/Madeira-Moodle.csv"; // Caminho para o ficheiro CSV
        Propriedade propriedade = new Propriedade(file);
    }
}
