package com.trendmicro.hippo.commonjs.module.provider;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.commonjs.module.ModuleScript;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SoftCachingModuleScriptProvider extends CachingModuleScriptProviderBase {
  private static final long serialVersionUID = 1L;
  
  private transient ReferenceQueue<Script> scriptRefQueue = new ReferenceQueue<>();
  
  private transient ConcurrentMap<String, ScriptReference> scripts = new ConcurrentHashMap<>(16, 0.75F, getConcurrencyLevel());
  
  public SoftCachingModuleScriptProvider(ModuleSourceProvider paramModuleSourceProvider) {
    super(paramModuleSourceProvider);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    this.scriptRefQueue = new ReferenceQueue<>();
    this.scripts = new ConcurrentHashMap<>();
    for (Map.Entry entry : ((Map)paramObjectInputStream.readObject()).entrySet()) {
      CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = (CachingModuleScriptProviderBase.CachedModuleScript)entry.getValue();
      putLoadedModule((String)entry.getKey(), cachedModuleScript.getModule(), cachedModuleScript.getValidator());
    } 
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (Map.Entry<String, ScriptReference> entry : this.scripts.entrySet()) {
      CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = ((ScriptReference)entry.getValue()).getCachedModuleScript();
      if (cachedModuleScript != null)
        hashMap.put(entry.getKey(), cachedModuleScript); 
    } 
    paramObjectOutputStream.writeObject(hashMap);
  }
  
  protected CachingModuleScriptProviderBase.CachedModuleScript getLoadedModule(String paramString) {
    ScriptReference scriptReference = this.scripts.get(paramString);
    if (scriptReference != null) {
      CachingModuleScriptProviderBase.CachedModuleScript cachedModuleScript = scriptReference.getCachedModuleScript();
    } else {
      scriptReference = null;
    } 
    return (CachingModuleScriptProviderBase.CachedModuleScript)scriptReference;
  }
  
  public ModuleScript getModuleScript(Context paramContext, String paramString, URI paramURI1, URI paramURI2, Scriptable paramScriptable) throws Exception {
    while (true) {
      ScriptReference scriptReference = (ScriptReference)this.scriptRefQueue.poll();
      if (scriptReference == null)
        return super.getModuleScript(paramContext, paramString, paramURI1, paramURI2, paramScriptable); 
      this.scripts.remove(scriptReference.getModuleId(), scriptReference);
    } 
  }
  
  protected void putLoadedModule(String paramString, ModuleScript paramModuleScript, Object paramObject) {
    this.scripts.put(paramString, new ScriptReference(paramModuleScript.getScript(), paramString, paramModuleScript.getUri(), paramModuleScript.getBase(), paramObject, this.scriptRefQueue));
  }
  
  private static class ScriptReference extends SoftReference<Script> {
    private final URI base;
    
    private final String moduleId;
    
    private final URI uri;
    
    private final Object validator;
    
    ScriptReference(Script param1Script, String param1String, URI param1URI1, URI param1URI2, Object param1Object, ReferenceQueue<Script> param1ReferenceQueue) {
      super(param1Script, param1ReferenceQueue);
      this.moduleId = param1String;
      this.uri = param1URI1;
      this.base = param1URI2;
      this.validator = param1Object;
    }
    
    CachingModuleScriptProviderBase.CachedModuleScript getCachedModuleScript() {
      Script script = get();
      return (script == null) ? null : new CachingModuleScriptProviderBase.CachedModuleScript(new ModuleScript(script, this.uri, this.base), this.validator);
    }
    
    String getModuleId() {
      return this.moduleId;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/SoftCachingModuleScriptProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */