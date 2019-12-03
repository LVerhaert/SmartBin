package smartbin;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Deze klasse communiceert met de database
 * @author aangepast door Liza Verhaert
 */
public class DBCommunicator {

    private Connection conn;
    
    /**
     * Constructor.
     */
    public DBCommunicator() {
    }

    /**
     * Leg de verbinding
     * @return gelegde verbinding
     */
    private Connection createConnection() {
        conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://salt.db.elephantsql.com/uwjguczg";
            String username = "uwjguczg";
            String password = "ty-fYrVSTZv3xFXf8Ed7nJsYnzqRsDnM";
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Methode om een opdracht te versturen en de resultaten terug te krijgen
     * @param strSQL de SQL-opdracht die uitgevoerd moet worden
     * @return de resultaten van de opdracht
     */
    public ResultSet getData(String strSQL) {
        ResultSet result = null;
        try {
            Statement stmt = createConnection().createStatement();
            result = stmt.executeQuery(strSQL);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        closeConnection();
        return result;
    }

    /**
     * Methode om een opdracht te versturen en terug te krijgen of het gelukt is of niet
     * @param strDML de DML-opdracht die uitgevoerd moet worden
     * @return 0 als het niet gelukt is, 1 als het wel gelukt is
     */
    public int executeDML(String strDML) {
        int result = 0;
        try {
            Statement stmt = createConnection().createStatement();
            result = stmt.executeUpdate(strDML);
        } catch (Exception e) {
            if (e.getMessage().contains("not enough values")) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Null Value");
                alert.setHeaderText(null);
                alert.setContentText("Vul alle velden in.");
                alert.showAndWait();
            } else if (e.getMessage().contains("unique constraint")) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Constraint Violation");
                alert.setHeaderText(null);
                alert.setContentText("Dubbele of overlappende informatie, probeer iets anders.");
                alert.showAndWait();
            } else if (e.getMessage().contains("missing comma")) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Illegal Character");
                alert.setHeaderText(null);
                alert.setContentText("Gebruik geen '");
                alert.showAndWait();
            } else {
                System.err.println(e.getMessage());
            }
        }
        closeConnection();
        return result;
    }

    private void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
