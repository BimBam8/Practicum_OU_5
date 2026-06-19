package domein;

import data.Mapper;

/**
 * Kapstokklasse voor de Prik2Go applicatie.
 * Beheert alle vestigingen en hun klanten.
 * 
 * @author Niels
 * @author Sem
 */
public class PrikToGo {

    private final Vestiging[] vestigingen;

    /**
     * Maakt een nieuwe PrikToGo aan en laadt alle vestigingen uit de database.
     */
    public PrikToGo() {
        vestigingen = Mapper.getVestigingen();
    }

    /**
     * Geeft de klantenlijst van een geselecteerde vestiging terug.
     * @param id index van de vestiging
     * @return array van klantnummers als strings
     */
    public String[] selecteerVestiging(int id) {
        return vestigingen[id].getKlantenInfo();
    }

    /**
     * Geeft de plaatsnamen van alle vestigingen terug.
     * @return array van plaatsnamen
     */
    public String[] getOverzichtVestigingen() {
        if (vestigingen == null){
            return null;
        }
        String[] reStrings = new String[vestigingen.length];
        for (int i = 0; i < reStrings.length; i++) {
            reStrings[i] = vestigingen[i].getPlaatsNaam();
        }
        return reStrings;
    }
}