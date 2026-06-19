package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    public static Vestiging[] getVestigingen() {
        Connection connection = null;
        try {
            Class.forName(DRIVERNAME);

            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            List<String[]> sqlRows = new ArrayList<String[]>();
            String sqlString = "SELECT plaats, postcode FROM vestiging";

            PreparedStatement ps = connection.prepareStatement(sqlString);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sqlRows.add(new String[] { rs.getString("plaats"), rs.getString("postcode") });
            }

            Vestiging[] vestigingen = new Vestiging[sqlRows.size()];
            for (int i = 0; i < sqlRows.size(); i++) {
                String plaats = sqlRows.get(i)[0];
                String postcode = sqlRows.get(i)[1];
                vestigingen[i] = new Vestiging(plaats, postcode, getKlanten(plaats, connection));
            }
            return vestigingen;

        } catch (Exception e) {

            e.fillInStackTrace();
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

    /**
     * Haalt de klanten op van een speciefieke vestiging bij een specifieke database
     * connectie.
     * 
     * @param vestiging
     * @param connection
     * @return De verkregen klanten array. Anders een lege klanten array.
     * @throws SQLException
     */
    private static Klant[] getKlanten(String vestiging, Connection connection) throws SQLException {
        String sql = "SELECT DISTINCT k.nr, k.postcode " +
                "FROM klant k " +
                "JOIN bezoek b ON k.nr = b.klant " +
                "WHERE b.vestiging = ?";
        List<Klant> klanten = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, vestiging);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    klanten.add(new Klant(rs.getInt("nr"), rs.getString("postcode")));
                }
            }
        }
        return klanten.toArray(new Klant[0]);
    }
}
