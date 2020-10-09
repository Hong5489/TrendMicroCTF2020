package com.trendmicro.hippo.tools.debugger.treetable;

import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public class AbstractCellEditor implements CellEditor {
  protected EventListenerList listenerList = new EventListenerList();
  
  public void addCellEditorListener(CellEditorListener paramCellEditorListener) {
    this.listenerList.add(CellEditorListener.class, paramCellEditorListener);
  }
  
  public void cancelCellEditing() {}
  
  protected void fireEditingCanceled() {
    Object[] arrayOfObject = this.listenerList.getListenerList();
    for (int i = arrayOfObject.length - 2; i >= 0; i -= 2) {
      if (arrayOfObject[i] == CellEditorListener.class)
        ((CellEditorListener)arrayOfObject[i + 1]).editingCanceled(new ChangeEvent(this)); 
    } 
  }
  
  protected void fireEditingStopped() {
    Object[] arrayOfObject = this.listenerList.getListenerList();
    for (int i = arrayOfObject.length - 2; i >= 0; i -= 2) {
      if (arrayOfObject[i] == CellEditorListener.class)
        ((CellEditorListener)arrayOfObject[i + 1]).editingStopped(new ChangeEvent(this)); 
    } 
  }
  
  public Object getCellEditorValue() {
    return null;
  }
  
  public boolean isCellEditable(EventObject paramEventObject) {
    return true;
  }
  
  public void removeCellEditorListener(CellEditorListener paramCellEditorListener) {
    this.listenerList.remove(CellEditorListener.class, paramCellEditorListener);
  }
  
  public boolean shouldSelectCell(EventObject paramEventObject) {
    return false;
  }
  
  public boolean stopCellEditing() {
    return true;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/treetable/AbstractCellEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */