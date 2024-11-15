package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.*;

@Component
public class PropertiesLoader {

    private String file;

    /*Construtor para leitura do ficheiro CSV*/
    public PropertiesLoader(String file) {
        this.file = file;
    }

    public Map<String, Property> readProperties() throws Exception /*Exception para que verifique se o caminho do ficheiro*/ {
        /* Map<String, Property> properties = new HashMap<>();
         * Mapa para guardar as propriedades lidas do ficheiro CSV
        * Dois importantes identificadores:
        *   - Chave (String), representa o ObjectID
        *   - Valor (Propriedade, representa o objeta que encapsula todos os dados lidos da linha do ficheiro)
        *
        *  GeometryFactory gf = new GeometryFactory();
        * Faz parte da biblioteca JTS e será usada para criar objetos geométricos
        *
        *  WKTReader reader = new WKTReader(gf);
        * É o interpretador de GeometryFactory, converte geometrias de formato WKT em objetos Geometry
        *
        * Ex. de uso da linguagem WKT:
        * MULTIPOLYGON (((299218.5204 3623637.4791, ...)))
         */
        Map<String, Property> properties = new HashMap<>();
        GeometryFactory gf = new GeometryFactory();
        WKTReader reader = new WKTReader(gf);

        FileReader fileReader = new FileReader(file);

        /*Lê cada linha do ficheiro CSV e retorna como um objeto CSVRecord:*/
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withDelimiter(';') //delimitando com ';'
                .withFirstRecordAsHeader() //ignorando a primeira linha (como cabeçalho)
                .withIgnoreSurroundingSpaces()
                .withAllowMissingColumnNames() // Permitir colunas sem nome
                .parse(fileReader);

        for (CSVRecord record : records) {
            String objectID = record.get("OBJECTID");
            String parID = record.get("PAR_ID");

            String parNum = record.get("PAR_NUM");
            double parNumDouble = Double.parseDouble(parNum.replace(",", ".")); // Converte para double, caso tenha vírgula decimal
            String parNumAsString = String.format("%.0f", parNumDouble); // Formata como string sem notação científica

            double shapeLength = Double.parseDouble(record.get("Shape_Length"));
            double shapeArea = Double.parseDouble(record.get("Shape_Area"));

            //campo geometry [ MULTIPOLYGON(((...))) ]
            String wkt = record.get("geometry");
            Geometry geometry = reader.read(wkt);

            String owner = record.get("OWNER");

            //Instacia uma nova propriedade
            Property property = new Property(objectID, parID, parNumAsString, shapeLength, shapeArea, geometry, owner);
            properties.put(objectID, property);

            System.out.println(property);

            //TEMPORARIO!!!! faz iterações até determinada propriedade (dado um erro de formatação no excel)
            if(objectID.equals("9322"))
                break;
        }

        return properties;
    }


}
