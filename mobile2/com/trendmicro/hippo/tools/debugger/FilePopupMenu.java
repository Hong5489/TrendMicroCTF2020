package com.trendmicro.hippo.tools.debugger;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class FilePopupMenu extends JPopupMenu {
  private static final long serialVersionUID = 3589525009546013565L;
  
  int x;
  
  int y;
  
  public FilePopupMenu(FileTextArea paramFileTextArea) {
    JMenuItem jMenuItem = new JMenuItem("Set Breakpoint");
    add(jMenuItem);
    jMenuItem.addActionListener(paramFileTextArea);
    jMenuItem = new JMenuItem("Clear Breakpoint");
    add(jMenuItem);
    jMenuItem.addActionListener(paramFileTextArea);
    jMenuItem = new JMenuItem("Run");
    add(jMenuItem);
    jMenuItem.addActionListener(paramFileTextArea);
  }
  
  public void show(JComponent paramJComponent, int paramInt1, int paramInt2) {
    this.x = paramInt1;
    this.y = paramInt2;
    show(paramJComponent, paramInt1, paramInt2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/FilePopupMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */