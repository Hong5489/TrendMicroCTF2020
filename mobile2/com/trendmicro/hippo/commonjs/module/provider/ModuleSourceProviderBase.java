package com.trendmicro.hippo.commonjs.module.provider;

import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class ModuleSourceProviderBase implements ModuleSourceProvider, Serializable {
  private static final long serialVersionUID = 1L;
  
  private static String ensureTrailingSlash(String paramString) {
    if (!paramString.endsWith("/"))
      paramString = paramString.concat("/"); 
    return paramString;
  }
  
  private ModuleSource loadFromPathArray(String paramString, Scriptable paramScriptable, Object paramObject) throws IOException {
    int i;
    long l = ScriptRuntime.toUint32(ScriptableObject.getProperty(paramScriptable, "length"));
    if (l > 2147483647L) {
      i = Integer.MAX_VALUE;
    } else {
      i = (int)l;
    } 
    byte b = 0;
    while (b < i) {
      String str = ensureTrailingSlash((String)ScriptableObject.getTypedProperty(paramScriptable, b, String.class));
      try {
        URI uRI1 = new URI();
        this(str);
        URI uRI2 = uRI1;
        if (!uRI1.isAbsolute()) {
          File file = new File();
          this(str);
          uRI2 = file.toURI().resolve("");
        } 
        ModuleSource moduleSource = loadFromUri(uRI2.resolve(paramString), uRI2, paramObject);
        if (moduleSource != null)
          return moduleSource; 
        b++;
      } catch (URISyntaxException uRISyntaxException) {
        throw new MalformedURLException(uRISyntaxException.getMessage());
      } 
    } 
    return null;
  }
  
  protected boolean entityNeedsRevalidation(Object paramObject) {
    return true;
  }
  
  protected ModuleSource loadFromFallbackLocations(String paramString, Object paramObject) throws IOException, URISyntaxException {
    return null;
  }
  
  protected ModuleSource loadFromPrivilegedLocations(String paramString, Object paramObject) throws IOException, URISyntaxException {
    return null;
  }
  
  protected abstract ModuleSource loadFromUri(URI paramURI1, URI paramURI2, Object paramObject) throws IOException, URISyntaxException;
  
  public ModuleSource loadSource(String paramString, Scriptable paramScriptable, Object paramObject) throws IOException, URISyntaxException {
    if (!entityNeedsRevalidation(paramObject))
      return NOT_MODIFIED; 
    ModuleSource moduleSource = loadFromPrivilegedLocations(paramString, paramObject);
    if (moduleSource != null)
      return moduleSource; 
    if (paramScriptable != null) {
      ModuleSource moduleSource1 = loadFromPathArray(paramString, paramScriptable, paramObject);
      if (moduleSource1 != null)
        return moduleSource1; 
    } 
    return loadFromFallbackLocations(paramString, paramObject);
  }
  
  public ModuleSource loadSource(URI paramURI1, URI paramURI2, Object paramObject) throws IOException, URISyntaxException {
    return loadFromUri(paramURI1, paramURI2, paramObject);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/ModuleSourceProviderBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */