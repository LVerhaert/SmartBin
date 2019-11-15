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
    private int afstandsensor;
    private String dekselpos;
    private String ledkleur;
    private String baktype;
    private boolean vol;
    
    public Bak (int baknr, int gewichtsensor, int afstandsensor, String dekselpos, String ledkleur, String baktype, boolean vol) {
        this.baknr = baknr;
        this.gewichtsensor = gewichtsensor;
        this.afstandsensor = afstandsensor;
        this.dekselpos = dekselpos;
        this.ledkleur = ledkleur;
        this.baktype = baktype;
        this.vol = vol;
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

    public int getAfstandsensor() {
        return afstandsensor;
    }

    public void setAfstandsensor(int afstandsensor) {
        this.afstandsensor = afstandsensor;
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

    public boolean isVol() {
        return vol;
    }

    public void setVol(boolean vol) {
        this.vol = vol;
    }
    
    
}
