package com.trendmicro.hippo.tools.debugger;

import com.trendmicro.hippo.Callable;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextAction;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.ImporterTopLevel;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.ObjArray;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.debug.DebugFrame;
import com.trendmicro.hippo.debug.DebuggableObject;
import com.trendmicro.hippo.debug.DebuggableScript;
import com.trendmicro.hippo.debug.Debugger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Dim {
  public static final int BREAK = 4;
  
  public static final int EXIT = 5;
  
  public static final int GO = 3;
  
  private static final int IPROXY_COMPILE_SCRIPT = 2;
  
  private static final int IPROXY_DEBUG = 0;
  
  private static final int IPROXY_EVAL_SCRIPT = 3;
  
  private static final int IPROXY_LISTEN = 1;
  
  private static final int IPROXY_OBJECT_IDS = 7;
  
  private static final int IPROXY_OBJECT_PROPERTY = 6;
  
  private static final int IPROXY_OBJECT_TO_STRING = 5;
  
  private static final int IPROXY_STRING_IS_COMPILABLE = 4;
  
  public static final int STEP_INTO = 1;
  
  public static final int STEP_OUT = 2;
  
  public static final int STEP_OVER = 0;
  
  private boolean breakFlag;
  
  private boolean breakOnEnter;
  
  private boolean breakOnExceptions;
  
  private boolean breakOnReturn;
  
  private GuiCallback callback;
  
  private ContextFactory contextFactory;
  
  private StackFrame evalFrame;
  
  private String evalRequest;
  
  private String evalResult;
  
  private Object eventThreadMonitor = new Object();
  
  private int frameIndex = -1;
  
  private final Map<String, FunctionSource> functionNames = Collections.synchronizedMap(new HashMap<>());
  
  private final Map<DebuggableScript, FunctionSource> functionToSource = Collections.synchronizedMap(new HashMap<>());
  
  private boolean insideInterruptLoop;
  
  private volatile ContextData interruptedContextData;
  
  private DimIProxy listener;
  
  private Object monitor = new Object();
  
  private volatile int returnValue = -1;
  
  private ScopeProvider scopeProvider;
  
  private SourceProvider sourceProvider;
  
  private final Map<String, SourceInfo> urlToSourceInfo = Collections.synchronizedMap(new HashMap<>());
  
  private static void collectFunctions_r(DebuggableScript paramDebuggableScript, ObjArray paramObjArray) {
    paramObjArray.add(paramDebuggableScript);
    for (byte b = 0; b != paramDebuggableScript.getFunctionCount(); b++)
      collectFunctions_r(paramDebuggableScript.getFunction(b), paramObjArray); 
  }
  
  private static String do_eval(Context paramContext, StackFrame paramStackFrame, String paramString) {
    String str1;
    String str2 = "";
    Debugger debugger = paramContext.getDebugger();
    Object object = paramContext.getDebuggerContextData();
    int i = paramContext.getOptimizationLevel();
    paramContext.setDebugger(null, null);
    paramContext.setOptimizationLevel(-1);
    paramContext.setGeneratingDebug(false);
    try {
      Object object1 = ((Callable)paramContext.compileString(paramString, "", 0, null)).call(paramContext, paramStackFrame.scope, paramStackFrame.thisObj, ScriptRuntime.emptyArgs);
      if (object1 == Undefined.instance) {
        object1 = str2;
      } else {
        object1 = ScriptRuntime.toString(object1);
      } 
    } catch (Exception exception) {
      String str = exception.getMessage();
    } finally {}
    paramContext.setGeneratingDebug(true);
    paramContext.setOptimizationLevel(i);
    paramContext.setDebugger(debugger, object);
    StackFrame stackFrame = paramStackFrame;
    if (paramStackFrame == null)
      str1 = "null"; 
    return str1;
  }
  
  private FunctionSource functionSource(DebuggableScript paramDebuggableScript) {
    return this.functionToSource.get(paramDebuggableScript);
  }
  
  private static DebuggableScript[] getAllFunctions(DebuggableScript paramDebuggableScript) {
    ObjArray objArray = new ObjArray();
    collectFunctions_r(paramDebuggableScript, objArray);
    DebuggableScript[] arrayOfDebuggableScript = new DebuggableScript[objArray.size()];
    objArray.toArray((Object[])arrayOfDebuggableScript);
    return arrayOfDebuggableScript;
  }
  
  private FunctionSource getFunctionSource(DebuggableScript paramDebuggableScript) {
    DebuggableScript debuggableScript;
    FunctionSource functionSource1 = functionSource(paramDebuggableScript);
    FunctionSource functionSource2 = functionSource1;
    if (functionSource1 == null) {
      String str = getNormalizedUrl(paramDebuggableScript);
      functionSource2 = functionSource1;
      if (sourceInfo(str) == null) {
        functionSource2 = functionSource1;
        if (!paramDebuggableScript.isGeneratedScript()) {
          str = loadSource(str);
          functionSource2 = functionSource1;
          if (str != null)
            for (debuggableScript = paramDebuggableScript;; debuggableScript = debuggableScript1) {
              DebuggableScript debuggableScript1 = debuggableScript.getParent();
              if (debuggableScript1 == null) {
                registerTopScript(debuggableScript, str);
                FunctionSource functionSource = functionSource(paramDebuggableScript);
                break;
              } 
            }  
        } 
      } 
    } 
    return (FunctionSource)debuggableScript;
  }
  
  private String getNormalizedUrl(DebuggableScript paramDebuggableScript) {
    String str1;
    String str2 = paramDebuggableScript.getSourceName();
    if (str2 == null) {
      str1 = "<stdin>";
    } else {
      DebuggableScript debuggableScript = null;
      int i = str2.length();
      int j = 0;
      while (true) {
        StringBuilder stringBuilder;
        int k = str2.indexOf('#', j);
        if (k >= 0) {
          String str;
          DebuggableScript debuggableScript1 = null;
          int m;
          for (m = k + 1; m != i; m++) {
            char c = str2.charAt(m);
            if ('0' > c || c > '9')
              break; 
          } 
          int n = j;
          paramDebuggableScript = debuggableScript1;
          if (m != k + 1) {
            n = j;
            paramDebuggableScript = debuggableScript1;
            if ("(eval)".regionMatches(0, str2, m, 6)) {
              n = m + 6;
              str = "(eval)";
            } 
          } 
          if (str == null) {
            j = n;
          } else {
            StringBuilder stringBuilder1;
            debuggableScript1 = debuggableScript;
            if (debuggableScript == null) {
              stringBuilder1 = new StringBuilder();
              stringBuilder1.append(str2.substring(0, k));
            } 
            stringBuilder1.append(str);
            stringBuilder = stringBuilder1;
            j = n;
            continue;
          } 
        } 
        str1 = str2;
        if (stringBuilder != null) {
          if (j != i)
            stringBuilder.append(str2.substring(j)); 
          str1 = stringBuilder.toString();
        } 
        return str1;
      } 
    } 
    return str1;
  }
  
  private Object[] getObjectIdsImpl(Context paramContext, Object paramObject) {
    Object[] arrayOfObject;
    if (!(paramObject instanceof Scriptable) || paramObject == Undefined.instance)
      return Context.emptyArgs; 
    paramObject = paramObject;
    if (paramObject instanceof DebuggableObject) {
      arrayOfObject = ((DebuggableObject)paramObject).getAllIds();
    } else {
      arrayOfObject = paramObject.getIds();
    } 
    Scriptable scriptable1 = paramObject.getPrototype();
    Scriptable scriptable2 = paramObject.getParentScope();
    int i = 0;
    if (scriptable1 != null)
      i = 0 + 1; 
    int j = i;
    if (scriptable2 != null)
      j = i + 1; 
    paramObject = arrayOfObject;
    if (j != 0) {
      paramObject = new Object[arrayOfObject.length + j];
      System.arraycopy(arrayOfObject, 0, paramObject, j, arrayOfObject.length);
      Object object = paramObject;
      i = 0;
      if (scriptable1 != null) {
        object[0] = "__proto__";
        i = 0 + 1;
      } 
      paramObject = object;
      if (scriptable2 != null) {
        object[i] = "__parent__";
        paramObject = object;
      } 
    } 
    return (Object[])paramObject;
  }
  
  private Object getObjectPropertyImpl(Context paramContext, Object paramObject1, Object paramObject2) {
    Object object = paramObject1;
    if (paramObject2 instanceof String) {
      paramObject1 = paramObject2;
      if (!paramObject1.equals("this"))
        if (paramObject1.equals("__proto__")) {
          object = object.getPrototype();
        } else if (paramObject1.equals("__parent__")) {
          object = object.getParentScope();
        } else {
          paramObject1 = ScriptableObject.getProperty((Scriptable)object, (String)paramObject1);
          object = paramObject1;
          if (paramObject1 == ScriptableObject.NOT_FOUND)
            object = Undefined.instance; 
        }  
    } else {
      paramObject1 = ScriptableObject.getProperty((Scriptable)object, ((Integer)paramObject2).intValue());
      object = paramObject1;
      if (paramObject1 == ScriptableObject.NOT_FOUND)
        object = Undefined.instance; 
    } 
    return object;
  }
  
  private void handleBreakpointHit(StackFrame paramStackFrame, Context paramContext) {
    this.breakFlag = false;
    interrupted(paramContext, paramStackFrame, null);
  }
  
  private void handleExceptionThrown(Context paramContext, Throwable paramThrowable, StackFrame paramStackFrame) {
    if (this.breakOnExceptions) {
      ContextData contextData = paramStackFrame.contextData();
      if (contextData.lastProcessedException != paramThrowable) {
        interrupted(paramContext, paramStackFrame, paramThrowable);
        ContextData.access$302(contextData, paramThrowable);
      } 
    } 
  }
  
  private void interrupted(Context paramContext, StackFrame paramStackFrame, Throwable paramThrowable) {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual contextData : ()Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   4: astore #4
    //   6: aload_0
    //   7: getfield callback : Lcom/trendmicro/hippo/tools/debugger/GuiCallback;
    //   10: invokeinterface isGuiEventThread : ()Z
    //   15: istore #5
    //   17: aload #4
    //   19: iload #5
    //   21: invokestatic access$402 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;Z)Z
    //   24: pop
    //   25: aload_0
    //   26: getfield eventThreadMonitor : Ljava/lang/Object;
    //   29: astore #6
    //   31: aload #6
    //   33: monitorenter
    //   34: iload #5
    //   36: ifeq -> 55
    //   39: aload_0
    //   40: getfield interruptedContextData : Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   43: ifnull -> 81
    //   46: aload #6
    //   48: monitorexit
    //   49: iconst_1
    //   50: istore #7
    //   52: goto -> 93
    //   55: aload_0
    //   56: getfield interruptedContextData : Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   59: astore #8
    //   61: aload #8
    //   63: ifnull -> 81
    //   66: aload_0
    //   67: getfield eventThreadMonitor : Ljava/lang/Object;
    //   70: invokevirtual wait : ()V
    //   73: goto -> 55
    //   76: astore_1
    //   77: aload #6
    //   79: monitorexit
    //   80: return
    //   81: aload_0
    //   82: aload #4
    //   84: putfield interruptedContextData : Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   87: aload #6
    //   89: monitorexit
    //   90: iconst_0
    //   91: istore #7
    //   93: iload #7
    //   95: ifeq -> 99
    //   98: return
    //   99: aload_0
    //   100: getfield interruptedContextData : Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   103: ifnonnull -> 110
    //   106: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   109: pop
    //   110: aload_0
    //   111: aload #4
    //   113: invokevirtual frameCount : ()I
    //   116: iconst_1
    //   117: isub
    //   118: putfield frameIndex : I
    //   121: invokestatic currentThread : ()Ljava/lang/Thread;
    //   124: invokevirtual toString : ()Ljava/lang/String;
    //   127: astore #8
    //   129: aload_3
    //   130: ifnonnull -> 138
    //   133: aconst_null
    //   134: astore_3
    //   135: goto -> 143
    //   138: aload_3
    //   139: invokevirtual toString : ()Ljava/lang/String;
    //   142: astore_3
    //   143: iconst_m1
    //   144: istore #7
    //   146: iload #5
    //   148: ifne -> 342
    //   151: aload_0
    //   152: getfield monitor : Ljava/lang/Object;
    //   155: astore #6
    //   157: aload #6
    //   159: monitorenter
    //   160: aload_0
    //   161: getfield insideInterruptLoop : Z
    //   164: ifeq -> 171
    //   167: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   170: pop
    //   171: aload_0
    //   172: iconst_1
    //   173: putfield insideInterruptLoop : Z
    //   176: aload_0
    //   177: aconst_null
    //   178: putfield evalRequest : Ljava/lang/String;
    //   181: aload_0
    //   182: iconst_m1
    //   183: putfield returnValue : I
    //   186: aload_0
    //   187: getfield callback : Lcom/trendmicro/hippo/tools/debugger/GuiCallback;
    //   190: aload_2
    //   191: aload #8
    //   193: aload_3
    //   194: invokeinterface enterInterrupt : (Lcom/trendmicro/hippo/tools/debugger/Dim$StackFrame;Ljava/lang/String;Ljava/lang/String;)V
    //   199: aload_0
    //   200: getfield monitor : Ljava/lang/Object;
    //   203: invokevirtual wait : ()V
    //   206: aload_0
    //   207: getfield evalRequest : Ljava/lang/String;
    //   210: ifnull -> 282
    //   213: aload_0
    //   214: aconst_null
    //   215: putfield evalResult : Ljava/lang/String;
    //   218: aload_0
    //   219: getfield evalFrame : Lcom/trendmicro/hippo/tools/debugger/Dim$StackFrame;
    //   222: astore_2
    //   223: aload_0
    //   224: getfield evalRequest : Ljava/lang/String;
    //   227: astore_3
    //   228: aload_0
    //   229: aload_1
    //   230: aload_2
    //   231: aload_3
    //   232: invokestatic do_eval : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/tools/debugger/Dim$StackFrame;Ljava/lang/String;)Ljava/lang/String;
    //   235: putfield evalResult : Ljava/lang/String;
    //   238: aload_0
    //   239: aconst_null
    //   240: putfield evalRequest : Ljava/lang/String;
    //   243: aload_0
    //   244: aconst_null
    //   245: putfield evalFrame : Lcom/trendmicro/hippo/tools/debugger/Dim$StackFrame;
    //   248: aload_0
    //   249: getfield monitor : Ljava/lang/Object;
    //   252: invokevirtual notify : ()V
    //   255: goto -> 199
    //   258: astore_1
    //   259: goto -> 263
    //   262: astore_1
    //   263: aload_0
    //   264: aconst_null
    //   265: putfield evalRequest : Ljava/lang/String;
    //   268: aload_0
    //   269: aconst_null
    //   270: putfield evalFrame : Lcom/trendmicro/hippo/tools/debugger/Dim$StackFrame;
    //   273: aload_0
    //   274: getfield monitor : Ljava/lang/Object;
    //   277: invokevirtual notify : ()V
    //   280: aload_1
    //   281: athrow
    //   282: aload_0
    //   283: getfield returnValue : I
    //   286: iconst_m1
    //   287: if_icmpeq -> 299
    //   290: aload_0
    //   291: getfield returnValue : I
    //   294: istore #7
    //   296: goto -> 313
    //   299: goto -> 199
    //   302: astore_1
    //   303: goto -> 325
    //   306: astore_1
    //   307: invokestatic currentThread : ()Ljava/lang/Thread;
    //   310: invokevirtual interrupt : ()V
    //   313: aload_0
    //   314: iconst_0
    //   315: putfield insideInterruptLoop : Z
    //   318: aload #6
    //   320: monitorexit
    //   321: goto -> 394
    //   324: astore_1
    //   325: aload_0
    //   326: iconst_0
    //   327: putfield insideInterruptLoop : Z
    //   330: aload_1
    //   331: athrow
    //   332: astore_1
    //   333: aload #6
    //   335: monitorexit
    //   336: aload_1
    //   337: athrow
    //   338: astore_1
    //   339: goto -> 333
    //   342: aload_0
    //   343: iconst_m1
    //   344: putfield returnValue : I
    //   347: aload_0
    //   348: getfield callback : Lcom/trendmicro/hippo/tools/debugger/GuiCallback;
    //   351: aload_2
    //   352: aload #8
    //   354: aload_3
    //   355: invokeinterface enterInterrupt : (Lcom/trendmicro/hippo/tools/debugger/Dim$StackFrame;Ljava/lang/String;Ljava/lang/String;)V
    //   360: aload_0
    //   361: getfield returnValue : I
    //   364: istore #7
    //   366: iload #7
    //   368: iconst_m1
    //   369: if_icmpne -> 388
    //   372: aload_0
    //   373: getfield callback : Lcom/trendmicro/hippo/tools/debugger/GuiCallback;
    //   376: invokeinterface dispatchNextGuiEvent : ()V
    //   381: goto -> 385
    //   384: astore_1
    //   385: goto -> 360
    //   388: aload_0
    //   389: getfield returnValue : I
    //   392: istore #7
    //   394: iload #7
    //   396: ifeq -> 463
    //   399: iload #7
    //   401: iconst_1
    //   402: if_icmpeq -> 446
    //   405: iload #7
    //   407: iconst_2
    //   408: if_icmpeq -> 414
    //   411: goto -> 481
    //   414: aload #4
    //   416: invokevirtual frameCount : ()I
    //   419: iconst_1
    //   420: if_icmple -> 481
    //   423: aload #4
    //   425: iconst_1
    //   426: invokestatic access$1402 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;Z)Z
    //   429: pop
    //   430: aload #4
    //   432: aload #4
    //   434: invokevirtual frameCount : ()I
    //   437: iconst_1
    //   438: isub
    //   439: invokestatic access$1502 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;I)I
    //   442: pop
    //   443: goto -> 481
    //   446: aload #4
    //   448: iconst_1
    //   449: invokestatic access$1402 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;Z)Z
    //   452: pop
    //   453: aload #4
    //   455: iconst_m1
    //   456: invokestatic access$1502 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;I)I
    //   459: pop
    //   460: goto -> 481
    //   463: aload #4
    //   465: iconst_1
    //   466: invokestatic access$1402 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;Z)Z
    //   469: pop
    //   470: aload #4
    //   472: aload #4
    //   474: invokevirtual frameCount : ()I
    //   477: invokestatic access$1502 : (Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;I)I
    //   480: pop
    //   481: aload_0
    //   482: getfield eventThreadMonitor : Ljava/lang/Object;
    //   485: astore_1
    //   486: aload_1
    //   487: monitorenter
    //   488: aload_0
    //   489: aconst_null
    //   490: putfield interruptedContextData : Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   493: aload_0
    //   494: getfield eventThreadMonitor : Ljava/lang/Object;
    //   497: invokevirtual notifyAll : ()V
    //   500: aload_1
    //   501: monitorexit
    //   502: return
    //   503: astore_2
    //   504: aload_1
    //   505: monitorexit
    //   506: aload_2
    //   507: athrow
    //   508: astore_1
    //   509: goto -> 513
    //   512: astore_1
    //   513: aload_0
    //   514: getfield eventThreadMonitor : Ljava/lang/Object;
    //   517: astore_2
    //   518: aload_2
    //   519: monitorenter
    //   520: aload_0
    //   521: aconst_null
    //   522: putfield interruptedContextData : Lcom/trendmicro/hippo/tools/debugger/Dim$ContextData;
    //   525: aload_0
    //   526: getfield eventThreadMonitor : Ljava/lang/Object;
    //   529: invokevirtual notifyAll : ()V
    //   532: aload_2
    //   533: monitorexit
    //   534: aload_1
    //   535: athrow
    //   536: astore_1
    //   537: aload_2
    //   538: monitorexit
    //   539: aload_1
    //   540: athrow
    //   541: astore_1
    //   542: aload #6
    //   544: monitorexit
    //   545: aload_1
    //   546: athrow
    //   547: astore_1
    //   548: goto -> 542
    // Exception table:
    //   from	to	target	type
    //   39	49	541	finally
    //   55	61	541	finally
    //   66	73	76	java/lang/InterruptedException
    //   66	73	541	finally
    //   77	80	541	finally
    //   81	90	541	finally
    //   110	129	512	finally
    //   138	143	512	finally
    //   151	160	512	finally
    //   160	171	332	finally
    //   171	199	332	finally
    //   199	206	306	java/lang/InterruptedException
    //   199	206	302	finally
    //   206	218	302	finally
    //   218	228	262	finally
    //   228	238	258	finally
    //   238	255	324	finally
    //   263	280	324	finally
    //   280	282	324	finally
    //   282	296	324	finally
    //   307	313	324	finally
    //   313	318	338	finally
    //   318	321	338	finally
    //   325	330	338	finally
    //   330	332	338	finally
    //   333	336	338	finally
    //   336	338	508	finally
    //   342	360	508	finally
    //   360	366	508	finally
    //   372	381	384	java/lang/InterruptedException
    //   372	381	508	finally
    //   388	394	508	finally
    //   414	430	508	finally
    //   430	443	508	finally
    //   446	460	508	finally
    //   463	481	508	finally
    //   488	502	503	finally
    //   504	506	503	finally
    //   520	534	536	finally
    //   537	539	536	finally
    //   542	545	547	finally
  }
  
  private String loadSource(String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_1
    //   3: bipush #35
    //   5: invokevirtual indexOf : (I)I
    //   8: istore_3
    //   9: aload_1
    //   10: astore #4
    //   12: iload_3
    //   13: iflt -> 24
    //   16: aload_1
    //   17: iconst_0
    //   18: iload_3
    //   19: invokevirtual substring : (II)Ljava/lang/String;
    //   22: astore #4
    //   24: aload_2
    //   25: astore #5
    //   27: aload #4
    //   29: astore #6
    //   31: aload #4
    //   33: bipush #58
    //   35: invokevirtual indexOf : (I)I
    //   38: istore_3
    //   39: aload #4
    //   41: astore_1
    //   42: iload_3
    //   43: ifge -> 485
    //   46: aload_2
    //   47: astore #5
    //   49: aload #4
    //   51: astore #6
    //   53: aload #4
    //   55: ldc_w '~/'
    //   58: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   61: ifeq -> 193
    //   64: aload_2
    //   65: astore #5
    //   67: aload #4
    //   69: astore #6
    //   71: ldc_w 'user.home'
    //   74: invokestatic getSystemProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   77: astore #7
    //   79: aload #7
    //   81: ifnull -> 193
    //   84: aload_2
    //   85: astore #5
    //   87: aload #4
    //   89: astore #6
    //   91: aload #4
    //   93: iconst_2
    //   94: invokevirtual substring : (I)Ljava/lang/String;
    //   97: astore_1
    //   98: aload_2
    //   99: astore #5
    //   101: aload #4
    //   103: astore #6
    //   105: new java/io/File
    //   108: astore #8
    //   110: aload_2
    //   111: astore #5
    //   113: aload #4
    //   115: astore #6
    //   117: new java/io/File
    //   120: astore #9
    //   122: aload_2
    //   123: astore #5
    //   125: aload #4
    //   127: astore #6
    //   129: aload #9
    //   131: aload #7
    //   133: invokespecial <init> : (Ljava/lang/String;)V
    //   136: aload_2
    //   137: astore #5
    //   139: aload #4
    //   141: astore #6
    //   143: aload #8
    //   145: aload #9
    //   147: aload_1
    //   148: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
    //   151: aload_2
    //   152: astore #5
    //   154: aload #4
    //   156: astore #6
    //   158: aload #8
    //   160: invokevirtual exists : ()Z
    //   163: ifeq -> 193
    //   166: aload_2
    //   167: astore #5
    //   169: aload #4
    //   171: astore #6
    //   173: new java/io/FileInputStream
    //   176: astore_1
    //   177: aload_2
    //   178: astore #5
    //   180: aload #4
    //   182: astore #6
    //   184: aload_1
    //   185: aload #8
    //   187: invokespecial <init> : (Ljava/io/File;)V
    //   190: goto -> 531
    //   193: aload_2
    //   194: astore #5
    //   196: aload #4
    //   198: astore #6
    //   200: new java/io/File
    //   203: astore_1
    //   204: aload_2
    //   205: astore #5
    //   207: aload #4
    //   209: astore #6
    //   211: aload_1
    //   212: aload #4
    //   214: invokespecial <init> : (Ljava/lang/String;)V
    //   217: aload_2
    //   218: astore #5
    //   220: aload #4
    //   222: astore #6
    //   224: aload_1
    //   225: invokevirtual exists : ()Z
    //   228: ifeq -> 250
    //   231: aload_2
    //   232: astore #5
    //   234: aload #4
    //   236: astore #6
    //   238: new java/io/FileInputStream
    //   241: dup
    //   242: aload_1
    //   243: invokespecial <init> : (Ljava/io/File;)V
    //   246: astore_1
    //   247: goto -> 531
    //   250: goto -> 254
    //   253: astore_1
    //   254: aload_2
    //   255: astore #5
    //   257: aload #4
    //   259: astore #6
    //   261: aload #4
    //   263: ldc_w '//'
    //   266: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   269: ifeq -> 338
    //   272: aload_2
    //   273: astore #5
    //   275: aload #4
    //   277: astore #6
    //   279: new java/lang/StringBuilder
    //   282: astore_1
    //   283: aload_2
    //   284: astore #5
    //   286: aload #4
    //   288: astore #6
    //   290: aload_1
    //   291: invokespecial <init> : ()V
    //   294: aload_2
    //   295: astore #5
    //   297: aload #4
    //   299: astore #6
    //   301: aload_1
    //   302: ldc_w 'http:'
    //   305: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: aload_2
    //   310: astore #5
    //   312: aload #4
    //   314: astore #6
    //   316: aload_1
    //   317: aload #4
    //   319: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   322: pop
    //   323: aload_2
    //   324: astore #5
    //   326: aload #4
    //   328: astore #6
    //   330: aload_1
    //   331: invokevirtual toString : ()Ljava/lang/String;
    //   334: astore_1
    //   335: goto -> 485
    //   338: aload_2
    //   339: astore #5
    //   341: aload #4
    //   343: astore #6
    //   345: aload #4
    //   347: ldc_w '/'
    //   350: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   353: ifeq -> 422
    //   356: aload_2
    //   357: astore #5
    //   359: aload #4
    //   361: astore #6
    //   363: new java/lang/StringBuilder
    //   366: astore_1
    //   367: aload_2
    //   368: astore #5
    //   370: aload #4
    //   372: astore #6
    //   374: aload_1
    //   375: invokespecial <init> : ()V
    //   378: aload_2
    //   379: astore #5
    //   381: aload #4
    //   383: astore #6
    //   385: aload_1
    //   386: ldc_w 'http://127.0.0.1'
    //   389: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: pop
    //   393: aload_2
    //   394: astore #5
    //   396: aload #4
    //   398: astore #6
    //   400: aload_1
    //   401: aload #4
    //   403: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   406: pop
    //   407: aload_2
    //   408: astore #5
    //   410: aload #4
    //   412: astore #6
    //   414: aload_1
    //   415: invokevirtual toString : ()Ljava/lang/String;
    //   418: astore_1
    //   419: goto -> 485
    //   422: aload_2
    //   423: astore #5
    //   425: aload #4
    //   427: astore #6
    //   429: new java/lang/StringBuilder
    //   432: astore_1
    //   433: aload_2
    //   434: astore #5
    //   436: aload #4
    //   438: astore #6
    //   440: aload_1
    //   441: invokespecial <init> : ()V
    //   444: aload_2
    //   445: astore #5
    //   447: aload #4
    //   449: astore #6
    //   451: aload_1
    //   452: ldc_w 'http://'
    //   455: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   458: pop
    //   459: aload_2
    //   460: astore #5
    //   462: aload #4
    //   464: astore #6
    //   466: aload_1
    //   467: aload #4
    //   469: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   472: pop
    //   473: aload_2
    //   474: astore #5
    //   476: aload #4
    //   478: astore #6
    //   480: aload_1
    //   481: invokevirtual toString : ()Ljava/lang/String;
    //   484: astore_1
    //   485: aload_2
    //   486: astore #5
    //   488: aload_1
    //   489: astore #6
    //   491: new java/net/URL
    //   494: astore #4
    //   496: aload_2
    //   497: astore #5
    //   499: aload_1
    //   500: astore #6
    //   502: aload #4
    //   504: aload_1
    //   505: invokespecial <init> : (Ljava/lang/String;)V
    //   508: aload_2
    //   509: astore #5
    //   511: aload_1
    //   512: astore #6
    //   514: aload #4
    //   516: invokevirtual openStream : ()Ljava/io/InputStream;
    //   519: astore #4
    //   521: aload #4
    //   523: astore #5
    //   525: aload_1
    //   526: astore #4
    //   528: aload #5
    //   530: astore_1
    //   531: new java/io/InputStreamReader
    //   534: astore #5
    //   536: aload #5
    //   538: aload_1
    //   539: invokespecial <init> : (Ljava/io/InputStream;)V
    //   542: aload #5
    //   544: invokestatic readReader : (Ljava/io/Reader;)Ljava/lang/String;
    //   547: astore #5
    //   549: aload #5
    //   551: astore_2
    //   552: aload_2
    //   553: astore #5
    //   555: aload #4
    //   557: astore #6
    //   559: aload_1
    //   560: invokevirtual close : ()V
    //   563: aload_2
    //   564: astore #5
    //   566: goto -> 648
    //   569: astore #8
    //   571: aload_2
    //   572: astore #5
    //   574: aload #4
    //   576: astore #6
    //   578: aload_1
    //   579: invokevirtual close : ()V
    //   582: aload_2
    //   583: astore #5
    //   585: aload #4
    //   587: astore #6
    //   589: aload #8
    //   591: athrow
    //   592: astore_2
    //   593: getstatic java/lang/System.err : Ljava/io/PrintStream;
    //   596: astore_1
    //   597: new java/lang/StringBuilder
    //   600: dup
    //   601: invokespecial <init> : ()V
    //   604: astore #4
    //   606: aload #4
    //   608: ldc_w 'Failed to load source from '
    //   611: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   614: pop
    //   615: aload #4
    //   617: aload #6
    //   619: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   622: pop
    //   623: aload #4
    //   625: ldc_w ': '
    //   628: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   631: pop
    //   632: aload #4
    //   634: aload_2
    //   635: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   638: pop
    //   639: aload_1
    //   640: aload #4
    //   642: invokevirtual toString : ()Ljava/lang/String;
    //   645: invokevirtual println : (Ljava/lang/String;)V
    //   648: aload #5
    //   650: areturn
    // Exception table:
    //   from	to	target	type
    //   31	39	592	java/io/IOException
    //   53	64	253	java/lang/SecurityException
    //   53	64	592	java/io/IOException
    //   71	79	253	java/lang/SecurityException
    //   71	79	592	java/io/IOException
    //   91	98	253	java/lang/SecurityException
    //   91	98	592	java/io/IOException
    //   105	110	253	java/lang/SecurityException
    //   105	110	592	java/io/IOException
    //   117	122	253	java/lang/SecurityException
    //   117	122	592	java/io/IOException
    //   129	136	253	java/lang/SecurityException
    //   129	136	592	java/io/IOException
    //   143	151	253	java/lang/SecurityException
    //   143	151	592	java/io/IOException
    //   158	166	253	java/lang/SecurityException
    //   158	166	592	java/io/IOException
    //   173	177	253	java/lang/SecurityException
    //   173	177	592	java/io/IOException
    //   184	190	253	java/lang/SecurityException
    //   184	190	592	java/io/IOException
    //   200	204	253	java/lang/SecurityException
    //   200	204	592	java/io/IOException
    //   211	217	253	java/lang/SecurityException
    //   211	217	592	java/io/IOException
    //   224	231	253	java/lang/SecurityException
    //   224	231	592	java/io/IOException
    //   238	247	253	java/lang/SecurityException
    //   238	247	592	java/io/IOException
    //   261	272	592	java/io/IOException
    //   279	283	592	java/io/IOException
    //   290	294	592	java/io/IOException
    //   301	309	592	java/io/IOException
    //   316	323	592	java/io/IOException
    //   330	335	592	java/io/IOException
    //   345	356	592	java/io/IOException
    //   363	367	592	java/io/IOException
    //   374	378	592	java/io/IOException
    //   385	393	592	java/io/IOException
    //   400	407	592	java/io/IOException
    //   414	419	592	java/io/IOException
    //   429	433	592	java/io/IOException
    //   440	444	592	java/io/IOException
    //   451	459	592	java/io/IOException
    //   466	473	592	java/io/IOException
    //   480	485	592	java/io/IOException
    //   491	496	592	java/io/IOException
    //   502	508	592	java/io/IOException
    //   514	521	592	java/io/IOException
    //   531	549	569	finally
    //   559	563	592	java/io/IOException
    //   578	582	592	java/io/IOException
    //   589	592	592	java/io/IOException
  }
  
  private void registerTopScript(DebuggableScript paramDebuggableScript, String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: invokeinterface isTopLevel : ()Z
    //   6: ifeq -> 232
    //   9: aload_0
    //   10: aload_1
    //   11: invokespecial getNormalizedUrl : (Lcom/trendmicro/hippo/debug/DebuggableScript;)Ljava/lang/String;
    //   14: astore_3
    //   15: aload_1
    //   16: invokestatic getAllFunctions : (Lcom/trendmicro/hippo/debug/DebuggableScript;)[Lcom/trendmicro/hippo/debug/DebuggableScript;
    //   19: astore #4
    //   21: aload_0
    //   22: getfield sourceProvider : Lcom/trendmicro/hippo/tools/debugger/SourceProvider;
    //   25: astore #5
    //   27: aload #5
    //   29: ifnull -> 50
    //   32: aload #5
    //   34: aload_1
    //   35: invokeinterface getSource : (Lcom/trendmicro/hippo/debug/DebuggableScript;)Ljava/lang/String;
    //   40: astore_1
    //   41: aload_1
    //   42: ifnull -> 50
    //   45: aload_1
    //   46: astore_2
    //   47: goto -> 50
    //   50: new com/trendmicro/hippo/tools/debugger/Dim$SourceInfo
    //   53: dup
    //   54: aload_2
    //   55: aload #4
    //   57: aload_3
    //   58: aconst_null
    //   59: invokespecial <init> : (Ljava/lang/String;[Lcom/trendmicro/hippo/debug/DebuggableScript;Ljava/lang/String;Lcom/trendmicro/hippo/tools/debugger/Dim$1;)V
    //   62: astore_1
    //   63: aload_0
    //   64: getfield urlToSourceInfo : Ljava/util/Map;
    //   67: astore_2
    //   68: aload_2
    //   69: monitorenter
    //   70: aload_0
    //   71: getfield urlToSourceInfo : Ljava/util/Map;
    //   74: aload_3
    //   75: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   80: checkcast com/trendmicro/hippo/tools/debugger/Dim$SourceInfo
    //   83: astore #5
    //   85: aload #5
    //   87: ifnull -> 96
    //   90: aload_1
    //   91: aload #5
    //   93: invokestatic access$200 : (Lcom/trendmicro/hippo/tools/debugger/Dim$SourceInfo;Lcom/trendmicro/hippo/tools/debugger/Dim$SourceInfo;)V
    //   96: aload_0
    //   97: getfield urlToSourceInfo : Ljava/util/Map;
    //   100: aload_3
    //   101: aload_1
    //   102: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   107: pop
    //   108: iconst_0
    //   109: istore #6
    //   111: iload #6
    //   113: aload_1
    //   114: invokevirtual functionSourcesTop : ()I
    //   117: if_icmpeq -> 160
    //   120: aload_1
    //   121: iload #6
    //   123: invokevirtual functionSource : (I)Lcom/trendmicro/hippo/tools/debugger/Dim$FunctionSource;
    //   126: astore_3
    //   127: aload_3
    //   128: invokevirtual name : ()Ljava/lang/String;
    //   131: astore #5
    //   133: aload #5
    //   135: invokevirtual length : ()I
    //   138: ifeq -> 154
    //   141: aload_0
    //   142: getfield functionNames : Ljava/util/Map;
    //   145: aload #5
    //   147: aload_3
    //   148: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   153: pop
    //   154: iinc #6, 1
    //   157: goto -> 111
    //   160: aload_2
    //   161: monitorexit
    //   162: aload_0
    //   163: getfield functionToSource : Ljava/util/Map;
    //   166: astore_2
    //   167: aload_2
    //   168: monitorenter
    //   169: iconst_0
    //   170: istore #6
    //   172: iload #6
    //   174: aload #4
    //   176: arraylength
    //   177: if_icmpeq -> 209
    //   180: aload_1
    //   181: iload #6
    //   183: invokevirtual functionSource : (I)Lcom/trendmicro/hippo/tools/debugger/Dim$FunctionSource;
    //   186: astore_3
    //   187: aload_0
    //   188: getfield functionToSource : Ljava/util/Map;
    //   191: aload #4
    //   193: iload #6
    //   195: aaload
    //   196: aload_3
    //   197: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   202: pop
    //   203: iinc #6, 1
    //   206: goto -> 172
    //   209: aload_2
    //   210: monitorexit
    //   211: aload_0
    //   212: getfield callback : Lcom/trendmicro/hippo/tools/debugger/GuiCallback;
    //   215: aload_1
    //   216: invokeinterface updateSourceText : (Lcom/trendmicro/hippo/tools/debugger/Dim$SourceInfo;)V
    //   221: return
    //   222: astore_1
    //   223: aload_2
    //   224: monitorexit
    //   225: aload_1
    //   226: athrow
    //   227: astore_1
    //   228: aload_2
    //   229: monitorexit
    //   230: aload_1
    //   231: athrow
    //   232: new java/lang/IllegalArgumentException
    //   235: dup
    //   236: invokespecial <init> : ()V
    //   239: athrow
    // Exception table:
    //   from	to	target	type
    //   70	85	227	finally
    //   90	96	227	finally
    //   96	108	227	finally
    //   111	154	227	finally
    //   160	162	227	finally
    //   172	203	222	finally
    //   209	211	222	finally
    //   223	225	222	finally
    //   228	230	227	finally
  }
  
  public void attachTo(ContextFactory paramContextFactory) {
    detach();
    this.contextFactory = paramContextFactory;
    DimIProxy dimIProxy = new DimIProxy(this, 1);
    this.listener = dimIProxy;
    paramContextFactory.addListener(dimIProxy);
  }
  
  public void clearAllBreakpoints() {
    Iterator<SourceInfo> iterator = this.urlToSourceInfo.values().iterator();
    while (iterator.hasNext())
      ((SourceInfo)iterator.next()).removeAllBreakpoints(); 
  }
  
  public void compileScript(String paramString1, String paramString2) {
    DimIProxy dimIProxy = new DimIProxy(this, 2);
    DimIProxy.access$502(dimIProxy, paramString1);
    DimIProxy.access$602(dimIProxy, paramString2);
    dimIProxy.withContext();
  }
  
  public void contextSwitch(int paramInt) {
    this.frameIndex = paramInt;
  }
  
  public ContextData currentContextData() {
    return this.interruptedContextData;
  }
  
  public void detach() {
    DimIProxy dimIProxy = this.listener;
    if (dimIProxy != null) {
      this.contextFactory.removeListener(dimIProxy);
      this.contextFactory = null;
      this.listener = null;
    } 
  }
  
  public void dispose() {
    detach();
  }
  
  public String eval(String paramString) {
    String str = "undefined";
    if (paramString == null)
      return "undefined"; 
    ContextData contextData = currentContextData();
    if (contextData == null || this.frameIndex >= contextData.frameCount())
      return "undefined"; 
    StackFrame stackFrame = contextData.getFrame(this.frameIndex);
    if (contextData.eventThreadFlag) {
      str = do_eval(Context.getCurrentContext(), stackFrame, paramString);
    } else {
      synchronized (this.monitor) {
        if (this.insideInterruptLoop) {
          this.evalRequest = paramString;
          this.evalFrame = stackFrame;
          this.monitor.notify();
          try {
            do {
              this.monitor.wait();
            } while (this.evalRequest != null);
          } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
          } 
          str = this.evalResult;
        } 
        return str;
      } 
    } 
    return str;
  }
  
  public void evalScript(String paramString1, String paramString2) {
    DimIProxy dimIProxy = new DimIProxy(this, 3);
    DimIProxy.access$502(dimIProxy, paramString1);
    DimIProxy.access$602(dimIProxy, paramString2);
    dimIProxy.withContext();
  }
  
  public String[] functionNames() {
    synchronized (this.urlToSourceInfo) {
      return (String[])this.functionNames.keySet().toArray((Object[])new String[this.functionNames.size()]);
    } 
  }
  
  public FunctionSource functionSourceByName(String paramString) {
    return this.functionNames.get(paramString);
  }
  
  public Object[] getObjectIds(Object paramObject) {
    DimIProxy dimIProxy = new DimIProxy(this, 7);
    DimIProxy.access$802(dimIProxy, paramObject);
    dimIProxy.withContext();
    return dimIProxy.objectArrayResult;
  }
  
  public Object getObjectProperty(Object paramObject1, Object paramObject2) {
    DimIProxy dimIProxy = new DimIProxy(this, 6);
    DimIProxy.access$802(dimIProxy, paramObject1);
    DimIProxy.access$1102(dimIProxy, paramObject2);
    dimIProxy.withContext();
    return dimIProxy.objectResult;
  }
  
  public void go() {
    synchronized (this.monitor) {
      this.returnValue = 3;
      this.monitor.notifyAll();
      return;
    } 
  }
  
  public String objectToString(Object paramObject) {
    DimIProxy dimIProxy = new DimIProxy(this, 5);
    DimIProxy.access$802(dimIProxy, paramObject);
    dimIProxy.withContext();
    return dimIProxy.stringResult;
  }
  
  public void setBreak() {
    this.breakFlag = true;
  }
  
  public void setBreakOnEnter(boolean paramBoolean) {
    this.breakOnEnter = paramBoolean;
  }
  
  public void setBreakOnExceptions(boolean paramBoolean) {
    this.breakOnExceptions = paramBoolean;
  }
  
  public void setBreakOnReturn(boolean paramBoolean) {
    this.breakOnReturn = paramBoolean;
  }
  
  public void setGuiCallback(GuiCallback paramGuiCallback) {
    this.callback = paramGuiCallback;
  }
  
  public void setReturnValue(int paramInt) {
    synchronized (this.monitor) {
      this.returnValue = paramInt;
      this.monitor.notify();
      return;
    } 
  }
  
  public void setScopeProvider(ScopeProvider paramScopeProvider) {
    this.scopeProvider = paramScopeProvider;
  }
  
  public void setSourceProvider(SourceProvider paramSourceProvider) {
    this.sourceProvider = paramSourceProvider;
  }
  
  public SourceInfo sourceInfo(String paramString) {
    return this.urlToSourceInfo.get(paramString);
  }
  
  public boolean stringIsCompilableUnit(String paramString) {
    DimIProxy dimIProxy = new DimIProxy(this, 4);
    DimIProxy.access$602(dimIProxy, paramString);
    dimIProxy.withContext();
    return dimIProxy.booleanResult;
  }
  
  public static class ContextData {
    private boolean breakNextLine;
    
    private boolean eventThreadFlag;
    
    private ObjArray frameStack = new ObjArray();
    
    private Throwable lastProcessedException;
    
    private int stopAtFrameDepth = -1;
    
    public static ContextData get(Context param1Context) {
      return (ContextData)param1Context.getDebuggerContextData();
    }
    
    private void popFrame() {
      this.frameStack.pop();
    }
    
    private void pushFrame(Dim.StackFrame param1StackFrame) {
      this.frameStack.push(param1StackFrame);
    }
    
    public int frameCount() {
      return this.frameStack.size();
    }
    
    public Dim.StackFrame getFrame(int param1Int) {
      int i = this.frameStack.size();
      return (Dim.StackFrame)this.frameStack.get(i - param1Int - 1);
    }
  }
  
  private static class DimIProxy implements ContextAction, ContextFactory.Listener, Debugger {
    private boolean booleanResult;
    
    private Dim dim;
    
    private Object id;
    
    private Object object;
    
    private Object[] objectArrayResult;
    
    private Object objectResult;
    
    private String stringResult;
    
    private String text;
    
    private int type;
    
    private String url;
    
    private DimIProxy(Dim param1Dim, int param1Int) {
      this.dim = param1Dim;
      this.type = param1Int;
    }
    
    private void withContext() {
      this.dim.contextFactory.call(this);
    }
    
    public void contextCreated(Context param1Context) {
      if (this.type != 1)
        Kit.codeBug(); 
      Dim.ContextData contextData = new Dim.ContextData();
      param1Context.setDebugger(new DimIProxy(this.dim, 0), contextData);
      param1Context.setGeneratingDebug(true);
      param1Context.setOptimizationLevel(-1);
    }
    
    public void contextReleased(Context param1Context) {
      if (this.type != 1)
        Kit.codeBug(); 
    }
    
    public DebugFrame getFrame(Context param1Context, DebuggableScript param1DebuggableScript) {
      if (this.type != 0)
        Kit.codeBug(); 
      Dim.FunctionSource functionSource = this.dim.getFunctionSource(param1DebuggableScript);
      return (functionSource == null) ? null : new Dim.StackFrame(param1Context, this.dim, functionSource);
    }
    
    public void handleCompilationDone(Context param1Context, DebuggableScript param1DebuggableScript, String param1String) {
      if (this.type != 0)
        Kit.codeBug(); 
      if (!param1DebuggableScript.isTopLevel())
        return; 
      this.dim.registerTopScript(param1DebuggableScript, param1String);
    }
    
    public Object run(Context param1Context) {
      Object object;
      Scriptable scriptable1;
      Scriptable scriptable2;
      ImporterTopLevel importerTopLevel;
      switch (this.type) {
        default:
          throw Kit.codeBug();
        case 7:
          this.objectArrayResult = this.dim.getObjectIdsImpl(param1Context, this.object);
          return null;
        case 6:
          this.objectResult = this.dim.getObjectPropertyImpl(param1Context, this.object, this.id);
          return null;
        case 5:
          if (this.object == Undefined.instance) {
            this.stringResult = "undefined";
          } else {
            object = this.object;
            if (object == null) {
              this.stringResult = "null";
            } else if (object instanceof com.trendmicro.hippo.NativeCall) {
              this.stringResult = "[object Call]";
            } else {
              this.stringResult = Context.toString(object);
            } 
          } 
          return null;
        case 4:
          this.booleanResult = object.stringIsCompilableUnit(this.text);
          return null;
        case 3:
          scriptable1 = null;
          if (this.dim.scopeProvider != null)
            scriptable1 = this.dim.scopeProvider.getScope(); 
          scriptable2 = scriptable1;
          if (scriptable1 == null)
            importerTopLevel = new ImporterTopLevel((Context)object); 
          object.evaluateString((Scriptable)importerTopLevel, this.text, this.url, 1, null);
          return null;
        case 2:
          break;
      } 
      object.compileString(this.text, this.url, 1, null);
      return null;
    }
  }
  
  public static class FunctionSource {
    private int firstLine;
    
    private String name;
    
    private Dim.SourceInfo sourceInfo;
    
    private FunctionSource(Dim.SourceInfo param1SourceInfo, int param1Int, String param1String) {
      if (param1String != null) {
        this.sourceInfo = param1SourceInfo;
        this.firstLine = param1Int;
        this.name = param1String;
        return;
      } 
      throw new IllegalArgumentException();
    }
    
    public int firstLine() {
      return this.firstLine;
    }
    
    public String name() {
      return this.name;
    }
    
    public Dim.SourceInfo sourceInfo() {
      return this.sourceInfo;
    }
  }
  
  public static class SourceInfo {
    private static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    
    private boolean[] breakableLines;
    
    private boolean[] breakpoints;
    
    private Dim.FunctionSource[] functionSources;
    
    private String source;
    
    private String url;
    
    private SourceInfo(String param1String1, DebuggableScript[] param1ArrayOfDebuggableScript, String param1String2) {
      boolean[] arrayOfBoolean;
      this.source = param1String1;
      this.url = param1String2;
      int i = param1ArrayOfDebuggableScript.length;
      int[][] arrayOfInt = new int[i][];
      int j;
      for (j = 0; j != i; j++)
        arrayOfInt[j] = param1ArrayOfDebuggableScript[j].getLineNumbers(); 
      int k = 0;
      int m = -1;
      int[] arrayOfInt1 = new int[i];
      int n = 0;
      while (n != i) {
        int i1;
        int[] arrayOfInt2 = arrayOfInt[n];
        if (arrayOfInt2 == null || arrayOfInt2.length == 0) {
          arrayOfInt1[n] = -1;
          i1 = m;
        } else {
          i1 = arrayOfInt2[0];
          j = i1;
          int i2 = 1;
          while (i2 != arrayOfInt2.length) {
            int i4;
            int i5;
            int i3 = arrayOfInt2[i2];
            if (i3 < i1) {
              i4 = i3;
              i5 = j;
            } else {
              i4 = i1;
              i5 = j;
              if (i3 > j) {
                i5 = i3;
                i4 = i1;
              } 
            } 
            i2++;
            i1 = i4;
            j = i5;
          } 
          arrayOfInt1[n] = i1;
          if (k > m) {
            k = i1;
            i1 = j;
          } else {
            i2 = k;
            if (i1 < k)
              i2 = i1; 
            k = i2;
            i1 = m;
            if (j > m) {
              k = i2;
              i1 = j;
            } 
          } 
        } 
        n++;
        m = i1;
      } 
      if (k > m) {
        arrayOfBoolean = EMPTY_BOOLEAN_ARRAY;
        this.breakableLines = arrayOfBoolean;
        this.breakpoints = arrayOfBoolean;
      } else {
        boolean bool;
        if (k >= 0) {
          j = m + 1;
          this.breakableLines = new boolean[j];
          this.breakpoints = new boolean[j];
          for (j = 0; j != i; j++) {
            boolean bool1 = arrayOfBoolean[j];
            if (bool1 != null && bool1.length != 0)
              for (byte b = 0; b != bool1.length; b++) {
                bool = bool1[b];
                this.breakableLines[bool] = true;
              }  
          } 
        } else {
          throw new IllegalStateException(String.valueOf(bool));
        } 
      } 
      this.functionSources = new Dim.FunctionSource[i];
      for (j = 0; j != i; j++) {
        param1String2 = param1ArrayOfDebuggableScript[j].getFunctionName();
        String str = param1String2;
        if (param1String2 == null)
          str = ""; 
        this.functionSources[j] = new Dim.FunctionSource(this, arrayOfInt1[j], str);
      } 
    }
    
    private void copyBreakpointsFrom(SourceInfo param1SourceInfo) {
      int i = param1SourceInfo.breakpoints.length;
      boolean[] arrayOfBoolean = this.breakpoints;
      int j = i;
      if (i > arrayOfBoolean.length)
        j = arrayOfBoolean.length; 
      for (i = 0; i != j; i++) {
        if (param1SourceInfo.breakpoints[i])
          this.breakpoints[i] = true; 
      } 
    }
    
    public boolean breakableLine(int param1Int) {
      boolean bool;
      boolean[] arrayOfBoolean = this.breakableLines;
      if (param1Int < arrayOfBoolean.length && arrayOfBoolean[param1Int]) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean breakpoint(int param1Int) {
      if (breakableLine(param1Int)) {
        boolean bool;
        boolean[] arrayOfBoolean = this.breakpoints;
        if (param1Int < arrayOfBoolean.length && arrayOfBoolean[param1Int]) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      } 
      throw new IllegalArgumentException(String.valueOf(param1Int));
    }
    
    public boolean breakpoint(int param1Int, boolean param1Boolean) {
      if (breakableLine(param1Int))
        synchronized (this.breakpoints) {
          if (this.breakpoints[param1Int] != param1Boolean) {
            this.breakpoints[param1Int] = param1Boolean;
            param1Boolean = true;
          } else {
            param1Boolean = false;
          } 
          return param1Boolean;
        }  
      throw new IllegalArgumentException(String.valueOf(param1Int));
    }
    
    public Dim.FunctionSource functionSource(int param1Int) {
      return this.functionSources[param1Int];
    }
    
    public int functionSourcesTop() {
      return this.functionSources.length;
    }
    
    public void removeAllBreakpoints() {
      // Byte code:
      //   0: aload_0
      //   1: getfield breakpoints : [Z
      //   4: astore_1
      //   5: aload_1
      //   6: monitorenter
      //   7: iconst_0
      //   8: istore_2
      //   9: iload_2
      //   10: aload_0
      //   11: getfield breakpoints : [Z
      //   14: arraylength
      //   15: if_icmpeq -> 31
      //   18: aload_0
      //   19: getfield breakpoints : [Z
      //   22: iload_2
      //   23: iconst_0
      //   24: bastore
      //   25: iinc #2, 1
      //   28: goto -> 9
      //   31: aload_1
      //   32: monitorexit
      //   33: return
      //   34: astore_3
      //   35: aload_1
      //   36: monitorexit
      //   37: aload_3
      //   38: athrow
      // Exception table:
      //   from	to	target	type
      //   9	25	34	finally
      //   31	33	34	finally
      //   35	37	34	finally
    }
    
    public String source() {
      return this.source;
    }
    
    public String url() {
      return this.url;
    }
  }
  
  public static class StackFrame implements DebugFrame {
    private boolean[] breakpoints;
    
    private Dim.ContextData contextData;
    
    private Dim dim;
    
    private Dim.FunctionSource fsource;
    
    private int lineNumber;
    
    private Scriptable scope;
    
    private Scriptable thisObj;
    
    private StackFrame(Context param1Context, Dim param1Dim, Dim.FunctionSource param1FunctionSource) {
      this.dim = param1Dim;
      this.contextData = Dim.ContextData.get(param1Context);
      this.fsource = param1FunctionSource;
      this.breakpoints = (param1FunctionSource.sourceInfo()).breakpoints;
      this.lineNumber = param1FunctionSource.firstLine();
    }
    
    public Dim.ContextData contextData() {
      return this.contextData;
    }
    
    public String getFunctionName() {
      return this.fsource.name();
    }
    
    public int getLineNumber() {
      return this.lineNumber;
    }
    
    public String getUrl() {
      return this.fsource.sourceInfo().url();
    }
    
    public void onDebuggerStatement(Context param1Context) {
      this.dim.handleBreakpointHit(this, param1Context);
    }
    
    public void onEnter(Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject) {
      this.contextData.pushFrame(this);
      this.scope = param1Scriptable1;
      this.thisObj = param1Scriptable2;
      if (this.dim.breakOnEnter)
        this.dim.handleBreakpointHit(this, param1Context); 
    }
    
    public void onExceptionThrown(Context param1Context, Throwable param1Throwable) {
      this.dim.handleExceptionThrown(param1Context, param1Throwable, this);
    }
    
    public void onExit(Context param1Context, boolean param1Boolean, Object param1Object) {
      if (this.dim.breakOnReturn && !param1Boolean)
        this.dim.handleBreakpointHit(this, param1Context); 
      this.contextData.popFrame();
    }
    
    public void onLineChange(Context param1Context, int param1Int) {
      this.lineNumber = param1Int;
      if (!this.breakpoints[param1Int] && !this.dim.breakFlag) {
        boolean bool1 = this.contextData.breakNextLine;
        boolean bool2 = bool1;
        if (bool1) {
          bool2 = bool1;
          if (this.contextData.stopAtFrameDepth >= 0)
            if (this.contextData.frameCount() <= this.contextData.stopAtFrameDepth) {
              bool2 = true;
            } else {
              bool2 = false;
            }  
        } 
        if (!bool2)
          return; 
        Dim.ContextData.access$1502(this.contextData, -1);
        Dim.ContextData.access$1402(this.contextData, false);
      } 
      this.dim.handleBreakpointHit(this, param1Context);
    }
    
    public Object scope() {
      return this.scope;
    }
    
    public Dim.SourceInfo sourceInfo() {
      return this.fsource.sourceInfo();
    }
    
    public Object thisObj() {
      return this.thisObj;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/debugger/Dim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */