package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Token;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SwitchCase extends AstNode {
  private AstNode expression;
  
  private List<AstNode> statements;
  
  public SwitchCase() {}
  
  public SwitchCase(int paramInt) {
    super(paramInt);
  }
  
  public SwitchCase(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addStatement(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    if (this.statements == null)
      this.statements = new ArrayList<>(); 
    setLength(paramAstNode.getPosition() + paramAstNode.getLength() - getPosition());
    this.statements.add(paramAstNode);
    paramAstNode.setParent(this);
  }
  
  public AstNode getExpression() {
    return this.expression;
  }
  
  public List<AstNode> getStatements() {
    return this.statements;
  }
  
  public boolean isDefault() {
    boolean bool;
    if (this.expression == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setExpression(AstNode paramAstNode) {
    this.expression = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setStatements(List<AstNode> paramList) {
    List<AstNode> list = this.statements;
    if (list != null)
      list.clear(); 
    Iterator<AstNode> iterator = paramList.iterator();
    while (iterator.hasNext())
      addStatement(iterator.next()); 
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    if (this.expression == null) {
      stringBuilder.append("default:\n");
    } else {
      stringBuilder.append("case ");
      stringBuilder.append(this.expression.toSource(0));
      stringBuilder.append(":");
      if (getInlineComment() != null)
        stringBuilder.append(getInlineComment().toSource(paramInt + 1)); 
      stringBuilder.append("\n");
    } 
    List<AstNode> list = this.statements;
    if (list != null)
      for (AstNode astNode : list) {
        stringBuilder.append(astNode.toSource(paramInt + 1));
        if (astNode.getType() == 162 && ((Comment)astNode).getCommentType() == Token.CommentType.LINE)
          stringBuilder.append("\n"); 
      }  
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      AstNode astNode = this.expression;
      if (astNode != null)
        astNode.visit(paramNodeVisitor); 
      List<AstNode> list = this.statements;
      if (list != null) {
        Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext())
          ((AstNode)iterator.next()).visit(paramNodeVisitor); 
      } 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/SwitchCase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */