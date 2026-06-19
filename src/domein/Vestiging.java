package domein;

import domein.Klant;

/**
 * Stelt een vestiging voor met een plaatsnaam, postcode en bijbehorende klanten.
 * 
 * @author Niels
 * @author Sem
 */
public class Vestiging {
    private final String plaatsNaam;
    private final String postcode; 
    private final Klant[] klanten;

    /**
     * Maakt een nieuwe vestiging aan.
     * @param plaatsNaam naam van de plaats
     * @param postcode postcode van de vestiging
     * @param klanten array van klanten die de vestiging bezoeken
     */
    public Vestiging(String plaatsNaam, String postcode, Klant[] klanten) {
        this.plaatsNaam = plaatsNaam;
        this.postcode = postcode;
        this.klanten = klanten;
    }

    /**
     * Geeft de klantnummers van alle klanten als strings terug.
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
     * @return de plaatsnaam
     */
    public String getPlaatsNaam() {
        return plaatsNaam;
    }
}