package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.tools.shell.ConsoleTextArea;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

class JSInternalConsole extends JInternalFrame implements ActionListener {
  private static final long serialVersionUID = -5523468828771087292L;
  
  ConsoleTextArea consoleTextArea;
  
  public JSInternalConsole(String paramString) {
    super(paramString, true, false, true, true);
    ConsoleTextArea consoleTextArea = new ConsoleTextArea(null);
    this.consoleTextArea = consoleTextArea;
    consoleTextArea.setRows(24);
    this.consoleTextArea.setColumns(80);
    setContentPane(new JScrollPane((Component)this.consoleTextArea));
    pack();
    addInternalFrameListener(new InternalFrameAdapter() {
          public void internalFrameActivated(InternalFrameEvent param1InternalFrameEvent) {
            if (JSInternalConsole.this.consoleTextArea.hasFocus()) {
              JSInternalConsole.this.consoleTextArea.getCaret().setVisible(false);
              JSInternalConsole.this.consoleTextArea.getCaret().setVisible(true);
            } 
          }
        });
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("Cut")) {
      this.consoleTextArea.cut();
    } else if (str.equals("Copy")) {
      this.consoleTextArea.copy();
    } else if (str.equals("Paste")) {
      this.consoleTextArea.paste();
    } 
  }
  
  public PrintStream getErr() {
    return this.consoleTextArea.getErr();
  }
  
  public InputStream getIn() {
    return this.consoleTextArea.getIn();
  }
  
  public PrintStream getOut() {
    return this.consoleTextArea.getOut();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/JSInternalConsole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */