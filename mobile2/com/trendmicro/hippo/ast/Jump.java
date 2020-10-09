package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;

public class Jump extends AstNode {
  private Jump jumpNode;
  
  public Node target;
  
  private Node target2;
  
  public Jump() {}
  
  public Jump(int paramInt) {}
  
  public Jump(int paramInt1, int paramInt2) {
    this(paramInt1);
    setLineno(paramInt2);
  }
  
  public Jump(int paramInt, Node paramNode) {
    this(paramInt);
    addChildToBack(paramNode);
  }
  
  public Jump(int paramInt1, Node paramNode, int paramInt2) {
    this(paramInt1, paramNode);
    setLineno(paramInt2);
  }
  
  public Node getContinue() {
    if (this.type != 133)
      codeBug(); 
    return this.target2;
  }
  
  public Node getDefault() {
    if (this.type != 115)
      codeBug(); 
    return this.target2;
  }
  
  public Node getFinally() {
    if (this.type != 82)
      codeBug(); 
    return this.target2;
  }
  
  public Jump getJumpStatement() {
    if (this.type != 121 && this.type != 122)
      codeBug(); 
    return this.jumpNode;
  }
  
  public Jump getLoop() {
    if (this.type != 131)
      codeBug(); 
    return this.jumpNode;
  }
  
  public void setContinue(Node paramNode) {
    if (this.type != 133)
      codeBug(); 
    if (paramNode.getType() != 132)
      codeBug(); 
    if (this.target2 != null)
      codeBug(); 
    this.target2 = paramNode;
  }
  
  public void setDefault(Node paramNode) {
    if (this.type != 115)
      codeBug(); 
    if (paramNode.getType() != 132)
      codeBug(); 
    if (this.target2 != null)
      codeBug(); 
    this.target2 = paramNode;
  }
  
  public void setFinally(Node paramNode) {
    if (this.type != 82)
      codeBug(); 
    if (paramNode.getType() != 132)
      codeBug(); 
    if (this.target2 != null)
      codeBug(); 
    this.target2 = paramNode;
  }
  
  public void setJumpStatement(Jump paramJump) {
    if (this.type != 121 && this.type != 122)
      codeBug(); 
    if (paramJump == null)
      codeBug(); 
    if (this.jumpNode != null)
      codeBug(); 
    this.jumpNode = paramJump;
  }
  
  public void setLoop(Jump paramJump) {
    if (this.type != 131)
      codeBug(); 
    if (paramJump == null)
      codeBug(); 
    if (this.jumpNode != null)
      codeBug(); 
    this.jumpNode = paramJump;
  }
  
  public String toSource(int paramInt) {
    throw new UnsupportedOperationException(toString());
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    throw new UnsupportedOperationException(toString());
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Jump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */