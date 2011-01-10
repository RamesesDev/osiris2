/*
 * XPanel.java
 *
 * Created on November 6, 2010, 2:00 PM
 *
 */

package com.rameses.rcp.control;

import com.rameses.common.ExpressionResolver;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.support.ThemeUI;
import com.rameses.rcp.ui.ControlContainer;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author jaycverg
 */
public class XPanel extends JPanel implements UIControl, ControlContainer {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String visibleWhen;
    private String emptyText;
    
    private boolean contentVisible = true;
    private LayoutManager oldLayout;
    private JLabel label = new JLabel();
    private boolean hasLabel;
        
    
    public XPanel() {
        label.setFont( ThemeUI.getFont("XLabel.font") );
        label.setVerticalAlignment(SwingConstants.TOP);
    }
    
    public void refresh() {
        if ( ValueUtil.isEmpty(visibleWhen) ) return;
        
        ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
        Object res = er.evaluate(binding.getBean(), visibleWhen);
        if ( "false".equals(res+"") && contentVisible )
            updateContent(false);
        else if ( !contentVisible )
            updateContent(true);
        
        
        //temporary solution. to support parent whose layout is a CardLayout.
        //There is a limitation to this solution. The XPanel's Card name must be the
        //name of the component. i.e. edit Card name and name msut be the same
        if( "true".equals(res+"") ) {
            LayoutManager lm = super.getParent().getLayout();
            if(lm instanceof CardLayout) {
                CardLayout cl = (CardLayout)lm;
                cl.show( super.getParent(), this.getName() );
            }
        }
    }
    
    private void updateContent(boolean visible) {
        if ( label == null ) {
            setVisible(visible);
        } else {
            for(Component c : getComponents()) c.setVisible(visible);
            repaint();
        }
        contentVisible = visible;
    }
    
    public void load() {
        if ( ValueUtil.isEmpty(emptyText) ) return;
        
        hasLabel = true;
        label.setText(emptyText);
        label.setSize( label.getPreferredSize() );
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        
        if ( hasLabel && !contentVisible ) {
            if ( isPreferredSizeSet() ) label.setSize( getPreferredSize() );
            Graphics g2 = g.create();
            g2.translate(0,0);
            label.paint(g2);
            g2.dispose();
        }
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="  getters/setters  ">
    public Dimension getPreferredSize() {
        if ( hasLabel && !contentVisible && !isPreferredSizeSet() )
            return label.getPreferredSize();
        
        return super.getPreferredSize();
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
    
    public boolean isHasNonDynamicContents() {
        return true;
    }
    
    public String getVisibleWhen() {
        return visibleWhen;
    }
    
    public void setVisibleWhen(String visibleWhen) {
        this.visibleWhen = visibleWhen;
    }
    
    public String getEmptyText() {
        return emptyText;
    }
    
    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }
    
    public Insets getEmptyTextPadding() {
        return label.getInsets();
    }
    
    public void setEmptyTextPadding(Insets padding) {
        if ( padding != null )
            label.setBorder(new EmptyBorder(padding));
        else
            label.setBorder(new EmptyBorder(1,1,1,1));
    }
    
    public Font getEmptyTextFont() {
        return label.getFont();
    }
    
    public void setEmptyTextFont(Font font) {
        label.setFont( font );
    }
    
    //</editor-fold>

    public int getEmptyTextVAlignment() {
        return label.getVerticalAlignment();
    }

    public void setEmptyTextVAlignment(int alignment) {
        label.setVerticalAlignment(alignment);
    }

    public int getEmptyTextHAlignment() {
        return label.getHorizontalAlignment();
    }

    public void setEmptyTextHAlignment(int alignment) {
        label.setHorizontalAlignment(alignment);
    }
    
    
}
