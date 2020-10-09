package com.trendmicro.hippo.ast;

public class LetNode extends Scope {
  private AstNode body;
  
  private int lp = -1;
  
  private int rp = -1;
  
  private VariableDeclaration variables;
  
  public LetNode() {}
  
  public LetNode(int paramInt) {
    super(paramInt);
  }
  
  public LetNode(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public AstNode getBody() {
    return this.body;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public VariableDeclaration getVariables() {
    return this.variables;
  }
  
  public void setBody(AstNode paramAstNode) {
    this.body = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
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
  
  public void setVariables(VariableDeclaration paramVariableDeclaration) {
    assertNotNull(paramVariableDeclaration);
    this.variables = paramVariableDeclaration;
    paramVariableDeclaration.setParent(this);
  }
  
  public String toSource(int paramInt) {
    String str = makeIndent(paramInt);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append("let (");
    printList(this.variables.getVariables(), stringBuilder);
    stringBuilder.append(") ");
    AstNode astNode = this.body;
    if (astNode != null)
      stringBuilder.append(astNode.toSource(paramInt)); 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.variables.visit(paramNodeVisitor);
      AstNode astNode = this.body;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/LetNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */