package com.rameses.web.fileupload;
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
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class MultipartRequest extends HttpServletRequestWrapper {
    
    private Map<String,List<String>> formParameters;
    private Map<String,FileItem> fileParameters;
    private HttpServletRequest req;
    
    public MultipartRequest(HttpServletRequest req) {
        super(req);
        formParameters = new HashMap<String,List<String>>();
        fileParameters = new HashMap<String,FileItem>();
        
        try {
            ServletFileUpload upload = new ServletFileUpload(new ProgressMonitorFileItemFactory(req));
            List list = upload.parseRequest( req );
            for( Object o : list ) 
            {
                FileItem fi = (FileItem)o;
                if( !fi.isFormField() ) {
                    fileParameters.put( fi.getFieldName(), fi );
                } else {
                    List<String> params = formParameters.get(fi.getFieldName());
                    if( params == null ) {
                        params = new ArrayList();
                        formParameters.put(fi.getFieldName(), params);
                    }
                    params.add( fi.getString() );
                }
            }
        } 
        catch(FileUploadException fe){
            //do nothing
        }
    }
    
    public Map<String, FileItem> getFileParameters() {
        return fileParameters;
    }
    
    public FileItem getFileParameter(String name) {
        return fileParameters.get(name);
    }
    
    public Map getParameterMap() {
        return formParameters;
    }
    
    public String[] getParameterValues(String name) {
        List l = formParameters.get( name );
        if( l != null )
            return (String[]) l.toArray();
        
        return null;
    }
    
    public String getParameter(String name) {
        List l = formParameters.get(name);
        if( l != null ) {
            return (String) l.get(0);
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
