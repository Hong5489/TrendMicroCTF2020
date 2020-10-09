package com.trendmicro.hippo;

import java.lang.reflect.AccessibleObject;

public abstract class VMBridge {
  static final VMBridge instance = makeInstance();
  
  private static VMBridge makeInstance() {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "com.trendmicro.hippo.VMBridge_custom";
    arrayOfString[1] = "com.trendmicro.hippo.jdk18.VMBridge_jdk18";
    for (byte b = 0; b != arrayOfString.length; b++) {
      Class<?> clazz = Kit.classOrNull(arrayOfString[b]);
      if (clazz != null) {
        VMBridge vMBridge = (VMBridge)Kit.newInstanceOrNull(clazz);
        if (vMBridge != null)
          return vMBridge; 
      } 
    } 
    throw new IllegalStateException("Failed to create VMBridge instance");
  }
  
  protected abstract Context getContext(Object paramObject);
  
  protected abstract Object getInterfaceProxyHelper(ContextFactory paramContextFactory, Class<?>[] paramArrayOfClass);
  
  protected abstract Object getThreadContextHelper();
  
  protected abstract Object newInterfaceProxy(Object paramObject1, ContextFactory paramContextFactory, InterfaceAdapter paramInterfaceAdapter, Object paramObject2, Scriptable paramScriptable);
  
  protected abstract void setContext(Object paramObject, Context paramContext);
  
  protected abstract boolean tryToMakeAccessible(AccessibleObject paramAccessibleObject);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/VMBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */