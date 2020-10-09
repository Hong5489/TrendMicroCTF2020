package com.trendmicro.hippo.commonjs.module;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.Scriptable;
import java.io.Serializable;

public class RequireBuilder implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private ModuleScriptProvider moduleScriptProvider;
  
  private Script postExec;
  
  private Script preExec;
  
  private boolean sandboxed = true;
  
  public Require createRequire(Context paramContext, Scriptable paramScriptable) {
    return new Require(paramContext, paramScriptable, this.moduleScriptProvider, this.preExec, this.postExec, this.sandboxed);
  }
  
  public RequireBuilder setModuleScriptProvider(ModuleScriptProvider paramModuleScriptProvider) {
    this.moduleScriptProvider = paramModuleScriptProvider;
    return this;
  }
  
  public RequireBuilder setPostExec(Script paramScript) {
    this.postExec = paramScript;
    return this;
  }
  
  public RequireBuilder setPreExec(Script paramScript) {
    this.preExec = paramScript;
    return this;
  }
  
  public RequireBuilder setSandboxed(boolean paramBoolean) {
    this.sandboxed = paramBoolean;
    return this;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/RequireBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */