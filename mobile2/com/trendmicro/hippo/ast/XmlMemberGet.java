package com.trendmicro.hippo.ast;

public class XmlMemberGet extends InfixExpression {
  public XmlMemberGet() {}
  
  public XmlMemberGet(int paramInt) {
    super(paramInt);
  }
  
  public XmlMemberGet(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public XmlMemberGet(int paramInt1, int paramInt2, AstNode paramAstNode, XmlRef paramXmlRef) {
    super(paramInt1, paramInt2, paramAstNode, paramXmlRef);
  }
  
  public XmlMemberGet(AstNode paramAstNode, XmlRef paramXmlRef) {
    super(paramAstNode, paramXmlRef);
  }
  
  public XmlMemberGet(AstNode paramAstNode, XmlRef paramXmlRef, int paramInt) {
    super(144, paramAstNode, paramXmlRef, paramInt);
  }
  
  private String dotsToString() {
    int i = getType();
    if (i != 109) {
      if (i == 144)
        return ".."; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Invalid type of XmlMemberGet: ");
      stringBuilder.append(getType());
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    return ".";
  }
  
  public XmlRef getMemberRef() {
    return (XmlRef)getRight();
  }
  
  public AstNode getTarget() {
    return getLeft();
  }
  
  public void setProperty(XmlRef paramXmlRef) {
    setRight(paramXmlRef);
  }
  
  public void setTarget(AstNode paramAstNode) {
    setLeft(paramAstNode);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(getLeft().toSource(0));
    stringBuilder.append(dotsToString());
    stringBuilder.append(getRight().toSource(0));
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlMemberGet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */