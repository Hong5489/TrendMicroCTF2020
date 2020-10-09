package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.Comment;
import com.trendmicro.hippo.ast.Jump;
import com.trendmicro.hippo.ast.Name;
import com.trendmicro.hippo.ast.NumberLiteral;
import com.trendmicro.hippo.ast.Scope;
import com.trendmicro.hippo.ast.ScriptNode;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Node implements Iterable<Node> {
  public static final int ARROW_FUNCTION_PROP = 27;
  
  public static final int ATTRIBUTE_FLAG = 2;
  
  public static final int BOTH = 0;
  
  public static final int CASEARRAY_PROP = 5;
  
  public static final int CATCH_SCOPE_PROP = 14;
  
  public static final int CONTROL_BLOCK_PROP = 18;
  
  public static final int DECR_FLAG = 1;
  
  public static final int DESCENDANTS_FLAG = 4;
  
  public static final int DESTRUCTURING_ARRAY_LENGTH = 21;
  
  public static final int DESTRUCTURING_NAMES = 22;
  
  public static final int DESTRUCTURING_PARAMS = 23;
  
  public static final int DESTRUCTURING_SHORTHAND = 26;
  
  public static final int DIRECTCALL_PROP = 9;
  
  public static final int END_DROPS_OFF = 1;
  
  public static final int END_RETURNS = 2;
  
  public static final int END_RETURNS_VALUE = 4;
  
  public static final int END_UNREACHED = 0;
  
  public static final int END_YIELDS = 8;
  
  public static final int EXPRESSION_CLOSURE_PROP = 25;
  
  public static final int FUNCTION_PROP = 1;
  
  public static final int GENERATOR_END_PROP = 20;
  
  public static final int INCRDECR_PROP = 13;
  
  public static final int ISNUMBER_PROP = 8;
  
  public static final int JSDOC_PROP = 24;
  
  public static final int LABEL_ID_PROP = 15;
  
  public static final int LAST_PROP = 27;
  
  public static final int LEFT = 1;
  
  public static final int LOCAL_BLOCK_PROP = 3;
  
  public static final int LOCAL_PROP = 2;
  
  public static final int MEMBER_TYPE_PROP = 16;
  
  public static final int NAME_PROP = 17;
  
  public static final int NON_SPECIALCALL = 0;
  
  private static final Node NOT_SET = new Node(-1);
  
  public static final int OBJECT_IDS_PROP = 12;
  
  public static final int PARENTHESIZED_PROP = 19;
  
  public static final int POST_FLAG = 2;
  
  public static final int PROPERTY_FLAG = 1;
  
  public static final int REGEXP_PROP = 4;
  
  public static final int RIGHT = 2;
  
  public static final int SKIP_INDEXES_PROP = 11;
  
  public static final int SPECIALCALL_EVAL = 1;
  
  public static final int SPECIALCALL_PROP = 10;
  
  public static final int SPECIALCALL_WITH = 2;
  
  public static final int TARGETBLOCK_PROP = 6;
  
  public static final int VARIABLE_PROP = 7;
  
  protected Node first;
  
  protected Node last;
  
  protected int lineno = -1;
  
  protected Node next;
  
  protected PropListItem propListHead;
  
  protected int type = -1;
  
  public Node(int paramInt) {
    this.type = paramInt;
  }
  
  public Node(int paramInt1, int paramInt2) {
    this.type = paramInt1;
    this.lineno = paramInt2;
  }
  
  public Node(int paramInt, Node paramNode) {
    this.type = paramInt;
    this.last = paramNode;
    this.first = paramNode;
    paramNode.next = null;
  }
  
  public Node(int paramInt1, Node paramNode, int paramInt2) {
    this(paramInt1, paramNode);
    this.lineno = paramInt2;
  }
  
  public Node(int paramInt, Node paramNode1, Node paramNode2) {
    this.type = paramInt;
    this.first = paramNode1;
    this.last = paramNode2;
    paramNode1.next = paramNode2;
    paramNode2.next = null;
  }
  
  public Node(int paramInt1, Node paramNode1, Node paramNode2, int paramInt2) {
    this(paramInt1, paramNode1, paramNode2);
    this.lineno = paramInt2;
  }
  
  public Node(int paramInt, Node paramNode1, Node paramNode2, Node paramNode3) {
    this.type = paramInt;
    this.first = paramNode1;
    this.last = paramNode3;
    paramNode1.next = paramNode2;
    paramNode2.next = paramNode3;
    paramNode3.next = null;
  }
  
  public Node(int paramInt1, Node paramNode1, Node paramNode2, Node paramNode3, int paramInt2) {
    this(paramInt1, paramNode1, paramNode2, paramNode3);
    this.lineno = paramInt2;
  }
  
  private static void appendPrintId(Node paramNode, ObjToIntMap paramObjToIntMap, StringBuilder paramStringBuilder) {}
  
  private int endCheck() {
    int i = this.type;
    if (i != 4) {
      if (i != 50)
        if (i != 73) {
          if (i != 130 && i != 142) {
            if (i != 121) {
              if (i != 122) {
                switch (i) {
                  default:
                    return 1;
                  case 134:
                    node = this.first;
                    return (node != null) ? node.endCheck() : 1;
                  case 133:
                    return endCheckLoop();
                  case 132:
                    break;
                } 
                Node node = this.next;
                return (node != null) ? node.endCheck() : 1;
              } 
            } else {
              return endCheckBreak();
            } 
          } else {
            Node node = this.first;
            if (node == null)
              return 1; 
            i = node.type;
            return (i != 7) ? ((i != 82) ? ((i != 115) ? ((i != 131) ? endCheckBlock() : node.endCheckLabel()) : node.endCheckSwitch()) : node.endCheckTry()) : node.endCheckIf();
          } 
        } else {
          return 8;
        }  
      return 0;
    } 
    return (this.first != null) ? 4 : 2;
  }
  
  private int endCheckBlock() {
    int i = 1;
    for (Node node = this.first; (i & 0x1) != 0 && node != null; node = node.next)
      i = i & 0xFFFFFFFE | node.endCheck(); 
    return i;
  }
  
  private int endCheckBreak() {
    ((Jump)this).getJumpStatement().putIntProp(18, 1);
    return 0;
  }
  
  private int endCheckIf() {
    Node node1 = this.next;
    Node node2 = ((Jump)this).target;
    int i = node1.endCheck();
    if (node2 != null) {
      i |= node2.endCheck();
    } else {
      i |= 0x1;
    } 
    return i;
  }
  
  private int endCheckLabel() {
    return this.next.endCheck() | getIntProp(18, 0);
  }
  
  private int endCheckLoop() {
    Node node;
    for (node = this.first; node.next != this.last; node = node.next);
    if (node.type != 6)
      return 1; 
    int i = ((Jump)node).target.next.endCheck();
    int j = i;
    if (node.first.type == 45)
      j = i & 0xFFFFFFFE; 
    return j | getIntProp(18, 0);
  }
  
  private int endCheckSwitch() {
    return 0;
  }
  
  private int endCheckTry() {
    return 0;
  }
  
  private PropListItem ensureProperty(int paramInt) {
    PropListItem propListItem1 = lookupProperty(paramInt);
    PropListItem propListItem2 = propListItem1;
    if (propListItem1 == null) {
      propListItem2 = new PropListItem();
      propListItem2.type = paramInt;
      propListItem2.next = this.propListHead;
      this.propListHead = propListItem2;
    } 
    return propListItem2;
  }
  
  private static void generatePrintIds(Node paramNode, ObjToIntMap paramObjToIntMap) {}
  
  private PropListItem lookupProperty(int paramInt) {
    PropListItem propListItem;
    for (propListItem = this.propListHead; propListItem != null && paramInt != propListItem.type; propListItem = propListItem.next);
    return propListItem;
  }
  
  public static Node newNumber(double paramDouble) {
    NumberLiteral numberLiteral = new NumberLiteral();
    numberLiteral.setNumber(paramDouble);
    return (Node)numberLiteral;
  }
  
  public static Node newString(int paramInt, String paramString) {
    Name name = new Name();
    name.setIdentifier(paramString);
    name.setType(paramInt);
    return (Node)name;
  }
  
  public static Node newString(String paramString) {
    return newString(41, paramString);
  }
  
  public static Node newTarget() {
    return new Node(132);
  }
  
  private static final String propToString(int paramInt) {
    return null;
  }
  
  private void resetTargets_r() {
    int i = this.type;
    if (i == 132 || i == 73)
      labelId(-1); 
    for (Node node = this.first; node != null; node = node.next)
      node.resetTargets_r(); 
  }
  
  private void toString(ObjToIntMap paramObjToIntMap, StringBuilder paramStringBuilder) {}
  
  private static void toStringTreeHelper(ScriptNode paramScriptNode, Node paramNode, ObjToIntMap paramObjToIntMap, int paramInt, StringBuilder paramStringBuilder) {}
  
  public void addChildAfter(Node paramNode1, Node paramNode2) {
    if (paramNode1.next == null) {
      paramNode1.next = paramNode2.next;
      paramNode2.next = paramNode1;
      if (this.last == paramNode2)
        this.last = paramNode1; 
      return;
    } 
    throw new RuntimeException("newChild had siblings in addChildAfter");
  }
  
  public void addChildBefore(Node paramNode1, Node paramNode2) {
    if (paramNode1.next == null) {
      Node node = this.first;
      if (node == paramNode2) {
        paramNode1.next = node;
        this.first = paramNode1;
        return;
      } 
      addChildAfter(paramNode1, getChildBefore(paramNode2));
      return;
    } 
    throw new RuntimeException("newChild had siblings in addChildBefore");
  }
  
  public void addChildToBack(Node paramNode) {
    paramNode.next = null;
    Node node = this.last;
    if (node == null) {
      this.last = paramNode;
      this.first = paramNode;
      return;
    } 
    node.next = paramNode;
    this.last = paramNode;
  }
  
  public void addChildToFront(Node paramNode) {
    paramNode.next = this.first;
    this.first = paramNode;
    if (this.last == null)
      this.last = paramNode; 
  }
  
  public void addChildrenToBack(Node paramNode) {
    Node node = this.last;
    if (node != null)
      node.next = paramNode; 
    this.last = paramNode.getLastSibling();
    if (this.first == null)
      this.first = paramNode; 
  }
  
  public void addChildrenToFront(Node paramNode) {
    Node node = paramNode.getLastSibling();
    node.next = this.first;
    this.first = paramNode;
    if (this.last == null)
      this.last = node; 
  }
  
  public Node getChildBefore(Node paramNode) {
    if (paramNode == this.first)
      return null; 
    Node node = this.first;
    while (node.next != paramNode) {
      node = node.next;
      if (node != null)
        continue; 
      throw new RuntimeException("node is not a child");
    } 
    return node;
  }
  
  public final double getDouble() {
    return ((NumberLiteral)this).getNumber();
  }
  
  public int getExistingIntProp(int paramInt) {
    PropListItem propListItem = lookupProperty(paramInt);
    if (propListItem == null)
      Kit.codeBug(); 
    return propListItem.intValue;
  }
  
  public Node getFirstChild() {
    return this.first;
  }
  
  public int getIntProp(int paramInt1, int paramInt2) {
    PropListItem propListItem = lookupProperty(paramInt1);
    return (propListItem == null) ? paramInt2 : propListItem.intValue;
  }
  
  public String getJsDoc() {
    Comment comment = getJsDocNode();
    return (comment != null) ? comment.getValue() : null;
  }
  
  public Comment getJsDocNode() {
    return (Comment)getProp(24);
  }
  
  public Node getLastChild() {
    return this.last;
  }
  
  public Node getLastSibling() {
    Node node;
    for (node = this; node.next != null; node = node.next);
    return node;
  }
  
  public int getLineno() {
    return this.lineno;
  }
  
  public Node getNext() {
    return this.next;
  }
  
  public Object getProp(int paramInt) {
    PropListItem propListItem = lookupProperty(paramInt);
    return (propListItem == null) ? null : propListItem.objectValue;
  }
  
  public Scope getScope() {
    return ((Name)this).getScope();
  }
  
  public final String getString() {
    return ((Name)this).getIdentifier();
  }
  
  public int getType() {
    return this.type;
  }
  
  public boolean hasChildren() {
    boolean bool;
    if (this.first != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean hasConsistentReturnUsage() {
    int i = endCheck();
    return ((i & 0x4) == 0 || (i & 0xB) == 0);
  }
  
  public boolean hasSideEffects() {
    // Byte code:
    //   0: aload_0
    //   1: getfield type : I
    //   4: istore_1
    //   5: iconst_1
    //   6: istore_2
    //   7: iconst_1
    //   8: istore_3
    //   9: iload_1
    //   10: bipush #30
    //   12: if_icmpeq -> 689
    //   15: iload_1
    //   16: bipush #31
    //   18: if_icmpeq -> 689
    //   21: iload_1
    //   22: bipush #37
    //   24: if_icmpeq -> 689
    //   27: iload_1
    //   28: bipush #38
    //   30: if_icmpeq -> 689
    //   33: iload_1
    //   34: bipush #50
    //   36: if_icmpeq -> 689
    //   39: iload_1
    //   40: bipush #51
    //   42: if_icmpeq -> 689
    //   45: iload_1
    //   46: bipush #56
    //   48: if_icmpeq -> 689
    //   51: iload_1
    //   52: bipush #57
    //   54: if_icmpeq -> 689
    //   57: iload_1
    //   58: bipush #82
    //   60: if_icmpeq -> 689
    //   63: iload_1
    //   64: bipush #83
    //   66: if_icmpeq -> 689
    //   69: iload_1
    //   70: lookupswitch default -> 416, -1 -> 689, 35 -> 689, 65 -> 689, 73 -> 689, 90 -> 670, 91 -> 689, 92 -> 689, 93 -> 689, 94 -> 689, 95 -> 689, 96 -> 689, 97 -> 689, 98 -> 689, 99 -> 689, 100 -> 689, 101 -> 689, 102 -> 689, 103 -> 599, 118 -> 689, 119 -> 689, 120 -> 689, 121 -> 689, 122 -> 689, 123 -> 689, 124 -> 689, 125 -> 689, 126 -> 689, 130 -> 689, 131 -> 689, 132 -> 689, 133 -> 689, 134 -> 670, 135 -> 689, 136 -> 689, 140 -> 689, 141 -> 689, 142 -> 689, 143 -> 689, 154 -> 689, 155 -> 689, 159 -> 689, 160 -> 689
    //   416: iload_1
    //   417: tableswitch default -> 460, 2 -> 689, 3 -> 689, 4 -> 689, 5 -> 689, 6 -> 689, 7 -> 689, 8 -> 689
    //   460: iload_1
    //   461: tableswitch default -> 488, 69 -> 689, 70 -> 689, 71 -> 689
    //   488: iload_1
    //   489: tableswitch default -> 520, 105 -> 550, 106 -> 550, 107 -> 689, 108 -> 689
    //   520: iload_1
    //   521: tableswitch default -> 548, 113 -> 689, 114 -> 689, 115 -> 689
    //   548: iconst_0
    //   549: ireturn
    //   550: aload_0
    //   551: getfield first : Lcom/trendmicro/hippo/Node;
    //   554: ifnull -> 564
    //   557: aload_0
    //   558: getfield last : Lcom/trendmicro/hippo/Node;
    //   561: ifnonnull -> 568
    //   564: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   567: pop
    //   568: iload_3
    //   569: istore_2
    //   570: aload_0
    //   571: getfield first : Lcom/trendmicro/hippo/Node;
    //   574: invokevirtual hasSideEffects : ()Z
    //   577: ifne -> 597
    //   580: aload_0
    //   581: getfield last : Lcom/trendmicro/hippo/Node;
    //   584: invokevirtual hasSideEffects : ()Z
    //   587: ifeq -> 595
    //   590: iload_3
    //   591: istore_2
    //   592: goto -> 597
    //   595: iconst_0
    //   596: istore_2
    //   597: iload_2
    //   598: ireturn
    //   599: aload_0
    //   600: getfield first : Lcom/trendmicro/hippo/Node;
    //   603: astore #4
    //   605: aload #4
    //   607: ifnull -> 630
    //   610: aload #4
    //   612: getfield next : Lcom/trendmicro/hippo/Node;
    //   615: astore #4
    //   617: aload #4
    //   619: ifnull -> 630
    //   622: aload #4
    //   624: getfield next : Lcom/trendmicro/hippo/Node;
    //   627: ifnonnull -> 634
    //   630: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   633: pop
    //   634: aload_0
    //   635: getfield first : Lcom/trendmicro/hippo/Node;
    //   638: getfield next : Lcom/trendmicro/hippo/Node;
    //   641: invokevirtual hasSideEffects : ()Z
    //   644: ifeq -> 666
    //   647: aload_0
    //   648: getfield first : Lcom/trendmicro/hippo/Node;
    //   651: getfield next : Lcom/trendmicro/hippo/Node;
    //   654: getfield next : Lcom/trendmicro/hippo/Node;
    //   657: invokevirtual hasSideEffects : ()Z
    //   660: ifeq -> 666
    //   663: goto -> 668
    //   666: iconst_0
    //   667: istore_2
    //   668: iload_2
    //   669: ireturn
    //   670: aload_0
    //   671: getfield last : Lcom/trendmicro/hippo/Node;
    //   674: astore #4
    //   676: aload #4
    //   678: ifnull -> 687
    //   681: aload #4
    //   683: invokevirtual hasSideEffects : ()Z
    //   686: ireturn
    //   687: iconst_1
    //   688: ireturn
    //   689: iconst_1
    //   690: ireturn
  }
  
  public Iterator<Node> iterator() {
    return new NodeIterator();
  }
  
  public final int labelId() {
    int i = this.type;
    if (i != 132 && i != 73)
      Kit.codeBug(); 
    return getIntProp(15, -1);
  }
  
  public void labelId(int paramInt) {
    int i = this.type;
    if (i != 132 && i != 73)
      Kit.codeBug(); 
    putIntProp(15, paramInt);
  }
  
  public void putIntProp(int paramInt1, int paramInt2) {
    (ensureProperty(paramInt1)).intValue = paramInt2;
  }
  
  public void putProp(int paramInt, Object paramObject) {
    if (paramObject == null) {
      removeProp(paramInt);
    } else {
      (ensureProperty(paramInt)).objectValue = paramObject;
    } 
  }
  
  public void removeChild(Node paramNode) {
    Node node = getChildBefore(paramNode);
    if (node == null) {
      this.first = this.first.next;
    } else {
      node.next = paramNode.next;
    } 
    if (paramNode == this.last)
      this.last = node; 
    paramNode.next = null;
  }
  
  public void removeChildren() {
    this.last = null;
    this.first = null;
  }
  
  public void removeProp(int paramInt) {
    PropListItem propListItem = this.propListHead;
    if (propListItem != null) {
      PropListItem propListItem1 = null;
      while (propListItem.type != paramInt) {
        propListItem1 = propListItem;
        PropListItem propListItem2 = propListItem.next;
        propListItem = propListItem2;
        if (propListItem2 == null)
          return; 
      } 
      if (propListItem1 == null) {
        this.propListHead = propListItem.next;
      } else {
        propListItem1.next = propListItem.next;
      } 
    } 
  }
  
  public void replaceChild(Node paramNode1, Node paramNode2) {
    paramNode2.next = paramNode1.next;
    if (paramNode1 == this.first) {
      this.first = paramNode2;
    } else {
      (getChildBefore(paramNode1)).next = paramNode2;
    } 
    if (paramNode1 == this.last)
      this.last = paramNode2; 
    paramNode1.next = null;
  }
  
  public void replaceChildAfter(Node paramNode1, Node paramNode2) {
    Node node = paramNode1.next;
    paramNode2.next = node.next;
    paramNode1.next = paramNode2;
    if (node == this.last)
      this.last = paramNode2; 
    node.next = null;
  }
  
  public void resetTargets() {
    if (this.type == 126) {
      resetTargets_r();
    } else {
      Kit.codeBug();
    } 
  }
  
  public final void setDouble(double paramDouble) {
    ((NumberLiteral)this).setNumber(paramDouble);
  }
  
  public void setJsDocNode(Comment paramComment) {
    putProp(24, paramComment);
  }
  
  public void setLineno(int paramInt) {
    this.lineno = paramInt;
  }
  
  public void setScope(Scope paramScope) {
    if (paramScope == null)
      Kit.codeBug(); 
    if (this instanceof Name) {
      ((Name)this).setScope(paramScope);
      return;
    } 
    throw Kit.codeBug();
  }
  
  public final void setString(String paramString) {
    if (paramString == null)
      Kit.codeBug(); 
    ((Name)this).setIdentifier(paramString);
  }
  
  public Node setType(int paramInt) {
    this.type = paramInt;
    return this;
  }
  
  public String toString() {
    return String.valueOf(this.type);
  }
  
  public String toStringTree(ScriptNode paramScriptNode) {
    return null;
  }
  
  public class NodeIterator implements Iterator<Node> {
    private Node cursor = Node.this.first;
    
    private Node prev = Node.NOT_SET;
    
    private Node prev2;
    
    private boolean removed = false;
    
    public boolean hasNext() {
      boolean bool;
      if (this.cursor != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Node next() {
      Node node = this.cursor;
      if (node != null) {
        this.removed = false;
        this.prev2 = this.prev;
        this.prev = node;
        this.cursor = node.next;
        return this.prev;
      } 
      throw new NoSuchElementException();
    }
    
    public void remove() {
      if (this.prev != Node.NOT_SET) {
        if (!this.removed) {
          if (this.prev == Node.this.first) {
            Node.this.first = this.prev.next;
          } else if (this.prev == Node.this.last) {
            this.prev2.next = null;
            Node.this.last = this.prev2;
          } else {
            this.prev2.next = this.cursor;
          } 
          return;
        } 
        throw new IllegalStateException("remove() already called for current element");
      } 
      throw new IllegalStateException("next() has not been called");
    }
  }
  
  private static class PropListItem {
    int intValue;
    
    PropListItem next;
    
    Object objectValue;
    
    int type;
    
    private PropListItem() {}
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */