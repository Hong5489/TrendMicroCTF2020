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
import java.util.Arrays;
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

class FindFunction extends JDialog implements ActionListener {
  private static final long serialVersionUID = 559491015232880916L;
  
  private JButton cancelButton;
  
  private SwingGui debugGui;
  
  private JList<String> list;
  
  private JButton setButton;
  
  private String value;
  
  public FindFunction(SwingGui paramSwingGui, String paramString1, String paramString2) {
    super(paramSwingGui, paramString1, true);
    boolean bool;
    this.debugGui = paramSwingGui;
    this.cancelButton = new JButton("Cancel");
    this.setButton = new JButton("Select");
    this.cancelButton.addActionListener(this);
    this.setButton.addActionListener(this);
    getRootPane().setDefaultButton(this.setButton);
    JList<String> jList = new JList(new DefaultListModel());
    this.list = jList;
    DefaultListModel<String> defaultListModel = (DefaultListModel)jList.getModel();
    defaultListModel.clear();
    String[] arrayOfString = paramSwingGui.dim.functionNames();
    Arrays.sort((Object[])arrayOfString);
    for (byte b = 0; b < arrayOfString.length; b++)
      defaultListModel.addElement(arrayOfString[b]); 
    this.list.setSelectedIndex(0);
    JButton jButton = this.setButton;
    if (arrayOfString.length > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    jButton.setEnabled(bool);
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
              FindFunction.access$002(FindFunction.this, (String)null);
              FindFunction.this.setVisible(false);
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
      if (this.list.getSelectedIndex() < 0)
        return; 
      try {
        this.value = this.list.getSelectedValue();
        setVisible(false);
        Dim.FunctionSource functionSource = this.debugGui.dim.functionSourceByName(this.value);
        if (functionSource != null) {
          str = functionSource.sourceInfo().url();
          int i = functionSource.firstLine();
          this.debugGui.showFileWindow(str, i);
        } 
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        return;
      } 
    } 
  }
  
  public String showDialog(Component paramComponent) {
    this.value = null;
    setLocationRelativeTo(paramComponent);
    setVisible(true);
    return this.value;
  }
  
  class MouseHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent param1MouseEvent) {
      if (param1MouseEvent.getClickCount() == 2)
        FindFunction.this.setButton.doClick(); 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/FindFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */