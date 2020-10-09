package com.trendmicro.hippo.ast;

import java.util.Iterator;

public class NewExpression extends FunctionCall {
  private ObjectLiteral initializer;
  
  public NewExpression() {}
  
  public NewExpression(int paramInt) {
    super(paramInt);
  }
  
  public NewExpression(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public ObjectLiteral getInitializer() {
    return this.initializer;
  }
  
  public void setInitializer(ObjectLiteral paramObjectLiteral) {
    this.initializer = paramObjectLiteral;
    if (paramObjectLiteral != null)
      paramObjectLiteral.setParent(this); 
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("new ");
    stringBuilder.append(this.target.toSource(0));
    stringBuilder.append("(");
    if (this.arguments != null)
      printList(this.arguments, stringBuilder); 
    stringBuilder.append(")");
    if (this.initializer != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.initializer.toSource(0));
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      this.target.visit(paramNodeVisitor);
      Iterator<AstNode> iterator = getArguments().iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
      ObjectLiteral objectLiteral = this.initializer;
      if (objectLiteral != null)
        objectLiteral.visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/NewExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */