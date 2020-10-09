package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Token;

public class InfixExpression extends AstNode {
  protected AstNode left;
  
  protected int operatorPosition = -1;
  
  protected AstNode right;
  
  public InfixExpression() {}
  
  public InfixExpression(int paramInt) {
    super(paramInt);
  }
  
  public InfixExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public InfixExpression(int paramInt1, int paramInt2, AstNode paramAstNode1, AstNode paramAstNode2) {
    super(paramInt1, paramInt2);
    setLeft(paramAstNode1);
    setRight(paramAstNode2);
  }
  
  public InfixExpression(int paramInt1, AstNode paramAstNode1, AstNode paramAstNode2, int paramInt2) {
    setType(paramInt1);
    setOperatorPosition(paramInt2 - paramAstNode1.getPosition());
    setLeftAndRight(paramAstNode1, paramAstNode2);
  }
  
  public InfixExpression(AstNode paramAstNode1, AstNode paramAstNode2) {
    setLeftAndRight(paramAstNode1, paramAstNode2);
  }
  
  public AstNode getLeft() {
    return this.left;
  }
  
  public int getOperator() {
    return getType();
  }
  
  public int getOperatorPosition() {
    return this.operatorPosition;
  }
  
  public AstNode getRight() {
    return this.right;
  }
  
  public boolean hasSideEffects() {
    int i = getType();
    boolean bool1 = true;
    boolean bool2 = true;
    if (i != 90) {
      if (i != 105 && i != 106)
        return super.hasSideEffects(); 
      AstNode astNode1 = this.left;
      if (astNode1 == null || !astNode1.hasSideEffects()) {
        astNode1 = this.right;
        if (astNode1 == null || !astNode1.hasSideEffects())
          bool2 = false; 
      } 
      return bool2;
    } 
    AstNode astNode = this.right;
    if (astNode != null && astNode.hasSideEffects()) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  public void setLeft(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.left = paramAstNode;
    setLineno(paramAstNode.getLineno());
    paramAstNode.setParent(this);
  }
  
  public void setLeftAndRight(AstNode paramAstNode1, AstNode paramAstNode2) {
    assertNotNull(paramAstNode1);
    assertNotNull(paramAstNode2);
    setBounds(paramAstNode1.getPosition(), paramAstNode2.getPosition() + paramAstNode2.getLength());
    setLeft(paramAstNode1);
    setRight(paramAstNode2);
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
  
  public void setOperatorPosition(int paramInt) {
    this.operatorPosition = paramInt;
  }
  
  public void setRight(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.right = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.left.toSource());
    stringBuilder.append(" ");
    stringBuilder.append(operatorToString(getType()));
    stringBuilder.append(" ");
    stringBuilder.append(this.right.toSource());
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.left.visit(paramNodeVisitor);
      this.right.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/InfixExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */