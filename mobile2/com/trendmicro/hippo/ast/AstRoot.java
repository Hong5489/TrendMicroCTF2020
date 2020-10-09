package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class AstRoot extends ScriptNode {
  private SortedSet<Comment> comments;
  
  public AstRoot() {}
  
  public AstRoot(int paramInt) {
    super(paramInt);
  }
  
  public void addComment(Comment paramComment) {
    assertNotNull(paramComment);
    if (this.comments == null)
      this.comments = new TreeSet<>(new AstNode.PositionComparator()); 
    this.comments.add(paramComment);
    paramComment.setParent(this);
  }
  
  public void checkParentLinks() {
    visit(new NodeVisitor() {
          public boolean visit(AstNode param1AstNode) {
            if (param1AstNode.getType() == 137)
              return true; 
            if (param1AstNode.getParent() != null)
              return true; 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No parent for node: ");
            stringBuilder.append(param1AstNode);
            stringBuilder.append("\n");
            stringBuilder.append(param1AstNode.toSource(0));
            throw new IllegalStateException(stringBuilder.toString());
          }
        });
  }
  
  public String debugPrint() {
    AstNode.DebugPrintVisitor debugPrintVisitor = new AstNode.DebugPrintVisitor(new StringBuilder(1000));
    visitAll(debugPrintVisitor);
    return debugPrintVisitor.toString();
  }
  
  public SortedSet<Comment> getComments() {
    return this.comments;
  }
  
  public void setComments(SortedSet<Comment> paramSortedSet) {
    if (paramSortedSet == null) {
      this.comments = null;
    } else {
      SortedSet<Comment> sortedSet = this.comments;
      if (sortedSet != null)
        sortedSet.clear(); 
      Iterator<Comment> iterator = paramSortedSet.iterator();
      while (iterator.hasNext())
        addComment(iterator.next()); 
    } 
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Node node : this) {
      stringBuilder.append(((AstNode)node).toSource(paramInt));
      if (node.getType() == 162)
        stringBuilder.append("\n"); 
    } 
    return stringBuilder.toString();
  }
  
  public void visitAll(NodeVisitor paramNodeVisitor) {
    visit(paramNodeVisitor);
    visitComments(paramNodeVisitor);
  }
  
  public void visitComments(NodeVisitor paramNodeVisitor) {
    SortedSet<Comment> sortedSet = this.comments;
    if (sortedSet != null) {
      Iterator<Comment> iterator = sortedSet.iterator();
      while (iterator.hasNext())
        paramNodeVisitor.visit(iterator.next()); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/AstRoot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */