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
public class RelationalOperator extends Operator {

    public RelationalOperator(String operator) throws Exception {

        super(operator, Operator.RELATIONAL_TYPE);
    }

    public boolean appliesOperator(int numericalValue1, int numericalValue2) throws Exception {

        boolean res = false;
        String operator = this.getOperator();

        if (operator.equals(R_GREATER_OR_EQUAL)) {
            res = numericalValue1 >= numericalValue2;
        } else if (operator.equals(R_GREATER)) {
            res = numericalValue1 > numericalValue2;
        } else if (operator.equals(R_EQUAL)) {
            res = numericalValue1 == numericalValue2;
        } else if (operator.equals(R_LESS)) {
            res = numericalValue1 < numericalValue2;
        } else if (operator.equals(R_LESS_OR_EQUAL)) {
            res = numericalValue1 <= numericalValue2;
        } else if (operator.equals(R_NOT_EQUAL)) {
            res = numericalValue1 != numericalValue2;
        }

        return res;
    }
}
