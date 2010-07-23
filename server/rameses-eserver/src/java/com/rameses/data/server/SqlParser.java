package com.rameses.data.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SqlParser {
    
    public SqlParser() {
    }
    
    public static class SqlObjectParser extends DefaultHandler {
        
        private StringBuffer sb = new StringBuffer();
        private SqlObject sqlo;
        
        public SqlObjectParser(SqlObject o ) {
            this.sqlo = o;
        }
        
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if( qName.equals("sql-select") ) {
                sb.delete(0, sb.length());
                String ds = attributes.getValue("datasource");
                if(ds!=null) sqlo.setDatasource(ds);
                String handler = attributes.getValue("handler");
                if( handler !=null) sqlo.setHandler(handler);
                sqlo.setMethod("select");
            }
            else if(qName.equalsIgnoreCase("sql-update")) {
                sb.delete(0, sb.length());
                String ds = attributes.getValue("datasource");
                if(ds!=null) sqlo.setDatasource(ds);
                sqlo.setMethod("update");
            } 
            else if(qName.equalsIgnoreCase("sql-insert")) {
                String target = attributes.getValue("target");
                String mode = attributes.getValue("mode");
                String ds = attributes.getValue("datasource");
                String batchSize = attributes.getValue("batchSize");
                sqlo.setTarget(target);
                if(mode!=null) sqlo.setMode(mode);
                if(ds!=null) sqlo.setDatasource(ds);
                if(batchSize !=null) sqlo.setBatchSize( new Integer(batchSize) );
                sqlo.setMethod("insert");
            } else if( qName.equalsIgnoreCase("columns")) {
                sb.delete(0, sb.length());
            } else if( qName.equalsIgnoreCase( "source")) {
                sb.delete(0, sb.length());
            } else if(qName.equalsIgnoreCase("condition")) {
                sb.delete(0, sb.length());
            }
        }
        
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if( qName.equals("sql-select") ) {
                sqlo.setSource( sb.toString() );
            }
            else if( qName.equals("sql-update") ) {
                sqlo.setSource( sb.toString() );
            }
            else if( qName.equalsIgnoreCase("columns")) {
                BufferedReader r = new BufferedReader( new StringReader( sb.toString() ));
                String s = null;
                try {
                    while( (s = r.readLine() ) != null ) {
                        if( s != null && s.trim().length() > 0  ) {
                            String[] x = new String[2];
                            x[0] = s.substring( 0, s.indexOf("=")  ).trim();
                            x[1] = s.substring( s.indexOf("=")+1 ).trim();
                            sqlo.getFields().add(x);
                        }
                    }
                } catch(Exception ign) {
                    ign.printStackTrace();
                }
            } else if( qName.equalsIgnoreCase( "source")) {
                sqlo.setSource(sb.toString());
            } else if(qName.equalsIgnoreCase("condition")) {
                sqlo.setCondition(sb.toString());
            }
        }
        
        public void characters(char[] ch, int start, int length) throws SAXException {
            sb.append(ch, start, length);
        }
    }
    
    public static SqlCacheBean parseStatement( String s ) {
        return parseStatement(Thread.currentThread().getContextClassLoader().getResourceAsStream( s ));
    }
    
    public static SqlCacheBean parseStatement( InputStream is ) {
        try {
            SqlObject o = new SqlObject();
            SAXParser p = SAXParserFactory.newInstance().newSAXParser();
            p.parse( is, new SqlObjectParser(o) );
            
            SqlCacheBean sqle = new SqlCacheBean();
            sqle.setDatasource( o.getDatasource() );
            sqle.setMethod( o.getMethod() );
            sqle.setHandler(o.getHandler());
            sqle.setBatchSize( o.getBatchSize() );
            if( o.getMethod().equalsIgnoreCase("insert")) {
                sqle.setStatement( createInsertStatement(o) );
            }
            else if( o.getMethod().equalsIgnoreCase("insert")) {
                sqle.setStatement( o.getSource() );
            }
            else {
                sqle.setStatement( o.getSource() );
            }
            return sqle;
        } 
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }
    
    public static String createInsertStatement( SqlObject o ) {
        StringBuffer f1 = new StringBuffer();
        StringBuffer f2 = new StringBuffer();
        
        f1.append("(");
        boolean s = false;
        for( String[] fld: o.getFields() ) {
            if( s ) {
                f1.append(",");
                f2.append(",");
            } else {
                s = true;
            }
            f1.append( fld[0] );
            f2.append( fld[1] );
        }
        f1.append(")");
        
        //start the transformation
        StringBuffer sb = new StringBuffer();
        if( o.getMode()!=null && o.getMode().equalsIgnoreCase("replace") ) {
            sb.append(" REPLACE INTO ");
        } else {
            sb.append( " INSERT " );
            if(o.getMode()!=null ) sb.append( o.getMode() + "\n" );
            sb.append( " INTO  " );
        }
        sb.append( o.getTarget() + "\n");
        sb.append( f1 );
        if( o.getSource()!=null && o.getSource().trim().length()>0) {
            sb.append( " SELECT \n " );
            sb.append( f2 );
            sb.append( " FROM \n");
            sb.append( o.getSource() );
            if( o.getCondition() != null && o.getCondition().trim().length()>0) {
                sb.append( " WHERE ");
                sb.append( o.getCondition() );
            }
        } else {
            sb.append( " VALUES \n " );
            sb.append( "(");
            sb.append( f2 );
            sb.append(")");
        }
        return sb.toString();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="SQL PARSER OBJECT">
    public static class SqlObject {
        
        private String method;
        private String datasource;
        private String target;
        private String mode;
        private List<String[]> fields = new ArrayList();
        private String source;
        private String condition;
        private String handler;
        private Integer batchSize;
        
        public SqlObject() {
        }
        
        public String getTarget() {
            return target;
        }
        
        public void setTarget(String target) {
            this.target = target;
        }
        
        
        public List<String[]> getFields() {
            return fields;
        }
        
        public void setFields(List fields) {
            this.fields = fields;
        }
        
        public String getSource() {
            return source;
        }
        
        public void setSource(String source) {
            this.source = source;
        }
        
        public String getMode() {
            return mode;
        }
        
        public void setMode(String mode) {
            this.mode = mode;
        }
        
        public String getCondition() {
            return condition;
        }
        
        public void setCondition(String condition) {
            this.condition = condition;
        }
        
        public String getDatasource() {
            return datasource;
        }
        
        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        public Integer getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
        }
        
    }
    //</editor-fold>
    
}
