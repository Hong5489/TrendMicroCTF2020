package com.trendmicro.hippo.ast;

public class BreakStatement extends Jump {
  private Name breakLabel;
  
  private AstNode target;
  
  public BreakStatement() {}
  
  public BreakStatement(int paramInt) {
    this.position = paramInt;
  }
  
  public BreakStatement(int paramInt1, int paramInt2) {
    this.position = paramInt1;
    this.length = paramInt2;
  }
  
  public Name getBreakLabel() {
    return this.breakLabel;
  }
  
  public AstNode getBreakTarget() {
    return this.target;
  }
  
  public void setBreakLabel(Name paramName) {
    this.breakLabel = paramName;
    if (paramName != null)
      paramName.setParent(this); 
  }
  
  public void setBreakTarget(Jump paramJump) {
    assertNotNull(paramJump);
    this.target = paramJump;
    setJumpStatement(paramJump);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("break");
    if (this.breakLabel != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.breakLabel.toSource(0));
    } 
    stringBuilder.append(";\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Name name = this.breakLabel;
      if (name != null)
        name.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/BreakStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */