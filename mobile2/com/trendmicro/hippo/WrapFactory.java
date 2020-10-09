package com.trendmicro.hippo;

public class WrapFactory {
  private boolean javaPrimitiveWrap = true;
  
  public final boolean isJavaPrimitiveWrap() {
    return this.javaPrimitiveWrap;
  }
  
  public final void setJavaPrimitiveWrap(boolean paramBoolean) {
    Context context = Context.getCurrentContext();
    if (context != null && context.isSealed())
      Context.onSealedMutation(); 
    this.javaPrimitiveWrap = paramBoolean;
  }
  
  public Object wrap(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    if (paramObject == null || paramObject == Undefined.instance || paramObject instanceof Scriptable)
      return paramObject; 
    if (paramClass != null && paramClass.isPrimitive())
      return (paramClass == void.class) ? Undefined.instance : ((paramClass == char.class) ? Integer.valueOf(((Character)paramObject).charValue()) : paramObject); 
    if (!isJavaPrimitiveWrap()) {
      if (paramObject instanceof String || paramObject instanceof Boolean || paramObject instanceof Integer || paramObject instanceof Short || paramObject instanceof Long || paramObject instanceof Float || paramObject instanceof Double)
        return paramObject; 
      if (paramObject instanceof Character)
        return String.valueOf(((Character)paramObject).charValue()); 
    } 
    return paramObject.getClass().isArray() ? NativeJavaArray.wrap(paramScriptable, paramObject) : wrapAsJavaObject(paramContext, paramScriptable, paramObject, paramClass);
  }
  
  public Scriptable wrapAsJavaObject(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    return new NativeJavaObject(paramScriptable, paramObject, paramClass);
  }
  
  public Scriptable wrapJavaClass(Context paramContext, Scriptable paramScriptable, Class<?> paramClass) {
    return new NativeJavaClass(paramScriptable, paramClass);
  }
  
  public Scriptable wrapNewObject(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    return (paramObject instanceof Scriptable) ? (Scriptable)paramObject : (paramObject.getClass().isArray() ? NativeJavaArray.wrap(paramScriptable, paramObject) : wrapAsJavaObject(paramContext, paramScriptable, paramObject, null));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/WrapFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */