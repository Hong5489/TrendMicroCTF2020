package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.ScriptNode;
import java.util.List;

public interface Evaluator {
  void captureStackInfo(HippoException paramHippoException);
  
  Object compile(CompilerEnvirons paramCompilerEnvirons, ScriptNode paramScriptNode, String paramString, boolean paramBoolean);
  
  Function createFunctionObject(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2);
  
  Script createScriptObject(Object paramObject1, Object paramObject2);
  
  String getPatchedStack(HippoException paramHippoException, String paramString);
  
  List<String> getScriptStack(HippoException paramHippoException);
  
  String getSourcePositionFromStack(Context paramContext, int[] paramArrayOfint);
  
  void setEvalScriptFlag(Script paramScript);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Evaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */