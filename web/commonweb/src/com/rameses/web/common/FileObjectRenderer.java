/*
 * FileObjectRenderer.java
 *
 * Created on May 12, 2009, 9:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;

public class FileObjectRenderer implements PhaseListener {
    
    public static final int BUFFER_SIZE = 10240;
    public static final String FILE_ID = "file_id";
    public static final String LINK_ID = "filelink_id";
    
    
    
    public void beforePhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        
        Object obj = null;
        String fileId = null;
        if (req.getParameter(FILE_ID) != null) {
            fileId = req.getParameter(FILE_ID);
            obj = req.getSession().getAttribute(fileId);
        } else if (req.getParameter(LINK_ID) != null) {
            String linkId = req.getParameter(LINK_ID);
            obj = context.getExternalContext().getSessionMap().get(linkId);
        }
        
        if ( obj != null ) {
            try {
                HttpServletResponse resp = (HttpServletResponse) context.getExternalContext().getResponse();
                InputStream is = null;
                
                String filename = null;
                String contentType = null;
                
                if ( obj instanceof FileItem ) {
                    FileItem file = (FileItem) obj;
                    contentType = file.getContentType();
                    filename = file.getName();
                    is = file.getInputStream();
                    
                } else if ( obj instanceof byte[] ) {
                    is = new ByteArrayInputStream( (byte[]) obj);
                }
                
                resp.addHeader("Cache-Control", "max-age=60");
                resp.addHeader("Cache-Control", "public");
                
                if (req.getParameter("filename") != null) {
                    filename = req.getParameter("filename");
                }
                
                String disposition = "inline";
                String download = req.getParameter("download");
                if (download != null && download.equals("true")) {
                    disposition = "attachment";
                }
                resp.setHeader("Content-Disposition", disposition + ";filename=" + filename);
                
                if ( contentType != null ) {
                    resp.setContentType( contentType );
                }
                
                renderResponse(is, resp.getOutputStream());
                context.responseComplete();
            } catch(Exception e){
                throw new IllegalStateException("Error rendering request resource", e);
            }
        }
    }
    
    public void afterPhase(PhaseEvent phaseEvent) {
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods ">
    private void renderResponse(InputStream is, OutputStream os) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            bis = new BufferedInputStream(is, BUFFER_SIZE);
            bos = new BufferedOutputStream(os, BUFFER_SIZE);
            
            int bytesRead = -1;
            while( (bytesRead=bis.read(buffer)) != -1 ) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
            
        } catch (Exception ign) {
            //ignore
        } finally {
            try{bos.close();}catch(Exception ign) {;}
            try{bis.close();}catch(Exception ign) {;}
        }
    }
    //</editor-fold>
    
    
}
