package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TryStatement extends AstNode {
  private static final List<CatchClause> NO_CATCHES = Collections.unmodifiableList(new ArrayList<>());
  
  private List<CatchClause> catchClauses;
  
  private AstNode finallyBlock;
  
  private int finallyPosition = -1;
  
  private AstNode tryBlock;
  
  public TryStatement() {}
  
  public TryStatement(int paramInt) {
    super(paramInt);
  }
  
  public TryStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addCatchClause(CatchClause paramCatchClause) {
    assertNotNull(paramCatchClause);
    if (this.catchClauses == null)
      this.catchClauses = new ArrayList<>(); 
    this.catchClauses.add(paramCatchClause);
    paramCatchClause.setParent(this);
  }
  
  public List<CatchClause> getCatchClauses() {
    List<CatchClause> list = this.catchClauses;
    if (list == null)
      list = NO_CATCHES; 
    return list;
  }
  
  public AstNode getFinallyBlock() {
    return this.finallyBlock;
  }
  
  public int getFinallyPosition() {
    return this.finallyPosition;
  }
  
  public AstNode getTryBlock() {
    return this.tryBlock;
  }
  
  public void setCatchClauses(List<CatchClause> paramList) {
    if (paramList == null) {
      this.catchClauses = null;
    } else {
      List<CatchClause> list = this.catchClauses;
      if (list != null)
        list.clear(); 
      Iterator<CatchClause> iterator = paramList.iterator();
      while (iterator.hasNext())
        addCatchClause(iterator.next()); 
    } 
  }
  
  public void setFinallyBlock(AstNode paramAstNode) {
    this.finallyBlock = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setFinallyPosition(int paramInt) {
    this.finallyPosition = paramInt;
  }
  
  public void setTryBlock(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.tryBlock = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(250);
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("try ");
    if (getInlineComment() != null) {
      stringBuilder.append(getInlineComment().toSource(paramInt + 1));
      stringBuilder.append("\n");
    } 
    stringBuilder.append(this.tryBlock.toSource(paramInt).trim());
    Iterator<CatchClause> iterator = getCatchClauses().iterator();
    while (iterator.hasNext())
      stringBuilder.append(((CatchClause)iterator.next()).toSource(paramInt)); 
    if (this.finallyBlock != null) {
      stringBuilder.append(" finally ");
      stringBuilder.append(this.finallyBlock.toSource(paramInt));
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.tryBlock.visit(paramNodeVisitor);
      Iterator<CatchClause> iterator = getCatchClauses().iterator();
      while (iterator.hasNext())
        ((CatchClause)iterator.next()).visit(paramNodeVisitor); 
      AstNode astNode = this.finallyBlock;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/TryStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */