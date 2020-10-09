package com.trendmicro.hippo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class SecureCaller {
  private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers;
  
  private static final byte[] secureCallerImplBytecode = loadBytecode();
  
  static {
    callers = new WeakHashMap<>();
  }
  
  static Object callSecurely(CodeSource paramCodeSource, Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: new com/trendmicro/hippo/SecureCaller$1
    //   3: dup
    //   4: invokestatic currentThread : ()Ljava/lang/Thread;
    //   7: invokespecial <init> : (Ljava/lang/Thread;)V
    //   10: invokestatic doPrivileged : (Ljava/security/PrivilegedAction;)Ljava/lang/Object;
    //   13: checkcast java/lang/ClassLoader
    //   16: astore #6
    //   18: getstatic com/trendmicro/hippo/SecureCaller.callers : Ljava/util/Map;
    //   21: astore #7
    //   23: aload #7
    //   25: monitorenter
    //   26: getstatic com/trendmicro/hippo/SecureCaller.callers : Ljava/util/Map;
    //   29: aload_0
    //   30: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   35: checkcast java/util/Map
    //   38: astore #8
    //   40: aload #8
    //   42: astore #9
    //   44: aload #8
    //   46: ifnonnull -> 71
    //   49: new java/util/WeakHashMap
    //   52: astore #9
    //   54: aload #9
    //   56: invokespecial <init> : ()V
    //   59: getstatic com/trendmicro/hippo/SecureCaller.callers : Ljava/util/Map;
    //   62: aload_0
    //   63: aload #9
    //   65: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   70: pop
    //   71: aload #7
    //   73: monitorexit
    //   74: aload #9
    //   76: monitorenter
    //   77: aload #9
    //   79: aload #6
    //   81: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   86: checkcast java/lang/ref/SoftReference
    //   89: astore #8
    //   91: aload #8
    //   93: ifnull -> 109
    //   96: aload #8
    //   98: invokevirtual get : ()Ljava/lang/Object;
    //   101: checkcast com/trendmicro/hippo/SecureCaller
    //   104: astore #8
    //   106: goto -> 112
    //   109: aconst_null
    //   110: astore #8
    //   112: aload #8
    //   114: ifnonnull -> 180
    //   117: new com/trendmicro/hippo/SecureCaller$2
    //   120: astore #8
    //   122: aload #8
    //   124: aload #6
    //   126: aload_0
    //   127: invokespecial <init> : (Ljava/lang/ClassLoader;Ljava/security/CodeSource;)V
    //   130: aload #8
    //   132: invokestatic doPrivileged : (Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;
    //   135: checkcast com/trendmicro/hippo/SecureCaller
    //   138: astore_0
    //   139: new java/lang/ref/SoftReference
    //   142: astore #8
    //   144: aload #8
    //   146: aload_0
    //   147: invokespecial <init> : (Ljava/lang/Object;)V
    //   150: aload #9
    //   152: aload #6
    //   154: aload #8
    //   156: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   161: pop
    //   162: goto -> 183
    //   165: astore_0
    //   166: new java/lang/reflect/UndeclaredThrowableException
    //   169: astore_1
    //   170: aload_1
    //   171: aload_0
    //   172: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   175: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   178: aload_1
    //   179: athrow
    //   180: aload #8
    //   182: astore_0
    //   183: aload #9
    //   185: monitorexit
    //   186: aload_0
    //   187: aload_1
    //   188: aload_2
    //   189: aload_3
    //   190: aload #4
    //   192: aload #5
    //   194: invokevirtual call : (Lcom/trendmicro/hippo/Callable;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
    //   197: areturn
    //   198: astore_0
    //   199: aload #9
    //   201: monitorexit
    //   202: aload_0
    //   203: athrow
    //   204: astore_0
    //   205: aload #7
    //   207: monitorexit
    //   208: aload_0
    //   209: athrow
    // Exception table:
    //   from	to	target	type
    //   26	40	204	finally
    //   49	59	204	finally
    //   59	71	204	finally
    //   71	74	204	finally
    //   77	91	198	finally
    //   96	106	198	finally
    //   117	162	165	java/security/PrivilegedActionException
    //   117	162	198	finally
    //   166	180	198	finally
    //   183	186	198	finally
    //   199	202	198	finally
    //   205	208	204	finally
  }
  
  private static byte[] loadBytecode() {
    return AccessController.<byte[]>doPrivileged(new PrivilegedAction() {
          public Object run() {
            return SecureCaller.loadBytecodePrivileged();
          }
        });
  }
  
  private static byte[] loadBytecodePrivileged() {
    URL uRL = SecureCaller.class.getResource("SecureCallerImpl.clazz");
    try {
      InputStream inputStream = uRL.openStream();
      try {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this();
        while (true) {
          byte[] arrayOfByte;
          int i = inputStream.read();
          if (i == -1) {
            arrayOfByte = byteArrayOutputStream.toByteArray();
            return arrayOfByte;
          } 
          arrayOfByte.write(i);
        } 
      } finally {
        inputStream.close();
      } 
    } catch (IOException iOException) {
      throw new UndeclaredThrowableException(iOException);
    } 
  }
  
  public abstract Object call(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  
  private static class SecureClassLoaderImpl extends SecureClassLoader {
    SecureClassLoaderImpl(ClassLoader param1ClassLoader) {
      super(param1ClassLoader);
    }
    
    Class<?> defineAndLinkClass(String param1String, byte[] param1ArrayOfbyte, CodeSource param1CodeSource) {
      Class<?> clazz = defineClass(param1String, param1ArrayOfbyte, 0, param1ArrayOfbyte.length, param1CodeSource);
      resolveClass(clazz);
      return clazz;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SecureCaller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */