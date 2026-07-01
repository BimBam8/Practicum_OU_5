package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.DriverManager;

import domein.Klant;
import domein.Vestiging;

/**
 * Mapper klasse voor connectie met de database
 * 
 * @author Sem
 * @author Niels
 */
public class Mapper {

    private static final String DATABASE_URL = "jdbc:firebirdsql://localhost:3052//var/lib/firebird/data/Prik2Go_res_v3.fdb";
    private static final String DATABASE_USER = "sysdba";
    private static final String DATABASE_PASSWORD = "masterkey";
    private static final String DRIVERNAME = "org.firebirdsql.jdbc.FBDriver";

    /**
     * Maakt een tijdelijke connectie aan met de database. Om de informatie van
     * vestigingen op te vragen.
     * 
     * In geval van Exception returnt null
     * 
     * @return De verkregen vestigingen array van de database. Anders null
     */
    // public static Vestiging[] getVestigingen() {
    // Connection connection = null;
    // try {
    // Class.forName(DRIVERNAME);

    // connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER,
    // DATABASE_PASSWORD);

    // List<String[]> sqlRows = new ArrayList<String[]>();
    // String sqlString = "SELECT plaats, postcode FROM vestiging ORDER BY plaats";

    // PreparedStatement ps = connection.prepareStatement(sqlString);
    // ResultSet rs = ps.executeQuery();

    // while (rs.next()) {
    // sqlRows.add(new String[] { rs.getString("plaats"), rs.getString("postcode")
    // });
    // }

    // Vestiging[] vestigingen = new Vestiging[sqlRows.size()];
    // List<Integer> bekendeKlanten = new ArrayList<>(); // lijst van klantnummers
    // die al zijn toegevoegd aan een
    // // vestiging. De distinct helpt niet bij het opvragen van
    // // klanten per vestiging.
    // for (int i = 0; i < sqlRows.size(); i++) {
    // String plaats = sqlRows.get(i)[0];
    // String postcode = sqlRows.get(i)[1];
    // vestigingen[i] = new Vestiging(plaats, postcode, getKlanten(plaats, i,
    // connection, bekendeKlanten));// i
    // // is
    // // deindex
    // // van
    // // de
    // // originele
    // // vestiging
    // // van
    // // de
    // // klant
    // }
    // return vestigingen;

    // } catch (Exception e) {

    // e.printStackTrace();
    // return null;

    // } finally {
    // if (connection != null) {
    // try {
    // connection.close();
    // } catch (Exception e) {
    // }
    // }
    // }
    // }

    public static Vestiging[] getVestigingen() {
        Connection connection = null;
        try {
            Class.forName(DRIVERNAME);

            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            List<String> plaatsen = new ArrayList<>();
            List<String> postcodes = new ArrayList<>();
            Map<String, Integer> plaatsIndex = new HashMap<>();
            String sqlString = "SELECT plaats, postcode FROM vestiging ORDER BY plaats";

            PreparedStatement ps = connection.prepareStatement(sqlString);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String plaats = rs.getString("plaats");
                plaatsIndex.put(plaats, plaatsen.size());
                plaatsen.add(plaats);
                postcodes.add(rs.getString("postcode"));
            }

            // Per vestiging de klanten ophalen (met meteen hun ranglijst).
            // bekendeKlanten zorgt ervoor dat elke klant aan precies één
            // vestiging wordt toegewezen de eerste alfabetisch waar hij komt.
            int aantal = plaatsen.size();
            Set<Integer> bekendeKlanten = new HashSet<>();
            Vestiging[] vestigingen = new Vestiging[aantal];
            for (int i = 0; i < aantal; i++) {
                Klant[] klanten = getKlanten(connection, plaatsen.get(i), bekendeKlanten, plaatsIndex);
                vestigingen[i] = new Vestiging(plaatsen.get(i), postcodes.get(i), klanten);
            }
            return vestigingen;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static Klant[] getKlanten(Connection connection, String plaats,
            Set<Integer> bekendeKlanten, Map<String, Integer> plaatsIndex) throws SQLException {

        String sql = "SELECT DISTINCT k.nr AS klantnr, k.postcode AS klantpc, "
                + "v.plaats AS vestiging, "
                + "CASE WHEN v.plaats = ? THEN 0 ELSE 1 END AS isorigineel, "
                + "sqrt(power(kp.lat - vp.lat, 2) + power(kp.lng - vp.lng, 2)) AS afstand "
                + "FROM klant k "
                + "JOIN bezoek b ON k.nr = b.klant "
                + "JOIN PostcodeInfo kp ON k.postcode = kp.postcode "
                + "CROSS JOIN Vestiging v "
                + "JOIN PostcodeInfo vp ON v.postcode = vp.postcode "
                + "WHERE b.vestiging = ? "
                + "ORDER BY k.nr, isorigineel, afstand";

        List<Klant> klanten = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, plaats); // voor de CASE (oorspronkelijke vestiging vooraan)
            ps.setString(2, plaats); // voor de WHERE (klanten van deze vestiging)
            try (ResultSet rs = ps.executeQuery()) {

                int huidigNr = -1;
                boolean overslaan = false; // true als deze klant al elders is toegewezen
                String huidigPc = null;
                List<Integer> ranglijst = new ArrayList<>(); // al in de juiste volgorde vanuit SQL

                while (rs.next()) {
                    int nr = rs.getInt("klantnr");

                    // Nieuwe klant begint: vorige klant afronden.
                    if (nr != huidigNr) {
                        if (huidigNr != -1 && !overslaan) {
                            klanten.add(new Klant(huidigNr, huidigPc, toIntArray(ranglijst)));
                        }
                        huidigNr = nr;
                        huidigPc = rs.getString("klantpc");
                        overslaan = !bekendeKlanten.add(nr); // add() == false betekent: al bekend
                        ranglijst = new ArrayList<>();
                    }

                    if (overslaan) {
                        continue;
                    }
                    Integer idx = plaatsIndex.get(rs.getString("vestiging"));
                    if (idx != null) {
                        ranglijst.add(idx);
                    }
                }

                // Laatste klant afronden.
                if (huidigNr != -1 && !overslaan) {
                    klanten.add(new Klant(huidigNr, huidigPc, toIntArray(ranglijst)));
                }
            }
        }
        return klanten.toArray(new Klant[0]);
    }

    /**
     * Zet een lijst van Integers om naar een int[].
     *
     * @param lijst de lijst
     * @return de bijbehorende int[]
     */
    private static int[] toIntArray(List<Integer> lijst) {
        int[] array = new int[lijst.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = lijst.get(i);
        }
        return array;
    }
}

// /**
// * Haalt de klanten op van een speciefieke vestiging bij een specifieke
// database
// * connectie.
// *
// * @param vestiging
// * @param connection
// * @return De verkregen klanten array. Anders een lege klanten array.
// * @throws SQLException
// */
// private static Klant[] getKlanten(String vestiging, int
// origineleVestigingIndex, Connection connection,
// List<Integer> bekendeKlanten) throws SQLException {
// String sql = "SELECT DISTINCT k.nr, k.postcode " +
// "FROM klant k " +
// "JOIN bezoek b ON k.nr = b.klant " +
// "WHERE b.vestiging = ?";

// List<Klant> klanten = new ArrayList<>();
// try (PreparedStatement ps = connection.prepareStatement(sql)) {
// ps.setString(1, vestiging);
// try (ResultSet rs = ps.executeQuery()) {
// while (rs.next()) {
// // eerst controleren of de klant al bij een eerdere vestiging is toegevoegd.
// Zo
// // niet, dan toevoegen aan de lijst van klanten.
// int nr = rs.getInt("nr");
// if (!bekendeKlanten.contains(nr)) {
// bekendeKlanten.add(nr);
// klanten.add(new Klant(nr, rs.getString("postcode"),
// origineleVestigingIndex));
// }
// }
// }
// }
// return klanten.toArray(new Klant[0]);
// }

// /**
// * Maakt een tijdelijke connectie aan met de database om
// afstand-klant-vestiging
// * ranglijt te maken
// * .
// * Vult de distVestigingen van de klanten in de startup van de applicatie.
// */
// public static void vulRanglijsten(Vestiging[] vestigingen) {
// Connection connection = null;
// try {
// Class.forName(DRIVERNAME);
// connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER,
// DATABASE_PASSWORD);

// String sql = "SELECT DISTINCT k.nr AS klantnr, " +
// "v.plaats AS vestiging, " +
// "sqrt(power(kp.lat - vp.lat, 2) + power(kp.lng - vp.lng, 2)) AS afstand " +
// "FROM Klant AS k " +
// "JOIN PostcodeInfo AS kp ON k.postcode = kp.postcode " +
// "CROSS JOIN Vestiging AS v " +
// "JOIN PostcodeInfo AS vp ON v.postcode = vp.postcode " +
// "ORDER BY k.nr, afstand";

// PreparedStatement ps = connection.prepareStatement(sql);
// ResultSet rs = ps.executeQuery();

// int huidigKlantNr = -1;
// List<Integer> huidigeRanglijst = new ArrayList<>();

// while (rs.next()) {
// int klantNr = rs.getInt("klantnr");
// String naam = rs.getString("vestiging");

// // nieuwe klant begint
// if (klantNr != huidigKlantNr) {
// if (huidigKlantNr != -1) {
// // omzetten van List naar int[]
// int[] array = new int[huidigeRanglijst.size()];
// for (int i = 0; i < huidigeRanglijst.size(); i++) {
// array[i] = huidigeRanglijst.get(i);
// }
// // sla ranglijst op bij vorige klant
// for (Vestiging v : vestigingen) {
// for (Klant k : v.getKlanten()) {
// if (k.getNummer() == huidigKlantNr) {
// k.setDistVestigingen(array);
// break;
// }
// }
// }
// }
// huidigKlantNr = klantNr;
// huidigeRanglijst = new ArrayList<>();
// }

// // zoek index van vestiging in de array
// for (int i = 0; i < vestigingen.length; i++) {
// if (vestigingen[i].getPlaatsNaam().equals(naam)) {
// huidigeRanglijst.add(i);
// break;
// }
// }
// }

// // laatste klant
// if (huidigKlantNr != -1) {
// int[] array = new int[huidigeRanglijst.size()];
// for (int i = 0; i < huidigeRanglijst.size(); i++) {
// array[i] = huidigeRanglijst.get(i);
// }
// for (Vestiging v : vestigingen) {
// for (Klant k : v.getKlanten()) {
// if (k.getNummer() == huidigKlantNr) {
// k.setDistVestigingen(array);
// break;
// }
// }
// }
// }

// } catch (Exception e) {
// e.printStackTrace();
// } finally {
// if (connection != null) {
// try {
// connection.close();
// } catch (Exception e) {
// }
// }
// }
// }
// }
