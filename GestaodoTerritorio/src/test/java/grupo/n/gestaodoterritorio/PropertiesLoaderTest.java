package grupo.n.gestaodoterritorio;


import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.models.PropertiesLoader;
import grupo.n.gestaodoterritorio.models.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class PropertiesLoaderTest {
    private String fileStr;
    private PropertiesLoader pLoader;
    private Map<String, Property> props;


    @BeforeEach
    void setUp() throws FileNotFoundException, URISyntaxException {
        URL resourceUrl = getClass().getClassLoader().getResource("Madeira-Moodle-1.1.csv");
        if (resourceUrl != null) {
            String filePath = resourceUrl.getPath();  // Aqui usamos getPath() em vez de toString()
            fileStr = filePath;  // Caminho da string
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            pLoader = new PropertiesLoader(fileStr);
        } else {
            System.out.println("Arquivo não encontrado!");
        }
    }

    @Test
    void readProperties() throws Exception {
        props = pLoader.readProperties();

        //Verifica que o mapa não está vazio
        assertFalse(props.isEmpty(), "O mapa não está vazio!");

        //Verifica se existe determinada propriedade por objectID
        assertTrue(props.containsKey("1"), "Deve conter a propriedade com ObjectID=1");

        //Verfica se a propriedade tem os valores esperados
        Property p1 = props.get("1");
        assertNotNull(p1, "A propriedade não pode ser nula!");
        assertEquals("1", p1.getObjectId(), "O ObjectID da propriedade tem que ser 1");
        assertEquals("Arco da Calheta", p1.getParish(), "A freguesia da propriedade tem que ser 'Arco da Calheta'");
        assertEquals("Calheta", p1.getCounty(), "O concelho/municipio da propriedade tem que ser 'Calheta'");
        assertEquals("Ilha da Madeira (Madeira)", p1.getDistrict(), "O distrito/ilha da propriedade tem que ser 'Ilha da Madeira (Madeira)'");
        assertTrue(p1.getShapeArea() > 0, "A area da propriedade tem que ser maior que 0");
        assertTrue(p1.getShapeLength() > 0, "O comprimento da propriedade tem que ser maior que 0");
    }

    @Test
    void testAverageAreaProp() throws Exception {
        pLoader.readProperties();

        //Média da area geografica
        double averageArea = pLoader.averageAreaProp("Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)");

        //Verfica que é maior que zero
        assertTrue(averageArea > 0, "A média da área deve ser maior que 0");

        //Caso em que a area geografica nao existe, sendo que a area tem de resultar em zero
        double averageAreaEmpty = pLoader.averageAreaProp("Avenidas Novas", "Lisboa", "Lisboa");
        assertEquals(0, averageAreaEmpty, "Sendo que a area geografica não existe, a área terá que resultar zero");
    }
    @Test
    void testAverageAreabyOwner() throws Exception {
        pLoader.readOwners();
        Owner o1=new Owner("50");
        String o1s=o1.getOwnerID();
        double averageArea = pLoader.averagePropAreaByOwner("Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)",o1s);
        assertTrue(averageArea > 0, "A média da área deve ser maior que 0");
        double averageAreaEmpty = pLoader.averagePropAreaByOwner("Avenidas Novas", "Lisboa", "Lisboa",o1s);
        assertEquals(0, averageAreaEmpty, "Sendo que a area geografica não existe, a área terá que resultar zero");
    }
}