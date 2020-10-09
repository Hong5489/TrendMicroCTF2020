package com.trendmicro.hippo.ast;

public class ErrorNode extends AstNode {
  private String message;
  
  public ErrorNode() {}
  
  public ErrorNode(int paramInt) {
    super(paramInt);
  }
  
  public ErrorNode(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public void setMessage(String paramString) {
    this.message = paramString;
  }
  
  public String toSource(int paramInt) {
    return "";
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ErrorNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */