package com.trendmicro.hippo.ast;

public class ArrayComprehensionLoop extends ForInLoop {
  public ArrayComprehensionLoop() {}
  
  public ArrayComprehensionLoop(int paramInt) {
    super(paramInt);
  }
  
  public ArrayComprehensionLoop(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getBody() {
    return null;
  }
  
  public void setBody(AstNode paramAstNode) {
    throw new UnsupportedOperationException("this node type has no body");
  }
  
  public String toSource(int paramInt) {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(" for ");
    if (isForEach()) {
      str = "each ";
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    stringBuilder.append("(");
    stringBuilder.append(this.iterator.toSource(0));
    if (isForOf()) {
      str = " of ";
    } else {
      str = " in ";
    } 
    stringBuilder.append(str);
    stringBuilder.append(this.iteratedObject.toSource(0));
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.iterator.visit(paramNodeVisitor);
      this.iteratedObject.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ArrayComprehensionLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */