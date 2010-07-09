package com.rameses.platform.interfaces;

import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author jaycverg
 */
public interface Platform {
    
    /** 
     * actionSource must the component that triggers the action,
     * it could be a JButton, JMenu, etc.
     */
    void showWindow(JComponent actionSource, JComponent comp, Map properties);
    void showPopup(JComponent actionSource, JComponent comp, Map properties);
    void showError(JComponent actionSource, Exception e); //shows fatal errors
    boolean showConfirm(JComponent actionSource, Object message); //shows a confirmation message box
    void showInfo(JComponent actionSource, Object message); //shows plain information
    void showAlert(JComponent actionSource, Object message); //shows warning messages
    Object showInput(JComponent actionSource, Object message); //shows an input message box
    MainWindow getMainWindow();
    void setMainWindow(MainWindow mw);
}
