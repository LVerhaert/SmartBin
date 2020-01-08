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

    private DBCommunicator dbcommunicator;

    public Data() {
        System.out.println("Connecting to database...");
        dbcommunicator = new DBCommunicator();
        resetAfval();
        resetAfvalinbakken();
        resetBakken();
        System.out.println(afval.size() + " afvalitems, " + bakken.size() + " bakken gevonden.");
    }

    private void resetAfval() {
        afval = new ArrayList<>();
        String strSQL = "select * from afval";
        ResultSet dbResult = dbcommunicator.getData(strSQL);
        try {
            while (dbResult.next()) {
                String chipnr = dbResult.getString("chipnr");
                String afvaltype = dbResult.getString("afvaltype");
                if (afvaltype.startsWith("glas")) {
                    afvaltype = "glas";
                }
                afval.add(new Afval(chipnr, afvaltype));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    private void resetAfvalinbakken() {
        afvalinbakken = new ArrayList<>();
        String strSQL = "select * from afvalinbak";
        ResultSet dbResult = dbcommunicator.getData(strSQL);
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
        ResultSet dbResult = dbcommunicator.getData(strSQL);
        try {
            while (dbResult.next()) {
                int baknr = dbResult.getInt("baknr");
                int gewichtsensor = dbResult.getInt("gewichtsensor");
                String dekselpos = dbResult.getString("dekselpos");
                String baktype = dbResult.getString("baktype");
                bakken.add(new Bak(baknr, gewichtsensor, dekselpos, baktype));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addAfval(Afval a) {
        afval.add(a);
    }

    public Afval getAfvalViaChipnr(String chipnr) {
        for (Afval a : afval) {
            if (chipnr.equals(a.getChipnr())) {
                return a;
            }
        }
        System.out.println("Geen afval met dit chipnummer (\"" + chipnr + "\") gevonden.");
        return null;
    }

    public int getBak(String baktype) {
        for (Bak b : bakken) {
            if (baktype.equals(b.getBaktype())) {
                return b.getBaknr();
            }
        }
        System.out.println("Geen bak met dit type (\"" + baktype + "\") gevonden.");
        return 0;
    }

    public String getAfvalInWelkeBak(String afvaltype) {
        for (AfvalInBak aib : afvalinbakken) {
            if (afvaltype.equals(aib.getAfvaltype())) {
                return aib.getBaktype();
            }
        }
        System.out.println("Ik weet niet in welk baktype dit type afval (\"" + afvaltype + "\") moet.");
        return null;
    }

}
