package com.trendmicro.hippo.tools.debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

class EvalWindow extends JInternalFrame implements ActionListener {
  private static final long serialVersionUID = -2860585845212160176L;
  
  private EvalTextArea evalTextArea;
  
  public EvalWindow(String paramString, SwingGui paramSwingGui) {
    super(paramString, true, false, true, true);
    EvalTextArea evalTextArea = new EvalTextArea(paramSwingGui);
    this.evalTextArea = evalTextArea;
    evalTextArea.setRows(24);
    this.evalTextArea.setColumns(80);
    setContentPane(new JScrollPane(this.evalTextArea));
    pack();
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("Cut")) {
      this.evalTextArea.cut();
    } else if (str.equals("Copy")) {
      this.evalTextArea.copy();
    } else if (str.equals("Paste")) {
      this.evalTextArea.paste();
    } 
  }
  
  public void setEnabled(boolean paramBoolean) {
    super.setEnabled(paramBoolean);
    this.evalTextArea.setEnabled(paramBoolean);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/EvalWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */