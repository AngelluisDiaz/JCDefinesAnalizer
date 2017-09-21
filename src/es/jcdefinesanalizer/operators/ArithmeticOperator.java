/*
 * Copyright (C) 2017 AngelluisDiaz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.jcdefinesanalizer.operators;

/**
 *
 * @author AngelluisDiaz
 */
public class ArithmeticOperator extends Operator{

    public ArithmeticOperator(String operator) throws Exception {

        super(operator, Operator.ARITHMETIC_TYPE);
    }

    public int appliesOperator(int numericalValue1, int numericalValue2) throws Exception {

        int res = 0;
        String operator = this.getOperator();

        if (operator.equals(A_PLUS)) {
            res = numericalValue1 + numericalValue2;
        } else if (operator.equals(A_SUBTRACT)) {
            res = numericalValue1 - numericalValue2;
        } else if (operator.equals(A_MULTIPLY)) {
            res = numericalValue1 * numericalValue2;
        } else if (operator.equals(A_DIVIDE)) {
            res = numericalValue1 / numericalValue2;
        } else if (operator.equals(A_MODULUS)) {
            res = numericalValue1 % numericalValue2;
        } else if (operator.equals(A_INCREMENT)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(A_DECREMENT)) {
            throw new Exception("Operation can't be applied");
        }

        return res;
    }

    public int appliesOperator(int numericalValue) throws Exception {

        int res = 0;
        String operator = this.getOperator();

        if (operator.equals(A_PLUS)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(A_SUBTRACT)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(A_MULTIPLY)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(A_DIVIDE)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(A_MODULUS)) {
            throw new Exception("Operation can't be applied");
        } else if (operator.equals(A_INCREMENT)) {
            res = numericalValue++;
        } else if (operator.equals(A_DECREMENT)) {
            res = numericalValue--;
        }

        return res;
    }
}
