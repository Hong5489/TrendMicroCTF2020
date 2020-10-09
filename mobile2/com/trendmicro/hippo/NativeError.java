package com.trendmicro.hippo;

import java.io.Serializable;
import java.lang.reflect.Method;

final class NativeError extends IdScriptableObject {
  private static final int ConstructorId_captureStackTrace = -1;
  
  public static final int DEFAULT_STACK_LIMIT = -1;
  
  private static final Method ERROR_DELEGATE_GET_STACK;
  
  private static final Method ERROR_DELEGATE_SET_STACK;
  
  private static final Object ERROR_TAG = "Error";
  
  private static final int Id_constructor = 1;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  private static final int MAX_PROTOTYPE_ID = 3;
  
  private static final String STACK_HIDE_KEY = "_stackHide";
  
  private static final long serialVersionUID = -5338413581437645187L;
  
  private HippoException stackProvider;
  
  static {
    try {
      ERROR_DELEGATE_GET_STACK = NativeError.class.getMethod("getStackDelegated", new Class[] { Scriptable.class });
      ERROR_DELEGATE_SET_STACK = NativeError.class.getMethod("setStackDelegated", new Class[] { Scriptable.class, Object.class });
      return;
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new RuntimeException(noSuchMethodException);
    } 
  }
  
  private Object callPrepareStack(Function paramFunction, ScriptStackElement[] paramArrayOfScriptStackElement) {
    Context context = Context.getCurrentContext();
    Object[] arrayOfObject = new Object[paramArrayOfScriptStackElement.length];
    for (byte b = 0; b < paramArrayOfScriptStackElement.length; b++) {
      NativeCallSite nativeCallSite = (NativeCallSite)context.newObject(this, "CallSite");
      nativeCallSite.setElement(paramArrayOfScriptStackElement[b]);
      arrayOfObject[b] = nativeCallSite;
    } 
    return paramFunction.call(context, paramFunction, this, new Object[] { this, context.newArray(this, arrayOfObject) });
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    NativeError nativeError = new NativeError();
    ScriptableObject.putProperty(nativeError, "name", "Error");
    ScriptableObject.putProperty(nativeError, "message", "");
    ScriptableObject.putProperty(nativeError, "fileName", "");
    ScriptableObject.putProperty(nativeError, "lineNumber", Integer.valueOf(0));
    nativeError.setAttributes("name", 2);
    nativeError.setAttributes("message", 2);
    nativeError.exportAsJSClass(3, paramScriptable, paramBoolean);
    NativeCallSite.init(nativeError, paramBoolean);
  }
  
  private static void js_captureStackTrace(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    ScriptableObject scriptableObject = (ScriptableObject)ScriptRuntime.toObjectOrNull(paramContext, paramArrayOfObject[0], paramScriptable);
    if (paramArrayOfObject.length > 1) {
      Function function = (Function)ScriptRuntime.toObjectOrNull(paramContext, paramArrayOfObject[1], paramScriptable);
    } else {
      paramArrayOfObject = null;
    } 
    NativeError nativeError = (NativeError)paramContext.newObject(paramScriptable, "Error");
    nativeError.setStackProvider(new EvaluatorException("[object Object]"));
    if (paramArrayOfObject != null) {
      Object object = paramArrayOfObject.get("name", (Scriptable)paramArrayOfObject);
      if (object != null && !Undefined.instance.equals(object))
        nativeError.associateValue("_stackHide", Context.toString(object)); 
    } 
    scriptableObject.defineProperty("stack", nativeError, ERROR_DELEGATE_GET_STACK, ERROR_DELEGATE_SET_STACK, 0);
  }
  
  private static String js_toSource(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2) {
    Object object2 = ScriptableObject.getProperty(paramScriptable2, "name");
    Object object3 = ScriptableObject.getProperty(paramScriptable2, "message");
    Object object4 = ScriptableObject.getProperty(paramScriptable2, "fileName");
    Object object5 = ScriptableObject.getProperty(paramScriptable2, "lineNumber");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(new ");
    Object object1 = object2;
    if (object2 == NOT_FOUND)
      object1 = Undefined.instance; 
    stringBuilder.append(ScriptRuntime.toString(object1));
    stringBuilder.append("(");
    if (object3 != NOT_FOUND || object4 != NOT_FOUND || object5 != NOT_FOUND) {
      object1 = object3;
      if (object3 == NOT_FOUND)
        object1 = ""; 
      stringBuilder.append(ScriptRuntime.uneval(paramContext, paramScriptable1, object1));
      if (object4 != NOT_FOUND || object5 != NOT_FOUND) {
        stringBuilder.append(", ");
        object1 = object4;
        if (object4 == NOT_FOUND)
          object1 = ""; 
        stringBuilder.append(ScriptRuntime.uneval(paramContext, paramScriptable1, object1));
        if (object5 != NOT_FOUND) {
          int i = ScriptRuntime.toInt32(object5);
          if (i != 0) {
            stringBuilder.append(", ");
            stringBuilder.append(ScriptRuntime.toString(i));
          } 
        } 
      } 
    } 
    stringBuilder.append("))");
    return stringBuilder.toString();
  }
  
  private static Object js_toString(Scriptable paramScriptable) {
    Object object2 = ScriptableObject.getProperty(paramScriptable, "name");
    if (object2 == NOT_FOUND || object2 == Undefined.instance) {
      object2 = "Error";
    } else {
      object2 = ScriptRuntime.toString(object2);
    } 
    Object object1 = ScriptableObject.getProperty(paramScriptable, "message");
    if (object1 == NOT_FOUND || object1 == Undefined.instance) {
      object1 = "";
    } else {
      object1 = ScriptRuntime.toString(object1);
    } 
    if (object2.toString().length() == 0)
      return object1; 
    if (object1.toString().length() == 0)
      return object2; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append((String)object2);
    stringBuilder.append(": ");
    stringBuilder.append((String)object1);
    return stringBuilder.toString();
  }
  
  static NativeError make(Context paramContext, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject, Object[] paramArrayOfObject) {
    Scriptable scriptable = (Scriptable)paramIdFunctionObject.get("prototype", paramIdFunctionObject);
    NativeError nativeError = new NativeError();
    nativeError.setPrototype(scriptable);
    nativeError.setParentScope(paramScriptable);
    int i = paramArrayOfObject.length;
    if (i >= 1) {
      if (paramArrayOfObject[0] != Undefined.instance)
        ScriptableObject.putProperty(nativeError, "message", ScriptRuntime.toString(paramArrayOfObject[0])); 
      if (i >= 2) {
        ScriptableObject.putProperty(nativeError, "fileName", paramArrayOfObject[1]);
        if (i >= 3) {
          i = ScriptRuntime.toInt32(paramArrayOfObject[2]);
          ScriptableObject.putProperty(nativeError, "lineNumber", Integer.valueOf(i));
        } 
      } 
    } 
    return nativeError;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(ERROR_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != -1) {
      if (i != 1) {
        if (i != 2) {
          if (i == 3)
            return js_toSource(paramContext, paramScriptable1, paramScriptable2); 
          throw new IllegalArgumentException(String.valueOf(i));
        } 
        return js_toString(paramScriptable2);
      } 
      return make(paramContext, paramScriptable1, paramIdFunctionObject, paramArrayOfObject);
    } 
    js_captureStackTrace(paramContext, paramScriptable2, paramArrayOfObject);
    return Undefined.instance;
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    addIdFunctionProperty(paramIdFunctionObject, ERROR_TAG, -1, "captureStackTrace", 2);
    ProtoProps protoProps = new ProtoProps();
    associateValue("_ErrorPrototypeProps", protoProps);
    paramIdFunctionObject.defineProperty("stackTraceLimit", protoProps, ProtoProps.GET_STACK_LIMIT, ProtoProps.SET_STACK_LIMIT, 0);
    paramIdFunctionObject.defineProperty("prepareStackTrace", protoProps, ProtoProps.GET_PREPARE_STACK, ProtoProps.SET_PREPARE_STACK, 0);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 8) {
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
    return "Error";
  }
  
  public Object getStackDelegated(Scriptable paramScriptable) {
    Object object;
    if (this.stackProvider == null)
      return NOT_FOUND; 
    int i = -1;
    Function function = null;
    ProtoProps protoProps = (ProtoProps)((NativeError)getPrototype()).getAssociatedValue("_ErrorPrototypeProps");
    if (protoProps != null) {
      i = protoProps.getStackTraceLimit();
      function = protoProps.getPrepareStackTrace();
    } 
    String str = (String)getAssociatedValue("_stackHide");
    ScriptStackElement[] arrayOfScriptStackElement = this.stackProvider.getScriptStack(i, str);
    if (function == null) {
      object = HippoException.formatStackTrace(arrayOfScriptStackElement, this.stackProvider.details());
    } else {
      object = callPrepareStack((Function)object, arrayOfScriptStackElement);
    } 
    setStackDelegated(paramScriptable, object);
    return object;
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          bool = false;
          str = "toSource";
        } else {
          throw new IllegalArgumentException(String.valueOf(paramInt));
        } 
      } else {
        bool = false;
        str = "toString";
      } 
    } else {
      bool = true;
      str = "constructor";
    } 
    initPrototypeMethod(ERROR_TAG, paramInt, str, bool);
  }
  
  public void setStackDelegated(Scriptable paramScriptable, Object paramObject) {
    paramScriptable.delete("stack");
    this.stackProvider = null;
    paramScriptable.put("stack", paramScriptable, paramObject);
  }
  
  public void setStackProvider(HippoException paramHippoException) {
    if (this.stackProvider == null) {
      this.stackProvider = paramHippoException;
      defineProperty("stack", this, ERROR_DELEGATE_GET_STACK, ERROR_DELEGATE_SET_STACK, 2);
    } 
  }
  
  public String toString() {
    Object object = js_toString(this);
    if (object instanceof String) {
      object = object;
    } else {
      object = super.toString();
    } 
    return (String)object;
  }
  
  private static final class ProtoProps implements Serializable {
    static final Method GET_PREPARE_STACK;
    
    static final Method GET_STACK_LIMIT;
    
    static final String KEY = "_ErrorPrototypeProps";
    
    static final Method SET_PREPARE_STACK;
    
    static final Method SET_STACK_LIMIT;
    
    private static final long serialVersionUID = 1907180507775337939L;
    
    private Function prepareStackTrace;
    
    private int stackTraceLimit = -1;
    
    static {
      try {
        GET_STACK_LIMIT = ProtoProps.class.getMethod("getStackTraceLimit", new Class[] { Scriptable.class });
        SET_STACK_LIMIT = ProtoProps.class.getMethod("setStackTraceLimit", new Class[] { Scriptable.class, Object.class });
        GET_PREPARE_STACK = ProtoProps.class.getMethod("getPrepareStackTrace", new Class[] { Scriptable.class });
        SET_PREPARE_STACK = ProtoProps.class.getMethod("setPrepareStackTrace", new Class[] { Scriptable.class, Object.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        throw new RuntimeException(noSuchMethodException);
      } 
    }
    
    private ProtoProps() {}
    
    public Function getPrepareStackTrace() {
      return this.prepareStackTrace;
    }
    
    public Object getPrepareStackTrace(Scriptable param1Scriptable) {
      Object object;
      param1Scriptable = getPrepareStackTrace();
      if (param1Scriptable == null)
        object = Undefined.instance; 
      return object;
    }
    
    public int getStackTraceLimit() {
      return this.stackTraceLimit;
    }
    
    public Object getStackTraceLimit(Scriptable param1Scriptable) {
      int i = this.stackTraceLimit;
      return (i >= 0) ? Integer.valueOf(i) : Double.valueOf(Double.POSITIVE_INFINITY);
    }
    
    public void setPrepareStackTrace(Scriptable param1Scriptable, Object param1Object) {
      if (param1Object == null || Undefined.instance.equals(param1Object)) {
        this.prepareStackTrace = null;
        return;
      } 
      if (param1Object instanceof Function)
        this.prepareStackTrace = (Function)param1Object; 
    }
    
    public void setStackTraceLimit(Scriptable param1Scriptable, Object param1Object) {
      double d = Context.toNumber(param1Object);
      if (Double.isNaN(d) || Double.isInfinite(d)) {
        this.stackTraceLimit = -1;
        return;
      } 
      this.stackTraceLimit = (int)d;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */