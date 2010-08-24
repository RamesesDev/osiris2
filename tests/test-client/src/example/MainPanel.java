package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//http://terai.xrea.jp/Swing/ComboBoxSuggestion.html
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MainPanel extends JPanel{
    private final JTextField field;
    private final JComboBox combo = new JComboBox();
    private final Vector<String> model = new Vector<String>();
    
    public MainPanel() {
        super(new BorderLayout());
        combo.setEditable(true);
        field = (JTextField) combo.getEditor().getEditorComponent();
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyTypedInCombo(e);
            }
            @Override
            public void keyPressed(KeyEvent e) {
                keyPressedInCombo(e);
            }
            //public void keyReleased(KeyEvent e) {
        });
        initModel();
        
//         InputMap im = combo.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//         im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterPressed2");
//         combo.getActionMap().put("enterPressed2", new AbstractAction() {
//             public void actionPerformed(ActionEvent e) {
//                 String text = field.getText();
//                 if(!model.contains(text)) {
//                     model.addElement(text);
//                     Collections.sort(model);
//                     setModel(getSuggestedModel(model, text), text);
//                 }
//                 combo.hidePopup();
//                 //hide_flag = true;
//             }
//         });
        
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Auto-Completion ComboBox"));
        p.add(combo, BorderLayout.NORTH);
        
        Box box = Box.createVerticalBox();
        box.add(makeHelpPanel());
        box.add(Box.createVerticalStrut(5));
        box.add(p);
        add(box, BorderLayout.NORTH);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setPreferredSize(new Dimension(320, 180));
    }
    private boolean hide_flag = false;
    private void keyPressedInCombo(KeyEvent ke) {
        String text = field.getText();
        int code = ke.getKeyCode();
        //System.out.println(String.format("|press|%02d|%s",code,text));
        if(code==KeyEvent.VK_ENTER) {
            if(!model.contains(text)) {
                model.addElement(text);
                Collections.sort(model);
                setModel(getSuggestedModel(model, text), text);
            }
            hide_flag = true; //combo.hidePopup();
        }else if(code==KeyEvent.VK_ESCAPE) {
            hide_flag = true; //combo.hidePopup();
        }else if(code==KeyEvent.VK_RIGHT) {
            for(int i=0;i<model.size();i++) {
                String str = model.elementAt(i);
                if(str.startsWith(text)) {
                    combo.setSelectedIndex(-1);
                    field.setText(str);
                    return;
                }
            }
        }
    }
    private void keyTypedInCombo(final KeyEvent ke) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                String text = field.getText();
                //System.out.println(String.format("|typed|%02d|%s",0,text));
                if(text.length()==0) {
                    combo.hidePopup();
                    setModel(new DefaultComboBoxModel(model), "");
                }else{
                    DefaultComboBoxModel m = getSuggestedModel(model, text);
                    if(m.getSize()==0 || hide_flag) {
                        //System.out.println("----");
                        combo.hidePopup();
                        hide_flag = false;
                    }else{
                        setModel(m, text);
                        combo.showPopup();
                    }
                }
            }
        });
    }
    
    private void initModel() {
        model.addElement("aaaa");
        model.addElement("aaaabbb");
        model.addElement("aaaabbbcc");
        model.addElement("aaaabbbccddd");
        model.addElement("abcde");
        model.addElement("abefg");
        model.addElement("bbb1");
        model.addElement("bbb12");
        //Collections.sort(model);
        setModel(new DefaultComboBoxModel(model), "");
    }
    
    private void setModel(DefaultComboBoxModel mdl, String str) {
        combo.setModel(mdl);
        combo.setSelectedIndex(-1);
        field.setText(str);
    }
    
    private static DefaultComboBoxModel getSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) {
            if(s.startsWith(text)) m.addElement(s);
        }
        return m;
    }
    
    private static JPanel makeHelpPanel() {
        JPanel lp = new JPanel(new GridLayout(2,1,2,2));
        lp.add(new JLabel("Char: show Popup"));
        lp.add(new JLabel("ESC: hide Popup"));
        
        JPanel rp = new JPanel(new GridLayout(2,1,2,2));
        rp.add(new JLabel("RIGHT: Completion"));
        rp.add(new JLabel("ENTER: Add/Selection"));
        
        JSeparator sp = new JSeparator(JSeparator.VERTICAL);
        sp.setPreferredSize(new Dimension(4, 30));
        
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Help"));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth  = 1;
        c.gridheight = 1;
        c.gridy   = 0;
        c.weighty = 1.0;
        
        c.weightx = 1.0;
        c.fill    = GridBagConstraints.BOTH;
        c.gridx   = 0; p.add(lp, c);
        c.gridx   = 2; p.add(rp, c);
        
        c.insets  = new Insets(0, 5, 0, 5);
        c.weightx = 0.0;
        c.gridx   = 1;
        p.add(sp, c);
        
        return p;
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        }catch(Exception e) {;}
        
        JFrame frame = new JFrame("ComboBoxSuggestion");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
