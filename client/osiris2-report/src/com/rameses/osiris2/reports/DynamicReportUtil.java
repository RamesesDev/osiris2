
package com.rameses.osiris2.reports;

import com.rameses.rcp.common.Column;
import com.rameses.util.ValueUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
        jd.setName( model.getReportName() );
        jd.setBottomMargin( model.getBottomMargin() );
        jd.setTopMargin( model.getTopMargin() );
        jd.setLeftMargin( model.getLeftMargin() );
        jd.setRightMargin( model.getRightMargin() );
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
                if( type == null || "string".equals(type) ) //string is the default type of Column
                    type = String.class.getName();
                else if ( "decimal".equals(type) )
                    type = BigDecimal.class.getName();
                else if ( "integer".equals(type) )
                    type = Integer.class.getName();
                else if ( "boolean".equals(type) )
                    type = Boolean.class.getName();
                else if ( "double".equals(type) )
                    type = Double.class.getName();
                else if ( "date".equals(type) )
                    type = Date.class.getName();
                else if ( "timestamp".equals(type) )
                    type = Timestamp.class.getName();
                
                if( c.getWidth() == 0 ) c.setWidth(100);
                
                Class typeClass = DynamicReportUtil.class.getClassLoader().loadClass( type );
                jd.addField( createField( c.getName(), typeClass ));
                band.addElement( createStaticText( c.getCaption(), x ,y , c.getWidth(), height, padding, columnStyle ) );
                dataBand.addElement( createTextField( c, typeClass, x, y, c.getWidth(), height, padding, columnStyle ) );
                x += c.getWidth();
            }
            
            band.setHeight( height );
            dataBand.setHeight( height );
            
            jd.setColumnHeader( band );
            jd.setDetail( dataBand );
            
            String orientation = model.getOrientation()+"";
            if( "landscape".equals(orientation.toLowerCase()) ) {
                jd.setOrientation(JasperDesign.ORIENTATION_LANDSCAPE);
            } else {
                jd.setOrientation(JasperDesign.ORIENTATION_PORTRAIT);
            }
            
            if( model.getPageHeight() > 0 )
                jd.setPageHeight( model.getPageHeight() );
            if( model.getPageWidth() > 0 )
                jd.setPageWidth( model.getPageWidth() );
            
            if( !ValueUtil.isEmpty(model.getReportHeader()) ) {
                JRDesignBand header = new JRDesignBand();
                
                JRDesignStyle headerStyle = new JRDesignStyle();
                headerStyle.setName("Header_Style");
                headerStyle.setFontSize( fontSize );
                headerStyle.setBold(true);
                headerStyle.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
                headerStyle.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
                jd.addStyle(headerStyle);
                
                if( model.getReportHeaderHeight() > 0 )
                    height = model.getReportHeaderHeight();
                
                int width = jd.getPageWidth();
                width -= model.getLeftMargin() + model.getRightMargin();
                
                JRDesignStaticText h = createStaticText(model.getReportHeader(), 0, 0, width, height, 3, headerStyle);
                header.addElement(h);
                header.setHeight(height);
                
                jd.setTitle(header);
            }
            
            
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
    
    private static JRDesignElement createTextField(Column c, Class clz, int x, int y, int w, int h, int cellPadding, JRDesignStyle detailStyle) {
        JRDesignExpression exp = new JRDesignExpression();
        exp.setText("$F{" + c.getName() + "}");
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
        
        if( !ValueUtil.isEmpty(c.getFormat()) ) {
            tf.setPattern( c.getFormat() );
        }
        if( !ValueUtil.isEmpty(c.getAlignment()) ) {
            String align = c.getAlignment().toLowerCase();
            if( "right".equals(align) )
                tf.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
            else if ( "center".equals(align) )
                tf.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
            else if ( "justified".equals(align) )
                tf.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED);
            else
                tf.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
            
        }
        
        return tf;
    }
//</editor-fold>
    
    
}
