package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayComprehension extends Scope {
  private AstNode filter;
  
  private int ifPosition = -1;
  
  private List<ArrayComprehensionLoop> loops = new ArrayList<>();
  
  private int lp = -1;
  
  private AstNode result;
  
  private int rp = -1;
  
  public ArrayComprehension() {}
  
  public ArrayComprehension(int paramInt) {
    super(paramInt);
  }
  
  public ArrayComprehension(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addLoop(ArrayComprehensionLoop paramArrayComprehensionLoop) {
    assertNotNull(paramArrayComprehensionLoop);
    this.loops.add(paramArrayComprehensionLoop);
    paramArrayComprehensionLoop.setParent(this);
  }
  
  public AstNode getFilter() {
    return this.filter;
  }
  
  public int getFilterLp() {
    return this.lp;
  }
  
  public int getFilterRp() {
    return this.rp;
  }
  
  public int getIfPosition() {
    return this.ifPosition;
  }
  
  public List<ArrayComprehensionLoop> getLoops() {
    return this.loops;
  }
  
  public AstNode getResult() {
    return this.result;
  }
  
  public void setFilter(AstNode paramAstNode) {
    this.filter = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setFilterLp(int paramInt) {
    this.lp = paramInt;
  }
  
  public void setFilterRp(int paramInt) {
    this.rp = paramInt;
  }
  
  public void setIfPosition(int paramInt) {
    this.ifPosition = paramInt;
  }
  
  public void setLoops(List<ArrayComprehensionLoop> paramList) {
    assertNotNull(paramList);
    this.loops.clear();
    Iterator<ArrayComprehensionLoop> iterator = paramList.iterator();
    while (iterator.hasNext())
      addLoop(iterator.next()); 
  }
  
  public void setResult(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.result = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(250);
    stringBuilder.append("[");
    stringBuilder.append(this.result.toSource(0));
    Iterator<ArrayComprehensionLoop> iterator = this.loops.iterator();
    while (iterator.hasNext())
      stringBuilder.append(((ArrayComprehensionLoop)iterator.next()).toSource(0)); 
    if (this.filter != null) {
      stringBuilder.append(" if (");
      stringBuilder.append(this.filter.toSource(0));
      stringBuilder.append(")");
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (!paramNodeVisitor.visit(this))
      return; 
    this.result.visit(paramNodeVisitor);
    Iterator<ArrayComprehensionLoop> iterator = this.loops.iterator();
    while (iterator.hasNext())
      ((ArrayComprehensionLoop)iterator.next()).visit(paramNodeVisitor); 
    AstNode astNode = this.filter;
    if (astNode != null)
      astNode.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ArrayComprehension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */