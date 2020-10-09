package com.trendmicro.hippo;

import java.lang.reflect.Array;

public class NativeJavaArray extends NativeJavaObject implements SymbolScriptable {
  private static final long serialVersionUID = -924022554283675333L;
  
  Object array;
  
  Class<?> cls;
  
  int length;
  
  public NativeJavaArray(Scriptable paramScriptable, Object paramObject) {
    super(paramScriptable, (Object)null, ScriptRuntime.ObjectClass);
    Class<?> clazz = paramObject.getClass();
    if (clazz.isArray()) {
      this.array = paramObject;
      this.length = Array.getLength(paramObject);
      this.cls = clazz.getComponentType();
      return;
    } 
    throw new RuntimeException("Array expected");
  }
  
  public static NativeJavaArray wrap(Scriptable paramScriptable, Object paramObject) {
    return new NativeJavaArray(paramScriptable, paramObject);
  }
  
  public void delete(Symbol paramSymbol) {}
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    if (paramInt >= 0 && paramInt < this.length) {
      Context context = Context.getContext();
      Object object = Array.get(this.array, paramInt);
      return context.getWrapFactory().wrap(context, this, object, this.cls);
    } 
    return Undefined.instance;
  }
  
  public Object get(Symbol paramSymbol, Scriptable paramScriptable) {
    return SymbolKey.IS_CONCAT_SPREADABLE.equals(paramSymbol) ? Boolean.valueOf(true) : Scriptable.NOT_FOUND;
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    if (paramString.equals("length"))
      return Integer.valueOf(this.length); 
    Object object = super.get(paramString, paramScriptable);
    if (object != NOT_FOUND || ScriptableObject.hasProperty(getPrototype(), paramString))
      return object; 
    throw Context.reportRuntimeError2("msg.java.member.not.found", this.array.getClass().getName(), paramString);
  }
  
  public String getClassName() {
    return "JavaArray";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return (paramClass == null || paramClass == ScriptRuntime.StringClass) ? this.array.toString() : ((paramClass == ScriptRuntime.BooleanClass) ? Boolean.TRUE : ((paramClass == ScriptRuntime.NumberClass) ? ScriptRuntime.NaNobj : this));
  }
  
  public Object[] getIds() {
    Object[] arrayOfObject = new Object[this.length];
    int i = this.length;
    while (--i >= 0)
      arrayOfObject[i] = Integer.valueOf(i); 
    return arrayOfObject;
  }
  
  public Scriptable getPrototype() {
    if (this.prototype == null)
      this.prototype = ScriptableObject.getArrayPrototype(getParentScope()); 
    return this.prototype;
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    boolean bool;
    if (paramInt >= 0 && paramInt < this.length) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean has(Symbol paramSymbol, Scriptable paramScriptable) {
    return SymbolKey.IS_CONCAT_SPREADABLE.equals(paramSymbol);
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    return (paramString.equals("length") || super.has(paramString, paramScriptable));
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    if (!(paramScriptable instanceof Wrapper))
      return false; 
    Object object = ((Wrapper)paramScriptable).unwrap();
    return this.cls.isInstance(object);
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    if (paramInt >= 0 && paramInt < this.length) {
      Array.set(this.array, paramInt, Context.jsToJava(paramObject, this.cls));
      return;
    } 
    throw Context.reportRuntimeError2("msg.java.array.index.out.of.bounds", String.valueOf(paramInt), String.valueOf(this.length - 1));
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    if (paramString.equals("length"))
      return; 
    throw Context.reportRuntimeError1("msg.java.array.member.not.found", paramString);
  }
  
  public Object unwrap() {
    return this.array;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */