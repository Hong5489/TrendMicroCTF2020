package com.trendmicro.hippo.ast;

public class ConditionalExpression extends AstNode {
  private int colonPosition = -1;
  
  private AstNode falseExpression;
  
  private int questionMarkPosition = -1;
  
  private AstNode testExpression;
  
  private AstNode trueExpression;
  
  public ConditionalExpression() {}
  
  public ConditionalExpression(int paramInt) {
    super(paramInt);
  }
  
  public ConditionalExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public int getColonPosition() {
    return this.colonPosition;
  }
  
  public AstNode getFalseExpression() {
    return this.falseExpression;
  }
  
  public int getQuestionMarkPosition() {
    return this.questionMarkPosition;
  }
  
  public AstNode getTestExpression() {
    return this.testExpression;
  }
  
  public AstNode getTrueExpression() {
    return this.trueExpression;
  }
  
  public boolean hasSideEffects() {
    boolean bool;
    if (this.testExpression == null || this.trueExpression == null || this.falseExpression == null)
      codeBug(); 
    if (this.trueExpression.hasSideEffects() && this.falseExpression.hasSideEffects()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setColonPosition(int paramInt) {
    this.colonPosition = paramInt;
  }
  
  public void setFalseExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.falseExpression = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setQuestionMarkPosition(int paramInt) {
    this.questionMarkPosition = paramInt;
  }
  
  public void setTestExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.testExpression = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setTrueExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.trueExpression = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.testExpression.toSource(paramInt));
    stringBuilder.append(" ? ");
    stringBuilder.append(this.trueExpression.toSource(0));
    stringBuilder.append(" : ");
    stringBuilder.append(this.falseExpression.toSource(0));
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.testExpression.visit(paramNodeVisitor);
      this.trueExpression.visit(paramNodeVisitor);
      this.falseExpression.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ConditionalExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */