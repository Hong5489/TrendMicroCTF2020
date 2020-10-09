package com.trendmicro.hippo;

final class Arguments extends IdScriptableObject {
  private static final String FTAG = "Arguments";
  
  private static final int Id_callee = 1;
  
  private static final int Id_caller = 3;
  
  private static final int Id_length = 2;
  
  private static final int MAX_INSTANCE_ID = 3;
  
  private static BaseFunction iteratorMethod = new BaseFunction() {
      public Object call(Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject) {
        return new NativeArrayIterator(param1Scriptable1, param1Scriptable2, NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES);
      }
    };
  
  private static final long serialVersionUID = 4275508002492040609L;
  
  private NativeCall activation;
  
  private Object[] args;
  
  private int calleeAttr = 2;
  
  private Object calleeObj;
  
  private int callerAttr = 2;
  
  private Object callerObj;
  
  private int lengthAttr = 2;
  
  private Object lengthObj;
  
  public Arguments(NativeCall paramNativeCall) {
    this.activation = paramNativeCall;
    Scriptable scriptable = paramNativeCall.getParentScope();
    setParentScope(scriptable);
    setPrototype(ScriptableObject.getObjectPrototype(scriptable));
    Object[] arrayOfObject = paramNativeCall.originalArgs;
    this.args = arrayOfObject;
    this.lengthObj = Integer.valueOf(arrayOfObject.length);
    NativeFunction nativeFunction = paramNativeCall.function;
    this.calleeObj = nativeFunction;
    int i = nativeFunction.getLanguageVersion();
    if (i <= 130 && i != 0) {
      this.callerObj = null;
    } else {
      this.callerObj = NOT_FOUND;
    } 
    defineProperty(SymbolKey.ITERATOR, iteratorMethod, 2);
  }
  
  private Object arg(int paramInt) {
    if (paramInt >= 0) {
      Object[] arrayOfObject = this.args;
      if (arrayOfObject.length > paramInt)
        return arrayOfObject[paramInt]; 
    } 
    return NOT_FOUND;
  }
  
  private Object getFromActivation(int paramInt) {
    String str = this.activation.function.getParamOrVarName(paramInt);
    NativeCall nativeCall = this.activation;
    return nativeCall.get(str, nativeCall);
  }
  
  private void putIntoActivation(int paramInt, Object paramObject) {
    String str = this.activation.function.getParamOrVarName(paramInt);
    NativeCall nativeCall = this.activation;
    nativeCall.put(str, nativeCall, paramObject);
  }
  
  private void removeArg(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield args : [Ljava/lang/Object;
    //   6: iload_1
    //   7: aaload
    //   8: getstatic com/trendmicro/hippo/Arguments.NOT_FOUND : Ljava/lang/Object;
    //   11: if_acmpeq -> 51
    //   14: aload_0
    //   15: getfield args : [Ljava/lang/Object;
    //   18: aload_0
    //   19: getfield activation : Lcom/trendmicro/hippo/NativeCall;
    //   22: getfield originalArgs : [Ljava/lang/Object;
    //   25: if_acmpne -> 42
    //   28: aload_0
    //   29: aload_0
    //   30: getfield args : [Ljava/lang/Object;
    //   33: invokevirtual clone : ()Ljava/lang/Object;
    //   36: checkcast [Ljava/lang/Object;
    //   39: putfield args : [Ljava/lang/Object;
    //   42: aload_0
    //   43: getfield args : [Ljava/lang/Object;
    //   46: iload_1
    //   47: getstatic com/trendmicro/hippo/Arguments.NOT_FOUND : Ljava/lang/Object;
    //   50: aastore
    //   51: aload_0
    //   52: monitorexit
    //   53: return
    //   54: astore_2
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_2
    //   58: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	54	finally
    //   42	51	54	finally
    //   51	53	54	finally
    //   55	57	54	finally
  }
  
  private void replaceArg(int paramInt, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial sharedWithActivation : (I)Z
    //   5: ifeq -> 14
    //   8: aload_0
    //   9: iload_1
    //   10: aload_2
    //   11: invokespecial putIntoActivation : (ILjava/lang/Object;)V
    //   14: aload_0
    //   15: monitorenter
    //   16: aload_0
    //   17: getfield args : [Ljava/lang/Object;
    //   20: aload_0
    //   21: getfield activation : Lcom/trendmicro/hippo/NativeCall;
    //   24: getfield originalArgs : [Ljava/lang/Object;
    //   27: if_acmpne -> 44
    //   30: aload_0
    //   31: aload_0
    //   32: getfield args : [Ljava/lang/Object;
    //   35: invokevirtual clone : ()Ljava/lang/Object;
    //   38: checkcast [Ljava/lang/Object;
    //   41: putfield args : [Ljava/lang/Object;
    //   44: aload_0
    //   45: getfield args : [Ljava/lang/Object;
    //   48: iload_1
    //   49: aload_2
    //   50: aastore
    //   51: aload_0
    //   52: monitorexit
    //   53: return
    //   54: astore_2
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_2
    //   58: athrow
    // Exception table:
    //   from	to	target	type
    //   16	44	54	finally
    //   44	53	54	finally
    //   55	57	54	finally
  }
  
  private boolean sharedWithActivation(int paramInt) {
    if (Context.getContext().isStrictMode())
      return false; 
    NativeFunction nativeFunction = this.activation.function;
    int i = nativeFunction.getParamCount();
    if (paramInt < i) {
      if (paramInt < i - 1) {
        String str = nativeFunction.getParamOrVarName(paramInt);
        while (++paramInt < i) {
          if (str.equals(nativeFunction.getParamOrVarName(paramInt)))
            return false; 
          paramInt++;
        } 
      } 
      return true;
    } 
    return false;
  }
  
  void defineAttributesForStrictMode() {
    if (!Context.getContext().isStrictMode())
      return; 
    setGetterOrSetter("caller", 0, new ThrowTypeError("caller"), true);
    setGetterOrSetter("caller", 0, new ThrowTypeError("caller"), false);
    setGetterOrSetter("callee", 0, new ThrowTypeError("callee"), true);
    setGetterOrSetter("callee", 0, new ThrowTypeError("callee"), false);
    setAttributes("caller", 6);
    setAttributes("callee", 6);
    this.callerObj = null;
    this.calleeObj = null;
  }
  
  protected void defineOwnProperty(Context paramContext, Object paramObject, ScriptableObject paramScriptableObject, boolean paramBoolean) {
    super.defineOwnProperty(paramContext, paramObject, paramScriptableObject, paramBoolean);
    double d = ScriptRuntime.toNumber(paramObject);
    int i = (int)d;
    if (d != i)
      return; 
    if (arg(i) == NOT_FOUND)
      return; 
    if (isAccessorDescriptor(paramScriptableObject)) {
      removeArg(i);
      return;
    } 
    Object object = getProperty(paramScriptableObject, "value");
    if (object == NOT_FOUND)
      return; 
    replaceArg(i, object);
    if (isFalse(getProperty(paramScriptableObject, "writable")))
      removeArg(i); 
  }
  
  public void delete(int paramInt) {
    if (paramInt >= 0 && paramInt < this.args.length)
      removeArg(paramInt); 
    super.delete(paramInt);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    int i = 0;
    String str1 = null;
    int j = i;
    String str2 = str1;
    if (paramString.length() == 6) {
      char c = paramString.charAt(5);
      if (c == 'e') {
        str2 = "callee";
        j = 1;
      } else if (c == 'h') {
        str2 = "length";
        j = 2;
      } else {
        j = i;
        str2 = str1;
        if (c == 'r') {
          str2 = "caller";
          j = 3;
        } 
      } 
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
    if (Context.getContext().isStrictMode() && (i == 1 || i == 3))
      return super.findInstanceIdInfo(paramString); 
    if (i == 0)
      return super.findInstanceIdInfo(paramString); 
    if (i != 1) {
      if (i != 2) {
        if (i == 3) {
          j = this.callerAttr;
        } else {
          throw new IllegalStateException();
        } 
      } else {
        j = this.lengthAttr;
      } 
    } else {
      j = this.calleeAttr;
    } 
    return instanceIdInfo(j, i);
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    Object object = arg(paramInt);
    return (object == NOT_FOUND) ? super.get(paramInt, paramScriptable) : (sharedWithActivation(paramInt) ? getFromActivation(paramInt) : object);
  }
  
  public String getClassName() {
    return "Arguments";
  }
  
  Object[] getIds(boolean paramBoolean1, boolean paramBoolean2) {
    Object[] arrayOfObject1 = super.getIds(paramBoolean1, paramBoolean2);
    Object[] arrayOfObject2 = this.args;
    Object[] arrayOfObject3 = arrayOfObject1;
    if (arrayOfObject2.length != 0) {
      boolean[] arrayOfBoolean = new boolean[arrayOfObject2.length];
      int i = arrayOfObject2.length;
      int j = 0;
      while (j != arrayOfObject1.length) {
        Object object = arrayOfObject1[j];
        int k = i;
        if (object instanceof Integer) {
          int m = ((Integer)object).intValue();
          k = i;
          if (m >= 0) {
            k = i;
            if (m < this.args.length) {
              k = i;
              if (!arrayOfBoolean[m]) {
                arrayOfBoolean[m] = true;
                k = i - 1;
              } 
            } 
          } 
        } 
        j++;
        i = k;
      } 
      j = i;
      if (!paramBoolean1) {
        byte b = 0;
        while (true) {
          j = i;
          if (b < arrayOfBoolean.length) {
            j = i;
            if (!arrayOfBoolean[b]) {
              j = i;
              if (super.has(b, this)) {
                arrayOfBoolean[b] = true;
                j = i - 1;
              } 
            } 
            b++;
            i = j;
            continue;
          } 
          break;
        } 
      } 
      arrayOfObject3 = arrayOfObject1;
      if (j != 0) {
        arrayOfObject3 = new Object[arrayOfObject1.length + j];
        System.arraycopy(arrayOfObject1, 0, arrayOfObject3, j, arrayOfObject1.length);
        arrayOfObject1 = arrayOfObject3;
        int k = 0;
        i = 0;
        while (i != this.args.length) {
          int m = k;
          if (!arrayOfBoolean[i]) {
            arrayOfObject1[k] = Integer.valueOf(i);
            m = k + 1;
          } 
          i++;
          k = m;
        } 
        arrayOfObject3 = arrayOfObject1;
        if (k != j) {
          Kit.codeBug();
          arrayOfObject3 = arrayOfObject1;
        } 
      } 
    } 
    return arrayOfObject3;
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? null : "caller") : "length") : "callee";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    if (paramInt != 1) {
      if (paramInt != 2) {
        Object object2;
        if (paramInt != 3)
          return super.getInstanceIdValue(paramInt); 
        Object object1 = this.callerObj;
        if (object1 == UniqueTag.NULL_VALUE) {
          object2 = null;
        } else {
          object2 = object1;
          if (object1 == null) {
            NativeCall nativeCall = this.activation.parentActivationCall;
            object2 = object1;
            if (nativeCall != null)
              object2 = nativeCall.get("arguments", nativeCall); 
          } 
        } 
        return object2;
      } 
      return this.lengthObj;
    } 
    return this.calleeObj;
  }
  
  protected int getMaxInstanceId() {
    return 3;
  }
  
  protected ScriptableObject getOwnPropertyDescriptor(Context paramContext, Object paramObject) {
    if (paramObject instanceof Scriptable)
      return super.getOwnPropertyDescriptor(paramContext, paramObject); 
    double d = ScriptRuntime.toNumber(paramObject);
    int i = (int)d;
    if (d != i)
      return super.getOwnPropertyDescriptor(paramContext, paramObject); 
    Object object2 = arg(i);
    if (object2 == NOT_FOUND)
      return super.getOwnPropertyDescriptor(paramContext, paramObject); 
    if (sharedWithActivation(i))
      object2 = getFromActivation(i); 
    if (super.has(i, this)) {
      ScriptableObject scriptableObject = super.getOwnPropertyDescriptor(paramContext, paramObject);
      scriptableObject.put("value", scriptableObject, object2);
      return scriptableObject;
    } 
    paramObject = getParentScope();
    Object object1 = paramObject;
    if (paramObject == null)
      object1 = this; 
    return buildDataDescriptor((Scriptable)object1, object2, 0);
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    return (arg(paramInt) != NOT_FOUND) ? true : super.has(paramInt, paramScriptable);
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    if (arg(paramInt) == NOT_FOUND) {
      super.put(paramInt, paramScriptable, paramObject);
    } else {
      replaceArg(paramInt, paramObject);
    } 
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    super.put(paramString, paramScriptable, paramObject);
  }
  
  protected void setInstanceIdAttributes(int paramInt1, int paramInt2) {
    if (paramInt1 != 1) {
      if (paramInt1 != 2) {
        if (paramInt1 != 3) {
          super.setInstanceIdAttributes(paramInt1, paramInt2);
          return;
        } 
        this.callerAttr = paramInt2;
        return;
      } 
      this.lengthAttr = paramInt2;
      return;
    } 
    this.calleeAttr = paramInt2;
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          super.setInstanceIdValue(paramInt, paramObject);
          return;
        } 
        if (paramObject == null)
          paramObject = UniqueTag.NULL_VALUE; 
        this.callerObj = paramObject;
        return;
      } 
      this.lengthObj = paramObject;
      return;
    } 
    this.calleeObj = paramObject;
  }
  
  private static class ThrowTypeError extends BaseFunction {
    private String propertyName;
    
    ThrowTypeError(String param1String) {
      this.propertyName = param1String;
    }
    
    public Object call(Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject) {
      throw ScriptRuntime.typeError1("msg.arguments.not.access.strict", this.propertyName);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Arguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */