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

    /**
     * Maakt een nieuwe PrikToGo aan en laadt alle vestigingen uit de database.
     */
    public PrikToGo() {
        this.vestigingen = Mapper.getVestigingen();
        Mapper.vulRanglijsten(vestigingen);
            
        this.vestigingGesloten = new boolean[this.vestigingen.length];
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
        if (vestigingen.length <= 1) {
            // laatste geopende ves
            return;
        }

        vestigingGesloten[id] = true;
        vestigingen[id].setOpen(false);
        int[] diffs = new int[vestigingen.length];

        Klant[] teVerplaatsen = vestigingen[id].getKlanten();
        for (Klant k : teVerplaatsen) {
            int closestincr = getClosestVestiging(k);
            if (closestincr != -1) {
                diffs[k.getCurrentVestiging()] -= 1;
                diffs[closestincr] += 1;
                k.setCurrentVestiging(closestincr);
                voegKlantToe(vestigingen[closestincr], k);
            }
        }

        vestigingen[id].setKlanten(new Klant[0]);
        notifyObservers(diffs);
    }

    /*
     * Heropent een vestiging.
     * 
     * @param id index van de vestiging
     */
    public void heropenVestiging(int id) {
        vestigingGesloten[id] = false;
        vestigingen[id].setOpen(true);
        int[] diffs = new int[vestigingen.length];
        List<Klant> alle = getAlleKlanten();
        int teller = 0;
        for (Klant k : alle) {
            if (k.getOorspronkelijkeVestiging() == id) {  // aangepast naar originele vestiging uit database ipv dichtstbijzijnde vestiging
                teller++;
                diffs[k.getCurrentVestiging()] -= 1;
                diffs[id] += 1;
                k.setCurrentVestiging(id);
            }
        }
        schrijfKlantenTerug(alle);
        notifyObservers(diffs);
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
     * @return De incrementale waarde van de dichst bijzijnde vestiging bij de klant. Anders -1.
     */
    private int getClosestVestiging(Klant klant) {
        int[] dists = klant.getDistVestigingen();
        for (int j = 1; j < dists.length; j++) {
            if (!vestigingGesloten[dists[j]]) {
                return dists[j];
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
     * Geeft een map terug met de naam van de vestiging als key en het aantal klanten als value.
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
    public void toggleVestiging(String naam) {
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