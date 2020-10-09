package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LabeledStatement extends AstNode {
  private List<Label> labels = new ArrayList<>();
  
  private AstNode statement;
  
  public LabeledStatement() {}
  
  public LabeledStatement(int paramInt) {
    super(paramInt);
  }
  
  public LabeledStatement(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addLabel(Label paramLabel) {
    assertNotNull(paramLabel);
    this.labels.add(paramLabel);
    paramLabel.setParent(this);
  }
  
  public Label getFirstLabel() {
    return this.labels.get(0);
  }
  
  public Label getLabelByName(String paramString) {
    for (Label label : this.labels) {
      if (paramString.equals(label.getName()))
        return label; 
    } 
    return null;
  }
  
  public List<Label> getLabels() {
    return this.labels;
  }
  
  public AstNode getStatement() {
    return this.statement;
  }
  
  public boolean hasSideEffects() {
    return true;
  }
  
  public void setLabels(List<Label> paramList) {
    assertNotNull(paramList);
    List<Label> list = this.labels;
    if (list != null)
      list.clear(); 
    Iterator<Label> iterator = paramList.iterator();
    while (iterator.hasNext())
      addLabel(iterator.next()); 
  }
  
  public void setStatement(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.statement = paramAstNode;
    paramAstNode.setParent(this);
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    Iterator<Label> iterator = this.labels.iterator();
    while (iterator.hasNext())
      stringBuilder.append(((Label)iterator.next()).toSource(paramInt)); 
    stringBuilder.append(this.statement.toSource(paramInt + 1));
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<Label> iterator = this.labels.iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
      this.statement.visit(paramNodeVisitor);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/LabeledStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */