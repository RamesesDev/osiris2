package com.rameses.web.support;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ResponseWrapper extends HttpServletResponseWrapper {
    
    protected HttpServletResponse origResponse = null;
    protected ServletOutputStream stream = null;
    protected PrintWriter writer = null;
    
    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
    }
    
    public ServletOutputStream createOutputStream() throws IOException {
        if( (origResponse.getContentType()+"").contains("text"))
            return (new GZIPResponseStream(origResponse));
        else
            return origResponse.getOutputStream();
    }
    
    public PrintWriter createWriter() throws IOException {
        if( (origResponse.getContentType()+"").contains("text") ) {
            OutputStream s = createOutputStream();
            
            //i used ISO-8859-1 charset to support latin characters
            //return new PrintWriter(new OutputStreamWriter(s, "ISO-8859-1"));
            
            return new PrintWriter(s); //use the default charset
        }
        else {
            return origResponse.getWriter();
        }
    }
    
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {}
    }
    
    public void flushBuffer() throws IOException {
        stream.flush();
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }
        
        if (stream == null)
            stream = createOutputStream();
        return stream;
    }

    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return (writer);
        }
        
        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }
        
        writer = createWriter();
        
        return writer;
    }
    
    public void setContentLength(int length) {}
    
}
