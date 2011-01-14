/*
 * ControlProperty.java
 *
 * Created on June 21, 2010, 5:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author jaycverg
 */
public class ControlProperty {
    
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    
    private String caption = "Caption";
    private boolean captionSet;
    private char captionMnemonic;
    private int index;
    private boolean required;
    private int captionWidth = 0;
    private boolean showCaption = true;
    private String errorMessage;
    
    
    public ControlProperty() {;}
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    
    public String getCaption() { return caption; }
    
    public void setCaption(String caption) {
        support.firePropertyChange("caption", this.caption, caption);
        this.caption = caption;
        this.captionSet = true;
    }
    
    public boolean isCaptionSet() { return captionSet; }
    
    public char getCaptionMnemonic() { return captionMnemonic; }
    
    public void setCaptionMnemonic(char captionMnemonic) {
        support.firePropertyChange("captionMnemonic", this.captionMnemonic, captionMnemonic);
        this.captionMnemonic = captionMnemonic;
    }
    
    public int getIndex() { return index; }
    
    public void setIndex(int index) {
        support.firePropertyChange("index", this.index, index);
        this.index = index;
    }
    
    public boolean isRequired() { return required; }
    
    public void setRequired(boolean required) {
        support.firePropertyChange("required", this.required, required);
        this.required = required;
    }
    
    public int getCaptionWidth() { return captionWidth; }
    
    public void setCaptionWidth(int captionWidth) {
        support.firePropertyChange("captionWidth", this.captionWidth, captionWidth);
        this.captionWidth = captionWidth;
    }
    
    public boolean isShowCaption() { return showCaption; }
    
    public void setShowCaption(boolean showCaption) {
        support.firePropertyChange("showCaption", this.showCaption, showCaption);
        this.showCaption = showCaption;
    }
    
    public String getErrorMessage() { return errorMessage; }
    
    public void setErrorMessage(String message) {
        support.firePropertyChange("errorMessage", this.errorMessage, message);
        this.errorMessage = message;
    }
    
}
