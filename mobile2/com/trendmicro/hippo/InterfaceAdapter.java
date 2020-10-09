package com.trendmicro.hippo;

import java.lang.reflect.Method;

public class InterfaceAdapter {
  private final Object proxyHelper;
  
  private InterfaceAdapter(ContextFactory paramContextFactory, Class<?> paramClass) {
    this.proxyHelper = VMBridge.instance.getInterfaceProxyHelper(paramContextFactory, new Class[] { paramClass });
  }
  
  static Object create(Context paramContext, Class<?> paramClass, ScriptableObject paramScriptableObject) {
    if (paramClass.isInterface()) {
      Method[] arrayOfMethod1;
      Method[] arrayOfMethod2;
      Scriptable scriptable = ScriptRuntime.getTopCallScope(paramContext);
      ClassCache classCache = ClassCache.get(scriptable);
      InterfaceAdapter interfaceAdapter = (InterfaceAdapter)classCache.getInterfaceAdapter(paramClass);
      ContextFactory contextFactory = paramContext.getFactory();
      if (interfaceAdapter == null) {
        arrayOfMethod2 = paramClass.getMethods();
        if (paramScriptableObject instanceof Callable) {
          int i = arrayOfMethod2.length;
          if (i != 0) {
            if (i > 1) {
              String str = arrayOfMethod2[0].getName();
              byte b = 1;
              while (b < i) {
                if (str.equals(arrayOfMethod2[b].getName())) {
                  b++;
                  continue;
                } 
                throw Context.reportRuntimeError1("msg.no.function.interface.conversion", paramClass.getName());
              } 
            } 
          } else {
            throw Context.reportRuntimeError1("msg.no.empty.interface.conversion", paramClass.getName());
          } 
        } 
        InterfaceAdapter interfaceAdapter1 = new InterfaceAdapter(contextFactory, paramClass);
        classCache.cacheInterfaceAdapter(paramClass, interfaceAdapter1);
      } else {
        arrayOfMethod1 = arrayOfMethod2;
      } 
      return VMBridge.instance.newInterfaceProxy(((InterfaceAdapter)arrayOfMethod1).proxyHelper, contextFactory, (InterfaceAdapter)arrayOfMethod1, paramScriptableObject, scriptable);
    } 
    throw new IllegalArgumentException();
  }
  
  public Object invoke(ContextFactory paramContextFactory, Object paramObject1, Scriptable paramScriptable, Object paramObject2, Method paramMethod, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: aload_2
    //   3: aload_3
    //   4: aload #4
    //   6: aload #5
    //   8: aload #6
    //   10: <illegal opcode> run : (Lcom/trendmicro/hippo/InterfaceAdapter;Ljava/lang/Object;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Lcom/trendmicro/hippo/ContextAction;
    //   15: invokevirtual call : (Lcom/trendmicro/hippo/ContextAction;)Ljava/lang/Object;
    //   18: areturn
  }
  
  Object invokeImpl(Context paramContext, Object<?> paramObject1, Scriptable paramScriptable, Object paramObject2, Method paramMethod, Object[] paramArrayOfObject) {
    Class<?> clazz;
    Object object2;
    if (paramObject1 instanceof Callable) {
      paramObject1 = paramObject1;
    } else {
      Scriptable scriptable = (Scriptable)paramObject1;
      paramObject1 = (Object<?>)paramMethod.getName();
      object2 = ScriptableObject.getProperty(scriptable, (String)paramObject1);
      if (object2 == ScriptableObject.NOT_FOUND) {
        Context.reportWarning(ScriptRuntime.getMessage1("msg.undefined.function.interface", paramObject1));
        clazz = paramMethod.getReturnType();
        return (clazz == void.class) ? null : Context.jsToJava(null, clazz);
      } 
      if (object2 instanceof Callable) {
        paramObject1 = (Object<?>)object2;
      } else {
        throw Context.reportRuntimeError1("msg.not.function.interface", paramObject1);
      } 
    } 
    WrapFactory wrapFactory = clazz.getWrapFactory();
    if (paramArrayOfObject == null) {
      object2 = ScriptRuntime.emptyArgs;
    } else {
      int i = 0;
      int j = paramArrayOfObject.length;
      while (true) {
        object2 = paramArrayOfObject;
        if (i != j) {
          object2 = paramArrayOfObject[i];
          if (!(object2 instanceof String) && !(object2 instanceof Number) && !(object2 instanceof Boolean))
            paramArrayOfObject[i] = wrapFactory.wrap((Context)clazz, paramScriptable, object2, null); 
          i++;
          continue;
        } 
        break;
      } 
    } 
    Object object1 = paramObject1.call((Context)clazz, paramScriptable, wrapFactory.wrapAsJavaObject((Context)clazz, paramScriptable, paramObject2, null), (Object[])object2);
    paramObject1 = (Object<?>)paramMethod.getReturnType();
    if (paramObject1 == void.class) {
      object1 = null;
    } else {
      object1 = Context.jsToJava(object1, (Class<?>)paramObject1);
    } 
    return object1;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/InterfaceAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */