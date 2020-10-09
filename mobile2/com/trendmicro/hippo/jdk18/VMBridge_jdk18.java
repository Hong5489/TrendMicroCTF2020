package com.trendmicro.hippo.jdk18;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.InterfaceAdapter;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.VMBridge;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class VMBridge_jdk18 extends VMBridge {
  private ThreadLocal<Object[]> contextLocal = new ThreadLocal();
  
  protected Context getContext(Object paramObject) {
    return (Context)((Object[])paramObject)[0];
  }
  
  protected Object getInterfaceProxyHelper(ContextFactory paramContextFactory, Class<?>[] paramArrayOfClass) {
    Class<?> clazz = Proxy.getProxyClass(paramArrayOfClass[0].getClassLoader(), paramArrayOfClass);
    try {
      return clazz.getConstructor(new Class[] { InvocationHandler.class });
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new IllegalStateException(noSuchMethodException);
    } 
  }
  
  protected Object getThreadContextHelper() {
    Object[] arrayOfObject1 = this.contextLocal.get();
    Object[] arrayOfObject2 = arrayOfObject1;
    if (arrayOfObject1 == null) {
      arrayOfObject2 = new Object[1];
      this.contextLocal.set(arrayOfObject2);
    } 
    return arrayOfObject2;
  }
  
  protected Object newInterfaceProxy(Object paramObject1, final ContextFactory cf, final InterfaceAdapter adapter, final Object target, final Scriptable topScope) {
    paramObject1 = paramObject1;
    InvocationHandler invocationHandler = new InvocationHandler() {
        public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) {
          if (param1Method.getDeclaringClass() == Object.class) {
            String str = param1Method.getName();
            if (str.equals("equals")) {
              boolean bool = false;
              if (param1Object == param1ArrayOfObject[0])
                bool = true; 
              return Boolean.valueOf(bool);
            } 
            if (str.equals("hashCode"))
              return Integer.valueOf(target.hashCode()); 
            if (str.equals("toString")) {
              param1Object = new StringBuilder();
              param1Object.append("Proxy[");
              param1Object.append(target.toString());
              param1Object.append("]");
              return param1Object.toString();
            } 
          } 
          return adapter.invoke(cf, target, topScope, param1Object, param1Method, param1ArrayOfObject);
        }
      };
    try {
      return paramObject1.newInstance(new Object[] { invocationHandler });
    } catch (InvocationTargetException invocationTargetException) {
      throw Context.throwAsScriptRuntimeEx(invocationTargetException);
    } catch (IllegalAccessException illegalAccessException) {
      throw new IllegalStateException(illegalAccessException);
    } catch (InstantiationException instantiationException) {
      throw new IllegalStateException(instantiationException);
    } 
  }
  
  protected void setContext(Object paramObject, Context paramContext) {
    ((Object[])paramObject)[0] = paramContext;
  }
  
  protected boolean tryToMakeAccessible(AccessibleObject paramAccessibleObject) {
    if (paramAccessibleObject.isAccessible())
      return true; 
    try {
      paramAccessibleObject.setAccessible(true);
    } catch (Exception exception) {}
    return paramAccessibleObject.isAccessible();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/jdk18/VMBridge_jdk18.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */