package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domein.Klant;

public class KlantTest {

    private Klant klant;

    @Before
    public void setUp() {
        klant = new Klant(1001, "1234AB");
    }

    @Test
    public void testGetNummer() {
        assertEquals(1001, klant.getNummer());
    }
}
