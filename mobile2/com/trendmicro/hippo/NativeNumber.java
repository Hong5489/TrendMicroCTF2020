package com.trendmicro.hippo;

final class NativeNumber extends IdScriptableObject {
  private static final int ConstructorId_isFinite = -1;
  
  private static final int ConstructorId_isInteger = -3;
  
  private static final int ConstructorId_isNaN = -2;
  
  private static final int ConstructorId_isSafeInteger = -4;
  
  private static final int ConstructorId_parseFloat = -5;
  
  private static final int ConstructorId_parseInt = -6;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_toExponential = 7;
  
  private static final int Id_toFixed = 6;
  
  private static final int Id_toLocaleString = 3;
  
  private static final int Id_toPrecision = 8;
  
  private static final int Id_toSource = 4;
  
  private static final int Id_toString = 2;
  
  private static final int Id_valueOf = 5;
  
  private static final int MAX_PRECISION = 100;
  
  private static final int MAX_PROTOTYPE_ID = 8;
  
  public static final double MAX_SAFE_INTEGER = 9.007199254740991E15D;
  
  private static final double MIN_SAFE_INTEGER = -9.007199254740991E15D;
  
  private static final Object NUMBER_TAG = "Number";
  
  private static final long serialVersionUID = 3504516769741512101L;
  
  private double doubleValue;
  
  NativeNumber(double paramDouble) {
    this.doubleValue = paramDouble;
  }
  
  private Double doubleVal(Number paramNumber) {
    return (paramNumber instanceof Double) ? (Double)paramNumber : Double.valueOf(paramNumber.doubleValue());
  }
  
  private Object execConstructorCall(int paramInt, Object[] paramArrayOfObject) {
    Boolean bool = Boolean.valueOf(false);
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case -1:
        return (paramArrayOfObject.length == 0 || Undefined.instance == paramArrayOfObject[0]) ? bool : ((paramArrayOfObject[0] instanceof Number) ? isFinite(paramArrayOfObject[0]) : bool);
      case -2:
        return (paramArrayOfObject.length == 0 || Undefined.instance == paramArrayOfObject[0]) ? bool : ((paramArrayOfObject[0] instanceof Number) ? isNaN((Number)paramArrayOfObject[0]) : bool);
      case -3:
        return (paramArrayOfObject.length == 0 || Undefined.instance == paramArrayOfObject[0]) ? bool : ((paramArrayOfObject[0] instanceof Number) ? Boolean.valueOf(isInteger((Number)paramArrayOfObject[0])) : bool);
      case -4:
        return (paramArrayOfObject.length == 0 || Undefined.instance == paramArrayOfObject[0]) ? bool : ((paramArrayOfObject[0] instanceof Number) ? Boolean.valueOf(isSafeInteger((Number)paramArrayOfObject[0])) : bool);
      case -5:
        return NativeGlobal.js_parseFloat(paramArrayOfObject);
      case -6:
        break;
    } 
    return NativeGlobal.js_parseInt(paramArrayOfObject);
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeNumber(0.0D)).exportAsJSClass(8, paramScriptable, paramBoolean);
  }
  
  private boolean isDoubleInteger(Double paramDouble) {
    boolean bool;
    if (!paramDouble.isInfinite() && !paramDouble.isNaN() && Math.floor(paramDouble.doubleValue()) == paramDouble.doubleValue()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean isDoubleNan(Double paramDouble) {
    return paramDouble.isNaN();
  }
  
  private boolean isDoubleSafeInteger(Double paramDouble) {
    boolean bool;
    if (isDoubleInteger(paramDouble) && paramDouble.doubleValue() <= 9.007199254740991E15D && paramDouble.doubleValue() >= -9.007199254740991E15D) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static Object isFinite(Object paramObject) {
    boolean bool;
    paramObject = Double.valueOf(ScriptRuntime.toNumber(paramObject));
    if (!paramObject.isInfinite() && !paramObject.isNaN()) {
      bool = true;
    } else {
      bool = false;
    } 
    return ScriptRuntime.wrapBoolean(bool);
  }
  
  private boolean isInteger(Number paramNumber) {
    return ScriptRuntime.toBoolean(Boolean.valueOf(isDoubleInteger(doubleVal(paramNumber))));
  }
  
  private Object isNaN(Number paramNumber) {
    return Boolean.valueOf(ScriptRuntime.toBoolean(Boolean.valueOf(isDoubleNan(doubleVal(paramNumber)))));
  }
  
  private boolean isSafeInteger(Number paramNumber) {
    return ScriptRuntime.toBoolean(Boolean.valueOf(isDoubleSafeInteger(doubleVal(paramNumber))));
  }
  
  private static String num_to(double paramDouble, Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramArrayOfObject.length == 0) {
      paramInt3 = 0;
      paramInt2 = paramInt1;
      paramInt1 = paramInt3;
    } else {
      StringBuilder stringBuilder1;
      double d = ScriptRuntime.toInteger(paramArrayOfObject[0]);
      if (d >= paramInt3 && d <= 100.0D) {
        paramInt1 = ScriptRuntime.toInt32(d);
        stringBuilder1 = new StringBuilder();
        DToA.JS_dtostr(stringBuilder1, paramInt2, paramInt1 + paramInt4, paramDouble);
        return stringBuilder1.toString();
      } 
      throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage1("msg.bad.precision", ScriptRuntime.toString(stringBuilder1[0])));
    } 
    StringBuilder stringBuilder = new StringBuilder();
    DToA.JS_dtostr(stringBuilder, paramInt2, paramInt1 + paramInt4, paramDouble);
    return stringBuilder.toString();
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    StringBuilder stringBuilder;
    if (!paramIdFunctionObject.hasTag(NUMBER_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    double d = 0.0D;
    if (i == 1) {
      if (paramArrayOfObject.length >= 1)
        d = ScriptRuntime.toNumber(paramArrayOfObject[0]); 
      return (paramScriptable2 == null) ? new NativeNumber(d) : ScriptRuntime.wrapNumber(d);
    } 
    if (i < 1)
      return execConstructorCall(i, paramArrayOfObject); 
    if (paramScriptable2 instanceof NativeNumber) {
      d = ((NativeNumber)paramScriptable2).doubleValue;
      int j = 10;
      switch (i) {
        default:
          throw new IllegalArgumentException(String.valueOf(i));
        case 8:
          return (paramArrayOfObject.length == 0 || paramArrayOfObject[0] == Undefined.instance) ? ScriptRuntime.numberToString(d, 10) : (Double.isNaN(d) ? "NaN" : (Double.isInfinite(d) ? ((d >= 0.0D) ? "Infinity" : "-Infinity") : num_to(d, paramArrayOfObject, 0, 4, 1, 0)));
        case 7:
          return Double.isNaN(d) ? "NaN" : (Double.isInfinite(d) ? ((d >= 0.0D) ? "Infinity" : "-Infinity") : num_to(d, paramArrayOfObject, 1, 3, 0, 1));
        case 6:
          return num_to(d, paramArrayOfObject, 2, 2, -20, 0);
        case 5:
          return ScriptRuntime.wrapNumber(d);
        case 4:
          stringBuilder = new StringBuilder();
          stringBuilder.append("(new Number(");
          stringBuilder.append(ScriptRuntime.toString(d));
          stringBuilder.append("))");
          return stringBuilder.toString();
        case 2:
        case 3:
          break;
      } 
      if (paramArrayOfObject.length != 0 && paramArrayOfObject[0] != Undefined.instance)
        j = ScriptRuntime.toInt32(paramArrayOfObject[0]); 
      return ScriptRuntime.numberToString(d, j);
    } 
    throw incompatibleCallError(stringBuilder);
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    paramIdFunctionObject.defineProperty("NaN", ScriptRuntime.NaNobj, 7);
    paramIdFunctionObject.defineProperty("POSITIVE_INFINITY", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
    paramIdFunctionObject.defineProperty("NEGATIVE_INFINITY", ScriptRuntime.wrapNumber(Double.NEGATIVE_INFINITY), 7);
    paramIdFunctionObject.defineProperty("MAX_VALUE", ScriptRuntime.wrapNumber(Double.MAX_VALUE), 7);
    paramIdFunctionObject.defineProperty("MIN_VALUE", ScriptRuntime.wrapNumber(Double.MIN_VALUE), 7);
    paramIdFunctionObject.defineProperty("MAX_SAFE_INTEGER", ScriptRuntime.wrapNumber(9.007199254740991E15D), 7);
    paramIdFunctionObject.defineProperty("MIN_SAFE_INTEGER", ScriptRuntime.wrapNumber(-9.007199254740991E15D), 7);
    addIdFunctionProperty(paramIdFunctionObject, NUMBER_TAG, -1, "isFinite", 1);
    addIdFunctionProperty(paramIdFunctionObject, NUMBER_TAG, -2, "isNaN", 1);
    addIdFunctionProperty(paramIdFunctionObject, NUMBER_TAG, -3, "isInteger", 1);
    addIdFunctionProperty(paramIdFunctionObject, NUMBER_TAG, -4, "isSafeInteger", 1);
    addIdFunctionProperty(paramIdFunctionObject, NUMBER_TAG, -5, "parseFloat", 1);
    addIdFunctionProperty(paramIdFunctionObject, NUMBER_TAG, -6, "parseInt", 1);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i != 7) {
      if (i != 8) {
        if (i != 11) {
          if (i != 13) {
            if (i == 14) {
              str = "toLocaleString";
              b = 3;
            } 
          } else {
            str = "toExponential";
            b = 7;
          } 
        } else {
          i = paramString.charAt(0);
          if (i == 99) {
            str = "constructor";
            b = 1;
          } else if (i == 116) {
            str = "toPrecision";
            b = 8;
          } 
        } 
      } else {
        i = paramString.charAt(3);
        if (i == 111) {
          str = "toSource";
          b = 4;
        } else if (i == 116) {
          str = "toString";
          b = 2;
        } 
      } 
    } else {
      i = paramString.charAt(0);
      if (i == 116) {
        str = "toFixed";
        b = 6;
      } else if (i == 118) {
        str = "valueOf";
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
  
  public String getClassName() {
    return "Number";
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 8:
        bool = true;
        str = "toPrecision";
        break;
      case 7:
        bool = true;
        str = "toExponential";
        break;
      case 6:
        bool = true;
        str = "toFixed";
        break;
      case 5:
        bool = false;
        str = "valueOf";
        break;
      case 4:
        bool = false;
        str = "toSource";
        break;
      case 3:
        bool = true;
        str = "toLocaleString";
        break;
      case 2:
        bool = true;
        str = "toString";
        break;
      case 1:
        bool = true;
        str = "constructor";
        break;
    } 
    initPrototypeMethod(NUMBER_TAG, paramInt, str, bool);
  }
  
  public String toString() {
    return ScriptRuntime.numberToString(this.doubleValue, 10);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeNumber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */