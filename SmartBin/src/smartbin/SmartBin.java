/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartbin;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lizav
 */
public class SmartBin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBConnector connector = new DBConnector();
        String strSQL = "select * from afval";
        ResultSet dbResult = connector.getData(strSQL);
        System.out.println("afvalnr\t chipnr\t kleurwaarde\t afvaltype");
        try {
            while (dbResult.next()) {
                int afvalnr = dbResult.getInt("afvalnr");
                int chipnr = dbResult.getInt("chipnr");
                int kleurwaarde = dbResult.getInt("kleurwaarde");
                String afvaltype = dbResult.getString("afvaltype");
                System.out.println(afvalnr + "\t " + chipnr + "\t " + kleurwaarde + "\t\t " + afvaltype);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
