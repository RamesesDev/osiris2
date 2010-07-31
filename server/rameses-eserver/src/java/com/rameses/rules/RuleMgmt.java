/*
 * RuleMgmt.java
 *
 * Created on July 26, 2010, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import com.rameses.eserver.CONSTANTS;
import com.rameses.eserver.JndiUtil;
import com.rameses.eserver.MultiResourceHandler;
import com.rameses.eserver.ResourceServiceMBean;
import com.rameses.interfaces.RuleProvider;
import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLFilter;
import com.sun.jmx.remote.util.Service;
import groovy.lang.GroovyClassLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 *
 * @author elmo
 */
public class RuleMgmt implements RuleMgmtMBean, Serializable {
    
    private String jndiName;
    private ResourceServiceMBean resourceService;
    private Map<String, RuleProvider> ruleBases = new Hashtable();
    private ClassLoader origClassLoader;
    private String output = System.getProperty("jboss.server.temp.dir");
    
    public RuleMgmt() {
    }
    
    public void start() throws Exception {
        System.out.println("STARTING RULE MANAGEMENT [" + jndiName +"]" );
        InitialContext ctx = new InitialContext();
        resourceService = (ResourceServiceMBean )ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        JndiUtil.bind( ctx,jndiName,this );
        
        RuleBaseResourceHandler rh = new RuleBaseResourceHandler();
        resourceService.scanResources("rules://rulebases", rh);
        //resourceService.get
        origClassLoader = Thread.currentThread().getContextClassLoader();
        for(String path: rh.list) {
            String name = path.substring(path.lastIndexOf("/")+1);
            Iterator iter = Service.providers(RuleProvider.class, origClassLoader);
            if(iter.hasNext()) {
                try {
                    RuleProvider rp = (RuleProvider)iter.next();
                    rp.setName(name);
                    rp.setPath(path);
                    loadRuleProvider(rp, true);
                    ruleBases.put(name, rp);
                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR LOADING " + name + " -"+e.getMessage());
                }
            }
        }
        
    }
    
    private class FileScan implements URLFilter {
        public boolean accept(URL u, String filter) {
            return (filter.endsWith(".groovy"));
        }
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING RULE MANAGEMENT " + "[" + jndiName + "]" );
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx,jndiName );
        ruleBases.clear();
    }
    
    public void deploy() {
    }
    
    public void deploy(String ruleBaseName) {
        
    }
    
    public String getJndiName() {
        return jndiName;
    }
    
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
    
    public class RuleBaseResourceHandler implements MultiResourceHandler {
        List<String> list = new ArrayList();
        public void handle(InputStream is) throws Exception {
            InputStreamReader rd = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(rd);
            String path = null;
            while((path=br.readLine())!=null) {
                list.add(path.substring(0,path.length()-1));
            }
            is.close();
        }
    }
    
    public Object createObject(String name, String className) {
        try {
            RuleProvider rp = ruleBases.get(name);
            return rp.createFact(className);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    private void loadRuleProvider(RuleProvider rp, boolean compileClasses) throws Exception {
        if(compileClasses) {
            String name = rp.getName();
            
            GroovyClassLoader classLoader = new GroovyClassLoader(origClassLoader);
            System.out.println("loading path " + rp.getName());
            System.out.println("compiling now...in output " + output);
            
            String outPath = output+"/rules/"+name;
            File dir = new File(outPath);
            if(!dir.exists()) dir.mkdirs();
            
            classLoader.addClasspath( outPath );
            compileClasses( classLoader,rp.getPath(),outPath );
            rp.setClassLoader(classLoader);
        }
        
        //reloading the provider
        rp.load();
    }
    
    private void compileClasses(GroovyClassLoader classLoader, String path, String outPath) {
        CompilationUnit comp = new CompilationUnit(classLoader);
        CompilerConfiguration cconf = new CompilerConfiguration();
        cconf.setTargetDirectory(outPath);
        comp.configure(cconf);
        
        System.out.println("compiling facts...");
        try {
            URLDirectory d = new URLDirectory(new URL("file://"+path+"/facts"));
            for(URL u : d.list(new FileScan(), classLoader)) {
                comp.addSource(u);
            }
        } catch(Exception e) {
            System.out.println("scanning error. " + e.getMessage());
        }
        
        System.out.println("compiling actions...");
        try {
            URLDirectory d = new URLDirectory(new URL("file://"+path+"/actions"));
            for(URL u : d.list(new FileScan(), classLoader)) {
                comp.addSource(u);
            }
        } catch(Exception e) {
            System.out.println("scanning error. " + e.getMessage());
        }
        comp.compile();
    }
    
    
    public void reload(String name) {
        try {
            RuleProvider rp = ruleBases.get(name);
            loadRuleProvider(rp, true);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void fireRule(String ruleBaseName, List facts) {
        try {
            RuleProvider ws = ruleBases.get(ruleBaseName);
            ws.execute(facts,null,null);
        } catch(Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
    
    
    
    
}
