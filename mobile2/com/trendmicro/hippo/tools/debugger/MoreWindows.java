package com.trendmicro.hippo.tools.debugger;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class MoreWindows extends JDialog implements ActionListener {
  private static final long serialVersionUID = 5177066296457377546L;
  
  private JButton cancelButton;
  
  private JList<String> list;
  
  private JButton setButton;
  
  private SwingGui swingGui;
  
  private String value;
  
  MoreWindows(SwingGui paramSwingGui, Map<String, FileWindow> paramMap, String paramString1, String paramString2) {
    super(paramSwingGui, paramString1, true);
    this.swingGui = paramSwingGui;
    this.cancelButton = new JButton("Cancel");
    this.setButton = new JButton("Select");
    this.cancelButton.addActionListener(this);
    this.setButton.addActionListener(this);
    getRootPane().setDefaultButton(this.setButton);
    JList<String> jList = new JList(new DefaultListModel());
    this.list = jList;
    DefaultListModel<String> defaultListModel = (DefaultListModel)jList.getModel();
    defaultListModel.clear();
    Iterator<String> iterator = paramMap.keySet().iterator();
    while (iterator.hasNext())
      defaultListModel.addElement(iterator.next()); 
    this.list.setSelectedIndex(0);
    this.setButton.setEnabled(true);
    this.list.setSelectionMode(1);
    this.list.addMouseListener(new MouseHandler());
    JScrollPane jScrollPane = new JScrollPane(this.list);
    jScrollPane.setPreferredSize(new Dimension(320, 240));
    jScrollPane.setMinimumSize(new Dimension(250, 80));
    jScrollPane.setAlignmentX(0.0F);
    JPanel jPanel1 = new JPanel();
    jPanel1.setLayout(new BoxLayout(jPanel1, 1));
    JLabel jLabel = new JLabel(paramString2);
    jLabel.setLabelFor(this.list);
    jPanel1.add(jLabel);
    jPanel1.add(Box.createRigidArea(new Dimension(0, 5)));
    jPanel1.add(jScrollPane);
    jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    JPanel jPanel2 = new JPanel();
    jPanel2.setLayout(new BoxLayout(jPanel2, 0));
    jPanel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    jPanel2.add(Box.createHorizontalGlue());
    jPanel2.add(this.cancelButton);
    jPanel2.add(Box.createRigidArea(new Dimension(10, 0)));
    jPanel2.add(this.setButton);
    Container container = getContentPane();
    container.add(jPanel1, "Center");
    container.add(jPanel2, "South");
    pack();
    addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent param1KeyEvent) {
            if (param1KeyEvent.getKeyCode() == 27) {
              param1KeyEvent.consume();
              MoreWindows.access$102(MoreWindows.this, (String)null);
              MoreWindows.this.setVisible(false);
            } 
          }
        });
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("Cancel")) {
      setVisible(false);
      this.value = null;
    } else if (str.equals("Select")) {
      this.value = this.list.getSelectedValue();
      setVisible(false);
      this.swingGui.showFileWindow(this.value, -1);
    } 
  }
  
  public String showDialog(Component paramComponent) {
    this.value = null;
    setLocationRelativeTo(paramComponent);
    setVisible(true);
    return this.value;
  }
  
  private class MouseHandler extends MouseAdapter {
    private MouseHandler() {}
    
    public void mouseClicked(MouseEvent param1MouseEvent) {
      if (param1MouseEvent.getClickCount() == 2)
        MoreWindows.this.setButton.doClick(); 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/MoreWindows.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */