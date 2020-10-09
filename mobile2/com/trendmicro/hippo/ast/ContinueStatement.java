package com.trendmicro.hippo.ast;

public class ContinueStatement extends Jump {
  private Name label;
  
  private Loop target;
  
  public ContinueStatement() {}
  
  public ContinueStatement(int paramInt) {
    this(paramInt, -1);
  }
  
  public ContinueStatement(int paramInt1, int paramInt2) {
    this.position = paramInt1;
    this.length = paramInt2;
  }
  
  public ContinueStatement(int paramInt1, int paramInt2, Name paramName) {
    this(paramInt1, paramInt2);
    setLabel(paramName);
  }
  
  public ContinueStatement(int paramInt, Name paramName) {
    this(paramInt);
    setLabel(paramName);
  }
  
  public ContinueStatement(Name paramName) {
    setLabel(paramName);
  }
  
  public Name getLabel() {
    return this.label;
  }
  
  public Loop getTarget() {
    return this.target;
  }
  
  public void setLabel(Name paramName) {
    this.label = paramName;
    if (paramName != null)
      paramName.setParent(this); 
  }
  
  public void setTarget(Loop paramLoop) {
    assertNotNull(paramLoop);
    this.target = paramLoop;
    setJumpStatement(paramLoop);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("continue");
    if (this.label != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.label.toSource(0));
    } 
    stringBuilder.append(";\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Name name = this.label;
      if (name != null)
        name.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ContinueStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */