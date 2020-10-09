package com.trendmicro.hippo;

public class JavaScriptException extends HippoException {
  private static final long serialVersionUID = -7666130513694669293L;
  
  private Object value;
  
  @Deprecated
  public JavaScriptException(Object paramObject) {
    this(paramObject, "", 0);
  }
  
  public JavaScriptException(Object paramObject, String paramString, int paramInt) {
    recordErrorOrigin(paramString, paramInt, null, 0);
    this.value = paramObject;
    if (paramObject instanceof NativeError && Context.getContext().hasFeature(10)) {
      paramObject = paramObject;
      if (!paramObject.has("fileName", (Scriptable)paramObject))
        paramObject.put("fileName", (Scriptable)paramObject, paramString); 
      if (!paramObject.has("lineNumber", (Scriptable)paramObject))
        paramObject.put("lineNumber", (Scriptable)paramObject, Integer.valueOf(paramInt)); 
      paramObject.setStackProvider(this);
    } 
  }
  
  public String details() {
    Object object = this.value;
    if (object == null)
      return "null"; 
    if (object instanceof NativeError)
      return object.toString(); 
    try {
      return ScriptRuntime.toString(object);
    } catch (RuntimeException runtimeException) {
      Object object1 = this.value;
      return (object1 instanceof Scriptable) ? ScriptRuntime.defaultObjectToString((Scriptable)object1) : object1.toString();
    } 
  }
  
  @Deprecated
  public int getLineNumber() {
    return lineNumber();
  }
  
  @Deprecated
  public String getSourceName() {
    return sourceName();
  }
  
  public Object getValue() {
    return this.value;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/JavaScriptException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */