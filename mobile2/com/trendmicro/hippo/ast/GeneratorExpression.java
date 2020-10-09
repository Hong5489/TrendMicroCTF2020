package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneratorExpression extends Scope {
  private AstNode filter;
  
  private int ifPosition = -1;
  
  private List<GeneratorExpressionLoop> loops = new ArrayList<>();
  
  private int lp = -1;
  
  private AstNode result;
  
  private int rp = -1;
  
  public GeneratorExpression() {}
  
  public GeneratorExpression(int paramInt) {
    super(paramInt);
  }
  
  public GeneratorExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addLoop(GeneratorExpressionLoop paramGeneratorExpressionLoop) {
    assertNotNull(paramGeneratorExpressionLoop);
    this.loops.add(paramGeneratorExpressionLoop);
    paramGeneratorExpressionLoop.setParent(this);
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
  
  public List<GeneratorExpressionLoop> getLoops() {
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
  
  public void setLoops(List<GeneratorExpressionLoop> paramList) {
    assertNotNull(paramList);
    this.loops.clear();
    Iterator<GeneratorExpressionLoop> iterator = paramList.iterator();
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
    stringBuilder.append("(");
    stringBuilder.append(this.result.toSource(0));
    Iterator<GeneratorExpressionLoop> iterator = this.loops.iterator();
    while (iterator.hasNext())
      stringBuilder.append(((GeneratorExpressionLoop)iterator.next()).toSource(0)); 
    if (this.filter != null) {
      stringBuilder.append(" if (");
      stringBuilder.append(this.filter.toSource(0));
      stringBuilder.append(")");
    } 
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (!paramNodeVisitor.visit(this))
      return; 
    this.result.visit(paramNodeVisitor);
    Iterator<GeneratorExpressionLoop> iterator = this.loops.iterator();
    while (iterator.hasNext())
      ((GeneratorExpressionLoop)iterator.next()).visit(paramNodeVisitor); 
    AstNode astNode = this.filter;
    if (astNode != null)
      astNode.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/GeneratorExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */