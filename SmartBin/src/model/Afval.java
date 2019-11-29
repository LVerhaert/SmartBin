package model;

/**
 *
 * @author Liza Verhaert
 */
public class Afval {
//    private int afvalnr = 0;
    private String chipnr = "";
    private String afvaltype = "";
    private int kleurr = 0;
    private int kleurg = 0;
    private int kleurb = 0;
    
    public Afval(/*int afvalnr, */String chipnr, String afvaltype, int kleurr, int kleurg, int kleurb) {
//        this.afvalnr = afvalnr;
        this.chipnr = chipnr;
        this.afvaltype = afvaltype;
        this.kleurr = kleurr;
        this.kleurg = kleurg;
        this.kleurb = kleurb;
    }

    public Afval(String chipnr) {
        this.chipnr = chipnr;
    }
            
//    public int getAfvalnr() {
//        return afvalnr;
//    }
//
//    public void setAfvalnr(int afvalnr) {
//        this.afvalnr = afvalnr;
//    }

    public String getChipnr() {
        return chipnr;
    }

    public void setChipnr(String chipnr) {
        this.chipnr = chipnr;
    }

    public String getAfvaltype() {
        return afvaltype;
    }

    public void setAfvaltype(String afvaltype) {
        this.afvaltype = afvaltype;
    }
    
    public String getKleur() {
        return "(" + kleurr + "," + kleurg + "," + kleurb + ")";
    }

    public void setKleur(int kleurr, int kleurg, int kleurb) {
        this.kleurr = kleurr;
        this.kleurg = kleurg;
        this.kleurb = kleurb;
    }
    
    
}
