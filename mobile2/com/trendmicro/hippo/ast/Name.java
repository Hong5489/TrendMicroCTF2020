package com.trendmicro.hippo.ast;

public class Name extends AstNode {
  private String identifier;
  
  private Scope scope;
  
  public Name() {}
  
  public Name(int paramInt) {
    super(paramInt);
  }
  
  public Name(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public Name(int paramInt1, int paramInt2, String paramString) {
    super(paramInt1, paramInt2);
    setIdentifier(paramString);
  }
  
  public Name(int paramInt, String paramString) {
    super(paramInt);
    setIdentifier(paramString);
    setLength(paramString.length());
  }
  
  public Scope getDefiningScope() {
    Scope scope2;
    Scope scope1 = getEnclosingScope();
    String str = getIdentifier();
    if (scope1 == null) {
      str = null;
    } else {
      scope2 = scope1.getDefiningScope(str);
    } 
    return scope2;
  }
  
  public String getIdentifier() {
    return this.identifier;
  }
  
  public Scope getScope() {
    return this.scope;
  }
  
  public boolean isLocalName() {
    boolean bool;
    Scope scope = getDefiningScope();
    if (scope != null && scope.getParentScope() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int length() {
    int i;
    String str = this.identifier;
    if (str == null) {
      i = 0;
    } else {
      i = str.length();
    } 
    return i;
  }
  
  public void setIdentifier(String paramString) {
    assertNotNull(paramString);
    this.identifier = paramString;
    setLength(paramString.length());
  }
  
  public void setScope(Scope paramScope) {
    this.scope = paramScope;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    String str1 = this.identifier;
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


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Name.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */