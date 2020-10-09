package com.trendmicro.hippo.commonjs.module.provider;

import com.trendmicro.hippo.commonjs.module.ModuleScript;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StrongCachingModuleScriptProvider extends CachingModuleScriptProviderBase {
  private static final long serialVersionUID = 1L;
  
  private final Map<String, CachingModuleScriptProviderBase.CachedModuleScript> modules = new ConcurrentHashMap<>(16, 0.75F, getConcurrencyLevel());
  
  public StrongCachingModuleScriptProvider(ModuleSourceProvider paramModuleSourceProvider) {
    super(paramModuleSourceProvider);
  }
  
  protected CachingModuleScriptProviderBase.CachedModuleScript getLoadedModule(String paramString) {
    return this.modules.get(paramString);
  }
  
  protected void putLoadedModule(String paramString, ModuleScript paramModuleScript, Object paramObject) {
    this.modules.put(paramString, new CachingModuleScriptProviderBase.CachedModuleScript(paramModuleScript, paramObject));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/StrongCachingModuleScriptProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */