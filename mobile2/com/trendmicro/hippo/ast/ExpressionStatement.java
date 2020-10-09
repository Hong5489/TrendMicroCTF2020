package com.trendmicro.hippo.ast;

public class ExpressionStatement extends AstNode {
  private AstNode expr;
  
  public ExpressionStatement() {}
  
  public ExpressionStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ExpressionStatement(int paramInt1, int paramInt2, AstNode paramAstNode) {
    super(paramInt1, paramInt2);
    setExpression(paramAstNode);
  }
  
  public ExpressionStatement(AstNode paramAstNode) {
    this(paramAstNode.getPosition(), paramAstNode.getLength(), paramAstNode);
  }
  
  public ExpressionStatement(AstNode paramAstNode, boolean paramBoolean) {
    this(paramAstNode);
    if (paramBoolean)
      setHasResult(); 
  }
  
  public AstNode getExpression() {
    return this.expr;
  }
  
  public boolean hasSideEffects() {
    return (this.type == 135 || this.expr.hasSideEffects());
  }
  
  public void setExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.expr = paramAstNode;
    paramAstNode.setParent(this);
    setLineno(paramAstNode.getLineno());
  }
  
  public void setHasResult() {
    this.type = 135;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.expr.toSource(paramInt));
    stringBuilder.append(";");
    if (getInlineComment() != null)
      stringBuilder.append(getInlineComment().toSource(paramInt)); 
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this))
      this.expr.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ExpressionStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */