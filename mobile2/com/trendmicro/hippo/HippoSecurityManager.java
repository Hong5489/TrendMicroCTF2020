package com.trendmicro.hippo;

public class HippoSecurityManager extends SecurityManager {
  protected Class<?> getCurrentScriptClass() {
    for (Class<InterpretedFunction> clazz : getClassContext()) {
      if ((clazz != InterpretedFunction.class && NativeFunction.class.isAssignableFrom(clazz)) || PolicySecurityController.SecureCaller.class.isAssignableFrom(clazz))
        return clazz; 
    } 
    return null;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/HippoSecurityManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */