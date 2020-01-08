package model;

/**
 *
 * @author Liza Verhaert
 */
public class Afval {

    private String chipnr;
    private String afvaltype;

    public Afval(String chipnr, String afvaltype) {
        this.chipnr = chipnr;
        this.afvaltype = afvaltype;
    }

    public Afval(String chipnr) {
        this.chipnr = chipnr;
    }

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
}
