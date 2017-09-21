/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jcdefinesanalizer.variables;

/**
 *
 * @author AngelluisDiaz
 */
public class LogicalVariable {

    boolean value;

    public LogicalVariable(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
