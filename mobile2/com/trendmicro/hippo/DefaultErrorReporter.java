package com.trendmicro.hippo;

class DefaultErrorReporter implements ErrorReporter {
  static final DefaultErrorReporter instance = new DefaultErrorReporter();
  
  private ErrorReporter chainedReporter;
  
  private boolean forEval;
  
  static ErrorReporter forEval(ErrorReporter paramErrorReporter) {
    DefaultErrorReporter defaultErrorReporter = new DefaultErrorReporter();
    defaultErrorReporter.forEval = true;
    defaultErrorReporter.chainedReporter = paramErrorReporter;
    return defaultErrorReporter;
  }
  
  public void error(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    if (this.forEval) {
      String str = "SyntaxError";
      if (paramString1.startsWith("TypeError: ")) {
        str = "TypeError";
        paramString1 = paramString1.substring("TypeError: ".length());
      } 
      throw ScriptRuntime.constructError(str, paramString1, paramString2, paramInt1, paramString3, paramInt2);
    } 
    ErrorReporter errorReporter = this.chainedReporter;
    if (errorReporter != null) {
      errorReporter.error(paramString1, paramString2, paramInt1, paramString3, paramInt2);
      return;
    } 
    throw runtimeError(paramString1, paramString2, paramInt1, paramString3, paramInt2);
  }
  
  public EvaluatorException runtimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    ErrorReporter errorReporter = this.chainedReporter;
    return (errorReporter != null) ? errorReporter.runtimeError(paramString1, paramString2, paramInt1, paramString3, paramInt2) : new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
  }
  
  public void warning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    ErrorReporter errorReporter = this.chainedReporter;
    if (errorReporter != null)
      errorReporter.warning(paramString1, paramString2, paramInt1, paramString3, paramInt2); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/DefaultErrorReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */