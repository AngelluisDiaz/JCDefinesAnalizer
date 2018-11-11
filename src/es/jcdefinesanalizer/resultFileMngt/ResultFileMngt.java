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
package es.jcdefinesanalizer.resultFileMngt;

import es.jcdefinesanalizer.defines.Define;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author AngelluisDiaz
 */
public class ResultFileMngt {
    
    private File resultPropFile;
    private final static String ownerNameKey = "Owner";
    private String ownerNameBeforeDefines = "";
    private String commonOwner;
    public final static String YES = "Y";
    public final static String NO = "N";
    
    /**
     * Constructor indicating the absolut path of the desired properties file with the list of defines analized (or extradited).
     * @param resultPropFile Path del directorio de la prueba que se esta ejecutando.
     * @exception Exception if an exception has occurred.
     */
    public ResultFileMngt(String resultPropFile) throws Exception{
        
        try
        {            
            this.resultPropFile = new File(resultPropFile);

            //Si no existe se crea
            if (!this.resultPropFile .exists()) {             
                if (!this.resultPropFile .createNewFile()) {
                    throw new Exception("Properties result file can't be created");
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public boolean setOwnerDefines(String ownerName){
        Properties prop;
        FileInputStream fis;
        FileOutputStream fos;
        
        try
        {
            prop = new Properties();
            
            fis = new FileInputStream(resultPropFile);
            
            prop.load(fis);
            
            fis.close();
            
            prop.setProperty(ownerNameKey, ownerName);
            
            fos = new FileOutputStream(resultPropFile);
            
            prop.store(fos, null);
            
            fos.close();
            
            commonOwner = ownerName;
            
        }catch(Exception e){
            return false;
        }
        
        return true;
    }
    
    /**
     * Save in the result properties file the define indicated.
     * @param define The define
     * @return <code>true</code> if the define is correctly saved in file.
     */
    public boolean setDefine(Define define)
    {
        Properties prop;
        FileInputStream fis;
        FileOutputStream fos;
        String defineName;
        
        if(define==null){
            return false;
        }
        
        defineName = this.ownerNameBeforeDefines+define.getName();
        
        try
        {
            prop = new Properties();
            
            fis = new FileInputStream(resultPropFile);
            
            prop.load(fis);
            
            fis.close();
            
            if(define.isDefined()){
                if(define.hasValue()){
                    prop.setProperty(defineName, String.valueOf(define.getValue()));
                }
                else{
                    prop.setProperty(defineName, YES);
                }
                
            }
            else{
                prop.setProperty(defineName, NO);
            }
            
            fos = new FileOutputStream(resultPropFile);
            
            prop.store(fos, null);
            
            fos.close();
            
        }catch(Exception e){
            return false;
        }
        
        return true;
    }
    
    /**
     * Get the define from result properties file with the extracted defines.
     * @param defineName The define name.
     * @return The define.
     * @exception Exception if an exception has occurred.
     */
    public Define getDefine(String defineName) throws Exception
    {
        Properties prop;
        FileInputStream fis;
        String res;
        Define define;
        String defineNameInFile;
        
        defineNameInFile = this.ownerNameBeforeDefines+defineName;
        
        try
        {
            define = new Define(defineName);
            
            prop = new Properties();
            
            fis = new FileInputStream(resultPropFile);
            
            prop.load(fis);
            
            fis.close();
            
            //si no se encuentran en el fichero properties, se le asignara un valor null
            res = prop.getProperty(defineNameInFile, null);
            
            if(res==null){
                define.setAsUndefined();
            }
            else if(res.equals(YES)){
                define.setAsDefined();
            }
            else if(res.equals(NO)){
                define.setAsUndefined();
            }
            else{
                define.setAsDefined();
                define.setValue(Integer.parseInt(res));
            }
            
        }catch(Exception e){
            throw new Exception("Exception getting the define");
        }
        
        return define;
    }   
    
    /**
     * Get the owner name present in result properties file.
     * @return The owner name present in file.
     */
    public String getOwnerNameInResultPropFile()
    {
        Properties prop;
        FileInputStream fis;
        String ownername;
        
        try
        {            
            prop = new Properties();
            
            fis = new FileInputStream(resultPropFile);
            
            prop.load(fis);
            
            fis.close();
            
            ownername = prop.getProperty(ownerNameKey, null);
            
        }catch(Exception e){
            return null;
        }
        
        return ownername;
    }   
    
    /**
     * Clear the result properties file.
     * @return <code>true</code> if the file is clear.
     */
    public boolean clearResultPropFile()
    {
        Properties prop;
        FileInputStream fis;
        FileOutputStream fos;
        
        try
        {
            prop = new Properties();
            
            fis = new FileInputStream(resultPropFile);
            
            prop.load(fis);
            
            fis.close();
            
            prop.clear();
            
            fos = new FileOutputStream(resultPropFile);
            
            prop.store(fos, null);
            
            fos.close();
            
        }catch(Exception e){
            return false;
        }
        
        return true;
    }
    
    /**
     * Save in the result properties file all the defines indicated overwritten old defines.
     * @param defines The defines to save in properties file.
     * @return <code>true</code> if all defines are correctly saved in file.
     */
    public boolean setDefines(Define[] defines)
    {
        int errors = 0;
        
        if(!clearResultPropFile()){
            return false;
        }
                
        if(this.commonOwner!=null){
            if(!this.setOwnerDefines(commonOwner)){
                return false;
            }
        }
        
        for (Define define : defines) {
            if (!setDefine(define)) {
                errors++;
            }
        }
        
        return errors==0;
    }

    public File getResultPropFile() {
        return resultPropFile;
    }

    public void setResultPropFile(File resultPropFile) {
        this.resultPropFile = resultPropFile;
    }

    public String getOwnerNameBeforeDefines() {
        return ownerNameBeforeDefines;
    }

    public void setOwnerNameBeforeDefines(String ownerNameBeforeDefines) {
        
        if(ownerNameBeforeDefines!=null){
            this.ownerNameBeforeDefines = ownerNameBeforeDefines;
            if(!ownerNameBeforeDefines.equals("")){
                this.ownerNameBeforeDefines += ".";
            }
        }
    }
    
    public Define[] getDefines() throws Exception{
        
        Properties prop;
        FileInputStream fis;
        String defineName;
        Define define;
        ArrayList<Define> defines = new ArrayList();
        Enumeration em;
        String value;
        Define[] res;
        
        try
        {            
            prop = new Properties();
            
            fis = new FileInputStream(resultPropFile);
            
            prop.load(fis);
            
            fis.close();
            
            em = prop.keys();
                        
            while (em.hasMoreElements()) {
                
                defineName = (String) em.nextElement();
                
                if(defineName.equals(ownerNameKey)){
                    continue;
                }
                
                value = prop.getProperty(defineName, null);
                
                if (value == null) {
                    throw new Exception("Properties file has defines without value.");
                }
                
                if(!this.ownerNameBeforeDefines.equals("")){
                    defineName = defineName.substring(ownerNameBeforeDefines.length());
                }
                
                define = new Define(defineName);
                
                if(value.equals(YES)){
                    define.setAsDefined();
                }
                else if(value.equals(NO)){
                    define.setAsUndefined();
                }
                else{
                    define.setAsDefined();
                    define.setValue(Integer.parseInt(value));
                }
                
                defines.add(define);
            }
            
            res = new Define[defines.size()];
        
            res = defines.toArray(res);
            
            return res;
            
        }catch(Exception e){
            throw new Exception(e.getMessage()+" Exception getting all defines");
        }
    }
}
