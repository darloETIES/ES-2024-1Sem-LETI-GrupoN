package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import grupo.n.gestaodoterritorio.models.Owner;
import grupo.n.gestaodoterritorio.models.Property;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

    private static WKTReader wktReader; // Compartilhado por todos os testes
    private Geometry geometry;          // Inicializado para cada teste
    private Property p;          // Inicializado para cada teste

    @BeforeAll
    static void setUpClass() {
        wktReader = new WKTReader();
    }

    @BeforeEach
    void setUp() throws ParseException {
        String wktMultiPolygon = "MULTIPOLYGON (((299218.5203999998 3623637.4791, 299218.5033999998 3623637.4715, 299218.04000000004 3623638.4800000004, 299232.7400000002 3623644.6799999997, 299236.6233999999 3623637.1974, 299236.93709999975 3623636.7885999996, 299238.04000000004 3623633.4800000004, 299222.63999999966 3623627.1799999997, 299218.5203999998 3623637.4791)))";
        geometry = wktReader.read(wktMultiPolygon);

        //Confirmação que que a geometria é do tipo MultiPolygon
        assertInstanceOf(MultiPolygon.class, geometry);

        //Criação do objeto Property
        Owner o = new Owner("93");
        p = new Property("1", "7343148.0", "2996240000000", 57.2469341921808, 202.05981432070362, geometry, o, "Arco da Calheta", "Calheta", "Ilha da Madeira (Madeira)");

    }

    @Test
    void testPropertyConstructor() {
        assertEquals("1", p.getObjectId());
        assertEquals("7343148.0", p.getParId());
        assertEquals("2996240000000", p.getParNum());
        assertEquals(57.2469341921808, p.getShapeLength());
        assertEquals(202.05981432070362, p.getShapeArea());
        assertEquals(geometry, p.getGeometry());
        assertEquals("93", p.getOwner().getOwnerID());
        assertEquals("Arco da Calheta", p.getParish());
        assertEquals("Calheta", p.getCounty());
        assertEquals("Ilha da Madeira (Madeira)", p.getDistrict());
    }

    @Test
    void testPropertySetters() throws ParseException {
        String wktMultiPolygon = "MULTIPOLYGON (((298724.1991999997 3623192.6094000004, 298724.3200000003 3623192.619999999, 298724.26999999955 3623185.7200000007, 298723.8854 3623185.681500001, 298723.8854 3623185.6338, 298717.2167999996 3623184.6405999996, 298716.2909000004 3623184.495100001, 298716.1699999999 3623184.5700000003, 298711.51999999955 3623184.17, 298709.1414000001 3623183.7961999997, 298708.48000000045 3623183.3200000003, 298705.6799999997 3623183.2200000007, 298704.5800000001 3623183.3200000003, 298703.98000000045 3623184.119999999, 298703.48000000045 3623190.7200000007, 298704.0525000002 3623190.7905, 298704.0488999998 3623190.8441000003, 298705.574 3623190.9777000006, 298709.98000000045 3623191.5199999996, 298710.0937999999 3623191.3737000003, 298724.1991999997 3623192.6094000004)))";
        Geometry gm = wktReader.read(wktMultiPolygon);
        p.setObjectId("2");
        assertEquals("2", p.getObjectId());
        p.setParId("7344660.0");
        assertEquals("7344660.0", p.getParId());
        p.setParNum("2996220000000");
        assertEquals("2996220000000", p.getParNum());
        p.setShapeLength(55.63800662596267);
        assertEquals(55.63800662596267, p.getShapeLength());
        p.setShapeArea(151.76387471712783);
        assertEquals(151.76387471712783, p.getShapeArea());
        p.setGeometry(gm);
        assertEquals(gm, p.getGeometry());
        p.setParish("Arco da Calheta");
        assertEquals("Arco da Calheta", p.getParish());
        p.setCounty("Calheta");
        assertEquals("Calheta", p.getCounty());
        p.setDistrict("Ilha da Madeira (Madeira)");
        assertEquals("Ilha da Madeira (Madeira)", p.getDistrict());
    }

    @Test
    void testPropertyToString(){
        String expected = "1";
        assertEquals(expected, p.toString());
    }

}