package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.ScriptRuntime;

public class StringLiteral extends AstNode {
  private char quoteChar;
  
  private String value;
  
  public StringLiteral() {}
  
  public StringLiteral(int paramInt) {
    super(paramInt);
  }
  
  public StringLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public char getQuoteCharacter() {
    return this.quoteChar;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public String getValue(boolean paramBoolean) {
    if (!paramBoolean)
      return this.value; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.quoteChar);
    stringBuilder.append(this.value);
    stringBuilder.append(this.quoteChar);
    return stringBuilder.toString();
  }
  
  public void setQuoteCharacter(char paramChar) {
    this.quoteChar = (char)paramChar;
  }
  
  public void setValue(String paramString) {
    assertNotNull(paramString);
    this.value = paramString;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(makeIndent(paramInt));
    stringBuilder.append(this.quoteChar);
    stringBuilder.append(ScriptRuntime.escapeString(this.value, this.quoteChar));
    stringBuilder.append(this.quoteChar);
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/StringLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */