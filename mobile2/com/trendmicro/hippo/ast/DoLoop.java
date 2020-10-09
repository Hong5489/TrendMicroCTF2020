package com.trendmicro.hippo.ast;

public class DoLoop extends Loop {
  private AstNode condition;
  
  private int whilePosition = -1;
  
  public DoLoop() {}
  
  public DoLoop(int paramInt) {
    super(paramInt);
  }
  
  public DoLoop(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getCondition() {
    return this.condition;
  }
  
  public int getWhilePosition() {
    return this.whilePosition;
  }
  
  public void setCondition(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.condition = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setWhilePosition(int paramInt) {
    this.whilePosition = paramInt;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("do ");
    if (getInlineComment() != null) {
      stringBuilder.append(getInlineComment().toSource(paramInt + 1));
      stringBuilder.append("\n");
    } 
    stringBuilder.append(this.body.toSource(paramInt).trim());
    stringBuilder.append(" while (");
    stringBuilder.append(this.condition.toSource(0));
    stringBuilder.append(");\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.body.visit(paramNodeVisitor);
      this.condition.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/DoLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */