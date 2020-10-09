package com.trendmicro.hippo;

class NativeScript extends BaseFunction {
  private static final int Id_compile = 3;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_exec = 4;
  
  private static final int Id_toString = 2;
  
  private static final int MAX_PROTOTYPE_ID = 4;
  
  private static final Object SCRIPT_TAG = "Script";
  
  private static final long serialVersionUID = -6795101161980121700L;
  
  private Script script;
  
  private NativeScript(Script paramScript) {
    this.script = paramScript;
  }
  
  private static Script compile(Context paramContext, String paramString) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    String str1 = Context.getSourcePositionFromStack(arrayOfInt);
    String str2 = str1;
    if (str1 == null) {
      str2 = "<Script object>";
      arrayOfInt[0] = 1;
    } 
    return paramContext.compileString(paramString, null, DefaultErrorReporter.forEval(paramContext.getErrorReporter()), str2, arrayOfInt[0], null);
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeScript(null)).exportAsJSClass(4, paramScriptable, paramBoolean);
  }
  
  private static NativeScript realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeScript)
      return (NativeScript)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Script script = this.script;
    return (script != null) ? script.exec(paramContext, paramScriptable1) : Undefined.instance;
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    throw Context.reportRuntimeError0("msg.script.is.not.constructor");
  }
  
  String decompile(int paramInt1, int paramInt2) {
    Script script = this.script;
    return (script instanceof NativeFunction) ? ((NativeFunction)script).decompile(paramInt1, paramInt2) : super.decompile(paramInt1, paramInt2);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    String str1;
    if (!paramIdFunctionObject.hasTag(SCRIPT_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    String str2 = "";
    if (i != 1) {
      NativeScript nativeScript1;
      if (i != 2) {
        if (i != 3) {
          if (i != 4)
            throw new IllegalArgumentException(String.valueOf(i)); 
          throw Context.reportRuntimeError1("msg.cant.call.indirect", "exec");
        } 
        nativeScript1 = realThis(paramScriptable2, paramIdFunctionObject);
        nativeScript1.script = compile(paramContext, ScriptRuntime.toString(paramArrayOfObject, 0));
        return nativeScript1;
      } 
      Script script = (realThis(paramScriptable2, (IdFunctionObject)nativeScript1)).script;
      return (script == null) ? "" : paramContext.decompileScript(script, 0);
    } 
    if (paramArrayOfObject.length == 0) {
      str1 = str2;
    } else {
      str1 = ScriptRuntime.toString(paramArrayOfObject[0]);
    } 
    NativeScript nativeScript = new NativeScript(compile(paramContext, str1));
    ScriptRuntime.setObjectProtoAndParent(nativeScript, paramScriptable1);
    return nativeScript;
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i != 4) {
      if (i != 11) {
        if (i != 7) {
          if (i == 8) {
            str = "toString";
            b = 2;
          } 
        } else {
          str = "compile";
          b = 3;
        } 
      } else {
        str = "constructor";
        b = 1;
      } 
    } else {
      str = "exec";
      b = 4;
    } 
    i = b;
    if (str != null) {
      i = b;
      if (str != paramString) {
        i = b;
        if (!str.equals(paramString))
          i = 0; 
      } 
    } 
    return i;
  }
  
  public int getArity() {
    return 0;
  }
  
  public String getClassName() {
    return "Script";
  }
  
  public int getLength() {
    return 0;
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt == 4) {
            bool = false;
            str = "exec";
          } else {
            throw new IllegalArgumentException(String.valueOf(paramInt));
          } 
        } else {
          bool = true;
          str = "compile";
        } 
      } else {
        bool = false;
        str = "toString";
      } 
    } else {
      bool = true;
      str = "constructor";
    } 
    initPrototypeMethod(SCRIPT_TAG, paramInt, str, bool);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeScript.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */