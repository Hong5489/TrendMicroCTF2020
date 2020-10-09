package com.trendmicro.hippo.ast;

public class ForInLoop extends Loop {
  protected int eachPosition = -1;
  
  protected int inPosition = -1;
  
  protected boolean isForEach;
  
  protected boolean isForOf;
  
  protected AstNode iteratedObject;
  
  protected AstNode iterator;
  
  public ForInLoop() {}
  
  public ForInLoop(int paramInt) {
    super(paramInt);
  }
  
  public ForInLoop(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public int getEachPosition() {
    return this.eachPosition;
  }
  
  public int getInPosition() {
    return this.inPosition;
  }
  
  public AstNode getIteratedObject() {
    return this.iteratedObject;
  }
  
  public AstNode getIterator() {
    return this.iterator;
  }
  
  public boolean isForEach() {
    return this.isForEach;
  }
  
  public boolean isForOf() {
    return this.isForOf;
  }
  
  public void setEachPosition(int paramInt) {
    this.eachPosition = paramInt;
  }
  
  public void setInPosition(int paramInt) {
    this.inPosition = paramInt;
  }
  
  public void setIsForEach(boolean paramBoolean) {
    this.isForEach = paramBoolean;
  }
  
  public void setIsForOf(boolean paramBoolean) {
    this.isForOf = paramBoolean;
  }
  
  public void setIteratedObject(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.iteratedObject = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setIterator(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.iterator = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("for ");
    if (isForEach())
      stringBuilder.append("each "); 
    stringBuilder.append("(");
    stringBuilder.append(this.iterator.toSource(0));
    if (this.isForOf) {
      stringBuilder.append(" of ");
    } else {
      stringBuilder.append(" in ");
    } 
    stringBuilder.append(this.iteratedObject.toSource(0));
    stringBuilder.append(") ");
    if (this.body.getType() == 130) {
      stringBuilder.append(this.body.toSource(paramInt).trim());
      stringBuilder.append("\n");
    } else {
      stringBuilder.append("\n");
      stringBuilder.append(this.body.toSource(paramInt + 1));
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.iterator.visit(paramNodeVisitor);
      this.iteratedObject.visit(paramNodeVisitor);
      this.body.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ForInLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */