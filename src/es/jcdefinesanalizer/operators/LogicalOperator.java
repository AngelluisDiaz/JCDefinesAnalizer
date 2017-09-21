/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jcdefinesanalizer.operators;

/**
 *
 * @author AngelluisDiaz
 */
public class LogicalOperator extends Operator {

    public LogicalOperator(String operator) throws Exception {

        super(operator, LOGICAL_TYPE);
    }

    public boolean appliesOperator(boolean logicalValue1, boolean logicalValue2) throws Exception {

        boolean res = false;
        String operator = this.getOperator();

        if (operator.equals(L_OR)) {
            res = logicalValue1 || logicalValue2;
        } else if (operator.equals(L_AND)) {
            res = logicalValue1 && logicalValue2;
        } else if (operator.equals(L_NOT)) {
            throw new Exception("Operation can't be applied");
        }

        return res;
    }

    public boolean appliesOperator(boolean logicalValue) throws Exception {

        boolean res = false;
        String operator = this.getOperator();

        if (operator.equals(L_OR)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(L_AND)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(L_NOT)) {
            res = !logicalValue;
        }

        return res;
    }

}
