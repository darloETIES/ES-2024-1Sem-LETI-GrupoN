package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import grupo.n.gestaodoterritorio.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProposalTest {

    private Property p1, p2, p3;
    private Owner o1, o2, o3;




    @BeforeEach
    void setUp() throws ParseException {
        // Inicializando geometrias e objetos de teste


        o1 = new Owner("56");
        p1 = new Property("8", "7350748.0", "2986220000000", 225.20, 1812.93, null, o1, "Arco da Calheta", "Calheta", "Madeira");


        o2 = new Owner("20");
        p2 = new Property("15", "20746817.0", "2976210000000", 45.99, 77.94, null, o2, "Arco da Calheta", "Calheta", "Madeira");


        o3 = new Owner("31");
        p3 = new Property("16", "20746818.0", "2976220000000", 77.38, 347.82, null, o3, "Arco da Calheta", "Calheta", "Madeira");
    }

    @Test
    void testProposalConstructor() {
        Proposal proposal = new Proposal(o1, o2, p1, p2, p2, p3);

        assertNotNull(proposal);
        assertEquals("O proprietário 56 fica com as propriedades: 8 e 15", proposal.getSourceExch1());
        assertEquals("O proprietário 20 fica com as propriedades: 15 e 16", proposal.getTargetExch1());
        assertEquals("O proprietário 56 fica com as propriedades: 15 e 16", proposal.getSourceExch2());
        assertEquals("O proprietário 20 fica com as propriedades: 8 e 15", proposal.getTargetExch2());
    }

    @Test
    void testProposalConstructorWithNulls() {
        Proposal proposal = new Proposal(null, null, null, null, null, null);

        assertNull(proposal.getSource());
        assertNull(proposal.getTarget());
        assertNull(proposal.getSp1());
    }
    @Test
    void testeProposalMethods(){
        Proposal proposal = new Proposal(o1, o2, p1, p2, p2, p3);
        assertEquals("56", proposal.getSource().toString());
        assertEquals("20", proposal.getTarget().toString());
        assertEquals("8", proposal.getSp1().toString());
        assertEquals("15", proposal.getSp2().toString());
        assertEquals("15", proposal.getTp1().toString());
        assertEquals("16", proposal.getTp2().toString());

    }
}

class PairPropertyTest {

    @Test
    void testConstructorAndJointArea() {
        Property p1 = new Property("50", "1", "001", 10, 50, null, null, "", "", "");
        Property p2 = new Property("20", "2", "002", 20, 30, null, null, "", "", "");

        PairProperty pair = new PairProperty(p1, p2);

        assertNotNull(pair);
        assertEquals("50",pair.getProperty1().toString());
        assertEquals("20",pair.getProperty2().toString());
        assertEquals(80, pair.getJointArea());
    }

    @Test
    void testEquals() {
        Property p1 = new Property("A", "1", "001", 10, 50, null, null, "", "", "");
        Property p2 = new Property("B", "2", "002", 20, 30, null, null, "", "", "");

        PairProperty pair1 = new PairProperty(p1, p2);
        PairProperty pair2 = new PairProperty(p1, p2);

        assertTrue(pair1.equals(pair2));

        PairProperty pair3 = new PairProperty(p1, new Property("C", "3", "003", 30, 40, null, null, "", "", ""));
        assertFalse(pair1.equals(pair3));
    }


}

