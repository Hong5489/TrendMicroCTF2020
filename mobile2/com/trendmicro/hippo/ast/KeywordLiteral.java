package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;

public class KeywordLiteral extends AstNode {
  public KeywordLiteral() {}
  
  public KeywordLiteral(int paramInt) {
    super(paramInt);
  }
  
  public KeywordLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public KeywordLiteral(int paramInt1, int paramInt2, int paramInt3) {
    super(paramInt1, paramInt2);
    setType(paramInt3);
  }
  
  public boolean isBooleanLiteral() {
    return (this.type == 45 || this.type == 44);
  }
  
  public KeywordLiteral setType(int paramInt) {
    if (paramInt == 43 || paramInt == 42 || paramInt == 45 || paramInt == 44 || paramInt == 161) {
      this.type = paramInt;
      return this;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid node type: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    paramInt = getType();
    if (paramInt != 161) {
      switch (paramInt) {
        default:
          stringBuilder = new StringBuilder();
          stringBuilder.append("Invalid keyword literal type: ");
          stringBuilder.append(getType());
          throw new IllegalStateException(stringBuilder.toString());
        case 45:
          stringBuilder.append("true");
          return stringBuilder.toString();
        case 44:
          stringBuilder.append("false");
          return stringBuilder.toString();
        case 43:
          stringBuilder.append("this");
          return stringBuilder.toString();
        case 42:
          break;
      } 
      stringBuilder.append("null");
    } else {
      stringBuilder.append("debugger;\n");
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/KeywordLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */