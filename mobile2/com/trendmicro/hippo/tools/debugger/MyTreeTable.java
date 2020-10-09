package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.tools.debugger.treetable.JTreeTable;
import com.trendmicro.hippo.tools.debugger.treetable.TreeTableModel;
import com.trendmicro.hippo.tools.debugger.treetable.TreeTableModelAdapter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

class MyTreeTable extends JTreeTable {
  private static final long serialVersionUID = 3457265548184453049L;
  
  public MyTreeTable(VariableModel paramVariableModel) {
    super(paramVariableModel);
  }
  
  public boolean isCellEditable(EventObject paramEventObject) {
    if (paramEventObject instanceof MouseEvent) {
      paramEventObject = paramEventObject;
      if (paramEventObject.getModifiers() == 0 || ((paramEventObject.getModifiers() & 0x410) != 0 && (paramEventObject.getModifiers() & 0x1ACF) == 0)) {
        int i = rowAtPoint(paramEventObject.getPoint());
        for (int j = getColumnCount() - 1; j >= 0; j--) {
          if (TreeTableModel.class == getColumnClass(j)) {
            MouseEvent mouseEvent = new MouseEvent((Component)this.tree, paramEventObject.getID(), paramEventObject.getWhen(), paramEventObject.getModifiers(), paramEventObject.getX() - (getCellRect(i, j, true)).x, paramEventObject.getY(), paramEventObject.getClickCount(), paramEventObject.isPopupTrigger());
            this.tree.dispatchEvent(mouseEvent);
            break;
          } 
        } 
      } 
      return (paramEventObject.getClickCount() >= 3);
    } 
    return (paramEventObject == null);
  }
  
  public JTree resetTree(TreeTableModel paramTreeTableModel) {
    this.tree = new JTreeTable.TreeTableCellRenderer(this, (TreeModel)paramTreeTableModel);
    setModel((TableModel)new TreeTableModelAdapter(paramTreeTableModel, (JTree)this.tree));
    JTreeTable.ListToTreeSelectionModelWrapper listToTreeSelectionModelWrapper = new JTreeTable.ListToTreeSelectionModelWrapper(this);
    this.tree.setSelectionModel((TreeSelectionModel)listToTreeSelectionModelWrapper);
    setSelectionModel(listToTreeSelectionModelWrapper.getListSelectionModel());
    if (this.tree.getRowHeight() < 1)
      setRowHeight(18); 
    setDefaultRenderer(TreeTableModel.class, (TableCellRenderer)this.tree);
    setDefaultEditor(TreeTableModel.class, (TableCellEditor)new JTreeTable.TreeTableCellEditor(this));
    setShowGrid(true);
    setIntercellSpacing(new Dimension(1, 1));
    this.tree.setRootVisible(false);
    this.tree.setShowsRootHandles(true);
    DefaultTreeCellRenderer defaultTreeCellRenderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
    defaultTreeCellRenderer.setOpenIcon(null);
    defaultTreeCellRenderer.setClosedIcon(null);
    defaultTreeCellRenderer.setLeafIcon(null);
    return (JTree)this.tree;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/MyTreeTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */