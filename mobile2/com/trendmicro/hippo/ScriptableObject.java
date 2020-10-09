package com.trendmicro.hippo;

import com.trendmicro.hippo.annotations.JSSetter;
import com.trendmicro.hippo.debug.DebuggableObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public abstract class ScriptableObject implements Scriptable, SymbolScriptable, Serializable, DebuggableObject, ConstProperties {
  public static final int CONST = 13;
  
  public static final int DONTENUM = 2;
  
  public static final int EMPTY = 0;
  
  private static final Method GET_ARRAY_LENGTH;
  
  private static final Comparator<Object> KEY_COMPARATOR;
  
  public static final int PERMANENT = 4;
  
  public static final int READONLY = 1;
  
  public static final int UNINITIALIZED_CONST = 8;
  
  private static final long serialVersionUID = 2829861078851942586L;
  
  private volatile Map<Object, Object> associatedValues;
  
  private transient ExternalArrayData externalData;
  
  private boolean isExtensible = true;
  
  private boolean isSealed = false;
  
  private Scriptable parentScopeObject;
  
  private Scriptable prototypeObject;
  
  private transient SlotMapContainer slotMap;
  
  static {
    try {
      GET_ARRAY_LENGTH = ScriptableObject.class.getMethod("getExternalArrayLength", new Class[0]);
      KEY_COMPARATOR = new KeyComparator();
      return;
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new RuntimeException(noSuchMethodException);
    } 
  }
  
  public ScriptableObject() {
    this.slotMap = createSlotMap(0);
  }
  
  public ScriptableObject(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    if (paramScriptable1 != null) {
      this.parentScopeObject = paramScriptable1;
      this.prototypeObject = paramScriptable2;
      this.slotMap = createSlotMap(0);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  static <T extends Scriptable> BaseFunction buildClassCtor(Scriptable paramScriptable, Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic getMethodList : (Ljava/lang/Class;)[Ljava/lang/reflect/Method;
    //   4: astore #4
    //   6: iconst_0
    //   7: istore #5
    //   9: iload #5
    //   11: aload #4
    //   13: arraylength
    //   14: if_icmpge -> 192
    //   17: aload #4
    //   19: iload #5
    //   21: aaload
    //   22: astore #6
    //   24: aload #6
    //   26: invokevirtual getName : ()Ljava/lang/String;
    //   29: ldc 'init'
    //   31: invokevirtual equals : (Ljava/lang/Object;)Z
    //   34: ifne -> 40
    //   37: goto -> 186
    //   40: aload #6
    //   42: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   45: astore #7
    //   47: aload #7
    //   49: arraylength
    //   50: iconst_3
    //   51: if_icmpne -> 141
    //   54: aload #7
    //   56: iconst_0
    //   57: aaload
    //   58: getstatic com/trendmicro/hippo/ScriptRuntime.ContextClass : Ljava/lang/Class;
    //   61: if_acmpne -> 141
    //   64: aload #7
    //   66: iconst_1
    //   67: aaload
    //   68: getstatic com/trendmicro/hippo/ScriptRuntime.ScriptableClass : Ljava/lang/Class;
    //   71: if_acmpne -> 141
    //   74: aload #7
    //   76: iconst_2
    //   77: aaload
    //   78: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   81: if_acmpne -> 141
    //   84: aload #6
    //   86: invokevirtual getModifiers : ()I
    //   89: invokestatic isStatic : (I)Z
    //   92: ifeq -> 141
    //   95: invokestatic getContext : ()Lcom/trendmicro/hippo/Context;
    //   98: astore #7
    //   100: iload_2
    //   101: ifeq -> 111
    //   104: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   107: astore_1
    //   108: goto -> 115
    //   111: getstatic java/lang/Boolean.FALSE : Ljava/lang/Boolean;
    //   114: astore_1
    //   115: aload #6
    //   117: aconst_null
    //   118: iconst_3
    //   119: anewarray java/lang/Object
    //   122: dup
    //   123: iconst_0
    //   124: aload #7
    //   126: aastore
    //   127: dup
    //   128: iconst_1
    //   129: aload_0
    //   130: aastore
    //   131: dup
    //   132: iconst_2
    //   133: aload_1
    //   134: aastore
    //   135: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   138: pop
    //   139: aconst_null
    //   140: areturn
    //   141: aload #7
    //   143: arraylength
    //   144: iconst_1
    //   145: if_icmpne -> 186
    //   148: aload #7
    //   150: iconst_0
    //   151: aaload
    //   152: getstatic com/trendmicro/hippo/ScriptRuntime.ScriptableClass : Ljava/lang/Class;
    //   155: if_acmpne -> 186
    //   158: aload #6
    //   160: invokevirtual getModifiers : ()I
    //   163: invokestatic isStatic : (I)Z
    //   166: ifeq -> 186
    //   169: aload #6
    //   171: aconst_null
    //   172: iconst_1
    //   173: anewarray java/lang/Object
    //   176: dup
    //   177: iconst_0
    //   178: aload_0
    //   179: aastore
    //   180: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   183: pop
    //   184: aconst_null
    //   185: areturn
    //   186: iinc #5, 1
    //   189: goto -> 9
    //   192: aload_1
    //   193: invokevirtual getConstructors : ()[Ljava/lang/reflect/Constructor;
    //   196: astore #8
    //   198: aconst_null
    //   199: astore #7
    //   201: iconst_0
    //   202: istore #5
    //   204: aload #7
    //   206: astore #6
    //   208: iload #5
    //   210: aload #8
    //   212: arraylength
    //   213: if_icmpge -> 244
    //   216: aload #8
    //   218: iload #5
    //   220: aaload
    //   221: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   224: arraylength
    //   225: ifne -> 238
    //   228: aload #8
    //   230: iload #5
    //   232: aaload
    //   233: astore #6
    //   235: goto -> 244
    //   238: iinc #5, 1
    //   241: goto -> 204
    //   244: aload #6
    //   246: ifnull -> 1282
    //   249: aload #6
    //   251: getstatic com/trendmicro/hippo/ScriptRuntime.emptyArgs : [Ljava/lang/Object;
    //   254: invokevirtual newInstance : ([Ljava/lang/Object;)Ljava/lang/Object;
    //   257: checkcast com/trendmicro/hippo/Scriptable
    //   260: astore #9
    //   262: aload #9
    //   264: invokeinterface getClassName : ()Ljava/lang/String;
    //   269: astore #10
    //   271: aload_0
    //   272: invokestatic getTopLevelScope : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   275: aload #10
    //   277: invokestatic getProperty : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Ljava/lang/Object;
    //   280: astore #11
    //   282: aload #11
    //   284: instanceof com/trendmicro/hippo/BaseFunction
    //   287: ifeq -> 326
    //   290: aload #11
    //   292: checkcast com/trendmicro/hippo/BaseFunction
    //   295: invokevirtual getPrototypeProperty : ()Ljava/lang/Object;
    //   298: astore #6
    //   300: aload #6
    //   302: ifnull -> 323
    //   305: aload_1
    //   306: aload #6
    //   308: invokevirtual getClass : ()Ljava/lang/Class;
    //   311: invokevirtual equals : (Ljava/lang/Object;)Z
    //   314: ifeq -> 326
    //   317: aload #11
    //   319: checkcast com/trendmicro/hippo/BaseFunction
    //   322: areturn
    //   323: goto -> 326
    //   326: aconst_null
    //   327: astore #7
    //   329: aload #7
    //   331: astore #6
    //   333: iload_3
    //   334: ifeq -> 403
    //   337: aload_1
    //   338: invokevirtual getSuperclass : ()Ljava/lang/Class;
    //   341: astore #12
    //   343: aload #7
    //   345: astore #6
    //   347: getstatic com/trendmicro/hippo/ScriptRuntime.ScriptableClass : Ljava/lang/Class;
    //   350: aload #12
    //   352: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   355: ifeq -> 403
    //   358: aload #7
    //   360: astore #6
    //   362: aload #12
    //   364: invokevirtual getModifiers : ()I
    //   367: invokestatic isAbstract : (I)Z
    //   370: ifne -> 403
    //   373: aload_0
    //   374: aload #12
    //   376: invokestatic extendsScriptable : (Ljava/lang/Class;)Ljava/lang/Class;
    //   379: iload_2
    //   380: iload_3
    //   381: invokestatic defineClass : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Class;ZZ)Ljava/lang/String;
    //   384: astore #12
    //   386: aload #7
    //   388: astore #6
    //   390: aload #12
    //   392: ifnull -> 403
    //   395: aload_0
    //   396: aload #12
    //   398: invokestatic getClassPrototype : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Lcom/trendmicro/hippo/Scriptable;
    //   401: astore #6
    //   403: aload #6
    //   405: astore #12
    //   407: aload #6
    //   409: ifnonnull -> 418
    //   412: aload_0
    //   413: invokestatic getObjectPrototype : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   416: astore #12
    //   418: aload #9
    //   420: aload #12
    //   422: invokeinterface setPrototype : (Lcom/trendmicro/hippo/Scriptable;)V
    //   427: ldc 'jsStaticFunction_'
    //   429: astore #13
    //   431: ldc 'jsGet_'
    //   433: astore #14
    //   435: aload #4
    //   437: ldc com/trendmicro/hippo/annotations/JSConstructor
    //   439: invokestatic findAnnotatedMember : ([Ljava/lang/reflect/AccessibleObject;Ljava/lang/Class;)Ljava/lang/reflect/Member;
    //   442: astore #7
    //   444: aload #7
    //   446: astore #6
    //   448: aload #7
    //   450: ifnonnull -> 462
    //   453: aload #8
    //   455: ldc com/trendmicro/hippo/annotations/JSConstructor
    //   457: invokestatic findAnnotatedMember : ([Ljava/lang/reflect/AccessibleObject;Ljava/lang/Class;)Ljava/lang/reflect/Member;
    //   460: astore #6
    //   462: ldc_w 'jsConstructor'
    //   465: astore #15
    //   467: aload #6
    //   469: astore #7
    //   471: aload #6
    //   473: ifnonnull -> 486
    //   476: aload #4
    //   478: ldc_w 'jsConstructor'
    //   481: invokestatic findSingleMethod : ([Ljava/lang/reflect/Method;Ljava/lang/String;)Ljava/lang/reflect/Method;
    //   484: astore #7
    //   486: aload #7
    //   488: ifnonnull -> 582
    //   491: aload #8
    //   493: arraylength
    //   494: iconst_1
    //   495: if_icmpne -> 507
    //   498: aload #8
    //   500: iconst_0
    //   501: aaload
    //   502: astore #6
    //   504: goto -> 559
    //   507: aload #7
    //   509: astore #6
    //   511: aload #8
    //   513: arraylength
    //   514: iconst_2
    //   515: if_icmpne -> 559
    //   518: aload #8
    //   520: iconst_0
    //   521: aaload
    //   522: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   525: arraylength
    //   526: ifne -> 538
    //   529: aload #8
    //   531: iconst_1
    //   532: aaload
    //   533: astore #6
    //   535: goto -> 559
    //   538: aload #7
    //   540: astore #6
    //   542: aload #8
    //   544: iconst_1
    //   545: aaload
    //   546: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   549: arraylength
    //   550: ifne -> 559
    //   553: aload #8
    //   555: iconst_0
    //   556: aaload
    //   557: astore #6
    //   559: aload #6
    //   561: ifnull -> 571
    //   564: aload #6
    //   566: astore #7
    //   568: goto -> 582
    //   571: ldc_w 'msg.ctor.multiple.parms'
    //   574: aload_1
    //   575: invokevirtual getName : ()Ljava/lang/String;
    //   578: invokestatic reportRuntimeError1 : (Ljava/lang/String;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   581: athrow
    //   582: new com/trendmicro/hippo/FunctionObject
    //   585: dup
    //   586: aload #10
    //   588: aload #7
    //   590: aload_0
    //   591: invokespecial <init> : (Ljava/lang/String;Ljava/lang/reflect/Member;Lcom/trendmicro/hippo/Scriptable;)V
    //   594: astore #16
    //   596: aload #16
    //   598: invokevirtual isVarArgsMethod : ()Z
    //   601: ifne -> 1268
    //   604: aload #16
    //   606: aload_0
    //   607: aload #9
    //   609: invokevirtual initAsConstructor : (Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;)V
    //   612: new java/util/HashSet
    //   615: dup
    //   616: invokespecial <init> : ()V
    //   619: astore #17
    //   621: new java/util/HashSet
    //   624: dup
    //   625: invokespecial <init> : ()V
    //   628: astore #18
    //   630: aload #4
    //   632: arraylength
    //   633: istore #19
    //   635: aconst_null
    //   636: astore #8
    //   638: iconst_0
    //   639: istore #5
    //   641: iload #5
    //   643: iload #19
    //   645: if_icmpge -> 1210
    //   648: aload #4
    //   650: iload #5
    //   652: aaload
    //   653: astore #20
    //   655: aload #20
    //   657: aload #7
    //   659: if_acmpne -> 668
    //   662: aload #8
    //   664: astore_1
    //   665: goto -> 1173
    //   668: aload #20
    //   670: invokevirtual getName : ()Ljava/lang/String;
    //   673: astore #21
    //   675: aload #21
    //   677: ldc_w 'finishInit'
    //   680: invokevirtual equals : (Ljava/lang/Object;)Z
    //   683: ifeq -> 741
    //   686: aload #20
    //   688: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   691: astore_1
    //   692: aload_1
    //   693: arraylength
    //   694: iconst_3
    //   695: if_icmpne -> 741
    //   698: aload_1
    //   699: iconst_0
    //   700: aaload
    //   701: getstatic com/trendmicro/hippo/ScriptRuntime.ScriptableClass : Ljava/lang/Class;
    //   704: if_acmpne -> 741
    //   707: aload_1
    //   708: iconst_1
    //   709: aaload
    //   710: ldc com/trendmicro/hippo/FunctionObject
    //   712: if_acmpne -> 741
    //   715: aload_1
    //   716: iconst_2
    //   717: aaload
    //   718: getstatic com/trendmicro/hippo/ScriptRuntime.ScriptableClass : Ljava/lang/Class;
    //   721: if_acmpne -> 741
    //   724: aload #20
    //   726: invokevirtual getModifiers : ()I
    //   729: invokestatic isStatic : (I)Z
    //   732: ifeq -> 741
    //   735: aload #20
    //   737: astore_1
    //   738: goto -> 1173
    //   741: aload #21
    //   743: bipush #36
    //   745: invokevirtual indexOf : (I)I
    //   748: iconst_m1
    //   749: if_icmpeq -> 758
    //   752: aload #8
    //   754: astore_1
    //   755: goto -> 1173
    //   758: aload #21
    //   760: aload #15
    //   762: invokevirtual equals : (Ljava/lang/Object;)Z
    //   765: ifeq -> 774
    //   768: aload #8
    //   770: astore_1
    //   771: goto -> 1173
    //   774: aconst_null
    //   775: astore_1
    //   776: aconst_null
    //   777: astore #6
    //   779: aload #20
    //   781: ldc_w com/trendmicro/hippo/annotations/JSFunction
    //   784: invokevirtual isAnnotationPresent : (Ljava/lang/Class;)Z
    //   787: ifeq -> 802
    //   790: aload #20
    //   792: ldc_w com/trendmicro/hippo/annotations/JSFunction
    //   795: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   798: astore_1
    //   799: goto -> 865
    //   802: aload #20
    //   804: ldc_w com/trendmicro/hippo/annotations/JSStaticFunction
    //   807: invokevirtual isAnnotationPresent : (Ljava/lang/Class;)Z
    //   810: ifeq -> 825
    //   813: aload #20
    //   815: ldc_w com/trendmicro/hippo/annotations/JSStaticFunction
    //   818: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   821: astore_1
    //   822: goto -> 865
    //   825: aload #20
    //   827: ldc_w com/trendmicro/hippo/annotations/JSGetter
    //   830: invokevirtual isAnnotationPresent : (Ljava/lang/Class;)Z
    //   833: ifeq -> 848
    //   836: aload #20
    //   838: ldc_w com/trendmicro/hippo/annotations/JSGetter
    //   841: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   844: astore_1
    //   845: goto -> 865
    //   848: aload #20
    //   850: ldc_w com/trendmicro/hippo/annotations/JSSetter
    //   853: invokevirtual isAnnotationPresent : (Ljava/lang/Class;)Z
    //   856: ifeq -> 865
    //   859: aload #8
    //   861: astore_1
    //   862: goto -> 1173
    //   865: aload_1
    //   866: ifnonnull -> 928
    //   869: aload #21
    //   871: ldc_w 'jsFunction_'
    //   874: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   877: ifeq -> 888
    //   880: ldc_w 'jsFunction_'
    //   883: astore #6
    //   885: goto -> 928
    //   888: aload #21
    //   890: ldc 'jsStaticFunction_'
    //   892: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   895: ifeq -> 905
    //   898: ldc 'jsStaticFunction_'
    //   900: astore #6
    //   902: goto -> 928
    //   905: aload #21
    //   907: ldc 'jsGet_'
    //   909: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   912: ifeq -> 922
    //   915: ldc 'jsGet_'
    //   917: astore #6
    //   919: goto -> 928
    //   922: aload #8
    //   924: astore_1
    //   925: goto -> 1173
    //   928: aload_1
    //   929: instanceof com/trendmicro/hippo/annotations/JSStaticFunction
    //   932: ifne -> 951
    //   935: aload #6
    //   937: ldc 'jsStaticFunction_'
    //   939: if_acmpne -> 945
    //   942: goto -> 951
    //   945: iconst_0
    //   946: istore #22
    //   948: goto -> 954
    //   951: iconst_1
    //   952: istore #22
    //   954: iload #22
    //   956: ifeq -> 966
    //   959: aload #17
    //   961: astore #23
    //   963: goto -> 970
    //   966: aload #18
    //   968: astore #23
    //   970: aload #21
    //   972: aload #6
    //   974: aload_1
    //   975: invokestatic getPropertyName : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/annotation/Annotation;)Ljava/lang/String;
    //   978: astore #24
    //   980: aload #23
    //   982: aload #24
    //   984: invokevirtual contains : (Ljava/lang/Object;)Z
    //   987: ifne -> 1199
    //   990: aload #23
    //   992: aload #24
    //   994: invokevirtual add : (Ljava/lang/Object;)Z
    //   997: pop
    //   998: aload_1
    //   999: instanceof com/trendmicro/hippo/annotations/JSGetter
    //   1002: ifne -> 1119
    //   1005: aload #6
    //   1007: ldc 'jsGet_'
    //   1009: if_acmpne -> 1015
    //   1012: goto -> 1119
    //   1015: iload #22
    //   1017: ifeq -> 1041
    //   1020: aload #20
    //   1022: invokevirtual getModifiers : ()I
    //   1025: invokestatic isStatic : (I)Z
    //   1028: ifeq -> 1034
    //   1031: goto -> 1041
    //   1034: ldc_w 'jsStaticFunction must be used with static method.'
    //   1037: invokestatic reportRuntimeError : (Ljava/lang/String;)Lcom/trendmicro/hippo/EvaluatorException;
    //   1040: athrow
    //   1041: new com/trendmicro/hippo/FunctionObject
    //   1044: dup
    //   1045: aload #24
    //   1047: aload #20
    //   1049: aload #9
    //   1051: invokespecial <init> : (Ljava/lang/String;Ljava/lang/reflect/Member;Lcom/trendmicro/hippo/Scriptable;)V
    //   1054: astore #6
    //   1056: aload #6
    //   1058: invokevirtual isVarArgsConstructor : ()Z
    //   1061: ifne -> 1105
    //   1064: iload #22
    //   1066: ifeq -> 1075
    //   1069: aload #16
    //   1071: astore_1
    //   1072: goto -> 1078
    //   1075: aload #9
    //   1077: astore_1
    //   1078: aload_1
    //   1079: aload #24
    //   1081: aload #6
    //   1083: iconst_2
    //   1084: invokestatic defineProperty : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Ljava/lang/Object;I)V
    //   1087: aload #8
    //   1089: astore_1
    //   1090: iload_2
    //   1091: ifeq -> 1173
    //   1094: aload #6
    //   1096: invokevirtual sealObject : ()V
    //   1099: aload #8
    //   1101: astore_1
    //   1102: goto -> 1173
    //   1105: ldc_w 'msg.varargs.fun'
    //   1108: aload #7
    //   1110: invokeinterface getName : ()Ljava/lang/String;
    //   1115: invokestatic reportRuntimeError1 : (Ljava/lang/String;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   1118: athrow
    //   1119: aload #9
    //   1121: instanceof com/trendmicro/hippo/ScriptableObject
    //   1124: ifeq -> 1182
    //   1127: aload #4
    //   1129: aload #24
    //   1131: ldc_w 'jsSet_'
    //   1134: invokestatic findSetterMethod : ([Ljava/lang/reflect/Method;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/reflect/Method;
    //   1137: astore_1
    //   1138: aload_1
    //   1139: ifnull -> 1148
    //   1142: iconst_0
    //   1143: istore #22
    //   1145: goto -> 1151
    //   1148: iconst_1
    //   1149: istore #22
    //   1151: aload #9
    //   1153: checkcast com/trendmicro/hippo/ScriptableObject
    //   1156: aload #24
    //   1158: aconst_null
    //   1159: aload #20
    //   1161: aload_1
    //   1162: iload #22
    //   1164: bipush #6
    //   1166: ior
    //   1167: invokevirtual defineProperty : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;I)V
    //   1170: aload #8
    //   1172: astore_1
    //   1173: iinc #5, 1
    //   1176: aload_1
    //   1177: astore #8
    //   1179: goto -> 641
    //   1182: ldc_w 'msg.extend.scriptable'
    //   1185: aload #9
    //   1187: invokevirtual getClass : ()Ljava/lang/Class;
    //   1190: invokevirtual toString : ()Ljava/lang/String;
    //   1193: aload #24
    //   1195: invokestatic reportRuntimeError2 : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   1198: athrow
    //   1199: ldc_w 'duplicate.defineClass.name'
    //   1202: aload #21
    //   1204: aload #24
    //   1206: invokestatic reportRuntimeError2 : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   1209: athrow
    //   1210: aload #8
    //   1212: ifnull -> 1240
    //   1215: aload #8
    //   1217: aconst_null
    //   1218: iconst_3
    //   1219: anewarray java/lang/Object
    //   1222: dup
    //   1223: iconst_0
    //   1224: aload_0
    //   1225: aastore
    //   1226: dup
    //   1227: iconst_1
    //   1228: aload #16
    //   1230: aastore
    //   1231: dup
    //   1232: iconst_2
    //   1233: aload #9
    //   1235: aastore
    //   1236: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   1239: pop
    //   1240: iload_2
    //   1241: ifeq -> 1265
    //   1244: aload #16
    //   1246: invokevirtual sealObject : ()V
    //   1249: aload #9
    //   1251: instanceof com/trendmicro/hippo/ScriptableObject
    //   1254: ifeq -> 1265
    //   1257: aload #9
    //   1259: checkcast com/trendmicro/hippo/ScriptableObject
    //   1262: invokevirtual sealObject : ()V
    //   1265: aload #16
    //   1267: areturn
    //   1268: ldc_w 'msg.varargs.ctor'
    //   1271: aload #7
    //   1273: invokeinterface getName : ()Ljava/lang/String;
    //   1278: invokestatic reportRuntimeError1 : (Ljava/lang/String;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   1281: athrow
    //   1282: ldc_w 'msg.zero.arg.ctor'
    //   1285: aload_1
    //   1286: invokevirtual getName : ()Ljava/lang/String;
    //   1289: invokestatic reportRuntimeError1 : (Ljava/lang/String;Ljava/lang/Object;)Lcom/trendmicro/hippo/EvaluatorException;
    //   1292: athrow
  }
  
  protected static ScriptableObject buildDataDescriptor(Scriptable paramScriptable, Object paramObject, int paramInt) {
    boolean bool2;
    NativeObject nativeObject = new NativeObject();
    ScriptRuntime.setBuiltinProtoAndParent(nativeObject, paramScriptable, TopLevel.Builtins.Object);
    nativeObject.defineProperty("value", paramObject, 0);
    boolean bool1 = true;
    if ((paramInt & 0x1) == 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    nativeObject.defineProperty("writable", Boolean.valueOf(bool2), 0);
    if ((paramInt & 0x2) == 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    nativeObject.defineProperty("enumerable", Boolean.valueOf(bool2), 0);
    if ((paramInt & 0x4) == 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    nativeObject.defineProperty("configurable", Boolean.valueOf(bool2), 0);
    return nativeObject;
  }
  
  public static Object callMethod(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject) {
    Function function;
    Object object = getProperty(paramScriptable, paramString);
    if (object instanceof Function) {
      function = (Function)object;
      object = getTopLevelScope(paramScriptable);
      return (paramContext != null) ? function.call(paramContext, (Scriptable)object, paramScriptable, paramArrayOfObject) : Context.call(null, function, (Scriptable)object, paramScriptable, paramArrayOfObject);
    } 
    throw ScriptRuntime.notFunctionError(paramScriptable, function);
  }
  
  public static Object callMethod(Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject) {
    return callMethod(null, paramScriptable, paramString, paramArrayOfObject);
  }
  
  private void checkNotSealed(Object paramObject, int paramInt) {
    if (!isSealed())
      return; 
    if (paramObject != null) {
      paramObject = paramObject.toString();
    } else {
      paramObject = Integer.toString(paramInt);
    } 
    throw Context.reportRuntimeError1("msg.modify.sealed", paramObject);
  }
  
  static void checkValidAttributes(int paramInt) {
    if ((paramInt & 0xFFFFFFF0) == 0)
      return; 
    throw new IllegalArgumentException(String.valueOf(paramInt));
  }
  
  private SlotMapContainer createSlotMap(int paramInt) {
    Context context = Context.getCurrentContext();
    return (context != null && context.hasFeature(17)) ? new ThreadSafeSlotMapContainer(paramInt) : new SlotMapContainer(paramInt);
  }
  
  public static <T extends Scriptable> String defineClass(Scriptable paramScriptable, Class<T> paramClass, boolean paramBoolean1, boolean paramBoolean2) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    BaseFunction baseFunction = buildClassCtor(paramScriptable, paramClass, paramBoolean1, paramBoolean2);
    if (baseFunction == null)
      return null; 
    String str = baseFunction.getClassPrototype().getClassName();
    defineProperty(paramScriptable, str, baseFunction, 2);
    return str;
  }
  
  public static <T extends Scriptable> void defineClass(Scriptable paramScriptable, Class<T> paramClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    defineClass(paramScriptable, paramClass, false, false);
  }
  
  public static <T extends Scriptable> void defineClass(Scriptable paramScriptable, Class<T> paramClass, boolean paramBoolean) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    defineClass(paramScriptable, paramClass, paramBoolean, false);
  }
  
  public static void defineConstProperty(Scriptable paramScriptable, String paramString) {
    if (paramScriptable instanceof ConstProperties) {
      ((ConstProperties)paramScriptable).defineConst(paramString, paramScriptable);
    } else {
      defineProperty(paramScriptable, paramString, Undefined.instance, 13);
    } 
  }
  
  public static void defineProperty(Scriptable paramScriptable, String paramString, Object paramObject, int paramInt) {
    if (!(paramScriptable instanceof ScriptableObject)) {
      paramScriptable.put(paramString, paramScriptable, paramObject);
      return;
    } 
    ((ScriptableObject)paramScriptable).defineProperty(paramString, paramObject, paramInt);
  }
  
  public static boolean deleteProperty(Scriptable paramScriptable, int paramInt) {
    Scriptable scriptable = getBase(paramScriptable, paramInt);
    if (scriptable == null)
      return true; 
    scriptable.delete(paramInt);
    return true ^ scriptable.has(paramInt, paramScriptable);
  }
  
  public static boolean deleteProperty(Scriptable paramScriptable, String paramString) {
    Scriptable scriptable = getBase(paramScriptable, paramString);
    if (scriptable == null)
      return true; 
    scriptable.delete(paramString);
    return true ^ scriptable.has(paramString, paramScriptable);
  }
  
  protected static Scriptable ensureScriptable(Object paramObject) {
    if (paramObject instanceof Scriptable)
      return (Scriptable)paramObject; 
    throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(paramObject));
  }
  
  protected static ScriptableObject ensureScriptableObject(Object paramObject) {
    if (paramObject instanceof ScriptableObject)
      return (ScriptableObject)paramObject; 
    throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(paramObject));
  }
  
  protected static SymbolScriptable ensureSymbolScriptable(Object paramObject) {
    if (paramObject instanceof SymbolScriptable)
      return (SymbolScriptable)paramObject; 
    throw ScriptRuntime.typeError1("msg.object.not.symbolscriptable", ScriptRuntime.typeof(paramObject));
  }
  
  private static <T extends Scriptable> Class<T> extendsScriptable(Class<?> paramClass) {
    return (Class)(ScriptRuntime.ScriptableClass.isAssignableFrom(paramClass) ? paramClass : null);
  }
  
  private static Member findAnnotatedMember(AccessibleObject[] paramArrayOfAccessibleObject, Class<? extends Annotation> paramClass) {
    int i = paramArrayOfAccessibleObject.length;
    for (byte b = 0; b < i; b++) {
      AccessibleObject accessibleObject = paramArrayOfAccessibleObject[b];
      if (accessibleObject.isAnnotationPresent(paramClass))
        return (Member)accessibleObject; 
    } 
    return null;
  }
  
  private Slot findAttributeSlot(Symbol paramSymbol, SlotAccess paramSlotAccess) {
    Slot slot = this.slotMap.get(paramSymbol, 0, paramSlotAccess);
    if (slot != null)
      return slot; 
    throw Context.reportRuntimeError1("msg.prop.not.found", paramSymbol);
  }
  
  private Slot findAttributeSlot(String paramString, int paramInt, SlotAccess paramSlotAccess) {
    Slot slot = this.slotMap.get(paramString, paramInt, paramSlotAccess);
    if (slot == null) {
      if (paramString == null)
        paramString = Integer.toString(paramInt); 
      throw Context.reportRuntimeError1("msg.prop.not.found", paramString);
    } 
    return slot;
  }
  
  private static Method findSetterMethod(Method[] paramArrayOfMethod, String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("set");
    boolean bool = false;
    stringBuilder.append(Character.toUpperCase(paramString1.charAt(0)));
    stringBuilder.append(paramString1.substring(1));
    String str = stringBuilder.toString();
    int i = paramArrayOfMethod.length;
    byte b;
    for (b = 0; b < i; b++) {
      Method method = paramArrayOfMethod[b];
      JSSetter jSSetter = method.<JSSetter>getAnnotation(JSSetter.class);
      if (jSSetter != null && (paramString1.equals(jSSetter.value()) || ("".equals(jSSetter.value()) && str.equals(method.getName()))))
        return method; 
    } 
    stringBuilder = new StringBuilder();
    stringBuilder.append(paramString2);
    stringBuilder.append(paramString1);
    paramString2 = stringBuilder.toString();
    i = paramArrayOfMethod.length;
    for (b = bool; b < i; b++) {
      Method method = paramArrayOfMethod[b];
      if (paramString2.equals(method.getName()))
        return method; 
    } 
    return null;
  }
  
  public static Scriptable getArrayPrototype(Scriptable paramScriptable) {
    return TopLevel.getBuiltinPrototype(getTopLevelScope(paramScriptable), TopLevel.Builtins.Array);
  }
  
  private static Scriptable getBase(Scriptable paramScriptable, int paramInt) {
    while (!paramScriptable.has(paramInt, paramScriptable)) {
      Scriptable scriptable = paramScriptable.getPrototype();
      paramScriptable = scriptable;
      if (scriptable == null) {
        paramScriptable = scriptable;
        break;
      } 
    } 
    return paramScriptable;
  }
  
  private static Scriptable getBase(Scriptable paramScriptable, Symbol paramSymbol) {
    while (!ensureSymbolScriptable(paramScriptable).has(paramSymbol, paramScriptable)) {
      Scriptable scriptable = paramScriptable.getPrototype();
      paramScriptable = scriptable;
      if (scriptable == null) {
        paramScriptable = scriptable;
        break;
      } 
    } 
    return paramScriptable;
  }
  
  private static Scriptable getBase(Scriptable paramScriptable, String paramString) {
    while (!paramScriptable.has(paramString, paramScriptable)) {
      Scriptable scriptable = paramScriptable.getPrototype();
      paramScriptable = scriptable;
      if (scriptable == null) {
        paramScriptable = scriptable;
        break;
      } 
    } 
    return paramScriptable;
  }
  
  public static Scriptable getClassPrototype(Scriptable paramScriptable, String paramString) {
    Object object = getProperty(getTopLevelScope(paramScriptable), paramString);
    if (object instanceof BaseFunction) {
      object = ((BaseFunction)object).getPrototypeProperty();
    } else if (object instanceof Scriptable) {
      object = object;
      object = object.get("prototype", (Scriptable)object);
    } else {
      return null;
    } 
    return (object instanceof Scriptable) ? (Scriptable)object : null;
  }
  
  public static Object getDefaultValue(Scriptable paramScriptable, Class<?> paramClass) {
    String str;
    Object object = null;
    for (byte b = 0; b < 2; b++) {
      String str1;
      Class<?> clazz = ScriptRuntime.StringClass;
      boolean bool1 = false;
      boolean bool2 = false;
      if (paramClass == clazz) {
        if (b == 0)
          bool2 = true; 
      } else {
        bool2 = bool1;
        if (b == 1)
          bool2 = true; 
      } 
      if (bool2) {
        str1 = "toString";
      } else {
        str1 = "valueOf";
      } 
      Object object1 = getProperty(paramScriptable, str1);
      if (object1 instanceof Function) {
        Function function = (Function)object1;
        object1 = object;
        if (object == null)
          object1 = Context.getContext(); 
        Object object2 = function.call((Context)object1, function.getParentScope(), paramScriptable, ScriptRuntime.emptyArgs);
        object = object1;
        if (object2 != null) {
          if (!(object2 instanceof Scriptable))
            return object2; 
          if (paramClass == ScriptRuntime.ScriptableClass || paramClass == ScriptRuntime.FunctionClass)
            return object2; 
          object = object1;
          if (bool2) {
            object = object1;
            if (object2 instanceof Wrapper) {
              object2 = ((Wrapper)object2).unwrap();
              object = object1;
              if (object2 instanceof String)
                return object2; 
            } 
          } 
        } 
      } 
    } 
    if (paramClass == null) {
      str = "undefined";
    } else {
      str = paramClass.getName();
    } 
    throw ScriptRuntime.typeError1("msg.default.value", str);
  }
  
  public static Scriptable getFunctionPrototype(Scriptable paramScriptable) {
    return TopLevel.getBuiltinPrototype(getTopLevelScope(paramScriptable), TopLevel.Builtins.Function);
  }
  
  public static Scriptable getObjectPrototype(Scriptable paramScriptable) {
    return TopLevel.getBuiltinPrototype(getTopLevelScope(paramScriptable), TopLevel.Builtins.Object);
  }
  
  public static Object getProperty(Scriptable paramScriptable, int paramInt) {
    Object object;
    Scriptable scriptable2;
    Scriptable scriptable1 = paramScriptable;
    do {
      object = scriptable1.get(paramInt, paramScriptable);
      if (object != Scriptable.NOT_FOUND)
        break; 
      scriptable2 = scriptable1.getPrototype();
      scriptable1 = scriptable2;
    } while (scriptable2 != null);
    return object;
  }
  
  public static Object getProperty(Scriptable paramScriptable, Symbol paramSymbol) {
    Object object;
    Scriptable scriptable2;
    Scriptable scriptable1 = paramScriptable;
    do {
      object = ensureSymbolScriptable(scriptable1).get(paramSymbol, paramScriptable);
      if (object != Scriptable.NOT_FOUND)
        break; 
      scriptable2 = scriptable1.getPrototype();
      scriptable1 = scriptable2;
    } while (scriptable2 != null);
    return object;
  }
  
  public static Object getProperty(Scriptable paramScriptable, String paramString) {
    Object object;
    Scriptable scriptable2;
    Scriptable scriptable1 = paramScriptable;
    do {
      object = scriptable1.get(paramString, paramScriptable);
      if (object != Scriptable.NOT_FOUND)
        break; 
      scriptable2 = scriptable1.getPrototype();
      scriptable1 = scriptable2;
    } while (scriptable2 != null);
    return object;
  }
  
  public static Object[] getPropertyIds(Scriptable paramScriptable) {
    if (paramScriptable == null)
      return ScriptRuntime.emptyArgs; 
    Object[] arrayOfObject2 = paramScriptable.getIds();
    ObjToIntMap objToIntMap = null;
    Scriptable scriptable = paramScriptable;
    Object[] arrayOfObject1 = arrayOfObject2;
    while (true) {
      Scriptable scriptable2 = scriptable.getPrototype();
      if (scriptable2 == null) {
        if (objToIntMap != null)
          arrayOfObject1 = objToIntMap.getKeys(); 
        return arrayOfObject1;
      } 
      Object[] arrayOfObject = scriptable2.getIds();
      if (arrayOfObject.length == 0) {
        scriptable = scriptable2;
        continue;
      } 
      arrayOfObject2 = arrayOfObject1;
      ObjToIntMap objToIntMap1 = objToIntMap;
      if (objToIntMap == null) {
        if (arrayOfObject1.length == 0) {
          arrayOfObject1 = arrayOfObject;
          Scriptable scriptable3 = scriptable2;
          continue;
        } 
        objToIntMap1 = new ObjToIntMap(arrayOfObject1.length + arrayOfObject.length);
        for (byte b1 = 0; b1 != arrayOfObject1.length; b1++)
          objToIntMap1.intern(arrayOfObject1[b1]); 
        arrayOfObject2 = null;
      } 
      for (byte b = 0; b != arrayOfObject.length; b++)
        objToIntMap1.intern(arrayOfObject[b]); 
      arrayOfObject1 = arrayOfObject2;
      objToIntMap = objToIntMap1;
      Scriptable scriptable1 = scriptable2;
    } 
  }
  
  private static String getPropertyName(String paramString1, String paramString2, Annotation paramAnnotation) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 13
    //   4: aload_0
    //   5: aload_1
    //   6: invokevirtual length : ()I
    //   9: invokevirtual substring : (I)Ljava/lang/String;
    //   12: areturn
    //   13: aconst_null
    //   14: astore_1
    //   15: aload_2
    //   16: instanceof com/trendmicro/hippo/annotations/JSGetter
    //   19: ifeq -> 154
    //   22: aload_2
    //   23: checkcast com/trendmicro/hippo/annotations/JSGetter
    //   26: invokeinterface value : ()Ljava/lang/String;
    //   31: astore_2
    //   32: aload_2
    //   33: ifnull -> 45
    //   36: aload_2
    //   37: astore_1
    //   38: aload_2
    //   39: invokevirtual length : ()I
    //   42: ifne -> 191
    //   45: aload_2
    //   46: astore_1
    //   47: aload_0
    //   48: invokevirtual length : ()I
    //   51: iconst_3
    //   52: if_icmple -> 191
    //   55: aload_2
    //   56: astore_1
    //   57: aload_0
    //   58: ldc_w 'get'
    //   61: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   64: ifeq -> 191
    //   67: aload_0
    //   68: iconst_3
    //   69: invokevirtual substring : (I)Ljava/lang/String;
    //   72: astore_2
    //   73: aload_2
    //   74: astore_1
    //   75: aload_2
    //   76: iconst_0
    //   77: invokevirtual charAt : (I)C
    //   80: invokestatic isUpperCase : (C)Z
    //   83: ifeq -> 191
    //   86: aload_2
    //   87: invokevirtual length : ()I
    //   90: iconst_1
    //   91: if_icmpne -> 102
    //   94: aload_2
    //   95: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   98: astore_1
    //   99: goto -> 191
    //   102: aload_2
    //   103: astore_1
    //   104: aload_2
    //   105: iconst_1
    //   106: invokevirtual charAt : (I)C
    //   109: invokestatic isUpperCase : (C)Z
    //   112: ifne -> 191
    //   115: new java/lang/StringBuilder
    //   118: dup
    //   119: invokespecial <init> : ()V
    //   122: astore_1
    //   123: aload_1
    //   124: aload_2
    //   125: iconst_0
    //   126: invokevirtual charAt : (I)C
    //   129: invokestatic toLowerCase : (C)C
    //   132: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_1
    //   137: aload_2
    //   138: iconst_1
    //   139: invokevirtual substring : (I)Ljava/lang/String;
    //   142: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: pop
    //   146: aload_1
    //   147: invokevirtual toString : ()Ljava/lang/String;
    //   150: astore_1
    //   151: goto -> 191
    //   154: aload_2
    //   155: instanceof com/trendmicro/hippo/annotations/JSFunction
    //   158: ifeq -> 174
    //   161: aload_2
    //   162: checkcast com/trendmicro/hippo/annotations/JSFunction
    //   165: invokeinterface value : ()Ljava/lang/String;
    //   170: astore_1
    //   171: goto -> 191
    //   174: aload_2
    //   175: instanceof com/trendmicro/hippo/annotations/JSStaticFunction
    //   178: ifeq -> 191
    //   181: aload_2
    //   182: checkcast com/trendmicro/hippo/annotations/JSStaticFunction
    //   185: invokeinterface value : ()Ljava/lang/String;
    //   190: astore_1
    //   191: aload_1
    //   192: ifnull -> 204
    //   195: aload_1
    //   196: astore_2
    //   197: aload_1
    //   198: invokevirtual length : ()I
    //   201: ifne -> 206
    //   204: aload_0
    //   205: astore_2
    //   206: aload_2
    //   207: areturn
  }
  
  public static Scriptable getTopLevelScope(Scriptable paramScriptable) {
    while (true) {
      Scriptable scriptable = paramScriptable.getParentScope();
      if (scriptable == null)
        return paramScriptable; 
      paramScriptable = scriptable;
    } 
  }
  
  public static Object getTopScopeValue(Scriptable paramScriptable, Object paramObject) {
    paramScriptable = getTopLevelScope(paramScriptable);
    while (true) {
      if (paramScriptable instanceof ScriptableObject) {
        Object object = ((ScriptableObject)paramScriptable).getAssociatedValue(paramObject);
        if (object != null)
          return object; 
      } 
      paramScriptable = paramScriptable.getPrototype();
      if (paramScriptable == null)
        return null; 
    } 
  }
  
  public static <T> T getTypedProperty(Scriptable paramScriptable, int paramInt, Class<T> paramClass) {
    Object object2 = getProperty(paramScriptable, paramInt);
    Object object1 = object2;
    if (object2 == Scriptable.NOT_FOUND)
      object1 = null; 
    return paramClass.cast(Context.jsToJava(object1, paramClass));
  }
  
  public static <T> T getTypedProperty(Scriptable paramScriptable, String paramString, Class<T> paramClass) {
    Object object2 = getProperty(paramScriptable, paramString);
    Object object1 = object2;
    if (object2 == Scriptable.NOT_FOUND)
      object1 = null; 
    return paramClass.cast(Context.jsToJava(object1, paramClass));
  }
  
  public static boolean hasProperty(Scriptable paramScriptable, int paramInt) {
    boolean bool;
    if (getBase(paramScriptable, paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean hasProperty(Scriptable paramScriptable, Symbol paramSymbol) {
    boolean bool;
    if (getBase(paramScriptable, paramSymbol) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean hasProperty(Scriptable paramScriptable, String paramString) {
    boolean bool;
    if (getBase(paramScriptable, paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected static boolean isFalse(Object paramObject) {
    return isTrue(paramObject) ^ true;
  }
  
  protected static boolean isTrue(Object paramObject) {
    boolean bool;
    if (paramObject != NOT_FOUND && ScriptRuntime.toBoolean(paramObject)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean putConstImpl(String paramString, int paramInt1, Scriptable paramScriptable, Object paramObject, int paramInt2) {
    if (this.isExtensible || !Context.getContext().isStrictMode()) {
      Slot slot1;
      Slot slot2;
      if (this != paramScriptable) {
        Slot slot = this.slotMap.query(paramString, paramInt1);
        slot1 = slot;
        if (slot == null)
          return false; 
      } else {
        if (!isExtensible()) {
          Slot slot = this.slotMap.query(slot1, paramInt1);
          slot1 = slot;
          return (slot == null) ? true : slot1.setValue(paramObject, this, paramScriptable);
        } 
        checkNotSealed(slot1, paramInt1);
        slot2 = this.slotMap.get(slot1, paramInt1, SlotAccess.MODIFY_CONST);
        paramInt1 = slot2.getAttributes();
        if ((paramInt1 & 0x1) != 0) {
          if ((paramInt1 & 0x8) != 0) {
            slot2.value = paramObject;
            if (paramInt2 != 8)
              slot2.setAttributes(paramInt1 & 0xFFFFFFF7); 
          } 
          return true;
        } 
        throw Context.reportRuntimeError1("msg.var.redecl", slot1);
      } 
      return slot1.setValue(paramObject, this, (Scriptable)slot2);
    } 
    throw ScriptRuntime.typeError0("msg.not.extensible");
  }
  
  public static void putConstProperty(Scriptable paramScriptable, String paramString, Object paramObject) {
    Scriptable scriptable1 = getBase(paramScriptable, paramString);
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = paramScriptable; 
    if (scriptable2 instanceof ConstProperties)
      ((ConstProperties)scriptable2).putConst(paramString, paramScriptable, paramObject); 
  }
  
  private boolean putImpl(Object paramObject1, int paramInt, Scriptable paramScriptable, Object paramObject2) {
    if (this.isExtensible || !Context.getContext().isStrictMode()) {
      if (this != paramScriptable) {
        Slot slot = this.slotMap.query(paramObject1, paramInt);
        paramObject1 = slot;
        if (slot == null)
          return false; 
      } else if (!this.isExtensible) {
        Slot slot = this.slotMap.query(paramObject1, paramInt);
        paramObject1 = slot;
        if (slot == null)
          return true; 
      } else {
        if (this.isSealed)
          checkNotSealed(paramObject1, paramInt); 
        paramObject1 = this.slotMap.get(paramObject1, paramInt, SlotAccess.MODIFY);
      } 
      return paramObject1.setValue(paramObject2, this, paramScriptable);
    } 
    throw ScriptRuntime.typeError0("msg.not.extensible");
  }
  
  public static void putProperty(Scriptable paramScriptable, int paramInt, Object paramObject) {
    Scriptable scriptable1 = getBase(paramScriptable, paramInt);
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = paramScriptable; 
    scriptable2.put(paramInt, paramScriptable, paramObject);
  }
  
  public static void putProperty(Scriptable paramScriptable, Symbol paramSymbol, Object paramObject) {
    Scriptable scriptable1 = getBase(paramScriptable, paramSymbol);
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = paramScriptable; 
    ensureSymbolScriptable(scriptable2).put(paramSymbol, paramScriptable, paramObject);
  }
  
  public static void putProperty(Scriptable paramScriptable, String paramString, Object paramObject) {
    Scriptable scriptable1 = getBase(paramScriptable, paramString);
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = paramScriptable; 
    scriptable2.put(paramString, paramScriptable, paramObject);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    int i = paramObjectInputStream.readInt();
    this.slotMap = createSlotMap(i);
    for (byte b = 0; b < i; b++) {
      Slot slot = (Slot)paramObjectInputStream.readObject();
      this.slotMap.addSlot(slot);
    } 
  }
  
  public static void redefineProperty(Scriptable paramScriptable, String paramString, boolean paramBoolean) {
    paramScriptable = getBase(paramScriptable, paramString);
    if (paramScriptable == null)
      return; 
    if (!(paramScriptable instanceof ConstProperties) || !((ConstProperties)paramScriptable).isConst(paramString)) {
      if (!paramBoolean)
        return; 
      throw ScriptRuntime.typeError1("msg.var.redecl", paramString);
    } 
    throw ScriptRuntime.typeError1("msg.const.redecl", paramString);
  }
  
  private void setGetterOrSetter(String paramString, int paramInt, Callable paramCallable, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramString == null || paramInt == 0) {
      Slot slot;
      if (!paramBoolean2)
        checkNotSealed(paramString, paramInt); 
      if (isExtensible()) {
        slot = this.slotMap.get(paramString, paramInt, SlotAccess.MODIFY_GETTER_SETTER);
      } else {
        slot = this.slotMap.query(paramString, paramInt);
        if (!(slot instanceof GetterSlot))
          return; 
        slot = slot;
      } 
      if (paramBoolean2 || (slot.getAttributes() & 0x1) == 0) {
        if (paramBoolean1) {
          ((GetterSlot)slot).setter = paramCallable;
        } else {
          ((GetterSlot)slot).getter = paramCallable;
        } 
        ((GetterSlot)slot).value = Undefined.instance;
        return;
      } 
      throw Context.reportRuntimeError1("msg.modify.readonly", paramString);
    } 
    throw new IllegalArgumentException(paramString);
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    long l = this.slotMap.readLock();
    try {
      int i = this.slotMap.dirtySize();
      if (i == 0) {
        paramObjectOutputStream.writeInt(0);
      } else {
        paramObjectOutputStream.writeInt(i);
        Iterator<Slot> iterator = this.slotMap.iterator();
        while (iterator.hasNext())
          paramObjectOutputStream.writeObject(iterator.next()); 
      } 
      return;
    } finally {
      this.slotMap.unlockRead(l);
    } 
  }
  
  void addLazilyInitializedValue(String paramString, int paramInt1, LazilyLoadedCtor paramLazilyLoadedCtor, int paramInt2) {
    GetterSlot getterSlot;
    if (paramString == null || paramInt1 == 0) {
      checkNotSealed(paramString, paramInt1);
      getterSlot = (GetterSlot)this.slotMap.get(paramString, paramInt1, SlotAccess.MODIFY_GETTER_SETTER);
      getterSlot.setAttributes(paramInt2);
      getterSlot.getter = null;
      getterSlot.setter = null;
      getterSlot.value = paramLazilyLoadedCtor;
      return;
    } 
    throw new IllegalArgumentException(getterSlot);
  }
  
  protected int applyDescriptorToAttributeBitset(int paramInt, ScriptableObject paramScriptableObject) {
    Object object2 = getProperty(paramScriptableObject, "enumerable");
    int i = paramInt;
    if (object2 != NOT_FOUND) {
      if (ScriptRuntime.toBoolean(object2)) {
        paramInt &= 0xFFFFFFFD;
      } else {
        paramInt |= 0x2;
      } 
      i = paramInt;
    } 
    object2 = getProperty(paramScriptableObject, "writable");
    paramInt = i;
    if (object2 != NOT_FOUND)
      if (ScriptRuntime.toBoolean(object2)) {
        paramInt = i & 0xFFFFFFFE;
      } else {
        paramInt = i | 0x1;
      }  
    Object object1 = getProperty(paramScriptableObject, "configurable");
    i = paramInt;
    if (object1 != NOT_FOUND) {
      if (ScriptRuntime.toBoolean(object1)) {
        paramInt &= 0xFFFFFFFB;
      } else {
        paramInt |= 0x4;
      } 
      i = paramInt;
    } 
    return i;
  }
  
  public final Object associateValue(Object paramObject1, Object paramObject2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: ifnull -> 50
    //   6: aload_0
    //   7: getfield associatedValues : Ljava/util/Map;
    //   10: astore_3
    //   11: aload_3
    //   12: astore #4
    //   14: aload_3
    //   15: ifnonnull -> 34
    //   18: new java/util/HashMap
    //   21: astore #4
    //   23: aload #4
    //   25: invokespecial <init> : ()V
    //   28: aload_0
    //   29: aload #4
    //   31: putfield associatedValues : Ljava/util/Map;
    //   34: aload #4
    //   36: aload_1
    //   37: aload_2
    //   38: invokestatic initHash : (Ljava/util/Map;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   41: astore_1
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_1
    //   45: areturn
    //   46: astore_1
    //   47: goto -> 60
    //   50: new java/lang/IllegalArgumentException
    //   53: astore_1
    //   54: aload_1
    //   55: invokespecial <init> : ()V
    //   58: aload_1
    //   59: athrow
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   6	11	46	finally
    //   18	28	46	finally
    //   28	34	46	finally
    //   34	42	46	finally
    //   50	60	46	finally
  }
  
  public boolean avoidObjectDetection() {
    return false;
  }
  
  protected void checkPropertyChange(Object paramObject, ScriptableObject paramScriptableObject1, ScriptableObject paramScriptableObject2) {
    if (paramScriptableObject1 == null) {
      if (!isExtensible())
        throw ScriptRuntime.typeError0("msg.not.extensible"); 
    } else if (isFalse(paramScriptableObject1.get("configurable", paramScriptableObject1))) {
      if (!isTrue(getProperty(paramScriptableObject2, "configurable"))) {
        if (isTrue(paramScriptableObject1.get("enumerable", paramScriptableObject1)) == isTrue(getProperty(paramScriptableObject2, "enumerable"))) {
          boolean bool1 = isDataDescriptor(paramScriptableObject2);
          boolean bool2 = isAccessorDescriptor(paramScriptableObject2);
          if (bool1 || bool2)
            if (bool1 && isDataDescriptor(paramScriptableObject1)) {
              if (isFalse(paramScriptableObject1.get("writable", paramScriptableObject1)))
                if (!isTrue(getProperty(paramScriptableObject2, "writable"))) {
                  if (!sameValue(getProperty(paramScriptableObject2, "value"), paramScriptableObject1.get("value", paramScriptableObject1)))
                    throw ScriptRuntime.typeError1("msg.change.value.with.writable.false", paramObject); 
                } else {
                  throw ScriptRuntime.typeError1("msg.change.writable.false.to.true.with.configurable.false", paramObject);
                }  
            } else if (bool2 && isAccessorDescriptor(paramScriptableObject1)) {
              if (sameValue(getProperty(paramScriptableObject2, "set"), paramScriptableObject1.get("set", paramScriptableObject1))) {
                if (!sameValue(getProperty(paramScriptableObject2, "get"), paramScriptableObject1.get("get", paramScriptableObject1)))
                  throw ScriptRuntime.typeError1("msg.change.getter.with.configurable.false", paramObject); 
              } else {
                throw ScriptRuntime.typeError1("msg.change.setter.with.configurable.false", paramObject);
              } 
            } else {
              if (isDataDescriptor(paramScriptableObject1))
                throw ScriptRuntime.typeError1("msg.change.property.data.to.accessor.with.configurable.false", paramObject); 
              throw ScriptRuntime.typeError1("msg.change.property.accessor.to.data.with.configurable.false", paramObject);
            }  
        } else {
          throw ScriptRuntime.typeError1("msg.change.enumerable.with.configurable.false", paramObject);
        } 
      } else {
        throw ScriptRuntime.typeError1("msg.change.configurable.false.to.true", paramObject);
      } 
    } 
  }
  
  protected void checkPropertyDefinition(ScriptableObject paramScriptableObject) {
    Object object = getProperty(paramScriptableObject, "get");
    if (object == NOT_FOUND || object == Undefined.instance || object instanceof Callable) {
      object = getProperty(paramScriptableObject, "set");
      if (object == NOT_FOUND || object == Undefined.instance || object instanceof Callable) {
        if (!isDataDescriptor(paramScriptableObject) || !isAccessorDescriptor(paramScriptableObject))
          return; 
        throw ScriptRuntime.typeError0("msg.both.data.and.accessor.desc");
      } 
      throw ScriptRuntime.notFunctionError(object);
    } 
    throw ScriptRuntime.notFunctionError(object);
  }
  
  public void defineConst(String paramString, Scriptable paramScriptable) {
    if (putConstImpl(paramString, 0, paramScriptable, Undefined.instance, 8))
      return; 
    if (paramScriptable != this) {
      if (paramScriptable instanceof ConstProperties)
        ((ConstProperties)paramScriptable).defineConst(paramString, paramScriptable); 
      return;
    } 
    throw Kit.codeBug();
  }
  
  public void defineFunctionProperties(String[] paramArrayOfString, Class<?> paramClass, int paramInt) {
    Method[] arrayOfMethod = FunctionObject.getMethodList(paramClass);
    byte b = 0;
    while (b < paramArrayOfString.length) {
      String str = paramArrayOfString[b];
      Method method = FunctionObject.findSingleMethod(arrayOfMethod, str);
      if (method != null) {
        defineProperty(str, new FunctionObject(str, method, this), paramInt);
        b++;
        continue;
      } 
      throw Context.reportRuntimeError2("msg.method.not.found", str, paramClass.getName());
    } 
  }
  
  public void defineOwnProperties(Context paramContext, ScriptableObject paramScriptableObject) {
    Object[] arrayOfObject = paramScriptableObject.getIds(false, true);
    ScriptableObject[] arrayOfScriptableObject = new ScriptableObject[arrayOfObject.length];
    byte b = 0;
    int i = arrayOfObject.length;
    while (b < i) {
      ScriptableObject scriptableObject = ensureScriptableObject(ScriptRuntime.getObjectElem(paramScriptableObject, arrayOfObject[b], paramContext));
      checkPropertyDefinition(scriptableObject);
      arrayOfScriptableObject[b] = scriptableObject;
      b++;
    } 
    b = 0;
    i = arrayOfObject.length;
    while (b < i) {
      defineOwnProperty(paramContext, arrayOfObject[b], arrayOfScriptableObject[b]);
      b++;
    } 
  }
  
  public void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject) {
    checkPropertyDefinition(paramScriptableObject);
    defineOwnProperty(paramContext, paramObject, paramScriptableObject, true);
  }
  
  protected void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject, boolean paramBoolean) {
    GetterSlot getterSlot;
    boolean bool;
    Slot slot2;
    int i;
    Slot slot1 = getSlot(paramContext, paramObject, SlotAccess.QUERY);
    if (slot1 == null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramBoolean) {
      ScriptableObject scriptableObject;
      if (slot1 == null) {
        scriptableObject = null;
      } else {
        scriptableObject = slot1.getPropertyDescriptor(paramContext, this);
      } 
      checkPropertyChange(paramObject, scriptableObject, paramScriptableObject);
    } 
    paramBoolean = isAccessorDescriptor(paramScriptableObject);
    if (slot1 == null) {
      SlotAccess slotAccess;
      if (paramBoolean) {
        slotAccess = SlotAccess.MODIFY_GETTER_SETTER;
      } else {
        slotAccess = SlotAccess.MODIFY;
      } 
      slot2 = getSlot(paramContext, paramObject, slotAccess);
      i = applyDescriptorToAttributeBitset(7, paramScriptableObject);
    } else {
      i = applyDescriptorToAttributeBitset(slot1.getAttributes(), paramScriptableObject);
      slot2 = slot1;
    } 
    if (paramBoolean) {
      slot1 = slot2;
      if (!(slot2 instanceof GetterSlot))
        slot1 = getSlot(paramContext, paramObject, SlotAccess.MODIFY_GETTER_SETTER); 
      getterSlot = (GetterSlot)slot1;
      paramObject = getProperty(paramScriptableObject, "get");
      if (paramObject != NOT_FOUND)
        getterSlot.getter = paramObject; 
      paramObject = getProperty(paramScriptableObject, "set");
      if (paramObject != NOT_FOUND)
        getterSlot.setter = paramObject; 
      getterSlot.value = Undefined.instance;
      getterSlot.setAttributes(i);
    } else {
      slot1 = slot2;
      if (slot2 instanceof GetterSlot) {
        slot1 = slot2;
        if (isDataDescriptor(paramScriptableObject))
          slot1 = getSlot((Context)getterSlot, paramObject, SlotAccess.CONVERT_ACCESSOR_TO_DATA); 
      } 
      Object object = getProperty(paramScriptableObject, "value");
      if (object != NOT_FOUND) {
        slot1.value = object;
      } else if (bool) {
        slot1.value = Undefined.instance;
      } 
      slot1.setAttributes(i);
    } 
  }
  
  public void defineProperty(Symbol paramSymbol, Object paramObject, int paramInt) {
    checkNotSealed(paramSymbol, 0);
    put(paramSymbol, this, paramObject);
    setAttributes(paramSymbol, paramInt);
  }
  
  public void defineProperty(String paramString, Class<?> paramClass, int paramInt) {
    int i = paramString.length();
    if (i != 0) {
      char[] arrayOfChar = new char[i + 3];
      paramString.getChars(0, i, arrayOfChar, 3);
      arrayOfChar[3] = Character.toUpperCase(arrayOfChar[3]);
      arrayOfChar[0] = (char)'g';
      arrayOfChar[1] = (char)'e';
      arrayOfChar[2] = (char)'t';
      String str2 = new String(arrayOfChar);
      arrayOfChar[0] = (char)'s';
      String str1 = new String(arrayOfChar);
      Method[] arrayOfMethod = FunctionObject.getMethodList(paramClass);
      Method method2 = FunctionObject.findSingleMethod(arrayOfMethod, str2);
      Method method1 = FunctionObject.findSingleMethod(arrayOfMethod, str1);
      if (method1 == null)
        paramInt |= 0x1; 
      if (method1 == null)
        method1 = null; 
      defineProperty(paramString, null, method2, method1, paramInt);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void defineProperty(String paramString, Object paramObject, int paramInt) {
    checkNotSealed(paramString, 0);
    put(paramString, this, paramObject);
    setAttributes(paramString, paramInt);
  }
  
  public void defineProperty(String paramString, Object<Scriptable> paramObject, Method paramMethod1, Method paramMethod2, int paramInt) {
    MemberBox memberBox1;
    MemberBox memberBox2;
    String str = null;
    if (paramMethod1 != null) {
      boolean bool;
      MemberBox memberBox = new MemberBox(paramMethod1);
      if (!Modifier.isStatic(paramMethod1.getModifiers())) {
        if (paramObject != null) {
          bool = true;
        } else {
          bool = false;
        } 
        memberBox.delegateTo = paramObject;
      } else {
        bool = true;
        memberBox.delegateTo = void.class;
      } 
      String str1 = null;
      str = null;
      Class[] arrayOfClass = paramMethod1.getParameterTypes();
      if (arrayOfClass.length == 0) {
        str = str1;
        if (bool)
          str = "msg.obj.getter.parms"; 
      } else if (arrayOfClass.length == 1) {
        Class<Scriptable> clazz = arrayOfClass[0];
        if (clazz != ScriptRuntime.ScriptableClass && clazz != ScriptRuntime.ScriptableObjectClass) {
          str = "msg.bad.getter.parms";
        } else if (!bool) {
          str = "msg.bad.getter.parms";
        } 
      } else {
        str = "msg.bad.getter.parms";
      } 
      if (str == null) {
        memberBox2 = memberBox;
      } else {
        throw Context.reportRuntimeError1(memberBox2, paramMethod1.toString());
      } 
    } 
    paramMethod1 = null;
    if (paramMethod2 != null)
      if (paramMethod2.getReturnType() == void.class) {
        boolean bool;
        memberBox1 = new MemberBox(paramMethod2);
        if (!Modifier.isStatic(paramMethod2.getModifiers())) {
          if (paramObject != null) {
            bool = true;
          } else {
            bool = false;
          } 
          memberBox1.delegateTo = paramObject;
        } else {
          bool = true;
          memberBox1.delegateTo = void.class;
        } 
        Class<Scriptable> clazz = null;
        paramObject = null;
        Class[] arrayOfClass = paramMethod2.getParameterTypes();
        if (arrayOfClass.length == 1) {
          paramObject = (Object<Scriptable>)clazz;
          if (bool)
            paramObject = (Object<Scriptable>)"msg.setter2.expected"; 
        } else if (arrayOfClass.length == 2) {
          clazz = arrayOfClass[0];
          if (clazz != ScriptRuntime.ScriptableClass && clazz != ScriptRuntime.ScriptableObjectClass) {
            paramObject = (Object<Scriptable>)"msg.setter2.parms";
          } else if (!bool) {
            paramObject = (Object<Scriptable>)"msg.setter1.parms";
          } 
        } else {
          paramObject = (Object<Scriptable>)"msg.setter.parms";
        } 
        if (paramObject != null)
          throw Context.reportRuntimeError1(paramObject, paramMethod2.toString()); 
      } else {
        throw Context.reportRuntimeError1("msg.setter.return", paramMethod2.toString());
      }  
    GetterSlot getterSlot = (GetterSlot)this.slotMap.get(paramString, 0, SlotAccess.MODIFY_GETTER_SETTER);
    getterSlot.setAttributes(paramInt);
    getterSlot.getter = memberBox2;
    getterSlot.setter = memberBox1;
  }
  
  public void delete(int paramInt) {
    checkNotSealed(null, paramInt);
    this.slotMap.remove(null, paramInt);
  }
  
  public void delete(Symbol paramSymbol) {
    checkNotSealed(paramSymbol, 0);
    this.slotMap.remove(paramSymbol, 0);
  }
  
  public void delete(String paramString) {
    checkNotSealed(paramString, 0);
    this.slotMap.remove(paramString, 0);
  }
  
  protected Object equivalentValues(Object paramObject) {
    if (this == paramObject) {
      paramObject = Boolean.TRUE;
    } else {
      paramObject = Scriptable.NOT_FOUND;
    } 
    return paramObject;
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    ExternalArrayData externalArrayData = this.externalData;
    if (externalArrayData != null)
      return (paramInt < externalArrayData.getArrayLength()) ? this.externalData.getArrayElement(paramInt) : Scriptable.NOT_FOUND; 
    Slot slot = this.slotMap.query(null, paramInt);
    return (slot == null) ? Scriptable.NOT_FOUND : slot.getValue(paramScriptable);
  }
  
  public Object get(Symbol paramSymbol, Scriptable paramScriptable) {
    Slot slot = this.slotMap.query(paramSymbol, 0);
    return (slot == null) ? Scriptable.NOT_FOUND : slot.getValue(paramScriptable);
  }
  
  public Object get(Object paramObject) {
    Object object = null;
    if (paramObject instanceof String) {
      object = get((String)paramObject, this);
    } else if (paramObject instanceof Symbol) {
      object = get((Symbol)paramObject, this);
    } else if (paramObject instanceof Number) {
      object = get(((Number)paramObject).intValue(), this);
    } 
    return (object == Scriptable.NOT_FOUND || object == Undefined.instance) ? null : ((object instanceof Wrapper) ? ((Wrapper)object).unwrap() : object);
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Slot slot = this.slotMap.query(paramString, 0);
    return (slot == null) ? Scriptable.NOT_FOUND : slot.getValue(paramScriptable);
  }
  
  public Object[] getAllIds() {
    return getIds(true, false);
  }
  
  public final Object getAssociatedValue(Object paramObject) {
    Map<Object, Object> map = this.associatedValues;
    return (map == null) ? null : map.get(paramObject);
  }
  
  public int getAttributes(int paramInt) {
    return findAttributeSlot(null, paramInt, SlotAccess.QUERY).getAttributes();
  }
  
  @Deprecated
  public final int getAttributes(int paramInt, Scriptable paramScriptable) {
    return getAttributes(paramInt);
  }
  
  public int getAttributes(Symbol paramSymbol) {
    return findAttributeSlot(paramSymbol, SlotAccess.QUERY).getAttributes();
  }
  
  public int getAttributes(String paramString) {
    return findAttributeSlot(paramString, 0, SlotAccess.QUERY).getAttributes();
  }
  
  @Deprecated
  public final int getAttributes(String paramString, Scriptable paramScriptable) {
    return getAttributes(paramString);
  }
  
  public abstract String getClassName();
  
  public Object getDefaultValue(Class<?> paramClass) {
    return getDefaultValue(this, paramClass);
  }
  
  public ExternalArrayData getExternalArrayData() {
    return this.externalData;
  }
  
  public Object getExternalArrayLength() {
    int i;
    ExternalArrayData externalArrayData = this.externalData;
    if (externalArrayData == null) {
      i = 0;
    } else {
      i = externalArrayData.getArrayLength();
    } 
    return Integer.valueOf(i);
  }
  
  public Object getGetterOrSetter(String paramString, int paramInt, boolean paramBoolean) {
    Object object;
    if (paramString == null || paramInt == 0) {
      object = this.slotMap.query(paramString, paramInt);
      if (object == null)
        return null; 
      if (object instanceof GetterSlot) {
        object = object;
        if (paramBoolean) {
          object = ((GetterSlot)object).setter;
        } else {
          object = ((GetterSlot)object).getter;
        } 
        if (object == null)
          object = Undefined.instance; 
        return object;
      } 
      return Undefined.instance;
    } 
    throw new IllegalArgumentException(object);
  }
  
  public Object[] getIds() {
    return getIds(false, false);
  }
  
  Object[] getIds(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield externalData : Lcom/trendmicro/hippo/ExternalArrayData;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnonnull -> 15
    //   9: iconst_0
    //   10: istore #4
    //   12: goto -> 23
    //   15: aload_3
    //   16: invokeinterface getArrayLength : ()I
    //   21: istore #4
    //   23: iload #4
    //   25: ifne -> 35
    //   28: getstatic com/trendmicro/hippo/ScriptRuntime.emptyArgs : [Ljava/lang/Object;
    //   31: astore_3
    //   32: goto -> 71
    //   35: iload #4
    //   37: anewarray java/lang/Object
    //   40: astore #5
    //   42: iconst_0
    //   43: istore #6
    //   45: aload #5
    //   47: astore_3
    //   48: iload #6
    //   50: iload #4
    //   52: if_icmpge -> 71
    //   55: aload #5
    //   57: iload #6
    //   59: iload #6
    //   61: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   64: aastore
    //   65: iinc #6, 1
    //   68: goto -> 45
    //   71: aload_0
    //   72: getfield slotMap : Lcom/trendmicro/hippo/SlotMapContainer;
    //   75: invokevirtual isEmpty : ()Z
    //   78: ifeq -> 83
    //   81: aload_3
    //   82: areturn
    //   83: iload #4
    //   85: istore #7
    //   87: aload_0
    //   88: getfield slotMap : Lcom/trendmicro/hippo/SlotMapContainer;
    //   91: invokevirtual readLock : ()J
    //   94: lstore #8
    //   96: aload_0
    //   97: getfield slotMap : Lcom/trendmicro/hippo/SlotMapContainer;
    //   100: invokevirtual iterator : ()Ljava/util/Iterator;
    //   103: astore #10
    //   105: aload #10
    //   107: invokeinterface hasNext : ()Z
    //   112: ifeq -> 269
    //   115: aload #10
    //   117: invokeinterface next : ()Ljava/lang/Object;
    //   122: checkcast com/trendmicro/hippo/ScriptableObject$Slot
    //   125: astore #11
    //   127: iload_1
    //   128: ifne -> 148
    //   131: aload_3
    //   132: astore #5
    //   134: iload #7
    //   136: istore #6
    //   138: aload #11
    //   140: invokevirtual getAttributes : ()I
    //   143: iconst_2
    //   144: iand
    //   145: ifne -> 255
    //   148: iload_2
    //   149: ifne -> 170
    //   152: aload_3
    //   153: astore #5
    //   155: iload #7
    //   157: istore #6
    //   159: aload #11
    //   161: getfield name : Ljava/lang/Object;
    //   164: instanceof com/trendmicro/hippo/Symbol
    //   167: ifne -> 255
    //   170: aload_3
    //   171: astore #5
    //   173: iload #7
    //   175: iload #4
    //   177: if_icmpne -> 217
    //   180: aload_0
    //   181: getfield slotMap : Lcom/trendmicro/hippo/SlotMapContainer;
    //   184: invokevirtual dirtySize : ()I
    //   187: iload #4
    //   189: iadd
    //   190: anewarray java/lang/Object
    //   193: astore #12
    //   195: aload #12
    //   197: astore #5
    //   199: aload_3
    //   200: ifnull -> 217
    //   203: aload_3
    //   204: iconst_0
    //   205: aload #12
    //   207: iconst_0
    //   208: iload #4
    //   210: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   213: aload #12
    //   215: astore #5
    //   217: aload #11
    //   219: getfield name : Ljava/lang/Object;
    //   222: ifnull -> 234
    //   225: aload #11
    //   227: getfield name : Ljava/lang/Object;
    //   230: astore_3
    //   231: goto -> 243
    //   234: aload #11
    //   236: getfield indexOrHash : I
    //   239: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   242: astore_3
    //   243: aload #5
    //   245: iload #7
    //   247: aload_3
    //   248: aastore
    //   249: iload #7
    //   251: iconst_1
    //   252: iadd
    //   253: istore #6
    //   255: aload #5
    //   257: astore_3
    //   258: iload #6
    //   260: istore #7
    //   262: goto -> 105
    //   265: astore_3
    //   266: goto -> 341
    //   269: aload_0
    //   270: getfield slotMap : Lcom/trendmicro/hippo/SlotMapContainer;
    //   273: lload #8
    //   275: invokevirtual unlockRead : (J)V
    //   278: iload #7
    //   280: aload_3
    //   281: arraylength
    //   282: iload #4
    //   284: iadd
    //   285: if_icmpne -> 291
    //   288: goto -> 311
    //   291: iload #7
    //   293: anewarray java/lang/Object
    //   296: astore #5
    //   298: aload_3
    //   299: iconst_0
    //   300: aload #5
    //   302: iconst_0
    //   303: iload #7
    //   305: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   308: aload #5
    //   310: astore_3
    //   311: invokestatic getCurrentContext : ()Lcom/trendmicro/hippo/Context;
    //   314: astore #5
    //   316: aload #5
    //   318: ifnull -> 338
    //   321: aload #5
    //   323: bipush #16
    //   325: invokevirtual hasFeature : (I)Z
    //   328: ifeq -> 338
    //   331: aload_3
    //   332: getstatic com/trendmicro/hippo/ScriptableObject.KEY_COMPARATOR : Ljava/util/Comparator;
    //   335: invokestatic sort : ([Ljava/lang/Object;Ljava/util/Comparator;)V
    //   338: aload_3
    //   339: areturn
    //   340: astore_3
    //   341: aload_0
    //   342: getfield slotMap : Lcom/trendmicro/hippo/SlotMapContainer;
    //   345: lload #8
    //   347: invokevirtual unlockRead : (J)V
    //   350: aload_3
    //   351: athrow
    // Exception table:
    //   from	to	target	type
    //   96	105	340	finally
    //   105	127	340	finally
    //   138	148	340	finally
    //   159	170	340	finally
    //   180	195	340	finally
    //   203	213	340	finally
    //   217	231	265	finally
    //   234	243	265	finally
  }
  
  protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject) {
    Slot slot = getSlot(paramContext, paramObject, SlotAccess.QUERY);
    if (slot == null)
      return null; 
    paramObject = getParentScope();
    if (paramObject == null)
      paramObject = this; 
    return slot.getPropertyDescriptor(paramContext, (Scriptable)paramObject);
  }
  
  public Scriptable getParentScope() {
    return this.parentScopeObject;
  }
  
  public Scriptable getPrototype() {
    return this.prototypeObject;
  }
  
  protected Slot getSlot(Context paramContext, Object paramObject, SlotAccess paramSlotAccess) {
    if (paramObject instanceof Symbol)
      return this.slotMap.get(paramObject, 0, paramSlotAccess); 
    paramObject = ScriptRuntime.toStringIdOrIndex(paramContext, paramObject);
    return (paramObject == null) ? this.slotMap.get(null, ScriptRuntime.lastIndexResult(paramContext), paramSlotAccess) : this.slotMap.get(paramObject, 0, paramSlotAccess);
  }
  
  public String getTypeOf() {
    String str;
    if (avoidObjectDetection()) {
      str = "undefined";
    } else {
      str = "object";
    } 
    return str;
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    ExternalArrayData externalArrayData = this.externalData;
    boolean bool1 = true;
    boolean bool2 = true;
    if (externalArrayData != null) {
      if (paramInt >= externalArrayData.getArrayLength())
        bool2 = false; 
      return bool2;
    } 
    if (this.slotMap.query(null, paramInt) != null) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  public boolean has(Symbol paramSymbol, Scriptable paramScriptable) {
    SlotMapContainer slotMapContainer = this.slotMap;
    boolean bool = false;
    if (slotMapContainer.query(paramSymbol, 0) != null)
      bool = true; 
    return bool;
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    SlotMapContainer slotMapContainer = this.slotMap;
    boolean bool = false;
    if (slotMapContainer.query(paramString, 0) != null)
      bool = true; 
    return bool;
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    return ScriptRuntime.jsDelegatesTo(paramScriptable, this);
  }
  
  protected boolean isAccessorDescriptor(ScriptableObject paramScriptableObject) {
    return (hasProperty(paramScriptableObject, "get") || hasProperty(paramScriptableObject, "set"));
  }
  
  public boolean isConst(String paramString) {
    SlotMapContainer slotMapContainer = this.slotMap;
    boolean bool = false;
    Slot slot = slotMapContainer.query(paramString, 0);
    if (slot == null)
      return false; 
    if ((slot.getAttributes() & 0x5) == 5)
      bool = true; 
    return bool;
  }
  
  protected boolean isDataDescriptor(ScriptableObject paramScriptableObject) {
    return (hasProperty(paramScriptableObject, "value") || hasProperty(paramScriptableObject, "writable"));
  }
  
  public boolean isEmpty() {
    return this.slotMap.isEmpty();
  }
  
  public boolean isExtensible() {
    return this.isExtensible;
  }
  
  protected boolean isGenericDescriptor(ScriptableObject paramScriptableObject) {
    boolean bool;
    if (!isDataDescriptor(paramScriptableObject) && !isAccessorDescriptor(paramScriptableObject)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected boolean isGetterOrSetter(String paramString, int paramInt, boolean paramBoolean) {
    Slot slot = this.slotMap.query(paramString, paramInt);
    if (slot instanceof GetterSlot) {
      if (paramBoolean && ((GetterSlot)slot).setter != null)
        return true; 
      if (!paramBoolean && ((GetterSlot)slot).getter != null)
        return true; 
    } 
    return false;
  }
  
  public final boolean isSealed() {
    return this.isSealed;
  }
  
  public void preventExtensions() {
    this.isExtensible = false;
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    ExternalArrayData externalArrayData = this.externalData;
    if (externalArrayData != null) {
      if (paramInt < externalArrayData.getArrayLength()) {
        this.externalData.setArrayElement(paramInt, paramObject);
        return;
      } 
      throw new JavaScriptException(ScriptRuntime.newNativeError(Context.getCurrentContext(), this, TopLevel.NativeErrors.RangeError, new Object[] { "External array index out of bounds " }), null, 0);
    } 
    if (putImpl(null, paramInt, paramScriptable, paramObject))
      return; 
    if (paramScriptable != this) {
      paramScriptable.put(paramInt, paramScriptable, paramObject);
      return;
    } 
    throw Kit.codeBug();
  }
  
  public void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject) {
    if (putImpl(paramSymbol, 0, paramScriptable, paramObject))
      return; 
    if (paramScriptable != this) {
      ensureSymbolScriptable(paramScriptable).put(paramSymbol, paramScriptable, paramObject);
      return;
    } 
    throw Kit.codeBug();
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    if (putImpl(paramString, 0, paramScriptable, paramObject))
      return; 
    if (paramScriptable != this) {
      paramScriptable.put(paramString, paramScriptable, paramObject);
      return;
    } 
    throw Kit.codeBug();
  }
  
  public void putConst(String paramString, Scriptable paramScriptable, Object paramObject) {
    if (putConstImpl(paramString, 0, paramScriptable, paramObject, 1))
      return; 
    if (paramScriptable != this) {
      if (paramScriptable instanceof ConstProperties) {
        ((ConstProperties)paramScriptable).putConst(paramString, paramScriptable, paramObject);
      } else {
        paramScriptable.put(paramString, paramScriptable, paramObject);
      } 
      return;
    } 
    throw Kit.codeBug();
  }
  
  protected boolean sameValue(Object paramObject1, Object paramObject2) {
    if (paramObject1 == NOT_FOUND)
      return true; 
    Object object = paramObject2;
    if (paramObject2 == NOT_FOUND)
      object = Undefined.instance; 
    if (object instanceof Number && paramObject1 instanceof Number) {
      double d1 = ((Number)object).doubleValue();
      double d2 = ((Number)paramObject1).doubleValue();
      if (Double.isNaN(d1) && Double.isNaN(d2))
        return true; 
      if (d1 == 0.0D && Double.doubleToLongBits(d1) != Double.doubleToLongBits(d2))
        return false; 
    } 
    return ScriptRuntime.shallowEq(object, paramObject1);
  }
  
  public void sealObject() {
    if (!this.isSealed) {
      long l = this.slotMap.readLock();
      try {
        for (Slot slot : this.slotMap) {
          Object object = slot.value;
          if (object instanceof LazilyLoadedCtor) {
            object = object;
            try {
              object.init();
            } finally {
              slot.value = object.getValue();
            } 
          } 
        } 
        this.isSealed = true;
      } finally {
        this.slotMap.unlockRead(l);
      } 
    } 
  }
  
  public void setAttributes(int paramInt1, int paramInt2) {
    checkNotSealed(null, paramInt1);
    findAttributeSlot(null, paramInt1, SlotAccess.MODIFY).setAttributes(paramInt2);
  }
  
  @Deprecated
  public void setAttributes(int paramInt1, Scriptable paramScriptable, int paramInt2) {
    setAttributes(paramInt1, paramInt2);
  }
  
  public void setAttributes(Symbol paramSymbol, int paramInt) {
    checkNotSealed(paramSymbol, 0);
    findAttributeSlot(paramSymbol, SlotAccess.MODIFY).setAttributes(paramInt);
  }
  
  public void setAttributes(String paramString, int paramInt) {
    checkNotSealed(paramString, 0);
    findAttributeSlot(paramString, 0, SlotAccess.MODIFY).setAttributes(paramInt);
  }
  
  @Deprecated
  public final void setAttributes(String paramString, Scriptable paramScriptable, int paramInt) {
    setAttributes(paramString, paramInt);
  }
  
  public void setExternalArrayData(ExternalArrayData paramExternalArrayData) {
    this.externalData = paramExternalArrayData;
    if (paramExternalArrayData == null) {
      delete("length");
    } else {
      defineProperty("length", null, GET_ARRAY_LENGTH, null, 3);
    } 
  }
  
  public void setGetterOrSetter(String paramString, int paramInt, Callable paramCallable, boolean paramBoolean) {
    setGetterOrSetter(paramString, paramInt, paramCallable, paramBoolean, false);
  }
  
  public void setParentScope(Scriptable paramScriptable) {
    this.parentScopeObject = paramScriptable;
  }
  
  public void setPrototype(Scriptable paramScriptable) {
    this.prototypeObject = paramScriptable;
  }
  
  public int size() {
    return this.slotMap.size();
  }
  
  static final class GetterSlot extends Slot {
    private static final long serialVersionUID = -4900574849788797588L;
    
    Object getter;
    
    Object setter;
    
    GetterSlot(Object param1Object, int param1Int1, int param1Int2) {
      super(param1Object, param1Int1, param1Int2);
    }
    
    ScriptableObject getPropertyDescriptor(Context param1Context, Scriptable param1Scriptable) {
      String str;
      boolean bool2;
      int i = getAttributes();
      NativeObject nativeObject = new NativeObject();
      ScriptRuntime.setBuiltinProtoAndParent(nativeObject, param1Scriptable, TopLevel.Builtins.Object);
      boolean bool1 = true;
      if ((i & 0x2) == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      nativeObject.defineProperty("enumerable", Boolean.valueOf(bool2), 0);
      if ((i & 0x4) == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      nativeObject.defineProperty("configurable", Boolean.valueOf(bool2), 0);
      if (this.getter == null && this.setter == null) {
        if ((i & 0x1) == 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        } 
        nativeObject.defineProperty("writable", Boolean.valueOf(bool2), 0);
      } 
      if (this.name == null) {
        str = "f";
      } else {
        str = this.name.toString();
      } 
      Object object = this.getter;
      if (object != null)
        if (object instanceof MemberBox) {
          nativeObject.defineProperty("get", new FunctionObject(str, ((MemberBox)this.getter).member(), param1Scriptable), 0);
        } else if (object instanceof Member) {
          nativeObject.defineProperty("get", new FunctionObject(str, (Member)this.getter, param1Scriptable), 0);
        } else {
          nativeObject.defineProperty("get", object, 0);
        }  
      object = this.setter;
      if (object != null)
        if (object instanceof MemberBox) {
          nativeObject.defineProperty("set", new FunctionObject(str, ((MemberBox)this.setter).member(), param1Scriptable), 0);
        } else if (object instanceof Member) {
          nativeObject.defineProperty("set", new FunctionObject(str, (Member)this.setter, param1Scriptable), 0);
        } else {
          nativeObject.defineProperty("set", object, 0);
        }  
      return nativeObject;
    }
    
    Object getValue(Scriptable param1Scriptable) {
      Object object2 = this.getter;
      if (object2 != null) {
        Object[] arrayOfObject;
        if (object2 instanceof MemberBox) {
          MemberBox memberBox = (MemberBox)object2;
          if (memberBox.delegateTo == null) {
            Object[] arrayOfObject1 = ScriptRuntime.emptyArgs;
            object2 = param1Scriptable;
            arrayOfObject = arrayOfObject1;
          } else {
            object2 = memberBox.delegateTo;
            Object[] arrayOfObject1 = new Object[1];
            arrayOfObject1[0] = arrayOfObject;
            arrayOfObject = arrayOfObject1;
          } 
          return memberBox.invoke(object2, arrayOfObject);
        } 
        if (object2 instanceof Function) {
          object2 = object2;
          return object2.call(Context.getContext(), object2.getParentScope(), (Scriptable)arrayOfObject, ScriptRuntime.emptyArgs);
        } 
      } 
      object2 = this.value;
      Object object1 = object2;
      if (object2 instanceof LazilyLoadedCtor) {
        object2 = object2;
        try {
          object2.init();
          object2 = object2.getValue();
          object1 = object2;
        } finally {
          this.value = object2.getValue();
        } 
      } 
      return object1;
    }
    
    boolean setValue(Object param1Object, Scriptable param1Scriptable1, Scriptable param1Scriptable2) {
      Object object2;
      if (this.setter == null) {
        String str;
        if (this.getter != null) {
          Context context = Context.getContext();
          if (context.isStrictMode() || context.hasFeature(11)) {
            str = "";
            if (this.name != null) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("[");
              stringBuilder.append(param1Scriptable2.getClassName());
              stringBuilder.append("].");
              stringBuilder.append(this.name.toString());
              str = stringBuilder.toString();
            } 
            throw ScriptRuntime.typeError2("msg.set.prop.no.setter", str, Context.toString(param1Object));
          } 
          return true;
        } 
        return super.setValue(param1Object, (Scriptable)str, param1Scriptable2);
      } 
      Object object1 = Context.getContext();
      Object object3 = this.setter;
      if (object3 instanceof MemberBox) {
        object3 = object3;
        Class<?>[] arrayOfClass = ((MemberBox)object3).argTypes;
        object1 = FunctionObject.convertArg((Context)object1, param1Scriptable2, param1Object, FunctionObject.getTypeTag(arrayOfClass[arrayOfClass.length - 1]));
        if (((MemberBox)object3).delegateTo == null) {
          param1Object = new Object[] { object1 };
        } else {
          param1Object = ((MemberBox)object3).delegateTo;
          object1 = new Object[] { param1Scriptable2, object1 };
          object2 = param1Object;
          param1Object = object1;
        } 
        object3.invoke(object2, (Object[])param1Object);
      } else if (object3 instanceof Function) {
        object3 = object3;
        object3.call((Context)object1, object3.getParentScope(), (Scriptable)object2, new Object[] { param1Object });
      } 
      return true;
    }
  }
  
  public static final class KeyComparator implements Comparator<Object> {
    public int compare(Object param1Object1, Object param1Object2) {
      if (param1Object1 instanceof Integer) {
        if (param1Object2 instanceof Integer) {
          int i = ((Integer)param1Object1).intValue();
          int j = ((Integer)param1Object2).intValue();
          return (i < j) ? -1 : ((i > j) ? 1 : 0);
        } 
        return -1;
      } 
      return (param1Object2 instanceof Integer) ? 1 : 0;
    }
  }
  
  static class Slot implements Serializable {
    private static final long serialVersionUID = -6090581677123995491L;
    
    private short attributes;
    
    int indexOrHash;
    
    Object name;
    
    transient Slot next;
    
    transient Slot orderedNext;
    
    Object value;
    
    Slot(Object param1Object, int param1Int1, int param1Int2) {
      this.name = param1Object;
      this.indexOrHash = param1Int1;
      this.attributes = (short)(short)param1Int2;
    }
    
    private void readObject(ObjectInputStream param1ObjectInputStream) throws IOException, ClassNotFoundException {
      param1ObjectInputStream.defaultReadObject();
      Object object = this.name;
      if (object != null)
        this.indexOrHash = object.hashCode(); 
    }
    
    int getAttributes() {
      return this.attributes;
    }
    
    ScriptableObject getPropertyDescriptor(Context param1Context, Scriptable param1Scriptable) {
      return ScriptableObject.buildDataDescriptor(param1Scriptable, this.value, this.attributes);
    }
    
    Object getValue(Scriptable param1Scriptable) {
      return this.value;
    }
    
    void setAttributes(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: iload_1
      //   3: invokestatic checkValidAttributes : (I)V
      //   6: aload_0
      //   7: iload_1
      //   8: i2s
      //   9: i2s
      //   10: putfield attributes : S
      //   13: aload_0
      //   14: monitorexit
      //   15: return
      //   16: astore_2
      //   17: aload_0
      //   18: monitorexit
      //   19: aload_2
      //   20: athrow
      // Exception table:
      //   from	to	target	type
      //   2	13	16	finally
    }
    
    boolean setValue(Object param1Object, Scriptable param1Scriptable1, Scriptable param1Scriptable2) {
      if ((this.attributes & 0x1) != 0) {
        if (!Context.getContext().isStrictMode())
          return true; 
        throw ScriptRuntime.typeError1("msg.modify.readonly", this.name);
      } 
      if (param1Scriptable1 == param1Scriptable2) {
        this.value = param1Object;
        return true;
      } 
      return false;
    }
  }
  
  enum SlotAccess {
    CONVERT_ACCESSOR_TO_DATA, MODIFY, MODIFY_CONST, MODIFY_GETTER_SETTER, QUERY;
    
    static {
      SlotAccess slotAccess = new SlotAccess("CONVERT_ACCESSOR_TO_DATA", 4);
      CONVERT_ACCESSOR_TO_DATA = slotAccess;
      $VALUES = new SlotAccess[] { QUERY, MODIFY, MODIFY_CONST, MODIFY_GETTER_SETTER, slotAccess };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ScriptableObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */