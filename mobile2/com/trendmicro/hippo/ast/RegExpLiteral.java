package com.trendmicro.hippo.ast;

public class RegExpLiteral extends AstNode {
  private String flags;
  
  private String value;
  
  public RegExpLiteral() {}
  
  public RegExpLiteral(int paramInt) {
    super(paramInt);
  }
  
  public RegExpLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public String getFlags() {
    return this.flags;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setFlags(String paramString) {
    this.flags = paramString;
  }
  
  public void setValue(String paramString) {
    assertNotNull(paramString);
    this.value = paramString;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("/");
    stringBuilder.append(this.value);
    stringBuilder.append("/");
    String str1 = this.flags;
    String str2 = str1;
    if (str1 == null)
      str2 = ""; 
    stringBuilder.append(str2);
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/RegExpLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */