package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SwitchStatement extends Jump {
  private static final List<SwitchCase> NO_CASES = Collections.unmodifiableList(new ArrayList<>());
  
  private List<SwitchCase> cases;
  
  private AstNode expression;
  
  private int lp = -1;
  
  private int rp = -1;
  
  public SwitchStatement() {}
  
  public SwitchStatement(int paramInt) {
    this.position = paramInt;
  }
  
  public SwitchStatement(int paramInt1, int paramInt2) {
    this.position = paramInt1;
    this.length = paramInt2;
  }
  
  public void addCase(SwitchCase paramSwitchCase) {
    assertNotNull(paramSwitchCase);
    if (this.cases == null)
      this.cases = new ArrayList<>(); 
    this.cases.add(paramSwitchCase);
    paramSwitchCase.setParent(this);
  }
  
  public List<SwitchCase> getCases() {
    List<SwitchCase> list = this.cases;
    if (list == null)
      list = NO_CASES; 
    return list;
  }
  
  public AstNode getExpression() {
    return this.expression;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public void setCases(List<SwitchCase> paramList) {
    if (paramList == null) {
      this.cases = null;
    } else {
      List<SwitchCase> list = this.cases;
      if (list != null)
        list.clear(); 
      Iterator<SwitchCase> iterator = paramList.iterator();
      while (iterator.hasNext())
        addCase(iterator.next()); 
    } 
  }
  
  public void setExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.expression = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setLp(int paramInt) {
    this.lp = paramInt;
  }
  
  public void setParens(int paramInt1, int paramInt2) {
    this.lp = paramInt1;
    this.rp = paramInt2;
  }
  
  public void setRp(int paramInt) {
    this.rp = paramInt;
  }
  
  public String toSource(int paramInt) {
    String str = makeIndent(paramInt);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append("switch (");
    stringBuilder.append(this.expression.toSource(0));
    stringBuilder.append(") {\n");
    List<SwitchCase> list = this.cases;
    if (list != null) {
      Iterator<SwitchCase> iterator = list.iterator();
      while (iterator.hasNext())
        stringBuilder.append(((SwitchCase)iterator.next()).toSource(paramInt + 1)); 
    } 
    stringBuilder.append(str);
    stringBuilder.append("}\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.expression.visit(paramNodeVisitor);
      Iterator<SwitchCase> iterator = getCases().iterator();
      while (iterator.hasNext())
        ((SwitchCase)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/SwitchStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */