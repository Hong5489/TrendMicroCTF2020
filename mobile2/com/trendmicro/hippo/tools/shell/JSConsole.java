package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.SecurityUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class JSConsole extends JFrame implements ActionListener {
  static final long serialVersionUID = 2551225560631876300L;
  
  private File CWD;
  
  private ConsoleTextArea consoleTextArea;
  
  private JFileChooser dlg;
  
  public JSConsole(String[] paramArrayOfString) {
    super("Console");
    JMenuBar jMenuBar = new JMenuBar();
    createFileChooser();
    String[] arrayOfString1 = { "Load...", "Exit" };
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "Cut";
    arrayOfString2[1] = "Copy";
    arrayOfString2[2] = "Paste";
    String[] arrayOfString3 = new String[3];
    arrayOfString3[0] = "Metal";
    arrayOfString3[1] = "Windows";
    arrayOfString3[2] = "Motif";
    JMenu jMenu1 = new JMenu("File");
    jMenu1.setMnemonic('F');
    JMenu jMenu2 = new JMenu("Edit");
    jMenu2.setMnemonic('E');
    JMenu jMenu3 = new JMenu("Platform");
    jMenu3.setMnemonic('P');
    byte b;
    for (b = 0; b < arrayOfString1.length; b++) {
      (new char[2])[0] = 'L';
      (new char[2])[1] = 'X';
      JMenuItem jMenuItem = new JMenuItem(arrayOfString1[b], (new char[2])[b]);
      (new String[2])[0] = "Load";
      (new String[2])[1] = "Exit";
      jMenuItem.setActionCommand((new String[2])[b]);
      jMenuItem.addActionListener(this);
      jMenu1.add(jMenuItem);
    } 
    for (b = 0; b < arrayOfString2.length; b++) {
      (new char[3])[0] = 'T';
      (new char[3])[1] = 'C';
      (new char[3])[2] = 'P';
      JMenuItem jMenuItem = new JMenuItem(arrayOfString2[b], (new char[3])[b]);
      jMenuItem.addActionListener(this);
      jMenu2.add(jMenuItem);
    } 
    ButtonGroup buttonGroup = new ButtonGroup();
    for (b = 0; b < arrayOfString3.length; b++) {
      (new boolean[3])[0] = true;
      (new boolean[3])[1] = false;
      (new boolean[3])[2] = false;
      JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(arrayOfString3[b], (new boolean[3])[b]);
      buttonGroup.add(jRadioButtonMenuItem);
      jRadioButtonMenuItem.addActionListener(this);
      jMenu3.add(jRadioButtonMenuItem);
    } 
    jMenuBar.add(jMenu1);
    jMenuBar.add(jMenu2);
    jMenuBar.add(jMenu3);
    setJMenuBar(jMenuBar);
    this.consoleTextArea = new ConsoleTextArea(paramArrayOfString);
    setContentPane(new JScrollPane(this.consoleTextArea));
    this.consoleTextArea.setRows(24);
    this.consoleTextArea.setColumns(80);
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent param1WindowEvent) {
            System.exit(0);
          }
        });
    pack();
    setVisible(true);
    Main.setIn(this.consoleTextArea.getIn());
    Main.setOut(this.consoleTextArea.getOut());
    Main.setErr(this.consoleTextArea.getErr());
    Main.main(paramArrayOfString);
  }
  
  public static void main(String[] paramArrayOfString) {
    new JSConsole(paramArrayOfString);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    ConsoleTextArea consoleTextArea;
    String str = paramActionEvent.getActionCommand();
    paramActionEvent = null;
    if (str.equals("Load")) {
      String str1 = chooseFile();
      if (str1 != null) {
        str1 = str1.replace('\\', '/');
        consoleTextArea = this.consoleTextArea;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("load(\"");
        stringBuilder.append(str1);
        stringBuilder.append("\");");
        consoleTextArea.eval(stringBuilder.toString());
      } 
    } else if (consoleTextArea.equals("Exit")) {
      System.exit(0);
    } else if (consoleTextArea.equals("Cut")) {
      this.consoleTextArea.cut();
    } else if (consoleTextArea.equals("Copy")) {
      this.consoleTextArea.copy();
    } else if (consoleTextArea.equals("Paste")) {
      this.consoleTextArea.paste();
    } else {
      String str1;
      if (consoleTextArea.equals("Metal")) {
        str1 = "javax.swing.plaf.metal.MetalLookAndFeel";
      } else if (consoleTextArea.equals("Windows")) {
        str1 = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
      } else if (consoleTextArea.equals("Motif")) {
        str1 = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
      } 
      if (str1 != null)
        try {
          UIManager.setLookAndFeel(str1);
          SwingUtilities.updateComponentTreeUI(this);
          this.consoleTextArea.postUpdateUI();
          createFileChooser();
        } catch (Exception exception) {
          JOptionPane.showMessageDialog(this, exception.getMessage(), "Platform", 0);
        }  
    } 
  }
  
  public String chooseFile() {
    if (this.CWD == null) {
      String str = SecurityUtilities.getSystemProperty("user.dir");
      if (str != null)
        this.CWD = new File(str); 
    } 
    File file = this.CWD;
    if (file != null)
      this.dlg.setCurrentDirectory(file); 
    this.dlg.setDialogTitle("Select a file to load");
    if (this.dlg.showOpenDialog(this) == 0) {
      String str = this.dlg.getSelectedFile().getPath();
      this.CWD = new File(this.dlg.getSelectedFile().getParent());
      return str;
    } 
    return null;
  }
  
  public void createFileChooser() {
    this.dlg = new JFileChooser();
    FileFilter fileFilter = new FileFilter() {
        public boolean accept(File param1File) {
          if (param1File.isDirectory())
            return true; 
          String str = param1File.getName();
          int i = str.lastIndexOf('.');
          return (i > 0 && i < str.length() - 1 && str.substring(i + 1).toLowerCase().equals("js"));
        }
        
        public String getDescription() {
          return "JavaScript Files (*.js)";
        }
      };
    this.dlg.addChoosableFileFilter(fileFilter);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/JSConsole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */