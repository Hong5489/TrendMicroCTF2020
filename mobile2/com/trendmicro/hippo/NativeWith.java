package com.trendmicro.hippo;

import java.io.Serializable;

public class NativeWith implements Scriptable, SymbolScriptable, IdFunctionCall, Serializable {
  private static final Object FTAG = "With";
  
  private static final int Id_constructor = 1;
  
  private static final long serialVersionUID = 1L;
  
  protected Scriptable parent;
  
  protected Scriptable prototype;
  
  private NativeWith() {}
  
  protected NativeWith(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    this.parent = paramScriptable1;
    this.prototype = paramScriptable2;
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    NativeWith nativeWith = new NativeWith();
    nativeWith.setParentScope(paramScriptable);
    nativeWith.setPrototype(ScriptableObject.getObjectPrototype(paramScriptable));
    paramScriptable = new IdFunctionObject(nativeWith, FTAG, 1, "With", 0, paramScriptable);
    paramScriptable.markAsConstructor(nativeWith);
    if (paramBoolean)
      paramScriptable.sealObject(); 
    paramScriptable.exportAsScopeProperty();
  }
  
  static boolean isWithFunction(Object paramObject) {
    boolean bool = paramObject instanceof IdFunctionObject;
    boolean bool1 = false;
    if (bool) {
      paramObject = paramObject;
      bool = bool1;
      if (paramObject.hasTag(FTAG)) {
        bool = bool1;
        if (paramObject.methodId() == 1)
          bool = true; 
      } 
      return bool;
    } 
    return false;
  }
  
  static Object newWithSpecial(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Scriptable scriptable;
    ScriptRuntime.checkDeprecated(paramContext, "With");
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    NativeWith nativeWith = new NativeWith();
    if (paramArrayOfObject.length == 0) {
      scriptable = ScriptableObject.getObjectPrototype(paramScriptable);
    } else {
      scriptable = ScriptRuntime.toObject((Context)scriptable, paramScriptable, paramArrayOfObject[0]);
    } 
    nativeWith.setPrototype(scriptable);
    nativeWith.setParentScope(paramScriptable);
    return nativeWith;
  }
  
  public void delete(int paramInt) {
    this.prototype.delete(paramInt);
  }
  
  public void delete(Symbol paramSymbol) {
    Scriptable scriptable = this.prototype;
    if (scriptable instanceof SymbolScriptable)
      ((SymbolScriptable)scriptable).delete(paramSymbol); 
  }
  
  public void delete(String paramString) {
    this.prototype.delete(paramString);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (paramIdFunctionObject.hasTag(FTAG) && paramIdFunctionObject.methodId() == 1)
      throw Context.reportRuntimeError1("msg.cant.call.indirect", "With"); 
    throw paramIdFunctionObject.unknown();
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    Scriptable scriptable = paramScriptable;
    if (paramScriptable == this)
      scriptable = this.prototype; 
    return this.prototype.get(paramInt, scriptable);
  }
  
  public Object get(Symbol paramSymbol, Scriptable paramScriptable) {
    Scriptable scriptable = paramScriptable;
    if (paramScriptable == this)
      scriptable = this.prototype; 
    paramScriptable = this.prototype;
    return (paramScriptable instanceof SymbolScriptable) ? ((SymbolScriptable)paramScriptable).get(paramSymbol, scriptable) : Scriptable.NOT_FOUND;
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Scriptable scriptable = paramScriptable;
    if (paramScriptable == this)
      scriptable = this.prototype; 
    return this.prototype.get(paramString, scriptable);
  }
  
  public String getClassName() {
    return "With";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return this.prototype.getDefaultValue(paramClass);
  }
  
  public Object[] getIds() {
    return this.prototype.getIds();
  }
  
  public Scriptable getParentScope() {
    return this.parent;
  }
  
  public Scriptable getPrototype() {
    return this.prototype;
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    paramScriptable = this.prototype;
    return paramScriptable.has(paramInt, paramScriptable);
  }
  
  public boolean has(Symbol paramSymbol, Scriptable paramScriptable) {
    paramScriptable = this.prototype;
    return (paramScriptable instanceof SymbolScriptable) ? ((SymbolScriptable)paramScriptable).has(paramSymbol, paramScriptable) : false;
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    paramScriptable = this.prototype;
    return paramScriptable.has(paramString, paramScriptable);
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    return this.prototype.hasInstance(paramScriptable);
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    Scriptable scriptable = paramScriptable;
    if (paramScriptable == this)
      scriptable = this.prototype; 
    this.prototype.put(paramInt, scriptable, paramObject);
  }
  
  public void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject) {
    Scriptable scriptable = paramScriptable;
    if (paramScriptable == this)
      scriptable = this.prototype; 
    paramScriptable = this.prototype;
    if (paramScriptable instanceof SymbolScriptable)
      ((SymbolScriptable)paramScriptable).put(paramSymbol, scriptable, paramObject); 
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    Scriptable scriptable = paramScriptable;
    if (paramScriptable == this)
      scriptable = this.prototype; 
    this.prototype.put(paramString, scriptable, paramObject);
  }
  
  public void setParentScope(Scriptable paramScriptable) {
    this.parent = paramScriptable;
  }
  
  public void setPrototype(Scriptable paramScriptable) {
    this.prototype = paramScriptable;
  }
  
  protected Object updateDotQuery(boolean paramBoolean) {
    throw new IllegalStateException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeWith.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */