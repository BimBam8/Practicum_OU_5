package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private int hoeveelheidGesloten;

    /**
     * Maakt een nieuwe PrikToGo aan en laadt alle vestigingen uit de database.
     */
    public PrikToGo() {
        this.vestigingen = Mapper.getVestigingen();
        if (this.vestigingen == null) {
            System.err.println("error ves is null");
        }
        // even testen of de ranglijsten goed zijn gevuld
        this.hoeveelheidGesloten = 0;
        this.vestigingGesloten = new boolean[this.vestigingen.length];
    }

    /**
     * Geeft de klantenlijst van een geselecteerde vestiging terug.
     * 
     * @param id index van de vestiging
     * @return array van klantnummers als strings
     */
    public String[] selecteerVestiging(int id) {
        // ik denk dat we beter een lege lijst kunnen teruggeven als de vestiging gesloten is, zodat de GUI netjes blijft werken
        //if (vestigingGesloten[id]){return null;}
        if (vestigingGesloten[id]) {return new String[0];}
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

        // Sem, kijgen we hier niet mogelijk een ArrayIndexOutOfBoundsException? 
        // Hij draait alleen bij startup dus het gaat niet fout, maar theoretisch zouden we hem kunnen krijgen toch.
        String[] reStrings = new String[vestigingen.length-hoeveelheidGesloten];
        for (int i = 0; i < vestigingen.length; i++) {
            if (vestigingGesloten[i]){continue;}
            reStrings[i] = vestigingen[i].getPlaatsNaam();
        }
        return reStrings;
    }

    /*
     * Sluit een vestiging.
     * 
     * @param id index van de vestiging
     */
    public void sluitVestiging(int id) throws IllegalArgumentException {
        if (vestigingen.length - hoeveelheidGesloten <= 1) {
            // laatste geopende ves
            // hier een exception gooien zodat de GUI een melding kan geven dat de laatste vestiging niet gesloten kan worden. Vangen in de VisualizerController.
            throw new IllegalArgumentException("Je mag de laatste geopende vestiging niet sluiten.");
        }
        hoeveelheidGesloten += 1;
        vestigingGesloten[id] = true;
        vestigingen[id].setOpen(false);
        Klant[] teVerplaatsen = vestigingen[id].getKlanten();
        for (Klant k : teVerplaatsen) {
            int closestincr = getClosestVestiging(k);
            if (closestincr != -1) {
                k.setCurrentVestiging(closestincr);
                voegKlantToe(vestigingen[closestincr], k);
            }
        }

        vestigingen[id].setKlanten(new Klant[0]);
        notifyObservers();
    }

    /*
     * Heropent een vestiging.
     * 
     * @param id index van de vestiging
     */
    public void heropenVestiging(int id) {
        hoeveelheidGesloten -= 1;
        vestigingGesloten[id] = false;
        vestigingen[id].setOpen(true);
        List<Klant> alle = getAlleKlanten();
        for (Klant k : alle) {
            if (k.getOorspronkelijkeVestiging() == id) {
                k.setCurrentVestiging(id);
            }
        }
        schrijfKlantenTerug(alle);
        notifyObservers();
    }

    /**
     * 
     * 
     * @param alle
     */
    private void schrijfKlantenTerug(List<Klant> alle) {
        List<List<Klant>> perVestiging = new ArrayList<>();
        for (int i = 0; i < vestigingen.length; i++) {
            perVestiging.add(new ArrayList<>());
        }
        for (Klant k : alle) {
            perVestiging.get(k.getCurrentVestiging()).add(k);
        }
        for (int i = 0; i < vestigingen.length; i++) {
            vestigingen[i].setKlanten(perVestiging.get(i).toArray(new Klant[0]));
        }
    }

    /**
     * 
     * 
     * @param klant
     * @return De incrementale waarde van de dichst bijzijnde vestiging bij de
     *         klant. Anders -1.
     */
    private int getClosestVestiging(Klant klant) {
        int[] dists = klant.getDistVestigingen();
        for (int i = 1; i < dists.length; i++) {
            if (!vestigingGesloten[dists[i]]) {
                return dists[i];
            }
        }
        return -1;
    }

    /**
     * Verkrijgt alle klanten.
     * Gaat door alle vestigingen heen en haalt de klanten op.
     * 
     * @return Alle Klanten uit alle vestigingen
     */
    private List<Klant> getAlleKlanten() {
        List<Klant> klanten = new ArrayList<>();
        for (Vestiging v : vestigingen) {
            for (Klant k : v.getKlanten()) {
                klanten.add(k);
            }
        }
        return klanten;
    }

    /**
     * Voegt een Klant k toe aan een Vestiging v
     * 
     * @param v
     * @param k
     */
    private void voegKlantToe(Vestiging v, Klant k) {
        List<Klant> lijst = new ArrayList<>(Arrays.asList(v.getKlanten()));
        lijst.add(k);
        v.setKlanten(lijst.toArray(new Klant[0]));
    }

    /**
     * Geeft een map terug met de naam van de vestiging als key en het aantal
     * klanten als value.
     * 
     * @return Map met (key, value) paren van vestiging naam en aantal klanten
     */
    public Map<String, Integer> getKlantenAantalPerVestiging() {
        Map<String, Integer> resultaat = new java.util.HashMap<>();
        for (Vestiging v : vestigingen) {
            resultaat.put(v.getPlaatsNaam(), v.getKlanten().length);
        }
        return resultaat;
    }

    /**
     * Toggle de open-status van een vestiging.
     * is nodig om de juiste gegevens door te gebruiken die worden
     * doorgestuurd door de visualizerController als er op een bar wordt geklikt.
     * 
     * @param naam de naam van de vestiging
     */
    public void toggleVestiging(String naam) throws IllegalArgumentException{
        for (int i = 0; i < vestigingen.length; i++) {
            if (vestigingen[i].getPlaatsNaam().equals(naam)) {
                if (vestigingen[i].isOpen()) {
                    sluitVestiging(i);
                } else {
                    heropenVestiging(i);
                }
                break;
            }
        }
    }
}