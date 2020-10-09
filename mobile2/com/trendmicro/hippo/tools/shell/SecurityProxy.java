package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.SecurityController;

public abstract class SecurityProxy extends SecurityController {
  protected abstract void callProcessFileSecure(Context paramContext, Scriptable paramScriptable, String paramString);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/SecurityProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */