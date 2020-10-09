package com.trendmicro.hippo.tools.debugger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

class FileHeader extends JPanel implements MouseListener {
  private static final long serialVersionUID = -2858905404778259127L;
  
  private FileWindow fileWindow;
  
  private int pressLine = -1;
  
  public FileHeader(FileWindow paramFileWindow) {
    this.fileWindow = paramFileWindow;
    addMouseListener(this);
    update();
  }
  
  public void mouseClicked(MouseEvent paramMouseEvent) {}
  
  public void mouseEntered(MouseEvent paramMouseEvent) {}
  
  public void mouseExited(MouseEvent paramMouseEvent) {}
  
  public void mousePressed(MouseEvent paramMouseEvent) {
    int i = getFontMetrics(this.fileWindow.textArea.getFont()).getHeight();
    this.pressLine = paramMouseEvent.getY() / i;
  }
  
  public void mouseReleased(MouseEvent paramMouseEvent) {
    if (paramMouseEvent.getComponent() == this && (paramMouseEvent.getModifiers() & 0x10) != 0) {
      int i = paramMouseEvent.getY() / getFontMetrics(this.fileWindow.textArea.getFont()).getHeight();
      if (i == this.pressLine) {
        this.fileWindow.toggleBreakPoint(i + 1);
      } else {
        this.pressLine = -1;
      } 
    } 
  }
  
  public void paint(Graphics paramGraphics) {
    FileHeader fileHeader = this;
    super.paint(paramGraphics);
    FileTextArea fileTextArea = fileHeader.fileWindow.textArea;
    Font font = fileTextArea.getFont();
    paramGraphics.setFont(font);
    FontMetrics fontMetrics = fileHeader.getFontMetrics(font);
    Rectangle rectangle = paramGraphics.getClipBounds();
    paramGraphics.setColor(getBackground());
    paramGraphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    int i = fontMetrics.getMaxAscent();
    int j = fontMetrics.getHeight();
    int k = fileTextArea.getLineCount() + 1;
    if (Integer.toString(k).length() < 2);
    int m = rectangle.y / j;
    int n = (rectangle.y + rectangle.height) / j + 1;
    int i1 = getWidth();
    int i2 = n;
    if (n > k)
      i2 = k; 
    while (true) {
      FileHeader fileHeader1 = this;
      if (m < i2) {
        k = -2;
        try {
          n = fileTextArea.getLineStartOffset(m);
        } catch (BadLocationException badLocationException) {
          n = k;
        } 
        boolean bool = fileHeader1.fileWindow.isBreakPoint(m + 1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Integer.toString(m + 1));
        stringBuilder.append(" ");
        String str = stringBuilder.toString();
        k = m * j;
        paramGraphics.setColor(Color.blue);
        paramGraphics.drawString(str, 0, k + i);
        int i3 = i1 - i;
        if (bool) {
          paramGraphics.setColor(new Color(128, 0, 0));
          int i4 = k + i - 9;
          paramGraphics.fillOval(i3, i4, 9, 9);
          paramGraphics.drawOval(i3, i4, 8, 8);
          paramGraphics.drawOval(i3, i4, 9, 9);
        } 
        if (n == fileHeader1.fileWindow.currentPos) {
          Polygon polygon = new Polygon();
          int i4 = k + i - 10;
          polygon.addPoint(i3, i4 + 3);
          polygon.addPoint(i3 + 5, i4 + 3);
          k = i3 + 5;
          for (n = i4; k <= i3 + 10; n++) {
            polygon.addPoint(k, n);
            k++;
          } 
          k = i3 + 9;
          while (k >= i3 + 5) {
            polygon.addPoint(k, n);
            k--;
            n++;
          } 
          polygon.addPoint(i3 + 5, i4 + 7);
          polygon.addPoint(i3, i4 + 7);
          paramGraphics.setColor(Color.yellow);
          paramGraphics.fillPolygon(polygon);
          paramGraphics.setColor(Color.black);
          paramGraphics.drawPolygon(polygon);
        } 
        m++;
        continue;
      } 
      break;
    } 
  }
  
  public void update() {
    FileTextArea fileTextArea = this.fileWindow.textArea;
    Font font = fileTextArea.getFont();
    setFont(font);
    FontMetrics fontMetrics = getFontMetrics(font);
    int i = fontMetrics.getHeight();
    int j = fileTextArea.getLineCount() + 1;
    String str2 = Integer.toString(j);
    String str1 = str2;
    if (str2.length() < 2)
      str1 = "99"; 
    Dimension dimension = new Dimension();
    dimension.width = fontMetrics.stringWidth(str1) + 16;
    dimension.height = j * i + 100;
    setPreferredSize(dimension);
    setSize(dimension);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/FileHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */