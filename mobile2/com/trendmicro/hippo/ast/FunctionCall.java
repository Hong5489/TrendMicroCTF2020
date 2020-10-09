package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FunctionCall extends AstNode {
  protected static final List<AstNode> NO_ARGS = Collections.unmodifiableList(new ArrayList<>());
  
  protected List<AstNode> arguments;
  
  protected int lp = -1;
  
  protected int rp = -1;
  
  protected AstNode target;
  
  public FunctionCall() {}
  
  public FunctionCall(int paramInt) {
    super(paramInt);
  }
  
  public FunctionCall(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addArgument(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    if (this.arguments == null)
      this.arguments = new ArrayList<>(); 
    this.arguments.add(paramAstNode);
    paramAstNode.setParent(this);
  }
  
  public List<AstNode> getArguments() {
    List<AstNode> list = this.arguments;
    if (list == null)
      list = NO_ARGS; 
    return list;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public AstNode getTarget() {
    return this.target;
  }
  
  public void setArguments(List<AstNode> paramList) {
    if (paramList == null) {
      this.arguments = null;
    } else {
      List<AstNode> list = this.arguments;
      if (list != null)
        list.clear(); 
      Iterator<AstNode> iterator = paramList.iterator();
      while (iterator.hasNext())
        addArgument(iterator.next()); 
    } 
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
  
  public void setTarget(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.target = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.target.toSource(0));
    stringBuilder.append("(");
    List<AstNode> list = this.arguments;
    if (list != null)
      printList(list, stringBuilder); 
    stringBuilder.append(")");
    if (getInlineComment() != null) {
      stringBuilder.append(getInlineComment().toSource(paramInt));
      stringBuilder.append("\n");
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.target.visit(paramNodeVisitor);
      Iterator<AstNode> iterator = getArguments().iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/FunctionCall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */