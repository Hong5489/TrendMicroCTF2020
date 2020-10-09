package com.trendmicro.hippo.tools.debugger;

import javax.swing.JTable;

class Evaluator extends JTable {
  private static final long serialVersionUID = 8133672432982594256L;
  
  MyTableModel tableModel = (MyTableModel)getModel();
  
  public Evaluator(SwingGui paramSwingGui) {
    super(new MyTableModel(paramSwingGui));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/Evaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */