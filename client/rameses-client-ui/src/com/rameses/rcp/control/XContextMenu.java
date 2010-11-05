/*
 * XContextMenu.java
 *
 * Created on November 5, 2010, 1:17 PM
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.constant.UIConstants;
import com.rameses.rcp.control.menu.MenuItemProxy;
import com.rameses.rcp.control.menu.MenuProxy;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ControlEvent;
import com.rameses.rcp.framework.EventListener;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.WrapperControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 *
 * @author jaycverg
 */
public class XContextMenu extends JPanel implements UIControl, EventListener {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private String handler;
    private boolean dynamic;
    private String eventName = ControlEvent.RIGHT_CLICK;
    private String menuFor;
    private String menuPosition = UIConstants.MOUSE_POSITION;
    
    private TreeNodeModel nodeModel;
    private JPopupMenu popupMenu;
    private Component target;
    
    
    public XContextMenu() {
        if ( Beans.isDesignTime() ) {
            setOpaque(true);
            setBackground(Color.GREEN);
            setPreferredSize(new Dimension(10,10));
        } else {
            setPreferredSize(new Dimension(0,0));
            setVisible(false);
        }
    }
    
    public void refresh() {
        
    }
    
    public void load() {
        if( ValueUtil.isEmpty(handler) ) {
            throw new IllegalStateException( "XTree Error: A handler must be provided" );
        }
        
        nodeModel = (TreeNodeModel) UIControlUtil.getBeanValue(this, handler);
        binding.getEventManager().addListener(menuFor, this);
        target = (Component) binding.find(menuFor);
        
        popupMenu = new JPopupMenu();
        buildMenus();
    }
    
    private void buildMenus() {
        popupMenu.removeAll();
        for( Node n : nodeModel.fetchNodes( nodeModel.getRootNode() )) {
            if ( n.isLeaf() )
                popupMenu.add( new MenuItemProxy(this, nodeModel, n) );
            else
                popupMenu.add( new MenuProxy(this, nodeModel, n) );
        }
        popupMenu.revalidate();
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void onEvent(ControlEvent evt) {
        if ( !eventName.equals( evt.getEventName() ) ) return;
        
        if ( popupMenu.getComponentCount() == 0 ) return;
        
        Point p = null;
        if ( UIConstants.MOUSE_POSITION.equals(menuPosition) && evt.getSourceEvent() instanceof MouseEvent )
            p = ((MouseEvent) evt.getSourceEvent()).getPoint();
        else
            p = target.getLocation();
        
        if ( target instanceof WrapperControl )
            popupMenu.show( ((WrapperControl)target).getWrappedComponent(), p.x, p.y);
        else
            popupMenu.show(target, p.x, p.y);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
    
    public Binding getBinding() {
        return binding;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public String getMenuFor() {
        return menuFor;
    }
    
    public void setMenuFor(String menuFor) {
        this.menuFor = menuFor;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public String getMenuPosition() {
        return menuPosition;
    }
    
    public void setMenuPosition(String menuPosition) {
        this.menuPosition = menuPosition;
    }
    //</editor-fold>
    
}
