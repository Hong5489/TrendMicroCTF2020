package com.trendmicro.hippo;

public interface ConstProperties {
  void defineConst(String paramString, Scriptable paramScriptable);
  
  boolean isConst(String paramString);
  
  void putConst(String paramString, Scriptable paramScriptable, Object paramObject);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ConstProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */