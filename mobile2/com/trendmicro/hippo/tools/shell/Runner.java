package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextAction;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.Scriptable;

class Runner implements Runnable, ContextAction<Object> {
  private Object[] args;
  
  private Function f;
  
  ContextFactory factory;
  
  private Script s;
  
  private Scriptable scope;
  
  Runner(Scriptable paramScriptable, Function paramFunction, Object[] paramArrayOfObject) {
    this.scope = paramScriptable;
    this.f = paramFunction;
    this.args = paramArrayOfObject;
  }
  
  Runner(Scriptable paramScriptable, Script paramScript) {
    this.scope = paramScriptable;
    this.s = paramScript;
  }
  
  public Object run(Context paramContext) {
    Function function = this.f;
    if (function != null) {
      Scriptable scriptable = this.scope;
      return function.call(paramContext, scriptable, scriptable, this.args);
    } 
    return this.s.exec(paramContext, this.scope);
  }
  
  public void run() {
    this.factory.call(this);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/Runner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */