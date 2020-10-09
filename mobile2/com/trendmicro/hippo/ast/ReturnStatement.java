package com.trendmicro.hippo.ast;

public class ReturnStatement extends AstNode {
  private AstNode returnValue;
  
  public ReturnStatement() {}
  
  public ReturnStatement(int paramInt) {
    super(paramInt);
  }
  
  public ReturnStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ReturnStatement(int paramInt1, int paramInt2, AstNode paramAstNode) {
    super(paramInt1, paramInt2);
    setReturnValue(paramAstNode);
  }
  
  public AstNode getReturnValue() {
    return this.returnValue;
  }
  
  public void setReturnValue(AstNode paramAstNode) {
    this.returnValue = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("return");
    if (this.returnValue != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.returnValue.toSource(0));
    } 
    stringBuilder.append(";\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      AstNode astNode = this.returnValue;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ReturnStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */