package com.trendmicro.hippo.ast;

public abstract class Loop extends Scope {
  protected AstNode body;
  
  protected int lp = -1;
  
  protected int rp = -1;
  
  public Loop() {}
  
  public Loop(int paramInt) {
    super(paramInt);
  }
  
  public Loop(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getBody() {
    return this.body;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public void setBody(AstNode paramAstNode) {
    this.body = paramAstNode;
    setLength(paramAstNode.getPosition() + paramAstNode.getLength() - getPosition());
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
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Loop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */