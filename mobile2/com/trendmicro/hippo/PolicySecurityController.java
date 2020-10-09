package com.trendmicro.hippo;

import com.trendmicro.classfile.ClassFileWriter;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Map;
import java.util.WeakHashMap;

public class PolicySecurityController extends SecurityController {
  private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers;
  
  private static final byte[] secureCallerImplBytecode = loadBytecode();
  
  static {
    callers = new WeakHashMap<>();
  }
  
  private static byte[] loadBytecode() {
    String str = SecureCaller.class.getName();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(str);
    stringBuilder2.append("Impl");
    ClassFileWriter classFileWriter = new ClassFileWriter(stringBuilder2.toString(), str, "<generated>");
    classFileWriter.startMethod("<init>", "()V", (short)1);
    classFileWriter.addALoad(0);
    classFileWriter.addInvoke(183, str, "<init>", "()V");
    classFileWriter.add(177);
    classFileWriter.stopMethod((short)1);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("(Lcom/trendmicro/hippo/Callable;");
    stringBuilder1.append("Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    classFileWriter.startMethod("call", stringBuilder1.toString(), (short)17);
    for (byte b = 1; b < 6; b++)
      classFileWriter.addALoad(b); 
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append("(");
    stringBuilder1.append("Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    classFileWriter.addInvoke(185, "com/trendmicro/hippo/Callable", "call", stringBuilder1.toString());
    classFileWriter.add(176);
    classFileWriter.stopMethod((short)6);
    return classFileWriter.toByteArray();
  }
  
  public Object callWithDomain(Object<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> paramObject, Context paramContext, Callable paramCallable, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: new com/trendmicro/hippo/PolicySecurityController$2
    //   3: dup
    //   4: aload_0
    //   5: aload_2
    //   6: invokespecial <init> : (Lcom/trendmicro/hippo/PolicySecurityController;Lcom/trendmicro/hippo/Context;)V
    //   9: invokestatic doPrivileged : (Ljava/security/PrivilegedAction;)Ljava/lang/Object;
    //   12: checkcast java/lang/ClassLoader
    //   15: astore #7
    //   17: aload_1
    //   18: checkcast java/security/CodeSource
    //   21: astore #8
    //   23: getstatic com/trendmicro/hippo/PolicySecurityController.callers : Ljava/util/Map;
    //   26: astore_1
    //   27: aload_1
    //   28: monitorenter
    //   29: getstatic com/trendmicro/hippo/PolicySecurityController.callers : Ljava/util/Map;
    //   32: aload #8
    //   34: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   39: checkcast java/util/Map
    //   42: astore #9
    //   44: aload #9
    //   46: ifnonnull -> 75
    //   49: new java/util/WeakHashMap
    //   52: astore #9
    //   54: aload #9
    //   56: invokespecial <init> : ()V
    //   59: getstatic com/trendmicro/hippo/PolicySecurityController.callers : Ljava/util/Map;
    //   62: aload #8
    //   64: aload #9
    //   66: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   71: pop
    //   72: goto -> 75
    //   75: aload_1
    //   76: monitorexit
    //   77: aload #9
    //   79: monitorenter
    //   80: aload #9
    //   82: aload #7
    //   84: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   89: checkcast java/lang/ref/SoftReference
    //   92: astore_1
    //   93: aload_1
    //   94: ifnull -> 108
    //   97: aload_1
    //   98: invokevirtual get : ()Ljava/lang/Object;
    //   101: checkcast com/trendmicro/hippo/PolicySecurityController$SecureCaller
    //   104: astore_1
    //   105: goto -> 110
    //   108: aconst_null
    //   109: astore_1
    //   110: aload_1
    //   111: ifnonnull -> 176
    //   114: new com/trendmicro/hippo/PolicySecurityController$3
    //   117: astore_1
    //   118: aload_1
    //   119: aload_0
    //   120: aload #7
    //   122: aload #8
    //   124: invokespecial <init> : (Lcom/trendmicro/hippo/PolicySecurityController;Ljava/lang/ClassLoader;Ljava/security/CodeSource;)V
    //   127: aload_1
    //   128: invokestatic doPrivileged : (Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;
    //   131: checkcast com/trendmicro/hippo/PolicySecurityController$SecureCaller
    //   134: astore_1
    //   135: new java/lang/ref/SoftReference
    //   138: astore #8
    //   140: aload #8
    //   142: aload_1
    //   143: invokespecial <init> : (Ljava/lang/Object;)V
    //   146: aload #9
    //   148: aload #7
    //   150: aload #8
    //   152: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   157: pop
    //   158: goto -> 176
    //   161: astore_1
    //   162: new java/lang/reflect/UndeclaredThrowableException
    //   165: astore_2
    //   166: aload_2
    //   167: aload_1
    //   168: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   171: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   174: aload_2
    //   175: athrow
    //   176: aload #9
    //   178: monitorexit
    //   179: aload_1
    //   180: aload_3
    //   181: aload_2
    //   182: aload #4
    //   184: aload #5
    //   186: aload #6
    //   188: invokevirtual call : (Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   191: areturn
    //   192: astore_1
    //   193: aload #9
    //   195: monitorexit
    //   196: aload_1
    //   197: athrow
    //   198: astore_2
    //   199: aload_1
    //   200: monitorexit
    //   201: aload_2
    //   202: athrow
    // Exception table:
    //   from	to	target	type
    //   29	44	198	finally
    //   49	72	198	finally
    //   75	77	198	finally
    //   80	93	192	finally
    //   97	105	192	finally
    //   114	158	161	java/security/PrivilegedActionException
    //   114	158	192	finally
    //   162	176	192	finally
    //   176	179	192	finally
    //   193	196	192	finally
    //   199	201	198	finally
  }
  
  public GeneratedClassLoader createClassLoader(final ClassLoader parent, final Object securityDomain) {
    return AccessController.<Loader>doPrivileged(new PrivilegedAction() {
          public Object run() {
            return new PolicySecurityController.Loader(parent, (CodeSource)securityDomain);
          }
        });
  }
  
  public Object getDynamicSecurityDomain(Object paramObject) {
    return paramObject;
  }
  
  public Class<?> getStaticSecurityDomainClassInternal() {
    return CodeSource.class;
  }
  
  private static class Loader extends SecureClassLoader implements GeneratedClassLoader {
    private final CodeSource codeSource;
    
    Loader(ClassLoader param1ClassLoader, CodeSource param1CodeSource) {
      super(param1ClassLoader);
      this.codeSource = param1CodeSource;
    }
    
    public Class<?> defineClass(String param1String, byte[] param1ArrayOfbyte) {
      return defineClass(param1String, param1ArrayOfbyte, 0, param1ArrayOfbyte.length, this.codeSource);
    }
    
    public void linkClass(Class<?> param1Class) {
      resolveClass(param1Class);
    }
  }
  
  public static abstract class SecureCaller {
    public abstract Object call(Callable param1Callable, Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/PolicySecurityController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */