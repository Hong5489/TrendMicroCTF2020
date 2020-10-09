package com.trendmicro.hippo.regexp;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.IdScriptableObject;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.TopLevel;
import com.trendmicro.hippo.Undefined;

public class NativeRegExp extends IdScriptableObject implements Function {
  private static final int ANCHOR_BOL = -2;
  
  private static final int INDEX_LEN = 2;
  
  private static final int Id_compile = 1;
  
  private static final int Id_exec = 4;
  
  private static final int Id_global = 3;
  
  private static final int Id_ignoreCase = 4;
  
  private static final int Id_lastIndex = 1;
  
  private static final int Id_multiline = 5;
  
  private static final int Id_prefix = 6;
  
  private static final int Id_source = 2;
  
  private static final int Id_test = 5;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  public static final int JSREG_FOLD = 2;
  
  public static final int JSREG_GLOB = 1;
  
  public static final int JSREG_MULTILINE = 4;
  
  public static final int MATCH = 1;
  
  private static final int MAX_INSTANCE_ID = 5;
  
  private static final int MAX_PROTOTYPE_ID = 6;
  
  public static final int PREFIX = 2;
  
  private static final Object REGEXP_TAG = new Object();
  
  private static final byte REOP_ALNUM = 9;
  
  private static final byte REOP_ALT = 31;
  
  private static final byte REOP_ALTPREREQ = 53;
  
  private static final byte REOP_ALTPREREQ2 = 55;
  
  private static final byte REOP_ALTPREREQi = 54;
  
  private static final byte REOP_ASSERT = 41;
  
  private static final byte REOP_ASSERTNOTTEST = 44;
  
  private static final byte REOP_ASSERTTEST = 43;
  
  private static final byte REOP_ASSERT_NOT = 42;
  
  private static final byte REOP_BACKREF = 13;
  
  private static final byte REOP_BOL = 2;
  
  private static final byte REOP_CLASS = 22;
  
  private static final byte REOP_DIGIT = 7;
  
  private static final byte REOP_DOT = 6;
  
  private static final byte REOP_EMPTY = 1;
  
  private static final byte REOP_END = 57;
  
  private static final byte REOP_ENDCHILD = 49;
  
  private static final byte REOP_EOL = 3;
  
  private static final byte REOP_FLAT = 14;
  
  private static final byte REOP_FLAT1 = 15;
  
  private static final byte REOP_FLAT1i = 17;
  
  private static final byte REOP_FLATi = 16;
  
  private static final byte REOP_JUMP = 32;
  
  private static final byte REOP_LPAREN = 29;
  
  private static final byte REOP_MINIMALOPT = 47;
  
  private static final byte REOP_MINIMALPLUS = 46;
  
  private static final byte REOP_MINIMALQUANT = 48;
  
  private static final byte REOP_MINIMALREPEAT = 52;
  
  private static final byte REOP_MINIMALSTAR = 45;
  
  private static final byte REOP_NCLASS = 23;
  
  private static final byte REOP_NONALNUM = 10;
  
  private static final byte REOP_NONDIGIT = 8;
  
  private static final byte REOP_NONSPACE = 12;
  
  private static final byte REOP_OPT = 28;
  
  private static final byte REOP_PLUS = 27;
  
  private static final byte REOP_QUANT = 25;
  
  private static final byte REOP_REPEAT = 51;
  
  private static final byte REOP_RPAREN = 30;
  
  private static final byte REOP_SIMPLE_END = 23;
  
  private static final byte REOP_SIMPLE_START = 1;
  
  private static final byte REOP_SPACE = 11;
  
  private static final byte REOP_STAR = 26;
  
  private static final byte REOP_UCFLAT1 = 18;
  
  private static final byte REOP_UCFLAT1i = 19;
  
  private static final byte REOP_WBDRY = 4;
  
  private static final byte REOP_WNONBDRY = 5;
  
  public static final int TEST = 0;
  
  private static final boolean debug = false;
  
  private static final long serialVersionUID = 4965263491464903264L;
  
  Object lastIndex;
  
  private int lastIndexAttr;
  
  private RECompiled re;
  
  NativeRegExp() {
    this.lastIndex = Double.valueOf(0.0D);
    this.lastIndexAttr = 6;
  }
  
  NativeRegExp(Scriptable paramScriptable, RECompiled paramRECompiled) {
    Double double_ = Double.valueOf(0.0D);
    this.lastIndex = double_;
    this.lastIndexAttr = 6;
    this.re = paramRECompiled;
    this.lastIndex = double_;
    ScriptRuntime.setBuiltinProtoAndParent((ScriptableObject)this, paramScriptable, TopLevel.Builtins.RegExp);
  }
  
  private static void addCharacterRangeToCharSet(RECharSet paramRECharSet, char paramChar1, char paramChar2) {
    int i = paramChar1 / 8;
    int j = paramChar2 / 8;
    if (paramChar2 < paramRECharSet.length && paramChar1 <= paramChar2) {
      byte[] arrayOfByte;
      paramChar1 = (char)(paramChar1 & 0x7);
      paramChar2 = (char)(paramChar2 & 0x7);
      if (i == j) {
        arrayOfByte = paramRECharSet.bits;
        arrayOfByte[i] = (byte)(byte)(255 >> 7 - paramChar2 - paramChar1 << paramChar1 | arrayOfByte[i]);
      } else {
        byte[] arrayOfByte1 = ((RECharSet)arrayOfByte).bits;
        arrayOfByte1[i] = (byte)(byte)(arrayOfByte1[i] | 255 << paramChar1);
        for (int k = i + 1; k < j; k++)
          ((RECharSet)arrayOfByte).bits[k] = (byte)-1; 
        arrayOfByte = ((RECharSet)arrayOfByte).bits;
        arrayOfByte[j] = (byte)(byte)(255 >> 7 - paramChar2 | arrayOfByte[j]);
      } 
      return;
    } 
    throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
  }
  
  private static void addCharacterToCharSet(RECharSet paramRECharSet, char paramChar) {
    int i = paramChar / 8;
    if (paramChar < paramRECharSet.length) {
      byte[] arrayOfByte = paramRECharSet.bits;
      arrayOfByte[i] = (byte)(byte)(arrayOfByte[i] | 1 << (paramChar & 0x7));
      return;
    } 
    throw ScriptRuntime.constructError("SyntaxError", "invalid range in character class");
  }
  
  private static int addIndex(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      if (paramInt2 <= 65535) {
        paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 >> 8);
        paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)paramInt2;
        return paramInt1 + 2;
      } 
      throw Context.reportRuntimeError("Too complex regexp");
    } 
    throw Kit.codeBug();
  }
  
  private static boolean backrefMatcher(REGlobalData paramREGlobalData, int paramInt1, String paramString, int paramInt2) {
    if (paramREGlobalData.parens == null || paramInt1 >= paramREGlobalData.parens.length)
      return false; 
    int i = paramREGlobalData.parensIndex(paramInt1);
    if (i == -1)
      return true; 
    int j = paramREGlobalData.parensLength(paramInt1);
    if (paramREGlobalData.cp + j > paramInt2)
      return false; 
    if ((paramREGlobalData.regexp.flags & 0x2) != 0) {
      for (paramInt1 = 0; paramInt1 < j; paramInt1++) {
        char c1 = paramString.charAt(i + paramInt1);
        char c2 = paramString.charAt(paramREGlobalData.cp + paramInt1);
        if (c1 != c2 && upcase(c1) != upcase(c2))
          return false; 
      } 
    } else if (!paramString.regionMatches(i, paramString, paramREGlobalData.cp, j)) {
      return false;
    } 
    paramREGlobalData.cp += j;
    return true;
  }
  
  private static boolean calculateBitmapSize(CompilerState paramCompilerState, RENode paramRENode, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_3
    //   1: istore #5
    //   3: iconst_0
    //   4: istore #6
    //   6: iconst_0
    //   7: istore #7
    //   9: iconst_0
    //   10: istore #8
    //   12: aload_1
    //   13: iconst_0
    //   14: putfield bmsize : I
    //   17: aload_1
    //   18: iconst_1
    //   19: putfield sense : Z
    //   22: iload #5
    //   24: iload #4
    //   26: if_icmpne -> 31
    //   29: iconst_1
    //   30: ireturn
    //   31: iload #5
    //   33: istore_3
    //   34: iload #6
    //   36: istore #9
    //   38: iload #7
    //   40: istore #10
    //   42: iload #8
    //   44: istore #11
    //   46: aload_2
    //   47: iload #5
    //   49: caload
    //   50: bipush #94
    //   52: if_icmpne -> 77
    //   55: iload #5
    //   57: iconst_1
    //   58: iadd
    //   59: istore_3
    //   60: aload_1
    //   61: iconst_0
    //   62: putfield sense : Z
    //   65: iload #8
    //   67: istore #11
    //   69: iload #7
    //   71: istore #10
    //   73: iload #6
    //   75: istore #9
    //   77: iload_3
    //   78: iload #4
    //   80: if_icmpeq -> 782
    //   83: iconst_2
    //   84: istore #6
    //   86: aload_2
    //   87: iload_3
    //   88: caload
    //   89: bipush #92
    //   91: if_icmpeq -> 105
    //   94: aload_2
    //   95: iload_3
    //   96: caload
    //   97: istore #5
    //   99: iinc #3, 1
    //   102: goto -> 617
    //   105: iload_3
    //   106: iconst_1
    //   107: iadd
    //   108: istore #5
    //   110: iload #5
    //   112: iconst_1
    //   113: iadd
    //   114: istore_3
    //   115: aload_2
    //   116: iload #5
    //   118: caload
    //   119: istore #5
    //   121: iload #5
    //   123: bipush #68
    //   125: if_icmpeq -> 386
    //   128: iload #5
    //   130: bipush #83
    //   132: if_icmpeq -> 386
    //   135: iload #5
    //   137: bipush #87
    //   139: if_icmpeq -> 386
    //   142: iload #5
    //   144: bipush #102
    //   146: if_icmpeq -> 613
    //   149: iload #5
    //   151: bipush #110
    //   153: if_icmpeq -> 606
    //   156: iload #5
    //   158: tableswitch default -> 204, 48 -> 470, 49 -> 470, 50 -> 470, 51 -> 470, 52 -> 470, 53 -> 470, 54 -> 470, 55 -> 470
    //   204: iload #5
    //   206: tableswitch default -> 232, 98 -> 463, 99 -> 419, 100 -> 396
    //   232: iload #5
    //   234: tableswitch default -> 276, 114 -> 389, 115 -> 386, 116 -> 379, 117 -> 286, 118 -> 279, 119 -> 386, 120 -> 291
    //   276: goto -> 617
    //   279: bipush #11
    //   281: istore #5
    //   283: goto -> 617
    //   286: iconst_2
    //   287: iconst_2
    //   288: iadd
    //   289: istore #6
    //   291: iconst_0
    //   292: istore #5
    //   294: iconst_0
    //   295: istore #12
    //   297: iload_3
    //   298: istore #8
    //   300: iload #5
    //   302: istore #7
    //   304: iload #12
    //   306: iload #6
    //   308: if_icmpge -> 369
    //   311: iload_3
    //   312: istore #8
    //   314: iload #5
    //   316: istore #7
    //   318: iload_3
    //   319: iload #4
    //   321: if_icmpge -> 369
    //   324: iload_3
    //   325: iconst_1
    //   326: iadd
    //   327: istore #8
    //   329: aload_2
    //   330: iload_3
    //   331: caload
    //   332: iload #5
    //   334: invokestatic xDigitToInt : (II)I
    //   337: istore #5
    //   339: iload #5
    //   341: ifge -> 360
    //   344: iload #8
    //   346: iload #12
    //   348: iconst_1
    //   349: iadd
    //   350: isub
    //   351: istore #8
    //   353: bipush #92
    //   355: istore #7
    //   357: goto -> 369
    //   360: iinc #12, 1
    //   363: iload #8
    //   365: istore_3
    //   366: goto -> 297
    //   369: iload #7
    //   371: istore #5
    //   373: iload #8
    //   375: istore_3
    //   376: goto -> 617
    //   379: bipush #9
    //   381: istore #5
    //   383: goto -> 617
    //   386: goto -> 757
    //   389: bipush #13
    //   391: istore #5
    //   393: goto -> 617
    //   396: iload #11
    //   398: ifeq -> 412
    //   401: ldc_w 'msg.bad.range'
    //   404: ldc_w ''
    //   407: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   410: iconst_0
    //   411: ireturn
    //   412: bipush #57
    //   414: istore #5
    //   416: goto -> 617
    //   419: iload_3
    //   420: iload #4
    //   422: if_icmpge -> 453
    //   425: aload_2
    //   426: iload_3
    //   427: caload
    //   428: invokestatic isControlLetter : (C)Z
    //   431: ifeq -> 453
    //   434: iload_3
    //   435: iconst_1
    //   436: iadd
    //   437: istore #5
    //   439: aload_2
    //   440: iload_3
    //   441: caload
    //   442: bipush #31
    //   444: iand
    //   445: i2c
    //   446: istore_3
    //   447: iload #5
    //   449: istore_3
    //   450: goto -> 456
    //   453: iinc #3, -1
    //   456: bipush #92
    //   458: istore #5
    //   460: goto -> 617
    //   463: bipush #8
    //   465: istore #5
    //   467: goto -> 617
    //   470: iload #5
    //   472: bipush #48
    //   474: isub
    //   475: istore #6
    //   477: aload_2
    //   478: iload_3
    //   479: caload
    //   480: istore #12
    //   482: iload_3
    //   483: istore #5
    //   485: iload #6
    //   487: istore #8
    //   489: bipush #48
    //   491: iload #12
    //   493: if_icmpgt -> 596
    //   496: iload_3
    //   497: istore #5
    //   499: iload #6
    //   501: istore #8
    //   503: iload #12
    //   505: bipush #55
    //   507: if_icmpgt -> 596
    //   510: iload_3
    //   511: iconst_1
    //   512: iadd
    //   513: istore #7
    //   515: iload #6
    //   517: bipush #8
    //   519: imul
    //   520: iload #12
    //   522: bipush #48
    //   524: isub
    //   525: iadd
    //   526: istore_3
    //   527: aload_2
    //   528: iload #7
    //   530: caload
    //   531: istore #6
    //   533: iload #7
    //   535: istore #5
    //   537: iload_3
    //   538: istore #8
    //   540: bipush #48
    //   542: iload #6
    //   544: if_icmpgt -> 596
    //   547: iload #7
    //   549: istore #5
    //   551: iload_3
    //   552: istore #8
    //   554: iload #6
    //   556: bipush #55
    //   558: if_icmpgt -> 596
    //   561: iload #7
    //   563: iconst_1
    //   564: iadd
    //   565: istore #5
    //   567: iload_3
    //   568: bipush #8
    //   570: imul
    //   571: iload #6
    //   573: bipush #48
    //   575: isub
    //   576: iadd
    //   577: istore #8
    //   579: iload #8
    //   581: sipush #255
    //   584: if_icmpgt -> 590
    //   587: goto -> 596
    //   590: iinc #5, -1
    //   593: iload_3
    //   594: istore #8
    //   596: iload #5
    //   598: istore_3
    //   599: iload #8
    //   601: istore #5
    //   603: goto -> 617
    //   606: bipush #10
    //   608: istore #5
    //   610: goto -> 617
    //   613: bipush #12
    //   615: istore #5
    //   617: iload #11
    //   619: ifeq -> 646
    //   622: iload #9
    //   624: iload #5
    //   626: if_icmple -> 640
    //   629: ldc_w 'msg.bad.range'
    //   632: ldc_w ''
    //   635: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   638: iconst_0
    //   639: ireturn
    //   640: iconst_0
    //   641: istore #7
    //   643: goto -> 684
    //   646: iload #11
    //   648: istore #7
    //   650: iload_3
    //   651: iload #4
    //   653: iconst_1
    //   654: isub
    //   655: if_icmpge -> 684
    //   658: iload #11
    //   660: istore #7
    //   662: aload_2
    //   663: iload_3
    //   664: caload
    //   665: bipush #45
    //   667: if_icmpne -> 684
    //   670: iinc #3, 1
    //   673: iconst_1
    //   674: istore #11
    //   676: iload #5
    //   678: i2c
    //   679: istore #9
    //   681: goto -> 77
    //   684: iload #5
    //   686: istore #8
    //   688: aload_0
    //   689: getfield flags : I
    //   692: iconst_2
    //   693: iand
    //   694: ifeq -> 731
    //   697: iload #5
    //   699: i2c
    //   700: invokestatic upcase : (C)C
    //   703: istore #8
    //   705: iload #5
    //   707: i2c
    //   708: invokestatic downcase : (C)C
    //   711: istore #5
    //   713: iload #8
    //   715: iload #5
    //   717: if_icmplt -> 727
    //   720: iload #8
    //   722: istore #5
    //   724: goto -> 727
    //   727: iload #5
    //   729: istore #8
    //   731: iload #10
    //   733: istore #5
    //   735: iload #8
    //   737: iload #10
    //   739: if_icmple -> 746
    //   742: iload #8
    //   744: istore #5
    //   746: iload #5
    //   748: istore #10
    //   750: iload #7
    //   752: istore #11
    //   754: goto -> 77
    //   757: iload #11
    //   759: ifeq -> 773
    //   762: ldc_w 'msg.bad.range'
    //   765: ldc_w ''
    //   768: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   771: iconst_0
    //   772: ireturn
    //   773: aload_1
    //   774: ldc_w 65536
    //   777: putfield bmsize : I
    //   780: iconst_1
    //   781: ireturn
    //   782: aload_1
    //   783: iload #10
    //   785: iconst_1
    //   786: iadd
    //   787: putfield bmsize : I
    //   790: iconst_1
    //   791: ireturn
  }
  
  private static boolean classMatcher(REGlobalData paramREGlobalData, RECharSet paramRECharSet, char paramChar) {
    if (!paramRECharSet.converted)
      processCharSet(paramREGlobalData, paramRECharSet); 
    int i = paramRECharSet.length;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 0) {
      bool2 = bool1;
      if (paramChar < paramRECharSet.length)
        if ((paramRECharSet.bits[paramChar >> 3] & 1 << (paramChar & 0x7)) == 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }  
    } 
    return paramRECharSet.sense ^ bool2;
  }
  
  static RECompiled compileRE(Context paramContext, String paramString1, String paramString2, boolean paramBoolean) {
    CompilerState compilerState1;
    RECompiled rECompiled = new RECompiled(paramString1);
    int i = paramString1.length();
    int j = 0;
    int k = 0;
    if (paramString2 != null) {
      byte b = 0;
      while (true) {
        j = k;
        if (b < paramString2.length()) {
          char c = paramString2.charAt(b);
          j = 0;
          if (c == 'g') {
            j = 1;
          } else if (c == 'i') {
            j = 2;
          } else if (c == 'm') {
            j = 4;
          } else {
            reportError("msg.invalid.re.flag", String.valueOf(c));
          } 
          if ((k & j) != 0)
            reportError("msg.invalid.re.flag", String.valueOf(c)); 
          k |= j;
          b++;
          continue;
        } 
        break;
      } 
    } 
    rECompiled.flags = j;
    CompilerState compilerState2 = new CompilerState(paramContext, rECompiled.source, i, j);
    if (paramBoolean && i > 0) {
      compilerState2.result = new RENode((byte)14);
      compilerState2.result.chr = (char)compilerState2.cpbegin[0];
      compilerState2.result.length = i;
      compilerState2.result.flatIndex = 0;
      compilerState2.progLength += 5;
      compilerState1 = compilerState2;
    } else {
      if (!parseDisjunction(compilerState2))
        return null; 
      compilerState1 = compilerState2;
      if (compilerState2.maxBackReference > compilerState2.parenCount) {
        CompilerState compilerState = new CompilerState(paramContext, rECompiled.source, i, j);
        compilerState.backReferenceLimit = compilerState.parenCount;
        compilerState1 = compilerState;
        if (!parseDisjunction(compilerState))
          return null; 
      } 
    } 
    rECompiled.program = new byte[compilerState1.progLength + 1];
    if (compilerState1.classCount != 0) {
      rECompiled.classList = new RECharSet[compilerState1.classCount];
      rECompiled.classCount = compilerState1.classCount;
    } 
    j = emitREBytecode(compilerState1, rECompiled, 0, compilerState1.result);
    rECompiled.program[j] = (byte)57;
    rECompiled.parenCount = compilerState1.parenCount;
    j = rECompiled.program[0];
    if (j != 2) {
      if (j != 31) {
        switch (j) {
          default:
            return rECompiled;
          case 18:
          case 19:
            rECompiled.anchorCh = (char)getIndex(rECompiled.program, 1);
          case 15:
          case 17:
            rECompiled.anchorCh = (char)(rECompiled.program[1] & 0xFF);
          case 14:
          case 16:
            break;
        } 
        j = getIndex(rECompiled.program, 1);
        rECompiled.anchorCh = rECompiled.source[j];
      } 
      RENode rENode = compilerState1.result;
      if (rENode.kid.op == 2 && rENode.kid2.op == 2)
        rECompiled.anchorCh = -2; 
    } 
    rECompiled.anchorCh = -2;
  }
  
  private static void doFlat(CompilerState paramCompilerState, char paramChar) {
    paramCompilerState.result = new RENode((byte)14);
    paramCompilerState.result.chr = (char)paramChar;
    paramCompilerState.result.length = 1;
    paramCompilerState.result.flatIndex = -1;
    paramCompilerState.progLength += 3;
  }
  
  private static char downcase(char paramChar) {
    if (paramChar < '')
      return ('A' <= paramChar && paramChar <= 'Z') ? (char)(paramChar + 32) : paramChar; 
    char c = Character.toLowerCase(paramChar);
    if (c < '') {
      c = paramChar;
      paramChar = c;
    } else {
      paramChar = c;
    } 
    return paramChar;
  }
  
  private static int emitREBytecode(CompilerState paramCompilerState, RECompiled paramRECompiled, int paramInt, RENode paramRENode) {
    byte[] arrayOfByte = paramRECompiled.program;
    int i;
    for (i = paramInt;; i = paramInt) {
      if (paramRENode != null) {
        paramInt = i + 1;
        arrayOfByte[i] = (byte)paramRENode.op;
        byte b = paramRENode.op;
        int k = 1;
        if (b != 1) {
          if (b != 22) {
            if (b != 25) {
              if (b != 29) {
                i = paramInt;
                if (b != 31) {
                  if (b != 13) {
                    if (b != 14) {
                      if (b != 41) {
                        if (b != 42) {
                          int m;
                          char c;
                          RENode rENode1;
                          switch (b) {
                            case 53:
                            case 54:
                            case 55:
                              if (paramRENode.op == 54) {
                                i = k;
                              } else {
                                i = 0;
                              } 
                              c = paramRENode.chr;
                              k = c;
                              if (i != 0)
                                k = upcase(c); 
                              addIndex(arrayOfByte, paramInt, k);
                              m = paramInt + 2;
                              k = paramRENode.index;
                              paramInt = k;
                              if (i != 0)
                                paramInt = upcase((char)k); 
                              addIndex(arrayOfByte, m, paramInt);
                              i = m + 2;
                              rENode1 = paramRENode.kid2;
                              paramInt = emitREBytecode(paramCompilerState, paramRECompiled, i + 2, paramRENode.kid);
                              k = paramInt + 1;
                              arrayOfByte[paramInt] = (byte)32;
                              paramInt = k + 2;
                              resolveForwardJump(arrayOfByte, i, paramInt);
                              paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, rENode1);
                              i = paramInt + 1;
                              arrayOfByte[paramInt] = (byte)32;
                              paramInt = i + 2;
                              resolveForwardJump(arrayOfByte, k, paramInt);
                              resolveForwardJump(arrayOfByte, i, paramInt);
                              break;
                          } 
                        } else {
                          k = emitREBytecode(paramCompilerState, paramRECompiled, paramInt + 2, paramRENode.kid);
                          i = k + 1;
                          arrayOfByte[k] = (byte)44;
                          resolveForwardJump(arrayOfByte, paramInt, i);
                          paramInt = i;
                        } 
                      } else {
                        k = emitREBytecode(paramCompilerState, paramRECompiled, paramInt + 2, paramRENode.kid);
                        i = k + 1;
                        arrayOfByte[k] = (byte)43;
                        resolveForwardJump(arrayOfByte, paramInt, i);
                        paramInt = i;
                      } 
                    } else {
                      if (paramRENode.flatIndex != -1)
                        while (paramRENode.next != null && paramRENode.next.op == 14 && paramRENode.flatIndex + paramRENode.length == paramRENode.next.flatIndex) {
                          paramRENode.length += paramRENode.next.length;
                          paramRENode.next = paramRENode.next.next;
                        }  
                      if (paramRENode.flatIndex != -1 && paramRENode.length > 1) {
                        if ((paramCompilerState.flags & 0x2) != 0) {
                          arrayOfByte[paramInt - 1] = (byte)16;
                        } else {
                          arrayOfByte[paramInt - 1] = (byte)14;
                        } 
                        paramInt = addIndex(arrayOfByte, addIndex(arrayOfByte, paramInt, paramRENode.flatIndex), paramRENode.length);
                      } else if (paramRENode.chr < 'Ā') {
                        if ((paramCompilerState.flags & 0x2) != 0) {
                          arrayOfByte[paramInt - 1] = (byte)17;
                        } else {
                          arrayOfByte[paramInt - 1] = (byte)15;
                        } 
                        i = paramInt + 1;
                        arrayOfByte[paramInt] = (byte)(byte)paramRENode.chr;
                        paramInt = i;
                      } else {
                        if ((paramCompilerState.flags & 0x2) != 0) {
                          arrayOfByte[paramInt - 1] = (byte)19;
                        } else {
                          arrayOfByte[paramInt - 1] = (byte)18;
                        } 
                        paramInt = addIndex(arrayOfByte, paramInt, paramRENode.chr);
                      } 
                    } 
                  } else {
                    paramInt = addIndex(arrayOfByte, paramInt, paramRENode.parenIndex);
                  } 
                  continue;
                } 
              } else {
                paramInt = emitREBytecode(paramCompilerState, paramRECompiled, addIndex(arrayOfByte, paramInt, paramRENode.parenIndex), paramRENode.kid);
                arrayOfByte[paramInt] = (byte)30;
                paramInt = addIndex(arrayOfByte, paramInt + 1, paramRENode.parenIndex);
                continue;
              } 
            } else {
              if (paramRENode.min == 0 && paramRENode.max == -1) {
                if (paramRENode.greedy) {
                  i = 26;
                } else {
                  i = 45;
                } 
                arrayOfByte[paramInt - 1] = (byte)i;
              } else if (paramRENode.min == 0 && paramRENode.max == 1) {
                if (paramRENode.greedy) {
                  i = 28;
                } else {
                  i = 47;
                } 
                arrayOfByte[paramInt - 1] = (byte)i;
              } else if (paramRENode.min == 1 && paramRENode.max == -1) {
                if (paramRENode.greedy) {
                  i = 27;
                } else {
                  i = 46;
                } 
                arrayOfByte[paramInt - 1] = (byte)i;
              } else {
                if (!paramRENode.greedy)
                  arrayOfByte[paramInt - 1] = (byte)48; 
                paramInt = addIndex(arrayOfByte, addIndex(arrayOfByte, paramInt, paramRENode.min), paramRENode.max + 1);
              } 
              k = addIndex(arrayOfByte, addIndex(arrayOfByte, paramInt, paramRENode.parenCount), paramRENode.parenIndex);
              i = emitREBytecode(paramCompilerState, paramRECompiled, k + 2, paramRENode.kid);
              paramInt = i + 1;
              arrayOfByte[i] = (byte)49;
              resolveForwardJump(arrayOfByte, k, paramInt);
              continue;
            } 
          } else {
            if (!paramRENode.sense)
              arrayOfByte[paramInt - 1] = (byte)23; 
            paramInt = addIndex(arrayOfByte, paramInt, paramRENode.index);
            paramRECompiled.classList[paramRENode.index] = new RECharSet(paramRENode.bmsize, paramRENode.startIndex, paramRENode.kidlen, paramRENode.sense);
            continue;
          } 
        } else {
          paramInt--;
          continue;
        } 
      } else {
        break;
      } 
      RENode rENode = paramRENode.kid2;
      paramInt = emitREBytecode(paramCompilerState, paramRECompiled, i + 2, paramRENode.kid);
      int j = paramInt + 1;
      arrayOfByte[paramInt] = (byte)32;
      paramInt = j + 2;
      resolveForwardJump(arrayOfByte, i, paramInt);
      paramInt = emitREBytecode(paramCompilerState, paramRECompiled, paramInt, rENode);
      i = paramInt + 1;
      arrayOfByte[paramInt] = (byte)32;
      paramInt = i + 2;
      resolveForwardJump(arrayOfByte, j, paramInt);
      resolveForwardJump(arrayOfByte, i, paramInt);
      break;
      paramRENode = paramRENode.next;
    } 
    return i;
  }
  
  private static String escapeRegExp(Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic toString : (Ljava/lang/Object;)Ljava/lang/String;
    //   4: astore_1
    //   5: aconst_null
    //   6: astore_0
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_1
    //   10: bipush #47
    //   12: invokevirtual indexOf : (I)I
    //   15: istore_3
    //   16: iload_3
    //   17: iconst_m1
    //   18: if_icmple -> 102
    //   21: iload_3
    //   22: iload_2
    //   23: if_icmpeq -> 44
    //   26: aload_0
    //   27: astore #4
    //   29: iload_2
    //   30: istore #5
    //   32: aload_1
    //   33: iload_3
    //   34: iconst_1
    //   35: isub
    //   36: invokevirtual charAt : (I)C
    //   39: bipush #92
    //   41: if_icmpeq -> 83
    //   44: aload_0
    //   45: astore #4
    //   47: aload_0
    //   48: ifnonnull -> 60
    //   51: new java/lang/StringBuilder
    //   54: dup
    //   55: invokespecial <init> : ()V
    //   58: astore #4
    //   60: aload #4
    //   62: aload_1
    //   63: iload_2
    //   64: iload_3
    //   65: invokevirtual append : (Ljava/lang/CharSequence;II)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: aload #4
    //   71: ldc_w '\/'
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: iload_3
    //   79: iconst_1
    //   80: iadd
    //   81: istore #5
    //   83: aload_1
    //   84: bipush #47
    //   86: iload_3
    //   87: iconst_1
    //   88: iadd
    //   89: invokevirtual indexOf : (II)I
    //   92: istore_3
    //   93: aload #4
    //   95: astore_0
    //   96: iload #5
    //   98: istore_2
    //   99: goto -> 16
    //   102: aload_1
    //   103: astore #4
    //   105: aload_0
    //   106: ifnull -> 126
    //   109: aload_0
    //   110: aload_1
    //   111: iload_2
    //   112: aload_1
    //   113: invokevirtual length : ()I
    //   116: invokevirtual append : (Ljava/lang/CharSequence;II)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: aload_0
    //   121: invokevirtual toString : ()Ljava/lang/String;
    //   124: astore #4
    //   126: aload #4
    //   128: areturn
  }
  
  private Object execSub(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, int paramInt) {
    String str;
    double d1;
    RegExpImpl regExpImpl = getImpl(paramContext);
    if (paramArrayOfObject.length == 0) {
      str = regExpImpl.input;
      if (str == null)
        str = ScriptRuntime.toString(Undefined.instance); 
    } else {
      str = ScriptRuntime.toString(str[0]);
    } 
    if ((this.re.flags & 0x1) != 0) {
      d1 = ScriptRuntime.toInteger(this.lastIndex);
    } else {
      d1 = 0.0D;
    } 
    double d2 = 0.0D;
    if (d1 < 0.0D || str.length() < d1) {
      this.lastIndex = Double.valueOf(0.0D);
      return null;
    } 
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = (int)d1;
    Object object2 = executeRegExp(paramContext, paramScriptable, regExpImpl, str, arrayOfInt, paramInt);
    Object object1 = object2;
    if ((this.re.flags & 0x1) != 0) {
      if (object2 == null || object2 == Undefined.instance) {
        d1 = d2;
      } else {
        d1 = arrayOfInt[0];
      } 
      this.lastIndex = Double.valueOf(d1);
      object1 = object2;
    } 
    return object1;
  }
  
  private static boolean executeREBytecode(REGlobalData paramREGlobalData, String paramString, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   4: getfield program : [B
    //   7: astore_3
    //   8: bipush #57
    //   10: istore #4
    //   12: iconst_0
    //   13: istore #5
    //   15: iconst_0
    //   16: istore #6
    //   18: iconst_0
    //   19: iconst_1
    //   20: iadd
    //   21: istore #7
    //   23: aload_3
    //   24: iconst_0
    //   25: baload
    //   26: istore #8
    //   28: iload #4
    //   30: istore #9
    //   32: iload #5
    //   34: istore #10
    //   36: iload #6
    //   38: istore #11
    //   40: iload #7
    //   42: istore #12
    //   44: iload #8
    //   46: istore #13
    //   48: aload_0
    //   49: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   52: getfield anchorCh : I
    //   55: ifge -> 188
    //   58: iload #4
    //   60: istore #9
    //   62: iload #5
    //   64: istore #10
    //   66: iload #6
    //   68: istore #11
    //   70: iload #7
    //   72: istore #12
    //   74: iload #8
    //   76: istore #13
    //   78: iload #8
    //   80: invokestatic reopIsSimple : (I)Z
    //   83: ifeq -> 188
    //   86: iconst_0
    //   87: istore #11
    //   89: iload #7
    //   91: istore #12
    //   93: iload #8
    //   95: istore #13
    //   97: iload #11
    //   99: istore #14
    //   101: aload_0
    //   102: getfield cp : I
    //   105: iload_2
    //   106: if_icmpgt -> 169
    //   109: aload_0
    //   110: aload_1
    //   111: iload #8
    //   113: aload_3
    //   114: iload #7
    //   116: iload_2
    //   117: iconst_1
    //   118: invokestatic simpleMatch : (Lcom/trendmicro/hippo/regexp/REGlobalData;Ljava/lang/String;I[BIIZ)I
    //   121: istore #13
    //   123: iload #13
    //   125: iflt -> 146
    //   128: iconst_1
    //   129: istore #14
    //   131: iload #13
    //   133: iconst_1
    //   134: iadd
    //   135: istore #12
    //   137: aload_3
    //   138: iload #13
    //   140: baload
    //   141: istore #13
    //   143: goto -> 169
    //   146: aload_0
    //   147: aload_0
    //   148: getfield skipped : I
    //   151: iconst_1
    //   152: iadd
    //   153: putfield skipped : I
    //   156: aload_0
    //   157: aload_0
    //   158: getfield cp : I
    //   161: iconst_1
    //   162: iadd
    //   163: putfield cp : I
    //   166: goto -> 89
    //   169: iload #4
    //   171: istore #9
    //   173: iload #5
    //   175: istore #10
    //   177: iload #6
    //   179: istore #11
    //   181: iload #14
    //   183: ifne -> 188
    //   186: iconst_0
    //   187: ireturn
    //   188: iload #13
    //   190: invokestatic reopIsSimple : (I)Z
    //   193: ifeq -> 240
    //   196: aload_0
    //   197: aload_1
    //   198: iload #13
    //   200: aload_3
    //   201: iload #12
    //   203: iload_2
    //   204: iconst_1
    //   205: invokestatic simpleMatch : (Lcom/trendmicro/hippo/regexp/REGlobalData;Ljava/lang/String;I[BIIZ)I
    //   208: istore #11
    //   210: iload #11
    //   212: iflt -> 221
    //   215: iconst_1
    //   216: istore #13
    //   218: goto -> 224
    //   221: iconst_0
    //   222: istore #13
    //   224: iload #13
    //   226: ifeq -> 233
    //   229: iload #11
    //   231: istore #12
    //   233: iload #13
    //   235: istore #11
    //   237: goto -> 1610
    //   240: iload #13
    //   242: bipush #57
    //   244: if_icmpeq -> 2281
    //   247: iload #13
    //   249: tableswitch default -> 296, 25 -> 1963, 26 -> 1963, 27 -> 1963, 28 -> 1963, 29 -> 1929, 30 -> 1883, 31 -> 1770, 32 -> 1747
    //   296: iload #13
    //   298: tableswitch default -> 348, 41 -> 1554, 42 -> 1442, 43 -> 1373, 44 -> 1373, 45 -> 1370, 46 -> 1370, 47 -> 1370, 48 -> 1370, 49 -> 1356
    //   348: iload #13
    //   350: tableswitch default -> 384, 51 -> 912, 52 -> 519, 53 -> 391, 54 -> 391, 55 -> 391
    //   384: ldc_w 'invalid bytecode'
    //   387: invokestatic codeBug : (Ljava/lang/String;)Ljava/lang/RuntimeException;
    //   390: athrow
    //   391: aload_3
    //   392: iload #12
    //   394: invokestatic getIndex : ([BI)I
    //   397: i2c
    //   398: istore #8
    //   400: iinc #12, 2
    //   403: aload_3
    //   404: iload #12
    //   406: invokestatic getIndex : ([BI)I
    //   409: i2c
    //   410: istore #14
    //   412: iinc #12, 2
    //   415: aload_0
    //   416: getfield cp : I
    //   419: iload_2
    //   420: if_icmpne -> 429
    //   423: iconst_0
    //   424: istore #11
    //   426: goto -> 1610
    //   429: aload_1
    //   430: aload_0
    //   431: getfield cp : I
    //   434: invokevirtual charAt : (I)C
    //   437: istore #15
    //   439: iload #13
    //   441: bipush #55
    //   443: if_icmpne -> 478
    //   446: iload #15
    //   448: iload #8
    //   450: if_icmpeq -> 516
    //   453: aload_0
    //   454: aload_0
    //   455: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   458: getfield classList : [Lcom/trendmicro/hippo/regexp/RECharSet;
    //   461: iload #14
    //   463: aaload
    //   464: iload #15
    //   466: invokestatic classMatcher : (Lcom/trendmicro/hippo/regexp/REGlobalData;Lcom/trendmicro/hippo/regexp/RECharSet;C)Z
    //   469: ifne -> 516
    //   472: iconst_0
    //   473: istore #11
    //   475: goto -> 1610
    //   478: iload #15
    //   480: istore #7
    //   482: iload #13
    //   484: bipush #54
    //   486: if_icmpne -> 496
    //   489: iload #15
    //   491: invokestatic upcase : (C)C
    //   494: istore #7
    //   496: iload #7
    //   498: iload #8
    //   500: if_icmpeq -> 516
    //   503: iload #7
    //   505: iload #14
    //   507: if_icmpeq -> 516
    //   510: iconst_0
    //   511: istore #11
    //   513: goto -> 1610
    //   516: goto -> 1770
    //   519: aload_0
    //   520: invokestatic popProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;)Lcom/trendmicro/hippo/regexp/REProgState;
    //   523: astore #16
    //   525: iload #11
    //   527: ifne -> 671
    //   530: aload #16
    //   532: getfield max : I
    //   535: iconst_m1
    //   536: if_icmpeq -> 567
    //   539: aload #16
    //   541: getfield max : I
    //   544: ifle -> 550
    //   547: goto -> 567
    //   550: aload #16
    //   552: getfield continuationPc : I
    //   555: istore #10
    //   557: aload #16
    //   559: getfield continuationOp : I
    //   562: istore #9
    //   564: goto -> 1610
    //   567: aload_0
    //   568: aload #16
    //   570: getfield min : I
    //   573: aload #16
    //   575: getfield max : I
    //   578: aload_0
    //   579: getfield cp : I
    //   582: aconst_null
    //   583: aload #16
    //   585: getfield continuationOp : I
    //   588: aload #16
    //   590: getfield continuationPc : I
    //   593: invokestatic pushProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;IIILcom/trendmicro/hippo/regexp/REBackTrackData;II)V
    //   596: bipush #52
    //   598: istore #9
    //   600: iload #12
    //   602: istore #10
    //   604: aload_3
    //   605: iload #12
    //   607: invokestatic getIndex : ([BI)I
    //   610: istore #13
    //   612: iinc #12, 2
    //   615: aload_3
    //   616: iload #12
    //   618: invokestatic getIndex : ([BI)I
    //   621: istore #8
    //   623: iload #12
    //   625: iconst_4
    //   626: iadd
    //   627: istore #7
    //   629: iconst_0
    //   630: istore #12
    //   632: iload #12
    //   634: iload #13
    //   636: if_icmpge -> 656
    //   639: aload_0
    //   640: iload #8
    //   642: iload #12
    //   644: iadd
    //   645: iconst_m1
    //   646: iconst_0
    //   647: invokevirtual setParens : (III)V
    //   650: iinc #12, 1
    //   653: goto -> 632
    //   656: aload_3
    //   657: iload #7
    //   659: baload
    //   660: istore #13
    //   662: iload #7
    //   664: iconst_1
    //   665: iadd
    //   666: istore #12
    //   668: goto -> 188
    //   671: aload #16
    //   673: getfield min : I
    //   676: ifne -> 711
    //   679: aload_0
    //   680: getfield cp : I
    //   683: aload #16
    //   685: getfield index : I
    //   688: if_icmpne -> 711
    //   691: aload #16
    //   693: getfield continuationPc : I
    //   696: istore #10
    //   698: aload #16
    //   700: getfield continuationOp : I
    //   703: istore #9
    //   705: iconst_0
    //   706: istore #11
    //   708: goto -> 1610
    //   711: aload #16
    //   713: getfield min : I
    //   716: istore #9
    //   718: aload #16
    //   720: getfield max : I
    //   723: istore #10
    //   725: iload #9
    //   727: istore #13
    //   729: iload #9
    //   731: ifeq -> 740
    //   734: iload #9
    //   736: iconst_1
    //   737: isub
    //   738: istore #13
    //   740: iload #10
    //   742: istore #9
    //   744: iload #10
    //   746: iconst_m1
    //   747: if_icmpeq -> 756
    //   750: iload #10
    //   752: iconst_1
    //   753: isub
    //   754: istore #9
    //   756: aload_0
    //   757: iload #13
    //   759: iload #9
    //   761: aload_0
    //   762: getfield cp : I
    //   765: aconst_null
    //   766: aload #16
    //   768: getfield continuationOp : I
    //   771: aload #16
    //   773: getfield continuationPc : I
    //   776: invokestatic pushProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;IIILcom/trendmicro/hippo/regexp/REBackTrackData;II)V
    //   779: iload #13
    //   781: ifeq -> 859
    //   784: bipush #52
    //   786: istore #9
    //   788: iload #12
    //   790: istore #10
    //   792: aload_3
    //   793: iload #12
    //   795: invokestatic getIndex : ([BI)I
    //   798: istore #13
    //   800: iinc #12, 2
    //   803: aload_3
    //   804: iload #12
    //   806: invokestatic getIndex : ([BI)I
    //   809: istore #7
    //   811: iload #12
    //   813: iconst_4
    //   814: iadd
    //   815: istore #8
    //   817: iconst_0
    //   818: istore #12
    //   820: iload #12
    //   822: iload #13
    //   824: if_icmpge -> 844
    //   827: aload_0
    //   828: iload #7
    //   830: iload #12
    //   832: iadd
    //   833: iconst_m1
    //   834: iconst_0
    //   835: invokevirtual setParens : (III)V
    //   838: iinc #12, 1
    //   841: goto -> 820
    //   844: aload_3
    //   845: iload #8
    //   847: baload
    //   848: istore #13
    //   850: iload #8
    //   852: iconst_1
    //   853: iadd
    //   854: istore #12
    //   856: goto -> 188
    //   859: aload #16
    //   861: getfield continuationPc : I
    //   864: istore #10
    //   866: aload #16
    //   868: getfield continuationOp : I
    //   871: istore #9
    //   873: aload_0
    //   874: bipush #52
    //   876: iload #12
    //   878: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BI)V
    //   881: aload_0
    //   882: invokestatic popProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;)Lcom/trendmicro/hippo/regexp/REProgState;
    //   885: pop
    //   886: iinc #12, 4
    //   889: iload #12
    //   891: aload_3
    //   892: iload #12
    //   894: invokestatic getOffset : ([BI)I
    //   897: iadd
    //   898: istore #12
    //   900: aload_3
    //   901: iload #12
    //   903: baload
    //   904: istore #13
    //   906: iinc #12, 1
    //   909: goto -> 188
    //   912: iload #11
    //   914: istore #13
    //   916: aload_0
    //   917: invokestatic popProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;)Lcom/trendmicro/hippo/regexp/REProgState;
    //   920: astore #16
    //   922: iload #13
    //   924: ifne -> 973
    //   927: aload #16
    //   929: getfield min : I
    //   932: ifne -> 938
    //   935: iconst_1
    //   936: istore #13
    //   938: aload #16
    //   940: getfield continuationPc : I
    //   943: istore #10
    //   945: aload #16
    //   947: getfield continuationOp : I
    //   950: istore #9
    //   952: iinc #12, 4
    //   955: iload #12
    //   957: aload_3
    //   958: iload #12
    //   960: invokestatic getOffset : ([BI)I
    //   963: iadd
    //   964: istore #12
    //   966: iload #13
    //   968: istore #11
    //   970: goto -> 1610
    //   973: aload #16
    //   975: getfield min : I
    //   978: ifne -> 1027
    //   981: aload_0
    //   982: getfield cp : I
    //   985: aload #16
    //   987: getfield index : I
    //   990: if_icmpne -> 1027
    //   993: aload #16
    //   995: getfield continuationPc : I
    //   998: istore #10
    //   1000: aload #16
    //   1002: getfield continuationOp : I
    //   1005: istore #9
    //   1007: iinc #12, 4
    //   1010: iload #12
    //   1012: aload_3
    //   1013: iload #12
    //   1015: invokestatic getOffset : ([BI)I
    //   1018: iadd
    //   1019: istore #12
    //   1021: iconst_0
    //   1022: istore #11
    //   1024: goto -> 1610
    //   1027: aload #16
    //   1029: getfield min : I
    //   1032: istore #9
    //   1034: aload #16
    //   1036: getfield max : I
    //   1039: istore #10
    //   1041: iload #9
    //   1043: istore #11
    //   1045: iload #9
    //   1047: ifeq -> 1056
    //   1050: iload #9
    //   1052: iconst_1
    //   1053: isub
    //   1054: istore #11
    //   1056: iload #10
    //   1058: istore #9
    //   1060: iload #10
    //   1062: iconst_m1
    //   1063: if_icmpeq -> 1072
    //   1066: iload #10
    //   1068: iconst_1
    //   1069: isub
    //   1070: istore #9
    //   1072: iload #9
    //   1074: ifne -> 1111
    //   1077: aload #16
    //   1079: getfield continuationPc : I
    //   1082: istore #10
    //   1084: aload #16
    //   1086: getfield continuationOp : I
    //   1089: istore #9
    //   1091: iinc #12, 4
    //   1094: iload #12
    //   1096: aload_3
    //   1097: iload #12
    //   1099: invokestatic getOffset : ([BI)I
    //   1102: iadd
    //   1103: istore #12
    //   1105: iconst_1
    //   1106: istore #11
    //   1108: goto -> 1610
    //   1111: iload #12
    //   1113: bipush #6
    //   1115: iadd
    //   1116: istore #7
    //   1118: aload_3
    //   1119: iload #7
    //   1121: baload
    //   1122: istore #8
    //   1124: aload_0
    //   1125: getfield cp : I
    //   1128: istore #10
    //   1130: iload #8
    //   1132: invokestatic reopIsSimple : (I)Z
    //   1135: ifeq -> 1214
    //   1138: aload_0
    //   1139: aload_1
    //   1140: iload #8
    //   1142: aload_3
    //   1143: iload #7
    //   1145: iconst_1
    //   1146: iadd
    //   1147: iload_2
    //   1148: iconst_1
    //   1149: invokestatic simpleMatch : (Lcom/trendmicro/hippo/regexp/REGlobalData;Ljava/lang/String;I[BIIZ)I
    //   1152: istore #7
    //   1154: iload #7
    //   1156: ifge -> 1208
    //   1159: iload #11
    //   1161: ifne -> 1170
    //   1164: iconst_1
    //   1165: istore #13
    //   1167: goto -> 1173
    //   1170: iconst_0
    //   1171: istore #13
    //   1173: aload #16
    //   1175: getfield continuationPc : I
    //   1178: istore #10
    //   1180: aload #16
    //   1182: getfield continuationOp : I
    //   1185: istore #9
    //   1187: iinc #12, 4
    //   1190: iload #12
    //   1192: aload_3
    //   1193: iload #12
    //   1195: invokestatic getOffset : ([BI)I
    //   1198: iadd
    //   1199: istore #12
    //   1201: iload #13
    //   1203: istore #11
    //   1205: goto -> 1610
    //   1208: iconst_1
    //   1209: istore #13
    //   1211: goto -> 1214
    //   1214: iload #10
    //   1216: istore #14
    //   1218: bipush #51
    //   1220: istore #8
    //   1222: iload #12
    //   1224: istore #10
    //   1226: aload_0
    //   1227: iload #11
    //   1229: iload #9
    //   1231: iload #14
    //   1233: aconst_null
    //   1234: aload #16
    //   1236: getfield continuationOp : I
    //   1239: aload #16
    //   1241: getfield continuationPc : I
    //   1244: invokestatic pushProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;IIILcom/trendmicro/hippo/regexp/REBackTrackData;II)V
    //   1247: iload #11
    //   1249: ifne -> 1317
    //   1252: aload_0
    //   1253: bipush #51
    //   1255: iload #12
    //   1257: iload #14
    //   1259: aload #16
    //   1261: getfield continuationOp : I
    //   1264: aload #16
    //   1266: getfield continuationPc : I
    //   1269: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BIIII)V
    //   1272: aload_3
    //   1273: iload #12
    //   1275: invokestatic getIndex : ([BI)I
    //   1278: istore #14
    //   1280: aload_3
    //   1281: iload #12
    //   1283: iconst_2
    //   1284: iadd
    //   1285: invokestatic getIndex : ([BI)I
    //   1288: istore #9
    //   1290: iconst_0
    //   1291: istore #11
    //   1293: iload #11
    //   1295: iload #14
    //   1297: if_icmpge -> 1317
    //   1300: aload_0
    //   1301: iload #9
    //   1303: iload #11
    //   1305: iadd
    //   1306: iconst_m1
    //   1307: iconst_0
    //   1308: invokevirtual setParens : (III)V
    //   1311: iinc #11, 1
    //   1314: goto -> 1293
    //   1317: aload_3
    //   1318: iload #7
    //   1320: baload
    //   1321: bipush #49
    //   1323: if_icmpeq -> 1353
    //   1326: iload #7
    //   1328: iconst_1
    //   1329: iadd
    //   1330: istore #12
    //   1332: aload_3
    //   1333: iload #7
    //   1335: baload
    //   1336: istore #7
    //   1338: iload #8
    //   1340: istore #9
    //   1342: iload #13
    //   1344: istore #11
    //   1346: iload #7
    //   1348: istore #13
    //   1350: goto -> 188
    //   1353: goto -> 916
    //   1356: iconst_1
    //   1357: istore #11
    //   1359: iload #10
    //   1361: istore #12
    //   1363: iload #9
    //   1365: istore #13
    //   1367: goto -> 188
    //   1370: goto -> 1966
    //   1373: aload_0
    //   1374: invokestatic popProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;)Lcom/trendmicro/hippo/regexp/REProgState;
    //   1377: astore #16
    //   1379: aload_0
    //   1380: aload #16
    //   1382: getfield index : I
    //   1385: putfield cp : I
    //   1388: aload_0
    //   1389: aload #16
    //   1391: getfield backTrack : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1394: putfield backTrackStackTop : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1397: aload #16
    //   1399: getfield continuationPc : I
    //   1402: istore #10
    //   1404: aload #16
    //   1406: getfield continuationOp : I
    //   1409: istore #9
    //   1411: iload #13
    //   1413: bipush #44
    //   1415: if_icmpne -> 1439
    //   1418: iload #11
    //   1420: ifne -> 1429
    //   1423: iconst_1
    //   1424: istore #13
    //   1426: goto -> 1432
    //   1429: iconst_0
    //   1430: istore #13
    //   1432: iload #13
    //   1434: istore #11
    //   1436: goto -> 1439
    //   1439: goto -> 1610
    //   1442: aload_3
    //   1443: iload #12
    //   1445: invokestatic getIndex : ([BI)I
    //   1448: istore #8
    //   1450: iload #12
    //   1452: iconst_2
    //   1453: iadd
    //   1454: istore #7
    //   1456: iload #7
    //   1458: iconst_1
    //   1459: iadd
    //   1460: istore #13
    //   1462: aload_3
    //   1463: iload #7
    //   1465: baload
    //   1466: istore #7
    //   1468: iload #7
    //   1470: invokestatic reopIsSimple : (I)Z
    //   1473: ifeq -> 1514
    //   1476: aload_0
    //   1477: aload_1
    //   1478: iload #7
    //   1480: aload_3
    //   1481: iload #13
    //   1483: iload_2
    //   1484: iconst_0
    //   1485: invokestatic simpleMatch : (Lcom/trendmicro/hippo/regexp/REGlobalData;Ljava/lang/String;I[BIIZ)I
    //   1488: istore #14
    //   1490: iload #14
    //   1492: iflt -> 1514
    //   1495: aload_3
    //   1496: iload #14
    //   1498: baload
    //   1499: bipush #44
    //   1501: if_icmpne -> 1514
    //   1504: iload #13
    //   1506: istore #12
    //   1508: iconst_0
    //   1509: istore #11
    //   1511: goto -> 1610
    //   1514: aload_0
    //   1515: iconst_0
    //   1516: iconst_0
    //   1517: aload_0
    //   1518: getfield cp : I
    //   1521: aload_0
    //   1522: getfield backTrackStackTop : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1525: iload #9
    //   1527: iload #10
    //   1529: invokestatic pushProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;IIILcom/trendmicro/hippo/regexp/REBackTrackData;II)V
    //   1532: aload_0
    //   1533: bipush #44
    //   1535: iload #12
    //   1537: iload #8
    //   1539: iadd
    //   1540: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BI)V
    //   1543: iload #13
    //   1545: istore #12
    //   1547: iload #7
    //   1549: istore #13
    //   1551: goto -> 188
    //   1554: aload_3
    //   1555: iload #12
    //   1557: invokestatic getIndex : ([BI)I
    //   1560: istore #8
    //   1562: iload #12
    //   1564: iconst_2
    //   1565: iadd
    //   1566: istore #7
    //   1568: iload #7
    //   1570: iconst_1
    //   1571: iadd
    //   1572: istore #13
    //   1574: aload_3
    //   1575: iload #7
    //   1577: baload
    //   1578: istore #7
    //   1580: iload #7
    //   1582: invokestatic reopIsSimple : (I)Z
    //   1585: ifeq -> 1707
    //   1588: aload_0
    //   1589: aload_1
    //   1590: iload #7
    //   1592: aload_3
    //   1593: iload #13
    //   1595: iload_2
    //   1596: iconst_0
    //   1597: invokestatic simpleMatch : (Lcom/trendmicro/hippo/regexp/REGlobalData;Ljava/lang/String;I[BIIZ)I
    //   1600: ifge -> 1707
    //   1603: iload #13
    //   1605: istore #12
    //   1607: iconst_0
    //   1608: istore #11
    //   1610: iload #11
    //   1612: ifne -> 1695
    //   1615: aload_0
    //   1616: getfield backTrackStackTop : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1619: astore #16
    //   1621: aload #16
    //   1623: ifnull -> 1693
    //   1626: aload_0
    //   1627: aload #16
    //   1629: getfield previous : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1632: putfield backTrackStackTop : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1635: aload_0
    //   1636: aload #16
    //   1638: getfield parens : [J
    //   1641: putfield parens : [J
    //   1644: aload_0
    //   1645: aload #16
    //   1647: getfield cp : I
    //   1650: putfield cp : I
    //   1653: aload_0
    //   1654: aload #16
    //   1656: getfield stateStackTop : Lcom/trendmicro/hippo/regexp/REProgState;
    //   1659: putfield stateStackTop : Lcom/trendmicro/hippo/regexp/REProgState;
    //   1662: aload #16
    //   1664: getfield continuationOp : I
    //   1667: istore #9
    //   1669: aload #16
    //   1671: getfield continuationPc : I
    //   1674: istore #10
    //   1676: aload #16
    //   1678: getfield pc : I
    //   1681: istore #12
    //   1683: aload #16
    //   1685: getfield op : I
    //   1688: istore #13
    //   1690: goto -> 188
    //   1693: iconst_0
    //   1694: ireturn
    //   1695: aload_3
    //   1696: iload #12
    //   1698: baload
    //   1699: istore #13
    //   1701: iinc #12, 1
    //   1704: goto -> 188
    //   1707: aload_0
    //   1708: iconst_0
    //   1709: iconst_0
    //   1710: aload_0
    //   1711: getfield cp : I
    //   1714: aload_0
    //   1715: getfield backTrackStackTop : Lcom/trendmicro/hippo/regexp/REBackTrackData;
    //   1718: iload #9
    //   1720: iload #10
    //   1722: invokestatic pushProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;IIILcom/trendmicro/hippo/regexp/REBackTrackData;II)V
    //   1725: aload_0
    //   1726: bipush #43
    //   1728: iload #12
    //   1730: iload #8
    //   1732: iadd
    //   1733: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BI)V
    //   1736: iload #13
    //   1738: istore #12
    //   1740: iload #7
    //   1742: istore #13
    //   1744: goto -> 188
    //   1747: iload #12
    //   1749: aload_3
    //   1750: iload #12
    //   1752: invokestatic getOffset : ([BI)I
    //   1755: iadd
    //   1756: istore #12
    //   1758: aload_3
    //   1759: iload #12
    //   1761: baload
    //   1762: istore #13
    //   1764: iinc #12, 1
    //   1767: goto -> 188
    //   1770: iload #12
    //   1772: aload_3
    //   1773: iload #12
    //   1775: invokestatic getOffset : ([BI)I
    //   1778: iadd
    //   1779: istore #7
    //   1781: iload #12
    //   1783: iconst_2
    //   1784: iadd
    //   1785: istore #13
    //   1787: iload #13
    //   1789: iconst_1
    //   1790: iadd
    //   1791: istore #12
    //   1793: aload_3
    //   1794: iload #13
    //   1796: baload
    //   1797: istore #13
    //   1799: aload_0
    //   1800: getfield cp : I
    //   1803: istore #8
    //   1805: iload #13
    //   1807: invokestatic reopIsSimple : (I)Z
    //   1810: ifeq -> 1862
    //   1813: aload_0
    //   1814: aload_1
    //   1815: iload #13
    //   1817: aload_3
    //   1818: iload #12
    //   1820: iload_2
    //   1821: iconst_1
    //   1822: invokestatic simpleMatch : (Lcom/trendmicro/hippo/regexp/REGlobalData;Ljava/lang/String;I[BIIZ)I
    //   1825: istore #12
    //   1827: iload #12
    //   1829: ifge -> 1847
    //   1832: aload_3
    //   1833: iload #7
    //   1835: baload
    //   1836: istore #13
    //   1838: iload #7
    //   1840: iconst_1
    //   1841: iadd
    //   1842: istore #12
    //   1844: goto -> 188
    //   1847: aload_3
    //   1848: iload #12
    //   1850: baload
    //   1851: istore #13
    //   1853: iconst_1
    //   1854: istore #11
    //   1856: iinc #12, 1
    //   1859: goto -> 1862
    //   1862: aload_0
    //   1863: aload_3
    //   1864: iload #7
    //   1866: baload
    //   1867: iload #7
    //   1869: iconst_1
    //   1870: iadd
    //   1871: iload #8
    //   1873: iload #9
    //   1875: iload #10
    //   1877: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BIIII)V
    //   1880: goto -> 188
    //   1883: aload_3
    //   1884: iload #12
    //   1886: invokestatic getIndex : ([BI)I
    //   1889: istore #13
    //   1891: iinc #12, 2
    //   1894: aload_0
    //   1895: iload #13
    //   1897: invokevirtual parensIndex : (I)I
    //   1900: istore #7
    //   1902: aload_0
    //   1903: iload #13
    //   1905: iload #7
    //   1907: aload_0
    //   1908: getfield cp : I
    //   1911: iload #7
    //   1913: isub
    //   1914: invokevirtual setParens : (III)V
    //   1917: aload_3
    //   1918: iload #12
    //   1920: baload
    //   1921: istore #13
    //   1923: iinc #12, 1
    //   1926: goto -> 188
    //   1929: aload_3
    //   1930: iload #12
    //   1932: invokestatic getIndex : ([BI)I
    //   1935: istore #13
    //   1937: iinc #12, 2
    //   1940: aload_0
    //   1941: iload #13
    //   1943: aload_0
    //   1944: getfield cp : I
    //   1947: iconst_0
    //   1948: invokevirtual setParens : (III)V
    //   1951: aload_3
    //   1952: iload #12
    //   1954: baload
    //   1955: istore #13
    //   1957: iinc #12, 1
    //   1960: goto -> 188
    //   1963: goto -> 1370
    //   1966: iconst_0
    //   1967: istore #14
    //   1969: iconst_0
    //   1970: istore #5
    //   1972: iconst_0
    //   1973: istore #8
    //   1975: iconst_0
    //   1976: istore #7
    //   1978: iload #13
    //   1980: tableswitch default -> 2012, 25 -> 2100, 26 -> 2084, 27 -> 2068, 28 -> 2048
    //   2012: iload #13
    //   2014: tableswitch default -> 2044, 45 -> 2087, 46 -> 2071, 47 -> 2051, 48 -> 2103
    //   2044: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   2047: athrow
    //   2048: iconst_1
    //   2049: istore #7
    //   2051: iconst_0
    //   2052: istore #14
    //   2054: iconst_1
    //   2055: istore #8
    //   2057: iload #7
    //   2059: istore #13
    //   2061: iload #14
    //   2063: istore #7
    //   2065: goto -> 2139
    //   2068: iconst_1
    //   2069: istore #14
    //   2071: iconst_1
    //   2072: istore #7
    //   2074: iconst_m1
    //   2075: istore #8
    //   2077: iload #14
    //   2079: istore #13
    //   2081: goto -> 2139
    //   2084: iconst_1
    //   2085: istore #5
    //   2087: iconst_0
    //   2088: istore #7
    //   2090: iconst_m1
    //   2091: istore #8
    //   2093: iload #5
    //   2095: istore #13
    //   2097: goto -> 2139
    //   2100: iconst_1
    //   2101: istore #8
    //   2103: aload_3
    //   2104: iload #12
    //   2106: invokestatic getOffset : ([BI)I
    //   2109: istore #7
    //   2111: iinc #12, 2
    //   2114: aload_3
    //   2115: iload #12
    //   2117: invokestatic getOffset : ([BI)I
    //   2120: istore #13
    //   2122: iload #13
    //   2124: iconst_1
    //   2125: isub
    //   2126: istore #14
    //   2128: iinc #12, 2
    //   2131: iload #8
    //   2133: istore #13
    //   2135: iload #14
    //   2137: istore #8
    //   2139: aload_0
    //   2140: iload #7
    //   2142: iload #8
    //   2144: aload_0
    //   2145: getfield cp : I
    //   2148: aconst_null
    //   2149: iload #9
    //   2151: iload #10
    //   2153: invokestatic pushProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;IIILcom/trendmicro/hippo/regexp/REBackTrackData;II)V
    //   2156: iload #13
    //   2158: ifeq -> 2203
    //   2161: aload_0
    //   2162: bipush #51
    //   2164: iload #12
    //   2166: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BI)V
    //   2169: iload #12
    //   2171: bipush #6
    //   2173: iadd
    //   2174: istore #10
    //   2176: aload_3
    //   2177: iload #10
    //   2179: baload
    //   2180: istore #13
    //   2182: bipush #51
    //   2184: istore #9
    //   2186: iload #10
    //   2188: iconst_1
    //   2189: iadd
    //   2190: istore #7
    //   2192: iload #12
    //   2194: istore #10
    //   2196: iload #7
    //   2198: istore #12
    //   2200: goto -> 2278
    //   2203: iload #7
    //   2205: ifeq -> 2242
    //   2208: iload #12
    //   2210: bipush #6
    //   2212: iadd
    //   2213: istore #10
    //   2215: aload_3
    //   2216: iload #10
    //   2218: baload
    //   2219: istore #13
    //   2221: bipush #52
    //   2223: istore #9
    //   2225: iload #10
    //   2227: iconst_1
    //   2228: iadd
    //   2229: istore #7
    //   2231: iload #12
    //   2233: istore #10
    //   2235: iload #7
    //   2237: istore #12
    //   2239: goto -> 2278
    //   2242: aload_0
    //   2243: bipush #52
    //   2245: iload #12
    //   2247: invokestatic pushBackTrackState : (Lcom/trendmicro/hippo/regexp/REGlobalData;BI)V
    //   2250: aload_0
    //   2251: invokestatic popProgState : (Lcom/trendmicro/hippo/regexp/REGlobalData;)Lcom/trendmicro/hippo/regexp/REProgState;
    //   2254: pop
    //   2255: iinc #12, 4
    //   2258: iload #12
    //   2260: aload_3
    //   2261: iload #12
    //   2263: invokestatic getOffset : ([BI)I
    //   2266: iadd
    //   2267: istore #12
    //   2269: aload_3
    //   2270: iload #12
    //   2272: baload
    //   2273: istore #13
    //   2275: iinc #12, 1
    //   2278: goto -> 188
    //   2281: iconst_1
    //   2282: ireturn
  }
  
  private static boolean flatNIMatcher(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, String paramString, int paramInt3) {
    if (paramREGlobalData.cp + paramInt2 > paramInt3)
      return false; 
    char[] arrayOfChar = paramREGlobalData.regexp.source;
    for (paramInt3 = 0; paramInt3 < paramInt2; paramInt3++) {
      char c1 = arrayOfChar[paramInt1 + paramInt3];
      char c2 = paramString.charAt(paramREGlobalData.cp + paramInt3);
      if (c1 != c2 && upcase(c1) != upcase(c2))
        return false; 
    } 
    paramREGlobalData.cp += paramInt2;
    return true;
  }
  
  private static boolean flatNMatcher(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, String paramString, int paramInt3) {
    if (paramREGlobalData.cp + paramInt2 > paramInt3)
      return false; 
    for (paramInt3 = 0; paramInt3 < paramInt2; paramInt3++) {
      if (paramREGlobalData.regexp.source[paramInt1 + paramInt3] != paramString.charAt(paramREGlobalData.cp + paramInt3))
        return false; 
    } 
    paramREGlobalData.cp += paramInt2;
    return true;
  }
  
  private static int getDecimalValue(char paramChar, CompilerState paramCompilerState, int paramInt, String paramString) {
    boolean bool = false;
    int i = paramCompilerState.cp;
    char[] arrayOfChar = paramCompilerState.cpbegin;
    int j;
    for (j = paramChar - 48; paramCompilerState.cp != paramCompilerState.cpend; j = k) {
      char c = arrayOfChar[paramCompilerState.cp];
      if (!isDigit(c))
        break; 
      boolean bool1 = bool;
      int k = j;
      if (!bool) {
        k = j * 10 + c - 48;
        if (k < paramInt) {
          bool1 = bool;
        } else {
          bool1 = true;
          k = paramInt;
        } 
      } 
      paramCompilerState.cp++;
      bool = bool1;
    } 
    if (bool)
      reportError(paramString, String.valueOf(arrayOfChar, i, paramCompilerState.cp - i)); 
    return j;
  }
  
  private static RegExpImpl getImpl(Context paramContext) {
    return (RegExpImpl)ScriptRuntime.getRegExpProxy(paramContext);
  }
  
  private static int getIndex(byte[] paramArrayOfbyte, int paramInt) {
    return (paramArrayOfbyte[paramInt] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 1] & 0xFF;
  }
  
  private static int getOffset(byte[] paramArrayOfbyte, int paramInt) {
    return getIndex(paramArrayOfbyte, paramInt);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    NativeRegExp nativeRegExp = new NativeRegExp();
    nativeRegExp.re = compileRE(paramContext, "", (String)null, false);
    nativeRegExp.activatePrototypeMap(6);
    nativeRegExp.setParentScope(paramScriptable);
    nativeRegExp.setPrototype(getObjectPrototype(paramScriptable));
    NativeRegExpCtor nativeRegExpCtor = new NativeRegExpCtor();
    nativeRegExp.defineProperty("constructor", nativeRegExpCtor, 2);
    ScriptRuntime.setFunctionProtoAndParent(nativeRegExpCtor, paramScriptable);
    nativeRegExpCtor.setImmunePrototypeProperty(nativeRegExp);
    if (paramBoolean) {
      nativeRegExp.sealObject();
      nativeRegExpCtor.sealObject();
    } 
    defineProperty(paramScriptable, "RegExp", nativeRegExpCtor, 2);
  }
  
  private static boolean isControlLetter(char paramChar) {
    boolean bool;
    if (('a' <= paramChar && paramChar <= 'z') || ('A' <= paramChar && paramChar <= 'Z')) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isDigit(char paramChar) {
    boolean bool;
    if ('0' <= paramChar && paramChar <= '9') {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isLineTerm(char paramChar) {
    return ScriptRuntime.isJSLineTerminator(paramChar);
  }
  
  private static boolean isREWhiteSpace(int paramInt) {
    return ScriptRuntime.isJSWhitespaceOrLineTerminator(paramInt);
  }
  
  private static boolean isWord(char paramChar) {
    return (('a' <= paramChar && paramChar <= 'z') || ('A' <= paramChar && paramChar <= 'Z') || isDigit(paramChar) || paramChar == '_');
  }
  
  private static boolean matchRegExp(REGlobalData paramREGlobalData, RECompiled paramRECompiled, String paramString, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramRECompiled.parenCount != 0) {
      paramREGlobalData.parens = new long[paramRECompiled.parenCount];
    } else {
      paramREGlobalData.parens = null;
    } 
    paramREGlobalData.backTrackStackTop = null;
    paramREGlobalData.stateStackTop = null;
    if (paramBoolean || (paramRECompiled.flags & 0x4) != 0) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    paramREGlobalData.multiline = paramBoolean;
    paramREGlobalData.regexp = paramRECompiled;
    int i = paramREGlobalData.regexp.anchorCh;
    int j;
    for (j = paramInt1; j <= paramInt2; j = paramREGlobalData.skipped + paramInt1 + 1) {
      int k = j;
      if (i >= 0)
        while (true) {
          if (j == paramInt2)
            return false; 
          char c = paramString.charAt(j);
          k = j;
          if (c != i) {
            if ((paramREGlobalData.regexp.flags & 0x2) != 0 && upcase(c) == upcase((char)i)) {
              k = j;
              break;
            } 
            j++;
            continue;
          } 
          break;
        }  
      paramREGlobalData.cp = k;
      paramREGlobalData.skipped = k - paramInt1;
      for (j = 0; j < paramRECompiled.parenCount; j++)
        paramREGlobalData.parens[j] = -1L; 
      paramBoolean = executeREBytecode(paramREGlobalData, paramString, paramInt2);
      paramREGlobalData.backTrackStackTop = null;
      paramREGlobalData.stateStackTop = null;
      if (paramBoolean)
        return true; 
      if (i == -2 && !paramREGlobalData.multiline) {
        paramREGlobalData.skipped = paramInt2;
        return false;
      } 
    } 
    return false;
  }
  
  private static boolean parseAlternative(CompilerState paramCompilerState) {
    RENode rENode1 = null;
    RENode rENode2 = null;
    char[] arrayOfChar = paramCompilerState.cpbegin;
    label24: while (paramCompilerState.cp != paramCompilerState.cpend && arrayOfChar[paramCompilerState.cp] != '|' && (paramCompilerState.parenNesting == 0 || arrayOfChar[paramCompilerState.cp] != ')')) {
      RENode rENode3;
      RENode rENode4;
      if (!parseTerm(paramCompilerState))
        return false; 
      if (rENode1 == null) {
        rENode3 = paramCompilerState.result;
        rENode4 = rENode3;
      } else {
        rENode2.next = paramCompilerState.result;
        rENode4 = rENode2;
        rENode3 = rENode1;
      } 
      while (true) {
        rENode1 = rENode3;
        rENode2 = rENode4;
        if (rENode4.next != null) {
          rENode4 = rENode4.next;
          continue;
        } 
        continue label24;
      } 
    } 
    if (rENode1 == null) {
      paramCompilerState.result = new RENode((byte)1);
    } else {
      paramCompilerState.result = rENode1;
    } 
    return true;
  }
  
  private static boolean parseDisjunction(CompilerState paramCompilerState) {
    if (!parseAlternative(paramCompilerState))
      return false; 
    char[] arrayOfChar = paramCompilerState.cpbegin;
    int i = paramCompilerState.cp;
    if (i != arrayOfChar.length && arrayOfChar[i] == '|') {
      paramCompilerState.cp++;
      RENode rENode = new RENode((byte)31);
      rENode.kid = paramCompilerState.result;
      if (!parseDisjunction(paramCompilerState))
        return false; 
      rENode.kid2 = paramCompilerState.result;
      paramCompilerState.result = rENode;
      if (rENode.kid.op == 14 && rENode.kid2.op == 14) {
        if ((paramCompilerState.flags & 0x2) == 0) {
          i = 53;
        } else {
          i = 54;
        } 
        rENode.op = (byte)i;
        rENode.chr = (char)rENode.kid.chr;
        rENode.index = rENode.kid2.chr;
        paramCompilerState.progLength += 13;
      } else if (rENode.kid.op == 22 && rENode.kid.index < 256 && rENode.kid2.op == 14 && (paramCompilerState.flags & 0x2) == 0) {
        rENode.op = (byte)55;
        rENode.chr = (char)rENode.kid2.chr;
        rENode.index = rENode.kid.index;
        paramCompilerState.progLength += 13;
      } else if (rENode.kid.op == 14 && rENode.kid2.op == 22 && rENode.kid2.index < 256 && (paramCompilerState.flags & 0x2) == 0) {
        rENode.op = (byte)55;
        rENode.chr = (char)rENode.kid.chr;
        rENode.index = rENode.kid2.index;
        paramCompilerState.progLength += 13;
      } else {
        paramCompilerState.progLength += 9;
      } 
    } 
    return true;
  }
  
  private static boolean parseTerm(CompilerState paramCompilerState) {
    // Byte code:
    //   0: aload_0
    //   1: getfield cpbegin : [C
    //   4: astore_1
    //   5: aload_0
    //   6: getfield cp : I
    //   9: istore_2
    //   10: aload_0
    //   11: iload_2
    //   12: iconst_1
    //   13: iadd
    //   14: putfield cp : I
    //   17: aload_1
    //   18: iload_2
    //   19: caload
    //   20: istore_2
    //   21: iconst_2
    //   22: istore_3
    //   23: aload_0
    //   24: getfield parenCount : I
    //   27: istore #4
    //   29: iload_2
    //   30: bipush #36
    //   32: if_icmpeq -> 2229
    //   35: iload_2
    //   36: bipush #46
    //   38: if_icmpeq -> 1632
    //   41: iload_2
    //   42: bipush #63
    //   44: if_icmpeq -> 1613
    //   47: iload_2
    //   48: bipush #94
    //   50: if_icmpeq -> 1589
    //   53: iload_2
    //   54: bipush #91
    //   56: if_icmpeq -> 1411
    //   59: iload_2
    //   60: bipush #92
    //   62: if_icmpeq -> 430
    //   65: iload_2
    //   66: tableswitch default -> 96, 40 -> 163, 41 -> 152, 42 -> 1613, 43 -> 1613
    //   96: aload_0
    //   97: new com/trendmicro/hippo/regexp/RENode
    //   100: dup
    //   101: bipush #14
    //   103: invokespecial <init> : (B)V
    //   106: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   109: aload_0
    //   110: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   113: iload_2
    //   114: i2c
    //   115: putfield chr : C
    //   118: aload_0
    //   119: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   122: iconst_1
    //   123: putfield length : I
    //   126: aload_0
    //   127: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   130: aload_0
    //   131: getfield cp : I
    //   134: iconst_1
    //   135: isub
    //   136: putfield flatIndex : I
    //   139: aload_0
    //   140: aload_0
    //   141: getfield progLength : I
    //   144: iconst_3
    //   145: iadd
    //   146: putfield progLength : I
    //   149: goto -> 1655
    //   152: ldc_w 'msg.re.unmatched.right.paren'
    //   155: ldc_w ''
    //   158: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   161: iconst_0
    //   162: ireturn
    //   163: aconst_null
    //   164: astore #5
    //   166: aload_0
    //   167: getfield cp : I
    //   170: istore_2
    //   171: aload_0
    //   172: getfield cp : I
    //   175: iconst_1
    //   176: iadd
    //   177: aload_0
    //   178: getfield cpend : I
    //   181: if_icmpge -> 292
    //   184: aload_1
    //   185: aload_0
    //   186: getfield cp : I
    //   189: caload
    //   190: bipush #63
    //   192: if_icmpne -> 292
    //   195: aload_1
    //   196: aload_0
    //   197: getfield cp : I
    //   200: iconst_1
    //   201: iadd
    //   202: caload
    //   203: istore_2
    //   204: iload_2
    //   205: bipush #61
    //   207: if_icmpeq -> 222
    //   210: iload_2
    //   211: bipush #33
    //   213: if_icmpeq -> 222
    //   216: iload_2
    //   217: bipush #58
    //   219: if_icmpne -> 292
    //   222: aload_0
    //   223: aload_0
    //   224: getfield cp : I
    //   227: iconst_2
    //   228: iadd
    //   229: putfield cp : I
    //   232: iload_2
    //   233: bipush #61
    //   235: if_icmpne -> 262
    //   238: new com/trendmicro/hippo/regexp/RENode
    //   241: dup
    //   242: bipush #41
    //   244: invokespecial <init> : (B)V
    //   247: astore #5
    //   249: aload_0
    //   250: aload_0
    //   251: getfield progLength : I
    //   254: iconst_4
    //   255: iadd
    //   256: putfield progLength : I
    //   259: goto -> 332
    //   262: iload_2
    //   263: bipush #33
    //   265: if_icmpne -> 332
    //   268: new com/trendmicro/hippo/regexp/RENode
    //   271: dup
    //   272: bipush #42
    //   274: invokespecial <init> : (B)V
    //   277: astore #5
    //   279: aload_0
    //   280: aload_0
    //   281: getfield progLength : I
    //   284: iconst_4
    //   285: iadd
    //   286: putfield progLength : I
    //   289: goto -> 332
    //   292: new com/trendmicro/hippo/regexp/RENode
    //   295: dup
    //   296: bipush #29
    //   298: invokespecial <init> : (B)V
    //   301: astore #5
    //   303: aload_0
    //   304: aload_0
    //   305: getfield progLength : I
    //   308: bipush #6
    //   310: iadd
    //   311: putfield progLength : I
    //   314: aload_0
    //   315: getfield parenCount : I
    //   318: istore_2
    //   319: aload_0
    //   320: iload_2
    //   321: iconst_1
    //   322: iadd
    //   323: putfield parenCount : I
    //   326: aload #5
    //   328: iload_2
    //   329: putfield parenIndex : I
    //   332: aload_0
    //   333: aload_0
    //   334: getfield parenNesting : I
    //   337: iconst_1
    //   338: iadd
    //   339: putfield parenNesting : I
    //   342: aload_0
    //   343: invokestatic parseDisjunction : (Lcom/trendmicro/hippo/regexp/CompilerState;)Z
    //   346: ifne -> 351
    //   349: iconst_0
    //   350: ireturn
    //   351: aload_0
    //   352: getfield cp : I
    //   355: aload_0
    //   356: getfield cpend : I
    //   359: if_icmpeq -> 419
    //   362: aload_1
    //   363: aload_0
    //   364: getfield cp : I
    //   367: caload
    //   368: bipush #41
    //   370: if_icmpeq -> 376
    //   373: goto -> 419
    //   376: aload_0
    //   377: aload_0
    //   378: getfield cp : I
    //   381: iconst_1
    //   382: iadd
    //   383: putfield cp : I
    //   386: aload_0
    //   387: aload_0
    //   388: getfield parenNesting : I
    //   391: iconst_1
    //   392: isub
    //   393: putfield parenNesting : I
    //   396: aload #5
    //   398: ifnull -> 1655
    //   401: aload #5
    //   403: aload_0
    //   404: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   407: putfield kid : Lcom/trendmicro/hippo/regexp/RENode;
    //   410: aload_0
    //   411: aload #5
    //   413: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   416: goto -> 1655
    //   419: ldc_w 'msg.unterm.paren'
    //   422: ldc_w ''
    //   425: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   428: iconst_0
    //   429: ireturn
    //   430: aload_0
    //   431: getfield cp : I
    //   434: aload_0
    //   435: getfield cpend : I
    //   438: if_icmpge -> 1400
    //   441: aload_0
    //   442: getfield cp : I
    //   445: istore_2
    //   446: aload_0
    //   447: iload_2
    //   448: iconst_1
    //   449: iadd
    //   450: putfield cp : I
    //   453: aload_1
    //   454: iload_2
    //   455: caload
    //   456: istore #6
    //   458: iload #6
    //   460: bipush #66
    //   462: if_icmpeq -> 1376
    //   465: iload #6
    //   467: bipush #68
    //   469: if_icmpeq -> 1350
    //   472: iload #6
    //   474: bipush #83
    //   476: if_icmpeq -> 1324
    //   479: iload #6
    //   481: bipush #87
    //   483: if_icmpeq -> 1298
    //   486: iload #6
    //   488: bipush #102
    //   490: if_icmpeq -> 1289
    //   493: iload #6
    //   495: bipush #110
    //   497: if_icmpeq -> 1280
    //   500: iload #6
    //   502: tableswitch default -> 556, 48 -> 1197, 49 -> 995, 50 -> 995, 51 -> 995, 52 -> 995, 53 -> 995, 54 -> 995, 55 -> 995, 56 -> 995, 57 -> 995
    //   556: iload #6
    //   558: tableswitch default -> 584, 98 -> 971, 99 -> 897, 100 -> 871
    //   584: iload #6
    //   586: tableswitch default -> 628, 114 -> 862, 115 -> 836, 116 -> 827, 117 -> 719, 118 -> 710, 119 -> 684, 120 -> 723
    //   628: aload_0
    //   629: new com/trendmicro/hippo/regexp/RENode
    //   632: dup
    //   633: bipush #14
    //   635: invokespecial <init> : (B)V
    //   638: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   641: aload_0
    //   642: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   645: iload #6
    //   647: putfield chr : C
    //   650: aload_0
    //   651: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   654: iconst_1
    //   655: putfield length : I
    //   658: aload_0
    //   659: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   662: aload_0
    //   663: getfield cp : I
    //   666: iconst_1
    //   667: isub
    //   668: putfield flatIndex : I
    //   671: aload_0
    //   672: aload_0
    //   673: getfield progLength : I
    //   676: iconst_3
    //   677: iadd
    //   678: putfield progLength : I
    //   681: goto -> 1655
    //   684: aload_0
    //   685: new com/trendmicro/hippo/regexp/RENode
    //   688: dup
    //   689: bipush #9
    //   691: invokespecial <init> : (B)V
    //   694: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   697: aload_0
    //   698: aload_0
    //   699: getfield progLength : I
    //   702: iconst_1
    //   703: iadd
    //   704: putfield progLength : I
    //   707: goto -> 1655
    //   710: aload_0
    //   711: bipush #11
    //   713: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   716: goto -> 1655
    //   719: iconst_2
    //   720: iconst_2
    //   721: iadd
    //   722: istore_3
    //   723: iconst_0
    //   724: istore_2
    //   725: iconst_0
    //   726: istore #7
    //   728: iload_2
    //   729: istore #8
    //   731: iload #7
    //   733: iload_3
    //   734: if_icmpge -> 817
    //   737: iload_2
    //   738: istore #8
    //   740: aload_0
    //   741: getfield cp : I
    //   744: aload_0
    //   745: getfield cpend : I
    //   748: if_icmpge -> 817
    //   751: aload_0
    //   752: getfield cp : I
    //   755: istore #8
    //   757: aload_0
    //   758: iload #8
    //   760: iconst_1
    //   761: iadd
    //   762: putfield cp : I
    //   765: aload_1
    //   766: iload #8
    //   768: caload
    //   769: iload_2
    //   770: invokestatic xDigitToInt : (II)I
    //   773: istore_2
    //   774: iload_2
    //   775: ifge -> 811
    //   778: aload_0
    //   779: aload_0
    //   780: getfield cp : I
    //   783: iload #7
    //   785: iconst_2
    //   786: iadd
    //   787: isub
    //   788: putfield cp : I
    //   791: aload_0
    //   792: getfield cp : I
    //   795: istore_2
    //   796: aload_0
    //   797: iload_2
    //   798: iconst_1
    //   799: iadd
    //   800: putfield cp : I
    //   803: aload_1
    //   804: iload_2
    //   805: caload
    //   806: istore #8
    //   808: goto -> 817
    //   811: iinc #7, 1
    //   814: goto -> 728
    //   817: aload_0
    //   818: iload #8
    //   820: i2c
    //   821: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   824: goto -> 1655
    //   827: aload_0
    //   828: bipush #9
    //   830: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   833: goto -> 1655
    //   836: aload_0
    //   837: new com/trendmicro/hippo/regexp/RENode
    //   840: dup
    //   841: bipush #11
    //   843: invokespecial <init> : (B)V
    //   846: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   849: aload_0
    //   850: aload_0
    //   851: getfield progLength : I
    //   854: iconst_1
    //   855: iadd
    //   856: putfield progLength : I
    //   859: goto -> 1655
    //   862: aload_0
    //   863: bipush #13
    //   865: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   868: goto -> 1655
    //   871: aload_0
    //   872: new com/trendmicro/hippo/regexp/RENode
    //   875: dup
    //   876: bipush #7
    //   878: invokespecial <init> : (B)V
    //   881: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   884: aload_0
    //   885: aload_0
    //   886: getfield progLength : I
    //   889: iconst_1
    //   890: iadd
    //   891: putfield progLength : I
    //   894: goto -> 1655
    //   897: aload_0
    //   898: getfield cp : I
    //   901: aload_0
    //   902: getfield cpend : I
    //   905: if_icmpge -> 946
    //   908: aload_1
    //   909: aload_0
    //   910: getfield cp : I
    //   913: caload
    //   914: invokestatic isControlLetter : (C)Z
    //   917: ifeq -> 946
    //   920: aload_0
    //   921: getfield cp : I
    //   924: istore_2
    //   925: aload_0
    //   926: iload_2
    //   927: iconst_1
    //   928: iadd
    //   929: putfield cp : I
    //   932: aload_1
    //   933: iload_2
    //   934: caload
    //   935: bipush #31
    //   937: iand
    //   938: i2c
    //   939: istore_2
    //   940: iload_2
    //   941: istore #6
    //   943: goto -> 962
    //   946: aload_0
    //   947: aload_0
    //   948: getfield cp : I
    //   951: iconst_1
    //   952: isub
    //   953: putfield cp : I
    //   956: bipush #92
    //   958: istore_2
    //   959: iload_2
    //   960: istore #6
    //   962: aload_0
    //   963: iload #6
    //   965: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   968: goto -> 1655
    //   971: aload_0
    //   972: new com/trendmicro/hippo/regexp/RENode
    //   975: dup
    //   976: iconst_4
    //   977: invokespecial <init> : (B)V
    //   980: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   983: aload_0
    //   984: aload_0
    //   985: getfield progLength : I
    //   988: iconst_1
    //   989: iadd
    //   990: putfield progLength : I
    //   993: iconst_1
    //   994: ireturn
    //   995: aload_0
    //   996: getfield cp : I
    //   999: istore_2
    //   1000: iload #6
    //   1002: aload_0
    //   1003: ldc 65535
    //   1005: ldc_w 'msg.overlarge.backref'
    //   1008: invokestatic getDecimalValue : (CLcom/trendmicro/hippo/regexp/CompilerState;ILjava/lang/String;)I
    //   1011: istore_3
    //   1012: iload_3
    //   1013: aload_0
    //   1014: getfield backReferenceLimit : I
    //   1017: if_icmple -> 1033
    //   1020: aload_0
    //   1021: getfield cx : Lcom/trendmicro/hippo/Context;
    //   1024: ldc_w 'msg.bad.backref'
    //   1027: ldc_w ''
    //   1030: invokestatic reportWarning : (Lcom/trendmicro/hippo/Context;Ljava/lang/String;Ljava/lang/String;)V
    //   1033: iload_3
    //   1034: aload_0
    //   1035: getfield backReferenceLimit : I
    //   1038: if_icmple -> 1148
    //   1041: aload_0
    //   1042: iload_2
    //   1043: iconst_1
    //   1044: isub
    //   1045: putfield cp : I
    //   1048: iload #6
    //   1050: bipush #56
    //   1052: if_icmplt -> 1064
    //   1055: aload_0
    //   1056: bipush #92
    //   1058: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   1061: goto -> 1655
    //   1064: aload_0
    //   1065: aload_0
    //   1066: getfield cp : I
    //   1069: iconst_1
    //   1070: iadd
    //   1071: putfield cp : I
    //   1074: iload #6
    //   1076: bipush #48
    //   1078: isub
    //   1079: istore_2
    //   1080: iload_2
    //   1081: bipush #32
    //   1083: if_icmpge -> 1139
    //   1086: aload_0
    //   1087: getfield cp : I
    //   1090: aload_0
    //   1091: getfield cpend : I
    //   1094: if_icmpge -> 1139
    //   1097: aload_1
    //   1098: aload_0
    //   1099: getfield cp : I
    //   1102: caload
    //   1103: istore_3
    //   1104: iload_3
    //   1105: bipush #48
    //   1107: if_icmplt -> 1139
    //   1110: iload_3
    //   1111: bipush #55
    //   1113: if_icmpgt -> 1139
    //   1116: aload_0
    //   1117: aload_0
    //   1118: getfield cp : I
    //   1121: iconst_1
    //   1122: iadd
    //   1123: putfield cp : I
    //   1126: iload_2
    //   1127: bipush #8
    //   1129: imul
    //   1130: iload_3
    //   1131: bipush #48
    //   1133: isub
    //   1134: iadd
    //   1135: istore_2
    //   1136: goto -> 1080
    //   1139: aload_0
    //   1140: iload_2
    //   1141: i2c
    //   1142: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   1145: goto -> 1655
    //   1148: aload_0
    //   1149: new com/trendmicro/hippo/regexp/RENode
    //   1152: dup
    //   1153: bipush #13
    //   1155: invokespecial <init> : (B)V
    //   1158: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1161: aload_0
    //   1162: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1165: iload_3
    //   1166: iconst_1
    //   1167: isub
    //   1168: putfield parenIndex : I
    //   1171: aload_0
    //   1172: aload_0
    //   1173: getfield progLength : I
    //   1176: iconst_3
    //   1177: iadd
    //   1178: putfield progLength : I
    //   1181: aload_0
    //   1182: getfield maxBackReference : I
    //   1185: iload_3
    //   1186: if_icmpge -> 1655
    //   1189: aload_0
    //   1190: iload_3
    //   1191: putfield maxBackReference : I
    //   1194: goto -> 1655
    //   1197: aload_0
    //   1198: getfield cx : Lcom/trendmicro/hippo/Context;
    //   1201: ldc_w 'msg.bad.backref'
    //   1204: ldc_w ''
    //   1207: invokestatic reportWarning : (Lcom/trendmicro/hippo/Context;Ljava/lang/String;Ljava/lang/String;)V
    //   1210: iconst_0
    //   1211: istore_2
    //   1212: iload_2
    //   1213: bipush #32
    //   1215: if_icmpge -> 1271
    //   1218: aload_0
    //   1219: getfield cp : I
    //   1222: aload_0
    //   1223: getfield cpend : I
    //   1226: if_icmpge -> 1271
    //   1229: aload_1
    //   1230: aload_0
    //   1231: getfield cp : I
    //   1234: caload
    //   1235: istore_3
    //   1236: iload_3
    //   1237: bipush #48
    //   1239: if_icmplt -> 1271
    //   1242: iload_3
    //   1243: bipush #55
    //   1245: if_icmpgt -> 1271
    //   1248: aload_0
    //   1249: aload_0
    //   1250: getfield cp : I
    //   1253: iconst_1
    //   1254: iadd
    //   1255: putfield cp : I
    //   1258: iload_2
    //   1259: bipush #8
    //   1261: imul
    //   1262: iload_3
    //   1263: bipush #48
    //   1265: isub
    //   1266: iadd
    //   1267: istore_2
    //   1268: goto -> 1212
    //   1271: aload_0
    //   1272: iload_2
    //   1273: i2c
    //   1274: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   1277: goto -> 1655
    //   1280: aload_0
    //   1281: bipush #10
    //   1283: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   1286: goto -> 1655
    //   1289: aload_0
    //   1290: bipush #12
    //   1292: invokestatic doFlat : (Lcom/trendmicro/hippo/regexp/CompilerState;C)V
    //   1295: goto -> 1655
    //   1298: aload_0
    //   1299: new com/trendmicro/hippo/regexp/RENode
    //   1302: dup
    //   1303: bipush #10
    //   1305: invokespecial <init> : (B)V
    //   1308: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1311: aload_0
    //   1312: aload_0
    //   1313: getfield progLength : I
    //   1316: iconst_1
    //   1317: iadd
    //   1318: putfield progLength : I
    //   1321: goto -> 1655
    //   1324: aload_0
    //   1325: new com/trendmicro/hippo/regexp/RENode
    //   1328: dup
    //   1329: bipush #12
    //   1331: invokespecial <init> : (B)V
    //   1334: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1337: aload_0
    //   1338: aload_0
    //   1339: getfield progLength : I
    //   1342: iconst_1
    //   1343: iadd
    //   1344: putfield progLength : I
    //   1347: goto -> 1655
    //   1350: aload_0
    //   1351: new com/trendmicro/hippo/regexp/RENode
    //   1354: dup
    //   1355: bipush #8
    //   1357: invokespecial <init> : (B)V
    //   1360: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1363: aload_0
    //   1364: aload_0
    //   1365: getfield progLength : I
    //   1368: iconst_1
    //   1369: iadd
    //   1370: putfield progLength : I
    //   1373: goto -> 1655
    //   1376: aload_0
    //   1377: new com/trendmicro/hippo/regexp/RENode
    //   1380: dup
    //   1381: iconst_5
    //   1382: invokespecial <init> : (B)V
    //   1385: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1388: aload_0
    //   1389: aload_0
    //   1390: getfield progLength : I
    //   1393: iconst_1
    //   1394: iadd
    //   1395: putfield progLength : I
    //   1398: iconst_1
    //   1399: ireturn
    //   1400: ldc_w 'msg.trail.backslash'
    //   1403: ldc_w ''
    //   1406: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   1409: iconst_0
    //   1410: ireturn
    //   1411: aload_0
    //   1412: new com/trendmicro/hippo/regexp/RENode
    //   1415: dup
    //   1416: bipush #22
    //   1418: invokespecial <init> : (B)V
    //   1421: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1424: aload_0
    //   1425: getfield cp : I
    //   1428: istore_2
    //   1429: aload_0
    //   1430: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1433: iload_2
    //   1434: putfield startIndex : I
    //   1437: aload_0
    //   1438: getfield cp : I
    //   1441: aload_0
    //   1442: getfield cpend : I
    //   1445: if_icmpne -> 1459
    //   1448: ldc_w 'msg.unterm.class'
    //   1451: ldc_w ''
    //   1454: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   1457: iconst_0
    //   1458: ireturn
    //   1459: aload_1
    //   1460: aload_0
    //   1461: getfield cp : I
    //   1464: caload
    //   1465: bipush #92
    //   1467: if_icmpne -> 1483
    //   1470: aload_0
    //   1471: aload_0
    //   1472: getfield cp : I
    //   1475: iconst_1
    //   1476: iadd
    //   1477: putfield cp : I
    //   1480: goto -> 1576
    //   1483: aload_1
    //   1484: aload_0
    //   1485: getfield cp : I
    //   1488: caload
    //   1489: bipush #93
    //   1491: if_icmpne -> 1576
    //   1494: aload_0
    //   1495: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1498: aload_0
    //   1499: getfield cp : I
    //   1502: iload_2
    //   1503: isub
    //   1504: putfield kidlen : I
    //   1507: aload_0
    //   1508: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1511: astore #5
    //   1513: aload_0
    //   1514: getfield classCount : I
    //   1517: istore_3
    //   1518: aload_0
    //   1519: iload_3
    //   1520: iconst_1
    //   1521: iadd
    //   1522: putfield classCount : I
    //   1525: aload #5
    //   1527: iload_3
    //   1528: putfield index : I
    //   1531: aload_0
    //   1532: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1535: astore #5
    //   1537: aload_0
    //   1538: getfield cp : I
    //   1541: istore_3
    //   1542: aload_0
    //   1543: iload_3
    //   1544: iconst_1
    //   1545: iadd
    //   1546: putfield cp : I
    //   1549: aload_0
    //   1550: aload #5
    //   1552: aload_1
    //   1553: iload_2
    //   1554: iload_3
    //   1555: invokestatic calculateBitmapSize : (Lcom/trendmicro/hippo/regexp/CompilerState;Lcom/trendmicro/hippo/regexp/RENode;[CII)Z
    //   1558: ifne -> 1563
    //   1561: iconst_0
    //   1562: ireturn
    //   1563: aload_0
    //   1564: aload_0
    //   1565: getfield progLength : I
    //   1568: iconst_3
    //   1569: iadd
    //   1570: putfield progLength : I
    //   1573: goto -> 1655
    //   1576: aload_0
    //   1577: aload_0
    //   1578: getfield cp : I
    //   1581: iconst_1
    //   1582: iadd
    //   1583: putfield cp : I
    //   1586: goto -> 1437
    //   1589: aload_0
    //   1590: new com/trendmicro/hippo/regexp/RENode
    //   1593: dup
    //   1594: iconst_2
    //   1595: invokespecial <init> : (B)V
    //   1598: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1601: aload_0
    //   1602: aload_0
    //   1603: getfield progLength : I
    //   1606: iconst_1
    //   1607: iadd
    //   1608: putfield progLength : I
    //   1611: iconst_1
    //   1612: ireturn
    //   1613: ldc_w 'msg.bad.quant'
    //   1616: aload_1
    //   1617: aload_0
    //   1618: getfield cp : I
    //   1621: iconst_1
    //   1622: isub
    //   1623: caload
    //   1624: invokestatic valueOf : (C)Ljava/lang/String;
    //   1627: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   1630: iconst_0
    //   1631: ireturn
    //   1632: aload_0
    //   1633: new com/trendmicro/hippo/regexp/RENode
    //   1636: dup
    //   1637: bipush #6
    //   1639: invokespecial <init> : (B)V
    //   1642: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1645: aload_0
    //   1646: aload_0
    //   1647: getfield progLength : I
    //   1650: iconst_1
    //   1651: iadd
    //   1652: putfield progLength : I
    //   1655: aload_0
    //   1656: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1659: astore #5
    //   1661: aload_0
    //   1662: getfield cp : I
    //   1665: aload_0
    //   1666: getfield cpend : I
    //   1669: if_icmpne -> 1674
    //   1672: iconst_1
    //   1673: ireturn
    //   1674: iconst_0
    //   1675: istore_2
    //   1676: iconst_0
    //   1677: istore #8
    //   1679: aload_1
    //   1680: aload_0
    //   1681: getfield cp : I
    //   1684: caload
    //   1685: istore_3
    //   1686: iload_3
    //   1687: bipush #42
    //   1689: if_icmpeq -> 2086
    //   1692: iload_3
    //   1693: bipush #43
    //   1695: if_icmpeq -> 2041
    //   1698: iload_3
    //   1699: bipush #63
    //   1701: if_icmpeq -> 1996
    //   1704: iload_3
    //   1705: bipush #123
    //   1707: if_icmpeq -> 1713
    //   1710: goto -> 2128
    //   1713: iconst_m1
    //   1714: istore #9
    //   1716: aload_0
    //   1717: getfield cp : I
    //   1720: istore #10
    //   1722: aload_0
    //   1723: getfield cp : I
    //   1726: iconst_1
    //   1727: iadd
    //   1728: istore_2
    //   1729: aload_0
    //   1730: iload_2
    //   1731: putfield cp : I
    //   1734: iload #8
    //   1736: istore_3
    //   1737: iload_2
    //   1738: aload_1
    //   1739: arraylength
    //   1740: if_icmpge -> 1979
    //   1743: aload_1
    //   1744: aload_0
    //   1745: getfield cp : I
    //   1748: caload
    //   1749: istore #6
    //   1751: iload #8
    //   1753: istore_3
    //   1754: iload #6
    //   1756: invokestatic isDigit : (C)Z
    //   1759: ifeq -> 1979
    //   1762: aload_0
    //   1763: aload_0
    //   1764: getfield cp : I
    //   1767: iconst_1
    //   1768: iadd
    //   1769: putfield cp : I
    //   1772: iload #6
    //   1774: aload_0
    //   1775: ldc 65535
    //   1777: ldc_w 'msg.overlarge.min'
    //   1780: invokestatic getDecimalValue : (CLcom/trendmicro/hippo/regexp/CompilerState;ILjava/lang/String;)I
    //   1783: istore #11
    //   1785: iload #8
    //   1787: istore_3
    //   1788: aload_0
    //   1789: getfield cp : I
    //   1792: aload_1
    //   1793: arraylength
    //   1794: if_icmpge -> 1979
    //   1797: aload_1
    //   1798: aload_0
    //   1799: getfield cp : I
    //   1802: caload
    //   1803: istore #7
    //   1805: iload #7
    //   1807: bipush #44
    //   1809: if_icmpne -> 1923
    //   1812: aload_0
    //   1813: getfield cp : I
    //   1816: iconst_1
    //   1817: iadd
    //   1818: istore_2
    //   1819: aload_0
    //   1820: iload_2
    //   1821: putfield cp : I
    //   1824: iload_2
    //   1825: aload_1
    //   1826: arraylength
    //   1827: if_icmpge -> 1923
    //   1830: aload_1
    //   1831: aload_0
    //   1832: getfield cp : I
    //   1835: caload
    //   1836: istore #6
    //   1838: iload #6
    //   1840: istore #7
    //   1842: iload #9
    //   1844: istore_2
    //   1845: iload #6
    //   1847: invokestatic isDigit : (C)Z
    //   1850: ifeq -> 1926
    //   1853: aload_0
    //   1854: getfield cp : I
    //   1857: iconst_1
    //   1858: iadd
    //   1859: istore_3
    //   1860: aload_0
    //   1861: iload_3
    //   1862: putfield cp : I
    //   1865: iload #6
    //   1867: istore #7
    //   1869: iload #9
    //   1871: istore_2
    //   1872: iload_3
    //   1873: aload_1
    //   1874: arraylength
    //   1875: if_icmpge -> 1926
    //   1878: iload #6
    //   1880: aload_0
    //   1881: ldc 65535
    //   1883: ldc_w 'msg.overlarge.max'
    //   1886: invokestatic getDecimalValue : (CLcom/trendmicro/hippo/regexp/CompilerState;ILjava/lang/String;)I
    //   1889: istore_3
    //   1890: aload_1
    //   1891: aload_0
    //   1892: getfield cp : I
    //   1895: caload
    //   1896: istore #7
    //   1898: iload_3
    //   1899: istore_2
    //   1900: iload #11
    //   1902: iload_3
    //   1903: if_icmple -> 1926
    //   1906: ldc_w 'msg.max.lt.min'
    //   1909: aload_1
    //   1910: aload_0
    //   1911: getfield cp : I
    //   1914: caload
    //   1915: invokestatic valueOf : (C)Ljava/lang/String;
    //   1918: invokestatic reportError : (Ljava/lang/String;Ljava/lang/String;)V
    //   1921: iconst_0
    //   1922: ireturn
    //   1923: iload #11
    //   1925: istore_2
    //   1926: iload #8
    //   1928: istore_3
    //   1929: iload #7
    //   1931: bipush #125
    //   1933: if_icmpne -> 1979
    //   1936: aload_0
    //   1937: new com/trendmicro/hippo/regexp/RENode
    //   1940: dup
    //   1941: bipush #25
    //   1943: invokespecial <init> : (B)V
    //   1946: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1949: aload_0
    //   1950: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1953: iload #11
    //   1955: putfield min : I
    //   1958: aload_0
    //   1959: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   1962: iload_2
    //   1963: putfield max : I
    //   1966: aload_0
    //   1967: aload_0
    //   1968: getfield progLength : I
    //   1971: bipush #12
    //   1973: iadd
    //   1974: putfield progLength : I
    //   1977: iconst_1
    //   1978: istore_3
    //   1979: iload_3
    //   1980: istore_2
    //   1981: iload_3
    //   1982: ifne -> 2128
    //   1985: aload_0
    //   1986: iload #10
    //   1988: putfield cp : I
    //   1991: iload_3
    //   1992: istore_2
    //   1993: goto -> 2128
    //   1996: aload_0
    //   1997: new com/trendmicro/hippo/regexp/RENode
    //   2000: dup
    //   2001: bipush #25
    //   2003: invokespecial <init> : (B)V
    //   2006: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2009: aload_0
    //   2010: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2013: iconst_0
    //   2014: putfield min : I
    //   2017: aload_0
    //   2018: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2021: iconst_1
    //   2022: putfield max : I
    //   2025: aload_0
    //   2026: aload_0
    //   2027: getfield progLength : I
    //   2030: bipush #8
    //   2032: iadd
    //   2033: putfield progLength : I
    //   2036: iconst_1
    //   2037: istore_2
    //   2038: goto -> 2128
    //   2041: aload_0
    //   2042: new com/trendmicro/hippo/regexp/RENode
    //   2045: dup
    //   2046: bipush #25
    //   2048: invokespecial <init> : (B)V
    //   2051: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2054: aload_0
    //   2055: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2058: iconst_1
    //   2059: putfield min : I
    //   2062: aload_0
    //   2063: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2066: iconst_m1
    //   2067: putfield max : I
    //   2070: aload_0
    //   2071: aload_0
    //   2072: getfield progLength : I
    //   2075: bipush #8
    //   2077: iadd
    //   2078: putfield progLength : I
    //   2081: iconst_1
    //   2082: istore_2
    //   2083: goto -> 2128
    //   2086: aload_0
    //   2087: new com/trendmicro/hippo/regexp/RENode
    //   2090: dup
    //   2091: bipush #25
    //   2093: invokespecial <init> : (B)V
    //   2096: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2099: aload_0
    //   2100: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2103: iconst_0
    //   2104: putfield min : I
    //   2107: aload_0
    //   2108: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2111: iconst_m1
    //   2112: putfield max : I
    //   2115: aload_0
    //   2116: aload_0
    //   2117: getfield progLength : I
    //   2120: bipush #8
    //   2122: iadd
    //   2123: putfield progLength : I
    //   2126: iconst_1
    //   2127: istore_2
    //   2128: iload_2
    //   2129: ifne -> 2134
    //   2132: iconst_1
    //   2133: ireturn
    //   2134: aload_0
    //   2135: aload_0
    //   2136: getfield cp : I
    //   2139: iconst_1
    //   2140: iadd
    //   2141: putfield cp : I
    //   2144: aload_0
    //   2145: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2148: aload #5
    //   2150: putfield kid : Lcom/trendmicro/hippo/regexp/RENode;
    //   2153: aload_0
    //   2154: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2157: iload #4
    //   2159: putfield parenIndex : I
    //   2162: aload_0
    //   2163: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2166: aload_0
    //   2167: getfield parenCount : I
    //   2170: iload #4
    //   2172: isub
    //   2173: putfield parenCount : I
    //   2176: aload_0
    //   2177: getfield cp : I
    //   2180: aload_0
    //   2181: getfield cpend : I
    //   2184: if_icmpge -> 2219
    //   2187: aload_1
    //   2188: aload_0
    //   2189: getfield cp : I
    //   2192: caload
    //   2193: bipush #63
    //   2195: if_icmpne -> 2219
    //   2198: aload_0
    //   2199: aload_0
    //   2200: getfield cp : I
    //   2203: iconst_1
    //   2204: iadd
    //   2205: putfield cp : I
    //   2208: aload_0
    //   2209: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2212: iconst_0
    //   2213: putfield greedy : Z
    //   2216: goto -> 2227
    //   2219: aload_0
    //   2220: getfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2223: iconst_1
    //   2224: putfield greedy : Z
    //   2227: iconst_1
    //   2228: ireturn
    //   2229: aload_0
    //   2230: new com/trendmicro/hippo/regexp/RENode
    //   2233: dup
    //   2234: iconst_3
    //   2235: invokespecial <init> : (B)V
    //   2238: putfield result : Lcom/trendmicro/hippo/regexp/RENode;
    //   2241: aload_0
    //   2242: aload_0
    //   2243: getfield progLength : I
    //   2246: iconst_1
    //   2247: iadd
    //   2248: putfield progLength : I
    //   2251: iconst_1
    //   2252: ireturn
  }
  
  private static REProgState popProgState(REGlobalData paramREGlobalData) {
    REProgState rEProgState = paramREGlobalData.stateStackTop;
    paramREGlobalData.stateStackTop = rEProgState.previous;
    return rEProgState;
  }
  
  private static void processCharSet(REGlobalData paramREGlobalData, RECharSet paramRECharSet) {
    // Byte code:
    //   0: aload_1
    //   1: monitorenter
    //   2: aload_1
    //   3: getfield converted : Z
    //   6: ifne -> 19
    //   9: aload_0
    //   10: aload_1
    //   11: invokestatic processCharSetImpl : (Lcom/trendmicro/hippo/regexp/REGlobalData;Lcom/trendmicro/hippo/regexp/RECharSet;)V
    //   14: aload_1
    //   15: iconst_1
    //   16: putfield converted : Z
    //   19: aload_1
    //   20: monitorexit
    //   21: return
    //   22: astore_0
    //   23: aload_1
    //   24: monitorexit
    //   25: aload_0
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
    //   19	21	22	finally
    //   23	25	22	finally
  }
  
  private static void processCharSetImpl(REGlobalData paramREGlobalData, RECharSet paramRECharSet) {
    // Byte code:
    //   0: aload_1
    //   1: getfield startIndex : I
    //   4: istore_2
    //   5: aload_1
    //   6: getfield strlength : I
    //   9: iload_2
    //   10: iadd
    //   11: istore_3
    //   12: iconst_0
    //   13: istore #4
    //   15: iconst_0
    //   16: istore #5
    //   18: aload_1
    //   19: aload_1
    //   20: getfield length : I
    //   23: bipush #7
    //   25: iadd
    //   26: bipush #8
    //   28: idiv
    //   29: newarray byte
    //   31: putfield bits : [B
    //   34: iload_2
    //   35: iload_3
    //   36: if_icmpne -> 40
    //   39: return
    //   40: aload_0
    //   41: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   44: getfield source : [C
    //   47: iload_2
    //   48: caload
    //   49: bipush #94
    //   51: if_icmpne -> 64
    //   54: iinc #2, 1
    //   57: iload #4
    //   59: istore #6
    //   61: goto -> 68
    //   64: iload #4
    //   66: istore #6
    //   68: iload_2
    //   69: iload_3
    //   70: if_icmpeq -> 1080
    //   73: iconst_2
    //   74: istore #7
    //   76: aload_0
    //   77: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   80: getfield source : [C
    //   83: iload_2
    //   84: caload
    //   85: bipush #92
    //   87: if_icmpeq -> 118
    //   90: aload_0
    //   91: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   94: getfield source : [C
    //   97: astore #8
    //   99: iload_2
    //   100: iconst_1
    //   101: iadd
    //   102: istore #4
    //   104: aload #8
    //   106: iload_2
    //   107: caload
    //   108: istore_2
    //   109: iload_2
    //   110: istore #9
    //   112: iload #4
    //   114: istore_2
    //   115: goto -> 784
    //   118: iinc #2, 1
    //   121: aload_0
    //   122: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   125: getfield source : [C
    //   128: astore #8
    //   130: iload_2
    //   131: iconst_1
    //   132: iadd
    //   133: istore #4
    //   135: aload #8
    //   137: iload_2
    //   138: caload
    //   139: istore_2
    //   140: iload_2
    //   141: bipush #68
    //   143: if_icmpeq -> 1050
    //   146: iload_2
    //   147: bipush #83
    //   149: if_icmpeq -> 1016
    //   152: iload_2
    //   153: bipush #87
    //   155: if_icmpeq -> 981
    //   158: iload_2
    //   159: bipush #102
    //   161: if_icmpeq -> 773
    //   164: iload_2
    //   165: bipush #110
    //   167: if_icmpeq -> 761
    //   170: iload_2
    //   171: tableswitch default -> 216, 48 -> 608, 49 -> 608, 50 -> 608, 51 -> 608, 52 -> 608, 53 -> 608, 54 -> 608, 55 -> 608
    //   216: iload_2
    //   217: tableswitch default -> 244, 98 -> 596, 99 -> 530, 100 -> 515
    //   244: iload_2
    //   245: tableswitch default -> 288, 114 -> 503, 115 -> 469, 116 -> 457, 117 -> 344, 118 -> 332, 119 -> 297, 120 -> 349
    //   288: iload_2
    //   289: istore #9
    //   291: iload #4
    //   293: istore_2
    //   294: goto -> 784
    //   297: aload_1
    //   298: getfield length : I
    //   301: iconst_1
    //   302: isub
    //   303: istore_2
    //   304: iload #4
    //   306: istore #10
    //   308: iload_2
    //   309: iflt -> 1074
    //   312: iload_2
    //   313: i2c
    //   314: invokestatic isWord : (C)Z
    //   317: ifeq -> 326
    //   320: aload_1
    //   321: iload_2
    //   322: i2c
    //   323: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   326: iinc #2, -1
    //   329: goto -> 304
    //   332: bipush #11
    //   334: istore_2
    //   335: iload_2
    //   336: istore #9
    //   338: iload #4
    //   340: istore_2
    //   341: goto -> 784
    //   344: iconst_2
    //   345: iconst_2
    //   346: iadd
    //   347: istore #7
    //   349: iconst_0
    //   350: istore_2
    //   351: iconst_0
    //   352: istore #11
    //   354: iload_2
    //   355: istore #12
    //   357: iload #4
    //   359: istore #10
    //   361: iload #11
    //   363: iload #7
    //   365: if_icmpge -> 444
    //   368: iload_2
    //   369: istore #12
    //   371: iload #4
    //   373: istore #10
    //   375: iload #4
    //   377: iload_3
    //   378: if_icmpge -> 444
    //   381: aload_0
    //   382: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   385: getfield source : [C
    //   388: astore #8
    //   390: iload #4
    //   392: iconst_1
    //   393: iadd
    //   394: istore #10
    //   396: aload #8
    //   398: iload #4
    //   400: caload
    //   401: invokestatic toASCIIHexDigit : (I)I
    //   404: istore #4
    //   406: iload #4
    //   408: ifge -> 427
    //   411: bipush #92
    //   413: istore #12
    //   415: iload #10
    //   417: iload #11
    //   419: iconst_1
    //   420: iadd
    //   421: isub
    //   422: istore #10
    //   424: goto -> 444
    //   427: iload_2
    //   428: iconst_4
    //   429: ishl
    //   430: iload #4
    //   432: ior
    //   433: istore_2
    //   434: iinc #11, 1
    //   437: iload #10
    //   439: istore #4
    //   441: goto -> 354
    //   444: iload #12
    //   446: i2c
    //   447: istore_2
    //   448: iload_2
    //   449: istore #9
    //   451: iload #10
    //   453: istore_2
    //   454: goto -> 784
    //   457: bipush #9
    //   459: istore_2
    //   460: iload_2
    //   461: istore #9
    //   463: iload #4
    //   465: istore_2
    //   466: goto -> 784
    //   469: aload_1
    //   470: getfield length : I
    //   473: iconst_1
    //   474: isub
    //   475: istore_2
    //   476: iload #4
    //   478: istore #10
    //   480: iload_2
    //   481: iflt -> 1074
    //   484: iload_2
    //   485: invokestatic isREWhiteSpace : (I)Z
    //   488: ifeq -> 497
    //   491: aload_1
    //   492: iload_2
    //   493: i2c
    //   494: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   497: iinc #2, -1
    //   500: goto -> 476
    //   503: bipush #13
    //   505: istore_2
    //   506: iload_2
    //   507: istore #9
    //   509: iload #4
    //   511: istore_2
    //   512: goto -> 784
    //   515: aload_1
    //   516: bipush #48
    //   518: bipush #57
    //   520: invokestatic addCharacterRangeToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;CC)V
    //   523: iload #4
    //   525: istore #10
    //   527: goto -> 1074
    //   530: iload #4
    //   532: iload_3
    //   533: if_icmpge -> 580
    //   536: aload_0
    //   537: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   540: getfield source : [C
    //   543: iload #4
    //   545: caload
    //   546: invokestatic isControlLetter : (C)Z
    //   549: ifeq -> 580
    //   552: aload_0
    //   553: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   556: getfield source : [C
    //   559: iload #4
    //   561: caload
    //   562: bipush #31
    //   564: iand
    //   565: i2c
    //   566: istore #10
    //   568: iload #4
    //   570: iconst_1
    //   571: iadd
    //   572: istore_2
    //   573: iload #10
    //   575: istore #9
    //   577: goto -> 784
    //   580: iload #4
    //   582: iconst_1
    //   583: isub
    //   584: istore_2
    //   585: bipush #92
    //   587: istore #4
    //   589: iload #4
    //   591: istore #9
    //   593: goto -> 784
    //   596: bipush #8
    //   598: istore_2
    //   599: iload_2
    //   600: istore #9
    //   602: iload #4
    //   604: istore_2
    //   605: goto -> 784
    //   608: iload_2
    //   609: bipush #48
    //   611: isub
    //   612: istore #11
    //   614: aload_0
    //   615: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   618: getfield source : [C
    //   621: iload #4
    //   623: caload
    //   624: istore #12
    //   626: iload #4
    //   628: istore_2
    //   629: iload #11
    //   631: istore #10
    //   633: bipush #48
    //   635: iload #12
    //   637: if_icmpgt -> 749
    //   640: iload #4
    //   642: istore_2
    //   643: iload #11
    //   645: istore #10
    //   647: iload #12
    //   649: bipush #55
    //   651: if_icmpgt -> 749
    //   654: iload #4
    //   656: iconst_1
    //   657: iadd
    //   658: istore #7
    //   660: iload #11
    //   662: bipush #8
    //   664: imul
    //   665: iload #12
    //   667: bipush #48
    //   669: isub
    //   670: iadd
    //   671: istore #4
    //   673: aload_0
    //   674: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   677: getfield source : [C
    //   680: iload #7
    //   682: caload
    //   683: istore #11
    //   685: iload #7
    //   687: istore_2
    //   688: iload #4
    //   690: istore #10
    //   692: bipush #48
    //   694: iload #11
    //   696: if_icmpgt -> 749
    //   699: iload #7
    //   701: istore_2
    //   702: iload #4
    //   704: istore #10
    //   706: iload #11
    //   708: bipush #55
    //   710: if_icmpgt -> 749
    //   713: iload #7
    //   715: iconst_1
    //   716: iadd
    //   717: istore_2
    //   718: iload #4
    //   720: bipush #8
    //   722: imul
    //   723: iload #11
    //   725: bipush #48
    //   727: isub
    //   728: iadd
    //   729: istore #10
    //   731: iload #10
    //   733: sipush #255
    //   736: if_icmpgt -> 742
    //   739: goto -> 749
    //   742: iinc #2, -1
    //   745: iload #4
    //   747: istore #10
    //   749: iload #10
    //   751: i2c
    //   752: istore #4
    //   754: iload #4
    //   756: istore #9
    //   758: goto -> 784
    //   761: bipush #10
    //   763: istore_2
    //   764: iload_2
    //   765: istore #9
    //   767: iload #4
    //   769: istore_2
    //   770: goto -> 784
    //   773: bipush #12
    //   775: istore #10
    //   777: iload #4
    //   779: istore_2
    //   780: iload #10
    //   782: istore #9
    //   784: iload #5
    //   786: ifeq -> 898
    //   789: aload_0
    //   790: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   793: getfield flags : I
    //   796: iconst_2
    //   797: iand
    //   798: ifeq -> 884
    //   801: iload #6
    //   803: istore #4
    //   805: iload #4
    //   807: istore #13
    //   809: iload #13
    //   811: iload #9
    //   813: if_icmpgt -> 892
    //   816: aload_1
    //   817: iload #13
    //   819: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   822: iload #13
    //   824: invokestatic upcase : (C)C
    //   827: istore #14
    //   829: iload #13
    //   831: invokestatic downcase : (C)C
    //   834: istore #15
    //   836: iload #13
    //   838: iload #14
    //   840: if_icmpeq -> 849
    //   843: aload_1
    //   844: iload #14
    //   846: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   849: iload #13
    //   851: iload #15
    //   853: if_icmpeq -> 862
    //   856: aload_1
    //   857: iload #15
    //   859: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   862: iload #13
    //   864: iconst_1
    //   865: iadd
    //   866: i2c
    //   867: istore #4
    //   869: iload #4
    //   871: ifne -> 877
    //   874: goto -> 892
    //   877: iload #4
    //   879: istore #13
    //   881: goto -> 809
    //   884: aload_1
    //   885: iload #6
    //   887: iload #9
    //   889: invokestatic addCharacterRangeToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;CC)V
    //   892: iconst_0
    //   893: istore #5
    //   895: goto -> 68
    //   898: aload_0
    //   899: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   902: getfield flags : I
    //   905: iconst_2
    //   906: iand
    //   907: ifeq -> 931
    //   910: aload_1
    //   911: iload #9
    //   913: invokestatic upcase : (C)C
    //   916: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   919: aload_1
    //   920: iload #9
    //   922: invokestatic downcase : (C)C
    //   925: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   928: goto -> 937
    //   931: aload_1
    //   932: iload #9
    //   934: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   937: iload_2
    //   938: istore #10
    //   940: iload_2
    //   941: iload_3
    //   942: iconst_1
    //   943: isub
    //   944: if_icmpge -> 1074
    //   947: iload_2
    //   948: istore #10
    //   950: aload_0
    //   951: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   954: getfield source : [C
    //   957: iload_2
    //   958: caload
    //   959: bipush #45
    //   961: if_icmpne -> 1074
    //   964: iconst_1
    //   965: istore #5
    //   967: iload #9
    //   969: istore #4
    //   971: iinc #2, 1
    //   974: iload #4
    //   976: istore #6
    //   978: goto -> 68
    //   981: aload_1
    //   982: getfield length : I
    //   985: iconst_1
    //   986: isub
    //   987: istore_2
    //   988: iload #4
    //   990: istore #10
    //   992: iload_2
    //   993: iflt -> 1074
    //   996: iload_2
    //   997: i2c
    //   998: invokestatic isWord : (C)Z
    //   1001: ifne -> 1010
    //   1004: aload_1
    //   1005: iload_2
    //   1006: i2c
    //   1007: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   1010: iinc #2, -1
    //   1013: goto -> 988
    //   1016: aload_1
    //   1017: getfield length : I
    //   1020: iconst_1
    //   1021: isub
    //   1022: istore_2
    //   1023: iload #4
    //   1025: istore #10
    //   1027: iload_2
    //   1028: iflt -> 1074
    //   1031: iload_2
    //   1032: invokestatic isREWhiteSpace : (I)Z
    //   1035: ifne -> 1044
    //   1038: aload_1
    //   1039: iload_2
    //   1040: i2c
    //   1041: invokestatic addCharacterToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;C)V
    //   1044: iinc #2, -1
    //   1047: goto -> 1023
    //   1050: aload_1
    //   1051: iconst_0
    //   1052: bipush #47
    //   1054: invokestatic addCharacterRangeToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;CC)V
    //   1057: aload_1
    //   1058: bipush #58
    //   1060: aload_1
    //   1061: getfield length : I
    //   1064: iconst_1
    //   1065: isub
    //   1066: i2c
    //   1067: invokestatic addCharacterRangeToCharSet : (Lcom/trendmicro/hippo/regexp/RECharSet;CC)V
    //   1070: iload #4
    //   1072: istore #10
    //   1074: iload #10
    //   1076: istore_2
    //   1077: goto -> 68
    //   1080: return
  }
  
  private static void pushBackTrackState(REGlobalData paramREGlobalData, byte paramByte, int paramInt) {
    REProgState rEProgState = paramREGlobalData.stateStackTop;
    paramREGlobalData.backTrackStackTop = new REBackTrackData(paramREGlobalData, paramByte, paramInt, paramREGlobalData.cp, rEProgState.continuationOp, rEProgState.continuationPc);
  }
  
  private static void pushBackTrackState(REGlobalData paramREGlobalData, byte paramByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramREGlobalData.backTrackStackTop = new REBackTrackData(paramREGlobalData, paramByte, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  private static void pushProgState(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, int paramInt3, REBackTrackData paramREBackTrackData, int paramInt4, int paramInt5) {
    paramREGlobalData.stateStackTop = new REProgState(paramREGlobalData.stateStackTop, paramInt1, paramInt2, paramInt3, paramREBackTrackData, paramInt4, paramInt5);
  }
  
  private static NativeRegExp realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeRegExp)
      return (NativeRegExp)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  private static boolean reopIsSimple(int paramInt) {
    boolean bool = true;
    if (paramInt < 1 || paramInt > 23)
      bool = false; 
    return bool;
  }
  
  private static void reportError(String paramString1, String paramString2) {
    throw ScriptRuntime.constructError("SyntaxError", ScriptRuntime.getMessage1(paramString1, paramString2));
  }
  
  private static void reportWarning(Context paramContext, String paramString1, String paramString2) {
    if (paramContext.hasFeature(11))
      Context.reportWarning(ScriptRuntime.getMessage1(paramString1, paramString2)); 
  }
  
  private static void resolveForwardJump(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      addIndex(paramArrayOfbyte, paramInt1, paramInt2 - paramInt1);
      return;
    } 
    throw Kit.codeBug();
  }
  
  private static int simpleMatch(REGlobalData paramREGlobalData, String paramString, int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #7
    //   3: iconst_0
    //   4: istore #8
    //   6: iconst_0
    //   7: istore #9
    //   9: aload_0
    //   10: getfield cp : I
    //   13: istore #10
    //   15: iconst_0
    //   16: istore #11
    //   18: iconst_0
    //   19: istore #12
    //   21: iload_2
    //   22: tableswitch default -> 128, 1 -> 1259, 2 -> 1203, 3 -> 1147, 4 -> 1072, 5 -> 994, 6 -> 938, 7 -> 882, 8 -> 826, 9 -> 770, 10 -> 714, 11 -> 658, 12 -> 602, 13 -> 575, 14 -> 536, 15 -> 475, 16 -> 436, 17 -> 358, 18 -> 291, 19 -> 210, 20 -> 128, 21 -> 128, 22 -> 132, 23 -> 132
    //   128: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   131: athrow
    //   132: aload_3
    //   133: iload #4
    //   135: invokestatic getIndex : ([BI)I
    //   138: istore #12
    //   140: iinc #4, 2
    //   143: iload #8
    //   145: istore #7
    //   147: iload #4
    //   149: istore_2
    //   150: aload_0
    //   151: getfield cp : I
    //   154: iload #5
    //   156: if_icmpeq -> 1265
    //   159: iload #8
    //   161: istore #7
    //   163: iload #4
    //   165: istore_2
    //   166: aload_0
    //   167: aload_0
    //   168: getfield regexp : Lcom/trendmicro/hippo/regexp/RECompiled;
    //   171: getfield classList : [Lcom/trendmicro/hippo/regexp/RECharSet;
    //   174: iload #12
    //   176: aaload
    //   177: aload_1
    //   178: aload_0
    //   179: getfield cp : I
    //   182: invokevirtual charAt : (I)C
    //   185: invokestatic classMatcher : (Lcom/trendmicro/hippo/regexp/REGlobalData;Lcom/trendmicro/hippo/regexp/RECharSet;C)Z
    //   188: ifeq -> 1265
    //   191: aload_0
    //   192: aload_0
    //   193: getfield cp : I
    //   196: iconst_1
    //   197: iadd
    //   198: putfield cp : I
    //   201: iconst_1
    //   202: istore #7
    //   204: iload #4
    //   206: istore_2
    //   207: goto -> 1265
    //   210: aload_3
    //   211: iload #4
    //   213: invokestatic getIndex : ([BI)I
    //   216: i2c
    //   217: istore #13
    //   219: iinc #4, 2
    //   222: iload #8
    //   224: istore #7
    //   226: iload #4
    //   228: istore_2
    //   229: aload_0
    //   230: getfield cp : I
    //   233: iload #5
    //   235: if_icmpeq -> 1265
    //   238: aload_1
    //   239: aload_0
    //   240: getfield cp : I
    //   243: invokevirtual charAt : (I)C
    //   246: istore #14
    //   248: iload #13
    //   250: iload #14
    //   252: if_icmpeq -> 272
    //   255: iload #9
    //   257: istore #7
    //   259: iload #13
    //   261: invokestatic upcase : (C)C
    //   264: iload #14
    //   266: invokestatic upcase : (C)C
    //   269: if_icmpne -> 285
    //   272: iconst_1
    //   273: istore #7
    //   275: aload_0
    //   276: aload_0
    //   277: getfield cp : I
    //   280: iconst_1
    //   281: iadd
    //   282: putfield cp : I
    //   285: iload #4
    //   287: istore_2
    //   288: goto -> 1265
    //   291: aload_3
    //   292: iload #4
    //   294: invokestatic getIndex : ([BI)I
    //   297: i2c
    //   298: istore #12
    //   300: iinc #4, 2
    //   303: iload #8
    //   305: istore #7
    //   307: iload #4
    //   309: istore_2
    //   310: aload_0
    //   311: getfield cp : I
    //   314: iload #5
    //   316: if_icmpeq -> 1265
    //   319: iload #8
    //   321: istore #7
    //   323: iload #4
    //   325: istore_2
    //   326: aload_1
    //   327: aload_0
    //   328: getfield cp : I
    //   331: invokevirtual charAt : (I)C
    //   334: iload #12
    //   336: if_icmpne -> 1265
    //   339: iconst_1
    //   340: istore #7
    //   342: aload_0
    //   343: aload_0
    //   344: getfield cp : I
    //   347: iconst_1
    //   348: iadd
    //   349: putfield cp : I
    //   352: iload #4
    //   354: istore_2
    //   355: goto -> 1265
    //   358: iload #4
    //   360: iconst_1
    //   361: iadd
    //   362: istore_2
    //   363: aload_3
    //   364: iload #4
    //   366: baload
    //   367: sipush #255
    //   370: iand
    //   371: i2c
    //   372: istore #13
    //   374: aload_0
    //   375: getfield cp : I
    //   378: iload #5
    //   380: if_icmpeq -> 429
    //   383: aload_1
    //   384: aload_0
    //   385: getfield cp : I
    //   388: invokevirtual charAt : (I)C
    //   391: istore #14
    //   393: iload #13
    //   395: iload #14
    //   397: if_icmpeq -> 413
    //   400: iload #13
    //   402: invokestatic upcase : (C)C
    //   405: iload #14
    //   407: invokestatic upcase : (C)C
    //   410: if_icmpne -> 426
    //   413: iconst_1
    //   414: istore #7
    //   416: aload_0
    //   417: aload_0
    //   418: getfield cp : I
    //   421: iconst_1
    //   422: iadd
    //   423: putfield cp : I
    //   426: goto -> 1265
    //   429: iload #8
    //   431: istore #7
    //   433: goto -> 1265
    //   436: aload_3
    //   437: iload #4
    //   439: invokestatic getIndex : ([BI)I
    //   442: istore #12
    //   444: iload #4
    //   446: iconst_2
    //   447: iadd
    //   448: istore_2
    //   449: aload_3
    //   450: iload_2
    //   451: invokestatic getIndex : ([BI)I
    //   454: istore #4
    //   456: iinc #2, 2
    //   459: aload_0
    //   460: iload #12
    //   462: iload #4
    //   464: aload_1
    //   465: iload #5
    //   467: invokestatic flatNIMatcher : (Lcom/trendmicro/hippo/regexp/REGlobalData;IILjava/lang/String;I)Z
    //   470: istore #7
    //   472: goto -> 1265
    //   475: iload #4
    //   477: iconst_1
    //   478: iadd
    //   479: istore_2
    //   480: aload_3
    //   481: iload #4
    //   483: baload
    //   484: sipush #255
    //   487: iand
    //   488: i2c
    //   489: istore #4
    //   491: aload_0
    //   492: getfield cp : I
    //   495: iload #5
    //   497: if_icmpeq -> 529
    //   500: aload_1
    //   501: aload_0
    //   502: getfield cp : I
    //   505: invokevirtual charAt : (I)C
    //   508: iload #4
    //   510: if_icmpne -> 529
    //   513: iconst_1
    //   514: istore #7
    //   516: aload_0
    //   517: aload_0
    //   518: getfield cp : I
    //   521: iconst_1
    //   522: iadd
    //   523: putfield cp : I
    //   526: goto -> 1265
    //   529: iload #8
    //   531: istore #7
    //   533: goto -> 1265
    //   536: aload_3
    //   537: iload #4
    //   539: invokestatic getIndex : ([BI)I
    //   542: istore #12
    //   544: iload #4
    //   546: iconst_2
    //   547: iadd
    //   548: istore_2
    //   549: aload_3
    //   550: iload_2
    //   551: invokestatic getIndex : ([BI)I
    //   554: istore #4
    //   556: iinc #2, 2
    //   559: aload_0
    //   560: iload #12
    //   562: iload #4
    //   564: aload_1
    //   565: iload #5
    //   567: invokestatic flatNMatcher : (Lcom/trendmicro/hippo/regexp/REGlobalData;IILjava/lang/String;I)Z
    //   570: istore #7
    //   572: goto -> 1265
    //   575: aload_3
    //   576: iload #4
    //   578: invokestatic getIndex : ([BI)I
    //   581: istore #12
    //   583: iload #4
    //   585: iconst_2
    //   586: iadd
    //   587: istore_2
    //   588: aload_0
    //   589: iload #12
    //   591: aload_1
    //   592: iload #5
    //   594: invokestatic backrefMatcher : (Lcom/trendmicro/hippo/regexp/REGlobalData;ILjava/lang/String;I)Z
    //   597: istore #7
    //   599: goto -> 1265
    //   602: iload #8
    //   604: istore #7
    //   606: iload #4
    //   608: istore_2
    //   609: aload_0
    //   610: getfield cp : I
    //   613: iload #5
    //   615: if_icmpeq -> 1265
    //   618: iload #8
    //   620: istore #7
    //   622: iload #4
    //   624: istore_2
    //   625: aload_1
    //   626: aload_0
    //   627: getfield cp : I
    //   630: invokevirtual charAt : (I)C
    //   633: invokestatic isREWhiteSpace : (I)Z
    //   636: ifne -> 1265
    //   639: iconst_1
    //   640: istore #7
    //   642: aload_0
    //   643: aload_0
    //   644: getfield cp : I
    //   647: iconst_1
    //   648: iadd
    //   649: putfield cp : I
    //   652: iload #4
    //   654: istore_2
    //   655: goto -> 1265
    //   658: iload #8
    //   660: istore #7
    //   662: iload #4
    //   664: istore_2
    //   665: aload_0
    //   666: getfield cp : I
    //   669: iload #5
    //   671: if_icmpeq -> 1265
    //   674: iload #8
    //   676: istore #7
    //   678: iload #4
    //   680: istore_2
    //   681: aload_1
    //   682: aload_0
    //   683: getfield cp : I
    //   686: invokevirtual charAt : (I)C
    //   689: invokestatic isREWhiteSpace : (I)Z
    //   692: ifeq -> 1265
    //   695: iconst_1
    //   696: istore #7
    //   698: aload_0
    //   699: aload_0
    //   700: getfield cp : I
    //   703: iconst_1
    //   704: iadd
    //   705: putfield cp : I
    //   708: iload #4
    //   710: istore_2
    //   711: goto -> 1265
    //   714: iload #8
    //   716: istore #7
    //   718: iload #4
    //   720: istore_2
    //   721: aload_0
    //   722: getfield cp : I
    //   725: iload #5
    //   727: if_icmpeq -> 1265
    //   730: iload #8
    //   732: istore #7
    //   734: iload #4
    //   736: istore_2
    //   737: aload_1
    //   738: aload_0
    //   739: getfield cp : I
    //   742: invokevirtual charAt : (I)C
    //   745: invokestatic isWord : (C)Z
    //   748: ifne -> 1265
    //   751: iconst_1
    //   752: istore #7
    //   754: aload_0
    //   755: aload_0
    //   756: getfield cp : I
    //   759: iconst_1
    //   760: iadd
    //   761: putfield cp : I
    //   764: iload #4
    //   766: istore_2
    //   767: goto -> 1265
    //   770: iload #8
    //   772: istore #7
    //   774: iload #4
    //   776: istore_2
    //   777: aload_0
    //   778: getfield cp : I
    //   781: iload #5
    //   783: if_icmpeq -> 1265
    //   786: iload #8
    //   788: istore #7
    //   790: iload #4
    //   792: istore_2
    //   793: aload_1
    //   794: aload_0
    //   795: getfield cp : I
    //   798: invokevirtual charAt : (I)C
    //   801: invokestatic isWord : (C)Z
    //   804: ifeq -> 1265
    //   807: iconst_1
    //   808: istore #7
    //   810: aload_0
    //   811: aload_0
    //   812: getfield cp : I
    //   815: iconst_1
    //   816: iadd
    //   817: putfield cp : I
    //   820: iload #4
    //   822: istore_2
    //   823: goto -> 1265
    //   826: iload #8
    //   828: istore #7
    //   830: iload #4
    //   832: istore_2
    //   833: aload_0
    //   834: getfield cp : I
    //   837: iload #5
    //   839: if_icmpeq -> 1265
    //   842: iload #8
    //   844: istore #7
    //   846: iload #4
    //   848: istore_2
    //   849: aload_1
    //   850: aload_0
    //   851: getfield cp : I
    //   854: invokevirtual charAt : (I)C
    //   857: invokestatic isDigit : (C)Z
    //   860: ifne -> 1265
    //   863: iconst_1
    //   864: istore #7
    //   866: aload_0
    //   867: aload_0
    //   868: getfield cp : I
    //   871: iconst_1
    //   872: iadd
    //   873: putfield cp : I
    //   876: iload #4
    //   878: istore_2
    //   879: goto -> 1265
    //   882: iload #8
    //   884: istore #7
    //   886: iload #4
    //   888: istore_2
    //   889: aload_0
    //   890: getfield cp : I
    //   893: iload #5
    //   895: if_icmpeq -> 1265
    //   898: iload #8
    //   900: istore #7
    //   902: iload #4
    //   904: istore_2
    //   905: aload_1
    //   906: aload_0
    //   907: getfield cp : I
    //   910: invokevirtual charAt : (I)C
    //   913: invokestatic isDigit : (C)Z
    //   916: ifeq -> 1265
    //   919: iconst_1
    //   920: istore #7
    //   922: aload_0
    //   923: aload_0
    //   924: getfield cp : I
    //   927: iconst_1
    //   928: iadd
    //   929: putfield cp : I
    //   932: iload #4
    //   934: istore_2
    //   935: goto -> 1265
    //   938: iload #8
    //   940: istore #7
    //   942: iload #4
    //   944: istore_2
    //   945: aload_0
    //   946: getfield cp : I
    //   949: iload #5
    //   951: if_icmpeq -> 1265
    //   954: iload #8
    //   956: istore #7
    //   958: iload #4
    //   960: istore_2
    //   961: aload_1
    //   962: aload_0
    //   963: getfield cp : I
    //   966: invokevirtual charAt : (I)C
    //   969: invokestatic isLineTerm : (C)Z
    //   972: ifne -> 1265
    //   975: iconst_1
    //   976: istore #7
    //   978: aload_0
    //   979: aload_0
    //   980: getfield cp : I
    //   983: iconst_1
    //   984: iadd
    //   985: putfield cp : I
    //   988: iload #4
    //   990: istore_2
    //   991: goto -> 1265
    //   994: aload_0
    //   995: getfield cp : I
    //   998: ifeq -> 1025
    //   1001: aload_1
    //   1002: aload_0
    //   1003: getfield cp : I
    //   1006: iconst_1
    //   1007: isub
    //   1008: invokevirtual charAt : (I)C
    //   1011: invokestatic isWord : (C)Z
    //   1014: ifne -> 1020
    //   1017: goto -> 1025
    //   1020: iconst_0
    //   1021: istore_2
    //   1022: goto -> 1027
    //   1025: iconst_1
    //   1026: istore_2
    //   1027: aload_0
    //   1028: getfield cp : I
    //   1031: iload #5
    //   1033: if_icmpge -> 1056
    //   1036: aload_1
    //   1037: aload_0
    //   1038: getfield cp : I
    //   1041: invokevirtual charAt : (I)C
    //   1044: invokestatic isWord : (C)Z
    //   1047: ifeq -> 1056
    //   1050: iconst_1
    //   1051: istore #5
    //   1053: goto -> 1060
    //   1056: iload #12
    //   1058: istore #5
    //   1060: iload_2
    //   1061: iload #5
    //   1063: ixor
    //   1064: istore #7
    //   1066: iload #4
    //   1068: istore_2
    //   1069: goto -> 1265
    //   1072: aload_0
    //   1073: getfield cp : I
    //   1076: ifeq -> 1103
    //   1079: aload_1
    //   1080: aload_0
    //   1081: getfield cp : I
    //   1084: iconst_1
    //   1085: isub
    //   1086: invokevirtual charAt : (I)C
    //   1089: invokestatic isWord : (C)Z
    //   1092: ifne -> 1098
    //   1095: goto -> 1103
    //   1098: iconst_0
    //   1099: istore_2
    //   1100: goto -> 1105
    //   1103: iconst_1
    //   1104: istore_2
    //   1105: aload_0
    //   1106: getfield cp : I
    //   1109: iload #5
    //   1111: if_icmpge -> 1132
    //   1114: iload #11
    //   1116: istore #5
    //   1118: aload_1
    //   1119: aload_0
    //   1120: getfield cp : I
    //   1123: invokevirtual charAt : (I)C
    //   1126: invokestatic isWord : (C)Z
    //   1129: ifne -> 1135
    //   1132: iconst_1
    //   1133: istore #5
    //   1135: iload_2
    //   1136: iload #5
    //   1138: ixor
    //   1139: istore #7
    //   1141: iload #4
    //   1143: istore_2
    //   1144: goto -> 1265
    //   1147: aload_0
    //   1148: getfield cp : I
    //   1151: iload #5
    //   1153: if_icmpeq -> 1194
    //   1156: iload #8
    //   1158: istore #7
    //   1160: iload #4
    //   1162: istore_2
    //   1163: aload_0
    //   1164: getfield multiline : Z
    //   1167: ifeq -> 1265
    //   1170: aload_1
    //   1171: aload_0
    //   1172: getfield cp : I
    //   1175: invokevirtual charAt : (I)C
    //   1178: invokestatic isLineTerm : (C)Z
    //   1181: ifne -> 1194
    //   1184: iload #8
    //   1186: istore #7
    //   1188: iload #4
    //   1190: istore_2
    //   1191: goto -> 1265
    //   1194: iconst_1
    //   1195: istore #7
    //   1197: iload #4
    //   1199: istore_2
    //   1200: goto -> 1265
    //   1203: aload_0
    //   1204: getfield cp : I
    //   1207: ifeq -> 1250
    //   1210: iload #8
    //   1212: istore #7
    //   1214: iload #4
    //   1216: istore_2
    //   1217: aload_0
    //   1218: getfield multiline : Z
    //   1221: ifeq -> 1265
    //   1224: aload_1
    //   1225: aload_0
    //   1226: getfield cp : I
    //   1229: iconst_1
    //   1230: isub
    //   1231: invokevirtual charAt : (I)C
    //   1234: invokestatic isLineTerm : (C)Z
    //   1237: ifne -> 1250
    //   1240: iload #8
    //   1242: istore #7
    //   1244: iload #4
    //   1246: istore_2
    //   1247: goto -> 1265
    //   1250: iconst_1
    //   1251: istore #7
    //   1253: iload #4
    //   1255: istore_2
    //   1256: goto -> 1265
    //   1259: iconst_1
    //   1260: istore #7
    //   1262: iload #4
    //   1264: istore_2
    //   1265: iload #7
    //   1267: ifeq -> 1283
    //   1270: iload #6
    //   1272: ifne -> 1281
    //   1275: aload_0
    //   1276: iload #10
    //   1278: putfield cp : I
    //   1281: iload_2
    //   1282: ireturn
    //   1283: aload_0
    //   1284: iload #10
    //   1286: putfield cp : I
    //   1289: iconst_m1
    //   1290: ireturn
  }
  
  private static int toASCIIHexDigit(int paramInt) {
    if (paramInt < 48)
      return -1; 
    if (paramInt <= 57)
      return paramInt - 48; 
    paramInt |= 0x20;
    return (97 <= paramInt && paramInt <= 102) ? (paramInt - 97 + 10) : -1;
  }
  
  private static char upcase(char paramChar) {
    if (paramChar < '')
      return ('a' <= paramChar && paramChar <= 'z') ? (char)(paramChar - 32) : paramChar; 
    char c = Character.toUpperCase(paramChar);
    if (c < '') {
      c = paramChar;
      paramChar = c;
    } else {
      paramChar = c;
    } 
    return paramChar;
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return execSub(paramContext, paramScriptable1, paramArrayOfObject, 1);
  }
  
  Scriptable compile(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    NativeRegExp nativeRegExp;
    String str;
    if (paramArrayOfObject.length > 0 && paramArrayOfObject[0] instanceof NativeRegExp) {
      if (paramArrayOfObject.length <= 1 || paramArrayOfObject[1] == Undefined.instance) {
        nativeRegExp = (NativeRegExp)paramArrayOfObject[0];
        this.re = nativeRegExp.re;
        this.lastIndex = nativeRegExp.lastIndex;
        return (Scriptable)this;
      } 
      throw ScriptRuntime.typeError0("msg.bad.regexp.compile");
    } 
    if (paramArrayOfObject.length == 0 || paramArrayOfObject[0] instanceof Undefined) {
      str = "";
    } else {
      str = escapeRegExp(paramArrayOfObject[0]);
    } 
    if (paramArrayOfObject.length > 1 && paramArrayOfObject[1] != Undefined.instance) {
      String str1 = ScriptRuntime.toString(paramArrayOfObject[1]);
    } else {
      paramArrayOfObject = null;
    } 
    this.re = compileRE((Context)nativeRegExp, str, (String)paramArrayOfObject, false);
    this.lastIndex = Double.valueOf(0.0D);
    return (Scriptable)this;
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    return (Scriptable)execSub(paramContext, paramScriptable, paramArrayOfObject, 1);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    if (!paramIdFunctionObject.hasTag(REGEXP_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    switch (i) {
      default:
        throw new IllegalArgumentException(String.valueOf(i));
      case 6:
        return realThis(paramScriptable2, paramIdFunctionObject).execSub(paramContext, paramScriptable1, paramArrayOfObject, 2);
      case 5:
        object = realThis(paramScriptable2, paramIdFunctionObject).execSub(paramContext, paramScriptable1, paramArrayOfObject, 0);
        if (Boolean.TRUE.equals(object)) {
          object = Boolean.TRUE;
        } else {
          object = Boolean.FALSE;
        } 
        return object;
      case 4:
        return realThis(paramScriptable2, (IdFunctionObject)object).execSub(paramContext, paramScriptable1, paramArrayOfObject, 1);
      case 2:
      case 3:
        return realThis(paramScriptable2, (IdFunctionObject)object).toString();
      case 1:
        break;
    } 
    return realThis(paramScriptable2, (IdFunctionObject)object).compile(paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  Object executeRegExp(Context paramContext, Scriptable paramScriptable, RegExpImpl paramRegExpImpl, String paramString, int[] paramArrayOfint, int paramInt) {
    Boolean bool;
    Scriptable scriptable1;
    Scriptable scriptable2;
    NativeRegExp nativeRegExp = this;
    REGlobalData rEGlobalData = new REGlobalData();
    int i = paramArrayOfint[0];
    int j = paramString.length();
    if (i > j)
      i = j; 
    if (!matchRegExp(rEGlobalData, nativeRegExp.re, paramString, i, j, paramRegExpImpl.multiline))
      return (paramInt != 2) ? null : Undefined.instance; 
    int k = rEGlobalData.cp;
    paramArrayOfint[0] = k;
    int m = k - rEGlobalData.skipped + i;
    int n = k - m;
    if (paramInt == 0) {
      bool = Boolean.TRUE;
      paramArrayOfint = null;
    } else {
      scriptable1 = paramContext.newArray((Scriptable)bool, 0);
      scriptable2 = scriptable1;
      scriptable2.put(0, scriptable2, paramString.substring(n, n + m));
    } 
    if (nativeRegExp.re.parenCount == 0) {
      paramRegExpImpl.parens = null;
      paramRegExpImpl.lastParen = new SubString();
    } else {
      NativeRegExp nativeRegExp1 = null;
      paramRegExpImpl.parens = new SubString[nativeRegExp.re.parenCount];
      byte b = 0;
      for (nativeRegExp = nativeRegExp1; b < this.re.parenCount; nativeRegExp = nativeRegExp1) {
        int i1 = rEGlobalData.parensIndex(b);
        if (i1 != -1) {
          SubString subString = new SubString(paramString, i1, rEGlobalData.parensLength(b));
          paramRegExpImpl.parens[b] = subString;
          if (paramInt != 0)
            scriptable2.put(b + 1, scriptable2, subString.toString()); 
        } else {
          nativeRegExp1 = nativeRegExp;
          if (paramInt != 0) {
            scriptable2.put(b + 1, scriptable2, Undefined.instance);
            nativeRegExp1 = nativeRegExp;
          } 
        } 
        b++;
      } 
      paramRegExpImpl.lastParen = (SubString)nativeRegExp;
    } 
    if (paramInt != 0) {
      scriptable2.put("index", scriptable2, Integer.valueOf(rEGlobalData.skipped + i));
      scriptable2.put("input", scriptable2, paramString);
    } 
    if (paramRegExpImpl.lastMatch == null) {
      paramRegExpImpl.lastMatch = new SubString();
      paramRegExpImpl.leftContext = new SubString();
      paramRegExpImpl.rightContext = new SubString();
    } 
    paramRegExpImpl.lastMatch.str = paramString;
    paramRegExpImpl.lastMatch.index = n;
    paramRegExpImpl.lastMatch.length = m;
    paramRegExpImpl.leftContext.str = paramString;
    if (paramContext.getLanguageVersion() == 120) {
      paramRegExpImpl.leftContext.index = i;
      paramRegExpImpl.leftContext.length = rEGlobalData.skipped;
    } else {
      paramRegExpImpl.leftContext.index = 0;
      paramRegExpImpl.leftContext.length = rEGlobalData.skipped + i;
    } 
    paramRegExpImpl.rightContext.str = paramString;
    paramRegExpImpl.rightContext.index = k;
    paramRegExpImpl.rightContext.length = j - k;
    return scriptable1;
  }
  
  protected int findInstanceIdInfo(String paramString) {
    int i = 0;
    String str = null;
    int j = paramString.length();
    if (j == 6) {
      j = paramString.charAt(0);
      if (j == 103) {
        str = "global";
        i = 3;
      } else if (j == 115) {
        str = "source";
        i = 2;
      } 
    } else if (j == 9) {
      j = paramString.charAt(0);
      if (j == 108) {
        str = "lastIndex";
        i = 1;
      } else if (j == 109) {
        str = "multiline";
        i = 5;
      } 
    } else if (j == 10) {
      str = "ignoreCase";
      i = 4;
    } 
    j = i;
    if (str != null) {
      j = i;
      if (str != paramString) {
        j = i;
        if (!str.equals(paramString))
          j = 0; 
      } 
    } 
    if (j == 0)
      return super.findInstanceIdInfo(paramString); 
    if (j != 1) {
      if (j == 2 || j == 3 || j == 4 || j == 5) {
        i = 7;
        return instanceIdInfo(i, j);
      } 
      throw new IllegalStateException();
    } 
    i = this.lastIndexAttr;
    return instanceIdInfo(i, j);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i != 4) {
      if (i != 6) {
        if (i != 7) {
          if (i == 8) {
            i = paramString.charAt(3);
            if (i == 111) {
              str = "toSource";
              b = 3;
            } else if (i == 116) {
              str = "toString";
              b = 2;
            } 
          } 
        } else {
          str = "compile";
          b = 1;
        } 
      } else {
        str = "prefix";
        b = 6;
      } 
    } else {
      i = paramString.charAt(0);
      if (i == 101) {
        str = "exec";
        b = 4;
      } else if (i == 116) {
        str = "test";
        b = 5;
      } 
    } 
    i = b;
    if (str != null) {
      i = b;
      if (str != paramString) {
        i = b;
        if (!str.equals(paramString))
          i = 0; 
      } 
    } 
    return i;
  }
  
  public String getClassName() {
    return "RegExp";
  }
  
  int getFlags() {
    return this.re.flags;
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? ((paramInt != 5) ? super.getInstanceIdName(paramInt) : "multiline") : "ignoreCase") : "global") : "source") : "lastIndex";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt != 4) {
            if (paramInt != 5)
              return super.getInstanceIdValue(paramInt); 
            if ((this.re.flags & 0x4) == 0)
              bool3 = false; 
            return ScriptRuntime.wrapBoolean(bool3);
          } 
          if ((0x2 & this.re.flags) != 0) {
            bool3 = bool1;
          } else {
            bool3 = false;
          } 
          return ScriptRuntime.wrapBoolean(bool3);
        } 
        if ((this.re.flags & 0x1) != 0) {
          bool3 = bool2;
        } else {
          bool3 = false;
        } 
        return ScriptRuntime.wrapBoolean(bool3);
      } 
      return new String(this.re.source);
    } 
    return this.lastIndex;
  }
  
  protected int getMaxInstanceId() {
    return 5;
  }
  
  public String getTypeOf() {
    return "object";
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 6:
        b = 1;
        str = "prefix";
        break;
      case 5:
        b = 1;
        str = "test";
        break;
      case 4:
        b = 1;
        str = "exec";
        break;
      case 3:
        b = 0;
        str = "toSource";
        break;
      case 2:
        b = 0;
        str = "toString";
        break;
      case 1:
        b = 2;
        str = "compile";
        break;
    } 
    initPrototypeMethod(REGEXP_TAG, paramInt, str, b);
  }
  
  protected void setInstanceIdAttributes(int paramInt1, int paramInt2) {
    if (paramInt1 != 1) {
      super.setInstanceIdAttributes(paramInt1, paramInt2);
      return;
    } 
    this.lastIndexAttr = paramInt2;
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    if (paramInt != 1) {
      if (paramInt != 2 && paramInt != 3 && paramInt != 4 && paramInt != 5) {
        super.setInstanceIdValue(paramInt, paramObject);
        return;
      } 
      return;
    } 
    this.lastIndex = paramObject;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('/');
    if (this.re.source.length != 0) {
      stringBuilder.append(this.re.source);
    } else {
      stringBuilder.append("(?:)");
    } 
    stringBuilder.append('/');
    if ((this.re.flags & 0x1) != 0)
      stringBuilder.append('g'); 
    if ((this.re.flags & 0x2) != 0)
      stringBuilder.append('i'); 
    if ((this.re.flags & 0x4) != 0)
      stringBuilder.append('m'); 
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/NativeRegExp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */