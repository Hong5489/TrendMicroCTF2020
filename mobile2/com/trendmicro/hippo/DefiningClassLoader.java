package com.trendmicro.hippo;

public class DefiningClassLoader extends ClassLoader implements GeneratedClassLoader {
  private final ClassLoader parentLoader = getClass().getClassLoader();
  
  public DefiningClassLoader() {}
  
  public DefiningClassLoader(ClassLoader paramClassLoader) {}
  
  public Class<?> defineClass(String paramString, byte[] paramArrayOfbyte) {
    return defineClass(paramString, paramArrayOfbyte, 0, paramArrayOfbyte.length, SecurityUtilities.getProtectionDomain(getClass()));
  }
  
  public void linkClass(Class<?> paramClass) {
    resolveClass(paramClass);
  }
  
  public Class<?> loadClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
    Class<?> clazz1 = findLoadedClass(paramString);
    Class<?> clazz2 = clazz1;
    if (clazz1 == null) {
      ClassLoader classLoader = this.parentLoader;
      if (classLoader != null) {
        Class<?> clazz = classLoader.loadClass(paramString);
      } else {
        clazz2 = findSystemClass(paramString);
      } 
    } 
    if (paramBoolean)
      resolveClass(clazz2); 
    return clazz2;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/DefiningClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */