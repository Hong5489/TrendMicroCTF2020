package com.trendmicro.hippo.ast;

public class ObjectProperty extends InfixExpression {
  public ObjectProperty() {}
  
  public ObjectProperty(int paramInt) {
    super(paramInt);
  }
  
  public ObjectProperty(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public boolean isGetterMethod() {
    boolean bool;
    if (this.type == 152) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isMethod() {
    return (isGetterMethod() || isSetterMethod() || isNormalMethod());
  }
  
  public boolean isNormalMethod() {
    boolean bool;
    if (this.type == 164) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isSetterMethod() {
    boolean bool;
    if (this.type == 153) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setIsGetterMethod() {
    this.type = 152;
  }
  
  public void setIsNormalMethod() {
    this.type = 164;
  }
  
  public void setIsSetterMethod() {
    this.type = 153;
  }
  
  public void setNodeType(int paramInt) {
    if (paramInt == 104 || paramInt == 152 || paramInt == 153 || paramInt == 164) {
      setType(paramInt);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("invalid node type: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n");
    stringBuilder.append(makeIndent(paramInt + 1));
    if (isGetterMethod()) {
      stringBuilder.append("get ");
    } else if (isSetterMethod()) {
      stringBuilder.append("set ");
    } 
    AstNode astNode = this.left;
    int i = getType();
    boolean bool = false;
    if (i == 104) {
      i = 0;
    } else {
      i = paramInt;
    } 
    stringBuilder.append(astNode.toSource(i));
    if (this.type == 104)
      stringBuilder.append(": "); 
    astNode = this.right;
    if (getType() == 104) {
      paramInt = bool;
    } else {
      paramInt++;
    } 
    stringBuilder.append(astNode.toSource(paramInt));
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ObjectProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */