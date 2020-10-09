package com.trendmicro.hippo;

import java.lang.reflect.Method;
import java.util.concurrent.CopyOnWriteArrayList;

public class NativeJavaMethod extends BaseFunction {
  private static final int PREFERENCE_AMBIGUOUS = 3;
  
  private static final int PREFERENCE_EQUAL = 0;
  
  private static final int PREFERENCE_FIRST_ARG = 1;
  
  private static final int PREFERENCE_SECOND_ARG = 2;
  
  private static final boolean debug = false;
  
  private static final long serialVersionUID = -3440381785576412928L;
  
  private String functionName;
  
  MemberBox[] methods;
  
  private transient CopyOnWriteArrayList<ResolvedOverload> overloadCache;
  
  NativeJavaMethod(MemberBox paramMemberBox, String paramString) {
    this.functionName = paramString;
    this.methods = new MemberBox[] { paramMemberBox };
  }
  
  public NativeJavaMethod(Method paramMethod, String paramString) {
    this(new MemberBox(paramMethod), paramString);
  }
  
  NativeJavaMethod(MemberBox[] paramArrayOfMemberBox) {
    this.functionName = paramArrayOfMemberBox[0].getName();
    this.methods = paramArrayOfMemberBox;
  }
  
  NativeJavaMethod(MemberBox[] paramArrayOfMemberBox, String paramString) {
    this.functionName = paramString;
    this.methods = paramArrayOfMemberBox;
  }
  
  static int findFunction(Context paramContext, MemberBox[] paramArrayOfMemberBox, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_1
    //   1: arraylength
    //   2: ifne -> 7
    //   5: iconst_m1
    //   6: ireturn
    //   7: aload_1
    //   8: arraylength
    //   9: istore_3
    //   10: iconst_1
    //   11: istore #4
    //   13: iload_3
    //   14: iconst_1
    //   15: if_icmpne -> 98
    //   18: aload_1
    //   19: iconst_0
    //   20: aaload
    //   21: astore_0
    //   22: aload_0
    //   23: getfield argTypes : [Ljava/lang/Class;
    //   26: astore_1
    //   27: aload_1
    //   28: arraylength
    //   29: istore #5
    //   31: aload_0
    //   32: getfield vararg : Z
    //   35: ifeq -> 53
    //   38: iinc #5, -1
    //   41: iload #5
    //   43: istore_3
    //   44: iload #5
    //   46: aload_2
    //   47: arraylength
    //   48: if_icmple -> 65
    //   51: iconst_m1
    //   52: ireturn
    //   53: iload #5
    //   55: istore_3
    //   56: iload #5
    //   58: aload_2
    //   59: arraylength
    //   60: if_icmpeq -> 65
    //   63: iconst_m1
    //   64: ireturn
    //   65: iconst_0
    //   66: istore #5
    //   68: iload #5
    //   70: iload_3
    //   71: if_icmpeq -> 96
    //   74: aload_2
    //   75: iload #5
    //   77: aaload
    //   78: aload_1
    //   79: iload #5
    //   81: aaload
    //   82: invokestatic canConvert : (Ljava/lang/Object;Ljava/lang/Class;)Z
    //   85: ifne -> 90
    //   88: iconst_m1
    //   89: ireturn
    //   90: iinc #5, 1
    //   93: goto -> 68
    //   96: iconst_0
    //   97: ireturn
    //   98: iconst_m1
    //   99: istore #5
    //   101: aconst_null
    //   102: astore #6
    //   104: iconst_0
    //   105: istore #7
    //   107: iconst_0
    //   108: istore_3
    //   109: iload_3
    //   110: aload_1
    //   111: arraylength
    //   112: if_icmpge -> 527
    //   115: aload_1
    //   116: iload_3
    //   117: aaload
    //   118: astore #8
    //   120: aload #8
    //   122: getfield argTypes : [Ljava/lang/Class;
    //   125: astore #9
    //   127: aload #9
    //   129: arraylength
    //   130: istore #10
    //   132: aload #8
    //   134: getfield vararg : Z
    //   137: ifeq -> 157
    //   140: iinc #10, -1
    //   143: iload #10
    //   145: istore #11
    //   147: iload #10
    //   149: aload_2
    //   150: arraylength
    //   151: if_icmple -> 171
    //   154: goto -> 476
    //   157: iload #10
    //   159: istore #11
    //   161: iload #10
    //   163: aload_2
    //   164: arraylength
    //   165: if_icmpeq -> 171
    //   168: goto -> 476
    //   171: iconst_0
    //   172: istore #10
    //   174: iload #10
    //   176: iload #11
    //   178: if_icmpge -> 205
    //   181: aload_2
    //   182: iload #10
    //   184: aaload
    //   185: aload #9
    //   187: iload #10
    //   189: aaload
    //   190: invokestatic canConvert : (Ljava/lang/Object;Ljava/lang/Class;)Z
    //   193: ifne -> 199
    //   196: goto -> 476
    //   199: iinc #10, 1
    //   202: goto -> 174
    //   205: iload #5
    //   207: ifge -> 220
    //   210: iload_3
    //   211: istore #5
    //   213: iload #7
    //   215: istore #11
    //   217: goto -> 517
    //   220: iconst_0
    //   221: istore #12
    //   223: iconst_0
    //   224: istore #10
    //   226: iconst_m1
    //   227: istore #13
    //   229: iload #11
    //   231: istore #14
    //   233: iload #13
    //   235: iload #7
    //   237: if_icmpeq -> 446
    //   240: iload #13
    //   242: iconst_m1
    //   243: if_icmpne -> 253
    //   246: iload #5
    //   248: istore #11
    //   250: goto -> 260
    //   253: aload #6
    //   255: iload #13
    //   257: iaload
    //   258: istore #11
    //   260: aload_1
    //   261: iload #11
    //   263: aaload
    //   264: astore #15
    //   266: aload_0
    //   267: bipush #13
    //   269: invokevirtual hasFeature : (I)Z
    //   272: ifeq -> 315
    //   275: aload #15
    //   277: invokevirtual isPublic : ()Z
    //   280: aload #8
    //   282: invokevirtual isPublic : ()Z
    //   285: if_icmpeq -> 315
    //   288: aload #15
    //   290: invokevirtual isPublic : ()Z
    //   293: ifne -> 305
    //   296: iload #12
    //   298: iconst_1
    //   299: iadd
    //   300: istore #11
    //   302: goto -> 375
    //   305: iinc #10, 1
    //   308: iload #12
    //   310: istore #11
    //   312: goto -> 375
    //   315: aload_2
    //   316: aload #9
    //   318: aload #8
    //   320: getfield vararg : Z
    //   323: aload #15
    //   325: getfield argTypes : [Ljava/lang/Class;
    //   328: aload #15
    //   330: getfield vararg : Z
    //   333: invokestatic preferSignature : ([Ljava/lang/Object;[Ljava/lang/Class;Z[Ljava/lang/Class;Z)I
    //   336: istore #11
    //   338: iload #11
    //   340: iconst_3
    //   341: if_icmpne -> 347
    //   344: goto -> 446
    //   347: iload #11
    //   349: iconst_1
    //   350: if_icmpne -> 362
    //   353: iload #12
    //   355: iconst_1
    //   356: iadd
    //   357: istore #11
    //   359: goto -> 375
    //   362: iload #11
    //   364: iconst_2
    //   365: if_icmpne -> 385
    //   368: iinc #10, 1
    //   371: iload #12
    //   373: istore #11
    //   375: iinc #13, 1
    //   378: iload #11
    //   380: istore #12
    //   382: goto -> 233
    //   385: iload #11
    //   387: ifeq -> 394
    //   390: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   393: pop
    //   394: aload #15
    //   396: invokevirtual isStatic : ()Z
    //   399: ifeq -> 476
    //   402: aload #15
    //   404: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   407: aload #8
    //   409: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   412: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   415: ifeq -> 476
    //   418: iload #13
    //   420: iconst_m1
    //   421: if_icmpne -> 437
    //   424: iload_3
    //   425: istore #5
    //   427: iconst_1
    //   428: istore #4
    //   430: iload #7
    //   432: istore #11
    //   434: goto -> 517
    //   437: aload #6
    //   439: iload #13
    //   441: iload_3
    //   442: iastore
    //   443: goto -> 476
    //   446: iload #12
    //   448: iload #7
    //   450: iconst_1
    //   451: iadd
    //   452: if_icmpne -> 467
    //   455: iload_3
    //   456: istore #5
    //   458: iconst_0
    //   459: istore #11
    //   461: iconst_1
    //   462: istore #4
    //   464: goto -> 517
    //   467: iload #10
    //   469: iload #7
    //   471: iconst_1
    //   472: iadd
    //   473: if_icmpne -> 486
    //   476: iconst_1
    //   477: istore #4
    //   479: iload #7
    //   481: istore #11
    //   483: goto -> 517
    //   486: aload #6
    //   488: ifnonnull -> 502
    //   491: aload_1
    //   492: arraylength
    //   493: iconst_1
    //   494: isub
    //   495: newarray int
    //   497: astore #6
    //   499: goto -> 502
    //   502: iconst_1
    //   503: istore #4
    //   505: aload #6
    //   507: iload #7
    //   509: iload_3
    //   510: iastore
    //   511: iload #7
    //   513: iconst_1
    //   514: iadd
    //   515: istore #11
    //   517: iinc #3, 1
    //   520: iload #11
    //   522: istore #7
    //   524: goto -> 109
    //   527: iload #5
    //   529: ifge -> 534
    //   532: iconst_m1
    //   533: ireturn
    //   534: iload #7
    //   536: ifne -> 542
    //   539: iload #5
    //   541: ireturn
    //   542: new java/lang/StringBuilder
    //   545: dup
    //   546: invokespecial <init> : ()V
    //   549: astore_0
    //   550: iconst_m1
    //   551: istore_3
    //   552: iload_3
    //   553: iload #7
    //   555: if_icmpeq -> 601
    //   558: iload_3
    //   559: iconst_m1
    //   560: if_icmpne -> 570
    //   563: iload #5
    //   565: istore #11
    //   567: goto -> 576
    //   570: aload #6
    //   572: iload_3
    //   573: iaload
    //   574: istore #11
    //   576: aload_0
    //   577: ldc '\\n    '
    //   579: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   582: pop
    //   583: aload_0
    //   584: aload_1
    //   585: iload #11
    //   587: aaload
    //   588: invokevirtual toJavaDeclaration : ()Ljava/lang/String;
    //   591: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   594: pop
    //   595: iinc #3, 1
    //   598: goto -> 552
    //   601: aload_1
    //   602: iload #5
    //   604: aaload
    //   605: astore #8
    //   607: aload #8
    //   609: invokevirtual getName : ()Ljava/lang/String;
    //   612: astore #6
    //   614: aload #8
    //   616: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   619: invokevirtual getName : ()Ljava/lang/String;
    //   622: astore #8
    //   624: aload_1
    //   625: iconst_0
    //   626: aaload
    //   627: invokevirtual isCtor : ()Z
    //   630: ifeq -> 649
    //   633: ldc 'msg.constructor.ambiguous'
    //   635: aload #6
    //   637: aload_2
    //   638: invokestatic scriptSignature : ([Ljava/lang/Object;)Ljava/lang/String;
    //   641: aload_0
    //   642: invokevirtual toString : ()Ljava/lang/String;
    //   645: invokestatic reportRuntimeError3 : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   648: athrow
    //   649: ldc 'msg.method.ambiguous'
    //   651: aload #8
    //   653: aload #6
    //   655: aload_2
    //   656: invokestatic scriptSignature : ([Ljava/lang/Object;)Ljava/lang/String;
    //   659: aload_0
    //   660: invokevirtual toString : ()Ljava/lang/String;
    //   663: invokestatic reportRuntimeError4 : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   666: athrow
  }
  
  private static int preferSignature(Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass1, boolean paramBoolean1, Class<?>[] paramArrayOfClass2, boolean paramBoolean2) {
    int j;
    int i = 0;
    byte b = 0;
    while (true) {
      j = i;
      if (b < paramArrayOfObject.length) {
        Class<?> clazz1;
        Class<?> clazz2;
        if (paramBoolean1 && b >= paramArrayOfClass1.length) {
          clazz1 = paramArrayOfClass1[paramArrayOfClass1.length - 1];
        } else {
          clazz1 = paramArrayOfClass1[b];
        } 
        if (paramBoolean2 && b >= paramArrayOfClass2.length) {
          clazz2 = paramArrayOfClass2[paramArrayOfClass2.length - 1];
        } else {
          clazz2 = paramArrayOfClass2[b];
        } 
        if (clazz1 != clazz2) {
          Object object = paramArrayOfObject[b];
          j = NativeJavaObject.getConversionWeight(object, clazz1);
          int k = NativeJavaObject.getConversionWeight(object, clazz2);
          if (j < k) {
            j = 1;
          } else if (j > k) {
            j = 2;
          } else if (j == 0) {
            if (clazz1.isAssignableFrom(clazz2)) {
              j = 2;
            } else if (clazz2.isAssignableFrom(clazz1)) {
              j = 1;
            } else {
              j = 3;
            } 
          } else {
            j = 3;
          } 
          j = i | j;
          i = j;
          if (j == 3)
            break; 
        } 
        b++;
        continue;
      } 
      break;
    } 
    return j;
  }
  
  private static void printDebug(String paramString, MemberBox paramMemberBox, Object[] paramArrayOfObject) {}
  
  static String scriptSignature(Object[] paramArrayOfObject) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b != paramArrayOfObject.length; b++) {
      Object object = paramArrayOfObject[b];
      if (object == null) {
        object = "null";
      } else if (object instanceof Boolean) {
        object = "boolean";
      } else if (object instanceof String) {
        object = "string";
      } else if (object instanceof Number) {
        object = "number";
      } else if (object instanceof Scriptable) {
        if (object instanceof Undefined) {
          object = "undefined";
        } else if (object instanceof Wrapper) {
          object = ((Wrapper)object).unwrap().getClass().getName();
        } else if (object instanceof Function) {
          object = "function";
        } else {
          object = "object";
        } 
      } else {
        object = JavaMembers.javaSignature(object.getClass());
      } 
      if (b)
        stringBuilder.append(','); 
      stringBuilder.append((String)object);
    } 
    return stringBuilder.toString();
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_0
    //   1: getfield methods : [Lcom/trendmicro/hippo/MemberBox;
    //   4: arraylength
    //   5: ifeq -> 565
    //   8: aload_0
    //   9: aload_1
    //   10: aload #4
    //   12: invokevirtual findCachedFunction : (Lcom/trendmicro/hippo/Context;[Ljava/lang/Object;)I
    //   15: istore #5
    //   17: iload #5
    //   19: iflt -> 484
    //   22: aload_0
    //   23: getfield methods : [Lcom/trendmicro/hippo/MemberBox;
    //   26: iload #5
    //   28: aaload
    //   29: astore #6
    //   31: aload #6
    //   33: getfield argTypes : [Ljava/lang/Class;
    //   36: astore #7
    //   38: aload #6
    //   40: getfield vararg : Z
    //   43: ifeq -> 251
    //   46: aload #7
    //   48: arraylength
    //   49: anewarray java/lang/Object
    //   52: astore #8
    //   54: iconst_0
    //   55: istore #5
    //   57: iload #5
    //   59: aload #7
    //   61: arraylength
    //   62: iconst_1
    //   63: isub
    //   64: if_icmpge -> 91
    //   67: aload #8
    //   69: iload #5
    //   71: aload #4
    //   73: iload #5
    //   75: aaload
    //   76: aload #7
    //   78: iload #5
    //   80: aaload
    //   81: invokestatic jsToJava : (Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
    //   84: aastore
    //   85: iinc #5, 1
    //   88: goto -> 57
    //   91: aload #4
    //   93: arraylength
    //   94: aload #7
    //   96: arraylength
    //   97: if_icmpne -> 163
    //   100: aload #4
    //   102: aload #4
    //   104: arraylength
    //   105: iconst_1
    //   106: isub
    //   107: aaload
    //   108: ifnull -> 139
    //   111: aload #4
    //   113: aload #4
    //   115: arraylength
    //   116: iconst_1
    //   117: isub
    //   118: aaload
    //   119: instanceof com/trendmicro/hippo/NativeArray
    //   122: ifne -> 139
    //   125: aload #4
    //   127: aload #4
    //   129: arraylength
    //   130: iconst_1
    //   131: isub
    //   132: aaload
    //   133: instanceof com/trendmicro/hippo/NativeJavaArray
    //   136: ifeq -> 163
    //   139: aload #4
    //   141: aload #4
    //   143: arraylength
    //   144: iconst_1
    //   145: isub
    //   146: aaload
    //   147: aload #7
    //   149: aload #7
    //   151: arraylength
    //   152: iconst_1
    //   153: isub
    //   154: aaload
    //   155: invokestatic jsToJava : (Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
    //   158: astore #4
    //   160: goto -> 238
    //   163: aload #7
    //   165: aload #7
    //   167: arraylength
    //   168: iconst_1
    //   169: isub
    //   170: aaload
    //   171: invokevirtual getComponentType : ()Ljava/lang/Class;
    //   174: astore #9
    //   176: aload #9
    //   178: aload #4
    //   180: arraylength
    //   181: aload #7
    //   183: arraylength
    //   184: isub
    //   185: iconst_1
    //   186: iadd
    //   187: invokestatic newInstance : (Ljava/lang/Class;I)Ljava/lang/Object;
    //   190: astore #10
    //   192: iconst_0
    //   193: istore #5
    //   195: iload #5
    //   197: aload #10
    //   199: invokestatic getLength : (Ljava/lang/Object;)I
    //   202: if_icmpge -> 234
    //   205: aload #10
    //   207: iload #5
    //   209: aload #4
    //   211: aload #7
    //   213: arraylength
    //   214: iconst_1
    //   215: isub
    //   216: iload #5
    //   218: iadd
    //   219: aaload
    //   220: aload #9
    //   222: invokestatic jsToJava : (Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
    //   225: invokestatic set : (Ljava/lang/Object;ILjava/lang/Object;)V
    //   228: iinc #5, 1
    //   231: goto -> 195
    //   234: aload #10
    //   236: astore #4
    //   238: aload #8
    //   240: aload #7
    //   242: arraylength
    //   243: iconst_1
    //   244: isub
    //   245: aload #4
    //   247: aastore
    //   248: goto -> 338
    //   251: iconst_0
    //   252: istore #5
    //   254: aload #4
    //   256: astore #10
    //   258: aload #10
    //   260: astore #8
    //   262: iload #5
    //   264: aload #10
    //   266: arraylength
    //   267: if_icmpge -> 338
    //   270: aload #10
    //   272: iload #5
    //   274: aaload
    //   275: astore #11
    //   277: aload #11
    //   279: aload #7
    //   281: iload #5
    //   283: aaload
    //   284: invokestatic jsToJava : (Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
    //   287: astore #9
    //   289: aload #10
    //   291: astore #8
    //   293: aload #9
    //   295: aload #11
    //   297: if_acmpeq -> 328
    //   300: aload #10
    //   302: astore #8
    //   304: aload #4
    //   306: aload #10
    //   308: if_acmpne -> 321
    //   311: aload #10
    //   313: invokevirtual clone : ()Ljava/lang/Object;
    //   316: checkcast [Ljava/lang/Object;
    //   319: astore #8
    //   321: aload #8
    //   323: iload #5
    //   325: aload #9
    //   327: aastore
    //   328: iinc #5, 1
    //   331: aload #8
    //   333: astore #10
    //   335: goto -> 258
    //   338: aload #6
    //   340: invokevirtual isStatic : ()Z
    //   343: ifeq -> 351
    //   346: aconst_null
    //   347: astore_3
    //   348: goto -> 399
    //   351: aload_3
    //   352: astore #4
    //   354: aload #6
    //   356: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   359: astore #7
    //   361: aload #4
    //   363: ifnull -> 464
    //   366: aload #4
    //   368: instanceof com/trendmicro/hippo/Wrapper
    //   371: ifeq -> 452
    //   374: aload #4
    //   376: checkcast com/trendmicro/hippo/Wrapper
    //   379: invokeinterface unwrap : ()Ljava/lang/Object;
    //   384: astore #10
    //   386: aload #7
    //   388: aload #10
    //   390: invokevirtual isInstance : (Ljava/lang/Object;)Z
    //   393: ifeq -> 452
    //   396: aload #10
    //   398: astore_3
    //   399: aload #6
    //   401: aload_3
    //   402: aload #8
    //   404: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   407: astore #4
    //   409: aload #6
    //   411: invokevirtual method : ()Ljava/lang/reflect/Method;
    //   414: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   417: astore_3
    //   418: aload_1
    //   419: invokevirtual getWrapFactory : ()Lcom/trendmicro/hippo/WrapFactory;
    //   422: aload_1
    //   423: aload_2
    //   424: aload #4
    //   426: aload_3
    //   427: invokevirtual wrap : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
    //   430: astore_2
    //   431: aload_2
    //   432: astore_1
    //   433: aload_2
    //   434: ifnonnull -> 450
    //   437: aload_2
    //   438: astore_1
    //   439: aload_3
    //   440: getstatic java/lang/Void.TYPE : Ljava/lang/Class;
    //   443: if_acmpne -> 450
    //   446: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   449: astore_1
    //   450: aload_1
    //   451: areturn
    //   452: aload #4
    //   454: invokeinterface getPrototype : ()Lcom/trendmicro/hippo/Scriptable;
    //   459: astore #4
    //   461: goto -> 361
    //   464: ldc_w 'msg.nonjava.method'
    //   467: aload_0
    //   468: invokevirtual getFunctionName : ()Ljava/lang/String;
    //   471: aload_3
    //   472: invokestatic toString : (Ljava/lang/Object;)Ljava/lang/String;
    //   475: aload #7
    //   477: invokevirtual getName : ()Ljava/lang/String;
    //   480: invokestatic reportRuntimeError3 : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   483: athrow
    //   484: aload_0
    //   485: getfield methods : [Lcom/trendmicro/hippo/MemberBox;
    //   488: iconst_0
    //   489: aaload
    //   490: invokevirtual method : ()Ljava/lang/reflect/Method;
    //   493: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   496: astore_1
    //   497: new java/lang/StringBuilder
    //   500: dup
    //   501: invokespecial <init> : ()V
    //   504: astore_2
    //   505: aload_2
    //   506: aload_1
    //   507: invokevirtual getName : ()Ljava/lang/String;
    //   510: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   513: pop
    //   514: aload_2
    //   515: bipush #46
    //   517: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload_2
    //   522: aload_0
    //   523: invokevirtual getFunctionName : ()Ljava/lang/String;
    //   526: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   529: pop
    //   530: aload_2
    //   531: bipush #40
    //   533: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   536: pop
    //   537: aload_2
    //   538: aload #4
    //   540: invokestatic scriptSignature : ([Ljava/lang/Object;)Ljava/lang/String;
    //   543: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   546: pop
    //   547: aload_2
    //   548: bipush #41
    //   550: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   553: pop
    //   554: ldc_w 'msg.java.no_such_method'
    //   557: aload_2
    //   558: invokevirtual toString : ()Ljava/lang/String;
    //   561: invokestatic reportRuntimeError1 : (Ljava/lang/String;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   564: athrow
    //   565: new java/lang/RuntimeException
    //   568: dup
    //   569: ldc_w 'No methods defined for call'
    //   572: invokespecial <init> : (Ljava/lang/String;)V
    //   575: athrow
  }
  
  String decompile(int paramInt1, int paramInt2) {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    if ((paramInt2 & 0x1) != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 == 0) {
      stringBuilder.append("function ");
      stringBuilder.append(getFunctionName());
      stringBuilder.append("() {");
    } 
    stringBuilder.append("/*\n");
    stringBuilder.append(toString());
    if (paramInt1 != 0) {
      str = "*/\n";
    } else {
      str = "*/}\n";
    } 
    stringBuilder.append(str);
    return stringBuilder.toString();
  }
  
  int findCachedFunction(Context paramContext, Object[] paramArrayOfObject) {
    ResolvedOverload resolvedOverload;
    MemberBox[] arrayOfMemberBox = this.methods;
    if (arrayOfMemberBox.length > 1) {
      CopyOnWriteArrayList<ResolvedOverload> copyOnWriteArrayList = this.overloadCache;
      if (copyOnWriteArrayList != null) {
        for (ResolvedOverload resolvedOverload1 : copyOnWriteArrayList) {
          if (resolvedOverload1.matches(paramArrayOfObject))
            return resolvedOverload1.index; 
        } 
      } else {
        this.overloadCache = new CopyOnWriteArrayList<>();
      } 
      int i = findFunction(paramContext, this.methods, paramArrayOfObject);
      if (this.overloadCache.size() < this.methods.length * 2)
        synchronized (this.overloadCache) {
          resolvedOverload = new ResolvedOverload();
          this(paramArrayOfObject, i);
          if (!this.overloadCache.contains(resolvedOverload))
            this.overloadCache.add(0, resolvedOverload); 
        }  
      return i;
    } 
    return findFunction(paramContext, (MemberBox[])resolvedOverload, paramArrayOfObject);
  }
  
  public String getFunctionName() {
    return this.functionName;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    int i = 0;
    int j = this.methods.length;
    while (i != j) {
      if (this.methods[i].isMethod()) {
        Method method = this.methods[i].method();
        stringBuilder.append(JavaMembers.javaSignature(method.getReturnType()));
        stringBuilder.append(' ');
        stringBuilder.append(method.getName());
      } else {
        stringBuilder.append(this.methods[i].getName());
      } 
      stringBuilder.append(JavaMembers.liveConnectSignature((this.methods[i]).argTypes));
      stringBuilder.append('\n');
      i++;
    } 
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */