
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
    
    private String name;
    private List<Column> columns = new ArrayList();
    private Object data;
    private JasperPrint report;
    
    
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
}
