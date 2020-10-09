package com.trendmicro.hippo.ast;

public class VariableInitializer extends AstNode {
  private AstNode initializer;
  
  private AstNode target;
  
  public VariableInitializer() {}
  
  public VariableInitializer(int paramInt) {
    super(paramInt);
  }
  
  public VariableInitializer(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getInitializer() {
    return this.initializer;
  }
  
  public AstNode getTarget() {
    return this.target;
  }
  
  public boolean isDestructuring() {
    return this.target instanceof Name ^ true;
  }
  
  public void setInitializer(AstNode paramAstNode) {
    this.initializer = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setNodeType(int paramInt) {
    if (paramInt == 123 || paramInt == 155 || paramInt == 154) {
      setType(paramInt);
      return;
    } 
    throw new IllegalArgumentException("invalid node type");
  }
  
  public void setTarget(AstNode paramAstNode) {
    if (paramAstNode != null) {
      this.target = paramAstNode;
      paramAstNode.setParent(this);
      return;
    } 
    throw new IllegalArgumentException("invalid target arg");
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.target.toSource(0));
    if (this.initializer != null) {
      stringBuilder.append(" = ");
      stringBuilder.append(this.initializer.toSource(0));
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.target.visit(paramNodeVisitor);
      AstNode astNode = this.initializer;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/VariableInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */