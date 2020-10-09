package com.trendmicro.hippo.commonjs.module.provider;

import java.io.Reader;
import java.io.Serializable;
import java.net.URI;

public class ModuleSource implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final URI base;
  
  private final Reader reader;
  
  private final Object securityDomain;
  
  private final URI uri;
  
  private final Object validator;
  
  public ModuleSource(Reader paramReader, Object paramObject1, URI paramURI1, URI paramURI2, Object paramObject2) {
    this.reader = paramReader;
    this.securityDomain = paramObject1;
    this.uri = paramURI1;
    this.base = paramURI2;
    this.validator = paramObject2;
  }
  
  public URI getBase() {
    return this.base;
  }
  
  public Reader getReader() {
    return this.reader;
  }
  
  public Object getSecurityDomain() {
    return this.securityDomain;
  }
  
  public URI getUri() {
    return this.uri;
  }
  
  public Object getValidator() {
    return this.validator;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/ModuleSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */