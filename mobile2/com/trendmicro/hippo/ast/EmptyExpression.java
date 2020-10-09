package com.trendmicro.hippo.ast;

public class EmptyExpression extends AstNode {
  public EmptyExpression() {}
  
  public EmptyExpression(int paramInt) {
    super(paramInt);
  }
  
  public EmptyExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public String toSource(int paramInt) {
    return makeIndent(paramInt);
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/EmptyExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */