package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.Token;

public class Symbol {
  private Scope containingTable;
  
  private int declType;
  
  private int index = -1;
  
  private String name;
  
  private Node node;
  
  public Symbol() {}
  
  public Symbol(int paramInt, String paramString) {
    setName(paramString);
    setDeclType(paramInt);
  }
  
  public Scope getContainingTable() {
    return this.containingTable;
  }
  
  public int getDeclType() {
    return this.declType;
  }
  
  public String getDeclTypeName() {
    return Token.typeToName(this.declType);
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Node getNode() {
    return this.node;
  }
  
  public void setContainingTable(Scope paramScope) {
    this.containingTable = paramScope;
  }
  
  public void setDeclType(int paramInt) {
    if (paramInt == 110 || paramInt == 88 || paramInt == 123 || paramInt == 154 || paramInt == 155) {
      this.declType = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid declType: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setIndex(int paramInt) {
    this.index = paramInt;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setNode(Node paramNode) {
    this.node = paramNode;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Symbol (");
    stringBuilder.append(getDeclTypeName());
    stringBuilder.append(") name=");
    stringBuilder.append(this.name);
    if (this.node != null) {
      stringBuilder.append(" line=");
      stringBuilder.append(this.node.getLineno());
    } 
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Symbol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */