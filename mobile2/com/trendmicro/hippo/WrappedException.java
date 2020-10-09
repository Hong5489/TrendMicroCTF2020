package com.trendmicro.hippo;

public class WrappedException extends EvaluatorException {
  private static final long serialVersionUID = -1551979216966520648L;
  
  private Throwable exception;
  
  public WrappedException(Throwable paramThrowable) {
    super(stringBuilder.toString());
    this.exception = paramThrowable;
    initCause(paramThrowable);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    String str = Context.getSourcePositionFromStack(arrayOfInt);
    int i = arrayOfInt[0];
    if (str != null)
      initSourceName(str); 
    if (i != 0)
      initLineNumber(i); 
  }
  
  public Throwable getWrappedException() {
    return this.exception;
  }
  
  @Deprecated
  public Object unwrap() {
    return getWrappedException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/WrappedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */