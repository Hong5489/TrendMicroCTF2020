package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ObjectLiteral extends AstNode implements DestructuringForm {
  private static final List<ObjectProperty> NO_ELEMS = Collections.unmodifiableList(new ArrayList<>());
  
  private List<ObjectProperty> elements;
  
  boolean isDestructuring;
  
  public ObjectLiteral() {}
  
  public ObjectLiteral(int paramInt) {
    super(paramInt);
  }
  
  public ObjectLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addElement(ObjectProperty paramObjectProperty) {
    assertNotNull(paramObjectProperty);
    if (this.elements == null)
      this.elements = new ArrayList<>(); 
    this.elements.add(paramObjectProperty);
    paramObjectProperty.setParent(this);
  }
  
  public List<ObjectProperty> getElements() {
    List<ObjectProperty> list = this.elements;
    if (list == null)
      list = NO_ELEMS; 
    return list;
  }
  
  public boolean isDestructuring() {
    return this.isDestructuring;
  }
  
  public void setElements(List<ObjectProperty> paramList) {
    if (paramList == null) {
      this.elements = null;
    } else {
      List<ObjectProperty> list = this.elements;
      if (list != null)
        list.clear(); 
      Iterator<ObjectProperty> iterator = paramList.iterator();
      while (iterator.hasNext())
        addElement(iterator.next()); 
    } 
  }
  
  public void setIsDestructuring(boolean paramBoolean) {
    this.isDestructuring = paramBoolean;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("{");
    List<ObjectProperty> list = this.elements;
    if (list != null)
      printList(list, stringBuilder); 
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<ObjectProperty> iterator = getElements().iterator();
      while (iterator.hasNext())
        ((ObjectProperty)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ObjectLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */