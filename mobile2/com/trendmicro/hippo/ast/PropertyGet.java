package com.trendmicro.hippo.ast;

public class PropertyGet extends InfixExpression {
  public PropertyGet() {}
  
  public PropertyGet(int paramInt) {
    super(paramInt);
  }
  
  public PropertyGet(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public PropertyGet(int paramInt1, int paramInt2, AstNode paramAstNode, Name paramName) {
    super(paramInt1, paramInt2, paramAstNode, paramName);
  }
  
  public PropertyGet(AstNode paramAstNode, Name paramName) {
    super(paramAstNode, paramName);
  }
  
  public PropertyGet(AstNode paramAstNode, Name paramName, int paramInt) {
    super(33, paramAstNode, paramName, paramInt);
  }
  
  public Name getProperty() {
    return (Name)getRight();
  }
  
  public AstNode getTarget() {
    return getLeft();
  }
  
  public void setProperty(Name paramName) {
    setRight(paramName);
  }
  
  public void setTarget(AstNode paramAstNode) {
    setLeft(paramAstNode);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(getLeft().toSource(0));
    stringBuilder.append(".");
    stringBuilder.append(getRight().toSource(0));
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      getTarget().visit(paramNodeVisitor);
      getProperty().visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/PropertyGet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */