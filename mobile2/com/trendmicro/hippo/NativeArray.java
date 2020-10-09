package com.trendmicro.hippo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class NativeArray extends IdScriptableObject implements List {
  private static final Object ARRAY_TAG = "Array";
  
  private static final int ConstructorId_concat = -13;
  
  private static final int ConstructorId_every = -17;
  
  private static final int ConstructorId_filter = -18;
  
  private static final int ConstructorId_find = -22;
  
  private static final int ConstructorId_findIndex = -23;
  
  private static final int ConstructorId_forEach = -19;
  
  private static final int ConstructorId_from = -28;
  
  private static final int ConstructorId_indexOf = -15;
  
  private static final int ConstructorId_isArray = -26;
  
  private static final int ConstructorId_join = -5;
  
  private static final int ConstructorId_lastIndexOf = -16;
  
  private static final int ConstructorId_map = -20;
  
  private static final int ConstructorId_of = -27;
  
  private static final int ConstructorId_pop = -9;
  
  private static final int ConstructorId_push = -8;
  
  private static final int ConstructorId_reduce = -24;
  
  private static final int ConstructorId_reduceRight = -25;
  
  private static final int ConstructorId_reverse = -6;
  
  private static final int ConstructorId_shift = -10;
  
  private static final int ConstructorId_slice = -14;
  
  private static final int ConstructorId_some = -21;
  
  private static final int ConstructorId_sort = -7;
  
  private static final int ConstructorId_splice = -12;
  
  private static final int ConstructorId_unshift = -11;
  
  private static final Comparator<Object> DEFAULT_COMPARATOR;
  
  private static final int DEFAULT_INITIAL_CAPACITY = 10;
  
  private static final double GROW_FACTOR = 1.5D;
  
  private static final int Id_concat = 13;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_copyWithin = 31;
  
  private static final int Id_entries = 29;
  
  private static final int Id_every = 17;
  
  private static final int Id_fill = 26;
  
  private static final int Id_filter = 18;
  
  private static final int Id_find = 22;
  
  private static final int Id_findIndex = 23;
  
  private static final int Id_forEach = 19;
  
  private static final int Id_includes = 30;
  
  private static final int Id_indexOf = 15;
  
  private static final int Id_join = 5;
  
  private static final int Id_keys = 27;
  
  private static final int Id_lastIndexOf = 16;
  
  private static final int Id_length = 1;
  
  private static final int Id_map = 20;
  
  private static final int Id_pop = 9;
  
  private static final int Id_push = 8;
  
  private static final int Id_reduce = 24;
  
  private static final int Id_reduceRight = 25;
  
  private static final int Id_reverse = 6;
  
  private static final int Id_shift = 10;
  
  private static final int Id_slice = 14;
  
  private static final int Id_some = 21;
  
  private static final int Id_sort = 7;
  
  private static final int Id_splice = 12;
  
  private static final int Id_toLocaleString = 3;
  
  private static final int Id_toSource = 4;
  
  private static final int Id_toString = 2;
  
  private static final int Id_unshift = 11;
  
  private static final int Id_values = 28;
  
  private static final int MAX_INSTANCE_ID = 1;
  
  private static final int MAX_PRE_GROW_SIZE = 1431655764;
  
  private static final int MAX_PROTOTYPE_ID = 32;
  
  private static final Long NEGATIVE_ONE = Long.valueOf(-1L);
  
  private static final Comparator<Object> STRING_COMPARATOR = new StringLikeComparator();
  
  private static final int SymbolId_iterator = 32;
  
  private static int maximumInitialCapacity = 0;
  
  private static final long serialVersionUID = 7331366857676127338L;
  
  private Object[] dense;
  
  private boolean denseOnly;
  
  private long length;
  
  private int lengthAttr;
  
  static {
    DEFAULT_COMPARATOR = new ElementComparator();
    maximumInitialCapacity = 10000;
  }
  
  public NativeArray(long paramLong) {
    boolean bool;
    this.lengthAttr = 6;
    if (paramLong <= maximumInitialCapacity) {
      bool = true;
    } else {
      bool = false;
    } 
    this.denseOnly = bool;
    if (bool) {
      int i = (int)paramLong;
      int j = i;
      if (i < 10)
        j = 10; 
      Object[] arrayOfObject = new Object[j];
      this.dense = arrayOfObject;
      Arrays.fill(arrayOfObject, Scriptable.NOT_FOUND);
    } 
    this.length = paramLong;
  }
  
  public NativeArray(Object[] paramArrayOfObject) {
    this.lengthAttr = 6;
    this.denseOnly = true;
    this.dense = paramArrayOfObject;
    this.length = paramArrayOfObject.length;
  }
  
  private static Scriptable callConstructorOrCreateArray(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, long paramLong, boolean paramBoolean) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #6
    //   3: aload_2
    //   4: instanceof com/trendmicro/hippo/Function
    //   7: istore #7
    //   9: iconst_0
    //   10: istore #8
    //   12: aload #6
    //   14: astore #9
    //   16: iload #7
    //   18: ifeq -> 97
    //   21: iload #5
    //   23: ifne -> 43
    //   26: lload_3
    //   27: lconst_0
    //   28: lcmp
    //   29: ifle -> 35
    //   32: goto -> 43
    //   35: getstatic com/trendmicro/hippo/ScriptRuntime.emptyArgs : [Ljava/lang/Object;
    //   38: astore #9
    //   40: goto -> 57
    //   43: iconst_1
    //   44: anewarray java/lang/Object
    //   47: astore #9
    //   49: aload #9
    //   51: iconst_0
    //   52: lload_3
    //   53: invokestatic valueOf : (J)Ljava/lang/Long;
    //   56: aastore
    //   57: aload_2
    //   58: checkcast com/trendmicro/hippo/Function
    //   61: aload_0
    //   62: aload_1
    //   63: aload #9
    //   65: invokeinterface construct : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;
    //   70: astore #9
    //   72: goto -> 97
    //   75: astore_2
    //   76: ldc 'TypeError'
    //   78: aload_2
    //   79: invokevirtual getName : ()Ljava/lang/String;
    //   82: invokevirtual equals : (Ljava/lang/Object;)Z
    //   85: ifeq -> 95
    //   88: aload #6
    //   90: astore #9
    //   92: goto -> 97
    //   95: aload_2
    //   96: athrow
    //   97: aload #9
    //   99: astore_2
    //   100: aload #9
    //   102: ifnonnull -> 128
    //   105: lload_3
    //   106: ldc2_w 2147483647
    //   109: lcmp
    //   110: ifle -> 116
    //   113: goto -> 120
    //   116: lload_3
    //   117: l2i
    //   118: istore #8
    //   120: aload_0
    //   121: aload_1
    //   122: iload #8
    //   124: invokevirtual newArray : (Lcom/trendmicro/hippo/Scriptable;I)Lcom/trendmicro/hippo/Scriptable;
    //   127: astore_2
    //   128: aload_2
    //   129: areturn
    // Exception table:
    //   from	to	target	type
    //   35	40	75	com/trendmicro/hippo/EcmaError
    //   43	57	75	com/trendmicro/hippo/EcmaError
    //   57	72	75	com/trendmicro/hippo/EcmaError
  }
  
  private static long concatSpreadArg(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, long paramLong) {
    long l1 = getLengthProperty(paramContext, paramScriptable2, false);
    long l2 = l1 + paramLong;
    if (l2 <= 2147483647L && paramScriptable1 instanceof NativeArray) {
      NativeArray nativeArray = (NativeArray)paramScriptable1;
      if (nativeArray.denseOnly && paramScriptable2 instanceof NativeArray) {
        NativeArray nativeArray1 = (NativeArray)paramScriptable2;
        if (nativeArray1.denseOnly) {
          nativeArray.ensureCapacity((int)l2);
          System.arraycopy(nativeArray1.dense, 0, nativeArray.dense, (int)paramLong, (int)l1);
          return l2;
        } 
      } 
    } 
    long l3 = paramLong;
    paramLong = 0L;
    while (paramLong < l1) {
      Object object = getRawElem(paramScriptable2, paramLong);
      if (object != Scriptable.NOT_FOUND)
        defineElem(paramContext, paramScriptable1, l3, object); 
      paramLong++;
      l3++;
    } 
    return l2;
  }
  
  private ScriptableObject defaultIndexPropertyDescriptor(Object paramObject) {
    Scriptable scriptable1 = getParentScope();
    Scriptable scriptable2 = scriptable1;
    if (scriptable1 == null)
      scriptable2 = this; 
    scriptable1 = new NativeObject();
    ScriptRuntime.setBuiltinProtoAndParent((ScriptableObject)scriptable1, scriptable2, TopLevel.Builtins.Object);
    scriptable1.defineProperty("value", paramObject, 0);
    scriptable1.defineProperty("writable", Boolean.valueOf(true), 0);
    scriptable1.defineProperty("enumerable", Boolean.valueOf(true), 0);
    scriptable1.defineProperty("configurable", Boolean.valueOf(true), 0);
    return (ScriptableObject)scriptable1;
  }
  
  private static void defineElem(Context paramContext, Scriptable paramScriptable, long paramLong, Object paramObject) {
    if (paramLong > 2147483647L) {
      paramScriptable.put(Long.toString(paramLong), paramScriptable, paramObject);
    } else {
      paramScriptable.put((int)paramLong, paramScriptable, paramObject);
    } 
  }
  
  private static void deleteElem(Scriptable paramScriptable, long paramLong) {
    int i = (int)paramLong;
    if (i == paramLong) {
      paramScriptable.delete(i);
    } else {
      paramScriptable.delete(Long.toString(paramLong));
    } 
  }
  
  private static long doConcat(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object paramObject, long paramLong) {
    if (isConcatSpreadable(paramContext, paramScriptable1, paramObject))
      return concatSpreadArg(paramContext, paramScriptable2, (Scriptable)paramObject, paramLong); 
    defineElem(paramContext, paramScriptable2, paramLong, paramObject);
    return 1L + paramLong;
  }
  
  private boolean ensureCapacity(int paramInt) {
    Object[] arrayOfObject = this.dense;
    if (paramInt > arrayOfObject.length) {
      if (paramInt > 1431655764) {
        this.denseOnly = false;
        return false;
      } 
      arrayOfObject = new Object[Math.max(paramInt, (int)(arrayOfObject.length * 1.5D))];
      Object[] arrayOfObject1 = this.dense;
      System.arraycopy(arrayOfObject1, 0, arrayOfObject, 0, arrayOfObject1.length);
      Arrays.fill(arrayOfObject, this.dense.length, arrayOfObject.length, Scriptable.NOT_FOUND);
      this.dense = arrayOfObject;
    } 
    return true;
  }
  
  private static Object getElem(Context paramContext, Scriptable paramScriptable, long paramLong) {
    Object object = getRawElem(paramScriptable, paramLong);
    if (object == Scriptable.NOT_FOUND)
      object = Undefined.instance; 
    return object;
  }
  
  static long getLengthProperty(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    if (paramScriptable instanceof NativeString)
      return ((NativeString)paramScriptable).getLength(); 
    if (paramScriptable instanceof NativeArray)
      return ((NativeArray)paramScriptable).getLength(); 
    Object object = ScriptableObject.getProperty(paramScriptable, "length");
    if (object == Scriptable.NOT_FOUND)
      return 0L; 
    double d = ScriptRuntime.toNumber(object);
    if (d > 9.007199254740991E15D) {
      if (!paramBoolean)
        return 2147483647L; 
      throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.arraylength.bad"));
    } 
    return (d < 0.0D) ? 0L : ScriptRuntime.toUint32(object);
  }
  
  static int getMaximumInitialCapacity() {
    return maximumInitialCapacity;
  }
  
  private static Object getRawElem(Scriptable paramScriptable, long paramLong) {
    return (paramLong > 2147483647L) ? ScriptableObject.getProperty(paramScriptable, Long.toString(paramLong)) : ScriptableObject.getProperty(paramScriptable, (int)paramLong);
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeArray(0L)).exportAsJSClass(32, paramScriptable, paramBoolean);
  }
  
  private static boolean isConcatSpreadable(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    if (paramObject instanceof Scriptable) {
      Object object = ScriptableObject.getProperty((Scriptable)paramObject, SymbolKey.IS_CONCAT_SPREADABLE);
      if (object != Scriptable.NOT_FOUND && !Undefined.isUndefined(object))
        return ScriptRuntime.toBoolean(object); 
    } 
    return (paramContext.getLanguageVersion() < 200 && ScriptRuntime.instanceOf(paramObject, ScriptRuntime.getExistingCtor(paramContext, paramScriptable, "Array"), paramContext)) ? true : js_isArray(paramObject);
  }
  
  private static Object iterativeMethod(Context paramContext, IdFunctionObject paramIdFunctionObject, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    boolean bool;
    paramScriptable2 = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
    int i = Math.abs(paramIdFunctionObject.methodId());
    if (22 == i || 23 == i)
      paramScriptable2 = ScriptRuntimeES6.requireObjectCoercible(paramContext, paramScriptable2, paramIdFunctionObject); 
    if (i == 20) {
      bool = true;
    } else {
      bool = false;
    } 
    long l = getLengthProperty(paramContext, paramScriptable2, bool);
    if (paramArrayOfObject.length > 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    if (object != null && object instanceof Function) {
      if (paramContext.getLanguageVersion() < 200 || !(object instanceof com.trendmicro.hippo.regexp.NativeRegExp)) {
        Scriptable scriptable1;
        Function function = (Function)object;
        Scriptable scriptable2 = ScriptableObject.getTopLevelScope(function);
        if (paramArrayOfObject.length < 2 || paramArrayOfObject[1] == null || paramArrayOfObject[1] == Undefined.instance) {
          scriptable1 = scriptable2;
        } else {
          scriptable1 = ScriptRuntime.toObject(paramContext, paramScriptable1, scriptable1[1]);
        } 
        Scriptable scriptable3 = null;
        if (i == 18 || i == 20) {
          boolean bool1;
          if (i == 20) {
            bool1 = (int)l;
          } else {
            bool1 = false;
          } 
          scriptable3 = paramContext.newArray(paramScriptable1, bool1);
        } 
        long l1 = 0L;
        long l2;
        for (l2 = 0L; l2 < l; l2++) {
          Object[] arrayOfObject = new Object[3];
          Object object1 = getRawElem(paramScriptable2, l2);
          if (object1 == Scriptable.NOT_FOUND)
            if (i == 22 || i == 23) {
              object1 = Undefined.instance;
            } else {
              continue;
            }  
          arrayOfObject[0] = object1;
          arrayOfObject[1] = Long.valueOf(l2);
          arrayOfObject[2] = paramScriptable2;
          Object object2 = function.call(paramContext, scriptable2, scriptable1, arrayOfObject);
          switch (i) {
            case 23:
              if (ScriptRuntime.toBoolean(object2))
                return ScriptRuntime.wrapNumber(l2); 
              break;
            case 22:
              if (ScriptRuntime.toBoolean(object2))
                return object1; 
              break;
            case 21:
              if (ScriptRuntime.toBoolean(object2))
                return Boolean.TRUE; 
              break;
            case 20:
              defineElem(paramContext, scriptable3, l2, object2);
              break;
            case 18:
              if (ScriptRuntime.toBoolean(object2)) {
                defineElem(paramContext, scriptable3, l1, arrayOfObject[0]);
                l1++;
              } 
              break;
            case 17:
              if (!ScriptRuntime.toBoolean(object2))
                return Boolean.FALSE; 
              break;
          } 
          continue;
        } 
        switch (i) {
          default:
            return Undefined.instance;
          case 23:
            return ScriptRuntime.wrapNumber(-1.0D);
          case 21:
            return Boolean.FALSE;
          case 18:
          case 20:
            return scriptable3;
          case 17:
            break;
        } 
        return Boolean.TRUE;
      } 
      throw ScriptRuntime.notFunctionError(object);
    } 
    throw ScriptRuntime.notFunctionError(object);
  }
  
  private static Object jsConstructor(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length == 0)
      return new NativeArray(0L); 
    if (paramContext.getLanguageVersion() == 120)
      return new NativeArray(paramArrayOfObject); 
    Object object = paramArrayOfObject[0];
    if (paramArrayOfObject.length > 1 || !(object instanceof Number))
      return new NativeArray(paramArrayOfObject); 
    long l = ScriptRuntime.toUint32(object);
    if (l == ((Number)object).doubleValue())
      return new NativeArray(l); 
    throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.arraylength.bad"));
  }
  
  private static Scriptable js_concat(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    paramScriptable1 = getTopLevelScope(paramScriptable1);
    byte b = 0;
    Scriptable scriptable = paramContext.newArray(paramScriptable1, 0);
    long l = doConcat(paramContext, paramScriptable1, scriptable, paramScriptable2, 0L);
    int i = paramArrayOfObject.length;
    while (b < i) {
      l = doConcat(paramContext, paramScriptable1, scriptable, paramArrayOfObject[b], l);
      b++;
    } 
    setLengthProperty(paramContext, scriptable, l);
    return scriptable;
  }
  
  private static Object js_copyWithin(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    Object object1;
    long l3;
    Object object2;
    long l4;
    Scriptable scriptable = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
    long l1 = getLengthProperty(paramContext, scriptable, false);
    if (paramArrayOfObject.length >= 1) {
      object1 = paramArrayOfObject[0];
    } else {
      object1 = Undefined.instance;
    } 
    long l2 = (long)ScriptRuntime.toInteger(object1);
    if (l2 < 0L) {
      l3 = Math.max(l1 + l2, 0L);
    } else {
      l3 = Math.min(l2, l1);
    } 
    if (paramArrayOfObject.length >= 2) {
      object2 = paramArrayOfObject[1];
    } else {
      object2 = Undefined.instance;
    } 
    l2 = (long)ScriptRuntime.toInteger(object2);
    if (l2 < 0L) {
      l4 = Math.max(l1 + l2, 0L);
    } else {
      l4 = Math.min(l2, l1);
    } 
    if (paramArrayOfObject.length >= 3 && !Undefined.isUndefined(paramArrayOfObject[2])) {
      l2 = (long)ScriptRuntime.toInteger(paramArrayOfObject[2]);
    } else {
      l2 = l1;
    } 
    if (l2 < 0L) {
      l2 = Math.max(l1 + l2, 0L);
    } else {
      l2 = Math.min(l2, l1);
    } 
    long l5 = Math.min(l2 - l4, l1 - l3);
    byte b1 = 1;
    l2 = l4;
    byte b2 = b1;
    long l6 = l3;
    if (l4 < l3) {
      l2 = l4;
      b2 = b1;
      l6 = l3;
      if (l3 < l4 + l5) {
        b2 = -1;
        l2 = l4 + l5 - 1L;
        l6 = l3 + l5 - 1L;
      } 
    } 
    if (scriptable instanceof NativeArray && l5 <= 2147483647L) {
      NativeArray nativeArray = (NativeArray)scriptable;
      if (nativeArray.denseOnly) {
        while (l5 > 0L) {
          arrayOfObject = nativeArray.dense;
          arrayOfObject[(int)l6] = arrayOfObject[(int)l2];
          l2 += b2;
          l6 += b2;
          l5--;
        } 
        return paramScriptable2;
      } 
    } 
    while (l5 > 0L) {
      object1 = getRawElem(scriptable, l2);
      if (object1 == Scriptable.NOT_FOUND || Undefined.isUndefined(object1)) {
        deleteElem(scriptable, l6);
      } else {
        setElem((Context)arrayOfObject, scriptable, l6, object1);
      } 
      l2 += b2;
      l6 += b2;
      l5--;
    } 
    return paramScriptable2;
  }
  
  private static Object js_fill(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    long l3;
    long l1 = getLengthProperty(paramContext, ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2), false);
    long l2 = 0L;
    if (paramArrayOfObject.length >= 2)
      l2 = (long)ScriptRuntime.toInteger(paramArrayOfObject[1]); 
    if (l2 < 0L) {
      l2 = Math.max(l1 + l2, 0L);
    } else {
      l2 = Math.min(l2, l1);
    } 
    if (paramArrayOfObject.length >= 3 && !Undefined.isUndefined(paramArrayOfObject[2])) {
      l3 = (long)ScriptRuntime.toInteger(paramArrayOfObject[2]);
    } else {
      l3 = l1;
    } 
    if (l3 < 0L) {
      l3 = Math.max(l1 + l3, 0L);
    } else {
      l3 = Math.min(l3, l1);
    } 
    if (paramArrayOfObject.length > 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    while (l2 < l3) {
      setRawElem(paramContext, paramScriptable2, l2, object);
      l2++;
    } 
    return paramScriptable2;
  }
  
  private static Object js_from(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_3
    //   1: arraylength
    //   2: iconst_1
    //   3: if_icmplt -> 14
    //   6: aload_3
    //   7: iconst_0
    //   8: aaload
    //   9: astore #4
    //   11: goto -> 19
    //   14: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   17: astore #4
    //   19: aload_1
    //   20: aload #4
    //   22: invokestatic toObject : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;
    //   25: astore #5
    //   27: aload_3
    //   28: arraylength
    //   29: iconst_2
    //   30: if_icmplt -> 41
    //   33: aload_3
    //   34: iconst_1
    //   35: aaload
    //   36: astore #4
    //   38: goto -> 46
    //   41: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   44: astore #4
    //   46: getstatic com/trendmicro/hippo/Undefined.SCRIPTABLE_UNDEFINED : Lcom/trendmicro/hippo/Scriptable;
    //   49: astore #6
    //   51: aload #4
    //   53: invokestatic isUndefined : (Ljava/lang/Object;)Z
    //   56: iconst_1
    //   57: ixor
    //   58: istore #7
    //   60: iload #7
    //   62: ifeq -> 109
    //   65: aload #4
    //   67: instanceof com/trendmicro/hippo/Function
    //   70: ifeq -> 102
    //   73: aload #4
    //   75: checkcast com/trendmicro/hippo/Function
    //   78: astore #4
    //   80: aload_3
    //   81: arraylength
    //   82: iconst_3
    //   83: if_icmplt -> 96
    //   86: aload_3
    //   87: iconst_2
    //   88: aaload
    //   89: invokestatic ensureScriptable : (Ljava/lang/Object;)Lcom/trendmicro/hippo/Scriptable;
    //   92: astore_3
    //   93: goto -> 115
    //   96: aload #6
    //   98: astore_3
    //   99: goto -> 115
    //   102: ldc_w 'msg.map.function.not'
    //   105: invokestatic typeError0 : (Ljava/lang/String;)Lcom/trendmicro/hippo/EcmaError;
    //   108: athrow
    //   109: aload #6
    //   111: astore_3
    //   112: aconst_null
    //   113: astore #4
    //   115: aload #5
    //   117: getstatic com/trendmicro/hippo/SymbolKey.ITERATOR : Lcom/trendmicro/hippo/SymbolKey;
    //   120: invokestatic getProperty : (Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Symbol;)Ljava/lang/Object;
    //   123: astore #6
    //   125: aload #5
    //   127: instanceof com/trendmicro/hippo/NativeArray
    //   130: ifne -> 341
    //   133: aload #6
    //   135: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
    //   138: if_acmpeq -> 341
    //   141: aload #6
    //   143: invokestatic isUndefined : (Ljava/lang/Object;)Z
    //   146: ifne -> 338
    //   149: aload #5
    //   151: aload_0
    //   152: aload_1
    //   153: invokestatic callIterator : (Ljava/lang/Object;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   156: astore #6
    //   158: aload #6
    //   160: invokestatic isUndefined : (Ljava/lang/Object;)Z
    //   163: ifne -> 335
    //   166: aload_0
    //   167: aload_1
    //   168: aload_2
    //   169: lconst_0
    //   170: iconst_0
    //   171: invokestatic callConstructorOrCreateArray : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;JZ)Lcom/trendmicro/hippo/Scriptable;
    //   174: astore #8
    //   176: lconst_0
    //   177: lstore #9
    //   179: new com/trendmicro/hippo/IteratorLikeIterable
    //   182: dup
    //   183: aload_0
    //   184: aload_1
    //   185: aload #6
    //   187: invokespecial <init> : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
    //   190: astore #5
    //   192: aload #4
    //   194: astore #6
    //   196: aload_3
    //   197: astore #6
    //   199: aload #5
    //   201: invokevirtual iterator : ()Lcom/trendmicro/hippo/IteratorLikeIterable$Itr;
    //   204: astore #11
    //   206: aload_3
    //   207: astore_2
    //   208: aload #4
    //   210: astore_3
    //   211: aload_3
    //   212: astore #6
    //   214: aload_2
    //   215: astore #6
    //   217: aload #11
    //   219: invokeinterface hasNext : ()Z
    //   224: ifeq -> 301
    //   227: aload_3
    //   228: astore #6
    //   230: aload_2
    //   231: astore #6
    //   233: aload #11
    //   235: invokeinterface next : ()Ljava/lang/Object;
    //   240: astore #4
    //   242: iload #7
    //   244: ifeq -> 278
    //   247: aload_3
    //   248: aload_0
    //   249: aload_1
    //   250: aload_2
    //   251: iconst_2
    //   252: anewarray java/lang/Object
    //   255: dup
    //   256: iconst_0
    //   257: aload #4
    //   259: aastore
    //   260: dup
    //   261: iconst_1
    //   262: lload #9
    //   264: invokestatic valueOf : (J)Ljava/lang/Long;
    //   267: aastore
    //   268: invokeinterface call : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   273: astore #4
    //   275: goto -> 278
    //   278: aload_0
    //   279: aload #8
    //   281: lload #9
    //   283: aload #4
    //   285: invokestatic defineElem : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;JLjava/lang/Object;)V
    //   288: lload #9
    //   290: lconst_1
    //   291: ladd
    //   292: lstore #9
    //   294: goto -> 211
    //   297: astore_0
    //   298: goto -> 319
    //   301: aload #5
    //   303: invokevirtual close : ()V
    //   306: aload_0
    //   307: aload #8
    //   309: lload #9
    //   311: invokestatic setLengthProperty : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;J)Ljava/lang/Object;
    //   314: pop
    //   315: aload #8
    //   317: areturn
    //   318: astore_0
    //   319: aload #5
    //   321: invokevirtual close : ()V
    //   324: goto -> 333
    //   327: astore_1
    //   328: aload_0
    //   329: aload_1
    //   330: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
    //   333: aload_0
    //   334: athrow
    //   335: goto -> 341
    //   338: goto -> 341
    //   341: aload_0
    //   342: aload #5
    //   344: iconst_0
    //   345: invokestatic getLengthProperty : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Z)J
    //   348: lstore #12
    //   350: lload #12
    //   352: lstore #9
    //   354: aload_0
    //   355: aload_1
    //   356: aload_2
    //   357: lload #12
    //   359: iconst_1
    //   360: invokestatic callConstructorOrCreateArray : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;JZ)Lcom/trendmicro/hippo/Scriptable;
    //   363: astore #6
    //   365: lconst_0
    //   366: lstore #12
    //   368: lload #12
    //   370: lload #9
    //   372: lcmp
    //   373: ifge -> 447
    //   376: aload #5
    //   378: lload #12
    //   380: invokestatic getRawElem : (Lcom/trendmicro/hippo/Scriptable;J)Ljava/lang/Object;
    //   383: astore_2
    //   384: aload_2
    //   385: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
    //   388: if_acmpeq -> 438
    //   391: iload #7
    //   393: ifeq -> 426
    //   396: aload #4
    //   398: aload_0
    //   399: aload_1
    //   400: aload_3
    //   401: iconst_2
    //   402: anewarray java/lang/Object
    //   405: dup
    //   406: iconst_0
    //   407: aload_2
    //   408: aastore
    //   409: dup
    //   410: iconst_1
    //   411: lload #12
    //   413: invokestatic valueOf : (J)Ljava/lang/Long;
    //   416: aastore
    //   417: invokeinterface call : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   422: astore_2
    //   423: goto -> 426
    //   426: aload_0
    //   427: aload #6
    //   429: lload #12
    //   431: aload_2
    //   432: invokestatic defineElem : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;JLjava/lang/Object;)V
    //   435: goto -> 438
    //   438: lload #12
    //   440: lconst_1
    //   441: ladd
    //   442: lstore #12
    //   444: goto -> 368
    //   447: aload_0
    //   448: aload #6
    //   450: lload #9
    //   452: invokestatic setLengthProperty : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;J)Ljava/lang/Object;
    //   455: pop
    //   456: aload #6
    //   458: areturn
    // Exception table:
    //   from	to	target	type
    //   199	206	318	finally
    //   217	227	318	finally
    //   233	242	318	finally
    //   247	275	297	finally
    //   278	288	297	finally
    //   319	324	327	finally
  }
  
  private static Boolean js_includes(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    long l2;
    if (paramArrayOfObject.length > 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    Scriptable scriptable = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
    long l1 = ScriptRuntime.toLength(new Object[] { getProperty(paramScriptable2, "length") }0);
    if (l1 == 0L)
      return Boolean.FALSE; 
    if (paramArrayOfObject.length < 2) {
      l2 = 0L;
    } else {
      long l = (long)ScriptRuntime.toInteger(paramArrayOfObject[1]);
      if (l < 0L) {
        l += l1;
        if (l < 0L)
          l = 0L; 
      } 
      l2 = l;
      if (l > l1 - 1L)
        return Boolean.FALSE; 
    } 
    long l3 = l2;
    if (scriptable instanceof NativeArray) {
      paramScriptable2 = scriptable;
      l3 = l2;
      if (((NativeArray)paramScriptable2).denseOnly) {
        Scriptable scriptable1 = paramScriptable2.getPrototype();
        for (int i = (int)l2; i < l1; i++) {
          Object object2 = ((NativeArray)paramScriptable2).dense[i];
          Object object1 = object2;
          if (object2 == NOT_FOUND) {
            object1 = object2;
            if (scriptable1 != null)
              object1 = ScriptableObject.getProperty(scriptable1, i); 
          } 
          object2 = object1;
          if (object1 == NOT_FOUND)
            object2 = Undefined.instance; 
          if (ScriptRuntime.sameZero(object2, object))
            return Boolean.TRUE; 
        } 
        return Boolean.FALSE;
      } 
    } 
    while (l3 < l1) {
      Object object2 = getRawElem(scriptable, l3);
      Object object1 = object2;
      if (object2 == NOT_FOUND)
        object1 = Undefined.instance; 
      if (ScriptRuntime.sameZero(object1, object))
        return Boolean.TRUE; 
      l3++;
    } 
    return Boolean.FALSE;
  }
  
  private static Object js_indexOf(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object1;
    Object object2;
    long l2;
    if (paramArrayOfObject.length > 0) {
      object2 = paramArrayOfObject[0];
    } else {
      object2 = Undefined.instance;
    } 
    paramScriptable1 = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
    long l1 = getLengthProperty(paramContext, paramScriptable1, false);
    if (paramArrayOfObject.length < 2) {
      l2 = 0L;
    } else {
      l2 = (long)ScriptRuntime.toInteger(paramArrayOfObject[1]);
      long l = l2;
      if (l2 < 0L) {
        l2 += l1;
        l = l2;
        if (l2 < 0L)
          l = 0L; 
      } 
      l2 = l;
      if (l > l1 - 1L)
        return NEGATIVE_ONE; 
    } 
    if (paramScriptable1 instanceof NativeArray) {
      paramScriptable2 = paramScriptable1;
      if (((NativeArray)paramScriptable2).denseOnly) {
        Scriptable scriptable = paramScriptable2.getPrototype();
        for (int i = (int)l2; i < l1; i++) {
          object1 = ((NativeArray)paramScriptable2).dense[i];
          Object object = object1;
          if (object1 == NOT_FOUND) {
            object = object1;
            if (scriptable != null)
              object = ScriptableObject.getProperty(scriptable, i); 
          } 
          if (object != NOT_FOUND && ScriptRuntime.shallowEq(object, object2))
            return Long.valueOf(i); 
        } 
        return NEGATIVE_ONE;
      } 
    } 
    while (l2 < l1) {
      Object object = getRawElem((Scriptable)object1, l2);
      if (object != NOT_FOUND && ScriptRuntime.shallowEq(object, object2))
        return Long.valueOf(l2); 
      l2++;
    } 
    return NEGATIVE_ONE;
  }
  
  private static boolean js_isArray(Object paramObject) {
    return !(paramObject instanceof Scriptable) ? false : "Array".equals(((Scriptable)paramObject).getClassName());
  }
  
  private static String js_join(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    long l = getLengthProperty(paramContext, paramScriptable, false);
    int i = (int)l;
    if (l == i) {
      Object object;
      String str;
      if (paramArrayOfObject.length < 1 || paramArrayOfObject[0] == Undefined.instance) {
        str = ",";
      } else {
        str = ScriptRuntime.toString(str[0]);
      } 
      if (paramScriptable instanceof NativeArray) {
        NativeArray nativeArray = (NativeArray)paramScriptable;
        if (nativeArray.denseOnly) {
          stringBuilder = new StringBuilder();
          for (byte b = 0; b < i; b++) {
            if (b != 0)
              stringBuilder.append(str); 
            object = nativeArray.dense;
            if (b < object.length) {
              object = object[b];
              if (object != null && object != Undefined.instance && object != Scriptable.NOT_FOUND)
                stringBuilder.append(ScriptRuntime.toString(object)); 
            } 
          } 
          return stringBuilder.toString();
        } 
      } 
      if (i == 0)
        return ""; 
      String[] arrayOfString = new String[i];
      int k = 0;
      int j = 0;
      while (j != i) {
        Object object1 = getElem((Context)stringBuilder, (Scriptable)object, j);
        int m = k;
        if (object1 != null) {
          m = k;
          if (object1 != Undefined.instance) {
            object1 = ScriptRuntime.toString(object1);
            m = k + object1.length();
            arrayOfString[j] = (String)object1;
          } 
        } 
        j++;
        k = m;
      } 
      StringBuilder stringBuilder = new StringBuilder(k + (i - 1) * str.length());
      for (j = 0; j != i; j++) {
        if (j != 0)
          stringBuilder.append(str); 
        object = arrayOfString[j];
        if (object != null)
          stringBuilder.append((String)object); 
      } 
      return stringBuilder.toString();
    } 
    throw Context.reportRuntimeError1("msg.arraylength.too.big", String.valueOf(l));
  }
  
  private static Object js_lastIndexOf(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object1;
    Object object2;
    long l2;
    if (paramArrayOfObject.length > 0) {
      object2 = paramArrayOfObject[0];
    } else {
      object2 = Undefined.instance;
    } 
    paramScriptable1 = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
    long l1 = getLengthProperty(paramContext, paramScriptable1, false);
    if (paramArrayOfObject.length < 2) {
      l2 = l1 - 1L;
    } else {
      long l;
      l2 = (long)ScriptRuntime.toInteger(paramArrayOfObject[1]);
      if (l2 >= l1) {
        l = l1 - 1L;
      } else {
        l = l2;
        if (l2 < 0L)
          l = l2 + l1; 
      } 
      l2 = l;
      if (l < 0L)
        return NEGATIVE_ONE; 
    } 
    if (paramScriptable1 instanceof NativeArray) {
      paramScriptable2 = paramScriptable1;
      if (((NativeArray)paramScriptable2).denseOnly) {
        Scriptable scriptable = paramScriptable2.getPrototype();
        for (int i = (int)l2; i >= 0; i--) {
          object1 = ((NativeArray)paramScriptable2).dense[i];
          Object object = object1;
          if (object1 == NOT_FOUND) {
            object = object1;
            if (scriptable != null)
              object = ScriptableObject.getProperty(scriptable, i); 
          } 
          if (object != NOT_FOUND && ScriptRuntime.shallowEq(object, object2))
            return Long.valueOf(i); 
        } 
        return NEGATIVE_ONE;
      } 
    } 
    while (l2 >= 0L) {
      Object object = getRawElem((Scriptable)object1, l2);
      if (object != NOT_FOUND && ScriptRuntime.shallowEq(object, object2))
        return Long.valueOf(l2); 
      l2--;
    } 
    return NEGATIVE_ONE;
  }
  
  private static Object js_of(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    paramScriptable1 = callConstructorOrCreateArray(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject.length, true);
    for (byte b = 0; b < paramArrayOfObject.length; b++)
      defineElem(paramContext, paramScriptable1, b, paramArrayOfObject[b]); 
    setLengthProperty(paramContext, paramScriptable1, paramArrayOfObject.length);
    return paramScriptable1;
  }
  
  private static Object js_pop(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    Object object1;
    Object object2;
    if (paramScriptable instanceof NativeArray) {
      object2 = paramScriptable;
      if (((NativeArray)object2).denseOnly) {
        long l1 = ((NativeArray)object2).length;
        if (l1 > 0L) {
          ((NativeArray)object2).length = --l1;
          arrayOfObject = ((NativeArray)object2).dense;
          object1 = arrayOfObject[(int)l1];
          arrayOfObject[(int)l1] = NOT_FOUND;
          return object1;
        } 
      } 
    } 
    long l = getLengthProperty((Context)arrayOfObject, (Scriptable)object1, false);
    if (l > 0L) {
      l--;
      object2 = getElem((Context)arrayOfObject, (Scriptable)object1, l);
      deleteElem((Scriptable)object1, l);
    } else {
      object2 = Undefined.instance;
    } 
    setLengthProperty((Context)arrayOfObject, (Scriptable)object1, l);
    return object2;
  }
  
  private static Object js_push(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    if (paramScriptable instanceof NativeArray) {
      NativeArray nativeArray = (NativeArray)paramScriptable;
      if (nativeArray.denseOnly && nativeArray.ensureCapacity((int)nativeArray.length + paramArrayOfObject.length)) {
        for (byte b1 = 0; b1 < paramArrayOfObject.length; b1++) {
          arrayOfObject = nativeArray.dense;
          long l1 = nativeArray.length;
          nativeArray.length = 1L + l1;
          arrayOfObject[(int)l1] = paramArrayOfObject[b1];
        } 
        return ScriptRuntime.wrapNumber(nativeArray.length);
      } 
    } 
    long l = getLengthProperty((Context)arrayOfObject, paramScriptable, false);
    for (byte b = 0; b < paramArrayOfObject.length; b++)
      setElem((Context)arrayOfObject, paramScriptable, b + l, paramArrayOfObject[b]); 
    Object object = setLengthProperty((Context)arrayOfObject, paramScriptable, l + paramArrayOfObject.length);
    if (arrayOfObject.getLanguageVersion() == 120) {
      Object object1;
      if (paramArrayOfObject.length == 0) {
        object1 = Undefined.instance;
      } else {
        object1 = paramArrayOfObject[paramArrayOfObject.length - 1];
      } 
      return object1;
    } 
    return object;
  }
  
  private static Scriptable js_reverse(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object object;
    if (paramScriptable instanceof NativeArray) {
      NativeArray nativeArray = (NativeArray)paramScriptable;
      if (nativeArray.denseOnly) {
        byte b = 0;
        for (int i = (int)nativeArray.length - 1; b < i; i--) {
          Object[] arrayOfObject = nativeArray.dense;
          object = arrayOfObject[b];
          arrayOfObject[b] = arrayOfObject[i];
          arrayOfObject[i] = object;
          b++;
        } 
        return paramScriptable;
      } 
    } 
    long l1 = getLengthProperty((Context)object, paramScriptable, false);
    long l2 = l1 / 2L;
    long l3;
    for (l3 = 0L; l3 < l2; l3++) {
      long l = l1 - l3 - 1L;
      Object object1 = getRawElem(paramScriptable, l3);
      setRawElem((Context)object, paramScriptable, l3, getRawElem(paramScriptable, l));
      setRawElem((Context)object, paramScriptable, l, object1);
    } 
    return paramScriptable;
  }
  
  private static Object js_shift(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object object1;
    Object[] arrayOfObject;
    Object object2;
    if (paramScriptable instanceof NativeArray) {
      object2 = paramScriptable;
      if (((NativeArray)object2).denseOnly) {
        long l1 = ((NativeArray)object2).length;
        if (l1 > 0L) {
          ((NativeArray)object2).length = --l1;
          arrayOfObject = ((NativeArray)object2).dense;
          object1 = arrayOfObject[0];
          System.arraycopy(arrayOfObject, 1, arrayOfObject, 0, (int)l1);
          ((NativeArray)object2).dense[(int)((NativeArray)object2).length] = NOT_FOUND;
          if (object1 == NOT_FOUND)
            object1 = Undefined.instance; 
          return object1;
        } 
      } 
    } 
    long l = getLengthProperty((Context)object1, (Scriptable)arrayOfObject, false);
    if (l > 0L) {
      long l1 = l - 1L;
      object2 = getElem((Context)object1, (Scriptable)arrayOfObject, 0L);
      if (l1 > 0L)
        for (l = 1L; l <= l1; l++)
          setRawElem((Context)object1, (Scriptable)arrayOfObject, l - 1L, getRawElem((Scriptable)arrayOfObject, l));  
      deleteElem((Scriptable)arrayOfObject, l1);
      l = l1;
    } else {
      object2 = Undefined.instance;
    } 
    setLengthProperty((Context)object1, (Scriptable)arrayOfObject, l);
    return object2;
  }
  
  private Scriptable js_slice(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    long l3;
    Scriptable scriptable = paramContext.newArray(getTopLevelScope(this), 0);
    long l1 = getLengthProperty(paramContext, paramScriptable, false);
    if (paramArrayOfObject.length == 0) {
      long l = 0L;
      l3 = l1;
      l1 = l;
    } else {
      long l = toSliceIndex(ScriptRuntime.toInteger(paramArrayOfObject[0]), l1);
      if (paramArrayOfObject.length == 1 || paramArrayOfObject[1] == Undefined.instance) {
        l3 = l1;
        l1 = l;
      } else {
        l3 = toSliceIndex(ScriptRuntime.toInteger(paramArrayOfObject[1]), l1);
        l1 = l;
      } 
    } 
    long l2;
    for (l2 = l1; l2 < l3; l2++) {
      Object object = getRawElem(paramScriptable, l2);
      if (object != NOT_FOUND)
        defineElem(paramContext, scriptable, l2 - l1, object); 
    } 
    setLengthProperty(paramContext, scriptable, Math.max(0L, l3 - l1));
    return scriptable;
  }
  
  private static Scriptable js_sort(final Context cx, final Scriptable scope, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Comparator<Object> comparator;
    if (paramArrayOfObject.length > 0 && Undefined.instance != paramArrayOfObject[0]) {
      final Callable jsCompareFunction = ScriptRuntime.getValueFunctionAndThis(paramArrayOfObject[0], cx);
      final Scriptable funThis = ScriptRuntime.lastStoredScriptable(cx);
      comparator = new ElementComparator(new Comparator() {
            public int compare(Object param1Object1, Object param1Object2) {
              Object[] arrayOfObject = cmpBuf;
              arrayOfObject[0] = param1Object1;
              arrayOfObject[1] = param1Object2;
              double d = ScriptRuntime.toNumber(jsCompareFunction.call(cx, scope, funThis, arrayOfObject));
              return (d < 0.0D) ? -1 : ((d > 0.0D) ? 1 : 0);
            }
          });
    } else {
      comparator = DEFAULT_COMPARATOR;
    } 
    long l = getLengthProperty(cx, paramScriptable2, false);
    int i = (int)l;
    if (l == i) {
      paramArrayOfObject = new Object[i];
      int j;
      for (j = 0; j != i; j++)
        paramArrayOfObject[j] = getRawElem(paramScriptable2, j); 
      Sorting.get().hybridSort(paramArrayOfObject, comparator);
      for (j = 0; j < i; j++)
        setRawElem(cx, paramScriptable2, j, paramArrayOfObject[j]); 
      return paramScriptable2;
    } 
    throw Context.reportRuntimeError1("msg.arraylength.too.big", String.valueOf(l));
  }
  
  private static Object js_splice(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    long l3;
    NativeArray nativeArray = null;
    boolean bool = false;
    if (paramScriptable2 instanceof NativeArray) {
      nativeArray = (NativeArray)paramScriptable2;
      bool = nativeArray.denseOnly;
    } 
    Object object = getTopLevelScope(paramScriptable1);
    int i = paramArrayOfObject.length;
    if (i == 0)
      return paramContext.newArray((Scriptable)object, 0); 
    long l1 = getLengthProperty(paramContext, paramScriptable2, false);
    long l2 = toSliceIndex(ScriptRuntime.toInteger(paramArrayOfObject[0]), l1);
    i--;
    if (paramArrayOfObject.length == 1) {
      l3 = l1 - l2;
    } else {
      double d = ScriptRuntime.toInteger(paramArrayOfObject[1]);
      if (d < 0.0D) {
        l3 = 0L;
      } else if (d > (l1 - l2)) {
        l3 = l1 - l2;
      } else {
        l3 = (long)d;
      } 
      i--;
    } 
    long l4 = l2 + l3;
    if (l3 != 0L) {
      if (l3 == 1L && paramContext.getLanguageVersion() == 120) {
        Object object1 = getElem(paramContext, paramScriptable2, l2);
      } else if (bool) {
        int k = (int)(l4 - l2);
        Object[] arrayOfObject1 = new Object[k];
        System.arraycopy(nativeArray.dense, (int)l2, arrayOfObject1, 0, k);
        Scriptable scriptable = paramContext.newArray((Scriptable)object, arrayOfObject1);
      } else {
        paramScriptable1 = paramContext.newArray((Scriptable)object, 0);
        long l;
        for (l = l2; l != l4; l++) {
          object = getRawElem(paramScriptable2, l);
          if (object != NOT_FOUND)
            setElem(paramContext, paramScriptable1, l - l2, object); 
        } 
        setLengthProperty(paramContext, paramScriptable1, l4 - l2);
      } 
    } else if (paramContext.getLanguageVersion() == 120) {
      Object object1 = Undefined.instance;
    } else {
      paramScriptable1 = paramContext.newArray((Scriptable)object, 0);
    } 
    long l5 = l1;
    long l6 = i - l3;
    if (bool && l5 + l6 < 2147483647L) {
      int k = (int)(l5 + l6);
      object = nativeArray;
      if (object.ensureCapacity(k)) {
        arrayOfObject = ((NativeArray)object).dense;
        System.arraycopy(arrayOfObject, (int)l4, arrayOfObject, (int)(i + l2), (int)(l5 - l4));
        if (i > 0)
          System.arraycopy(paramArrayOfObject, 2, ((NativeArray)object).dense, (int)l2, i); 
        if (l6 < 0L)
          Arrays.fill(((NativeArray)object).dense, (int)(l5 + l6), (int)l5, NOT_FOUND); 
        ((NativeArray)object).length = l5 + l6;
        return paramScriptable1;
      } 
    } 
    l1 = l5;
    if (l6 > 0L) {
      for (l3 = l1 - 1L; l3 >= l4; l3--)
        setRawElem((Context)arrayOfObject, paramScriptable2, l3 + l6, getRawElem(paramScriptable2, l3)); 
      l3 = l2;
    } else {
      Scriptable scriptable = paramScriptable2;
      l3 = l2;
      if (l6 < 0L) {
        for (l3 = l4; l3 < l1; l3++)
          setRawElem((Context)arrayOfObject, scriptable, l3 + l6, getRawElem(scriptable, l3)); 
        l5 = l1 - 1L;
        while (true) {
          l3 = l2;
          if (l5 >= l1 + l6) {
            deleteElem(scriptable, l5);
            l5--;
            continue;
          } 
          break;
        } 
      } 
    } 
    int j = paramArrayOfObject.length;
    for (byte b = 0; b < i; b++)
      setElem((Context)arrayOfObject, paramScriptable2, l3 + b, paramArrayOfObject[b + j - i]); 
    setLengthProperty((Context)arrayOfObject, paramScriptable2, l1 + l6);
    return paramScriptable1;
  }
  
  private static Object js_unshift(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    if (paramScriptable instanceof NativeArray) {
      NativeArray nativeArray = (NativeArray)paramScriptable;
      if (nativeArray.denseOnly && nativeArray.ensureCapacity((int)nativeArray.length + paramArrayOfObject.length)) {
        arrayOfObject = nativeArray.dense;
        System.arraycopy(arrayOfObject, 0, arrayOfObject, paramArrayOfObject.length, (int)nativeArray.length);
        for (byte b = 0; b < paramArrayOfObject.length; b++)
          nativeArray.dense[b] = paramArrayOfObject[b]; 
        long l1 = nativeArray.length + paramArrayOfObject.length;
        nativeArray.length = l1;
        return ScriptRuntime.wrapNumber(l1);
      } 
    } 
    long l = getLengthProperty((Context)arrayOfObject, paramScriptable, false);
    int i = paramArrayOfObject.length;
    if (paramArrayOfObject.length > 0) {
      if (l > 0L) {
        long l1;
        for (l1 = l - 1L; l1 >= 0L; l1--) {
          Object object = getRawElem(paramScriptable, l1);
          setRawElem((Context)arrayOfObject, paramScriptable, i + l1, object);
        } 
      } 
      for (i = 0; i < paramArrayOfObject.length; i++)
        setElem((Context)arrayOfObject, paramScriptable, i, paramArrayOfObject[i]); 
    } 
    return setLengthProperty((Context)arrayOfObject, paramScriptable, l + paramArrayOfObject.length);
  }
  
  private static Object reduceMethod(Context paramContext, int paramInt, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    Scriptable scriptable = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
    long l = getLengthProperty(paramContext, scriptable, false);
    if (paramArrayOfObject.length > 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    if (object != null && object instanceof Function) {
      Function function = (Function)object;
      Scriptable scriptable1 = ScriptableObject.getTopLevelScope(function);
      if (paramInt == 24) {
        paramInt = 1;
      } else {
        paramInt = 0;
      } 
      if (paramArrayOfObject.length > 1) {
        object = paramArrayOfObject[1];
      } else {
        object = Scriptable.NOT_FOUND;
      } 
      long l1;
      for (l1 = 0L; l1 < l; l1++) {
        long l2;
        if (paramInt != 0) {
          l2 = l1;
        } else {
          l2 = l - 1L - l1;
        } 
        Object object1 = getRawElem(scriptable, l2);
        if (object1 != Scriptable.NOT_FOUND)
          if (object == Scriptable.NOT_FOUND) {
            object = object1;
          } else {
            object = function.call(paramContext, scriptable1, scriptable1, new Object[] { object, object1, Long.valueOf(l2), scriptable });
          }  
      } 
      if (object != Scriptable.NOT_FOUND)
        return object; 
      throw ScriptRuntime.typeError0("msg.empty.array.reduce");
    } 
    throw ScriptRuntime.notFunctionError(object);
  }
  
  private static void setElem(Context paramContext, Scriptable paramScriptable, long paramLong, Object paramObject) {
    if (paramLong > 2147483647L) {
      ScriptableObject.putProperty(paramScriptable, Long.toString(paramLong), paramObject);
    } else {
      ScriptableObject.putProperty(paramScriptable, (int)paramLong, paramObject);
    } 
  }
  
  private void setLength(Object paramObject) {
    if ((this.lengthAttr & 0x1) != 0)
      return; 
    double d = ScriptRuntime.toNumber(paramObject);
    long l = ScriptRuntime.toUint32(d);
    if (l == d) {
      if (this.denseOnly) {
        long l2 = this.length;
        if (l < l2) {
          paramObject = this.dense;
          Arrays.fill((Object[])paramObject, (int)l, paramObject.length, NOT_FOUND);
          this.length = l;
          return;
        } 
        if (l < 1431655764L && l < l2 * 1.5D && ensureCapacity((int)l)) {
          this.length = l;
          return;
        } 
        this.denseOnly = false;
      } 
      long l1 = this.length;
      if (l < l1)
        if (l1 - l > 4096L) {
          paramObject = getIds();
          for (byte b = 0; b < paramObject.length; b++) {
            Object object = paramObject[b];
            if (object instanceof String) {
              object = object;
              if (toArrayIndex((String)object) >= l)
                delete((String)object); 
            } else {
              int i = ((Integer)object).intValue();
              if (i >= l)
                delete(i); 
            } 
          } 
        } else {
          for (l1 = l; l1 < this.length; l1++)
            deleteElem(this, l1); 
        }  
      this.length = l;
      return;
    } 
    throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.arraylength.bad"));
  }
  
  private static Object setLengthProperty(Context paramContext, Scriptable paramScriptable, long paramLong) {
    Number number = ScriptRuntime.wrapNumber(paramLong);
    ScriptableObject.putProperty(paramScriptable, "length", number);
    return number;
  }
  
  static void setMaximumInitialCapacity(int paramInt) {
    maximumInitialCapacity = paramInt;
  }
  
  private static void setRawElem(Context paramContext, Scriptable paramScriptable, long paramLong, Object paramObject) {
    if (paramObject == NOT_FOUND) {
      deleteElem(paramScriptable, paramLong);
    } else {
      setElem(paramContext, paramScriptable, paramLong, paramObject);
    } 
  }
  
  private static long toArrayIndex(double paramDouble) {
    if (paramDouble == paramDouble) {
      long l = ScriptRuntime.toUint32(paramDouble);
      if (l == paramDouble && l != 4294967295L)
        return l; 
    } 
    return -1L;
  }
  
  private static long toArrayIndex(Object paramObject) {
    return (paramObject instanceof String) ? toArrayIndex((String)paramObject) : ((paramObject instanceof Number) ? toArrayIndex(((Number)paramObject).doubleValue()) : -1L);
  }
  
  private static long toArrayIndex(String paramString) {
    long l = toArrayIndex(ScriptRuntime.toNumber(paramString));
    return Long.toString(l).equals(paramString) ? l : -1L;
  }
  
  private static int toDenseIndex(Object paramObject) {
    byte b;
    long l = toArrayIndex(paramObject);
    if (0L <= l && l < 2147483647L) {
      b = (int)l;
    } else {
      b = -1;
    } 
    return b;
  }
  
  private static long toSliceIndex(double paramDouble, long paramLong) {
    if (paramDouble < 0.0D) {
      if (paramLong + paramDouble < 0.0D) {
        paramLong = 0L;
      } else {
        paramLong = (long)(paramLong + paramDouble);
      } 
    } else if (paramDouble <= paramLong) {
      paramLong = (long)paramDouble;
    } 
    return paramLong;
  }
  
  private static String toStringHelper(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, boolean paramBoolean1, boolean paramBoolean2) {
    String str;
    boolean bool3;
    boolean bool;
    long l1 = getLengthProperty(paramContext, paramScriptable2, false);
    StringBuilder stringBuilder = new StringBuilder(256);
    if (paramBoolean1) {
      stringBuilder.append('[');
      str = ", ";
    } else {
      str = ",";
    } 
    boolean bool1 = false;
    boolean bool2 = false;
    long l2 = 0L;
    if (paramContext.iterating == null) {
      bool3 = true;
      bool = false;
      paramContext.iterating = new ObjToIntMap(31);
    } else {
      bool3 = false;
      bool = paramContext.iterating.has(paramScriptable2);
    } 
    if (!bool) {
      try {
        paramContext.iterating.put(paramScriptable2, 0);
        if (!paramBoolean1 || paramContext.getLanguageVersion() < 150) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
      } finally {
        if (bool3)
          paramContext.iterating = null; 
      } 
    } else {
      bool2 = bool1;
    } 
    if (bool3)
      paramContext.iterating = null; 
    if (paramBoolean1)
      if (!bool2 && l2 > 0L) {
        stringBuilder.append(", ]");
      } else {
        stringBuilder.append(']');
      }  
    return stringBuilder.toString();
  }
  
  public void add(int paramInt, Object paramObject) {
    throw new UnsupportedOperationException();
  }
  
  public boolean add(Object paramObject) {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(int paramInt, Collection paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(Collection paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  public boolean contains(Object paramObject) {
    boolean bool;
    if (indexOf(paramObject) > -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean containsAll(Collection paramCollection) {
    Iterator iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      if (!contains(iterator.next()))
        return false; 
    } 
    return true;
  }
  
  protected void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject, boolean paramBoolean) {
    if (this.dense != null) {
      Object[] arrayOfObject = this.dense;
      this.dense = null;
      this.denseOnly = false;
      for (byte b = 0; b < arrayOfObject.length; b++) {
        if (arrayOfObject[b] != NOT_FOUND)
          put(b, this, arrayOfObject[b]); 
      } 
    } 
    long l = toArrayIndex(paramObject);
    if (l >= this.length)
      this.length = 1L + l; 
    super.defineOwnProperty(paramContext, paramObject, paramScriptableObject, paramBoolean);
  }
  
  public void delete(int paramInt) {
    Object[] arrayOfObject = this.dense;
    if (arrayOfObject != null && paramInt >= 0 && paramInt < arrayOfObject.length && !isSealed() && (this.denseOnly || !isGetterOrSetter((String)null, paramInt, true))) {
      this.dense[paramInt] = NOT_FOUND;
    } else {
      super.delete(paramInt);
    } 
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(ARRAY_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    Scriptable scriptable = paramScriptable2;
    while (true) {
      StringBuilder stringBuilder;
      Object[] arrayOfObject;
      boolean bool = true;
      byte b = 1;
      switch (i) {
        default:
          switch (i) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append("Array.prototype has no method: ");
              stringBuilder.append(paramIdFunctionObject.getFunctionName());
              throw new IllegalArgumentException(stringBuilder.toString());
            case 31:
              return js_copyWithin((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 30:
              return js_includes((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 29:
              return new NativeArrayIterator(paramScriptable1, ScriptRuntime.toObject((Context)stringBuilder, paramScriptable1, scriptable), NativeArrayIterator.ARRAY_ITERATOR_TYPE.ENTRIES);
            case 28:
            case 32:
              return new NativeArrayIterator(paramScriptable1, ScriptRuntime.toObject((Context)stringBuilder, paramScriptable1, scriptable), NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES);
            case 27:
              return new NativeArrayIterator(paramScriptable1, ScriptRuntime.toObject((Context)stringBuilder, paramScriptable1, scriptable), NativeArrayIterator.ARRAY_ITERATOR_TYPE.KEYS);
            case 26:
              return js_fill((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 24:
            case 25:
              return reduceMethod((Context)stringBuilder, i, paramScriptable1, scriptable, paramArrayOfObject);
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
              return iterativeMethod((Context)stringBuilder, paramIdFunctionObject, paramScriptable1, scriptable, paramArrayOfObject);
            case 16:
              return js_lastIndexOf((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 15:
              return js_indexOf((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 14:
              return js_slice((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 13:
              return js_concat((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 12:
              return js_splice((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 11:
              return js_unshift((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 10:
              return js_shift((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 9:
              return js_pop((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 8:
              return js_push((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 7:
              return js_sort((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
            case 6:
              return js_reverse((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 5:
              return js_join((Context)stringBuilder, scriptable, paramArrayOfObject);
            case 4:
              return toStringHelper((Context)stringBuilder, paramScriptable1, scriptable, true, false);
            case 3:
              return toStringHelper((Context)stringBuilder, paramScriptable1, scriptable, false, true);
            case 2:
              return toStringHelper((Context)stringBuilder, paramScriptable1, scriptable, stringBuilder.hasFeature(4), false);
            case 1:
              break;
          } 
          if (scriptable == null) {
            i = b;
          } else {
            i = 0;
          } 
          return (i == 0) ? paramIdFunctionObject.construct((Context)stringBuilder, paramScriptable1, paramArrayOfObject) : jsConstructor((Context)stringBuilder, paramScriptable1, paramArrayOfObject);
        case -25:
        case -24:
        case -23:
        case -22:
        case -21:
        case -20:
        case -19:
        case -18:
        case -17:
        case -16:
        case -15:
        case -14:
        case -13:
        case -12:
        case -11:
        case -10:
        case -9:
        case -8:
        case -7:
        case -6:
        case -5:
          arrayOfObject = paramArrayOfObject;
          if (paramArrayOfObject.length > 0) {
            scriptable = ScriptRuntime.toObject((Context)stringBuilder, paramScriptable1, paramArrayOfObject[0]);
            arrayOfObject = new Object[paramArrayOfObject.length - 1];
            for (b = 0; b < arrayOfObject.length; b++)
              arrayOfObject[b] = paramArrayOfObject[b + 1]; 
          } 
          i = -i;
          paramArrayOfObject = arrayOfObject;
          continue;
        case -26:
          if (paramArrayOfObject.length <= 0 || !js_isArray(paramArrayOfObject[0]))
            bool = false; 
          return Boolean.valueOf(bool);
        case -27:
          return js_of((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
        case -28:
          break;
      } 
      return js_from((Context)stringBuilder, paramScriptable1, scriptable, paramArrayOfObject);
    } 
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -5, "join", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -6, "reverse", 0);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -7, "sort", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -8, "push", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -9, "pop", 0);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -10, "shift", 0);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -11, "unshift", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -12, "splice", 2);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -13, "concat", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -14, "slice", 2);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -15, "indexOf", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -16, "lastIndexOf", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -17, "every", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -18, "filter", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -19, "forEach", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -20, "map", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -21, "some", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -22, "find", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -23, "findIndex", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -24, "reduce", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -25, "reduceRight", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -26, "isArray", 1);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -27, "of", 0);
    addIdFunctionProperty(paramIdFunctionObject, ARRAY_TAG, -28, "from", 1);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    return paramString.equals("length") ? instanceIdInfo(this.lengthAttr, 1) : super.findInstanceIdInfo(paramString);
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.ITERATOR.equals(paramSymbol) ? 32 : 0;
  }
  
  protected int findPrototypeId(String paramString) {
    String str2;
    int i = 0;
    String str1 = null;
    int j = paramString.length();
    if (j != 14) {
      char c;
      switch (j) {
        default:
          j = i;
          str2 = str1;
          break;
        case 11:
          c = paramString.charAt(0);
          if (c == 'c') {
            str2 = "constructor";
            j = 1;
            break;
          } 
          if (c == 'l') {
            str2 = "lastIndexOf";
            j = 16;
            break;
          } 
          j = i;
          str2 = str1;
          if (c == 'r') {
            str2 = "reduceRight";
            j = 25;
          } 
          break;
        case 10:
          str2 = "copyWithin";
          j = 31;
          break;
        case 9:
          str2 = "findIndex";
          j = 23;
          break;
        case 8:
          c = paramString.charAt(3);
          if (c == 'l') {
            str2 = "includes";
            j = 30;
            break;
          } 
          if (c == 'o') {
            str2 = "toSource";
            j = 4;
            break;
          } 
          j = i;
          str2 = str1;
          if (c == 't') {
            str2 = "toString";
            j = 2;
          } 
          break;
        case 7:
          j = paramString.charAt(0);
          if (j != 101) {
            if (j != 102) {
              if (j != 105) {
                if (j != 114) {
                  if (j != 117) {
                    j = i;
                    str2 = str1;
                    break;
                  } 
                  str2 = "unshift";
                  j = 11;
                  break;
                } 
                str2 = "reverse";
                j = 6;
                break;
              } 
              str2 = "indexOf";
              j = 15;
              break;
            } 
            str2 = "forEach";
            j = 19;
            break;
          } 
          str2 = "entries";
          j = 29;
          break;
        case 6:
          j = paramString.charAt(0);
          if (j != 99) {
            if (j != 102) {
              if (j != 118) {
                if (j != 114) {
                  if (j != 115) {
                    j = i;
                    str2 = str1;
                    break;
                  } 
                  str2 = "splice";
                  j = 12;
                  break;
                } 
                str2 = "reduce";
                j = 24;
                break;
              } 
              str2 = "values";
              j = 28;
              break;
            } 
            str2 = "filter";
            j = 18;
            break;
          } 
          str2 = "concat";
          j = 13;
          break;
        case 5:
          c = paramString.charAt(1);
          if (c == 'h') {
            str2 = "shift";
            j = 10;
            break;
          } 
          if (c == 'l') {
            str2 = "slice";
            j = 14;
            break;
          } 
          j = i;
          str2 = str1;
          if (c == 'v') {
            str2 = "every";
            j = 17;
          } 
          break;
        case 4:
          j = paramString.charAt(2);
          if (j != 105) {
            if (j != 121) {
              if (j != 114) {
                if (j != 115) {
                  switch (j) {
                    default:
                      j = i;
                      str2 = str1;
                      break;
                    case 110:
                      str2 = "find";
                      j = 22;
                      break;
                    case 109:
                      str2 = "some";
                      j = 21;
                      break;
                    case 108:
                      break;
                  } 
                  str2 = "fill";
                  j = 26;
                  break;
                } 
                str2 = "push";
                j = 8;
                break;
              } 
              str2 = "sort";
              j = 7;
              break;
            } 
            str2 = "keys";
            j = 27;
            break;
          } 
          str2 = "join";
          j = 5;
          break;
        case 3:
          c = paramString.charAt(0);
          if (c == 'm') {
            j = i;
            str2 = str1;
            if (paramString.charAt(2) == 'p') {
              j = i;
              str2 = str1;
              if (paramString.charAt(1) == 'a')
                return 20; 
            } 
            break;
          } 
          j = i;
          str2 = str1;
          if (c == 'p') {
            j = i;
            str2 = str1;
            if (paramString.charAt(2) == 'p') {
              j = i;
              str2 = str1;
              if (paramString.charAt(1) == 'o')
                return 9; 
            } 
          } 
          break;
      } 
    } else {
      str2 = "toLocaleString";
      j = 3;
    } 
    i = j;
    if (str2 != null) {
      i = j;
      if (str2 != paramString) {
        i = j;
        if (!str2.equals(paramString))
          i = 0; 
      } 
    } 
    return i;
  }
  
  public Object get(int paramInt) {
    return get(paramInt);
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    if (!this.denseOnly && isGetterOrSetter((String)null, paramInt, false))
      return super.get(paramInt, paramScriptable); 
    Object[] arrayOfObject = this.dense;
    return (arrayOfObject != null && paramInt >= 0 && paramInt < arrayOfObject.length) ? arrayOfObject[paramInt] : super.get(paramInt, paramScriptable);
  }
  
  public Object get(long paramLong) {
    if (paramLong >= 0L && paramLong < this.length) {
      Object object = getRawElem(this, paramLong);
      return (object == Scriptable.NOT_FOUND || object == Undefined.instance) ? null : ((object instanceof Wrapper) ? ((Wrapper)object).unwrap() : object);
    } 
    throw new IndexOutOfBoundsException();
  }
  
  public int getAttributes(int paramInt) {
    Object[] arrayOfObject = this.dense;
    return (arrayOfObject != null && paramInt >= 0 && paramInt < arrayOfObject.length && arrayOfObject[paramInt] != NOT_FOUND) ? 0 : super.getAttributes(paramInt);
  }
  
  public String getClassName() {
    return "Array";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return (paramClass == ScriptRuntime.NumberClass && Context.getContext().getLanguageVersion() == 120) ? Long.valueOf(this.length) : super.getDefaultValue(paramClass);
  }
  
  public Object[] getIds(boolean paramBoolean1, boolean paramBoolean2) {
    Object[] arrayOfObject1 = super.getIds(paramBoolean1, paramBoolean2);
    Object[] arrayOfObject2 = this.dense;
    if (arrayOfObject2 == null)
      return arrayOfObject1; 
    int i = arrayOfObject2.length;
    long l = this.length;
    int j = i;
    if (i > l)
      j = (int)l; 
    if (j == 0)
      return arrayOfObject1; 
    int k = arrayOfObject1.length;
    Object[] arrayOfObject3 = new Object[j + k];
    int m = 0;
    i = 0;
    while (i != j) {
      int n = m;
      if (this.dense[i] != NOT_FOUND) {
        arrayOfObject3[m] = Integer.valueOf(i);
        n = m + 1;
      } 
      i++;
      m = n;
    } 
    arrayOfObject2 = arrayOfObject3;
    if (m != j) {
      arrayOfObject2 = new Object[m + k];
      System.arraycopy(arrayOfObject3, 0, arrayOfObject2, 0, m);
    } 
    System.arraycopy(arrayOfObject1, 0, arrayOfObject2, m, k);
    return arrayOfObject2;
  }
  
  public List<Integer> getIndexIds() {
    Object[] arrayOfObject = getIds();
    ArrayList<Integer> arrayList = new ArrayList(arrayOfObject.length);
    int i = arrayOfObject.length;
    for (byte b = 0; b < i; b++) {
      Object object = arrayOfObject[b];
      int j = ScriptRuntime.toInt32(object);
      if (j >= 0 && ScriptRuntime.toString(j).equals(ScriptRuntime.toString(object)))
        arrayList.add(Integer.valueOf(j)); 
    } 
    return arrayList;
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt == 1) ? "length" : super.getInstanceIdName(paramInt);
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt == 1) ? ScriptRuntime.wrapNumber(this.length) : super.getInstanceIdValue(paramInt);
  }
  
  public long getLength() {
    return this.length;
  }
  
  protected int getMaxInstanceId() {
    return 1;
  }
  
  protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject) {
    if (this.dense != null) {
      int i = toDenseIndex(paramObject);
      if (i >= 0) {
        Object[] arrayOfObject = this.dense;
        if (i < arrayOfObject.length && arrayOfObject[i] != NOT_FOUND)
          return defaultIndexPropertyDescriptor(this.dense[i]); 
      } 
    } 
    return super.getOwnPropertyDescriptor(paramContext, paramObject);
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    boolean bool = this.denseOnly;
    boolean bool1 = false;
    if (!bool && isGetterOrSetter((String)null, paramInt, false))
      return super.has(paramInt, paramScriptable); 
    Object[] arrayOfObject = this.dense;
    if (arrayOfObject != null && paramInt >= 0 && paramInt < arrayOfObject.length) {
      if (arrayOfObject[paramInt] != NOT_FOUND)
        bool1 = true; 
      return bool1;
    } 
    return super.has(paramInt, paramScriptable);
  }
  
  public int indexOf(Object paramObject) {
    long l = this.length;
    if (l <= 2147483647L) {
      int i = (int)l;
      if (paramObject == null) {
        for (byte b = 0; b < i; b++) {
          if (get(b) == null)
            return b; 
        } 
      } else {
        for (byte b = 0; b < i; b++) {
          if (paramObject.equals(get(b)))
            return b; 
        } 
      } 
      return -1;
    } 
    throw new IllegalStateException();
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    if (paramInt == 32) {
      initPrototypeMethod(ARRAY_TAG, paramInt, SymbolKey.ITERATOR, "[Symbol.iterator]", 0);
      return;
    } 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 31:
        b = 2;
        str = "copyWithin";
        break;
      case 30:
        b = 1;
        str = "includes";
        break;
      case 29:
        b = 0;
        str = "entries";
        break;
      case 28:
        b = 0;
        str = "values";
        break;
      case 27:
        b = 0;
        str = "keys";
        break;
      case 26:
        b = 1;
        str = "fill";
        break;
      case 25:
        b = 1;
        str = "reduceRight";
        break;
      case 24:
        b = 1;
        str = "reduce";
        break;
      case 23:
        b = 1;
        str = "findIndex";
        break;
      case 22:
        b = 1;
        str = "find";
        break;
      case 21:
        b = 1;
        str = "some";
        break;
      case 20:
        b = 1;
        str = "map";
        break;
      case 19:
        b = 1;
        str = "forEach";
        break;
      case 18:
        b = 1;
        str = "filter";
        break;
      case 17:
        b = 1;
        str = "every";
        break;
      case 16:
        b = 1;
        str = "lastIndexOf";
        break;
      case 15:
        b = 1;
        str = "indexOf";
        break;
      case 14:
        b = 2;
        str = "slice";
        break;
      case 13:
        b = 1;
        str = "concat";
        break;
      case 12:
        b = 2;
        str = "splice";
        break;
      case 11:
        b = 1;
        str = "unshift";
        break;
      case 10:
        b = 0;
        str = "shift";
        break;
      case 9:
        b = 0;
        str = "pop";
        break;
      case 8:
        b = 1;
        str = "push";
        break;
      case 7:
        b = 1;
        str = "sort";
        break;
      case 6:
        b = 0;
        str = "reverse";
        break;
      case 5:
        b = 1;
        str = "join";
        break;
      case 4:
        b = 0;
        str = "toSource";
        break;
      case 3:
        b = 0;
        str = "toLocaleString";
        break;
      case 2:
        b = 0;
        str = "toString";
        break;
      case 1:
        b = 1;
        str = "constructor";
        break;
    } 
    initPrototypeMethod(ARRAY_TAG, paramInt, str, (String)null, b);
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.length == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Iterator iterator() {
    return listIterator(0);
  }
  
  @Deprecated
  public long jsGet_length() {
    return getLength();
  }
  
  public int lastIndexOf(Object paramObject) {
    long l = this.length;
    if (l <= 2147483647L) {
      int i = (int)l;
      if (paramObject == null) {
        while (--i >= 0) {
          if (get(i) == null)
            return i; 
          i--;
        } 
      } else {
        while (--i >= 0) {
          if (paramObject.equals(get(i)))
            return i; 
          i--;
        } 
      } 
      return -1;
    } 
    throw new IllegalStateException();
  }
  
  public ListIterator listIterator() {
    return listIterator(0);
  }
  
  public ListIterator listIterator(final int start) {
    long l = this.length;
    if (l <= 2147483647L) {
      final int len = (int)l;
      if (start >= 0 && start <= i)
        return new ListIterator() {
            int cursor = start;
            
            public void add(Object param1Object) {
              throw new UnsupportedOperationException();
            }
            
            public boolean hasNext() {
              boolean bool;
              if (this.cursor < len) {
                bool = true;
              } else {
                bool = false;
              } 
              return bool;
            }
            
            public boolean hasPrevious() {
              boolean bool;
              if (this.cursor > 0) {
                bool = true;
              } else {
                bool = false;
              } 
              return bool;
            }
            
            public Object next() {
              int i = this.cursor;
              if (i != len) {
                NativeArray nativeArray = NativeArray.this;
                this.cursor = i + 1;
                return nativeArray.get(i);
              } 
              throw new NoSuchElementException();
            }
            
            public int nextIndex() {
              return this.cursor;
            }
            
            public Object previous() {
              int i = this.cursor;
              if (i != 0) {
                NativeArray nativeArray = NativeArray.this;
                this.cursor = --i;
                return nativeArray.get(i);
              } 
              throw new NoSuchElementException();
            }
            
            public int previousIndex() {
              return this.cursor - 1;
            }
            
            public void remove() {
              throw new UnsupportedOperationException();
            }
            
            public void set(Object param1Object) {
              throw new UnsupportedOperationException();
            }
          }; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Index: ");
      stringBuilder.append(start);
      throw new IndexOutOfBoundsException(stringBuilder.toString());
    } 
    throw new IllegalStateException();
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    if (paramScriptable == this && !isSealed() && this.dense != null && paramInt >= 0 && (this.denseOnly || !isGetterOrSetter((String)null, paramInt, true))) {
      if (!isExtensible() && this.length <= paramInt)
        return; 
      Object[] arrayOfObject = this.dense;
      if (paramInt < arrayOfObject.length) {
        arrayOfObject[paramInt] = paramObject;
        if (this.length <= paramInt)
          this.length = paramInt + 1L; 
        return;
      } 
      if (this.denseOnly && paramInt < arrayOfObject.length * 1.5D && ensureCapacity(paramInt + 1)) {
        this.dense[paramInt] = paramObject;
        this.length = paramInt + 1L;
        return;
      } 
      this.denseOnly = false;
    } 
    super.put(paramInt, paramScriptable, paramObject);
    if (paramScriptable == this && (0x1 & this.lengthAttr) == 0 && this.length <= paramInt)
      this.length = paramInt + 1L; 
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    super.put(paramString, paramScriptable, paramObject);
    if (paramScriptable == this) {
      long l = toArrayIndex(paramString);
      if (l >= this.length) {
        this.length = 1L + l;
        this.denseOnly = false;
      } 
    } 
  }
  
  public Object remove(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public boolean remove(Object paramObject) {
    throw new UnsupportedOperationException();
  }
  
  public boolean removeAll(Collection paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public boolean retainAll(Collection paramCollection) {
    throw new UnsupportedOperationException();
  }
  
  public Object set(int paramInt, Object paramObject) {
    throw new UnsupportedOperationException();
  }
  
  void setDenseOnly(boolean paramBoolean) {
    if (!paramBoolean || this.denseOnly) {
      this.denseOnly = paramBoolean;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  protected void setInstanceIdAttributes(int paramInt1, int paramInt2) {
    if (paramInt1 == 1)
      this.lengthAttr = paramInt2; 
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    if (paramInt == 1) {
      setLength(paramObject);
      return;
    } 
    super.setInstanceIdValue(paramInt, paramObject);
  }
  
  public int size() {
    long l = this.length;
    if (l <= 2147483647L)
      return (int)l; 
    throw new IllegalStateException();
  }
  
  public List subList(int paramInt1, int paramInt2) {
    throw new UnsupportedOperationException();
  }
  
  public Object[] toArray() {
    return toArray(ScriptRuntime.emptyArgs);
  }
  
  public Object[] toArray(Object[] paramArrayOfObject) {
    long l = this.length;
    if (l <= 2147483647L) {
      int i = (int)l;
      if (paramArrayOfObject.length < i)
        paramArrayOfObject = (Object[])Array.newInstance(paramArrayOfObject.getClass().getComponentType(), i); 
      for (byte b = 0; b < i; b++)
        paramArrayOfObject[b] = get(b); 
      return paramArrayOfObject;
    } 
    throw new IllegalStateException();
  }
  
  public static final class ElementComparator implements Comparator<Object> {
    private final Comparator<Object> child = NativeArray.STRING_COMPARATOR;
    
    public ElementComparator() {}
    
    public ElementComparator(Comparator<Object> param1Comparator) {}
    
    public int compare(Object param1Object1, Object param1Object2) {
      Object object = Undefined.instance;
      boolean bool = true;
      if (param1Object1 == object)
        return (param1Object2 == Undefined.instance) ? 0 : ((param1Object2 == Scriptable.NOT_FOUND) ? -1 : 1); 
      if (param1Object1 == Scriptable.NOT_FOUND) {
        if (param1Object2 == Scriptable.NOT_FOUND)
          bool = false; 
        return bool;
      } 
      return (param1Object2 == Scriptable.NOT_FOUND) ? -1 : ((param1Object2 == Undefined.instance) ? -1 : this.child.compare(param1Object1, param1Object2));
    }
  }
  
  public static final class StringLikeComparator implements Comparator<Object> {
    public int compare(Object param1Object1, Object param1Object2) {
      return ScriptRuntime.toString(param1Object1).compareTo(ScriptRuntime.toString(param1Object2));
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */