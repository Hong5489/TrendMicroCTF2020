package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.tools.debugger.treetable.TreeTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

class ContextWindow extends JPanel implements ActionListener {
  private static final long serialVersionUID = 2306040975490228051L;
  
  private EvalTextArea cmdLine;
  
  JComboBox<String> context;
  
  private SwingGui debugGui;
  
  private boolean enabled;
  
  private Evaluator evaluator;
  
  private MyTreeTable localsTable;
  
  JSplitPane split;
  
  private MyTableModel tableModel;
  
  private JTabbedPane tabs;
  
  private JTabbedPane tabs2;
  
  private MyTreeTable thisTable;
  
  List<String> toolTips;
  
  public ContextWindow(final SwingGui debugGui) {
    this.debugGui = debugGui;
    this.enabled = false;
    JPanel jPanel1 = new JPanel();
    final JToolBar finalT1 = new JToolBar();
    jToolBar2.setName("Variables");
    jToolBar2.setLayout(new GridLayout());
    jToolBar2.add(jPanel1);
    final JPanel finalP1 = new JPanel();
    jPanel2.setLayout(new GridLayout());
    final JPanel finalP2 = new JPanel();
    jPanel3.setLayout(new GridLayout());
    jPanel2.add(jToolBar2);
    JLabel jLabel = new JLabel("Context:");
    JComboBox<String> jComboBox = new JComboBox();
    this.context = jComboBox;
    jComboBox.setLightWeightPopupEnabled(false);
    this.toolTips = Collections.synchronizedList(new ArrayList<>());
    jLabel.setBorder(this.context.getBorder());
    this.context.addActionListener(this);
    this.context.setActionCommand("ContextSwitch");
    GridBagLayout gridBagLayout = new GridBagLayout();
    jPanel1.setLayout(gridBagLayout);
    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
    gridBagConstraints2.insets.left = 5;
    gridBagConstraints2.anchor = 17;
    gridBagConstraints2.ipadx = 5;
    gridBagLayout.setConstraints(jLabel, gridBagConstraints2);
    jPanel1.add(jLabel);
    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    gridBagConstraints1.gridwidth = 0;
    gridBagConstraints1.fill = 2;
    gridBagConstraints1.anchor = 17;
    gridBagLayout.setConstraints(this.context, gridBagConstraints1);
    jPanel1.add(this.context);
    JTabbedPane jTabbedPane2 = new JTabbedPane(3);
    this.tabs = jTabbedPane2;
    jTabbedPane2.setPreferredSize(new Dimension(500, 300));
    this.thisTable = new MyTreeTable(new VariableModel());
    JScrollPane jScrollPane3 = new JScrollPane((Component)this.thisTable);
    jScrollPane3.getViewport().setViewSize(new Dimension(5, 2));
    this.tabs.add("this", jScrollPane3);
    MyTreeTable myTreeTable = new MyTreeTable(new VariableModel());
    this.localsTable = myTreeTable;
    myTreeTable.setAutoResizeMode(4);
    this.localsTable.setPreferredSize(null);
    JScrollPane jScrollPane2 = new JScrollPane((Component)this.localsTable);
    this.tabs.add("Locals", jScrollPane2);
    gridBagConstraints1.weighty = 1.0D;
    gridBagConstraints1.weightx = 1.0D;
    gridBagConstraints1.gridheight = 0;
    gridBagConstraints1.fill = 1;
    gridBagConstraints1.anchor = 17;
    gridBagLayout.setConstraints(this.tabs, gridBagConstraints1);
    jPanel1.add(this.tabs);
    this.evaluator = new Evaluator(debugGui);
    this.cmdLine = new EvalTextArea(debugGui);
    this.tableModel = this.evaluator.tableModel;
    JScrollPane jScrollPane1 = new JScrollPane(this.evaluator);
    final JToolBar finalT2 = new JToolBar();
    jToolBar1.setName("Evaluate");
    JTabbedPane jTabbedPane1 = new JTabbedPane(3);
    this.tabs2 = jTabbedPane1;
    jTabbedPane1.add("Watch", jScrollPane1);
    this.tabs2.add("Evaluate", new JScrollPane(this.cmdLine));
    this.tabs2.setPreferredSize(new Dimension(500, 300));
    jToolBar1.setLayout(new GridLayout());
    jToolBar1.add(this.tabs2);
    jPanel3.add(jToolBar1);
    this.evaluator.setAutoResizeMode(4);
    final JSplitPane finalSplit = new JSplitPane(1, jPanel2, jPanel3);
    this.split = jSplitPane;
    jSplitPane.setOneTouchExpandable(true);
    SwingGui.setResizeWeight(this.split, 0.5D);
    setLayout(new BorderLayout());
    add(this.split, "Center");
    jSplitPane = this.split;
    ComponentListener componentListener = new ComponentListener() {
        boolean t2Docked = true;
        
        void check(Component param1Component) {
          Container container = finalThis.getParent();
          if (container == null)
            return; 
          param1Component = finalT1.getParent();
          boolean bool1 = true;
          boolean bool2 = true;
          if (param1Component != null)
            if (param1Component != finalP1) {
              while (!(param1Component instanceof JFrame))
                param1Component = param1Component.getParent(); 
              param1Component = param1Component;
              debugGui.addTopLevel("Variables", (JFrame)param1Component);
              if (!param1Component.isResizable()) {
                param1Component.setResizable(true);
                param1Component.setDefaultCloseOperation(0);
                final WindowListener[] l = param1Component.<WindowListener>getListeners(WindowListener.class);
                param1Component.removeWindowListener(arrayOfWindowListener[0]);
                param1Component.addWindowListener(new WindowAdapter() {
                      public void windowClosing(WindowEvent param2WindowEvent) {
                        ContextWindow.this.context.hidePopup();
                        l[0].windowClosing(param2WindowEvent);
                      }
                    });
              } 
              bool1 = false;
            } else {
              bool1 = true;
            }  
          param1Component = finalT2.getParent();
          if (param1Component != null)
            if (param1Component != finalP2) {
              while (!(param1Component instanceof JFrame))
                param1Component = param1Component.getParent(); 
              param1Component = param1Component;
              debugGui.addTopLevel("Evaluate", (JFrame)param1Component);
              param1Component.setResizable(true);
              bool2 = false;
            } else {
              bool2 = true;
            }  
          if (bool1) {
            boolean bool = this.t2Docked;
            if (bool && bool2 && bool)
              return; 
          } 
          this.t2Docked = bool2;
          param1Component = container;
          if (bool1) {
            if (bool2) {
              finalSplit.setDividerLocation(0.5D);
            } else {
              finalSplit.setDividerLocation(1.0D);
            } 
            if (false)
              param1Component.setDividerLocation(0.66D); 
          } else if (bool2) {
            finalSplit.setDividerLocation(0.0D);
            param1Component.setDividerLocation(0.66D);
          } else {
            param1Component.setDividerLocation(1.0D);
          } 
        }
        
        public void componentHidden(ComponentEvent param1ComponentEvent) {
          check(param1ComponentEvent.getComponent());
        }
        
        public void componentMoved(ComponentEvent param1ComponentEvent) {
          check(param1ComponentEvent.getComponent());
        }
        
        public void componentResized(ComponentEvent param1ComponentEvent) {
          check(param1ComponentEvent.getComponent());
        }
        
        public void componentShown(ComponentEvent param1ComponentEvent) {
          check(param1ComponentEvent.getComponent());
        }
      };
    jPanel2.addContainerListener(new ContainerListener() {
          public void componentAdded(ContainerEvent param1ContainerEvent) {
            JSplitPane jSplitPane = (JSplitPane)finalThis.getParent();
            if (param1ContainerEvent.getChild() == finalT1) {
              if (finalT2.getParent() == finalP2) {
                finalSplit.setDividerLocation(0.5D);
              } else {
                finalSplit.setDividerLocation(1.0D);
              } 
              jSplitPane.setDividerLocation(0.66D);
            } 
          }
          
          public void componentRemoved(ContainerEvent param1ContainerEvent) {
            JSplitPane jSplitPane = (JSplitPane)finalThis.getParent();
            if (param1ContainerEvent.getChild() == finalT1)
              if (finalT2.getParent() == finalP2) {
                finalSplit.setDividerLocation(0.0D);
                jSplitPane.setDividerLocation(0.66D);
              } else {
                jSplitPane.setDividerLocation(1.0D);
              }  
          }
        });
    jToolBar2.addComponentListener(componentListener);
    jToolBar1.addComponentListener(componentListener);
    setEnabled(false);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent) {
    if (!this.enabled)
      return; 
    if (paramActionEvent.getActionCommand().equals("ContextSwitch")) {
      Dim.ContextData contextData = this.debugGui.dim.currentContextData();
      if (contextData == null)
        return; 
      int i = this.context.getSelectedIndex();
      this.context.setToolTipText(this.toolTips.get(i));
      if (i >= contextData.frameCount())
        return; 
      Dim.StackFrame stackFrame = contextData.getFrame(i);
      Object object2 = stackFrame.scope();
      Object object1 = stackFrame.thisObj();
      this.thisTable.resetTree(new VariableModel(this.debugGui.dim, object1));
      if (object2 != object1) {
        object1 = new VariableModel(this.debugGui.dim, object2);
      } else {
        object1 = new VariableModel();
      } 
      this.localsTable.resetTree((TreeTableModel)object1);
      this.debugGui.dim.contextSwitch(i);
      this.debugGui.showStopLine(stackFrame);
      this.tableModel.updateModel();
    } 
  }
  
  public void disableUpdate() {
    this.enabled = false;
  }
  
  public void enableUpdate() {
    this.enabled = true;
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.context.setEnabled(paramBoolean);
    this.thisTable.setEnabled(paramBoolean);
    this.localsTable.setEnabled(paramBoolean);
    this.evaluator.setEnabled(paramBoolean);
    this.cmdLine.setEnabled(paramBoolean);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/ContextWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */