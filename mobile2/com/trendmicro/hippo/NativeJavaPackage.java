package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;

public class NativeJavaPackage extends ScriptableObject {
  private static final long serialVersionUID = 7445054382212031523L;
  
  private transient ClassLoader classLoader;
  
  private Set<String> negativeCache = null;
  
  private String packageName;
  
  @Deprecated
  public NativeJavaPackage(String paramString) {
    this(false, paramString, Context.getCurrentContext().getApplicationClassLoader());
  }
  
  @Deprecated
  public NativeJavaPackage(String paramString, ClassLoader paramClassLoader) {
    this(false, paramString, paramClassLoader);
  }
  
  NativeJavaPackage(boolean paramBoolean, String paramString, ClassLoader paramClassLoader) {
    this.packageName = paramString;
    this.classLoader = paramClassLoader;
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    this.classLoader = Context.getCurrentContext().getApplicationClassLoader();
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof NativeJavaPackage;
    boolean bool1 = false;
    if (bool) {
      paramObject = paramObject;
      bool = bool1;
      if (this.packageName.equals(((NativeJavaPackage)paramObject).packageName)) {
        bool = bool1;
        if (this.classLoader == ((NativeJavaPackage)paramObject).classLoader)
          bool = true; 
      } 
      return bool;
    } 
    return false;
  }
  
  NativeJavaPackage forcePackage(String paramString, Scriptable paramScriptable) {
    Object object = super.get(paramString, this);
    if (object != null && object instanceof NativeJavaPackage)
      return (NativeJavaPackage)object; 
    if (this.packageName.length() == 0) {
      object = paramString;
    } else {
      object = new StringBuilder();
      object.append(this.packageName);
      object.append(".");
      object.append(paramString);
      object = object.toString();
    } 
    object = new NativeJavaPackage(true, (String)object, this.classLoader);
    ScriptRuntime.setObjectProtoAndParent((ScriptableObject)object, paramScriptable);
    super.put(paramString, this, object);
    return (NativeJavaPackage)object;
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    return NOT_FOUND;
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    return getPkgProperty(paramString, paramScriptable, true);
  }
  
  public String getClassName() {
    return "JavaPackage";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return toString();
  }
  
  Object getPkgProperty(String paramString, Scriptable paramScriptable, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: aload_2
    //   5: invokespecial get : (Ljava/lang/String;Lcom/trendmicro/hippo/Scriptable;)Ljava/lang/Object;
    //   8: astore #4
    //   10: getstatic com/trendmicro/hippo/NativeJavaPackage.NOT_FOUND : Ljava/lang/Object;
    //   13: astore #5
    //   15: aload #4
    //   17: aload #5
    //   19: if_acmpeq -> 27
    //   22: aload_0
    //   23: monitorexit
    //   24: aload #4
    //   26: areturn
    //   27: aload_0
    //   28: getfield negativeCache : Ljava/util/Set;
    //   31: ifnull -> 55
    //   34: aload_0
    //   35: getfield negativeCache : Ljava/util/Set;
    //   38: aload_1
    //   39: invokeinterface contains : (Ljava/lang/Object;)Z
    //   44: istore #6
    //   46: iload #6
    //   48: ifeq -> 55
    //   51: aload_0
    //   52: monitorexit
    //   53: aconst_null
    //   54: areturn
    //   55: aload_0
    //   56: getfield packageName : Ljava/lang/String;
    //   59: invokevirtual length : ()I
    //   62: ifne -> 71
    //   65: aload_1
    //   66: astore #5
    //   68: goto -> 113
    //   71: new java/lang/StringBuilder
    //   74: astore #4
    //   76: aload #4
    //   78: invokespecial <init> : ()V
    //   81: aload #4
    //   83: aload_0
    //   84: getfield packageName : Ljava/lang/String;
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: aload #4
    //   93: bipush #46
    //   95: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: aload #4
    //   101: aload_1
    //   102: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: pop
    //   106: aload #4
    //   108: invokevirtual toString : ()Ljava/lang/String;
    //   111: astore #5
    //   113: invokestatic getContext : ()Lcom/trendmicro/hippo/Context;
    //   116: astore #7
    //   118: aload #7
    //   120: invokevirtual getClassShutter : ()Lcom/trendmicro/hippo/ClassShutter;
    //   123: astore #8
    //   125: aconst_null
    //   126: astore #9
    //   128: aload #8
    //   130: ifnull -> 149
    //   133: aload #9
    //   135: astore #4
    //   137: aload #8
    //   139: aload #5
    //   141: invokeinterface visibleToScripts : (Ljava/lang/String;)Z
    //   146: ifeq -> 215
    //   149: aload_0
    //   150: getfield classLoader : Ljava/lang/ClassLoader;
    //   153: ifnull -> 170
    //   156: aload_0
    //   157: getfield classLoader : Ljava/lang/ClassLoader;
    //   160: aload #5
    //   162: invokestatic classOrNull : (Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/lang/Class;
    //   165: astore #8
    //   167: goto -> 177
    //   170: aload #5
    //   172: invokestatic classOrNull : (Ljava/lang/String;)Ljava/lang/Class;
    //   175: astore #8
    //   177: aload #9
    //   179: astore #4
    //   181: aload #8
    //   183: ifnull -> 215
    //   186: aload #7
    //   188: invokevirtual getWrapFactory : ()Lcom/trendmicro/hippo/WrapFactory;
    //   191: aload #7
    //   193: aload_0
    //   194: invokestatic getTopLevelScope : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   197: aload #8
    //   199: invokevirtual wrapJavaClass : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Class;)Lcom/trendmicro/hippo/Scriptable;
    //   202: astore #4
    //   204: aload #4
    //   206: aload_0
    //   207: invokevirtual getPrototype : ()Lcom/trendmicro/hippo/Scriptable;
    //   210: invokeinterface setPrototype : (Lcom/trendmicro/hippo/Scriptable;)V
    //   215: aload #4
    //   217: astore #8
    //   219: aload #4
    //   221: ifnonnull -> 295
    //   224: iload_3
    //   225: ifeq -> 257
    //   228: new com/trendmicro/hippo/NativeJavaPackage
    //   231: astore #8
    //   233: aload #8
    //   235: iconst_1
    //   236: aload #5
    //   238: aload_0
    //   239: getfield classLoader : Ljava/lang/ClassLoader;
    //   242: invokespecial <init> : (ZLjava/lang/String;Ljava/lang/ClassLoader;)V
    //   245: aload #8
    //   247: aload_0
    //   248: invokevirtual getParentScope : ()Lcom/trendmicro/hippo/Scriptable;
    //   251: invokestatic setObjectProtoAndParent : (Lcom/trendmicro/hippo/ScriptableObject;Lcom/trendmicro/hippo/Scriptable;)V
    //   254: goto -> 295
    //   257: aload_0
    //   258: getfield negativeCache : Ljava/util/Set;
    //   261: ifnonnull -> 280
    //   264: new java/util/HashSet
    //   267: astore #5
    //   269: aload #5
    //   271: invokespecial <init> : ()V
    //   274: aload_0
    //   275: aload #5
    //   277: putfield negativeCache : Ljava/util/Set;
    //   280: aload_0
    //   281: getfield negativeCache : Ljava/util/Set;
    //   284: aload_1
    //   285: invokeinterface add : (Ljava/lang/Object;)Z
    //   290: pop
    //   291: aload #4
    //   293: astore #8
    //   295: aload #8
    //   297: ifnull -> 308
    //   300: aload_0
    //   301: aload_1
    //   302: aload_2
    //   303: aload #8
    //   305: invokespecial put : (Ljava/lang/String;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
    //   308: aload_0
    //   309: monitorexit
    //   310: aload #8
    //   312: areturn
    //   313: astore_1
    //   314: aload_0
    //   315: monitorexit
    //   316: aload_1
    //   317: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	313	finally
    //   27	46	313	finally
    //   55	65	313	finally
    //   71	113	313	finally
    //   113	125	313	finally
    //   137	149	313	finally
    //   149	167	313	finally
    //   170	177	313	finally
    //   186	215	313	finally
    //   228	254	313	finally
    //   257	280	313	finally
    //   280	291	313	finally
    //   300	308	313	finally
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    return false;
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    return true;
  }
  
  public int hashCode() {
    int j;
    int i = this.packageName.hashCode();
    ClassLoader classLoader = this.classLoader;
    if (classLoader == null) {
      j = 0;
    } else {
      j = classLoader.hashCode();
    } 
    return i ^ j;
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    throw Context.reportRuntimeError0("msg.pkg.int");
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {}
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[JavaPackage ");
    stringBuilder.append(this.packageName);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaPackage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */