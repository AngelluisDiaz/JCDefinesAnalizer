/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jcdefinesanalizer.utils;

import es.jcdefinesanalizer.operators.Operator;

/**
 *
 * @author AngelluisDiaz
 */
public class Utils {

    public static boolean isAnumber(String thing) {

        int thingSize;
        String carac;
        int numberOfNumbers = 0;
        String mything = thing.replaceAll(" ", "");

        if (thing == null) {
            return false;
        }

        if (thing.equals("")) {
            return false;
        }

        thingSize = mything.length();

        if (mything.startsWith("-") || mything.startsWith("0x")) {
            return true;
        }

        for (int i = 0; i < thingSize; i++) {
            carac = mything.substring(i, i + 1);

            if (carac.equals("0")
                    || carac.equals("1")
                    || carac.equals("2")
                    || carac.equals("3")
                    || carac.equals("4")
                    || carac.equals("5")
                    || carac.equals("6")
                    || carac.equals("7")
                    || carac.equals("8")
                    || carac.equals("9")) {
                numberOfNumbers++;
            }
        }

        return numberOfNumbers == thingSize;
    }

    public static boolean isAnOperator(String element) {

        if (element.equals("defined")
                || element.equals(Operator.A_PLUS)
                || element.equals(Operator.A_SUBTRACT)
                || element.equals(Operator.A_MULTIPLY)
                || element.equals(Operator.A_DIVIDE)
                || element.equals(Operator.A_MODULUS)
                || element.equals(Operator.A_INCREMENT)
                || element.equals(Operator.A_DECREMENT)
                || element.equals(Operator.B_OR)
                || element.equals(Operator.B_AND)
                || element.equals(Operator.B_XOR)
                || element.equals(Operator.B_COMP)
                || element.equals(Operator.B_LEFT)
                || element.equals(Operator.B_RIGHT)
                || element.equals(Operator.L_OR)
                || element.equals(Operator.L_AND)
                || element.equals(Operator.L_NOT)
                || element.equals(Operator.R_GREATER_OR_EQUAL)
                || element.equals(Operator.R_GREATER)
                || element.equals(Operator.R_EQUAL)
                || element.equals(Operator.R_LESS)
                || element.equals(Operator.R_LESS_OR_EQUAL)
                || element.equals(Operator.R_NOT_EQUAL)) {
            return true;
        }
        return false;
    }

    public static String[] convertToArrayString(String line) throws Exception {

        String orgLine;
        String[] baline;
        int orgLineSize;
        String resLine = "";
        String carac;
        int definedLenght = 7;//longitud de "defined"
        int index = 0;
        int lastIndex = 0;
        
        orgLine = line.replaceAll(" ", "").replaceAll("\r", " ").replaceAll("\t", " ");

        orgLineSize = orgLine.length();

        //se eliminan los parentesis que estan rodeando a los defines si es que estan puestos
        //entre la palabra "defined" y el nombre del define. Por ejemplo: "defined(nombreDefine)"
        //quedaria "definednombreDefine"
        if (orgLine.contains("defined")) {
            for (int i = definedLenght; i < orgLineSize; i++) {

                carac = orgLine.substring(i, i + 1);

                if (carac.equals("(")) {
                    if (orgLine.substring(i - definedLenght, i).equals("defined")) {

                        resLine += orgLine.substring(index, i);

                        index = orgLine.indexOf(")", i + 1);

                        resLine += orgLine.substring(i + 1, index);

                        index = index + 1;
                    }
                }
            }
        }

        if (!resLine.equals("")) {

            resLine += orgLine.substring(index);

            orgLine = resLine;

            orgLineSize = orgLine.length();

            resLine = "";
        }

        //analize if this line has commentaries and delete them
        while(orgLine.contains("/*")){
            
            index = orgLine.indexOf("/*");
            lastIndex = orgLine.indexOf("*/", index);
            
            orgLine = orgLine.substring(0,index)+orgLine.substring(lastIndex+2);
        }
        
        while(orgLine.contains("//")){
            
            index = orgLine.indexOf("//");
            
            orgLine = orgLine.substring(0,index);
        }

        orgLineSize = orgLine.length();
        
        //separamos numeros, defines (variables) y operadores con :
        for (int i = 0; i < orgLineSize; i++) {

            carac = orgLine.substring(i, i + 1);

            if (carac.equals("(")) {
                resLine += ":" + carac + ":";
            } else if (carac.equals(")")) {
                resLine += ":" + carac + ":";
            } else if (carac.equals("|")) {
                //si el siguiente tb es un "|"
                //estariamos ante un "||"
                if (orgLine.substring(i + 1, i + 2).equals("|")) {
                    resLine += ":" + "||" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals("&")) {
                //si el siguiente tb es un "&"
                //estariamos ante un "&&"
                if (orgLine.substring(i + 1, i + 2).equals("&")) {
                    resLine += ":" + "&&" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals("+")) {
                //si el siguiente tb es un "+"
                //estariamos ante un "++"
                if (orgLine.substring(i + 1, i + 2).equals("+")) {
                    resLine += ":" + "++" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals("-")) {
                //si el siguiente tb es un "-"
                //estariamos ante un "--"
                if (orgLine.substring(i + 1, i + 2).equals("-")) {
                    resLine += ":" + "--" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals(">")) {
                //si el siguiente tb es un "="
                //estariamos ante un ">="
                if (orgLine.substring(i + 1, i + 2).equals("=")) {
                    resLine += ":" + ">=" + ":";
                    i++;
                } //si el siguiente tb es un ">"
                //estariamos ante un ">"
                else if (orgLine.substring(i + 1, i + 2).equals(">")) {
                    resLine += ":" + ">>" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals("<")) {
                //si el siguiente tb es un "="
                //estariamos ante un "<="
                if (orgLine.substring(i + 1, i + 2).equals("=")) {
                    resLine += ":" + "<=" + ":";
                    i++;
                } //si el siguiente tb es un "<"
                //estariamos ante un "<<"
                else if (orgLine.substring(i + 1, i + 2).equals("<")) {
                    resLine += ":" + "<<" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals("=")) {
                //si el siguiente tb es un "="
                //estariamos ante un "=="
                //y si no seria o ">=" o "<=" y ya estan contemplados
                if (orgLine.substring(i + 1, i + 2).equals("=")) {
                    resLine += ":" + "==" + ":";
                    i++;
                }
            } else if (carac.equals("~")) {
                resLine += ":" + carac + ":";
            } else if (carac.equals("*")) {
                resLine += ":" + carac + ":";
            } else if (carac.equals("/")) {
                resLine += ":" + carac + ":";
            } else if (carac.equals("%")) {
                resLine += ":" + carac + ":";
            } else if (carac.equals("!")) {
                //si el siguiente tb es un "="
                //estariamos ante un "!="
                if (orgLine.substring(i + 1, i + 2).equals("=")) {
                    resLine += ":" + "!=" + ":";
                    i++;
                } else {
                    resLine += ":" + carac + ":";
                }
            } else if (carac.equals("^")) {
                resLine += ":" + carac + ":";
            } else {
                resLine += carac;
            }
        }

        //se crea el array resultante con todos los elementos separado cada uno 
        //en un indice del array
        baline = resLine.split(":");

        int nunOfEmptyString = 0;

        for (int i = 0; i < baline.length; i++) {
            if (baline[i].equals("")) {
                nunOfEmptyString++;
            }
        }

        String[] balineWitoutEmpties = new String[baline.length - nunOfEmptyString];

        index = 0;

        for (int i = 0; i < baline.length; i++) {
            if (!baline[i].equals("")) {
                balineWitoutEmpties[index] = baline[i];
                index++;
            }
        }

        return balineWitoutEmpties;
    }
    
    public static boolean isCommented(String preMark, String postMark){
        
        if(preMark.contains("//") || (preMark.contains("/*") && !preMark.contains("*/")))
        {
            return true;
        }
        
        if(!postMark.contains("/*") && postMark.contains("*/")){
            return true;
        }
        
        int nCommentOpen = 0;
        int nCommentClose = 0;
                
        nCommentOpen = preMark.length() - preMark.replace("/*", "").length();
        nCommentClose = preMark.length() - preMark.replace("*/", "").length();
        
        if(nCommentOpen>nCommentClose){
            return true;
        }
        
        nCommentOpen = postMark.length() - postMark.replace("/*", "").length();
        nCommentClose = postMark.length() - postMark.replace("*/", "").length();
        
        return nCommentOpen<nCommentClose;
    }
}
