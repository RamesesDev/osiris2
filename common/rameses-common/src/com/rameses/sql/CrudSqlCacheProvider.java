/*
 * CrudSqlCacheProvider.java
 *
 * Created on August 3, 2010, 3:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class parses the following to its desired field
 * <table name="tableName">
 *      name1=fieldName1 *    //* represents primary keys
 *      name2=fieldName2
 *      name3=fieldName3
 * </table>
 */
public class CrudSqlCacheProvider extends SqlCacheProvider {
    
    private final static String CREATE  = "create";
    private final static String READ  = "read";
    private final static String UPDATE  = "update";
    private final static String DELETE  = "delete";
    private final static String LIST  = "list";
    
    /** Creates a new instance of CrudSqlCacheProvider */
    public CrudSqlCacheProvider() {
    }
    
    public boolean accept(String name) {
        return name.endsWith(".crud");
    }
    
    public SqlCache createSqlCache(String name) {
        //fix first the name
        String resName = name;
        String action = null;
        String ext = ".crud";
        if( name.endsWith("_"+CREATE+ext) ) action = CREATE;
        else if( name.endsWith("_"+READ+ext) ) action = READ;
        else if( name.endsWith("_"+UPDATE+ext) ) action = UPDATE;
        else if( name.endsWith("_"+DELETE+ext) ) action = DELETE;
        else if( name.endsWith("_"+LIST+ext) ) action = LIST;
        if( action == null )
            throw new IllegalStateException("Please explicitly provide an action for " + name );
            
        String alias = name.substring(0, name.lastIndexOf( "_" + action+ext));
        resName = alias +  ext;

        //System.out.println("resName is " + resName );
        //System.out.println("action is " + action );
        InputStream is = this.getSqlCacheResourceHandler().getResource( resName );
        if( is == null )
            throw new IllegalStateException( "CrudSqlCache error. Resource [" + name + "] does not exist");
        
        //String action = name
        CrudParser cp = new CrudParser(is);
        if(action.equals(CREATE)) return getCreateSqlCache(cp);
        else if(action.equals(READ))return getReadSqlCache(cp);
        else if(action.equals(UPDATE))return getUpdateSqlCache(cp);
        else if(action.equals(DELETE)) return getDeleteSqlCache(cp);
        else if(action.equals(LIST)) return getListSqlCache(cp, alias);
        else
            throw new IllegalStateException("Crud action " + action + " is not supported!");
    }
    
    /**
     * generates a standard insert statement
     */
    private SqlCache getCreateSqlCache( CrudParser cp ) {
        List paramNames = new ArrayList();
        StringBuffer sb = new StringBuffer();
        StringBuffer tail = new StringBuffer();
        sb.append( "INSERT INTO " + cp.tableName + " (");
        tail.append( "(" );
        boolean firstPass = true;
            for(CrudField cf: cp.fields) {
                if(firstPass) 
                    firstPass = false;
                else {
                    sb.append(",");
                    tail.append(",");
                }    
                sb.append( cf.fieldName );
                tail.append( "?");
                paramNames.add( cf.name );
            }
        sb.append( ")");
        tail.append( ")");
        String stmt = sb.append( " VALUES ").append(tail).toString();
        return new SqlCache(stmt,paramNames);
    }
    
    /**
     * generates a select statement with primary key as the finder
     */
    private SqlCache getReadSqlCache( CrudParser cp ) {
        List paramNames = new ArrayList();
        List<CrudField> primKeys = new ArrayList();
        
        StringBuffer sb = new StringBuffer();
        sb.append( "SELECT ");
        boolean firstPass = true;
        for(CrudField cf: cp.fields) {
            if(cf.primary) {
                primKeys.add(cf);
                paramNames.add( cf.name );
            }
            if(firstPass) 
                firstPass = false;
            else {
                sb.append(",");
            }    
            sb.append( cf.fieldName + " AS " + cf.name);
        }
        sb.append( " FROM " + cp.tableName);
        if( primKeys.size()== 0 ) 
            throw new IllegalStateException("There must be at least one primary key for CRUD getReadSqlCache");
            
        sb.append( " WHERE ");
        int i = 0; 
        for(CrudField p: primKeys) {
            if( i>0) sb.append( " AND " );
            sb.append( p.fieldName + "=?" );
            i++;
        }
        String stmt = sb.toString();
        return new SqlCache(stmt,paramNames);
    }
    
    private SqlCache getUpdateSqlCache( CrudParser cp ) {
        List paramNames = new ArrayList();
        List<CrudField> primKeys = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append( "UPDATE " + cp.tableName + " SET ");
        boolean firstPass = true;
        for(CrudField cf: cp.fields) {
            if(cf.primary) {
                primKeys.add(cf);
                continue;
            }
            if(firstPass) 
                firstPass = false;
            else {
                sb.append(",");
            }    
            sb.append( cf.fieldName + "=?" );
            paramNames.add( cf.name );            
        }
        if( primKeys.size()== 0 ) 
            throw new IllegalStateException("There must be at least one primary key for CRUD getReadSqlCache");
            
        sb.append( " WHERE ");
        int i = 0; 
        for(CrudField p: primKeys) {
            if( i>0) sb.append( " AND " );
            sb.append( p.fieldName + "=?" );
            paramNames.add( p.name );
            i++;
        }
        String stmt = sb.toString();
        return new SqlCache(stmt,paramNames);
    }
    
    private SqlCache getDeleteSqlCache( CrudParser cp ) {
        List paramNames = new ArrayList();
        List<CrudField> primKeys = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append( "DELETE " + cp.tableName);
        boolean firstPass = true;
        for(CrudField cf: cp.fields) {
            if(cf.primary) {
                primKeys.add(cf);
                paramNames.add( cf.name );
            }
        }
        if( primKeys.size()== 0 ) 
            throw new IllegalStateException("There must be at least one primary key for CRUD getReadSqlCache");
            
        sb.append( " WHERE ");
        int i = 0; 
        for(CrudField p: primKeys) {
            if( i>0) sb.append( " AND " );
            sb.append( p.fieldName + "=?" );
            i++;
        }
        String stmt = sb.toString();
        return new SqlCache(stmt,paramNames);
    }
    
    /***
     * standard list statement is as follows
     * <br>
     * SELECT ${fields} FROM ( select field1 as name1, field2 as name2 from table ) tbl ${condition}
     */
    private SqlCache getListSqlCache( CrudParser cp , String alias) {
        if(alias ==null ) alias = cp.tableName;
        alias = alias.replaceAll("/", "_");
        List<CrudField> primKeys = new ArrayList();
        List paramNames = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append( "SELECT ${columns} FROM (");
        sb.append( "SELECT ");
        boolean firstPass = true;
        for(CrudField cf: cp.fields) {
            if(firstPass) 
                firstPass = false;
            else {
                sb.append(",");
            }    
            sb.append( cf.fieldName + " AS " + cf.name);
        }
        sb.append( " FROM " + cp.tableName);
        sb.append( " ) " + alias );
        sb.append( " ${condition}");
        
        String stmt = sb.toString();
        return new SqlCache(stmt,paramNames);
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="CRUD PARSER">
    private class CrudParser extends DefaultHandler {
        private StringBuffer sb;
        String tableName;
        List<CrudField> fields = new ArrayList();
        
        public CrudParser(InputStream is ) {
            try {
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                parser.parse( is, this );
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
            finally {
                try { is.close(); } catch(Exception ign){;}
            }
        }
        
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if(qName.equals("table")) {
                tableName = attributes.getValue( "name" );
                sb = new StringBuffer();
            }
        }
        
        public void characters(char[] ch, int start, int length) throws SAXException {
            sb.append( ch, start, length );
        }
        
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(qName.equals("table")) {
                String txt = sb.toString();
                try {
                    StringReader rd = new StringReader(txt);
                    BufferedReader brd = new BufferedReader(rd);
                    String line = null;
                    while( (line=brd.readLine())!=null ) {
                        if(line.trim().length()==0) continue;
                        if(line.trim().startsWith("#"))continue; 
                        
                        //we are parsing the ff: name=fieldName *
                        CrudField crf = new CrudField();
                        crf.name = line.substring(0, line.indexOf("=")).trim();
                        String f = line.substring(line.indexOf("=")+1).trim();
                        if(f.contains("*")) {
                            crf.primary = true;
                            crf.fieldName = f.substring(0, f.indexOf("*")).trim();
                        }
                        else {
                            crf.fieldName = f.trim();
                        }
                        fields.add(crf);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private static class CrudField {
        String name;
        String fieldName;
        boolean primary;
    }
    
    //</editor-fold>
    
    
}
