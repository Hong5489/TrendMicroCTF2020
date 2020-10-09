package com.trendmicro.hippo.ast;

public class WithStatement extends AstNode {
  private AstNode expression;
  
  private int lp = -1;
  
  private int rp = -1;
  
  private AstNode statement;
  
  public WithStatement() {}
  
  public WithStatement(int paramInt) {
    super(paramInt);
  }
  
  public WithStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
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
  
  public AstNode getStatement() {
    return this.statement;
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
  
  public void setStatement(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.statement = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("with (");
    stringBuilder.append(this.expression.toSource(0));
    stringBuilder.append(") ");
    if (getInlineComment() != null)
      stringBuilder.append(getInlineComment().toSource(paramInt + 1)); 
    if (this.statement.getType() == 130) {
      if (getInlineComment() != null)
        stringBuilder.append("\n"); 
      stringBuilder.append(this.statement.toSource(paramInt).trim());
      stringBuilder.append("\n");
    } else {
      stringBuilder.append("\n");
      stringBuilder.append(this.statement.toSource(paramInt + 1));
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.expression.visit(paramNodeVisitor);
      this.statement.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/WithStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */