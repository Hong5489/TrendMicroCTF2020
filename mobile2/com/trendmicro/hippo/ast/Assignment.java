package com.trendmicro.hippo.ast;

public class Assignment extends InfixExpression {
  public Assignment() {}
  
  public Assignment(int paramInt) {
    super(paramInt);
  }
  
  public Assignment(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public Assignment(int paramInt1, int paramInt2, AstNode paramAstNode1, AstNode paramAstNode2) {
    super(paramInt1, paramInt2, paramAstNode1, paramAstNode2);
  }
  
  public Assignment(int paramInt1, AstNode paramAstNode1, AstNode paramAstNode2, int paramInt2) {
    super(paramInt1, paramAstNode1, paramAstNode2, paramInt2);
  }
  
  public Assignment(AstNode paramAstNode1, AstNode paramAstNode2) {
    super(paramAstNode1, paramAstNode2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Assignment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */