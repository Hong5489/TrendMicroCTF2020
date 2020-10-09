package com.trendmicro.hippo.ast;

public class WhileLoop extends Loop {
  private AstNode condition;
  
  public WhileLoop() {}
  
  public WhileLoop(int paramInt) {
    super(paramInt);
  }
  
  public WhileLoop(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getCondition() {
    return this.condition;
  }
  
  public void setCondition(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.condition = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("while (");
    stringBuilder.append(this.condition.toSource(0));
    stringBuilder.append(") ");
    if (getInlineComment() != null) {
      stringBuilder.append(getInlineComment().toSource(paramInt + 1));
      stringBuilder.append("\n");
    } 
    if (this.body.getType() == 130) {
      stringBuilder.append(this.body.toSource(paramInt).trim());
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
      this.condition.visit(paramNodeVisitor);
      this.body.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/WhileLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */