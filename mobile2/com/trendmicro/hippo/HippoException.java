package com.trendmicro.hippo;

import java.io.CharArrayWriter;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public abstract class HippoException extends RuntimeException {
  private static final Pattern JAVA_STACK_PATTERN = Pattern.compile("_c_(.*)_\\d+");
  
  private static final long serialVersionUID = 1883500631321581169L;
  
  private static StackStyle stackStyle = StackStyle.HIPPO;
  
  private int columnNumber;
  
  int[] interpreterLineData;
  
  Object interpreterStackInfo;
  
  private int lineNumber;
  
  private String lineSource;
  
  private String sourceName;
  
  static {
    String str = System.getProperty("hippo.stack.style");
    if (str != null)
      if ("Hippo".equalsIgnoreCase(str)) {
        stackStyle = StackStyle.HIPPO;
      } else if ("TrendMicro".equalsIgnoreCase(str)) {
        stackStyle = StackStyle.TRENDMICRO;
      } else if ("V8".equalsIgnoreCase(str)) {
        stackStyle = StackStyle.V8;
      }  
  }
  
  HippoException() {
    Evaluator evaluator = Context.createInterpreter();
    if (evaluator != null)
      evaluator.captureStackInfo(this); 
  }
  
  HippoException(String paramString) {
    super(paramString);
    Evaluator evaluator = Context.createInterpreter();
    if (evaluator != null)
      evaluator.captureStackInfo(this); 
  }
  
  static String formatStackTrace(ScriptStackElement[] paramArrayOfScriptStackElement, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    String str = SecurityUtilities.getSystemProperty("line.separator");
    if (stackStyle == StackStyle.V8 && !"null".equals(paramString)) {
      stringBuilder.append(paramString);
      stringBuilder.append(str);
    } 
    int i = paramArrayOfScriptStackElement.length;
    for (byte b = 0; b < i; b++) {
      ScriptStackElement scriptStackElement = paramArrayOfScriptStackElement[b];
      int j = null.$SwitchMap$com$trendmicro$hippo$StackStyle[stackStyle.ordinal()];
      if (j != 1) {
        if (j != 2) {
          if (j == 3)
            scriptStackElement.renderJavaStyle(stringBuilder); 
        } else {
          scriptStackElement.renderV8Style(stringBuilder);
        } 
      } else {
        scriptStackElement.renderTrendMicroStyle(stringBuilder);
      } 
      stringBuilder.append(str);
    } 
    return stringBuilder.toString();
  }
  
  private String generateStackTrace() {
    CharArrayWriter charArrayWriter = new CharArrayWriter();
    super.printStackTrace(new PrintWriter(charArrayWriter));
    String str = charArrayWriter.toString();
    Evaluator evaluator = Context.createInterpreter();
    return (evaluator != null) ? evaluator.getPatchedStack(this, str) : null;
  }
  
  public static StackStyle getStackStyle() {
    return stackStyle;
  }
  
  public static void setStackStyle(StackStyle paramStackStyle) {
    stackStyle = paramStackStyle;
  }
  
  public static void useTrendMicroStackStyle(boolean paramBoolean) {
    StackStyle stackStyle;
    if (paramBoolean) {
      stackStyle = StackStyle.TRENDMICRO;
    } else {
      stackStyle = StackStyle.HIPPO;
    } 
    stackStyle = stackStyle;
  }
  
  public static boolean usesTrendMicroStackStyle() {
    boolean bool;
    if (stackStyle == StackStyle.TRENDMICRO) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final int columnNumber() {
    return this.columnNumber;
  }
  
  public String details() {
    return super.getMessage();
  }
  
  public final String getMessage() {
    String str1 = details();
    if (this.sourceName == null || this.lineNumber <= 0)
      return str1; 
    StringBuilder stringBuilder = new StringBuilder(str1);
    stringBuilder.append(" (");
    String str2 = this.sourceName;
    if (str2 != null)
      stringBuilder.append(str2); 
    if (this.lineNumber > 0) {
      stringBuilder.append('#');
      stringBuilder.append(this.lineNumber);
    } 
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
  
  public ScriptStackElement[] getScriptStack() {
    return getScriptStack(-1, null);
  }
  
  public ScriptStackElement[] getScriptStack(int paramInt, String paramString) {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aconst_null
    //   9: astore #4
    //   11: aload #4
    //   13: astore #5
    //   15: aload_0
    //   16: getfield interpreterStackInfo : Ljava/lang/Object;
    //   19: ifnull -> 50
    //   22: invokestatic createInterpreter : ()Lcom/trendmicro/hippo/Evaluator;
    //   25: astore #6
    //   27: aload #4
    //   29: astore #5
    //   31: aload #6
    //   33: instanceof com/trendmicro/hippo/Interpreter
    //   36: ifeq -> 50
    //   39: aload #6
    //   41: checkcast com/trendmicro/hippo/Interpreter
    //   44: aload_0
    //   45: invokevirtual getScriptStackElements : (Lcom/trendmicro/hippo/HippoException;)[[Lcom/trendmicro/hippo/ScriptStackElement;
    //   48: astore #5
    //   50: iconst_0
    //   51: istore #7
    //   53: aload_0
    //   54: invokevirtual getStackTrace : ()[Ljava/lang/StackTraceElement;
    //   57: astore #6
    //   59: iconst_0
    //   60: istore #8
    //   62: aload_2
    //   63: ifnonnull -> 72
    //   66: iconst_1
    //   67: istore #9
    //   69: goto -> 75
    //   72: iconst_0
    //   73: istore #9
    //   75: aload #6
    //   77: arraylength
    //   78: istore #10
    //   80: iconst_0
    //   81: istore #11
    //   83: iload #11
    //   85: iload #10
    //   87: if_icmpge -> 524
    //   90: aload #6
    //   92: iload #11
    //   94: aaload
    //   95: astore #12
    //   97: aload #12
    //   99: invokevirtual getFileName : ()Ljava/lang/String;
    //   102: astore #13
    //   104: aload #12
    //   106: invokevirtual getMethodName : ()Ljava/lang/String;
    //   109: ldc '_c_'
    //   111: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   114: ifeq -> 285
    //   117: aload #12
    //   119: invokevirtual getLineNumber : ()I
    //   122: iconst_m1
    //   123: if_icmple -> 285
    //   126: aload #13
    //   128: ifnull -> 285
    //   131: aload #13
    //   133: ldc '.java'
    //   135: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   138: ifne -> 285
    //   141: aload #12
    //   143: invokevirtual getMethodName : ()Ljava/lang/String;
    //   146: astore #14
    //   148: getstatic com/trendmicro/hippo/HippoException.JAVA_STACK_PATTERN : Ljava/util/regex/Pattern;
    //   151: aload #14
    //   153: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   156: astore #4
    //   158: ldc '_c_script_0'
    //   160: aload #14
    //   162: invokevirtual equals : (Ljava/lang/Object;)Z
    //   165: ifne -> 187
    //   168: aload #4
    //   170: invokevirtual find : ()Z
    //   173: ifeq -> 187
    //   176: aload #4
    //   178: iconst_1
    //   179: invokevirtual group : (I)Ljava/lang/String;
    //   182: astore #4
    //   184: goto -> 190
    //   187: aconst_null
    //   188: astore #4
    //   190: iload #9
    //   192: ifne -> 214
    //   195: aload_2
    //   196: aload #4
    //   198: invokevirtual equals : (Ljava/lang/Object;)Z
    //   201: ifeq -> 214
    //   204: iconst_1
    //   205: istore #15
    //   207: iload #8
    //   209: istore #16
    //   211: goto -> 278
    //   214: iload #8
    //   216: istore #16
    //   218: iload #9
    //   220: istore #15
    //   222: iload #9
    //   224: ifeq -> 278
    //   227: iload_1
    //   228: iflt -> 245
    //   231: iload #8
    //   233: istore #16
    //   235: iload #9
    //   237: istore #15
    //   239: iload #8
    //   241: iload_1
    //   242: if_icmpge -> 278
    //   245: aload_3
    //   246: new com/trendmicro/hippo/ScriptStackElement
    //   249: dup
    //   250: aload #13
    //   252: aload #4
    //   254: aload #12
    //   256: invokevirtual getLineNumber : ()I
    //   259: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;I)V
    //   262: invokeinterface add : (Ljava/lang/Object;)Z
    //   267: pop
    //   268: iload #8
    //   270: iconst_1
    //   271: iadd
    //   272: istore #16
    //   274: iload #9
    //   276: istore #15
    //   278: iload #7
    //   280: istore #17
    //   282: goto -> 506
    //   285: iload #8
    //   287: istore #16
    //   289: iload #9
    //   291: istore #15
    //   293: ldc 'com.trendmicro.hippo.Interpreter'
    //   295: aload #12
    //   297: invokevirtual getClassName : ()Ljava/lang/String;
    //   300: invokevirtual equals : (Ljava/lang/Object;)Z
    //   303: ifeq -> 278
    //   306: iload #7
    //   308: istore #17
    //   310: iload #8
    //   312: istore #16
    //   314: iload #9
    //   316: istore #15
    //   318: ldc 'interpretLoop'
    //   320: aload #12
    //   322: invokevirtual getMethodName : ()Ljava/lang/String;
    //   325: invokevirtual equals : (Ljava/lang/Object;)Z
    //   328: ifeq -> 506
    //   331: iload #7
    //   333: istore #17
    //   335: iload #8
    //   337: istore #16
    //   339: iload #9
    //   341: istore #15
    //   343: aload #5
    //   345: ifnull -> 506
    //   348: iload #7
    //   350: istore #17
    //   352: iload #8
    //   354: istore #16
    //   356: iload #9
    //   358: istore #15
    //   360: aload #5
    //   362: arraylength
    //   363: iload #7
    //   365: if_icmple -> 506
    //   368: iload #7
    //   370: iconst_1
    //   371: iadd
    //   372: istore #16
    //   374: aload #5
    //   376: iload #7
    //   378: aaload
    //   379: astore #13
    //   381: aload #13
    //   383: arraylength
    //   384: istore #18
    //   386: iconst_0
    //   387: istore #15
    //   389: iload #15
    //   391: iload #18
    //   393: if_icmpge -> 494
    //   396: aload #13
    //   398: iload #15
    //   400: aaload
    //   401: astore #4
    //   403: iload #9
    //   405: ifne -> 430
    //   408: aload_2
    //   409: aload #4
    //   411: getfield functionName : Ljava/lang/String;
    //   414: invokevirtual equals : (Ljava/lang/Object;)Z
    //   417: ifeq -> 430
    //   420: iconst_1
    //   421: istore #17
    //   423: iload #8
    //   425: istore #7
    //   427: goto -> 480
    //   430: iload #8
    //   432: istore #7
    //   434: iload #9
    //   436: istore #17
    //   438: iload #9
    //   440: ifeq -> 480
    //   443: iload_1
    //   444: iflt -> 461
    //   447: iload #8
    //   449: istore #7
    //   451: iload #9
    //   453: istore #17
    //   455: iload #8
    //   457: iload_1
    //   458: if_icmpge -> 480
    //   461: aload_3
    //   462: aload #4
    //   464: invokeinterface add : (Ljava/lang/Object;)Z
    //   469: pop
    //   470: iload #8
    //   472: iconst_1
    //   473: iadd
    //   474: istore #7
    //   476: iload #9
    //   478: istore #17
    //   480: iinc #15, 1
    //   483: iload #7
    //   485: istore #8
    //   487: iload #17
    //   489: istore #9
    //   491: goto -> 389
    //   494: iload #16
    //   496: istore #17
    //   498: iload #9
    //   500: istore #15
    //   502: iload #8
    //   504: istore #16
    //   506: iinc #11, 1
    //   509: iload #17
    //   511: istore #7
    //   513: iload #16
    //   515: istore #8
    //   517: iload #15
    //   519: istore #9
    //   521: goto -> 83
    //   524: aload_3
    //   525: aload_3
    //   526: invokeinterface size : ()I
    //   531: anewarray com/trendmicro/hippo/ScriptStackElement
    //   534: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   539: checkcast [Lcom/trendmicro/hippo/ScriptStackElement;
    //   542: areturn
  }
  
  public String getScriptStackTrace() {
    return getScriptStackTrace(-1, null);
  }
  
  public String getScriptStackTrace(int paramInt, String paramString) {
    return formatStackTrace(getScriptStack(paramInt, paramString), details());
  }
  
  @Deprecated
  public String getScriptStackTrace(FilenameFilter paramFilenameFilter) {
    return getScriptStackTrace();
  }
  
  public final void initColumnNumber(int paramInt) {
    if (paramInt > 0) {
      if (this.columnNumber <= 0) {
        this.columnNumber = paramInt;
        return;
      } 
      throw new IllegalStateException();
    } 
    throw new IllegalArgumentException(String.valueOf(paramInt));
  }
  
  public final void initLineNumber(int paramInt) {
    if (paramInt > 0) {
      if (this.lineNumber <= 0) {
        this.lineNumber = paramInt;
        return;
      } 
      throw new IllegalStateException();
    } 
    throw new IllegalArgumentException(String.valueOf(paramInt));
  }
  
  public final void initLineSource(String paramString) {
    if (paramString != null) {
      if (this.lineSource == null) {
        this.lineSource = paramString;
        return;
      } 
      throw new IllegalStateException();
    } 
    throw new IllegalArgumentException();
  }
  
  public final void initSourceName(String paramString) {
    if (paramString != null) {
      if (this.sourceName == null) {
        this.sourceName = paramString;
        return;
      } 
      throw new IllegalStateException();
    } 
    throw new IllegalArgumentException();
  }
  
  public final int lineNumber() {
    return this.lineNumber;
  }
  
  public final String lineSource() {
    return this.lineSource;
  }
  
  public void printStackTrace(PrintStream paramPrintStream) {
    if (this.interpreterStackInfo == null) {
      super.printStackTrace(paramPrintStream);
    } else {
      paramPrintStream.print(generateStackTrace());
    } 
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter) {
    if (this.interpreterStackInfo == null) {
      super.printStackTrace(paramPrintWriter);
    } else {
      paramPrintWriter.print(generateStackTrace());
    } 
  }
  
  final void recordErrorOrigin(String paramString1, int paramInt1, String paramString2, int paramInt2) {
    int i = paramInt1;
    if (paramInt1 == -1)
      i = 0; 
    if (paramString1 != null)
      initSourceName(paramString1); 
    if (i != 0)
      initLineNumber(i); 
    if (paramString2 != null)
      initLineSource(paramString2); 
    if (paramInt2 != 0)
      initColumnNumber(paramInt2); 
  }
  
  public final String sourceName() {
    return this.sourceName;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/HippoException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */