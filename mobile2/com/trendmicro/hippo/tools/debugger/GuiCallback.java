package com.trendmicro.hippo.tools.debugger;

public interface GuiCallback {
  void dispatchNextGuiEvent() throws InterruptedException;
  
  void enterInterrupt(Dim.StackFrame paramStackFrame, String paramString1, String paramString2);
  
  boolean isGuiEventThread();
  
  void updateSourceText(Dim.SourceInfo paramSourceInfo);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/GuiCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */