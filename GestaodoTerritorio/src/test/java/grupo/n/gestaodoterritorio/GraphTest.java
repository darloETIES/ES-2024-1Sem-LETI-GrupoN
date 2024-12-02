package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private Graph<Property> graph;
    private static WKTReader wktReader;
    private Property p1, p2, p3;
    private Geometry g1, g2, g3;

    @BeforeAll
    static void setUpClass(){
        wktReader = new WKTReader();
    }

    @BeforeEach
    void setUp() throws ParseException {
        graph = new Graph<>();

        //NOTA: As propriedades foram verificadas previamente sobre as suas adjacências
        //p1 e p3 têm vizinhança com p2
        //no entanto, p1 e p3 não têm qualquer vizinhança entre si
        //
        // Representação em grafo:
        // (p1)--(p2)--(p3)

        String wktMP_g1 = "MULTIPOLYGON (((297564.6500000004 3621571.6808, 297564.75 3621571.76, 297566.75 3621579.16, 297569.6810999997 3621578.244000001, 297574.9500000002 3621573.16, 297575.02359999996 3621573.391899999, 297583.14159999974 3621562.4713000003, 297581.4797 3621560.9661, 297581.5499999998 3621560.960000001, 297581.1500000004 3621557.960000001, 297573.9500000002 3621552.5600000005, 297572.95249999966 3621549.424900001, 297572.32739999983 3621549.189200001, 297572.25 3621549.16, 297572.2419999996 3621549.1619000006, 297566.0454000002 3621547.1843, 297559.4079 3621545.8892, 297559.4275000002 3621545.9461000003, 297556.4500000002 3621545.76, 297555.3499999996 3621545.8599999994, 297558.07830000017 3621549.4978, 297557.95079999976 3621549.4507999998, 297559.1358000003 3621551.4560000002, 297561.9500000002 3621550.26, 297563.4500000002 3621552.3599999994, 297562.3499999996 3621554.16, 297562.6500000004 3621556.26, 297562.41110000014 3621556.4089, 297563.06070000026 3621557.3455999997, 297563.0499999998 3621557.3599999994, 297564.0499999998 3621560.76, 297566.9500000002 3621565.460000001, 297566.25 3621567.960000001, 297564.6500000004 3621569.26, 297564.6500000004 3621571.6808)))";
        g1 = wktReader.read(wktMP_g1);
        Owner o1 = new Owner("56");
        p1 = new Property("8", "7350748.0", "2986220000000", 225.20073321609982, 1812.9340319167845, g1, o1, "Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)");

        String wktMP_g2 = "MULTIPOLYGON (((297586.4343999997 3621564.392000001, 297586.9848999996 3621561.339299999, 297587.1743999999 3621560.2885999996, 297587.3499999996 3621560.26, 297588.1074000001 3621557.3145000003, 297578.3499999996 3621551.460000001, 297573.82380000036 3621549.7534, 297572.95249999966 3621549.424900001, 297573.9500000002 3621552.5600000005, 297581.1500000004 3621557.960000001, 297581.5499999998 3621560.960000001, 297581.4797 3621560.9661, 297583.14159999974 3621562.4713000003, 297583.1500000004 3621562.460000001, 297583.6841000002 3621562.7742, 297585.8513000002 3621564.0490000006, 297586.4343999997 3621564.392000001)))";
        g2 = wktReader.read(wktMP_g2);
        Owner o2 = new Owner("20");
        p2 = new Property("15", "20746817.0", "2976210000000", 45.99013260047487, 77.94686062009467, g2, o2, "Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)");

        String wktMP_g3 = "MULTIPOLYGON (((297586.4343999997 3621564.392000001, 297588.03670000006 3621565.3345999997, 297588.06269999966 3621565.3160999995, 297588.1034000004 3621565.3737000003, 297588.25600000005 3621565.59, 297591.9480999997 3621570.8203999996, 297595.10170000046 3621575.780200001, 297597.71719999984 3621580.1851000004, 297607.1448999997 3621579.3827, 297607.30370000005 3621579.3692000005, 297608.25 3621578.960000001, 297609.25 3621561.26, 297605.25 3621560.5600000005, 297605.1676000003 3621560.6731000002, 297605.1500000004 3621560.5600000005, 297602.75 3621560.3599999994, 297598.0499999998 3621560.66, 297594.9500000002 3621559.5600000005, 297588.37200000044 3621557.5667000003, 297588.3499999996 3621557.460000001, 297588.1074000001 3621557.3145000003, 297587.3499999996 3621560.26, 297587.1743999999 3621560.2885999996, 297586.9848999996 3621561.339299999, 297586.4343999997 3621564.392000001)))";
        g3 = wktReader.read(wktMP_g3);
        Owner o3 = new Owner("31");
        p3 = new Property("16", "20746818.0", "2976220000000", 77.38560252506595, 347.829668448073, g3, o3, "Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)");

    }

    @Test
    void testCreateGraph() {
        Map<String, Property> props = new HashMap<>();
        props.put(p1.getObjectId(), p1);
        props.put(p2.getObjectId(), p2);
        props.put(p3.getObjectId(), p3);

        graph.createGraph(props);

        //Verifica se printGraphStats() executa sem lançar nenhuma exceção
        assertDoesNotThrow(() -> graph.printGraphStats(), "Erro ao imprimir as estatísticas do grafo!");


    }

    @BeforeEach
    void setUpNoIntersectioTest() {
        graph = new Graph<>();
    }

    @Test
    void testNoIntersection() {
        Map<String, Property> props = new HashMap<>();
        props.put(p1.getObjectId(), p1);
        props.put(p3.getObjectId(), p3);
        graph.createGraph(props);
        System.out.println("Vizinhanças: " + graph.getGraph().edgeSet().size());
        //Verifica se não há arestas (0 vizinhanças)
        assertEquals(0, graph.getGraph().edgeSet().size());

    }

    @BeforeEach
    void setUpTestIntersection() {
        graph = new Graph<>();
    }

    @Test
    void testIntersection() {
        Map<String, Property> props = new HashMap<>();
        props.put(p1.getObjectId(), p1);
        props.put(p2.getObjectId(), p2);
        graph.createGraph(props);
        System.out.println("Vizinhanças: " + graph.getGraph().edgeSet().size());
        //Verifica se há 1 aresta entre as duas propriedades (1 vizinhança)
        assertEquals(1, graph.getGraph().edgeSet().size());
    }


}