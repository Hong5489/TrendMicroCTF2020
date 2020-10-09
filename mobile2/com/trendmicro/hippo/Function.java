package com.trendmicro.hippo;

public interface Function extends Scriptable, Callable {
  Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  
  Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */