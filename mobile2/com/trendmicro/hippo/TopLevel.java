package com.trendmicro.hippo;

import java.util.EnumMap;

public class TopLevel extends IdScriptableObject {
  private static final long serialVersionUID = -4648046356662472260L;
  
  private EnumMap<Builtins, BaseFunction> ctors;
  
  private EnumMap<NativeErrors, BaseFunction> errors;
  
  public static Function getBuiltinCtor(Context paramContext, Scriptable paramScriptable, Builtins paramBuiltins) {
    if (paramScriptable instanceof TopLevel) {
      BaseFunction baseFunction = ((TopLevel)paramScriptable).getBuiltinCtor(paramBuiltins);
      if (baseFunction != null)
        return baseFunction; 
    } 
    return ScriptRuntime.getExistingCtor(paramContext, paramScriptable, paramBuiltins.name());
  }
  
  public static Scriptable getBuiltinPrototype(Scriptable paramScriptable, Builtins paramBuiltins) {
    if (paramScriptable instanceof TopLevel) {
      Scriptable scriptable = ((TopLevel)paramScriptable).getBuiltinPrototype(paramBuiltins);
      if (scriptable != null)
        return scriptable; 
    } 
    return ScriptableObject.getClassPrototype(paramScriptable, paramBuiltins.name());
  }
  
  static Function getNativeErrorCtor(Context paramContext, Scriptable paramScriptable, NativeErrors paramNativeErrors) {
    if (paramScriptable instanceof TopLevel) {
      BaseFunction baseFunction = ((TopLevel)paramScriptable).getNativeErrorCtor(paramNativeErrors);
      if (baseFunction != null)
        return baseFunction; 
    } 
    return ScriptRuntime.getExistingCtor(paramContext, paramScriptable, paramNativeErrors.name());
  }
  
  public void cacheBuiltins() {
    this.ctors = new EnumMap<>(Builtins.class);
    Builtins[] arrayOfBuiltins = Builtins.values();
    int i = arrayOfBuiltins.length;
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      Builtins builtins = arrayOfBuiltins[b];
      Object object = ScriptableObject.getProperty(this, builtins.name());
      if (object instanceof BaseFunction)
        this.ctors.put(builtins, (BaseFunction)object); 
    } 
    this.errors = new EnumMap<>(NativeErrors.class);
    NativeErrors[] arrayOfNativeErrors = NativeErrors.values();
    i = arrayOfNativeErrors.length;
    for (b = bool; b < i; b++) {
      NativeErrors nativeErrors = arrayOfNativeErrors[b];
      Object object = ScriptableObject.getProperty(this, nativeErrors.name());
      if (object instanceof BaseFunction)
        this.errors.put(nativeErrors, (BaseFunction)object); 
    } 
  }
  
  public BaseFunction getBuiltinCtor(Builtins paramBuiltins) {
    EnumMap<Builtins, BaseFunction> enumMap = this.ctors;
    if (enumMap != null) {
      BaseFunction baseFunction = enumMap.get(paramBuiltins);
    } else {
      paramBuiltins = null;
    } 
    return (BaseFunction)paramBuiltins;
  }
  
  public Scriptable getBuiltinPrototype(Builtins paramBuiltins) {
    BaseFunction baseFunction1 = getBuiltinCtor(paramBuiltins);
    BaseFunction baseFunction2 = null;
    if (baseFunction1 != null) {
      Object object = baseFunction1.getPrototypeProperty();
    } else {
      baseFunction1 = null;
    } 
    if (baseFunction1 instanceof Scriptable)
      baseFunction2 = baseFunction1; 
    return baseFunction2;
  }
  
  public String getClassName() {
    return "global";
  }
  
  BaseFunction getNativeErrorCtor(NativeErrors paramNativeErrors) {
    EnumMap<NativeErrors, BaseFunction> enumMap = this.errors;
    if (enumMap != null) {
      BaseFunction baseFunction = enumMap.get(paramNativeErrors);
    } else {
      paramNativeErrors = null;
    } 
    return (BaseFunction)paramNativeErrors;
  }
  
  public enum Builtins {
    Array, Boolean, Error, Function, Number, Object, RegExp, String, Symbol;
    
    static {
      Number = new Builtins("Number", 4);
      Boolean = new Builtins("Boolean", 5);
      RegExp = new Builtins("RegExp", 6);
      Error = new Builtins("Error", 7);
      Builtins builtins = new Builtins("Symbol", 8);
      Symbol = builtins;
      $VALUES = new Builtins[] { Object, Array, Function, String, Number, Boolean, RegExp, Error, builtins };
    }
  }
  
  enum NativeErrors {
    Error, EvalError, InternalError, JavaException, RangeError, ReferenceError, SyntaxError, TypeError, URIError;
    
    static {
      NativeErrors nativeErrors = new NativeErrors("JavaException", 8);
      JavaException = nativeErrors;
      $VALUES = new NativeErrors[] { Error, EvalError, RangeError, ReferenceError, SyntaxError, TypeError, URIError, InternalError, nativeErrors };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/TopLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */