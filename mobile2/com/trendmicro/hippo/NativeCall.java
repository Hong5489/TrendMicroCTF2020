package com.trendmicro.hippo;

public final class NativeCall extends IdScriptableObject {
  private static final Object CALL_TAG = "Call";
  
  private static final int Id_constructor = 1;
  
  private static final int MAX_PROTOTYPE_ID = 1;
  
  private static final long serialVersionUID = -7471457301304454454L;
  
  private Arguments arguments;
  
  NativeFunction function;
  
  boolean isStrict;
  
  Object[] originalArgs;
  
  transient NativeCall parentActivationCall;
  
  NativeCall() {}
  
  NativeCall(NativeFunction paramNativeFunction, Scriptable paramScriptable, Object[] paramArrayOfObject, boolean paramBoolean1, boolean paramBoolean2) {
    Object[] arrayOfObject;
    this.function = paramNativeFunction;
    setParentScope(paramScriptable);
    if (paramArrayOfObject == null) {
      arrayOfObject = ScriptRuntime.emptyArgs;
    } else {
      arrayOfObject = paramArrayOfObject;
    } 
    this.originalArgs = arrayOfObject;
    this.isStrict = paramBoolean2;
    int i = paramNativeFunction.getParamAndVarCount();
    int j = paramNativeFunction.getParamCount();
    if (i != 0)
      for (byte b = 0; b < j; b++) {
        Object object;
        String str = paramNativeFunction.getParamOrVarName(b);
        if (b < paramArrayOfObject.length) {
          object = paramArrayOfObject[b];
        } else {
          object = Undefined.instance;
        } 
        defineProperty(str, object, 4);
      }  
    if (!has("arguments", this) && !paramBoolean1) {
      Arguments arguments = new Arguments(this);
      this.arguments = arguments;
      defineProperty("arguments", arguments, 4);
    } 
    if (i != 0)
      for (int k = j; k < i; k++) {
        String str = paramNativeFunction.getParamOrVarName(k);
        if (!has(str, this))
          if (paramNativeFunction.getParamOrVarConst(k)) {
            defineProperty(str, Undefined.instance, 13);
          } else {
            defineProperty(str, Undefined.instance, 4);
          }  
      }  
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeCall()).exportAsJSClass(1, paramScriptable, paramBoolean);
  }
  
  public void defineAttributesForArguments() {
    Arguments arguments = this.arguments;
    if (arguments != null)
      arguments.defineAttributesForStrictMode(); 
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(CALL_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i == 1) {
      if (paramScriptable2 == null) {
        ScriptRuntime.checkDeprecated(paramContext, "Call");
        NativeCall nativeCall = new NativeCall();
        nativeCall.setPrototype(getObjectPrototype(paramScriptable1));
        return nativeCall;
      } 
      throw Context.reportRuntimeError1("msg.only.from.new", "Call");
    } 
    throw new IllegalArgumentException(String.valueOf(i));
  }
  
  protected int findPrototypeId(String paramString) {
    return paramString.equals("constructor");
  }
  
  public String getClassName() {
    return "Call";
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt == 1) {
      initPrototypeMethod(CALL_TAG, paramInt, "constructor", 1);
      return;
    } 
    throw new IllegalArgumentException(String.valueOf(paramInt));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeCall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */