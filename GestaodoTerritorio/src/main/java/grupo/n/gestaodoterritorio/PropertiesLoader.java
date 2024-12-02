package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.FileReader;
import java.util.*;


public class PropertiesLoader {


    private String file;
    private List<Property> propertiesList;
    private List<Owner> ownerList;
    //listas de apoio para sugestao de trocas
    private List<Property> AUXsourceList;
    private List<Property> AUXtargetList;
    private List<Proposal> suggestions; //lista para devolver todas as sugestoes de troca



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

            Owner owner = new Owner("-1");
            for(Owner ownerAux : ownerList){
                if(ownerAux.getOwnerID().equals(ownerId)){
                   owner = ownerAux;
                   break;
                } else if (!ownerAux.getOwnerID().equals(ownerId) && ownerList.getLast().equals(ownerAux)) {
                    owner = new Owner(ownerId);
                    ownerList.add(owner);
                }

            }

            //Instacia uma nova propriedade
            Property property = new Property(objectID, parID, parNumAsString, shapeLength, shapeArea, geometry, owner, parish, county, district);
            propertiesList.add(property);
            properties.put(objectID, property);
            owner.addToOwnerPropertyList(property);

            System.out.println(property);
            //System.out.println("Owner: " + ownerId + "Ter esta propriedade: " + property + "Lista: " + propertiesList);

        }

        return properties;
    }
    public Map<String, Owner> readOwners() throws Exception /*Exception para que verifique se o caminho do ficheiro*/ {

        Map<String, Owner> Owners = new HashMap<>();
        Map<String, Property> properties = readProperties();
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


            String ownerId = record.get("OWNER");

            Owner Owner = new Owner(ownerId);
            Owners.put(ownerId, Owner);
            for (Property property : properties.values()) {
                String ownerID = property.getOwner().getOwnerID(); // Assume the Property has an Owner ID
                Owner owner = Owners.get(ownerID);

                // If the owner doesn't exist, create a new Owner and associate the property
                if (owner == null) {
                    owner = new Owner(ownerID);  // Create new Owner if it doesn't exist
                    Owners.put(ownerID, owner);
                }
            }
        }
        return Owners;
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
    public double averagePropAreaByOwner(String parish, String county, String district, String ownerID) {
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
                    boolean matchesOwner1 = property1.getOwner().getOwnerID().equals(ownerID);

                    boolean matchesParish2 = property2.getParish().equals(parish);
                    boolean matchesCounty2 = property2.getCounty().equals(county);
                    boolean matchesDistrict2 = property2.getDistrict().equals(district);
                    boolean matchesOwner2 = property2.getOwner().getOwnerID().equals(ownerID);
                    Geometry g1 = property1.getGeometry();
                    Geometry g2 = property2.getGeometry();
                    boolean intersects = g1.intersects(g2);

                    if (matchesParish1 && matchesCounty1 && matchesDistrict1 && matchesOwner1 && matchesParish2 && matchesCounty2 && matchesOwner2 && matchesDistrict2 && intersects ) {
                        //se as propriedades forem do mesmo dono e area geografica e se tambem forem adjacentes
                        totalArea += property1.getShapeArea() + property2.getShapeArea();
                        count++; //sendo adjacentes soma se a contagem apenas uma unidade ja que o objetivo e contar propriedades adjacentes como sendo uma
                    }else if(matchesParish1 && matchesCounty1 && matchesDistrict1 && matchesOwner1 && matchesParish2 && matchesCounty2 && matchesOwner2 && matchesDistrict2 && !intersects){
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

    public List<Proposal> exchSuggestions(SimpleGraph<Owner, DefaultEdge> graph){
        /*1º passo - passar por todas as adjacências do grafo de proprietários
        2º passo- para cada adjacência, verificar (entre todas as duplas de propriedades possíveis, sendo uma de cada proprietário) se dois proprietários sao vizinhos em dois sítios
        3º passo - tendo as duplas de vizinhança, ver se nenhum proprietário perde 5% de área face a sua situação original
        4º passo - exprimir essa sugestao */
        for (DefaultEdge edge : graph.edgeSet()) { //1ºPasso
            Owner source = graph.getEdgeSource(edge);// Obter os vértices conectados pela aresta (ou seja dois proprietarios com relacao de vizinhanca)
            Owner target = graph.getEdgeTarget(edge);
            int nr_propriedades_vizinhas = 0; //auxiliar para contar quantas vezes dois proprietarios sao vizinhos com diferentes propriedades
            List<Property> sourceList = source.getOwnerPropertyList();
            List<Property> targetList = target.getOwnerPropertyList();
            double originalSourceArea = 0;
            double originalTargetArea = 0;
            //Limpa-se as listas de prop aux, para que o calculo efetuado para um novo caso de proprietarios
            // seja feito sem as propriedades selecionadas na iteracao de proprietarios anterior
            AUXsourceList.clear();
            AUXtargetList.clear();
            for(Property property1 : sourceList){ //2ºPasso
                for(Property property2 : targetList){
                        Geometry g1 = property1.getGeometry();
                        Geometry g2 = property2.getGeometry();
                        boolean intersects = g1.intersects(g2);
                        if (intersects) {
                            nr_propriedades_vizinhas++;
                            AUXsourceList.add(property1);//guardar quais as propriedades vizinhas para poder fazer calculos com as suas areas
                            AUXtargetList.add(property2);
                            if(nr_propriedades_vizinhas==2){ //se forem vizinhos em dois sitios diferentes podemos sugerir uma troca
                                break;
                            }
                        }
                }
            }
            if(nr_propriedades_vizinhas ==2 ){//se dois proprietarios sao vizinhos em dois sitios ver entao se nao existem perdas maiores q 0.05, para a troca ser viavel
                for(Property property1 : AUXsourceList){
                    originalSourceArea += property1.getShapeArea();
                }
                for(Property property2 : AUXtargetList){
                    originalTargetArea += property2.getShapeArea();
                }
                if(fairExch(AUXsourceList, originalSourceArea, AUXtargetList, originalTargetArea)){ //3ºPasso
                    Proposal sug = new Proposal(source, target, AUXsourceList, AUXtargetList); //4ºPasso
                    suggestions.add(sug);
                }
            }
        }
        return suggestions;
    }

    //Metodo para verificar que em nenhum dos casos nenhum proprietario perde mais do que 5% de area de forma a troca ser justa
    public boolean fairExch(List<Property> sourceList, double sourceArea, List<Property> targetList, double targetArea){
        double newSourceAreaExch1 = sourceList.get(0).getShapeArea() + targetList.get(0).getShapeArea() ;
        double fair1 = Math.abs((newSourceAreaExch1-sourceArea)/sourceArea); //tera q ser menor q 0.05
        double newTargetAreaExch1 = sourceList.get(1).getShapeArea() + targetList.get(1).getShapeArea() ;
        double fair2 = Math.abs((newTargetAreaExch1-targetArea)/targetArea);

        double newSourceAreaExch2 = sourceList.get(1).getShapeArea() + targetList.get(1).getShapeArea() ;
        double fair3 = Math.abs((newSourceAreaExch2-sourceArea)/sourceArea);
        double newTargetAreaExch2 = sourceList.get(0).getShapeArea() + targetList.get(0).getShapeArea() ;
        double fair4 = Math.abs((newTargetAreaExch2-targetArea)/targetArea);

        boolean isFair = (fair1 < 0.05 && fair2 < 0.05 && fair3 < 0.05 && fair4 < 0.05);
        return isFair;
    }


}