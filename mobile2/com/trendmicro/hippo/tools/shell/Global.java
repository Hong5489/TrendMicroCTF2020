package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.ImporterTopLevel;
import com.trendmicro.hippo.NativeArray;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.Synchronizer;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.Wrapper;
import com.trendmicro.hippo.commonjs.module.ModuleScriptProvider;
import com.trendmicro.hippo.commonjs.module.Require;
import com.trendmicro.hippo.commonjs.module.RequireBuilder;
import com.trendmicro.hippo.commonjs.module.provider.ModuleSourceProvider;
import com.trendmicro.hippo.commonjs.module.provider.SoftCachingModuleScriptProvider;
import com.trendmicro.hippo.commonjs.module.provider.UrlModuleSourceProvider;
import com.trendmicro.hippo.serialize.ScriptableInputStream;
import com.trendmicro.hippo.serialize.ScriptableOutputStream;
import com.trendmicro.hippo.tools.ToolErrorReporter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Global extends ImporterTopLevel {
  static final long serialVersionUID = 4029130780977538005L;
  
  boolean attemptedJLineLoad;
  
  private ShellConsole console;
  
  private HashMap<String, String> doctestCanonicalizations;
  
  private PrintStream errStream;
  
  NativeArray history;
  
  private InputStream inStream;
  
  boolean initialized;
  
  private PrintStream outStream;
  
  private String[] prompts = new String[] { "js> ", "  > " };
  
  private QuitAction quitAction;
  
  private boolean sealedStdLib = false;
  
  public Global() {}
  
  public Global(Context paramContext) {
    init(paramContext);
  }
  
  public static void defineClass(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    Class<?> clazz = getClass(paramArrayOfObject);
    if (Scriptable.class.isAssignableFrom(clazz)) {
      ScriptableObject.defineClass(paramScriptable, clazz);
      return;
    } 
    throw reportRuntimeError("msg.must.implement.Scriptable");
  }
  
  public static Object deserialize(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IOException, ClassNotFoundException {
    if (paramArrayOfObject.length >= 1) {
      FileInputStream fileInputStream = new FileInputStream(Context.toString(paramArrayOfObject[0]));
      Scriptable scriptable = ScriptableObject.getTopLevelScope(paramScriptable);
      ScriptableInputStream scriptableInputStream = new ScriptableInputStream(fileInputStream, scriptable);
      Object object = scriptableInputStream.readObject();
      scriptableInputStream.close();
      return Context.toObject(object, scriptable);
    } 
    throw Context.reportRuntimeError("Expected a filename to read the serialization from");
  }
  
  private static Object doPrint(Object[] paramArrayOfObject, Function paramFunction, boolean paramBoolean) {
    PrintStream printStream = getInstance(paramFunction).getOut();
    for (byte b = 0; b < paramArrayOfObject.length; b++) {
      if (b > 0)
        printStream.print(" "); 
      printStream.print(Context.toString(paramArrayOfObject[b]));
    } 
    if (paramBoolean)
      printStream.println(); 
    return Context.getUndefinedValue();
  }
  
  public static Object doctest(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    if (paramArrayOfObject.length == 0)
      return Boolean.FALSE; 
    String str = Context.toString(paramArrayOfObject[0]);
    Global global = getInstance(paramFunction);
    return new Integer(global.runDoctest(paramContext, (Scriptable)global, str, (String)null, 0));
  }
  
  private boolean doctestOutputMatches(String paramString1, String paramString2) {
    paramString1 = paramString1.trim();
    paramString2 = paramString2.trim().replace("\r\n", "\n");
    if (paramString1.equals(paramString2))
      return true; 
    for (Map.Entry<String, String> entry : this.doctestCanonicalizations.entrySet())
      paramString1 = paramString1.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue()); 
    if (paramString1.equals(paramString2))
      return true; 
    Pattern pattern = Pattern.compile("@[0-9a-fA-F]+");
    Matcher matcher1 = pattern.matcher(paramString1);
    Matcher matcher2 = pattern.matcher(paramString2);
    while (true) {
      if (!matcher1.find())
        return false; 
      if (!matcher2.find())
        return false; 
      if (matcher2.start() != matcher1.start())
        return false; 
      int i = matcher1.start();
      if (!paramString1.substring(0, i).equals(paramString2.substring(0, i)))
        return false; 
      String str1 = matcher1.group();
      String str2 = matcher2.group();
      String str3 = this.doctestCanonicalizations.get(str1);
      if (str3 == null) {
        this.doctestCanonicalizations.put(str1, str2);
        paramString1 = paramString1.replace(str1, str2);
      } else if (!str2.equals(str3)) {
        return false;
      } 
      if (paramString1.equals(paramString2))
        return true; 
    } 
  }
  
  public static void gc(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    System.gc();
  }
  
  private static String getCharCodingFromType(String paramString) {
    int i = paramString.indexOf(';');
    if (i >= 0) {
      int j = paramString.length();
      while (++i != j && paramString.charAt(i) <= ' ')
        i++; 
      if ("charset".regionMatches(true, 0, paramString, i, "charset".length())) {
        for (i += "charset".length(); i != j && paramString.charAt(i) <= ' '; i++);
        if (i != j && paramString.charAt(i) == '=') {
          while (++i != j && paramString.charAt(i) <= ' ')
            i++; 
          if (i != j) {
            while (paramString.charAt(j - 1) <= ' ')
              j--; 
            return paramString.substring(i, j);
          } 
        } 
      } 
    } 
    return null;
  }
  
  private static Class<?> getClass(Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length != 0) {
      Object<?> object = (Object<?>)paramArrayOfObject[0];
      if (object instanceof Wrapper) {
        object = (Object<?>)((Wrapper)object).unwrap();
        if (object instanceof Class)
          return (Class)object; 
      } 
      String str = Context.toString(paramArrayOfObject[0]);
      try {
        return Class.forName(str);
      } catch (ClassNotFoundException classNotFoundException) {
        throw reportRuntimeError("msg.class.not.found", str);
      } 
    } 
    throw reportRuntimeError("msg.expected.string.arg");
  }
  
  private static Global getInstance(Function paramFunction) {
    Scriptable scriptable = paramFunction.getParentScope();
    if (scriptable instanceof Global)
      return (Global)scriptable; 
    throw reportRuntimeError("msg.bad.shell.function.scope", String.valueOf(scriptable));
  }
  
  public static void help(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    getInstance(paramFunction).getOut().println(ToolErrorReporter.getMessage("msg.help"));
  }
  
  public static void load(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    int i = paramArrayOfObject.length;
    byte b = 0;
    while (b < i) {
      String str = Context.toString(paramArrayOfObject[b]);
      try {
        Main.processFile(paramContext, paramScriptable, str);
        b++;
      } catch (IOException iOException) {
        throw Context.reportRuntimeError(ToolErrorReporter.getMessage("msg.couldnt.read.source", str, iOException.getMessage()));
      } catch (VirtualMachineError virtualMachineError) {
        virtualMachineError.printStackTrace();
        throw Context.reportRuntimeError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
      } 
    } 
  }
  
  public static void loadClass(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IllegalAccessException, InstantiationException {
    Class<?> clazz = getClass(paramArrayOfObject);
    if (Script.class.isAssignableFrom(clazz)) {
      ((Script)clazz.newInstance()).exec(paramContext, paramScriptable);
      return;
    } 
    throw reportRuntimeError("msg.must.implement.Script");
  }
  
  private boolean loadJLine(Charset paramCharset) {
    boolean bool = this.attemptedJLineLoad;
    boolean bool1 = true;
    if (!bool) {
      this.attemptedJLineLoad = true;
      this.console = ShellConsole.getConsole((Scriptable)this, paramCharset);
    } 
    if (this.console == null)
      bool1 = false; 
    return bool1;
  }
  
  static void pipe(boolean paramBoolean, InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    try {
      byte[] arrayOfByte = new byte[4096];
      while (true) {
        int i;
        if (!paramBoolean) {
          i = paramInputStream.read(arrayOfByte, 0, 4096);
        } else {
          try {
            i = paramInputStream.read(arrayOfByte, 0, 4096);
            if (i < 0)
              break; 
            if (paramBoolean) {
              paramOutputStream.write(arrayOfByte, 0, i);
              paramOutputStream.flush();
              continue;
            } 
            try {
              paramOutputStream.write(arrayOfByte, 0, i);
              paramOutputStream.flush();
              continue;
            } catch (IOException null) {
              break;
            } 
          } catch (IOException iOException1) {
            break;
          } 
        } 
        if (i < 0)
          break; 
        if (paramBoolean) {
          paramOutputStream.write((byte[])iOException1, 0, i);
          paramOutputStream.flush();
          continue;
        } 
        try {
          paramOutputStream.write((byte[])iOException1, 0, i);
          paramOutputStream.flush();
        } catch (IOException iOException2) {
          break;
        } 
      } 
      return;
    } finally {
      if (paramBoolean) {
        try {
          iOException.close();
        } catch (IOException iOException1) {}
      } else {
        paramOutputStream.close();
      } 
    } 
  }
  
  public static Object print(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    return doPrint(paramArrayOfObject, paramFunction, true);
  }
  
  public static void quit(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    Global global = getInstance(paramFunction);
    if (global.quitAction != null) {
      int i = paramArrayOfObject.length;
      int j = 0;
      if (i != 0)
        j = ScriptRuntime.toInt32(paramArrayOfObject[0]); 
      global.quitAction.quit(paramContext, j);
    } 
  }
  
  public static Object readFile(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IOException {
    if (paramArrayOfObject.length != 0) {
      String str1;
      String str2 = ScriptRuntime.toString(paramArrayOfObject[0]);
      paramContext = null;
      if (paramArrayOfObject.length >= 2)
        str1 = ScriptRuntime.toString(paramArrayOfObject[1]); 
      return readUrl(str2, str1, true);
    } 
    throw reportRuntimeError("msg.shell.readFile.bad.args");
  }
  
  private static String readReader(Reader paramReader) throws IOException {
    return readReader(paramReader, 4096);
  }
  
  private static String readReader(Reader paramReader, int paramInt) throws IOException {
    char[] arrayOfChar = new char[paramInt];
    paramInt = 0;
    while (true) {
      int i = paramReader.read(arrayOfChar, paramInt, arrayOfChar.length - paramInt);
      if (i < 0)
        return new String(arrayOfChar, 0, paramInt); 
      paramInt += i;
      char[] arrayOfChar1 = arrayOfChar;
      if (paramInt == arrayOfChar.length) {
        arrayOfChar1 = new char[arrayOfChar.length * 2];
        System.arraycopy(arrayOfChar, 0, arrayOfChar1, 0, paramInt);
      } 
      arrayOfChar = arrayOfChar1;
    } 
  }
  
  public static Object readUrl(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IOException {
    if (paramArrayOfObject.length != 0) {
      String str1;
      String str2 = ScriptRuntime.toString(paramArrayOfObject[0]);
      paramContext = null;
      if (paramArrayOfObject.length >= 2)
        str1 = ScriptRuntime.toString(paramArrayOfObject[1]); 
      return readUrl(str2, str1, false);
    } 
    throw reportRuntimeError("msg.shell.readUrl.bad.args");
  }
  
  private static String readUrl(String paramString1, String paramString2, boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: iload_2
    //   3: ifne -> 106
    //   6: aload_3
    //   7: astore #4
    //   9: new java/net/URL
    //   12: astore #5
    //   14: aload_3
    //   15: astore #4
    //   17: aload #5
    //   19: aload_0
    //   20: invokespecial <init> : (Ljava/lang/String;)V
    //   23: aload_3
    //   24: astore #4
    //   26: aload #5
    //   28: invokevirtual openConnection : ()Ljava/net/URLConnection;
    //   31: astore #5
    //   33: aload_3
    //   34: astore #4
    //   36: aload #5
    //   38: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   41: astore_0
    //   42: aload_0
    //   43: astore #4
    //   45: aload #5
    //   47: invokevirtual getContentLength : ()I
    //   50: istore #6
    //   52: iload #6
    //   54: istore #7
    //   56: iload #6
    //   58: ifgt -> 66
    //   61: sipush #1024
    //   64: istore #7
    //   66: aload_1
    //   67: astore #4
    //   69: aload_1
    //   70: ifnonnull -> 100
    //   73: aload_0
    //   74: astore #4
    //   76: aload #5
    //   78: invokevirtual getContentType : ()Ljava/lang/String;
    //   81: astore_3
    //   82: aload_1
    //   83: astore #4
    //   85: aload_3
    //   86: ifnull -> 100
    //   89: aload_0
    //   90: astore #4
    //   92: aload_3
    //   93: invokestatic getCharCodingFromType : (Ljava/lang/String;)Ljava/lang/String;
    //   96: astore_1
    //   97: aload_1
    //   98: astore #4
    //   100: aload #4
    //   102: astore_1
    //   103: goto -> 203
    //   106: aload_3
    //   107: astore #4
    //   109: new java/io/File
    //   112: astore #5
    //   114: aload_3
    //   115: astore #4
    //   117: aload #5
    //   119: aload_0
    //   120: invokespecial <init> : (Ljava/lang/String;)V
    //   123: aload_3
    //   124: astore #4
    //   126: aload #5
    //   128: invokevirtual exists : ()Z
    //   131: ifeq -> 376
    //   134: aload_3
    //   135: astore #4
    //   137: aload #5
    //   139: invokevirtual canRead : ()Z
    //   142: ifeq -> 316
    //   145: aload_3
    //   146: astore #4
    //   148: aload #5
    //   150: invokevirtual length : ()J
    //   153: lstore #8
    //   155: lload #8
    //   157: l2i
    //   158: istore #7
    //   160: iload #7
    //   162: i2l
    //   163: lload #8
    //   165: lcmp
    //   166: ifne -> 258
    //   169: iload #7
    //   171: ifne -> 190
    //   174: iconst_0
    //   175: ifeq -> 186
    //   178: new java/lang/NullPointerException
    //   181: dup
    //   182: invokespecial <init> : ()V
    //   185: athrow
    //   186: ldc_w ''
    //   189: areturn
    //   190: aload_3
    //   191: astore #4
    //   193: new java/io/FileInputStream
    //   196: dup
    //   197: aload #5
    //   199: invokespecial <init> : (Ljava/io/File;)V
    //   202: astore_0
    //   203: aload_1
    //   204: ifnonnull -> 225
    //   207: aload_0
    //   208: astore #4
    //   210: new java/io/InputStreamReader
    //   213: astore_1
    //   214: aload_0
    //   215: astore #4
    //   217: aload_1
    //   218: aload_0
    //   219: invokespecial <init> : (Ljava/io/InputStream;)V
    //   222: goto -> 238
    //   225: aload_0
    //   226: astore #4
    //   228: new java/io/InputStreamReader
    //   231: dup
    //   232: aload_0
    //   233: aload_1
    //   234: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
    //   237: astore_1
    //   238: aload_0
    //   239: astore #4
    //   241: aload_1
    //   242: iload #7
    //   244: invokestatic readReader : (Ljava/io/Reader;I)Ljava/lang/String;
    //   247: astore_1
    //   248: aload_0
    //   249: ifnull -> 256
    //   252: aload_0
    //   253: invokevirtual close : ()V
    //   256: aload_1
    //   257: areturn
    //   258: aload_3
    //   259: astore #4
    //   261: new java/io/IOException
    //   264: astore_1
    //   265: aload_3
    //   266: astore #4
    //   268: new java/lang/StringBuilder
    //   271: astore_0
    //   272: aload_3
    //   273: astore #4
    //   275: aload_0
    //   276: invokespecial <init> : ()V
    //   279: aload_3
    //   280: astore #4
    //   282: aload_0
    //   283: ldc_w 'Too big file size: '
    //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: aload_3
    //   291: astore #4
    //   293: aload_0
    //   294: lload #8
    //   296: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   299: pop
    //   300: aload_3
    //   301: astore #4
    //   303: aload_1
    //   304: aload_0
    //   305: invokevirtual toString : ()Ljava/lang/String;
    //   308: invokespecial <init> : (Ljava/lang/String;)V
    //   311: aload_3
    //   312: astore #4
    //   314: aload_1
    //   315: athrow
    //   316: aload_3
    //   317: astore #4
    //   319: new java/io/IOException
    //   322: astore #5
    //   324: aload_3
    //   325: astore #4
    //   327: new java/lang/StringBuilder
    //   330: astore_1
    //   331: aload_3
    //   332: astore #4
    //   334: aload_1
    //   335: invokespecial <init> : ()V
    //   338: aload_3
    //   339: astore #4
    //   341: aload_1
    //   342: ldc_w 'Cannot read file: '
    //   345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   348: pop
    //   349: aload_3
    //   350: astore #4
    //   352: aload_1
    //   353: aload_0
    //   354: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   357: pop
    //   358: aload_3
    //   359: astore #4
    //   361: aload #5
    //   363: aload_1
    //   364: invokevirtual toString : ()Ljava/lang/String;
    //   367: invokespecial <init> : (Ljava/lang/String;)V
    //   370: aload_3
    //   371: astore #4
    //   373: aload #5
    //   375: athrow
    //   376: aload_3
    //   377: astore #4
    //   379: new java/io/FileNotFoundException
    //   382: astore_1
    //   383: aload_3
    //   384: astore #4
    //   386: new java/lang/StringBuilder
    //   389: astore #5
    //   391: aload_3
    //   392: astore #4
    //   394: aload #5
    //   396: invokespecial <init> : ()V
    //   399: aload_3
    //   400: astore #4
    //   402: aload #5
    //   404: ldc_w 'File not found: '
    //   407: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: pop
    //   411: aload_3
    //   412: astore #4
    //   414: aload #5
    //   416: aload_0
    //   417: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   420: pop
    //   421: aload_3
    //   422: astore #4
    //   424: aload_1
    //   425: aload #5
    //   427: invokevirtual toString : ()Ljava/lang/String;
    //   430: invokespecial <init> : (Ljava/lang/String;)V
    //   433: aload_3
    //   434: astore #4
    //   436: aload_1
    //   437: athrow
    //   438: astore_0
    //   439: aload #4
    //   441: ifnull -> 449
    //   444: aload #4
    //   446: invokevirtual close : ()V
    //   449: aload_0
    //   450: athrow
    // Exception table:
    //   from	to	target	type
    //   9	14	438	finally
    //   17	23	438	finally
    //   26	33	438	finally
    //   36	42	438	finally
    //   45	52	438	finally
    //   76	82	438	finally
    //   92	97	438	finally
    //   109	114	438	finally
    //   117	123	438	finally
    //   126	134	438	finally
    //   137	145	438	finally
    //   148	155	438	finally
    //   193	203	438	finally
    //   210	214	438	finally
    //   217	222	438	finally
    //   228	238	438	finally
    //   241	248	438	finally
    //   261	265	438	finally
    //   268	272	438	finally
    //   275	279	438	finally
    //   282	290	438	finally
    //   293	300	438	finally
    //   303	311	438	finally
    //   314	316	438	finally
    //   319	324	438	finally
    //   327	331	438	finally
    //   334	338	438	finally
    //   341	349	438	finally
    //   352	358	438	finally
    //   361	370	438	finally
    //   373	376	438	finally
    //   379	383	438	finally
    //   386	391	438	finally
    //   394	399	438	finally
    //   402	411	438	finally
    //   414	421	438	finally
    //   424	433	438	finally
    //   436	438	438	finally
  }
  
  public static Object readline(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IOException {
    Global global = getInstance(paramFunction);
    return (paramArrayOfObject.length > 0) ? global.console.readLine(Context.toString(paramArrayOfObject[0])) : global.console.readLine();
  }
  
  static RuntimeException reportRuntimeError(String paramString) {
    return (RuntimeException)Context.reportRuntimeError(ToolErrorReporter.getMessage(paramString));
  }
  
  static RuntimeException reportRuntimeError(String paramString1, String paramString2) {
    return (RuntimeException)Context.reportRuntimeError(ToolErrorReporter.getMessage(paramString1, paramString2));
  }
  
  public static Object runCommand(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IOException {
    int i = paramArrayOfObject.length;
    if (i != 0 && (i != 1 || !(paramArrayOfObject[0] instanceof Scriptable))) {
      Object object1;
      Object object6;
      Object object7;
      String[] arrayOfString2;
      InputStream inputStream;
      Object object2 = null;
      Object object3 = null;
      Object object4 = null;
      Object object5 = null;
      ByteArrayOutputStream byteArrayOutputStream1 = null;
      ByteArrayOutputStream byteArrayOutputStream2 = null;
      String[] arrayOfString1 = null;
      if (paramArrayOfObject[i - 1] instanceof Scriptable) {
        Object object9;
        object2 = paramArrayOfObject[i - 1];
        i--;
        object6 = ScriptableObject.getProperty((Scriptable)object2, "env");
        if (object6 != Scriptable.NOT_FOUND) {
          if (object6 == null) {
            arrayOfString1 = new String[0];
            object3 = null;
          } else if (object6 instanceof Scriptable) {
            Scriptable scriptable = (Scriptable)object6;
            object6 = ScriptableObject.getPropertyIds(scriptable);
            object4 = new String[object6.length];
            for (j = 0; j != object6.length; j++) {
              String str;
              object9 = object6[j];
              if (object9 instanceof String) {
                str = (String)object9;
                object9 = ScriptableObject.getProperty(scriptable, str);
              } else {
                int k = ((Number)object9).intValue();
                str = Integer.toString(k);
                object9 = ScriptableObject.getProperty(scriptable, k);
              } 
              Object object = object9;
              if (object9 == ScriptableObject.NOT_FOUND)
                object = Undefined.instance; 
              object9 = new StringBuilder();
              object9.append(str);
              object9.append('=');
              object9.append(ScriptRuntime.toString(object));
              object4[j] = object9.toString();
            } 
            arrayOfString1 = (String[])object4;
          } else {
            throw reportRuntimeError("msg.runCommand.bad.env");
          } 
        } else {
          object3 = null;
        } 
        inputStream = null;
        object6 = ScriptableObject.getProperty((Scriptable)object2, "dir");
        if (object6 != Scriptable.NOT_FOUND) {
          File file = new File(ScriptRuntime.toString(object6));
        } else {
          object4 = null;
        } 
        object6 = ScriptableObject.getProperty((Scriptable)object2, "input");
        if (object6 != Scriptable.NOT_FOUND)
          inputStream = toInputStream(object6); 
        Object object10 = ScriptableObject.getProperty((Scriptable)object2, "output");
        if (object10 != Scriptable.NOT_FOUND) {
          object6 = toOutputStream(object10);
          object3 = object6;
          object7 = byteArrayOutputStream1;
          if (object6 == null) {
            object7 = new ByteArrayOutputStream();
            object3 = object7;
          } 
        } else {
          object7 = byteArrayOutputStream1;
        } 
        Object object8 = ScriptableObject.getProperty((Scriptable)object2, "err");
        object6 = object5;
        arrayOfString2 = (String[])byteArrayOutputStream2;
        if (object8 != Scriptable.NOT_FOUND) {
          object6 = toOutputStream(object8);
          if (object6 == null) {
            arrayOfString2 = (String[])new ByteArrayOutputStream();
            object6 = arrayOfString2;
          } else {
            arrayOfString2 = (String[])byteArrayOutputStream2;
          } 
        } 
        object5 = ScriptableObject.getProperty((Scriptable)object2, "args");
        String[] arrayOfString = (String[])object4;
        if (object5 != Scriptable.NOT_FOUND) {
          Object[] arrayOfObject = paramContext.getElements(Context.toObject(object5, getTopLevelScope(paramScriptable)));
          object5 = arrayOfString1;
          object9 = object7;
          Scriptable scriptable = (Scriptable)object2;
          object2 = object3;
          Object object = object6;
          object3 = arrayOfString2;
          object6 = object10;
          object7 = object8;
          arrayOfString2 = (String[])object5;
        } else {
          object5 = object9;
          paramScriptable = null;
          object9 = object7;
          Object object = object2;
          object2 = object3;
          object4 = object6;
          object3 = arrayOfString2;
          object6 = object10;
          object7 = object8;
          arrayOfString2 = (String[])object5;
        } 
      } else {
        byteArrayOutputStream2 = null;
        inputStream = null;
        arrayOfString2 = null;
        object3 = null;
        paramScriptable = null;
        object7 = null;
        arrayOfString1 = null;
        object6 = null;
        paramContext = null;
      } 
      Global global = getInstance(paramFunction);
      if (object2 == null) {
        if (global != null) {
          object1 = global.getOut();
        } else {
          object1 = System.out;
        } 
        object2 = object1;
      } 
      if (object4 == null) {
        if (global != null) {
          object1 = global.getErr();
        } else {
          object1 = System.err;
        } 
      } else {
        object1 = object4;
      } 
      if (paramScriptable == null) {
        j = i;
      } else {
        j = paramScriptable.length + i;
      } 
      object4 = new String[j];
      int j;
      for (j = 0; j != i; j++)
        object4[j] = ScriptRuntime.toString(paramArrayOfObject[j]); 
      if (paramScriptable != null)
        for (j = 0; j != paramScriptable.length; j++)
          object4[i + j] = ScriptRuntime.toString(paramScriptable[j]);  
      i = runProcess((String[])object4, arrayOfString2, (File)byteArrayOutputStream2, inputStream, (OutputStream)object2, (OutputStream)object1);
      if (arrayOfString1 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ScriptRuntime.toString(object6));
        stringBuilder.append(arrayOfString1.toString());
        ScriptableObject.putProperty((Scriptable)paramContext, "output", stringBuilder.toString());
      } 
      if (object3 != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ScriptRuntime.toString(object7));
        stringBuilder.append(object3.toString());
        ScriptableObject.putProperty((Scriptable)paramContext, "err", stringBuilder.toString());
      } 
      return new Integer(i);
    } 
    throw reportRuntimeError("msg.runCommand.bad.args");
  }
  
  private static int runProcess(String[] paramArrayOfString1, String[] paramArrayOfString2, File paramFile, InputStream paramInputStream, OutputStream paramOutputStream1, OutputStream paramOutputStream2) throws IOException {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 17
    //   4: invokestatic getRuntime : ()Ljava/lang/Runtime;
    //   7: aload_0
    //   8: aconst_null
    //   9: aload_2
    //   10: invokevirtual exec : ([Ljava/lang/String;[Ljava/lang/String;Ljava/io/File;)Ljava/lang/Process;
    //   13: astore_0
    //   14: goto -> 27
    //   17: invokestatic getRuntime : ()Ljava/lang/Runtime;
    //   20: aload_0
    //   21: aload_1
    //   22: aload_2
    //   23: invokevirtual exec : ([Ljava/lang/String;[Ljava/lang/String;Ljava/io/File;)Ljava/lang/Process;
    //   26: astore_0
    //   27: aconst_null
    //   28: astore_1
    //   29: aload_3
    //   30: ifnull -> 54
    //   33: new com/trendmicro/hippo/tools/shell/PipeThread
    //   36: astore_1
    //   37: aload_1
    //   38: iconst_0
    //   39: aload_3
    //   40: aload_0
    //   41: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   44: invokespecial <init> : (ZLjava/io/InputStream;Ljava/io/OutputStream;)V
    //   47: aload_1
    //   48: invokevirtual start : ()V
    //   51: goto -> 61
    //   54: aload_0
    //   55: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   58: invokevirtual close : ()V
    //   61: aconst_null
    //   62: astore_2
    //   63: aload #4
    //   65: ifnull -> 90
    //   68: new com/trendmicro/hippo/tools/shell/PipeThread
    //   71: astore_2
    //   72: aload_2
    //   73: iconst_1
    //   74: aload_0
    //   75: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   78: aload #4
    //   80: invokespecial <init> : (ZLjava/io/InputStream;Ljava/io/OutputStream;)V
    //   83: aload_2
    //   84: invokevirtual start : ()V
    //   87: goto -> 97
    //   90: aload_0
    //   91: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   94: invokevirtual close : ()V
    //   97: aconst_null
    //   98: astore_3
    //   99: aload #5
    //   101: ifnull -> 126
    //   104: new com/trendmicro/hippo/tools/shell/PipeThread
    //   107: astore_3
    //   108: aload_3
    //   109: iconst_1
    //   110: aload_0
    //   111: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   114: aload #5
    //   116: invokespecial <init> : (ZLjava/io/InputStream;Ljava/io/OutputStream;)V
    //   119: aload_3
    //   120: invokevirtual start : ()V
    //   123: goto -> 133
    //   126: aload_0
    //   127: invokevirtual getErrorStream : ()Ljava/io/InputStream;
    //   130: invokevirtual close : ()V
    //   133: aload_0
    //   134: invokevirtual waitFor : ()I
    //   137: pop
    //   138: aload_2
    //   139: ifnull -> 146
    //   142: aload_2
    //   143: invokevirtual join : ()V
    //   146: aload_1
    //   147: ifnull -> 154
    //   150: aload_1
    //   151: invokevirtual join : ()V
    //   154: aload_3
    //   155: ifnull -> 162
    //   158: aload_3
    //   159: invokevirtual join : ()V
    //   162: aload_0
    //   163: invokevirtual exitValue : ()I
    //   166: istore #6
    //   168: aload_0
    //   169: invokevirtual destroy : ()V
    //   172: iload #6
    //   174: ireturn
    //   175: astore #4
    //   177: goto -> 133
    //   180: astore_1
    //   181: aload_0
    //   182: invokevirtual destroy : ()V
    //   185: aload_1
    //   186: athrow
    // Exception table:
    //   from	to	target	type
    //   33	47	180	finally
    //   47	51	180	finally
    //   54	61	180	finally
    //   68	83	180	finally
    //   83	87	180	finally
    //   90	97	180	finally
    //   104	119	180	finally
    //   119	123	180	finally
    //   126	133	180	finally
    //   133	138	175	java/lang/InterruptedException
    //   133	138	180	finally
    //   142	146	175	java/lang/InterruptedException
    //   142	146	180	finally
    //   150	154	175	java/lang/InterruptedException
    //   150	154	180	finally
    //   158	162	175	java/lang/InterruptedException
    //   158	162	180	finally
    //   162	168	180	finally
  }
  
  public static void seal(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    byte b;
    for (b = 0; b != paramArrayOfObject.length; b++) {
      Object object = paramArrayOfObject[b];
      if (!(object instanceof ScriptableObject) || object == Undefined.instance) {
        if (!(object instanceof Scriptable) || object == Undefined.instance)
          throw reportRuntimeError("msg.shell.seal.not.object"); 
        throw reportRuntimeError("msg.shell.seal.not.scriptable");
      } 
    } 
    for (b = 0; b != paramArrayOfObject.length; b++)
      ((ScriptableObject)paramArrayOfObject[b]).sealObject(); 
  }
  
  public static void serialize(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) throws IOException {
    if (paramArrayOfObject.length >= 2) {
      Object object = paramArrayOfObject[0];
      ScriptableOutputStream scriptableOutputStream = new ScriptableOutputStream(new FileOutputStream(Context.toString(paramArrayOfObject[1])), ScriptableObject.getTopLevelScope(paramScriptable));
      scriptableOutputStream.writeObject(object);
      scriptableOutputStream.close();
      return;
    } 
    throw Context.reportRuntimeError("Expected an object to serialize and a filename to write the serialization to");
  }
  
  public static Object spawn(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    Runner runner;
    Scriptable scriptable = paramFunction.getParentScope();
    if (paramArrayOfObject.length != 0 && paramArrayOfObject[0] instanceof Function) {
      Object[] arrayOfObject1;
      paramFunction = null;
      Function function = paramFunction;
      if (paramArrayOfObject.length > 1) {
        function = paramFunction;
        if (paramArrayOfObject[1] instanceof Scriptable)
          arrayOfObject1 = paramContext.getElements((Scriptable)paramArrayOfObject[1]); 
      } 
      Object[] arrayOfObject2 = arrayOfObject1;
      if (arrayOfObject1 == null)
        arrayOfObject2 = ScriptRuntime.emptyArgs; 
      runner = new Runner(scriptable, (Function)paramArrayOfObject[0], arrayOfObject2);
    } else {
      if (paramArrayOfObject.length != 0 && paramArrayOfObject[0] instanceof Script) {
        runner = new Runner(scriptable, (Script)paramArrayOfObject[0]);
        runner.factory = paramContext.getFactory();
        thread = new Thread(runner);
        thread.start();
        return thread;
      } 
      throw reportRuntimeError("msg.spawn.args");
    } 
    runner.factory = thread.getFactory();
    Thread thread = new Thread(runner);
    thread.start();
    return thread;
  }
  
  public static Object sync(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    if (paramArrayOfObject.length >= 1 && paramArrayOfObject.length <= 2 && paramArrayOfObject[0] instanceof Function) {
      paramScriptable = null;
      Object object = paramScriptable;
      if (paramArrayOfObject.length == 2) {
        object = paramScriptable;
        if (paramArrayOfObject[1] != Undefined.instance)
          object = paramArrayOfObject[1]; 
      } 
      return new Synchronizer((Scriptable)paramArrayOfObject[0], object);
    } 
    throw reportRuntimeError("msg.sync.args");
  }
  
  private static InputStream toInputStream(Object paramObject) throws IOException {
    InputStream inputStream1 = null;
    String str1 = null;
    InputStream inputStream2 = inputStream1;
    String str2 = str1;
    if (paramObject instanceof Wrapper) {
      Object object = ((Wrapper)paramObject).unwrap();
      if (object instanceof InputStream) {
        inputStream2 = (InputStream)object;
        str2 = str1;
      } else if (object instanceof byte[]) {
        inputStream2 = new ByteArrayInputStream((byte[])object);
        str2 = str1;
      } else if (object instanceof Reader) {
        str2 = readReader((Reader)object);
        inputStream2 = inputStream1;
      } else {
        inputStream2 = inputStream1;
        str2 = str1;
        if (object instanceof char[]) {
          str2 = new String((char[])object);
          inputStream2 = inputStream1;
        } 
      } 
    } 
    inputStream1 = inputStream2;
    if (inputStream2 == null) {
      String str = str2;
      if (str2 == null)
        str = ScriptRuntime.toString(paramObject); 
      inputStream1 = new ByteArrayInputStream(str.getBytes());
    } 
    return inputStream1;
  }
  
  private static OutputStream toOutputStream(Object paramObject) {
    OutputStream outputStream1 = null;
    OutputStream outputStream2 = outputStream1;
    if (paramObject instanceof Wrapper) {
      paramObject = ((Wrapper)paramObject).unwrap();
      outputStream2 = outputStream1;
      if (paramObject instanceof OutputStream)
        outputStream2 = (OutputStream)paramObject; 
    } 
    return outputStream2;
  }
  
  public static Object toint32(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    Object object;
    if (paramArrayOfObject.length != 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    return (object instanceof Integer) ? object : ScriptRuntime.wrapInt(ScriptRuntime.toInt32(object));
  }
  
  public static double version(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    if (paramArrayOfObject.length > 0)
      paramContext.setLanguageVersion((int)Context.toNumber(paramArrayOfObject[0])); 
    return paramContext.getLanguageVersion();
  }
  
  public static Object write(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, Function paramFunction) {
    return doPrint(paramArrayOfObject, paramFunction, false);
  }
  
  public ShellConsole getConsole(Charset paramCharset) {
    if (!loadJLine(paramCharset))
      this.console = ShellConsole.getConsole(getIn(), getErr(), paramCharset); 
    return this.console;
  }
  
  public PrintStream getErr() {
    PrintStream printStream1 = this.errStream;
    PrintStream printStream2 = printStream1;
    if (printStream1 == null)
      printStream2 = System.err; 
    return printStream2;
  }
  
  public InputStream getIn() {
    if (this.inStream == null && !this.attemptedJLineLoad && loadJLine(Charset.defaultCharset()))
      this.inStream = this.console.getIn(); 
    InputStream inputStream1 = this.inStream;
    InputStream inputStream2 = inputStream1;
    if (inputStream1 == null)
      inputStream2 = System.in; 
    return inputStream2;
  }
  
  public PrintStream getOut() {
    PrintStream printStream1 = this.outStream;
    PrintStream printStream2 = printStream1;
    if (printStream1 == null)
      printStream2 = System.out; 
    return printStream2;
  }
  
  public String[] getPrompts(Context paramContext) {
    if (ScriptableObject.hasProperty((Scriptable)this, "prompts")) {
      Object object = ScriptableObject.getProperty((Scriptable)this, "prompts");
      if (object instanceof Scriptable) {
        Scriptable scriptable = (Scriptable)object;
        if (ScriptableObject.hasProperty(scriptable, 0) && ScriptableObject.hasProperty(scriptable, 1)) {
          Object object1 = ScriptableObject.getProperty(scriptable, 0);
          object = object1;
          if (object1 instanceof Function)
            object = ((Function)object1).call(paramContext, (Scriptable)this, scriptable, new Object[0]); 
          this.prompts[0] = Context.toString(object);
          object1 = ScriptableObject.getProperty(scriptable, 1);
          object = object1;
          if (object1 instanceof Function)
            object = ((Function)object1).call(paramContext, (Scriptable)this, scriptable, new Object[0]); 
          this.prompts[1] = Context.toString(object);
        } 
      } 
    } 
    return this.prompts;
  }
  
  public void init(Context paramContext) {
    initStandardObjects(paramContext, this.sealedStdLib);
    defineFunctionProperties(new String[] { 
          "defineClass", "deserialize", "doctest", "gc", "help", "load", "loadClass", "print", "quit", "readline", 
          "readFile", "readUrl", "runCommand", "seal", "serialize", "spawn", "sync", "toint32", "version", "write" }, Global.class, 2);
    Environment.defineClass((ScriptableObject)this);
    defineProperty("environment", new Environment((ScriptableObject)this), 2);
    NativeArray nativeArray = (NativeArray)paramContext.newArray((Scriptable)this, 0);
    this.history = nativeArray;
    defineProperty("history", nativeArray, 2);
    this.initialized = true;
  }
  
  public void init(ContextFactory paramContextFactory) {
    paramContextFactory.call(paramContext -> {
          init(paramContext);
          return null;
        });
  }
  
  public void initQuitAction(QuitAction paramQuitAction) {
    if (paramQuitAction != null) {
      if (this.quitAction == null) {
        this.quitAction = paramQuitAction;
        return;
      } 
      throw new IllegalArgumentException("The method is once-call.");
    } 
    throw new IllegalArgumentException("quitAction is null");
  }
  
  public Require installRequire(Context paramContext, List<String> paramList, boolean paramBoolean) {
    RequireBuilder requireBuilder = new RequireBuilder();
    requireBuilder.setSandboxed(paramBoolean);
    ArrayList<URI> arrayList = new ArrayList();
    if (paramList != null)
      for (String str : paramList) {
        try {
          URI uRI2 = new URI();
          this(str);
          URI uRI1 = uRI2;
          if (!uRI2.isAbsolute()) {
            File file = new File();
            this(str);
            uRI1 = file.toURI().resolve("");
          } 
          uRI2 = uRI1;
          if (!uRI1.toString().endsWith("/")) {
            uRI2 = new URI();
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append(uRI1);
            stringBuilder.append("/");
            this(stringBuilder.toString());
          } 
          arrayList.add(uRI2);
        } catch (URISyntaxException uRISyntaxException) {
          throw new RuntimeException(uRISyntaxException);
        } 
      }  
    requireBuilder.setModuleScriptProvider((ModuleScriptProvider)new SoftCachingModuleScriptProvider((ModuleSourceProvider)new UrlModuleSourceProvider(arrayList, null)));
    Require require = requireBuilder.createRequire((Context)uRISyntaxException, (Scriptable)this);
    require.install((Scriptable)this);
    return require;
  }
  
  public boolean isInitialized() {
    return this.initialized;
  }
  
  public int runDoctest(Context paramContext, Scriptable paramScriptable, String paramString1, String paramString2, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: new java/util/HashMap
    //   4: dup
    //   5: invokespecial <init> : ()V
    //   8: putfield doctestCanonicalizations : Ljava/util/HashMap;
    //   11: aload_3
    //   12: ldc_w '\\r\\n?|\\n'
    //   15: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   18: astore_3
    //   19: aload_0
    //   20: getfield prompts : [Ljava/lang/String;
    //   23: iconst_0
    //   24: aaload
    //   25: invokevirtual trim : ()Ljava/lang/String;
    //   28: astore #6
    //   30: aload_0
    //   31: getfield prompts : [Ljava/lang/String;
    //   34: iconst_1
    //   35: aaload
    //   36: invokevirtual trim : ()Ljava/lang/String;
    //   39: astore #7
    //   41: iconst_0
    //   42: istore #8
    //   44: iconst_0
    //   45: istore #9
    //   47: iload #8
    //   49: istore #10
    //   51: iload #9
    //   53: istore #11
    //   55: aload_3
    //   56: astore #12
    //   58: iload #9
    //   60: aload_3
    //   61: arraylength
    //   62: if_icmpge -> 97
    //   65: iload #8
    //   67: istore #10
    //   69: iload #9
    //   71: istore #11
    //   73: aload_3
    //   74: astore #12
    //   76: aload_3
    //   77: iload #9
    //   79: aaload
    //   80: invokevirtual trim : ()Ljava/lang/String;
    //   83: aload #6
    //   85: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   88: ifne -> 97
    //   91: iinc #9, 1
    //   94: goto -> 47
    //   97: iload #11
    //   99: aload #12
    //   101: arraylength
    //   102: if_icmpge -> 777
    //   105: aload #12
    //   107: iload #11
    //   109: aaload
    //   110: invokevirtual trim : ()Ljava/lang/String;
    //   113: aload #6
    //   115: invokevirtual length : ()I
    //   118: invokevirtual substring : (I)Ljava/lang/String;
    //   121: astore #13
    //   123: new java/lang/StringBuilder
    //   126: dup
    //   127: invokespecial <init> : ()V
    //   130: astore_3
    //   131: aload_3
    //   132: aload #13
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: aload_3
    //   139: ldc '\\n'
    //   141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: pop
    //   145: aload_3
    //   146: invokevirtual toString : ()Ljava/lang/String;
    //   149: astore #13
    //   151: iload #11
    //   153: iconst_1
    //   154: iadd
    //   155: istore #9
    //   157: iload #9
    //   159: aload #12
    //   161: arraylength
    //   162: if_icmpge -> 259
    //   165: aload #12
    //   167: iload #9
    //   169: aaload
    //   170: invokevirtual trim : ()Ljava/lang/String;
    //   173: aload #7
    //   175: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   178: ifeq -> 259
    //   181: new java/lang/StringBuilder
    //   184: dup
    //   185: invokespecial <init> : ()V
    //   188: astore_3
    //   189: aload_3
    //   190: aload #13
    //   192: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   195: pop
    //   196: aload_3
    //   197: aload #12
    //   199: iload #9
    //   201: aaload
    //   202: invokevirtual trim : ()Ljava/lang/String;
    //   205: aload #7
    //   207: invokevirtual length : ()I
    //   210: invokevirtual substring : (I)Ljava/lang/String;
    //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload_3
    //   218: invokevirtual toString : ()Ljava/lang/String;
    //   221: astore_3
    //   222: new java/lang/StringBuilder
    //   225: dup
    //   226: invokespecial <init> : ()V
    //   229: astore #13
    //   231: aload #13
    //   233: aload_3
    //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: pop
    //   238: aload #13
    //   240: ldc '\\n'
    //   242: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: pop
    //   246: aload #13
    //   248: invokevirtual toString : ()Ljava/lang/String;
    //   251: astore #13
    //   253: iinc #9, 1
    //   256: goto -> 157
    //   259: ldc_w ''
    //   262: astore #14
    //   264: iload #9
    //   266: aload #12
    //   268: arraylength
    //   269: if_icmpge -> 332
    //   272: aload #12
    //   274: iload #9
    //   276: aaload
    //   277: invokevirtual trim : ()Ljava/lang/String;
    //   280: aload #6
    //   282: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   285: ifne -> 332
    //   288: new java/lang/StringBuilder
    //   291: dup
    //   292: invokespecial <init> : ()V
    //   295: astore_3
    //   296: aload_3
    //   297: aload #14
    //   299: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: pop
    //   303: aload_3
    //   304: aload #12
    //   306: iload #9
    //   308: aaload
    //   309: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   312: pop
    //   313: aload_3
    //   314: ldc '\\n'
    //   316: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: pop
    //   320: aload_3
    //   321: invokevirtual toString : ()Ljava/lang/String;
    //   324: astore #14
    //   326: iinc #9, 1
    //   329: goto -> 264
    //   332: aload_0
    //   333: invokevirtual getOut : ()Ljava/io/PrintStream;
    //   336: astore #15
    //   338: aload_0
    //   339: invokevirtual getErr : ()Ljava/io/PrintStream;
    //   342: astore #16
    //   344: new java/io/ByteArrayOutputStream
    //   347: dup
    //   348: invokespecial <init> : ()V
    //   351: astore #17
    //   353: new java/io/ByteArrayOutputStream
    //   356: dup
    //   357: invokespecial <init> : ()V
    //   360: astore #18
    //   362: aload_0
    //   363: new java/io/PrintStream
    //   366: dup
    //   367: aload #17
    //   369: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   372: invokevirtual setOut : (Ljava/io/PrintStream;)V
    //   375: aload_0
    //   376: new java/io/PrintStream
    //   379: dup
    //   380: aload #18
    //   382: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   385: invokevirtual setErr : (Ljava/io/PrintStream;)V
    //   388: ldc_w ''
    //   391: astore #19
    //   393: aload_1
    //   394: invokevirtual getErrorReporter : ()Lcom/trendmicro/hippo/ErrorReporter;
    //   397: astore #20
    //   399: aload_1
    //   400: new com/trendmicro/hippo/tools/ToolErrorReporter
    //   403: dup
    //   404: iconst_0
    //   405: aload_0
    //   406: invokevirtual getErr : ()Ljava/io/PrintStream;
    //   409: invokespecial <init> : (ZLjava/io/PrintStream;)V
    //   412: invokevirtual setErrorReporter : (Lcom/trendmicro/hippo/ErrorReporter;)Lcom/trendmicro/hippo/ErrorReporter;
    //   415: pop
    //   416: aload_1
    //   417: aload_2
    //   418: aload #13
    //   420: ldc_w 'doctest input'
    //   423: iconst_1
    //   424: aconst_null
    //   425: invokevirtual evaluateString : (Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/Object;
    //   428: astore #21
    //   430: invokestatic getUndefinedValue : ()Ljava/lang/Object;
    //   433: astore #22
    //   435: aload #19
    //   437: astore_3
    //   438: aload #21
    //   440: aload #22
    //   442: if_acmpeq -> 487
    //   445: aload #21
    //   447: instanceof com/trendmicro/hippo/Function
    //   450: ifeq -> 470
    //   453: aload #19
    //   455: astore_3
    //   456: aload #13
    //   458: invokevirtual trim : ()Ljava/lang/String;
    //   461: ldc_w 'function'
    //   464: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   467: ifne -> 487
    //   470: aload #21
    //   472: invokestatic toString : (Ljava/lang/Object;)Ljava/lang/String;
    //   475: astore_3
    //   476: goto -> 487
    //   479: astore_2
    //   480: goto -> 715
    //   483: astore_3
    //   484: goto -> 526
    //   487: aload_0
    //   488: aload #15
    //   490: invokevirtual setOut : (Ljava/io/PrintStream;)V
    //   493: aload_0
    //   494: aload #16
    //   496: invokevirtual setErr : (Ljava/io/PrintStream;)V
    //   499: aload_1
    //   500: aload #20
    //   502: invokevirtual setErrorReporter : (Lcom/trendmicro/hippo/ErrorReporter;)Lcom/trendmicro/hippo/ErrorReporter;
    //   505: pop
    //   506: new java/lang/StringBuilder
    //   509: dup
    //   510: invokespecial <init> : ()V
    //   513: astore #19
    //   515: goto -> 569
    //   518: astore_2
    //   519: goto -> 715
    //   522: astore_3
    //   523: goto -> 526
    //   526: aload_1
    //   527: invokevirtual getErrorReporter : ()Lcom/trendmicro/hippo/ErrorReporter;
    //   530: aload_3
    //   531: invokestatic reportException : (Lcom/trendmicro/hippo/ErrorReporter;Lcom/trendmicro/hippo/HippoException;)V
    //   534: aload_0
    //   535: aload #15
    //   537: invokevirtual setOut : (Ljava/io/PrintStream;)V
    //   540: aload_0
    //   541: aload #16
    //   543: invokevirtual setErr : (Ljava/io/PrintStream;)V
    //   546: aload_1
    //   547: aload #20
    //   549: invokevirtual setErrorReporter : (Lcom/trendmicro/hippo/ErrorReporter;)Lcom/trendmicro/hippo/ErrorReporter;
    //   552: pop
    //   553: new java/lang/StringBuilder
    //   556: dup
    //   557: invokespecial <init> : ()V
    //   560: astore #20
    //   562: aload #19
    //   564: astore_3
    //   565: aload #20
    //   567: astore #19
    //   569: aload #19
    //   571: aload_3
    //   572: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   575: pop
    //   576: aload #19
    //   578: aload #18
    //   580: invokevirtual toString : ()Ljava/lang/String;
    //   583: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   586: pop
    //   587: aload #19
    //   589: aload #17
    //   591: invokevirtual toString : ()Ljava/lang/String;
    //   594: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   597: pop
    //   598: aload #19
    //   600: invokevirtual toString : ()Ljava/lang/String;
    //   603: astore_3
    //   604: aload_0
    //   605: aload #14
    //   607: aload_3
    //   608: invokespecial doctestOutputMatches : (Ljava/lang/String;Ljava/lang/String;)Z
    //   611: ifne -> 704
    //   614: new java/lang/StringBuilder
    //   617: dup
    //   618: invokespecial <init> : ()V
    //   621: astore_1
    //   622: aload_1
    //   623: ldc_w 'doctest failure running:\\n'
    //   626: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   629: pop
    //   630: aload_1
    //   631: aload #13
    //   633: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   636: pop
    //   637: aload_1
    //   638: ldc_w 'expected: '
    //   641: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: pop
    //   645: aload_1
    //   646: aload #14
    //   648: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   651: pop
    //   652: aload_1
    //   653: ldc_w 'actual: '
    //   656: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   659: pop
    //   660: aload_1
    //   661: aload_3
    //   662: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   665: pop
    //   666: aload_1
    //   667: ldc '\\n'
    //   669: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   672: pop
    //   673: aload_1
    //   674: invokevirtual toString : ()Ljava/lang/String;
    //   677: astore_1
    //   678: aload #4
    //   680: ifnull -> 699
    //   683: aload_1
    //   684: aload #4
    //   686: iload #5
    //   688: iload #9
    //   690: iadd
    //   691: iconst_1
    //   692: isub
    //   693: aconst_null
    //   694: iconst_0
    //   695: invokestatic reportRuntimeError : (Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;I)Lcom/trendmicro/hippo/EvaluatorException;
    //   698: athrow
    //   699: aload_1
    //   700: invokestatic reportRuntimeError : (Ljava/lang/String;)Lcom/trendmicro/hippo/EvaluatorException;
    //   703: athrow
    //   704: iinc #10, 1
    //   707: iload #9
    //   709: istore #11
    //   711: goto -> 97
    //   714: astore_2
    //   715: aload_0
    //   716: aload #15
    //   718: invokevirtual setOut : (Ljava/io/PrintStream;)V
    //   721: aload_0
    //   722: aload #16
    //   724: invokevirtual setErr : (Ljava/io/PrintStream;)V
    //   727: aload_1
    //   728: aload #20
    //   730: invokevirtual setErrorReporter : (Lcom/trendmicro/hippo/ErrorReporter;)Lcom/trendmicro/hippo/ErrorReporter;
    //   733: pop
    //   734: new java/lang/StringBuilder
    //   737: dup
    //   738: invokespecial <init> : ()V
    //   741: astore_1
    //   742: aload_1
    //   743: ldc_w ''
    //   746: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   749: pop
    //   750: aload_1
    //   751: aload #18
    //   753: invokevirtual toString : ()Ljava/lang/String;
    //   756: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   759: pop
    //   760: aload_1
    //   761: aload #17
    //   763: invokevirtual toString : ()Ljava/lang/String;
    //   766: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   769: pop
    //   770: aload_1
    //   771: invokevirtual toString : ()Ljava/lang/String;
    //   774: pop
    //   775: aload_2
    //   776: athrow
    //   777: iload #10
    //   779: ireturn
    // Exception table:
    //   from	to	target	type
    //   416	435	522	com/trendmicro/hippo/HippoException
    //   416	435	518	finally
    //   445	453	483	com/trendmicro/hippo/HippoException
    //   445	453	479	finally
    //   456	470	483	com/trendmicro/hippo/HippoException
    //   456	470	479	finally
    //   470	476	483	com/trendmicro/hippo/HippoException
    //   470	476	479	finally
    //   526	534	714	finally
  }
  
  public void setErr(PrintStream paramPrintStream) {
    this.errStream = paramPrintStream;
  }
  
  public void setIn(InputStream paramInputStream) {
    this.inStream = paramInputStream;
  }
  
  public void setOut(PrintStream paramPrintStream) {
    this.outStream = paramPrintStream;
  }
  
  public void setSealedStdLib(boolean paramBoolean) {
    this.sealedStdLib = paramBoolean;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/Global.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */