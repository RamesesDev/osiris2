/*
 * Opener.java
 *
 * Created on October 14, 2009, 7:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.rcp.common;

public class FloatingOpener extends Opener {
    
    private String owner;
    
    /**
     * orientation values can be a combination of x y properties relative
     * to its owner
     * ex.:   left center, left top, left bottom,
     *        center top, center center, center bottom,
     *        right top, right center, right bottom
     *
     * if a single value is set like "center", same value will be applied
     * to both directions (ie. "center center")
     */
    private String orientation;
    
    
    public FloatingOpener() {
        super();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  getters/setters  ">
    /**
     * This forces the target to be a floating window
     */
    public String getTarget() {
        return "_floating";
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String getOrientation() {
        return orientation;
    }
    
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    public void setCloseOnFocusLost(Boolean close) {
        getProperties().put("closeOnFocusLost", close);
    }
    
    public Boolean isCloseOnFocusLost() {
        return (Boolean) getProperties().get("closeOnFocusLost");
    }
    
    public void setDraggable(Boolean draggable) {
        getProperties().put("draggable", draggable);
    }
    
    public Boolean isDraggable() {
        return (Boolean) getProperties().get("draggable");
    }
    
    public String getId() {
        if ( owner != null )
            return super.getId() + "_" + owner;
        
        return super.getId();
    }
    //</editor-fold>
    
}
