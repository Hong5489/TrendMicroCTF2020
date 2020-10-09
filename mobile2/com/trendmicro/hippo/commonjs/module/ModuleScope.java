package com.trendmicro.hippo.commonjs.module;

import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.TopLevel;
import java.net.URI;

public class ModuleScope extends TopLevel {
  private static final long serialVersionUID = 1L;
  
  private final URI base;
  
  private final URI uri;
  
  public ModuleScope(Scriptable paramScriptable, URI paramURI1, URI paramURI2) {
    this.uri = paramURI1;
    this.base = paramURI2;
    setPrototype(paramScriptable);
    cacheBuiltins();
  }
  
  public URI getBase() {
    return this.base;
  }
  
  public URI getUri() {
    return this.uri;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/ModuleScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */