package com.trendmicro.hippo;

import com.trendmicro.hippo.debug.DebuggableScript;

public abstract class NativeFunction extends BaseFunction {
  private static final long serialVersionUID = 8713897114082216401L;
  
  final String decompile(int paramInt1, int paramInt2) {
    String str = getEncodedSource();
    if (str == null)
      return super.decompile(paramInt1, paramInt2); 
    UintMap uintMap = new UintMap(1);
    uintMap.put(1, paramInt1);
    return Decompiler.decompile(str, paramInt2, uintMap);
  }
  
  public int getArity() {
    return getParamCount();
  }
  
  public DebuggableScript getDebuggableView() {
    return null;
  }
  
  public String getEncodedSource() {
    return null;
  }
  
  protected abstract int getLanguageVersion();
  
  public int getLength() {
    int i = getParamCount();
    if (getLanguageVersion() != 120)
      return i; 
    NativeCall nativeCall = ScriptRuntime.findFunctionActivation(Context.getContext(), this);
    return (nativeCall == null) ? i : nativeCall.originalArgs.length;
  }
  
  protected abstract int getParamAndVarCount();
  
  protected abstract int getParamCount();
  
  protected boolean getParamOrVarConst(int paramInt) {
    return false;
  }
  
  protected abstract String getParamOrVarName(int paramInt);
  
  public final void initScriptFunction(Context paramContext, Scriptable paramScriptable) {
    ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable);
  }
  
  @Deprecated
  public String jsGet_name() {
    return getFunctionName();
  }
  
  public Object resumeGenerator(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject1, Object paramObject2) {
    throw new EvaluatorException("resumeGenerator() not implemented");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */