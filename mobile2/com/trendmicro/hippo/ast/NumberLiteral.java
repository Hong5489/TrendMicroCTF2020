package com.trendmicro.hippo.ast;

public class NumberLiteral extends AstNode {
  private double number;
  
  private String value;
  
  public NumberLiteral() {}
  
  public NumberLiteral(double paramDouble) {
    setDouble(paramDouble);
    setValue(Double.toString(paramDouble));
  }
  
  public NumberLiteral(int paramInt) {
    super(paramInt);
  }
  
  public NumberLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public NumberLiteral(int paramInt, String paramString) {
    super(paramInt);
    setValue(paramString);
    setLength(paramString.length());
  }
  
  public NumberLiteral(int paramInt, String paramString, double paramDouble) {
    this(paramInt, paramString);
    setDouble(paramDouble);
  }
  
  public double getNumber() {
    return this.number;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setNumber(double paramDouble) {
    this.number = paramDouble;
  }
  
  public void setValue(String paramString) {
    assertNotNull(paramString);
    this.value = paramString;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    String str1 = this.value;
    String str2 = str1;
    if (str1 == null)
      str2 = "<null>"; 
    stringBuilder.append(str2);
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/NumberLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */