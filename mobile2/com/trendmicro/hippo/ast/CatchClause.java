package com.trendmicro.hippo.ast;

public class CatchClause extends AstNode {
  private Block body;
  
  private AstNode catchCondition;
  
  private int ifPosition = -1;
  
  private int lp = -1;
  
  private int rp = -1;
  
  private Name varName;
  
  public CatchClause() {}
  
  public CatchClause(int paramInt) {
    super(paramInt);
  }
  
  public CatchClause(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public Block getBody() {
    return this.body;
  }
  
  public AstNode getCatchCondition() {
    return this.catchCondition;
  }
  
  public int getIfPosition() {
    return this.ifPosition;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public Name getVarName() {
    return this.varName;
  }
  
  public void setBody(Block paramBlock) {
    assertNotNull(paramBlock);
    this.body = paramBlock;
    paramBlock.setParent(this);
  }
  
  public void setCatchCondition(AstNode paramAstNode) {
    this.catchCondition = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setIfPosition(int paramInt) {
    this.ifPosition = paramInt;
  }
  
  public void setLp(int paramInt) {
    this.lp = paramInt;
  }
  
  public void setParens(int paramInt1, int paramInt2) {
    this.lp = paramInt1;
    this.rp = paramInt2;
  }
  
  public void setRp(int paramInt) {
    this.rp = paramInt;
  }
  
  public void setVarName(Name paramName) {
    assertNotNull(paramName);
    this.varName = paramName;
    paramName.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("catch (");
    stringBuilder.append(this.varName.toSource(0));
    if (this.catchCondition != null) {
      stringBuilder.append(" if ");
      stringBuilder.append(this.catchCondition.toSource(0));
    } 
    stringBuilder.append(") ");
    stringBuilder.append(this.body.toSource(0));
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.varName.visit(paramNodeVisitor);
      AstNode astNode = this.catchCondition;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
      this.body.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/CatchClause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */