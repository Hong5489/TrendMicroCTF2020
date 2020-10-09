package com.trendmicro.hippo;

public class BoundFunction extends BaseFunction {
  private static final long serialVersionUID = 2118137342826470729L;
  
  private final Object[] boundArgs;
  
  private final Scriptable boundThis;
  
  private final int length;
  
  private final Callable targetFunction;
  
  public BoundFunction(Context paramContext, Scriptable paramScriptable1, Callable paramCallable, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    this.targetFunction = paramCallable;
    this.boundThis = paramScriptable2;
    this.boundArgs = paramArrayOfObject;
    boolean bool1 = paramCallable instanceof BaseFunction;
    Boolean bool = Boolean.valueOf(false);
    if (bool1) {
      this.length = Math.max(0, ((BaseFunction)paramCallable).getLength() - paramArrayOfObject.length);
    } else {
      this.length = 0;
    } 
    ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable1);
    paramScriptable1 = ScriptRuntime.typeErrorThrower(paramContext);
    NativeObject nativeObject = new NativeObject();
    nativeObject.put("get", nativeObject, paramScriptable1);
    nativeObject.put("set", nativeObject, paramScriptable1);
    nativeObject.put("enumerable", nativeObject, bool);
    nativeObject.put("configurable", nativeObject, bool);
    nativeObject.preventExtensions();
    defineOwnProperty(paramContext, "caller", nativeObject, false);
    defineOwnProperty(paramContext, "arguments", nativeObject, false);
  }
  
  private Object[] concat(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
    Object[] arrayOfObject = new Object[paramArrayOfObject1.length + paramArrayOfObject2.length];
    System.arraycopy(paramArrayOfObject1, 0, arrayOfObject, 0, paramArrayOfObject1.length);
    System.arraycopy(paramArrayOfObject2, 0, arrayOfObject, paramArrayOfObject1.length, paramArrayOfObject2.length);
    return arrayOfObject;
  }
  
  static boolean equalObjectGraphs(BoundFunction paramBoundFunction1, BoundFunction paramBoundFunction2, EqualObjectGraphs paramEqualObjectGraphs) {
    boolean bool;
    if (paramEqualObjectGraphs.equalGraphs(paramBoundFunction1.boundThis, paramBoundFunction2.boundThis) && paramEqualObjectGraphs.equalGraphs(paramBoundFunction1.targetFunction, paramBoundFunction2.targetFunction) && paramEqualObjectGraphs.equalGraphs(paramBoundFunction1.boundArgs, paramBoundFunction2.boundArgs)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    paramScriptable2 = this.boundThis;
    if (paramScriptable2 == null)
      paramScriptable2 = ScriptRuntime.getTopCallScope(paramContext); 
    return this.targetFunction.call(paramContext, paramScriptable1, paramScriptable2, concat(this.boundArgs, paramArrayOfObject));
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Callable callable = this.targetFunction;
    if (callable instanceof Function)
      return ((Function)callable).construct(paramContext, paramScriptable, concat(this.boundArgs, paramArrayOfObject)); 
    throw ScriptRuntime.typeError0("msg.not.ctor");
  }
  
  public int getLength() {
    return this.length;
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    Callable callable = this.targetFunction;
    if (callable instanceof Function)
      return ((Function)callable).hasInstance(paramScriptable); 
    throw ScriptRuntime.typeError0("msg.not.ctor");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/BoundFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */