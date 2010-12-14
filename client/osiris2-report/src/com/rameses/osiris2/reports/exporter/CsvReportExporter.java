
package com.rameses.osiris2.reports.exporter;

import com.rameses.osiris2.reports.*;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

/**
 * @author jaycverg
 */
public class CsvReportExporter extends ReportExporter{
    
    private String delimiter = ",";
    
    public JRExporter getExporter() {
        JRExporter exporterXLS = new JRCsvExporter();
        exporterXLS.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, getDelimiter());
        return exporterXLS;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
