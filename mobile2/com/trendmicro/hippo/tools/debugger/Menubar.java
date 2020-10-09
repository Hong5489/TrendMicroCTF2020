package com.trendmicro.hippo.tools.debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class Menubar extends JMenuBar implements ActionListener {
  private static final long serialVersionUID = 3217170497245911461L;
  
  private JCheckBoxMenuItem breakOnEnter;
  
  private JCheckBoxMenuItem breakOnExceptions;
  
  private JCheckBoxMenuItem breakOnReturn;
  
  private SwingGui debugGui;
  
  private List<JMenuItem> interruptOnlyItems = Collections.synchronizedList(new ArrayList<>());
  
  private List<JMenuItem> runOnlyItems = Collections.synchronizedList(new ArrayList<>());
  
  private JMenu windowMenu;
  
  Menubar(SwingGui paramSwingGui) {
    this.debugGui = paramSwingGui;
    String[] arrayOfString2 = { "Open...", "Run...", "", "Exit" };
    String[] arrayOfString3 = { "Open", "Load", "", "Exit" };
    int[] arrayOfInt1 = new int[4];
    arrayOfInt1[0] = 79;
    arrayOfInt1[1] = 78;
    arrayOfInt1[2] = 0;
    arrayOfInt1[3] = 81;
    String[] arrayOfString4 = new String[5];
    arrayOfString4[0] = "Cut";
    arrayOfString4[1] = "Copy";
    arrayOfString4[2] = "Paste";
    arrayOfString4[3] = "Go to function...";
    arrayOfString4[4] = "Go to line...";
    int[] arrayOfInt2 = new int[5];
    arrayOfInt2[0] = 0;
    arrayOfInt2[1] = 0;
    arrayOfInt2[2] = 0;
    arrayOfInt2[3] = 0;
    arrayOfInt2[4] = 76;
    String[] arrayOfString1 = { "Break", "Go", "Step Into", "Step Over", "Step Out" };
    String[] arrayOfString5 = new String[3];
    arrayOfString5[0] = "Metal";
    arrayOfString5[1] = "Windows";
    arrayOfString5[2] = "Motif";
    int[] arrayOfInt3 = new int[7];
    arrayOfInt3[0] = 19;
    arrayOfInt3[1] = 116;
    arrayOfInt3[2] = 122;
    arrayOfInt3[3] = 118;
    arrayOfInt3[4] = 119;
    arrayOfInt3[5] = 0;
    arrayOfInt3[6] = 0;
    JMenu jMenu1 = new JMenu("File");
    jMenu1.setMnemonic('F');
    JMenu jMenu2 = new JMenu("Edit");
    jMenu2.setMnemonic('E');
    JMenu jMenu3 = new JMenu("Platform");
    jMenu3.setMnemonic('P');
    JMenu jMenu4 = new JMenu("Debug");
    jMenu4.setMnemonic('D');
    JMenu jMenu5 = new JMenu("Window");
    this.windowMenu = jMenu5;
    jMenu5.setMnemonic('W');
    byte b = 0;
    while (true) {
      int i = arrayOfString2.length;
      if (b < i) {
        if (arrayOfString2[b].length() == 0) {
          jMenu1.addSeparator();
        } else {
          (new char[4])[0] = '0';
          (new char[4])[1] = 'N';
          (new char[4])[2] = Character.MIN_VALUE;
          (new char[4])[3] = 'X';
          JMenuItem jMenuItem = new JMenuItem(arrayOfString2[b], (new char[4])[b]);
          jMenuItem.setActionCommand(arrayOfString3[b]);
          jMenuItem.addActionListener(this);
          jMenu1.add(jMenuItem);
          if (arrayOfInt1[b] != 0)
            jMenuItem.setAccelerator(KeyStroke.getKeyStroke(arrayOfInt1[b], 2)); 
        } 
        b++;
        continue;
      } 
      for (b = 0; b < arrayOfString4.length; b++) {
        (new char[5])[0] = 'T';
        (new char[5])[1] = 'C';
        (new char[5])[2] = 'P';
        (new char[5])[3] = 'F';
        (new char[5])[4] = 'L';
        JMenuItem jMenuItem = new JMenuItem(arrayOfString4[b], (new char[5])[b]);
        jMenuItem.addActionListener(this);
        jMenu2.add(jMenuItem);
        if (arrayOfInt2[b] != 0)
          jMenuItem.setAccelerator(KeyStroke.getKeyStroke(arrayOfInt2[b], 2)); 
      } 
      for (b = 0; b < arrayOfString5.length; b++) {
        (new char[3])[0] = 'M';
        (new char[3])[1] = 'W';
        (new char[3])[2] = 'F';
        JMenuItem jMenuItem = new JMenuItem(arrayOfString5[b], (new char[3])[b]);
        jMenuItem.addActionListener(this);
        jMenu3.add(jMenuItem);
      } 
      b = 0;
      arrayOfString2 = arrayOfString3;
      while (b < arrayOfString1.length) {
        (new char[5])[0] = 'B';
        (new char[5])[1] = 'G';
        (new char[5])[2] = 'I';
        (new char[5])[3] = 'O';
        (new char[5])[4] = 'T';
        JMenuItem jMenuItem = new JMenuItem(arrayOfString1[b], (new char[5])[b]);
        jMenuItem.addActionListener(this);
        if (arrayOfInt3[b] != 0)
          jMenuItem.setAccelerator(KeyStroke.getKeyStroke(arrayOfInt3[b], 0)); 
        if (b != 0) {
          this.interruptOnlyItems.add(jMenuItem);
        } else {
          this.runOnlyItems.add(jMenuItem);
        } 
        jMenu4.add(jMenuItem);
        b++;
      } 
      JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem("Break on Exceptions");
      this.breakOnExceptions = jCheckBoxMenuItem;
      jCheckBoxMenuItem.setMnemonic('X');
      this.breakOnExceptions.addActionListener(this);
      this.breakOnExceptions.setSelected(false);
      jMenu4.add(this.breakOnExceptions);
      jCheckBoxMenuItem = new JCheckBoxMenuItem("Break on Function Enter");
      this.breakOnEnter = jCheckBoxMenuItem;
      jCheckBoxMenuItem.setMnemonic('E');
      this.breakOnEnter.addActionListener(this);
      this.breakOnEnter.setSelected(false);
      jMenu4.add(this.breakOnEnter);
      jCheckBoxMenuItem = new JCheckBoxMenuItem("Break on Function Return");
      this.breakOnReturn = jCheckBoxMenuItem;
      jCheckBoxMenuItem.setMnemonic('R');
      this.breakOnReturn.addActionListener(this);
      this.breakOnReturn.setSelected(false);
      jMenu4.add(this.breakOnReturn);
      add(jMenu1);
      add(jMenu2);
      add(jMenu4);
      JMenu jMenu = this.windowMenu;
      JMenuItem jMenuItem1 = new JMenuItem("Cascade", 65);
      jMenu.add(jMenuItem1);
      jMenuItem1.addActionListener(this);
      jMenuItem1 = this.windowMenu;
      JMenuItem jMenuItem2 = new JMenuItem("Tile", 84);
      jMenuItem1.add(jMenuItem2);
      jMenuItem2.addActionListener(this);
      this.windowMenu.addSeparator();
      jMenuItem2 = this.windowMenu;
      jMenuItem1 = new JMenuItem("Console", 67);
      jMenuItem2.add(jMenuItem1);
      jMenuItem1.addActionListener(this);
      add(this.windowMenu);
      updateEnabled(false);
      return;
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    String str1;
    String str2 = paramActionEvent.getActionCommand();
    if (str2.equals("Metal")) {
      str1 = "javax.swing.plaf.metal.MetalLookAndFeel";
    } else if (str2.equals("Windows")) {
      str1 = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    } else if (str2.equals("Motif")) {
      str1 = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    } else {
      Object object = str1.getSource();
      if (object == this.breakOnExceptions) {
        this.debugGui.dim.setBreakOnExceptions(this.breakOnExceptions.isSelected());
      } else if (object == this.breakOnEnter) {
        this.debugGui.dim.setBreakOnEnter(this.breakOnEnter.isSelected());
      } else if (object == this.breakOnReturn) {
        this.debugGui.dim.setBreakOnReturn(this.breakOnReturn.isSelected());
      } else {
        this.debugGui.actionPerformed((ActionEvent)str1);
      } 
      return;
    } 
    try {
      UIManager.setLookAndFeel(str1);
      SwingUtilities.updateComponentTreeUI(this.debugGui);
      SwingUtilities.updateComponentTreeUI(this.debugGui.dlg);
    } catch (Exception exception) {}
  }
  
  public void addFile(String paramString) {
    JMenu jMenu;
    int i = this.windowMenu.getItemCount();
    int j = i;
    if (i == 4) {
      this.windowMenu.addSeparator();
      j = i + 1;
    } 
    JMenuItem jMenuItem = this.windowMenu.getItem(j - 1);
    boolean bool = false;
    byte b = 5;
    i = bool;
    int k = b;
    if (jMenuItem != null) {
      i = bool;
      k = b;
      if (jMenuItem.getText().equals("More Windows...")) {
        i = 1;
        k = 5 + 1;
      } 
    } 
    if (i == 0 && j - 4 == 5) {
      jMenu = this.windowMenu;
      jMenuItem = new JMenuItem("More Windows...", 77);
      jMenu.add(jMenuItem);
      jMenuItem.setActionCommand("More Windows...");
      jMenuItem.addActionListener(this);
      return;
    } 
    if (j - 4 <= k) {
      k = j;
      if (i != 0) {
        k = j - 1;
        this.windowMenu.remove(jMenuItem);
      } 
      String str = SwingGui.getShortName((String)jMenu);
      JMenu jMenu1 = this.windowMenu;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append((char)(k - 4 + 48));
      stringBuilder.append(" ");
      stringBuilder.append(str);
      JMenuItem jMenuItem1 = new JMenuItem(stringBuilder.toString(), k - 4 + 48);
      jMenu1.add(jMenuItem1);
      if (i != 0)
        this.windowMenu.add(jMenuItem); 
      jMenuItem1.setActionCommand((String)jMenu);
      jMenuItem1.addActionListener(this);
      return;
    } 
  }
  
  public JCheckBoxMenuItem getBreakOnEnter() {
    return this.breakOnEnter;
  }
  
  public JCheckBoxMenuItem getBreakOnExceptions() {
    return this.breakOnExceptions;
  }
  
  public JCheckBoxMenuItem getBreakOnReturn() {
    return this.breakOnReturn;
  }
  
  public JMenu getDebugMenu() {
    return getMenu(2);
  }
  
  public void updateEnabled(boolean paramBoolean) {
    byte b;
    for (b = 0; b != this.interruptOnlyItems.size(); b++)
      ((JMenuItem)this.interruptOnlyItems.get(b)).setEnabled(paramBoolean); 
    for (b = 0; b != this.runOnlyItems.size(); b++)
      ((JMenuItem)this.runOnlyItems.get(b)).setEnabled(paramBoolean ^ true); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/Menubar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */