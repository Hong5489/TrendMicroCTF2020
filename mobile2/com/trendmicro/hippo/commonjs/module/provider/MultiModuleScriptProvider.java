package com.trendmicro.hippo.commonjs.module.provider;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.commonjs.module.ModuleScript;
import com.trendmicro.hippo.commonjs.module.ModuleScriptProvider;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;

public class MultiModuleScriptProvider implements ModuleScriptProvider {
  private final ModuleScriptProvider[] providers;
  
  public MultiModuleScriptProvider(Iterable<? extends ModuleScriptProvider> paramIterable) {
    LinkedList<ModuleScriptProvider> linkedList = new LinkedList();
    Iterator<? extends ModuleScriptProvider> iterator = paramIterable.iterator();
    while (iterator.hasNext())
      linkedList.add(iterator.next()); 
    this.providers = linkedList.<ModuleScriptProvider>toArray(new ModuleScriptProvider[linkedList.size()]);
  }
  
  public ModuleScript getModuleScript(Context paramContext, String paramString, URI paramURI1, URI paramURI2, Scriptable paramScriptable) throws Exception {
    ModuleScriptProvider[] arrayOfModuleScriptProvider = this.providers;
    int i = arrayOfModuleScriptProvider.length;
    for (byte b = 0; b < i; b++) {
      ModuleScript moduleScript = arrayOfModuleScriptProvider[b].getModuleScript(paramContext, paramString, paramURI1, paramURI2, paramScriptable);
      if (moduleScript != null)
        return moduleScript; 
    } 
    return null;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/MultiModuleScriptProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */