package com.trendmicro.hippo;

public class EcmaError extends HippoException {
  private static final long serialVersionUID = -6261226256957286699L;
  
  private String errorMessage;
  
  private String errorName;
  
  @Deprecated
  public EcmaError(Scriptable paramScriptable, String paramString1, int paramInt1, int paramInt2, String paramString2) {
    this("InternalError", ScriptRuntime.toString(paramScriptable), paramString1, paramInt1, paramString2, paramInt2);
  }
  
  EcmaError(String paramString1, String paramString2, String paramString3, int paramInt1, String paramString4, int paramInt2) {
    recordErrorOrigin(paramString3, paramInt1, paramString4, paramInt2);
    this.errorName = paramString1;
    this.errorMessage = paramString2;
  }
  
  public String details() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.errorName);
    stringBuilder.append(": ");
    stringBuilder.append(this.errorMessage);
    return stringBuilder.toString();
  }
  
  @Deprecated
  public int getColumnNumber() {
    return columnNumber();
  }
  
  public String getErrorMessage() {
    return this.errorMessage;
  }
  
  @Deprecated
  public Scriptable getErrorObject() {
    return null;
  }
  
  @Deprecated
  public int getLineNumber() {
    return lineNumber();
  }
  
  @Deprecated
  public String getLineSource() {
    return lineSource();
  }
  
  public String getName() {
    return this.errorName;
  }
  
  @Deprecated
  public String getSourceName() {
    return sourceName();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/EcmaError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */