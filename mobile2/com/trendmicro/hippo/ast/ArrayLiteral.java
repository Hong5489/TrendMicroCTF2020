package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ArrayLiteral extends AstNode implements DestructuringForm {
  private static final List<AstNode> NO_ELEMS = Collections.unmodifiableList(new ArrayList<>());
  
  private int destructuringLength;
  
  private List<AstNode> elements;
  
  private boolean isDestructuring;
  
  private int skipCount;
  
  public ArrayLiteral() {}
  
  public ArrayLiteral(int paramInt) {
    super(paramInt);
  }
  
  public ArrayLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addElement(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    if (this.elements == null)
      this.elements = new ArrayList<>(); 
    this.elements.add(paramAstNode);
    paramAstNode.setParent(this);
  }
  
  public int getDestructuringLength() {
    return this.destructuringLength;
  }
  
  public AstNode getElement(int paramInt) {
    List<AstNode> list = this.elements;
    if (list != null)
      return list.get(paramInt); 
    throw new IndexOutOfBoundsException("no elements");
  }
  
  public List<AstNode> getElements() {
    List<AstNode> list = this.elements;
    if (list == null)
      list = NO_ELEMS; 
    return list;
  }
  
  public int getSize() {
    int i;
    List<AstNode> list = this.elements;
    if (list == null) {
      i = 0;
    } else {
      i = list.size();
    } 
    return i;
  }
  
  public int getSkipCount() {
    return this.skipCount;
  }
  
  public boolean isDestructuring() {
    return this.isDestructuring;
  }
  
  public void setDestructuringLength(int paramInt) {
    this.destructuringLength = paramInt;
  }
  
  public void setElements(List<AstNode> paramList) {
    if (paramList == null) {
      this.elements = null;
    } else {
      List<AstNode> list = this.elements;
      if (list != null)
        list.clear(); 
      Iterator<AstNode> iterator = paramList.iterator();
      while (iterator.hasNext())
        addElement(iterator.next()); 
    } 
  }
  
  public void setIsDestructuring(boolean paramBoolean) {
    this.isDestructuring = paramBoolean;
  }
  
  public void setSkipCount(int paramInt) {
    this.skipCount = paramInt;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("[");
    List<AstNode> list = this.elements;
    if (list != null)
      printList(list, stringBuilder); 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<AstNode> iterator = getElements().iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ArrayLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */