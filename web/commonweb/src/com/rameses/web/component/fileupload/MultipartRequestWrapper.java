package com.rameses.web.component.fileupload;

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


public class MultipartRequestWrapper extends HttpServletRequestWrapper {

   private Map<String,String[]> formParameters;
   private Map<String,FileItem> fileParameters;
   private HttpServletRequest req;

    public MultipartRequestWrapper(HttpServletRequest req) {
        super(req);
        formParameters = new HashMap<String,String[]>();
        fileParameters = new HashMap<String,FileItem>();

        try {
            ServletFileUpload upload = new ServletFileUpload(new ProgressMonitorFileItemFactory(req));
            List list = upload.parseRequest( req );
            for( Object o : list ) {                
                FileItem fi = (FileItem)o;
                if( !fi.isFormField() ) {                    
                    fileParameters.put( fi.getFieldName(), fi );
                }
                else {
                    formParameters.put(fi.getFieldName(), new String[]{ fi.getString() } );
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
