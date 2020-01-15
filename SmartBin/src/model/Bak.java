package model;

/**
 *
 * @author Liza Verhaert
 */
public class Bak {

    private int baknr;
    private String baktype;
    private int afvalaantal;
    private final int AFVALLIMIET = 4;

    public Bak(int baknr, String baktype, int afvalaantal) {
        this.baknr = baknr;
        this.baktype = baktype;
        this.afvalaantal = afvalaantal;
    }

    public int getBaknr() {
        return baknr;
    }

    public void setBaknr(int baknr) {
        this.baknr = baknr;
    }

    public String getBaktype() {
        return baktype;
    }

    public void setBaktype(String baktype) {
        this.baktype = baktype;
    }

    public int getAfvalaantal() {
        return afvalaantal;
    }

    public void setAfvalaantal(int afvalaantal) {
        this.afvalaantal = afvalaantal;
    }
    
    public void addAfval() {
        afvalaantal++;
        if (afvalaantal >= AFVALLIMIET) {
            System.out.println("Bak #" + baknr + " is nu vol.");
        }
    }
}
