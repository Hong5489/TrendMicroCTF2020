package com.trendmicro.hippo.commonjs.module.provider;

import java.io.Serializable;
import java.net.URLConnection;

public class DefaultUrlConnectionExpiryCalculator implements UrlConnectionExpiryCalculator, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final long relativeExpiry;
  
  public DefaultUrlConnectionExpiryCalculator() {
    this(60000L);
  }
  
  public DefaultUrlConnectionExpiryCalculator(long paramLong) {
    if (paramLong >= 0L) {
      this.relativeExpiry = paramLong;
      return;
    } 
    throw new IllegalArgumentException("relativeExpiry < 0");
  }
  
  public long calculateExpiry(URLConnection paramURLConnection) {
    return System.currentTimeMillis() + this.relativeExpiry;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/DefaultUrlConnectionExpiryCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */