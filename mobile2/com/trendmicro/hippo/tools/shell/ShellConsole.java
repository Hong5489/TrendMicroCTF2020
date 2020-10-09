package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Scriptable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.Charset;

public abstract class ShellConsole {
  private static final Class[] BOOLEAN_ARG;
  
  private static final Class[] CHARSEQ_ARG;
  
  private static final Class[] NO_ARG = new Class[0];
  
  private static final Class[] STRING_ARG;
  
  static {
    BOOLEAN_ARG = new Class[] { boolean.class };
    STRING_ARG = new Class[] { String.class };
    CHARSEQ_ARG = new Class[] { CharSequence.class };
  }
  
  public static ShellConsole getConsole(Scriptable paramScriptable, Charset paramCharset) {
    ClassLoader classLoader1 = ShellConsole.class.getClassLoader();
    ClassLoader classLoader2 = classLoader1;
    if (classLoader1 == null)
      classLoader2 = ClassLoader.getSystemClassLoader(); 
    if (classLoader2 == null)
      return null; 
    try {
      Class<?> clazz = Kit.classOrNull(classLoader2, "jline.console.ConsoleReader");
      if (clazz != null)
        return getJLineShellConsoleV2(classLoader2, clazz, paramScriptable, paramCharset); 
      clazz = Kit.classOrNull(classLoader2, "jline.ConsoleReader");
      if (clazz != null)
        return getJLineShellConsoleV1(classLoader2, clazz, paramScriptable, paramCharset); 
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InstantiationException instantiationException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    return null;
  }
  
  public static ShellConsole getConsole(InputStream paramInputStream, PrintStream paramPrintStream, Charset paramCharset) {
    return new SimpleShellConsole(paramInputStream, paramPrintStream, paramCharset);
  }
  
  private static JLineShellConsoleV1 getJLineShellConsoleV1(ClassLoader paramClassLoader, Class<?> paramClass, Scriptable paramScriptable, Charset paramCharset) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    paramClass = paramClass.getConstructor(new Class[0]).newInstance(new Object[0]);
    tryInvoke(paramClass, "setBellEnabled", BOOLEAN_ARG, new Object[] { Boolean.FALSE });
    Class<?> clazz = Kit.classOrNull(paramClassLoader, "jline.Completor");
    FlexibleCompletor flexibleCompletor = new FlexibleCompletor(clazz, paramScriptable);
    Object object = Proxy.newProxyInstance(paramClassLoader, new Class[] { clazz }, flexibleCompletor);
    tryInvoke(paramClass, "addCompletor", new Class[] { clazz }, new Object[] { object });
    return new JLineShellConsoleV1(paramClass, paramCharset);
  }
  
  private static JLineShellConsoleV2 getJLineShellConsoleV2(ClassLoader paramClassLoader, Class<?> paramClass, Scriptable paramScriptable, Charset paramCharset) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    paramClass = paramClass.getConstructor(new Class[0]).newInstance(new Object[0]);
    tryInvoke(paramClass, "setBellEnabled", BOOLEAN_ARG, new Object[] { Boolean.FALSE });
    Class<?> clazz = Kit.classOrNull(paramClassLoader, "jline.console.completer.Completer");
    FlexibleCompletor flexibleCompletor = new FlexibleCompletor(clazz, paramScriptable);
    Object object = Proxy.newProxyInstance(paramClassLoader, new Class[] { clazz }, flexibleCompletor);
    tryInvoke(paramClass, "addCompleter", new Class[] { clazz }, new Object[] { object });
    return new JLineShellConsoleV2(paramClass, paramCharset);
  }
  
  private static Object tryInvoke(Object paramObject, String paramString, Class[] paramArrayOfClass, Object... paramVarArgs) {
    try {
      Method method = paramObject.getClass().getDeclaredMethod(paramString, paramArrayOfClass);
      if (method != null)
        return method.invoke(paramObject, paramVarArgs); 
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalArgumentException illegalArgumentException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    return null;
  }
  
  public abstract void flush() throws IOException;
  
  public abstract InputStream getIn();
  
  public abstract void print(String paramString) throws IOException;
  
  public abstract void println() throws IOException;
  
  public abstract void println(String paramString) throws IOException;
  
  public abstract String readLine() throws IOException;
  
  public abstract String readLine(String paramString) throws IOException;
  
  private static class ConsoleInputStream extends InputStream {
    private static final byte[] EMPTY = new byte[0];
    
    private boolean atEOF = false;
    
    private byte[] buffer = EMPTY;
    
    private final ShellConsole console;
    
    private final Charset cs;
    
    private int cursor = -1;
    
    public ConsoleInputStream(ShellConsole param1ShellConsole, Charset param1Charset) {
      this.console = param1ShellConsole;
      this.cs = param1Charset;
    }
    
    private boolean ensureInput() throws IOException {
      if (this.atEOF)
        return false; 
      int i = this.cursor;
      if (i < 0 || i > this.buffer.length) {
        if (readNextLine() == -1) {
          this.atEOF = true;
          return false;
        } 
        this.cursor = 0;
      } 
      return true;
    }
    
    private int readNextLine() throws IOException {
      String str = this.console.readLine(null);
      if (str != null) {
        byte[] arrayOfByte = str.getBytes(this.cs);
        this.buffer = arrayOfByte;
        return arrayOfByte.length;
      } 
      this.buffer = EMPTY;
      return -1;
    }
    
    public int read() throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokespecial ensureInput : ()Z
      //   6: istore_1
      //   7: iload_1
      //   8: ifne -> 15
      //   11: aload_0
      //   12: monitorexit
      //   13: iconst_m1
      //   14: ireturn
      //   15: aload_0
      //   16: getfield cursor : I
      //   19: aload_0
      //   20: getfield buffer : [B
      //   23: arraylength
      //   24: if_icmpne -> 42
      //   27: aload_0
      //   28: aload_0
      //   29: getfield cursor : I
      //   32: iconst_1
      //   33: iadd
      //   34: putfield cursor : I
      //   37: aload_0
      //   38: monitorexit
      //   39: bipush #10
      //   41: ireturn
      //   42: aload_0
      //   43: getfield buffer : [B
      //   46: astore_2
      //   47: aload_0
      //   48: getfield cursor : I
      //   51: istore_3
      //   52: aload_0
      //   53: iload_3
      //   54: iconst_1
      //   55: iadd
      //   56: putfield cursor : I
      //   59: aload_2
      //   60: iload_3
      //   61: baload
      //   62: istore_3
      //   63: aload_0
      //   64: monitorexit
      //   65: iload_3
      //   66: ireturn
      //   67: astore_2
      //   68: aload_0
      //   69: monitorexit
      //   70: aload_2
      //   71: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	67	finally
      //   15	37	67	finally
      //   42	59	67	finally
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: ifnull -> 151
      //   6: iload_2
      //   7: iflt -> 141
      //   10: iload_3
      //   11: iflt -> 141
      //   14: aload_1
      //   15: arraylength
      //   16: istore #4
      //   18: iload_3
      //   19: iload #4
      //   21: iload_2
      //   22: isub
      //   23: if_icmpgt -> 141
      //   26: iload_3
      //   27: ifne -> 34
      //   30: aload_0
      //   31: monitorexit
      //   32: iconst_0
      //   33: ireturn
      //   34: aload_0
      //   35: invokespecial ensureInput : ()Z
      //   38: istore #5
      //   40: iload #5
      //   42: ifne -> 49
      //   45: aload_0
      //   46: monitorexit
      //   47: iconst_m1
      //   48: ireturn
      //   49: iload_3
      //   50: aload_0
      //   51: getfield buffer : [B
      //   54: arraylength
      //   55: aload_0
      //   56: getfield cursor : I
      //   59: isub
      //   60: invokestatic min : (II)I
      //   63: istore #6
      //   65: iconst_0
      //   66: istore #4
      //   68: iload #4
      //   70: iload #6
      //   72: if_icmpge -> 100
      //   75: aload_1
      //   76: iload_2
      //   77: iload #4
      //   79: iadd
      //   80: aload_0
      //   81: getfield buffer : [B
      //   84: aload_0
      //   85: getfield cursor : I
      //   88: iload #4
      //   90: iadd
      //   91: baload
      //   92: i2b
      //   93: bastore
      //   94: iinc #4, 1
      //   97: goto -> 68
      //   100: iload #6
      //   102: istore #4
      //   104: iload #6
      //   106: iload_3
      //   107: if_icmpge -> 125
      //   110: aload_1
      //   111: iload #6
      //   113: iload_2
      //   114: iadd
      //   115: bipush #10
      //   117: i2b
      //   118: bastore
      //   119: iload #6
      //   121: iconst_1
      //   122: iadd
      //   123: istore #4
      //   125: aload_0
      //   126: aload_0
      //   127: getfield cursor : I
      //   130: iload #4
      //   132: iadd
      //   133: putfield cursor : I
      //   136: aload_0
      //   137: monitorexit
      //   138: iload #4
      //   140: ireturn
      //   141: new java/lang/IndexOutOfBoundsException
      //   144: astore_1
      //   145: aload_1
      //   146: invokespecial <init> : ()V
      //   149: aload_1
      //   150: athrow
      //   151: new java/lang/NullPointerException
      //   154: astore_1
      //   155: aload_1
      //   156: invokespecial <init> : ()V
      //   159: aload_1
      //   160: athrow
      //   161: astore_1
      //   162: aload_0
      //   163: monitorexit
      //   164: aload_1
      //   165: athrow
      // Exception table:
      //   from	to	target	type
      //   14	18	161	finally
      //   34	40	161	finally
      //   49	65	161	finally
      //   75	94	161	finally
      //   125	136	161	finally
      //   141	151	161	finally
      //   151	161	161	finally
    }
  }
  
  private static class JLineShellConsoleV1 extends ShellConsole {
    private final InputStream in;
    
    private final Object reader;
    
    JLineShellConsoleV1(Object param1Object, Charset param1Charset) {
      this.reader = param1Object;
      this.in = new ShellConsole.ConsoleInputStream(this, param1Charset);
    }
    
    public void flush() throws IOException {
      ShellConsole.tryInvoke(this.reader, "flushConsole", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public InputStream getIn() {
      return this.in;
    }
    
    public void print(String param1String) throws IOException {
      ShellConsole.tryInvoke(this.reader, "printString", ShellConsole.STRING_ARG, new Object[] { param1String });
    }
    
    public void println() throws IOException {
      ShellConsole.tryInvoke(this.reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public void println(String param1String) throws IOException {
      ShellConsole.tryInvoke(this.reader, "printString", ShellConsole.STRING_ARG, new Object[] { param1String });
      ShellConsole.tryInvoke(this.reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public String readLine() throws IOException {
      return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public String readLine(String param1String) throws IOException {
      return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.STRING_ARG, new Object[] { param1String });
    }
  }
  
  private static class JLineShellConsoleV2 extends ShellConsole {
    private final InputStream in;
    
    private final Object reader;
    
    JLineShellConsoleV2(Object param1Object, Charset param1Charset) {
      this.reader = param1Object;
      this.in = new ShellConsole.ConsoleInputStream(this, param1Charset);
    }
    
    public void flush() throws IOException {
      ShellConsole.tryInvoke(this.reader, "flush", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public InputStream getIn() {
      return this.in;
    }
    
    public void print(String param1String) throws IOException {
      ShellConsole.tryInvoke(this.reader, "print", ShellConsole.CHARSEQ_ARG, new Object[] { param1String });
    }
    
    public void println() throws IOException {
      ShellConsole.tryInvoke(this.reader, "println", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public void println(String param1String) throws IOException {
      ShellConsole.tryInvoke(this.reader, "println", ShellConsole.CHARSEQ_ARG, new Object[] { param1String });
    }
    
    public String readLine() throws IOException {
      return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
    }
    
    public String readLine(String param1String) throws IOException {
      return (String)ShellConsole.tryInvoke(this.reader, "readLine", ShellConsole.STRING_ARG, new Object[] { param1String });
    }
  }
  
  private static class SimpleShellConsole extends ShellConsole {
    private final InputStream in;
    
    private final PrintWriter out;
    
    private final BufferedReader reader;
    
    SimpleShellConsole(InputStream param1InputStream, PrintStream param1PrintStream, Charset param1Charset) {
      this.in = param1InputStream;
      this.out = new PrintWriter(param1PrintStream);
      this.reader = new BufferedReader(new InputStreamReader(param1InputStream, param1Charset));
    }
    
    public void flush() throws IOException {
      this.out.flush();
    }
    
    public InputStream getIn() {
      return this.in;
    }
    
    public void print(String param1String) throws IOException {
      this.out.print(param1String);
    }
    
    public void println() throws IOException {
      this.out.println();
    }
    
    public void println(String param1String) throws IOException {
      this.out.println(param1String);
    }
    
    public String readLine() throws IOException {
      return this.reader.readLine();
    }
    
    public String readLine(String param1String) throws IOException {
      if (param1String != null) {
        this.out.write(param1String);
        this.out.flush();
      } 
      return this.reader.readLine();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/ShellConsole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */