package com.trendmicro.hippo;

import java.io.Serializable;

public abstract class Ref implements Serializable {
  private static final long serialVersionUID = 4044540354730911424L;
  
  public boolean delete(Context paramContext) {
    return false;
  }
  
  public abstract Object get(Context paramContext);
  
  public boolean has(Context paramContext) {
    return true;
  }
  
  public Object set(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    return set(paramContext, paramObject);
  }
  
  @Deprecated
  public abstract Object set(Context paramContext, Object paramObject);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Ref.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */