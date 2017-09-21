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
