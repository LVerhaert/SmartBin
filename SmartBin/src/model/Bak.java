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
public class Bak {
    
    private int baknr;
    private int gewichtsensor;
    private String dekselpos;
    private String ledkleur;
    private String baktype;
    
    public Bak (int baknr, int gewichtsensor, String dekselpos, String ledkleur, String baktype) {
        this.baknr = baknr;
        this.gewichtsensor = gewichtsensor;
        this.dekselpos = dekselpos;
        this.ledkleur = ledkleur;
        this.baktype = baktype;
    }

    public int getBaknr() {
        return baknr;
    }

    public void setBaknr(int baknr) {
        this.baknr = baknr;
    }

    public int getGewichtsensor() {
        return gewichtsensor;
    }

    public void setGewichtsensor(int gewichtsensor) {
        this.gewichtsensor = gewichtsensor;
    }

    public String getDekselpos() {
        return dekselpos;
    }

    public void setDekselpos(String dekselpos) {
        this.dekselpos = dekselpos;
    }
    
    public void sluitDeksel() {
        if (dekselpos == "dicht") {
            System.out.println("Deksel van bak " + baknr + " is al dicht");
        } else {
            this.dekselpos = "dicht";
        }
    }
    
    public void openDeksel() {
        if (dekselpos == "open") {
            System.out.println("Deksel van bak " + baknr + " is al open");
        } else {
            this.dekselpos = "open";
        }
    }

    public String getLedkleur() {
        return ledkleur;
    }
    
    public boolean ledkleurIsGroen() {
        if (this.ledkleur == "groen") {
            return true;
        }
        return false;
    }

    public void setLedkleur(String ledkleur) {
        this.ledkleur = ledkleur;
    }
    
    public void veranderLedkleur() {
        if (this.ledkleur == "rood") {
            this.ledkleur = "groen";
        } else {
            this.ledkleur = "rood";
        }
    }

    public String getBaktype() {
        return baktype;
    }

    public void setBaktype(String baktype) {
        this.baktype = baktype;
    }

}
