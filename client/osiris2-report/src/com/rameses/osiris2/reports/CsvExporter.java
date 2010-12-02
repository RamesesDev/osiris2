
package com.rameses.osiris2.reports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

public abstract class CsvExporter extends ReportExporter{
    
    public JRExporter getExporter() {
        JRExporter exporterXLS = new JRCsvExporter();
        exporterXLS.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, getDelimiter());
        return exporterXLS;
    }
    
    //overridable
    public String getDelimiter() { return ","; }
    
}
