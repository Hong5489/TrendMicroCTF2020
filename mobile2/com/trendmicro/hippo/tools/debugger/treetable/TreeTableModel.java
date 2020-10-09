package com.trendmicro.hippo.tools.debugger.treetable;

import javax.swing.tree.TreeModel;

public interface TreeTableModel extends TreeModel {
  Class<?> getColumnClass(int paramInt);
  
  int getColumnCount();
  
  String getColumnName(int paramInt);
  
  Object getValueAt(Object paramObject, int paramInt);
  
  boolean isCellEditable(Object paramObject, int paramInt);
  
  void setValueAt(Object paramObject1, Object paramObject2, int paramInt);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/treetable/TreeTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */