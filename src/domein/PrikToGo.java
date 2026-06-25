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
    private boolean[] vestigingGesloten;

    /**
     * Maakt een nieuwe PrikToGo aan en laadt alle vestigingen uit de database.
     */
    public PrikToGo() {
        this.vestigingen = Mapper.getVestigingen();
        Mapper.vulRanglijsten(vestigingen);
        // even testen of de ranglijsten goed zijn gevuld
        for (Vestiging v : vestigingen) {
            for (Klant k : v.getKlanten()) {
                System.out.println("Klant " + k.getNummer() + 
                    " currentVestiging: " + k.getCurrentVestiging() +
                    " ranglijst: " + java.util.Arrays.toString(k.getDistVestigingen()));
    }
}

        this.vestigingGesloten = new boolean[this.vestigingen.length];
        for (boolean b : vestigingGesloten) {
            System.out.println(b);
        }
    }

    /**
     * Geeft de klantenlijst van een geselecteerde vestiging terug.
     * 
     * @param id index van de vestiging
     * @return array van klantnummers als strings
     */
    public String[] selecteerVestiging(int id) {
        return vestigingen[id].getKlantenInfo();
    }

    /**
     * Geeft de plaatsnamen van alle vestigingen terug.
     * 
     * @return array van plaatsnamen
     */
    public String[] getOverzichtVestigingen() {
        if (vestigingen == null) {
            return null;
        }
        String[] reStrings = new String[vestigingen.length];
        for (int i = 0; i < reStrings.length; i++) {
            reStrings[i] = vestigingen[i].getPlaatsNaam();
        }
        return reStrings;
    }

    /*
     * Sluit een vestiging.
     * 
     * @param id index van de vestiging
     */
    public void sluitVestiging(int id) {
        if (vestigingen.length == 1) {
            String message = "Kan niet sluiten: laatste vestiging";
            System.out.println(message);
            // hier moeten we nog iets mee? moet dit in een try catch met een error? bericht
            // moet in de view komen

            // Dit kunnen we ook in de frontend berekenen
        }
        vestigingGesloten[id] = true;
        vestigingen[id].setOpen(false);
        // moet nog verder uitgewerkt worden: klanten herverdelen

        Klant[] klanten = vestigingen[id].getKlanten();
        // tijdelijk vasthouden van vestigingen
        int[] nieuweDichstbijzijnde = new int[klanten.length];
        int[] diffs = new int[vestigingen.length];

        for (int i = 0; i < klanten.length; i++) {
            int closest = getClosestVestiging(klanten[i]);
            nieuweDichstbijzijnde[i] = closest;
            diffs[closest] += 1;
        }
        for (int i = 0; i < diffs.length; i++) {
            
        }
        vestigingen[8] = new Vestiging(null, null, klanten);
        notifyObservers();
    }

    /*
     * Heropent een vestiging.
     * 
     * @param id index van de vestiging
     */
    public void heropenVestiging(int id) {
        vestigingen[id].setOpen(true);
        // hier moeten we nog iets mee: klanten moeten herverdeeld worden.
        this.notifyObservers();
    }

    private int getClosestVestiging(Klant klant) {
        int[] dists = klant.getDistVestiginen();
        int incr = klant.getDistIncr();
        
        for (int j = 1; j < dists.length; j++) {
            if (!vestigingGesloten[dists[j]]) {
                return dists[j];
            }
        }
        return -1;
    }
    // -----------------tijdelijk voor testen er moet hier een betere methode voor komen of een andere manier------------------------------
    private Klant[] getAllKlanten() {
        Klant[][] klantenMat = new Klant[vestigingen.length][0];
        int totalLen = 0;
        for (int i = 0; i < vestigingen.length; i++) {
            if (!vestigingGesloten[i]) {
                Klant[] kten = vestigingen[i].getKlanten();
                for (int j = 0; j < ; j++) {
                    
                }
            }
            
        } 
        Klant[] k = new Klant[totalLen];
            
        
    
    }
}