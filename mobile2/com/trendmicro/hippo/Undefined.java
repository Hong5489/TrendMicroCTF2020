package com.trendmicro.hippo;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Undefined implements Serializable {
  public static final Scriptable SCRIPTABLE_UNDEFINED;
  
  public static final Object instance = new Undefined();
  
  private static final long serialVersionUID = 9195680630202616767L;
  
  static {
    ClassLoader classLoader = Undefined.class.getClassLoader();
    InvocationHandler invocationHandler = new InvocationHandler() {
        public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
          if (param1Method.getName().equals("toString"))
            return "undefined"; 
          if (param1Method.getName().equals("equals")) {
            int i = param1ArrayOfObject.length;
            boolean bool1 = false;
            boolean bool2 = bool1;
            if (i > 0) {
              bool2 = bool1;
              if (Undefined.isUndefined(param1ArrayOfObject[0]))
                bool2 = true; 
            } 
            return Boolean.valueOf(bool2);
          } 
          param1Object = new StringBuilder();
          param1Object.append("undefined doesn't support ");
          param1Object.append(param1Method.getName());
          throw new UnsupportedOperationException(param1Object.toString());
        }
      };
    SCRIPTABLE_UNDEFINED = (Scriptable)Proxy.newProxyInstance(classLoader, new Class[] { Scriptable.class }, invocationHandler);
  }
  
  public static boolean isUndefined(Object paramObject) {
    return (instance == paramObject || SCRIPTABLE_UNDEFINED == paramObject);
  }
  
  public boolean equals(Object paramObject) {
    return (isUndefined(paramObject) || super.equals(paramObject));
  }
  
  public int hashCode() {
    return 0;
  }
  
  public Object readResolve() {
    return instance;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Undefined.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */