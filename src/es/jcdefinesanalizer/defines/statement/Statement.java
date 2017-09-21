/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jcdefinesanalizer.defines.statement;

import es.jcdefinesanalizer.defines.Define;
import java.util.ArrayList;
import es.jcdefinesanalizer.operators.ArithmeticOperator;
import es.jcdefinesanalizer.operators.BitwiseOperator;
import es.jcdefinesanalizer.operators.LogicalOperator;
import es.jcdefinesanalizer.operators.Operator;
import es.jcdefinesanalizer.operators.RelationalOperator;
import es.jcdefinesanalizer.utils.Utils;
import es.jcdefinesanalizer.variables.LogicalVariable;
import es.jcdefinesanalizer.variables.NumericalVariable;

/**
 *
 * @author AngelluisDiaz
 */
public class Statement {

    private ArrayList<Object> elements = new ArrayList();
    private ArrayList<Define> extractedDefines;

    public Statement(ArrayList<Define> extractedDefines) {
        this.extractedDefines = extractedDefines;
    }

    public boolean hasElements() {
        return !elements.isEmpty();
    }

    public void addElement(Object object) throws Exception {

        if ((object instanceof NumericalVariable)
                || (object instanceof LogicalVariable)
                || (object instanceof ArithmeticOperator)
                || (object instanceof RelationalOperator)
                || (object instanceof LogicalOperator)
                || (object instanceof BitwiseOperator)
                || (object instanceof Operator)) {
            elements.add(object);
        } else {
            throw new IllegalArgumentException("Illegal object type");
        }

    }

    public void addElement(String[] elements) throws Exception {

        for (int i = 0; i < elements.length; i++) {
            if (!elements[i].equals("")) {
                addElement(elements[i]);
            }
        }
    }

    public void addElement(String unknownElement) throws Exception {
        Object object = null;
        int baseNumber;
        Define define;

        if (Utils.isAnumber(unknownElement)) {
            if (unknownElement.startsWith("0x")) {
                baseNumber = 16;
            } else {
                baseNumber = 10;
            }
            object = new NumericalVariable(Integer.parseInt(unknownElement, baseNumber));
        } else if (unknownElement.contains("defined")) {

            define = getDefine(unknownElement.substring(7));

            if (define != null) {

                if (define.isDefined()) {
                    object = new LogicalVariable(true);
                } else {
                    object = new LogicalVariable(false);
                }
            }   
            else{
                object = new LogicalVariable(false);
            }
            
        } else if (Utils.isAnOperator(unknownElement)) {

            try {
                object = new ArithmeticOperator(unknownElement);
            } catch (Exception e) {
                try {
                    object = new RelationalOperator(unknownElement);
                } catch (Exception ex) {
                    try {
                        object = new LogicalOperator(unknownElement);
                    } catch (Exception exx) {
                        try {
                            object = new BitwiseOperator(unknownElement);
                        } catch (Exception exxx) {
                            throw new Exception("Element of condition not recognized");
                        }
                    }
                }
            }
        } else {
            define = getDefine(unknownElement);
            
            if (define != null) {
                if (define.isDefined()) {
                    if (define.hasValue()) {
                        object = new NumericalVariable(define.getValue());
                    } else {
                        object = new LogicalVariable(true);
                    }
                } else {
                    object = new LogicalVariable(false);
                }
            }
        }

        if (object == null) {
            throw new Exception("Element of condition not recognized");
        }

        addElement(object);
    }

    public Object getStatmentResult() throws Exception {

        ArrayList<Object> newElements;
            //boolean statmentResult = false;

        if (this.elements.isEmpty()) {
            throw new Exception("Statment without elements");
        }

            //realiza todas las operaciones () [] -> . ++ --
        //solo implementado ++ --
        newElements = doAllPostFixOperations(elements);

            //+ - ! ~ ++ -- (type)* & sizeof
        //implementado ! ~ 
        newElements = doAllUnaryOperations(newElements);

        //* / %
        newElements = doAllMultiplicativeOperations(newElements);

        //+ -
        newElements = doAllAdditiveOperations(newElements);

        //>> <<
        newElements = doAllShiftOperations(newElements);

        //< <= > >=
        newElements = doAllRelationalOperations(newElements);

        //== !=
        newElements = doAllEqualityOperations(newElements);

        //& ^ |
        newElements = doAllBitwiseOperations(newElements);

        //&& ||
        newElements = doAllLogicalOperations(newElements);

            //?:
        //no implementado
        newElements = doAllConditionalOperations(newElements);

            //= += -= *= /= %= >>= <<= &= ^= |=
        //no implementado
        newElements = doAllAssigmentOperations(newElements);

        if (newElements.size() != 1) {
            throw new Exception("Statment can't be analized");
        }

//            statmentResult = ((LogicalVariable)newElements.get(0)).getValue();
//            
//            return statmentResult;
        return newElements.get(0);
    }

        //realiza todas las operaciones () [] -> . ++ --
    //solo implementado ++ --
    private ArrayList<Object> doAllPostFixOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        ArithmeticOperator arithmeticOperator;
        NumericalVariable numericalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        newElements.add(lastElement);

        indexNewElements++;

        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                //se gestiona ++ y --
                if (element instanceof ArithmeticOperator) {
                    arithmeticOperator = (ArithmeticOperator) element;

                    if (arithmeticOperator.getOperator().equals(Operator.A_INCREMENT)
                            || arithmeticOperator.getOperator().equals(Operator.A_DECREMENT)) {

                            //se incremente o se decrementa el ultimo elemento leido el cual debe ser numerico
                        numericalVariable = (NumericalVariable) lastElement;

                        numericalVariable.setValue(arithmeticOperator.appliesOperator(numericalVariable.getValue()));

                        lastElement = numericalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

        //+ - ! ~ ++ -- (type)* & sizeof
    //implementado ! ~ 
    private ArrayList<Object> doAllUnaryOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        LogicalOperator logicalOperator;
        BitwiseOperator bitwiseOperator;
        NumericalVariable numericalVariable;
        LogicalVariable logicalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        for (int i = 0; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                //se gestiona ~ 
                if (element instanceof BitwiseOperator) {
                    bitwiseOperator = (BitwiseOperator) element;

                    if (bitwiseOperator.getOperator().equals(Operator.B_COMP)) {

                            //se invierten todos los bits del siguiente elemento que tendra que ser numerico
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        numericalVariable.setValue(bitwiseOperator.appliesOperator(numericalVariable.getValue()));

                        lastElement = numericalVariable;

                        newElements.add(indexNewElements, lastElement);

                        indexNewElements++;

                        i++;

                        continue;
                    }
                } //se gestiona !
                else if (element instanceof LogicalOperator) {
                    logicalOperator = (LogicalOperator) element;

                    if (logicalOperator.getOperator().equals(Operator.L_NOT)) {

                            //se niega el siguiente elemento que tendra que ser logico
                        logicalVariable = (LogicalVariable) elements.get(i + 1);

                        logicalVariable.setValue(logicalOperator.appliesOperator(logicalVariable.getValue()));

                        lastElement = logicalVariable;

                        newElements.add(indexNewElements, lastElement);

                        indexNewElements++;

                        i++;

                        continue;
                    }
                }

            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //* / %
    private ArrayList<Object> doAllMultiplicativeOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        ArithmeticOperator arithmeticOperator;
        NumericalVariable numericalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        newElements.add(lastElement);

        indexNewElements++;

        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof ArithmeticOperator) {
                    arithmeticOperator = (ArithmeticOperator) element;

                    //se gestiona *
                    if (arithmeticOperator.getOperator().equals(Operator.A_MULTIPLY)
                            || arithmeticOperator.getOperator().equals(Operator.A_DIVIDE)
                            || arithmeticOperator.getOperator().equals(Operator.A_MODULUS)) {

                            //se multiplica/divide/% el ultimo elemento leido con el siguiente, ambos deben ser numericos
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        numericalVariable.setValue(arithmeticOperator.appliesOperator(((NumericalVariable) lastElement).getValue(), numericalVariable.getValue()));

                        lastElement = numericalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //+ -
    private ArrayList<Object> doAllAdditiveOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        ArithmeticOperator arithmeticOperator;
        NumericalVariable numericalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        newElements.add(lastElement);

        indexNewElements++;

        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof ArithmeticOperator) {
                    arithmeticOperator = (ArithmeticOperator) element;

                    //se gestiona + y -
                    if (arithmeticOperator.getOperator().equals(Operator.A_PLUS)
                            || arithmeticOperator.getOperator().equals(Operator.A_SUBTRACT)) {

                            //se suma/resta el ultimo elemento leido con el siguiente, ambos deben ser numericos
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        numericalVariable.setValue(arithmeticOperator.appliesOperator(((NumericalVariable) lastElement).getValue(), numericalVariable.getValue()));

                        lastElement = numericalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //>> <<
    private ArrayList<Object> doAllShiftOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        BitwiseOperator bitwiseOperator;
        NumericalVariable numericalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        newElements.add(lastElement);

        indexNewElements++;

        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof BitwiseOperator) {
                    bitwiseOperator = (BitwiseOperator) element;

                    //se gestiona << y >>
                    if (bitwiseOperator.getOperator().equals(Operator.B_LEFT)
                            || bitwiseOperator.getOperator().equals(Operator.B_RIGHT)) {

                            //se corren los bits a la izquierda/derecha del ultimo elemento leido 
                        //tantos bits como indique el siguiente, ambos deben ser numericos
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        numericalVariable.setValue(bitwiseOperator.appliesOperator(((NumericalVariable) lastElement).getValue(), numericalVariable.getValue()));

                        lastElement = numericalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //< <= > >=
    private ArrayList<Object> doAllRelationalOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        RelationalOperator relationalOperator;
        NumericalVariable numericalVariable;
        LogicalVariable logicalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        newElements.add(lastElement);

        indexNewElements++;

        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof RelationalOperator) {
                    relationalOperator = (RelationalOperator) element;

                    //se gestiona < <= > >=
                    if (relationalOperator.getOperator().equals(Operator.R_LESS)
                            || relationalOperator.getOperator().equals(Operator.R_LESS_OR_EQUAL)
                            || relationalOperator.getOperator().equals(Operator.R_GREATER)
                            || relationalOperator.getOperator().equals(Operator.R_GREATER_OR_EQUAL)) {

                            //se comparan el ultimo elemento obtenido con el siguiente. ambos deben ser numericos
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        logicalVariable = new LogicalVariable(false);

                        logicalVariable.setValue(relationalOperator.appliesOperator(((NumericalVariable) lastElement).getValue(), numericalVariable.getValue()));

                        lastElement = logicalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //== !=
    private ArrayList<Object> doAllEqualityOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        RelationalOperator relationalOperator;
        NumericalVariable numericalVariable;
        LogicalVariable logicalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        newElements.add(lastElement);

        indexNewElements++;

        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof RelationalOperator) {
                    relationalOperator = (RelationalOperator) element;

                    //se gestiona == !=
                    if (relationalOperator.getOperator().equals(Operator.R_EQUAL)
                            || relationalOperator.getOperator().equals(Operator.R_NOT_EQUAL)) {

                            //se comparan el ultimo elemento obtenido con el siguiente. ambos deben ser numericos
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        logicalVariable = new LogicalVariable(false);

                        logicalVariable.setValue(relationalOperator.appliesOperator(((NumericalVariable) lastElement).getValue(), numericalVariable.getValue()));

                        lastElement = logicalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //& ^ |
    private ArrayList<Object> doAllBitwiseOperations(ArrayList<Object> elements) throws Exception {

        ArrayList<Object> newElements;

        if (elements.isEmpty()) {
            return elements;
        }

        //&
        newElements = doAllBitwiseOperations(elements, Operator.B_AND);

        //^
        newElements = doAllBitwiseOperations(newElements, Operator.B_XOR);

        //|
        newElements = doAllBitwiseOperations(newElements, Operator.B_OR);

        return newElements;
    }

    //& ^ |
    private ArrayList<Object> doAllBitwiseOperations(ArrayList<Object> elements, String boperator) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        BitwiseOperator bitwiseOperator;
        NumericalVariable numericalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        if (!boperator.equals(Operator.B_AND)
                && !boperator.equals(Operator.B_XOR)
                && !boperator.equals(Operator.B_OR)) {
            throw new IllegalArgumentException("Unexpected operator value");
        }

        newElements.add(lastElement);

        indexNewElements++;

        //se gestiona &
        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof BitwiseOperator) {
                    bitwiseOperator = (BitwiseOperator) element;

                    //& ^ |
                    if (bitwiseOperator.getOperator().equals(boperator)) {

                            //se realiza una AND binaria del ultimo elemento obtenido con el siguiente. 
                        //Ambos deben ser numericos
                        numericalVariable = (NumericalVariable) elements.get(i + 1);

                        numericalVariable.setValue(bitwiseOperator.appliesOperator(((NumericalVariable) lastElement).getValue(), numericalVariable.getValue()));

                        lastElement = numericalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

    //&& ||
    private ArrayList<Object> doAllLogicalOperations(ArrayList<Object> elements) throws Exception {
        ArrayList<Object> newElements;

        if (elements.isEmpty()) {
            return elements;
        }

        //&&
        newElements = doAllLogicalOperations(elements, Operator.L_AND);

        //||
        newElements = doAllLogicalOperations(newElements, Operator.L_OR);

        return newElements;
    }

    //&& ||
    private ArrayList<Object> doAllLogicalOperations(ArrayList<Object> elements, String boperator) throws Exception {
        ArrayList<Object> newElements = new ArrayList();
        Object lastElement = elements.get(0);
        Object element;
        LogicalOperator logicalOperator;
        LogicalVariable logicalVariable;
        int indexNewElements = 0;

        if (elements.isEmpty()) {
            return elements;
        }

        if (!boperator.equals(Operator.L_AND)
                && !boperator.equals(Operator.L_OR)) {
            throw new IllegalArgumentException("Unexpected operator value");
        }

        newElements.add(lastElement);

        indexNewElements++;

        //se gestiona &
        for (int i = 1; i < elements.size(); i++) {

            element = elements.get(i);

            if (element instanceof Operator) {

                if (element instanceof LogicalOperator) {
                    logicalOperator = (LogicalOperator) element;

                    //&& ||
                    if (logicalOperator.getOperator().equals(boperator)) {

                            //se realiza una AND/OR logica del ultimo elemento obtenido con el siguiente. 
                        //Ambos deben ser logicos
                        logicalVariable = (LogicalVariable) elements.get(i + 1);

                        logicalVariable.setValue(logicalOperator.appliesOperator(((LogicalVariable) lastElement).getValue(), logicalVariable.getValue()));

                        lastElement = logicalVariable;

                        newElements.remove(indexNewElements - 1);

                        newElements.add(indexNewElements - 1, lastElement);

                        i++;

                        continue;
                    }
                }
            }

            lastElement = element;
            newElements.add(indexNewElements, element);
            indexNewElements++;
        }

        return newElements;
    }

        //?:
    //no implementado
    private ArrayList<Object> doAllConditionalOperations(ArrayList<Object> elements) {
            //no implementado

        return elements;
    }

        //= += -= *= /= %= >>= <<= &= ^= |=
    //no implementado
    private ArrayList<Object> doAllAssigmentOperations(ArrayList<Object> elements) {
            //no implementado

        return elements;
    }
    
    private Define getDefine(String defineName) throws Exception{
            
        int index = findDefine(defineName);
        
        if(index==-1){
            return null;
        }
        
        return this.extractedDefines.get(index);
    }
    
    private int findDefine(String defineName) {
            
        for(int i=0;i<this.extractedDefines.size();i++){
            if(extractedDefines.get(i).getName().equals(defineName)){
                return i;
            }
        }
        
        return -1;
    }
    
}
