package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Callable;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.GeneratedClassLoader;
import com.trendmicro.hippo.Scriptable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class JavaPolicySecurity extends SecurityProxy {
  public JavaPolicySecurity() {
    new CodeSource(null, (Certificate[])null);
  }
  
  private ProtectionDomain getDynamicDomain(ProtectionDomain paramProtectionDomain) {
    return new ProtectionDomain(null, new ContextPermissions(paramProtectionDomain));
  }
  
  private ProtectionDomain getUrlDomain(URL paramURL) {
    CodeSource codeSource = new CodeSource(paramURL, (Certificate[])null);
    return new ProtectionDomain(codeSource, Policy.getPolicy().getPermissions(codeSource));
  }
  
  private URL getUrlObj(String paramString) {
    URL uRL;
    try {
      URL uRL1 = new URL();
      this(paramString);
      uRL = uRL1;
    } catch (MalformedURLException malformedURLException) {
      String str2 = System.getProperty("user.dir").replace('\\', '/');
      String str1 = str2;
      if (!str2.endsWith("/")) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append('/');
        str1 = stringBuilder.toString();
      } 
      try {
        URL uRL1 = new URL();
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("file:");
        stringBuilder.append(str1);
        this(stringBuilder.toString());
        return new URL(uRL1, (String)uRL);
      } catch (MalformedURLException malformedURLException1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can not construct file URL for '");
        stringBuilder.append((String)uRL);
        stringBuilder.append("':");
        stringBuilder.append(malformedURLException1.getMessage());
        throw new RuntimeException(stringBuilder.toString());
      } 
    } 
    return uRL;
  }
  
  protected void callProcessFileSecure(final Context cx, final Scriptable scope, final String filename) {
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            URL uRL = JavaPolicySecurity.this.getUrlObj(filename);
            ProtectionDomain protectionDomain = JavaPolicySecurity.this.getUrlDomain(uRL);
            try {
              Main.processFileSecure(cx, scope, uRL.toExternalForm(), protectionDomain);
              return null;
            } catch (IOException iOException) {
              throw new RuntimeException(iOException);
            } 
          }
        });
  }
  
  public Object callWithDomain(Object paramObject, final Context cx, final Callable callable, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
    paramObject = new AccessControlContext(new ProtectionDomain[] { getDynamicDomain((ProtectionDomain)paramObject) });
    return AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            return callable.call(cx, scope, thisObj, args);
          }
        }(AccessControlContext)paramObject);
  }
  
  public GeneratedClassLoader createClassLoader(final ClassLoader parentLoader, final Object domain) {
    return AccessController.<GeneratedClassLoader>doPrivileged((PrivilegedAction)new PrivilegedAction<Loader>() {
          public JavaPolicySecurity.Loader run() {
            return new JavaPolicySecurity.Loader(parentLoader, domain);
          }
        });
  }
  
  public Object getDynamicSecurityDomain(Object paramObject) {
    return getDynamicDomain((ProtectionDomain)paramObject);
  }
  
  public Class<?> getStaticSecurityDomainClassInternal() {
    return ProtectionDomain.class;
  }
  
  private static class ContextPermissions extends PermissionCollection {
    static final long serialVersionUID = -1721494496320750721L;
    
    AccessControlContext _context = AccessController.getContext();
    
    PermissionCollection _statisPermissions;
    
    ContextPermissions(ProtectionDomain param1ProtectionDomain) {
      if (param1ProtectionDomain != null)
        this._statisPermissions = param1ProtectionDomain.getPermissions(); 
      setReadOnly();
    }
    
    public void add(Permission param1Permission) {
      throw new RuntimeException("NOT IMPLEMENTED");
    }
    
    public Enumeration<Permission> elements() {
      return new Enumeration<Permission>() {
          public boolean hasMoreElements() {
            return false;
          }
          
          public Permission nextElement() {
            return null;
          }
        };
    }
    
    public boolean implies(Permission param1Permission) {
      PermissionCollection permissionCollection = this._statisPermissions;
      if (permissionCollection != null && !permissionCollection.implies(param1Permission))
        return false; 
      try {
        this._context.checkPermission(param1Permission);
        return true;
      } catch (AccessControlException accessControlException) {
        return false;
      } 
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getClass().getName());
      stringBuilder.append('@');
      stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      stringBuilder.append(" (context=");
      stringBuilder.append(this._context);
      stringBuilder.append(", static_permitions=");
      stringBuilder.append(this._statisPermissions);
      stringBuilder.append(')');
      return stringBuilder.toString();
    }
  }
  
  class null implements Enumeration<Permission> {
    public boolean hasMoreElements() {
      return false;
    }
    
    public Permission nextElement() {
      return null;
    }
  }
  
  private static class Loader extends ClassLoader implements GeneratedClassLoader {
    private ProtectionDomain domain;
    
    Loader(ClassLoader param1ClassLoader, ProtectionDomain param1ProtectionDomain) {
      super(param1ClassLoader);
      this.domain = param1ProtectionDomain;
    }
    
    public Class<?> defineClass(String param1String, byte[] param1ArrayOfbyte) {
      return defineClass(param1String, param1ArrayOfbyte, 0, param1ArrayOfbyte.length, this.domain);
    }
    
    public void linkClass(Class<?> param1Class) {
      resolveClass(param1Class);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/JavaPolicySecurity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */