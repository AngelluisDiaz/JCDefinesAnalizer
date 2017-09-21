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
public class BitwiseOperator extends Operator {

    public BitwiseOperator(String operator) throws Exception {

        super(operator, Operator.BITWISE_TYPE);
    }

    public int appliesOperator(int numericalValue1, int numericalValue2) throws Exception {

        int res = 0;
        String operator = this.getOperator();

        if (operator.equals(B_OR)) {
            res = numericalValue1 | numericalValue2;
        } else if (operator.equals(B_AND)) {
            res = numericalValue1 & numericalValue2;
        } else if (operator.equals(B_XOR)) {
            res = numericalValue1 ^ numericalValue2;
        } else if (operator.equals(B_COMP)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(B_LEFT)) {
            res = numericalValue1 << numericalValue2;
        } else if (operator.equals(B_RIGHT)) {
            res = numericalValue1 >> numericalValue2;
        }

        return res;
    }

    public int appliesOperator(int numericalValue) throws Exception {

        int res = 0;
        String operator = this.getOperator();

        if (operator.equals(B_OR)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(B_AND)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(B_XOR)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(B_COMP)) {
            res = ~numericalValue;
        } else if (operator.equals(B_LEFT)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(B_RIGHT)) {
            throw new Exception("Operation can't be applied");
        }

        return res;
    }
}
