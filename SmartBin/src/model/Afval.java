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
public class Afval {
    private int afvalnr;
    private String chipnr;
    private int kleurwaarde;
    private String afvaltype;
    
    public Afval(int afvalnr, String chipnr, int kleurwaarde, String afvaltype) {
        this.afvalnr = afvalnr;
        this.chipnr = chipnr;
        this.kleurwaarde = kleurwaarde;
        this.afvaltype = afvaltype;
    }

    public int getAfvalnr() {
        return afvalnr;
    }

    public void setAfvalnr(int afvalnr) {
        this.afvalnr = afvalnr;
    }

    public String getChipnr() {
        return chipnr;
    }

    public void setChipnr(String chipnr) {
        this.chipnr = chipnr;
    }

    public int getKleurwaarde() {
        return kleurwaarde;
    }

    public void setKleurwaarde(int kleurwaarde) {
        this.kleurwaarde = kleurwaarde;
    }
    
    public String getAfvaltype() {
        return afvaltype;
    }

    public void setAfvaltype(String afvaltype) {
        this.afvaltype = afvaltype;
    }
    
    
}
