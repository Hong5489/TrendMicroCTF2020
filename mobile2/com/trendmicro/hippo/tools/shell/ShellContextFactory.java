package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.ErrorReporter;

public class ShellContextFactory extends ContextFactory {
  private boolean allowReservedKeywords = true;
  
  private String characterEncoding;
  
  private ErrorReporter errorReporter;
  
  private boolean generatingDebug;
  
  private int languageVersion = 180;
  
  private int optimizationLevel;
  
  private boolean strictMode;
  
  private boolean warningAsError;
  
  public String getCharacterEncoding() {
    return this.characterEncoding;
  }
  
  protected boolean hasFeature(Context paramContext, int paramInt) {
    if (paramInt != 3) {
      switch (paramInt) {
        default:
          return super.hasFeature(paramContext, paramInt);
        case 12:
          return this.warningAsError;
        case 10:
          return this.generatingDebug;
        case 8:
        case 9:
        case 11:
          break;
      } 
      return this.strictMode;
    } 
    return this.allowReservedKeywords;
  }
  
  protected void onContextCreated(Context paramContext) {
    paramContext.setLanguageVersion(this.languageVersion);
    paramContext.setOptimizationLevel(this.optimizationLevel);
    ErrorReporter errorReporter = this.errorReporter;
    if (errorReporter != null)
      paramContext.setErrorReporter(errorReporter); 
    paramContext.setGeneratingDebug(this.generatingDebug);
    super.onContextCreated(paramContext);
  }
  
  public void setAllowReservedKeywords(boolean paramBoolean) {
    this.allowReservedKeywords = paramBoolean;
  }
  
  public void setCharacterEncoding(String paramString) {
    this.characterEncoding = paramString;
  }
  
  public void setErrorReporter(ErrorReporter paramErrorReporter) {
    if (paramErrorReporter != null) {
      this.errorReporter = paramErrorReporter;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void setGeneratingDebug(boolean paramBoolean) {
    this.generatingDebug = paramBoolean;
  }
  
  public void setLanguageVersion(int paramInt) {
    Context.checkLanguageVersion(paramInt);
    checkNotSealed();
    this.languageVersion = paramInt;
  }
  
  public void setOptimizationLevel(int paramInt) {
    Context.checkOptimizationLevel(paramInt);
    checkNotSealed();
    this.optimizationLevel = paramInt;
  }
  
  public void setStrictMode(boolean paramBoolean) {
    checkNotSealed();
    this.strictMode = paramBoolean;
  }
  
  public void setWarningAsError(boolean paramBoolean) {
    checkNotSealed();
    this.warningAsError = paramBoolean;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/ShellContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */