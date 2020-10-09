package com.trendmicro.hippo;

public class BaseFunction extends IdScriptableObject implements Function {
  private static final Object FUNCTION_TAG = "Function";
  
  private static final int Id_apply = 4;
  
  private static final int Id_arguments = 5;
  
  private static final int Id_arity = 2;
  
  private static final int Id_bind = 6;
  
  private static final int Id_call = 5;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_length = 1;
  
  private static final int Id_name = 3;
  
  private static final int Id_prototype = 4;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  private static final int MAX_INSTANCE_ID = 5;
  
  private static final int MAX_PROTOTYPE_ID = 6;
  
  private static final long serialVersionUID = 5311394446546053859L;
  
  private int argumentsAttributes = 6;
  
  private Object argumentsObj = NOT_FOUND;
  
  private Object prototypeProperty;
  
  private int prototypePropertyAttributes = 6;
  
  public BaseFunction() {}
  
  public BaseFunction(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    super(paramScriptable1, paramScriptable2);
  }
  
  private Object getArguments() {
    if (defaultHas("arguments")) {
      object = defaultGet("arguments");
    } else {
      object = this.argumentsObj;
    } 
    if (object != NOT_FOUND)
      return object; 
    Object object = ScriptRuntime.findFunctionActivation(Context.getContext(), this);
    if (object == null) {
      object = null;
    } else {
      object = object.get("arguments", (Scriptable)object);
    } 
    return object;
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    BaseFunction baseFunction = new BaseFunction();
    baseFunction.prototypePropertyAttributes = 7;
    baseFunction.exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  static boolean isApply(IdFunctionObject paramIdFunctionObject) {
    boolean bool;
    if (paramIdFunctionObject.hasTag(FUNCTION_TAG) && paramIdFunctionObject.methodId() == 4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isApplyOrCall(IdFunctionObject paramIdFunctionObject) {
    if (paramIdFunctionObject.hasTag(FUNCTION_TAG)) {
      int i = paramIdFunctionObject.methodId();
      if (i == 4 || i == 5)
        return true; 
    } 
    return false;
  }
  
  private static Object jsConstructor(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    int i = paramArrayOfObject.length;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("function ");
    if (paramContext.getLanguageVersion() != 120)
      stringBuilder.append("anonymous"); 
    stringBuilder.append('(');
    for (byte b = 0; b < i - 1; b++) {
      if (b > 0)
        stringBuilder.append(','); 
      stringBuilder.append(ScriptRuntime.toString(paramArrayOfObject[b]));
    } 
    stringBuilder.append(") {");
    if (i != 0)
      stringBuilder.append(ScriptRuntime.toString(paramArrayOfObject[i - 1])); 
    stringBuilder.append("\n}");
    String str2 = stringBuilder.toString();
    int[] arrayOfInt = new int[1];
    String str1 = Context.getSourcePositionFromStack(arrayOfInt);
    if (str1 == null) {
      arrayOfInt[0] = 1;
      str1 = "<eval'ed string>";
    } 
    String str3 = ScriptRuntime.makeUrlForGeneratedScript(false, str1, arrayOfInt[0]);
    Scriptable scriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    ErrorReporter errorReporter = DefaultErrorReporter.forEval(paramContext.getErrorReporter());
    Evaluator evaluator = Context.createInterpreter();
    if (evaluator != null)
      return paramContext.compileFunction(scriptable, str2, evaluator, errorReporter, str3, 1, null); 
    throw new JavaScriptException("Interpreter not present", str1, arrayOfInt[0]);
  }
  
  private BaseFunction realFunction(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    Object object2 = paramScriptable.getDefaultValue(ScriptRuntime.FunctionClass);
    Object object1 = object2;
    if (object2 instanceof Delegator)
      object1 = ((Delegator)object2).getDelegee(); 
    if (object1 instanceof BaseFunction)
      return (BaseFunction)object1; 
    throw ScriptRuntime.typeError1("msg.incompat.call", paramIdFunctionObject.getFunctionName());
  }
  
  private Object setupDefaultPrototype() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield prototypeProperty : Ljava/lang/Object;
    //   6: ifnull -> 18
    //   9: aload_0
    //   10: getfield prototypeProperty : Ljava/lang/Object;
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: new com/trendmicro/hippo/NativeObject
    //   21: astore_2
    //   22: aload_2
    //   23: invokespecial <init> : ()V
    //   26: aload_2
    //   27: ldc 'constructor'
    //   29: aload_0
    //   30: iconst_2
    //   31: invokevirtual defineProperty : (Ljava/lang/String;Ljava/lang/Object;I)V
    //   34: aload_0
    //   35: aload_2
    //   36: putfield prototypeProperty : Ljava/lang/Object;
    //   39: aload_0
    //   40: invokestatic getObjectPrototype : (Lcom/trendmicro/hippo/Scriptable;)Lcom/trendmicro/hippo/Scriptable;
    //   43: astore_1
    //   44: aload_1
    //   45: aload_2
    //   46: if_acmpeq -> 54
    //   49: aload_2
    //   50: aload_1
    //   51: invokevirtual setPrototype : (Lcom/trendmicro/hippo/Scriptable;)V
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_2
    //   57: areturn
    //   58: astore_1
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_1
    //   62: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	58	finally
    //   18	44	58	finally
    //   49	54	58	finally
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return Undefined.instance;
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object object1;
    Object object2;
    Scriptable scriptable = createObject(paramContext, paramScriptable);
    if (scriptable != null) {
      object2 = call(paramContext, paramScriptable, scriptable, paramArrayOfObject);
      object1 = scriptable;
      if (object2 instanceof Scriptable)
        object1 = object2; 
    } else {
      object1 = call((Context)object1, (Scriptable)object2, (Scriptable)null, paramArrayOfObject);
      if (object1 instanceof Scriptable) {
        object2 = object1;
        if (object2.getPrototype() == null) {
          object1 = getClassPrototype();
          if (object2 != object1)
            object2.setPrototype((Scriptable)object1); 
        } 
        object1 = object2;
        if (object2.getParentScope() == null) {
          Scriptable scriptable1 = getParentScope();
          object1 = object2;
          if (object2 != scriptable1) {
            object2.setParentScope(scriptable1);
            object1 = object2;
          } 
        } 
        return (Scriptable)object1;
      } 
      object1 = new StringBuilder();
      object1.append("Bad implementaion of call as constructor, name=");
      object1.append(getFunctionName());
      object1.append(" in ");
      object1.append(getClass().getName());
      throw new IllegalStateException(object1.toString());
    } 
    return (Scriptable)object1;
  }
  
  public Scriptable createObject(Context paramContext, Scriptable paramScriptable) {
    NativeObject nativeObject = new NativeObject();
    nativeObject.setPrototype(getClassPrototype());
    nativeObject.setParentScope(getParentScope());
    return nativeObject;
  }
  
  String decompile(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    if ((paramInt2 & 0x1) != 0) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    if (paramInt1 == 0) {
      stringBuilder.append("function ");
      stringBuilder.append(getFunctionName());
      stringBuilder.append("() {\n\t");
    } 
    stringBuilder.append("[native code, arity=");
    stringBuilder.append(getArity());
    stringBuilder.append("]\n");
    if (paramInt1 == 0)
      stringBuilder.append("}\n"); 
    return stringBuilder.toString();
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    BaseFunction baseFunction;
    byte b1;
    byte b2;
    if (!paramIdFunctionObject.hasTag(FUNCTION_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    boolean bool = true;
    switch (i) {
      default:
        throw new IllegalArgumentException(String.valueOf(i));
      case 6:
        if (paramScriptable2 instanceof Callable) {
          Callable callable = (Callable)paramScriptable2;
          i = paramArrayOfObject.length;
          if (i > 0) {
            paramScriptable2 = ScriptRuntime.toObjectOrNull(paramContext, paramArrayOfObject[0], paramScriptable1);
            arrayOfObject = new Object[i - 1];
            System.arraycopy(paramArrayOfObject, 1, arrayOfObject, 0, i - 1);
          } else {
            arrayOfObject = ScriptRuntime.emptyArgs;
            paramScriptable2 = null;
          } 
          return new BoundFunction(paramContext, paramScriptable1, callable, paramScriptable2, arrayOfObject);
        } 
        throw ScriptRuntime.notFunctionError(paramScriptable2);
      case 4:
      case 5:
        if (i != 4)
          bool = false; 
        return ScriptRuntime.applyOrCall(bool, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
      case 3:
        baseFunction = realFunction(paramScriptable2, (IdFunctionObject)arrayOfObject);
        i = 0;
        b1 = 2;
        b2 = b1;
        if (paramArrayOfObject.length != 0) {
          i = ScriptRuntime.toInt32(paramArrayOfObject[0]);
          if (i >= 0) {
            b2 = 0;
          } else {
            i = 0;
            b2 = b1;
          } 
        } 
        return baseFunction.decompile(i, b2);
      case 2:
        return realFunction(paramScriptable2, (IdFunctionObject)baseFunction).decompile(ScriptRuntime.toInt32(paramArrayOfObject, 0), 0);
      case 1:
        break;
    } 
    return jsConstructor(paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    paramIdFunctionObject.setPrototype(this);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    int i = 0;
    String str = null;
    int j = paramString.length();
    if (j != 4) {
      if (j != 5) {
        if (j != 6) {
          if (j == 9) {
            j = paramString.charAt(0);
            if (j == 97) {
              str = "arguments";
              i = 5;
            } else if (j == 112) {
              str = "prototype";
              i = 4;
            } 
          } 
        } else {
          str = "length";
          i = 1;
        } 
      } else {
        str = "arity";
        i = 2;
      } 
    } else {
      str = "name";
      i = 3;
    } 
    j = i;
    if (str != null) {
      j = i;
      if (str != paramString) {
        j = i;
        if (!str.equals(paramString))
          j = 0; 
      } 
    } 
    if (j == 0)
      return super.findInstanceIdInfo(paramString); 
    if (j != 1 && j != 2 && j != 3) {
      if (j != 4) {
        if (j == 5) {
          i = this.argumentsAttributes;
        } else {
          throw new IllegalStateException();
        } 
      } else {
        if (!hasPrototypeProperty())
          return 0; 
        i = this.prototypePropertyAttributes;
      } 
    } else {
      i = 7;
    } 
    return instanceIdInfo(i, j);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i != 4) {
      if (i != 5) {
        if (i != 8) {
          if (i == 11) {
            str = "constructor";
            b = 1;
          } 
        } else {
          i = paramString.charAt(3);
          if (i == 111) {
            str = "toSource";
            b = 3;
          } else if (i == 116) {
            str = "toString";
            b = 2;
          } 
        } 
      } else {
        str = "apply";
        b = 4;
      } 
    } else {
      i = paramString.charAt(0);
      if (i == 98) {
        str = "bind";
        b = 6;
      } else if (i == 99) {
        str = "call";
        b = 5;
      } 
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
    return "Function";
  }
  
  protected Scriptable getClassPrototype() {
    Object object = getPrototypeProperty();
    return (object instanceof Scriptable) ? (Scriptable)object : ScriptableObject.getObjectPrototype(this);
  }
  
  public String getFunctionName() {
    return "";
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? ((paramInt != 5) ? super.getInstanceIdName(paramInt) : "arguments") : "prototype") : "name") : "arity") : "length";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? ((paramInt != 5) ? super.getInstanceIdValue(paramInt) : getArguments()) : getPrototypeProperty()) : getFunctionName()) : ScriptRuntime.wrapInt(getArity())) : ScriptRuntime.wrapInt(getLength());
  }
  
  public int getLength() {
    return 0;
  }
  
  protected int getMaxInstanceId() {
    return 5;
  }
  
  protected Object getPrototypeProperty() {
    Object object2;
    Object object1 = this.prototypeProperty;
    if (object1 == null) {
      if (this instanceof NativeFunction) {
        object2 = setupDefaultPrototype();
      } else {
        object2 = Undefined.instance;
      } 
    } else {
      object2 = object1;
      if (object1 == UniqueTag.NULL_VALUE)
        object2 = null; 
    } 
    return object2;
  }
  
  public String getTypeOf() {
    String str;
    if (avoidObjectDetection()) {
      str = "undefined";
    } else {
      str = "function";
    } 
    return str;
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    Object object = ScriptableObject.getProperty(this, "prototype");
    if (object instanceof Scriptable)
      return ScriptRuntime.jsDelegatesTo(paramScriptable, (Scriptable)object); 
    throw ScriptRuntime.typeError1("msg.instanceof.bad.prototype", getFunctionName());
  }
  
  protected boolean hasPrototypeProperty() {
    return (this.prototypeProperty != null || this instanceof NativeFunction);
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 6:
        b = 1;
        str = "bind";
        break;
      case 5:
        b = 1;
        str = "call";
        break;
      case 4:
        b = 2;
        str = "apply";
        break;
      case 3:
        b = 1;
        str = "toSource";
        break;
      case 2:
        b = 0;
        str = "toString";
        break;
      case 1:
        b = 1;
        str = "constructor";
        break;
    } 
    initPrototypeMethod(FUNCTION_TAG, paramInt, str, b);
  }
  
  public void setImmunePrototypeProperty(Object paramObject) {
    if ((this.prototypePropertyAttributes & 0x1) == 0) {
      if (paramObject == null)
        paramObject = UniqueTag.NULL_VALUE; 
      this.prototypeProperty = paramObject;
      this.prototypePropertyAttributes = 7;
      return;
    } 
    throw new IllegalStateException();
  }
  
  protected void setInstanceIdAttributes(int paramInt1, int paramInt2) {
    if (paramInt1 != 4) {
      if (paramInt1 != 5) {
        super.setInstanceIdAttributes(paramInt1, paramInt2);
        return;
      } 
      this.argumentsAttributes = paramInt2;
      return;
    } 
    this.prototypePropertyAttributes = paramInt2;
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    if (paramInt != 1 && paramInt != 2 && paramInt != 3) {
      if (paramInt != 4) {
        if (paramInt != 5) {
          super.setInstanceIdValue(paramInt, paramObject);
          return;
        } 
        if (paramObject == NOT_FOUND)
          Kit.codeBug(); 
        if (defaultHas("arguments")) {
          defaultPut("arguments", paramObject);
        } else if ((0x1 & this.argumentsAttributes) == 0) {
          this.argumentsObj = paramObject;
        } 
        return;
      } 
      if ((0x1 & this.prototypePropertyAttributes) == 0) {
        if (paramObject == null)
          paramObject = UniqueTag.NULL_VALUE; 
        this.prototypeProperty = paramObject;
      } 
      return;
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/BaseFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */