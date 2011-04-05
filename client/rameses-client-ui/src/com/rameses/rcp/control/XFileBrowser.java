/*
 * XFileBrowser.java
 *
 * Created on July 21, 2010, 2:01 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class XFileBrowser extends AbstractIconedTextField {
    
    private JFileChooser fchooser;
    private FileFilter filter;
    private String customFilter;
    private boolean multiSelect;
    private boolean listFiles = true;
    private boolean selectFilesOnly = true;
    private String fileNamePattern = ".*";
    private String dialogType = "open";
    
    
    
    public XFileBrowser() {
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        fchooser = new JFileChooser();
        fchooser.setFileFilter( filter = new XFileBrowserFiler() );
        
        setMultiSelect(false);
        setSelectFilesOnly(true);
        setIcon("com/rameses/rcp/icons/folder_open.png");
        setOrientation( super.ICON_ON_RIGHT );
        setHint("Browse File");
        super.setEditable(false);
    }
    //</editor-fold>
    
    public void actionPerformed() {
        if ( !isFocusable() || !isEnabled() ) {
            return;
        }
        
        int resp = 0;
        if ( "open".equals(dialogType) ) {
            resp = fchooser.showOpenDialog(null);
        } else {
            resp = fchooser.showSaveDialog(null);
        }
        
        if ( resp == JFileChooser.APPROVE_OPTION ) {
            UIInputUtil.updateBeanValue(this);
        }
        refresh();
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  refresh/load  ">
    public void refresh() {
        try {
            if( !isReadonly() && !isFocusable() ) setFocusable(true);
            
            Object value = UIControlUtil.getBeanValue(this);
            if ( value != null ) {
                if ( value.getClass().isArray() ) {
                    Object[] arr = (Object[]) value;
                    StringBuffer sb = new StringBuffer();
                    for ( int i = 0; i < arr.length; ++i ) {
                        if ( i > 0 ) sb.append(", ");
                        sb.append(arr[i]+"");
                        setText( sb.toString() );
                    }
                    fchooser.setSelectedFiles( (File[]) arr);
                } else {
                    setText( value+"" );
                    fchooser.setSelectedFile( (File) value);
                }
                
            } else {
                setText("");
                if ( multiSelect ) {
                    fchooser.setSelectedFiles(null);
                } else {
                    fchooser.setSelectedFile(null);
                }
            }
        } catch(Exception e) {
            //block the input if name is null
            setText("");
            setFocusable(false);
        }
    }
    
    public void load() {
        if ( !ValueUtil.isEmpty(customFilter) ) {
            FileFilter ff = (FileFilter) UIControlUtil.getBeanValue(this, customFilter);
            fchooser.setFileFilter(ff);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setEditable(boolean b) {;}
    
    public Object getValue() {
        return multiSelect? fchooser.getSelectedFiles() : fchooser.getSelectedFile();
    }
    
    public void setValue(Object value) {}
    public boolean isNullWhenEmpty() { return true; }
    
    public String getCustomFilter() {
        return customFilter;
    }
    
    public void setCustomFilter(String customFilter) {
        this.customFilter = customFilter;
    }
    
    public boolean isListFiles() {
        return listFiles;
    }
    
    public void setListFiles(boolean listFiles) {
        this.listFiles = listFiles;
    }
    
    public String getFileNamePattern() {
        return fileNamePattern;
    }
    
    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }
    
    public boolean isMultiSelect() {
        return multiSelect;
    }
    
    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
        fchooser.setMultiSelectionEnabled( multiSelect );
    }
    
    public boolean isSelectFilesOnly() {
        return selectFilesOnly;
    }
    
    public void setSelectFilesOnly(boolean selectFilesOnly) {
        this.selectFilesOnly = selectFilesOnly;
        if ( selectFilesOnly ) {
            fchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        } else {
            fchooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
    }
    
    public String getDialogType() {
        return dialogType;
    }
    
    public void setDialogType(String dialogType) {
        this.dialogType = dialogType;
    }
    
    public boolean isImmediate() {
        return true;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  XFileBrowserFiler (class)  ">
    private class XFileBrowserFiler extends FileFilter {
        
        public boolean accept(File f) {
            if ( f.isDirectory() ) return true;
            if ( !listFiles ) return false;
            
            if ( fileNamePattern.equals(".*") || ValueUtil.isEmpty(fileNamePattern) ) {
                return true;
            }
            
            return f.getName().matches(fileNamePattern);
        }
        
        public String getDescription() {
            return "";
        }
        
    }
    //</editor-fold>
    
}
