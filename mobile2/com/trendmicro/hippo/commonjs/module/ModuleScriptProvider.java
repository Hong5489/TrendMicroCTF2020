package com.trendmicro.hippo.commonjs.module;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Scriptable;
import java.net.URI;

public interface ModuleScriptProvider {
  ModuleScript getModuleScript(Context paramContext, String paramString, URI paramURI1, URI paramURI2, Scriptable paramScriptable) throws Exception;
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/ModuleScriptProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */