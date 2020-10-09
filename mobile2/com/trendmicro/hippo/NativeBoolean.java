package com.trendmicro.hippo;

final class NativeBoolean extends IdScriptableObject {
  private static final Object BOOLEAN_TAG = "Boolean";
  
  private static final int Id_constructor = 1;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  private static final int Id_valueOf = 4;
  
  private static final int MAX_PROTOTYPE_ID = 4;
  
  private static final long serialVersionUID = -3716996899943880933L;
  
  private boolean booleanValue;
  
  NativeBoolean(boolean paramBoolean) {
    this.booleanValue = paramBoolean;
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeBoolean(false)).exportAsJSClass(4, paramScriptable, paramBoolean);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    String str;
    if (!paramIdFunctionObject.hasTag(BOOLEAN_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    boolean bool = true;
    if (i == 1) {
      if (paramArrayOfObject.length == 0) {
        bool = false;
      } else if (!(paramArrayOfObject[0] instanceof ScriptableObject) || !((ScriptableObject)paramArrayOfObject[0]).avoidObjectDetection()) {
        bool = ScriptRuntime.toBoolean(paramArrayOfObject[0]);
      } 
      return (paramScriptable2 == null) ? new NativeBoolean(bool) : ScriptRuntime.wrapBoolean(bool);
    } 
    if (paramScriptable2 instanceof NativeBoolean) {
      bool = ((NativeBoolean)paramScriptable2).booleanValue;
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            return ScriptRuntime.wrapBoolean(bool); 
          throw new IllegalArgumentException(String.valueOf(i));
        } 
        if (bool) {
          str = "(new Boolean(true))";
        } else {
          str = "(new Boolean(false))";
        } 
        return str;
      } 
      if (bool) {
        str = "true";
      } else {
        str = "false";
      } 
      return str;
    } 
    throw incompatibleCallError(str);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 7) {
      str = "valueOf";
      b = 4;
    } else if (i == 8) {
      i = paramString.charAt(3);
      if (i == 111) {
        str = "toSource";
        b = 3;
      } else if (i == 116) {
        str = "toString";
        b = 2;
      } 
    } else if (i == 11) {
      str = "constructor";
      b = 1;
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
  
  public String getClassName() {
    return "Boolean";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return (paramClass == ScriptRuntime.BooleanClass) ? ScriptRuntime.wrapBoolean(this.booleanValue) : super.getDefaultValue(paramClass);
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt == 4) {
            bool = false;
            str = "valueOf";
          } else {
            throw new IllegalArgumentException(String.valueOf(paramInt));
          } 
        } else {
          bool = false;
          str = "toSource";
        } 
      } else {
        bool = false;
        str = "toString";
      } 
    } else {
      bool = true;
      str = "constructor";
    } 
    initPrototypeMethod(BOOLEAN_TAG, paramInt, str, bool);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeBoolean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */