package com.rameses.rcp.control.gis;

/**
 *
 * @author Windhel
 * 20110607
 */
public class PolygonSymbolizer extends RuleSymbolizerModel {
    
    //are default values
    private String fillColor = "#000000";
    private String fillOpacity = "1";
    private String strokeColor = "#000000";
    private String strokeWidth = "1";
    
    public PolygonSymbolizer() {}
    
    public PolygonSymbolizer(String fillColor, String fillOpacity, String strokeColor, String strokeWidth) {
        this.fillColor = fillColor;
        this.fillOpacity = fillOpacity;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }
    
    public PolygonSymbolizer(String fillColor, String strokeColor) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
    }
    
    //<editor-fold defaultstate="collapsed" desc=" toXml() ">
    public StringBuffer toXml() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("           <polygonsymbolizer>\n");
        sb.append("              <Fill>\n");
        sb.append("                 <CssParameter name=\"fill\">" + fillColor + "</CssParameter>\n");
        sb.append("                 <CssParameter name=\"fill-opacity\">" + fillOpacity + "</CssParameter>\n");
        sb.append("              </Fill>\n");
        sb.append("              <Stroke>\n");
        sb.append("                 <CssParameter name=\"stroke\">" + strokeColor + "</CssParameter>\n");
        sb.append("                 <CssParameter name=\"stroke-width\">" + strokeWidth + "</CssParameter>\n");
        sb.append("              </Stroke>\n");
        sb.append("           </polygonsymbolizer>\n");
        
        return sb;
    }
    //</editor-fold>
}
