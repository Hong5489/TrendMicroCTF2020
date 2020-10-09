package com.trendmicro.hippo.optimizer;

import com.trendmicro.hippo.CompilerEnvirons;
import com.trendmicro.hippo.IRFactory;
import com.trendmicro.hippo.JavaAdapter;
import com.trendmicro.hippo.ObjToIntMap;
import com.trendmicro.hippo.Parser;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.ast.AstRoot;
import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.ScriptNode;

public class ClassCompiler {
  private CompilerEnvirons compilerEnv;
  
  private String mainMethodClassName;
  
  private Class<?> targetExtends;
  
  private Class<?>[] targetImplements;
  
  public ClassCompiler(CompilerEnvirons paramCompilerEnvirons) {
    if (paramCompilerEnvirons != null) {
      this.compilerEnv = paramCompilerEnvirons;
      this.mainMethodClassName = "com.trendmicro.hippo.optimizer.OptRuntime";
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public Object[] compileToClassFiles(String paramString1, String paramString2, int paramInt, String paramString3) {
    String str;
    AstRoot astRoot = (new Parser(this.compilerEnv)).parse(paramString1, paramString2, paramInt);
    ScriptNode scriptNode = (new IRFactory(this.compilerEnv)).transformTree(astRoot);
    Class<?> clazz2 = getTargetExtends();
    Class[] arrayOfClass = getTargetImplements();
    if (arrayOfClass == null && clazz2 == null) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramInt != 0) {
      str = paramString3;
    } else {
      str = makeAuxiliaryClassName(paramString3, "1");
    } 
    Codegen codegen = new Codegen();
    codegen.setMainMethodClass(this.mainMethodClassName);
    byte[] arrayOfByte = codegen.compileToClassFile(this.compilerEnv, str, scriptNode, scriptNode.getEncodedSource(), false);
    if (paramInt != 0)
      return new Object[] { str, arrayOfByte }; 
    int i = scriptNode.getFunctionCount();
    ObjToIntMap objToIntMap = new ObjToIntMap(i);
    for (paramInt = 0; paramInt != i; paramInt++) {
      FunctionNode functionNode = scriptNode.getFunctionNode(paramInt);
      String str1 = functionNode.getName();
      if (str1 != null && str1.length() != 0)
        objToIntMap.put(str1, functionNode.getParamCount()); 
    } 
    Class<?> clazz1 = clazz2;
    if (clazz2 == null)
      clazz1 = ScriptRuntime.ObjectClass; 
    return new Object[] { paramString3, JavaAdapter.createAdapterCode(objToIntMap, paramString3, clazz1, arrayOfClass, str), str, arrayOfByte };
  }
  
  public CompilerEnvirons getCompilerEnv() {
    return this.compilerEnv;
  }
  
  public String getMainMethodClass() {
    return this.mainMethodClassName;
  }
  
  public Class<?> getTargetExtends() {
    return this.targetExtends;
  }
  
  public Class<?>[] getTargetImplements() {
    Class<?>[] arrayOfClass = this.targetImplements;
    if (arrayOfClass == null) {
      arrayOfClass = null;
    } else {
      arrayOfClass = (Class[])arrayOfClass.clone();
    } 
    return arrayOfClass;
  }
  
  protected String makeAuxiliaryClassName(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString1);
    stringBuilder.append(paramString2);
    return stringBuilder.toString();
  }
  
  public void setMainMethodClass(String paramString) {
    this.mainMethodClassName = paramString;
  }
  
  public void setTargetExtends(Class<?> paramClass) {
    this.targetExtends = paramClass;
  }
  
  public void setTargetImplements(Class<?>[] paramArrayOfClass) {
    if (paramArrayOfClass == null) {
      paramArrayOfClass = null;
    } else {
      paramArrayOfClass = (Class[])paramArrayOfClass.clone();
    } 
    this.targetImplements = paramArrayOfClass;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/ClassCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */