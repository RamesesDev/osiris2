package com.rameses.client.updates;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class UpdateConf extends DefaultHandler {
    
    private List<ModuleEntry> modules = new ArrayList<ModuleEntry>();
    private Map env = new HashMap();
    private StringBuffer sbuff = new StringBuffer();
    private String confPath;
    private long lastModified;
    
    private String hostPath;
    private String appPath;
    private String appurl;
    
    public UpdateConf(String appurl, String appPath) {
        this.appurl = appurl;
        this.appPath = appPath;
    }
    
    public void init() throws Exception 
    {
        //check malformed url first
        try {
            URL u = new URL(appurl);
        } catch (Exception e) {
            System.out.println("[URL-Error] " + e.getMessage());
        }
        
        int lastIndex = appurl.lastIndexOf("/");
        String appName = appurl.substring(appurl.lastIndexOf("/",lastIndex-1 )+1, lastIndex );
        hostPath = appurl.substring(0, appurl.lastIndexOf("/")+1);

        if (!appPath.endsWith("/")) appPath = appPath + "/";
        
        appPath = appPath + appName;
        
        //create the directories
        File f = new File(appPath);
        if (!f.exists()) f.mkdirs();
        
        confPath = appPath + "/update.xml";
    }
    
    public void load() throws Exception {
        modules.clear();
        env.clear();
        
        File f = new File(confPath);
        if(!f.exists())
            throw new Exception(confPath + " does not exist!");
        
        lastModified = f.lastModified();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object o = CipherUtil.decode((Serializable)ois.readObject());
            bis = new ByteArrayInputStream(o.toString().getBytes());
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(bis, this);
        } catch(Exception ex) {
            f.delete();
            throw ex;
        } finally {
            try { fis.close();  } catch(Exception ign){;}
            try { bis.close();  } catch(Exception ign){;}
        }
    }
    
    public void startElement(String string, String string0, String qName, Attributes attr) throws SAXException {
        if( qName.equals("module")) {
            String name = attr.getValue("file");
            if(name==null) {
                throw new IllegalStateException("module file must not be null");
            }
            String sz = attr.getValue("size");
            String version = attr.getValue("version");
            ModuleEntry me = new ModuleEntry(name,version,sz);
            me.setAppPath(appPath);
            me.setHostPath(hostPath);
            
            //ensure no double entry
            if( getModules().indexOf(me)<0 ) {
                getModules().add(me);
            }
        } else if( qName.equals("env")) {
            sbuff.delete(0, sbuff.length());
        }
    }
    
    
    private void populate(Map map, Attributes attr ){
        for( int i=0; i<attr.getLength();i++) {
            map.put( attr.getQName(i), attr.getValue(i) );
        }
    }
    
    public void characters(char[] c, int i, int i0) throws SAXException {
        sbuff.append(c, i, i0);
    }
    
    public void endElement(String string, String string0, String qName) throws SAXException {
        if(qName.equals("env")) {
            try {
                Properties props = new Properties();
                props.load(new ByteArrayInputStream(sbuff.toString().getBytes()));
                for( Object o : props.entrySet() ) {
                    Map.Entry me = (Map.Entry)o;
                    String key = (me.getKey()+"").trim();
                    String val = (me.getValue()+"").trim();
                    if( val.endsWith(";")) val = val.substring(0, val.length()-1);
                    env.put(key , val  );
                }
            } catch (Exception ign) {;}
        }
    }
    
    public List<ModuleEntry> getModules() {
        return modules;
    }
    
    public Map getEnv() {
        return env;
    }
    
    public long getLastModified() {
        return lastModified;
    }
    
    public void setHostPath(String hostPath) {
        this.hostPath = hostPath;
    }
    
    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }
    
    
    
    public void download() throws Exception {
        
        File f = new File(confPath);
        long modified = 0;
        URLConnection uc = null;
        InputStream is = null;
        ObjectOutputStream oos = null;
        
        try 
        {
            URL u = new URL(appurl);
            uc = u.openConnection();
            //uc.connect();
            
            modified = uc.getLastModified();
            
            if (modified != 0)
            {
                try
                {
                    //check if there is an existing conf then rename it.
                    createTemp();
                } 
                catch(Exception ign) {;} 
                
                is = uc.getInputStream();
                int i = 0;
                StringBuffer sb = new StringBuffer();
                while( (i=is.read())!=-1) {
                    sb.append((char)i);
                }

                Object o = CipherUtil.encode((Serializable)sb.toString());
                oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject( o );
                oos.flush();
            }
        } 
        catch(Exception ex) {
            throw ex;
        } 
        finally 
        {
            try 
            {
                if (uc instanceof HttpURLConnection) 
                    ((HttpURLConnection)uc).disconnect();
            } 
            catch(Exception ign){;}
            
            try { oos.close(); } catch(Exception ign){;}
            try { is.close(); } catch(Exception ign){;}
        }
        
        if (modified != 0)
        {
            //after downloading, reload the conf
            f.setLastModified(modified);
        }
        
        load();
    }
    
    public boolean hasUpdates() 
    {
        URLConnection uc = null;
        InputStream is = null;
        try {
            URL u = new URL(appurl);
            uc = u.openConnection();
            long modified = uc.getLastModified();
            if (modified == 0) return false;
            if( modified == lastModified)
                return false;
            return true;
        } catch(Exception ex) {
            return false;
        } finally {
            try {
                if( uc instanceof HttpURLConnection) {
                    ((HttpURLConnection)uc).disconnect();
                }
            } catch(Exception ign){;}
            try { is.close(); } catch(Exception ign){;}
        }
    }
    
    public boolean exists() {
        return new File(confPath).exists();
    }
    
    public void createTemp() {
        File f = new File(confPath);
        if(f.exists()) {
            File tmp = new File(confPath+"~");
            f.renameTo(tmp);
        }
    }

    public boolean isIncomplete() {
        return new File(confPath+"~").exists();
    }
    
    //revert if incomplete
    public void revert() {
        File f = new File(confPath+"~");
        File f1 = new File(confPath);
        if( f1.exists() ) {
            f1.delete();
        }
        if(f.exists()) {
            f.renameTo(f1);
        }
    }
    
    //removes the temp file
    public void complete() {
        File f = new File(confPath+"~");
        f.delete();
    }
 
    public List<ModuleEntry> getOldModules() {
        List<ModuleEntry> list = new ArrayList<ModuleEntry>();
        list.addAll( this.modules );
        return list;
    }
    
}
