
package com.rameses.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class ConfigProperties {
    
    private File file;
    
    private Map<String,String> includeEntry = new HashMap<String,String>();
    private List<String> excludeEntry = new ArrayList<String>() ;
    private Properties props;
    
    public ConfigProperties() {;}
    
    public ConfigProperties(URL url) {
        setUrl( url );
    }
    
    public ConfigProperties(File file) {
        setFile( file );
    }
    
    public void update(){
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            readConfig( baos );
            writeConfig( new ByteArrayInputStream( baos.toByteArray() ) );
        }catch(Exception ex){
            throw new IllegalStateException( ex );
        }finally{
            try{ baos.close(); }catch(Exception ign){;}
        }
    }
    
    public void readConfig( OutputStream out ){
        BufferedReader reader = null;
        BufferedWriter writer = null;
        
        try{
            
            writer = new BufferedWriter( new OutputStreamWriter( out ) );
            reader = new BufferedReader( new FileReader( file ) );
            
            String line = null;
            
            while( (line = reader.readLine()) != null ){
                if( line.startsWith("#") || line.trim().length() == 0 ){
                    writer.write( line + "\n" );
                }else{
                    String[] values = line.split("=");
                    line = updateLine( values[0], line );
                    if( line != null ) writer.write( line +"\n");
                }
                
                writer.flush();
            }
            
            Iterator itr = includeEntry.keySet().iterator();
            
            while( itr.hasNext() ){
                String key = (String)itr.next();
                writer.write( key+"="+includeEntry.get( key ) + "\n");
                writer.flush();
            }
            
            includeEntry.clear();
            
        }catch(Exception ex){
            throw new IllegalStateException(ex);
        }finally{
            try{ reader.close(); }catch(Exception ign){;}
            try{ writer.close(); }catch(Exception ign){;}
        }
    }
    
    public void setFile(File file){
        this.file = file;
    }
    
    public void setUrl(String url) throws MalformedURLException{
        setUrl( new URL( url ) );
    }
    
    public void setFile(String file){
        setFile( new File( file ) );
    }
    
    public void put(String name, String entry){
        includeEntry.put( name, entry );
    }
    
    public void remove(String name){
        excludeEntry.add( name );
    }
    
    private String updateLine(String key, String line) {
        if( excludeEntry.remove( key ) ) return null;
        String value = includeEntry.get( key );
        if(  value == null) return  line;
        includeEntry.remove( key );
        return key+"="+value;
    }
    
    private void writeConfig(InputStream is) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try{
            reader = new BufferedReader( new InputStreamReader( is ) );
            writer = new BufferedWriter( new FileWriter( file ) );
            
            String line = null;
            while( (line = reader.readLine() ) != null ){
                writer.write( line +"\n");
                writer.flush();
            }
        }catch(Exception ex){
            throw new IllegalStateException( ex );
        }finally{
            try{ reader.close(); }catch(Exception ign){;}
            try{ writer.close(); }catch(Exception ign){;}
        }
    }
    
    private void setUrl(URL url) {
        try {
            URI uri = url.toURI();
            setFile( new File( uri ) );
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex.getMessage(), ex );
        }
    }
    
    private Properties getProps() throws Exception {
        if(props==null) {
            props = new Properties();
            props.load( new BufferedInputStream( new FileInputStream( file ) ) );
        }
        return props;
    }
    
    public Iterator<Map.Entry<Object, Object>> iterator() throws Exception{
        return getProps().entrySet().iterator();
    }
    
    public Object getProperty(String name) {
        try {
            return getProps().getProperty(name);
        }
        catch(Exception ign) {
            return null;
        }
    }
    
}
