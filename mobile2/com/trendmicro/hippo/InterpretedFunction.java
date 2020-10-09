package com.trendmicro.hippo;

import com.trendmicro.hippo.debug.DebuggableScript;

final class InterpretedFunction extends NativeFunction implements Script {
  private static final long serialVersionUID = 541475680333911468L;
  
  InterpreterData idata;
  
  SecurityController securityController;
  
  Object securityDomain;
  
  private InterpretedFunction(InterpretedFunction paramInterpretedFunction, int paramInt) {
    this.idata = paramInterpretedFunction.idata.itsNestedFunctions[paramInt];
    this.securityController = paramInterpretedFunction.securityController;
    this.securityDomain = paramInterpretedFunction.securityDomain;
  }
  
  private InterpretedFunction(InterpreterData paramInterpreterData, Object paramObject) {
    this.idata = paramInterpreterData;
    SecurityController securityController = Context.getContext().getSecurityController();
    if (securityController != null) {
      Object object = securityController.getDynamicSecurityDomain(paramObject);
    } else if (paramObject == null) {
      paramInterpreterData = null;
    } else {
      throw new IllegalArgumentException();
    } 
    this.securityController = securityController;
    this.securityDomain = paramInterpreterData;
  }
  
  static InterpretedFunction createFunction(Context paramContext, Scriptable paramScriptable, InterpretedFunction paramInterpretedFunction, int paramInt) {
    paramInterpretedFunction = new InterpretedFunction(paramInterpretedFunction, paramInt);
    paramInterpretedFunction.initScriptFunction(paramContext, paramScriptable);
    return paramInterpretedFunction;
  }
  
  static InterpretedFunction createFunction(Context paramContext, Scriptable paramScriptable, InterpreterData paramInterpreterData, Object paramObject) {
    InterpretedFunction interpretedFunction = new InterpretedFunction(paramInterpreterData, paramObject);
    interpretedFunction.initScriptFunction(paramContext, paramScriptable);
    return interpretedFunction;
  }
  
  static InterpretedFunction createScript(InterpreterData paramInterpreterData, Object paramObject) {
    return new InterpretedFunction(paramInterpreterData, paramObject);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return !ScriptRuntime.hasTopCall(paramContext) ? ScriptRuntime.doTopCall(this, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, this.idata.isStrict) : Interpreter.interpret(this, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
  }
  
  public Object exec(Context paramContext, Scriptable paramScriptable) {
    if (isScript())
      return !ScriptRuntime.hasTopCall(paramContext) ? ScriptRuntime.doTopCall(this, paramContext, paramScriptable, paramScriptable, ScriptRuntime.emptyArgs, this.idata.isStrict) : Interpreter.interpret(this, paramContext, paramScriptable, paramScriptable, ScriptRuntime.emptyArgs); 
    throw new IllegalStateException();
  }
  
  public DebuggableScript getDebuggableView() {
    return this.idata;
  }
  
  public String getEncodedSource() {
    return Interpreter.getEncodedSource(this.idata);
  }
  
  public String getFunctionName() {
    String str;
    if (this.idata.itsName == null) {
      str = "";
    } else {
      str = this.idata.itsName;
    } 
    return str;
  }
  
  protected int getLanguageVersion() {
    return this.idata.languageVersion;
  }
  
  protected int getParamAndVarCount() {
    return this.idata.argNames.length;
  }
  
  protected int getParamCount() {
    return this.idata.argCount;
  }
  
  protected boolean getParamOrVarConst(int paramInt) {
    return this.idata.argIsConst[paramInt];
  }
  
  protected String getParamOrVarName(int paramInt) {
    return this.idata.argNames[paramInt];
  }
  
  public boolean isScript() {
    boolean bool;
    if (this.idata.itsFunctionType == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Object resumeGenerator(Context paramContext, Scriptable paramScriptable, int paramInt, Object paramObject1, Object paramObject2) {
    return Interpreter.resumeGenerator(paramContext, paramScriptable, paramInt, paramObject1, paramObject2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/InterpretedFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */