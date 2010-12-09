package com.rameses.osiris2.reports.exporter;

import com.rameses.osiris2.reports.*;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * @author jaycverg
 */
public class XlsReportExporter extends ReportExporter{
    
    public JRExporter getExporter() {
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        return exporter;
    }
    
}
