package com.trendmicro.hippo;

public interface ErrorReporter {
  void error(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2);
  
  EvaluatorException runtimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2);
  
  void warning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ErrorReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */