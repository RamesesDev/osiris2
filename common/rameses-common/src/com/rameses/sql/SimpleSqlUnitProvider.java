/*
 * SimpleSqlUnitProvider.java
 *
 * Created on August 13, 2010, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import com.rameses.io.StreamUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SimpleSqlUnitProvider extends SqlUnitProvider {
    
    public String getType() {
        return "sql";
    }
    
    /**
     * pass an sql name as follows:
     *
     *   customer.sql = will locate the resource and send back sql text immediately 
     *
     *   customer:getData.sql = will search for the resource named customer.
     *      then parses the text until it can find the [bracketed marker]
     *
     *   sample resource: customer.sql
     *   [getData]
     *   select * from table1.data
     *
     *   [getSample] 
     *   update s=? from data
     */
    public SqlUnit getSqlUnit(String name) {
        InputStream is = null;
        try {
            if(name.indexOf(":")>0) {
                String resName = name.substring( 0, name.indexOf(":") );
                String subName = name.substring(name.indexOf(":")+1, name.indexOf(".sql"));
                
                if( getDialect() != null ) {
                    String dname = getDialect().getName();
                    try {
                        is = getConf().getResourceProvider().getResource(resName + "_" + dname + ".sql");
                    }
                    catch(Exception e){}
                }
                if( is == null ) {
                    is = getConf().getResourceProvider().getResource(resName + ".sql");
                }
                
                String txt = getSubSqlUnit( subName, is );
                if(txt.trim().length()==0)
                    throw new RuntimeException( subName + " for resource " + resName + " does not exist!");
                
                return new SqlUnit(txt);
            }
            else {
                if( getDialect() != null ) {
                    String resName = name.replaceAll("\\.sql$", "");
                    String dname = getDialect().getName();
                    try {
                        is = getConf().getResourceProvider().getResource(resName + "_" + dname + ".sql");
                    }catch(Exception e){}
                }
                if( is == null ) {
                    is = getConf().getResourceProvider().getResource(name);
                }
                
                String txt = StreamUtil.toString(is);
                return new SqlUnit(txt);
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    //handle group sql
    private String getSubSqlUnit( String subName, InputStream is ) {
        InputStreamReader isr = null;
        //locate the subName
        BufferedReader br = null;
        String line = null;
        boolean started = false;
        try {
            isr = new InputStreamReader(is);
            br =  new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            while( (line = br.readLine())!=null ) {
                if( line.trim().length() == 0 ) continue;
                if( line.trim().startsWith("#" )) continue;
                if( line.trim().startsWith("[")){
                    if(started) break;
                    if(line.trim().replaceAll("\\[|\\]", "").trim().equals(subName)) {
                        started = true;
                    } 
                    continue;                    
                }
                else if(started) {
                    //always add extra space per each line
                    sb.append( line + " " );
                }
            }
            return sb.toString();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        finally {
            try {isr.close();} catch(Exception ign){;}
            try {br.close();} catch(Exception ign){;}            
        }
        
    }
    
    
    
    
    
    
}
