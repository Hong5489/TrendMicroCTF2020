package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.Token;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VariableDeclaration extends AstNode {
  private boolean isStatement;
  
  private List<VariableInitializer> variables = new ArrayList<>();
  
  public VariableDeclaration() {}
  
  public VariableDeclaration(int paramInt) {
    super(paramInt);
  }
  
  public VariableDeclaration(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  private String declTypeName() {
    return Token.typeToName(this.type).toLowerCase();
  }
  
  public void addVariable(VariableInitializer paramVariableInitializer) {
    assertNotNull(paramVariableInitializer);
    this.variables.add(paramVariableInitializer);
    paramVariableInitializer.setParent(this);
  }
  
  public List<VariableInitializer> getVariables() {
    return this.variables;
  }
  
  public boolean isConst() {
    boolean bool;
    if (this.type == 155) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isLet() {
    boolean bool;
    if (this.type == 154) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isStatement() {
    return this.isStatement;
  }
  
  public boolean isVar() {
    boolean bool;
    if (this.type == 123) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void setIsStatement(boolean paramBoolean) {
    this.isStatement = paramBoolean;
  }
  
  public Node setType(int paramInt) {
    if (paramInt == 123 || paramInt == 155 || paramInt == 154)
      return super.setType(paramInt); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("invalid decl type: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setVariables(List<VariableInitializer> paramList) {
    assertNotNull(paramList);
    this.variables.clear();
    Iterator<VariableInitializer> iterator = paramList.iterator();
    while (iterator.hasNext())
      addVariable(iterator.next()); 
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(declTypeName());
    stringBuilder.append(" ");
    printList(this.variables, stringBuilder);
    if (isStatement())
      stringBuilder.append(";"); 
    if (getInlineComment() != null) {
      stringBuilder.append(getInlineComment().toSource(paramInt));
      stringBuilder.append("\n");
    } else if (isStatement()) {
      stringBuilder.append("\n");
    } 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<VariableInitializer> iterator = this.variables.iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/VariableDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */