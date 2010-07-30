
package com.rameses.web.chart;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.io.OutputStream;
import javax.faces.context.FacesContext;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChartPhaseListener implements PhaseListener {
    
    public static final String FILE_ID = "graph_fileID";
    public static final String ABORT_PHASE4 = "_ABORT_PHASE4";
    
    public ChartPhaseListener() {
    }
    
    public void beforePhase(PhaseEvent phaseEvent) {
        if (phaseEvent.getPhaseId() != PhaseId.RESTORE_VIEW) return;
        
        FacesContext context = phaseEvent.getFacesContext();
        if (context.getResponseComplete()) return;
        
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        
        if (req.getParameter(FILE_ID)!=null) {
            String graphId = req.getParameter(FILE_ID);
            
            Map session = context.getExternalContext().getSessionMap();
            AbstractChartHandler h = (AbstractChartHandler) session.get(graphId);

            if (h != null) {
                context.responseComplete();
                HttpServletResponse res = (HttpServletResponse)context.getExternalContext().getResponse();
                OutputStream out = null;
                BufferedImage image = null;
                try {
                    out = res.getOutputStream();
                    int width = Integer.parseInt(h.getWidth());
                    int height = Integer.parseInt(h.getHeight());
                    image =  h.createChart().createBufferedImage(width, height);
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                    encoder.encode(image);
                    out.flush();
                } catch(NumberFormatException ex) {
                    throw new IllegalStateException("[height and width value] for chart must be of integer type.");
                } catch(Exception ex) {
                    throw new IllegalStateException(ex);
                } finally {
                    try { out.close(); } catch(Exception ign){;}
                    image = null;
                    session.remove(graphId);
                }
            }
        }
    }
    
    public void afterPhase(PhaseEvent phaseEvent) {
        /**
         * used when refreshing the chart using ajax
         * - abort phase 4 after the chart component had put the 
         *   chart handler back to the session
         */
        if (phaseEvent.getPhaseId().getOrdinal() != 4) return;
        FacesContext context = phaseEvent.getFacesContext();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        if (req.getAttribute(ABORT_PHASE4) == null) return;
        context.responseComplete();
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
