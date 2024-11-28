package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.*;


public class PropertiesLoader {

    private String file;
    private List<Property> propertiesList;
    private List<Owner> ownerList;
    /**Construtor para leitura do ficheiro CSV*/
    public PropertiesLoader(String file) {
        this.file = file;
        this.propertiesList = new ArrayList<>();
        this.ownerList = new ArrayList<>();
    }

    /**
     * leitura de propriedades
     * @return
     * @throws Exception
     */
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

            //String owner = record.get("OWNER");
            String ownerId = record.get("OWNER");
            //acrescimo dos campos freguesia, concelho e ilha, nova versao no moodle
            String parish = record.get("Freguesia");
            String county = record.get("Municipio");
            String district = record.get("Ilha");
            //Instacia uma nova propriedade
            Property property = new Property(objectID, parID, parNumAsString, shapeLength, shapeArea, geometry, ownerId, parish, county, district);
            propertiesList.add(property);
            properties.put(objectID, property);
            for(Owner owner : ownerList){
              if(ownerList.contains(owner)){
                  owner.addToOwnerPropertyList(property);
              }
              else{
                  ownerList.add(new Owner(ownerId) );
                  owner.addToOwnerPropertyList(property);
              }
             }


            System.out.println(property);
            //System.out.println("Owner: " + ownerId + "Ter esta propriedade: " + property + "Lista: " + propertiesList);

        }

        return properties;
    }

    /**
     * cálculo da área média das propriedades de uma dada região
     * @param parish
     * @param county
     * @param district
     * @return
     */
    public double averageAreaProp(String parish, String county, String district) {
        double totalArea = 0;
        int count = 0;

        for (Property property : propertiesList) {
            boolean matchesParish = property.getParish().equals(parish);
            boolean matchesCounty =  property.getCounty().equals(county);
            boolean matchesDistrict = property.getDistrict().equals(district);

            if (matchesParish && matchesCounty && matchesDistrict) {
                totalArea += property.getShapeArea();
                count++;
            }
        }

        // Verifica se count é maior que 0 para evitar divisão por zero (mas no caso de ser 0, entra na condição)
        if (count == 0) {
            return 0; // Caso não haja propriedades que satisfaçam os critérios
        }

        return totalArea / count;

    }
    public double averagePropAreaByOwner(String parish, String county, String district, String owner) {
        double totalArea = 0;
        int count = 0;
        for (Property property1 : propertiesList) {
            for (Property property2 : propertiesList) {
                //if para evitar propriedades repetidas e simétricas( ou seja se propA e adjacente a propB nao e necessario verificar se propB e adjacente a propA
                if (!property1.getObjectId().equals(property2.getObjectId()) && Integer.parseInt(property1.getObjectId())< Integer.parseInt(property2.getObjectId())) {
                    //verificar que as propriedades cumprem os requisitos (mesmo dono e area geografica de input)
                    boolean matchesParish1 = property1.getParish().equals(parish);
                    boolean matchesCounty1 =  property1.getCounty().equals(county);
                    boolean matchesDistrict1 = property1.getDistrict().equals(district);
                    boolean matchesOwner1 = property1.getOwner().equals(owner);

                    boolean matchesParish2 = property2.getParish().equals(parish);
                    boolean matchesCounty2 = property2.getCounty().equals(county);
                    boolean matchesDistrict2 = property2.getDistrict().equals(district);
                    boolean matchesOwner2 = property2.getOwner().equals(owner);
                    Geometry g1 = property1.getGeometry();
                    Geometry g2 = property2.getGeometry();
                    boolean intersects = g1.intersects(g2);

                        if (matchesParish1 && matchesCounty1 && matchesDistrict1 && matchesOwner1 && matchesParish2 && matchesCounty2 && matchesOwner2 && matchesDistrict2 && intersects ) {
                        //se as propriedades forem do mesmo dono e area geografica e se tambem forem adjacentes
                        totalArea += property1.getShapeArea() + property2.getShapeArea();
                        count++; //sendo adjacentes soma se a contagem apenas uma unidade ja que o objetivo e contar propriedades adjacentes como sendo uma
                        }else if(matchesParish1 && matchesCounty1 && matchesDistrict1 && matchesOwner1 && matchesParish2 && matchesCounty2 && matchesOwner2 && matchesDistrict2){
                        //se as propriedades forem do mesmo dono e area geografica, mas nao forem adjacentes
                        totalArea += property1.getShapeArea() + property2.getShapeArea();
                        count+=2; //nao sendo adjacente soma se a contagem duas propriedades
                        }
                }

            }


        }
        if (count == 0) {
            return 0;
        }
        return totalArea / count;

    }


}
