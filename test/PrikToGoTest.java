package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domein.PrikToGo;
import domein.Vestiging;
import data.Mapper;

public class PrikToGoTest {

    private PrikToGo ptg;
    Vestiging[] vestigingen;

    @Before
    public void setUp() {
        ptg = new PrikToGo();
        vestigingen = Mapper.getVestigingen();
    }

    @Test
    public void testGetOverzichtVestigingen() {
        String[] ptgvestigingen = ptg.getOverzichtVestigingen();
        assertArrayEquals(ptgvestigingen, vestigingen);
        assertEquals(12, vestigingen.length);
        assertEquals("Amsterdam", vestigingen[0]);
        assertEquals("Rotterdam", vestigingen[1]);
    }

    @Test
    public void testSelecteerVestiging() {
        String[] klanten = ptg.selecteerVestiging(0);
        assertArrayEquals(vestigingen[0].getKlantenInfo(), klanten);
        assertEquals("1001", klanten[0]);
        assertEquals("1002", klanten[1]);
    }

    @Test
    public void testSelecteerVestigingRdam() {
        String[] klanten = ptg.selecteerVestiging(1);
        assertEquals(7, klanten.length);
        assertEquals("2001", klanten[0]);
        assertEquals("2002", klanten[1]);
    }
}