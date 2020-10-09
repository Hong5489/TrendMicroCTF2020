package com.trendmicro.hippo;

import com.trendmicro.hippo.xml.XMLLib;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ContextFactory {
  private static ContextFactory global = new ContextFactory();
  
  private static volatile boolean hasCustomGlobal;
  
  private ClassLoader applicationClassLoader;
  
  private boolean disabledListening;
  
  private volatile Object listeners;
  
  private final Object listenersLock = new Object();
  
  private volatile boolean sealed;
  
  public static ContextFactory getGlobal() {
    return global;
  }
  
  public static GlobalSetter getGlobalSetter() {
    // Byte code:
    //   0: ldc com/trendmicro/hippo/ContextFactory
    //   2: monitorenter
    //   3: getstatic com/trendmicro/hippo/ContextFactory.hasCustomGlobal : Z
    //   6: ifne -> 26
    //   9: iconst_1
    //   10: putstatic com/trendmicro/hippo/ContextFactory.hasCustomGlobal : Z
    //   13: new com/trendmicro/hippo/ContextFactory$1GlobalSetterImpl
    //   16: dup
    //   17: invokespecial <init> : ()V
    //   20: astore_0
    //   21: ldc com/trendmicro/hippo/ContextFactory
    //   23: monitorexit
    //   24: aload_0
    //   25: areturn
    //   26: new java/lang/IllegalStateException
    //   29: astore_0
    //   30: aload_0
    //   31: invokespecial <init> : ()V
    //   34: aload_0
    //   35: athrow
    //   36: astore_0
    //   37: ldc com/trendmicro/hippo/ContextFactory
    //   39: monitorexit
    //   40: aload_0
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	36	finally
    //   26	36	36	finally
  }
  
  public static boolean hasExplicitGlobal() {
    return hasCustomGlobal;
  }
  
  public static void initGlobal(ContextFactory paramContextFactory) {
    // Byte code:
    //   0: ldc com/trendmicro/hippo/ContextFactory
    //   2: monitorenter
    //   3: aload_0
    //   4: ifnull -> 39
    //   7: getstatic com/trendmicro/hippo/ContextFactory.hasCustomGlobal : Z
    //   10: ifne -> 25
    //   13: iconst_1
    //   14: putstatic com/trendmicro/hippo/ContextFactory.hasCustomGlobal : Z
    //   17: aload_0
    //   18: putstatic com/trendmicro/hippo/ContextFactory.global : Lcom/trendmicro/hippo/ContextFactory;
    //   21: ldc com/trendmicro/hippo/ContextFactory
    //   23: monitorexit
    //   24: return
    //   25: new java/lang/IllegalStateException
    //   28: astore_0
    //   29: aload_0
    //   30: invokespecial <init> : ()V
    //   33: aload_0
    //   34: athrow
    //   35: astore_0
    //   36: goto -> 49
    //   39: new java/lang/IllegalArgumentException
    //   42: astore_0
    //   43: aload_0
    //   44: invokespecial <init> : ()V
    //   47: aload_0
    //   48: athrow
    //   49: ldc com/trendmicro/hippo/ContextFactory
    //   51: monitorexit
    //   52: aload_0
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   7	21	35	finally
    //   25	35	35	finally
    //   39	49	35	finally
  }
  
  private boolean isDom3Present() {
    Class<?> clazz = Kit.classOrNull("org.w3c.dom.Node");
    if (clazz == null)
      return false; 
    try {
      clazz.getMethod("getUserData", new Class[] { String.class });
      return true;
    } catch (NoSuchMethodException noSuchMethodException) {
      return false;
    } 
  }
  
  public final void addListener(Listener paramListener) {
    checkNotSealed();
    synchronized (this.listenersLock) {
      if (!this.disabledListening) {
        this.listeners = Kit.addListener(this.listeners, paramListener);
        return;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this();
      throw illegalStateException;
    } 
  }
  
  public final <T> T call(ContextAction<T> paramContextAction) {
    return Context.call(this, paramContextAction);
  }
  
  protected final void checkNotSealed() {
    if (!this.sealed)
      return; 
    throw new IllegalStateException();
  }
  
  protected GeneratedClassLoader createClassLoader(final ClassLoader parent) {
    return AccessController.<GeneratedClassLoader>doPrivileged((PrivilegedAction)new PrivilegedAction<DefiningClassLoader>() {
          public DefiningClassLoader run() {
            return new DefiningClassLoader(parent);
          }
        });
  }
  
  final void disableContextListening() {
    checkNotSealed();
    synchronized (this.listenersLock) {
      this.disabledListening = true;
      this.listeners = null;
      return;
    } 
  }
  
  protected Object doTopCall(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object = paramCallable.call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
    if (object instanceof ConsString)
      object = object.toString(); 
    return object;
  }
  
  @Deprecated
  public final Context enter() {
    return enterContext(null);
  }
  
  public Context enterContext() {
    return enterContext(null);
  }
  
  public final Context enterContext(Context paramContext) {
    return Context.enter(paramContext, this);
  }
  
  @Deprecated
  public final void exit() {
    Context.exit();
  }
  
  public final ClassLoader getApplicationClassLoader() {
    return this.applicationClassLoader;
  }
  
  protected XMLLib.Factory getE4xImplementationFactory() {
    return isDom3Present() ? XMLLib.Factory.create("com.trendmicro.hippo.xmlimpl.XMLLibImpl") : null;
  }
  
  protected boolean hasFeature(Context paramContext, int paramInt) {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    boolean bool4 = true;
    boolean bool5 = true;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 19:
        return false;
      case 18:
        return false;
      case 17:
        return false;
      case 16:
        if (paramContext.getLanguageVersion() < 200)
          bool5 = false; 
        return bool5;
      case 15:
        if (paramContext.getLanguageVersion() <= 170) {
          bool5 = bool1;
        } else {
          bool5 = false;
        } 
        return bool5;
      case 14:
        return true;
      case 13:
        return false;
      case 12:
        return false;
      case 11:
        return false;
      case 10:
        return false;
      case 9:
        return false;
      case 8:
        return false;
      case 7:
        return false;
      case 6:
        paramInt = paramContext.getLanguageVersion();
        bool5 = bool2;
        if (paramInt != 0)
          if (paramInt >= 160) {
            bool5 = bool2;
          } else {
            bool5 = false;
          }  
        return bool5;
      case 5:
        return true;
      case 4:
        if (paramContext.getLanguageVersion() == 120) {
          bool5 = bool3;
        } else {
          bool5 = false;
        } 
        return bool5;
      case 3:
        return true;
      case 2:
        return false;
      case 1:
        break;
    } 
    paramInt = paramContext.getLanguageVersion();
    bool5 = bool4;
    if (paramInt != 100) {
      bool5 = bool4;
      if (paramInt != 110)
        if (paramInt == 120) {
          bool5 = bool4;
        } else {
          bool5 = false;
        }  
    } 
    return bool5;
  }
  
  public final void initApplicationClassLoader(ClassLoader paramClassLoader) {
    if (paramClassLoader != null) {
      if (Kit.testIfCanLoadHippoClasses(paramClassLoader)) {
        if (this.applicationClassLoader == null) {
          checkNotSealed();
          this.applicationClassLoader = paramClassLoader;
          return;
        } 
        throw new IllegalStateException("applicationClassLoader can only be set once");
      } 
      throw new IllegalArgumentException("Loader can not resolve classes");
    } 
    throw new IllegalArgumentException("loader is null");
  }
  
  public final boolean isSealed() {
    return this.sealed;
  }
  
  protected Context makeContext() {
    return new Context(this);
  }
  
  protected void observeInstructionCount(Context paramContext, int paramInt) {}
  
  protected void onContextCreated(Context paramContext) {
    Object object = this.listeners;
    for (byte b = 0;; b++) {
      Listener listener = (Listener)Kit.getListener(object, b);
      if (listener == null)
        return; 
      listener.contextCreated(paramContext);
    } 
  }
  
  protected void onContextReleased(Context paramContext) {
    Object object = this.listeners;
    for (byte b = 0;; b++) {
      Listener listener = (Listener)Kit.getListener(object, b);
      if (listener == null)
        return; 
      listener.contextReleased(paramContext);
    } 
  }
  
  public final void removeListener(Listener paramListener) {
    checkNotSealed();
    synchronized (this.listenersLock) {
      if (!this.disabledListening) {
        this.listeners = Kit.removeListener(this.listeners, paramListener);
        return;
      } 
      IllegalStateException illegalStateException = new IllegalStateException();
      this();
      throw illegalStateException;
    } 
  }
  
  public final void seal() {
    checkNotSealed();
    this.sealed = true;
  }
  
  public static interface GlobalSetter {
    ContextFactory getContextFactoryGlobal();
    
    void setContextFactoryGlobal(ContextFactory param1ContextFactory);
  }
  
  public static interface Listener {
    void contextCreated(Context param1Context);
    
    void contextReleased(Context param1Context);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */