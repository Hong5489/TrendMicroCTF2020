package com.trendmicro.hippo.tools.debugger;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;

class FileTextArea extends JTextArea implements ActionListener, PopupMenuListener, KeyListener, MouseListener {
  private static final long serialVersionUID = -25032065448563720L;
  
  private FilePopupMenu popup;
  
  private FileWindow w;
  
  public FileTextArea(FileWindow paramFileWindow) {
    this.w = paramFileWindow;
    FilePopupMenu filePopupMenu = new FilePopupMenu(this);
    this.popup = filePopupMenu;
    filePopupMenu.addPopupMenuListener(this);
    addMouseListener(this);
    addKeyListener(this);
    setFont(new Font("Monospaced", 0, 12));
  }
  
  private void checkPopup(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.isPopupTrigger())
      this.popup.show(this, paramMouseEvent.getX(), paramMouseEvent.getY()); 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    int i = viewToModel(new Point(this.popup.x, this.popup.y));
    this.popup.setVisible(false);
    String str = paramActionEvent.getActionCommand();
    int j = -1;
    try {
      i = getLineOfOffset(i);
      j = i;
    } catch (Exception exception) {}
    if (str.equals("Set Breakpoint")) {
      this.w.setBreakPoint(j + 1);
    } else if (str.equals("Clear Breakpoint")) {
      this.w.clearBreakPoint(j + 1);
    } else if (str.equals("Run")) {
      this.w.load();
    } 
  }
  
  public void keyPressed(KeyEvent paramKeyEvent) {
    int i = paramKeyEvent.getKeyCode();
    if (i != 127)
      switch (i) {
        default:
          return;
        case 8:
        case 9:
        case 10:
          break;
      }  
    paramKeyEvent.consume();
  }
  
  public void keyReleased(KeyEvent paramKeyEvent) {
    paramKeyEvent.consume();
  }
  
  public void keyTyped(KeyEvent paramKeyEvent) {
    paramKeyEvent.consume();
  }
  
  public void mouseClicked(MouseEvent paramMouseEvent) {
    checkPopup(paramMouseEvent);
    requestFocus();
    getCaret().setVisible(true);
  }
  
  public void mouseEntered(MouseEvent paramMouseEvent) {}
  
  public void mouseExited(MouseEvent paramMouseEvent) {}
  
  public void mousePressed(MouseEvent paramMouseEvent) {
    checkPopup(paramMouseEvent);
  }
  
  public void mouseReleased(MouseEvent paramMouseEvent) {
    checkPopup(paramMouseEvent);
  }
  
  public void popupMenuCanceled(PopupMenuEvent paramPopupMenuEvent) {}
  
  public void popupMenuWillBecomeInvisible(PopupMenuEvent paramPopupMenuEvent) {}
  
  public void popupMenuWillBecomeVisible(PopupMenuEvent paramPopupMenuEvent) {}
  
  public void select(int paramInt) {
    if (paramInt >= 0)
      try {
        int i = getLineOfOffset(paramInt);
        Rectangle rectangle = modelToView(paramInt);
        if (rectangle == null) {
          select(paramInt, paramInt);
        } else {
          try {
            Rectangle rectangle2 = modelToView(getLineStartOffset(i + 1));
            if (rectangle2 != null)
              rectangle = rectangle2; 
          } catch (Exception exception) {}
          Rectangle rectangle1 = ((JViewport)getParent()).getViewRect();
          if (rectangle1.y + rectangle1.height > rectangle.y) {
            select(paramInt, paramInt);
          } else {
            rectangle.y += (rectangle1.height - rectangle.height) / 2;
            scrollRectToVisible(rectangle);
            select(paramInt, paramInt);
          } 
        } 
      } catch (BadLocationException badLocationException) {
        select(paramInt, paramInt);
      }  
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/FileTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */