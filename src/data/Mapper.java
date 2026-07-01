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

        String sql = "SELECT DISTINCT k.nr AS klantnr, "
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
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, plaats); // voor de CASE (oorspronkelijke vestiging vooraan)
        ps.setString(2, plaats); // voor de WHERE (klanten van deze vestiging)
        ResultSet rs = ps.executeQuery();

        int huidigNr = -1;
        boolean overslaan = false; // true als deze klant al elders is toegewezen
        List<Integer> ranglijst = new ArrayList<>(); // al in de juiste volgorde vanuit SQL

        while (rs.next()) {
            int nr = rs.getInt("klantnr");

            // Nieuwe klant begint: vorige klant afronden.
            if (nr != huidigNr) {
                if (huidigNr != -1 && !overslaan) {
                    klanten.add(new Klant(huidigNr, toIntArray(ranglijst)));
                }
                huidigNr = nr;
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
            klanten.add(new Klant(huidigNr, toIntArray(ranglijst)));
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