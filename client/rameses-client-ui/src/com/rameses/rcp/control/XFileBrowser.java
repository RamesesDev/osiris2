/*
 * XFileBrowser.java
 *
 * Created on July 21, 2010, 2:01 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.ActiveControl;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


public class XFileBrowser extends JPanel implements UIInput, Validatable, ActiveControl, ActionListener {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String onAfterUpdate;
    private JFileChooser fchooser;
    private FileFilter filter;
    private String customFilter;
    private boolean multiSelect;
    private boolean listFiles = true;
    private boolean selectFilesOnly = true;
    private String fileNamePattern = ".*";
    private String dialogType = "open";
    private boolean readonly;
    private ControlProperty property = new ControlProperty();
    private ActionMessage actionMessage = new ActionMessage();
    
    private JTextField txtField;
    private JButton btnBrowse;
    
    
    public XFileBrowser() {
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        fchooser = new JFileChooser();
        fchooser.setFileFilter( filter = new XFileBrowserFiler() );
        
        setMultiSelect(false);
        setSelectFilesOnly(true);
        
        txtField = new JTextField();
        txtField.setEditable(false);
        txtField.addActionListener(this);
        txtField.addMouseListener( new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                actionPerformed(null);
            }
        });
        
        btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(this);
        
        setLayout(new BorderLayout());
        add( txtField, BorderLayout.CENTER );
        add( btnBrowse, BorderLayout.EAST );
        
        Dimension d = txtField.getPreferredSize();
        setPreferredSize( new Dimension(100, d.height));
    }
    //</editor-fold>
    
    
    public void actionPerformed(ActionEvent e) {
        if ( !txtField.isFocusable() || !txtField.isEnabled() ) {
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
    
    //<editor-fold defaultstate="collapsed" desc="  refresh/load  ">
    public void refresh() {
        Object value = UIControlUtil.getBeanValue(this);
        if ( value != null ) {
            if ( value.getClass().isArray() ) {
                Object[] arr = (Object[]) value;
                StringBuffer sb = new StringBuffer();
                for ( int i = 0; i < arr.length; ++i ) {
                    if ( i > 0 ) sb.append(", ");
                    sb.append(arr[i]+"");
                    txtField.setText( sb.toString() );
                }
                fchooser.setSelectedFiles( (File[]) arr);
            } else {
                txtField.setText( value+"" );
                fchooser.setSelectedFile( (File) value);
            }
            
        } else {
            txtField.setText("");
            if ( multiSelect ) {
                fchooser.setSelectedFiles(null);
            } else {
                fchooser.setSelectedFile(null);
            }
        }
    }
    
    public void load() {
        if ( !ValueUtil.isEmpty(customFilter) ) {
            FileFilter ff = (FileFilter) UIControlUtil.getBeanValue(this, customFilter);
            fchooser.setFileFilter(ff);
        }
    }
    //</editor-fold>
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }

  
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        txtField.setText(name);
    }

    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public Object getValue() {
        return multiSelect? fchooser.getSelectedFiles() : fchooser.getSelectedFile();
    }
    
    public void setValue(Object value) {}
    public boolean isNullWhenEmpty() { return true; }
    
    public String getOnAfterUpdate() {
        return onAfterUpdate;
    }
    
    public void setOnAfterUpdate(String onAfterUpdate) {
        this.onAfterUpdate = onAfterUpdate;
    }
    
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

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        btnBrowse.setEnabled(!readonly);
        btnBrowse.setFocusable(!readonly);
        txtField.setFocusable(!readonly);
    }

    public boolean isReadonly() {
        return readonly;
    }

    public String getCaption() {
        return property.getCaption();
    }

    public void setCaption(String caption) {
        property.setCaption(caption);
    }

    public boolean isRequired() {
        return property.isRequired();
    }

    public void setRequired(boolean required) {
        property.setRequired(required);
    }

    public void validateInput() {
        actionMessage.clearMessages();
        property.setErrorMessage(null);
        if ( isRequired() && ValueUtil.isEmpty(getValue()) ) {
            actionMessage.addMessage("1001", "${0} is required", new Object[]{ getCaption() });
            property.setErrorMessage( actionMessage.toString() );
        }
    }

    public ActionMessage getActionMessage() {
        return actionMessage;
    }

    public ControlProperty getControlProperty() {
        return property;
    }

    public void setRequestFocus(boolean focus) {
        if ( focus ) btnBrowse.requestFocus();
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
