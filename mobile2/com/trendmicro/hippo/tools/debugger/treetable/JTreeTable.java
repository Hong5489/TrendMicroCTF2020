package com.trendmicro.hippo.tools.debugger.treetable;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeTable extends JTable {
  private static final long serialVersionUID = -2103973006456695515L;
  
  protected TreeTableCellRenderer tree;
  
  public JTreeTable(TreeTableModel paramTreeTableModel) {
    this.tree = new TreeTableCellRenderer(paramTreeTableModel);
    setModel(new TreeTableModelAdapter(paramTreeTableModel, this.tree));
    ListToTreeSelectionModelWrapper listToTreeSelectionModelWrapper = new ListToTreeSelectionModelWrapper();
    this.tree.setSelectionModel(listToTreeSelectionModelWrapper);
    setSelectionModel(listToTreeSelectionModelWrapper.getListSelectionModel());
    setDefaultRenderer(TreeTableModel.class, this.tree);
    setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
    setShowGrid(false);
    setIntercellSpacing(new Dimension(0, 0));
    if (this.tree.getRowHeight() < 1)
      setRowHeight(18); 
  }
  
  public int getEditingRow() {
    int i;
    if (getColumnClass(this.editingColumn) == TreeTableModel.class) {
      i = -1;
    } else {
      i = this.editingRow;
    } 
    return i;
  }
  
  public JTree getTree() {
    return this.tree;
  }
  
  public void setRowHeight(int paramInt) {
    super.setRowHeight(paramInt);
    TreeTableCellRenderer treeTableCellRenderer = this.tree;
    if (treeTableCellRenderer != null && treeTableCellRenderer.getRowHeight() != paramInt)
      this.tree.setRowHeight(getRowHeight()); 
  }
  
  public void updateUI() {
    super.updateUI();
    TreeTableCellRenderer treeTableCellRenderer = this.tree;
    if (treeTableCellRenderer != null)
      treeTableCellRenderer.updateUI(); 
    LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
  }
  
  public class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
    private static final long serialVersionUID = 8168140829623071131L;
    
    protected boolean updatingListSelectionModel;
    
    public ListToTreeSelectionModelWrapper() {
      getListSelectionModel().addListSelectionListener(createListSelectionListener());
    }
    
    protected ListSelectionListener createListSelectionListener() {
      return new ListSelectionHandler();
    }
    
    public ListSelectionModel getListSelectionModel() {
      return this.listSelectionModel;
    }
    
    public void resetRowSelection() {
      if (!this.updatingListSelectionModel) {
        this.updatingListSelectionModel = true;
        try {
          super.resetRowSelection();
        } finally {
          this.updatingListSelectionModel = false;
        } 
      } 
    }
    
    protected void updateSelectedPathsFromSelectedRows() {
      if (!this.updatingListSelectionModel) {
        this.updatingListSelectionModel = true;
        try {
          int i = this.listSelectionModel.getMinSelectionIndex();
          int j = this.listSelectionModel.getMaxSelectionIndex();
          clearSelection();
          if (i != -1 && j != -1)
            while (i <= j) {
              if (this.listSelectionModel.isSelectedIndex(i)) {
                TreePath treePath = JTreeTable.this.tree.getPathForRow(i);
                if (treePath != null)
                  addSelectionPath(treePath); 
              } 
              i++;
            }  
        } finally {
          this.updatingListSelectionModel = false;
        } 
      } 
    }
    
    class ListSelectionHandler implements ListSelectionListener {
      public void valueChanged(ListSelectionEvent param2ListSelectionEvent) {
        JTreeTable.ListToTreeSelectionModelWrapper.this.updateSelectedPathsFromSelectedRows();
      }
    }
  }
  
  class ListSelectionHandler implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent param1ListSelectionEvent) {
      this.this$1.updateSelectedPathsFromSelectedRows();
    }
  }
  
  public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    public Component getTableCellEditorComponent(JTable param1JTable, Object param1Object, boolean param1Boolean, int param1Int1, int param1Int2) {
      return JTreeTable.this.tree;
    }
    
    public boolean isCellEditable(EventObject param1EventObject) {
      if (param1EventObject instanceof MouseEvent)
        for (int i = JTreeTable.this.getColumnCount() - 1; i >= 0; i--) {
          if (JTreeTable.this.getColumnClass(i) == TreeTableModel.class) {
            param1EventObject = param1EventObject;
            param1EventObject = new MouseEvent(JTreeTable.this.tree, param1EventObject.getID(), param1EventObject.getWhen(), param1EventObject.getModifiers(), param1EventObject.getX() - (JTreeTable.this.getCellRect(0, i, true)).x, param1EventObject.getY(), param1EventObject.getClickCount(), param1EventObject.isPopupTrigger());
            JTreeTable.this.tree.dispatchEvent((AWTEvent)param1EventObject);
            break;
          } 
        }  
      return false;
    }
  }
  
  public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
    private static final long serialVersionUID = -193867880014600717L;
    
    protected int visibleRow;
    
    public TreeTableCellRenderer(TreeModel param1TreeModel) {
      super(param1TreeModel);
    }
    
    public Component getTableCellRendererComponent(JTable param1JTable, Object param1Object, boolean param1Boolean1, boolean param1Boolean2, int param1Int1, int param1Int2) {
      if (param1Boolean1) {
        setBackground(param1JTable.getSelectionBackground());
      } else {
        setBackground(param1JTable.getBackground());
      } 
      this.visibleRow = param1Int1;
      return this;
    }
    
    public void paint(Graphics param1Graphics) {
      param1Graphics.translate(0, -this.visibleRow * getRowHeight());
      super.paint(param1Graphics);
    }
    
    public void setBounds(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      super.setBounds(param1Int1, 0, param1Int3, JTreeTable.this.getHeight());
    }
    
    public void setRowHeight(int param1Int) {
      if (param1Int > 0) {
        super.setRowHeight(param1Int);
        if (JTreeTable.this.getRowHeight() != param1Int)
          JTreeTable.this.setRowHeight(getRowHeight()); 
      } 
    }
    
    public void updateUI() {
      super.updateUI();
      TreeCellRenderer treeCellRenderer = getCellRenderer();
      if (treeCellRenderer instanceof javax.swing.tree.DefaultTreeCellRenderer) {
        treeCellRenderer = treeCellRenderer;
        treeCellRenderer.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
        treeCellRenderer.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
      } 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/treetable/JTreeTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */