package com.trendmicro.hippo;

public final class NativeGenerator extends IdScriptableObject {
  public static final int GENERATOR_CLOSE = 2;
  
  public static final int GENERATOR_SEND = 0;
  
  private static final Object GENERATOR_TAG = "Generator";
  
  public static final int GENERATOR_THROW = 1;
  
  private static final int Id___iterator__ = 5;
  
  private static final int Id_close = 1;
  
  private static final int Id_next = 2;
  
  private static final int Id_send = 3;
  
  private static final int Id_throw = 4;
  
  private static final int MAX_PROTOTYPE_ID = 5;
  
  private static final long serialVersionUID = 1645892441041347273L;
  
  private boolean firstTime = true;
  
  private NativeFunction function;
  
  private int lineNumber;
  
  private String lineSource;
  
  private boolean locked;
  
  private Object savedState;
  
  private NativeGenerator() {}
  
  public NativeGenerator(Scriptable paramScriptable, NativeFunction paramNativeFunction, Object paramObject) {
    this.function = paramNativeFunction;
    this.savedState = paramObject;
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    setParentScope(paramScriptable);
    setPrototype((NativeGenerator)ScriptableObject.getTopScopeValue(paramScriptable, GENERATOR_TAG));
  }
  
  static NativeGenerator init(ScriptableObject paramScriptableObject, boolean paramBoolean) {
    NativeGenerator nativeGenerator = new NativeGenerator();
    if (paramScriptableObject != null) {
      nativeGenerator.setParentScope(paramScriptableObject);
      nativeGenerator.setPrototype(getObjectPrototype(paramScriptableObject));
    } 
    nativeGenerator.activatePrototypeMap(5);
    if (paramBoolean)
      nativeGenerator.sealObject(); 
    if (paramScriptableObject != null)
      paramScriptableObject.associateValue(GENERATOR_TAG, nativeGenerator); 
    return nativeGenerator;
  }
  
  private Object resume(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: getfield savedState : Ljava/lang/Object;
    //   4: ifnonnull -> 49
    //   7: iload_3
    //   8: iconst_2
    //   9: if_icmpne -> 16
    //   12: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   15: areturn
    //   16: iload_3
    //   17: iconst_1
    //   18: if_icmpne -> 27
    //   21: aload #4
    //   23: astore_1
    //   24: goto -> 32
    //   27: aload_2
    //   28: invokestatic getStopIterationObject : (Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   31: astore_1
    //   32: new com/trendmicro/hippo/JavaScriptException
    //   35: dup
    //   36: aload_1
    //   37: aload_0
    //   38: getfield lineSource : Ljava/lang/String;
    //   41: aload_0
    //   42: getfield lineNumber : I
    //   45: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/String;I)V
    //   48: athrow
    //   49: aload_0
    //   50: monitorenter
    //   51: aload_0
    //   52: getfield locked : Z
    //   55: ifne -> 108
    //   58: aload_0
    //   59: iconst_1
    //   60: putfield locked : Z
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_0
    //   66: getfield function : Lcom/trendmicro/hippo/NativeFunction;
    //   69: aload_1
    //   70: aload_2
    //   71: iload_3
    //   72: aload_0
    //   73: getfield savedState : Ljava/lang/Object;
    //   76: aload #4
    //   78: invokevirtual resumeGenerator : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;ILjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   81: astore_1
    //   82: aload_0
    //   83: monitorenter
    //   84: aload_0
    //   85: iconst_0
    //   86: putfield locked : Z
    //   89: aload_0
    //   90: monitorexit
    //   91: iload_3
    //   92: iconst_2
    //   93: if_icmpne -> 101
    //   96: aload_0
    //   97: aconst_null
    //   98: putfield savedState : Ljava/lang/Object;
    //   101: aload_1
    //   102: areturn
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    //   108: ldc 'msg.already.exec.gen'
    //   110: invokestatic typeError0 : (Ljava/lang/String;)Lcom/trendmicro/hippo/EcmaError;
    //   113: athrow
    //   114: astore_1
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_1
    //   118: athrow
    //   119: astore_1
    //   120: goto -> 178
    //   123: astore_1
    //   124: aload_0
    //   125: aload_1
    //   126: invokevirtual lineNumber : ()I
    //   129: putfield lineNumber : I
    //   132: aload_0
    //   133: aload_1
    //   134: invokevirtual lineSource : ()Ljava/lang/String;
    //   137: putfield lineSource : Ljava/lang/String;
    //   140: aload_0
    //   141: aconst_null
    //   142: putfield savedState : Ljava/lang/Object;
    //   145: aload_1
    //   146: athrow
    //   147: astore_1
    //   148: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   151: astore_1
    //   152: aload_0
    //   153: monitorenter
    //   154: aload_0
    //   155: iconst_0
    //   156: putfield locked : Z
    //   159: aload_0
    //   160: monitorexit
    //   161: iload_3
    //   162: iconst_2
    //   163: if_icmpne -> 171
    //   166: aload_0
    //   167: aconst_null
    //   168: putfield savedState : Ljava/lang/Object;
    //   171: aload_1
    //   172: areturn
    //   173: astore_1
    //   174: aload_0
    //   175: monitorexit
    //   176: aload_1
    //   177: athrow
    //   178: aload_0
    //   179: monitorenter
    //   180: aload_0
    //   181: iconst_0
    //   182: putfield locked : Z
    //   185: aload_0
    //   186: monitorexit
    //   187: iload_3
    //   188: iconst_2
    //   189: if_icmpne -> 197
    //   192: aload_0
    //   193: aconst_null
    //   194: putfield savedState : Ljava/lang/Object;
    //   197: aload_1
    //   198: athrow
    //   199: astore_1
    //   200: aload_0
    //   201: monitorexit
    //   202: aload_1
    //   203: athrow
    // Exception table:
    //   from	to	target	type
    //   49	51	147	com/trendmicro/hippo/NativeGenerator$GeneratorClosedException
    //   49	51	123	com/trendmicro/hippo/HippoException
    //   49	51	119	finally
    //   51	65	114	finally
    //   65	82	147	com/trendmicro/hippo/NativeGenerator$GeneratorClosedException
    //   65	82	123	com/trendmicro/hippo/HippoException
    //   65	82	119	finally
    //   84	91	103	finally
    //   104	106	103	finally
    //   108	114	114	finally
    //   115	117	114	finally
    //   117	119	147	com/trendmicro/hippo/NativeGenerator$GeneratorClosedException
    //   117	119	123	com/trendmicro/hippo/HippoException
    //   117	119	119	finally
    //   124	145	119	finally
    //   145	147	119	finally
    //   148	152	119	finally
    //   154	161	173	finally
    //   174	176	173	finally
    //   180	187	199	finally
    //   200	202	199	finally
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object;
    if (!paramIdFunctionObject.hasTag(GENERATOR_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (paramScriptable2 instanceof NativeGenerator) {
      NativeGenerator nativeGenerator = (NativeGenerator)paramScriptable2;
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 4) {
              if (i == 5)
                return paramScriptable2; 
              throw new IllegalArgumentException(String.valueOf(i));
            } 
            if (paramArrayOfObject.length > 0) {
              object = paramArrayOfObject[0];
            } else {
              object = Undefined.instance;
            } 
            return nativeGenerator.resume(paramContext, paramScriptable1, 1, object);
          } 
          if (paramArrayOfObject.length > 0) {
            object = paramArrayOfObject[0];
          } else {
            object = Undefined.instance;
          } 
          if (!nativeGenerator.firstTime || object.equals(Undefined.instance))
            return nativeGenerator.resume(paramContext, paramScriptable1, 0, object); 
          throw ScriptRuntime.typeError0("msg.send.newborn");
        } 
        nativeGenerator.firstTime = false;
        return nativeGenerator.resume(paramContext, paramScriptable1, 0, Undefined.instance);
      } 
      return nativeGenerator.resume(paramContext, paramScriptable1, 2, new GeneratorClosedException());
    } 
    throw incompatibleCallError(object);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 4) {
      i = paramString.charAt(0);
      if (i == 110) {
        str = "next";
        b = 2;
      } else if (i == 115) {
        str = "send";
        b = 3;
      } 
    } else if (i == 5) {
      i = paramString.charAt(0);
      if (i == 99) {
        str = "close";
        b = 1;
      } else if (i == 116) {
        str = "throw";
        b = 4;
      } 
    } else if (i == 12) {
      str = "__iterator__";
      b = 5;
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
    return "Generator";
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt != 4) {
            if (paramInt == 5) {
              bool = true;
              str = "__iterator__";
            } else {
              throw new IllegalArgumentException(String.valueOf(paramInt));
            } 
          } else {
            bool = false;
            str = "throw";
          } 
        } else {
          bool = false;
          str = "send";
        } 
      } else {
        bool = true;
        str = "next";
      } 
    } else {
      bool = true;
      str = "close";
    } 
    initPrototypeMethod(GENERATOR_TAG, paramInt, str, bool);
  }
  
  public static class GeneratorClosedException extends RuntimeException {
    private static final long serialVersionUID = 2561315658662379681L;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */