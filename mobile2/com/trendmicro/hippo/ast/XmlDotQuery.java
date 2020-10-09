package com.trendmicro.hippo.ast;

public class XmlDotQuery extends InfixExpression {
  private int rp = -1;
  
  public XmlDotQuery() {}
  
  public XmlDotQuery(int paramInt) {
    super(paramInt);
  }
  
  public XmlDotQuery(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public void setRp(int paramInt) {
    this.rp = paramInt;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(getLeft().toSource(0));
    stringBuilder.append(".(");
    stringBuilder.append(getRight().toSource(0));
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlDotQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */