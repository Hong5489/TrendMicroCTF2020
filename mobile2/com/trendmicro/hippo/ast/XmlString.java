package com.trendmicro.hippo.ast;

public class XmlString extends XmlFragment {
  private String xml;
  
  public XmlString() {}
  
  public XmlString(int paramInt) {
    super(paramInt);
  }
  
  public XmlString(int paramInt, String paramString) {
    super(paramInt);
    setXml(paramString);
  }
  
  public String getXml() {
    return this.xml;
  }
  
  public void setXml(String paramString) {
    assertNotNull(paramString);
    this.xml = paramString;
    setLength(paramString.length());
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.xml);
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */