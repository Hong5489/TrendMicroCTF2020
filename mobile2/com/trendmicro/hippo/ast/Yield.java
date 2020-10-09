package com.trendmicro.hippo.ast;

public class Yield extends AstNode {
  private AstNode value;
  
  public Yield() {}
  
  public Yield(int paramInt) {
    super(paramInt);
  }
  
  public Yield(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public Yield(int paramInt1, int paramInt2, AstNode paramAstNode) {
    super(paramInt1, paramInt2);
    setValue(paramAstNode);
  }
  
  public AstNode getValue() {
    return this.value;
  }
  
  public void setValue(AstNode paramAstNode) {
    this.value = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public String toSource(int paramInt) {
    String str;
    if (this.value == null) {
      str = "yield";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("yield ");
      stringBuilder.append(this.value.toSource(0));
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      AstNode astNode = this.value;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Yield.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */