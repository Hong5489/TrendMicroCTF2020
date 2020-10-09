package com.trendmicro.hippo.ast;

public class ElementGet extends AstNode {
  private AstNode element;
  
  private int lb = -1;
  
  private int rb = -1;
  
  private AstNode target;
  
  public ElementGet() {}
  
  public ElementGet(int paramInt) {
    super(paramInt);
  }
  
  public ElementGet(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ElementGet(AstNode paramAstNode1, AstNode paramAstNode2) {
    setTarget(paramAstNode1);
    setElement(paramAstNode2);
  }
  
  public AstNode getElement() {
    return this.element;
  }
  
  public int getLb() {
    return this.lb;
  }
  
  public int getRb() {
    return this.rb;
  }
  
  public AstNode getTarget() {
    return this.target;
  }
  
  public void setElement(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.element = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setLb(int paramInt) {
    this.lb = paramInt;
  }
  
  public void setParens(int paramInt1, int paramInt2) {
    this.lb = paramInt1;
    this.rb = paramInt2;
  }
  
  public void setRb(int paramInt) {
    this.rb = paramInt;
  }
  
  public void setTarget(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.target = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.target.toSource(0));
    stringBuilder.append("[");
    stringBuilder.append(this.element.toSource(0));
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.target.visit(paramNodeVisitor);
      this.element.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ElementGet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */