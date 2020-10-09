package com.trendmicro.hippo.tools.debugger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class MyTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 2971618907207577000L;
  
  private SwingGui debugGui;
  
  private List<String> expressions;
  
  private List<String> values;
  
  public MyTableModel(SwingGui paramSwingGui) {
    this.debugGui = paramSwingGui;
    this.expressions = Collections.synchronizedList(new ArrayList<>());
    this.values = Collections.synchronizedList(new ArrayList<>());
    this.expressions.add("");
    this.values.add("");
  }
  
  public int getColumnCount() {
    return 2;
  }
  
  public String getColumnName(int paramInt) {
    return (paramInt != 0) ? ((paramInt != 1) ? null : "Value") : "Expression";
  }
  
  public int getRowCount() {
    return this.expressions.size();
  }
  
  public Object getValueAt(int paramInt1, int paramInt2) {
    return (paramInt2 != 0) ? ((paramInt2 != 1) ? "" : this.values.get(paramInt1)) : this.expressions.get(paramInt1);
  }
  
  public boolean isCellEditable(int paramInt1, int paramInt2) {
    return true;
  }
  
  public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {
    if (paramInt2 != 0) {
      if (paramInt2 == 1)
        fireTableDataChanged(); 
    } else {
      String str = paramObject.toString();
      this.expressions.set(paramInt1, str);
      paramObject = "";
      if (str.length() > 0) {
        str = this.debugGui.dim.eval(str);
        paramObject = str;
        if (str == null)
          paramObject = ""; 
      } 
      this.values.set(paramInt1, paramObject);
      updateModel();
      if (paramInt1 + 1 == this.expressions.size()) {
        this.expressions.add("");
        this.values.add("");
        fireTableRowsInserted(paramInt1 + 1, paramInt1 + 1);
      } 
    } 
  }
  
  void updateModel() {
    for (byte b = 0; b < this.expressions.size(); b++) {
      String str = this.expressions.get(b);
      if (str.length() > 0) {
        String str1 = this.debugGui.dim.eval(str);
        str = str1;
        if (str1 == null)
          str = ""; 
      } else {
        str = "";
      } 
      str = str.replace('\n', ' ');
      this.values.set(b, str);
    } 
    fireTableDataChanged();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/MyTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */