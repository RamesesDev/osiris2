/*
 * TemplateTransformer.java
 *
 * Created on July 16, 2010, 5:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.templates;

import java.io.OutputStream;
import java.io.Serializable;

public interface Template extends Serializable {
    
    Object transform( Object data, OutputStream out );
    
}
