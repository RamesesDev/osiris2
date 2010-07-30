
package com.rameses.web.chart;

import com.rameses.web.common.ResourceUtil;
import java.io.IOException;
import java.rmi.server.UID;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author rameses
 */
public class UIChart extends HtmlInputHidden {
    
    public static final String REFRESH_PARAM = "refresh_chart";
    
    
    public UIChart() {
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        //add javascript resource
        ResourceUtil.addScriptResource("js/jquery.js");
        ResourceUtil.addScriptResource("javascript/graph.js");
        
        AbstractChartHandler gh = (AbstractChartHandler) getAttributes().get("value");
        gh.setAttributes( getAttributes() );
        String fileId = "CHRT" + new UID();
        context.getExternalContext().getSessionMap().put(fileId, gh);
        renderChart(context, gh, fileId);
    }
    
    private void renderChart(FacesContext context, AbstractChartHandler gh, String fileId) throws IOException {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        
        StringBuffer surl = new StringBuffer();
        surl.append( req.getRequestURI() );
        surl.append("?" + ChartPhaseListener.FILE_ID + "=" + fileId);
        surl.append("&" + req.getQueryString());
        
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
        String imgId = clientId + "_img";
        writer.startElement("div", this);
        if(gh.getTopCaption() != null){
            writer.startElement("p", null);
            writer.startElement("h3", null);
            writer.write(gh.getTopCaption());
            writer.endElement("h3");
            writer.endElement("p");
        }
        writer.startElement("img", this);
        writer.writeAttribute("id", imgId, null);
        
        writer.writeAttribute("src", surl.toString(), null);
        writer.endElement("img");
        if (gh.getTimer() != null) {
            String viewId = context.getViewRoot().getViewId();
            writer.write("<script type=\"text/javascript\">");
            writer.write("new AjaxGraph('" + imgId + "', '" + clientId + "'," + gh.getTimer() + ");");
            writer.write("</script>");
        }
        if(gh.getBottomCaption() != null){
            writer.startElement("p", null);
            writer.startElement("h3", null);
            writer.write(gh.getBottomCaption());
            writer.endElement("h3");
            writer.endElement("p");
        }
        writer.endElement("div");
        
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("value", fileId, null);
        writer.endElement("input");
        
    }
    
    public void encodeEnd(FacesContext context) throws IOException {;}
    
    public void decode(FacesContext context) {
        String clientId = getClientId(context);
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        
        if (req.getParameter(REFRESH_PARAM) == null) return;
        if (req.getParameter(clientId) == null) return;
        
        String fileId = req.getParameter(clientId);
        if (fileId == null) return;
        
        AbstractChartHandler gh = (AbstractChartHandler) getAttributes().get("value");
        gh.setAttributes( getAttributes() );
        
        context.getExternalContext().getSessionMap().put(fileId, gh);
        
        //notify the ChartPhaseListener to the abort phase
        req.setAttribute(ChartPhaseListener.ABORT_PHASE4, true);
    }
    
}
