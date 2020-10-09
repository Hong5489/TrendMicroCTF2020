package com.trendmicro.hippo.ast;

public class ThrowStatement extends AstNode {
  private AstNode expression;
  
  public ThrowStatement() {}
  
  public ThrowStatement(int paramInt) {
    super(paramInt);
  }
  
  public ThrowStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ThrowStatement(int paramInt1, int paramInt2, AstNode paramAstNode) {
    super(paramInt1, paramInt2);
    setExpression(paramAstNode);
  }
  
  public ThrowStatement(int paramInt, AstNode paramAstNode) {
    super(paramInt, paramAstNode.getLength());
    setExpression(paramAstNode);
  }
  
  public ThrowStatement(AstNode paramAstNode) {
    setExpression(paramAstNode);
  }
  
  public AstNode getExpression() {
    return this.expression;
  }
  
  public void setExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.expression = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("throw");
    stringBuilder.append(" ");
    stringBuilder.append(this.expression.toSource(0));
    stringBuilder.append(";\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this))
      this.expression.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ThrowStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */