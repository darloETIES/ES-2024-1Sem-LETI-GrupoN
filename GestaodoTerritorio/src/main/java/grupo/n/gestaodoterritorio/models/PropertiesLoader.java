package grupo.n.gestaodoterritorio.models;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import grupo.n.gestaodoterritorio.comparators.ComparatorPairProperty;
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
    private List<Proposal> suggestions; //lista para devolver todas as sugestoes de troca
    private List<PairProperty> pairs; //guardar pares de propriedades e a sua area

    /**Construtor para leitura do ficheiro CSV*/
    public PropertiesLoader(String file) {
        this.file = file;
        this.propertiesList = new ArrayList<>();
        this.ownerList = new ArrayList<>();
        this.suggestions = new ArrayList<>(); // Inicializando a lista de sugestões
        this.pairs = new ArrayList<>(); // Inicializando a lista de pares de propriedades
    }

    /**
     * Leitura de propriedades do ficheiro dado
     * @return Mapa com a propriedade lida e respetivo ID
     * @throws Exception
     */
    public Map<String, Property> readProperties() throws Exception /*Exception para que verifique se o caminho do ficheiro*/ {

        Map<String, Property> properties = new HashMap<>();//Mapa para guardar as propriedades lidas do ficheiro CSV, chave->ObjectID, Valor->Propriedade
        GeometryFactory gf = new GeometryFactory(); //Faz parte da biblioteca JTS e será usada para criar objetos geométricos
        WKTReader reader = new WKTReader(gf);//É o interpretador de GeometryFactory, converte geometrias de formato WKT em objetos Geometry

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
        }
        return properties;
    }

    /**
     * Leitura de proprietários através do mapa de propriedades
     * @return Mapa com o ID do proprietário e o proprietário
     * @throws Exception
     */
    public Map<String, Owner> readOwners() throws Exception /*Exception para que verifique se o caminho do ficheiro*/ {

        // Mapa para guardar os proprietários
        Map<String, Owner> owners = new HashMap<>();

        // Carregamento das propriedades
        Map<String, Property> properties = readProperties();

        //Iteração sobre todas as propriedades para as associar com os respetivos donos
        for (Property property : properties.values()) {
            String ownerId = property.getOwner().getOwnerID();

            //Obtem ou cria o proprietário
            Owner owner = owners.computeIfAbsent(ownerId, Owner::new);

            // Adiciona o proprietário à lista de proprietários
            owner.addToOwnerPropertyList(property);
        }

        // Retorna o mapa completo de proprietários
        return owners;
    }
    /**
     * Cálculo da área média das propriedades de uma dada área geográfica
     * @param parish Freguesia da propriedade
     * @param county Concelho da propriedade
     * @param district Distrito da propriedade
     * @return Área média das propriedades da área geográfica
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

    /**
     * Área média das propriedades de um proprietário numa dada área geográfica contando que duas propriedades vizinhas contam como uma
     * @param parish Freguesia da propriedade
     * @param county Concelho da propriedade
     * @param district Distrito da propriedade
     * @param ownerID ID do proprietário
     * @return Área média das propriedades de um proprietário numa dada área geográfica
     */
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
                        intersectCounter++;
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

    /**
     * Método para dado um grafo de proprietários gerar sugestões de trocas para facilitar o aumento de área média das propriedades de um proprietário
     * @param graph Grafo de proprietários sobre o qual são geradas as sugestões
     * @return Lista de sugestões de troca
     */
    public List<Proposal> exchSuggestions(SimpleGraph<Owner, DefaultEdge> graph){
        /*1º passo - passar por todas as adjacências do grafo de proprietários
        2º passo- para cada adjacência, verificar (entre todas as duplas de propriedades possíveis, sendo uma de cada proprietário) se dois proprietários sao vizinhos em dois sítios
        3º passo - tendo as duplas de vizinhança, ver se nenhum proprietário perde 5% de área face a sua situação original
        4º passo - exprimir essa sugestao */
        suggestions.clear();
        for (DefaultEdge edge : graph.edgeSet()) { //1ºPasso
            Owner source = graph.getEdgeSource(edge);// Obter os vértices conectados pela aresta (ou seja dois proprietarios com relacao de vizinhanca)
            Owner target = graph.getEdgeTarget(edge);
            int nr_propriedades_vizinhas = 0; //auxiliar para contar quantas vezes dois proprietarios sao vizinhos com diferentes propriedades
            List<Property> sourceList = source.getOwnerPropertyList();
            List<Property> targetList = target.getOwnerPropertyList();
            pairs.clear();
            for(Property property1 : sourceList){ //2ºPasso
                for(Property property2 : targetList){
                        Geometry g1 = property1.getGeometry();
                        Geometry g2 = property2.getGeometry();
                        boolean intersects = g1.intersects(g2);
                        if (intersects) {
                            nr_propriedades_vizinhas++;
                            pairs.add(new PairProperty(property1, property2));
                        }
                }
            }
            //ORDENAR AREAS das adjacencias
            ComparatorPairProperty comp = new ComparatorPairProperty();
            Collections.sort(pairs, comp);

            //Ja ordenado, percorremos a lista a partir das maiores areas conjuntas e propomos a troca para a troca justa com maiores areas de adjacencias
            if(nr_propriedades_vizinhas>1){ //temos que ter pelo menos duas adjacencias entre os 2 proprietarios
                int i =0;
                boolean hasSuggested = false;
                while (i + 1 < pairs.size() && !hasSuggested) { // Verificar se i + 1 é válido
                    if(fairExch(pairs.get(i).getProperty1(),pairs.get(i+1).getProperty1(),pairs.get(i).getProperty2(),pairs.get(i+1).getProperty2())){
                        Proposal sug = new Proposal(source,target,pairs.get(i).getProperty1(),pairs.get(i+1).getProperty1(),pairs.get(i).getProperty2(),pairs.get(i+1).getProperty2());
                        suggestions.add(sug);
                        hasSuggested = true;
                    }
                    i++;
                }
            }

        }
        return suggestions;
    }

    /**
     * Verifica se uma troca de propriedades é justa
     * @param sp1 Propriedade pertencente ao primeiro dono
     * @param sp2 Propriedade pertencente ao primeiro dono
     * @param tp1 Propriedade pertencente ao segundo dono
     * @param tp2 Propriedade pertencente ao segundo dono
     * @return Boolean sobre a justiça de uma troca de propriedades
     */
    public boolean fairExch(Property sp1, Property sp2, Property tp1, Property tp2){
        double originalSourceArea = sp1.getShapeArea()+ sp2.getShapeArea();
        double originalTargetArea = tp1.getShapeArea()+ tp2.getShapeArea();

        double newSourceAreaExch1 = sp1.getShapeArea() + tp1.getShapeArea();
        double fair1 = Math.abs((newSourceAreaExch1-originalSourceArea)/originalSourceArea);
        double newTargetAreaExch1 = sp2.getShapeArea() + tp2.getShapeArea();
        double fair2 = Math.abs((newTargetAreaExch1-originalTargetArea)/originalTargetArea);

        double newSourceAreaExch2 = sp2.getShapeArea() + tp2.getShapeArea();
        double fair3 = Math.abs((newSourceAreaExch2-originalSourceArea)/originalSourceArea);
        double newTargetAreaExch2 = sp1.getShapeArea() + tp1.getShapeArea();
        double fair4 = Math.abs((newTargetAreaExch2-originalTargetArea)/originalTargetArea);

        return (fair1 < 0.05 && fair2 < 0.05 && fair3 < 0.05 && fair4 < 0.05); //troca justa -> nao se pode perder mais do que 5%
    }

}