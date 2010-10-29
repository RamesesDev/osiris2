/*
 * ModuleEntry.java
 *
 * Created on November 24, 2009, 9:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.updates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author elmo
 * States 
 *  0 = no change
 *  1 = create
 *  2 = modify
 *  3 = remove
 *
 */
public class ModuleEntry {
    
    private String name;
    private double version = 0.0;
    private double size = 0.0;
    private String appPath;
    private String hostPath;
    private int state = Status.NO_CHANGE;
    
    
    public ModuleEntry(String n, String v, String sz) {
        this.name = n;
        if(v!=null)this.version = Double.parseDouble(v);
        if(sz!=null) this.size = Double.parseDouble(sz);
    }

    public boolean equals(Object obj) {
        if(obj==null) return  false;
        if(!(obj instanceof ModuleEntry))return false;
        return name.equals(((ModuleEntry)obj).getName());
    }

    public String getName() {
        return name;
    }
    
    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
        if( !this.appPath.endsWith("/")) this.appPath = this.appPath + "/";
    }

    public void setHostPath(String hostPath) {
        this.hostPath = hostPath;
        if( !this.hostPath.endsWith("/")) this.hostPath = this.hostPath + "/";
    }
    
    public void download() throws Exception {
        URLConnection uc = null;
        InputStream is = null;
        FileOutputStream fos = null;
        File file = new File(appPath+name+"~");
        try {
            fos = new FileOutputStream( file );
            URL u = new URL(hostPath + name);
            uc = u.openConnection();
            is = uc.getInputStream();
            int i = -1;
            while((i=is.read())!=-1) {
                fos.write(i);
            }
            fos.flush();
        } 
        catch (Exception e) {
            throw e;
        }
        finally {
            try {
                if( uc instanceof HttpURLConnection ) {
                    ((HttpURLConnection)uc).disconnect();
                }
            } 
            catch (Exception ign) {;}
            try { is.close(); } catch (Exception ign) {;}
            try { fos.close(); } catch (Exception ign) {;}
        }
        
        File successFile = new File(appPath+name);
        file.renameTo(successFile);
    }
    
    public void replace() throws Exception {
        delete();
        download();
    }
    
    public void delete() {
        File f = new File(appPath+name);
        if(f.exists()) f.delete();
    }

    public void update() throws Exception {
        switch( state ) {
            case Status.REMOVE:
                delete();
                break;
                
            case Status.CREATE:
            case Status.MODIFY:
                System.out.println("replacing " + name);   
                delete();
                download();
                break;
                
        }
    }

    public double getVersion() {
        return version;
    }

    public double getSize() {
        return size;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    public URL getURL() throws Exception {
        //for counter measure, ensure that file exists!
        File f = new File(appPath+name);
        if(!f.exists()) {
            this.download();
            
        }
        return f.toURL();
    }

    public int getState() {
        return state;
    }
    
}
