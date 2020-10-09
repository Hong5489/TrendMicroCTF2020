package com.trendmicro.hippo.tools;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ErrorReporter;
import com.trendmicro.hippo.EvaluatorException;
import com.trendmicro.hippo.HippoException;
import com.trendmicro.hippo.SecurityUtilities;
import com.trendmicro.hippo.WrappedException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ToolErrorReporter implements ErrorReporter {
  private static final String messagePrefix = "js: ";
  
  private PrintStream err;
  
  private boolean hasReportedErrorFlag;
  
  private boolean reportWarnings;
  
  public ToolErrorReporter(boolean paramBoolean) {
    this(paramBoolean, System.err);
  }
  
  public ToolErrorReporter(boolean paramBoolean, PrintStream paramPrintStream) {
    this.reportWarnings = paramBoolean;
    this.err = paramPrintStream;
  }
  
  private String buildIndicator(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < paramInt - 1; b++)
      stringBuilder.append("."); 
    stringBuilder.append("^");
    return stringBuilder.toString();
  }
  
  private static String getExceptionMessage(HippoException paramHippoException) {
    String str;
    if (paramHippoException instanceof com.trendmicro.hippo.JavaScriptException) {
      str = getMessage("msg.uncaughtJSException", paramHippoException.details());
    } else if (str instanceof com.trendmicro.hippo.EcmaError) {
      str = getMessage("msg.uncaughtEcmaError", str.details());
    } else if (str instanceof EvaluatorException) {
      str = str.details();
    } else {
      str = str.toString();
    } 
    return str;
  }
  
  public static String getMessage(String paramString) {
    return getMessage(paramString, (Object[])null);
  }
  
  public static String getMessage(String paramString, Object paramObject1, Object paramObject2) {
    return getMessage(paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public static String getMessage(String paramString1, String paramString2) {
    return getMessage(paramString1, new Object[] { paramString2 });
  }
  
  public static String getMessage(String paramString, Object[] paramArrayOfObject) {
    Locale locale;
    Context context = Context.getCurrentContext();
    if (context == null) {
      locale = Locale.getDefault();
    } else {
      locale = locale.getLocale();
    } 
    ResourceBundle resourceBundle = ResourceBundle.getBundle("com.trendmicro.hippo.tools.resources.Messages", locale);
    try {
      String str = resourceBundle.getString(paramString);
      return (paramArrayOfObject == null) ? str : (new MessageFormat(str)).format(paramArrayOfObject);
    } catch (MissingResourceException missingResourceException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("no message resource found for message property ");
      stringBuilder.append(paramString);
      throw new RuntimeException(stringBuilder.toString());
    } 
  }
  
  private void reportErrorMessage(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2, boolean paramBoolean) {
    if (paramInt1 > 0) {
      String str = String.valueOf(paramInt1);
      if (paramString2 != null) {
        paramString1 = getMessage("msg.format3", new Object[] { paramString2, str, paramString1 });
      } else {
        paramString1 = getMessage("msg.format2", new Object[] { str, paramString1 });
      } 
    } else {
      paramString1 = getMessage("msg.format1", new Object[] { paramString1 });
    } 
    paramString2 = paramString1;
    if (paramBoolean)
      paramString2 = getMessage("msg.warning", paramString1); 
    PrintStream printStream = this.err;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("js: ");
    stringBuilder.append(paramString2);
    printStream.println(stringBuilder.toString());
    if (paramString3 != null) {
      printStream = this.err;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("js: ");
      stringBuilder2.append(paramString3);
      printStream.println(stringBuilder2.toString());
      PrintStream printStream1 = this.err;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("js: ");
      stringBuilder1.append(buildIndicator(paramInt2));
      printStream1.println(stringBuilder1.toString());
    } 
  }
  
  public static void reportException(ErrorReporter paramErrorReporter, HippoException paramHippoException) {
    if (paramErrorReporter instanceof ToolErrorReporter) {
      ((ToolErrorReporter)paramErrorReporter).reportException(paramHippoException);
    } else {
      paramErrorReporter.error(getExceptionMessage(paramHippoException), paramHippoException.sourceName(), paramHippoException.lineNumber(), paramHippoException.lineSource(), paramHippoException.columnNumber());
    } 
  }
  
  public void error(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    this.hasReportedErrorFlag = true;
    reportErrorMessage(paramString1, paramString2, paramInt1, paramString3, paramInt2, false);
  }
  
  public boolean hasReportedError() {
    return this.hasReportedErrorFlag;
  }
  
  public boolean isReportingWarnings() {
    return this.reportWarnings;
  }
  
  public void reportException(HippoException paramHippoException) {
    if (paramHippoException instanceof WrappedException) {
      ((WrappedException)paramHippoException).printStackTrace(this.err);
    } else {
      String str = SecurityUtilities.getSystemProperty("line.separator");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getExceptionMessage(paramHippoException));
      stringBuilder.append(str);
      stringBuilder.append(paramHippoException.getScriptStackTrace());
      reportErrorMessage(stringBuilder.toString(), paramHippoException.sourceName(), paramHippoException.lineNumber(), paramHippoException.lineSource(), paramHippoException.columnNumber(), false);
    } 
  }
  
  public EvaluatorException runtimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    return new EvaluatorException(paramString1, paramString2, paramInt1, paramString3, paramInt2);
  }
  
  public void setIsReportingWarnings(boolean paramBoolean) {
    this.reportWarnings = paramBoolean;
  }
  
  public void warning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    if (!this.reportWarnings)
      return; 
    reportErrorMessage(paramString1, paramString2, paramInt1, paramString3, paramInt2, true);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/ToolErrorReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */