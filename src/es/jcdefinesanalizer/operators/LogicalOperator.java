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
