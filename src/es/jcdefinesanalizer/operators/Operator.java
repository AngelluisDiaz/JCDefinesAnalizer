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
public class Operator {

    //logical operators
    public static final String L_OR = "||";
    public static final String L_AND = "&&";
    public static final String L_NOT = "!";

    //bitwise operators
    public static final String B_OR = "|";
    public static final String B_AND = "&";
    public static final String B_XOR = "^";
    public static final String B_COMP = "~";
    public static final String B_LEFT = "<<";
    public static final String B_RIGHT = ">>";

    //relational operators
    public static final String R_GREATER_OR_EQUAL = ">=";
    public static final String R_GREATER = ">";
    public static final String R_EQUAL = "==";
    public static final String R_LESS = "<";
    public static final String R_LESS_OR_EQUAL = "<=";
    public static final String R_NOT_EQUAL = "!=";

    //arithmetic operators
    public static final String A_PLUS = "+";
    public static final String A_SUBTRACT = "-";
    public static final String A_MULTIPLY = "*";
    public static final String A_DIVIDE = "/";
    public static final String A_MODULUS = "%";
    public static final String A_INCREMENT = "++";
    public static final String A_DECREMENT = "--";

    private String operatorValue;

    public static final int LOGICAL_TYPE = 0;
    public static final int ARITHMETIC_TYPE = 1;
    public static final int RELATIONAL_TYPE = 2;
    public static final int BITWISE_TYPE = 3;

    private int operatorType;

    public Operator(String operator, int operatorType) throws Exception {
        checkOperator(operator, operatorType);
        operatorValue = operator;
        this.operatorType = operatorType;
    }

    public String getOperator() {
        return operatorValue;
    }

    public int getOperatorType() {
        return operatorType;
    }

    void setOperator(String operator, int operatorType) throws Exception {
        checkOperator(operator, operatorType);
        operatorValue = operator;
        this.operatorType = operatorType;
    }

    private void checkOperator(String operator, int operatorType) throws Exception {

        switch (operatorType) {
            case Operator.ARITHMETIC_TYPE:
                checkArithmeticOperator(operator);
                break;
            case Operator.BITWISE_TYPE:
                checkBitwiseOperator(operator);
                break;
            case Operator.LOGICAL_TYPE:
                checkLogicalOperator(operator);
                break;
            case Operator.RELATIONAL_TYPE:
                checkRelationalOperator(operator);
                break;
            default:
                throw new Exception("Illegal operator type");

        }

    }

    private void checkArithmeticOperator(String operator) throws Exception {

        if (!operator.equals(A_PLUS)
                && !operator.equals(A_SUBTRACT)
                && !operator.equals(A_MULTIPLY)
                && !operator.equals(A_DIVIDE)
                && !operator.equals(A_MODULUS)
                && !operator.equals(A_INCREMENT)
                && !operator.equals(A_DECREMENT)) {
            throw new Exception("Invalid arithmetic operator");
        }
    }

    private void checkBitwiseOperator(String operator) throws Exception {

        if (!operator.equals(B_OR)
                && !operator.equals(B_AND)
                && !operator.equals(B_XOR)
                && !operator.equals(B_COMP)
                && !operator.equals(B_LEFT)
                && !operator.equals(B_RIGHT)) {
            throw new Exception("Invalid arithmetic operator");
        }
    }

    private void checkLogicalOperator(String operator) throws Exception {

        if (!operator.equals(L_OR)
                && !operator.equals(L_AND)
                && !operator.equals(L_NOT)) {
            throw new Exception("Invalid arithmetic operator");
        }
    }

    private void checkRelationalOperator(String operator) throws Exception {

        if (!operator.equals(R_GREATER_OR_EQUAL)
                && !operator.equals(R_GREATER)
                && !operator.equals(R_EQUAL)
                && !operator.equals(R_LESS)
                && !operator.equals(R_LESS_OR_EQUAL)
                && !operator.equals(R_NOT_EQUAL)) {
            throw new Exception("Invalid arithmetic operator");
        }
    }
}
