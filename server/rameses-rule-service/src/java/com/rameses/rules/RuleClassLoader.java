package com.rameses.rules;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;

public class RuleClassLoader extends ClassLoader {
    
    private static String prefix = "META-INF/rules/";
    
    private static final String TMPDIR = "jboss.server.temp.dir";
    
    private String rulebasename;
    
    private GroovyClassLoader classLoader;
    private Map<String, Class> map = new HashMap<String,Class>();
    private CompilerConfiguration conf; 
    private String output;
    
    public RuleClassLoader(String rbname, ClassLoader c) {
        super(c);
        this.rulebasename = rbname;
        classLoader = new GroovyClassLoader(c);
        output = System.getProperty(TMPDIR)+ "/out";
        File f = new File( output );
        if( !f.exists() )  f.mkdirs();
        classLoader.addClasspath(f.getPath());
        classLoader.setShouldRecompile(true);
        conf = new CompilerConfiguration();
        conf.setTargetDirectory(f.getPath());
    }
    
    protected Class findClass(String c ) throws ClassNotFoundException {
        if( c.startsWith("facts.") ) {
            //do not enter if it is a ShadowProxy OR if it is a package name
            //To check if it is a package name it has only 1 dot. example facts.tc
            if(c.matches(".*ShadowProxy.*") || !c.substring(c.indexOf(".")+1).contains(".") ) {
                try {
                    return super.findClass(c);
                }
                catch(ClassNotFoundException ex) {
                    throw ex;
                }
            }
            else if( !map.containsKey(c)) {
                try {
                    String filename = c.replaceAll("\\.", "/");
                    String path = prefix + filename;
                    CompilationUnit comp = new CompilationUnit(classLoader);
                    comp.configure(conf);
                    comp.addSource( Thread.currentThread().getContextClassLoader().getResource(path) );
                    comp.compile();
                    Class cls = classLoader.loadClass(c,true,false);
                    System.out.println("LOADED CLASS ->" + cls );
                    map.put(c, cls);
                }
                catch(Exception ign) {
                    return super.findClass(c);
                }
            }
            return map.get(c);
        } else {
            return super.findClass(c);
        }
    }
    
    public void flushAll() {
        map.clear();
    }
    
    
    
}
