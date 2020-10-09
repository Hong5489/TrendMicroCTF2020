package com.trendmicro.hippo.ast;

public class IfStatement extends AstNode {
  private AstNode condition;
  
  private AstNode elseKeyWordInlineComment;
  
  private AstNode elsePart;
  
  private int elsePosition = -1;
  
  private int lp = -1;
  
  private int rp = -1;
  
  private AstNode thenPart;
  
  public IfStatement() {}
  
  public IfStatement(int paramInt) {
    super(paramInt);
  }
  
  public IfStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getCondition() {
    return this.condition;
  }
  
  public AstNode getElseKeyWordInlineComment() {
    return this.elseKeyWordInlineComment;
  }
  
  public AstNode getElsePart() {
    return this.elsePart;
  }
  
  public int getElsePosition() {
    return this.elsePosition;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public AstNode getThenPart() {
    return this.thenPart;
  }
  
  public void setCondition(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.condition = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public void setElseKeyWordInlineComment(AstNode paramAstNode) {
    this.elseKeyWordInlineComment = paramAstNode;
  }
  
  public void setElsePart(AstNode paramAstNode) {
    this.elsePart = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setElsePosition(int paramInt) {
    this.elsePosition = paramInt;
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
  
  public void setThenPart(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.thenPart = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    String str = makeIndent(paramInt);
    StringBuilder stringBuilder = new StringBuilder(32);
    stringBuilder.append(str);
    stringBuilder.append("if (");
    stringBuilder.append(this.condition.toSource(0));
    stringBuilder.append(") ");
    if (getInlineComment() != null) {
      stringBuilder.append("    ");
      stringBuilder.append(getInlineComment().toSource());
      stringBuilder.append("\n");
    } 
    if (this.thenPart.getType() != 130) {
      if (getInlineComment() == null)
        stringBuilder.append("\n"); 
      stringBuilder.append(makeIndent(paramInt + 1));
    } 
    stringBuilder.append(this.thenPart.toSource(paramInt).trim());
    if (this.elsePart != null) {
      if (this.thenPart.getType() != 130) {
        stringBuilder.append("\n");
        stringBuilder.append(str);
        stringBuilder.append("else ");
      } else {
        stringBuilder.append(" else ");
      } 
      if (getElseKeyWordInlineComment() != null) {
        stringBuilder.append("    ");
        stringBuilder.append(getElseKeyWordInlineComment().toSource());
        stringBuilder.append("\n");
      } 
      if (this.elsePart.getType() != 130 && this.elsePart.getType() != 113) {
        if (getElseKeyWordInlineComment() == null)
          stringBuilder.append("\n"); 
        stringBuilder.append(makeIndent(paramInt + 1));
      } 
      stringBuilder.append(this.elsePart.toSource(paramInt).trim());
    } 
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.condition.visit(paramNodeVisitor);
      this.thenPart.visit(paramNodeVisitor);
      AstNode astNode = this.elsePart;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/IfStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */