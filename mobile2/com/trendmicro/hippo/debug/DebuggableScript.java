package com.trendmicro.hippo.debug;

public interface DebuggableScript {
  DebuggableScript getFunction(int paramInt);
  
  int getFunctionCount();
  
  String getFunctionName();
  
  int[] getLineNumbers();
  
  int getParamAndVarCount();
  
  int getParamCount();
  
  String getParamOrVarName(int paramInt);
  
  DebuggableScript getParent();
  
  String getSourceName();
  
  boolean isFunction();
  
  boolean isGeneratedScript();
  
  boolean isTopLevel();
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/debug/DebuggableScript.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */