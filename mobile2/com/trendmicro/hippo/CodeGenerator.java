package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.Jump;
import com.trendmicro.hippo.ast.ScriptNode;

class CodeGenerator extends Icode {
  private static final int ECF_TAIL = 1;
  
  private static final int MIN_FIXUP_TABLE_SIZE = 40;
  
  private static final int MIN_LABEL_TABLE_SIZE = 32;
  
  private CompilerEnvirons compilerEnv;
  
  private int doubleTableTop;
  
  private int exceptionTableTop;
  
  private long[] fixupTable;
  
  private int fixupTableTop;
  
  private int iCodeTop;
  
  private InterpreterData itsData;
  
  private boolean itsInFunctionFlag;
  
  private boolean itsInTryFlag;
  
  private int[] labelTable;
  
  private int labelTableTop;
  
  private int lineNumber;
  
  private ObjArray literalIds = new ObjArray();
  
  private int localTop;
  
  private ScriptNode scriptOrFn;
  
  private int stackDepth;
  
  private ObjToIntMap strings = new ObjToIntMap(20);
  
  private void addBackwardGoto(int paramInt1, int paramInt2) {
    int i = this.iCodeTop;
    if (i > paramInt2) {
      addGotoOp(paramInt1);
      resolveGoto(i, paramInt2);
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void addExceptionHandler(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, int paramInt5) {
    int[] arrayOfInt2;
    int i = this.exceptionTableTop;
    int[] arrayOfInt1 = this.itsData.itsExceptionTable;
    if (arrayOfInt1 == null) {
      if (i != 0)
        Kit.codeBug(); 
      arrayOfInt2 = new int[12];
      this.itsData.itsExceptionTable = arrayOfInt2;
    } else {
      arrayOfInt2 = arrayOfInt1;
      if (arrayOfInt1.length == i) {
        arrayOfInt2 = new int[arrayOfInt1.length * 2];
        System.arraycopy(this.itsData.itsExceptionTable, 0, arrayOfInt2, 0, i);
        this.itsData.itsExceptionTable = arrayOfInt2;
      } 
    } 
    arrayOfInt2[i + 0] = paramInt1;
    arrayOfInt2[i + 1] = paramInt2;
    arrayOfInt2[i + 2] = paramInt3;
    arrayOfInt2[i + 3] = paramBoolean;
    arrayOfInt2[i + 4] = paramInt4;
    arrayOfInt2[i + 5] = paramInt5;
    this.exceptionTableTop = i + 6;
  }
  
  private void addGoto(Node paramNode, int paramInt) {
    int i = getTargetLabel(paramNode);
    if (i >= this.labelTableTop)
      Kit.codeBug(); 
    int j = this.labelTable[i];
    if (j != -1) {
      addBackwardGoto(paramInt, j);
    } else {
      j = this.iCodeTop;
      addGotoOp(paramInt);
      paramInt = this.fixupTableTop;
      long[] arrayOfLong = this.fixupTable;
      if (arrayOfLong == null || paramInt == arrayOfLong.length) {
        arrayOfLong = this.fixupTable;
        if (arrayOfLong == null) {
          this.fixupTable = new long[40];
        } else {
          long[] arrayOfLong1 = new long[arrayOfLong.length * 2];
          System.arraycopy(arrayOfLong, 0, arrayOfLong1, 0, paramInt);
          this.fixupTable = arrayOfLong1;
        } 
      } 
      this.fixupTableTop = paramInt + 1;
      this.fixupTable[paramInt] = i << 32L | j;
    } 
  }
  
  private void addGotoOp(int paramInt) {
    byte[] arrayOfByte1 = this.itsData.itsICode;
    int i = this.iCodeTop;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (i + 3 > arrayOfByte1.length)
      arrayOfByte2 = increaseICodeCapacity(3); 
    arrayOfByte2[i] = (byte)(byte)paramInt;
    this.iCodeTop = i + 1 + 2;
  }
  
  private void addIcode(int paramInt) {
    if (Icode.validIcode(paramInt)) {
      addUint8(paramInt & 0xFF);
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void addIndexOp(int paramInt1, int paramInt2) {
    addIndexPrefix(paramInt2);
    if (Icode.validIcode(paramInt1)) {
      addIcode(paramInt1);
    } else {
      addToken(paramInt1);
    } 
  }
  
  private void addIndexPrefix(int paramInt) {
    if (paramInt < 0)
      Kit.codeBug(); 
    if (paramInt < 6) {
      addIcode(-32 - paramInt);
    } else if (paramInt <= 255) {
      addIcode(-38);
      addUint8(paramInt);
    } else if (paramInt <= 65535) {
      addIcode(-39);
      addUint16(paramInt);
    } else {
      addIcode(-40);
      addInt(paramInt);
    } 
  }
  
  private void addInt(int paramInt) {
    byte[] arrayOfByte1 = this.itsData.itsICode;
    int i = this.iCodeTop;
    byte[] arrayOfByte2 = arrayOfByte1;
    if (i + 4 > arrayOfByte1.length)
      arrayOfByte2 = increaseICodeCapacity(4); 
    arrayOfByte2[i] = (byte)(byte)(paramInt >>> 24);
    arrayOfByte2[i + 1] = (byte)(byte)(paramInt >>> 16);
    arrayOfByte2[i + 2] = (byte)(byte)(paramInt >>> 8);
    arrayOfByte2[i + 3] = (byte)(byte)paramInt;
    this.iCodeTop = i + 4;
  }
  
  private void addStringOp(int paramInt, String paramString) {
    addStringPrefix(paramString);
    if (Icode.validIcode(paramInt)) {
      addIcode(paramInt);
    } else {
      addToken(paramInt);
    } 
  }
  
  private void addStringPrefix(String paramString) {
    int i = this.strings.get(paramString, -1);
    int j = i;
    if (i == -1) {
      j = this.strings.size();
      this.strings.put(paramString, j);
    } 
    if (j < 4) {
      addIcode(-41 - j);
    } else if (j <= 255) {
      addIcode(-45);
      addUint8(j);
    } else if (j <= 65535) {
      addIcode(-46);
      addUint16(j);
    } else {
      addIcode(-47);
      addInt(j);
    } 
  }
  
  private void addToken(int paramInt) {
    if (Icode.validTokenCode(paramInt)) {
      addUint8(paramInt);
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void addUint16(int paramInt) {
    if ((0xFFFF0000 & paramInt) == 0) {
      byte[] arrayOfByte1 = this.itsData.itsICode;
      int i = this.iCodeTop;
      byte[] arrayOfByte2 = arrayOfByte1;
      if (i + 2 > arrayOfByte1.length)
        arrayOfByte2 = increaseICodeCapacity(2); 
      arrayOfByte2[i] = (byte)(byte)(paramInt >>> 8);
      arrayOfByte2[i + 1] = (byte)(byte)paramInt;
      this.iCodeTop = i + 2;
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void addUint8(int paramInt) {
    if ((paramInt & 0xFFFFFF00) == 0) {
      byte[] arrayOfByte1 = this.itsData.itsICode;
      int i = this.iCodeTop;
      byte[] arrayOfByte2 = arrayOfByte1;
      if (i == arrayOfByte1.length)
        arrayOfByte2 = increaseICodeCapacity(1); 
      arrayOfByte2[i] = (byte)(byte)paramInt;
      this.iCodeTop = i + 1;
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void addVarOp(int paramInt1, int paramInt2) {
    if (paramInt1 != -7)
      if (paramInt1 != 157) {
        if (paramInt1 == 55 || paramInt1 == 56) {
          if (paramInt2 < 128) {
            if (paramInt1 == 55) {
              paramInt1 = -48;
            } else {
              paramInt1 = -49;
            } 
            addIcode(paramInt1);
            addUint8(paramInt2);
            return;
          } 
        } else {
          throw Kit.codeBug();
        } 
      } else {
        if (paramInt2 < 128) {
          addIcode(-61);
          addUint8(paramInt2);
          return;
        } 
        addIndexOp(-60, paramInt2);
        return;
      }  
    addIndexOp(paramInt1, paramInt2);
  }
  
  private int allocLocal() {
    int i = this.localTop;
    int j = this.localTop + 1;
    this.localTop = j;
    if (j > this.itsData.itsMaxLocals)
      this.itsData.itsMaxLocals = this.localTop; 
    return i;
  }
  
  private RuntimeException badTree(Node paramNode) {
    throw new RuntimeException(paramNode.toString());
  }
  
  private void fixLabelGotos() {
    byte b = 0;
    while (b < this.fixupTableTop) {
      long l = this.fixupTable[b];
      int i = (int)(l >> 32L);
      int j = (int)l;
      i = this.labelTable[i];
      if (i != -1) {
        resolveGoto(j, i);
        b++;
        continue;
      } 
      throw Kit.codeBug();
    } 
    this.fixupTableTop = 0;
  }
  
  private void generateCallFunAndThis(Node paramNode) {
    int i = paramNode.getType();
    if (i != 33 && i != 36) {
      if (i != 39) {
        visitExpression(paramNode, 0);
        addIcode(-18);
        stackChange(1);
      } else {
        addStringOp(-15, paramNode.getString());
        stackChange(2);
      } 
    } else {
      paramNode = paramNode.getFirstChild();
      visitExpression(paramNode, 0);
      paramNode = paramNode.getNext();
      if (i == 33) {
        addStringOp(-16, paramNode.getString());
        stackChange(1);
      } else {
        visitExpression(paramNode, 0);
        addIcode(-17);
      } 
    } 
  }
  
  private void generateFunctionICode() {
    this.itsInFunctionFlag = true;
    FunctionNode functionNode = (FunctionNode)this.scriptOrFn;
    this.itsData.itsFunctionType = functionNode.getFunctionType();
    this.itsData.itsNeedsActivation = functionNode.requiresActivation();
    if (functionNode.getFunctionName() != null)
      this.itsData.itsName = functionNode.getName(); 
    if (functionNode.isGenerator()) {
      addIcode(-62);
      addUint16(functionNode.getBaseLineno() & 0xFFFF);
    } 
    if (functionNode.isInStrictMode())
      this.itsData.isStrict = true; 
    generateICodeFromTree(functionNode.getLastChild());
  }
  
  private void generateICodeFromTree(Node paramNode) {
    generateNestedFunctions();
    generateRegExpLiterals();
    visitStatement(paramNode, 0);
    fixLabelGotos();
    if (this.itsData.itsFunctionType == 0)
      addToken(65); 
    int i = this.itsData.itsICode.length;
    int j = this.iCodeTop;
    if (i != j) {
      byte[] arrayOfByte = new byte[j];
      System.arraycopy(this.itsData.itsICode, 0, arrayOfByte, 0, this.iCodeTop);
      this.itsData.itsICode = arrayOfByte;
    } 
    if (this.strings.size() == 0) {
      this.itsData.itsStringTable = null;
    } else {
      this.itsData.itsStringTable = new String[this.strings.size()];
      ObjToIntMap.Iterator iterator = this.strings.newIterator();
      iterator.start();
      while (!iterator.done()) {
        String str = (String)iterator.getKey();
        j = iterator.getValue();
        if (this.itsData.itsStringTable[j] != null)
          Kit.codeBug(); 
        this.itsData.itsStringTable[j] = str;
        iterator.next();
      } 
    } 
    if (this.doubleTableTop == 0) {
      this.itsData.itsDoubleTable = null;
    } else {
      j = this.itsData.itsDoubleTable.length;
      i = this.doubleTableTop;
      if (j != i) {
        double[] arrayOfDouble = new double[i];
        System.arraycopy(this.itsData.itsDoubleTable, 0, arrayOfDouble, 0, this.doubleTableTop);
        this.itsData.itsDoubleTable = arrayOfDouble;
      } 
    } 
    if (this.exceptionTableTop != 0) {
      i = this.itsData.itsExceptionTable.length;
      j = this.exceptionTableTop;
      if (i != j) {
        int[] arrayOfInt = new int[j];
        System.arraycopy(this.itsData.itsExceptionTable, 0, arrayOfInt, 0, this.exceptionTableTop);
        this.itsData.itsExceptionTable = arrayOfInt;
      } 
    } 
    this.itsData.itsMaxVars = this.scriptOrFn.getParamAndVarCount();
    InterpreterData interpreterData = this.itsData;
    interpreterData.itsMaxFrameArray = interpreterData.itsMaxVars + this.itsData.itsMaxLocals + this.itsData.itsMaxStack;
    this.itsData.argNames = this.scriptOrFn.getParamAndVarNames();
    this.itsData.argIsConst = this.scriptOrFn.getParamAndVarConst();
    this.itsData.argCount = this.scriptOrFn.getParamCount();
    this.itsData.encodedSourceStart = this.scriptOrFn.getEncodedSourceStart();
    this.itsData.encodedSourceEnd = this.scriptOrFn.getEncodedSourceEnd();
    if (this.literalIds.size() != 0)
      this.itsData.literalIds = this.literalIds.toArray(); 
  }
  
  private void generateNestedFunctions() {
    int i = this.scriptOrFn.getFunctionCount();
    if (i == 0)
      return; 
    InterpreterData[] arrayOfInterpreterData = new InterpreterData[i];
    for (int j = 0; j != i; j++) {
      FunctionNode functionNode = this.scriptOrFn.getFunctionNode(j);
      CodeGenerator codeGenerator = new CodeGenerator();
      codeGenerator.compilerEnv = this.compilerEnv;
      codeGenerator.scriptOrFn = (ScriptNode)functionNode;
      codeGenerator.itsData = new InterpreterData(this.itsData);
      codeGenerator.generateFunctionICode();
      arrayOfInterpreterData[j] = codeGenerator.itsData;
    } 
    this.itsData.itsNestedFunctions = arrayOfInterpreterData;
  }
  
  private void generateRegExpLiterals() {
    int i = this.scriptOrFn.getRegexpCount();
    if (i == 0)
      return; 
    Context context = Context.getContext();
    RegExpProxy regExpProxy = ScriptRuntime.checkRegExpProxy(context);
    Object[] arrayOfObject = new Object[i];
    for (int j = 0; j != i; j++)
      arrayOfObject[j] = regExpProxy.compileRegExp(context, this.scriptOrFn.getRegexpString(j), this.scriptOrFn.getRegexpFlags(j)); 
    this.itsData.itsRegExpLiterals = arrayOfObject;
  }
  
  private int getDoubleIndex(double paramDouble) {
    int i = this.doubleTableTop;
    if (i == 0) {
      this.itsData.itsDoubleTable = new double[64];
    } else if (this.itsData.itsDoubleTable.length == i) {
      double[] arrayOfDouble = new double[i * 2];
      System.arraycopy(this.itsData.itsDoubleTable, 0, arrayOfDouble, 0, i);
      this.itsData.itsDoubleTable = arrayOfDouble;
    } 
    this.itsData.itsDoubleTable[i] = paramDouble;
    this.doubleTableTop = i + 1;
    return i;
  }
  
  private int getLocalBlockRef(Node paramNode) {
    return ((Node)paramNode.getProp(3)).getExistingIntProp(2);
  }
  
  private int getTargetLabel(Node paramNode) {
    int i = paramNode.labelId();
    if (i != -1)
      return i; 
    i = this.labelTableTop;
    int[] arrayOfInt = this.labelTable;
    if (arrayOfInt == null || i == arrayOfInt.length) {
      arrayOfInt = this.labelTable;
      if (arrayOfInt == null) {
        this.labelTable = new int[32];
      } else {
        int[] arrayOfInt1 = new int[arrayOfInt.length * 2];
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, i);
        this.labelTable = arrayOfInt1;
      } 
    } 
    this.labelTableTop = i + 1;
    this.labelTable[i] = -1;
    paramNode.labelId(i);
    return i;
  }
  
  private byte[] increaseICodeCapacity(int paramInt) {
    int i = this.itsData.itsICode.length;
    int j = this.iCodeTop;
    if (j + paramInt > i) {
      int k = i * 2;
      i = k;
      if (j + paramInt > k)
        i = j + paramInt; 
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(this.itsData.itsICode, 0, arrayOfByte, 0, j);
      this.itsData.itsICode = arrayOfByte;
      return arrayOfByte;
    } 
    throw Kit.codeBug();
  }
  
  private void markTargetLabel(Node paramNode) {
    int i = getTargetLabel(paramNode);
    if (this.labelTable[i] != -1)
      Kit.codeBug(); 
    this.labelTable[i] = this.iCodeTop;
  }
  
  private void releaseLocal(int paramInt) {
    int i = this.localTop - 1;
    this.localTop = i;
    if (paramInt != i)
      Kit.codeBug(); 
  }
  
  private void resolveForwardGoto(int paramInt) {
    int i = this.iCodeTop;
    if (i >= paramInt + 3) {
      resolveGoto(paramInt, i);
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void resolveGoto(int paramInt1, int paramInt2) {
    int i = paramInt2 - paramInt1;
    if (i < 0 || i > 2) {
      int j = paramInt1 + 1;
      paramInt1 = i;
      if (i != (short)i) {
        if (this.itsData.longJumps == null)
          this.itsData.longJumps = new UintMap(); 
        this.itsData.longJumps.put(j, paramInt2);
        paramInt1 = 0;
      } 
      byte[] arrayOfByte = this.itsData.itsICode;
      arrayOfByte[j] = (byte)(byte)(paramInt1 >> 8);
      arrayOfByte[j + 1] = (byte)(byte)paramInt1;
      return;
    } 
    throw Kit.codeBug();
  }
  
  private void stackChange(int paramInt) {
    if (paramInt <= 0) {
      this.stackDepth += paramInt;
    } else {
      paramInt = this.stackDepth + paramInt;
      if (paramInt > this.itsData.itsMaxStack)
        this.itsData.itsMaxStack = paramInt; 
      this.stackDepth = paramInt;
    } 
  }
  
  private void updateLineNumber(Node paramNode) {
    int i = paramNode.getLineno();
    if (i != this.lineNumber && i >= 0) {
      if (this.itsData.firstLinePC < 0)
        this.itsData.firstLinePC = i; 
      this.lineNumber = i;
      addIcode(-26);
      addUint16(0xFFFF & i);
    } 
  }
  
  private void visitArrayComprehension(Node paramNode1, Node paramNode2, Node paramNode3) {
    visitStatement(paramNode2, this.stackDepth);
    visitExpression(paramNode3, 0);
  }
  
  private void visitExpression(Node paramNode, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getType : ()I
    //   4: istore_3
    //   5: aload_1
    //   6: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   9: astore #4
    //   11: aload_0
    //   12: getfield stackDepth : I
    //   15: istore #5
    //   17: iconst_0
    //   18: istore #6
    //   20: iload_3
    //   21: bipush #90
    //   23: if_icmpeq -> 2086
    //   26: bipush #7
    //   28: istore #7
    //   30: iload_3
    //   31: bipush #103
    //   33: if_icmpeq -> 2003
    //   36: iconst_1
    //   37: istore #8
    //   39: iload_3
    //   40: bipush #110
    //   42: if_icmpeq -> 1950
    //   45: iload_3
    //   46: bipush #127
    //   48: if_icmpeq -> 1914
    //   51: iload_3
    //   52: sipush #143
    //   55: if_icmpeq -> 1853
    //   58: iload_3
    //   59: sipush #147
    //   62: if_icmpeq -> 1805
    //   65: iload_3
    //   66: sipush #160
    //   69: if_icmpeq -> 1757
    //   72: iload_3
    //   73: tableswitch default -> 256, 8 -> 1720, 9 -> 1690, 10 -> 1690, 11 -> 1690, 12 -> 1690, 13 -> 1690, 14 -> 1690, 15 -> 1690, 16 -> 1690, 17 -> 1690, 18 -> 1690, 19 -> 1690, 20 -> 1690, 21 -> 1690, 22 -> 1690, 23 -> 1690, 24 -> 1690, 25 -> 1690, 26 -> 1914, 27 -> 1914, 28 -> 1914, 29 -> 1914, 30 -> 1467, 31 -> 1406, 32 -> 1914, 33 -> 1383, 34 -> 1383, 35 -> 1305, 36 -> 1690, 37 -> 1226, 38 -> 1467, 39 -> 1209, 40 -> 1092, 41 -> 1209, 42 -> 1079, 43 -> 1079, 44 -> 1079, 45 -> 1079, 46 -> 1690, 47 -> 1690, 48 -> 1060, 49 -> 1209
    //   256: iload_3
    //   257: tableswitch default -> 292, 52 -> 1690, 53 -> 1690, 54 -> 1041, 55 -> 1005, 56 -> 961
    //   292: iload_3
    //   293: tableswitch default -> 320, 62 -> 943, 63 -> 943, 64 -> 1079
    //   320: iload_3
    //   321: tableswitch default -> 400, 66 -> 933, 67 -> 933, 68 -> 918, 69 -> 1853, 70 -> 918, 71 -> 1467, 72 -> 894, 73 -> 848, 74 -> 1720, 75 -> 833, 76 -> 833, 77 -> 833, 78 -> 776, 79 -> 776, 80 -> 776, 81 -> 776
    //   400: iload_3
    //   401: tableswitch default -> 432, 105 -> 693, 106 -> 693, 107 -> 683, 108 -> 683
    //   432: iload_3
    //   433: tableswitch default -> 464, 138 -> 604, 139 -> 596, 140 -> 1305, 141 -> 1226
    //   464: iload_3
    //   465: tableswitch default -> 492, 156 -> 558, 157 -> 513, 158 -> 498
    //   492: aload_0
    //   493: aload_1
    //   494: invokespecial badTree : (Lcom/trendmicro/hippo/Node;)Ljava/lang/RuntimeException;
    //   497: athrow
    //   498: aload_0
    //   499: aload_1
    //   500: aload #4
    //   502: aload #4
    //   504: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   507: invokespecial visitArrayComprehension : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   510: goto -> 2134
    //   513: aload_0
    //   514: getfield itsData : Lcom/trendmicro/hippo/InterpreterData;
    //   517: getfield itsNeedsActivation : Z
    //   520: ifeq -> 527
    //   523: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   526: pop
    //   527: aload_0
    //   528: getfield scriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   531: aload #4
    //   533: invokevirtual getIndexForNameNode : (Lcom/trendmicro/hippo/Node;)I
    //   536: istore_2
    //   537: aload_0
    //   538: aload #4
    //   540: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   543: iconst_0
    //   544: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   547: aload_0
    //   548: sipush #157
    //   551: iload_2
    //   552: invokespecial addVarOp : (II)V
    //   555: goto -> 2134
    //   558: aload #4
    //   560: invokevirtual getString : ()Ljava/lang/String;
    //   563: astore_1
    //   564: aload_0
    //   565: aload #4
    //   567: iconst_0
    //   568: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   571: aload_0
    //   572: aload #4
    //   574: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   577: iconst_0
    //   578: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   581: aload_0
    //   582: bipush #-59
    //   584: aload_1
    //   585: invokespecial addStringOp : (ILjava/lang/String;)V
    //   588: aload_0
    //   589: iconst_m1
    //   590: invokespecial stackChange : (I)V
    //   593: goto -> 2134
    //   596: aload_0
    //   597: iconst_1
    //   598: invokespecial stackChange : (I)V
    //   601: goto -> 2134
    //   604: iconst_m1
    //   605: istore #6
    //   607: iload #6
    //   609: istore_2
    //   610: aload_0
    //   611: getfield itsInFunctionFlag : Z
    //   614: ifeq -> 639
    //   617: iload #6
    //   619: istore_2
    //   620: aload_0
    //   621: getfield itsData : Lcom/trendmicro/hippo/InterpreterData;
    //   624: getfield itsNeedsActivation : Z
    //   627: ifne -> 639
    //   630: aload_0
    //   631: getfield scriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   634: aload_1
    //   635: invokevirtual getIndexForNameNode : (Lcom/trendmicro/hippo/Node;)I
    //   638: istore_2
    //   639: iload_2
    //   640: iconst_m1
    //   641: if_icmpne -> 662
    //   644: aload_0
    //   645: bipush #-14
    //   647: aload_1
    //   648: invokevirtual getString : ()Ljava/lang/String;
    //   651: invokespecial addStringOp : (ILjava/lang/String;)V
    //   654: aload_0
    //   655: iconst_1
    //   656: invokespecial stackChange : (I)V
    //   659: goto -> 680
    //   662: aload_0
    //   663: bipush #55
    //   665: iload_2
    //   666: invokespecial addVarOp : (II)V
    //   669: aload_0
    //   670: iconst_1
    //   671: invokespecial stackChange : (I)V
    //   674: aload_0
    //   675: bipush #32
    //   677: invokespecial addToken : (I)V
    //   680: goto -> 2134
    //   683: aload_0
    //   684: aload_1
    //   685: aload #4
    //   687: invokespecial visitIncDec : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   690: goto -> 2134
    //   693: aload_0
    //   694: aload #4
    //   696: iconst_0
    //   697: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   700: aload_0
    //   701: iconst_m1
    //   702: invokespecial addIcode : (I)V
    //   705: aload_0
    //   706: iconst_1
    //   707: invokespecial stackChange : (I)V
    //   710: aload_0
    //   711: getfield iCodeTop : I
    //   714: istore #8
    //   716: iload_3
    //   717: bipush #106
    //   719: if_icmpne -> 729
    //   722: iload #7
    //   724: istore #6
    //   726: goto -> 733
    //   729: bipush #6
    //   731: istore #6
    //   733: aload_0
    //   734: iload #6
    //   736: invokespecial addGotoOp : (I)V
    //   739: aload_0
    //   740: iconst_m1
    //   741: invokespecial stackChange : (I)V
    //   744: aload_0
    //   745: bipush #-4
    //   747: invokespecial addIcode : (I)V
    //   750: aload_0
    //   751: iconst_m1
    //   752: invokespecial stackChange : (I)V
    //   755: aload_0
    //   756: aload #4
    //   758: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   761: iload_2
    //   762: iconst_1
    //   763: iand
    //   764: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   767: aload_0
    //   768: iload #8
    //   770: invokespecial resolveForwardGoto : (I)V
    //   773: goto -> 2134
    //   776: aload_1
    //   777: bipush #16
    //   779: iconst_0
    //   780: invokevirtual getIntProp : (II)I
    //   783: istore #7
    //   785: iconst_0
    //   786: istore_2
    //   787: aload_0
    //   788: aload #4
    //   790: iconst_0
    //   791: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   794: iload_2
    //   795: iconst_1
    //   796: iadd
    //   797: istore #6
    //   799: aload #4
    //   801: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   804: astore_1
    //   805: aload_1
    //   806: astore #4
    //   808: iload #6
    //   810: istore_2
    //   811: aload_1
    //   812: ifnonnull -> 787
    //   815: aload_0
    //   816: iload_3
    //   817: iload #7
    //   819: invokespecial addIndexOp : (II)V
    //   822: aload_0
    //   823: iconst_1
    //   824: iload #6
    //   826: isub
    //   827: invokespecial stackChange : (I)V
    //   830: goto -> 2134
    //   833: aload_0
    //   834: aload #4
    //   836: iconst_0
    //   837: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   840: aload_0
    //   841: iload_3
    //   842: invokespecial addToken : (I)V
    //   845: goto -> 2134
    //   848: aload #4
    //   850: ifnull -> 863
    //   853: aload_0
    //   854: aload #4
    //   856: iconst_0
    //   857: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   860: goto -> 874
    //   863: aload_0
    //   864: bipush #-50
    //   866: invokespecial addIcode : (I)V
    //   869: aload_0
    //   870: iconst_1
    //   871: invokespecial stackChange : (I)V
    //   874: aload_0
    //   875: bipush #73
    //   877: invokespecial addToken : (I)V
    //   880: aload_0
    //   881: aload_1
    //   882: invokevirtual getLineno : ()I
    //   885: ldc 65535
    //   887: iand
    //   888: invokespecial addUint16 : (I)V
    //   891: goto -> 2134
    //   894: aload_0
    //   895: aload #4
    //   897: iconst_0
    //   898: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   901: aload_0
    //   902: iload_3
    //   903: aload_1
    //   904: bipush #17
    //   906: invokevirtual getProp : (I)Ljava/lang/Object;
    //   909: checkcast java/lang/String
    //   912: invokespecial addStringOp : (ILjava/lang/String;)V
    //   915: goto -> 2134
    //   918: aload_0
    //   919: aload #4
    //   921: iconst_0
    //   922: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   925: aload_0
    //   926: iload_3
    //   927: invokespecial addToken : (I)V
    //   930: goto -> 2134
    //   933: aload_0
    //   934: aload_1
    //   935: aload #4
    //   937: invokespecial visitLiteral : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   940: goto -> 2134
    //   943: aload_0
    //   944: iload_3
    //   945: aload_0
    //   946: aload_1
    //   947: invokespecial getLocalBlockRef : (Lcom/trendmicro/hippo/Node;)I
    //   950: invokespecial addIndexOp : (II)V
    //   953: aload_0
    //   954: iconst_1
    //   955: invokespecial stackChange : (I)V
    //   958: goto -> 2134
    //   961: aload_0
    //   962: getfield itsData : Lcom/trendmicro/hippo/InterpreterData;
    //   965: getfield itsNeedsActivation : Z
    //   968: ifeq -> 975
    //   971: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   974: pop
    //   975: aload_0
    //   976: getfield scriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   979: aload #4
    //   981: invokevirtual getIndexForNameNode : (Lcom/trendmicro/hippo/Node;)I
    //   984: istore_2
    //   985: aload_0
    //   986: aload #4
    //   988: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   991: iconst_0
    //   992: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   995: aload_0
    //   996: bipush #56
    //   998: iload_2
    //   999: invokespecial addVarOp : (II)V
    //   1002: goto -> 2134
    //   1005: aload_0
    //   1006: getfield itsData : Lcom/trendmicro/hippo/InterpreterData;
    //   1009: getfield itsNeedsActivation : Z
    //   1012: ifeq -> 1019
    //   1015: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   1018: pop
    //   1019: aload_0
    //   1020: bipush #55
    //   1022: aload_0
    //   1023: getfield scriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   1026: aload_1
    //   1027: invokevirtual getIndexForNameNode : (Lcom/trendmicro/hippo/Node;)I
    //   1030: invokespecial addVarOp : (II)V
    //   1033: aload_0
    //   1034: iconst_1
    //   1035: invokespecial stackChange : (I)V
    //   1038: goto -> 2134
    //   1041: aload_0
    //   1042: bipush #54
    //   1044: aload_0
    //   1045: aload_1
    //   1046: invokespecial getLocalBlockRef : (Lcom/trendmicro/hippo/Node;)I
    //   1049: invokespecial addIndexOp : (II)V
    //   1052: aload_0
    //   1053: iconst_1
    //   1054: invokespecial stackChange : (I)V
    //   1057: goto -> 2134
    //   1060: aload_0
    //   1061: bipush #48
    //   1063: aload_1
    //   1064: iconst_4
    //   1065: invokevirtual getExistingIntProp : (I)I
    //   1068: invokespecial addIndexOp : (II)V
    //   1071: aload_0
    //   1072: iconst_1
    //   1073: invokespecial stackChange : (I)V
    //   1076: goto -> 2134
    //   1079: aload_0
    //   1080: iload_3
    //   1081: invokespecial addToken : (I)V
    //   1084: aload_0
    //   1085: iconst_1
    //   1086: invokespecial stackChange : (I)V
    //   1089: goto -> 2134
    //   1092: aload_1
    //   1093: invokevirtual getDouble : ()D
    //   1096: dstore #9
    //   1098: dload #9
    //   1100: d2i
    //   1101: istore_2
    //   1102: iload_2
    //   1103: i2d
    //   1104: dload #9
    //   1106: dcmpl
    //   1107: ifne -> 1189
    //   1110: iload_2
    //   1111: ifne -> 1138
    //   1114: aload_0
    //   1115: bipush #-51
    //   1117: invokespecial addIcode : (I)V
    //   1120: dconst_1
    //   1121: dload #9
    //   1123: ddiv
    //   1124: dconst_0
    //   1125: dcmpg
    //   1126: ifge -> 1201
    //   1129: aload_0
    //   1130: bipush #29
    //   1132: invokespecial addToken : (I)V
    //   1135: goto -> 1201
    //   1138: iload_2
    //   1139: iconst_1
    //   1140: if_icmpne -> 1152
    //   1143: aload_0
    //   1144: bipush #-52
    //   1146: invokespecial addIcode : (I)V
    //   1149: goto -> 1201
    //   1152: iload_2
    //   1153: i2s
    //   1154: iload_2
    //   1155: if_icmpne -> 1175
    //   1158: aload_0
    //   1159: bipush #-27
    //   1161: invokespecial addIcode : (I)V
    //   1164: aload_0
    //   1165: iload_2
    //   1166: ldc 65535
    //   1168: iand
    //   1169: invokespecial addUint16 : (I)V
    //   1172: goto -> 1201
    //   1175: aload_0
    //   1176: bipush #-28
    //   1178: invokespecial addIcode : (I)V
    //   1181: aload_0
    //   1182: iload_2
    //   1183: invokespecial addInt : (I)V
    //   1186: goto -> 1201
    //   1189: aload_0
    //   1190: bipush #40
    //   1192: aload_0
    //   1193: dload #9
    //   1195: invokespecial getDoubleIndex : (D)I
    //   1198: invokespecial addIndexOp : (II)V
    //   1201: aload_0
    //   1202: iconst_1
    //   1203: invokespecial stackChange : (I)V
    //   1206: goto -> 2134
    //   1209: aload_0
    //   1210: iload_3
    //   1211: aload_1
    //   1212: invokevirtual getString : ()Ljava/lang/String;
    //   1215: invokespecial addStringOp : (ILjava/lang/String;)V
    //   1218: aload_0
    //   1219: iconst_1
    //   1220: invokespecial stackChange : (I)V
    //   1223: goto -> 2134
    //   1226: aload_0
    //   1227: aload #4
    //   1229: iconst_0
    //   1230: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1233: aload #4
    //   1235: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1238: astore_1
    //   1239: aload_0
    //   1240: aload_1
    //   1241: iconst_0
    //   1242: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1245: aload_1
    //   1246: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1249: astore_1
    //   1250: iload_3
    //   1251: sipush #141
    //   1254: if_icmpne -> 1284
    //   1257: aload_0
    //   1258: bipush #-2
    //   1260: invokespecial addIcode : (I)V
    //   1263: aload_0
    //   1264: iconst_2
    //   1265: invokespecial stackChange : (I)V
    //   1268: aload_0
    //   1269: bipush #36
    //   1271: invokespecial addToken : (I)V
    //   1274: aload_0
    //   1275: iconst_m1
    //   1276: invokespecial stackChange : (I)V
    //   1279: aload_0
    //   1280: iconst_m1
    //   1281: invokespecial stackChange : (I)V
    //   1284: aload_0
    //   1285: aload_1
    //   1286: iconst_0
    //   1287: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1290: aload_0
    //   1291: bipush #37
    //   1293: invokespecial addToken : (I)V
    //   1296: aload_0
    //   1297: bipush #-2
    //   1299: invokespecial stackChange : (I)V
    //   1302: goto -> 2134
    //   1305: aload_0
    //   1306: aload #4
    //   1308: iconst_0
    //   1309: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1312: aload #4
    //   1314: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1317: astore #4
    //   1319: aload #4
    //   1321: invokevirtual getString : ()Ljava/lang/String;
    //   1324: astore_1
    //   1325: aload #4
    //   1327: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1330: astore #4
    //   1332: iload_3
    //   1333: sipush #140
    //   1336: if_icmpne -> 1361
    //   1339: aload_0
    //   1340: iconst_m1
    //   1341: invokespecial addIcode : (I)V
    //   1344: aload_0
    //   1345: iconst_1
    //   1346: invokespecial stackChange : (I)V
    //   1349: aload_0
    //   1350: bipush #33
    //   1352: aload_1
    //   1353: invokespecial addStringOp : (ILjava/lang/String;)V
    //   1356: aload_0
    //   1357: iconst_m1
    //   1358: invokespecial stackChange : (I)V
    //   1361: aload_0
    //   1362: aload #4
    //   1364: iconst_0
    //   1365: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1368: aload_0
    //   1369: bipush #35
    //   1371: aload_1
    //   1372: invokespecial addStringOp : (ILjava/lang/String;)V
    //   1375: aload_0
    //   1376: iconst_m1
    //   1377: invokespecial stackChange : (I)V
    //   1380: goto -> 2134
    //   1383: aload_0
    //   1384: aload #4
    //   1386: iconst_0
    //   1387: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1390: aload_0
    //   1391: iload_3
    //   1392: aload #4
    //   1394: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1397: invokevirtual getString : ()Ljava/lang/String;
    //   1400: invokespecial addStringOp : (ILjava/lang/String;)V
    //   1403: goto -> 2134
    //   1406: aload #4
    //   1408: invokevirtual getType : ()I
    //   1411: bipush #49
    //   1413: if_icmpne -> 1422
    //   1416: iload #8
    //   1418: istore_2
    //   1419: goto -> 1424
    //   1422: iconst_0
    //   1423: istore_2
    //   1424: aload_0
    //   1425: aload #4
    //   1427: iconst_0
    //   1428: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1431: aload_0
    //   1432: aload #4
    //   1434: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1437: iconst_0
    //   1438: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1441: iload_2
    //   1442: ifeq -> 1453
    //   1445: aload_0
    //   1446: iconst_0
    //   1447: invokespecial addIcode : (I)V
    //   1450: goto -> 1459
    //   1453: aload_0
    //   1454: bipush #31
    //   1456: invokespecial addToken : (I)V
    //   1459: aload_0
    //   1460: iconst_m1
    //   1461: invokespecial stackChange : (I)V
    //   1464: goto -> 2134
    //   1467: iload_3
    //   1468: bipush #30
    //   1470: if_icmpne -> 1483
    //   1473: aload_0
    //   1474: aload #4
    //   1476: iconst_0
    //   1477: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1480: goto -> 1489
    //   1483: aload_0
    //   1484: aload #4
    //   1486: invokespecial generateCallFunAndThis : (Lcom/trendmicro/hippo/Node;)V
    //   1489: iconst_0
    //   1490: istore #7
    //   1492: aload #4
    //   1494: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1497: astore #11
    //   1499: aload #11
    //   1501: astore #4
    //   1503: aload #11
    //   1505: ifnull -> 1521
    //   1508: aload_0
    //   1509: aload #4
    //   1511: iconst_0
    //   1512: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1515: iinc #7, 1
    //   1518: goto -> 1492
    //   1521: aload_1
    //   1522: bipush #10
    //   1524: iconst_0
    //   1525: invokevirtual getIntProp : (II)I
    //   1528: istore #8
    //   1530: iload_3
    //   1531: bipush #71
    //   1533: if_icmpeq -> 1588
    //   1536: iload #8
    //   1538: ifeq -> 1588
    //   1541: aload_0
    //   1542: bipush #-21
    //   1544: iload #7
    //   1546: invokespecial addIndexOp : (II)V
    //   1549: aload_0
    //   1550: iload #8
    //   1552: invokespecial addUint8 : (I)V
    //   1555: iload #6
    //   1557: istore_2
    //   1558: iload_3
    //   1559: bipush #30
    //   1561: if_icmpne -> 1566
    //   1564: iconst_1
    //   1565: istore_2
    //   1566: aload_0
    //   1567: iload_2
    //   1568: invokespecial addUint8 : (I)V
    //   1571: aload_0
    //   1572: aload_0
    //   1573: getfield lineNumber : I
    //   1576: ldc 65535
    //   1578: iand
    //   1579: invokespecial addUint16 : (I)V
    //   1582: iload_3
    //   1583: istore #6
    //   1585: goto -> 1641
    //   1588: iload_3
    //   1589: istore #6
    //   1591: iload_3
    //   1592: bipush #38
    //   1594: if_icmpne -> 1633
    //   1597: iload_3
    //   1598: istore #6
    //   1600: iload_2
    //   1601: iconst_1
    //   1602: iand
    //   1603: ifeq -> 1633
    //   1606: iload_3
    //   1607: istore #6
    //   1609: aload_0
    //   1610: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   1613: invokevirtual isGenerateDebugInfo : ()Z
    //   1616: ifne -> 1633
    //   1619: iload_3
    //   1620: istore #6
    //   1622: aload_0
    //   1623: getfield itsInTryFlag : Z
    //   1626: ifne -> 1633
    //   1629: bipush #-55
    //   1631: istore #6
    //   1633: aload_0
    //   1634: iload #6
    //   1636: iload #7
    //   1638: invokespecial addIndexOp : (II)V
    //   1641: iload #6
    //   1643: bipush #30
    //   1645: if_icmpne -> 1658
    //   1648: aload_0
    //   1649: iload #7
    //   1651: ineg
    //   1652: invokespecial stackChange : (I)V
    //   1655: goto -> 1666
    //   1658: aload_0
    //   1659: iconst_m1
    //   1660: iload #7
    //   1662: isub
    //   1663: invokespecial stackChange : (I)V
    //   1666: iload #7
    //   1668: aload_0
    //   1669: getfield itsData : Lcom/trendmicro/hippo/InterpreterData;
    //   1672: getfield itsMaxCalleeArgs : I
    //   1675: if_icmple -> 1687
    //   1678: aload_0
    //   1679: getfield itsData : Lcom/trendmicro/hippo/InterpreterData;
    //   1682: iload #7
    //   1684: putfield itsMaxCalleeArgs : I
    //   1687: goto -> 2134
    //   1690: aload_0
    //   1691: aload #4
    //   1693: iconst_0
    //   1694: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1697: aload_0
    //   1698: aload #4
    //   1700: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1703: iconst_0
    //   1704: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1707: aload_0
    //   1708: iload_3
    //   1709: invokespecial addToken : (I)V
    //   1712: aload_0
    //   1713: iconst_m1
    //   1714: invokespecial stackChange : (I)V
    //   1717: goto -> 2134
    //   1720: aload #4
    //   1722: invokevirtual getString : ()Ljava/lang/String;
    //   1725: astore_1
    //   1726: aload_0
    //   1727: aload #4
    //   1729: iconst_0
    //   1730: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1733: aload_0
    //   1734: aload #4
    //   1736: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1739: iconst_0
    //   1740: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1743: aload_0
    //   1744: iload_3
    //   1745: aload_1
    //   1746: invokespecial addStringOp : (ILjava/lang/String;)V
    //   1749: aload_0
    //   1750: iconst_m1
    //   1751: invokespecial stackChange : (I)V
    //   1754: goto -> 2134
    //   1757: aload_1
    //   1758: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1761: astore_1
    //   1762: aload_1
    //   1763: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1766: astore #4
    //   1768: aload_0
    //   1769: aload_1
    //   1770: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1773: iconst_0
    //   1774: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1777: aload_0
    //   1778: iconst_2
    //   1779: invokespecial addToken : (I)V
    //   1782: aload_0
    //   1783: iconst_m1
    //   1784: invokespecial stackChange : (I)V
    //   1787: aload_0
    //   1788: aload #4
    //   1790: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1793: iconst_0
    //   1794: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1797: aload_0
    //   1798: iconst_3
    //   1799: invokespecial addToken : (I)V
    //   1802: goto -> 2134
    //   1805: aload_0
    //   1806: aload_1
    //   1807: invokespecial updateLineNumber : (Lcom/trendmicro/hippo/Node;)V
    //   1810: aload_0
    //   1811: aload #4
    //   1813: iconst_0
    //   1814: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1817: aload_0
    //   1818: bipush #-53
    //   1820: invokespecial addIcode : (I)V
    //   1823: aload_0
    //   1824: iconst_m1
    //   1825: invokespecial stackChange : (I)V
    //   1828: aload_0
    //   1829: getfield iCodeTop : I
    //   1832: istore_2
    //   1833: aload_0
    //   1834: aload #4
    //   1836: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1839: iconst_0
    //   1840: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1843: aload_0
    //   1844: bipush #-54
    //   1846: iload_2
    //   1847: invokespecial addBackwardGoto : (II)V
    //   1850: goto -> 2134
    //   1853: aload_0
    //   1854: aload #4
    //   1856: iconst_0
    //   1857: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1860: aload #4
    //   1862: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1865: astore_1
    //   1866: iload_3
    //   1867: sipush #143
    //   1870: if_icmpne -> 1894
    //   1873: aload_0
    //   1874: iconst_m1
    //   1875: invokespecial addIcode : (I)V
    //   1878: aload_0
    //   1879: iconst_1
    //   1880: invokespecial stackChange : (I)V
    //   1883: aload_0
    //   1884: bipush #68
    //   1886: invokespecial addToken : (I)V
    //   1889: aload_0
    //   1890: iconst_m1
    //   1891: invokespecial stackChange : (I)V
    //   1894: aload_0
    //   1895: aload_1
    //   1896: iconst_0
    //   1897: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1900: aload_0
    //   1901: bipush #69
    //   1903: invokespecial addToken : (I)V
    //   1906: aload_0
    //   1907: iconst_m1
    //   1908: invokespecial stackChange : (I)V
    //   1911: goto -> 2134
    //   1914: aload_0
    //   1915: aload #4
    //   1917: iconst_0
    //   1918: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   1921: iload_3
    //   1922: bipush #127
    //   1924: if_icmpne -> 1942
    //   1927: aload_0
    //   1928: bipush #-4
    //   1930: invokespecial addIcode : (I)V
    //   1933: aload_0
    //   1934: bipush #-50
    //   1936: invokespecial addIcode : (I)V
    //   1939: goto -> 2134
    //   1942: aload_0
    //   1943: iload_3
    //   1944: invokespecial addToken : (I)V
    //   1947: goto -> 2134
    //   1950: aload_1
    //   1951: iconst_1
    //   1952: invokevirtual getExistingIntProp : (I)I
    //   1955: istore_2
    //   1956: aload_0
    //   1957: getfield scriptOrFn : Lcom/trendmicro/hippo/ast/ScriptNode;
    //   1960: iload_2
    //   1961: invokevirtual getFunctionNode : (I)Lcom/trendmicro/hippo/ast/FunctionNode;
    //   1964: astore_1
    //   1965: aload_1
    //   1966: invokevirtual getFunctionType : ()I
    //   1969: iconst_2
    //   1970: if_icmpeq -> 1988
    //   1973: aload_1
    //   1974: invokevirtual getFunctionType : ()I
    //   1977: iconst_4
    //   1978: if_icmpne -> 1984
    //   1981: goto -> 1988
    //   1984: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   1987: athrow
    //   1988: aload_0
    //   1989: bipush #-19
    //   1991: iload_2
    //   1992: invokespecial addIndexOp : (II)V
    //   1995: aload_0
    //   1996: iconst_1
    //   1997: invokespecial stackChange : (I)V
    //   2000: goto -> 2134
    //   2003: aload #4
    //   2005: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   2008: astore_1
    //   2009: aload_1
    //   2010: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   2013: astore #11
    //   2015: aload_0
    //   2016: aload #4
    //   2018: iconst_0
    //   2019: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   2022: aload_0
    //   2023: getfield iCodeTop : I
    //   2026: istore_3
    //   2027: aload_0
    //   2028: bipush #7
    //   2030: invokespecial addGotoOp : (I)V
    //   2033: aload_0
    //   2034: iconst_m1
    //   2035: invokespecial stackChange : (I)V
    //   2038: aload_0
    //   2039: aload_1
    //   2040: iload_2
    //   2041: iconst_1
    //   2042: iand
    //   2043: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   2046: aload_0
    //   2047: getfield iCodeTop : I
    //   2050: istore #6
    //   2052: aload_0
    //   2053: iconst_5
    //   2054: invokespecial addGotoOp : (I)V
    //   2057: aload_0
    //   2058: iload_3
    //   2059: invokespecial resolveForwardGoto : (I)V
    //   2062: aload_0
    //   2063: iload #5
    //   2065: putfield stackDepth : I
    //   2068: aload_0
    //   2069: aload #11
    //   2071: iload_2
    //   2072: iconst_1
    //   2073: iand
    //   2074: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   2077: aload_0
    //   2078: iload #6
    //   2080: invokespecial resolveForwardGoto : (I)V
    //   2083: goto -> 2134
    //   2086: aload_1
    //   2087: invokevirtual getLastChild : ()Lcom/trendmicro/hippo/Node;
    //   2090: astore_1
    //   2091: aload #4
    //   2093: aload_1
    //   2094: if_acmpeq -> 2125
    //   2097: aload_0
    //   2098: aload #4
    //   2100: iconst_0
    //   2101: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   2104: aload_0
    //   2105: bipush #-4
    //   2107: invokespecial addIcode : (I)V
    //   2110: aload_0
    //   2111: iconst_m1
    //   2112: invokespecial stackChange : (I)V
    //   2115: aload #4
    //   2117: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   2120: astore #4
    //   2122: goto -> 2091
    //   2125: aload_0
    //   2126: aload #4
    //   2128: iload_2
    //   2129: iconst_1
    //   2130: iand
    //   2131: invokespecial visitExpression : (Lcom/trendmicro/hippo/Node;I)V
    //   2134: iload #5
    //   2136: iconst_1
    //   2137: iadd
    //   2138: aload_0
    //   2139: getfield stackDepth : I
    //   2142: if_icmpeq -> 2149
    //   2145: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   2148: pop
    //   2149: return
  }
  
  private void visitIncDec(Node paramNode1, Node paramNode2) {
    int i = paramNode1.getExistingIntProp(13);
    int j = paramNode2.getType();
    if (j != 33) {
      if (j != 36) {
        if (j != 39) {
          if (j != 55) {
            if (j == 68) {
              visitExpression(paramNode2.getFirstChild(), 0);
              addIcode(-11);
              addUint8(i);
            } else {
              throw badTree(paramNode1);
            } 
          } else {
            if (this.itsData.itsNeedsActivation)
              Kit.codeBug(); 
            addVarOp(-7, this.scriptOrFn.getIndexForNameNode(paramNode2));
            addUint8(i);
            stackChange(1);
          } 
        } else {
          addStringOp(-8, paramNode2.getString());
          addUint8(i);
          stackChange(1);
        } 
      } else {
        paramNode1 = paramNode2.getFirstChild();
        visitExpression(paramNode1, 0);
        visitExpression(paramNode1.getNext(), 0);
        addIcode(-10);
        addUint8(i);
        stackChange(-1);
      } 
    } else {
      paramNode1 = paramNode2.getFirstChild();
      visitExpression(paramNode1, 0);
      addStringOp(-9, paramNode1.getNext().getString());
      addUint8(i);
    } 
  }
  
  private void visitLiteral(Node paramNode1, Node paramNode2) {
    int j;
    Object[] arrayOfObject;
    int i = paramNode1.getType();
    Node node = null;
    if (i == 66) {
      j = 0;
      Node node1;
      for (node1 = paramNode2; node1 != null; node1 = node1.getNext())
        j++; 
      node1 = node;
    } else if (i == 67) {
      arrayOfObject = (Object[])paramNode1.getProp(12);
      j = arrayOfObject.length;
    } else {
      throw badTree(paramNode1);
    } 
    addIndexOp(-29, j);
    stackChange(2);
    while (paramNode2 != null) {
      j = paramNode2.getType();
      if (j == 152) {
        visitExpression(paramNode2.getFirstChild(), 0);
        addIcode(-57);
      } else if (j == 153) {
        visitExpression(paramNode2.getFirstChild(), 0);
        addIcode(-58);
      } else if (j == 164) {
        visitExpression(paramNode2.getFirstChild(), 0);
        addIcode(-30);
      } else {
        visitExpression(paramNode2, 0);
        addIcode(-30);
      } 
      stackChange(-1);
      paramNode2 = paramNode2.getNext();
    } 
    if (i == 66) {
      int[] arrayOfInt = (int[])paramNode1.getProp(11);
      if (arrayOfInt == null) {
        addToken(66);
      } else {
        j = this.literalIds.size();
        this.literalIds.add(arrayOfInt);
        addIndexOp(-31, j);
      } 
    } else {
      j = this.literalIds.size();
      this.literalIds.add(arrayOfObject);
      addIndexOp(67, j);
    } 
    stackChange(-1);
  }
  
  private void visitStatement(Node paramNode, int paramInt) {
    int i = paramNode.getType();
    Node node = paramNode.getFirstChild();
    if (i != -62) {
      Node node1;
      if (i != 65) {
        Jump jump;
        int j = 1;
        if (i != 82) {
          int k = -5;
          if (i != 110) {
            if (i != 115) {
              if (i != 124) {
                if (i != 126) {
                  if (i != 142) {
                    if (i != 161) {
                      if (i != 50) {
                        if (i != 51) {
                          String str;
                          Node node2;
                          switch (i) {
                            default:
                              switch (i) {
                                default:
                                  node2 = node;
                                  switch (i) {
                                    default:
                                      throw badTree(paramNode);
                                    case 136:
                                      addGoto(((Jump)paramNode).target, -23);
                                      break;
                                    case 134:
                                    case 135:
                                      updateLineNumber(paramNode);
                                      visitExpression(node, 0);
                                      j = k;
                                      if (i == 134)
                                        j = -4; 
                                      addIcode(j);
                                      stackChange(-1);
                                      break;
                                    case 132:
                                      markTargetLabel(paramNode);
                                      break;
                                    case 129:
                                    case 130:
                                    case 131:
                                    case 133:
                                      updateLineNumber(paramNode);
                                      node2 = node;
                                      break;
                                    case 137:
                                      break;
                                  } 
                                  while (node2 != null) {
                                    visitStatement(node2, paramInt);
                                    node2 = node2.getNext();
                                  } 
                                  if (this.stackDepth == paramInt)
                                    return; 
                                  throw Kit.codeBug();
                                case 58:
                                case 59:
                                case 60:
                                case 61:
                                  visitExpression(node, 0);
                                  addIndexOp(i, getLocalBlockRef(paramNode));
                                  stackChange(-1);
                                  break;
                                case 57:
                                  k = getLocalBlockRef(paramNode);
                                  i = paramNode.getExistingIntProp(14);
                                  str = node.getString();
                                  visitExpression(node.getNext(), 0);
                                  addStringPrefix(str);
                                  addIndexPrefix(k);
                                  addToken(57);
                                  if (i == 0)
                                    j = 0; 
                                  addUint8(j);
                                  stackChange(-1);
                                  break;
                              } 
                              while (node2 != null) {
                                visitStatement(node2, paramInt);
                                node2 = node2.getNext();
                              } 
                              if (this.stackDepth == paramInt)
                                return; 
                              throw Kit.codeBug();
                            case 6:
                            case 7:
                              node1 = ((Jump)str).target;
                              visitExpression(node, 0);
                              addGoto(node1, i);
                              stackChange(-1);
                              break;
                            case 5:
                              addGoto(((Jump)node1).target, i);
                              break;
                            case 4:
                              updateLineNumber(node1);
                              if (node1.getIntProp(20, 0) != 0) {
                                addIcode(-63);
                                addUint16(0xFFFF & this.lineNumber);
                                break;
                              } 
                              if (node != null) {
                                visitExpression(node, 1);
                                addToken(4);
                                stackChange(-1);
                                break;
                              } 
                              addIcode(-22);
                              break;
                            case 3:
                              addToken(3);
                              break;
                            case 2:
                              visitExpression(node, 0);
                              addToken(2);
                              stackChange(-1);
                              break;
                          } 
                        } else {
                          updateLineNumber(node1);
                          addIndexOp(51, getLocalBlockRef(node1));
                        } 
                      } else {
                        updateLineNumber(node1);
                        visitExpression(node, 0);
                        addToken(50);
                        addUint16(0xFFFF & this.lineNumber);
                        stackChange(-1);
                      } 
                    } else {
                      addIcode(-64);
                    } 
                  } else {
                    j = allocLocal();
                    node1.putIntProp(2, j);
                    updateLineNumber(node1);
                    while (node != null) {
                      visitStatement(node, paramInt);
                      node = node.getNext();
                    } 
                    addIndexOp(-56, j);
                    releaseLocal(j);
                  } 
                } else {
                  stackChange(1);
                  j = getLocalBlockRef(node1);
                  addIndexOp(-24, j);
                  stackChange(-1);
                  while (node != null) {
                    visitStatement(node, paramInt);
                    node = node.getNext();
                  } 
                  addIndexOp(-25, j);
                } 
              } else {
              
              } 
            } else {
              updateLineNumber(node1);
              visitExpression(node, 0);
              jump = (Jump)node.getNext();
              while (jump != null) {
                if (jump.getType() == 116) {
                  node = jump.getFirstChild();
                  addIcode(-1);
                  stackChange(1);
                  visitExpression(node, 0);
                  addToken(46);
                  stackChange(-1);
                  addGoto(jump.target, -6);
                  stackChange(-1);
                  jump = (Jump)jump.getNext();
                  continue;
                } 
                throw badTree(jump);
              } 
              addIcode(-4);
              stackChange(-1);
            } 
          } else {
            k = jump.getExistingIntProp(1);
            j = this.scriptOrFn.getFunctionNode(k).getFunctionType();
            if (j == 3) {
              addIndexOp(-20, k);
            } else if (j != 1) {
              throw Kit.codeBug();
            } 
            if (!this.itsInFunctionFlag) {
              addIndexOp(-19, k);
              stackChange(1);
              addIcode(-5);
              stackChange(-1);
            } 
          } 
        } else {
          jump = jump;
          i = getLocalBlockRef((Node)jump);
          int k = allocLocal();
          addIndexOp(-13, k);
          j = this.iCodeTop;
          boolean bool = this.itsInTryFlag;
          this.itsInTryFlag = true;
          while (node != null) {
            visitStatement(node, paramInt);
            node = node.getNext();
          } 
          this.itsInTryFlag = bool;
          node = jump.target;
          if (node != null) {
            int m = this.labelTable[getTargetLabel(node)];
            addExceptionHandler(j, m, m, false, i, k);
          } 
          node1 = jump.getFinally();
          if (node1 != null) {
            int m = this.labelTable[getTargetLabel(node1)];
            addExceptionHandler(j, m, m, true, i, k);
          } 
          addIndexOp(-56, k);
          releaseLocal(k);
        } 
      } else {
        updateLineNumber(node1);
        addToken(65);
      } 
    } 
    if (this.stackDepth == paramInt)
      return; 
    throw Kit.codeBug();
  }
  
  public InterpreterData compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean) {
    this.compilerEnv = paramCompilerEnvirons;
    (new NodeTransformer()).transform(paramScriptNode, paramCompilerEnvirons);
    if (paramBoolean) {
      this.scriptOrFn = (ScriptNode)paramScriptNode.getFunctionNode(0);
    } else {
      this.scriptOrFn = paramScriptNode;
    } 
    InterpreterData interpreterData = new InterpreterData(paramCompilerEnvirons.getLanguageVersion(), this.scriptOrFn.getSourceName(), paramString, this.scriptOrFn.isInStrictMode());
    this.itsData = interpreterData;
    interpreterData.topLevel = true;
    if (paramBoolean) {
      generateFunctionICode();
    } else {
      generateICodeFromTree((Node)this.scriptOrFn);
    } 
    return this.itsData;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/CodeGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */