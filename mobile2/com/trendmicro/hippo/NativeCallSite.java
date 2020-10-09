package com.trendmicro.hippo;

public class NativeCallSite extends IdScriptableObject {
  private static final String CALLSITE_TAG = "CallSite";
  
  private static final int Id_constructor = 1;
  
  private static final int Id_getColumnNumber = 9;
  
  private static final int Id_getEvalOrigin = 10;
  
  private static final int Id_getFileName = 7;
  
  private static final int Id_getFunction = 4;
  
  private static final int Id_getFunctionName = 5;
  
  private static final int Id_getLineNumber = 8;
  
  private static final int Id_getMethodName = 6;
  
  private static final int Id_getThis = 2;
  
  private static final int Id_getTypeName = 3;
  
  private static final int Id_isConstructor = 14;
  
  private static final int Id_isEval = 12;
  
  private static final int Id_isNative = 13;
  
  private static final int Id_isToplevel = 11;
  
  private static final int Id_toString = 15;
  
  private static final int MAX_PROTOTYPE_ID = 15;
  
  private ScriptStackElement element;
  
  private static Object getFileName(Scriptable paramScriptable) {
    String str;
    while (paramScriptable != null && !(paramScriptable instanceof NativeCallSite))
      paramScriptable = paramScriptable.getPrototype(); 
    if (paramScriptable == null)
      return NOT_FOUND; 
    ScriptStackElement scriptStackElement = ((NativeCallSite)paramScriptable).element;
    if (scriptStackElement == null) {
      scriptStackElement = null;
    } else {
      str = scriptStackElement.fileName;
    } 
    return str;
  }
  
  private static Object getFunctionName(Scriptable paramScriptable) {
    String str;
    while (paramScriptable != null && !(paramScriptable instanceof NativeCallSite))
      paramScriptable = paramScriptable.getPrototype(); 
    if (paramScriptable == null)
      return NOT_FOUND; 
    ScriptStackElement scriptStackElement = ((NativeCallSite)paramScriptable).element;
    if (scriptStackElement == null) {
      scriptStackElement = null;
    } else {
      str = scriptStackElement.functionName;
    } 
    return str;
  }
  
  private static Object getLineNumber(Scriptable paramScriptable) {
    while (paramScriptable != null && !(paramScriptable instanceof NativeCallSite))
      paramScriptable = paramScriptable.getPrototype(); 
    if (paramScriptable == null)
      return NOT_FOUND; 
    NativeCallSite nativeCallSite = (NativeCallSite)paramScriptable;
    ScriptStackElement scriptStackElement = nativeCallSite.element;
    return (scriptStackElement == null || scriptStackElement.lineNumber < 0) ? Undefined.instance : Integer.valueOf(nativeCallSite.element.lineNumber);
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeCallSite()).exportAsJSClass(15, paramScriptable, paramBoolean);
  }
  
  private static Object js_toString(Scriptable paramScriptable) {
    while (paramScriptable != null && !(paramScriptable instanceof NativeCallSite))
      paramScriptable = paramScriptable.getPrototype(); 
    if (paramScriptable == null)
      return NOT_FOUND; 
    paramScriptable = paramScriptable;
    StringBuilder stringBuilder = new StringBuilder();
    ((NativeCallSite)paramScriptable).element.renderJavaStyle(stringBuilder);
    return stringBuilder.toString();
  }
  
  static NativeCallSite make(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    NativeCallSite nativeCallSite = new NativeCallSite();
    paramScriptable2 = (Scriptable)paramScriptable2.get("prototype", paramScriptable2);
    nativeCallSite.setParentScope(paramScriptable1);
    nativeCallSite.setPrototype(paramScriptable2);
    return nativeCallSite;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag("CallSite"))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    switch (i) {
      default:
        throw new IllegalArgumentException(String.valueOf(i));
      case 15:
        return js_toString(paramScriptable2);
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
        return Boolean.FALSE;
      case 8:
        return getLineNumber(paramScriptable2);
      case 7:
        return getFileName(paramScriptable2);
      case 6:
        return null;
      case 5:
        return getFunctionName(paramScriptable2);
      case 2:
      case 3:
      case 4:
      case 9:
        return Undefined.instance;
      case 1:
        break;
    } 
    return make(paramScriptable1, paramIdFunctionObject);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    switch (paramString.length()) {
      case 15:
        c = paramString.charAt(3);
        if (c == 'C') {
          str = "getColumnNumber";
          b = 9;
          break;
        } 
        if (c == 'F') {
          str = "getFunctionName";
          b = 5;
        } 
        break;
      case 13:
        c = paramString.charAt(3);
        if (c != 'E') {
          if (c != 'o') {
            if (c != 'L') {
              if (c != 'M')
                break; 
              str = "getMethodName";
              b = 6;
              break;
            } 
            str = "getLineNumber";
            b = 8;
            break;
          } 
          str = "isConstructor";
          b = 14;
          break;
        } 
        str = "getEvalOrigin";
        b = 10;
        break;
      case 11:
        c = paramString.charAt(4);
        if (c != 'i') {
          if (c != 'y') {
            if (c != 't') {
              if (c != 'u')
                break; 
              str = "getFunction";
              b = 4;
              break;
            } 
            str = "constructor";
            b = 1;
            break;
          } 
          str = "getTypeName";
          b = 3;
          break;
        } 
        str = "getFileName";
        b = 7;
        break;
      case 10:
        str = "isToplevel";
        b = 11;
        break;
      case 8:
        c = paramString.charAt(0);
        if (c == 'i') {
          str = "isNative";
          b = 13;
          break;
        } 
        if (c == 't') {
          str = "toString";
          b = 15;
        } 
        break;
      case 7:
        str = "getThis";
        b = 2;
        break;
      case 6:
        str = "isEval";
        b = 12;
        break;
    } 
    char c = b;
    if (str != null) {
      c = b;
      if (str != paramString) {
        c = b;
        if (!str.equals(paramString))
          c = Character.MIN_VALUE; 
      } 
    } 
    return c;
  }
  
  public String getClassName() {
    return "CallSite";
  }
  
  protected void initPrototypeId(int paramInt) {
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 15:
        str = "toString";
        break;
      case 14:
        str = "isConstructor";
        break;
      case 13:
        str = "isNative";
        break;
      case 12:
        str = "isEval";
        break;
      case 11:
        str = "isToplevel";
        break;
      case 10:
        str = "getEvalOrigin";
        break;
      case 9:
        str = "getColumnNumber";
        break;
      case 8:
        str = "getLineNumber";
        break;
      case 7:
        str = "getFileName";
        break;
      case 6:
        str = "getMethodName";
        break;
      case 5:
        str = "getFunctionName";
        break;
      case 4:
        str = "getFunction";
        break;
      case 3:
        str = "getTypeName";
        break;
      case 2:
        str = "getThis";
        break;
      case 1:
        str = "constructor";
        break;
    } 
    initPrototypeMethod("CallSite", paramInt, str, 0);
  }
  
  void setElement(ScriptStackElement paramScriptStackElement) {
    this.element = paramScriptStackElement;
  }
  
  public String toString() {
    ScriptStackElement scriptStackElement = this.element;
    return (scriptStackElement == null) ? "" : scriptStackElement.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeCallSite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */