package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.tools.debugger.treetable.TreeTableModel;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

class VariableModel implements TreeTableModel {
  private static final VariableNode[] CHILDLESS;
  
  private static final String[] cNames = new String[] { " Name", " Value" };
  
  private static final Class<?>[] cTypes = new Class[] { TreeTableModel.class, String.class };
  
  private Dim debugger;
  
  private VariableNode root;
  
  static {
    CHILDLESS = new VariableNode[0];
  }
  
  public VariableModel() {}
  
  public VariableModel(Dim paramDim, Object paramObject) {
    this.debugger = paramDim;
    this.root = new VariableNode(paramObject, "this");
  }
  
  private VariableNode[] children(VariableNode paramVariableNode) {
    VariableNode[] arrayOfVariableNode2;
    if (paramVariableNode.children != null)
      return paramVariableNode.children; 
    Object object = getValue(paramVariableNode);
    Object[] arrayOfObject = this.debugger.getObjectIds(object);
    if (arrayOfObject == null || arrayOfObject.length == 0) {
      arrayOfVariableNode2 = CHILDLESS;
      VariableNode.access$002(paramVariableNode, arrayOfVariableNode2);
      return arrayOfVariableNode2;
    } 
    Arrays.sort(arrayOfObject, new Comparator() {
          public int compare(Object param1Object1, Object param1Object2) {
            return (param1Object1 instanceof String) ? ((param1Object2 instanceof Integer) ? -1 : ((String)param1Object1).compareToIgnoreCase((String)param1Object2)) : ((param1Object2 instanceof String) ? 1 : (((Integer)param1Object1).intValue() - ((Integer)param1Object2).intValue()));
          }
        });
    VariableNode[] arrayOfVariableNode1 = new VariableNode[arrayOfObject.length];
    byte b = 0;
    while (true) {
      arrayOfVariableNode2 = arrayOfVariableNode1;
      if (b != arrayOfObject.length) {
        arrayOfVariableNode1[b] = new VariableNode(object, arrayOfObject[b]);
        b++;
        continue;
      } 
      break;
    } 
    VariableNode.access$002(paramVariableNode, arrayOfVariableNode2);
    return arrayOfVariableNode2;
  }
  
  public void addTreeModelListener(TreeModelListener paramTreeModelListener) {}
  
  public Object getChild(Object paramObject, int paramInt) {
    return (this.debugger == null) ? null : children((VariableNode)paramObject)[paramInt];
  }
  
  public int getChildCount(Object paramObject) {
    return (this.debugger == null) ? 0 : (children((VariableNode)paramObject)).length;
  }
  
  public Class<?> getColumnClass(int paramInt) {
    return cTypes[paramInt];
  }
  
  public int getColumnCount() {
    return cNames.length;
  }
  
  public String getColumnName(int paramInt) {
    return cNames[paramInt];
  }
  
  public int getIndexOfChild(Object paramObject1, Object paramObject2) {
    if (this.debugger == null)
      return -1; 
    VariableNode variableNode = (VariableNode)paramObject1;
    paramObject1 = paramObject2;
    paramObject2 = children(variableNode);
    for (byte b = 0; b != paramObject2.length; b++) {
      if (paramObject2[b] == paramObject1)
        return b; 
    } 
    return -1;
  }
  
  public Object getRoot() {
    return (this.debugger == null) ? null : this.root;
  }
  
  public Object getValue(VariableNode paramVariableNode) {
    try {
      return this.debugger.getObjectProperty(paramVariableNode.object, paramVariableNode.id);
    } catch (Exception exception) {
      return "undefined";
    } 
  }
  
  public Object getValueAt(Object paramObject, int paramInt) {
    String str;
    Dim dim = this.debugger;
    if (dim == null)
      return null; 
    paramObject = paramObject;
    if (paramInt != 0) {
      if (paramInt != 1)
        return null; 
      try {
        paramObject = dim.objectToString(getValue((VariableNode)paramObject));
      } catch (RuntimeException runtimeException) {
        str = runtimeException.getMessage();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      int i = str.length();
      for (paramInt = 0; paramInt < i; paramInt++) {
        char c = str.charAt(paramInt);
        char c1 = c;
        if (Character.isISOControl(c)) {
          byte b = 32;
          c1 = b;
        } 
        stringBuilder.append(c1);
      } 
      return stringBuilder.toString();
    } 
    return str.toString();
  }
  
  public boolean isCellEditable(Object paramObject, int paramInt) {
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isLeaf(Object paramObject) {
    Dim dim = this.debugger;
    boolean bool = true;
    if (dim == null)
      return true; 
    if ((children((VariableNode)paramObject)).length != 0)
      bool = false; 
    return bool;
  }
  
  public void removeTreeModelListener(TreeModelListener paramTreeModelListener) {}
  
  public void setValueAt(Object paramObject1, Object paramObject2, int paramInt) {}
  
  public void valueForPathChanged(TreePath paramTreePath, Object paramObject) {}
  
  private static class VariableNode {
    private VariableNode[] children;
    
    private Object id;
    
    private Object object;
    
    public VariableNode(Object param1Object1, Object param1Object2) {
      this.object = param1Object1;
      this.id = param1Object2;
    }
    
    public String toString() {
      Object object = this.id;
      if (object instanceof String) {
        object = object;
      } else {
        object = new StringBuilder();
        object.append("[");
        object.append(((Integer)this.id).intValue());
        object.append("]");
        object = object.toString();
      } 
      return (String)object;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/VariableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */