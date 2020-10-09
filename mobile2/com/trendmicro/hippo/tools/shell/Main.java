package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextAction;
import com.trendmicro.hippo.ErrorReporter;
import com.trendmicro.hippo.GeneratedClassLoader;
import com.trendmicro.hippo.HippoException;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.SecurityController;
import com.trendmicro.hippo.commonjs.module.ModuleScope;
import com.trendmicro.hippo.commonjs.module.Require;
import com.trendmicro.hippo.tools.SourceReader;
import com.trendmicro.hippo.tools.ToolErrorReporter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
  private static final int EXITCODE_FILE_NOT_FOUND = 4;
  
  private static final int EXITCODE_RUNTIME_ERROR = 3;
  
  protected static ToolErrorReporter errorReporter;
  
  protected static int exitCode;
  
  static List<String> fileList;
  
  public static Global global;
  
  static String mainModule;
  
  static List<String> modulePath;
  
  static boolean processStdin;
  
  static Require require;
  
  static boolean sandboxed;
  
  private static final ScriptCache scriptCache;
  
  private static SecurityProxy securityImpl;
  
  public static ShellContextFactory shellContextFactory = new ShellContextFactory();
  
  static boolean useRequire;
  
  static {
    global = new Global();
    exitCode = 0;
    processStdin = true;
    fileList = new ArrayList<>();
    sandboxed = false;
    useRequire = false;
    scriptCache = new ScriptCache(32);
    global.initQuitAction(new IProxy(3));
  }
  
  static void evalInlineScript(Context paramContext, String paramString) {
    try {
      Script script = paramContext.compileString(paramString, "<command>", 1, null);
      if (script != null)
        script.exec(paramContext, getShellScope()); 
    } catch (HippoException hippoException) {
      ToolErrorReporter.reportException(paramContext.getErrorReporter(), hippoException);
      exitCode = 3;
    } catch (VirtualMachineError virtualMachineError) {
      virtualMachineError.printStackTrace();
      Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
      exitCode = 3;
    } 
  }
  
  public static int exec(String[] paramArrayOfString) {
    ToolErrorReporter toolErrorReporter = new ToolErrorReporter(false, global.getErr());
    errorReporter = toolErrorReporter;
    shellContextFactory.setErrorReporter((ErrorReporter)toolErrorReporter);
    paramArrayOfString = processOptions(paramArrayOfString);
    int i = exitCode;
    if (i > 0)
      return i; 
    if (processStdin)
      fileList.add(null); 
    if (!global.initialized)
      global.init(shellContextFactory); 
    IProxy iProxy = new IProxy(1);
    iProxy.args = paramArrayOfString;
    shellContextFactory.call(iProxy);
    return exitCode;
  }
  
  private static byte[] getDigest(Object paramObject) {
    byte[] arrayOfByte = null;
    if (paramObject != null) {
      if (paramObject instanceof String) {
        try {
          arrayOfByte = ((String)paramObject).getBytes("UTF-8");
          paramObject = arrayOfByte;
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          paramObject = ((String)paramObject).getBytes();
        } 
      } else {
        paramObject = paramObject;
      } 
      try {
        arrayOfByte = MessageDigest.getInstance("MD5").digest((byte[])paramObject);
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        throw new RuntimeException(noSuchAlgorithmException);
      } 
    } 
    return arrayOfByte;
  }
  
  public static PrintStream getErr() {
    return getGlobal().getErr();
  }
  
  public static Global getGlobal() {
    return global;
  }
  
  public static InputStream getIn() {
    return getGlobal().getIn();
  }
  
  public static PrintStream getOut() {
    return getGlobal().getOut();
  }
  
  static Scriptable getScope(String paramString) {
    if (useRequire) {
      URI uRI;
      if (paramString == null) {
        uRI = (new File(System.getProperty("user.dir"))).toURI();
      } else if (SourceReader.toUrl((String)uRI) != null) {
        try {
          URI uRI1 = new URI();
          this((String)uRI);
          uRI = uRI1;
        } catch (URISyntaxException uRISyntaxException) {
          uRI = (new File((String)uRI)).toURI();
        } 
      } else {
        uRI = (new File((String)uRI)).toURI();
      } 
      return (Scriptable)new ModuleScope((Scriptable)global, uRI, null);
    } 
    return (Scriptable)global;
  }
  
  static Scriptable getShellScope() {
    return getScope(null);
  }
  
  private static void initJavaPolicySecuritySupport() {
    try {
      SecurityProxy securityProxy = (SecurityProxy)Class.forName("com.trendmicro.hippo.tools.shell.JavaPolicySecurity").newInstance();
      securityImpl = securityProxy;
      SecurityController.initGlobal(securityProxy);
      return;
    } catch (ClassNotFoundException classNotFoundException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InstantiationException instantiationException) {
    
    } catch (LinkageError linkageError) {}
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Can not load security support: ");
    stringBuilder.append(linkageError);
    throw new IllegalStateException(stringBuilder.toString(), linkageError);
  }
  
  private static Script loadCompiledScript(Context paramContext, String paramString, byte[] paramArrayOfbyte, Object paramObject) throws FileNotFoundException {
    Class<?> clazz;
    if (paramArrayOfbyte != null) {
      int i = paramString.lastIndexOf('/');
      if (i < 0) {
        i = 0;
      } else {
        i++;
      } 
      int j = paramString.lastIndexOf('.');
      int k = j;
      if (j < i)
        k = paramString.length(); 
      paramString = paramString.substring(i, k);
      try {
        GeneratedClassLoader generatedClassLoader = SecurityController.createLoader(paramContext.getApplicationClassLoader(), paramObject);
        clazz = generatedClassLoader.defineClass(paramString, paramArrayOfbyte);
        generatedClassLoader.linkClass(clazz);
        if (Script.class.isAssignableFrom(clazz))
          return (Script)clazz.newInstance(); 
        throw Context.reportRuntimeError("msg.must.implement.Script");
      } catch (IllegalAccessException illegalAccessException) {
        Context.reportError(illegalAccessException.toString());
        throw new RuntimeException(illegalAccessException);
      } catch (InstantiationException instantiationException) {
        Context.reportError(instantiationException.toString());
        throw new RuntimeException(instantiationException);
      } 
    } 
    throw new FileNotFoundException(clazz);
  }
  
  public static void main(String[] paramArrayOfString) {
    try {
      if (Boolean.getBoolean("hippo.use_java_policy_security"))
        initJavaPolicySecuritySupport(); 
    } catch (SecurityException securityException) {
      securityException.printStackTrace(System.err);
    } 
    int i = exec(paramArrayOfString);
    if (i != 0)
      System.exit(i); 
  }
  
  public static void processFile(Context paramContext, Scriptable paramScriptable, String paramString) throws IOException {
    SecurityProxy securityProxy = securityImpl;
    if (securityProxy == null) {
      processFileSecure(paramContext, paramScriptable, paramString, null);
    } else {
      securityProxy.callProcessFileSecure(paramContext, paramScriptable, paramString);
    } 
  }
  
  public static void processFileNoThrow(Context paramContext, Scriptable paramScriptable, String paramString) {
    try {
      processFile(paramContext, paramScriptable, paramString);
    } catch (IOException iOException) {
      Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", paramString, iOException.getMessage()));
      exitCode = 4;
    } catch (HippoException hippoException) {
      ToolErrorReporter.reportException(iOException.getErrorReporter(), hippoException);
      exitCode = 3;
    } catch (VirtualMachineError virtualMachineError) {
      virtualMachineError.printStackTrace();
      Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
      exitCode = 3;
    } 
  }
  
  static void processFileSecure(Context paramContext, Scriptable paramScriptable, String paramString, Object paramObject) throws IOException {
    Script script;
    boolean bool = paramString.endsWith(".class");
    Object object = readFileOrUrl(paramString, bool ^ true);
    byte[] arrayOfByte = getDigest(object);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append("_");
    stringBuilder.append(paramContext.getOptimizationLevel());
    String str = stringBuilder.toString();
    ScriptReference scriptReference1 = scriptCache.get(str, arrayOfByte);
    if (scriptReference1 != null) {
      Script script1 = scriptReference1.get();
    } else {
      scriptReference1 = null;
    } 
    ScriptReference scriptReference2 = scriptReference1;
    if (scriptReference1 == null) {
      Script script1;
      if (bool) {
        script1 = loadCompiledScript(paramContext, paramString, (byte[])object, paramObject);
      } else {
        String str2 = (String)object;
        String str1 = str2;
        if (str2.length() > 0) {
          str1 = str2;
          if (str2.charAt(0) == '#') {
            byte b = 1;
            while (true) {
              str1 = str2;
              if (b != str2.length()) {
                char c = str2.charAt(b);
                if (c == '\n' || c == '\r') {
                  str1 = str2.substring(b);
                  break;
                } 
                b++;
                continue;
              } 
              break;
            } 
          } 
        } 
        script1 = paramContext.compileString(str1, (String)script1, 1, paramObject);
      } 
      scriptCache.put(str, arrayOfByte, script1);
      script = script1;
    } 
    if (script != null)
      script.exec(paramContext, paramScriptable); 
  }
  
  static void processFiles(Context paramContext, String[] paramArrayOfString) {
    Object[] arrayOfObject = new Object[paramArrayOfString.length];
    System.arraycopy(paramArrayOfString, 0, arrayOfObject, 0, paramArrayOfString.length);
    Scriptable scriptable = paramContext.newArray((Scriptable)global, arrayOfObject);
    global.defineProperty("arguments", scriptable, 2);
    for (String str : fileList) {
      try {
        processSource(paramContext, str);
      } catch (IOException iOException) {
        Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", str, iOException.getMessage()));
        exitCode = 4;
      } catch (HippoException hippoException) {
        ToolErrorReporter.reportException(paramContext.getErrorReporter(), hippoException);
        exitCode = 3;
      } catch (VirtualMachineError virtualMachineError) {
        virtualMachineError.printStackTrace();
        Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
        exitCode = 3;
      } 
    } 
  }
  
  public static String[] processOptions(String[] paramArrayOfString) {
    IProxy iProxy;
    for (int i = 0;; i++) {
      String str1;
      String[] arrayOfString;
      if (i == paramArrayOfString.length)
        return new String[0]; 
      String str2 = paramArrayOfString[i];
      if (!str2.startsWith("-")) {
        processStdin = false;
        fileList.add(str2);
        mainModule = str2;
        arrayOfString = new String[paramArrayOfString.length - i - 1];
        System.arraycopy(paramArrayOfString, i + 1, arrayOfString, 0, paramArrayOfString.length - i - 1);
        return arrayOfString;
      } 
      if (arrayOfString.equals("-version")) {
        if (++i == paramArrayOfString.length) {
          paramArrayOfString = arrayOfString;
          break;
        } 
        try {
          int j = Integer.parseInt(paramArrayOfString[i]);
          if (!Context.isValidLanguageVersion(j)) {
            str1 = paramArrayOfString[i];
            break;
          } 
          shellContextFactory.setLanguageVersion(j);
        } catch (NumberFormatException numberFormatException) {
          str1 = str1[i];
          break;
        } 
      } else {
        NumberFormatException numberFormatException1;
        if (numberFormatException.equals("-opt") || numberFormatException.equals("-O")) {
          int j = i + 1;
          if (j == str1.length) {
            numberFormatException1 = numberFormatException;
            break;
          } 
          try {
            int k = Integer.parseInt((String)numberFormatException1[j]);
            if (k == -2) {
              i = -1;
            } else {
              i = k;
              if (!Context.isValidOptimizationLevel(k)) {
                numberFormatException1 = numberFormatException1[j];
                break;
              } 
            } 
            shellContextFactory.setOptimizationLevel(i);
            i = j;
            i++;
            continue;
          } catch (NumberFormatException numberFormatException2) {
            numberFormatException1 = numberFormatException1[j];
            break;
          } 
        } 
        if (numberFormatException2.equals("-encoding")) {
          if (++i == numberFormatException1.length) {
            numberFormatException1 = numberFormatException2;
            break;
          } 
          numberFormatException2 = numberFormatException1[i];
          shellContextFactory.setCharacterEncoding((String)numberFormatException2);
        } else if (numberFormatException2.equals("-strict")) {
          shellContextFactory.setStrictMode(true);
          shellContextFactory.setAllowReservedKeywords(false);
          errorReporter.setIsReportingWarnings(true);
        } else if (numberFormatException2.equals("-fatal-warnings")) {
          shellContextFactory.setWarningAsError(true);
        } else {
          IProxy iProxy1;
          if (numberFormatException2.equals("-e")) {
            processStdin = false;
            if (++i == numberFormatException1.length) {
              numberFormatException1 = numberFormatException2;
              break;
            } 
            if (!global.initialized)
              global.init(shellContextFactory); 
            iProxy1 = new IProxy(2);
            iProxy1.scriptText = (String)numberFormatException1[i];
            shellContextFactory.call(iProxy1);
          } else if (iProxy1.equals("-require")) {
            useRequire = true;
          } else if (iProxy1.equals("-sandbox")) {
            sandboxed = true;
            useRequire = true;
          } else if (iProxy1.equals("-modules")) {
            if (++i == numberFormatException1.length) {
              iProxy = iProxy1;
              break;
            } 
            if (modulePath == null)
              modulePath = new ArrayList<>(); 
            modulePath.add(iProxy[i]);
            useRequire = true;
          } else if (iProxy1.equals("-w")) {
            errorReporter.setIsReportingWarnings(true);
          } else if (iProxy1.equals("-f")) {
            processStdin = false;
            if (++i == iProxy.length) {
              iProxy = iProxy1;
              break;
            } 
            if (iProxy[i].equals("-")) {
              fileList.add(null);
            } else {
              fileList.add(iProxy[i]);
              mainModule = (String)iProxy[i];
            } 
          } else if (iProxy1.equals("-sealedlib")) {
            global.setSealedStdLib(true);
          } else if (iProxy1.equals("-debug")) {
            shellContextFactory.setGeneratingDebug(true);
          } else {
            if (iProxy1.equals("-?") || iProxy1.equals("-help")) {
              global.getOut().println(ToolErrorReporter.getMessage("msg.shell.usage", Main.class.getName()));
              exitCode = 1;
              return null;
            } 
            iProxy = iProxy1;
            break;
          } 
        } 
      } 
    } 
    global.getOut().println(ToolErrorReporter.getMessage("msg.shell.invalid", (String)iProxy));
    global.getOut().println(ToolErrorReporter.getMessage("msg.shell.usage", Main.class.getName()));
    exitCode = 1;
    return null;
  }
  
  public static void processSource(Context paramContext, String paramString) throws IOException {
    if (paramString == null || paramString.equals("-")) {
      Charset charset;
      Scriptable scriptable = getShellScope();
      String str = shellContextFactory.getCharacterEncoding();
      if (str != null) {
        charset = Charset.forName(str);
      } else {
        charset = Charset.defaultCharset();
      } 
      ShellConsole shellConsole = global.getConsole(charset);
      if (paramString == null)
        shellConsole.println(paramContext.getImplementationVersion()); 
      byte b = 1;
      boolean bool = false;
      while (!bool) {
        String[] arrayOfString = global.getPrompts(paramContext);
        charset = null;
        if (paramString == null)
          str1 = arrayOfString[0]; 
        shellConsole.flush();
        String str2 = "";
        String str3 = str1;
        String str1 = str2;
        try {
          while (true) {
            str2 = shellConsole.readLine(str3);
            if (str2 == null) {
              bool = true;
              break;
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str1);
            stringBuilder.append(str2);
            stringBuilder.append("\n");
            str1 = stringBuilder.toString();
            b++;
            if (paramContext.stringIsCompilableUnit(str1))
              break; 
            String str4 = arrayOfString[1];
          } 
        } catch (IOException iOException) {
          shellConsole.println(iOException.toString());
        } 
      } 
      shellConsole.println();
      shellConsole.flush();
      return;
    } 
    if (useRequire && paramString.equals(mainModule)) {
      require.requireMain(paramContext, paramString);
    } else {
      processFile(paramContext, getScope(paramString), paramString);
    } 
  }
  
  private static Object readFileOrUrl(String paramString, boolean paramBoolean) throws IOException {
    return SourceReader.readFileOrUrl(paramString, paramBoolean, shellContextFactory.getCharacterEncoding());
  }
  
  public static void setErr(PrintStream paramPrintStream) {
    getGlobal().setErr(paramPrintStream);
  }
  
  public static void setIn(InputStream paramInputStream) {
    getGlobal().setIn(paramInputStream);
  }
  
  public static void setOut(PrintStream paramPrintStream) {
    getGlobal().setOut(paramPrintStream);
  }
  
  private static class IProxy implements ContextAction<Object>, QuitAction {
    private static final int EVAL_INLINE_SCRIPT = 2;
    
    private static final int PROCESS_FILES = 1;
    
    private static final int SYSTEM_EXIT = 3;
    
    String[] args;
    
    String scriptText;
    
    private int type;
    
    IProxy(int param1Int) {
      this.type = param1Int;
    }
    
    public void quit(Context param1Context, int param1Int) {
      if (this.type == 3) {
        System.exit(param1Int);
        return;
      } 
      throw Kit.codeBug();
    }
    
    public Object run(Context param1Context) {
      if (Main.useRequire)
        Main.require = Main.global.installRequire(param1Context, Main.modulePath, Main.sandboxed); 
      int i = this.type;
      if (i == 1) {
        Main.processFiles(param1Context, this.args);
      } else {
        if (i == 2) {
          Main.evalInlineScript(param1Context, this.scriptText);
          return null;
        } 
        throw Kit.codeBug();
      } 
      return null;
    }
  }
  
  static class ScriptCache extends LinkedHashMap<String, ScriptReference> {
    int capacity;
    
    ReferenceQueue<Script> queue;
    
    ScriptCache(int param1Int) {
      super(param1Int + 1, 2.0F, true);
      this.capacity = param1Int;
      this.queue = new ReferenceQueue<>();
    }
    
    Main.ScriptReference get(String param1String, byte[] param1ArrayOfbyte) {
      while (true) {
        Main.ScriptReference scriptReference2 = (Main.ScriptReference)this.queue.poll();
        if (scriptReference2 != null) {
          remove(scriptReference2.path);
          continue;
        } 
        scriptReference2 = get(param1String);
        Main.ScriptReference scriptReference1 = scriptReference2;
        if (scriptReference2 != null) {
          scriptReference1 = scriptReference2;
          if (!Arrays.equals(param1ArrayOfbyte, scriptReference2.digest)) {
            remove(scriptReference2.path);
            scriptReference1 = null;
          } 
        } 
        return scriptReference1;
      } 
    }
    
    void put(String param1String, byte[] param1ArrayOfbyte, Script param1Script) {
      put(param1String, new Main.ScriptReference(param1String, param1ArrayOfbyte, param1Script, this.queue));
    }
    
    protected boolean removeEldestEntry(Map.Entry<String, Main.ScriptReference> param1Entry) {
      boolean bool;
      if (size() > this.capacity) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  static class ScriptReference extends SoftReference<Script> {
    byte[] digest;
    
    String path;
    
    ScriptReference(String param1String, byte[] param1ArrayOfbyte, Script param1Script, ReferenceQueue<Script> param1ReferenceQueue) {
      super(param1Script, param1ReferenceQueue);
      this.path = param1String;
      this.digest = param1ArrayOfbyte;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */