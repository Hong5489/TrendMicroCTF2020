package com.trendmicro.hippo.tools.debugger;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

class FileWindow extends JInternalFrame implements ActionListener {
  private static final long serialVersionUID = -6212382604952082370L;
  
  int currentPos;
  
  private SwingGui debugGui;
  
  private FileHeader fileHeader;
  
  private JScrollPane p;
  
  private Dim.SourceInfo sourceInfo;
  
  FileTextArea textArea;
  
  public FileWindow(SwingGui paramSwingGui, Dim.SourceInfo paramSourceInfo) {
    super(SwingGui.getShortName(paramSourceInfo.url()), true, true, true, true);
    this.debugGui = paramSwingGui;
    this.sourceInfo = paramSourceInfo;
    updateToolTip();
    this.currentPos = -1;
    FileTextArea fileTextArea = new FileTextArea(this);
    this.textArea = fileTextArea;
    fileTextArea.setRows(24);
    this.textArea.setColumns(80);
    this.p = new JScrollPane();
    this.fileHeader = new FileHeader(this);
    this.p.setViewportView(this.textArea);
    this.p.setRowHeaderView(this.fileHeader);
    setContentPane(this.p);
    pack();
    updateText(paramSourceInfo);
    this.textArea.select(0);
  }
  
  private void updateToolTip() {
    int j;
    int i = getComponentCount() - 1;
    if (i > 1) {
      j = 1;
    } else {
      j = i;
      if (i < 0)
        return; 
    } 
    Component component = getComponent(j);
    if (component != null && component instanceof JComponent)
      ((JComponent)component).setToolTipText(getUrl()); 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str = paramActionEvent.getActionCommand();
    if (!str.equals("Cut"))
      if (str.equals("Copy")) {
        this.textArea.copy();
      } else {
        str.equals("Paste");
      }  
  }
  
  public void clearBreakPoint(int paramInt) {
    if (this.sourceInfo.breakableLine(paramInt) && this.sourceInfo.breakpoint(paramInt, false))
      this.fileHeader.repaint(); 
  }
  
  public void dispose() {
    this.debugGui.removeWindow(this);
    super.dispose();
  }
  
  public int getPosition(int paramInt) {
    byte b = -1;
    try {
      paramInt = this.textArea.getLineStartOffset(paramInt);
    } catch (BadLocationException badLocationException) {
      paramInt = b;
    } 
    return paramInt;
  }
  
  public String getUrl() {
    return this.sourceInfo.url();
  }
  
  public boolean isBreakPoint(int paramInt) {
    boolean bool;
    if (this.sourceInfo.breakableLine(paramInt) && this.sourceInfo.breakpoint(paramInt)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void load() {
    String str = getUrl();
    if (str != null) {
      RunProxy runProxy = new RunProxy(this.debugGui, 2);
      runProxy.fileName = str;
      runProxy.text = this.sourceInfo.source();
      (new Thread(runProxy)).start();
    } 
  }
  
  public void select(int paramInt1, int paramInt2) {
    int i = this.textArea.getDocument().getLength();
    this.textArea.select(i, i);
    this.textArea.select(paramInt1, paramInt2);
  }
  
  public void setBreakPoint(int paramInt) {
    if (this.sourceInfo.breakableLine(paramInt) && this.sourceInfo.breakpoint(paramInt, true))
      this.fileHeader.repaint(); 
  }
  
  public void setPosition(int paramInt) {
    this.textArea.select(paramInt);
    this.currentPos = paramInt;
    this.fileHeader.repaint();
  }
  
  public void toggleBreakPoint(int paramInt) {
    if (!isBreakPoint(paramInt)) {
      setBreakPoint(paramInt);
    } else {
      clearBreakPoint(paramInt);
    } 
  }
  
  public void updateText(Dim.SourceInfo paramSourceInfo) {
    this.sourceInfo = paramSourceInfo;
    String str = paramSourceInfo.source();
    if (!this.textArea.getText().equals(str)) {
      this.textArea.setText(str);
      int i = 0;
      if (this.currentPos != -1)
        i = this.currentPos; 
      this.textArea.select(i);
    } 
    this.fileHeader.update();
    this.fileHeader.repaint();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/FileWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */