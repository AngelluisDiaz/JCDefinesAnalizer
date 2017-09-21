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
package es.jcdefinesanalizer;

import es.jcdefinesanalizer.defines.Define;
import es.jcdefinesanalizer.defines.statement.Condition;
import es.jcdefinesanalizer.defines.statement.Statement;
import es.jcdefinesanalizer.utils.Utils;
import es.jcdefinesanalizer.variables.LogicalVariable;
import es.jcdefinesanalizer.variables.NumericalVariable;
import es.jcdefinesanalizer.operators.Operator;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author AngelluisDiaz
 */
public class JCDefinesAnalizer {
    
    private String hFilePath;
    private String lineInAnalisys;
    private long lineInAnalisysNumber = 0;
    private ArrayList<Define> extractedDefines = new ArrayList();
    
    /**
     * Constructor indicating the .h file to analize i.e to extract the constants created with the directive #define. 
     * This object can't recognize macros. 
     * @param hfile Absolute .h file to extract all the constants created with the directive #define. This object 
     * can't extract or recognized macros. It is recommended that the .h file doen't have any macros.
     * @exception Exception if an exception has occurred.
     */
    public JCDefinesAnalizer(String hfile) throws Exception{
                
        this.hFilePath = hfile;
        
        try{
            extractDefines();
        }catch(Exception e){
            throw new RuntimeException(e.getMessage()+"In line "+this.lineInAnalisysNumber+" : "+this.lineInAnalisys, e.getCause());
        }
    }          
        
    /**
     * To know if a define is defined into the .h file analized.
     * @return <code>true</code> if the define is defined.
     */
    public boolean isThisDefineEnabled(String defineName) throws Exception{
        
        for(int i=0;i<this.extractedDefines.size();i++){
            if(extractedDefines.get(i).getName().equals(defineName)){
                if(extractedDefines.get(i).isDefined()){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get all defines constants extracted from h file analized.
     * @return The array of defines. Null in case of an error.
     */
    public Define[] getDefines(){
        
        Define[] defines = new Define[extractedDefines.size()];
        
        try{
            defines = this.extractedDefines.toArray(defines);
            
        }catch(Exception e){
            return null;
        }
        
        return defines;
    }
    
    /**
     * Get the define object extracted from h file analized.
     * @param defineName The define name.
     * @return The define. Null if it is not found into h file analized.
     * @exception Exception if an exception has occurred.
     */
    public Define getDefine(String defineName) throws Exception{
            
        int index = findDefine(defineName);
        
        if(index==-1){
            return null;
        }
        
        return this.extractedDefines.get(index);
    }
    
    private void removeDefine(String defineName) {
            
        for(int i=0;i<this.extractedDefines.size();i++){
            if(extractedDefines.get(i).getName().equals(defineName)){
                extractedDefines.remove(i);
                break;
            }
        }
    }
    
    private int findDefine(String defineName) {
            
        for(int i=0;i<this.extractedDefines.size();i++){
            if(extractedDefines.get(i).getName().equals(defineName)){
                return i;
            }
        }
        
        return -1;
    }
    
    private void extractDefines() throws Exception{
        
        String line;
        RandomAccessFile buffer_wr;
        
        buffer_wr = new RandomAccessFile(hFilePath, "rw");
        
        do {
            line = readNewLineFromFile(buffer_wr);

            recognizeLine(buffer_wr, line);

        }while (line != null);

        buffer_wr.close();  
    }
    
    private void recognizeLine(RandomAccessFile buffer_wr, String line) throws Exception
    {
        int index;
        
        if (line != null)
        {
            if(line.contains("#define"))
            {
                index = line.indexOf("#define");

                //si en esta linea en la que aparece el "#define" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+7))){
                    return;
                }               

                //se obtiene el nombre del define y su valor (si es que tiene)
                mngtDefine(line.substring(index+7));
            }
            else if(line.contains("#undef"))
            {
                index = line.indexOf("#undef");

                //si en esta linea en la que aparece el "#undef" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+6))){
                    return;
                }

                //se obtiene el nombre del define y se indica como no activado en el fichero
                //properties si es que estaba marcado como activado
                mngtUndef(line.substring(index+6));
            }
            else if(line.contains("#ifdef"))
            {
                index = line.indexOf("#ifdef");

                //si en esta linea en la que aparece el "#ifdef" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+6))){
                    return;
                }

                //se gestiona el contenido dentro del #ifdef
                mngtIfdef(buffer_wr, line.substring(index+6));
            }
            else if(line.contains("#ifndef"))
            {
                index = line.indexOf("#ifndef");

                //si en esta linea en la que aparece el "#ifndef" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+7))){
                    return;
                }

                //se gestiona el contenido dentro del #ifndef
                mngtIfndef(buffer_wr, line.substring(index+7));
            }            
            else if(line.contains("#if"))
            {
                index = line.indexOf("#if");

                //si en esta linea en la que aparece el "#if" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+3))){
                    return;
                }

                //se gestiona el contenido dentro del #if
                mngtIf(buffer_wr, line.substring(index+3));
            }
            else if(line.contains("#else"))
            {
                index = line.indexOf("#else");

                //si en esta linea en la que aparece el "#else" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+5))){
                    return;
                }

                //se gestiona el contenido dentro del #else
                mngtElse(buffer_wr, line.substring(index+5));
            }
            else if(line.contains("#elif"))
            {
                index = line.indexOf("#elif");

                //si en esta linea en la que aparece el "#elif" se encuentra comentada, saltamos a 
                //la siguiente linea
                if(Utils.isCommented(line.substring(0,index), line.substring(index+5))){
                    return;
                }
                
                //se gestiona el contenido dentro del #elif
                mngtIf(buffer_wr, line.substring(index+5));
            }
        }
    }
        
    private void mngtDefine(String line) throws Exception{
        Define define;
        
        //se obtiene el nombre del define y su valor (si es que tiene)
        define = getDefineNameAnValueFromLine(line);

        if(define==null){
            throw new Exception("Error getting define from line "+lineInAnalisysNumber+": "+lineInAnalisys);
        }
        
        define.setAsDefined();
        
        this.extractedDefines.add(define);
    }
    
    private void mngtUndef(String line) throws Exception{
        Define define;
        int index = 0;
        
        //se obtiene el nombre del define y su valor (si es que tiene)
        define = getDefineNameAnValueFromLine(line);

        if(define==null){
            throw new Exception("Error getting define from line "+lineInAnalisysNumber+": "+lineInAnalisys);
        }        
        
        index = findDefine(define.getName());
        
        if(index!=-1){
            ((Define)this.extractedDefines.get(index)).setAsUndefined();
        }       
    }
        
    private boolean lineHasAnEndCondition(String line){
        
        int index;
        
        if(line==null){
            return false;
        }
        
        if(line.contains("#endif")){
            index = line.indexOf("#endif");

            //si en esta linea en la que aparece el "#ifdef" se encuentra comentada, saltamos a 
            //la siguiente linea
            if(!Utils.isCommented(line.substring(0,index), line.substring(index+6))){
                return true;
            }
        }
        
        
        return false;
    }
    
    private boolean lineHasAnOpenCondition(String line){
        
        int index;
        
        if(line==null){
            return false;
        }
        
        if(line.contains("#ifdef")){
            index = line.indexOf("#ifdef");

            //si en esta linea en la que aparece el "#ifdef" se encuentra comentada, saltamos a 
            //la siguiente linea
            if(!Utils.isCommented(line.substring(0,index), line.substring(index+6))){
                return true;
            }
        }
        else if(line.contains("#ifndef")){
            index = line.indexOf("#ifndef");

            //si en esta linea en la que aparece el "#ifdef" se encuentra comentada, saltamos a 
            //la siguiente linea
            if(!Utils.isCommented(line.substring(0,index), line.substring(index+7))){
                return true;
            }
        }
        else if(line.contains("#if")){
            index = line.indexOf("#if");

            //si en esta linea en la que aparece el "#ifdef" se encuentra comentada, saltamos a 
            //la siguiente linea
            if(!Utils.isCommented(line.substring(0,index), line.substring(index+3))){
                return true;
            }
        }
        
        return false;
    }
    
    private void mngtIfdef(RandomAccessFile buffer_wr, String line) throws Exception{
        
        Define define;
        
        //se obtiene el nombre del define al que hace referencia el #ifdef
        //logicamente no va a tener valor asi que no se tiene en cuenta esto ultimo
        define = getDefineNameAnValueFromLine(line);

        if(define==null){
            throw new Exception("Error getting define from line "+lineInAnalisysNumber+": "+lineInAnalisys);
        }
        
        //si no esta activado el define al que hace referencia el #ifdef
        //saltamos la condicion entera
        if(!this.isThisDefineEnabled(define.getName())){
                    
            jumpCondition(buffer_wr);
        }
        //si el define al que hace referencia el #ifdef esta activado
        //gestionamos la informacion que contenga
        else
        {
            analizeInstructionsUnderCondition(buffer_wr);            
        }
    }
    
    private void mngtIfndef(RandomAccessFile buffer_wr, String line) throws Exception{
        Define define;
        
        //se obtiene el nombre del define al que hace referencia el #ifdef
        //logicamente no va a tener valor asi que no se tiene en cuenta esto ultimo
        define = getDefineNameAnValueFromLine(line);

        if(define==null){
            throw new Exception("Error getting define from line "+lineInAnalisysNumber+": "+lineInAnalisys);
        }
        
        //si esta activado el define al que hace referencia el #ifndef
        //saltamos la condicion entera
        if(this.isThisDefineEnabled(define.getName())){
                        
            jumpCondition(buffer_wr);
        }
        //si el define al que hace referencia el #ifndef no esta activado
        //gestionamos la informacion que contenga
        else
        {
            analizeInstructionsUnderCondition(buffer_wr);  
        }
    }
    
    private void mngtIf(RandomAccessFile buffer_wr, String line) throws Exception{
        
        //si no se cumple la condicion del #if 
        //saltamos la condicion entera
        if(!isConditionFulfilled(line)){
                        
            jumpCondition(buffer_wr);
        }
        //si se cumple la condicion del #if
        //gestionamos la informacion que contenga
        else
        {
            analizeInstructionsUnderCondition(buffer_wr);  
        }
    }
    
    private void mngtElse(RandomAccessFile buffer_wr, String line) throws Exception{
        
        analizeInstructionsUnderCondition(buffer_wr);
    }
    
    //return the define extracted from line of .h file but it is returned no 
    //indicating if it is defined, only with his name an his value if his value is found in the line
    private Define getDefineNameAnValueFromLine(String line) throws Exception
    {
        String[] aLine;
        String subLine;
        String myline;
        Define define = null;
        Define otherDefine = null;
        int indexOfDefine = 0;
        boolean canBeAConditionAfterDefine = false;
        String[] asubLine;
        
        myline = line;
        
        if(myline.contains("//")){
            myline = myline.substring(0,myline.indexOf("//"));
        }
        
        while(myline.contains("/*")){
            myline = myline.substring(0,myline.indexOf("/*"));
            if(myline.contains("*/")){                
                myline += myline.substring(myline.indexOf("*/")+2);
            }
        }
        
        myline = myline.replaceAll(" ", ":").replaceAll("\r", ":").replaceAll("\t", ":");
        
        aLine = myline.split(":");
        
        for(int i=0;i<aLine.length;i++)
        {
           subLine = aLine[i];
           
           if(subLine!=null){
               if(!subLine.equals("")){
                   
                   //si no se ha obtenido el nombre del define
                   if(define==null){
                       //si se trata de un nombre y no de un operador u otra cosa
                       if(!Utils.isAnOperator(subLine)){
                           
                           define = new Define(subLine);
                           
                           indexOfDefine = i;
                       }
                   }
                   //si ya se ha obtenido el nombre del define
                   //se obtiene su valor si es que tiene y no se ha obtenido aun
                   else if(!define.hasValue()){
                       
                       //si se trata de un nombre o valor
                       if (!Utils.isAnOperator(subLine)) {
                           if (subLine.startsWith("0x")) {
                               
                               subLine = subLine.substring(2);
                               
                               define.setValue(Integer.parseInt(subLine, 16));
                                       
                           } else if(isAnumber(subLine)){
                               
                               define.setValue(Integer.parseInt(subLine, 10));
                           }
                           else{
                               canBeAConditionAfterDefine = true;                               
                           }
                       }
                   }
               }
           }
        }
        
        //if the define name have been recognized and if it is possible a condition to determinate the define value
        if (define != null && canBeAConditionAfterDefine && !define.hasValue()) {
                        
            subLine = "";
            
            for(int i=indexOfDefine+1;i<aLine.length;i++){
               subLine += aLine[i];
            }
            
            try{
                asubLine = Utils.convertToArrayString(subLine);            

                Condition condition = new Condition();
                condition.setParentCondition(asubLine);
                condition.setCondition(asubLine);

                condition = this.analizeFullCondition(condition);

                if (!condition.hasResultCondition()) {
                    throw new Exception("Error getting define from line " + lineInAnalisysNumber + ": " + lineInAnalisys);
                }

                if (!(condition.getResultCondition() instanceof NumericalVariable)) {
                    throw new Exception("Error getting define from line " + lineInAnalisysNumber + ": " + lineInAnalisys);
                }
                
                define.setValue(((NumericalVariable) condition.getResultCondition()).getValue());
                
            }catch(Exception e){
                //En lugar de dar la excepcion actualmente se ha optado por seguir adelante e indicar que si esta activado 
                //el define. Ya que lo que viene despues del define debera ser una condicion que resulte en un valor
                //numerico y si este calculo no se puede realizar porque se hace referencia a un define o funcion que no se 
                //encuentra dentro del .h que se esta analizando, en lugar de dar error y no seguir hacia adelante, se asume
                //que se encuentra activo el define con un valor desconocido, es decir, como si se hubiera definido sin valor.
            }
        }
        
        return define;
    }
    
    private boolean isAnumber(String thing){
        
        int thingSize;
        String carac;
        int numberOfNumbers = 0;
        String mything = thing.replaceAll(" ", "");
        
        if(thing==null){
            return false;
        }
        
        if(thing.equals("")){
            return false;
        }
        
        thingSize = mything.length();
        
        if(mything.startsWith("-")||mything.startsWith("0x")){
            return true;
        }
        
        for(int i=0;i<thingSize;i++){
            carac = mything.substring(i,i+1);

            if(carac.equals("0")
                ||carac.equals("1")
                ||carac.equals("2")
                ||carac.equals("3")
                ||carac.equals("4")
                ||carac.equals("5")
                ||carac.equals("6")
                ||carac.equals("7")
                ||carac.equals("8")
                ||carac.equals("9")
                ){
                numberOfNumbers++;
            }
        }
        
        return numberOfNumbers==thingSize;
    }
    
    private boolean isConditionFulfilled(String[] sArrayWithCondition) throws Exception{
        Object conditionResult;        
        Statement gerenalStatment = new Statement(this.extractedDefines);     
        int index = 0;
        Condition subCondition;
        Condition subsubCondition;
        boolean oneSubConditionAtLeast = false;
        
        do{
            subCondition = getSubCondition(sArrayWithCondition, index);

            if(subCondition!=null){
                
                //at least the complete condition has one subcondition between parentheses 
                oneSubConditionAtLeast = true;
                        
                subsubCondition = analizeSubCondition(subCondition.getCondition());
                
                if(subsubCondition!=null){
                    if(subsubCondition.hasResultCondition()){
                        gerenalStatment.addElement(subsubCondition.getResultCondition());
                    }
                    else{
                        gerenalStatment.addElement(subsubCondition.getCondition());
                    }
                }
                else{
                    gerenalStatment.addElement(subCondition.getCondition());
                }
                
                index += subCondition.getIndexInParent()+subCondition.getCondition().length+1;
                
                if(subCondition.getIndexInParent()+subCondition.getCondition().length+1<sArrayWithCondition.length){
                    gerenalStatment.addElement(sArrayWithCondition[subCondition.getIndexInParent()+subCondition.getCondition().length+1]);
                    
                    index += 1;
                }                
            }
            
        }while(subCondition!=null);
        
        if(!oneSubConditionAtLeast){
            gerenalStatment.addElement(sArrayWithCondition);
        }
        
        if(!gerenalStatment.hasElements()){
            throw new Exception("Error analizing condition in line "+lineInAnalisysNumber+" : "+lineInAnalisys);
        }
        
        conditionResult = gerenalStatment.getStatmentResult();
        
        return ((LogicalVariable)conditionResult).getValue();
    }
    
    private Condition analizeSubCondition(String[] condition) throws Exception{     
        Statement gerenalStatment = new Statement(this.extractedDefines);  
        Condition subsubCondition;
        int index = 0;
        Condition subCondition;
        Object conditionResult = null;
        Condition resCondition = null;
        
        do{
            subCondition = getSubCondition(condition, index);

            if(subCondition!=null){

                subsubCondition = analizeSubCondition(subCondition.getCondition());
                
                if(subsubCondition!=null){
                    if(subsubCondition.hasResultCondition()){
                        gerenalStatment.addElement(subsubCondition.getResultCondition());
                    }
                    else{
                        gerenalStatment.addElement(subsubCondition.getCondition());
                    }
                }
                else{
                    gerenalStatment.addElement(subCondition.getCondition());
                }
                
                index += subCondition.getIndexInParent()+subCondition.getCondition().length+1;

                if(subCondition.getIndexInParent()+subCondition.getCondition().length+1<condition.length){
                    gerenalStatment.addElement(condition[subCondition.getIndexInParent()+subCondition.getCondition().length+1]);
                    
                    index += 1;
                }  
            }
            
        }while(subCondition!=null);
        
        if(gerenalStatment.hasElements()){
            conditionResult = gerenalStatment.getStatmentResult();
            
            resCondition = new Condition();
        
            resCondition.setParentCondition(condition);
            resCondition.setCondition(condition);
            
            resCondition.setResultCondition(conditionResult);
        }
        
        return resCondition;
    }
    
    private Condition analizeFullCondition(Condition condition) throws Exception{     
        Statement gerenalStatment = new Statement(this.extractedDefines);  
        Condition subsubCondition;
        int index = 0;
        Condition subCondition;
        Object conditionResult = null;
        boolean oneSubConditionAtLeast = false;
        Condition resCondition = new Condition();
        
        resCondition.setParentCondition(condition.getCondition());
        resCondition.setCondition(condition.getCondition());
        
        do{
            subCondition = getSubCondition(condition.getCondition(), index);
            
            if(subCondition!=null){
                
                oneSubConditionAtLeast = true;
                
                subsubCondition = analizeSubCondition(subCondition.getCondition());
                
                if(subsubCondition!=null){
                    if(subsubCondition.hasResultCondition()){
                        gerenalStatment.addElement(subsubCondition.getResultCondition());
                    }
                    else{
                        gerenalStatment.addElement(subsubCondition.getCondition());
                    }
                }
                else{
                    gerenalStatment.addElement(subCondition.getCondition());
                }
                
                index += subCondition.getIndexInParent()+subCondition.getCondition().length+1;

                if(subCondition.getIndexInParent()+subCondition.getCondition().length+1<condition.getCondition().length){
                    gerenalStatment.addElement(condition.getCondition()[subCondition.getIndexInParent()+subCondition.getCondition().length+1]);
                    
                    index += 1;
                }  
            }
            
        }while(subCondition!=null);
        
        if(!oneSubConditionAtLeast){
            gerenalStatment.addElement(condition.getCondition());
        }
        
        if(gerenalStatment.hasElements()){
            conditionResult = gerenalStatment.getStatmentResult();
            
            resCondition.setResultCondition(conditionResult);
        }
        
        return resCondition;
    }        
    
    private Condition getSubCondition(String[] parentCondition, int fromIndex) throws Exception{
        
        int indexClose = 0;
        int indexStart = 0;
        Condition condition = null;        
        String[] subCondition = null;
        boolean thereAreParentheses = false;
        int indexInParent = 0;
        
        for(int i=fromIndex;i<parentCondition.length;i++){
                        
            if(parentCondition[i].equals("(")){
                
                thereAreParentheses = true;
                        
                indexStart = i;
                
                indexClose = findHisCloseParenthesis(parentCondition, i+1);
                
                if(indexClose==i+1){
                    throw new Exception("Statment can't be recognized in line "+this.lineInAnalisysNumber+" "+this.lineInAnalisys);
                }
                
                subCondition = new String[indexClose-i-1];
                
                for(int j=i+1;j<indexClose;j++){
                    subCondition[j-(i+1)] = parentCondition[j];
                } 
                
                indexInParent = indexStart+1;
                        
                break;
            }
        }
    
        //if there aren't parentheses meaby there are a operator which divides conditions
        if(!thereAreParentheses && (fromIndex>0) && (fromIndex<=parentCondition.length-1)){
                        
            if (parentCondition[fromIndex-1].equals(Operator.L_OR)
                    || parentCondition[fromIndex-1].equals(Operator.L_AND)
                    || parentCondition[fromIndex-1].equals(Operator.R_GREATER_OR_EQUAL)
                    || parentCondition[fromIndex-1].equals(Operator.R_GREATER)
                    || parentCondition[fromIndex-1].equals(Operator.R_EQUAL)
                    || parentCondition[fromIndex-1].equals(Operator.R_LESS)
                    || parentCondition[fromIndex-1].equals(Operator.R_LESS_OR_EQUAL)
                    || parentCondition[fromIndex-1].equals(Operator.R_NOT_EQUAL)) {
                
                subCondition = new String[parentCondition.length-fromIndex];
                
                for(int j=fromIndex;j<parentCondition.length;j++){
                    subCondition[j-fromIndex] = parentCondition[j];
                } 
                
                indexInParent = fromIndex;
            }
            
        }
        
        if(subCondition!=null){
            condition = new Condition();
            condition.setCondition(subCondition);
            condition.setParentCondition(parentCondition);
            condition.setIndexInParent(indexInParent);
        }
        
        return condition;
    }
    
    private boolean isConditionFulfilled(String lineWithCondition) throws Exception{
        
        boolean conditionResult = false;
        String[] aLine;
        
        //obtengo un array de String. En cada posicion del array
        //esta cada operador, parentesis o nombre de define que aparezca en 
        //la linea de condicion del #if o #Elseif
        aLine = Utils.convertToArrayString(lineWithCondition);
        
        conditionResult = isConditionFulfilled(aLine);
       
        return conditionResult;
        
    }
    
    private int findHisCloseParenthesis(String[] asCond, int fromIndex){
        
        int numCloseParenthesis = 0;
        int numOpenParenthesis = 0; 
               
        for(int i=fromIndex;i<asCond.length;i++){
            
            if(asCond[i].equals("(")){
                numOpenParenthesis++;
            }
            else if(asCond[i].equals(")")){
                numCloseParenthesis++;
            }            
            
            if(numOpenParenthesis<numCloseParenthesis){
                return i;
            }
        }
        
        return fromIndex;
    }
    
    private String readNewLineFromFile(RandomAccessFile buffer_wr) throws Exception{
        String line;
        
        line = buffer_wr.readLine();
        
        this.lineInAnalisys = line;
        
        this.lineInAnalisysNumber++;
        
        return line;
    }
    
    private void jumpCondition(RandomAccessFile buffer_wr) throws Exception {
        
        String line;
        boolean endCondition = false;
        int numOpenConds = 0;
        int numEndConds = 0;
        
        do {
            line = readNewLineFromFile(buffer_wr);

            if (lineHasAnOpenCondition(line)) {
                numOpenConds++;
            } else if (lineHasAnEndCondition(line)) {
                numEndConds++;

                if (numEndConds > numOpenConds) {
                    endCondition = true;
                }
            }

        } while (line != null && !endCondition);
    }
    
    private void analizeInstructionsUnderCondition(RandomAccessFile buffer_wr) throws Exception{
        
        String line;
        boolean endCondition = false;
        
        do {
            line = readNewLineFromFile(buffer_wr);
            
            if(line == null){
                return;
            }
            
            if (lineHasAnEndCondition(line)) {
                endCondition = true;
            }
            else{
                recognizeLine(buffer_wr, line);
            }

        } while (!endCondition);
    }
}
