package grupo.n.gestaodoterritorio;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.csv.CSVFormat;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class LoadRusticProperties {
    private static List<Propriedade> propriedades = new ArrayList<Propriedade>();
    private Graph<Propriedade, DefaultEdge> graph;
    private Map<String, Propriedade> object; //objeto, ou seja: proprietario, propriedade

    public LoadRusticProperties(String file) {
        graph = new SimpleGraph<>(DefaultEdge.class);
        object = new HashMap<>();
        loadProperties(file);
    }


    public void loadProperties(String file) {

        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader wktReader = new WKTReader(geometryFactory);

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            csvReader.readNext(); // Ignora a primeira linha (cabecalho com os parametros)

            List<String[]> records = csvReader.readAll(); // Le todos os parametros do ficheiro



            for (String[] record : records) {
                String objectId = record[0];
                String parId = record[1];
                String parNum = record[2];
                double shapeLength = Double.parseDouble(record[3]);
                double shapeArea = Double.parseDouble(record[4]);
                String geometryWTK = record[5];
                int owner = Integer.parseInt(record[6]);

                MultiPolygon geometry = null;
                try {
                    geometry = (MultiPolygon) wktReader.read(geometryWTK);
                } catch (com.vividsolutions.jts.io.ParseException e) {
                    System.out.println("Erro ao ler " + objectId + ": " + e.getMessage());
                }
                //System.out.println(Arrays.toString(record));
                Propriedade propriedade = new Propriedade(objectId, parId, parNum, shapeLength, shapeArea, geometry, owner);
                propriedades.add(propriedade);
            }


        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Propriedade> getPropriedades() {
        return propriedades;
    }
}
