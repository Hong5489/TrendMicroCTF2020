package com.trendmicro.hippo;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public class Kit {
  public static Object addListener(Object paramObject1, Object paramObject2) {
    if (paramObject2 != null) {
      if (!(paramObject2 instanceof Object[])) {
        if (paramObject1 == null) {
          paramObject1 = paramObject2;
        } else if (!(paramObject1 instanceof Object[])) {
          paramObject1 = new Object[] { paramObject1, paramObject2 };
        } else {
          Object[] arrayOfObject = (Object[])paramObject1;
          int i = arrayOfObject.length;
          if (i >= 2) {
            paramObject1 = new Object[i + 1];
            System.arraycopy(arrayOfObject, 0, paramObject1, 0, i);
            paramObject1[i] = paramObject2;
            return paramObject1;
          } 
          throw new IllegalArgumentException();
        } 
        return paramObject1;
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static Class<?> classOrNull(ClassLoader paramClassLoader, String paramString) {
    try {
      return paramClassLoader.loadClass(paramString);
    } catch (ClassNotFoundException classNotFoundException) {
    
    } catch (SecurityException securityException) {
    
    } catch (LinkageError linkageError) {
    
    } catch (IllegalArgumentException illegalArgumentException) {}
    return null;
  }
  
  public static Class<?> classOrNull(String paramString) {
    try {
      return Class.forName(paramString);
    } catch (ClassNotFoundException classNotFoundException) {
    
    } catch (SecurityException securityException) {
    
    } catch (LinkageError linkageError) {
    
    } catch (IllegalArgumentException illegalArgumentException) {}
    return null;
  }
  
  public static RuntimeException codeBug() throws RuntimeException {
    IllegalStateException illegalStateException = new IllegalStateException("FAILED ASSERTION");
    illegalStateException.printStackTrace(System.err);
    throw illegalStateException;
  }
  
  public static RuntimeException codeBug(String paramString) throws RuntimeException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FAILED ASSERTION: ");
    stringBuilder.append(paramString);
    IllegalStateException illegalStateException = new IllegalStateException(stringBuilder.toString());
    illegalStateException.printStackTrace(System.err);
    throw illegalStateException;
  }
  
  public static Object getListener(Object paramObject, int paramInt) {
    if (paramInt == 0) {
      if (paramObject == null)
        return null; 
      if (!(paramObject instanceof Object[]))
        return paramObject; 
      paramObject = paramObject;
      if (paramObject.length >= 2)
        return paramObject[0]; 
      throw new IllegalArgumentException();
    } 
    if (paramInt == 1) {
      if (!(paramObject instanceof Object[])) {
        if (paramObject != null)
          return null; 
        throw new IllegalArgumentException();
      } 
      return ((Object[])paramObject)[1];
    } 
    paramObject = paramObject;
    int i = paramObject.length;
    if (i >= 2)
      return (paramInt == i) ? null : paramObject[paramInt]; 
    throw new IllegalArgumentException();
  }
  
  static Object initHash(Map<Object, Object> paramMap, Object paramObject1, Object paramObject2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   9: astore_3
    //   10: aload_3
    //   11: ifnonnull -> 28
    //   14: aload_0
    //   15: aload_1
    //   16: aload_2
    //   17: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   22: pop
    //   23: aload_2
    //   24: astore_1
    //   25: goto -> 30
    //   28: aload_3
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: areturn
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	34	finally
    //   14	23	34	finally
    //   30	32	34	finally
    //   35	37	34	finally
  }
  
  public static Object makeHashKeyFromPair(Object paramObject1, Object paramObject2) {
    if (paramObject1 != null) {
      if (paramObject2 != null)
        return new ComplexKey(paramObject1, paramObject2); 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  static Object newInstanceOrNull(Class<?> paramClass) {
    try {
      return paramClass.newInstance();
    } catch (SecurityException securityException) {
    
    } catch (LinkageError linkageError) {
    
    } catch (InstantiationException instantiationException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
    return null;
  }
  
  public static String readReader(Reader paramReader) throws IOException {
    char[] arrayOfChar = new char[512];
    int i = 0;
    while (true) {
      int j = paramReader.read(arrayOfChar, i, arrayOfChar.length - i);
      if (j < 0)
        return new String(arrayOfChar, 0, i); 
      i += j;
      char[] arrayOfChar1 = arrayOfChar;
      if (i == arrayOfChar.length) {
        arrayOfChar1 = new char[arrayOfChar.length * 2];
        System.arraycopy(arrayOfChar, 0, arrayOfChar1, 0, i);
      } 
      arrayOfChar = arrayOfChar1;
    } 
  }
  
  public static byte[] readStream(InputStream paramInputStream, int paramInt) throws IOException {
    if (paramInt > 0) {
      byte[] arrayOfByte = new byte[paramInt];
      paramInt = 0;
      while (true) {
        int i = paramInputStream.read(arrayOfByte, paramInt, arrayOfByte.length - paramInt);
        if (i < 0) {
          byte[] arrayOfByte2 = arrayOfByte;
          if (paramInt != arrayOfByte.length) {
            arrayOfByte2 = new byte[paramInt];
            System.arraycopy(arrayOfByte, 0, arrayOfByte2, 0, paramInt);
          } 
          return arrayOfByte2;
        } 
        paramInt += i;
        byte[] arrayOfByte1 = arrayOfByte;
        if (paramInt == arrayOfByte.length) {
          arrayOfByte1 = new byte[arrayOfByte.length * 2];
          System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, paramInt);
        } 
        arrayOfByte = arrayOfByte1;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad initialBufferCapacity: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static Object removeListener(Object paramObject1, Object paramObject2) {
    if (paramObject2 != null) {
      if (!(paramObject2 instanceof Object[])) {
        Object object;
        if (paramObject1 == paramObject2) {
          object = null;
        } else {
          object = paramObject1;
          if (paramObject1 instanceof Object[]) {
            Object[] arrayOfObject = (Object[])paramObject1;
            int i = arrayOfObject.length;
            if (i >= 2) {
              if (i == 2) {
                if (arrayOfObject[1] == paramObject2) {
                  object = arrayOfObject[0];
                } else {
                  object = paramObject1;
                  if (arrayOfObject[0] == paramObject2)
                    object = arrayOfObject[1]; 
                } 
              } else {
                int j = i;
                while (true) {
                  int k = j - 1;
                  if (arrayOfObject[k] == paramObject2) {
                    object = new Object[i - 1];
                    System.arraycopy(arrayOfObject, 0, object, 0, k);
                    System.arraycopy(arrayOfObject, k + 1, object, k, i - k + 1);
                    break;
                  } 
                  j = k;
                  if (k == 0) {
                    object = paramObject1;
                    break;
                  } 
                } 
              } 
            } else {
              throw new IllegalArgumentException();
            } 
          } 
        } 
        return object;
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  static boolean testIfCanLoadHippoClasses(ClassLoader paramClassLoader) {
    Class<?> clazz = ScriptRuntime.ContextFactoryClass;
    return !(classOrNull(paramClassLoader, clazz.getName()) != clazz);
  }
  
  public static int xDigitToInt(int paramInt1, int paramInt2) {
    if (paramInt1 <= 57) {
      paramInt1 -= 48;
      if (paramInt1 >= 0)
        return paramInt2 << 4 | paramInt1; 
    } else if (paramInt1 <= 70) {
      if (65 <= paramInt1) {
        paramInt1 -= 55;
        return paramInt2 << 4 | paramInt1;
      } 
    } else if (paramInt1 <= 102 && 97 <= paramInt1) {
      paramInt1 -= 87;
      return paramInt2 << 4 | paramInt1;
    } 
    return -1;
  }
  
  private static final class ComplexKey {
    private int hash;
    
    private Object key1;
    
    private Object key2;
    
    ComplexKey(Object param1Object1, Object param1Object2) {
      this.key1 = param1Object1;
      this.key2 = param1Object2;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof ComplexKey;
      boolean bool1 = false;
      if (!bool)
        return false; 
      param1Object = param1Object;
      bool = bool1;
      if (this.key1.equals(((ComplexKey)param1Object).key1)) {
        bool = bool1;
        if (this.key2.equals(((ComplexKey)param1Object).key2))
          bool = true; 
      } 
      return bool;
    }
    
    public int hashCode() {
      if (this.hash == 0)
        this.hash = this.key1.hashCode() ^ this.key2.hashCode(); 
      return this.hash;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Kit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */