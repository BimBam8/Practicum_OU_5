package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domein.Klant;
import domein.Vestiging;

public class VestigingTest {

    private Vestiging vestiging;
    private Vestiging legeVestiging;

    @Before
    public void setUp() {
        Klant[] klanten = { new Klant(1001, "1234AB"), new Klant(1002, "5678CD") };
        vestiging = new Vestiging("Amsterdam", "1000AA", klanten);
        legeVestiging = new Vestiging("Rotterdam", "3000AA", new Klant[0]);
    }

    @Test
    public void testGetPlaatsNaam() {
        assertEquals("Amsterdam", vestiging.getPlaatsNaam());
    }

    @Test
    public void testGetKlantenInfo() {
        String[] info = vestiging.getKlantenInfo();
        assertEquals(2, info.length);
        assertEquals("1001", info[0]);
        assertEquals("1002", info[1]);
    }

    @Test
    public void testGetKlantenInfoLegeArray() {
        assertEquals(0, legeVestiging.getKlantenInfo().length);
    }
}
