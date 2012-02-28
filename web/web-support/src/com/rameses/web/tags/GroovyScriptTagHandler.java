/*
 * GroovyScriptTagHandler.java
 *
 * Created on February 4, 2012, 10:28 PM
 */

package com.rameses.web.tags;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

/**
 *
 * @author  jaycverg
 */

public class GroovyScriptTagHandler extends BodyTagSupport 
{
    private Map<Integer, Script> scriptcache = new Hashtable();
    
    public int doAfterBody() throws JspException 
    {
        String textScript = bodyContent.getString();
        Script script = scriptcache.get(textScript.hashCode());
        if(script == null) {
            GroovyShell gs = null;
            try {
                gs = new GroovyShell(new GroovyClassLoader());
                script = gs.parse(textScript);
                scriptcache.put(textScript.hashCode(), script);
            } 
            catch(Exception e) {
                e.printStackTrace();
            } 
            finally {
                gs = null;
            }
        }

        Binding b = new Binding();
        b.setVariable("request", pageContext.getRequest());
        b.setVariable("response", pageContext.getResponse());
        b.setVariable("application", pageContext.getServletContext());
        b.setVariable("pageContext", pageContext);
        //b.setVariable("LOOKUP_UTIL", new LookupUtil(pageContext.getServletContext()));
        try {
            pageContext.getOut().write("sample message from tag");
            b.setVariable("out", pageContext.getOut());
        } catch (Exception ex) {
            throw new JspException(ex);
        }
        
        Enumeration en = pageContext.getRequest().getAttributeNames();
        String attrname = null;
        while( en.hasMoreElements() ) {
            attrname = (String) en.nextElement();
            b.setVariable(attrname, pageContext.getRequest().getAttribute(attrname));
        }
        
        script.setBinding(b);
        script.run();
        
        return SKIP_BODY;
    }
   
}
