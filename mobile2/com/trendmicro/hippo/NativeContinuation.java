package com.trendmicro.hippo;

import java.util.Objects;

public final class NativeContinuation extends IdScriptableObject implements Function {
  private static final Object FTAG = "Continuation";
  
  private static final int Id_constructor = 1;
  
  private static final int MAX_PROTOTYPE_ID = 1;
  
  private static final long serialVersionUID = 1794167133757605367L;
  
  private Object implementation;
  
  public static boolean equalImplementations(NativeContinuation paramNativeContinuation1, NativeContinuation paramNativeContinuation2) {
    return Objects.equals(paramNativeContinuation1.implementation, paramNativeContinuation2.implementation);
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeContinuation()).exportAsJSClass(1, paramScriptable, paramBoolean);
  }
  
  public static boolean isContinuationConstructor(IdFunctionObject paramIdFunctionObject) {
    return (paramIdFunctionObject.hasTag(FTAG) && paramIdFunctionObject.methodId() == 1);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return Interpreter.restartContinuation(this, paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    throw Context.reportRuntimeError("Direct call is not supported");
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(FTAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != 1)
      throw new IllegalArgumentException(String.valueOf(i)); 
    throw Context.reportRuntimeError("Direct call is not supported");
  }
  
  protected int findPrototypeId(String paramString) {
    boolean bool1 = false;
    String str = null;
    if (paramString.length() == 11) {
      str = "constructor";
      bool1 = true;
    } 
    boolean bool2 = bool1;
    if (str != null) {
      bool2 = bool1;
      if (str != paramString) {
        bool2 = bool1;
        if (!str.equals(paramString))
          bool2 = false; 
      } 
    } 
    return bool2;
  }
  
  public String getClassName() {
    return "Continuation";
  }
  
  public Object getImplementation() {
    return this.implementation;
  }
  
  public void initImplementation(Object paramObject) {
    this.implementation = paramObject;
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt == 1) {
      initPrototypeMethod(FTAG, paramInt, "constructor", 0);
      return;
    } 
    throw new IllegalArgumentException(String.valueOf(paramInt));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeContinuation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */