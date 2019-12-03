package model;

/**
 *
 * @author Liza Verhaert
 */
public class AfvalInBak {
    
    private String afvaltype;
    private String baktype;
    
    public AfvalInBak (String afvaltype, String baktype) {
        this.afvaltype = afvaltype;
        this.baktype = baktype;
    }

    public String getAfvaltype() {
        return afvaltype;
    }

    public void setAfvaltype(String afvaltype) {
        this.afvaltype = afvaltype;
    }

    public String getBaktype() {
        return baktype;
    }

    public void setBaktype(String baktype) {
        this.baktype = baktype;
    }
    
    
}
