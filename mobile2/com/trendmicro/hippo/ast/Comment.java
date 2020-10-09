package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Token;

public class Comment extends AstNode {
  private Token.CommentType commentType;
  
  private String value;
  
  public Comment(int paramInt1, int paramInt2, Token.CommentType paramCommentType, String paramString) {
    super(paramInt1, paramInt2);
    this.commentType = paramCommentType;
    this.value = paramString;
  }
  
  public Token.CommentType getCommentType() {
    return this.commentType;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setCommentType(Token.CommentType paramCommentType) {
    this.commentType = paramCommentType;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
    setLength(paramString.length());
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(getLength() + 10);
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append(this.value);
    if (Token.CommentType.BLOCK_COMMENT == getCommentType())
      stringBuilder.append("\n"); 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    paramNodeVisitor.visit(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Comment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */