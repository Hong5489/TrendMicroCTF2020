package com.trendmicro.hippo;

public interface SymbolScriptable {
  void delete(Symbol paramSymbol);
  
  Object get(Symbol paramSymbol, Scriptable paramScriptable);
  
  boolean has(Symbol paramSymbol, Scriptable paramScriptable);
  
  void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SymbolScriptable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */