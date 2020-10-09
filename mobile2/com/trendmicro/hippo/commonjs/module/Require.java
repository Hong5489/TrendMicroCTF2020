package com.trendmicro.hippo.commonjs.module;

import com.trendmicro.hippo.BaseFunction;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Require extends BaseFunction {
  private static final ThreadLocal<Map<String, Scriptable>> loadingModuleInterfaces = new ThreadLocal<>();
  
  private static final long serialVersionUID = 1L;
  
  private final Map<String, Scriptable> exportedModuleInterfaces = new ConcurrentHashMap<>();
  
  private final Object loadLock = new Object();
  
  private Scriptable mainExports;
  
  private String mainModuleId = null;
  
  private final ModuleScriptProvider moduleScriptProvider;
  
  private final Scriptable nativeScope;
  
  private final Scriptable paths;
  
  private final Script postExec;
  
  private final Script preExec;
  
  private final boolean sandboxed;
  
  public Require(Context paramContext, Scriptable paramScriptable, ModuleScriptProvider paramModuleScriptProvider, Script paramScript1, Script paramScript2, boolean paramBoolean) {
    this.moduleScriptProvider = paramModuleScriptProvider;
    this.nativeScope = paramScriptable;
    this.sandboxed = paramBoolean;
    this.preExec = paramScript1;
    this.postExec = paramScript2;
    setPrototype(ScriptableObject.getFunctionPrototype(paramScriptable));
    if (!paramBoolean) {
      Scriptable scriptable = paramContext.newArray(paramScriptable, 0);
      this.paths = scriptable;
      defineReadOnlyProperty((ScriptableObject)this, "paths", scriptable);
    } else {
      this.paths = null;
    } 
  }
  
  private static void defineReadOnlyProperty(ScriptableObject paramScriptableObject, String paramString, Object paramObject) {
    ScriptableObject.putProperty((Scriptable)paramScriptableObject, paramString, paramObject);
    paramScriptableObject.setAttributes(paramString, 5);
  }
  
  private Scriptable executeModuleScript(Context paramContext, String paramString, Scriptable paramScriptable, ModuleScript paramModuleScript, boolean paramBoolean) {
    ScriptableObject scriptableObject = (ScriptableObject)paramContext.newObject(this.nativeScope);
    URI uRI1 = paramModuleScript.getUri();
    URI uRI2 = paramModuleScript.getBase();
    defineReadOnlyProperty(scriptableObject, "id", paramString);
    if (!this.sandboxed)
      defineReadOnlyProperty(scriptableObject, "uri", uRI1.toString()); 
    ModuleScope moduleScope = new ModuleScope(this.nativeScope, uRI1, uRI2);
    moduleScope.put("exports", (Scriptable)moduleScope, paramScriptable);
    moduleScope.put("module", (Scriptable)moduleScope, scriptableObject);
    scriptableObject.put("exports", (Scriptable)scriptableObject, paramScriptable);
    install((Scriptable)moduleScope);
    if (paramBoolean)
      defineReadOnlyProperty((ScriptableObject)this, "main", scriptableObject); 
    executeOptionalScript(this.preExec, paramContext, (Scriptable)moduleScope);
    paramModuleScript.getScript().exec(paramContext, (Scriptable)moduleScope);
    executeOptionalScript(this.postExec, paramContext, (Scriptable)moduleScope);
    return ScriptRuntime.toObject(paramContext, this.nativeScope, ScriptableObject.getProperty((Scriptable)scriptableObject, "exports"));
  }
  
  private static void executeOptionalScript(Script paramScript, Context paramContext, Scriptable paramScriptable) {
    if (paramScript != null)
      paramScript.exec(paramContext, paramScriptable); 
  }
  
  private Scriptable getExportedModuleInterface(Context paramContext, String paramString, URI paramURI1, URI paramURI2, boolean paramBoolean) {
    Scriptable scriptable = this.exportedModuleInterfaces.get(paramString);
    if (scriptable != null) {
      if (!paramBoolean)
        return scriptable; 
      throw new IllegalStateException("Attempt to set main module after it was loaded");
    } 
    Map<String, Scriptable> map = loadingModuleInterfaces.get();
    if (map != null) {
      Scriptable scriptable1 = (Scriptable)map.get(paramString);
      if (scriptable1 != null)
        return scriptable1; 
    } 
    Object object = this.loadLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      Scriptable scriptable2 = this.exportedModuleInterfaces.get(paramString);
      if (scriptable2 != null) {
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return scriptable2;
      } 
      ModuleScript moduleScript = getModule(paramContext, paramString, paramURI1, paramURI2);
      if (!this.sandboxed || moduleScript.isSandboxed()) {
        Map<String, Scriptable> map1;
        boolean bool;
        Scriptable scriptable3 = paramContext.newObject(this.nativeScope);
        if (map == null) {
          bool = true;
        } else {
          bool = false;
        } 
        if (bool) {
          try {
            map1 = (Map)new HashMap<>();
            this();
            loadingModuleInterfaces.set(map1);
          } finally {}
        } else {
          map1 = map;
        } 
        try {
          map1.put(paramString, scriptable3);
          try {
            Scriptable scriptable4 = executeModuleScript(paramContext, paramString, scriptable3, moduleScript, paramBoolean);
            if (scriptable3 != scriptable4) {
              map1.put(paramString, scriptable4);
            } else {
              scriptable4 = scriptable3;
            } 
            if (bool) {
              try {
                this.exportedModuleInterfaces.putAll(map1);
                loadingModuleInterfaces.set(null);
                /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
                return scriptable4;
              } finally {}
            } else {
              /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
              return scriptable4;
            } 
          } catch (RuntimeException runtimeException) {
            map1.remove(paramString);
            throw runtimeException;
          } finally {}
        } finally {}
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        throw paramContext;
      } 
      Scriptable scriptable1 = this.nativeScope;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Module \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\" is not contained in sandbox.");
      throw ScriptRuntime.throwError(paramContext, scriptable1, stringBuilder.toString());
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    throw paramContext;
  }
  
  private ModuleScript getModule(Context paramContext, String paramString, URI paramURI1, URI paramURI2) {
    try {
      ModuleScript moduleScript = this.moduleScriptProvider.getModuleScript(paramContext, paramString, paramURI1, paramURI2, this.paths);
      if (moduleScript != null)
        return moduleScript; 
      Scriptable scriptable = this.nativeScope;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Module \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\" not found.");
      throw ScriptRuntime.throwError(paramContext, scriptable, stringBuilder.toString());
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (Exception exception) {
      throw Context.throwAsScriptRuntimeEx(exception);
    } 
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    URI uRI;
    if (paramArrayOfObject != null && paramArrayOfObject.length >= 1) {
      URI uRI1;
      String str = (String)Context.jsToJava(paramArrayOfObject[0], String.class);
      if (str.startsWith("./") || str.startsWith("../")) {
        URI uRI2;
        if (paramScriptable2 instanceof ModuleScope) {
          StringBuilder stringBuilder1;
          ModuleScope moduleScope = (ModuleScope)paramScriptable2;
          URI uRI3 = moduleScope.getBase();
          URI uRI5 = moduleScope.getUri();
          URI uRI4 = uRI5.resolve(str);
          if (uRI3 == null) {
            String str1 = uRI4.toString();
            uRI1 = uRI4;
            uRI4 = uRI3;
            uRI3 = uRI1;
          } else {
            StringBuilder stringBuilder2;
            String str1 = uRI3.relativize(uRI5).resolve((String)uRI1).toString();
            if (str1.charAt(0) == '.') {
              String str2;
              if (!this.sandboxed) {
                str2 = uRI4.toString();
                uRI1 = uRI4;
                uRI4 = uRI3;
                uRI3 = uRI1;
              } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Module \"");
                stringBuilder2.append((String)uRI1);
                stringBuilder2.append("\" is not contained in sandbox.");
                throw ScriptRuntime.throwError(paramContext, str2, stringBuilder2.toString());
              } 
            } else {
              uRI2 = uRI1;
              uRI1 = uRI4;
              stringBuilder1 = stringBuilder2;
              uRI3 = uRI1;
            } 
          } 
          return getExportedModuleInterface(paramContext, (String)uRI2, uRI3, (URI)stringBuilder1, false);
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't resolve relative module ID \"");
        stringBuilder.append((String)uRI1);
        stringBuilder.append("\" when require() is used outside of a module");
        throw ScriptRuntime.throwError(paramContext, uRI2, stringBuilder.toString());
      } 
      uRI = uRI1;
      paramScriptable2 = null;
      paramArrayOfObject = null;
      return getExportedModuleInterface(paramContext, (String)uRI, (URI)paramScriptable2, (URI)paramArrayOfObject, false);
    } 
    throw ScriptRuntime.throwError(paramContext, uRI, "require() needs one argument");
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    throw ScriptRuntime.throwError(paramContext, paramScriptable, "require() can not be invoked as a constructor");
  }
  
  public int getArity() {
    return 1;
  }
  
  public String getFunctionName() {
    return "require";
  }
  
  public int getLength() {
    return 1;
  }
  
  public void install(Scriptable paramScriptable) {
    ScriptableObject.putProperty(paramScriptable, "require", this);
  }
  
  public Scriptable requireMain(Context paramContext, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mainModuleId : Ljava/lang/String;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnull -> 59
    //   9: aload_3
    //   10: aload_2
    //   11: invokevirtual equals : (Ljava/lang/Object;)Z
    //   14: ifeq -> 22
    //   17: aload_0
    //   18: getfield mainExports : Lcom/trendmicro/hippo/Scriptable;
    //   21: areturn
    //   22: new java/lang/StringBuilder
    //   25: dup
    //   26: invokespecial <init> : ()V
    //   29: astore_1
    //   30: aload_1
    //   31: ldc_w 'Main module already set to '
    //   34: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: pop
    //   38: aload_1
    //   39: aload_0
    //   40: getfield mainModuleId : Ljava/lang/String;
    //   43: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: pop
    //   47: new java/lang/IllegalStateException
    //   50: dup
    //   51: aload_1
    //   52: invokevirtual toString : ()Ljava/lang/String;
    //   55: invokespecial <init> : (Ljava/lang/String;)V
    //   58: athrow
    //   59: aload_0
    //   60: getfield moduleScriptProvider : Lcom/trendmicro/hippo/commonjs/module/ModuleScriptProvider;
    //   63: aload_1
    //   64: aload_2
    //   65: aconst_null
    //   66: aconst_null
    //   67: aload_0
    //   68: getfield paths : Lcom/trendmicro/hippo/Scriptable;
    //   71: invokeinterface getModuleScript : (Lcom/trendmicro/hippo/Context;Ljava/lang/String;Ljava/net/URI;Ljava/net/URI;Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/commonjs/module/ModuleScript;
    //   76: astore_3
    //   77: aload_3
    //   78: ifnull -> 97
    //   81: aload_0
    //   82: aload_0
    //   83: aload_1
    //   84: aload_2
    //   85: aconst_null
    //   86: aconst_null
    //   87: iconst_1
    //   88: invokespecial getExportedModuleInterface : (Lcom/trendmicro/hippo/Context;Ljava/lang/String;Ljava/net/URI;Ljava/net/URI;Z)Lcom/trendmicro/hippo/Scriptable;
    //   91: putfield mainExports : Lcom/trendmicro/hippo/Scriptable;
    //   94: goto -> 230
    //   97: aload_0
    //   98: getfield sandboxed : Z
    //   101: ifne -> 230
    //   104: aconst_null
    //   105: astore_3
    //   106: new java/net/URI
    //   109: astore #4
    //   111: aload #4
    //   113: aload_2
    //   114: invokespecial <init> : (Ljava/lang/String;)V
    //   117: aload #4
    //   119: astore_3
    //   120: goto -> 125
    //   123: astore #4
    //   125: aload_3
    //   126: ifnull -> 139
    //   129: aload_3
    //   130: astore #4
    //   132: aload_3
    //   133: invokevirtual isAbsolute : ()Z
    //   136: ifne -> 161
    //   139: new java/io/File
    //   142: dup
    //   143: aload_2
    //   144: invokespecial <init> : (Ljava/lang/String;)V
    //   147: astore_3
    //   148: aload_3
    //   149: invokevirtual isFile : ()Z
    //   152: ifeq -> 182
    //   155: aload_3
    //   156: invokevirtual toURI : ()Ljava/net/URI;
    //   159: astore #4
    //   161: aload_0
    //   162: aload_0
    //   163: aload_1
    //   164: aload #4
    //   166: invokevirtual toString : ()Ljava/lang/String;
    //   169: aload #4
    //   171: aconst_null
    //   172: iconst_1
    //   173: invokespecial getExportedModuleInterface : (Lcom/trendmicro/hippo/Context;Ljava/lang/String;Ljava/net/URI;Ljava/net/URI;Z)Lcom/trendmicro/hippo/Scriptable;
    //   176: putfield mainExports : Lcom/trendmicro/hippo/Scriptable;
    //   179: goto -> 230
    //   182: aload_0
    //   183: getfield nativeScope : Lcom/trendmicro/hippo/Scriptable;
    //   186: astore_3
    //   187: new java/lang/StringBuilder
    //   190: dup
    //   191: invokespecial <init> : ()V
    //   194: astore #4
    //   196: aload #4
    //   198: ldc 'Module "'
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload #4
    //   206: aload_2
    //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload #4
    //   213: ldc '" not found.'
    //   215: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: pop
    //   219: aload_1
    //   220: aload_3
    //   221: aload #4
    //   223: invokevirtual toString : ()Ljava/lang/String;
    //   226: invokestatic throwError : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;)Lcom/trendmicro/hippo/JavaScriptException;
    //   229: athrow
    //   230: aload_0
    //   231: aload_2
    //   232: putfield mainModuleId : Ljava/lang/String;
    //   235: aload_0
    //   236: getfield mainExports : Lcom/trendmicro/hippo/Scriptable;
    //   239: areturn
    //   240: astore_1
    //   241: new java/lang/RuntimeException
    //   244: dup
    //   245: aload_1
    //   246: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   249: athrow
    //   250: astore_1
    //   251: aload_1
    //   252: athrow
    // Exception table:
    //   from	to	target	type
    //   59	77	250	java/lang/RuntimeException
    //   59	77	240	java/lang/Exception
    //   106	117	123	java/net/URISyntaxException
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/Require.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */