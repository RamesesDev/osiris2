/*
 * XGroupedIconPanel.java
 *
 * Created on March 1, 2012, 4:06 PM
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Windhel
 */
public class XGroupedIconPanel extends JPanel implements UIControl{
    
    private Binding binding;
    private String[] depends;
    private int index;
    
    public XGroupedIconPanel() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        //setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createEtchedBorder());
    }
    
    //<editor-fold defaultstate="collapsed" desc="  setter / getter  ">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    //</editor-fold>
    
    public void refresh() {}
    
    public void load() {
        Map mapOfActions = new HashMap();
        List<Action> actions = new ArrayList();
        List categories = new ArrayList();
        categories.clear();
        removeAll();
        Object beanValue = UIControlUtil.getBeanValue(this);
        if(beanValue instanceof Collection) {
            for(Object o : (Collection) beanValue) {
                if(o instanceof Action) actions.add((Action)o);
            }
        }
        
        for(Action a : actions) {
            Map map = a.getProperties();
            if(categories.size() == 0) categories.add( map.get("category") );
            if(!categories.contains( map.get("category") )) categories.add( map.get("category") );
        }
        
        List memberList;
        List<Action> memberActions;
        for(Object category : categories) {
            memberList = new ArrayList();
            memberActions = new ArrayList();
            for(Action action : actions) {
                Map map = action.getProperties();
                if(map.get("category").equals(category)) {
                    memberList.add( action.getCaption() );
                    memberActions.add( action );
                }
            }
            add( new GroupClass(getClass().getResource("/com/rameses/rcp/icons/folder.png"), memberList, memberActions, category.toString()) );
        }
    }
}

//<editor-fold defaultstate="collapsed" desc="  GroupClass  ">
/*
 *as per category
 -------------------
 |(CENTER)| (EAST) |
 | LABEL/ | GROUP  |
 |  IMAGE | MEMBER |
 | HERE   |        |
 |        |        |
 -------------------
 */
class GroupClass extends JPanel {
    private BufferedImage bi;
    private final Color enterColor = new Color(156, 202, 247);
    
    public GroupClass(URL imgPath, List list, List<Action> actions, String description) {
        try {
            bi = resize(ImageIO.read(imgPath));
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon(bi));
            label.setVerticalAlignment(SwingConstants.TOP);
            setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
            
            final GroupMember gm = new GroupMember(list, actions,  description, this);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBorder(BorderFactory.createLineBorder(enterColor));
                    gm.setTitle(buildTitle("<u><b", "</b></u>", gm.getSelColor(), gm.getFontSize(), gm.getTitle()));
                }
                public void mouseExited(MouseEvent e) {
                    setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
                    gm.setTitle(buildTitle("<b", "</b>", gm.getUnselColor(), gm.getFontSize(), gm.getTitle()));
                }
            });
            setLayout(new BorderLayout());
            add(label, BorderLayout.WEST);
            add( gm );
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private BufferedImage resize(BufferedImage bi) {
        BufferedImage bit = new BufferedImage(32,32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bit.getGraphics();
        g.drawImage(bi, 0, 0, bit.getWidth(), bit.getHeight(), null);
        return bit;
    }
    
    public Color getEnterColor()  {
        return enterColor;
    }
    
    public String buildTitle(String startTag,String endTag, String color, String fontSize, String title) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append(startTag);
        sb.append(" style='color:");
        sb.append(color);
        sb.append(";font-size:");
        sb.append(fontSize);
        sb.append(";'>");
        sb.append(title);
        sb.append(endTag);
        sb.append("</html>");
        return sb.toString();
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="  GroupMember  ">
/*
 -------------------
 |    (NORTH)      |
 |   DESCRIPTION   |
 -------------------
 |    (CENTER)     |
 |     MEMBERS     |
 |                 |
 -------------------
 */
class GroupMember extends JPanel {
    
    private JLabel title;
    private String desc;
    private String sel = "#15b84f";
    private String unsel = "#006e12";
    private String fontSize = "12px";
    private int showOnly = 5;
    private JButton additAction = new JButton("show more...");
    private JPanel additCont = new JPanel();
    private JPanel members = new JPanel();
    private List<Action> actions;
    private JPanel panel = new JPanel();
    
    public GroupMember(final List memberList, List<Action> actions, final String desc, final GroupClass gc) {
        this.desc = desc;
        this.actions = actions;
        
        title = new JLabel(buildTitle("<b", "</b>", unsel, fontSize, desc));
        title.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                title.setText(buildTitle("<u><b", "</b></u>", sel, fontSize, desc));
                gc.setBorder(BorderFactory.createLineBorder(gc.getEnterColor()));
            }
            public void mouseExited(MouseEvent e) {
                title.setText(buildTitle("<b", "</b>", unsel, fontSize, desc));
                gc.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
            }
        });
        
        members.setLayout(new BoxLayout(members, BoxLayout.PAGE_AXIS));
        additCont.setLayout(new FlowLayout(FlowLayout.LEFT));
        /*
         * additAction shows ONLY if memberList.size() > showOnly
         * show more... and hide functionality ACTIONLISTENER
         * sets the GroupClass border accordingly MOUSEADAPTER
         */
        additAction.setBorderPainted(false);
        additAction.setContentAreaFilled(false);
        additAction.setFocusPainted(false);
        additAction.setForeground(Color.red);
        additAction.setFont(new Font(getFont().getName(), Font.PLAIN, 10));
        additAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int size = memberList.size();
                if(memberList.size() > showOnly)
                    size = showOnly;
                
                if( additAction.getText().equals("show more...")) {
                    additAction.setText("hide");
                    buildMembers(memberList.size(), memberList.size(), gc);
                } else {
                    additAction.setText("show more...");
                    buildMembers(memberList.size(), size, gc);
                }
                
                additAction.setFont(new Font(getFont().getName(), Font.PLAIN, 10));
                additAction.repaint();
                members.repaint();
            }
        });
        additAction.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                additAction.setFont(new Font(getFont().getName(), Font.BOLD, 10));
                additAction.repaint();
                title.setText(buildTitle("<u><b", "</b></u>", sel, fontSize, desc));
                gc.setBorder(BorderFactory.createLineBorder(gc.getEnterColor()));
            }
            public void mouseExited(MouseEvent e) {
                additAction.setFont(new Font(getFont().getName(), Font.PLAIN, 10));
                additAction.repaint();
                title.setText(buildTitle("<b", "</b>", unsel, fontSize, desc));
                gc.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
            }
        });
        
        int size = memberList.size();
        if(memberList.size() > showOnly)
            size = showOnly;
        
        if(memberList.size() > showOnly)
            additCont.add(additAction);
        
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                gc.setBorder(BorderFactory.createLineBorder(gc.getEnterColor()));
                title.setText(buildTitle("<u><b", "</b></u>", sel, fontSize, desc));
            }
            public void mouseExited(MouseEvent e) {
                gc.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
                title.setText(buildTitle("<b", "</b>", unsel, fontSize, desc));
            }
        });
        panel.setLayout(new BorderLayout());
        panel.add(members, BorderLayout.NORTH);
        panel.add(additCont, BorderLayout.CENTER);
        
        buildMembers(memberList.size(), size, gc);
        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
    
    private void buildMembers(int memberSize, int size, GroupClass gc) {
        panel.removeAll();
        members.removeAll();
        members.setLayout(new BoxLayout(members, BoxLayout.PAGE_AXIS));
        for(int i=0 ; i<size ; i++) {
            Member btn = new Member();
            Action action = actions.get(i);
            btn.setHorizontalTextPosition(SwingConstants.LEFT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCaption(action.getCaption());
            btn.setIndex(action.getIndex());
            //btn.setFont(getFont());
            //btn.setName(action.getName());
            btn.setPermission(action.getPermission());
            btn.setParams(action.getParams());
            btn.setGroupClass(gc);
            btn.setGroupMember( this );
            if ( !action.getClass().getName().equals(Action.class.getName()) ) {
                btn.putClientProperty(Action.class.getName(), action);
            }
            
            members.add(btn);
        }
        panel.setLayout(new BorderLayout());
        panel.add(members, BorderLayout.NORTH);
        panel.add(additCont, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
    
    public String buildTitle(String startTag,String endTag, String color, String fontSize, String title) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append(startTag);
        sb.append(" style='color:");
        sb.append(color);
        sb.append(";font-size:");
        sb.append(fontSize);
        sb.append(";'>");
        sb.append(title);
        sb.append(endTag);
        sb.append("</html>");
        return sb.toString();
    }
    
    public void setTitle(String desc) {
        title.setText(desc);
    }
    
    public String getTitle() {
        return desc;
    }

    public String getSelColor() {
        return sel;
    }

    public String getUnselColor() {
        return unsel;
    }
    
    public String getFontSize() {
        return fontSize;
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="  Member  ">
class Member extends XButton implements MouseListener{
    
    private String str;
    private GroupClass gc;
    private GroupMember gm;
    private Color enterColor;
    private String sel = "#438fda";
    private String unsel = "#0066cc";
    private String fontSize = "8px";
    
    public Member() {
        super();
        Insets insets = getBorder().getBorderInsets(this);
        setBorder(BorderFactory.createEmptyBorder(insets.top, 7, insets.bottom, insets.right));
        
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        addMouseListener(this);
    }
    
    public void setGroupClass(GroupClass gc) {
        this.gc = gc;
        enterColor = gc.getEnterColor();
    }
    
    public void setGroupMember(GroupMember gm) {
        this.gm = gm;
    }
    
    public void setCaption(String caption) {
        str = caption;
        setText(buildTitle("<b", "</b>",unsel, fontSize, str));
    }
    
    public String buildTitle(String startTag,String endTag, String color, String fontSize, String title) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append(startTag);
        sb.append(" style='color:");
        sb.append(color);
        sb.append(";font-size:");
        sb.append(fontSize);
        sb.append(";'>");
        sb.append(title);
        sb.append(endTag);
        sb.append("</html>");
        return sb.toString();
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
        gc.setBorder(BorderFactory.createLineBorder(enterColor));
        setText(buildTitle("<b", "</b>",  sel, fontSize, str));
        gm.setTitle(buildTitle("<u><b", "</b></u>", gm.getSelColor(), gm.getFontSize(), gm.getTitle()));
    }
    public void mouseExited(MouseEvent e) {
        gc.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        setText(buildTitle("<b", "</b>", unsel, fontSize, str));
        gm.setTitle(buildTitle("<b", "</b>", gm.getUnselColor(), gm.getFontSize(), gm.getTitle()));
    }
}
//</editor-fold>