package com.trendmicro.hippo;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassCache implements Serializable {
  private static final Object AKEY = "ClassCache";
  
  private static final long serialVersionUID = -8866246036237312215L;
  
  private Scriptable associatedScope;
  
  private volatile boolean cachingIsEnabled = true;
  
  private transient Map<JavaAdapter.JavaAdapterSignature, Class<?>> classAdapterCache;
  
  private transient Map<Class<?>, JavaMembers> classTable;
  
  private int generatedClassSerial;
  
  private transient Map<Class<?>, Object> interfaceAdapterCache;
  
  public static ClassCache get(Scriptable paramScriptable) {
    ClassCache classCache = (ClassCache)ScriptableObject.getTopScopeValue(paramScriptable, AKEY);
    if (classCache != null)
      return classCache; 
    throw new RuntimeException("Can't find top level scope for ClassCache.get");
  }
  
  public boolean associate(ScriptableObject paramScriptableObject) {
    if (paramScriptableObject.getParentScope() == null) {
      if (this == paramScriptableObject.associateValue(AKEY, this)) {
        this.associatedScope = paramScriptableObject;
        return true;
      } 
      return false;
    } 
    throw new IllegalArgumentException();
  }
  
  void cacheInterfaceAdapter(Class<?> paramClass, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cachingIsEnabled : Z
    //   6: ifeq -> 46
    //   9: aload_0
    //   10: getfield interfaceAdapterCache : Ljava/util/Map;
    //   13: ifnonnull -> 34
    //   16: new java/util/concurrent/ConcurrentHashMap
    //   19: astore_3
    //   20: aload_3
    //   21: bipush #16
    //   23: ldc 0.75
    //   25: iconst_1
    //   26: invokespecial <init> : (IFI)V
    //   29: aload_0
    //   30: aload_3
    //   31: putfield interfaceAdapterCache : Ljava/util/Map;
    //   34: aload_0
    //   35: getfield interfaceAdapterCache : Ljava/util/Map;
    //   38: aload_1
    //   39: aload_2
    //   40: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   45: pop
    //   46: aload_0
    //   47: monitorexit
    //   48: return
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	49	finally
    //   34	46	49	finally
  }
  
  public void clearCaches() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield classTable : Ljava/util/Map;
    //   7: aload_0
    //   8: aconst_null
    //   9: putfield classAdapterCache : Ljava/util/Map;
    //   12: aload_0
    //   13: aconst_null
    //   14: putfield interfaceAdapterCache : Ljava/util/Map;
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  Scriptable getAssociatedScope() {
    return this.associatedScope;
  }
  
  Map<Class<?>, JavaMembers> getClassCacheMap() {
    if (this.classTable == null)
      this.classTable = new ConcurrentHashMap<>(16, 0.75F, 1); 
    return this.classTable;
  }
  
  Object getInterfaceAdapter(Class<?> paramClass) {
    Map<Class<?>, Object> map = this.interfaceAdapterCache;
    if (map == null) {
      paramClass = null;
    } else {
      paramClass = (Class<?>)map.get(paramClass);
    } 
    return paramClass;
  }
  
  Map<JavaAdapter.JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
    if (this.classAdapterCache == null)
      this.classAdapterCache = new ConcurrentHashMap<>(16, 0.75F, 1); 
    return this.classAdapterCache;
  }
  
  public final boolean isCachingEnabled() {
    return this.cachingIsEnabled;
  }
  
  @Deprecated
  public boolean isInvokerOptimizationEnabled() {
    return false;
  }
  
  public final int newClassSerialNumber() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield generatedClassSerial : I
    //   6: iconst_1
    //   7: iadd
    //   8: istore_1
    //   9: aload_0
    //   10: iload_1
    //   11: putfield generatedClassSerial : I
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_1
    //   17: ireturn
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  public void setCachingEnabled(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cachingIsEnabled : Z
    //   6: istore_2
    //   7: iload_1
    //   8: iload_2
    //   9: if_icmpne -> 15
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: iload_1
    //   16: ifne -> 23
    //   19: aload_0
    //   20: invokevirtual clearCaches : ()V
    //   23: aload_0
    //   24: iload_1
    //   25: putfield cachingIsEnabled : Z
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: astore_3
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_3
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	31	finally
    //   19	23	31	finally
    //   23	28	31	finally
  }
  
  @Deprecated
  public void setInvokerOptimizationEnabled(boolean paramBoolean) {
    /* monitor enter ThisExpression{ObjectType{com/trendmicro/hippo/ClassCache}} */
    /* monitor exit ThisExpression{ObjectType{com/trendmicro/hippo/ClassCache}} */
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ClassCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */