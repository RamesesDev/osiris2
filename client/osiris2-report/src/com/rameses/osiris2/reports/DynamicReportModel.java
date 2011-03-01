
package com.rameses.osiris2.reports;

import com.rameses.rcp.common.Column;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 *
 * @author windhel
 *  modifed by jaycverg
 */
public class DynamicReportModel extends ReportModel {
    
    private String name = "Report_1";
    private List<Column> columns = new ArrayList();
    private Object data;
    private JasperPrint report;
    
    private int topMargin = 0;
    private int leftMargin = 0;
    private int bottomMargin = 0;
    private int rightMargin = 0;
    private String orientation;
    private int pageWidth;
    private int pageHeight;
    
    private String reportHeader;
    private int reportHeaderHeight;
    
    
    public DynamicReportModel(){}
    
    public DynamicReportModel(String name) {
        this.name = name;
    }
    
    public void addColumn(Column c) {
        columns.add(c);
    }
    
    public void addColumn(String name, Class c, int width) {
        Column col = new Column();
        col.setName( name );
        col.setWidth( width );
        col.setType( c.getName() );
        addColumn(col);
    }
    
    public JasperPrint getReport() {
        if ( report == null ) {
            buildReport();
        }
        return report;
    }
    
    public void buildReport() {
        try {
            JasperDesign jd = DynamicReportUtil.build(this);
            JasperReport jr = JasperCompileManager.compileReport( jd );
            ReportDataSource ds = new ReportDataSource( getReportData() );
            report = JasperFillManager.fillReport( jr, new HashMap(), ds );
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List getColumns() {
        return columns;
    }
    
    public Object getReportData() {
        return data;
    }
    
    public void setReportData(Object data) {
        this.data = data;
    }
    
    public String getReportName() {
        return name;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  getters/setters  ">
    public int getTopMargin() {
        return topMargin;
    }
    
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }
    
    public int getLeftMargin() {
        return leftMargin;
    }
    
    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }
    
    public int getBottomMargin() {
        return bottomMargin;
    }
    
    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }
    
    public int getRightMargin() {
        return rightMargin;
    }
    
    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }
    
    public String getOrientation() {
        return orientation;
    }
    
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    public int getPageWidth() {
        return pageWidth;
    }
    
    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }
    
    public int getPageHeight() {
        return pageHeight;
    }
    
    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }
    
    public int getReportHeaderHeight() {
        return reportHeaderHeight;
    }
    
    public void setReportHeaderHeight(int reportHeaderHeight) {
        this.reportHeaderHeight = reportHeaderHeight;
    }
    //</editor-fold>

    public String getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(String reportHeader) {
        this.reportHeader = reportHeader;
    }
}
