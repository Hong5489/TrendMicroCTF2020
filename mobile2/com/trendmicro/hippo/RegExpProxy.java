package com.trendmicro.hippo;

public interface RegExpProxy {
  public static final int RA_MATCH = 1;
  
  public static final int RA_REPLACE = 2;
  
  public static final int RA_SEARCH = 3;
  
  Object action(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, int paramInt);
  
  Object compileRegExp(Context paramContext, String paramString1, String paramString2);
  
  int find_split(Context paramContext, Scriptable paramScriptable1, String paramString1, String paramString2, Scriptable paramScriptable2, int[] paramArrayOfint1, int[] paramArrayOfint2, boolean[] paramArrayOfboolean, String[][] paramArrayOfString);
  
  boolean isRegExp(Scriptable paramScriptable);
  
  Object js_split(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject);
  
  Scriptable wrapRegExp(Context paramContext, Scriptable paramScriptable, Object paramObject);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/RegExpProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */