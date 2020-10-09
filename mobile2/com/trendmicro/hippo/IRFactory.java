package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.ArrayComprehension;
import com.trendmicro.hippo.ast.ArrayComprehensionLoop;
import com.trendmicro.hippo.ast.ArrayLiteral;
import com.trendmicro.hippo.ast.Assignment;
import com.trendmicro.hippo.ast.AstNode;
import com.trendmicro.hippo.ast.AstRoot;
import com.trendmicro.hippo.ast.Block;
import com.trendmicro.hippo.ast.BreakStatement;
import com.trendmicro.hippo.ast.CatchClause;
import com.trendmicro.hippo.ast.ConditionalExpression;
import com.trendmicro.hippo.ast.ContinueStatement;
import com.trendmicro.hippo.ast.DestructuringForm;
import com.trendmicro.hippo.ast.DoLoop;
import com.trendmicro.hippo.ast.ElementGet;
import com.trendmicro.hippo.ast.EmptyExpression;
import com.trendmicro.hippo.ast.ExpressionStatement;
import com.trendmicro.hippo.ast.ForInLoop;
import com.trendmicro.hippo.ast.ForLoop;
import com.trendmicro.hippo.ast.FunctionCall;
import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.GeneratorExpression;
import com.trendmicro.hippo.ast.IfStatement;
import com.trendmicro.hippo.ast.InfixExpression;
import com.trendmicro.hippo.ast.Jump;
import com.trendmicro.hippo.ast.Label;
import com.trendmicro.hippo.ast.LabeledStatement;
import com.trendmicro.hippo.ast.LetNode;
import com.trendmicro.hippo.ast.Name;
import com.trendmicro.hippo.ast.NewExpression;
import com.trendmicro.hippo.ast.NumberLiteral;
import com.trendmicro.hippo.ast.ObjectLiteral;
import com.trendmicro.hippo.ast.ObjectProperty;
import com.trendmicro.hippo.ast.ParenthesizedExpression;
import com.trendmicro.hippo.ast.PropertyGet;
import com.trendmicro.hippo.ast.RegExpLiteral;
import com.trendmicro.hippo.ast.ReturnStatement;
import com.trendmicro.hippo.ast.Scope;
import com.trendmicro.hippo.ast.ScriptNode;
import com.trendmicro.hippo.ast.StringLiteral;
import com.trendmicro.hippo.ast.SwitchCase;
import com.trendmicro.hippo.ast.SwitchStatement;
import com.trendmicro.hippo.ast.Symbol;
import com.trendmicro.hippo.ast.ThrowStatement;
import com.trendmicro.hippo.ast.TryStatement;
import com.trendmicro.hippo.ast.UnaryExpression;
import com.trendmicro.hippo.ast.VariableDeclaration;
import com.trendmicro.hippo.ast.VariableInitializer;
import com.trendmicro.hippo.ast.WhileLoop;
import com.trendmicro.hippo.ast.WithStatement;
import com.trendmicro.hippo.ast.XmlElemRef;
import com.trendmicro.hippo.ast.XmlExpression;
import com.trendmicro.hippo.ast.XmlFragment;
import com.trendmicro.hippo.ast.XmlLiteral;
import com.trendmicro.hippo.ast.XmlMemberGet;
import com.trendmicro.hippo.ast.XmlPropRef;
import com.trendmicro.hippo.ast.XmlRef;
import com.trendmicro.hippo.ast.XmlString;
import com.trendmicro.hippo.ast.Yield;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class IRFactory extends Parser {
  private static final int ALWAYS_FALSE_BOOLEAN = -1;
  
  private static final int ALWAYS_TRUE_BOOLEAN = 1;
  
  private static final int LOOP_DO_WHILE = 0;
  
  private static final int LOOP_FOR = 2;
  
  private static final int LOOP_WHILE = 1;
  
  private Decompiler decompiler = new Decompiler();
  
  public IRFactory() {}
  
  public IRFactory(CompilerEnvirons paramCompilerEnvirons) {
    this(paramCompilerEnvirons, paramCompilerEnvirons.getErrorReporter());
  }
  
  public IRFactory(CompilerEnvirons paramCompilerEnvirons, ErrorReporter paramErrorReporter) {
    super(paramCompilerEnvirons, paramErrorReporter);
  }
  
  private void addSwitchCase(Node paramNode1, Node paramNode2, Node paramNode3) {
    if (paramNode1.getType() == 130) {
      Jump jump = (Jump)paramNode1.getFirstChild();
      if (jump.getType() == 115) {
        Node node = Node.newTarget();
        if (paramNode2 != null) {
          Jump jump1 = new Jump(116, paramNode2);
          jump1.target = node;
          jump.addChildToBack((Node)jump1);
        } else {
          jump.setDefault(node);
        } 
        paramNode1.addChildToBack(node);
        paramNode1.addChildToBack(paramNode3);
        return;
      } 
      throw Kit.codeBug();
    } 
    throw Kit.codeBug();
  }
  
  private Node arrayCompTransformHelper(ArrayComprehension paramArrayComprehension, String paramString) {
    this.decompiler.addToken(84);
    int i = paramArrayComprehension.getLineno();
    Node node2 = transform(paramArrayComprehension.getResult());
    List<ArrayComprehensionLoop> list = paramArrayComprehension.getLoops();
    int j = list.size();
    Node[] arrayOfNode1 = new Node[j];
    Node[] arrayOfNode2 = new Node[j];
    byte b;
    for (b = 0; b < j; b++) {
      String str;
      ArrayComprehensionLoop arrayComprehensionLoop = list.get(b);
      this.decompiler.addName(" ");
      this.decompiler.addToken(120);
      if (arrayComprehensionLoop.isForEach())
        this.decompiler.addName("each "); 
      this.decompiler.addToken(88);
      AstNode astNode = arrayComprehensionLoop.getIterator();
      if (astNode.getType() == 39) {
        str = astNode.getString();
        this.decompiler.addName(str);
      } else {
        decompile(astNode);
        str = this.currentScriptOrFn.getNextTempName();
        defineSymbol(88, str, false);
        node2 = createBinary(90, createAssignment(91, (Node)astNode, createName(str)), node2);
      } 
      Node node = createName(str);
      defineSymbol(154, str, false);
      arrayOfNode1[b] = node;
      if (arrayComprehensionLoop.isForOf()) {
        this.decompiler.addName("of ");
      } else {
        this.decompiler.addToken(52);
      } 
      arrayOfNode2[b] = transform(arrayComprehensionLoop.getIteratedObject());
      this.decompiler.addToken(89);
    } 
    Node node1 = createName(paramString);
    ArrayComprehension arrayComprehension = null;
    Node node3 = createCallOrNew(38, createPropertyGet(node1, (String)null, "push", 0));
    Node node4 = new Node(134, node3, i);
    node1 = node4;
    if (paramArrayComprehension.getFilter() != null) {
      this.decompiler.addName(" ");
      this.decompiler.addToken(113);
      this.decompiler.addToken(88);
      node1 = createIf(transform(paramArrayComprehension.getFilter()), node4, (Node)null, i);
      this.decompiler.addToken(89);
    } 
    b = 0;
    j--;
    node4 = node1;
    paramArrayComprehension = arrayComprehension;
    node1 = node3;
    while (j >= 0) {
      try {
        ArrayComprehensionLoop arrayComprehensionLoop = list.get(j);
        Scope scope = createLoopNode((Node)paramArrayComprehension, arrayComprehensionLoop.getLineno());
        pushScope(scope);
        b++;
        Node node = arrayOfNode1[j];
        node3 = arrayOfNode2[j];
        try {
          boolean bool1 = arrayComprehensionLoop.isForEach();
          boolean bool2 = arrayComprehensionLoop.isForOf();
          try {
            node4 = createForIn(154, (Node)scope, node, node3, node4, bool1, bool2);
            j--;
          } finally {}
        } finally {}
      } finally {
        paramArrayComprehension = null;
      } 
    } 
    for (j = 0; j < b; j++)
      popScope(); 
    this.decompiler.addToken(85);
    node1.addChildToBack(node2);
    return node4;
  }
  
  private void closeSwitch(Node paramNode) {
    if (paramNode.getType() == 130) {
      Jump jump = (Jump)paramNode.getFirstChild();
      if (jump.getType() == 115) {
        Node node1 = Node.newTarget();
        jump.target = node1;
        Node node2 = jump.getDefault();
        Node node3 = node2;
        if (node2 == null)
          node3 = node1; 
        paramNode.addChildAfter((Node)makeJump(5, node3), (Node)jump);
        paramNode.addChildToBack(node1);
        return;
      } 
      throw Kit.codeBug();
    } 
    throw Kit.codeBug();
  }
  
  private Node createAssignment(int paramInt, Node paramNode1, Node paramNode2) {
    // Byte code:
    //   0: aload_0
    //   1: aload_2
    //   2: invokespecial makeReference : (Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   5: astore #4
    //   7: aload #4
    //   9: ifnonnull -> 63
    //   12: aload_2
    //   13: invokevirtual getType : ()I
    //   16: bipush #66
    //   18: if_icmpeq -> 41
    //   21: aload_2
    //   22: invokevirtual getType : ()I
    //   25: bipush #67
    //   27: if_icmpne -> 33
    //   30: goto -> 41
    //   33: aload_0
    //   34: ldc 'msg.bad.assign.left'
    //   36: invokevirtual reportError : (Ljava/lang/String;)V
    //   39: aload_3
    //   40: areturn
    //   41: iload_1
    //   42: bipush #91
    //   44: if_icmpeq -> 55
    //   47: aload_0
    //   48: ldc 'msg.bad.destruct.op'
    //   50: invokevirtual reportError : (Ljava/lang/String;)V
    //   53: aload_3
    //   54: areturn
    //   55: aload_0
    //   56: iconst_m1
    //   57: aload_2
    //   58: aload_3
    //   59: invokevirtual createDestructuringAssignment : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   62: areturn
    //   63: iload_1
    //   64: tableswitch default -> 128, 91 -> 374, 92 -> 192, 93 -> 186, 94 -> 180, 95 -> 174, 96 -> 168, 97 -> 162, 98 -> 156, 99 -> 150, 100 -> 144, 101 -> 138, 102 -> 132
    //   128: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   131: athrow
    //   132: bipush #25
    //   134: istore_1
    //   135: goto -> 195
    //   138: bipush #24
    //   140: istore_1
    //   141: goto -> 195
    //   144: bipush #23
    //   146: istore_1
    //   147: goto -> 195
    //   150: bipush #22
    //   152: istore_1
    //   153: goto -> 195
    //   156: bipush #21
    //   158: istore_1
    //   159: goto -> 195
    //   162: bipush #20
    //   164: istore_1
    //   165: goto -> 195
    //   168: bipush #19
    //   170: istore_1
    //   171: goto -> 195
    //   174: bipush #18
    //   176: istore_1
    //   177: goto -> 195
    //   180: bipush #11
    //   182: istore_1
    //   183: goto -> 195
    //   186: bipush #10
    //   188: istore_1
    //   189: goto -> 195
    //   192: bipush #9
    //   194: istore_1
    //   195: aload #4
    //   197: invokevirtual getType : ()I
    //   200: istore #5
    //   202: iload #5
    //   204: bipush #33
    //   206: if_icmpeq -> 309
    //   209: iload #5
    //   211: bipush #36
    //   213: if_icmpeq -> 309
    //   216: iload #5
    //   218: bipush #39
    //   220: if_icmpeq -> 276
    //   223: iload #5
    //   225: bipush #68
    //   227: if_icmpne -> 272
    //   230: aload #4
    //   232: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   235: astore_2
    //   236: aload_0
    //   237: aload_2
    //   238: invokevirtual checkMutableReference : (Lcom/trendmicro/hippo/Node;)V
    //   241: new com/trendmicro/hippo/Node
    //   244: dup
    //   245: sipush #143
    //   248: aload_2
    //   249: new com/trendmicro/hippo/Node
    //   252: dup
    //   253: iload_1
    //   254: new com/trendmicro/hippo/Node
    //   257: dup
    //   258: sipush #139
    //   261: invokespecial <init> : (I)V
    //   264: aload_3
    //   265: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   268: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   271: areturn
    //   272: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   275: athrow
    //   276: new com/trendmicro/hippo/Node
    //   279: dup
    //   280: iload_1
    //   281: aload #4
    //   283: aload_3
    //   284: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   287: astore_2
    //   288: new com/trendmicro/hippo/Node
    //   291: dup
    //   292: bipush #8
    //   294: bipush #49
    //   296: aload #4
    //   298: invokevirtual getString : ()Ljava/lang/String;
    //   301: invokestatic newString : (ILjava/lang/String;)Lcom/trendmicro/hippo/Node;
    //   304: aload_2
    //   305: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   308: areturn
    //   309: aload #4
    //   311: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   314: astore_2
    //   315: aload #4
    //   317: invokevirtual getLastChild : ()Lcom/trendmicro/hippo/Node;
    //   320: astore #4
    //   322: iload #5
    //   324: bipush #33
    //   326: if_icmpne -> 337
    //   329: sipush #140
    //   332: istore #5
    //   334: goto -> 342
    //   337: sipush #141
    //   340: istore #5
    //   342: new com/trendmicro/hippo/Node
    //   345: dup
    //   346: iload #5
    //   348: aload_2
    //   349: aload #4
    //   351: new com/trendmicro/hippo/Node
    //   354: dup
    //   355: iload_1
    //   356: new com/trendmicro/hippo/Node
    //   359: dup
    //   360: sipush #139
    //   363: invokespecial <init> : (I)V
    //   366: aload_3
    //   367: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   370: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   373: areturn
    //   374: aload_0
    //   375: aload #4
    //   377: aload_3
    //   378: invokevirtual simpleAssignment : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   381: areturn
  }
  
  private Node createBinary(int paramInt, Node paramNode1, Node paramNode2) {
    String str;
    if (paramInt != 105) {
      if (paramInt != 106) {
        switch (paramInt) {
          default:
            return new Node(paramInt, paramNode1, paramNode2);
          case 24:
            if (paramNode2.type == 40) {
              double d = paramNode2.getDouble();
              if (paramNode1.type == 40) {
                paramNode1.setDouble(paramNode1.getDouble() / d);
                return paramNode1;
              } 
              if (d == 1.0D)
                return new Node(28, paramNode1); 
            } 
          case 23:
            if (paramNode1.type == 40) {
              double d = paramNode1.getDouble();
              if (paramNode2.type == 40) {
                paramNode1.setDouble(paramNode2.getDouble() * d);
                return paramNode1;
              } 
              if (d == 1.0D)
                return new Node(28, paramNode2); 
            } else if (paramNode2.type == 40 && paramNode2.getDouble() == 1.0D) {
              return new Node(28, paramNode1);
            } 
          case 22:
            if (paramNode1.type == 40) {
              double d = paramNode1.getDouble();
              if (paramNode2.type == 40) {
                paramNode1.setDouble(d - paramNode2.getDouble());
                return paramNode1;
              } 
              if (d == 0.0D)
                return new Node(29, paramNode2); 
            } else if (paramNode2.type == 40 && paramNode2.getDouble() == 0.0D) {
              return new Node(28, paramNode1);
            } 
          case 21:
            break;
        } 
        if (paramNode1.type == 41) {
          if (paramNode2.type == 41) {
            str = paramNode2.getString();
          } else if (((Node)str).type == 40) {
            str = ScriptRuntime.numberToString(str.getDouble(), 10);
            paramNode1.setString(paramNode1.getString().concat(str));
            return paramNode1;
          } 
          paramNode1.setString(paramNode1.getString().concat(str));
          return paramNode1;
        } 
        if (paramNode1.type == 40) {
          if (((Node)str).type == 40) {
            paramNode1.setDouble(paramNode1.getDouble() + str.getDouble());
            return paramNode1;
          } 
          if (((Node)str).type == 41) {
            str.setString(ScriptRuntime.numberToString(paramNode1.getDouble(), 10).concat(str.getString()));
            return (Node)str;
          } 
        } 
      } 
      int j = isAlwaysDefinedBoolean(paramNode1);
      if (j == -1)
        return paramNode1; 
      if (j == 1)
        return (Node)str; 
    } 
    int i = isAlwaysDefinedBoolean(paramNode1);
    if (i == 1)
      return paramNode1; 
    if (i == -1)
      return (Node)str; 
  }
  
  private Node createCallOrNew(int paramInt, Node paramNode) {
    boolean bool = false;
    byte b = 0;
    if (paramNode.getType() == 39) {
      String str = paramNode.getString();
      if (str.equals("eval")) {
        b = 1;
      } else if (str.equals("With")) {
        b = 2;
      } 
    } else if (paramNode.getType() == 33) {
      b = bool;
      if (paramNode.getLastChild().getString().equals("eval"))
        b = 1; 
    } 
    paramNode = new Node(paramInt, paramNode);
    if (b != 0) {
      setRequiresActivation();
      paramNode.putIntProp(10, b);
    } 
    return paramNode;
  }
  
  private Node createCatch(String paramString, Node paramNode1, Node paramNode2, int paramInt) {
    Node node = paramNode1;
    if (paramNode1 == null)
      node = new Node(129); 
    return new Node(125, createName(paramString), node, paramNode2, paramInt);
  }
  
  private Node createCondExpr(Node paramNode1, Node paramNode2, Node paramNode3) {
    int i = isAlwaysDefinedBoolean(paramNode1);
    return (i == 1) ? paramNode2 : ((i == -1) ? paramNode3 : new Node(103, paramNode1, paramNode2, paramNode3));
  }
  
  private Node createElementGet(Node paramNode1, String paramString, Node paramNode2, int paramInt) {
    if (paramString == null && paramInt == 0) {
      if (paramNode1 != null)
        return new Node(36, paramNode1, paramNode2); 
      throw Kit.codeBug();
    } 
    return createMemberRefGet(paramNode1, paramString, paramNode2, paramInt);
  }
  
  private Node createExprStatementNoReturn(Node paramNode, int paramInt) {
    return new Node(134, paramNode, paramInt);
  }
  
  private Node createFor(Scope paramScope, Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4) {
    if (paramNode1.getType() == 154) {
      Scope scope = Scope.splitScope(paramScope);
      scope.setType(154);
      scope.addChildrenToBack(paramNode1);
      scope.addChildToBack(createLoop((Jump)paramScope, 2, paramNode4, paramNode2, new Node(129), paramNode3));
      return (Node)scope;
    } 
    return createLoop((Jump)paramScope, 2, paramNode4, paramNode2, paramNode1, paramNode3);
  }
  
  private Node createForIn(int paramInt, Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4, boolean paramBoolean1, boolean paramBoolean2) {
    Node node1;
    boolean bool;
    int i = -1;
    int j = 0;
    int k = paramNode2.getType();
    if (k == 123 || k == 154) {
      node1 = paramNode2.getLastChild();
      bool = node1.getType();
      if (bool == 66 || bool == 67) {
        i = bool;
        k = bool;
        bool = false;
        if (node1 instanceof ArrayLiteral)
          bool = ((ArrayLiteral)node1).getDestructuringLength(); 
      } else if (bool == 39) {
        node1 = Node.newString(39, node1.getString());
        bool = j;
      } else {
        reportError("msg.bad.for.in.lhs");
        return null;
      } 
      j = i;
      i = k;
      k = j;
    } else if (k == 66 || k == 67) {
      if (paramNode2 instanceof ArrayLiteral) {
        j = ((ArrayLiteral)paramNode2).getDestructuringLength();
        bool = k;
        i = k;
        node1 = paramNode2;
        k = bool;
        bool = j;
      } else {
        bool = k;
        j = 0;
        i = k;
        node1 = paramNode2;
        k = bool;
        bool = j;
      } 
    } else {
      node1 = makeReference(paramNode2);
      if (node1 == null) {
        reportError("msg.bad.for.in.lhs");
        return null;
      } 
      j = -1;
      bool = false;
      i = k;
      k = j;
    } 
    Node node2 = new Node(142);
    if (paramBoolean1) {
      j = 59;
    } else if (paramBoolean2) {
      j = 61;
    } else if (k != -1) {
      j = 60;
    } else {
      j = 58;
    } 
    Node node3 = new Node(j, paramNode3);
    node3.putProp(3, node2);
    Node node4 = new Node(62);
    node4.putProp(3, node2);
    paramNode3 = new Node(63);
    paramNode3.putProp(3, node2);
    Node node5 = new Node(130);
    if (k != -1) {
      paramNode3 = createDestructuringAssignment(paramInt, node1, paramNode3);
      if (!paramBoolean1 && !paramBoolean2 && (k == 67 || bool != 2))
        reportError("msg.bad.for.in.destruct"); 
    } else {
      paramNode3 = simpleAssignment(node1, paramNode3);
    } 
    node5.addChildToBack(new Node(134, paramNode3));
    node5.addChildToBack(paramNode4);
    paramNode1 = createLoop((Jump)paramNode1, 1, node5, node4, (Node)null, (Node)null);
    paramNode1.addChildToFront(node3);
    if (i == 123 || i == 154)
      paramNode1.addChildToFront(paramNode2); 
    node2.addChildToBack(paramNode1);
    return node2;
  }
  
  private Node createIf(Node paramNode1, Node paramNode2, Node paramNode3, int paramInt) {
    int i = isAlwaysDefinedBoolean(paramNode1);
    if (i == 1)
      return paramNode2; 
    if (i == -1)
      return (paramNode3 != null) ? paramNode3 : new Node(130, paramInt); 
    Node node1 = new Node(130, paramInt);
    Node node2 = Node.newTarget();
    Jump jump = new Jump(7, paramNode1);
    jump.target = node2;
    node1.addChildToBack((Node)jump);
    node1.addChildrenToBack(paramNode2);
    if (paramNode3 != null) {
      Node node = Node.newTarget();
      node1.addChildToBack((Node)makeJump(5, node));
      node1.addChildToBack(node2);
      node1.addChildrenToBack(paramNode3);
      node1.addChildToBack(node);
    } else {
      node1.addChildToBack(node2);
    } 
    return node1;
  }
  
  private Node createIncDec(int paramInt, boolean paramBoolean, Node paramNode) {
    paramNode = makeReference(paramNode);
    int i = paramNode.getType();
    if (i == 33 || i == 36 || i == 39 || i == 68) {
      paramNode = new Node(paramInt, paramNode);
      i = 0;
      if (paramInt == 108)
        i = false | true; 
      paramInt = i;
      if (paramBoolean)
        paramInt = i | 0x2; 
      paramNode.putIntProp(13, paramInt);
      return paramNode;
    } 
    throw Kit.codeBug();
  }
  
  private Node createLoop(Jump paramJump, int paramInt, Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4) {
    Node node2 = Node.newTarget();
    Node node3 = Node.newTarget();
    if (paramInt == 2 && paramNode2.getType() == 129)
      paramNode2 = new Node(45); 
    Jump jump = new Jump(6, paramNode2);
    jump.target = node2;
    Node node4 = Node.newTarget();
    paramJump.addChildToBack(node2);
    paramJump.addChildrenToBack(paramNode1);
    if (paramInt == 1 || paramInt == 2)
      paramJump.addChildrenToBack(new Node(129, paramJump.getLineno())); 
    paramJump.addChildToBack(node3);
    paramJump.addChildToBack((Node)jump);
    paramJump.addChildToBack(node4);
    paramJump.target = node4;
    Node node1 = node3;
    if (paramInt == 1 || paramInt == 2) {
      paramJump.addChildToFront((Node)makeJump(5, node3));
      if (paramInt == 2) {
        paramInt = paramNode3.getType();
        if (paramInt != 129) {
          if (paramInt != 123 && paramInt != 154)
            paramNode3 = new Node(134, paramNode3); 
          paramJump.addChildToFront(paramNode3);
        } 
        node1 = Node.newTarget();
        paramJump.addChildAfter(node1, paramNode1);
        if (paramNode4.getType() != 129)
          paramJump.addChildAfter(new Node(134, paramNode4), node1); 
        paramNode1 = node1;
        paramJump.setContinue(paramNode1);
        return (Node)paramJump;
      } 
    } 
    paramNode1 = node1;
    paramJump.setContinue(paramNode1);
    return (Node)paramJump;
  }
  
  private Scope createLoopNode(Node paramNode, int paramInt) {
    Scope scope = createScopeNode(133, paramInt);
    if (paramNode != null)
      ((Jump)paramNode).setLoop((Jump)scope); 
    return scope;
  }
  
  private Node createMemberRefGet(Node paramNode1, String paramString, Node paramNode2, int paramInt) {
    Node node = null;
    if (paramString != null)
      if (paramString.equals("*")) {
        node = new Node(42);
      } else {
        node = createName(paramString);
      }  
    if (paramNode1 == null) {
      if (paramString == null) {
        paramNode1 = new Node(80, paramNode2);
      } else {
        paramNode1 = new Node(81, node, paramNode2);
      } 
    } else if (paramString == null) {
      paramNode1 = new Node(78, paramNode1, paramNode2);
    } else {
      paramNode1 = new Node(79, paramNode1, node, paramNode2);
    } 
    if (paramInt != 0)
      paramNode1.putIntProp(16, paramInt); 
    return new Node(68, paramNode1);
  }
  
  private Node createPropertyGet(Node paramNode, String paramString1, String paramString2, int paramInt) {
    if (paramString1 == null && paramInt == 0) {
      if (paramNode == null)
        return createName(paramString2); 
      checkActivationName(paramString2, 33);
      if (ScriptRuntime.isSpecialProperty(paramString2)) {
        paramNode = new Node(72, paramNode);
        paramNode.putProp(17, paramString2);
        return new Node(68, paramNode);
      } 
      return new Node(33, paramNode, Node.newString(paramString2));
    } 
    return createMemberRefGet(paramNode, paramString1, Node.newString(paramString2), paramInt | 0x1);
  }
  
  private Node createString(String paramString) {
    return Node.newString(paramString);
  }
  
  private Node createTryCatchFinally(Node paramNode1, Node paramNode2, Node paramNode3, int paramInt) {
    boolean bool;
    if (paramNode3 != null && (paramNode3.getType() != 130 || paramNode3.hasChildren())) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramNode1.getType() == 130 && !paramNode1.hasChildren() && !bool)
      return paramNode1; 
    boolean bool1 = paramNode2.hasChildren();
    if (!bool && !bool1)
      return paramNode1; 
    Node node = new Node(142);
    Jump jump = new Jump(82, paramNode1, paramInt);
    jump.putProp(3, node);
    if (bool1) {
      Node node1 = Node.newTarget();
      jump.addChildToBack((Node)makeJump(5, node1));
      paramNode1 = Node.newTarget();
      jump.target = paramNode1;
      jump.addChildToBack(paramNode1);
      Node node2 = new Node(142);
      paramNode2 = paramNode2.getFirstChild();
      boolean bool2 = false;
      for (paramInt = 0; paramNode2 != null; paramInt++) {
        int i = paramNode2.getLineno();
        Node node3 = paramNode2.getFirstChild();
        Node node4 = node3.getNext();
        Node node5 = node4.getNext();
        paramNode2.removeChild(node3);
        paramNode2.removeChild(node4);
        paramNode2.removeChild(node5);
        node5.addChildToBack(new Node(3));
        node5.addChildToBack((Node)makeJump(5, node1));
        if (node4.getType() == 129) {
          bool2 = true;
        } else {
          node5 = createIf(node4, node5, (Node)null, i);
        } 
        node3 = new Node(57, node3, createUseLocal(node));
        node3.putProp(3, node2);
        node3.putIntProp(14, paramInt);
        node2.addChildToBack(node3);
        node2.addChildToBack(createWith(createUseLocal(node2), node5, i));
        paramNode2 = paramNode2.getNext();
      } 
      jump.addChildToBack(node2);
      if (!bool2) {
        paramNode1 = new Node(51);
        paramNode1.putProp(3, node);
        jump.addChildToBack(paramNode1);
      } 
      jump.addChildToBack(node1);
    } 
    if (bool) {
      paramNode2 = Node.newTarget();
      jump.setFinally(paramNode2);
      jump.addChildToBack((Node)makeJump(136, paramNode2));
      paramNode1 = Node.newTarget();
      jump.addChildToBack((Node)makeJump(5, paramNode1));
      jump.addChildToBack(paramNode2);
      paramNode2 = new Node(126, paramNode3);
      paramNode2.putProp(3, node);
      jump.addChildToBack(paramNode2);
      jump.addChildToBack(paramNode1);
    } 
    node.addChildToBack((Node)jump);
    return node;
  }
  
  private Node createUnary(int paramInt, Node paramNode) {
    int i = paramNode.getType();
    switch (paramInt) {
      default:
        return new Node(paramInt, paramNode);
      case 32:
        if (i == 39) {
          paramNode.setType(138);
          return paramNode;
        } 
      case 31:
        if (i == 39) {
          paramNode.setType(49);
          paramNode = new Node(paramInt, paramNode, Node.newString(paramNode.getString()));
        } else {
          if (i == 33 || i == 36) {
            Node node1 = paramNode.getFirstChild();
            Node node2 = paramNode.getLastChild();
            paramNode.removeChild(node1);
            paramNode.removeChild(node2);
            return new Node(paramInt, node1, node2);
          } 
          if (i == 68) {
            Node node = paramNode.getFirstChild();
            paramNode.removeChild(node);
            paramNode = new Node(70, node);
          } else {
            paramNode = new Node(paramInt, new Node(45), paramNode);
          } 
        } 
        return paramNode;
      case 29:
        if (i == 40) {
          paramNode.setDouble(-paramNode.getDouble());
          return paramNode;
        } 
      case 27:
        if (i == 40) {
          paramNode.setDouble(ScriptRuntime.toInt32(paramNode.getDouble()));
          return paramNode;
        } 
      case 26:
        break;
    } 
    int j = isAlwaysDefinedBoolean(paramNode);
    if (j != 0) {
      if (j == 1) {
        paramInt = 44;
      } else {
        paramInt = 45;
      } 
      if (i == 45 || i == 44) {
        paramNode.setType(paramInt);
        return paramNode;
      } 
      return new Node(paramInt);
    } 
  }
  
  private Node createUseLocal(Node paramNode) {
    if (142 == paramNode.getType()) {
      Node node = new Node(54);
      node.putProp(3, paramNode);
      return node;
    } 
    throw Kit.codeBug();
  }
  
  private Node createWith(Node paramNode1, Node paramNode2, int paramInt) {
    setRequiresActivation();
    Node node = new Node(130, paramInt);
    node.addChildToBack(new Node(2, paramNode1));
    node.addChildrenToBack(new Node(124, paramNode2, paramInt));
    node.addChildToBack(new Node(3));
    return node;
  }
  
  private Node genExprTransformHelper(GeneratorExpression paramGeneratorExpression) {
    // Byte code:
    //   0: aload_0
    //   1: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   4: bipush #88
    //   6: invokevirtual addToken : (I)V
    //   9: aload_1
    //   10: invokevirtual getLineno : ()I
    //   13: istore_2
    //   14: aload_0
    //   15: aload_1
    //   16: invokevirtual getResult : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   19: invokevirtual transform : (Lcom/trendmicro/hippo/ast/AstNode;)Lcom/trendmicro/hippo/Node;
    //   22: astore_3
    //   23: aload_1
    //   24: invokevirtual getLoops : ()Ljava/util/List;
    //   27: astore #4
    //   29: aload #4
    //   31: invokeinterface size : ()I
    //   36: istore #5
    //   38: iload #5
    //   40: anewarray com/trendmicro/hippo/Node
    //   43: astore #6
    //   45: iload #5
    //   47: anewarray com/trendmicro/hippo/Node
    //   50: astore #7
    //   52: iconst_0
    //   53: istore #8
    //   55: bipush #89
    //   57: istore #9
    //   59: iload #8
    //   61: iload #5
    //   63: if_icmpge -> 272
    //   66: aload #4
    //   68: iload #8
    //   70: invokeinterface get : (I)Ljava/lang/Object;
    //   75: checkcast com/trendmicro/hippo/ast/GeneratorExpressionLoop
    //   78: astore #10
    //   80: aload_0
    //   81: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   84: ldc ' '
    //   86: invokevirtual addName : (Ljava/lang/String;)V
    //   89: aload_0
    //   90: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   93: bipush #120
    //   95: invokevirtual addToken : (I)V
    //   98: aload_0
    //   99: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   102: bipush #88
    //   104: invokevirtual addToken : (I)V
    //   107: aload #10
    //   109: invokevirtual getIterator : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   112: astore #11
    //   114: aload #11
    //   116: invokevirtual getType : ()I
    //   119: bipush #39
    //   121: if_icmpne -> 143
    //   124: aload #11
    //   126: invokevirtual getString : ()Ljava/lang/String;
    //   129: astore #12
    //   131: aload_0
    //   132: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   135: aload #12
    //   137: invokevirtual addName : (Ljava/lang/String;)V
    //   140: goto -> 189
    //   143: aload_0
    //   144: aload #11
    //   146: invokevirtual decompile : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   149: aload_0
    //   150: getfield currentScriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   153: invokevirtual getNextTempName : ()Ljava/lang/String;
    //   156: astore #12
    //   158: aload_0
    //   159: bipush #88
    //   161: aload #12
    //   163: iconst_0
    //   164: invokevirtual defineSymbol : (ILjava/lang/String;Z)V
    //   167: aload_0
    //   168: bipush #90
    //   170: aload_0
    //   171: bipush #91
    //   173: aload #11
    //   175: aload_0
    //   176: aload #12
    //   178: invokevirtual createName : (Ljava/lang/String;)Lcom/trendmicro/hippo/Node;
    //   181: invokespecial createAssignment : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   184: aload_3
    //   185: invokespecial createBinary : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   188: astore_3
    //   189: aload_0
    //   190: aload #12
    //   192: invokevirtual createName : (Ljava/lang/String;)Lcom/trendmicro/hippo/Node;
    //   195: astore #11
    //   197: aload_0
    //   198: sipush #154
    //   201: aload #12
    //   203: iconst_0
    //   204: invokevirtual defineSymbol : (ILjava/lang/String;Z)V
    //   207: aload #6
    //   209: iload #8
    //   211: aload #11
    //   213: aastore
    //   214: aload #10
    //   216: invokevirtual isForOf : ()Z
    //   219: ifeq -> 234
    //   222: aload_0
    //   223: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   226: ldc 'of '
    //   228: invokevirtual addName : (Ljava/lang/String;)V
    //   231: goto -> 243
    //   234: aload_0
    //   235: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   238: bipush #52
    //   240: invokevirtual addToken : (I)V
    //   243: aload #7
    //   245: iload #8
    //   247: aload_0
    //   248: aload #10
    //   250: invokevirtual getIteratedObject : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   253: invokevirtual transform : (Lcom/trendmicro/hippo/ast/AstNode;)Lcom/trendmicro/hippo/Node;
    //   256: aastore
    //   257: aload_0
    //   258: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   261: bipush #89
    //   263: invokevirtual addToken : (I)V
    //   266: iinc #8, 1
    //   269: goto -> 55
    //   272: new com/trendmicro/hippo/Node
    //   275: dup
    //   276: bipush #73
    //   278: aload_3
    //   279: aload_1
    //   280: invokevirtual getLineno : ()I
    //   283: invokespecial <init> : (ILcom/trendmicro/hippo/Node;I)V
    //   286: astore #11
    //   288: new com/trendmicro/hippo/Node
    //   291: dup
    //   292: sipush #134
    //   295: aload #11
    //   297: iload_2
    //   298: invokespecial <init> : (ILcom/trendmicro/hippo/Node;I)V
    //   301: astore #12
    //   303: aload_1
    //   304: invokevirtual getFilter : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   307: astore #13
    //   309: aconst_null
    //   310: astore #10
    //   312: aload #12
    //   314: astore_3
    //   315: aload #13
    //   317: ifnull -> 373
    //   320: aload_0
    //   321: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   324: ldc ' '
    //   326: invokevirtual addName : (Ljava/lang/String;)V
    //   329: aload_0
    //   330: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   333: bipush #113
    //   335: invokevirtual addToken : (I)V
    //   338: aload_0
    //   339: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   342: bipush #88
    //   344: invokevirtual addToken : (I)V
    //   347: aload_0
    //   348: aload_0
    //   349: aload_1
    //   350: invokevirtual getFilter : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   353: invokevirtual transform : (Lcom/trendmicro/hippo/ast/AstNode;)Lcom/trendmicro/hippo/Node;
    //   356: aload #12
    //   358: aconst_null
    //   359: iload_2
    //   360: invokespecial createIf : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;I)Lcom/trendmicro/hippo/Node;
    //   363: astore_3
    //   364: aload_0
    //   365: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   368: bipush #89
    //   370: invokevirtual addToken : (I)V
    //   373: iconst_0
    //   374: istore #8
    //   376: iinc #5, -1
    //   379: aload_3
    //   380: astore #12
    //   382: aload #11
    //   384: astore_1
    //   385: aload #10
    //   387: astore_3
    //   388: iload #5
    //   390: iflt -> 514
    //   393: aload #4
    //   395: iload #5
    //   397: invokeinterface get : (I)Ljava/lang/Object;
    //   402: checkcast com/trendmicro/hippo/ast/GeneratorExpressionLoop
    //   405: astore #14
    //   407: aload_0
    //   408: aload_3
    //   409: aload #14
    //   411: invokevirtual getLineno : ()I
    //   414: invokespecial createLoopNode : (Lcom/trendmicro/hippo/Node;I)Lcom/trendmicro/hippo/ast/Scope;
    //   417: astore #11
    //   419: aload_0
    //   420: aload #11
    //   422: invokevirtual pushScope : (Lcom/trendmicro/hippo/ast/Scope;)V
    //   425: iinc #8, 1
    //   428: aload #6
    //   430: iload #5
    //   432: aaload
    //   433: astore #13
    //   435: aload #7
    //   437: iload #5
    //   439: aaload
    //   440: astore #10
    //   442: aload #14
    //   444: invokevirtual isForEach : ()Z
    //   447: istore #15
    //   449: aload #14
    //   451: invokevirtual isForOf : ()Z
    //   454: istore #16
    //   456: aload_0
    //   457: sipush #154
    //   460: aload #11
    //   462: aload #13
    //   464: aload #10
    //   466: aload #12
    //   468: iload #15
    //   470: iload #16
    //   472: invokespecial createForIn : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;ZZ)Lcom/trendmicro/hippo/Node;
    //   475: astore #12
    //   477: iinc #5, -1
    //   480: goto -> 388
    //   483: astore_1
    //   484: goto -> 492
    //   487: astore_1
    //   488: goto -> 492
    //   491: astore_1
    //   492: iconst_0
    //   493: istore #9
    //   495: iload #9
    //   497: iload #8
    //   499: if_icmpge -> 512
    //   502: aload_0
    //   503: invokevirtual popScope : ()V
    //   506: iinc #9, 1
    //   509: goto -> 495
    //   512: aload_1
    //   513: athrow
    //   514: iconst_0
    //   515: istore_2
    //   516: iload_2
    //   517: iload #8
    //   519: if_icmpge -> 532
    //   522: aload_0
    //   523: invokevirtual popScope : ()V
    //   526: iinc #2, 1
    //   529: goto -> 516
    //   532: aload_0
    //   533: getfield decompiler : Lcom/trendmicro/hippo/Decompiler;
    //   536: iload #9
    //   538: invokevirtual addToken : (I)V
    //   541: aload #12
    //   543: areturn
    // Exception table:
    //   from	to	target	type
    //   393	407	491	finally
    //   407	425	491	finally
    //   442	456	487	finally
    //   456	477	483	finally
  }
  
  private Object getPropKey(Node paramNode) {
    Object object;
    if (paramNode instanceof Name) {
      String str = ((Name)paramNode).getIdentifier();
      this.decompiler.addName(str);
      object = ScriptRuntime.getIndexObject(str);
    } else if (object instanceof StringLiteral) {
      object = ((StringLiteral)object).getValue();
      this.decompiler.addString((String)object);
      object = ScriptRuntime.getIndexObject((String)object);
    } else {
      if (object instanceof NumberLiteral) {
        double d = ((NumberLiteral)object).getNumber();
        this.decompiler.addNumber(d);
        return ScriptRuntime.getIndexObject(d);
      } 
      throw Kit.codeBug();
    } 
    return object;
  }
  
  private Node initFunction(FunctionNode paramFunctionNode, int paramInt1, Node paramNode, int paramInt2) {
    paramFunctionNode.setFunctionType(paramInt2);
    paramFunctionNode.addChildToBack(paramNode);
    if (paramFunctionNode.getFunctionCount() != 0)
      paramFunctionNode.setRequiresActivation(); 
    if (paramInt2 == 2) {
      Name name = paramFunctionNode.getFunctionName();
      if (name != null && name.length() != 0 && paramFunctionNode.getSymbol(name.getIdentifier()) == null) {
        paramFunctionNode.putSymbol(new Symbol(110, name.getIdentifier()));
        paramNode.addChildrenToFront(new Node(134, new Node(8, Node.newString(49, name.getIdentifier()), new Node(64))));
      } 
    } 
    Node node2 = paramNode.getLastChild();
    if (node2 == null || node2.getType() != 4)
      paramNode.addChildToBack(new Node(4)); 
    Node node1 = Node.newString(110, paramFunctionNode.getName());
    node1.putIntProp(1, paramInt1);
    return node1;
  }
  
  private static int isAlwaysDefinedBoolean(Node paramNode) {
    int i = paramNode.getType();
    if (i != 40)
      return (i != 42 && i != 44) ? ((i != 45) ? 0 : 1) : -1; 
    double d = paramNode.getDouble();
    return (d == d && d != 0.0D) ? 1 : -1;
  }
  
  private Jump makeJump(int paramInt, Node paramNode) {
    Jump jump = new Jump(paramInt);
    jump.target = paramNode;
    return jump;
  }
  
  private Node makeReference(Node paramNode) {
    int i = paramNode.getType();
    if (i != 33 && i != 36 && i != 68)
      if (i != 38) {
        if (i != 39)
          return null; 
      } else {
        paramNode.setType(71);
        return new Node(68, paramNode);
      }  
    return paramNode;
  }
  
  private Node transformArrayComp(ArrayComprehension paramArrayComprehension) {
    int i = paramArrayComprehension.getLineno();
    Scope scope = createScopeNode(158, i);
    String str = this.currentScriptOrFn.getNextTempName();
    pushScope(scope);
    try {
      defineSymbol(154, str, false);
      Node node1 = new Node();
      this(130, i);
      Node node2 = createCallOrNew(30, createName("Array"));
      Node node3 = new Node();
      this(134, createAssignment(91, createName(str), node2), i);
      node1.addChildToBack(node3);
      node1.addChildToBack(arrayCompTransformHelper(paramArrayComprehension, str));
      scope.addChildToBack(node1);
      scope.addChildToBack(createName(str));
      return (Node)scope;
    } finally {
      popScope();
    } 
  }
  
  private Node transformArrayLiteral(ArrayLiteral paramArrayLiteral) {
    ArrayList<Integer> arrayList;
    if (paramArrayLiteral.isDestructuring())
      return (Node)paramArrayLiteral; 
    this.decompiler.addToken(84);
    List<AstNode> list = paramArrayLiteral.getElements();
    Node node = new Node(66);
    AstNode astNode = null;
    byte b;
    for (b = 0; b < list.size(); b++) {
      AstNode astNode1 = list.get(b);
      if (astNode1.getType() != 129) {
        node.addChildToBack(transform(astNode1));
      } else {
        ArrayList<Integer> arrayList1;
        astNode1 = astNode;
        if (astNode == null)
          arrayList1 = new ArrayList(); 
        arrayList1.add(Integer.valueOf(b));
        arrayList = arrayList1;
      } 
      if (b < list.size() - 1)
        this.decompiler.addToken(90); 
    } 
    this.decompiler.addToken(85);
    node.putIntProp(21, paramArrayLiteral.getDestructuringLength());
    if (arrayList != null) {
      int[] arrayOfInt = new int[arrayList.size()];
      for (b = 0; b < arrayList.size(); b++)
        arrayOfInt[b] = ((Integer)arrayList.get(b)).intValue(); 
      node.putProp(11, arrayOfInt);
    } 
    return node;
  }
  
  private Node transformAssignment(Assignment paramAssignment) {
    Node node;
    AstNode astNode = removeParens(paramAssignment.getLeft());
    if (isDestructuring((Node)astNode)) {
      decompile(astNode);
    } else {
      node = transform(astNode);
    } 
    this.decompiler.addToken(paramAssignment.getType());
    return createAssignment(paramAssignment.getType(), node, transform(paramAssignment.getRight()));
  }
  
  private Node transformBlock(AstNode paramAstNode) {
    if (paramAstNode instanceof Scope)
      pushScope((Scope)paramAstNode); 
    try {
      ArrayList<Node> arrayList = new ArrayList();
      this();
      Iterator<AstNode> iterator = paramAstNode.iterator();
      while (iterator.hasNext())
        arrayList.add(transform(iterator.next())); 
      paramAstNode.removeChildren();
      iterator = (Iterator)arrayList.iterator();
      while (iterator.hasNext())
        paramAstNode.addChildToBack((Node)iterator.next()); 
      return (Node)paramAstNode;
    } finally {
      if (paramAstNode instanceof Scope)
        popScope(); 
    } 
  }
  
  private Node transformBreak(BreakStatement paramBreakStatement) {
    this.decompiler.addToken(121);
    if (paramBreakStatement.getBreakLabel() != null)
      this.decompiler.addName(paramBreakStatement.getBreakLabel().getIdentifier()); 
    this.decompiler.addEOL(83);
    return (Node)paramBreakStatement;
  }
  
  private Node transformCondExpr(ConditionalExpression paramConditionalExpression) {
    Node node1 = transform(paramConditionalExpression.getTestExpression());
    this.decompiler.addToken(103);
    Node node2 = transform(paramConditionalExpression.getTrueExpression());
    this.decompiler.addToken(104);
    return createCondExpr(node1, node2, transform(paramConditionalExpression.getFalseExpression()));
  }
  
  private Node transformContinue(ContinueStatement paramContinueStatement) {
    this.decompiler.addToken(122);
    if (paramContinueStatement.getLabel() != null)
      this.decompiler.addName(paramContinueStatement.getLabel().getIdentifier()); 
    this.decompiler.addEOL(83);
    return (Node)paramContinueStatement;
  }
  
  private Node transformDefaultXmlNamepace(UnaryExpression paramUnaryExpression) {
    this.decompiler.addToken(117);
    this.decompiler.addName(" xml");
    this.decompiler.addName(" namespace");
    this.decompiler.addToken(91);
    return createUnary(75, transform(paramUnaryExpression.getOperand()));
  }
  
  private Node transformDoLoop(DoLoop paramDoLoop) {
    paramDoLoop.setType(133);
    pushScope((Scope)paramDoLoop);
    try {
      this.decompiler.addToken(119);
      this.decompiler.addEOL(86);
      Node node1 = transform(paramDoLoop.getBody());
      this.decompiler.addToken(87);
      this.decompiler.addToken(118);
      this.decompiler.addToken(88);
      Node node2 = transform(paramDoLoop.getCondition());
      this.decompiler.addToken(89);
      this.decompiler.addEOL(83);
      return createLoop((Jump)paramDoLoop, 0, node1, node2, (Node)null, (Node)null);
    } finally {
      popScope();
    } 
  }
  
  private Node transformElementGet(ElementGet paramElementGet) {
    Node node2 = transform(paramElementGet.getTarget());
    this.decompiler.addToken(84);
    Node node1 = transform(paramElementGet.getElement());
    this.decompiler.addToken(85);
    return new Node(36, node2, node1);
  }
  
  private Node transformExprStmt(ExpressionStatement paramExpressionStatement) {
    Node node = transform(paramExpressionStatement.getExpression());
    this.decompiler.addEOL(83);
    return new Node(paramExpressionStatement.getType(), node, paramExpressionStatement.getLineno());
  }
  
  private Node transformForInLoop(ForInLoop paramForInLoop) {
    this.decompiler.addToken(120);
    if (paramForInLoop.isForEach())
      this.decompiler.addName("each "); 
    this.decompiler.addToken(88);
    paramForInLoop.setType(133);
    pushScope((Scope)paramForInLoop);
    int i = -1;
    try {
      AstNode astNode = paramForInLoop.getIterator();
      if (astNode instanceof VariableDeclaration)
        i = ((VariableDeclaration)astNode).getType(); 
      Node node2 = transform(astNode);
      if (paramForInLoop.isForOf()) {
        this.decompiler.addName("of ");
      } else {
        this.decompiler.addToken(52);
      } 
      Node node1 = transform(paramForInLoop.getIteratedObject());
      this.decompiler.addToken(89);
      this.decompiler.addEOL(86);
      Node node3 = transform(paramForInLoop.getBody());
      this.decompiler.addEOL(87);
      return createForIn(i, (Node)paramForInLoop, node2, node1, node3, paramForInLoop.isForEach(), paramForInLoop.isForOf());
    } finally {
      popScope();
    } 
  }
  
  private Node transformForLoop(ForLoop paramForLoop) {
    this.decompiler.addToken(120);
    this.decompiler.addToken(88);
    paramForLoop.setType(133);
    Scope scope = this.currentScope;
    this.currentScope = (Scope)paramForLoop;
    try {
      Node node1 = transform(paramForLoop.getInitializer());
      this.decompiler.addToken(83);
      Node node2 = transform(paramForLoop.getCondition());
      this.decompiler.addToken(83);
      Node node3 = transform(paramForLoop.getIncrement());
      this.decompiler.addToken(89);
      this.decompiler.addEOL(86);
      Node node4 = transform(paramForLoop.getBody());
      this.decompiler.addEOL(87);
      return createFor((Scope)paramForLoop, node1, node2, node3, node4);
    } finally {
      this.currentScope = scope;
    } 
  }
  
  private Node transformFunction(FunctionNode paramFunctionNode) {
    int i = paramFunctionNode.getFunctionType();
    int j = this.decompiler.markFunctionStart(i);
    Node node = decompileFunctionHeader(paramFunctionNode);
    int k = this.currentScriptOrFn.addFunction(paramFunctionNode);
    Parser.PerFunctionVariables perFunctionVariables = new Parser.PerFunctionVariables(this, paramFunctionNode);
    try {
      Node node1 = (Node)paramFunctionNode.getProp(23);
      paramFunctionNode.removeProp(23);
      int m = paramFunctionNode.getBody().getLineno();
      this.nestingOfFunction++;
      Node node2 = transform(paramFunctionNode.getBody());
      if (!paramFunctionNode.isExpressionClosure())
        this.decompiler.addToken(87); 
      paramFunctionNode.setEncodedSourceBounds(j, this.decompiler.markFunctionEnd(j));
      if (i != 2 && !paramFunctionNode.isExpressionClosure())
        this.decompiler.addToken(1); 
      if (node1 != null) {
        Node node4 = new Node();
        this(134, node1, m);
        node2.addChildToFront(node4);
      } 
      i = paramFunctionNode.getFunctionType();
      Node node3 = initFunction(paramFunctionNode, k, node2, i);
      node1 = node3;
      if (node != null) {
        node3 = createAssignment(91, node, node3);
        node1 = node3;
        if (i != 2)
          node1 = createExprStatementNoReturn(node3, paramFunctionNode.getLineno()); 
      } 
      return node1;
    } finally {
      this.nestingOfFunction--;
      perFunctionVariables.restore();
    } 
  }
  
  private Node transformFunctionCall(FunctionCall paramFunctionCall) {
    Node node = createCallOrNew(38, transform(paramFunctionCall.getTarget()));
    node.setLineno(paramFunctionCall.getLineno());
    this.decompiler.addToken(88);
    List<AstNode> list = paramFunctionCall.getArguments();
    for (byte b = 0; b < list.size(); b++) {
      node.addChildToBack(transform(list.get(b)));
      if (b < list.size() - 1)
        this.decompiler.addToken(90); 
    } 
    this.decompiler.addToken(89);
    return node;
  }
  
  private Node transformGenExpr(GeneratorExpression paramGeneratorExpression) {
    FunctionNode functionNode = new FunctionNode();
    functionNode.setSourceName(this.currentScriptOrFn.getNextTempName());
    functionNode.setIsGenerator();
    functionNode.setFunctionType(2);
    functionNode.setRequiresActivation();
    int i = functionNode.getFunctionType();
    int j = this.decompiler.markFunctionStart(i);
    Node node = decompileFunctionHeader(functionNode);
    int k = this.currentScriptOrFn.addFunction(functionNode);
    Parser.PerFunctionVariables perFunctionVariables = new Parser.PerFunctionVariables(this, functionNode);
    try {
      Node node1 = (Node)functionNode.getProp(23);
      functionNode.removeProp(23);
      int m = paramGeneratorExpression.lineno;
      this.nestingOfFunction++;
      Node node2 = genExprTransformHelper(paramGeneratorExpression);
      if (!functionNode.isExpressionClosure())
        this.decompiler.addToken(87); 
      functionNode.setEncodedSourceBounds(j, this.decompiler.markFunctionEnd(j));
      if (i != 2 && !functionNode.isExpressionClosure())
        this.decompiler.addToken(1); 
      if (node1 != null) {
        Node node4 = new Node();
        this(134, node1, m);
        node2.addChildToFront(node4);
      } 
      j = functionNode.getFunctionType();
      Node node3 = initFunction(functionNode, k, node2, j);
      node1 = node3;
      if (node != null) {
        node3 = createAssignment(91, node, node3);
        node1 = node3;
        if (j != 2)
          node1 = createExprStatementNoReturn(node3, functionNode.getLineno()); 
      } 
      this.nestingOfFunction--;
      perFunctionVariables.restore();
      node1 = createCallOrNew(38, node1);
      node1.setLineno(paramGeneratorExpression.getLineno());
      return node1;
    } finally {
      this.nestingOfFunction--;
      perFunctionVariables.restore();
    } 
  }
  
  private Node transformIf(IfStatement paramIfStatement) {
    this.decompiler.addToken(113);
    this.decompiler.addToken(88);
    Node node1 = transform(paramIfStatement.getCondition());
    this.decompiler.addToken(89);
    this.decompiler.addEOL(86);
    Node node2 = transform(paramIfStatement.getThenPart());
    Node node3 = null;
    if (paramIfStatement.getElsePart() != null) {
      this.decompiler.addToken(87);
      this.decompiler.addToken(114);
      this.decompiler.addEOL(86);
      node3 = transform(paramIfStatement.getElsePart());
    } 
    this.decompiler.addEOL(87);
    return createIf(node1, node2, node3, paramIfStatement.getLineno());
  }
  
  private Node transformInfix(InfixExpression paramInfixExpression) {
    Node node1 = transform(paramInfixExpression.getLeft());
    this.decompiler.addToken(paramInfixExpression.getType());
    Node node2 = transform(paramInfixExpression.getRight());
    if (paramInfixExpression instanceof com.trendmicro.hippo.ast.XmlDotQuery)
      this.decompiler.addToken(89); 
    return createBinary(paramInfixExpression.getType(), node1, node2);
  }
  
  private Node transformLabeledStatement(LabeledStatement paramLabeledStatement) {
    Label label = paramLabeledStatement.getFirstLabel();
    List list = paramLabeledStatement.getLabels();
    this.decompiler.addName(label.getName());
    if (list.size() > 1)
      for (Label label1 : list.subList(1, list.size())) {
        this.decompiler.addEOL(104);
        this.decompiler.addName(label1.getName());
      }  
    if (paramLabeledStatement.getStatement().getType() == 130) {
      this.decompiler.addToken(67);
      this.decompiler.addEOL(86);
    } else {
      this.decompiler.addEOL(104);
    } 
    Node node2 = transform(paramLabeledStatement.getStatement());
    if (paramLabeledStatement.getStatement().getType() == 130)
      this.decompiler.addEOL(87); 
    Node node1 = Node.newTarget();
    node2 = new Node(130, (Node)label, node2, node1);
    label.target = node1;
    return node2;
  }
  
  private Node transformLetNode(LetNode paramLetNode) {
    pushScope((Scope)paramLetNode);
    try {
      boolean bool;
      this.decompiler.addToken(154);
      this.decompiler.addToken(88);
      Node node = transformVariableInitializers(paramLetNode.getVariables());
      this.decompiler.addToken(89);
      paramLetNode.addChildToBack(node);
      if (paramLetNode.getType() == 159) {
        bool = true;
      } else {
        bool = false;
      } 
      if (paramLetNode.getBody() != null) {
        if (bool) {
          this.decompiler.addName(" ");
        } else {
          this.decompiler.addEOL(86);
        } 
        paramLetNode.addChildToBack(transform(paramLetNode.getBody()));
        if (!bool)
          this.decompiler.addEOL(87); 
      } 
      return (Node)paramLetNode;
    } finally {
      popScope();
    } 
  }
  
  private Node transformLiteral(AstNode paramAstNode) {
    this.decompiler.addToken(paramAstNode.getType());
    return (Node)paramAstNode;
  }
  
  private Node transformName(Name paramName) {
    this.decompiler.addName(paramName.getIdentifier());
    return (Node)paramName;
  }
  
  private Node transformNewExpr(NewExpression paramNewExpression) {
    this.decompiler.addToken(30);
    Node node = createCallOrNew(30, transform(paramNewExpression.getTarget()));
    node.setLineno(paramNewExpression.getLineno());
    List<AstNode> list = paramNewExpression.getArguments();
    this.decompiler.addToken(88);
    for (byte b = 0; b < list.size(); b++) {
      node.addChildToBack(transform(list.get(b)));
      if (b < list.size() - 1)
        this.decompiler.addToken(90); 
    } 
    this.decompiler.addToken(89);
    if (paramNewExpression.getInitializer() != null)
      node.addChildToBack(transformObjectLiteral(paramNewExpression.getInitializer())); 
    return node;
  }
  
  private Node transformNumber(NumberLiteral paramNumberLiteral) {
    this.decompiler.addNumber(paramNumberLiteral.getNumber());
    return (Node)paramNumberLiteral;
  }
  
  private Node transformObjectLiteral(ObjectLiteral paramObjectLiteral) {
    Object[] arrayOfObject;
    if (paramObjectLiteral.isDestructuring())
      return (Node)paramObjectLiteral; 
    this.decompiler.addToken(86);
    List list = paramObjectLiteral.getElements();
    Node node = new Node(67);
    if (list.isEmpty()) {
      arrayOfObject = ScriptRuntime.emptyArgs;
    } else {
      int i = arrayOfObject.size();
      int j = 0;
      Object[] arrayOfObject1 = new Object[i];
      for (ObjectProperty objectProperty : arrayOfObject) {
        Node node1;
        if (objectProperty.isGetterMethod()) {
          this.decompiler.addToken(152);
        } else if (objectProperty.isSetterMethod()) {
          this.decompiler.addToken(153);
        } else if (objectProperty.isNormalMethod()) {
          this.decompiler.addToken(164);
        } 
        int k = j + 1;
        arrayOfObject1[j] = getPropKey((Node)objectProperty.getLeft());
        if (!objectProperty.isMethod())
          this.decompiler.addToken(67); 
        Node node2 = transform(objectProperty.getRight());
        if (objectProperty.isGetterMethod()) {
          node1 = createUnary(152, node2);
        } else if (objectProperty.isSetterMethod()) {
          node1 = createUnary(153, node2);
        } else {
          node1 = node2;
          if (objectProperty.isNormalMethod())
            node1 = createUnary(164, node2); 
        } 
        node.addChildToBack(node1);
        if (k < i)
          this.decompiler.addToken(90); 
        j = k;
      } 
      arrayOfObject = arrayOfObject1;
    } 
    this.decompiler.addToken(87);
    node.putProp(12, arrayOfObject);
    return node;
  }
  
  private Node transformParenExpr(ParenthesizedExpression paramParenthesizedExpression) {
    AstNode astNode = paramParenthesizedExpression.getExpression();
    this.decompiler.addToken(88);
    byte b1 = 1;
    while (astNode instanceof ParenthesizedExpression) {
      this.decompiler.addToken(88);
      b1++;
      astNode = ((ParenthesizedExpression)astNode).getExpression();
    } 
    Node node = transform(astNode);
    for (byte b2 = 0; b2 < b1; b2++)
      this.decompiler.addToken(89); 
    node.putProp(19, Boolean.TRUE);
    return node;
  }
  
  private Node transformPropertyGet(PropertyGet paramPropertyGet) {
    Node node = transform(paramPropertyGet.getTarget());
    String str = paramPropertyGet.getProperty().getIdentifier();
    this.decompiler.addToken(109);
    this.decompiler.addName(str);
    return createPropertyGet(node, (String)null, str, 0);
  }
  
  private Node transformRegExp(RegExpLiteral paramRegExpLiteral) {
    this.decompiler.addRegexp(paramRegExpLiteral.getValue(), paramRegExpLiteral.getFlags());
    this.currentScriptOrFn.addRegExp(paramRegExpLiteral);
    return (Node)paramRegExpLiteral;
  }
  
  private Node transformReturn(ReturnStatement paramReturnStatement) {
    Node node1;
    Node node2;
    boolean bool1 = Boolean.TRUE.equals(paramReturnStatement.getProp(25));
    boolean bool2 = Boolean.TRUE.equals(paramReturnStatement.getProp(27));
    if (bool1) {
      if (!bool2)
        this.decompiler.addName(" "); 
    } else {
      this.decompiler.addToken(4);
    } 
    AstNode astNode = paramReturnStatement.getReturnValue();
    if (astNode == null) {
      node2 = null;
    } else {
      node2 = transform(astNode);
    } 
    if (!bool1)
      this.decompiler.addEOL(83); 
    if (astNode == null) {
      node1 = new Node(4, paramReturnStatement.getLineno());
    } else {
      node1 = new Node(4, node2, node1.getLineno());
    } 
    return node1;
  }
  
  private Node transformScript(ScriptNode paramScriptNode) {
    this.decompiler.addToken(137);
    if (this.currentScope != null)
      Kit.codeBug(); 
    this.currentScope = (Scope)paramScriptNode;
    Node node = new Node(130);
    Iterator<AstNode> iterator = paramScriptNode.iterator();
    while (iterator.hasNext())
      node.addChildToBack(transform(iterator.next())); 
    paramScriptNode.removeChildren();
    node = node.getFirstChild();
    if (node != null)
      paramScriptNode.addChildrenToBack(node); 
    return (Node)paramScriptNode;
  }
  
  private Node transformString(StringLiteral paramStringLiteral) {
    this.decompiler.addString(paramStringLiteral.getValue());
    return Node.newString(paramStringLiteral.getValue());
  }
  
  private Node transformSwitch(SwitchStatement paramSwitchStatement) {
    this.decompiler.addToken(115);
    this.decompiler.addToken(88);
    Node node = transform(paramSwitchStatement.getExpression());
    this.decompiler.addToken(89);
    paramSwitchStatement.addChildToBack(node);
    node = new Node(130, (Node)paramSwitchStatement, paramSwitchStatement.getLineno());
    this.decompiler.addEOL(86);
    for (SwitchCase switchCase : paramSwitchStatement.getCases()) {
      Node node1;
      AstNode astNode = switchCase.getExpression();
      paramSwitchStatement = null;
      if (astNode != null) {
        this.decompiler.addToken(116);
        node1 = transform(astNode);
      } else {
        this.decompiler.addToken(117);
      } 
      this.decompiler.addEOL(104);
      List list = switchCase.getStatements();
      Block block = new Block();
      if (list != null) {
        Iterator<AstNode> iterator = list.iterator();
        while (iterator.hasNext())
          block.addChildToBack(transform(iterator.next())); 
      } 
      addSwitchCase(node, node1, (Node)block);
    } 
    this.decompiler.addEOL(87);
    closeSwitch(node);
    return node;
  }
  
  private Node transformThrow(ThrowStatement paramThrowStatement) {
    this.decompiler.addToken(50);
    Node node = transform(paramThrowStatement.getExpression());
    this.decompiler.addEOL(83);
    return new Node(50, node, paramThrowStatement.getLineno());
  }
  
  private Node transformTry(TryStatement paramTryStatement) {
    this.decompiler.addToken(82);
    this.decompiler.addEOL(86);
    Node node1 = transform(paramTryStatement.getTryBlock());
    this.decompiler.addEOL(87);
    Block block = new Block();
    for (CatchClause catchClause : paramTryStatement.getCatchClauses()) {
      EmptyExpression emptyExpression;
      this.decompiler.addToken(125);
      this.decompiler.addToken(88);
      String str = catchClause.getVarName().getIdentifier();
      this.decompiler.addName(str);
      AstNode astNode = catchClause.getCatchCondition();
      if (astNode != null) {
        this.decompiler.addName(" ");
        this.decompiler.addToken(113);
        Node node3 = transform(astNode);
      } else {
        emptyExpression = new EmptyExpression();
      } 
      this.decompiler.addToken(89);
      this.decompiler.addEOL(86);
      Node node = transform((AstNode)catchClause.getBody());
      this.decompiler.addEOL(87);
      block.addChildToBack(createCatch(str, (Node)emptyExpression, node, catchClause.getLineno()));
    } 
    Node node2 = null;
    if (paramTryStatement.getFinallyBlock() != null) {
      this.decompiler.addToken(126);
      this.decompiler.addEOL(86);
      node2 = transform(paramTryStatement.getFinallyBlock());
      this.decompiler.addEOL(87);
    } 
    return createTryCatchFinally(node1, (Node)block, node2, paramTryStatement.getLineno());
  }
  
  private Node transformUnary(UnaryExpression paramUnaryExpression) {
    int i = paramUnaryExpression.getType();
    if (i == 75)
      return transformDefaultXmlNamepace(paramUnaryExpression); 
    if (paramUnaryExpression.isPrefix())
      this.decompiler.addToken(i); 
    Node node = transform(paramUnaryExpression.getOperand());
    if (paramUnaryExpression.isPostfix())
      this.decompiler.addToken(i); 
    return (i == 107 || i == 108) ? createIncDec(i, paramUnaryExpression.isPostfix(), node) : createUnary(i, node);
  }
  
  private Node transformVariableInitializers(VariableDeclaration paramVariableDeclaration) {
    List list = paramVariableDeclaration.getVariables();
    int i = list.size();
    byte b = 0;
    for (VariableInitializer variableInitializer : list) {
      Node node1;
      AstNode astNode1 = variableInitializer.getTarget();
      AstNode astNode2 = variableInitializer.getInitializer();
      if (variableInitializer.isDestructuring()) {
        decompile(astNode1);
      } else {
        node1 = transform(astNode1);
      } 
      Node node2 = null;
      if (astNode2 != null) {
        this.decompiler.addToken(91);
        node2 = transform(astNode2);
      } 
      if (variableInitializer.isDestructuring()) {
        if (node2 == null) {
          paramVariableDeclaration.addChildToBack(node1);
        } else {
          paramVariableDeclaration.addChildToBack(createDestructuringAssignment(paramVariableDeclaration.getType(), node1, node2));
        } 
      } else {
        if (node2 != null)
          node1.addChildToBack(node2); 
        paramVariableDeclaration.addChildToBack(node1);
      } 
      if (b < i - 1)
        this.decompiler.addToken(90); 
      b++;
    } 
    return (Node)paramVariableDeclaration;
  }
  
  private Node transformVariables(VariableDeclaration paramVariableDeclaration) {
    this.decompiler.addToken(paramVariableDeclaration.getType());
    transformVariableInitializers(paramVariableDeclaration);
    AstNode astNode = paramVariableDeclaration.getParent();
    if (!(astNode instanceof com.trendmicro.hippo.ast.Loop) && !(astNode instanceof LetNode))
      this.decompiler.addEOL(83); 
    return (Node)paramVariableDeclaration;
  }
  
  private Node transformWhileLoop(WhileLoop paramWhileLoop) {
    this.decompiler.addToken(118);
    paramWhileLoop.setType(133);
    pushScope((Scope)paramWhileLoop);
    try {
      this.decompiler.addToken(88);
      Node node1 = transform(paramWhileLoop.getCondition());
      this.decompiler.addToken(89);
      this.decompiler.addEOL(86);
      Node node2 = transform(paramWhileLoop.getBody());
      this.decompiler.addEOL(87);
      return createLoop((Jump)paramWhileLoop, 1, node2, node1, (Node)null, (Node)null);
    } finally {
      popScope();
    } 
  }
  
  private Node transformWith(WithStatement paramWithStatement) {
    this.decompiler.addToken(124);
    this.decompiler.addToken(88);
    Node node1 = transform(paramWithStatement.getExpression());
    this.decompiler.addToken(89);
    this.decompiler.addEOL(86);
    Node node2 = transform(paramWithStatement.getStatement());
    this.decompiler.addEOL(87);
    return createWith(node1, node2, paramWithStatement.getLineno());
  }
  
  private Node transformXmlLiteral(XmlLiteral paramXmlLiteral) {
    Node node1;
    Node node2 = new Node(30, paramXmlLiteral.getLineno());
    List<XmlString> list = paramXmlLiteral.getFragments();
    if (((XmlString)list.get(0)).getXml().trim().startsWith("<>")) {
      str = "XMLList";
    } else {
      str = "XML";
    } 
    node2.addChildToBack(createName(str));
    String str = null;
    for (XmlFragment xmlFragment : list) {
      String str1;
      Node node;
      if (xmlFragment instanceof XmlString) {
        str1 = ((XmlString)xmlFragment).getXml();
        this.decompiler.addName(str1);
        if (str == null) {
          node1 = createString(str1);
          continue;
        } 
        node1 = createBinary(21, node1, createString(str1));
        continue;
      } 
      XmlExpression xmlExpression = (XmlExpression)str1;
      boolean bool = xmlExpression.isXmlAttribute();
      this.decompiler.addToken(86);
      if (xmlExpression.getExpression() instanceof EmptyExpression) {
        node = createString("");
      } else {
        node = transform(node.getExpression());
      } 
      this.decompiler.addToken(87);
      if (bool) {
        node = createUnary(76, node);
        node = createBinary(21, createString("\""), node);
        node = createBinary(21, node, createString("\""));
      } else {
        node = createUnary(77, node);
      } 
      node1 = createBinary(21, node1, node);
    } 
    node2.addChildToBack(node1);
    return node2;
  }
  
  private Node transformXmlMemberGet(XmlMemberGet paramXmlMemberGet) {
    int i;
    XmlRef xmlRef = paramXmlMemberGet.getMemberRef();
    Node node = transform(paramXmlMemberGet.getLeft());
    if (xmlRef.isAttributeAccess()) {
      i = 2;
    } else {
      i = 0;
    } 
    if (paramXmlMemberGet.getType() == 144) {
      i |= 0x4;
      this.decompiler.addToken(144);
    } else {
      this.decompiler.addToken(109);
    } 
    return transformXmlRef(node, xmlRef, i);
  }
  
  private Node transformXmlRef(Node paramNode, XmlRef paramXmlRef, int paramInt) {
    String str;
    if ((paramInt & 0x2) != 0)
      this.decompiler.addToken(148); 
    Name name = paramXmlRef.getNamespace();
    if (name != null) {
      String str1 = name.getIdentifier();
    } else {
      name = null;
    } 
    if (name != null) {
      this.decompiler.addName((String)name);
      this.decompiler.addToken(145);
    } 
    if (paramXmlRef instanceof XmlPropRef) {
      str = ((XmlPropRef)paramXmlRef).getPropName().getIdentifier();
      this.decompiler.addName(str);
      return createPropertyGet(paramNode, (String)name, str, paramInt);
    } 
    this.decompiler.addToken(84);
    Node node = transform(((XmlElemRef)str).getExpression());
    this.decompiler.addToken(85);
    return createElementGet(paramNode, (String)name, node, paramInt);
  }
  
  private Node transformXmlRef(XmlRef paramXmlRef) {
    boolean bool;
    if (paramXmlRef.isAttributeAccess()) {
      bool = true;
    } else {
      bool = false;
    } 
    return transformXmlRef((Node)null, paramXmlRef, bool);
  }
  
  private Node transformYield(Yield paramYield) {
    Node node;
    this.decompiler.addToken(73);
    if (paramYield.getValue() == null) {
      node = null;
    } else {
      node = transform(paramYield.getValue());
    } 
    return (node != null) ? new Node(73, node, paramYield.getLineno()) : new Node(73, paramYield.getLineno());
  }
  
  void decompile(AstNode paramAstNode) {
    int i = paramAstNode.getType();
    if (i != 33) {
      if (i != 36) {
        if (i != 43) {
          if (i != 129)
            if (i != 66) {
              if (i != 67) {
                StringBuilder stringBuilder;
                switch (i) {
                  default:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("unexpected token: ");
                    stringBuilder.append(Token.typeToName(paramAstNode.getType()));
                    Kit.codeBug(stringBuilder.toString());
                    return;
                  case 41:
                    this.decompiler.addString(((StringLiteral)paramAstNode).getValue());
                    return;
                  case 40:
                    this.decompiler.addNumber(((NumberLiteral)paramAstNode).getNumber());
                    return;
                  case 39:
                    break;
                } 
                this.decompiler.addName(((Name)paramAstNode).getIdentifier());
              } else {
                decompileObjectLiteral((ObjectLiteral)paramAstNode);
              } 
            } else {
              decompileArrayLiteral((ArrayLiteral)paramAstNode);
            }  
        } else {
          this.decompiler.addToken(paramAstNode.getType());
        } 
      } else {
        decompileElementGet((ElementGet)paramAstNode);
      } 
    } else {
      decompilePropertyGet((PropertyGet)paramAstNode);
    } 
  }
  
  void decompileArrayLiteral(ArrayLiteral paramArrayLiteral) {
    this.decompiler.addToken(84);
    List<AstNode> list = paramArrayLiteral.getElements();
    int i = list.size();
    for (byte b = 0; b < i; b++) {
      decompile(list.get(b));
      if (b < i - 1)
        this.decompiler.addToken(90); 
    } 
    this.decompiler.addToken(85);
  }
  
  void decompileElementGet(ElementGet paramElementGet) {
    decompile(paramElementGet.getTarget());
    this.decompiler.addToken(84);
    decompile(paramElementGet.getElement());
    this.decompiler.addToken(85);
  }
  
  Node decompileFunctionHeader(FunctionNode paramFunctionNode) {
    Node node = null;
    if (paramFunctionNode.getFunctionName() != null) {
      this.decompiler.addName(paramFunctionNode.getName());
    } else if (paramFunctionNode.getMemberExprNode() != null) {
      node = transform(paramFunctionNode.getMemberExprNode());
    } 
    int i = paramFunctionNode.getFunctionType();
    byte b1 = 0;
    if (i == 4) {
      i = 1;
    } else {
      i = 0;
    } 
    byte b2 = b1;
    if (i != 0) {
      b2 = b1;
      if (paramFunctionNode.getLp() == -1)
        b2 = 1; 
    } 
    if (!b2)
      this.decompiler.addToken(88); 
    List<AstNode> list = paramFunctionNode.getParams();
    for (b1 = 0; b1 < list.size(); b1++) {
      decompile(list.get(b1));
      if (b1 < list.size() - 1)
        this.decompiler.addToken(90); 
    } 
    if (b2 == 0)
      this.decompiler.addToken(89); 
    if (i != 0)
      this.decompiler.addToken(165); 
    if (!paramFunctionNode.isExpressionClosure())
      this.decompiler.addEOL(86); 
    return node;
  }
  
  void decompileObjectLiteral(ObjectLiteral paramObjectLiteral) {
    this.decompiler.addToken(86);
    List<ObjectProperty> list = paramObjectLiteral.getElements();
    int i = list.size();
    for (byte b = 0; b < i; b++) {
      ObjectProperty objectProperty = list.get(b);
      boolean bool = Boolean.TRUE.equals(objectProperty.getProp(26));
      decompile(objectProperty.getLeft());
      if (!bool) {
        this.decompiler.addToken(104);
        decompile(objectProperty.getRight());
      } 
      if (b < i - 1)
        this.decompiler.addToken(90); 
    } 
    this.decompiler.addToken(87);
  }
  
  void decompilePropertyGet(PropertyGet paramPropertyGet) {
    decompile(paramPropertyGet.getTarget());
    this.decompiler.addToken(109);
    decompile((AstNode)paramPropertyGet.getProperty());
  }
  
  boolean isDestructuring(Node paramNode) {
    boolean bool;
    if (paramNode instanceof DestructuringForm && ((DestructuringForm)paramNode).isDestructuring()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Node transform(AstNode paramAstNode) {
    int i = paramAstNode.getType();
    if (i != 66) {
      if (i != 67) {
        if (i != 129) {
          if (i != 130) {
            StringBuilder stringBuilder;
            switch (i) {
              default:
                switch (i) {
                  default:
                    switch (i) {
                      default:
                        if (paramAstNode instanceof ExpressionStatement)
                          return transformExprStmt((ExpressionStatement)paramAstNode); 
                        if (paramAstNode instanceof Assignment)
                          return transformAssignment((Assignment)paramAstNode); 
                        if (paramAstNode instanceof UnaryExpression)
                          return transformUnary((UnaryExpression)paramAstNode); 
                        if (paramAstNode instanceof XmlMemberGet)
                          return transformXmlMemberGet((XmlMemberGet)paramAstNode); 
                        if (paramAstNode instanceof InfixExpression)
                          return transformInfix((InfixExpression)paramAstNode); 
                        if (paramAstNode instanceof VariableDeclaration)
                          return transformVariables((VariableDeclaration)paramAstNode); 
                        if (paramAstNode instanceof ParenthesizedExpression)
                          return transformParenExpr((ParenthesizedExpression)paramAstNode); 
                        if (paramAstNode instanceof LabeledStatement)
                          return transformLabeledStatement((LabeledStatement)paramAstNode); 
                        if (paramAstNode instanceof LetNode)
                          return transformLetNode((LetNode)paramAstNode); 
                        if (paramAstNode instanceof XmlRef)
                          return transformXmlRef((XmlRef)paramAstNode); 
                        if (paramAstNode instanceof XmlLiteral)
                          return transformXmlLiteral((XmlLiteral)paramAstNode); 
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Can't transform: ");
                        stringBuilder.append(paramAstNode);
                        throw new IllegalArgumentException(stringBuilder.toString());
                      case 122:
                        return transformContinue((ContinueStatement)paramAstNode);
                      case 121:
                        return transformBreak((BreakStatement)paramAstNode);
                      case 120:
                        return (paramAstNode instanceof ForInLoop) ? transformForInLoop((ForInLoop)paramAstNode) : transformForLoop((ForLoop)paramAstNode);
                      case 119:
                        return transformDoLoop((DoLoop)paramAstNode);
                      case 118:
                        break;
                    } 
                    return transformWhileLoop((WhileLoop)paramAstNode);
                  case 41:
                    return transformString((StringLiteral)paramAstNode);
                  case 40:
                    return transformNumber((NumberLiteral)paramAstNode);
                  case 39:
                    return transformName((Name)paramAstNode);
                  case 38:
                    return transformFunctionCall((FunctionCall)paramAstNode);
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                    break;
                } 
              case 163:
                return transformGenExpr((GeneratorExpression)paramAstNode);
              case 161:
                return transformLiteral(paramAstNode);
              case 158:
                return transformArrayComp((ArrayComprehension)paramAstNode);
              case 137:
                return transformScript((ScriptNode)paramAstNode);
              case 124:
                return transformWith((WithStatement)paramAstNode);
              case 115:
                return transformSwitch((SwitchStatement)paramAstNode);
              case 113:
                return transformIf((IfStatement)paramAstNode);
              case 110:
                return transformFunction((FunctionNode)paramAstNode);
              case 103:
                return transformCondExpr((ConditionalExpression)paramAstNode);
              case 82:
                return transformTry((TryStatement)paramAstNode);
              case 73:
                return transformYield((Yield)paramAstNode);
              case 50:
                return transformThrow((ThrowStatement)paramAstNode);
              case 48:
                return transformRegExp((RegExpLiteral)paramAstNode);
              case 36:
                return transformElementGet((ElementGet)paramAstNode);
              case 33:
                return transformPropertyGet((PropertyGet)paramAstNode);
              case 30:
                return transformNewExpr((NewExpression)paramAstNode);
              case 4:
                break;
            } 
            return transformReturn((ReturnStatement)paramAstNode);
          } 
          return transformBlock(paramAstNode);
        } 
        return (Node)paramAstNode;
      } 
      return transformObjectLiteral((ObjectLiteral)paramAstNode);
    } 
    return transformArrayLiteral((ArrayLiteral)paramAstNode);
  }
  
  public ScriptNode transformTree(AstRoot paramAstRoot) {
    this.currentScriptOrFn = (ScriptNode)paramAstRoot;
    this.inUseStrictDirective = paramAstRoot.isInStrictMode();
    int i = this.decompiler.getCurrentOffset();
    ScriptNode scriptNode = (ScriptNode)transform((AstNode)paramAstRoot);
    scriptNode.setEncodedSourceBounds(i, this.decompiler.getCurrentOffset());
    if (this.compilerEnv.isGeneratingSource())
      scriptNode.setEncodedSource(this.decompiler.getEncodedSource()); 
    this.decompiler = null;
    return scriptNode;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/IRFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */