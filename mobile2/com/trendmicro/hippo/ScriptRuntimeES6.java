package com.trendmicro.hippo;

public class ScriptRuntimeES6 {
  public static Scriptable requireObjectCoercible(Context paramContext, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable != null && !Undefined.isUndefined(paramScriptable))
      return paramScriptable; 
    throw ScriptRuntime.typeError2("msg.called.null.or.undefined", paramIdFunctionObject.getTag(), paramIdFunctionObject.getFunctionName());
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ScriptRuntimeES6.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */