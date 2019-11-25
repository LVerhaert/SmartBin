/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartbin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Afval;
import model.AfvalInBak;
import model.Bak;

/**
 *
 * @author Liza Verhaert
 */
public class Data {
    private ArrayList<Afval> afval;
    private ArrayList<AfvalInBak> afvalinbakken;
    private ArrayList<Bak> bakken;
    
    private DBCommunicator connector;
    
    public Data() {
        connector = new DBCommunicator();
        
        resetAfval();
        resetAfvalinbakken();
        resetBakken();
        
    }

    private void resetAfval() {
        afval = new ArrayList<>();
        String strSQL = "select * from afval";
        ResultSet dbResult = connector.getData(strSQL);
        try {
            while (dbResult.next()) {
                int afvalnr = dbResult.getInt("afvalnr");
                String chipnr = dbResult.getString("chipnr");
                String afvaltype = dbResult.getString("afvaltype");
                int kleurr = dbResult.getInt("kleurr");
                int kleurg = dbResult.getInt("kleurg");
                int kleurb = dbResult.getInt("kleurb");
                afval.add(new Afval(afvalnr, chipnr, afvaltype, kleurr, kleurg, kleurb));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
    }

    private void resetAfvalinbakken() {
        afvalinbakken = new ArrayList<>();
        String strSQL = "select * from afvalinbak";
        ResultSet dbResult = connector.getData(strSQL);
        try {
            while (dbResult.next()) {
                String afvaltype = dbResult.getString("afvaltype");
                String baktype = dbResult.getString("baktype");
                afvalinbakken.add(new AfvalInBak(afvaltype, baktype));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void resetBakken() {
        bakken = new ArrayList<>();
        String strSQL = "select * from bak";
        ResultSet dbResult = connector.getData(strSQL);
        try {
            while (dbResult.next()) {
                int baknr = dbResult.getInt("baknr");
                int gewichtsensor = dbResult.getInt("gewichtsensor");
                String dekselpos = dbResult.getString("dekselpos");
                String ledkleur = dbResult.getString("ledkleur");
                String baktype = dbResult.getString("baktype");
                bakken.add(new Bak(baknr, gewichtsensor, dekselpos, ledkleur, baktype));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
