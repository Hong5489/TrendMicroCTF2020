package com.trendmicro.hippo.ast;

public class Label extends Jump {
  private String name;
  
  public Label() {}
  
  public Label(int paramInt) {
    this(paramInt, -1);
  }
  
  public Label(int paramInt1, int paramInt2) {
    this.position = paramInt1;
    this.length = paramInt2;
  }
  
  public Label(int paramInt1, int paramInt2, String paramString) {
    this(paramInt1, paramInt2);
    setName(paramString);
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    if (paramString == null) {
      paramString = null;
    } else {
      paramString = paramString.trim();
    } 
    if (paramString != null && !"".equals(paramString)) {
      this.name = paramString;
      return;
    } 
    throw new IllegalArgumentException("invalid label name");
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.name);
    stringBuilder.append(":\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Label.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */