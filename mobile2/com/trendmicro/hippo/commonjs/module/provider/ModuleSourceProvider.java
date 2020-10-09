package com.trendmicro.hippo.commonjs.module.provider;

import com.trendmicro.hippo.Scriptable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public interface ModuleSourceProvider {
  public static final ModuleSource NOT_MODIFIED = new ModuleSource(null, null, null, null, null);
  
  ModuleSource loadSource(String paramString, Scriptable paramScriptable, Object paramObject) throws IOException, URISyntaxException;
  
  ModuleSource loadSource(URI paramURI1, URI paramURI2, Object paramObject) throws IOException, URISyntaxException;
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/ModuleSourceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */