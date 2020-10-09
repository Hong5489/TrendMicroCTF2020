package com.trendmicro.hippo.ast;

public class ParenthesizedExpression extends AstNode {
  private AstNode expression;
  
  public ParenthesizedExpression() {}
  
  public ParenthesizedExpression(int paramInt) {
    super(paramInt);
  }
  
  public ParenthesizedExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ParenthesizedExpression(int paramInt1, int paramInt2, AstNode paramAstNode) {
    super(paramInt1, paramInt2);
    setExpression(paramAstNode);
  }
  
  public ParenthesizedExpression(AstNode paramAstNode) {
    this(bool1, bool2, paramAstNode);
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
    stringBuilder.append("(");
    stringBuilder.append(this.expression.toSource(0));
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this))
      this.expression.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ParenthesizedExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */