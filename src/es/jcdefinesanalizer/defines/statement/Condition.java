/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jcdefinesanalizer.defines.statement;

/**
 *
 * @author AngelluisDiaz
 */
public class Condition {

    private String[] condition;
    private String[] parentCondition;
    private int indexInParent;
    private Object resultCondition;

    public Condition() {
    }

    public String[] getCondition() {
        return condition;
    }

    public void setCondition(String[] condition) {
        this.condition = condition;
    }

    public String[] getParentCondition() {
        return parentCondition;
    }

    public void setParentCondition(String[] parentCondition) {
        this.parentCondition = parentCondition;
    }

    public int getIndexInParent() {
        return indexInParent;
    }

    public void setIndexInParent(int indexInParent) {
        this.indexInParent = indexInParent;
    }

    public Object getResultCondition() {
        return resultCondition;
    }

    public void setResultCondition(Object resultCondition) {
        this.resultCondition = resultCondition;
    }

    public boolean hasResultCondition() {
        return resultCondition != null;
    }
}
