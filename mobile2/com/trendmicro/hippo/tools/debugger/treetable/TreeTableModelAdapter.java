package com.trendmicro.hippo.tools.debugger.treetable;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;

public class TreeTableModelAdapter extends AbstractTableModel {
  private static final long serialVersionUID = 48741114609209052L;
  
  JTree tree;
  
  TreeTableModel treeTableModel;
  
  public TreeTableModelAdapter(TreeTableModel paramTreeTableModel, JTree paramJTree) {
    this.tree = paramJTree;
    this.treeTableModel = paramTreeTableModel;
    paramJTree.addTreeExpansionListener(new TreeExpansionListener() {
          public void treeCollapsed(TreeExpansionEvent param1TreeExpansionEvent) {
            TreeTableModelAdapter.this.fireTableDataChanged();
          }
          
          public void treeExpanded(TreeExpansionEvent param1TreeExpansionEvent) {
            TreeTableModelAdapter.this.fireTableDataChanged();
          }
        });
    paramTreeTableModel.addTreeModelListener(new TreeModelListener() {
          public void treeNodesChanged(TreeModelEvent param1TreeModelEvent) {
            TreeTableModelAdapter.this.delayedFireTableDataChanged();
          }
          
          public void treeNodesInserted(TreeModelEvent param1TreeModelEvent) {
            TreeTableModelAdapter.this.delayedFireTableDataChanged();
          }
          
          public void treeNodesRemoved(TreeModelEvent param1TreeModelEvent) {
            TreeTableModelAdapter.this.delayedFireTableDataChanged();
          }
          
          public void treeStructureChanged(TreeModelEvent param1TreeModelEvent) {
            TreeTableModelAdapter.this.delayedFireTableDataChanged();
          }
        });
  }
  
  protected void delayedFireTableDataChanged() {
    SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            TreeTableModelAdapter.this.fireTableDataChanged();
          }
        });
  }
  
  public Class<?> getColumnClass(int paramInt) {
    return this.treeTableModel.getColumnClass(paramInt);
  }
  
  public int getColumnCount() {
    return this.treeTableModel.getColumnCount();
  }
  
  public String getColumnName(int paramInt) {
    return this.treeTableModel.getColumnName(paramInt);
  }
  
  public int getRowCount() {
    return this.tree.getRowCount();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    return this.treeTableModel.getValueAt(nodeForRow(paramInt1), paramInt2);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return this.treeTableModel.isCellEditable(nodeForRow(paramInt1), paramInt2);
  }
  
  protected Object nodeForRow(int paramInt) {
    return this.tree.getPathForRow(paramInt).getLastPathComponent();
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
    this.treeTableModel.setValueAt(paramObject, nodeForRow(paramInt1), paramInt2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/treetable/TreeTableModelAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */