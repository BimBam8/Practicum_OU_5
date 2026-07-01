package domein;

/**
 * Stelt een vestiging voor met een plaatsnaam, postcode en bijbehorende
 * klanten.
 * 
 * @author Niels
 * @author Sem
 */
public class Vestiging {
    private final String plaatsNaam;
    private final String postcode;
    private Klant[] klanten;
    private boolean isOpen;

    /**
     * Maakt een nieuwe vestiging aan.
     * 
     * @param plaatsNaam naam van de plaats
     * @param postcode   postcode van de vestiging
     * @param klanten    array van klanten die de vestiging bezoeken
     */
    public Vestiging(String plaatsNaam, String postcode, Klant[] klanten) {
        this.plaatsNaam = plaatsNaam;
        this.postcode = postcode;
        this.klanten = klanten;
        this.isOpen = true; // Standaard is de vestiging open
    }

    /**
     * Geeft de klantnummers van alle klanten als strings terug.
     * 
     * @return array van klantnummers als strings
     */
    public String[] getKlantenInfo() {
        String[] retString = new String[klanten.length];
        for (int i = 0; i < klanten.length; i++) {
            retString[i] = Integer.toString(klanten[i].getNummer());
        }
        return retString;
    }

    /**
     * Geeft de plaatsnaam van de vestiging terug.
     * 
     * @return de plaatsnaam
     */
    public String getPlaatsNaam() {
        return plaatsNaam;
    }

    /**
     * Geeft aan of de vestiging open is.
     * 
     * @return true als de vestiging open is, anders false
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Zet de open status van de vestiging.
     * 
     * @param isOpen true om de vestiging open te zetten, false om te sluiten
     */
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Klant[] getKlanten() {
        return klanten;
    }

    public void setKlanten(Klant[] nKlanten) {
        klanten = nKlanten;
    }
}