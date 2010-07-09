/*
 * ChangeManager.java
 *
 * Created on January 3, 2010, 9:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;

import com.rameses.util.PropertyResolver;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


public final class ChangeLog {
    
    private final Set<String> prefix = new HashSet<String>();
    
    private Stack<ChangeEntry> entries = new Stack<ChangeEntry>();
    private CheckChangesHandler changeHandler = new CheckChangesHandler();
    
    public Set getPrefix() {
        return prefix;
    }
    
    public int size() {
        return entries.size();
    }
    
    /**
     * set the bean and fieldNames. The change log checks first if fieldName
     * is a registered entry in the prefixes.
     */
    public void addEntry( Object bean, String fieldName, Object oldValue, Object newValue ) {
        for( String s: prefix ) {
            if( fieldName.startsWith(s) ) {
                PropertyResolver pr = ClientContext.getCurrentContext().getPropertyResolver();
                bean = pr.getProperty(bean, s);
                fieldName = fieldName.replace( s+".", "" );
                break;
            }
        }
        entries.push( new ChangeEntry(bean,fieldName, oldValue, newValue) );
    }
    
    public void clear() {
        clear(null);
    }
    
    public void clear(ChangeLogHandler handler) {
        entries.clear();
        if(handler!=null) handler.clear(this);
    }
    
    public String getDifference() {
        DiffChangeLogOutput out = new DiffChangeLogOutput();
        scan(out);
        return out.toString();
    }
    
    public static final class ChangeEntry {
        
        private Object entity;
        private String fieldName;
        private Object oldValue;
        private Object newValue;
        private Date logtime;
        
        ChangeEntry( Object entity, String fieldName, Object oldValue, Object newValue ) {
            this.entity = entity;
            this.fieldName = fieldName;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.logtime = new Date();
        }
        
        public String getKey() {
            return entity.hashCode() + ":" + fieldName;
        }
        
        public Object getOldValue() {
            return oldValue;
        }
        
        public Object getNewValue() {
            return newValue;
        }
        
        public boolean equals(Object obj) {
            if( obj == null ) return false;
            if(!(obj instanceof ChangeEntry)) return false;
            return getKey().equals( ((ChangeEntry)obj).getKey() );
        }
        
        public int getId() {
            return entity.hashCode();
        }
        
        public Object getEntity() {
            return entity;
        }
        
        public String getFieldName() {
            return fieldName;
        }
        
        public Date getLogtime() {
            return logtime;
        }
        
    }
    
    /***************************
     * for tracking history
     ****************************/
    public static final class ChangeHist {
        
        private Object entity;
        private int id;
        
        //first object is old, second is new...
        private List<FieldEntry> entries = new ArrayList<FieldEntry>();
        
        ChangeHist(int id, Object entity) {
            this.id = id;
            this.entity = entity;
        }
        
        public void addEntry(String fldName, Object oldValue, Object newValue, Date logTime ) {
            FieldEntry fe = new FieldEntry(fldName);
            int i = entries.indexOf(fe);
            if( i < 0 ) {
                entries.add(fe);
            } else {
                fe = entries.get(i);
            }
            fe.addEntry( logTime, oldValue, newValue );
        }
        
        public void scan( ChangeLogHandler handler ) {
            handler.startEntity( entity );
            for(FieldEntry fe: entries) {
                fe.scan( handler );
            }
            handler.endEntity();
        }
        
        public boolean equals(Object obj) {
            if(obj == null ) return false;
            if(!(obj instanceof ChangeHist)) return false;
            return  id == ((ChangeHist)obj).id ;
        }
        
    }
    
    public static final class FieldEntry {
        private String name;
        private Object oldValue;
        private Object newValue;
        
        //three values - logtime, oldValue, newValue
        private List<Object[]> changes = new ArrayList<Object[]>();
        
        FieldEntry(String name) {
            this.name = name;
        }
        public void addEntry(Date logTime, Object ov, Object nv) {
            if(changes.size()==0) this.oldValue = ov;
            this.newValue = nv;
            changes.add(new Object[]{logTime, ov, nv});
        }
        
        public void scan(ChangeLogHandler handler ) {
            handler.startField(name);
            handler.showChange( oldValue, newValue );
            for( Object[] hist : changes ) {
                Date logTime = (Date)hist[0];
                handler.showHistory(logTime, hist[1], hist[2]);
            }
            handler.endField();
        }
        
        public boolean equals(Object obj) {
            if(obj == null ) return false;
            if(!(obj instanceof FieldEntry)) return false;
            return  name.equals( ((FieldEntry)obj).name );
        }
        
        public Object getOldValue() {
            return oldValue;
        }
        
        public Object getNewValue() {
            return newValue;
        }
        
        public List<Object[]> getChanges() {
            return changes;
        }
    }
    
    
    private List<ChangeHist> buildHistory() {
        List<ChangeHist> list = new ArrayList<ChangeHist>();
        Enumeration<ChangeEntry> e = entries.elements();
        while(e.hasMoreElements()) {
            ChangeEntry ce = e.nextElement();
            ChangeHist ch = new ChangeHist( ce.getId(), ce.getEntity());
            int idx = list.indexOf(ch);
            if(idx<0) {
                list.add(ch);
            } else {
                ch = list.get(idx);
            }
            ch.addEntry(ce.getFieldName(), ce.getOldValue(), ce.getNewValue(), ce.getLogtime() );
        }
        return list;
    }
    
    public void scan( ChangeLogHandler handler ) {
        try {
            handler.start();
            for(ChangeHist ch: buildHistory()) {
                ch.scan( handler );
            }
            handler.end();
        } catch(StopScanningException ce) {
            //do nothing
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public String toString() {
        PrintLogOutput output = new PrintLogOutput();
        scan(output);
        return  output.toString();
    }
    
    
    
    
    public boolean hasChanges() {
        scan(changeHandler);
        return changeHandler.getHasChanges();
    }
    
    public void undo() {
        if(entries.empty()) return;
        ChangeEntry ce = entries.pop();
        PropertyResolver pr = ClientContext.getCurrentContext().getPropertyResolver();
        pr.setProperty( ce.getEntity(), ce.getFieldName(), ce.getOldValue());
    }
    
    public void undoAll() {
        while(!entries.empty()) {
            undo();
        }
    }
    
    public void destroy() {
        entries.clear();
        entries = null;
    }
    
    //DIFFERENT HANDLERS
    // <editor-fold defaultstate="collapsed" desc="DEFAULT CHANGE LOG HANDLER">
    public static abstract class DefaultChangeLogHandler implements ChangeLogHandler {
        public void start() {
        }
        
        public void startEntity(Object entity) {
        }
        
        public void startField(String fieldName) {
        }
        
        public void showChange(Object oldValue, Object newValue) {
        }
        
        public void showHistory(Date timeChanged, Object oldValue, Object newValue) {
        }
        
        public void endField() {
        }
        
        public void endEntity() {
        }
        
        public void end() {
        }
        
    }
    //</editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="PRINT OUTPUT HANDLER">
    private class PrintLogOutput extends DefaultChangeLogHandler {
        
        private StringBuffer buffer;
        
        public void start() {
            buffer = new StringBuffer();
            buffer.append( "CHANGE LOG ENTRIES: \n" );
        }
        
        public void startEntity(Object entity) {
            buffer.append( "********************************************\n" );
            buffer.append( "Change Log for class " + entity.getClass() + " id:" + entity.hashCode() + "\n" );
        }
        
        public void startField(String fieldName) {
            buffer.append("  [name: " + fieldName + "]");
        }
        
        public void showChange(Object oldValue, Object newValue) {
            buffer.append( "[old:" + oldValue + ", new:"+newValue + "]\n");
        }
        
        public void showHistory(Date timeChanged, Object oldValue, Object newValue) {
            buffer.append("         [" + timeChanged + ", old:" + oldValue + ", new:" + newValue + "]\n");
        }
        
        public String toString() {
            return buffer.toString();
        }
        
        public void clear(ChangeLog changes) {
        }
        
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="HANDLER FOR CHECKING CHANGES">
    private static final class CheckChangesHandler extends DefaultChangeLogHandler {
        
        private boolean hasChanges;
        
        public void start() {
            hasChanges = false;
        }
        
        public void showChange(Object oldValue, Object newValue) {
            boolean empty1 = empty(oldValue);
            boolean empty2 = empty(newValue);
            if(empty1 && empty2) {
                hasChanges = false;
            } else if( !empty1 ) {
                hasChanges = !oldValue.equals(newValue);
            } else {
                hasChanges = !newValue.equals(oldValue);
            }
            if(hasChanges)
                throw new StopScanningException();
        }
        
        private boolean empty( Object value ) {
            if( value == null ) return true;
            if( (value instanceof String) && (value+"").trim().length() == 0 )
                return true;
            else
                return false;
        }
        
        public boolean getHasChanges() {
            return hasChanges;
        }
        
        public void clear(ChangeLog changes) {
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  DIFFERENCE OUTPUT CHANGE LOG HANDLER  ">
    
    private class DiffChangeLogOutput extends DefaultChangeLogHandler {
        
        private StringBuffer buffer;
        
        public void start() {
            buffer = new StringBuffer();
        }
        
        public void startField(String fieldName) {
            buffer.append(" " + fieldName + ": ");
        }
        
        public void showChange(Object oldValue, Object newValue) {
            buffer.append( oldValue + " -> "+newValue + " ");
        }
        
        public void endField() {
            buffer.append(" | ");
        }
        
        public String toString() {
            return buffer.toString();
        }
        
        public void clear(ChangeLog log) {
        }
        
    }
    //</editor-fold>
    
    //This throwable is used to break the loop
    private static final class StopScanningException extends RuntimeException {
        
    }
    
}
