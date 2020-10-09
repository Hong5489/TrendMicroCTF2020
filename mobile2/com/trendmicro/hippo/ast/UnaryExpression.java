package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Token;

public class UnaryExpression extends AstNode {
  private boolean isPostfix;
  
  private AstNode operand;
  
  public UnaryExpression() {}
  
  public UnaryExpression(int paramInt) {
    super(paramInt);
  }
  
  public UnaryExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public UnaryExpression(int paramInt1, int paramInt2, AstNode paramAstNode) {
    this(paramInt1, paramInt2, paramAstNode, false);
  }
  
  public UnaryExpression(int paramInt1, int paramInt2, AstNode paramAstNode, boolean paramBoolean) {
    int i;
    assertNotNull(paramAstNode);
    if (paramBoolean) {
      i = paramAstNode.getPosition();
    } else {
      i = paramInt2;
    } 
    if (paramBoolean) {
      paramInt2 += 2;
    } else {
      paramInt2 = paramAstNode.getPosition() + paramAstNode.getLength();
    } 
    setBounds(i, paramInt2);
    setOperator(paramInt1);
    setOperand(paramAstNode);
    this.isPostfix = paramBoolean;
  }
  
  public AstNode getOperand() {
    return this.operand;
  }
  
  public int getOperator() {
    return this.type;
  }
  
  public boolean isPostfix() {
    return this.isPostfix;
  }
  
  public boolean isPrefix() {
    return this.isPostfix ^ true;
  }
  
  public void setIsPostfix(boolean paramBoolean) {
    this.isPostfix = paramBoolean;
  }
  
  public void setOperand(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.operand = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setOperator(int paramInt) {
    if (Token.isValidToken(paramInt)) {
      setType(paramInt);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid token: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    paramInt = getType();
    if (!this.isPostfix) {
      stringBuilder.append(operatorToString(paramInt));
      if (paramInt == 32 || paramInt == 31 || paramInt == 127)
        stringBuilder.append(" "); 
    } 
    stringBuilder.append(this.operand.toSource());
    if (this.isPostfix)
      stringBuilder.append(operatorToString(paramInt)); 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this))
      this.operand.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/UnaryExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */