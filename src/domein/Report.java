package domein;

/**
 * Report klasse
 * 
 * @author Sem
 * @author Niels
 */
public class Report {
    private final int[] numKlanten;
    private final String[] namen;

    public Report(Vestiging[] ves) {
        numKlanten = new int[ves.length];
        namen = new String[ves.length];

        for (int i = 0; i < ves.length; i++) {
            numKlanten[i] = ves[i].getKlanten().length;
            namen[i] = ves[i].getPlaatsNaam();
        }
    }
    public int getNumKlanten(int id) {
        return numKlanten[id];
    }
    public String getNaam(int id) {
        return namen[id];
    }
}
