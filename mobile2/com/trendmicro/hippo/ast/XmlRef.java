package com.trendmicro.hippo.ast;

public abstract class XmlRef extends AstNode {
  protected int atPos = -1;
  
  protected int colonPos = -1;
  
  protected Name namespace;
  
  public XmlRef() {}
  
  public XmlRef(int paramInt) {
    super(paramInt);
  }
  
  public XmlRef(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public int getAtPos() {
    return this.atPos;
  }
  
  public int getColonPos() {
    return this.colonPos;
  }
  
  public Name getNamespace() {
    return this.namespace;
  }
  
  public boolean isAttributeAccess() {
    boolean bool;
    if (this.atPos >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setAtPos(int paramInt) {
    this.atPos = paramInt;
  }
  
  public void setColonPos(int paramInt) {
    this.colonPos = paramInt;
  }
  
  public void setNamespace(Name paramName) {
    this.namespace = paramName;
    if (paramName != null)
      paramName.setParent(this); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */