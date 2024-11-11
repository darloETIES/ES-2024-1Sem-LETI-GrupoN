package grupo.n.gestaodoterritorio;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.csv.CSVFormat;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadRusticProperties
{
    private static List<Propriedade> propriedades = new ArrayList<Propriedade>();

    public LoadRusticProperties(String file){
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            List<String[]> records = csvReader.readAll(); // Lê todos os parâmetros do ficheiro

            for (String[] record : records) {
                for (String field : record) {
                    System.out.print(field + " | "); // Imprime cada campo do registo separado por "|"
                }
                System.out.println();
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }





}
