/*
 * AbstractSessionServlet.java
 * Created on September 12, 2011, 4:39 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.common;

import com.rameses.http.HttpConstants;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class ServletUtils {
    
    public static void writeText(HttpServletResponse response, String txt) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType("text/html;charset=UTF-8");
            out.print(txt);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {out.flush();}catch(Exception e) {;}
            try {out.close();}catch(Exception e) {;}
        }
    }
    
    public static void writeObject(HttpServletResponse response, Object data) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(response.getOutputStream());
            out.writeObject(data);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {out.flush(); } catch(Exception ign){;}
            try { out.close(); } catch(Exception ign){;}
        }
    }
    
    public static Object readObject(HttpServletRequest req) {
        ObjectInputStream in = null;
        InputStream is = null;
        try {
            is = req.getInputStream();
            if(is==null) return null;
            in = new ObjectInputStream(is);
            return in.readObject();
        } catch(Exception e) {
            //e.printStackTrace();
            return null;
        } finally {
            try { is.close(); } catch(Exception ign){;}
            try { in.close(); } catch(Exception ign){;}
        }
    }
    
    public static Object getRequestInfo(HttpServletRequest req) {
        if( req.getContentType()!=null && req.getContentType().equalsIgnoreCase(HttpConstants.APP_CONTENT_TYPE) ) {
            Object data = readObject(req);
            return data;
        }
        else {
            //assuming normal get conditions i.e. basic web post/get
            Map m = new HashMap();
            Enumeration e = req.getParameterNames();
            RequestParameterFilter f = (RequestParameterFilter)req.getAttribute(RequestParameterFilter.class.getName());
            while(e.hasMoreElements()) {
                String key =(String)e.nextElement();
                Object val = req.getParameter(key);
                if((val instanceof String) && f != null ) {
                    val = f.filter( (String) val);
                }
                m.put( key, val );
            }
            return m;
        }
    }
    
    
}
