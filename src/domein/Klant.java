package domein;

/**
 * Stelt een klant voor met een uniek nummer en postcode.
 * 
 * @author Niels
 * @author Sem
 */
public class Klant {
    private final int nr;
    private final String postcode;

    /**
     * Maakt een nieuwe klant aan.
     * @param nr uniek klantnummer
     * @param postcode postcode van de klant
     */
    public Klant(int nr, String postcode) {
        this.nr = nr;
        this.postcode = postcode;
    }

    /**
     * Geeft het klantnummer terug.
     * @return het klantnummer
     */
    public int getNummer() {
        return nr;
    }
}