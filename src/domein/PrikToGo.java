package domein;

import data.Mapper;
import observer.Subject;

/**
 * Kapstokklasse voor de Prik2Go applicatie.
 * Beheert alle vestigingen en hun klanten.
 * 
 * @author Niels
 * @author Sem
 */
public class PrikToGo extends Subject {

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

    /*
     * Sluit een  vestiging.
     * @param id index van de vestiging
     */
    public void sluitVestiging(int id) {
        if (vestigingen.length == 1){
            String message = "Kan niet sluiten: laatste vestiging";
            //hier moeten we nog iets mee? moet dit in een try catch met een error? bericht moet in de view koemn
        }
        vestigingen[id].setIsOpen(false);
            // moet nog verder uitgewerkt worden: klanten herverdelen
        
        notifyObservers();
    }

    /*
     * Heropent een vestiging.
     * @param id index van de vestiging
     */
    public void heropenVestiging(int id) {
        vestigingen[id].setIsOpen(true);
            // hier moeten we nog iets mee: klanten meoten herverdeeld worden.
        notifyObservers();
    }
}