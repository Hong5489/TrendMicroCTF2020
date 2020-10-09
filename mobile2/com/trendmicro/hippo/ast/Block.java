package com.trendmicro.hippo.ast;

import java.util.Iterator;

public class Block extends AstNode {
  public Block() {}
  
  public Block(int paramInt) {
    super(paramInt);
  }
  
  public Block(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addStatement(AstNode paramAstNode) {
    addChild(paramAstNode);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("{\n");
    for (AstNode astNode : this) {
      stringBuilder.append(astNode.toSource(paramInt + 1));
      if (astNode.getType() == 162)
        stringBuilder.append("\n"); 
    } 
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("}");
    if (getInlineComment() != null)
      stringBuilder.append(getInlineComment().toSource(paramInt)); 
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<AstNode> iterator = iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */