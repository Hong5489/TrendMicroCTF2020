package com.trendmicro.hippo;

import com.trendmicro.classfile.ClassFileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public final class JavaAdapter implements IdFunctionCall {
  private static final Object FTAG = "JavaAdapter";
  
  private static final int Id_JavaAdapter = 1;
  
  static int appendMethodSignature(Class<?>[] paramArrayOfClass, Class<?> paramClass, StringBuilder paramStringBuilder) {
    // Byte code:
    //   0: aload_2
    //   1: bipush #40
    //   3: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   6: pop
    //   7: aload_0
    //   8: arraylength
    //   9: iconst_1
    //   10: iadd
    //   11: istore_3
    //   12: aload_0
    //   13: arraylength
    //   14: istore #4
    //   16: iconst_0
    //   17: istore #5
    //   19: iload #5
    //   21: iload #4
    //   23: if_icmpge -> 72
    //   26: aload_0
    //   27: iload #5
    //   29: aaload
    //   30: astore #6
    //   32: aload_2
    //   33: aload #6
    //   35: invokestatic appendTypeString : (Ljava/lang/StringBuilder;Ljava/lang/Class;)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: aload #6
    //   41: getstatic java/lang/Long.TYPE : Ljava/lang/Class;
    //   44: if_acmpeq -> 58
    //   47: iload_3
    //   48: istore #7
    //   50: aload #6
    //   52: getstatic java/lang/Double.TYPE : Ljava/lang/Class;
    //   55: if_acmpne -> 63
    //   58: iload_3
    //   59: iconst_1
    //   60: iadd
    //   61: istore #7
    //   63: iinc #5, 1
    //   66: iload #7
    //   68: istore_3
    //   69: goto -> 19
    //   72: aload_2
    //   73: bipush #41
    //   75: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   78: pop
    //   79: aload_2
    //   80: aload_1
    //   81: invokestatic appendTypeString : (Ljava/lang/StringBuilder;Ljava/lang/Class;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: iload_3
    //   86: ireturn
  }
  
  private static void appendOverridableMethods(Class<?> paramClass, ArrayList<Method> paramArrayList, HashSet<String> paramHashSet) {
    Method[] arrayOfMethod = paramClass.getDeclaredMethods();
    for (byte b = 0; b < arrayOfMethod.length; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(arrayOfMethod[b].getName());
      stringBuilder.append(getMethodSignature(arrayOfMethod[b], arrayOfMethod[b].getParameterTypes()));
      String str = stringBuilder.toString();
      if (!paramHashSet.contains(str)) {
        int i = arrayOfMethod[b].getModifiers();
        if (!Modifier.isStatic(i))
          if (Modifier.isFinal(i)) {
            paramHashSet.add(str);
          } else if (Modifier.isPublic(i) || Modifier.isProtected(i)) {
            paramArrayList.add(arrayOfMethod[b]);
            paramHashSet.add(str);
          }  
      } 
    } 
  }
  
  private static StringBuilder appendTypeString(StringBuilder paramStringBuilder, Class<?> paramClass) {
    while (paramClass.isArray()) {
      paramStringBuilder.append('[');
      paramClass = paramClass.getComponentType();
    } 
    if (paramClass.isPrimitive()) {
      char c;
      if (paramClass == boolean.class) {
        byte b = 90;
        c = b;
      } else if (paramClass == long.class) {
        byte b = 74;
        c = b;
      } else {
        char c1 = Character.toUpperCase(paramClass.getName().charAt(0));
        c = c1;
      } 
      paramStringBuilder.append(c);
    } else {
      paramStringBuilder.append('L');
      paramStringBuilder.append(paramClass.getName().replace('.', '/'));
      paramStringBuilder.append(';');
    } 
    return paramStringBuilder;
  }
  
  public static Object callMethod(ContextFactory paramContextFactory, Scriptable paramScriptable, Function paramFunction, Object[] paramArrayOfObject, long paramLong) {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: aload_0
    //   7: astore #6
    //   9: aload_0
    //   10: ifnonnull -> 18
    //   13: invokestatic getGlobal : ()Lcom/trendmicro/hippo/ContextFactory;
    //   16: astore #6
    //   18: aload_2
    //   19: invokeinterface getParentScope : ()Lcom/trendmicro/hippo/Scriptable;
    //   24: astore #7
    //   26: lload #4
    //   28: lconst_0
    //   29: lcmp
    //   30: ifne -> 44
    //   33: aload #6
    //   35: aload_2
    //   36: aload #7
    //   38: aload_1
    //   39: aload_3
    //   40: invokestatic call : (Lcom/trendmicro/hippo/ContextFactory;Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   43: areturn
    //   44: invokestatic getCurrentContext : ()Lcom/trendmicro/hippo/Context;
    //   47: astore_0
    //   48: aload_0
    //   49: ifnull -> 64
    //   52: aload_0
    //   53: aload #7
    //   55: aload_1
    //   56: aload_2
    //   57: aload_3
    //   58: lload #4
    //   60: invokestatic doCall : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Function;[Ljava/lang/Object;J)Ljava/lang/Object;
    //   63: areturn
    //   64: aload #6
    //   66: aload #7
    //   68: aload_1
    //   69: aload_2
    //   70: aload_3
    //   71: lload #4
    //   73: <illegal opcode> run : (Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Function;[Ljava/lang/Object;J)Lcom/trendmicro/hippo/ContextAction;
    //   78: invokevirtual call : (Lcom/trendmicro/hippo/ContextAction;)Ljava/lang/Object;
    //   81: areturn
  }
  
  public static Object convertResult(Object paramObject, Class<?> paramClass) {
    return (paramObject == Undefined.instance && paramClass != ScriptRuntime.ObjectClass && paramClass != ScriptRuntime.StringClass) ? null : Context.jsToJava(paramObject, paramClass);
  }
  
  public static byte[] createAdapterCode(ObjToIntMap paramObjToIntMap, String paramString1, Class<?> paramClass, Class<?>[] paramArrayOfClass, String paramString2) {
    // Byte code:
    //   0: aload_3
    //   1: astore #5
    //   3: new com/trendmicro/classfile/ClassFileWriter
    //   6: dup
    //   7: aload_1
    //   8: aload_2
    //   9: invokevirtual getName : ()Ljava/lang/String;
    //   12: ldc '<adapter>'
    //   14: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   17: astore #6
    //   19: aload #6
    //   21: ldc 'factory'
    //   23: ldc 'Lcom/trendmicro/hippo/ContextFactory;'
    //   25: bipush #17
    //   27: invokevirtual addField : (Ljava/lang/String;Ljava/lang/String;S)V
    //   30: aload #6
    //   32: ldc 'delegee'
    //   34: ldc 'Lcom/trendmicro/hippo/Scriptable;'
    //   36: bipush #17
    //   38: invokevirtual addField : (Ljava/lang/String;Ljava/lang/String;S)V
    //   41: aload #6
    //   43: ldc 'self'
    //   45: ldc 'Lcom/trendmicro/hippo/Scriptable;'
    //   47: bipush #17
    //   49: invokevirtual addField : (Ljava/lang/String;Ljava/lang/String;S)V
    //   52: aload #5
    //   54: ifnonnull -> 63
    //   57: iconst_0
    //   58: istore #7
    //   60: goto -> 68
    //   63: aload #5
    //   65: arraylength
    //   66: istore #7
    //   68: iconst_0
    //   69: istore #8
    //   71: iload #8
    //   73: iload #7
    //   75: if_icmpge -> 105
    //   78: aload #5
    //   80: iload #8
    //   82: aaload
    //   83: ifnull -> 99
    //   86: aload #6
    //   88: aload #5
    //   90: iload #8
    //   92: aaload
    //   93: invokevirtual getName : ()Ljava/lang/String;
    //   96: invokevirtual addInterface : (Ljava/lang/String;)V
    //   99: iinc #8, 1
    //   102: goto -> 71
    //   105: aload_2
    //   106: invokevirtual getName : ()Ljava/lang/String;
    //   109: bipush #46
    //   111: bipush #47
    //   113: invokevirtual replace : (CC)Ljava/lang/String;
    //   116: astore #9
    //   118: aload_2
    //   119: invokevirtual getDeclaredConstructors : ()[Ljava/lang/reflect/Constructor;
    //   122: astore #5
    //   124: aload #5
    //   126: arraylength
    //   127: istore #10
    //   129: iconst_0
    //   130: istore #8
    //   132: iload #8
    //   134: iload #10
    //   136: if_icmpge -> 185
    //   139: aload #5
    //   141: iload #8
    //   143: aaload
    //   144: astore #11
    //   146: aload #11
    //   148: invokevirtual getModifiers : ()I
    //   151: istore #12
    //   153: iload #12
    //   155: invokestatic isPublic : (I)Z
    //   158: ifne -> 169
    //   161: iload #12
    //   163: invokestatic isProtected : (I)Z
    //   166: ifeq -> 179
    //   169: aload #6
    //   171: aload_1
    //   172: aload #9
    //   174: aload #11
    //   176: invokestatic generateCtor : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/reflect/Constructor;)V
    //   179: iinc #8, 1
    //   182: goto -> 132
    //   185: aload #6
    //   187: aload_1
    //   188: aload #9
    //   190: invokestatic generateSerialCtor : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;)V
    //   193: aload #4
    //   195: ifnull -> 208
    //   198: aload #6
    //   200: aload_1
    //   201: aload #9
    //   203: aload #4
    //   205: invokestatic generateEmptyCtor : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   208: new com/trendmicro/hippo/ObjToIntMap
    //   211: dup
    //   212: invokespecial <init> : ()V
    //   215: astore #11
    //   217: new com/trendmicro/hippo/ObjToIntMap
    //   220: dup
    //   221: invokespecial <init> : ()V
    //   224: astore #4
    //   226: iconst_0
    //   227: istore #8
    //   229: iload #8
    //   231: iload #7
    //   233: if_icmpge -> 450
    //   236: aload_3
    //   237: iload #8
    //   239: aaload
    //   240: invokevirtual getMethods : ()[Ljava/lang/reflect/Method;
    //   243: astore #13
    //   245: iconst_0
    //   246: istore #10
    //   248: aload #4
    //   250: astore #14
    //   252: aload #13
    //   254: astore #4
    //   256: iload #10
    //   258: aload #4
    //   260: arraylength
    //   261: if_icmpge -> 440
    //   264: aload #4
    //   266: iload #10
    //   268: aaload
    //   269: astore #15
    //   271: aload #15
    //   273: invokevirtual getModifiers : ()I
    //   276: istore #12
    //   278: iload #12
    //   280: invokestatic isStatic : (I)Z
    //   283: ifne -> 434
    //   286: iload #12
    //   288: invokestatic isFinal : (I)Z
    //   291: ifne -> 434
    //   294: aload #15
    //   296: invokevirtual isDefault : ()Z
    //   299: ifeq -> 305
    //   302: goto -> 434
    //   305: aload #15
    //   307: invokevirtual getName : ()Ljava/lang/String;
    //   310: astore #13
    //   312: aload #15
    //   314: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   317: astore #16
    //   319: aload_0
    //   320: aload #13
    //   322: invokevirtual has : (Ljava/lang/Object;)Z
    //   325: ifne -> 345
    //   328: aload_2
    //   329: aload #13
    //   331: aload #16
    //   333: invokevirtual getMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   336: pop
    //   337: goto -> 434
    //   340: astore #17
    //   342: goto -> 345
    //   345: aload #15
    //   347: aload #16
    //   349: invokestatic getMethodSignature : (Ljava/lang/reflect/Method;[Ljava/lang/Class;)Ljava/lang/String;
    //   352: astore #18
    //   354: new java/lang/StringBuilder
    //   357: dup
    //   358: invokespecial <init> : ()V
    //   361: astore #17
    //   363: aload #17
    //   365: aload #13
    //   367: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   370: pop
    //   371: aload #17
    //   373: aload #18
    //   375: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   378: pop
    //   379: aload #17
    //   381: invokevirtual toString : ()Ljava/lang/String;
    //   384: astore #17
    //   386: aload #11
    //   388: aload #17
    //   390: invokevirtual has : (Ljava/lang/Object;)Z
    //   393: ifne -> 431
    //   396: aload #6
    //   398: aload_1
    //   399: aload #13
    //   401: aload #16
    //   403: aload #15
    //   405: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   408: iconst_1
    //   409: invokestatic generateMethod : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
    //   412: aload #11
    //   414: aload #17
    //   416: iconst_0
    //   417: invokevirtual put : (Ljava/lang/Object;I)V
    //   420: aload #14
    //   422: aload #13
    //   424: iconst_0
    //   425: invokevirtual put : (Ljava/lang/Object;I)V
    //   428: goto -> 434
    //   431: goto -> 434
    //   434: iinc #10, 1
    //   437: goto -> 256
    //   440: iinc #8, 1
    //   443: aload #14
    //   445: astore #4
    //   447: goto -> 229
    //   450: aload_2
    //   451: invokestatic getOverridableMethods : (Ljava/lang/Class;)[Ljava/lang/reflect/Method;
    //   454: astore_2
    //   455: iconst_0
    //   456: istore #8
    //   458: iload #8
    //   460: aload_2
    //   461: arraylength
    //   462: if_icmpge -> 631
    //   465: aload_2
    //   466: iload #8
    //   468: aaload
    //   469: astore #5
    //   471: aload #5
    //   473: invokevirtual getModifiers : ()I
    //   476: invokestatic isAbstract : (I)Z
    //   479: istore #19
    //   481: aload #5
    //   483: invokevirtual getName : ()Ljava/lang/String;
    //   486: astore #13
    //   488: iload #19
    //   490: ifne -> 508
    //   493: aload_0
    //   494: aload #13
    //   496: invokevirtual has : (Ljava/lang/Object;)Z
    //   499: ifeq -> 505
    //   502: goto -> 508
    //   505: goto -> 625
    //   508: aload #5
    //   510: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   513: astore #14
    //   515: aload #5
    //   517: aload #14
    //   519: invokestatic getMethodSignature : (Ljava/lang/reflect/Method;[Ljava/lang/Class;)Ljava/lang/String;
    //   522: astore_3
    //   523: new java/lang/StringBuilder
    //   526: dup
    //   527: invokespecial <init> : ()V
    //   530: astore #16
    //   532: aload #16
    //   534: aload #13
    //   536: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   539: pop
    //   540: aload #16
    //   542: aload_3
    //   543: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   546: pop
    //   547: aload #16
    //   549: invokevirtual toString : ()Ljava/lang/String;
    //   552: astore #16
    //   554: aload #11
    //   556: aload #16
    //   558: invokevirtual has : (Ljava/lang/Object;)Z
    //   561: ifne -> 625
    //   564: aload #6
    //   566: aload_1
    //   567: aload #13
    //   569: aload #14
    //   571: aload #5
    //   573: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   576: iconst_1
    //   577: invokestatic generateMethod : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
    //   580: aload #11
    //   582: aload #16
    //   584: iconst_0
    //   585: invokevirtual put : (Ljava/lang/Object;I)V
    //   588: aload #4
    //   590: aload #13
    //   592: iconst_0
    //   593: invokevirtual put : (Ljava/lang/Object;I)V
    //   596: iload #19
    //   598: ifne -> 622
    //   601: aload #6
    //   603: aload_1
    //   604: aload #9
    //   606: aload #13
    //   608: aload_3
    //   609: aload #14
    //   611: aload #5
    //   613: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   616: invokestatic generateSuper : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;)V
    //   619: goto -> 625
    //   622: goto -> 625
    //   625: iinc #8, 1
    //   628: goto -> 458
    //   631: new com/trendmicro/hippo/ObjToIntMap$Iterator
    //   634: dup
    //   635: aload_0
    //   636: invokespecial <init> : (Lcom/trendmicro/hippo/ObjToIntMap;)V
    //   639: astore_2
    //   640: aload_2
    //   641: invokevirtual start : ()V
    //   644: aload_2
    //   645: invokevirtual done : ()Z
    //   648: ifne -> 725
    //   651: aload_2
    //   652: invokevirtual getKey : ()Ljava/lang/Object;
    //   655: checkcast java/lang/String
    //   658: astore_3
    //   659: aload #4
    //   661: aload_3
    //   662: invokevirtual has : (Ljava/lang/Object;)Z
    //   665: ifeq -> 671
    //   668: goto -> 718
    //   671: aload_2
    //   672: invokevirtual getValue : ()I
    //   675: istore #7
    //   677: iload #7
    //   679: anewarray java/lang/Class
    //   682: astore_0
    //   683: iconst_0
    //   684: istore #8
    //   686: iload #8
    //   688: iload #7
    //   690: if_icmpge -> 706
    //   693: aload_0
    //   694: iload #8
    //   696: getstatic com/trendmicro/hippo/ScriptRuntime.ObjectClass : Ljava/lang/Class;
    //   699: aastore
    //   700: iinc #8, 1
    //   703: goto -> 686
    //   706: aload #6
    //   708: aload_1
    //   709: aload_3
    //   710: aload_0
    //   711: getstatic com/trendmicro/hippo/ScriptRuntime.ObjectClass : Ljava/lang/Class;
    //   714: iconst_0
    //   715: invokestatic generateMethod : (Lcom/trendmicro/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
    //   718: aload_2
    //   719: invokevirtual next : ()V
    //   722: goto -> 644
    //   725: aload #6
    //   727: invokevirtual toByteArray : ()[B
    //   730: areturn
    // Exception table:
    //   from	to	target	type
    //   328	337	340	java/lang/NoSuchMethodException
  }
  
  public static Scriptable createAdapterWrapper(Scriptable paramScriptable, Object paramObject) {
    paramObject = new NativeJavaObject(ScriptableObject.getTopLevelScope(paramScriptable), paramObject, null, true);
    paramObject.setPrototype(paramScriptable);
    return (Scriptable)paramObject;
  }
  
  private static Object doCall(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Function paramFunction, Object[] paramArrayOfObject, long paramLong) {
    for (byte b = 0; b != paramArrayOfObject.length; b++) {
      if (0L != ((1 << b) & paramLong)) {
        Object object = paramArrayOfObject[b];
        if (!(object instanceof Scriptable))
          paramArrayOfObject[b] = paramContext.getWrapFactory().wrap(paramContext, paramScriptable1, object, null); 
      } 
    } 
    return paramFunction.call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
  }
  
  private static void generateCtor(ClassFileWriter paramClassFileWriter, String paramString1, String paramString2, Constructor<?> paramConstructor) {
    short s2;
    short s1 = 3;
    Class[] arrayOfClass = paramConstructor.getParameterTypes();
    if (arrayOfClass.length == 0) {
      paramClassFileWriter.startMethod("<init>", "(Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/ContextFactory;)V", (short)1);
      paramClassFileWriter.add(42);
      paramClassFileWriter.addInvoke(183, paramString2, "<init>", "()V");
      s2 = s1;
    } else {
      StringBuilder stringBuilder = new StringBuilder("(Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/ContextFactory;");
      int i = stringBuilder.length();
      int j = arrayOfClass.length;
      byte b = 0;
      for (s1 = 0; s1 < j; s1++)
        appendTypeString(stringBuilder, arrayOfClass[s1]); 
      stringBuilder.append(")V");
      paramClassFileWriter.startMethod("<init>", stringBuilder.toString(), (short)1);
      paramClassFileWriter.add(42);
      s1 = 3;
      j = arrayOfClass.length;
      while (b < j) {
        s1 = (short)(generatePushParam(paramClassFileWriter, s1, arrayOfClass[b]) + s1);
        b++;
      } 
      stringBuilder.delete(1, i);
      paramClassFileWriter.addInvoke(183, paramString2, "<init>", stringBuilder.toString());
      s2 = s1;
    } 
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(43);
    paramClassFileWriter.add(181, paramString1, "delegee", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(44);
    paramClassFileWriter.add(181, paramString1, "factory", "Lcom/trendmicro/hippo/ContextFactory;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(43);
    paramClassFileWriter.add(42);
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/JavaAdapter", "createAdapterWrapper", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(181, paramString1, "self", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod(s2);
  }
  
  private static void generateEmptyCtor(ClassFileWriter paramClassFileWriter, String paramString1, String paramString2, String paramString3) {
    paramClassFileWriter.startMethod("<init>", "()V", (short)1);
    paramClassFileWriter.add(42);
    paramClassFileWriter.addInvoke(183, paramString2, "<init>", "()V");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(1);
    paramClassFileWriter.add(181, paramString1, "factory", "Lcom/trendmicro/hippo/ContextFactory;");
    paramClassFileWriter.add(187, paramString3);
    paramClassFileWriter.add(89);
    paramClassFileWriter.addInvoke(183, paramString3, "<init>", "()V");
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/JavaAdapter", "runScript", "(Lcom/trendmicro/hippo/Script;)Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(76);
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(43);
    paramClassFileWriter.add(181, paramString1, "delegee", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(43);
    paramClassFileWriter.add(42);
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/JavaAdapter", "createAdapterWrapper", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(181, paramString1, "self", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod((short)2);
  }
  
  private static void generateMethod(ClassFileWriter paramClassFileWriter, String paramString1, String paramString2, Class<?>[] paramArrayOfClass, Class<?> paramClass, boolean paramBoolean) {
    StringBuilder stringBuilder = new StringBuilder();
    int i = appendMethodSignature(paramArrayOfClass, paramClass, stringBuilder);
    paramClassFileWriter.startMethod(paramString2, stringBuilder.toString(), (short)1);
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(180, paramString1, "factory", "Lcom/trendmicro/hippo/ContextFactory;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(180, paramString1, "self", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(180, paramString1, "delegee", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.addPush(paramString2);
    paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/JavaAdapter", "getFunction", "(Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Lcom/trendmicro/hippo/Function;");
    generatePushWrappedArgs(paramClassFileWriter, paramArrayOfClass, paramArrayOfClass.length);
    if (paramArrayOfClass.length <= 64) {
      long l = 0L;
      byte b = 0;
      while (b != paramArrayOfClass.length) {
        long l1 = l;
        if (!paramArrayOfClass[b].isPrimitive())
          l1 = l | (1 << b); 
        b++;
        l = l1;
      } 
      paramClassFileWriter.addPush(l);
      paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/JavaAdapter", "callMethod", "(Lcom/trendmicro/hippo/ContextFactory;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Function;[Ljava/lang/Object;J)Ljava/lang/Object;");
      generateReturnResult(paramClassFileWriter, paramClass, paramBoolean);
      paramClassFileWriter.stopMethod((short)i);
      return;
    } 
    throw Context.reportRuntimeError0("JavaAdapter can not subclass methods with more then 64 arguments.");
  }
  
  private static void generatePopResult(ClassFileWriter paramClassFileWriter, Class<?> paramClass) {
    if (paramClass.isPrimitive()) {
      char c = paramClass.getName().charAt(0);
      if (c != 'f') {
        if (c != 'i')
          if (c != 'l') {
            if (c != 's' && c != 'z')
              switch (c) {
                default:
                  return;
                case 'd':
                  paramClassFileWriter.add(175);
                case 'b':
                case 'c':
                  break;
              }  
          } else {
            paramClassFileWriter.add(173);
          }  
        paramClassFileWriter.add(172);
      } 
      paramClassFileWriter.add(174);
    } 
    paramClassFileWriter.add(176);
  }
  
  private static int generatePushParam(ClassFileWriter paramClassFileWriter, int paramInt, Class<?> paramClass) {
    if (!paramClass.isPrimitive()) {
      paramClassFileWriter.addALoad(paramInt);
      return 1;
    } 
    char c = paramClass.getName().charAt(0);
    if (c != 'f') {
      if (c != 'i')
        if (c != 'l') {
          if (c != 's' && c != 'z')
            switch (c) {
              default:
                throw Kit.codeBug();
              case 'd':
                paramClassFileWriter.addDLoad(paramInt);
                return 2;
              case 'b':
              case 'c':
                break;
            }  
        } else {
          paramClassFileWriter.addLLoad(paramInt);
          return 2;
        }  
      paramClassFileWriter.addILoad(paramInt);
      return 1;
    } 
    paramClassFileWriter.addFLoad(paramInt);
    return 1;
  }
  
  static void generatePushWrappedArgs(ClassFileWriter paramClassFileWriter, Class<?>[] paramArrayOfClass, int paramInt) {
    paramClassFileWriter.addPush(paramInt);
    paramClassFileWriter.add(189, "java/lang/Object");
    int i = 1;
    for (paramInt = 0; paramInt != paramArrayOfClass.length; paramInt++) {
      paramClassFileWriter.add(89);
      paramClassFileWriter.addPush(paramInt);
      i += generateWrapArg(paramClassFileWriter, i, paramArrayOfClass[paramInt]);
      paramClassFileWriter.add(83);
    } 
  }
  
  static void generateReturnResult(ClassFileWriter paramClassFileWriter, Class<?> paramClass, boolean paramBoolean) {
    if (paramClass == void.class) {
      paramClassFileWriter.add(87);
      paramClassFileWriter.add(177);
    } else if (paramClass == boolean.class) {
      paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/Context", "toBoolean", "(Ljava/lang/Object;)Z");
      paramClassFileWriter.add(172);
    } else if (paramClass == char.class) {
      paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/Context", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
      paramClassFileWriter.add(3);
      paramClassFileWriter.addInvoke(182, "java/lang/String", "charAt", "(I)C");
      paramClassFileWriter.add(172);
    } else {
      StringBuilder stringBuilder;
      if (paramClass.isPrimitive()) {
        paramClassFileWriter.addInvoke(184, "com/trendmicro/hippo/Context", "toNumber", "(Ljava/lang/Object;)D");
        char c = paramClass.getName().charAt(0);
        if (c != 'b')
          if (c != 'd') {
            if (c != 'f') {
              if (c != 'i')
                if (c != 'l') {
                  if (c != 's') {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected return type ");
                    stringBuilder.append(paramClass.toString());
                    throw new RuntimeException(stringBuilder.toString());
                  } 
                } else {
                  stringBuilder.add(143);
                  stringBuilder.add(173);
                  return;
                }  
            } else {
              stringBuilder.add(144);
              stringBuilder.add(174);
              return;
            } 
          } else {
            stringBuilder.add(175);
            return;
          }  
        stringBuilder.add(142);
        stringBuilder.add(172);
      } else {
        String str = paramClass.getName();
        if (paramBoolean) {
          stringBuilder.addLoadConstant(str);
          stringBuilder.addInvoke(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
          stringBuilder.addInvoke(184, "com/trendmicro/hippo/JavaAdapter", "convertResult", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
        } 
        stringBuilder.add(192, str);
        stringBuilder.add(176);
      } 
    } 
  }
  
  private static void generateSerialCtor(ClassFileWriter paramClassFileWriter, String paramString1, String paramString2) {
    paramClassFileWriter.startMethod("<init>", "(Lcom/trendmicro/hippo/ContextFactory;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;)V", (short)1);
    paramClassFileWriter.add(42);
    paramClassFileWriter.addInvoke(183, paramString2, "<init>", "()V");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(43);
    paramClassFileWriter.add(181, paramString1, "factory", "Lcom/trendmicro/hippo/ContextFactory;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(44);
    paramClassFileWriter.add(181, paramString1, "delegee", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(42);
    paramClassFileWriter.add(45);
    paramClassFileWriter.add(181, paramString1, "self", "Lcom/trendmicro/hippo/Scriptable;");
    paramClassFileWriter.add(177);
    paramClassFileWriter.stopMethod((short)4);
  }
  
  private static void generateSuper(ClassFileWriter paramClassFileWriter, String paramString1, String paramString2, String paramString3, String paramString4, Class<?>[] paramArrayOfClass, Class<?> paramClass) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("super$");
    stringBuilder.append(paramString3);
    paramClassFileWriter.startMethod(stringBuilder.toString(), paramString4, (short)1);
    byte b = 0;
    paramClassFileWriter.add(25, 0);
    int i = 1;
    int j = paramArrayOfClass.length;
    while (b < j) {
      i += generatePushParam(paramClassFileWriter, i, paramArrayOfClass[b]);
      b++;
    } 
    paramClassFileWriter.addInvoke(183, paramString2, paramString3, paramString4);
    if (!paramClass.equals(void.class)) {
      generatePopResult(paramClassFileWriter, paramClass);
    } else {
      paramClassFileWriter.add(177);
    } 
    paramClassFileWriter.stopMethod((short)(i + 1));
  }
  
  private static int generateWrapArg(ClassFileWriter paramClassFileWriter, int paramInt, Class<?> paramClass) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: iconst_1
    //   3: istore #4
    //   5: aload_2
    //   6: invokevirtual isPrimitive : ()Z
    //   9: ifne -> 24
    //   12: aload_0
    //   13: bipush #25
    //   15: iload_1
    //   16: invokevirtual add : (II)V
    //   19: iload_3
    //   20: istore_1
    //   21: goto -> 261
    //   24: aload_2
    //   25: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   28: if_acmpne -> 75
    //   31: aload_0
    //   32: sipush #187
    //   35: ldc_w 'java/lang/Boolean'
    //   38: invokevirtual add : (ILjava/lang/String;)V
    //   41: aload_0
    //   42: bipush #89
    //   44: invokevirtual add : (I)V
    //   47: aload_0
    //   48: bipush #21
    //   50: iload_1
    //   51: invokevirtual add : (II)V
    //   54: aload_0
    //   55: sipush #183
    //   58: ldc_w 'java/lang/Boolean'
    //   61: ldc_w '<init>'
    //   64: ldc_w '(Z)V'
    //   67: invokevirtual addInvoke : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   70: iload_3
    //   71: istore_1
    //   72: goto -> 261
    //   75: aload_2
    //   76: getstatic java/lang/Character.TYPE : Ljava/lang/Class;
    //   79: if_acmpne -> 110
    //   82: aload_0
    //   83: bipush #21
    //   85: iload_1
    //   86: invokevirtual add : (II)V
    //   89: aload_0
    //   90: sipush #184
    //   93: ldc_w 'java/lang/String'
    //   96: ldc_w 'valueOf'
    //   99: ldc_w '(C)Ljava/lang/String;'
    //   102: invokevirtual addInvoke : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   105: iload_3
    //   106: istore_1
    //   107: goto -> 261
    //   110: aload_0
    //   111: sipush #187
    //   114: ldc_w 'java/lang/Double'
    //   117: invokevirtual add : (ILjava/lang/String;)V
    //   120: aload_0
    //   121: bipush #89
    //   123: invokevirtual add : (I)V
    //   126: aload_2
    //   127: invokevirtual getName : ()Ljava/lang/String;
    //   130: iconst_0
    //   131: invokevirtual charAt : (I)C
    //   134: istore_3
    //   135: iload_3
    //   136: bipush #98
    //   138: if_icmpeq -> 228
    //   141: iload_3
    //   142: bipush #100
    //   144: if_icmpeq -> 216
    //   147: iload_3
    //   148: bipush #102
    //   150: if_icmpeq -> 196
    //   153: iload_3
    //   154: bipush #105
    //   156: if_icmpeq -> 228
    //   159: iload_3
    //   160: bipush #108
    //   162: if_icmpeq -> 177
    //   165: iload_3
    //   166: bipush #115
    //   168: if_icmpeq -> 228
    //   171: iload #4
    //   173: istore_1
    //   174: goto -> 245
    //   177: aload_0
    //   178: bipush #22
    //   180: iload_1
    //   181: invokevirtual add : (II)V
    //   184: aload_0
    //   185: sipush #138
    //   188: invokevirtual add : (I)V
    //   191: iconst_2
    //   192: istore_1
    //   193: goto -> 245
    //   196: aload_0
    //   197: bipush #23
    //   199: iload_1
    //   200: invokevirtual add : (II)V
    //   203: aload_0
    //   204: sipush #141
    //   207: invokevirtual add : (I)V
    //   210: iload #4
    //   212: istore_1
    //   213: goto -> 245
    //   216: aload_0
    //   217: bipush #24
    //   219: iload_1
    //   220: invokevirtual add : (II)V
    //   223: iconst_2
    //   224: istore_1
    //   225: goto -> 245
    //   228: aload_0
    //   229: bipush #21
    //   231: iload_1
    //   232: invokevirtual add : (II)V
    //   235: aload_0
    //   236: sipush #135
    //   239: invokevirtual add : (I)V
    //   242: iload #4
    //   244: istore_1
    //   245: aload_0
    //   246: sipush #183
    //   249: ldc_w 'java/lang/Double'
    //   252: ldc_w '<init>'
    //   255: ldc_w '(D)V'
    //   258: invokevirtual addInvoke : (ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   261: iload_1
    //   262: ireturn
  }
  
  private static Class<?> getAdapterClass(Scriptable paramScriptable1, Class<?> paramClass, Class<?>[] paramArrayOfClass, Scriptable paramScriptable2) {
    ClassCache classCache = ClassCache.get(paramScriptable1);
    Map<JavaAdapterSignature, Class<?>> map = classCache.getInterfaceAdapterCacheMap();
    ObjToIntMap objToIntMap = getObjectFunctionNames(paramScriptable2);
    JavaAdapterSignature javaAdapterSignature = new JavaAdapterSignature(paramClass, paramArrayOfClass, objToIntMap);
    Class clazz1 = map.get(javaAdapterSignature);
    Class<?> clazz = clazz1;
    if (clazz1 == null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("adapter");
      stringBuilder.append(classCache.newClassSerialNumber());
      String str = stringBuilder.toString();
      paramClass = loadAdapterClass(str, createAdapterCode(objToIntMap, str, paramClass, paramArrayOfClass, null));
      clazz = paramClass;
      if (classCache.isCachingEnabled()) {
        map.put(javaAdapterSignature, paramClass);
        clazz = paramClass;
      } 
    } 
    return clazz;
  }
  
  public static Object getAdapterSelf(Class<?> paramClass, Object paramObject) throws NoSuchFieldException, IllegalAccessException {
    return paramClass.getDeclaredField("self").get(paramObject);
  }
  
  static int[] getArgsToConvert(Class<?>[] paramArrayOfClass) {
    int i = 0;
    byte b = 0;
    while (b != paramArrayOfClass.length) {
      int k = i;
      if (!paramArrayOfClass[b].isPrimitive())
        k = i + 1; 
      b++;
      i = k;
    } 
    if (i == 0)
      return null; 
    int[] arrayOfInt = new int[i];
    int j = 0;
    b = 0;
    while (b != paramArrayOfClass.length) {
      i = j;
      if (!paramArrayOfClass[b].isPrimitive()) {
        arrayOfInt[j] = b;
        i = j + 1;
      } 
      b++;
      j = i;
    } 
    return arrayOfInt;
  }
  
  public static Function getFunction(Scriptable paramScriptable, String paramString) {
    Object object = ScriptableObject.getProperty(paramScriptable, paramString);
    if (object == Scriptable.NOT_FOUND)
      return null; 
    if (object instanceof Function)
      return (Function)object; 
    throw ScriptRuntime.notFunctionError(object, paramString);
  }
  
  private static String getMethodSignature(Method paramMethod, Class<?>[] paramArrayOfClass) {
    StringBuilder stringBuilder = new StringBuilder();
    appendMethodSignature(paramArrayOfClass, paramMethod.getReturnType(), stringBuilder);
    return stringBuilder.toString();
  }
  
  private static ObjToIntMap getObjectFunctionNames(Scriptable paramScriptable) {
    Object[] arrayOfObject = ScriptableObject.getPropertyIds(paramScriptable);
    ObjToIntMap objToIntMap = new ObjToIntMap(arrayOfObject.length);
    for (byte b = 0; b != arrayOfObject.length; b++) {
      if (arrayOfObject[b] instanceof String) {
        String str = (String)arrayOfObject[b];
        Object object = ScriptableObject.getProperty(paramScriptable, str);
        if (object instanceof Function) {
          object = object;
          int i = ScriptRuntime.toInt32(ScriptableObject.getProperty((Scriptable)object, "length"));
          int j = i;
          if (i < 0)
            j = 0; 
          objToIntMap.put(str, j);
        } 
      } 
    } 
    return objToIntMap;
  }
  
  static Method[] getOverridableMethods(Class<?> paramClass) {
    ArrayList<Method> arrayList = new ArrayList();
    HashSet<String> hashSet = new HashSet();
    for (Class<?> clazz = paramClass; clazz != null; clazz = clazz.getSuperclass())
      appendOverridableMethods(clazz, arrayList, hashSet); 
    while (paramClass != null) {
      Class[] arrayOfClass = paramClass.getInterfaces();
      int i = arrayOfClass.length;
      for (byte b = 0; b < i; b++)
        appendOverridableMethods(arrayOfClass[b], arrayList, hashSet); 
      paramClass = paramClass.getSuperclass();
    } 
    return arrayList.<Method>toArray(new Method[arrayList.size()]);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    IdFunctionObject idFunctionObject = new IdFunctionObject(new JavaAdapter(), FTAG, 1, "JavaAdapter", 1, paramScriptable);
    idFunctionObject.markAsConstructor((Scriptable)null);
    if (paramBoolean)
      idFunctionObject.sealObject(); 
    idFunctionObject.exportAsScopeProperty();
  }
  
  static Object js_createAdapter(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    int i = paramArrayOfObject.length;
    if (i != 0) {
      int j = 0;
      while (j < i - 1) {
        Object object = paramArrayOfObject[j];
        if (object instanceof NativeObject)
          break; 
        if (object instanceof NativeJavaClass) {
          j++;
          continue;
        } 
        throw ScriptRuntime.typeError2("msg.not.java.class.arg", String.valueOf(j), ScriptRuntime.toString(object));
      } 
      Class<?> clazz = null;
      Class[] arrayOfClass1 = new Class[j];
      byte b = 0;
      int k;
      for (k = 0; k < j; k++) {
        Class<?> clazz1 = ((NativeJavaClass)paramArrayOfObject[k]).getClassObject();
        if (!clazz1.isInterface()) {
          if (clazz == null) {
            clazz = clazz1;
          } else {
            throw ScriptRuntime.typeError2("msg.only.one.super", clazz.getName(), clazz1.getName());
          } 
        } else {
          arrayOfClass1[b] = clazz1;
          b++;
        } 
      } 
      if (clazz == null)
        clazz = ScriptRuntime.ObjectClass; 
      Class[] arrayOfClass2 = new Class[b];
      System.arraycopy(arrayOfClass1, 0, arrayOfClass2, 0, b);
      Scriptable scriptable = ScriptableObject.ensureScriptable(paramArrayOfObject[j]);
      clazz = getAdapterClass(paramScriptable, clazz, arrayOfClass2, scriptable);
      k = i - j - 1;
      if (k > 0) {
        try {
          Object[] arrayOfObject = new Object[k + 2];
          arrayOfObject[0] = scriptable;
          arrayOfObject[1] = paramContext.getFactory();
          System.arraycopy(paramArrayOfObject, j + 1, arrayOfObject, 2, k);
          scriptable = new NativeJavaClass();
          super(paramScriptable, clazz, true);
          scriptable = ((NativeJavaClass)scriptable).members.ctors;
          try {
            j = scriptable.findCachedFunction(paramContext, arrayOfObject);
            if (j >= 0) {
              try {
                object1 = NativeJavaClass.constructInternal(arrayOfObject, ((NativeJavaMethod)scriptable).methods[j]);
              } catch (Exception null) {}
            } else {
              String str = NativeJavaMethod.scriptSignature(paramArrayOfObject);
              try {
                throw Context.reportRuntimeError2("msg.no.java.ctor", clazz.getName(), str);
              } catch (Exception null) {}
              throw Context.throwAsScriptRuntimeEx(object1);
            } 
          } catch (Exception null) {}
        } catch (Exception object1) {}
      } else {
        Class<Scriptable> clazz2 = ScriptRuntime.ScriptableClass;
        Class<?> clazz1 = ScriptRuntime.ContextFactoryClass;
        object1 = object1.getFactory();
        object1 = clazz.getConstructor(new Class[] { clazz2, clazz1 }).newInstance(new Object[] { scriptable, object1 });
      } 
      Object object2 = getAdapterSelf(clazz, object1);
      if (object2 instanceof Wrapper) {
        object1 = ((Wrapper)object2).unwrap();
        if (object1 instanceof Scriptable) {
          if (object1 instanceof ScriptableObject)
            ScriptRuntime.setObjectProtoAndParent((ScriptableObject)object1, paramScriptable); 
          return object1;
        } 
      } 
      return object2;
    } 
    throw ScriptRuntime.typeError0("msg.adapter.zero.args");
  }
  
  static Class<?> loadAdapterClass(String paramString, byte[] paramArrayOfbyte) {
    Class<?> clazz2 = SecurityController.getStaticSecurityDomainClass();
    if (clazz2 == CodeSource.class || clazz2 == ProtectionDomain.class) {
      CodeSource codeSource;
      ProtectionDomain protectionDomain2 = SecurityUtilities.getScriptProtectionDomain();
      ProtectionDomain protectionDomain1 = protectionDomain2;
      if (protectionDomain2 == null)
        protectionDomain1 = JavaAdapter.class.getProtectionDomain(); 
      if (clazz2 == CodeSource.class)
        if (protectionDomain1 == null) {
          protectionDomain1 = null;
        } else {
          codeSource = protectionDomain1.getCodeSource();
        }  
      GeneratedClassLoader generatedClassLoader1 = SecurityController.createLoader(null, codeSource);
      clazz1 = generatedClassLoader1.defineClass(paramString, paramArrayOfbyte);
      generatedClassLoader1.linkClass(clazz1);
      return clazz1;
    } 
    GeneratedClassLoader generatedClassLoader = null;
    generatedClassLoader = SecurityController.createLoader(null, generatedClassLoader);
    Class<?> clazz1 = generatedClassLoader.defineClass((String)clazz1, paramArrayOfbyte);
    generatedClassLoader.linkClass(clazz1);
    return clazz1;
  }
  
  public static Object readAdapterObject(Scriptable paramScriptable, ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    Context context = Context.getCurrentContext();
    if (context != null) {
      ContextFactory contextFactory = context.getFactory();
    } else {
      context = null;
    } 
    Class<?> clazz1 = Class.forName((String)paramObjectInputStream.readObject());
    String[] arrayOfString = (String[])paramObjectInputStream.readObject();
    Class[] arrayOfClass = new Class[arrayOfString.length];
    for (byte b = 0; b < arrayOfString.length; b++)
      arrayOfClass[b] = Class.forName(arrayOfString[b]); 
    Scriptable scriptable = (Scriptable)paramObjectInputStream.readObject();
    Class<?> clazz3 = getAdapterClass(paramScriptable, clazz1, arrayOfClass, scriptable);
    Class<?> clazz2 = ScriptRuntime.ContextFactoryClass;
    Class<Scriptable> clazz = ScriptRuntime.ScriptableClass;
    clazz1 = ScriptRuntime.ScriptableClass;
    try {
      return clazz3.getConstructor(new Class[] { clazz2, clazz, clazz1 }).newInstance(new Object[] { context, scriptable, paramScriptable });
    } catch (InstantiationException instantiationException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (NoSuchMethodException noSuchMethodException) {}
    throw new ClassNotFoundException("adapter");
  }
  
  public static Scriptable runScript(Script paramScript) {
    return ContextFactory.getGlobal().<Scriptable>call(paramContext -> {
          ScriptableObject scriptableObject = ScriptRuntime.getGlobal(paramContext);
          paramScript.exec(paramContext, scriptableObject);
          return (ContextAction)scriptableObject;
        });
  }
  
  public static void writeAdapterObject(Object paramObject, ObjectOutputStream paramObjectOutputStream) throws IOException {
    Class<?> clazz = paramObject.getClass();
    paramObjectOutputStream.writeObject(clazz.getSuperclass().getName());
    Class[] arrayOfClass = clazz.getInterfaces();
    String[] arrayOfString = new String[arrayOfClass.length];
    for (byte b = 0; b < arrayOfClass.length; b++)
      arrayOfString[b] = arrayOfClass[b].getName(); 
    paramObjectOutputStream.writeObject(arrayOfString);
    try {
      paramObjectOutputStream.writeObject(clazz.getField("delegee").get(paramObject));
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (NoSuchFieldException noSuchFieldException) {}
    throw new IOException();
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (paramIdFunctionObject.hasTag(FTAG) && paramIdFunctionObject.methodId() == 1)
      return js_createAdapter(paramContext, paramScriptable1, paramArrayOfObject); 
    throw paramIdFunctionObject.unknown();
  }
  
  static class JavaAdapterSignature {
    Class<?>[] interfaces;
    
    ObjToIntMap names;
    
    Class<?> superClass;
    
    JavaAdapterSignature(Class<?> param1Class, Class<?>[] param1ArrayOfClass, ObjToIntMap param1ObjToIntMap) {
      this.superClass = param1Class;
      this.interfaces = param1ArrayOfClass;
      this.names = param1ObjToIntMap;
    }
    
    public boolean equals(Object param1Object) {
      if (!(param1Object instanceof JavaAdapterSignature))
        return false; 
      param1Object = param1Object;
      if (this.superClass != ((JavaAdapterSignature)param1Object).superClass)
        return false; 
      Class<?>[] arrayOfClass1 = this.interfaces;
      Class<?>[] arrayOfClass2 = ((JavaAdapterSignature)param1Object).interfaces;
      if (arrayOfClass1 != arrayOfClass2) {
        if (arrayOfClass1.length != arrayOfClass2.length)
          return false; 
        byte b = 0;
        while (true) {
          arrayOfClass2 = this.interfaces;
          if (b < arrayOfClass2.length) {
            if (arrayOfClass2[b] != ((JavaAdapterSignature)param1Object).interfaces[b])
              return false; 
            b++;
            continue;
          } 
          break;
        } 
      } 
      if (this.names.size() != ((JavaAdapterSignature)param1Object).names.size())
        return false; 
      ObjToIntMap.Iterator iterator = new ObjToIntMap.Iterator(this.names);
      iterator.start();
      while (!iterator.done()) {
        String str = (String)iterator.getKey();
        int i = iterator.getValue();
        if (i != ((JavaAdapterSignature)param1Object).names.get(str, i + 1))
          return false; 
        iterator.next();
      } 
      return true;
    }
    
    public int hashCode() {
      return this.superClass.hashCode() + Arrays.hashCode((Object[])this.interfaces) ^ this.names.size();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/JavaAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */