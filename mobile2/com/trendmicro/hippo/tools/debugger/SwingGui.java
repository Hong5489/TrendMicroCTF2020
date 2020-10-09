package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.SecurityUtilities;
import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

public class SwingGui extends JFrame implements GuiCallback {
  private static final long serialVersionUID = -8217029773456711621L;
  
  private EventQueue awtEventQueue;
  
  private JSInternalConsole console;
  
  private ContextWindow context;
  
  private FileWindow currentWindow;
  
  private JDesktopPane desk;
  
  Dim dim;
  
  JFileChooser dlg;
  
  private Runnable exitAction;
  
  private final Map<String, FileWindow> fileWindows = Collections.synchronizedMap(new TreeMap<>());
  
  private Menubar menubar;
  
  private JSplitPane split1;
  
  private JLabel statusBar;
  
  private JToolBar toolBar;
  
  private final Map<String, JFrame> toplevels = Collections.synchronizedMap(new HashMap<>());
  
  public SwingGui(Dim paramDim, String paramString) {
    super(paramString);
    this.dim = paramDim;
    init();
    paramDim.setGuiCallback(this);
  }
  
  private String chooseFile(String paramString) {
    File file;
    this.dlg.setDialogTitle(paramString);
    paramString = null;
    String str = SecurityUtilities.getSystemProperty("user.dir");
    if (str != null)
      file = new File(str); 
    if (file != null)
      this.dlg.setCurrentDirectory(file); 
    if (this.dlg.showOpenDialog(this) == 0)
      try {
        str = this.dlg.getSelectedFile().getCanonicalPath();
        File file1 = this.dlg.getSelectedFile().getParentFile();
        Properties properties = System.getProperties();
        properties.put("user.dir", file1.getPath());
        System.setProperties(properties);
        return str;
      } catch (IOException iOException) {
      
      } catch (SecurityException securityException) {} 
    return null;
  }
  
  private void exit() {
    Runnable runnable = this.exitAction;
    if (runnable != null)
      SwingUtilities.invokeLater(runnable); 
    this.dim.setReturnValue(5);
  }
  
  private JInternalFrame getSelectedFrame() {
    JInternalFrame[] arrayOfJInternalFrame = this.desk.getAllFrames();
    for (byte b = 0; b < arrayOfJInternalFrame.length; b++) {
      if (arrayOfJInternalFrame[b].isShowing())
        return arrayOfJInternalFrame[b]; 
    } 
    return arrayOfJInternalFrame[arrayOfJInternalFrame.length - 1];
  }
  
  static String getShortName(String paramString) {
    int i = paramString.lastIndexOf('/');
    int j = i;
    if (i < 0)
      j = paramString.lastIndexOf('\\'); 
    String str1 = paramString;
    String str2 = str1;
    if (j >= 0) {
      str2 = str1;
      if (j + 1 < paramString.length())
        str2 = paramString.substring(j + 1); 
    } 
    return str2;
  }
  
  private JMenu getWindowMenu() {
    return this.menubar.getMenu(3);
  }
  
  private void init() {
    Menubar menubar = new Menubar(this);
    this.menubar = menubar;
    setJMenuBar(menubar);
    this.toolBar = new JToolBar();
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "Break (Pause)";
    arrayOfString[1] = "Go (F5)";
    arrayOfString[2] = "Step Into (F11)";
    arrayOfString[3] = "Step Over (F7)";
    arrayOfString[4] = "Step Out (F8)";
    JButton jButton2 = new JButton("Break");
    jButton2.setToolTipText("Break");
    jButton2.setActionCommand("Break");
    jButton2.addActionListener(this.menubar);
    jButton2.setEnabled(true);
    int i = 0 + 1;
    jButton2.setToolTipText(arrayOfString[0]);
    JButton jButton3 = new JButton("Go");
    jButton3.setToolTipText("Go");
    jButton3.setActionCommand("Go");
    jButton3.addActionListener(this.menubar);
    jButton3.setEnabled(false);
    int j = i + 1;
    jButton3.setToolTipText(arrayOfString[i]);
    JButton jButton1 = new JButton("Step Into");
    jButton1.setToolTipText("Step Into");
    jButton1.setActionCommand("Step Into");
    jButton1.addActionListener(this.menubar);
    jButton1.setEnabled(false);
    i = j + 1;
    jButton1.setToolTipText(arrayOfString[j]);
    JButton jButton4 = new JButton("Step Over");
    jButton4.setToolTipText("Step Over");
    jButton4.setActionCommand("Step Over");
    jButton4.setEnabled(false);
    jButton4.addActionListener(this.menubar);
    j = i + 1;
    jButton4.setToolTipText(arrayOfString[i]);
    JButton jButton5 = new JButton("Step Out");
    jButton5.setToolTipText("Step Out");
    jButton5.setActionCommand("Step Out");
    jButton5.setEnabled(false);
    jButton5.addActionListener(this.menubar);
    jButton5.setToolTipText(arrayOfString[j]);
    Dimension dimension = jButton4.getPreferredSize();
    jButton2.setPreferredSize(dimension);
    jButton2.setMinimumSize(dimension);
    jButton2.setMaximumSize(dimension);
    jButton2.setSize(dimension);
    jButton3.setPreferredSize(dimension);
    jButton3.setMinimumSize(dimension);
    jButton3.setMaximumSize(dimension);
    jButton1.setPreferredSize(dimension);
    jButton1.setMinimumSize(dimension);
    jButton1.setMaximumSize(dimension);
    jButton4.setPreferredSize(dimension);
    jButton4.setMinimumSize(dimension);
    jButton4.setMaximumSize(dimension);
    jButton5.setPreferredSize(dimension);
    jButton5.setMinimumSize(dimension);
    jButton5.setMaximumSize(dimension);
    this.toolBar.add(jButton2);
    this.toolBar.add(jButton3);
    this.toolBar.add(jButton1);
    this.toolBar.add(jButton4);
    this.toolBar.add(jButton5);
    JPanel jPanel = new JPanel();
    jPanel.setLayout(new BorderLayout());
    getContentPane().add(this.toolBar, "North");
    getContentPane().add(jPanel, "Center");
    JDesktopPane jDesktopPane = new JDesktopPane();
    this.desk = jDesktopPane;
    jDesktopPane.setPreferredSize(new Dimension(600, 300));
    this.desk.setMinimumSize(new Dimension(150, 50));
    jDesktopPane = this.desk;
    JSInternalConsole jSInternalConsole = new JSInternalConsole("JavaScript Console");
    this.console = jSInternalConsole;
    jDesktopPane.add(jSInternalConsole);
    ContextWindow contextWindow = new ContextWindow(this);
    this.context = contextWindow;
    contextWindow.setPreferredSize(new Dimension(600, 120));
    this.context.setMinimumSize(new Dimension(50, 50));
    JSplitPane jSplitPane = new JSplitPane(0, this.desk, this.context);
    this.split1 = jSplitPane;
    jSplitPane.setOneTouchExpandable(true);
    setResizeWeight(this.split1, 0.66D);
    jPanel.add(this.split1, "Center");
    JLabel jLabel = new JLabel();
    this.statusBar = jLabel;
    jLabel.setText("Thread: ");
    jPanel.add(this.statusBar, "South");
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
    addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent param1WindowEvent) {
            SwingGui.this.exit();
          }
        });
  }
  
  private String readFile(String paramString) {
    try {
      FileReader fileReader = new FileReader();
      this(paramString);
      try {
        String str = Kit.readReader(fileReader);
        fileReader.close();
      } finally {
        try {
          fileReader.close();
        } finally {
          fileReader = null;
        } 
      } 
    } catch (IOException iOException) {
      String str = iOException.getMessage();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Error reading ");
      stringBuilder.append(paramString);
      MessageDialogWrapper.showMessageDialog(this, str, stringBuilder.toString(), 0);
      paramString = null;
    } 
    return paramString;
  }
  
  private void setFilePosition(FileWindow paramFileWindow, int paramInt) {
    FileTextArea fileTextArea = paramFileWindow.textArea;
    if (paramInt == -1) {
      try {
        paramFileWindow.setPosition(-1);
        if (this.currentWindow == paramFileWindow)
          this.currentWindow = null; 
      } catch (BadLocationException badLocationException) {}
    } else {
      paramInt = badLocationException.getLineStartOffset(paramInt - 1);
      if (this.currentWindow != null && this.currentWindow != paramFileWindow)
        this.currentWindow.setPosition(-1); 
      paramFileWindow.setPosition(paramInt);
      this.currentWindow = paramFileWindow;
    } 
    if (true) {
      if (paramFileWindow.isIcon())
        this.desk.getDesktopManager().deiconifyFrame(paramFileWindow); 
      this.desk.getDesktopManager().activateFrame(paramFileWindow);
      try {
        paramFileWindow.show();
        paramFileWindow.toFront();
        paramFileWindow.setSelected(true);
      } catch (Exception exception) {}
    } 
  }
  
  static void setResizeWeight(JSplitPane paramJSplitPane, double paramDouble) {
    try {
      Method method = JSplitPane.class.getMethod("setResizeWeight", new Class[] { double.class });
      Double double_ = new Double();
      this(paramDouble);
      method.invoke(paramJSplitPane, new Object[] { double_ });
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
  }
  
  private void updateEnabled(boolean paramBoolean) {
    ((Menubar)getJMenuBar()).updateEnabled(paramBoolean);
    byte b = 0;
    int i = this.toolBar.getComponentCount();
    while (b < i) {
      boolean bool;
      if (b == 0) {
        int j = paramBoolean ^ true;
      } else {
        bool = paramBoolean;
      } 
      this.toolBar.getComponent(b).setEnabled(bool);
      b++;
    } 
    if (paramBoolean) {
      this.toolBar.setEnabled(true);
      if (getExtendedState() == 1)
        setExtendedState(0); 
      toFront();
      this.context.setEnabled(true);
    } else {
      FileWindow fileWindow = this.currentWindow;
      if (fileWindow != null)
        fileWindow.setPosition(-1); 
      this.context.setEnabled(false);
    } 
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    JInternalFrame jInternalFrame;
    byte b2;
    String str = paramActionEvent.getActionCommand();
    byte b1 = -1;
    if (str.equals("Cut") || str.equals("Copy") || str.equals("Paste")) {
      jInternalFrame = getSelectedFrame();
      if (jInternalFrame != null && jInternalFrame instanceof ActionListener)
        ((ActionListener)jInternalFrame).actionPerformed(paramActionEvent); 
      b2 = b1;
    } else if (jInternalFrame.equals("Step Over")) {
      b2 = 0;
    } else if (jInternalFrame.equals("Step Into")) {
      b2 = 1;
    } else if (jInternalFrame.equals("Step Out")) {
      b2 = 2;
    } else if (jInternalFrame.equals("Go")) {
      b2 = 3;
    } else if (jInternalFrame.equals("Break")) {
      this.dim.setBreak();
      b2 = b1;
    } else if (jInternalFrame.equals("Exit")) {
      exit();
      b2 = b1;
    } else {
      String str1;
      if (jInternalFrame.equals("Open")) {
        String str2 = chooseFile("Select a file to compile");
        if (str2 != null) {
          str1 = readFile(str2);
          if (str1 != null) {
            RunProxy runProxy = new RunProxy(this, 1);
            runProxy.fileName = str2;
            runProxy.text = str1;
            (new Thread(runProxy)).start();
          } 
        } 
        b2 = b1;
      } else if (str1.equals("Load")) {
        str1 = chooseFile("Select a file to execute");
        if (str1 != null) {
          String str2 = readFile(str1);
          if (str2 != null) {
            RunProxy runProxy = new RunProxy(this, 2);
            runProxy.fileName = str1;
            runProxy.text = str2;
            (new Thread(runProxy)).start();
          } 
        } 
        b2 = b1;
      } else if (str1.equals("More Windows...")) {
        (new MoreWindows(this, this.fileWindows, "Window", "Files")).showDialog(this);
        b2 = b1;
      } else if (str1.equals("Console")) {
        if (this.console.isIcon())
          this.desk.getDesktopManager().deiconifyFrame(this.console); 
        this.console.show();
        this.desk.getDesktopManager().activateFrame(this.console);
        this.console.consoleTextArea.requestFocus();
        b2 = b1;
      } else if (str1.equals("Cut")) {
        b2 = b1;
      } else if (str1.equals("Copy")) {
        b2 = b1;
      } else if (str1.equals("Paste")) {
        b2 = b1;
      } else if (str1.equals("Go to function...")) {
        (new FindFunction(this, "Go to function", "Function")).showDialog(this);
        b2 = b1;
      } else if (str1.equals("Go to line...")) {
        String str2 = (String)JOptionPane.showInputDialog(this, "Line number", "Go to line...", 3, null, null, null);
        if (str2 == null || str2.trim().length() == 0)
          return; 
        try {
          showFileWindow((String)null, Integer.parseInt(str2));
        } catch (NumberFormatException numberFormatException) {}
        b2 = b1;
      } else if (str1.equals("Tile")) {
        int j;
        JInternalFrame[] arrayOfJInternalFrame = this.desk.getAllFrames();
        int i = arrayOfJInternalFrame.length;
        b2 = (int)Math.sqrt(i);
        if (b2 * b2 < i) {
          j = b2 + 1;
          if (b2 * j < i)
            b2++; 
        } else {
          j = b2;
        } 
        Dimension dimension = this.desk.getSize();
        int k = dimension.width / j;
        int m = dimension.height / b2;
        int n = 0;
        for (i = 0; i < b2; i++) {
          int i1 = 0;
          for (byte b = 0; b < j; b++) {
            int i2 = i * j + b;
            if (i2 >= arrayOfJInternalFrame.length)
              break; 
            JInternalFrame jInternalFrame1 = arrayOfJInternalFrame[i2];
            try {
              jInternalFrame1.setIcon(false);
              jInternalFrame1.setMaximum(false);
            } catch (Exception exception) {}
            this.desk.getDesktopManager().setBoundsForFrame(jInternalFrame1, i1, n, k, m);
            i1 += k;
          } 
          n += m;
        } 
        b2 = b1;
      } else {
        JInternalFrame jInternalFrame1;
        if (exception.equals("Cascade")) {
          JInternalFrame[] arrayOfJInternalFrame = this.desk.getAllFrames();
          int i = arrayOfJInternalFrame.length;
          int j = this.desk.getHeight() / i;
          b2 = j;
          if (j > 30)
            b2 = 30; 
          int k = i - 1;
          i = 0;
          for (j = 0; k >= 0; j += b2) {
            jInternalFrame1 = arrayOfJInternalFrame[k];
            try {
              jInternalFrame1.setIcon(false);
              jInternalFrame1.setMaximum(false);
            } catch (Exception exception1) {}
            Dimension dimension = jInternalFrame1.getPreferredSize();
            int n = dimension.width;
            int m = dimension.height;
            this.desk.getDesktopManager().setBoundsForFrame(jInternalFrame1, i, j, n, m);
            k--;
            i += b2;
          } 
          b2 = b1;
        } else {
          FileWindow fileWindow = getFileWindow((String)jInternalFrame1);
          if (fileWindow != null) {
            fileWindow = fileWindow;
            try {
              if (fileWindow.isIcon())
                fileWindow.setIcon(false); 
              fileWindow.setVisible(true);
              fileWindow.moveToFront();
              fileWindow.setSelected(true);
              b2 = b1;
            } catch (Exception exception1) {
              b2 = b1;
            } 
          } else {
            b2 = b1;
          } 
        } 
      } 
    } 
    if (b2 != -1) {
      updateEnabled(false);
      this.dim.setReturnValue(b2);
    } 
  }
  
  void addTopLevel(String paramString, JFrame paramJFrame) {
    if (paramJFrame != this)
      this.toplevels.put(paramString, paramJFrame); 
  }
  
  protected void createFileWindow(Dim.SourceInfo paramSourceInfo, int paramInt) {
    String str = paramSourceInfo.url();
    FileWindow fileWindow = new FileWindow(this, paramSourceInfo);
    this.fileWindows.put(str, fileWindow);
    if (paramInt != -1) {
      FileWindow fileWindow1 = this.currentWindow;
      if (fileWindow1 != null)
        fileWindow1.setPosition(-1); 
      try {
        fileWindow.setPosition(fileWindow.textArea.getLineStartOffset(paramInt - 1));
      } catch (BadLocationException badLocationException) {
        try {
          fileWindow.setPosition(fileWindow.textArea.getLineStartOffset(0));
        } catch (BadLocationException badLocationException1) {
          fileWindow.setPosition(-1);
        } 
      } 
    } 
    this.desk.add(fileWindow);
    if (paramInt != -1)
      this.currentWindow = fileWindow; 
    this.menubar.addFile(str);
    fileWindow.setVisible(true);
    if (true)
      try {
        fileWindow.setMaximum(true);
        fileWindow.setSelected(true);
        fileWindow.moveToFront();
      } catch (Exception exception) {} 
  }
  
  public void dispatchNextGuiEvent() throws InterruptedException {
    EventQueue eventQueue1 = this.awtEventQueue;
    EventQueue eventQueue2 = eventQueue1;
    if (eventQueue1 == null) {
      eventQueue2 = Toolkit.getDefaultToolkit().getSystemEventQueue();
      this.awtEventQueue = eventQueue2;
    } 
    AWTEvent aWTEvent = eventQueue2.getNextEvent();
    if (aWTEvent instanceof ActiveEvent) {
      ((ActiveEvent)aWTEvent).dispatch();
    } else {
      Object object = aWTEvent.getSource();
      if (object instanceof Component) {
        ((Component)object).dispatchEvent(aWTEvent);
      } else if (object instanceof MenuComponent) {
        ((MenuComponent)object).dispatchEvent(aWTEvent);
      } 
    } 
  }
  
  public void enterInterrupt(Dim.StackFrame paramStackFrame, String paramString1, String paramString2) {
    if (SwingUtilities.isEventDispatchThread()) {
      enterInterruptImpl(paramStackFrame, paramString1, paramString2);
    } else {
      RunProxy runProxy = new RunProxy(this, 4);
      runProxy.lastFrame = paramStackFrame;
      runProxy.threadTitle = paramString1;
      runProxy.alertMessage = paramString2;
      SwingUtilities.invokeLater(runProxy);
    } 
  }
  
  void enterInterruptImpl(Dim.StackFrame paramStackFrame, String paramString1, String paramString2) {
    JLabel jLabel = this.statusBar;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Thread: ");
    stringBuilder.append(paramString1);
    jLabel.setText(stringBuilder.toString());
    showStopLine(paramStackFrame);
    if (paramString2 != null)
      MessageDialogWrapper.showMessageDialog(this, paramString2, "Exception in Script", 0); 
    updateEnabled(true);
    Dim.ContextData contextData = paramStackFrame.contextData();
    JComboBox<String> jComboBox = this.context.context;
    List<String> list = this.context.toolTips;
    this.context.disableUpdate();
    int i = contextData.frameCount();
    jComboBox.removeAllItems();
    jComboBox.setSelectedItem((Object)null);
    list.clear();
    for (byte b = 0; b < i; b++) {
      paramStackFrame = contextData.getFrame(b);
      paramString1 = paramStackFrame.getUrl();
      int j = paramStackFrame.getLineNumber();
      String str = paramString1;
      if (paramString1.length() > 20) {
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("...");
        stringBuilder3.append(paramString1.substring(paramString1.length() - 17));
        str = stringBuilder3.toString();
      } 
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("\"");
      stringBuilder2.append(str);
      stringBuilder2.append("\", line ");
      stringBuilder2.append(j);
      jComboBox.insertItemAt(stringBuilder2.toString(), b);
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\"");
      stringBuilder1.append(paramString1);
      stringBuilder1.append("\", line ");
      stringBuilder1.append(j);
      list.add(stringBuilder1.toString());
    } 
    this.context.enableUpdate();
    jComboBox.setSelectedIndex(0);
    jComboBox.setMinimumSize(new Dimension(50, (jComboBox.getMinimumSize()).height));
  }
  
  public JSInternalConsole getConsole() {
    return this.console;
  }
  
  FileWindow getFileWindow(String paramString) {
    return (paramString == null || paramString.equals("<stdin>")) ? null : this.fileWindows.get(paramString);
  }
  
  public Menubar getMenubar() {
    return this.menubar;
  }
  
  public boolean isGuiEventThread() {
    return SwingUtilities.isEventDispatchThread();
  }
  
  void removeWindow(FileWindow paramFileWindow) {
    this.fileWindows.remove(paramFileWindow.getUrl());
    JMenu jMenu = getWindowMenu();
    int i = jMenu.getItemCount();
    JMenuItem jMenuItem = jMenu.getItem(i - 1);
    String str = getShortName(paramFileWindow.getUrl());
    for (byte b = 5; b < i; b++) {
      JMenuItem jMenuItem1 = jMenu.getItem(b);
      if (jMenuItem1 != null) {
        String str1 = jMenuItem1.getText();
        if (str1.substring(str1.indexOf(' ') + 1).equals(str)) {
          jMenu.remove(jMenuItem1);
          if (i == 6) {
            jMenu.remove(4);
            break;
          } 
          int j;
          for (j = b - 4; b < i - 1; j = k) {
            JMenuItem jMenuItem2 = jMenu.getItem(b);
            int k = j;
            if (jMenuItem2 != null) {
              str1 = jMenuItem2.getText();
              if (str1.equals("More Windows..."))
                break; 
              k = str1.indexOf(' ');
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append((char)(j + 48));
              stringBuilder.append(" ");
              stringBuilder.append(str1.substring(k + 1));
              jMenuItem2.setText(stringBuilder.toString());
              jMenuItem2.setMnemonic(j + 48);
              k = j + 1;
            } 
            b++;
          } 
          if (i - 6 == 0 && jMenuItem != jMenuItem1 && jMenuItem.getText().equals("More Windows..."))
            jMenu.remove(jMenuItem); 
          break;
        } 
      } 
    } 
    jMenu.revalidate();
  }
  
  public void setExitAction(Runnable paramRunnable) {
    this.exitAction = paramRunnable;
  }
  
  public void setVisible(boolean paramBoolean) {
    super.setVisible(paramBoolean);
    if (paramBoolean) {
      this.console.consoleTextArea.requestFocus();
      this.context.split.setDividerLocation(0.5D);
      try {
        this.console.setMaximum(true);
        this.console.setSelected(true);
        this.console.show();
        this.console.consoleTextArea.requestFocus();
      } catch (Exception exception) {}
    } 
  }
  
  protected void showFileWindow(String paramString, int paramInt) {
    JInternalFrame jInternalFrame1;
    if (paramString != null) {
      jInternalFrame1 = getFileWindow(paramString);
    } else {
      jInternalFrame1 = getSelectedFrame();
      if (jInternalFrame1 != null && jInternalFrame1 instanceof FileWindow) {
        jInternalFrame1 = jInternalFrame1;
      } else {
        jInternalFrame1 = this.currentWindow;
      } 
    } 
    JInternalFrame jInternalFrame2 = jInternalFrame1;
    if (jInternalFrame1 == null) {
      jInternalFrame2 = jInternalFrame1;
      if (paramString != null) {
        createFileWindow(this.dim.sourceInfo(paramString), -1);
        jInternalFrame2 = getFileWindow(paramString);
      } 
    } 
    if (jInternalFrame2 == null)
      return; 
    if (paramInt > -1) {
      int i = jInternalFrame2.getPosition(paramInt - 1);
      paramInt = jInternalFrame2.getPosition(paramInt);
      if (i <= 0)
        return; 
      ((FileWindow)jInternalFrame2).textArea.select(i);
      ((FileWindow)jInternalFrame2).textArea.setCaretPosition(i);
      ((FileWindow)jInternalFrame2).textArea.moveCaretPosition(paramInt - 1);
    } 
    try {
      if (jInternalFrame2.isIcon())
        jInternalFrame2.setIcon(false); 
      jInternalFrame2.setVisible(true);
      jInternalFrame2.moveToFront();
      jInternalFrame2.setSelected(true);
      requestFocus();
      jInternalFrame2.requestFocus();
      ((FileWindow)jInternalFrame2).textArea.requestFocus();
    } catch (Exception exception) {}
  }
  
  void showStopLine(Dim.StackFrame paramStackFrame) {
    String str = paramStackFrame.getUrl();
    if (str == null || str.equals("<stdin>")) {
      if (this.console.isVisible())
        this.console.show(); 
      return;
    } 
    showFileWindow(str, -1);
    int i = paramStackFrame.getLineNumber();
    FileWindow fileWindow = getFileWindow(str);
    if (fileWindow != null)
      setFilePosition(fileWindow, i); 
  }
  
  protected boolean updateFileWindow(Dim.SourceInfo paramSourceInfo) {
    FileWindow fileWindow = getFileWindow(paramSourceInfo.url());
    if (fileWindow != null) {
      fileWindow.updateText(paramSourceInfo);
      fileWindow.show();
      return true;
    } 
    return false;
  }
  
  public void updateSourceText(Dim.SourceInfo paramSourceInfo) {
    RunProxy runProxy = new RunProxy(this, 3);
    runProxy.sourceInfo = paramSourceInfo;
    SwingUtilities.invokeLater(runProxy);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/SwingGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */