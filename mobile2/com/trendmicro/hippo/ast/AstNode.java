package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.Token;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AstNode extends Node implements Comparable<AstNode> {
  private static Map<Integer, String> operatorNames;
  
  protected AstNode inlineComment;
  
  protected int length = 1;
  
  protected AstNode parent;
  
  protected int position = -1;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<>();
    operatorNames = (Map)hashMap;
    hashMap.put(Integer.valueOf(52), "in");
    operatorNames.put(Integer.valueOf(32), "typeof");
    operatorNames.put(Integer.valueOf(53), "instanceof");
    operatorNames.put(Integer.valueOf(31), "delete");
    operatorNames.put(Integer.valueOf(90), ",");
    operatorNames.put(Integer.valueOf(104), ":");
    operatorNames.put(Integer.valueOf(105), "||");
    operatorNames.put(Integer.valueOf(106), "&&");
    operatorNames.put(Integer.valueOf(107), "++");
    operatorNames.put(Integer.valueOf(108), "--");
    operatorNames.put(Integer.valueOf(9), "|");
    operatorNames.put(Integer.valueOf(10), "^");
    operatorNames.put(Integer.valueOf(11), "&");
    operatorNames.put(Integer.valueOf(12), "==");
    operatorNames.put(Integer.valueOf(13), "!=");
    operatorNames.put(Integer.valueOf(14), "<");
    operatorNames.put(Integer.valueOf(16), ">");
    operatorNames.put(Integer.valueOf(15), "<=");
    operatorNames.put(Integer.valueOf(17), ">=");
    operatorNames.put(Integer.valueOf(18), "<<");
    operatorNames.put(Integer.valueOf(19), ">>");
    operatorNames.put(Integer.valueOf(20), ">>>");
    operatorNames.put(Integer.valueOf(21), "+");
    operatorNames.put(Integer.valueOf(22), "-");
    operatorNames.put(Integer.valueOf(23), "*");
    operatorNames.put(Integer.valueOf(24), "/");
    operatorNames.put(Integer.valueOf(25), "%");
    operatorNames.put(Integer.valueOf(26), "!");
    operatorNames.put(Integer.valueOf(27), "~");
    operatorNames.put(Integer.valueOf(28), "+");
    operatorNames.put(Integer.valueOf(29), "-");
    operatorNames.put(Integer.valueOf(46), "===");
    operatorNames.put(Integer.valueOf(47), "!==");
    operatorNames.put(Integer.valueOf(91), "=");
    operatorNames.put(Integer.valueOf(92), "|=");
    operatorNames.put(Integer.valueOf(94), "&=");
    operatorNames.put(Integer.valueOf(95), "<<=");
    operatorNames.put(Integer.valueOf(96), ">>=");
    operatorNames.put(Integer.valueOf(97), ">>>=");
    operatorNames.put(Integer.valueOf(98), "+=");
    operatorNames.put(Integer.valueOf(99), "-=");
    operatorNames.put(Integer.valueOf(100), "*=");
    operatorNames.put(Integer.valueOf(101), "/=");
    operatorNames.put(Integer.valueOf(102), "%=");
    operatorNames.put(Integer.valueOf(93), "^=");
    operatorNames.put(Integer.valueOf(127), "void");
  }
  
  public AstNode() {
    super(-1);
  }
  
  public AstNode(int paramInt) {
    this();
    this.position = paramInt;
  }
  
  public AstNode(int paramInt1, int paramInt2) {
    this();
    this.position = paramInt1;
    this.length = paramInt2;
  }
  
  public static RuntimeException codeBug() throws RuntimeException {
    throw Kit.codeBug();
  }
  
  public static String operatorToString(int paramInt) {
    String str = operatorNames.get(Integer.valueOf(paramInt));
    if (str != null)
      return str; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid operator: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void addChild(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    setLength(paramAstNode.getPosition() + paramAstNode.getLength() - getPosition());
    addChildToBack(paramAstNode);
    paramAstNode.setParent(this);
  }
  
  protected void assertNotNull(Object paramObject) {
    if (paramObject != null)
      return; 
    throw new IllegalArgumentException("arg cannot be null");
  }
  
  public int compareTo(AstNode paramAstNode) {
    if (equals(paramAstNode))
      return 0; 
    int i = getAbsolutePosition();
    int j = paramAstNode.getAbsolutePosition();
    if (i < j)
      return -1; 
    if (j < i)
      return 1; 
    j = getLength();
    i = paramAstNode.getLength();
    return (j < i) ? -1 : ((i < j) ? 1 : (hashCode() - paramAstNode.hashCode()));
  }
  
  public String debugPrint() {
    DebugPrintVisitor debugPrintVisitor = new DebugPrintVisitor(new StringBuilder(1000));
    visit(debugPrintVisitor);
    return debugPrintVisitor.toString();
  }
  
  public int depth() {
    int i;
    AstNode astNode = this.parent;
    if (astNode == null) {
      i = 0;
    } else {
      i = astNode.depth() + 1;
    } 
    return i;
  }
  
  public int getAbsolutePosition() {
    int i = this.position;
    for (AstNode astNode = this.parent; astNode != null; astNode = astNode.getParent())
      i += astNode.getPosition(); 
    return i;
  }
  
  public AstRoot getAstRoot() {
    AstNode astNode;
    for (astNode = this; astNode != null && !(astNode instanceof AstRoot); astNode = astNode.getParent());
    return (AstRoot)astNode;
  }
  
  public FunctionNode getEnclosingFunction() {
    AstNode astNode;
    for (astNode = getParent(); astNode != null && !(astNode instanceof FunctionNode); astNode = astNode.getParent());
    return (FunctionNode)astNode;
  }
  
  public Scope getEnclosingScope() {
    AstNode astNode;
    for (astNode = getParent(); astNode != null && !(astNode instanceof Scope); astNode = astNode.getParent());
    return (Scope)astNode;
  }
  
  public AstNode getInlineComment() {
    return this.inlineComment;
  }
  
  public int getLength() {
    return this.length;
  }
  
  public int getLineno() {
    if (this.lineno != -1)
      return this.lineno; 
    AstNode astNode = this.parent;
    return (astNode != null) ? astNode.getLineno() : -1;
  }
  
  public AstNode getParent() {
    return this.parent;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public boolean hasSideEffects() {
    int i = getType();
    if (i != 30 && i != 31 && i != 37 && i != 38 && i != 50 && i != 51 && i != 56 && i != 57 && i != 82 && i != 83 && i != 107 && i != 108)
      switch (i) {
        default:
          switch (i) {
            default:
              switch (i) {
                default:
                  switch (i) {
                    default:
                      return false;
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 114:
                    case 115:
                      break;
                  } 
                  break;
                case 69:
                case 70:
                case 71:
                  break;
              } 
              break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
              break;
          } 
          break;
        case -1:
        case 35:
        case 65:
        case 73:
        case 91:
        case 92:
        case 93:
        case 94:
        case 95:
        case 96:
        case 97:
        case 98:
        case 99:
        case 100:
        case 101:
        case 102:
        case 118:
        case 119:
        case 120:
        case 121:
        case 122:
        case 123:
        case 124:
        case 125:
        case 126:
        case 130:
        case 131:
        case 132:
        case 133:
        case 135:
        case 136:
        case 140:
        case 141:
        case 142:
        case 143:
        case 154:
        case 155:
        case 159:
        case 160:
          break;
      }  
    return true;
  }
  
  public String makeIndent(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramInt; b++)
      stringBuilder.append("  "); 
    return stringBuilder.toString();
  }
  
  protected <T extends AstNode> void printList(List<T> paramList, StringBuilder paramStringBuilder) {
    int i = paramList.size();
    byte b = 0;
    for (AstNode astNode : paramList) {
      paramStringBuilder.append(astNode.toSource(0));
      if (b < i - 1) {
        paramStringBuilder.append(", ");
      } else if (astNode instanceof EmptyExpression) {
        paramStringBuilder.append(",");
      } 
      b++;
    } 
  }
  
  public void setBounds(int paramInt1, int paramInt2) {
    setPosition(paramInt1);
    setLength(paramInt2 - paramInt1);
  }
  
  public void setInlineComment(AstNode paramAstNode) {
    this.inlineComment = paramAstNode;
  }
  
  public void setLength(int paramInt) {
    this.length = paramInt;
  }
  
  public void setParent(AstNode paramAstNode) {
    AstNode astNode = this.parent;
    if (paramAstNode == astNode)
      return; 
    if (astNode != null)
      setRelative(-astNode.getAbsolutePosition()); 
    this.parent = paramAstNode;
    if (paramAstNode != null)
      setRelative(paramAstNode.getAbsolutePosition()); 
  }
  
  public void setPosition(int paramInt) {
    this.position = paramInt;
  }
  
  public void setRelative(int paramInt) {
    this.position -= paramInt;
  }
  
  public String shortName() {
    String str = getClass().getName();
    return str.substring(str.lastIndexOf(".") + 1);
  }
  
  public String toSource() {
    return toSource(0);
  }
  
  public abstract String toSource(int paramInt);
  
  public abstract void visit(NodeVisitor paramNodeVisitor);
  
  protected static class DebugPrintVisitor implements NodeVisitor {
    private static final int DEBUG_INDENT = 2;
    
    private StringBuilder buffer;
    
    public DebugPrintVisitor(StringBuilder param1StringBuilder) {
      this.buffer = param1StringBuilder;
    }
    
    private String makeIndent(int param1Int) {
      StringBuilder stringBuilder = new StringBuilder(param1Int * 2);
      for (byte b = 0; b < param1Int * 2; b++)
        stringBuilder.append(" "); 
      return stringBuilder.toString();
    }
    
    public String toString() {
      return this.buffer.toString();
    }
    
    public boolean visit(AstNode param1AstNode) {
      int i = param1AstNode.getType();
      String str = Token.typeToName(i);
      StringBuilder stringBuilder2 = this.buffer;
      stringBuilder2.append(param1AstNode.getAbsolutePosition());
      stringBuilder2.append("\t");
      this.buffer.append(makeIndent(param1AstNode.depth()));
      stringBuilder2 = this.buffer;
      stringBuilder2.append(str);
      stringBuilder2.append(" ");
      StringBuilder stringBuilder1 = this.buffer;
      stringBuilder1.append(param1AstNode.getPosition());
      stringBuilder1.append(" ");
      this.buffer.append(param1AstNode.getLength());
      if (i == 39) {
        stringBuilder1 = this.buffer;
        stringBuilder1.append(" ");
        stringBuilder1.append(((Name)param1AstNode).getIdentifier());
      } else if (i == 41) {
        stringBuilder1 = this.buffer;
        stringBuilder1.append(" ");
        stringBuilder1.append(((StringLiteral)param1AstNode).getValue(true));
      } 
      this.buffer.append("\n");
      return true;
    }
  }
  
  public static class PositionComparator implements Comparator<AstNode>, Serializable {
    private static final long serialVersionUID = 1L;
    
    public int compare(AstNode param1AstNode1, AstNode param1AstNode2) {
      return param1AstNode1.position - param1AstNode2.position;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/AstNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */