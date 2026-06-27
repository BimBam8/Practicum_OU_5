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
    private int[] distVestigingen;
    private final int oorspronkelijkeVestiging;
    private int currentVestiging;

    /**
     * Maakt een nieuwe klant aan.
     * 
     * @param nr       uniek klantnummer
     * @param postcode postcode van de klant
     */
    public Klant(int nr, String postcode, int oorspronkelijkeVestiging) {
        this.nr = nr;
        this.postcode = postcode;
        this.distVestigingen = null;
        this.oorspronkelijkeVestiging = oorspronkelijkeVestiging;
        this.currentVestiging = oorspronkelijkeVestiging;
    }

    /**
     * Geeft het klantnummer terug.
     * 
     * @return het klantnummer
     */
    public int getNummer() {
        return nr;
    }

    public int[] getDistVestigingen() {
        return distVestigingen;
    }

    public void setDistVestigingen(int[] vestigingenRanglijst) {
        this.distVestigingen = vestigingenRanglijst;
    }

    public void setCurrentVestiging(int currentVestiging) {
        this.currentVestiging = currentVestiging;
    }

    public int getOorspronkelijkeVestiging() {
        return oorspronkelijkeVestiging;
    }

    public int getCurrentVestiging() {
        return currentVestiging;
    }
}