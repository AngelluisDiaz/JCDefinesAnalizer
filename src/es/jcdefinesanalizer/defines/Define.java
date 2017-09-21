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
package es.jcdefinesanalizer.defines;

/**
 *
 * @author AngelluisDiaz
 */
public class Define {

    private String name;
    private int value;
    private boolean isdefined = false;
    private boolean hasValue = false;

    public Define(String name) {
        this.name = name;
    }

    public Define(String name, int value) {
        this.name = name;
        this.value = value;
        hasValue = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        hasValue = true;
    }

    public void setAsDefined() {
        this.isdefined = true;
    }

    public void setAsUndefined() {
        this.isdefined = false;
    }

    public boolean isDefined() {
        return this.isdefined;
    }

    public boolean hasValue() {
        return this.hasValue;
    }
}
