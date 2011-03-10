package com.rameses.osiris2.web;

import com.rameses.web.updates.UpdateCenter;
import com.rameses.common.CacheProvider;
import com.rameses.osiris2.AppContext;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Osiris2TestStartup extends HttpServlet {
    
    public static String CLIENT_CONF = "CLIENT_CONF";
    public static String APP_ROOT = "APP_ROOT";
    
    private ServletConfig servletConfig;
    
    private static String appRoot;
    
    public Osiris2TestStartup() {
    }
    
    //do not remove this. this is important...
    public ServletConfig getServletConfig() {
        return servletConfig;
    }
    
    public void init(ServletConfig config) throws ServletException {
        servletConfig = config;
        System.out.println("STARTING OSIRIS2 WEB (TEST PLATFORM)");
        
        appRoot = getServletContext().getInitParameter(APP_ROOT);
        if(appRoot==null) {
            appRoot = System.getProperty("jboss.server.temp.dir")+"/osiris2";
        }
        
        appRoot = appRoot + "/testplatform-modules";
        reload();
    }
    
    private void reload() throws ServletException{
        String conf = getServletContext().getInitParameter(CLIENT_CONF);
        
        //default classloader before loading
        ClassLoader defaultLoader = (ClassLoader)getServletContext().getAttribute("DEFAULT_CLASSLOADER");
        if(defaultLoader==null) {
            defaultLoader = Thread.currentThread().getContextClassLoader();
            getServletContext().setAttribute("DEFAULT_CLASSLOADER", defaultLoader);
        }
        
        InputStream is = defaultLoader.getResourceAsStream(conf);
        if( is == null )
            throw new ServletException("Client.conf " + conf + " does not exist.");
        try {
            Properties source = new Properties();
            source.load(is);
            
            Properties props = new Properties();
            for(Map.Entry me: source.entrySet()) {
                props.put(me.getKey(), resolveValue(me.getValue()+""));
            }
            
            String appurl = props.getProperty("app.url");
            if(appurl==null)
                throw new ServletException("app.url does not exist in the client.conf " + conf);
            
            UpdateCenter uc = new UpdateCenter( appurl );
            uc.setAppPath(appRoot);
            
            uc.start();
            
            //load the new ClassLoader
            ClassLoader classLoader = uc.getClassLoader(defaultLoader);
            OsirisWebAppContext ctx = new OsirisWebAppContext(classLoader);
            ctx.setEnv( uc.getEnv() );
            
            //loading the modules...
            ctx.load();
            
            
            getServletContext().setAttribute( AppContext.class.getName(), ctx );
            getServletContext().setAttribute( CacheProvider.class.getName(), new SessionCacheProvider() );
            System.out.println("OSIRIS2 WEB (TEST PLATFORM) STARTED");
            
        } catch(Exception ign){
            ign.printStackTrace();
        }
    }
    
    private String resolveValue(String value) {
        StringBuffer sb = new StringBuffer();        
        Matcher m = Pattern.compile("\\$\\{(.*)\\}").matcher(value);
        boolean found = m.find();
        
        if ( !found ) return value;
        
        while(found) {
            m.appendReplacement(sb, System.getProperty(m.group(1)) );
            found = m.find();
        }
        m.appendTail(sb);
        
        return sb.toString();
    }
    
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reload();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper method  ">
    private static final int BUFF_SIZE = 1024 * 32;
    
    private void copyDirectory(File sourceLocation , File targetLocation) {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            
            try {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);
                
                bis = new BufferedInputStream(in, BUFF_SIZE);
                bos = new BufferedOutputStream(out, BUFF_SIZE);
                
                // Copy the bits from instream to outstream
                byte[] buf = new byte[BUFF_SIZE];
                int len = -1;
                while ((len = bis.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
            } catch(Exception e) {
            } finally {
                try { bis.close(); } catch(Exception e){}
                try { bos.close(); } catch(Exception e){}
            }
        }
    }
    //</editor-fold>
}
