package com.trendmicro.hippo.ast;

public class ForLoop extends Loop {
  private AstNode condition;
  
  private AstNode increment;
  
  private AstNode initializer;
  
  public ForLoop() {}
  
  public ForLoop(int paramInt) {
    super(paramInt);
  }
  
  public ForLoop(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getCondition() {
    return this.condition;
  }
  
  public AstNode getIncrement() {
    return this.increment;
  }
  
  public AstNode getInitializer() {
    return this.initializer;
  }
  
  public void setCondition(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.condition = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setIncrement(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.increment = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setInitializer(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.initializer = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("for (");
    stringBuilder.append(this.initializer.toSource(0));
    stringBuilder.append("; ");
    stringBuilder.append(this.condition.toSource(0));
    stringBuilder.append("; ");
    stringBuilder.append(this.increment.toSource(0));
    stringBuilder.append(") ");
    if (getInlineComment() != null) {
      stringBuilder.append(getInlineComment().toSource());
      stringBuilder.append("\n");
    } 
    if (this.body.getType() == 130) {
      String str1 = this.body.toSource(paramInt);
      String str2 = str1;
      if (getInlineComment() == null)
        str2 = str1.trim(); 
      stringBuilder.append(str2);
      stringBuilder.append("\n");
    } else {
      if (getInlineComment() == null)
        stringBuilder.append("\n"); 
      stringBuilder.append(this.body.toSource(paramInt + 1));
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.initializer.visit(paramNodeVisitor);
      this.condition.visit(paramNodeVisitor);
      this.increment.visit(paramNodeVisitor);
      this.body.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ForLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */