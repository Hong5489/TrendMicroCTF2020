package com.trendmicro.hippo;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

public class SecurityUtilities {
  public static ProtectionDomain getProtectionDomain(final Class<?> clazz) {
    return AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>() {
          public ProtectionDomain run() {
            return clazz.getProtectionDomain();
          }
        });
  }
  
  public static ProtectionDomain getScriptProtectionDomain() {
    final SecurityManager securityManager = System.getSecurityManager();
    return (securityManager instanceof HippoSecurityManager) ? AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>() {
          public ProtectionDomain run() {
            ProtectionDomain protectionDomain;
            Class<?> clazz = ((HippoSecurityManager)securityManager).getCurrentScriptClass();
            if (clazz == null) {
              clazz = null;
            } else {
              protectionDomain = clazz.getProtectionDomain();
            } 
            return protectionDomain;
          }
        }) : null;
  }
  
  public static String getSystemProperty(final String name) {
    return AccessController.<String>doPrivileged(new PrivilegedAction<String>() {
          public String run() {
            return System.getProperty(name);
          }
        });
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SecurityUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */