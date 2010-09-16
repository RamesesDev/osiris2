/*
 * LineReader.java
 *
 * Created on September 12, 2010, 2:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author elmo
 */
public final class LineReader {
    
    public static void read(InputStream is, Handler handler) {
        InputStreamReader rd = null;
        BufferedReader brd = null;
        try {
            if(handler==null)
                throw new Exception("LineReader error. Handler is required");
            rd = new InputStreamReader(is);
            brd = new BufferedReader(rd);
            String line = null;
            while( (line=brd.readLine())!=null ) {
                if(line.trim().length()==0) continue;
                if(line.trim().startsWith("#"))continue;
                handler.read(line);
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try { brd.close(); } catch(Exception ign){;}
            try { rd.close(); } catch(Exception ign){;}
            try { is.close(); } catch(Exception ign){;}
        }
    }
    
    public static interface Handler {
        void read(String text);
    }

}