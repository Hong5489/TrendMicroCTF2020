package com.trendmicro.hippo;

public class Delegator implements Function, SymbolScriptable {
  protected Scriptable obj = null;
  
  public Delegator() {}
  
  public Delegator(Scriptable paramScriptable) {
    this.obj = paramScriptable;
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return ((Function)getDelegee()).call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Scriptable scriptable1;
    Scriptable scriptable2 = getDelegee();
    if (scriptable2 == null) {
      scriptable2 = newInstance();
      if (paramArrayOfObject.length == 0) {
        scriptable1 = new NativeObject();
      } else {
        scriptable1 = ScriptRuntime.toObject((Context)scriptable1, paramScriptable, paramArrayOfObject[0]);
      } 
      scriptable2.setDelegee(scriptable1);
      return scriptable2;
    } 
    return ((Function)scriptable2).construct((Context)scriptable1, paramScriptable, paramArrayOfObject);
  }
  
  public void delete(int paramInt) {
    getDelegee().delete(paramInt);
  }
  
  public void delete(Symbol paramSymbol) {
    Scriptable scriptable = getDelegee();
    if (scriptable instanceof SymbolScriptable)
      ((SymbolScriptable)scriptable).delete(paramSymbol); 
  }
  
  public void delete(String paramString) {
    getDelegee().delete(paramString);
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    return getDelegee().get(paramInt, paramScriptable);
  }
  
  public Object get(Symbol paramSymbol, Scriptable paramScriptable) {
    Scriptable scriptable = getDelegee();
    return (scriptable instanceof SymbolScriptable) ? ((SymbolScriptable)scriptable).get(paramSymbol, paramScriptable) : Scriptable.NOT_FOUND;
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    return getDelegee().get(paramString, paramScriptable);
  }
  
  public String getClassName() {
    return getDelegee().getClassName();
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return (paramClass == null || paramClass == ScriptRuntime.ScriptableClass || paramClass == ScriptRuntime.FunctionClass) ? this : getDelegee().getDefaultValue(paramClass);
  }
  
  public Scriptable getDelegee() {
    return this.obj;
  }
  
  public Object[] getIds() {
    return getDelegee().getIds();
  }
  
  public Scriptable getParentScope() {
    return getDelegee().getParentScope();
  }
  
  public Scriptable getPrototype() {
    return getDelegee().getPrototype();
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    return getDelegee().has(paramInt, paramScriptable);
  }
  
  public boolean has(Symbol paramSymbol, Scriptable paramScriptable) {
    Scriptable scriptable = getDelegee();
    return (scriptable instanceof SymbolScriptable) ? ((SymbolScriptable)scriptable).has(paramSymbol, paramScriptable) : false;
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    return getDelegee().has(paramString, paramScriptable);
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    return getDelegee().hasInstance(paramScriptable);
  }
  
  protected Delegator newInstance() {
    try {
      return (Delegator)getClass().newInstance();
    } catch (Exception exception) {
      throw Context.throwAsScriptRuntimeEx(exception);
    } 
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    getDelegee().put(paramInt, paramScriptable, paramObject);
  }
  
  public void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject) {
    Scriptable scriptable = getDelegee();
    if (scriptable instanceof SymbolScriptable)
      ((SymbolScriptable)scriptable).put(paramSymbol, paramScriptable, paramObject); 
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    getDelegee().put(paramString, paramScriptable, paramObject);
  }
  
  public void setDelegee(Scriptable paramScriptable) {
    this.obj = paramScriptable;
  }
  
  public void setParentScope(Scriptable paramScriptable) {
    getDelegee().setParentScope(paramScriptable);
  }
  
  public void setPrototype(Scriptable paramScriptable) {
    getDelegee().setPrototype(paramScriptable);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Delegator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */