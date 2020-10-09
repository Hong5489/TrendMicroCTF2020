package com.trendmicro.hippo.debug;

import com.trendmicro.hippo.Context;

public interface Debugger {
  DebugFrame getFrame(Context paramContext, DebuggableScript paramDebuggableScript);
  
  void handleCompilationDone(Context paramContext, DebuggableScript paramDebuggableScript, String paramString);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/debug/Debugger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */