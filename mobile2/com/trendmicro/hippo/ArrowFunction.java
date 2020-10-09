package com.trendmicro.hippo;

public class ArrowFunction extends BaseFunction {
  private static final long serialVersionUID = -7377989503697220633L;
  
  private final Scriptable boundThis;
  
  private final Callable targetFunction;
  
  public ArrowFunction(Context paramContext, Scriptable paramScriptable1, Callable paramCallable, Scriptable paramScriptable2) {
    this.targetFunction = paramCallable;
    this.boundThis = paramScriptable2;
    ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable1);
    paramCallable = ScriptRuntime.typeErrorThrower();
    paramScriptable1 = new NativeObject();
    paramScriptable1.put("get", paramScriptable1, paramCallable);
    paramScriptable1.put("set", paramScriptable1, paramCallable);
    Boolean bool = Boolean.valueOf(false);
    paramScriptable1.put("enumerable", paramScriptable1, bool);
    paramScriptable1.put("configurable", paramScriptable1, bool);
    paramScriptable1.preventExtensions();
    defineOwnProperty(paramContext, "caller", (ScriptableObject)paramScriptable1, false);
    defineOwnProperty(paramContext, "arguments", (ScriptableObject)paramScriptable1, false);
  }
  
  static boolean equalObjectGraphs(ArrowFunction paramArrowFunction1, ArrowFunction paramArrowFunction2, EqualObjectGraphs paramEqualObjectGraphs) {
    boolean bool;
    if (paramEqualObjectGraphs.equalGraphs(paramArrowFunction1.boundThis, paramArrowFunction2.boundThis) && paramEqualObjectGraphs.equalGraphs(paramArrowFunction1.targetFunction, paramArrowFunction2.targetFunction)) {
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
    return this.targetFunction.call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    throw ScriptRuntime.typeError1("msg.not.ctor", decompile(0, 0));
  }
  
  String decompile(int paramInt1, int paramInt2) {
    Callable callable = this.targetFunction;
    return (callable instanceof BaseFunction) ? ((BaseFunction)callable).decompile(paramInt1, paramInt2) : super.decompile(paramInt1, paramInt2);
  }
  
  public int getArity() {
    return getLength();
  }
  
  public int getLength() {
    Callable callable = this.targetFunction;
    return (callable instanceof BaseFunction) ? ((BaseFunction)callable).getLength() : 0;
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    Callable callable = this.targetFunction;
    if (callable instanceof Function)
      return ((Function)callable).hasInstance(paramScriptable); 
    throw ScriptRuntime.typeError0("msg.not.ctor");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ArrowFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */