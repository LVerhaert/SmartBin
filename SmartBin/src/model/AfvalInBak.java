/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
