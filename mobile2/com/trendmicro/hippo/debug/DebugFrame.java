package com.trendmicro.hippo.debug;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Scriptable;

public interface DebugFrame {
  void onDebuggerStatement(Context paramContext);
  
  void onEnter(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  
  void onExceptionThrown(Context paramContext, Throwable paramThrowable);
  
  void onExit(Context paramContext, boolean paramBoolean, Object paramObject);
  
  void onLineChange(Context paramContext, int paramInt);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/debug/DebugFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */