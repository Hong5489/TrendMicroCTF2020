package com.trendmicro.hippo.commonjs.module;

import com.trendmicro.hippo.Script;
import java.io.Serializable;
import java.net.URI;

public class ModuleScript implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final URI base;
  
  private final Script script;
  
  private final URI uri;
  
  public ModuleScript(Script paramScript, URI paramURI1, URI paramURI2) {
    this.script = paramScript;
    this.uri = paramURI1;
    this.base = paramURI2;
  }
  
  public URI getBase() {
    return this.base;
  }
  
  public Script getScript() {
    return this.script;
  }
  
  public URI getUri() {
    return this.uri;
  }
  
  public boolean isSandboxed() {
    URI uRI = this.base;
    if (uRI != null) {
      URI uRI1 = this.uri;
      if (uRI1 != null && !uRI.relativize(uRI1).isAbsolute())
        return true; 
    } 
    return false;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/ModuleScript.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */