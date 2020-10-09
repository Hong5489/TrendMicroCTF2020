package com.trendmicro.hippo;

public class EvaluatorException extends HippoException {
  private static final long serialVersionUID = -8743165779676009808L;
  
  public EvaluatorException(String paramString) {
    super(paramString);
  }
  
  public EvaluatorException(String paramString1, String paramString2, int paramInt) {
    this(paramString1, paramString2, paramInt, (String)null, 0);
  }
  
  public EvaluatorException(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    super(paramString1);
    recordErrorOrigin(paramString2, paramInt1, paramString3, paramInt2);
  }
  
  @Deprecated
  public int getColumnNumber() {
    return columnNumber();
  }
  
  @Deprecated
  public int getLineNumber() {
    return lineNumber();
  }
  
  @Deprecated
  public String getLineSource() {
    return lineSource();
  }
  
  @Deprecated
  public String getSourceName() {
    return sourceName();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/EvaluatorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */