/*
 * CustomActionManager.java
 *
 * Created on July 10, 2012, 11:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ActionCommand;
import com.rameses.anubis.ActionManager;
import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.Module;
import com.rameses.anubis.Project;
import com.rameses.io.StreamUtil;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class CustomActionManager extends ActionManager {
    
    private static String ACTION_DIR = "/actions/";
    
    public CustomActionManager(Project project) {
        super(project);
    }
    
    protected ActionCommand createActionCommand(final String name) throws Exception {
        InputStream is = null;
        is = ContentUtil.findResource( project.getUrl()+ACTION_DIR+name );
        if( is ==null ) {
            Module defaultModule = project.getSystemModule();
            is = defaultModule.getActionResource(name);
        }
        if( is == null )
            throw new Exception("Action file " + name + " not found");
        
        GroovyShell sh = new GroovyShell();
        Script scr = sh.parse( StreamUtil.toString(is) );
        return new MyActionCommand(scr);
    }
    
    private class MyActionCommand extends ActionCommand {
        private Script script;
        public MyActionCommand(Script s) {
            script = s;
        }
        public Object execute(Map params, Map env) throws Exception {
            if( params == null ) params = new HashMap();
            Script sc = script.getClass().newInstance();
            sc.setProperty("PARAMS", params );
            sc.setProperty("PROJECT", project );
            sc.setProperty("SERVICE", project.getServiceManager() );
            if( env != null ) {
                for(Object o : env.entrySet()) {
                    Map.Entry me = (Map.Entry)o;
                    sc.setProperty( me.getKey()+"", me.getValue() );
                }
            }
            return sc.run();
        }
    }
    
}
