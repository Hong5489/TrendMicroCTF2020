package com.trendmicro.hippo;

public abstract class SecurityController {
  private static SecurityController global;
  
  public static GeneratedClassLoader createLoader(ClassLoader paramClassLoader, Object paramObject) {
    GeneratedClassLoader generatedClassLoader;
    Context context = Context.getContext();
    ClassLoader classLoader = paramClassLoader;
    if (paramClassLoader == null)
      classLoader = context.getApplicationClassLoader(); 
    SecurityController securityController = context.getSecurityController();
    if (securityController == null) {
      generatedClassLoader = context.createClassLoader(classLoader);
    } else {
      generatedClassLoader = generatedClassLoader.createClassLoader(classLoader, generatedClassLoader.getDynamicSecurityDomain(paramObject));
    } 
    return generatedClassLoader;
  }
  
  public static Class<?> getStaticSecurityDomainClass() {
    Class<?> clazz;
    SecurityController securityController = Context.getContext().getSecurityController();
    if (securityController == null) {
      securityController = null;
    } else {
      clazz = securityController.getStaticSecurityDomainClassInternal();
    } 
    return clazz;
  }
  
  static SecurityController global() {
    return global;
  }
  
  public static boolean hasGlobal() {
    boolean bool;
    if (global != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static void initGlobal(SecurityController paramSecurityController) {
    if (paramSecurityController != null) {
      if (global == null) {
        global = paramSecurityController;
        return;
      } 
      throw new SecurityException("Cannot overwrite already installed global SecurityController");
    } 
    throw new IllegalArgumentException();
  }
  
  public Object callWithDomain(Object paramObject, Context paramContext, final Callable callable, Scriptable paramScriptable1, final Scriptable thisObj, final Object[] args) {
    return execWithDomain(paramContext, paramScriptable1, new Script() {
          public Object exec(Context param1Context, Scriptable param1Scriptable) {
            return callable.call(param1Context, param1Scriptable, thisObj, args);
          }
        }paramObject);
  }
  
  public abstract GeneratedClassLoader createClassLoader(ClassLoader paramClassLoader, Object paramObject);
  
  @Deprecated
  public Object execWithDomain(Context paramContext, Scriptable paramScriptable, Script paramScript, Object paramObject) {
    throw new IllegalStateException("callWithDomain should be overridden");
  }
  
  public abstract Object getDynamicSecurityDomain(Object paramObject);
  
  public Class<?> getStaticSecurityDomainClassInternal() {
    return null;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SecurityController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */