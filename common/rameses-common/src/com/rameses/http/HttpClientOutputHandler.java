/*
 * HttpClientOutputHandler.java
 *
 * Created on September 19, 2011, 9:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.http;

import java.io.InputStream;

/**
 *
 * @author jzamss
 */
public interface HttpClientOutputHandler {
    Object getResult(InputStream is);
}
