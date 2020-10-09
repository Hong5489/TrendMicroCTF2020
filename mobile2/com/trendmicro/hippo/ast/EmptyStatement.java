package com.trendmicro.hippo.ast;

public class EmptyStatement extends AstNode {
  public EmptyStatement() {}
  
  public EmptyStatement(int paramInt) {
    super(paramInt);
  }
  
  public EmptyStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(";\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/EmptyStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */