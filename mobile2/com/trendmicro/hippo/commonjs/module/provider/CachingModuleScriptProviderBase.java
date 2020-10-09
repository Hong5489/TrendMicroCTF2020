package com.trendmicro.hippo.commonjs.module.provider;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.commonjs.module.ModuleScript;
import com.trendmicro.hippo.commonjs.module.ModuleScriptProvider;
import java.io.Reader;
import java.io.Serializable;
import java.net.URI;

public abstract class CachingModuleScriptProviderBase implements ModuleScriptProvider, Serializable {
  private static final int loadConcurrencyLevel = Runtime.getRuntime().availableProcessors() * 8;
  
  private static final int loadLockCount;
  
  private static final int loadLockMask;
  
  private static final int loadLockShift;
  
  private static final long serialVersionUID = 1L;
  
  private final Object[] loadLocks = new Object[loadLockCount];
  
  private final ModuleSourceProvider moduleSourceProvider;
  
  static {
    byte b = 0;
    int i;
    for (i = 1; i < loadConcurrencyLevel; i <<= 1)
      b++; 
    loadLockShift = 32 - b;
    loadLockMask = i - 1;
    loadLockCount = i;
  }
  
  protected CachingModuleScriptProviderBase(ModuleSourceProvider paramModuleSourceProvider) {
    byte b = 0;
    while (true) {
      Object[] arrayOfObject = this.loadLocks;
      if (b < arrayOfObject.length) {
        arrayOfObject[b] = new Object();
        b++;
        continue;
      } 
      this.moduleSourceProvider = paramModuleSourceProvider;
      return;
    } 
  }
  
  private static boolean equal(Object paramObject1, Object paramObject2) {
    boolean bool;
    if (paramObject1 == null) {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      } 
    } else {
      bool = paramObject1.equals(paramObject2);
    } 
    return bool;
  }
  
  protected static int getConcurrencyLevel() {
    return loadLockCount;
  }
  
  private static Object getValidator(CachedModuleScript paramCachedModuleScript) {
    Object object;
    if (paramCachedModuleScript == null) {
      paramCachedModuleScript = null;
    } else {
      object = paramCachedModuleScript.getValidator();
    } 
    return object;
  }
  
  protected abstract CachedModuleScript getLoadedModule(String paramString);
  
  public ModuleScript getModuleScript(Context paramContext, String paramString, URI paramURI1, URI paramURI2, Scriptable paramScriptable) throws Exception {
    ModuleSource moduleSource;
    CachedModuleScript cachedModuleScript = getLoadedModule(paramString);
    Object object = getValidator(cachedModuleScript);
    if (paramURI1 == null) {
      moduleSource = this.moduleSourceProvider.loadSource(paramString, paramScriptable, object);
    } else {
      moduleSource = this.moduleSourceProvider.loadSource((URI)moduleSource, paramURI2, object);
    } 
    if (moduleSource == ModuleSourceProvider.NOT_MODIFIED)
      return cachedModuleScript.getModule(); 
    if (moduleSource == null)
      return null; 
    Reader reader = moduleSource.getReader();
    try {
      int i = paramString.hashCode();
      Object object1 = this.loadLocks[i >>> loadLockShift & loadLockMask];
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      try {
        ModuleScript moduleScript1;
        cachedModuleScript = getLoadedModule(paramString);
        if (cachedModuleScript != null && !equal(object, getValidator(cachedModuleScript))) {
          moduleScript1 = cachedModuleScript.getModule();
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          if (reader != null)
            reader.close(); 
          return moduleScript1;
        } 
        URI uRI = moduleSource.getUri();
        ModuleScript moduleScript2 = new ModuleScript();
        String str = uRI.toString();
        object = moduleSource.getSecurityDomain();
        try {
          this(moduleScript1.compileReader(reader, str, 1, object), uRI, moduleSource.getBase());
          putLoadedModule(paramString, moduleScript2, moduleSource.getValidator());
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          if (reader != null)
            reader.close(); 
          return moduleScript2;
        } finally {}
      } finally {}
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      try {
        throw paramContext;
      } finally {}
    } finally {}
    if (reader != null)
      try {
        reader.close();
      } finally {
        paramString = null;
      }  
    throw paramContext;
  }
  
  protected abstract void putLoadedModule(String paramString, ModuleScript paramModuleScript, Object paramObject);
  
  public static class CachedModuleScript {
    private final ModuleScript moduleScript;
    
    private final Object validator;
    
    public CachedModuleScript(ModuleScript param1ModuleScript, Object param1Object) {
      this.moduleScript = param1ModuleScript;
      this.validator = param1Object;
    }
    
    ModuleScript getModule() {
      return this.moduleScript;
    }
    
    Object getValidator() {
      return this.validator;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/CachingModuleScriptProviderBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */