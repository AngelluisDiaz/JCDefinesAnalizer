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
