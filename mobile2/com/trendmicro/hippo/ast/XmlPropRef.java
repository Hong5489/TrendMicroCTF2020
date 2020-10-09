package com.trendmicro.hippo.ast;

public class XmlPropRef extends XmlRef {
  private Name propName;
  
  public XmlPropRef() {}
  
  public XmlPropRef(int paramInt) {
    super(paramInt);
  }
  
  public XmlPropRef(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public Name getPropName() {
    return this.propName;
  }
  
  public void setPropName(Name paramName) {
    assertNotNull(paramName);
    this.propName = paramName;
    paramName.setParent(this);
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
    stringBuilder.append(this.propName.toSource(0));
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      if (this.namespace != null)
        this.namespace.visit(paramNodeVisitor); 
      this.propName.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlPropRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */