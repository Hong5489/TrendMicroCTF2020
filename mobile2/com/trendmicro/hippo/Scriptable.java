package com.trendmicro.hippo;

public interface Scriptable {
  public static final Object NOT_FOUND = UniqueTag.NOT_FOUND;
  
  void delete(int paramInt);
  
  void delete(String paramString);
  
  Object get(int paramInt, Scriptable paramScriptable);
  
  Object get(String paramString, Scriptable paramScriptable);
  
  String getClassName();
  
  Object getDefaultValue(Class<?> paramClass);
  
  Object[] getIds();
  
  Scriptable getParentScope();
  
  Scriptable getPrototype();
  
  boolean has(int paramInt, Scriptable paramScriptable);
  
  boolean has(String paramString, Scriptable paramScriptable);
  
  boolean hasInstance(Scriptable paramScriptable);
  
  void put(int paramInt, Scriptable paramScriptable, Object paramObject);
  
  void put(String paramString, Scriptable paramScriptable, Object paramObject);
  
  void setParentScope(Scriptable paramScriptable);
  
  void setPrototype(Scriptable paramScriptable);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Scriptable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */