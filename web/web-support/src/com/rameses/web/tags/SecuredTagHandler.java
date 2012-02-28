/*
 * GroovyScriptTagHandler.java
 *
 * Created on February 4, 2012, 10:28 PM
 */

package com.rameses.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

/**
 *
 * @author  jaycverg
 */

public class SecuredTagHandler extends BodyTagSupport 
{
    private String key;

    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
   
}
