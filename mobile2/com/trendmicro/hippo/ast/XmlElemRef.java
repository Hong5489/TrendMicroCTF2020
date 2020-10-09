package com.trendmicro.hippo.ast;

public class XmlElemRef extends XmlRef {
  private AstNode indexExpr;
  
  private int lb = -1;
  
  private int rb = -1;
  
  public XmlElemRef() {}
  
  public XmlElemRef(int paramInt) {
    super(paramInt);
  }
  
  public XmlElemRef(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getExpression() {
    return this.indexExpr;
  }
  
  public int getLb() {
    return this.lb;
  }
  
  public int getRb() {
    return this.rb;
  }
  
  public void setBrackets(int paramInt1, int paramInt2) {
    this.lb = paramInt1;
    this.rb = paramInt2;
  }
  
  public void setExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.indexExpr = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setLb(int paramInt) {
    this.lb = paramInt;
  }
  
  public void setRb(int paramInt) {
    this.rb = paramInt;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    if (isAttributeAccess())
      stringBuilder.append("@"); 
    if (this.namespace != null) {
      stringBuilder.append(this.namespace.toSource(0));
      stringBuilder.append("::");
    } 
    stringBuilder.append("[");
    stringBuilder.append(this.indexExpr.toSource(0));
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      if (this.namespace != null)
        this.namespace.visit(paramNodeVisitor); 
      this.indexExpr.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlElemRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */