package com.rameses.image.server;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class HttpMultipartRequestWrapper extends HttpServletRequestWrapper {
    
    private Map<String,String[]> formParameters;
    private List fileParameters;
    private HttpServletRequest req;
    
    public HttpMultipartRequestWrapper(HttpServletRequest req) {
        super(req);
        formParameters = new HashMap<String,String[]>();
        fileParameters = new ArrayList<FileItem>();
        
        try {
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            List list = upload.parseRequest( req );
            for( Object o : list ) {
                FileItem fi = (FileItem)o;
                if( !fi.isFormField() ) {
                    if ( fi.getSize() > 0 ) fileParameters.add( fi );
                } else {
                    formParameters.put(fi.getFieldName(), new String[]{ fi.getString() } );
                }
            }
        } catch(FileUploadException fe){
            //do nothing
        }
    }
    
    public List<FileItem> getFileParameters() {
        return fileParameters;
    }
    
    public Map getParameterMap() {
        return formParameters;
    }
    
    public String[] getParameterValues(String name) {
        return formParameters.get( name );
    }
    
    public String getParameter(String name) {
        if( formParameters.get(name)!=null ) {
            return formParameters.get(name)[0];
        }
        return null;
    }
    
    public Enumeration getParameterNames() {
        return new Enumeration() {
            Iterator iter = formParameters.keySet().iterator();
            public boolean hasMoreElements() {
                return iter.hasNext();
            }
            public Object nextElement() {
                return iter.next();
            }
        };
    }
    
}
