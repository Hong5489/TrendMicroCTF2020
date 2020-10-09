package com.trendmicro.hippo.optimizer;

import com.trendmicro.classfile.ClassFileWriter;
import com.trendmicro.hippo.CompilerEnvirons;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.Token;
import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.Jump;
import com.trendmicro.hippo.ast.ScriptNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

class BodyCodegen {
  private static final int ECMAERROR_EXCEPTION = 2;
  
  private static final int EVALUATOR_EXCEPTION = 1;
  
  private static final int EXCEPTION_MAX = 5;
  
  private static final int FINALLY_EXCEPTION = 4;
  
  static final int GENERATOR_START = 0;
  
  static final int GENERATOR_TERMINATE = -1;
  
  static final int GENERATOR_YIELD_START = 1;
  
  private static final int JAVASCRIPT_EXCEPTION = 0;
  
  private static final int MAX_LOCALS = 1024;
  
  private static final int THROWABLE_EXCEPTION = 3;
  
  private short argsLocal;
  
  ClassFileWriter cfw;
  
  Codegen codegen;
  
  CompilerEnvirons compilerEnv;
  
  private short contextLocal;
  
  private int enterAreaStartLabel;
  
  private int epilogueLabel;
  
  private ExceptionManager exceptionManager = new ExceptionManager();
  
  private Map<Node, FinallyReturnPoint> finallys;
  
  private short firstFreeLocal;
  
  private OptFunctionNode fnCurrent;
  
  private short funObjLocal;
  
  private short generatorStateLocal;
  
  private int generatorSwitch;
  
  private boolean hasVarsInRegs;
  
  private boolean inDirectCallFunction;
  
  private boolean inLocalBlock;
  
  private boolean isGenerator;
  
  private boolean itsForcedObjectParameters;
  
  private int itsLineNumber;
  
  private short itsOneArgArray;
  
  private short itsZeroArgArray;
  
  private List<Node> literals;
  
  private int[] locals;
  
  private short localsMax;
  
  private int maxLocals = 0;
  
  private int maxStack = 0;
  
  private short operationLocal;
  
  private short popvLocal;
  
  private int savedCodeOffset;
  
  ScriptNode scriptOrFn;
  
  public int scriptOrFnIndex;
  
  private short thisObjLocal;
  
  private short[] varRegisters;
  
  private short variableObjectLocal;
  
  private void addDoubleWrap() {
    addOptRuntimeInvoke("wrapDouble", "(D)Ljava/lang/Double;");
  }
  
  private void addGoto(Node paramNode, int paramInt) {
    int i = getTargetLabel(paramNode);
    this.cfw.add(paramInt, i);
  }
  
  private void addGotoWithReturn(Node paramNode) {
    FinallyReturnPoint finallyReturnPoint = this.finallys.get(paramNode);
    this.cfw.addLoadConstant(finallyReturnPoint.jsrPoints.size());
    addGoto(paramNode, 167);
    this.cfw.add(87);
    int i = this.cfw.acquireLabel();
    this.cfw.markLabel(i);
    finallyReturnPoint.jsrPoints.add(Integer.valueOf(i));
  }
  
  private void addInstructionCount() {
    addInstructionCount(Math.max(this.cfw.getCurrentCodeOffset() - this.savedCodeOffset, 1));
  }
  
  private void addInstructionCount(int paramInt) {
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addPush(paramInt);
    addScriptRuntimeInvoke("addInstructionCount", "(Lcom/trendmicro/hippo/Context;I)V");
  }
  
  private void addJumpedBooleanWrap(int paramInt1, int paramInt2) {
    this.cfw.markLabel(paramInt2);
    paramInt2 = this.cfw.acquireLabel();
    this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
    this.cfw.add(167, paramInt2);
    this.cfw.markLabel(paramInt1);
    this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
    this.cfw.markLabel(paramInt2);
    this.cfw.adjustStackTop(-1);
  }
  
  private void addLoadPropertyIds(Object[] paramArrayOfObject, int paramInt) {
    addNewObjectArray(paramInt);
    for (int i = 0; i != paramInt; i++) {
      this.cfw.add(89);
      this.cfw.addPush(i);
      Object object = paramArrayOfObject[i];
      if (object instanceof String) {
        this.cfw.addPush((String)object);
      } else {
        this.cfw.addPush(((Integer)object).intValue());
        addScriptRuntimeInvoke("wrapInt", "(I)Ljava/lang/Integer;");
      } 
      this.cfw.add(83);
    } 
  }
  
  private void addLoadPropertyValues(Node paramNode1, Node paramNode2, int paramInt) {
    if (this.isGenerator) {
      int i;
      for (i = 0; i != paramInt; i++) {
        int j = paramNode2.getType();
        if (j == 152 || j == 153 || j == 164) {
          generateExpression(paramNode2.getFirstChild(), paramNode1);
        } else {
          generateExpression(paramNode2, paramNode1);
        } 
        paramNode2 = paramNode2.getNext();
      } 
      addNewObjectArray(paramInt);
      for (i = 0; i != paramInt; i++) {
        this.cfw.add(90);
        this.cfw.add(95);
        this.cfw.addPush(paramInt - i - 1);
        this.cfw.add(95);
        this.cfw.add(83);
      } 
    } else {
      addNewObjectArray(paramInt);
      for (int i = 0; i != paramInt; i++) {
        this.cfw.add(89);
        this.cfw.addPush(i);
        int j = paramNode2.getType();
        if (j == 152 || j == 153 || j == 164) {
          generateExpression(paramNode2.getFirstChild(), paramNode1);
        } else {
          generateExpression(paramNode2, paramNode1);
        } 
        this.cfw.add(83);
        paramNode2 = paramNode2.getNext();
      } 
    } 
  }
  
  private void addNewObjectArray(int paramInt) {
    if (paramInt == 0) {
      paramInt = this.itsZeroArgArray;
      if (paramInt >= 0) {
        this.cfw.addALoad(paramInt);
      } else {
        this.cfw.add(178, "com/trendmicro/hippo/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
      } 
    } else {
      this.cfw.addPush(paramInt);
      this.cfw.add(189, "java/lang/Object");
    } 
  }
  
  private void addObjectToDouble() {
    addScriptRuntimeInvoke("toNumber", "(Ljava/lang/Object;)D");
  }
  
  private void addOptRuntimeInvoke(String paramString1, String paramString2) {
    this.cfw.addInvoke(184, "com/trendmicro/hippo/optimizer/OptRuntime", paramString1, paramString2);
  }
  
  private void addScriptRuntimeInvoke(String paramString1, String paramString2) {
    this.cfw.addInvoke(184, "com.trendmicro.hippo.ScriptRuntime", paramString1, paramString2);
  }
  
  private void dcpLoadAsNumber(int paramInt) {
    this.cfw.addALoad(paramInt);
    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
    int i = this.cfw.acquireLabel();
    this.cfw.add(165, i);
    short s = this.cfw.getStackTop();
    this.cfw.addALoad(paramInt);
    addObjectToDouble();
    int j = this.cfw.acquireLabel();
    this.cfw.add(167, j);
    this.cfw.markLabel(i, s);
    this.cfw.addDLoad(paramInt + 1);
    this.cfw.markLabel(j);
  }
  
  private void dcpLoadAsObject(int paramInt) {
    this.cfw.addALoad(paramInt);
    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
    int i = this.cfw.acquireLabel();
    this.cfw.add(165, i);
    short s = this.cfw.getStackTop();
    this.cfw.addALoad(paramInt);
    int j = this.cfw.acquireLabel();
    this.cfw.add(167, j);
    this.cfw.markLabel(i, s);
    this.cfw.addDLoad(paramInt + 1);
    addDoubleWrap();
    this.cfw.markLabel(j);
  }
  
  private void decReferenceWordLocal(short paramShort) {
    int[] arrayOfInt = this.locals;
    arrayOfInt[paramShort] = arrayOfInt[paramShort] - 1;
  }
  
  private String exceptionTypeToName(int paramInt) {
    if (paramInt == 0)
      return "com/trendmicro/hippo/JavaScriptException"; 
    if (paramInt == 1)
      return "com/trendmicro/hippo/EvaluatorException"; 
    if (paramInt == 2)
      return "com/trendmicro/hippo/EcmaError"; 
    if (paramInt == 3)
      return "java/lang/Throwable"; 
    if (paramInt == 4)
      return null; 
    throw Kit.codeBug();
  }
  
  private void genSimpleCompare(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 != -1) {
      switch (paramInt1) {
        default:
          throw Codegen.badTree();
        case 17:
          this.cfw.add(151);
          this.cfw.add(156, paramInt2);
          break;
        case 16:
          this.cfw.add(151);
          this.cfw.add(157, paramInt2);
          break;
        case 15:
          this.cfw.add(152);
          this.cfw.add(158, paramInt2);
          break;
        case 14:
          this.cfw.add(152);
          this.cfw.add(155, paramInt2);
          break;
      } 
      if (paramInt3 != -1)
        this.cfw.add(167, paramInt3); 
      return;
    } 
    throw Codegen.badTree();
  }
  
  private void generateActivationExit() {
    if (this.fnCurrent != null && !this.hasVarsInRegs) {
      this.cfw.addALoad(this.contextLocal);
      addScriptRuntimeInvoke("exitActivationFunction", "(Lcom/trendmicro/hippo/Context;)V");
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void generateArrayLiteralFactory(Node paramNode, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.codegen.getBodyMethodName(this.scriptOrFn));
    stringBuilder.append("_literal");
    stringBuilder.append(paramInt);
    String str = stringBuilder.toString();
    initBodyGeneration();
    short s = this.firstFreeLocal;
    paramInt = (short)(s + 1);
    this.firstFreeLocal = (short)paramInt;
    this.argsLocal = (short)s;
    this.localsMax = (short)paramInt;
    this.cfw.startMethod(str, "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;", (short)2);
    visitArrayLiteral(paramNode, paramNode.getFirstChild(), true);
    this.cfw.add(176);
    this.cfw.stopMethod((short)(this.localsMax + 1));
  }
  
  private void generateCallArgArray(Node paramNode1, Node paramNode2, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aload_2
    //   4: astore #5
    //   6: aload #5
    //   8: ifnull -> 24
    //   11: iinc #4, 1
    //   14: aload #5
    //   16: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   19: astore #5
    //   21: goto -> 6
    //   24: iload #4
    //   26: iconst_1
    //   27: if_icmpne -> 53
    //   30: aload_0
    //   31: getfield itsOneArgArray : S
    //   34: istore #6
    //   36: iload #6
    //   38: iflt -> 53
    //   41: aload_0
    //   42: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   45: iload #6
    //   47: invokevirtual addALoad : (I)V
    //   50: goto -> 59
    //   53: aload_0
    //   54: iload #4
    //   56: invokespecial addNewObjectArray : (I)V
    //   59: iconst_0
    //   60: istore #6
    //   62: iload #6
    //   64: iload #4
    //   66: if_icmpeq -> 236
    //   69: aload_0
    //   70: getfield isGenerator : Z
    //   73: ifne -> 94
    //   76: aload_0
    //   77: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   80: bipush #89
    //   82: invokevirtual add : (I)V
    //   85: aload_0
    //   86: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   89: iload #6
    //   91: invokevirtual addPush : (I)V
    //   94: iload_3
    //   95: ifne -> 107
    //   98: aload_0
    //   99: aload_2
    //   100: aload_1
    //   101: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   104: goto -> 148
    //   107: aload_0
    //   108: aload_2
    //   109: invokespecial nodeIsDirectCallParameter : (Lcom/trendmicro/hippo/Node;)I
    //   112: istore #7
    //   114: iload #7
    //   116: iflt -> 128
    //   119: aload_0
    //   120: iload #7
    //   122: invokespecial dcpLoadAsObject : (I)V
    //   125: goto -> 148
    //   128: aload_0
    //   129: aload_2
    //   130: aload_1
    //   131: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   134: aload_2
    //   135: bipush #8
    //   137: iconst_m1
    //   138: invokevirtual getIntProp : (II)I
    //   141: ifne -> 148
    //   144: aload_0
    //   145: invokespecial addDoubleWrap : ()V
    //   148: aload_0
    //   149: getfield isGenerator : Z
    //   152: ifeq -> 216
    //   155: aload_0
    //   156: invokespecial getNewWordLocal : ()S
    //   159: istore #8
    //   161: aload_0
    //   162: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   165: iload #8
    //   167: invokevirtual addAStore : (I)V
    //   170: aload_0
    //   171: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   174: sipush #192
    //   177: ldc_w '[Ljava/lang/Object;'
    //   180: invokevirtual add : (ILjava/lang/String;)V
    //   183: aload_0
    //   184: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   187: bipush #89
    //   189: invokevirtual add : (I)V
    //   192: aload_0
    //   193: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   196: iload #6
    //   198: invokevirtual addPush : (I)V
    //   201: aload_0
    //   202: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   205: iload #8
    //   207: invokevirtual addALoad : (I)V
    //   210: aload_0
    //   211: iload #8
    //   213: invokespecial releaseWordLocal : (S)V
    //   216: aload_0
    //   217: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   220: bipush #83
    //   222: invokevirtual add : (I)V
    //   225: aload_2
    //   226: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   229: astore_2
    //   230: iinc #6, 1
    //   233: goto -> 62
    //   236: return
  }
  
  private void generateCatchBlock(int paramInt1, short paramShort, int paramInt2, int paramInt3, int paramInt4) {
    paramInt1 = paramInt4;
    if (paramInt4 == 0)
      paramInt1 = this.cfw.acquireLabel(); 
    this.cfw.markHandler(paramInt1);
    this.cfw.addAStore(paramInt3);
    this.cfw.addALoad(paramShort);
    this.cfw.addAStore(this.variableObjectLocal);
    this.cfw.add(167, paramInt2);
  }
  
  private void generateCheckForThrowOrClose(int paramInt1, boolean paramBoolean, int paramInt2) {
    int i = this.cfw.acquireLabel();
    int j = this.cfw.acquireLabel();
    this.cfw.markLabel(i);
    this.cfw.addALoad(this.argsLocal);
    generateThrowJavaScriptException();
    this.cfw.markLabel(j);
    this.cfw.addALoad(this.argsLocal);
    this.cfw.add(192, "java/lang/Throwable");
    this.cfw.add(191);
    if (paramInt1 != -1)
      this.cfw.markLabel(paramInt1); 
    if (!paramBoolean)
      this.cfw.markTableSwitchCase(this.generatorSwitch, paramInt2); 
    this.cfw.addILoad(this.operationLocal);
    this.cfw.addLoadConstant(2);
    this.cfw.add(159, j);
    this.cfw.addILoad(this.operationLocal);
    this.cfw.addLoadConstant(1);
    this.cfw.add(159, i);
  }
  
  private void generateEpilogue() {
    if (this.compilerEnv.isGenerateObserverCount())
      addInstructionCount(); 
    if (this.isGenerator) {
      Map map = ((FunctionNode)this.scriptOrFn).getLiveLocals();
      if (map != null) {
        List<Node> list = ((FunctionNode)this.scriptOrFn).getResumptionPoints();
        for (byte b = 0; b < list.size(); b++) {
          Node node = list.get(b);
          int[] arrayOfInt = (int[])map.get(node);
          if (arrayOfInt != null) {
            this.cfw.markTableSwitchCase(this.generatorSwitch, getNextGeneratorState(node));
            generateGetGeneratorLocalsState();
            for (byte b1 = 0; b1 < arrayOfInt.length; b1++) {
              this.cfw.add(89);
              this.cfw.addLoadConstant(b1);
              this.cfw.add(50);
              this.cfw.addAStore(arrayOfInt[b1]);
            } 
            this.cfw.add(87);
            this.cfw.add(167, getTargetLabel(node));
          } 
        } 
      } 
      Map<Node, FinallyReturnPoint> map1 = this.finallys;
      if (map1 != null)
        for (Node node : map1.keySet()) {
          if (node.getType() == 126) {
            FinallyReturnPoint finallyReturnPoint = this.finallys.get(node);
            this.cfw.markLabel(finallyReturnPoint.tableLabel, (short)1);
            int j = this.cfw.addTableSwitch(0, finallyReturnPoint.jsrPoints.size() - 1);
            byte b2 = 0;
            this.cfw.markTableSwitchDefault(j);
            for (byte b1 = 0; b1 < finallyReturnPoint.jsrPoints.size(); b1++) {
              this.cfw.markTableSwitchCase(j, b2);
              this.cfw.add(167, ((Integer)finallyReturnPoint.jsrPoints.get(b1)).intValue());
              b2++;
            } 
          } 
        }  
    } 
    int i = this.epilogueLabel;
    if (i != -1)
      this.cfw.markLabel(i); 
    if (this.hasVarsInRegs) {
      this.cfw.add(176);
      return;
    } 
    if (this.isGenerator) {
      if (((FunctionNode)this.scriptOrFn).getResumptionPoints() != null)
        this.cfw.markTableSwitchDefault(this.generatorSwitch); 
      generateSetGeneratorResumptionPoint(-1);
      this.cfw.addALoad(this.variableObjectLocal);
      addOptRuntimeInvoke("throwStopIteration", "(Ljava/lang/Object;)V");
      Codegen.pushUndefined(this.cfw);
      this.cfw.add(176);
    } else if (this.fnCurrent == null) {
      this.cfw.addALoad(this.popvLocal);
      this.cfw.add(176);
    } else {
      generateActivationExit();
      this.cfw.add(176);
      i = this.cfw.acquireLabel();
      this.cfw.markHandler(i);
      short s = getNewWordLocal();
      this.cfw.addAStore(s);
      generateActivationExit();
      this.cfw.addALoad(s);
      releaseWordLocal(s);
      this.cfw.add(191);
      this.cfw.addExceptionHandler(this.enterAreaStartLabel, this.epilogueLabel, i, null);
    } 
  }
  
  private void generateExpression(Node paramNode1, Node paramNode2) {
    OptFunctionNode optFunctionNode;
    int i = paramNode1.getType();
    Node node = paramNode1.getFirstChild();
    if (i != 90) {
      if (i != 103) {
        boolean bool = true;
        if (i != 110) {
          if (i != 127) {
            if (i != 143)
              if (i != 147) {
                if (i != 160) {
                  String str;
                  if (i != 150) {
                    if (i != 151) {
                      StringBuilder stringBuilder;
                      String str1;
                      OptFunctionNode optFunctionNode1;
                      int k;
                      double d;
                      int m;
                      Node node2 = node;
                      switch (i) {
                        default:
                          switch (i) {
                            default:
                              switch (i) {
                                default:
                                  switch (i) {
                                    default:
                                      switch (i) {
                                        default:
                                          switch (i) {
                                            default:
                                              switch (i) {
                                                default:
                                                  stringBuilder = new StringBuilder();
                                                  stringBuilder.append("Unexpected node type ");
                                                  stringBuilder.append(i);
                                                  throw new RuntimeException(stringBuilder.toString());
                                                case 158:
                                                  paramNode2 = node.getNext();
                                                  generateStatement(node);
                                                  generateExpression(paramNode2, (Node)stringBuilder);
                                                case 157:
                                                  visitSetConstVar((Node)stringBuilder, node, true);
                                                case 156:
                                                  break;
                                              } 
                                              visitSetConst((Node)stringBuilder, node);
                                            case 139:
                                              return;
                                            case 138:
                                              visitTypeofname((Node)stringBuilder);
                                            case 141:
                                            case 140:
                                              break;
                                          } 
                                          break;
                                        case 107:
                                        case 108:
                                          visitIncDec((Node)stringBuilder);
                                        case 105:
                                        case 106:
                                          generateExpression(node, (Node)stringBuilder);
                                          this.cfw.add(89);
                                          addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                                          k = this.cfw.acquireLabel();
                                          if (i == 106) {
                                            this.cfw.add(153, k);
                                          } else {
                                            this.cfw.add(154, k);
                                          } 
                                          this.cfw.add(87);
                                          generateExpression(node.getNext(), (Node)stringBuilder);
                                          this.cfw.markLabel(k);
                                      } 
                                    case 78:
                                    case 79:
                                    case 80:
                                    case 81:
                                      k = stringBuilder.getIntProp(16, 0);
                                      do {
                                        generateExpression(node, (Node)stringBuilder);
                                        paramNode2 = node.getNext();
                                        node = paramNode2;
                                      } while (paramNode2 != null);
                                      this.cfw.addALoad(this.contextLocal);
                                      switch (i) {
                                        default:
                                          throw Kit.codeBug();
                                        case 81:
                                          str = "nameRef";
                                          str1 = "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Lcom/trendmicro/hippo/Ref;";
                                          this.cfw.addALoad(this.variableObjectLocal);
                                          break;
                                        case 80:
                                          str = "nameRef";
                                          str1 = "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Lcom/trendmicro/hippo/Ref;";
                                          this.cfw.addALoad(this.variableObjectLocal);
                                          break;
                                        case 79:
                                          str = "memberRef";
                                          str1 = "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;I)Lcom/trendmicro/hippo/Ref;";
                                          break;
                                        case 78:
                                          str = "memberRef";
                                          str1 = "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;I)Lcom/trendmicro/hippo/Ref;";
                                          break;
                                      } 
                                      this.cfw.addPush(k);
                                      addScriptRuntimeInvoke(str, str1);
                                    case 77:
                                      generateExpression(node, (Node)str);
                                      this.cfw.addALoad(this.contextLocal);
                                      addScriptRuntimeInvoke("escapeTextValue", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/String;");
                                    case 76:
                                      generateExpression(node, (Node)str);
                                      this.cfw.addALoad(this.contextLocal);
                                      addScriptRuntimeInvoke("escapeAttributeValue", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/String;");
                                    case 75:
                                      generateExpression(node, (Node)str);
                                      this.cfw.addALoad(this.contextLocal);
                                      addScriptRuntimeInvoke("setDefaultNamespace", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
                                    case 74:
                                      visitStrictSetName((Node)str, node);
                                    case 73:
                                      generateYieldPoint((Node)str, true);
                                    case 72:
                                      str1 = (String)str.getProp(17);
                                      generateExpression(node, (Node)str);
                                      this.cfw.addPush(str1);
                                      this.cfw.addALoad(this.contextLocal);
                                      this.cfw.addALoad(this.variableObjectLocal);
                                      addScriptRuntimeInvoke("specialRef", "(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Ref;");
                                    case 71:
                                      generateFunctionAndThisObj(node, (Node)str);
                                      generateCallArgArray((Node)str, node.getNext(), false);
                                      this.cfw.addALoad(this.contextLocal);
                                      addScriptRuntimeInvoke("callRef", "(Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Ref;");
                                    case 70:
                                      generateExpression(node, (Node)str);
                                      this.cfw.addALoad(this.contextLocal);
                                      addScriptRuntimeInvoke("refDel", "(Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
                                    case 68:
                                      generateExpression(node, (Node)str);
                                      this.cfw.addALoad(this.contextLocal);
                                      addScriptRuntimeInvoke("refGet", "(Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
                                    case 67:
                                      visitObjectLiteral((Node)str, node, false);
                                    case 66:
                                      visitArrayLiteral((Node)str, node, false);
                                    case 69:
                                      break;
                                  } 
                                  break;
                                case 64:
                                  this.cfw.add(42);
                                case 62:
                                case 63:
                                  k = getLocalBlockRegister((Node)str);
                                  this.cfw.addALoad(k);
                                  if (i == 62)
                                    addScriptRuntimeInvoke("enumNext", "(Ljava/lang/Object;)Ljava/lang/Boolean;"); 
                                  this.cfw.addALoad(this.contextLocal);
                                  addScriptRuntimeInvoke("enumId", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
                              } 
                            case 56:
                              visitSetVar((Node)str, node, true);
                            case 55:
                              visitGetVar((Node)str);
                            case 54:
                              this.cfw.addALoad(getLocalBlockRegister((Node)str));
                            case 52:
                            case 53:
                              break;
                          } 
                        case 49:
                          while (node2 != null) {
                            generateExpression(node2, (Node)str);
                            node2 = node2.getNext();
                          } 
                          this.cfw.addALoad(this.contextLocal);
                          this.cfw.addALoad(this.variableObjectLocal);
                          this.cfw.addPush(str.getString());
                          addScriptRuntimeInvoke("bind", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Lcom/trendmicro/hippo/Scriptable;");
                        case 48:
                          this.cfw.addALoad(this.contextLocal);
                          this.cfw.addALoad(this.variableObjectLocal);
                          i = str.getExistingIntProp(4);
                          this.cfw.add(178, this.codegen.mainClassName, this.codegen.getCompiledRegexpName(this.scriptOrFn, i), "Ljava/lang/Object;");
                          this.cfw.addInvoke(184, "com/trendmicro/hippo/ScriptRuntime", "wrapRegExp", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
                        case 45:
                          this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
                        case 44:
                          this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
                        case 43:
                          this.cfw.addALoad(this.thisObjLocal);
                        case 42:
                          this.cfw.add(1);
                        case 41:
                          this.cfw.addPush(str.getString());
                        case 40:
                          d = str.getDouble();
                          if (str.getIntProp(8, -1) != -1)
                            this.cfw.addPush(d); 
                          this.codegen.pushNumberAsObject(this.cfw, d);
                        case 39:
                          this.cfw.addALoad(this.contextLocal);
                          this.cfw.addALoad(this.variableObjectLocal);
                          this.cfw.addPush(str.getString());
                          addScriptRuntimeInvoke("name", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
                        case 37:
                          visitSetElem(i, (Node)str, node);
                        case 36:
                          generateExpression(node, (Node)str);
                          generateExpression(node.getNext(), (Node)str);
                          this.cfw.addALoad(this.contextLocal);
                          if (str.getIntProp(8, -1) != -1)
                            addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLcom/trendmicro/hippo/Context;)Ljava/lang/Object;"); 
                          this.cfw.addALoad(this.variableObjectLocal);
                          addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
                        case 35:
                          visitSetProp(i, (Node)str, node);
                        case 33:
                        case 34:
                          visitGetProp((Node)str, node);
                        case 32:
                          generateExpression(node, (Node)str);
                          addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                        case 31:
                          if (node.getType() != 49)
                            bool = false; 
                          generateExpression(node, (Node)str);
                          generateExpression(node.getNext(), (Node)str);
                          this.cfw.addALoad(this.contextLocal);
                          this.cfw.addPush(bool);
                          addScriptRuntimeInvoke("delete", "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Z)Ljava/lang/Object;");
                        case 30:
                        case 38:
                          k = str.getIntProp(10, 0);
                          if (k == 0) {
                            optFunctionNode1 = (OptFunctionNode)str.getProp(9);
                            if (optFunctionNode1 != null)
                              visitOptimizedCall((Node)str, optFunctionNode1, i, node); 
                            if (i == 38)
                              visitStandardCall((Node)str, node); 
                            visitStandardNew((Node)str, node);
                          } 
                          visitSpecialCall((Node)str, i, k, node);
                        case 28:
                        case 29:
                          generateExpression(node, (Node)str);
                          addObjectToDouble();
                          if (i == 29)
                            this.cfw.add(119); 
                          addDoubleWrap();
                        case 27:
                          generateExpression(node, (Node)str);
                          addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
                          this.cfw.addPush(-1);
                          this.cfw.add(130);
                          this.cfw.add(135);
                          addDoubleWrap();
                        case 26:
                          m = this.cfw.acquireLabel();
                          i = this.cfw.acquireLabel();
                          k = this.cfw.acquireLabel();
                          generateIfJump(node, (Node)str, m, i);
                          this.cfw.markLabel(m);
                          this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
                          this.cfw.add(167, k);
                          this.cfw.markLabel(i);
                          this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
                          this.cfw.markLabel(k);
                          this.cfw.adjustStackTop(-1);
                        case 24:
                        case 25:
                          if (i == 24) {
                            i = 111;
                          } else {
                            i = 115;
                          } 
                          visitArithmetic((Node)str, i, node, (Node)optFunctionNode1);
                        case 23:
                          visitArithmetic((Node)str, 107, node, (Node)optFunctionNode1);
                        case 22:
                          visitArithmetic((Node)str, 103, node, (Node)optFunctionNode1);
                        case 21:
                          generateExpression(node, (Node)str);
                          generateExpression(node.getNext(), (Node)str);
                          i = str.getIntProp(8, -1);
                          if (i != 0) {
                            if (i != 1) {
                              if (i != 2) {
                                if (node.getType() == 41)
                                  addScriptRuntimeInvoke("add", "(Ljava/lang/CharSequence;Ljava/lang/Object;)Ljava/lang/CharSequence;"); 
                                if (node.getNext().getType() == 41)
                                  addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/CharSequence;)Ljava/lang/CharSequence;"); 
                                this.cfw.addALoad(this.contextLocal);
                                addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
                              } 
                              addOptRuntimeInvoke("add", "(Ljava/lang/Object;D)Ljava/lang/Object;");
                            } 
                            addOptRuntimeInvoke("add", "(DLjava/lang/Object;)Ljava/lang/Object;");
                          } 
                          this.cfw.add(99);
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                          k = this.cfw.acquireLabel();
                          i = this.cfw.acquireLabel();
                          visitIfJumpRelOp((Node)str, node, k, i);
                          addJumpedBooleanWrap(k, i);
                        case 12:
                        case 13:
                        case 46:
                        case 47:
                          k = this.cfw.acquireLabel();
                          i = this.cfw.acquireLabel();
                          visitIfJumpEqOp((Node)str, node, k, i);
                          addJumpedBooleanWrap(k, i);
                        case 9:
                        case 10:
                        case 11:
                        case 18:
                        case 19:
                        case 20:
                          visitBitOp((Node)str, i, node);
                        case 8:
                          visitSetName((Node)str, node);
                      } 
                    } else {
                      generateExpression(node, (Node)str);
                      addObjectToDouble();
                    } 
                  } else {
                    i = -1;
                    if (node.getType() == 40)
                      i = node.getIntProp(8, -1); 
                    if (i != -1) {
                      node.removeProp(8);
                      generateExpression(node, (Node)str);
                      node.putIntProp(8, i);
                    } 
                    generateExpression(node, (Node)str);
                    addDoubleWrap();
                  } 
                } else {
                  paramNode2 = node.getNext();
                  paramNode1 = paramNode2.getNext();
                  generateStatement(node);
                  generateExpression(paramNode2.getFirstChild(), paramNode2);
                  generateStatement(paramNode1);
                } 
              } else {
                visitDotQuery(paramNode1, node);
              }  
            generateExpression(node, paramNode1);
            paramNode2 = node.getNext();
            if (i == 143) {
              this.cfw.add(89);
              this.cfw.addALoad(this.contextLocal);
              addScriptRuntimeInvoke("refGet", "(Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
            } 
            generateExpression(paramNode2, paramNode1);
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            addScriptRuntimeInvoke("refSet", "(Lcom/trendmicro/hippo/Ref;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
          } 
          generateExpression(node, paramNode1);
          this.cfw.add(87);
          Codegen.pushUndefined(this.cfw);
        } 
        if (this.fnCurrent != null || paramNode2.getType() != 137) {
          i = paramNode1.getExistingIntProp(1);
          optFunctionNode = OptFunctionNode.get(this.scriptOrFn, i);
          i = optFunctionNode.fnode.getFunctionType();
          if (i == 2 || i == 4)
            visitFunction(optFunctionNode, i); 
          throw Codegen.badTree();
        } 
      } 
      paramNode2 = node.getNext();
      Node node1 = paramNode2.getNext();
      generateExpression(node, (Node)optFunctionNode);
      addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
      int j = this.cfw.acquireLabel();
      this.cfw.add(153, j);
      short s = this.cfw.getStackTop();
      generateExpression(paramNode2, (Node)optFunctionNode);
      i = this.cfw.acquireLabel();
      this.cfw.add(167, i);
      this.cfw.markLabel(j, s);
      generateExpression(node1, (Node)optFunctionNode);
      this.cfw.markLabel(i);
    } 
    for (paramNode2 = node.getNext(); paramNode2 != null; paramNode2 = paramNode2.getNext()) {
      generateExpression(node, (Node)optFunctionNode);
      this.cfw.add(87);
      node = paramNode2;
    } 
    generateExpression(node, (Node)optFunctionNode);
  }
  
  private void generateFunctionAndThisObj(Node paramNode1, Node paramNode2) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getType : ()I
    //   4: istore_3
    //   5: aload_1
    //   6: invokevirtual getType : ()I
    //   9: istore #4
    //   11: iload #4
    //   13: bipush #33
    //   15: if_icmpeq -> 121
    //   18: iload #4
    //   20: bipush #34
    //   22: if_icmpeq -> 117
    //   25: iload #4
    //   27: bipush #36
    //   29: if_icmpeq -> 121
    //   32: iload #4
    //   34: bipush #39
    //   36: if_icmpeq -> 69
    //   39: aload_0
    //   40: aload_1
    //   41: aload_2
    //   42: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   45: aload_0
    //   46: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   49: aload_0
    //   50: getfield contextLocal : S
    //   53: invokevirtual addALoad : (I)V
    //   56: aload_0
    //   57: ldc_w 'getValueFunctionAndThis'
    //   60: ldc_w '(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Callable;'
    //   63: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   66: goto -> 244
    //   69: aload_1
    //   70: invokevirtual getString : ()Ljava/lang/String;
    //   73: astore_1
    //   74: aload_0
    //   75: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   78: aload_1
    //   79: invokevirtual addPush : (Ljava/lang/String;)V
    //   82: aload_0
    //   83: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   86: aload_0
    //   87: getfield contextLocal : S
    //   90: invokevirtual addALoad : (I)V
    //   93: aload_0
    //   94: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   97: aload_0
    //   98: getfield variableObjectLocal : S
    //   101: invokevirtual addALoad : (I)V
    //   104: aload_0
    //   105: ldc_w 'getNameFunctionAndThis'
    //   108: ldc_w '(Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;'
    //   111: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   114: goto -> 244
    //   117: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   120: athrow
    //   121: aload_1
    //   122: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   125: astore_2
    //   126: aload_0
    //   127: aload_2
    //   128: aload_1
    //   129: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   132: aload_2
    //   133: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   136: astore_2
    //   137: iload_3
    //   138: bipush #33
    //   140: if_icmpne -> 191
    //   143: aload_2
    //   144: invokevirtual getString : ()Ljava/lang/String;
    //   147: astore_1
    //   148: aload_0
    //   149: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   152: aload_1
    //   153: invokevirtual addPush : (Ljava/lang/String;)V
    //   156: aload_0
    //   157: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   160: aload_0
    //   161: getfield contextLocal : S
    //   164: invokevirtual addALoad : (I)V
    //   167: aload_0
    //   168: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   171: aload_0
    //   172: getfield variableObjectLocal : S
    //   175: invokevirtual addALoad : (I)V
    //   178: aload_0
    //   179: ldc_w 'getPropFunctionAndThis'
    //   182: ldc_w '(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;'
    //   185: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   188: goto -> 244
    //   191: aload_0
    //   192: aload_2
    //   193: aload_1
    //   194: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   197: aload_1
    //   198: bipush #8
    //   200: iconst_m1
    //   201: invokevirtual getIntProp : (II)I
    //   204: iconst_m1
    //   205: if_icmpeq -> 212
    //   208: aload_0
    //   209: invokespecial addDoubleWrap : ()V
    //   212: aload_0
    //   213: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   216: aload_0
    //   217: getfield contextLocal : S
    //   220: invokevirtual addALoad : (I)V
    //   223: aload_0
    //   224: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   227: aload_0
    //   228: getfield variableObjectLocal : S
    //   231: invokevirtual addALoad : (I)V
    //   234: aload_0
    //   235: ldc_w 'getElemFunctionAndThis'
    //   238: ldc_w '(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Callable;'
    //   241: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   244: aload_0
    //   245: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   248: aload_0
    //   249: getfield contextLocal : S
    //   252: invokevirtual addALoad : (I)V
    //   255: aload_0
    //   256: ldc_w 'lastStoredScriptable'
    //   259: ldc_w '(Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/Scriptable;'
    //   262: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   265: return
  }
  
  private void generateGenerator() {
    this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short)10);
    initBodyGeneration();
    short s1 = this.firstFreeLocal;
    short s2 = (short)(s1 + 1);
    this.firstFreeLocal = (short)s2;
    this.argsLocal = (short)s1;
    this.localsMax = (short)s2;
    if (this.fnCurrent != null) {
      this.cfw.addALoad(this.funObjLocal);
      this.cfw.addInvoke(185, "com/trendmicro/hippo/Scriptable", "getParentScope", "()Lcom/trendmicro/hippo/Scriptable;");
      this.cfw.addAStore(this.variableObjectLocal);
    } 
    this.cfw.addALoad(this.funObjLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addALoad(this.argsLocal);
    this.cfw.addPush(this.scriptOrFn.isInStrictMode());
    addScriptRuntimeInvoke("createFunctionActivation", "(Lcom/trendmicro/hippo/NativeFunction;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Z)Lcom/trendmicro/hippo/Scriptable;");
    this.cfw.addAStore(this.variableObjectLocal);
    this.cfw.add(187, this.codegen.mainClassName);
    this.cfw.add(89);
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addPush(this.scriptOrFnIndex);
    this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;I)V");
    generateNestedFunctionInits();
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addALoad(this.thisObjLocal);
    this.cfw.addLoadConstant(this.maxLocals);
    this.cfw.addLoadConstant(this.maxStack);
    addOptRuntimeInvoke("createNativeGenerator", "(Lcom/trendmicro/hippo/NativeFunction;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;II)Lcom/trendmicro/hippo/Scriptable;");
    this.cfw.add(176);
    this.cfw.stopMethod((short)(this.localsMax + 1));
  }
  
  private void generateGetGeneratorLocalsState() {
    this.cfw.addALoad(this.generatorStateLocal);
    addOptRuntimeInvoke("getGeneratorLocalsState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
  }
  
  private void generateGetGeneratorResumptionPoint() {
    this.cfw.addALoad(this.generatorStateLocal);
    this.cfw.add(180, "com/trendmicro/hippo/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
  }
  
  private void generateGetGeneratorStackState() {
    this.cfw.addALoad(this.generatorStateLocal);
    addOptRuntimeInvoke("getGeneratorStackState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
  }
  
  private void generateIfJump(Node paramNode1, Node paramNode2, int paramInt1, int paramInt2) {
    int i = paramNode1.getType();
    Node node = paramNode1.getFirstChild();
    if (i != 26) {
      if (i != 46 && i != 47)
        if (i != 52 && i != 53) {
          if (i != 105 && i != 106) {
            switch (i) {
              default:
                generateExpression(paramNode1, paramNode2);
                addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                this.cfw.add(154, paramInt1);
                this.cfw.add(167, paramInt2);
                return;
              case 14:
              case 15:
              case 16:
              case 17:
                visitIfJumpRelOp(paramNode1, node, paramInt1, paramInt2);
                return;
              case 12:
              case 13:
                break;
            } 
          } else {
            int j = this.cfw.acquireLabel();
            if (i == 106) {
              generateIfJump(node, paramNode1, j, paramInt2);
            } else {
              generateIfJump(node, paramNode1, paramInt1, j);
            } 
            this.cfw.markLabel(j);
            generateIfJump(node.getNext(), paramNode1, paramInt1, paramInt2);
            return;
          } 
        } else {
        
        }  
      visitIfJumpEqOp(paramNode1, node, paramInt1, paramInt2);
    } else {
      generateIfJump(node, paramNode1, paramInt2, paramInt1);
    } 
  }
  
  private void generateIntegerUnwrap() {
    this.cfw.addInvoke(182, "java/lang/Integer", "intValue", "()I");
  }
  
  private void generateIntegerWrap() {
    this.cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
  }
  
  private void generateNestedFunctionInits() {
    int i = this.scriptOrFn.getFunctionCount();
    for (int j = 0; j != i; j++) {
      OptFunctionNode optFunctionNode = OptFunctionNode.get(this.scriptOrFn, j);
      if (optFunctionNode.fnode.getFunctionType() == 1)
        visitFunction(optFunctionNode, 1); 
    } 
  }
  
  private void generateObjectLiteralFactory(Node paramNode, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.codegen.getBodyMethodName(this.scriptOrFn));
    stringBuilder.append("_literal");
    stringBuilder.append(paramInt);
    String str = stringBuilder.toString();
    initBodyGeneration();
    paramInt = this.firstFreeLocal;
    short s = (short)(paramInt + 1);
    this.firstFreeLocal = (short)s;
    this.argsLocal = (short)paramInt;
    this.localsMax = (short)s;
    this.cfw.startMethod(str, "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;", (short)2);
    visitObjectLiteral(paramNode, paramNode.getFirstChild(), true);
    this.cfw.add(176);
    this.cfw.stopMethod((short)(this.localsMax + 1));
  }
  
  private void generatePrologue() {
    String str;
    if (this.inDirectCallFunction) {
      int i = this.scriptOrFn.getParamCount();
      if (this.firstFreeLocal != 4)
        Kit.codeBug(); 
      int j;
      for (j = 0; j != i; j++) {
        short[] arrayOfShort = this.varRegisters;
        short s = this.firstFreeLocal;
        arrayOfShort[j] = (short)s;
        this.firstFreeLocal = (short)(short)(s + 3);
      } 
      if (!this.fnCurrent.getParameterNumberContext()) {
        this.itsForcedObjectParameters = true;
        for (j = 0; j != i; j++) {
          short s = this.varRegisters[j];
          this.cfw.addALoad(s);
          this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
          int k = this.cfw.acquireLabel();
          this.cfw.add(166, k);
          this.cfw.addDLoad(s + 1);
          addDoubleWrap();
          this.cfw.addAStore(s);
          this.cfw.markLabel(k);
        } 
      } 
    } 
    if (this.fnCurrent != null) {
      this.cfw.addALoad(this.funObjLocal);
      this.cfw.addInvoke(185, "com/trendmicro/hippo/Scriptable", "getParentScope", "()Lcom/trendmicro/hippo/Scriptable;");
      this.cfw.addAStore(this.variableObjectLocal);
    } 
    short s1 = this.firstFreeLocal;
    short s2 = (short)(s1 + 1);
    this.firstFreeLocal = (short)s2;
    this.argsLocal = (short)s1;
    this.localsMax = (short)s2;
    if (this.isGenerator) {
      s1 = (short)(s2 + 1);
      this.firstFreeLocal = (short)s1;
      this.operationLocal = (short)s2;
      this.localsMax = (short)s1;
      this.cfw.addALoad(this.thisObjLocal);
      s2 = this.firstFreeLocal;
      s1 = (short)(s2 + 1);
      this.firstFreeLocal = (short)s1;
      this.generatorStateLocal = (short)s2;
      this.localsMax = (short)s1;
      this.cfw.add(192, "com/trendmicro/hippo/optimizer/OptRuntime$GeneratorState");
      this.cfw.add(89);
      this.cfw.addAStore(this.generatorStateLocal);
      this.cfw.add(180, "com/trendmicro/hippo/optimizer/OptRuntime$GeneratorState", "thisObj", "Lcom/trendmicro/hippo/Scriptable;");
      this.cfw.addAStore(this.thisObjLocal);
      if (this.epilogueLabel == -1)
        this.epilogueLabel = this.cfw.acquireLabel(); 
      List list = ((FunctionNode)this.scriptOrFn).getResumptionPoints();
      if (list != null) {
        generateGetGeneratorResumptionPoint();
        this.generatorSwitch = this.cfw.addTableSwitch(0, list.size() + 0);
        generateCheckForThrowOrClose(-1, false, 0);
      } 
    } 
    if (this.fnCurrent == null && this.scriptOrFn.getRegexpCount() != 0) {
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addInvoke(184, this.codegen.mainClassName, "_reInit", "(Lcom/trendmicro/hippo/Context;)V");
    } 
    if (this.compilerEnv.isGenerateObserverCount())
      saveCurrentCodeOffset(); 
    if (this.hasVarsInRegs) {
      int i = this.scriptOrFn.getParamCount();
      if (i > 0 && !this.inDirectCallFunction) {
        this.cfw.addALoad(this.argsLocal);
        this.cfw.add(190);
        this.cfw.addPush(i);
        int n = this.cfw.acquireLabel();
        this.cfw.add(162, n);
        this.cfw.addALoad(this.argsLocal);
        this.cfw.addPush(i);
        addScriptRuntimeInvoke("padArguments", "([Ljava/lang/Object;I)[Ljava/lang/Object;");
        this.cfw.addAStore(this.argsLocal);
        this.cfw.markLabel(n);
      } 
      int k = this.fnCurrent.fnode.getParamCount();
      int m = this.fnCurrent.fnode.getParamAndVarCount();
      boolean[] arrayOfBoolean = this.fnCurrent.fnode.getParamAndVarConst();
      i = -1;
      int j = 0;
      while (j != m) {
        int n;
        s2 = -1;
        if (j < k) {
          n = i;
          if (!this.inDirectCallFunction) {
            s2 = getNewWordLocal();
            this.cfw.addALoad(this.argsLocal);
            this.cfw.addPush(j);
            this.cfw.add(50);
            this.cfw.addAStore(s2);
            n = i;
          } 
        } else if (this.fnCurrent.isNumberVar(j)) {
          s2 = getNewWordPairLocal(arrayOfBoolean[j]);
          this.cfw.addPush(0.0D);
          this.cfw.addDStore(s2);
          n = i;
        } else {
          s2 = getNewWordLocal(arrayOfBoolean[j]);
          if (i == -1) {
            Codegen.pushUndefined(this.cfw);
            i = s2;
          } else {
            this.cfw.addALoad(i);
          } 
          this.cfw.addAStore(s2);
          n = i;
        } 
        if (s2 >= 0) {
          if (arrayOfBoolean[j]) {
            this.cfw.addPush(0);
            ClassFileWriter classFileWriter = this.cfw;
            if (this.fnCurrent.isNumberVar(j)) {
              i = 2;
            } else {
              i = 1;
            } 
            classFileWriter.addIStore(i + s2);
          } 
          this.varRegisters[j] = (short)s2;
        } 
        if (this.compilerEnv.isGenerateDebugInfo()) {
          String str1 = this.fnCurrent.fnode.getParamOrVarName(j);
          if (this.fnCurrent.isNumberVar(j)) {
            str = "D";
          } else {
            str = "Ljava/lang/Object;";
          } 
          int i1 = this.cfw.getCurrentCodeOffset();
          i = s2;
          if (s2 < 0)
            i = this.varRegisters[j]; 
          this.cfw.addVariableDescriptor(str1, str, i1, i);
        } 
        j++;
        i = n;
      } 
      return;
    } 
    if (this.isGenerator)
      return; 
    s2 = 0;
    ScriptNode scriptNode = this.scriptOrFn;
    if (scriptNode instanceof FunctionNode)
      if (((FunctionNode)scriptNode).getFunctionType() == 4) {
        s2 = 1;
      } else {
        s2 = 0;
      }  
    if (this.fnCurrent != null) {
      String str1 = "activation";
      this.cfw.addALoad(this.funObjLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addALoad(this.argsLocal);
      if (s2 != 0) {
        str = "createArrowFunctionActivation";
      } else {
        str = "createFunctionActivation";
      } 
      this.cfw.addPush(this.scriptOrFn.isInStrictMode());
      addScriptRuntimeInvoke(str, "(Lcom/trendmicro/hippo/NativeFunction;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Z)Lcom/trendmicro/hippo/Scriptable;");
      this.cfw.addAStore(this.variableObjectLocal);
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      addScriptRuntimeInvoke("enterActivationFunction", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)V");
      str = str1;
    } else {
      str = "global";
      this.cfw.addALoad(this.funObjLocal);
      this.cfw.addALoad(this.thisObjLocal);
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addPush(0);
      addScriptRuntimeInvoke("initScript", "(Lcom/trendmicro/hippo/NativeFunction;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Z)V");
    } 
    this.enterAreaStartLabel = this.cfw.acquireLabel();
    this.epilogueLabel = this.cfw.acquireLabel();
    this.cfw.markLabel(this.enterAreaStartLabel);
    generateNestedFunctionInits();
    if (this.compilerEnv.isGenerateDebugInfo()) {
      ClassFileWriter classFileWriter = this.cfw;
      classFileWriter.addVariableDescriptor(str, "Lcom/trendmicro/hippo/Scriptable;", classFileWriter.getCurrentCodeOffset(), this.variableObjectLocal);
    } 
    OptFunctionNode optFunctionNode = this.fnCurrent;
    if (optFunctionNode == null) {
      this.popvLocal = getNewWordLocal();
      Codegen.pushUndefined(this.cfw);
      this.cfw.addAStore(this.popvLocal);
      int i = this.scriptOrFn.getEndLineno();
      if (i != -1)
        this.cfw.addLineNumberEntry((short)i); 
    } else {
      if (optFunctionNode.itsContainsCalls0) {
        this.itsZeroArgArray = getNewWordLocal();
        this.cfw.add(178, "com/trendmicro/hippo/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
        this.cfw.addAStore(this.itsZeroArgArray);
      } 
      if (this.fnCurrent.itsContainsCalls1) {
        this.itsOneArgArray = getNewWordLocal();
        this.cfw.addPush(1);
        this.cfw.add(189, "java/lang/Object");
        this.cfw.addAStore(this.itsOneArgArray);
      } 
    } 
  }
  
  private boolean generateSaveLocals(Node paramNode) {
    int i = 0;
    int j = 0;
    while (j < this.firstFreeLocal) {
      int m = i;
      if (this.locals[j] != 0)
        m = i + 1; 
      j++;
      i = m;
    } 
    if (i == 0) {
      ((FunctionNode)this.scriptOrFn).addLiveLocals(paramNode, null);
      return false;
    } 
    j = this.maxLocals;
    if (j <= i)
      j = i; 
    this.maxLocals = j;
    int[] arrayOfInt = new int[i];
    int k = 0;
    j = 0;
    while (j < this.firstFreeLocal) {
      int m = k;
      if (this.locals[j] != 0) {
        arrayOfInt[k] = j;
        m = k + 1;
      } 
      j++;
      k = m;
    } 
    ((FunctionNode)this.scriptOrFn).addLiveLocals(paramNode, arrayOfInt);
    generateGetGeneratorLocalsState();
    for (j = 0; j < i; j++) {
      this.cfw.add(89);
      this.cfw.addLoadConstant(j);
      this.cfw.addALoad(arrayOfInt[j]);
      this.cfw.add(83);
    } 
    this.cfw.add(87);
    return true;
  }
  
  private void generateSetGeneratorResumptionPoint(int paramInt) {
    this.cfw.addALoad(this.generatorStateLocal);
    this.cfw.addLoadConstant(paramInt);
    this.cfw.add(181, "com/trendmicro/hippo/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
  }
  
  private void generateStatement(Node paramNode) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial updateLineNumber : (Lcom/trendmicro/hippo/Node;)V
    //   5: aload_1
    //   6: invokevirtual getType : ()I
    //   9: istore_2
    //   10: aload_1
    //   11: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   14: astore_3
    //   15: iload_2
    //   16: bipush #50
    //   18: if_icmpeq -> 1308
    //   21: iload_2
    //   22: bipush #51
    //   24: if_icmpeq -> 1269
    //   27: iload_2
    //   28: bipush #65
    //   30: if_icmpeq -> 1143
    //   33: iload_2
    //   34: bipush #82
    //   36: if_icmpeq -> 1131
    //   39: iconst_1
    //   40: istore #4
    //   42: iload_2
    //   43: bipush #110
    //   45: if_icmpeq -> 1076
    //   48: iload_2
    //   49: bipush #115
    //   51: if_icmpeq -> 1050
    //   54: iload_2
    //   55: bipush #124
    //   57: if_icmpeq -> 1014
    //   60: iload_2
    //   61: bipush #126
    //   63: if_icmpeq -> 829
    //   66: iload_2
    //   67: sipush #142
    //   70: if_icmpeq -> 743
    //   73: iload_2
    //   74: sipush #161
    //   77: if_icmpeq -> 740
    //   80: iload_2
    //   81: tableswitch default -> 120, 2 -> 680, 3 -> 637, 4 -> 1143, 5 -> 610, 6 -> 610, 7 -> 610
    //   120: iload_2
    //   121: tableswitch default -> 156, 57 -> 497, 58 -> 398, 59 -> 398, 60 -> 398, 61 -> 398
    //   156: iload_2
    //   157: tableswitch default -> 208, 129 -> 1014, 130 -> 1014, 131 -> 1014, 132 -> 351, 133 -> 1014, 134 -> 247, 135 -> 212, 136 -> 610, 137 -> 1014
    //   208: invokestatic badTree : ()Ljava/lang/RuntimeException;
    //   211: athrow
    //   212: aload_0
    //   213: aload_3
    //   214: aload_1
    //   215: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   218: aload_0
    //   219: getfield popvLocal : S
    //   222: ifge -> 233
    //   225: aload_0
    //   226: aload_0
    //   227: invokespecial getNewWordLocal : ()S
    //   230: putfield popvLocal : S
    //   233: aload_0
    //   234: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   237: aload_0
    //   238: getfield popvLocal : S
    //   241: invokevirtual addAStore : (I)V
    //   244: goto -> 1332
    //   247: aload_3
    //   248: invokevirtual getType : ()I
    //   251: bipush #56
    //   253: if_icmpne -> 269
    //   256: aload_0
    //   257: aload_3
    //   258: aload_3
    //   259: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   262: iconst_0
    //   263: invokespecial visitSetVar : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Z)V
    //   266: goto -> 1332
    //   269: aload_3
    //   270: invokevirtual getType : ()I
    //   273: sipush #157
    //   276: if_icmpne -> 292
    //   279: aload_0
    //   280: aload_3
    //   281: aload_3
    //   282: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   285: iconst_0
    //   286: invokespecial visitSetConstVar : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Z)V
    //   289: goto -> 1332
    //   292: aload_3
    //   293: invokevirtual getType : ()I
    //   296: bipush #73
    //   298: if_icmpne -> 310
    //   301: aload_0
    //   302: aload_3
    //   303: iconst_0
    //   304: invokespecial generateYieldPoint : (Lcom/trendmicro/hippo/Node;Z)V
    //   307: goto -> 1332
    //   310: aload_0
    //   311: aload_3
    //   312: aload_1
    //   313: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   316: aload_1
    //   317: bipush #8
    //   319: iconst_m1
    //   320: invokevirtual getIntProp : (II)I
    //   323: iconst_m1
    //   324: if_icmpeq -> 339
    //   327: aload_0
    //   328: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   331: bipush #88
    //   333: invokevirtual add : (I)V
    //   336: goto -> 1332
    //   339: aload_0
    //   340: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   343: bipush #87
    //   345: invokevirtual add : (I)V
    //   348: goto -> 1332
    //   351: aload_0
    //   352: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   355: invokevirtual isGenerateObserverCount : ()Z
    //   358: ifeq -> 365
    //   361: aload_0
    //   362: invokespecial addInstructionCount : ()V
    //   365: aload_0
    //   366: aload_1
    //   367: invokespecial getTargetLabel : (Lcom/trendmicro/hippo/Node;)I
    //   370: istore #4
    //   372: aload_0
    //   373: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   376: iload #4
    //   378: invokevirtual markLabel : (I)V
    //   381: aload_0
    //   382: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   385: invokevirtual isGenerateObserverCount : ()Z
    //   388: ifeq -> 395
    //   391: aload_0
    //   392: invokespecial saveCurrentCodeOffset : ()V
    //   395: goto -> 1332
    //   398: aload_0
    //   399: aload_3
    //   400: aload_1
    //   401: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   404: aload_0
    //   405: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   408: aload_0
    //   409: getfield contextLocal : S
    //   412: invokevirtual addALoad : (I)V
    //   415: aload_0
    //   416: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   419: aload_0
    //   420: getfield variableObjectLocal : S
    //   423: invokevirtual addALoad : (I)V
    //   426: iload_2
    //   427: bipush #58
    //   429: if_icmpne -> 438
    //   432: iconst_0
    //   433: istore #4
    //   435: goto -> 463
    //   438: iload_2
    //   439: bipush #59
    //   441: if_icmpne -> 447
    //   444: goto -> 463
    //   447: iload_2
    //   448: bipush #61
    //   450: if_icmpne -> 460
    //   453: bipush #6
    //   455: istore #4
    //   457: goto -> 463
    //   460: iconst_2
    //   461: istore #4
    //   463: aload_0
    //   464: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   467: iload #4
    //   469: invokevirtual addPush : (I)V
    //   472: aload_0
    //   473: ldc_w 'enumInit'
    //   476: ldc_w '(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;'
    //   479: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   482: aload_0
    //   483: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   486: aload_0
    //   487: aload_1
    //   488: invokespecial getLocalBlockRegister : (Lcom/trendmicro/hippo/Node;)I
    //   491: invokevirtual addAStore : (I)V
    //   494: goto -> 1332
    //   497: aload_0
    //   498: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   501: iconst_0
    //   502: invokevirtual setStackTop : (S)V
    //   505: aload_0
    //   506: aload_1
    //   507: invokespecial getLocalBlockRegister : (Lcom/trendmicro/hippo/Node;)I
    //   510: istore_2
    //   511: aload_1
    //   512: bipush #14
    //   514: invokevirtual getExistingIntProp : (I)I
    //   517: istore #4
    //   519: aload_3
    //   520: invokevirtual getString : ()Ljava/lang/String;
    //   523: astore #5
    //   525: aload_0
    //   526: aload_3
    //   527: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   530: aload_1
    //   531: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   534: iload #4
    //   536: ifne -> 550
    //   539: aload_0
    //   540: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   543: iconst_1
    //   544: invokevirtual add : (I)V
    //   547: goto -> 558
    //   550: aload_0
    //   551: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   554: iload_2
    //   555: invokevirtual addALoad : (I)V
    //   558: aload_0
    //   559: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   562: aload #5
    //   564: invokevirtual addPush : (Ljava/lang/String;)V
    //   567: aload_0
    //   568: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   571: aload_0
    //   572: getfield contextLocal : S
    //   575: invokevirtual addALoad : (I)V
    //   578: aload_0
    //   579: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   582: aload_0
    //   583: getfield variableObjectLocal : S
    //   586: invokevirtual addALoad : (I)V
    //   589: aload_0
    //   590: ldc_w 'newCatchScope'
    //   593: ldc_w '(Ljava/lang/Throwable;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;'
    //   596: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   599: aload_0
    //   600: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   603: iload_2
    //   604: invokevirtual addAStore : (I)V
    //   607: goto -> 1332
    //   610: aload_0
    //   611: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   614: invokevirtual isGenerateObserverCount : ()Z
    //   617: ifeq -> 624
    //   620: aload_0
    //   621: invokespecial addInstructionCount : ()V
    //   624: aload_0
    //   625: aload_1
    //   626: checkcast com/trendmicro/hippo/ast/Jump
    //   629: iload_2
    //   630: aload_3
    //   631: invokespecial visitGoto : (Lcom/trendmicro/hippo/ast/Jump;ILcom/trendmicro/hippo/Node;)V
    //   634: goto -> 1332
    //   637: aload_0
    //   638: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   641: aload_0
    //   642: getfield variableObjectLocal : S
    //   645: invokevirtual addALoad : (I)V
    //   648: aload_0
    //   649: ldc_w 'leaveWith'
    //   652: ldc_w '(Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;'
    //   655: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   658: aload_0
    //   659: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   662: aload_0
    //   663: getfield variableObjectLocal : S
    //   666: invokevirtual addAStore : (I)V
    //   669: aload_0
    //   670: aload_0
    //   671: getfield variableObjectLocal : S
    //   674: invokespecial decReferenceWordLocal : (S)V
    //   677: goto -> 1332
    //   680: aload_0
    //   681: aload_3
    //   682: aload_1
    //   683: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   686: aload_0
    //   687: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   690: aload_0
    //   691: getfield contextLocal : S
    //   694: invokevirtual addALoad : (I)V
    //   697: aload_0
    //   698: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   701: aload_0
    //   702: getfield variableObjectLocal : S
    //   705: invokevirtual addALoad : (I)V
    //   708: aload_0
    //   709: ldc_w 'enterWith'
    //   712: ldc_w '(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;'
    //   715: invokespecial addScriptRuntimeInvoke : (Ljava/lang/String;Ljava/lang/String;)V
    //   718: aload_0
    //   719: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   722: aload_0
    //   723: getfield variableObjectLocal : S
    //   726: invokevirtual addAStore : (I)V
    //   729: aload_0
    //   730: aload_0
    //   731: getfield variableObjectLocal : S
    //   734: invokespecial incReferenceWordLocal : (S)V
    //   737: goto -> 1332
    //   740: goto -> 1332
    //   743: aload_0
    //   744: getfield inLocalBlock : Z
    //   747: istore #6
    //   749: aload_0
    //   750: iconst_1
    //   751: putfield inLocalBlock : Z
    //   754: aload_0
    //   755: invokespecial getNewWordLocal : ()S
    //   758: istore #4
    //   760: aload_0
    //   761: getfield isGenerator : Z
    //   764: ifeq -> 784
    //   767: aload_0
    //   768: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   771: iconst_1
    //   772: invokevirtual add : (I)V
    //   775: aload_0
    //   776: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   779: iload #4
    //   781: invokevirtual addAStore : (I)V
    //   784: aload_1
    //   785: iconst_2
    //   786: iload #4
    //   788: invokevirtual putIntProp : (II)V
    //   791: aload_3
    //   792: ifnull -> 808
    //   795: aload_0
    //   796: aload_3
    //   797: invokespecial generateStatement : (Lcom/trendmicro/hippo/Node;)V
    //   800: aload_3
    //   801: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   804: astore_3
    //   805: goto -> 791
    //   808: aload_0
    //   809: iload #4
    //   811: i2s
    //   812: invokespecial releaseWordLocal : (S)V
    //   815: aload_1
    //   816: iconst_2
    //   817: invokevirtual removeProp : (I)V
    //   820: aload_0
    //   821: iload #6
    //   823: putfield inLocalBlock : Z
    //   826: goto -> 1332
    //   829: aload_0
    //   830: getfield isGenerator : Z
    //   833: ifne -> 839
    //   836: goto -> 1332
    //   839: aload_0
    //   840: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   843: invokevirtual isGenerateObserverCount : ()Z
    //   846: ifeq -> 853
    //   849: aload_0
    //   850: invokespecial saveCurrentCodeOffset : ()V
    //   853: aload_0
    //   854: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   857: iconst_1
    //   858: invokevirtual setStackTop : (S)V
    //   861: aload_0
    //   862: invokespecial getNewWordLocal : ()S
    //   865: istore #7
    //   867: aload_0
    //   868: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   871: invokevirtual acquireLabel : ()I
    //   874: istore_2
    //   875: aload_0
    //   876: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   879: invokevirtual acquireLabel : ()I
    //   882: istore #4
    //   884: aload_0
    //   885: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   888: iload_2
    //   889: invokevirtual markLabel : (I)V
    //   892: aload_0
    //   893: invokespecial generateIntegerWrap : ()V
    //   896: aload_0
    //   897: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   900: iload #7
    //   902: invokevirtual addAStore : (I)V
    //   905: aload_3
    //   906: ifnull -> 922
    //   909: aload_0
    //   910: aload_3
    //   911: invokespecial generateStatement : (Lcom/trendmicro/hippo/Node;)V
    //   914: aload_3
    //   915: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   918: astore_3
    //   919: goto -> 905
    //   922: aload_0
    //   923: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   926: iload #7
    //   928: invokevirtual addALoad : (I)V
    //   931: aload_0
    //   932: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   935: sipush #192
    //   938: ldc_w 'java/lang/Integer'
    //   941: invokevirtual add : (ILjava/lang/String;)V
    //   944: aload_0
    //   945: invokespecial generateIntegerUnwrap : ()V
    //   948: aload_0
    //   949: getfield finallys : Ljava/util/Map;
    //   952: aload_1
    //   953: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   958: checkcast com/trendmicro/hippo/optimizer/BodyCodegen$FinallyReturnPoint
    //   961: astore_1
    //   962: aload_1
    //   963: aload_0
    //   964: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   967: invokevirtual acquireLabel : ()I
    //   970: putfield tableLabel : I
    //   973: aload_0
    //   974: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   977: sipush #167
    //   980: aload_1
    //   981: getfield tableLabel : I
    //   984: invokevirtual add : (II)V
    //   987: aload_0
    //   988: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   991: iconst_0
    //   992: invokevirtual setStackTop : (S)V
    //   995: aload_0
    //   996: iload #7
    //   998: i2s
    //   999: invokespecial releaseWordLocal : (S)V
    //   1002: aload_0
    //   1003: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1006: iload #4
    //   1008: invokevirtual markLabel : (I)V
    //   1011: goto -> 1332
    //   1014: aload_3
    //   1015: astore_1
    //   1016: aload_0
    //   1017: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   1020: invokevirtual isGenerateObserverCount : ()Z
    //   1023: ifeq -> 1033
    //   1026: aload_0
    //   1027: iconst_1
    //   1028: invokespecial addInstructionCount : (I)V
    //   1031: aload_3
    //   1032: astore_1
    //   1033: aload_1
    //   1034: ifnull -> 1332
    //   1037: aload_0
    //   1038: aload_1
    //   1039: invokespecial generateStatement : (Lcom/trendmicro/hippo/Node;)V
    //   1042: aload_1
    //   1043: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1046: astore_1
    //   1047: goto -> 1033
    //   1050: aload_0
    //   1051: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   1054: invokevirtual isGenerateObserverCount : ()Z
    //   1057: ifeq -> 1064
    //   1060: aload_0
    //   1061: invokespecial addInstructionCount : ()V
    //   1064: aload_0
    //   1065: aload_1
    //   1066: checkcast com/trendmicro/hippo/ast/Jump
    //   1069: aload_3
    //   1070: invokespecial visitSwitch : (Lcom/trendmicro/hippo/ast/Jump;Lcom/trendmicro/hippo/Node;)V
    //   1073: goto -> 1332
    //   1076: aload_1
    //   1077: iconst_1
    //   1078: invokevirtual getExistingIntProp : (I)I
    //   1081: istore #4
    //   1083: aload_0
    //   1084: getfield scriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   1087: iload #4
    //   1089: invokestatic get : (Lcom/trendmicro/hippo/ast/ScriptNode;I)Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   1092: astore_1
    //   1093: aload_1
    //   1094: getfield fnode : Lcom/trendmicro/hippo/ast/FunctionNode;
    //   1097: invokevirtual getFunctionType : ()I
    //   1100: istore #4
    //   1102: iload #4
    //   1104: iconst_3
    //   1105: if_icmpne -> 1118
    //   1108: aload_0
    //   1109: aload_1
    //   1110: iload #4
    //   1112: invokespecial visitFunction : (Lcom/trendmicro/hippo/optimizer/OptFunctionNode;I)V
    //   1115: goto -> 1332
    //   1118: iload #4
    //   1120: iconst_1
    //   1121: if_icmpne -> 1127
    //   1124: goto -> 1332
    //   1127: invokestatic badTree : ()Ljava/lang/RuntimeException;
    //   1130: athrow
    //   1131: aload_0
    //   1132: aload_1
    //   1133: checkcast com/trendmicro/hippo/ast/Jump
    //   1136: aload_3
    //   1137: invokespecial visitTryCatchFinally : (Lcom/trendmicro/hippo/ast/Jump;Lcom/trendmicro/hippo/Node;)V
    //   1140: goto -> 1332
    //   1143: aload_0
    //   1144: getfield isGenerator : Z
    //   1147: ifne -> 1205
    //   1150: aload_3
    //   1151: ifnull -> 1163
    //   1154: aload_0
    //   1155: aload_3
    //   1156: aload_1
    //   1157: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   1160: goto -> 1205
    //   1163: iload_2
    //   1164: iconst_4
    //   1165: if_icmpne -> 1178
    //   1168: aload_0
    //   1169: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1172: invokestatic pushUndefined : (Lcom/trendmicro/classfile/ClassFileWriter;)V
    //   1175: goto -> 1205
    //   1178: aload_0
    //   1179: getfield popvLocal : S
    //   1182: istore #4
    //   1184: iload #4
    //   1186: iflt -> 1201
    //   1189: aload_0
    //   1190: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1193: iload #4
    //   1195: invokevirtual addALoad : (I)V
    //   1198: goto -> 1205
    //   1201: invokestatic badTree : ()Ljava/lang/RuntimeException;
    //   1204: athrow
    //   1205: aload_0
    //   1206: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   1209: invokevirtual isGenerateObserverCount : ()Z
    //   1212: ifeq -> 1219
    //   1215: aload_0
    //   1216: invokespecial addInstructionCount : ()V
    //   1219: aload_0
    //   1220: getfield epilogueLabel : I
    //   1223: iconst_m1
    //   1224: if_icmpne -> 1252
    //   1227: aload_0
    //   1228: getfield hasVarsInRegs : Z
    //   1231: ifeq -> 1248
    //   1234: aload_0
    //   1235: aload_0
    //   1236: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1239: invokevirtual acquireLabel : ()I
    //   1242: putfield epilogueLabel : I
    //   1245: goto -> 1252
    //   1248: invokestatic badTree : ()Ljava/lang/RuntimeException;
    //   1251: athrow
    //   1252: aload_0
    //   1253: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1256: sipush #167
    //   1259: aload_0
    //   1260: getfield epilogueLabel : I
    //   1263: invokevirtual add : (II)V
    //   1266: goto -> 1332
    //   1269: aload_0
    //   1270: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   1273: invokevirtual isGenerateObserverCount : ()Z
    //   1276: ifeq -> 1283
    //   1279: aload_0
    //   1280: invokespecial addInstructionCount : ()V
    //   1283: aload_0
    //   1284: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1287: aload_0
    //   1288: aload_1
    //   1289: invokespecial getLocalBlockRegister : (Lcom/trendmicro/hippo/Node;)I
    //   1292: invokevirtual addALoad : (I)V
    //   1295: aload_0
    //   1296: getfield cfw : Lcom/trendmicro/classfile/ClassFileWriter;
    //   1299: sipush #191
    //   1302: invokevirtual add : (I)V
    //   1305: goto -> 1332
    //   1308: aload_0
    //   1309: aload_3
    //   1310: aload_1
    //   1311: invokespecial generateExpression : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   1314: aload_0
    //   1315: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   1318: invokevirtual isGenerateObserverCount : ()Z
    //   1321: ifeq -> 1328
    //   1324: aload_0
    //   1325: invokespecial addInstructionCount : ()V
    //   1328: aload_0
    //   1329: invokespecial generateThrowJavaScriptException : ()V
    //   1332: return
  }
  
  private void generateThrowJavaScriptException() {
    this.cfw.add(187, "com/trendmicro/hippo/JavaScriptException");
    this.cfw.add(90);
    this.cfw.add(95);
    this.cfw.addPush(this.scriptOrFn.getSourceName());
    this.cfw.addPush(this.itsLineNumber);
    this.cfw.addInvoke(183, "com/trendmicro/hippo/JavaScriptException", "<init>", "(Ljava/lang/Object;Ljava/lang/String;I)V");
    this.cfw.add(191);
  }
  
  private void generateYieldPoint(Node paramNode, boolean paramBoolean) {
    short s = this.cfw.getStackTop();
    int i = this.maxStack;
    if (i <= s)
      i = s; 
    this.maxStack = i;
    if (this.cfw.getStackTop() != 0) {
      generateGetGeneratorStackState();
      for (i = 0; i < s; i++) {
        this.cfw.add(90);
        this.cfw.add(95);
        this.cfw.addLoadConstant(i);
        this.cfw.add(95);
        this.cfw.add(83);
      } 
      this.cfw.add(87);
    } 
    Node node = paramNode.getFirstChild();
    if (node != null) {
      generateExpression(node, paramNode);
    } else {
      Codegen.pushUndefined(this.cfw);
    } 
    i = getNextGeneratorState(paramNode);
    generateSetGeneratorResumptionPoint(i);
    boolean bool = generateSaveLocals(paramNode);
    this.cfw.add(176);
    generateCheckForThrowOrClose(getTargetLabel(paramNode), bool, i);
    if (s != 0) {
      generateGetGeneratorStackState();
      for (i = 0; i < s; i++) {
        this.cfw.add(89);
        this.cfw.addLoadConstant(s - i - 1);
        this.cfw.add(50);
        this.cfw.add(95);
      } 
      this.cfw.add(87);
    } 
    if (paramBoolean)
      this.cfw.addALoad(this.argsLocal); 
  }
  
  private Node getFinallyAtTarget(Node paramNode) {
    if (paramNode == null)
      return null; 
    if (paramNode.getType() == 126)
      return paramNode; 
    if (paramNode.getType() == 132) {
      paramNode = paramNode.getNext();
      if (paramNode != null && paramNode.getType() == 126)
        return paramNode; 
    } 
    throw Kit.codeBug("bad finally target");
  }
  
  private int getLocalBlockRegister(Node paramNode) {
    return ((Node)paramNode.getProp(3)).getExistingIntProp(2);
  }
  
  private short getNewWordIntern(int paramInt) {
    int i;
    int[] arrayOfInt = this.locals;
    byte b = -1;
    if (paramInt > 1) {
      short s = this.firstFreeLocal;
      label38: while (true) {
        i = b;
        if (s + paramInt <= 1024) {
          int j;
          for (i = 0; i < paramInt; i++) {
            if (arrayOfInt[s + i] != 0) {
              j = s + i + 1;
              continue label38;
            } 
          } 
          i = j;
        } 
        break;
      } 
    } else {
      i = this.firstFreeLocal;
    } 
    if (i != -1) {
      arrayOfInt[i] = 1;
      if (paramInt > 1)
        arrayOfInt[i + 1] = 1; 
      if (paramInt > 2)
        arrayOfInt[i + 2] = 1; 
      if (i == this.firstFreeLocal) {
        for (paramInt = i + paramInt; paramInt < 1024; paramInt++) {
          if (arrayOfInt[paramInt] == 0) {
            paramInt = (short)paramInt;
            this.firstFreeLocal = (short)paramInt;
            if (this.localsMax < paramInt)
              this.localsMax = (short)paramInt; 
            return (short)i;
          } 
        } 
      } else {
        return (short)i;
      } 
    } 
    throw Context.reportRuntimeError("Program too complex (out of locals)");
  }
  
  private short getNewWordLocal() {
    return getNewWordIntern(1);
  }
  
  private short getNewWordLocal(boolean paramBoolean) {
    boolean bool;
    if (paramBoolean) {
      bool = true;
    } else {
      bool = true;
    } 
    return getNewWordIntern(bool);
  }
  
  private short getNewWordPairLocal(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 3;
    } else {
      b = 2;
    } 
    return getNewWordIntern(b);
  }
  
  private int getNextGeneratorState(Node paramNode) {
    return ((FunctionNode)this.scriptOrFn).getResumptionPoints().indexOf(paramNode) + 1;
  }
  
  private int getTargetLabel(Node paramNode) {
    int i = paramNode.labelId();
    int j = i;
    if (i == -1) {
      j = this.cfw.acquireLabel();
      paramNode.labelId(j);
    } 
    return j;
  }
  
  private void incReferenceWordLocal(short paramShort) {
    int[] arrayOfInt = this.locals;
    arrayOfInt[paramShort] = arrayOfInt[paramShort] + 1;
  }
  
  private void initBodyGeneration() {
    this.varRegisters = null;
    if (this.scriptOrFn.getType() == 110) {
      OptFunctionNode optFunctionNode = OptFunctionNode.get(this.scriptOrFn);
      this.fnCurrent = optFunctionNode;
      int i = optFunctionNode.fnode.requiresActivation() ^ true;
      this.hasVarsInRegs = i;
      if (i != 0) {
        int j = this.fnCurrent.fnode.getParamAndVarCount();
        if (j != 0)
          this.varRegisters = new short[j]; 
      } 
      boolean bool = this.fnCurrent.isTargetOfDirectCall();
      this.inDirectCallFunction = bool;
      if (bool && !this.hasVarsInRegs)
        Codegen.badTree(); 
    } else {
      this.fnCurrent = null;
      this.hasVarsInRegs = false;
      this.inDirectCallFunction = false;
    } 
    this.locals = new int[1024];
    this.funObjLocal = (short)0;
    this.contextLocal = (short)1;
    this.variableObjectLocal = (short)2;
    this.thisObjLocal = (short)3;
    this.localsMax = (short)4;
    this.firstFreeLocal = (short)4;
    this.popvLocal = (short)-1;
    this.argsLocal = (short)-1;
    this.itsZeroArgArray = (short)-1;
    this.itsOneArgArray = (short)-1;
    this.epilogueLabel = -1;
    this.enterAreaStartLabel = -1;
    this.generatorStateLocal = (short)-1;
  }
  
  private void inlineFinally(Node paramNode) {
    int i = this.cfw.acquireLabel();
    int j = this.cfw.acquireLabel();
    this.cfw.markLabel(i);
    inlineFinally(paramNode, i, j);
    this.cfw.markLabel(j);
  }
  
  private void inlineFinally(Node paramNode, int paramInt1, int paramInt2) {
    Node node = getFinallyAtTarget(paramNode);
    node.resetTargets();
    paramNode = node.getFirstChild();
    this.exceptionManager.markInlineFinallyStart(node, paramInt1);
    while (paramNode != null) {
      generateStatement(paramNode);
      paramNode = paramNode.getNext();
    } 
    this.exceptionManager.markInlineFinallyEnd(node, paramInt2);
  }
  
  private static boolean isArithmeticNode(Node paramNode) {
    int i = paramNode.getType();
    return (i == 22 || i == 25 || i == 24 || i == 23);
  }
  
  private int nodeIsDirectCallParameter(Node paramNode) {
    if (paramNode.getType() == 55 && this.inDirectCallFunction && !this.itsForcedObjectParameters) {
      int i = this.fnCurrent.getVarIndex(paramNode);
      if (this.fnCurrent.isParameter(i))
        return this.varRegisters[i]; 
    } 
    return -1;
  }
  
  private void releaseWordLocal(short paramShort) {
    if (paramShort < this.firstFreeLocal)
      this.firstFreeLocal = (short)paramShort; 
    this.locals[paramShort] = 0;
  }
  
  private void saveCurrentCodeOffset() {
    this.savedCodeOffset = this.cfw.getCurrentCodeOffset();
  }
  
  private void updateLineNumber(Node paramNode) {
    int i = paramNode.getLineno();
    this.itsLineNumber = i;
    if (i == -1)
      return; 
    this.cfw.addLineNumberEntry((short)i);
  }
  
  private boolean varIsDirectCallParameter(int paramInt) {
    boolean bool;
    if (this.fnCurrent.isParameter(paramInt) && this.inDirectCallFunction && !this.itsForcedObjectParameters) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void visitArithmetic(Node paramNode1, int paramInt, Node paramNode2, Node paramNode3) {
    if (paramNode1.getIntProp(8, -1) != -1) {
      generateExpression(paramNode2, paramNode1);
      generateExpression(paramNode2.getNext(), paramNode1);
      this.cfw.add(paramInt);
    } else {
      boolean bool = isArithmeticNode(paramNode3);
      generateExpression(paramNode2, paramNode1);
      if (!isArithmeticNode(paramNode2))
        addObjectToDouble(); 
      generateExpression(paramNode2.getNext(), paramNode1);
      if (!isArithmeticNode(paramNode2.getNext()))
        addObjectToDouble(); 
      this.cfw.add(paramInt);
      if (!bool)
        addDoubleWrap(); 
    } 
  }
  
  private void visitArrayLiteral(Node paramNode1, Node paramNode2, boolean paramBoolean) {
    String str;
    byte b = 0;
    for (Node node = paramNode2; node != null; node = node.getNext())
      b++; 
    if (!paramBoolean && (b > 10 || this.cfw.getCurrentCodeOffset() > 30000) && !this.hasVarsInRegs && !this.isGenerator && !this.inLocalBlock) {
      if (this.literals == null)
        this.literals = new LinkedList<>(); 
      this.literals.add(paramNode1);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.codegen.getBodyMethodName(this.scriptOrFn));
      stringBuilder.append("_literal");
      stringBuilder.append(this.literals.size());
      str = stringBuilder.toString();
      this.cfw.addALoad(this.funObjLocal);
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addALoad(this.thisObjLocal);
      this.cfw.addALoad(this.argsLocal);
      this.cfw.addInvoke(182, this.codegen.mainClassName, str, "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
      return;
    } 
    if (this.isGenerator) {
      byte b1;
      for (b1 = 0; b1 != b; b1++) {
        generateExpression(paramNode2, (Node)str);
        paramNode2 = paramNode2.getNext();
      } 
      addNewObjectArray(b);
      for (b1 = 0; b1 != b; b1++) {
        this.cfw.add(90);
        this.cfw.add(95);
        this.cfw.addPush(b - b1 - 1);
        this.cfw.add(95);
        this.cfw.add(83);
      } 
    } else {
      addNewObjectArray(b);
      for (byte b1 = 0; b1 != b; b1++) {
        this.cfw.add(89);
        this.cfw.addPush(b1);
        generateExpression(paramNode2, (Node)str);
        this.cfw.add(83);
        paramNode2 = paramNode2.getNext();
      } 
    } 
    int[] arrayOfInt = (int[])str.getProp(11);
    if (arrayOfInt == null) {
      this.cfw.add(1);
      this.cfw.add(3);
    } else {
      this.cfw.addPush(OptRuntime.encodeIntArray(arrayOfInt));
      this.cfw.addPush(arrayOfInt.length);
    } 
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    addOptRuntimeInvoke("newArrayLiteral", "([Ljava/lang/Object;Ljava/lang/String;ILcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;");
  }
  
  private void visitBitOp(Node paramNode1, int paramInt, Node paramNode2) {
    int i = paramNode1.getIntProp(8, -1);
    generateExpression(paramNode2, paramNode1);
    if (paramInt == 20) {
      addScriptRuntimeInvoke("toUint32", "(Ljava/lang/Object;)J");
      generateExpression(paramNode2.getNext(), paramNode1);
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
      this.cfw.addPush(31);
      this.cfw.add(126);
      this.cfw.add(125);
      this.cfw.add(138);
      addDoubleWrap();
      return;
    } 
    if (i == -1) {
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
      generateExpression(paramNode2.getNext(), paramNode1);
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
    } else {
      addScriptRuntimeInvoke("toInt32", "(D)I");
      generateExpression(paramNode2.getNext(), paramNode1);
      addScriptRuntimeInvoke("toInt32", "(D)I");
    } 
    if (paramInt != 18) {
      if (paramInt != 19) {
        switch (paramInt) {
          default:
            throw Codegen.badTree();
          case 11:
            this.cfw.add(126);
            break;
          case 10:
            this.cfw.add(130);
            break;
          case 9:
            this.cfw.add(128);
            break;
        } 
      } else {
        this.cfw.add(122);
      } 
    } else {
      this.cfw.add(120);
    } 
    this.cfw.add(135);
    if (i == -1)
      addDoubleWrap(); 
  }
  
  private void visitDotQuery(Node paramNode1, Node paramNode2) {
    updateLineNumber(paramNode1);
    generateExpression(paramNode2, paramNode1);
    this.cfw.addALoad(this.variableObjectLocal);
    addScriptRuntimeInvoke("enterDotQuery", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;");
    this.cfw.addAStore(this.variableObjectLocal);
    this.cfw.add(1);
    int i = this.cfw.acquireLabel();
    this.cfw.markLabel(i);
    this.cfw.add(87);
    generateExpression(paramNode2.getNext(), paramNode1);
    addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
    this.cfw.addALoad(this.variableObjectLocal);
    addScriptRuntimeInvoke("updateDotQuery", "(ZLcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
    this.cfw.add(89);
    this.cfw.add(198, i);
    this.cfw.addALoad(this.variableObjectLocal);
    addScriptRuntimeInvoke("leaveDotQuery", "(Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;");
    this.cfw.addAStore(this.variableObjectLocal);
  }
  
  private void visitFunction(OptFunctionNode paramOptFunctionNode, int paramInt) {
    int i = this.codegen.getIndex((ScriptNode)paramOptFunctionNode.fnode);
    this.cfw.add(187, this.codegen.mainClassName);
    this.cfw.add(89);
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addPush(i);
    this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;I)V");
    if (paramInt == 4) {
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addALoad(this.thisObjLocal);
      addOptRuntimeInvoke("bindThis", "(Lcom/trendmicro/hippo/NativeFunction;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Function;");
    } 
    if (paramInt == 2 || paramInt == 4)
      return; 
    this.cfw.addPush(paramInt);
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addALoad(this.contextLocal);
    addOptRuntimeInvoke("initFunction", "(Lcom/trendmicro/hippo/NativeFunction;ILcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;)V");
  }
  
  private void visitGetProp(Node paramNode1, Node paramNode2) {
    generateExpression(paramNode2, paramNode1);
    Node node = paramNode2.getNext();
    generateExpression(node, paramNode1);
    if (paramNode1.getType() == 34) {
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      addScriptRuntimeInvoke("getObjectPropNoWarn", "(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
      return;
    } 
    if (paramNode2.getType() == 43 && node.getType() == 41) {
      this.cfw.addALoad(this.contextLocal);
      addScriptRuntimeInvoke("getObjectProp", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
    } else {
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
    } 
  }
  
  private void visitGetVar(Node paramNode) {
    if (!this.hasVarsInRegs)
      Kit.codeBug(); 
    int i = this.fnCurrent.getVarIndex(paramNode);
    short s = this.varRegisters[i];
    if (varIsDirectCallParameter(i)) {
      if (paramNode.getIntProp(8, -1) != -1) {
        dcpLoadAsNumber(s);
      } else {
        dcpLoadAsObject(s);
      } 
    } else if (this.fnCurrent.isNumberVar(i)) {
      this.cfw.addDLoad(s);
    } else {
      this.cfw.addALoad(s);
    } 
  }
  
  private void visitGoto(Jump paramJump, int paramInt, Node paramNode) {
    Node node = paramJump.target;
    if (paramInt == 6 || paramInt == 7) {
      if (paramNode != null) {
        int i = getTargetLabel(node);
        int j = this.cfw.acquireLabel();
        if (paramInt == 6) {
          generateIfJump(paramNode, (Node)paramJump, i, j);
        } else {
          generateIfJump(paramNode, (Node)paramJump, j, i);
        } 
        this.cfw.markLabel(j);
        return;
      } 
      throw Codegen.badTree();
    } 
    if (paramInt == 136) {
      if (this.isGenerator) {
        addGotoWithReturn(node);
      } else {
        inlineFinally(node);
      } 
    } else {
      addGoto(node, 167);
    } 
  }
  
  private void visitIfJumpEqOp(Node paramNode1, Node paramNode2, int paramInt1, int paramInt2) {
    Node node = paramNode2;
    int i = paramInt1;
    int j = paramInt2;
    if (i != -1 && j != -1) {
      short s = this.cfw.getStackTop();
      int k = paramNode1.getType();
      Node node1 = paramNode2.getNext();
      if (paramNode2.getType() == 42 || node1.getType() == 42) {
        if (paramNode2.getType() == 42)
          node = node1; 
        generateExpression(node, paramNode1);
        char c = 'Ç';
        if (k == 46 || k == 47) {
          if (k == 46) {
            paramInt1 = 198;
          } else {
            paramInt1 = c;
          } 
          this.cfw.add(paramInt1, i);
        } else {
          if (k != 12)
            if (k == 13) {
              i = paramInt2;
              j = paramInt1;
            } else {
              throw Codegen.badTree();
            }  
          this.cfw.add(89);
          paramInt1 = this.cfw.acquireLabel();
          this.cfw.add(199, paramInt1);
          short s1 = this.cfw.getStackTop();
          this.cfw.add(87);
          this.cfw.add(167, i);
          this.cfw.markLabel(paramInt1, s1);
          Codegen.pushUndefined(this.cfw);
          this.cfw.add(165, i);
        } 
        this.cfw.add(167, j);
      } else {
        String str;
        paramInt2 = nodeIsDirectCallParameter(node);
        if (paramInt2 != -1 && node1.getType() == 150) {
          paramNode2 = node1.getFirstChild();
          if (paramNode2.getType() == 40) {
            this.cfw.addALoad(paramInt2);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            paramInt1 = this.cfw.acquireLabel();
            this.cfw.add(166, paramInt1);
            this.cfw.addDLoad(paramInt2 + 1);
            this.cfw.addPush(paramNode2.getDouble());
            this.cfw.add(151);
            if (k == 12) {
              this.cfw.add(153, i);
            } else {
              this.cfw.add(154, i);
            } 
            this.cfw.add(167, j);
            this.cfw.markLabel(paramInt1);
          } 
        } 
        generateExpression(node, paramNode1);
        generateExpression(node1, paramNode1);
        if (k != 12) {
          if (k != 13) {
            if (k != 46) {
              if (k == 47) {
                str = "shallowEq";
                paramInt1 = 153;
              } else {
                throw Codegen.badTree();
              } 
            } else {
              str = "shallowEq";
              paramInt1 = 154;
            } 
          } else {
            str = "eq";
            paramInt1 = 153;
          } 
        } else {
          str = "eq";
          paramInt1 = 154;
        } 
        addScriptRuntimeInvoke(str, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
        this.cfw.add(paramInt1, i);
        this.cfw.add(167, j);
      } 
      if (s == this.cfw.getStackTop())
        return; 
      throw Codegen.badTree();
    } 
    throw Codegen.badTree();
  }
  
  private void visitIfJumpRelOp(Node paramNode1, Node paramNode2, int paramInt1, int paramInt2) {
    if (paramInt1 != -1 && paramInt2 != -1) {
      String str;
      int i = paramNode1.getType();
      Node node = paramNode2.getNext();
      if (i == 53 || i == 52) {
        generateExpression(paramNode2, paramNode1);
        generateExpression(node, paramNode1);
        this.cfw.addALoad(this.contextLocal);
        if (i == 53) {
          str = "instanceOf";
        } else {
          str = "in";
        } 
        addScriptRuntimeInvoke(str, "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;)Z");
        this.cfw.add(154, paramInt1);
        this.cfw.add(167, paramInt2);
        return;
      } 
      int j = str.getIntProp(8, -1);
      int k = nodeIsDirectCallParameter(paramNode2);
      int m = nodeIsDirectCallParameter(node);
      if (j != -1) {
        if (j != 2) {
          generateExpression(paramNode2, (Node)str);
        } else if (k != -1) {
          dcpLoadAsNumber(k);
        } else {
          generateExpression(paramNode2, (Node)str);
          addObjectToDouble();
        } 
        if (j != 1) {
          generateExpression(node, (Node)str);
        } else if (m != -1) {
          dcpLoadAsNumber(m);
        } else {
          generateExpression(node, (Node)str);
          addObjectToDouble();
        } 
        genSimpleCompare(i, paramInt1, paramInt2);
      } else {
        if (k != -1 && m != -1) {
          j = this.cfw.getStackTop();
          int n = this.cfw.acquireLabel();
          this.cfw.addALoad(k);
          this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
          this.cfw.add(166, n);
          this.cfw.addDLoad(k + 1);
          dcpLoadAsNumber(m);
          genSimpleCompare(i, paramInt1, paramInt2);
          if (j == this.cfw.getStackTop()) {
            this.cfw.markLabel(n);
            n = this.cfw.acquireLabel();
            this.cfw.addALoad(m);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            this.cfw.add(166, n);
            this.cfw.addALoad(k);
            addObjectToDouble();
            this.cfw.addDLoad(m + 1);
            genSimpleCompare(i, paramInt1, paramInt2);
            if (j == this.cfw.getStackTop()) {
              this.cfw.markLabel(n);
              this.cfw.addALoad(k);
              this.cfw.addALoad(m);
            } else {
              throw Codegen.badTree();
            } 
          } else {
            throw Codegen.badTree();
          } 
        } else {
          generateExpression(paramNode2, (Node)str);
          generateExpression(node, (Node)str);
        } 
        if (i == 17 || i == 16)
          this.cfw.add(95); 
        if (i == 14 || i == 16) {
          str = "cmp_LT";
        } else {
          str = "cmp_LE";
        } 
        addScriptRuntimeInvoke(str, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
        this.cfw.add(154, paramInt1);
        this.cfw.add(167, paramInt2);
      } 
      return;
    } 
    throw Codegen.badTree();
  }
  
  private void visitIncDec(Node paramNode) {
    int i = paramNode.getExistingIntProp(13);
    Node node = paramNode.getFirstChild();
    int j = node.getType();
    if (j != 33) {
      if (j != 34) {
        if (j != 36) {
          if (j != 39) {
            if (j != 55) {
              if (j != 68) {
                Codegen.badTree();
              } else {
                generateExpression(node.getFirstChild(), paramNode);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(i);
                addScriptRuntimeInvoke("refIncrDecr", "(Lcom/trendmicro/hippo/Ref;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;");
              } 
            } else {
              boolean bool;
              if (!this.hasVarsInRegs)
                Kit.codeBug(); 
              if ((i & 0x2) != 0) {
                j = 1;
              } else {
                j = 0;
              } 
              int k = this.fnCurrent.getVarIndex(node);
              short s = this.varRegisters[k];
              if (this.fnCurrent.fnode.getParamAndVarConst()[k]) {
                if (paramNode.getIntProp(8, -1) != -1) {
                  bool = varIsDirectCallParameter(k);
                  this.cfw.addDLoad(s + bool);
                  if (j == 0) {
                    this.cfw.addPush(1.0D);
                    if ((i & 0x1) == 0) {
                      this.cfw.add(99);
                    } else {
                      this.cfw.add(103);
                    } 
                  } 
                } else {
                  if (varIsDirectCallParameter(bool)) {
                    dcpLoadAsObject(s);
                  } else {
                    this.cfw.addALoad(s);
                  } 
                  if (j != 0) {
                    this.cfw.add(89);
                    addObjectToDouble();
                    this.cfw.add(88);
                  } else {
                    addObjectToDouble();
                    this.cfw.addPush(1.0D);
                    if ((i & 0x1) == 0) {
                      this.cfw.add(99);
                    } else {
                      this.cfw.add(103);
                    } 
                    addDoubleWrap();
                  } 
                } 
              } else if (paramNode.getIntProp(8, -1) != -1) {
                bool = varIsDirectCallParameter(bool);
                this.cfw.addDLoad(s + bool);
                if (j != 0)
                  this.cfw.add(92); 
                this.cfw.addPush(1.0D);
                if ((i & 0x1) == 0) {
                  this.cfw.add(99);
                } else {
                  this.cfw.add(103);
                } 
                if (j == 0)
                  this.cfw.add(92); 
                this.cfw.addDStore(s + bool);
              } else {
                if (varIsDirectCallParameter(bool)) {
                  dcpLoadAsObject(s);
                } else {
                  this.cfw.addALoad(s);
                } 
                addObjectToDouble();
                if (j != 0)
                  this.cfw.add(92); 
                this.cfw.addPush(1.0D);
                if ((i & 0x1) == 0) {
                  this.cfw.add(99);
                } else {
                  this.cfw.add(103);
                } 
                addDoubleWrap();
                if (j == 0)
                  this.cfw.add(89); 
                this.cfw.addAStore(s);
                if (j != 0)
                  addDoubleWrap(); 
              } 
            } 
          } else {
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addPush(node.getString());
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addPush(i);
            addScriptRuntimeInvoke("nameIncrDecr", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Context;I)Ljava/lang/Object;");
          } 
        } else {
          node = node.getFirstChild();
          generateExpression(node, paramNode);
          generateExpression(node.getNext(), paramNode);
          this.cfw.addALoad(this.contextLocal);
          this.cfw.addALoad(this.variableObjectLocal);
          this.cfw.addPush(i);
          if (node.getNext().getIntProp(8, -1) != -1) {
            addOptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;DLcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;");
          } else {
            addScriptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;");
          } 
        } 
      } else {
        throw Kit.codeBug();
      } 
    } else {
      node = node.getFirstChild();
      generateExpression(node, paramNode);
      generateExpression(node.getNext(), paramNode);
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addPush(i);
      addScriptRuntimeInvoke("propIncrDecr", "(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;");
    } 
  }
  
  private void visitObjectLiteral(Node paramNode1, Node paramNode2, boolean paramBoolean) {
    String str;
    int k;
    Object[] arrayOfObject = (Object[])paramNode1.getProp(12);
    int i = arrayOfObject.length;
    if (!paramBoolean && (i > 10 || this.cfw.getCurrentCodeOffset() > 30000) && !this.hasVarsInRegs && !this.isGenerator && !this.inLocalBlock) {
      if (this.literals == null)
        this.literals = new LinkedList<>(); 
      this.literals.add(paramNode1);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.codegen.getBodyMethodName(this.scriptOrFn));
      stringBuilder.append("_literal");
      stringBuilder.append(this.literals.size());
      str = stringBuilder.toString();
      this.cfw.addALoad(this.funObjLocal);
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addALoad(this.thisObjLocal);
      this.cfw.addALoad(this.argsLocal);
      this.cfw.addInvoke(182, this.codegen.mainClassName, str, "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
      return;
    } 
    if (this.isGenerator) {
      addLoadPropertyValues((Node)str, paramNode2, i);
      addLoadPropertyIds(arrayOfObject, i);
      this.cfw.add(95);
    } else {
      addLoadPropertyIds(arrayOfObject, i);
      addLoadPropertyValues((Node)str, paramNode2, i);
    } 
    byte b = 0;
    Node node = paramNode2;
    int j = 0;
    while (true) {
      k = b;
      if (j != i) {
        k = node.getType();
        if (k == 152 || k == 153) {
          k = 1;
          break;
        } 
        node = node.getNext();
        j++;
        continue;
      } 
      break;
    } 
    if (k != 0) {
      this.cfw.addPush(i);
      this.cfw.add(188, 10);
      for (j = 0; j != i; j++) {
        this.cfw.add(89);
        this.cfw.addPush(j);
        k = paramNode2.getType();
        if (k == 152) {
          this.cfw.add(2);
        } else if (k == 153) {
          this.cfw.add(4);
        } else {
          this.cfw.add(3);
        } 
        this.cfw.add(79);
        paramNode2 = paramNode2.getNext();
      } 
    } else {
      this.cfw.add(1);
    } 
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    addScriptRuntimeInvoke("newObjectLiteral", "([Ljava/lang/Object;[Ljava/lang/Object;[ILcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;");
  }
  
  private void visitOptimizedCall(Node paramNode1, OptFunctionNode paramOptFunctionNode, int paramInt, Node paramNode2) {
    String str1;
    int j;
    Node node = paramNode2.getNext();
    String str2 = this.codegen.mainClassName;
    int i = 0;
    if (paramInt == 30) {
      generateExpression(paramNode2, paramNode1);
      j = i;
    } else {
      generateFunctionAndThisObj(paramNode2, paramNode1);
      i = getNewWordLocal();
      this.cfw.addAStore(i);
      j = i;
    } 
    int k = this.cfw.acquireLabel();
    int m = this.cfw.acquireLabel();
    this.cfw.add(89);
    this.cfw.add(193, str2);
    this.cfw.add(153, m);
    this.cfw.add(192, str2);
    this.cfw.add(89);
    this.cfw.add(180, str2, "_id", "I");
    this.cfw.addPush(this.codegen.getIndex((ScriptNode)paramOptFunctionNode.fnode));
    this.cfw.add(160, m);
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    if (paramInt == 30) {
      this.cfw.add(1);
    } else {
      this.cfw.addALoad(j);
    } 
    for (paramNode2 = node; paramNode2 != null; paramNode2 = paramNode2.getNext()) {
      i = nodeIsDirectCallParameter(paramNode2);
      if (i >= 0) {
        this.cfw.addALoad(i);
        this.cfw.addDLoad(i + 1);
      } else if (paramNode2.getIntProp(8, -1) == 0) {
        this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        generateExpression(paramNode2, paramNode1);
      } else {
        generateExpression(paramNode2, paramNode1);
        this.cfw.addPush(0.0D);
      } 
    } 
    this.cfw.add(178, "com/trendmicro/hippo/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
    ClassFileWriter classFileWriter = this.cfw;
    String str3 = this.codegen.mainClassName;
    if (paramInt == 30) {
      str1 = this.codegen.getDirectCtorName((ScriptNode)paramOptFunctionNode.fnode);
    } else {
      str1 = this.codegen.getBodyMethodName((ScriptNode)paramOptFunctionNode.fnode);
    } 
    classFileWriter.addInvoke(184, str3, str1, this.codegen.getBodyMethodSignature((ScriptNode)paramOptFunctionNode.fnode));
    this.cfw.add(167, k);
    this.cfw.markLabel(m);
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    if (paramInt != 30) {
      this.cfw.addALoad(j);
      releaseWordLocal(j);
    } 
    generateCallArgArray(paramNode1, node, true);
    if (paramInt == 30) {
      addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
    } else {
      this.cfw.addInvoke(185, "com/trendmicro/hippo/Callable", "call", "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    } 
    this.cfw.markLabel(k);
  }
  
  private void visitSetConst(Node paramNode1, Node paramNode2) {
    String str = paramNode1.getFirstChild().getString();
    while (paramNode2 != null) {
      generateExpression(paramNode2, paramNode1);
      paramNode2 = paramNode2.getNext();
    } 
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addPush(str);
    addScriptRuntimeInvoke("setConst", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Ljava/lang/String;)Ljava/lang/Object;");
  }
  
  private void visitSetConstVar(Node paramNode1, Node paramNode2, boolean paramBoolean) {
    boolean bool;
    if (!this.hasVarsInRegs)
      Kit.codeBug(); 
    int i = this.fnCurrent.getVarIndex(paramNode1);
    generateExpression(paramNode2.getNext(), paramNode1);
    if (paramNode1.getIntProp(8, -1) != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    short s = this.varRegisters[i];
    int j = this.cfw.acquireLabel();
    i = this.cfw.acquireLabel();
    if (bool) {
      this.cfw.addILoad(s + 2);
      this.cfw.add(154, i);
      short s1 = this.cfw.getStackTop();
      this.cfw.addPush(1);
      this.cfw.addIStore(s + 2);
      this.cfw.addDStore(s);
      if (paramBoolean) {
        this.cfw.addDLoad(s);
        this.cfw.markLabel(i, s1);
      } else {
        this.cfw.add(167, j);
        this.cfw.markLabel(i, s1);
        this.cfw.add(88);
      } 
    } else {
      this.cfw.addILoad(s + 1);
      this.cfw.add(154, i);
      short s1 = this.cfw.getStackTop();
      this.cfw.addPush(1);
      this.cfw.addIStore(s + 1);
      this.cfw.addAStore(s);
      if (paramBoolean) {
        this.cfw.addALoad(s);
        this.cfw.markLabel(i, s1);
      } else {
        this.cfw.add(167, j);
        this.cfw.markLabel(i, s1);
        this.cfw.add(87);
      } 
    } 
    this.cfw.markLabel(j);
  }
  
  private void visitSetElem(int paramInt, Node paramNode1, Node paramNode2) {
    boolean bool;
    generateExpression(paramNode2, paramNode1);
    paramNode2 = paramNode2.getNext();
    if (paramInt == 141)
      this.cfw.add(89); 
    generateExpression(paramNode2, paramNode1);
    paramNode2 = paramNode2.getNext();
    if (paramNode1.getIntProp(8, -1) != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramInt == 141)
      if (bool) {
        this.cfw.add(93);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
      } else {
        this.cfw.add(90);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
      }  
    generateExpression(paramNode2, paramNode1);
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    if (bool) {
      addScriptRuntimeInvoke("setObjectIndex", "(Ljava/lang/Object;DLjava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
    } else {
      addScriptRuntimeInvoke("setObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
    } 
  }
  
  private void visitSetName(Node paramNode1, Node paramNode2) {
    String str = paramNode1.getFirstChild().getString();
    while (paramNode2 != null) {
      generateExpression(paramNode2, paramNode1);
      paramNode2 = paramNode2.getNext();
    } 
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addPush(str);
    addScriptRuntimeInvoke("setName", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
  }
  
  private void visitSetProp(int paramInt, Node paramNode1, Node paramNode2) {
    generateExpression(paramNode2, paramNode1);
    Node node1 = paramNode2.getNext();
    if (paramInt == 140)
      this.cfw.add(89); 
    generateExpression(node1, paramNode1);
    Node node2 = node1.getNext();
    if (paramInt == 140) {
      this.cfw.add(90);
      if (paramNode2.getType() == 43 && node1.getType() == 41) {
        this.cfw.addALoad(this.contextLocal);
        addScriptRuntimeInvoke("getObjectProp", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Lcom/trendmicro/hippo/Context;)Ljava/lang/Object;");
      } else {
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
      } 
    } 
    generateExpression(node2, paramNode1);
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    addScriptRuntimeInvoke("setObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;");
  }
  
  private void visitSetVar(Node paramNode1, Node paramNode2, boolean paramBoolean) {
    int j;
    if (!this.hasVarsInRegs)
      Kit.codeBug(); 
    int i = this.fnCurrent.getVarIndex(paramNode1);
    generateExpression(paramNode2.getNext(), paramNode1);
    if (paramNode1.getIntProp(8, -1) != -1) {
      j = 1;
    } else {
      j = 0;
    } 
    short s = this.varRegisters[i];
    if (this.fnCurrent.fnode.getParamAndVarConst()[i]) {
      if (!paramBoolean)
        if (j) {
          this.cfw.add(88);
        } else {
          this.cfw.add(87);
        }  
    } else if (varIsDirectCallParameter(i)) {
      if (j) {
        if (paramBoolean)
          this.cfw.add(92); 
        this.cfw.addALoad(s);
        this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        i = this.cfw.acquireLabel();
        j = this.cfw.acquireLabel();
        this.cfw.add(165, i);
        short s1 = this.cfw.getStackTop();
        addDoubleWrap();
        this.cfw.addAStore(s);
        this.cfw.add(167, j);
        this.cfw.markLabel(i, s1);
        this.cfw.addDStore(s + 1);
        this.cfw.markLabel(j);
      } else {
        if (paramBoolean)
          this.cfw.add(89); 
        this.cfw.addAStore(s);
      } 
    } else {
      boolean bool = this.fnCurrent.isNumberVar(i);
      if (j != 0) {
        if (bool) {
          this.cfw.addDStore(s);
          if (paramBoolean)
            this.cfw.addDLoad(s); 
        } else {
          if (paramBoolean)
            this.cfw.add(92); 
          addDoubleWrap();
          this.cfw.addAStore(s);
        } 
      } else {
        if (bool)
          Kit.codeBug(); 
        this.cfw.addAStore(s);
        if (paramBoolean)
          this.cfw.addALoad(s); 
      } 
    } 
  }
  
  private void visitSpecialCall(Node paramNode1, int paramInt1, int paramInt2, Node paramNode2) {
    String str1;
    String str2;
    this.cfw.addALoad(this.contextLocal);
    if (paramInt1 == 30) {
      generateExpression(paramNode2, paramNode1);
    } else {
      generateFunctionAndThisObj(paramNode2, paramNode1);
    } 
    generateCallArgArray(paramNode1, paramNode2.getNext(), false);
    if (paramInt1 == 30) {
      str1 = "newObjectSpecial";
      str2 = "(Lcom/trendmicro/hippo/Context;Ljava/lang/Object;[Ljava/lang/Object;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;I)Ljava/lang/Object;";
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addALoad(this.thisObjLocal);
      this.cfw.addPush(paramInt2);
    } else {
      String str = "callSpecial";
      str2 = "(Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;ILjava/lang/String;I)Ljava/lang/Object;";
      this.cfw.addALoad(this.variableObjectLocal);
      this.cfw.addALoad(this.thisObjLocal);
      this.cfw.addPush(paramInt2);
      str1 = this.scriptOrFn.getSourceName();
      ClassFileWriter classFileWriter = this.cfw;
      if (str1 == null)
        str1 = ""; 
      classFileWriter.addPush(str1);
      this.cfw.addPush(this.itsLineNumber);
      str1 = str;
    } 
    addOptRuntimeInvoke(str1, str2);
  }
  
  private void visitStandardCall(Node paramNode1, Node paramNode2) {
    if (paramNode1.getType() == 38) {
      String str1;
      String str2;
      Node node = paramNode2.getNext();
      int i = paramNode2.getType();
      if (node == null) {
        if (i == 39) {
          str1 = paramNode2.getString();
          this.cfw.addPush(str1);
          str2 = "callName0";
          str1 = "(Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
        } else if (i == 33) {
          Node node1 = str2.getFirstChild();
          generateExpression(node1, (Node)str1);
          str1 = node1.getNext().getString();
          this.cfw.addPush(str1);
          str1 = "(Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
          str2 = "callProp0";
        } else if (i != 34) {
          generateFunctionAndThisObj((Node)str2, (Node)str1);
          str2 = "call0";
          str1 = "(Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
        } else {
          throw Kit.codeBug();
        } 
      } else if (i == 39) {
        str2 = str2.getString();
        generateCallArgArray((Node)str1, node, false);
        this.cfw.addPush(str2);
        str2 = "callName";
        str1 = "([Ljava/lang/Object;Ljava/lang/String;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
      } else {
        i = 0;
        for (Node node1 = node; node1 != null; node1 = node1.getNext())
          i++; 
        generateFunctionAndThisObj((Node)str2, (Node)str1);
        if (i == 1) {
          generateExpression(node, (Node)str1);
          str2 = "call1";
          str1 = "(Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
        } else if (i == 2) {
          generateExpression(node, (Node)str1);
          generateExpression(node.getNext(), (Node)str1);
          str2 = "call2";
          str1 = "(Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
        } else {
          generateCallArgArray((Node)str1, node, false);
          str2 = "callN";
          str1 = "(Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;";
        } 
      } 
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      addOptRuntimeInvoke(str2, str1);
      return;
    } 
    throw Codegen.badTree();
  }
  
  private void visitStandardNew(Node paramNode1, Node paramNode2) {
    if (paramNode1.getType() == 30) {
      Node node = paramNode2.getNext();
      generateExpression(paramNode2, paramNode1);
      this.cfw.addALoad(this.contextLocal);
      this.cfw.addALoad(this.variableObjectLocal);
      generateCallArgArray(paramNode1, node, false);
      addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
      return;
    } 
    throw Codegen.badTree();
  }
  
  private void visitStrictSetName(Node paramNode1, Node paramNode2) {
    String str = paramNode1.getFirstChild().getString();
    while (paramNode2 != null) {
      generateExpression(paramNode2, paramNode1);
      paramNode2 = paramNode2.getNext();
    } 
    this.cfw.addALoad(this.contextLocal);
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addPush(str);
    addScriptRuntimeInvoke("strictSetName", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
  }
  
  private void visitSwitch(Jump paramJump, Node paramNode) {
    generateExpression(paramNode, (Node)paramJump);
    short s = getNewWordLocal();
    this.cfw.addAStore(s);
    paramJump = (Jump)paramNode.getNext();
    while (paramJump != null) {
      if (paramJump.getType() == 116) {
        generateExpression(paramJump.getFirstChild(), (Node)paramJump);
        this.cfw.addALoad(s);
        addScriptRuntimeInvoke("shallowEq", "(Ljava/lang/Object;Ljava/lang/Object;)Z");
        addGoto(paramJump.target, 154);
        paramJump = (Jump)paramJump.getNext();
        continue;
      } 
      throw Codegen.badTree();
    } 
    releaseWordLocal(s);
  }
  
  private void visitTryCatchFinally(Jump paramJump, Node paramNode) {
    short s = getNewWordLocal();
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addAStore(s);
    int i = this.cfw.acquireLabel();
    this.cfw.markLabel(i, (short)0);
    Node node1 = paramJump.target;
    Node node2 = paramJump.getFinally();
    int[] arrayOfInt = new int[5];
    this.exceptionManager.pushExceptionInfo(paramJump);
    if (node1 != null) {
      arrayOfInt[0] = this.cfw.acquireLabel();
      arrayOfInt[1] = this.cfw.acquireLabel();
      arrayOfInt[2] = this.cfw.acquireLabel();
      Context context = Context.getCurrentContext();
      if (context != null && context.hasFeature(13))
        arrayOfInt[3] = this.cfw.acquireLabel(); 
    } 
    if (node2 != null)
      arrayOfInt[4] = this.cfw.acquireLabel(); 
    this.exceptionManager.setHandlers(arrayOfInt, i);
    if (this.isGenerator && node2 != null) {
      FinallyReturnPoint finallyReturnPoint = new FinallyReturnPoint();
      if (this.finallys == null)
        this.finallys = new HashMap<>(); 
      this.finallys.put(node2, finallyReturnPoint);
      this.finallys.put(node2.getNext(), finallyReturnPoint);
    } 
    while (paramNode != null) {
      if (paramNode == node1) {
        int m = getTargetLabel(node1);
        this.exceptionManager.removeHandler(0, m);
        this.exceptionManager.removeHandler(1, m);
        this.exceptionManager.removeHandler(2, m);
        this.exceptionManager.removeHandler(3, m);
      } 
      generateStatement(paramNode);
      paramNode = paramNode.getNext();
    } 
    int k = this.cfw.acquireLabel();
    this.cfw.add(167, k);
    int j = getLocalBlockRegister((Node)paramJump);
    if (node1 != null) {
      int m = node1.labelId();
      int n = arrayOfInt[0];
      int i1 = j;
      generateCatchBlock(0, s, m, i1, n);
      generateCatchBlock(1, s, m, i1, arrayOfInt[1]);
      generateCatchBlock(2, s, m, i1, arrayOfInt[2]);
      Context context = Context.getCurrentContext();
      if (context != null && context.hasFeature(13))
        generateCatchBlock(3, s, m, i1, arrayOfInt[3]); 
    } 
    if (node2 != null) {
      int m = this.cfw.acquireLabel();
      int n = this.cfw.acquireLabel();
      this.cfw.markHandler(m);
      if (!this.isGenerator)
        this.cfw.markLabel(arrayOfInt[4]); 
      this.cfw.addAStore(j);
      this.cfw.addALoad(s);
      this.cfw.addAStore(this.variableObjectLocal);
      int i1 = node2.labelId();
      if (this.isGenerator) {
        addGotoWithReturn(node2);
      } else {
        inlineFinally(node2, arrayOfInt[4], n);
      } 
      this.cfw.addALoad(j);
      if (this.isGenerator)
        this.cfw.add(192, "java/lang/Throwable"); 
      this.cfw.add(191);
      this.cfw.markLabel(n);
      if (this.isGenerator)
        this.cfw.addExceptionHandler(i, i1, m, null); 
    } 
    releaseWordLocal(s);
    this.cfw.markLabel(k);
    if (!this.isGenerator)
      this.exceptionManager.popExceptionInfo(); 
  }
  
  private void visitTypeofname(Node paramNode) {
    if (this.hasVarsInRegs) {
      int i = this.fnCurrent.fnode.getIndexForNameNode(paramNode);
      if (i >= 0) {
        if (this.fnCurrent.isNumberVar(i)) {
          this.cfw.addPush("number");
        } else if (varIsDirectCallParameter(i)) {
          short s1 = this.varRegisters[i];
          this.cfw.addALoad(s1);
          this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
          i = this.cfw.acquireLabel();
          this.cfw.add(165, i);
          short s2 = this.cfw.getStackTop();
          this.cfw.addALoad(s1);
          addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
          int j = this.cfw.acquireLabel();
          this.cfw.add(167, j);
          this.cfw.markLabel(i, s2);
          this.cfw.addPush("number");
          this.cfw.markLabel(j);
        } else {
          this.cfw.addALoad(this.varRegisters[i]);
          addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
        } 
        return;
      } 
    } 
    this.cfw.addALoad(this.variableObjectLocal);
    this.cfw.addPush(paramNode.getString());
    addScriptRuntimeInvoke("typeofName", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/String;");
  }
  
  void generateBodyCode() {
    ScriptNode scriptNode;
    this.isGenerator = Codegen.isGenerator(this.scriptOrFn);
    initBodyGeneration();
    if (this.isGenerator) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("(");
      stringBuilder.append(this.codegen.mainClassSignature);
      stringBuilder.append("Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;");
      String str = stringBuilder.toString();
      ClassFileWriter classFileWriter = this.cfw;
      stringBuilder = new StringBuilder();
      stringBuilder.append(this.codegen.getBodyMethodName(this.scriptOrFn));
      stringBuilder.append("_gen");
      classFileWriter.startMethod(stringBuilder.toString(), str, (short)10);
    } else {
      this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short)10);
    } 
    generatePrologue();
    if (this.fnCurrent != null) {
      Node node = this.scriptOrFn.getLastChild();
    } else {
      scriptNode = this.scriptOrFn;
    } 
    generateStatement((Node)scriptNode);
    generateEpilogue();
    this.cfw.stopMethod((short)(this.localsMax + 1));
    if (this.isGenerator)
      generateGenerator(); 
    if (this.literals != null)
      for (byte b = 0; b < this.literals.size(); b++) {
        Node node = this.literals.get(b);
        int i = node.getType();
        if (i != 66) {
          if (i != 67) {
            Kit.codeBug(Token.typeToName(i));
          } else {
            generateObjectLiteralFactory(node, b + 1);
          } 
        } else {
          generateArrayLiteralFactory(node, b + 1);
        } 
      }  
  }
  
  private class ExceptionManager {
    private LinkedList<ExceptionInfo> exceptionInfo = new LinkedList<>();
    
    private void endCatch(ExceptionInfo param1ExceptionInfo, int param1Int1, int param1Int2) {
      if (param1ExceptionInfo.exceptionStarts[param1Int1] != 0) {
        int i = param1ExceptionInfo.exceptionStarts[param1Int1];
        if (BodyCodegen.this.cfw.getLabelPC(i) != BodyCodegen.this.cfw.getLabelPC(param1Int2))
          BodyCodegen.this.cfw.addExceptionHandler(param1ExceptionInfo.exceptionStarts[param1Int1], param1Int2, param1ExceptionInfo.handlerLabels[param1Int1], BodyCodegen.this.exceptionTypeToName(param1Int1)); 
        return;
      } 
      throw new IllegalStateException("bad exception start");
    }
    
    private ExceptionInfo getTop() {
      return this.exceptionInfo.getLast();
    }
    
    void addHandler(int param1Int1, int param1Int2, int param1Int3) {
      ExceptionInfo exceptionInfo = getTop();
      exceptionInfo.handlerLabels[param1Int1] = param1Int2;
      exceptionInfo.exceptionStarts[param1Int1] = param1Int3;
    }
    
    void markInlineFinallyEnd(Node param1Node, int param1Int) {
      LinkedList<ExceptionInfo> linkedList = this.exceptionInfo;
      ListIterator<ExceptionInfo> listIterator = linkedList.listIterator(linkedList.size());
      while (listIterator.hasPrevious()) {
        ExceptionInfo exceptionInfo = listIterator.previous();
        for (byte b = 0; b < 5; b++) {
          if (exceptionInfo.handlerLabels[b] != 0 && exceptionInfo.currentFinally == param1Node) {
            exceptionInfo.exceptionStarts[b] = param1Int;
            exceptionInfo.currentFinally = null;
          } 
        } 
        if (exceptionInfo.finallyBlock == param1Node)
          break; 
      } 
    }
    
    void markInlineFinallyStart(Node param1Node, int param1Int) {
      LinkedList<ExceptionInfo> linkedList = this.exceptionInfo;
      ListIterator<ExceptionInfo> listIterator = linkedList.listIterator(linkedList.size());
      while (listIterator.hasPrevious()) {
        ExceptionInfo exceptionInfo = listIterator.previous();
        for (byte b = 0; b < 5; b++) {
          if (exceptionInfo.handlerLabels[b] != 0 && exceptionInfo.currentFinally == null) {
            endCatch(exceptionInfo, b, param1Int);
            exceptionInfo.exceptionStarts[b] = 0;
            exceptionInfo.currentFinally = param1Node;
          } 
        } 
        if (exceptionInfo.finallyBlock == param1Node)
          break; 
      } 
    }
    
    void popExceptionInfo() {
      this.exceptionInfo.removeLast();
    }
    
    void pushExceptionInfo(Jump param1Jump) {
      ExceptionInfo exceptionInfo = new ExceptionInfo(param1Jump, BodyCodegen.this.getFinallyAtTarget(param1Jump.getFinally()));
      this.exceptionInfo.add(exceptionInfo);
    }
    
    int removeHandler(int param1Int1, int param1Int2) {
      ExceptionInfo exceptionInfo = getTop();
      if (exceptionInfo.handlerLabels[param1Int1] != 0) {
        int i = exceptionInfo.handlerLabels[param1Int1];
        endCatch(exceptionInfo, param1Int1, param1Int2);
        exceptionInfo.handlerLabels[param1Int1] = 0;
        return i;
      } 
      return 0;
    }
    
    void setHandlers(int[] param1ArrayOfint, int param1Int) {
      for (byte b = 0; b < param1ArrayOfint.length; b++) {
        if (param1ArrayOfint[b] != 0)
          addHandler(b, param1ArrayOfint[b], param1Int); 
      } 
    }
    
    private class ExceptionInfo {
      Node currentFinally;
      
      int[] exceptionStarts;
      
      Node finallyBlock;
      
      int[] handlerLabels;
      
      ExceptionInfo(Jump param2Jump, Node param2Node) {
        this.finallyBlock = param2Node;
        this.handlerLabels = new int[5];
        this.exceptionStarts = new int[5];
        this.currentFinally = null;
      }
    }
  }
  
  private class ExceptionInfo {
    Node currentFinally;
    
    int[] exceptionStarts;
    
    Node finallyBlock;
    
    int[] handlerLabels;
    
    ExceptionInfo(Jump param1Jump, Node param1Node) {
      this.finallyBlock = param1Node;
      this.handlerLabels = new int[5];
      this.exceptionStarts = new int[5];
      this.currentFinally = null;
    }
  }
  
  static class FinallyReturnPoint {
    public List<Integer> jsrPoints = new ArrayList<>();
    
    public int tableLabel = 0;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/BodyCodegen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */