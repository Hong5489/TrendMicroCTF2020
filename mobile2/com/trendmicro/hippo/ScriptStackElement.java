package com.trendmicro.hippo;

import java.io.Serializable;

public final class ScriptStackElement implements Serializable {
  private static final long serialVersionUID = -6416688260860477449L;
  
  public final String fileName;
  
  public final String functionName;
  
  public final int lineNumber;
  
  public ScriptStackElement(String paramString1, String paramString2, int paramInt) {
    this.fileName = paramString1;
    this.functionName = paramString2;
    this.lineNumber = paramInt;
  }
  
  private void appendV8Location(StringBuilder paramStringBuilder) {
    paramStringBuilder.append(this.fileName);
    paramStringBuilder.append(':');
    int i = this.lineNumber;
    if (i <= -1)
      i = 0; 
    paramStringBuilder.append(i);
    paramStringBuilder.append(":0");
  }
  
  public void renderJavaStyle(StringBuilder paramStringBuilder) {
    paramStringBuilder.append("\tat ");
    paramStringBuilder.append(this.fileName);
    if (this.lineNumber > -1) {
      paramStringBuilder.append(':');
      paramStringBuilder.append(this.lineNumber);
    } 
    if (this.functionName != null) {
      paramStringBuilder.append(" (");
      paramStringBuilder.append(this.functionName);
      paramStringBuilder.append(')');
    } 
  }
  
  public void renderTrendMicroStyle(StringBuilder paramStringBuilder) {
    String str = this.functionName;
    if (str != null) {
      paramStringBuilder.append(str);
      paramStringBuilder.append("()");
    } 
    paramStringBuilder.append('@');
    paramStringBuilder.append(this.fileName);
    if (this.lineNumber > -1) {
      paramStringBuilder.append(':');
      paramStringBuilder.append(this.lineNumber);
    } 
  }
  
  public void renderV8Style(StringBuilder paramStringBuilder) {
    paramStringBuilder.append("    at ");
    String str = this.functionName;
    if (str == null || "anonymous".equals(str) || "undefined".equals(this.functionName)) {
      appendV8Location(paramStringBuilder);
      return;
    } 
    paramStringBuilder.append(this.functionName);
    paramStringBuilder.append(" (");
    appendV8Location(paramStringBuilder);
    paramStringBuilder.append(')');
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    renderTrendMicroStyle(stringBuilder);
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ScriptStackElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */