package com.rameses.rcp.common;

import com.rameses.util.ValueUtil;

/**
 *
 * @author Windhel
 * 20110607
 */
public class TextSymbolizer extends RuleSymbolizerModel{
    
    //are default values
    private String label = "title";
    private String fontColor = "#000000";
    private String bgColor = "#FFFFFF";
    private String bgOpacity = "0.0"; 
    private String bgRadius = "1";          //Sets the size of the halo radius in pixels. Default is 1.
    private String fontFamily = "Times";    //Determines the family name of the font to use for the label. Default is Times.
    private String fontStyle = "normal";    //Determines the style of the font. Options are normal, italic, and oblique. Default is normal.
    private String fontWeight = "normal";   //Determines the weight of the font. Options are normal and bold. Default is normal.
    private String fontSize = "10";         //Determines the size of the font in pixels. Default is 10.
    
    public TextSymbolizer() {}
    
    public TextSymbolizer(String label, String fontColor, String bgColor, String bgRadius, String bgOpacity, String fontFamily, String fontStyle, String fontWeight, String fontSize) {
        this.label = label;
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.bgRadius = bgRadius;
        this.fontFamily = fontFamily;
        this.fontStyle = fontStyle;
        this.fontWeight = fontWeight;
        this.fontSize = fontSize;
        this.bgOpacity = bgOpacity;
    }
    
    public TextSymbolizer(String label, String fontColor) {
        this.label = label;
        this.fontColor = fontColor;
    }
    
    public StringBuffer toXml() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("           <TextSymbolizer>\n");
        sb.append("               <Label>\n");
        sb.append("                  <ogc:PropertyName>" + label + "</ogc:PropertyName>\n");
        sb.append("               </Label>\n");
        sb.append("              <Fill>\n");
        sb.append("                <CssParameter name=\"fill\">" + fontColor + "</CssParameter>\n");
        sb.append("              </Fill>\n");
        
        if(!ValueUtil.isEmpty(bgColor)) {
            sb.append("          <Halo>\n");
            sb.append("              <Radius>\n");
            sb.append("                <ogc:Literal>" + bgRadius + "</ogc:Literal>\n");
            sb.append("              </Radius>\n");
            sb.append("              <Fill>\n");
            sb.append("                <CssParameter name=\"fill\">" + bgColor + "</CssParameter>\n");
            sb.append("                <CssParameter name=\"fill-opacity\">" + bgOpacity + "</CssParameter>\n");
            sb.append("              </Fill>\n");
            sb.append("          </Halo>\n");
        }
        
        sb.append("              <Font>\n");
        if(!ValueUtil.isEmpty(fontFamily))
            sb.append("                <CssParameter name=\"font-family\">" + fontFamily + "</CssParameter>\n");
        if(!ValueUtil.isEmpty(fontStyle))
            sb.append("                <CssParameter name=\"font-style\">" + fontStyle + "</CssParameter>\n");
        if(!ValueUtil.isEmpty(fontWeight))
            sb.append("                <CssParameter name=\"font-weight\">" + fontWeight + "</CssParameter>\n");
        if(!ValueUtil.isEmpty(fontSize))
            sb.append("                <CssParameter name=\"font-size\">" + fontSize + "</CssParameter>\n");
        sb.append("              </Font>\n");
        
        sb.append("            </TextSymbolizer>\n");
        
        return sb;
    }
    
}
