package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

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

        int c = 0; //REMOVER

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

            String ownerId = record.get("OWNER");
            //acrescimo dos campos freguesia, concelho e ilha, nova versao no moodle
            String parish = record.get("Freguesia");
            String county = record.get("Municipio");
            String district = record.get("Ilha");

            Owner owner = null;
            for (Owner o : ownerList) {
                if (o.getOwnerID().equals(ownerId)) {
                    owner = o;
                    break;
                }
            }

            if (owner == null) {
                // Se o proprietário não existe, cria um novo e adiciona à lista
                owner = new Owner(ownerId);
                ownerList.add(owner);
            }

            //Validar os registos (por exemplo: ignorar registos onde a area seja 0 ou a freguesia seja "NA", etc.)
            if(shapeLength == 0 || shapeArea == 0 || parish.equals("NA") || county.equals("NA") || district.equals("NA")) {
                break;
            }

            //Instacia uma nova propriedade
            Property property = new Property(objectID, parID, parNumAsString, shapeLength, shapeArea, geometry, owner, parish, county, district);
            propertiesList.add(property);
            properties.put(objectID, property);

            // Associa a propriedade ao proprietário
            owner.addToOwnerPropertyList(property);


            System.out.println(property);
            //System.out.println(objectID + " | " + owner);


            //System.out.println("Owner: " + ownerId + "Ter esta propriedade: " + property + "Lista: " + propertiesList);

        }
        return properties;
    }
    public Map<String, Owner> readOwners() throws Exception /*Exception para que verifique se o caminho do ficheiro*/ {

        // Map to store the owners
        Map<String, Owner> owners = new HashMap<>();

        // Load properties first
        Map<String, Property> properties = readProperties();

        // Iterate over all properties to associate them with their respective owners
        for (Property property : properties.values()) {
            String ownerId = property.getOwner().getOwnerID();

            // Retrieve or create the Owner
            Owner owner = owners.computeIfAbsent(ownerId, Owner::new);

            // Add the property to the owner's property list
            owner.addToOwnerPropertyList(property);
        }

        // Return the complete map of owners
        return owners;
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
            boolean matchesParish = property.getParish().trim().equalsIgnoreCase(parish.trim());
            boolean matchesCounty = property.getCounty().trim().equalsIgnoreCase(county.trim());
            boolean matchesDistrict = property.getDistrict().trim().equalsIgnoreCase(district.trim());

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
    public double averagePropAreaByOwner(String parish, String county, String district, String ownerID) {
        double totalArea = 0;
        int count = 0;

        //Instancia de um novo obj Owner
        Owner o = new Owner("-1");
        for(Owner owner : ownerList) { //foreach para encontrar o owner com o ownerID
            if (owner.getOwnerID().equals(ownerID)) {

                o = owner;
                System.out.println(o);
                break; //Sai do ciclo quando encontra
            }
        }

        int intersectCounter = 0; //num de vizinhanças entre propriedades do mesmo proprietario
        int propInGeoAreaCounter = 0;

        for (Property property1 : o.getOwnerPropertyList()) {

            boolean matchesParish1 = property1.getParish().trim().equalsIgnoreCase(parish.trim());
            boolean matchesCounty1 = property1.getCounty().trim().equalsIgnoreCase(county.trim());
            boolean matchesDistrict1 = property1.getDistrict().trim().equalsIgnoreCase(district.trim());



            if(matchesParish1 && matchesCounty1 && matchesDistrict1) {
                totalArea += property1.getShapeArea();
                propInGeoAreaCounter++;
                System.out.println("Parish: " + property1.getParish() + " == " + parish + " -> " + matchesParish1);
                System.out.println("County: " + property1.getCounty() + " == " + county + " -> " + matchesCounty1);
                System.out.println("District: " + property1.getDistrict() + " == " + district + " -> " + matchesDistrict1);
            }

            for (Property property2 : o.getOwnerPropertyList()) {
                //if para evitar propriedades repetidas e simétricas( ou seja se propA e adjacente a propB nao e necessario verificar se propB e adjacente a propA
                if (!property1.getObjectId().equals(property2.getObjectId()) && Integer.parseInt(property1.getObjectId())< Integer.parseInt(property2.getObjectId())) {
                    //verificar que as propriedades cumprem os requisitos (mesmo dono e area geografica de input)


                    boolean matchesParish2 = property2.getParish().trim().equalsIgnoreCase(parish.trim());
                    boolean matchesCounty2 = property2.getCounty().trim().equalsIgnoreCase(county.trim());
                    boolean matchesDistrict2 = property2.getDistrict().trim().equalsIgnoreCase(district.trim());

                    Geometry g1 = property1.getGeometry();
                    Geometry g2 = property2.getGeometry();
                    boolean intersects = g1.intersects(g2);

                    if (matchesParish1 && matchesCounty1 && matchesDistrict1 && matchesParish2 && matchesCounty2 && matchesDistrict2 && intersects ) {
                        //se as propriedades forem do mesmo dono e area geografica e se tambem forem adjacentes
                        //totalArea += property1.getShapeArea() + property2.getShapeArea();
                        //count++; //sendo adjacentes soma se a contagem apenas uma unidade ja que o objetivo e contar propriedades adjacentes como sendo uma
                        intersectCounter++;
                    }
                    else if(matchesParish1 && matchesCounty1 && matchesDistrict1 && matchesParish2 && matchesCounty2 && matchesDistrict2) {
                        //se as propriedades forem do mesmo dono e area geografica, mas nao forem adjacentes
                        //totalArea += property1.getShapeArea() + property2.getShapeArea();
                        //count+=2; //nao sendo adjacente soma se a contagem duas propriedades
                    }

                }

            }


        }


        //num de propriedades do proprietario (conforme os criterios) =
        // = num de propriedades do proprietario na area geografica - num de vizinhanças entre essas propriedades
        int counter = propInGeoAreaCounter-intersectCounter;

        if (counter == 0) {
            return 0;
        }
        System.out.println(o.getOwnerPropertyList().size());
        System.out.println(intersectCounter);
        System.out.println(counter);


        return totalArea / counter;

    }

    /*
    public double averagePropAreaByOwner(String parish, String county, String district, String ownerId) {
        // Filtrar propriedades que atendem aos critérios básicos
        List<Property> filteredProperties = new ArrayList<>();
        for (Property property : propertiesList) {
            boolean matchesParish = property.getParish().equals(parish);
            boolean matchesCounty = property.getCounty().equals(county);
            boolean matchesDistrict = property.getDistrict().equals(district);
            boolean matchesOwner = property.getOwner().getOwnerID().equals(ownerId);

            if (matchesParish && matchesCounty && matchesDistrict && matchesOwner) {
                filteredProperties.add(property);
            }
        }

        // Total da área
        double totalArea = 0;

        // Usar um Set para evitar duplicação de contagem de propriedades adjacentes
        Set<Property> uniqueProperties = new HashSet<>();

        // Verificar interseções e calcular a área
        for (int i = 0; i < filteredProperties.size(); i++) {
            Property property1 = filteredProperties.get(i);

            // Adicionar a propriedade atual ao conjunto de propriedades únicas
            uniqueProperties.add(property1);

            // Verificar adjacência com outras propriedades
            for (int j = i + 1; j < filteredProperties.size(); j++) {
                Property property2 = filteredProperties.get(j);

                // Se forem adjacentes, ambas são contadas como uma só
                if (property1.getGeometry().intersects(property2.getGeometry())) {
                    uniqueProperties.add(property2); // Garante que não há duplicatas
                }
            }

            // Somar a área da propriedade atual
            totalArea += property1.getShapeArea();
        }

        // Número total de propriedades (considerando adjacências como uma)
        int count = uniqueProperties.size();

        // Retorna a média
        return count == 0 ? 0 : totalArea / count;
    }

*/
}