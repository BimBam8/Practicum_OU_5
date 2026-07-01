package domein;

/**
 * Stelt een klant voor met een uniek nummer en postcode.
 * 
 * @author Niels
 * @author Sem
 */
public class Klant {
    private final int nr;
    private final int[] distVestigingen;
    private int currentVestiging;

    /**
     * Maakt een nieuwe klant aan.
     * 
     * @param nr       uniek klantnummer
     * @param postcode postcode van de klant
     */
    public Klant(int nr, int[] distVestigingen) {
        this.nr = nr;
        this.distVestigingen = distVestigingen;
        this.currentVestiging = distVestigingen[0];
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

    public void setCurrentVestiging(int currentVestiging) {
        this.currentVestiging = currentVestiging;
    }

    public int getOorspronkelijkeVestiging() {
        return distVestigingen[0];
    }

    public int getCurrentVestiging() {
        return currentVestiging;
    }
}