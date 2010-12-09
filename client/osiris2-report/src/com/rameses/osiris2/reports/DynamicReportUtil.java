
package com.rameses.osiris2.reports;

import com.rameses.rcp.common.Column;
import java.math.BigDecimal;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author rameses : Windhel
 */
public class DynamicReportUtil {
    
    static int fontSize = 8;
    
    public static JasperDesign build(DynamicReportModel model) {
        JasperDesign jd = new JasperDesign();
        jd.setName("TEST");
        jd.setBottomMargin(0);
        jd.setTopMargin(0);
        jd.setLeftMargin(0);
        jd.setRightMargin(0);
        try{
            int x = 0;
            int y = 0;
            int height = 15;
            int padding = 3;
            JRDesignStyle columnStyle = new JRDesignStyle();
            columnStyle.setName("Column_Style");
            columnStyle.setFontSize( fontSize );
            columnStyle.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
            columnStyle.setLeftBorder(JRDesignGraphicElement.PEN_THIN);
            columnStyle.setRightBorder(JRDesignGraphicElement.PEN_THIN);
            columnStyle.setBottomBorder(JRDesignGraphicElement.PEN_THIN);
            columnStyle.setTopBorder(JRDesignGraphicElement.PEN_THIN);
            jd.addStyle(columnStyle);
            
            JRDesignBand band = new JRDesignBand();
            JRDesignBand dataBand = new JRDesignBand();
            for(Object o : model.getColumns()) {
                Column c = (Column) o;
                String type = c.getType();
                if( type == null || type.equals("string") ) //string is the default type of Column
                    type = String.class.getName();
                
                if( c.getWidth() == 0 ) c.setWidth(100);
                
                Class typeClass = DynamicReportUtil.class.getClassLoader().loadClass( type );
                jd.addField( createField( c.getName(), typeClass ));
                band.addElement( createStaticText( c.getName(), x ,y , c.getWidth(), height, padding, columnStyle ) );
                dataBand.addElement( createTextField( "$F{" + c.getName() + "}", typeClass, x, y, c.getWidth(), height, padding, columnStyle ) );
                x += c.getWidth();
            }
            
            band.setHeight( height );
            dataBand.setHeight( height );
            
            jd.setColumnHeader( band );
            jd.setDetail( dataBand );
        }catch(Exception ex) { ex.printStackTrace(); }
        
        return jd;
    }

    //<editor-fold defaultstate="collapsed" desc="----- Helper methods -----">
    
    private static JRField createField(String name, Class clz) {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(clz);
        return field;
    }
    
    private static JRDesignVariable createVariable(String name, String varname, Class clz) {
        JRDesignVariable variable = new JRDesignVariable();
        variable.setName(varname);
        variable.setValueClass(clz);
        variable.setCalculation(variable.CALCULATION_SUM);
        
        JRDesignExpression exp = new JRDesignExpression();
        exp.setValueClass(clz);
        exp.setText("$F{" + name + "}");
        variable.setExpression(exp);
        variable.setResetType(variable.RESET_TYPE_REPORT);
        return variable;
    }
    
    
    private static JRDesignStaticText createStaticText(String text, int x, int y, int width, int height, int padding, JRDesignStyle columnStyle ) {
        JRDesignStaticText st = new JRDesignStaticText();
        st.setText(text);
        st.setX(x);
        st.setY(y);
        st.setWidth( width );
        st.setHeight( height );
        st.setLeftPadding( padding );
        st.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
        st.setStyle(columnStyle);
        return st;
    }
    
    private static JRDesignElement createTextField(String name, Class clz, int x, int y, int w, int h, int cellPadding, JRDesignStyle detailStyle) {
        JRDesignExpression exp = new JRDesignExpression();
        exp.setText(name);
        exp.setValueClass(clz);
        
        JRDesignTextField tf = new JRDesignTextField();
        tf.setExpression(exp);
        tf.setX(x);
        tf.setY(y);
        tf.setWidth( w );
        tf.setHeight(  h );
        tf.setStyle(detailStyle);
        tf.setLeftPadding( cellPadding );
        tf.setBlankWhenNull(Boolean.valueOf(true));
        
        if(clz.equals(BigDecimal.class)) {
            tf.setPattern("#,##0.00");
            tf.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
            tf.setRightPadding( cellPadding );
        }
        
        return tf;
    }
//</editor-fold>
    
    
}
