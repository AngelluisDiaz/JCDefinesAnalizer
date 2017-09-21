/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jcdefinesanalizer.defines;

/**
 *
 * @author AngelluisDiaz
 */
public class Define {

    private String name;
    private int value;
    private boolean isdefined = false;
    private boolean hasValue = false;

    public Define(String name) {
        this.name = name;
    }

    public Define(String name, int value) {
        this.name = name;
        this.value = value;
        hasValue = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        hasValue = true;
    }

    public void setAsDefined() {
        this.isdefined = true;
    }

    public void setAsUndefined() {
        this.isdefined = false;
    }

    public boolean isDefined() {
        return this.isdefined;
    }

    public boolean hasValue() {
        return this.hasValue;
    }
}
