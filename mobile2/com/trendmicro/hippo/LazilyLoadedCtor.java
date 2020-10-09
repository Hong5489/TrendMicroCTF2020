package com.trendmicro.hippo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class LazilyLoadedCtor implements Serializable {
  private static final int STATE_BEFORE_INIT = 0;
  
  private static final int STATE_INITIALIZING = 1;
  
  private static final int STATE_WITH_VALUE = 2;
  
  private static final long serialVersionUID = 1L;
  
  private final String className;
  
  private Object initializedValue;
  
  private final boolean privileged;
  
  private final String propertyName;
  
  private final ScriptableObject scope;
  
  private final boolean sealed;
  
  private int state;
  
  public LazilyLoadedCtor(ScriptableObject paramScriptableObject, String paramString1, String paramString2, boolean paramBoolean) {
    this(paramScriptableObject, paramString1, paramString2, paramBoolean, false);
  }
  
  LazilyLoadedCtor(ScriptableObject paramScriptableObject, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2) {
    this.scope = paramScriptableObject;
    this.propertyName = paramString1;
    this.className = paramString2;
    this.sealed = paramBoolean1;
    this.privileged = paramBoolean2;
    this.state = 0;
    paramScriptableObject.addLazilyInitializedValue(paramString1, 0, this, 2);
  }
  
  private Object buildValue() {
    return this.privileged ? AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            return LazilyLoadedCtor.this.buildValue0();
          }
        }) : buildValue0();
  }
  
  private Object buildValue0() {
    Class<? extends Scriptable> clazz = cast(Kit.classOrNull(this.className));
    if (clazz != null)
      try {
        BaseFunction baseFunction = ScriptableObject.buildClassCtor(this.scope, clazz, this.sealed, false);
        if (baseFunction != null)
          return baseFunction; 
        Object object2 = this.scope.get(this.propertyName, this.scope);
        Object object1 = Scriptable.NOT_FOUND;
        if (object2 != object1)
          return object2; 
      } catch (InvocationTargetException invocationTargetException) {
        Throwable throwable = invocationTargetException.getTargetException();
        if (throwable instanceof RuntimeException)
          throw (RuntimeException)throwable; 
      } catch (HippoException hippoException) {
      
      } catch (InstantiationException instantiationException) {
      
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (SecurityException securityException) {} 
    return Scriptable.NOT_FOUND;
  }
  
  private Class<? extends Scriptable> cast(Class<?> paramClass) {
    return (Class)paramClass;
  }
  
  Object getValue() {
    if (this.state == 2)
      return this.initializedValue; 
    throw new IllegalStateException(this.propertyName);
  }
  
  void init() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield state : I
    //   6: iconst_1
    //   7: if_icmpeq -> 60
    //   10: aload_0
    //   11: getfield state : I
    //   14: ifne -> 57
    //   17: aload_0
    //   18: iconst_1
    //   19: putfield state : I
    //   22: getstatic com/trendmicro/hippo/Scriptable.NOT_FOUND : Ljava/lang/Object;
    //   25: astore_1
    //   26: aload_0
    //   27: invokespecial buildValue : ()Ljava/lang/Object;
    //   30: astore_2
    //   31: aload_0
    //   32: aload_2
    //   33: putfield initializedValue : Ljava/lang/Object;
    //   36: aload_0
    //   37: iconst_2
    //   38: putfield state : I
    //   41: goto -> 57
    //   44: astore_2
    //   45: aload_0
    //   46: aload_1
    //   47: putfield initializedValue : Ljava/lang/Object;
    //   50: aload_0
    //   51: iconst_2
    //   52: putfield state : I
    //   55: aload_2
    //   56: athrow
    //   57: aload_0
    //   58: monitorexit
    //   59: return
    //   60: new java/lang/IllegalStateException
    //   63: astore_1
    //   64: new java/lang/StringBuilder
    //   67: astore_2
    //   68: aload_2
    //   69: invokespecial <init> : ()V
    //   72: aload_2
    //   73: ldc 'Recursive initialization for '
    //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: pop
    //   79: aload_2
    //   80: aload_0
    //   81: getfield propertyName : Ljava/lang/String;
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: aload_1
    //   89: aload_2
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokespecial <init> : (Ljava/lang/String;)V
    //   96: aload_1
    //   97: athrow
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	98	finally
    //   26	31	44	finally
    //   31	41	98	finally
    //   45	57	98	finally
    //   57	59	98	finally
    //   60	98	98	finally
    //   99	101	98	finally
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/LazilyLoadedCtor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */