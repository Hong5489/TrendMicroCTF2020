package com.trendmicro.hippo.ast;

public class XmlExpression extends XmlFragment {
  private AstNode expression;
  
  private boolean isXmlAttribute;
  
  public XmlExpression() {}
  
  public XmlExpression(int paramInt) {
    super(paramInt);
  }
  
  public XmlExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public XmlExpression(int paramInt, AstNode paramAstNode) {
    super(paramInt);
    setExpression(paramAstNode);
  }
  
  public AstNode getExpression() {
    return this.expression;
  }
  
  public boolean isXmlAttribute() {
    return this.isXmlAttribute;
  }
  
  public void setExpression(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.expression = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setIsXmlAttribute(boolean paramBoolean) {
    this.isXmlAttribute = paramBoolean;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("{");
    stringBuilder.append(this.expression.toSource(paramInt));
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this))
      this.expression.visit(paramNodeVisitor); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */