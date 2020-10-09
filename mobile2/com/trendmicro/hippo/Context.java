package com.trendmicro.hippo;

import com.trendmicro.classfile.ClassFileWriter;
import com.trendmicro.hippo.ast.AstRoot;
import com.trendmicro.hippo.ast.ScriptNode;
import com.trendmicro.hippo.debug.DebuggableScript;
import com.trendmicro.hippo.debug.Debugger;
import com.trendmicro.hippo.xml.XMLLib;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class Context {
  public static final int FEATURE_DYNAMIC_SCOPE = 7;
  
  public static final int FEATURE_E4X = 6;
  
  public static final int FEATURE_ENHANCED_JAVA_ACCESS = 13;
  
  public static final int FEATURE_ENUMERATE_IDS_FIRST = 16;
  
  public static final int FEATURE_INTEGER_WITHOUT_DECIMAL_PLACE = 18;
  
  public static final int FEATURE_LITTLE_ENDIAN = 19;
  
  public static final int FEATURE_LOCATION_INFORMATION_IN_ERROR = 10;
  
  public static final int FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME = 2;
  
  public static final int FEATURE_NON_ECMA_GET_YEAR = 1;
  
  public static final int FEATURE_OLD_UNDEF_NULL_THIS = 15;
  
  public static final int FEATURE_PARENT_PROTO_PROPERTIES = 5;
  
  @Deprecated
  public static final int FEATURE_PARENT_PROTO_PROPRTIES = 5;
  
  public static final int FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER = 3;
  
  public static final int FEATURE_STRICT_EVAL = 9;
  
  public static final int FEATURE_STRICT_MODE = 11;
  
  public static final int FEATURE_STRICT_VARS = 8;
  
  public static final int FEATURE_THREAD_SAFE_OBJECTS = 17;
  
  public static final int FEATURE_TO_STRING_AS_SOURCE = 4;
  
  public static final int FEATURE_V8_EXTENSIONS = 14;
  
  public static final int FEATURE_WARNING_AS_ERROR = 12;
  
  public static final int VERSION_1_0 = 100;
  
  public static final int VERSION_1_1 = 110;
  
  public static final int VERSION_1_2 = 120;
  
  public static final int VERSION_1_3 = 130;
  
  public static final int VERSION_1_4 = 140;
  
  public static final int VERSION_1_5 = 150;
  
  public static final int VERSION_1_6 = 160;
  
  public static final int VERSION_1_7 = 170;
  
  public static final int VERSION_1_8 = 180;
  
  public static final int VERSION_DEFAULT = 0;
  
  public static final int VERSION_ES6 = 200;
  
  public static final int VERSION_UNKNOWN = -1;
  
  private static Class<?> codegenClass;
  
  public static final Object[] emptyArgs = ScriptRuntime.emptyArgs;
  
  public static final String errorReporterProperty = "error reporter";
  
  private static String implementationVersion;
  
  private static Class<?> interpreterClass;
  
  public static final String languageVersionProperty = "language version";
  
  Set<String> activationNames;
  
  private ClassLoader applicationClassLoader;
  
  XMLLib cachedXMLLib;
  
  private ClassShutter classShutter;
  
  NativeCall currentActivationCall;
  
  Debugger debugger;
  
  private Object debuggerData;
  
  private int enterCount;
  
  private ErrorReporter errorReporter;
  
  private final ContextFactory factory;
  
  public boolean generateObserverCount;
  
  private boolean generatingDebug;
  
  private boolean generatingDebugChanged;
  
  private boolean generatingSource = true;
  
  private boolean hasClassShutter;
  
  int instructionCount;
  
  int instructionThreshold;
  
  Object interpreterSecurityDomain;
  
  boolean isContinuationsTopCall;
  
  boolean isTopLevelStrict;
  
  ObjToIntMap iterating;
  
  Object lastInterpreterFrame;
  
  private Locale locale;
  
  private int maximumInterpreterStackDepth;
  
  private int optimizationLevel;
  
  ObjArray previousInterpreterInvocations;
  
  private Object propertyListeners;
  
  RegExpProxy regExpProxy;
  
  int scratchIndex;
  
  Scriptable scratchScriptable;
  
  long scratchUint32;
  
  private Object sealKey;
  
  private boolean sealed;
  
  private SecurityController securityController;
  
  private Map<Object, Object> threadLocalMap;
  
  Scriptable topCallScope;
  
  BaseFunction typeErrorThrower;
  
  boolean useDynamicScope;
  
  int version;
  
  private WrapFactory wrapFactory;
  
  static {
    codegenClass = Kit.classOrNull("com.trendmicro.hippo.optimizer.Codegen");
    interpreterClass = Kit.classOrNull("com.trendmicro.hippo.Interpreter");
  }
  
  @Deprecated
  public Context() {
    this(ContextFactory.getGlobal());
  }
  
  protected Context(ContextFactory paramContextFactory) {
    byte b = 0;
    this.generateObserverCount = false;
    if (paramContextFactory != null) {
      this.factory = paramContextFactory;
      this.version = 0;
      if (codegenClass == null)
        b = -1; 
      this.optimizationLevel = b;
      this.maximumInterpreterStackDepth = Integer.MAX_VALUE;
      return;
    } 
    throw new IllegalArgumentException("factory == null");
  }
  
  @Deprecated
  public static void addContextListener(ContextListener paramContextListener) {
    if ("com.trendmicro.hippo.tools.debugger.Main".equals(paramContextListener.getClass().getName())) {
      Class<?> clazz1 = paramContextListener.getClass();
      Class<?> clazz2 = Kit.classOrNull("com.trendmicro.hippo.ContextFactory");
      ContextFactory contextFactory = ContextFactory.getGlobal();
      try {
        clazz1.getMethod("attachTo", new Class[] { clazz2 }).invoke(paramContextListener, new Object[] { contextFactory });
        return;
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      } 
    } 
    ContextFactory.getGlobal().addListener((ContextFactory.Listener)exception);
  }
  
  @Deprecated
  public static <T> T call(ContextAction<T> paramContextAction) {
    return call(ContextFactory.getGlobal(), paramContextAction);
  }
  
  public static Object call(ContextFactory paramContextFactory, Callable paramCallable, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    ContextFactory contextFactory = paramContextFactory;
    if (paramContextFactory == null)
      contextFactory = ContextFactory.getGlobal(); 
    return call(contextFactory, paramContext -> paramCallable.call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject));
  }
  
  static <T> T call(ContextFactory paramContextFactory, ContextAction<T> paramContextAction) {
    null = enter(null, paramContextFactory);
    try {
      null = (Context)paramContextAction.run(null);
      return (T)null;
    } finally {
      exit();
    } 
  }
  
  public static void checkLanguageVersion(int paramInt) {
    if (isValidLanguageVersion(paramInt))
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad language version: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static void checkOptimizationLevel(int paramInt) {
    if (isValidOptimizationLevel(paramInt))
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Optimization level outside [-1..9]: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private Object compileImpl(Scriptable paramScriptable, Reader paramReader, String paramString1, String paramString2, int paramInt, Object paramObject, boolean paramBoolean, Evaluator paramEvaluator, ErrorReporter paramErrorReporter) throws IOException {
    if (paramString2 == null)
      paramString2 = "unnamed script"; 
    if (paramObject == null || getSecurityController() != null) {
      Script script;
      Evaluator evaluator;
      Object object;
      boolean bool2;
      boolean bool3;
      Reader reader;
      boolean bool1 = true;
      if (paramReader == null) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (paramString1 == null) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      if ((bool2 ^ bool3) == 0)
        Kit.codeBug(); 
      if (paramScriptable == null) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      if (!(bool2 ^ paramBoolean))
        Kit.codeBug(); 
      CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
      compilerEnvirons.initFromContext(this);
      if (paramErrorReporter == null)
        paramErrorReporter = compilerEnvirons.getErrorReporter(); 
      if (this.debugger != null && paramReader != null) {
        paramString1 = Kit.readReader(paramReader);
        reader = null;
      } else {
        reader = paramReader;
      } 
      ScriptNode scriptNode = parse(reader, paramString1, paramString2, paramInt, compilerEnvirons, paramErrorReporter, paramBoolean);
      if (paramEvaluator == null) {
        try {
          evaluator = createCompiler();
        } catch (com.trendmicro.classfile.ClassFileWriter.ClassFileFormatException classFileFormatException) {}
      } else {
        evaluator = paramEvaluator;
      } 
      try {
        Object object1 = evaluator.compile(compilerEnvirons, scriptNode, scriptNode.getEncodedSource(), paramBoolean);
        object = object1;
      } catch (com.trendmicro.classfile.ClassFileWriter.ClassFileFormatException classFileFormatException) {
        evaluator = createInterpreter();
        object = parse(reader, paramString1, (String)object, paramInt, compilerEnvirons, paramErrorReporter, paramBoolean);
        object = evaluator.compile(compilerEnvirons, (ScriptNode)object, object.getEncodedSource(), paramBoolean);
      } 
      if (this.debugger != null) {
        if (paramString1 == null)
          Kit.codeBug(); 
        if (object instanceof DebuggableScript) {
          notifyDebugger_r(this, (DebuggableScript)object, paramString1);
        } else {
          throw new RuntimeException("NOT SUPPORTED");
        } 
      } 
      if (paramBoolean) {
        paramScriptable = evaluator.createFunctionObject(this, paramScriptable, object, paramObject);
      } else {
        script = evaluator.createScriptObject(object, paramObject);
      } 
      return script;
    } 
    throw new IllegalArgumentException("securityDomain should be null if setSecurityController() was never called");
  }
  
  private Evaluator createCompiler() {
    Evaluator evaluator1 = null;
    Evaluator evaluator2 = evaluator1;
    if (this.optimizationLevel >= 0) {
      Class<?> clazz = codegenClass;
      evaluator2 = evaluator1;
      if (clazz != null)
        evaluator2 = (Evaluator)Kit.newInstanceOrNull(clazz); 
    } 
    evaluator1 = evaluator2;
    if (evaluator2 == null)
      evaluator1 = createInterpreter(); 
    return evaluator1;
  }
  
  static Evaluator createInterpreter() {
    return (Evaluator)Kit.newInstanceOrNull(interpreterClass);
  }
  
  public static Context enter() {
    return enter(null);
  }
  
  @Deprecated
  public static Context enter(Context paramContext) {
    return enter(paramContext, ContextFactory.getGlobal());
  }
  
  static final Context enter(Context paramContext, ContextFactory paramContextFactory) {
    Object object = VMBridge.instance.getThreadContextHelper();
    Context context = VMBridge.instance.getContext(object);
    if (context != null) {
      paramContext = context;
    } else {
      if (paramContext == null) {
        context = paramContextFactory.makeContext();
        if (context.enterCount == 0) {
          paramContextFactory.onContextCreated(context);
          paramContext = context;
          if (paramContextFactory.isSealed()) {
            paramContext = context;
            if (!context.isSealed()) {
              context.seal(null);
              paramContext = context;
            } 
          } 
        } else {
          throw new IllegalStateException("factory.makeContext() returned Context instance already associated with some thread");
        } 
      } else if (paramContext.enterCount != 0) {
        throw new IllegalStateException("can not use Context instance already associated with some thread");
      } 
      VMBridge.instance.setContext(object, paramContext);
    } 
    paramContext.enterCount++;
    return paramContext;
  }
  
  public static void exit() {
    Object object = VMBridge.instance.getThreadContextHelper();
    Context context = VMBridge.instance.getContext(object);
    if (context != null) {
      if (context.enterCount < 1)
        Kit.codeBug(); 
      int i = context.enterCount - 1;
      context.enterCount = i;
      if (i == 0) {
        VMBridge.instance.setContext(object, null);
        context.factory.onContextReleased(context);
      } 
      return;
    } 
    throw new IllegalStateException("Calling Context.exit without previous Context.enter");
  }
  
  private void firePropertyChangeImpl(Object paramObject1, String paramString, Object paramObject2, Object paramObject3) {
    for (byte b = 0;; b++) {
      Object object = Kit.getListener(paramObject1, b);
      if (object == null)
        return; 
      if (object instanceof PropertyChangeListener)
        ((PropertyChangeListener)object).propertyChange(new PropertyChangeEvent(this, paramString, paramObject2, paramObject3)); 
    } 
  }
  
  static Context getContext() {
    Context context = getCurrentContext();
    if (context != null)
      return context; 
    throw new RuntimeException("No Context associated with current Thread");
  }
  
  public static Context getCurrentContext() {
    Object object = VMBridge.instance.getThreadContextHelper();
    return VMBridge.instance.getContext(object);
  }
  
  public static DebuggableScript getDebuggableView(Script paramScript) {
    return (paramScript instanceof NativeFunction) ? ((NativeFunction)paramScript).getDebuggableView() : null;
  }
  
  static String getSourcePositionFromStack(int[] paramArrayOfint) {
    Context context = getCurrentContext();
    if (context == null)
      return null; 
    if (context.lastInterpreterFrame != null) {
      Evaluator evaluator = createInterpreter();
      if (evaluator != null)
        return evaluator.getSourcePositionFromStack(context, paramArrayOfint); 
    } 
    for (StackTraceElement stackTraceElement : (new Throwable()).getStackTrace()) {
      String str = stackTraceElement.getFileName();
      if (str != null && !str.endsWith(".java")) {
        int i = stackTraceElement.getLineNumber();
        if (i >= 0) {
          paramArrayOfint[0] = i;
          return str;
        } 
      } 
    } 
    return null;
  }
  
  public static Object getUndefinedValue() {
    return Undefined.instance;
  }
  
  public static boolean isValidLanguageVersion(int paramInt) {
    switch (paramInt) {
      default:
        return false;
      case 0:
      case 100:
      case 110:
      case 120:
      case 130:
      case 140:
      case 150:
      case 160:
      case 170:
      case 180:
      case 200:
        break;
    } 
    return true;
  }
  
  public static boolean isValidOptimizationLevel(int paramInt) {
    boolean bool;
    if (-1 <= paramInt && paramInt <= 9) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static Object javaToJS(Object paramObject, Scriptable paramScriptable) {
    if (paramObject instanceof String || paramObject instanceof Number || paramObject instanceof Boolean || paramObject instanceof Scriptable)
      return paramObject; 
    if (paramObject instanceof Character)
      return String.valueOf(((Character)paramObject).charValue()); 
    Context context = getContext();
    return context.getWrapFactory().wrap(context, paramScriptable, paramObject, null);
  }
  
  public static Object jsToJava(Object paramObject, Class<?> paramClass) throws EvaluatorException {
    return NativeJavaObject.coerceTypeImpl(paramClass, paramObject);
  }
  
  private static void notifyDebugger_r(Context paramContext, DebuggableScript paramDebuggableScript, String paramString) {
    paramContext.debugger.handleCompilationDone(paramContext, paramDebuggableScript, paramString);
    for (byte b = 0; b != paramDebuggableScript.getFunctionCount(); b++)
      notifyDebugger_r(paramContext, paramDebuggableScript.getFunction(b), paramString); 
  }
  
  static void onSealedMutation() {
    throw new IllegalStateException();
  }
  
  private ScriptNode parse(Reader paramReader, String paramString1, String paramString2, int paramInt, CompilerEnvirons paramCompilerEnvirons, ErrorReporter paramErrorReporter, boolean paramBoolean) throws IOException {
    AstRoot astRoot;
    Parser parser = new Parser(paramCompilerEnvirons, paramErrorReporter);
    if (paramBoolean)
      parser.calledByCompileFunction = true; 
    if (isStrictMode())
      parser.setDefaultUseStrictDirective(true); 
    if (paramString1 != null) {
      astRoot = parser.parse(paramString1, paramString2, paramInt);
    } else {
      astRoot = parser.parse((Reader)astRoot, paramString2, paramInt);
    } 
    if (!paramBoolean || (astRoot.getFirstChild() != null && astRoot.getFirstChild().getType() == 110))
      return (new IRFactory(paramCompilerEnvirons, paramErrorReporter)).transformTree(astRoot); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("compileFunction only accepts source with single JS function: ");
    stringBuilder.append(paramString1);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @Deprecated
  public static void removeContextListener(ContextListener paramContextListener) {
    ContextFactory.getGlobal().addListener(paramContextListener);
  }
  
  public static void reportError(String paramString) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    reportError(paramString, getSourcePositionFromStack(arrayOfInt), arrayOfInt[0], null, 0);
  }
  
  public static void reportError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    Context context = getCurrentContext();
    if (context != null) {
      context.getErrorReporter().error(paramString1, paramString2, paramInt1, paramString3, paramInt2);
      return;
    } 
    throw new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
  }
  
  public static EvaluatorException reportRuntimeError(String paramString) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    return reportRuntimeError(paramString, getSourcePositionFromStack(arrayOfInt), arrayOfInt[0], null, 0);
  }
  
  public static EvaluatorException reportRuntimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    Context context = getCurrentContext();
    if (context != null)
      return context.getErrorReporter().runtimeError(paramString1, paramString2, paramInt1, paramString3, paramInt2); 
    throw new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
  }
  
  static EvaluatorException reportRuntimeError0(String paramString) {
    return reportRuntimeError(ScriptRuntime.getMessage0(paramString));
  }
  
  static EvaluatorException reportRuntimeError1(String paramString, Object paramObject) {
    return reportRuntimeError(ScriptRuntime.getMessage1(paramString, paramObject));
  }
  
  static EvaluatorException reportRuntimeError2(String paramString, Object paramObject1, Object paramObject2) {
    return reportRuntimeError(ScriptRuntime.getMessage2(paramString, paramObject1, paramObject2));
  }
  
  static EvaluatorException reportRuntimeError3(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
    return reportRuntimeError(ScriptRuntime.getMessage3(paramString, paramObject1, paramObject2, paramObject3));
  }
  
  static EvaluatorException reportRuntimeError4(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) {
    return reportRuntimeError(ScriptRuntime.getMessage4(paramString, paramObject1, paramObject2, paramObject3, paramObject4));
  }
  
  public static void reportWarning(String paramString) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    reportWarning(paramString, getSourcePositionFromStack(arrayOfInt), arrayOfInt[0], null, 0);
  }
  
  public static void reportWarning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    Context context = getContext();
    if (context.hasFeature(12)) {
      reportError(paramString1, paramString2, paramInt1, paramString3, paramInt2);
    } else {
      context.getErrorReporter().warning(paramString1, paramString2, paramInt1, paramString3, paramInt2);
    } 
  }
  
  public static void reportWarning(String paramString, Throwable paramThrowable) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    String str = getSourcePositionFromStack(arrayOfInt);
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    printWriter.println(paramString);
    paramThrowable.printStackTrace(printWriter);
    printWriter.flush();
    reportWarning(stringWriter.toString(), str, arrayOfInt[0], null, 0);
  }
  
  @Deprecated
  public static void setCachingEnabled(boolean paramBoolean) {}
  
  public static RuntimeException throwAsScriptRuntimeEx(Throwable paramThrowable) {
    while (paramThrowable instanceof InvocationTargetException)
      paramThrowable = ((InvocationTargetException)paramThrowable).getTargetException(); 
    if (paramThrowable instanceof Error) {
      Context context = getContext();
      if (context == null || !context.hasFeature(13))
        throw (Error)paramThrowable; 
    } 
    if (paramThrowable instanceof HippoException)
      throw (HippoException)paramThrowable; 
    throw new WrappedException(paramThrowable);
  }
  
  public static boolean toBoolean(Object paramObject) {
    return ScriptRuntime.toBoolean(paramObject);
  }
  
  public static double toNumber(Object paramObject) {
    return ScriptRuntime.toNumber(paramObject);
  }
  
  public static Scriptable toObject(Object paramObject, Scriptable paramScriptable) {
    return ScriptRuntime.toObject(paramScriptable, paramObject);
  }
  
  @Deprecated
  public static Scriptable toObject(Object paramObject, Scriptable paramScriptable, Class<?> paramClass) {
    return ScriptRuntime.toObject(paramScriptable, paramObject);
  }
  
  public static String toString(Object paramObject) {
    return ScriptRuntime.toString(paramObject);
  }
  
  @Deprecated
  public static Object toType(Object paramObject, Class<?> paramClass) throws IllegalArgumentException {
    try {
      return jsToJava(paramObject, paramClass);
    } catch (EvaluatorException evaluatorException) {
      throw new IllegalArgumentException(evaluatorException.getMessage(), evaluatorException);
    } 
  }
  
  public void addActivationName(String paramString) {
    if (this.sealed)
      onSealedMutation(); 
    if (this.activationNames == null)
      this.activationNames = new HashSet<>(); 
    this.activationNames.add(paramString);
  }
  
  public final void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
    if (this.sealed)
      onSealedMutation(); 
    this.propertyListeners = Kit.addListener(this.propertyListeners, paramPropertyChangeListener);
  }
  
  public Object callFunctionWithContinuations(Callable paramCallable, Scriptable paramScriptable, Object[] paramArrayOfObject) throws ContinuationPending {
    if (paramCallable instanceof InterpretedFunction) {
      if (!ScriptRuntime.hasTopCall(this)) {
        this.isContinuationsTopCall = true;
        return ScriptRuntime.doTopCall(paramCallable, this, paramScriptable, paramScriptable, paramArrayOfObject, this.isTopLevelStrict);
      } 
      throw new IllegalStateException("Cannot have any pending top calls when executing a script with continuations");
    } 
    throw new IllegalArgumentException("Function argument was not created by interpreted mode ");
  }
  
  public ContinuationPending captureContinuation() {
    return new ContinuationPending(Interpreter.captureContinuation(this));
  }
  
  final Function compileFunction(Scriptable paramScriptable, String paramString1, Evaluator paramEvaluator, ErrorReporter paramErrorReporter, String paramString2, int paramInt, Object paramObject) {
    try {
      return (Function)compileImpl(paramScriptable, null, paramString1, paramString2, paramInt, paramObject, true, paramEvaluator, paramErrorReporter);
    } catch (IOException iOException) {
      throw new RuntimeException(iOException);
    } 
  }
  
  public final Function compileFunction(Scriptable paramScriptable, String paramString1, String paramString2, int paramInt, Object paramObject) {
    return compileFunction(paramScriptable, paramString1, null, null, paramString2, paramInt, paramObject);
  }
  
  @Deprecated
  public final Script compileReader(Scriptable paramScriptable, Reader paramReader, String paramString, int paramInt, Object paramObject) throws IOException {
    return compileReader(paramReader, paramString, paramInt, paramObject);
  }
  
  public final Script compileReader(Reader paramReader, String paramString, int paramInt, Object paramObject) throws IOException {
    int i = paramInt;
    if (paramInt < 0)
      i = 0; 
    return (Script)compileImpl(null, paramReader, null, paramString, i, paramObject, false, null, null);
  }
  
  final Script compileString(String paramString1, Evaluator paramEvaluator, ErrorReporter paramErrorReporter, String paramString2, int paramInt, Object paramObject) {
    try {
      return (Script)compileImpl(null, null, paramString1, paramString2, paramInt, paramObject, false, paramEvaluator, paramErrorReporter);
    } catch (IOException iOException) {
      throw new RuntimeException(iOException);
    } 
  }
  
  public final Script compileString(String paramString1, String paramString2, int paramInt, Object paramObject) {
    int i = paramInt;
    if (paramInt < 0)
      i = 0; 
    return compileString(paramString1, null, null, paramString2, i, paramObject);
  }
  
  public GeneratedClassLoader createClassLoader(ClassLoader paramClassLoader) {
    return getFactory().createClassLoader(paramClassLoader);
  }
  
  public final String decompileFunction(Function paramFunction, int paramInt) {
    if (paramFunction instanceof BaseFunction)
      return ((BaseFunction)paramFunction).decompile(paramInt, 0); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("function ");
    stringBuilder.append(paramFunction.getClassName());
    stringBuilder.append("() {\n\t[native code]\n}\n");
    return stringBuilder.toString();
  }
  
  public final String decompileFunctionBody(Function paramFunction, int paramInt) {
    return (paramFunction instanceof BaseFunction) ? ((BaseFunction)paramFunction).decompile(paramInt, 1) : "[native code]\n";
  }
  
  public final String decompileScript(Script paramScript, int paramInt) {
    return ((NativeFunction)paramScript).decompile(paramInt, 0);
  }
  
  public final Object evaluateReader(Scriptable paramScriptable, Reader paramReader, String paramString, int paramInt, Object paramObject) throws IOException {
    Script script = compileReader(paramScriptable, paramReader, paramString, paramInt, paramObject);
    return (script != null) ? script.exec(this, paramScriptable) : null;
  }
  
  public final Object evaluateString(Scriptable paramScriptable, String paramString1, String paramString2, int paramInt, Object paramObject) {
    Script script = compileString(paramString1, paramString2, paramInt, paramObject);
    return (script != null) ? script.exec(this, paramScriptable) : null;
  }
  
  public Object executeScriptWithContinuations(Script paramScript, Scriptable paramScriptable) throws ContinuationPending {
    if (paramScript instanceof InterpretedFunction && ((InterpretedFunction)paramScript).isScript())
      return callFunctionWithContinuations((InterpretedFunction)paramScript, paramScriptable, ScriptRuntime.emptyArgs); 
    throw new IllegalArgumentException("Script argument was not a script or was not created by interpreted mode ");
  }
  
  final void firePropertyChange(String paramString, Object paramObject1, Object paramObject2) {
    Object object = this.propertyListeners;
    if (object != null)
      firePropertyChangeImpl(object, paramString, paramObject1, paramObject2); 
  }
  
  public final ClassLoader getApplicationClassLoader() {
    if (this.applicationClassLoader == null) {
      ContextFactory contextFactory = getFactory();
      ClassLoader classLoader1 = contextFactory.getApplicationClassLoader();
      ClassLoader classLoader2 = classLoader1;
      if (classLoader1 == null) {
        classLoader2 = Thread.currentThread().getContextClassLoader();
        if (classLoader2 != null && Kit.testIfCanLoadHippoClasses(classLoader2))
          return classLoader2; 
        Class<?> clazz = contextFactory.getClass();
        if (clazz != ScriptRuntime.ContextFactoryClass) {
          ClassLoader classLoader = clazz.getClassLoader();
        } else {
          classLoader2 = getClass().getClassLoader();
        } 
      } 
      this.applicationClassLoader = classLoader2;
    } 
    return this.applicationClassLoader;
  }
  
  final ClassShutter getClassShutter() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield classShutter : Lcom/trendmicro/hippo/ClassShutter;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public final ClassShutterSetter getClassShutterSetter() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield hasClassShutter : Z
    //   6: istore_1
    //   7: iload_1
    //   8: ifeq -> 15
    //   11: aload_0
    //   12: monitorexit
    //   13: aconst_null
    //   14: areturn
    //   15: aload_0
    //   16: iconst_1
    //   17: putfield hasClassShutter : Z
    //   20: new com/trendmicro/hippo/Context$1
    //   23: dup
    //   24: aload_0
    //   25: invokespecial <init> : (Lcom/trendmicro/hippo/Context;)V
    //   28: astore_2
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_2
    //   32: areturn
    //   33: astore_2
    //   34: aload_0
    //   35: monitorexit
    //   36: aload_2
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	33	finally
    //   15	29	33	finally
  }
  
  public final Debugger getDebugger() {
    return this.debugger;
  }
  
  public final Object getDebuggerContextData() {
    return this.debuggerData;
  }
  
  public XMLLib.Factory getE4xImplementationFactory() {
    return getFactory().getE4xImplementationFactory();
  }
  
  public final Object[] getElements(Scriptable paramScriptable) {
    return ScriptRuntime.getArrayElements(paramScriptable);
  }
  
  public final ErrorReporter getErrorReporter() {
    ErrorReporter errorReporter = this.errorReporter;
    return (errorReporter == null) ? DefaultErrorReporter.instance : errorReporter;
  }
  
  public final ContextFactory getFactory() {
    return this.factory;
  }
  
  public final String getImplementationVersion() {
    if (implementationVersion == null)
      try {
        Enumeration<URL> enumeration = Context.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        while (enumeration.hasMoreElements()) {
          URL uRL = enumeration.nextElement();
          null = null;
          InputStream inputStream = null;
          try {
            InputStream inputStream1 = uRL.openStream();
            inputStream = inputStream1;
            null = inputStream1;
            Manifest manifest = new Manifest();
            inputStream = inputStream1;
            null = inputStream1;
            this(inputStream1);
            inputStream = inputStream1;
            null = inputStream1;
            Attributes attributes = manifest.getMainAttributes();
            inputStream = inputStream1;
            null = inputStream1;
            if ("Hippo".equals(attributes.getValue("Implementation-Title"))) {
              inputStream = inputStream1;
              null = inputStream1;
              StringBuilder stringBuilder = new StringBuilder();
              inputStream = inputStream1;
              null = inputStream1;
              this();
              inputStream = inputStream1;
              null = inputStream1;
              stringBuilder.append("Hippo ");
              inputStream = inputStream1;
              null = inputStream1;
              stringBuilder.append(attributes.getValue("Implementation-Version"));
              inputStream = inputStream1;
              null = inputStream1;
              stringBuilder.append(" ");
              inputStream = inputStream1;
              null = inputStream1;
              stringBuilder.append(attributes.getValue("Built-Date").replaceAll("-", " "));
              inputStream = inputStream1;
              null = inputStream1;
              String str = stringBuilder.toString();
              inputStream = inputStream1;
              null = inputStream1;
              implementationVersion = str;
              return str;
            } 
          } catch (IOException iOException) {
          
          } finally {
            if (iOException != null)
              try {
                iOException.close();
              } catch (IOException iOException1) {} 
          } 
        } 
      } catch (IOException iOException) {
        return null;
      }  
    return implementationVersion;
  }
  
  public final int getInstructionObserverThreshold() {
    return this.instructionThreshold;
  }
  
  public final int getLanguageVersion() {
    return this.version;
  }
  
  public final Locale getLocale() {
    if (this.locale == null)
      this.locale = Locale.getDefault(); 
    return this.locale;
  }
  
  public final int getMaximumInterpreterStackDepth() {
    return this.maximumInterpreterStackDepth;
  }
  
  public final int getOptimizationLevel() {
    return this.optimizationLevel;
  }
  
  RegExpProxy getRegExpProxy() {
    if (this.regExpProxy == null) {
      Class<?> clazz = Kit.classOrNull("com.trendmicro.hippo.regexp.RegExpImpl");
      if (clazz != null)
        this.regExpProxy = (RegExpProxy)Kit.newInstanceOrNull(clazz); 
    } 
    return this.regExpProxy;
  }
  
  SecurityController getSecurityController() {
    SecurityController securityController = SecurityController.global();
    return (securityController != null) ? securityController : this.securityController;
  }
  
  public final Object getThreadLocal(Object paramObject) {
    Map<Object, Object> map = this.threadLocalMap;
    return (map == null) ? null : map.get(paramObject);
  }
  
  public final WrapFactory getWrapFactory() {
    if (this.wrapFactory == null)
      this.wrapFactory = new WrapFactory(); 
    return this.wrapFactory;
  }
  
  public boolean hasFeature(int paramInt) {
    return getFactory().hasFeature(this, paramInt);
  }
  
  public final Scriptable initSafeStandardObjects(ScriptableObject paramScriptableObject) {
    return initSafeStandardObjects(paramScriptableObject, false);
  }
  
  public final ScriptableObject initSafeStandardObjects() {
    return initSafeStandardObjects(null, false);
  }
  
  public ScriptableObject initSafeStandardObjects(ScriptableObject paramScriptableObject, boolean paramBoolean) {
    return ScriptRuntime.initSafeStandardObjects(this, paramScriptableObject, paramBoolean);
  }
  
  public final Scriptable initStandardObjects(ScriptableObject paramScriptableObject) {
    return initStandardObjects(paramScriptableObject, false);
  }
  
  public final ScriptableObject initStandardObjects() {
    return initStandardObjects(null, false);
  }
  
  public ScriptableObject initStandardObjects(ScriptableObject paramScriptableObject, boolean paramBoolean) {
    return ScriptRuntime.initStandardObjects(this, paramScriptableObject, paramBoolean);
  }
  
  public final boolean isActivationNeeded(String paramString) {
    boolean bool;
    Set<String> set = this.activationNames;
    if (set != null && set.contains(paramString)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isGeneratingDebug() {
    return this.generatingDebug;
  }
  
  public final boolean isGeneratingDebugChanged() {
    return this.generatingDebugChanged;
  }
  
  public final boolean isGeneratingSource() {
    return this.generatingSource;
  }
  
  public final boolean isSealed() {
    return this.sealed;
  }
  
  public final boolean isStrictMode() {
    if (!this.isTopLevelStrict) {
      NativeCall nativeCall = this.currentActivationCall;
      return (nativeCall != null && nativeCall.isStrict);
    } 
    return true;
  }
  
  final boolean isVersionECMA1() {
    int i = this.version;
    return (i == 0 || i >= 130);
  }
  
  public Scriptable newArray(Scriptable paramScriptable, int paramInt) {
    NativeArray nativeArray = new NativeArray(paramInt);
    ScriptRuntime.setBuiltinProtoAndParent(nativeArray, paramScriptable, TopLevel.Builtins.Array);
    return nativeArray;
  }
  
  public Scriptable newArray(Scriptable paramScriptable, Object[] paramArrayOfObject) {
    if (paramArrayOfObject.getClass().getComponentType() == ScriptRuntime.ObjectClass) {
      NativeArray nativeArray = new NativeArray(paramArrayOfObject);
      ScriptRuntime.setBuiltinProtoAndParent(nativeArray, paramScriptable, TopLevel.Builtins.Array);
      return nativeArray;
    } 
    throw new IllegalArgumentException();
  }
  
  public Scriptable newObject(Scriptable paramScriptable) {
    NativeObject nativeObject = new NativeObject();
    ScriptRuntime.setBuiltinProtoAndParent(nativeObject, paramScriptable, TopLevel.Builtins.Object);
    return nativeObject;
  }
  
  public Scriptable newObject(Scriptable paramScriptable, String paramString) {
    return newObject(paramScriptable, paramString, ScriptRuntime.emptyArgs);
  }
  
  public Scriptable newObject(Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject) {
    return ScriptRuntime.newObject(this, paramScriptable, paramString, paramArrayOfObject);
  }
  
  protected void observeInstructionCount(int paramInt) {
    getFactory().observeInstructionCount(this, paramInt);
  }
  
  public final void putThreadLocal(Object paramObject1, Object paramObject2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sealed : Z
    //   6: ifeq -> 12
    //   9: invokestatic onSealedMutation : ()V
    //   12: aload_0
    //   13: getfield threadLocalMap : Ljava/util/Map;
    //   16: ifnonnull -> 32
    //   19: new java/util/HashMap
    //   22: astore_3
    //   23: aload_3
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: aload_3
    //   29: putfield threadLocalMap : Ljava/util/Map;
    //   32: aload_0
    //   33: getfield threadLocalMap : Ljava/util/Map;
    //   36: aload_1
    //   37: aload_2
    //   38: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   43: pop
    //   44: aload_0
    //   45: monitorexit
    //   46: return
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	47	finally
    //   12	32	47	finally
    //   32	44	47	finally
  }
  
  public void removeActivationName(String paramString) {
    if (this.sealed)
      onSealedMutation(); 
    Set<String> set = this.activationNames;
    if (set != null)
      set.remove(paramString); 
  }
  
  public final void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
    if (this.sealed)
      onSealedMutation(); 
    this.propertyListeners = Kit.removeListener(this.propertyListeners, paramPropertyChangeListener);
  }
  
  public final void removeThreadLocal(Object paramObject) {
    if (this.sealed)
      onSealedMutation(); 
    Map<Object, Object> map = this.threadLocalMap;
    if (map == null)
      return; 
    map.remove(paramObject);
  }
  
  public Object resumeContinuation(Object paramObject1, Scriptable paramScriptable, Object paramObject2) throws ContinuationPending {
    return Interpreter.restartContinuation((NativeContinuation)paramObject1, this, paramScriptable, new Object[] { paramObject2 });
  }
  
  public final void seal(Object paramObject) {
    if (this.sealed)
      onSealedMutation(); 
    this.sealed = true;
    this.sealKey = paramObject;
  }
  
  public final void setApplicationClassLoader(ClassLoader paramClassLoader) {
    if (this.sealed)
      onSealedMutation(); 
    if (paramClassLoader == null) {
      this.applicationClassLoader = null;
      return;
    } 
    if (Kit.testIfCanLoadHippoClasses(paramClassLoader)) {
      this.applicationClassLoader = paramClassLoader;
      return;
    } 
    throw new IllegalArgumentException("Loader can not resolve Hippo classes");
  }
  
  public final void setClassShutter(ClassShutter paramClassShutter) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sealed : Z
    //   6: ifeq -> 12
    //   9: invokestatic onSealedMutation : ()V
    //   12: aload_1
    //   13: ifnull -> 49
    //   16: aload_0
    //   17: getfield hasClassShutter : Z
    //   20: ifne -> 36
    //   23: aload_0
    //   24: aload_1
    //   25: putfield classShutter : Lcom/trendmicro/hippo/ClassShutter;
    //   28: aload_0
    //   29: iconst_1
    //   30: putfield hasClassShutter : Z
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: new java/lang/SecurityException
    //   39: astore_1
    //   40: aload_1
    //   41: ldc_w 'Cannot overwrite existing ClassShutter object'
    //   44: invokespecial <init> : (Ljava/lang/String;)V
    //   47: aload_1
    //   48: athrow
    //   49: new java/lang/IllegalArgumentException
    //   52: astore_1
    //   53: aload_1
    //   54: invokespecial <init> : ()V
    //   57: aload_1
    //   58: athrow
    //   59: astore_1
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	59	finally
    //   16	33	59	finally
    //   36	49	59	finally
    //   49	59	59	finally
  }
  
  public final void setDebugger(Debugger paramDebugger, Object paramObject) {
    if (this.sealed)
      onSealedMutation(); 
    this.debugger = paramDebugger;
    this.debuggerData = paramObject;
  }
  
  public final ErrorReporter setErrorReporter(ErrorReporter paramErrorReporter) {
    if (this.sealed)
      onSealedMutation(); 
    if (paramErrorReporter != null) {
      ErrorReporter errorReporter = getErrorReporter();
      if (paramErrorReporter == errorReporter)
        return errorReporter; 
      Object object = this.propertyListeners;
      if (object != null)
        firePropertyChangeImpl(object, "error reporter", errorReporter, paramErrorReporter); 
      this.errorReporter = paramErrorReporter;
      return errorReporter;
    } 
    throw new IllegalArgumentException();
  }
  
  public void setGenerateObserverCount(boolean paramBoolean) {
    this.generateObserverCount = paramBoolean;
  }
  
  public final void setGeneratingDebug(boolean paramBoolean) {
    if (this.sealed)
      onSealedMutation(); 
    this.generatingDebugChanged = true;
    if (paramBoolean && getOptimizationLevel() > 0)
      setOptimizationLevel(0); 
    this.generatingDebug = paramBoolean;
  }
  
  public final void setGeneratingSource(boolean paramBoolean) {
    if (this.sealed)
      onSealedMutation(); 
    this.generatingSource = paramBoolean;
  }
  
  public final void setInstructionObserverThreshold(int paramInt) {
    if (this.sealed)
      onSealedMutation(); 
    if (paramInt >= 0) {
      boolean bool;
      this.instructionThreshold = paramInt;
      if (paramInt > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      setGenerateObserverCount(bool);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void setLanguageVersion(int paramInt) {
    if (this.sealed)
      onSealedMutation(); 
    checkLanguageVersion(paramInt);
    Object object = this.propertyListeners;
    if (object != null) {
      int i = this.version;
      if (paramInt != i)
        firePropertyChangeImpl(object, "language version", Integer.valueOf(i), Integer.valueOf(paramInt)); 
    } 
    this.version = paramInt;
  }
  
  public final Locale setLocale(Locale paramLocale) {
    if (this.sealed)
      onSealedMutation(); 
    Locale locale = this.locale;
    this.locale = paramLocale;
    return locale;
  }
  
  public final void setMaximumInterpreterStackDepth(int paramInt) {
    if (this.sealed)
      onSealedMutation(); 
    if (this.optimizationLevel == -1) {
      if (paramInt >= 1) {
        this.maximumInterpreterStackDepth = paramInt;
        return;
      } 
      throw new IllegalArgumentException("Cannot set maximumInterpreterStackDepth to less than 1");
    } 
    throw new IllegalStateException("Cannot set maximumInterpreterStackDepth when optimizationLevel != -1");
  }
  
  public final void setOptimizationLevel(int paramInt) {
    if (this.sealed)
      onSealedMutation(); 
    int i = paramInt;
    if (paramInt == -2)
      i = -1; 
    checkOptimizationLevel(i);
    if (codegenClass == null)
      i = -1; 
    this.optimizationLevel = i;
  }
  
  public final void setSecurityController(SecurityController paramSecurityController) {
    if (this.sealed)
      onSealedMutation(); 
    if (paramSecurityController != null) {
      if (this.securityController == null) {
        if (!SecurityController.hasGlobal()) {
          this.securityController = paramSecurityController;
          return;
        } 
        throw new SecurityException("Can not overwrite existing global SecurityController object");
      } 
      throw new SecurityException("Can not overwrite existing SecurityController object");
    } 
    throw new IllegalArgumentException();
  }
  
  public final void setWrapFactory(WrapFactory paramWrapFactory) {
    if (this.sealed)
      onSealedMutation(); 
    if (paramWrapFactory != null) {
      this.wrapFactory = paramWrapFactory;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public final boolean stringIsCompilableUnit(String paramString) {
    boolean bool1 = false;
    CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
    compilerEnvirons.initFromContext(this);
    boolean bool2 = false;
    compilerEnvirons.setGeneratingSource(false);
    Parser parser = new Parser(compilerEnvirons, DefaultErrorReporter.instance);
    try {
      parser.parse(paramString, (String)null, 1);
    } catch (EvaluatorException evaluatorException) {
      bool1 = true;
    } 
    if (!bool1 || !parser.eof())
      bool2 = true; 
    return bool2;
  }
  
  public final void unseal(Object paramObject) {
    if (paramObject != null) {
      if (this.sealKey == paramObject) {
        if (this.sealed) {
          this.sealed = false;
          this.sealKey = null;
          return;
        } 
        throw new IllegalStateException();
      } 
      throw new IllegalArgumentException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static interface ClassShutterSetter {
    ClassShutter getClassShutter();
    
    void setClassShutter(ClassShutter param1ClassShutter);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */