package com.trendmicro.hippo.optimizer;

import com.trendmicro.hippo.ArrowFunction;
import com.trendmicro.hippo.Callable;
import com.trendmicro.hippo.ConsString;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.JavaScriptException;
import com.trendmicro.hippo.NativeFunction;
import com.trendmicro.hippo.NativeGenerator;
import com.trendmicro.hippo.NativeIterator;
import com.trendmicro.hippo.Script;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;

public final class OptRuntime extends ScriptRuntime {
  public static final Double minusOneObj;
  
  public static final Double oneObj;
  
  public static final Double zeroObj = new Double(0.0D);
  
  static {
    oneObj = new Double(1.0D);
    minusOneObj = new Double(-1.0D);
  }
  
  public static Object add(double paramDouble, Object paramObject) {
    Object object = paramObject;
    if (paramObject instanceof Scriptable)
      object = ((Scriptable)paramObject).getDefaultValue(null); 
    return !(object instanceof CharSequence) ? wrapDouble(toNumber(object) + paramDouble) : new ConsString(toString(paramDouble), (CharSequence)object);
  }
  
  public static Object add(Object paramObject, double paramDouble) {
    Object object = paramObject;
    if (paramObject instanceof Scriptable)
      object = ((Scriptable)paramObject).getDefaultValue(null); 
    return !(object instanceof CharSequence) ? wrapDouble(toNumber(object) + paramDouble) : new ConsString((CharSequence)object, toString(paramDouble));
  }
  
  public static Function bindThis(NativeFunction paramNativeFunction, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2) {
    return (Function)new ArrowFunction(paramContext, paramScriptable1, (Callable)paramNativeFunction, paramScriptable2);
  }
  
  public static Object call0(Callable paramCallable, Scriptable paramScriptable1, Context paramContext, Scriptable paramScriptable2) {
    return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, ScriptRuntime.emptyArgs);
  }
  
  public static Object call1(Callable paramCallable, Scriptable paramScriptable1, Object paramObject, Context paramContext, Scriptable paramScriptable2) {
    return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, new Object[] { paramObject });
  }
  
  public static Object call2(Callable paramCallable, Scriptable paramScriptable1, Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable2) {
    return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, new Object[] { paramObject1, paramObject2 });
  }
  
  public static Object callN(Callable paramCallable, Scriptable paramScriptable1, Object[] paramArrayOfObject, Context paramContext, Scriptable paramScriptable2) {
    return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, paramArrayOfObject);
  }
  
  public static Object callName(Object[] paramArrayOfObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    return getNameFunctionAndThis(paramString, paramContext, paramScriptable).call(paramContext, paramScriptable, lastStoredScriptable(paramContext), paramArrayOfObject);
  }
  
  public static Object callName0(String paramString, Context paramContext, Scriptable paramScriptable) {
    return getNameFunctionAndThis(paramString, paramContext, paramScriptable).call(paramContext, paramScriptable, lastStoredScriptable(paramContext), ScriptRuntime.emptyArgs);
  }
  
  public static Object callProp0(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    return getPropFunctionAndThis(paramObject, paramString, paramContext, paramScriptable).call(paramContext, paramScriptable, lastStoredScriptable(paramContext), ScriptRuntime.emptyArgs);
  }
  
  public static Object callSpecial(Context paramContext, Callable paramCallable, Scriptable paramScriptable1, Object[] paramArrayOfObject, Scriptable paramScriptable2, Scriptable paramScriptable3, int paramInt1, String paramString, int paramInt2) {
    return ScriptRuntime.callSpecial(paramContext, paramCallable, paramScriptable1, paramArrayOfObject, paramScriptable2, paramScriptable3, paramInt1, paramString, paramInt2);
  }
  
  public static Scriptable createNativeGenerator(NativeFunction paramNativeFunction, Scriptable paramScriptable1, Scriptable paramScriptable2, int paramInt1, int paramInt2) {
    return (Scriptable)new NativeGenerator(paramScriptable1, paramNativeFunction, new GeneratorState(paramScriptable2, paramInt1, paramInt2));
  }
  
  private static int[] decodeIntArray(String paramString, int paramInt) {
    if (paramInt == 0) {
      if (paramString == null)
        return null; 
      throw new IllegalArgumentException();
    } 
    if (paramString.length() == paramInt * 2 + 1 || paramString.charAt(0) == '\001') {
      int[] arrayOfInt = new int[paramInt];
      for (int i = 0; i != paramInt; i++) {
        int j = i * 2 + 1;
        arrayOfInt[i] = paramString.charAt(j) << 16 | paramString.charAt(j + 1);
      } 
      return arrayOfInt;
    } 
    throw new IllegalArgumentException();
  }
  
  @Deprecated
  public static Object elemIncrDecr(Object paramObject, double paramDouble, Context paramContext, int paramInt) {
    return elemIncrDecr(paramObject, paramDouble, paramContext, getTopCallScope(paramContext), paramInt);
  }
  
  public static Object elemIncrDecr(Object paramObject, double paramDouble, Context paramContext, Scriptable paramScriptable, int paramInt) {
    return ScriptRuntime.elemIncrDecr(paramObject, Double.valueOf(paramDouble), paramContext, paramScriptable, paramInt);
  }
  
  static String encodeIntArray(int[] paramArrayOfint) {
    if (paramArrayOfint == null)
      return null; 
    int i = paramArrayOfint.length;
    char[] arrayOfChar = new char[i * 2 + 1];
    arrayOfChar[0] = (char)'\001';
    for (int j = 0; j != i; j++) {
      int k = paramArrayOfint[j];
      int m = j * 2 + 1;
      arrayOfChar[m] = (char)(char)(k >>> 16);
      arrayOfChar[m + 1] = (char)(char)k;
    } 
    return new String(arrayOfChar);
  }
  
  public static Object[] getGeneratorLocalsState(Object paramObject) {
    paramObject = paramObject;
    if (((GeneratorState)paramObject).localsState == null)
      ((GeneratorState)paramObject).localsState = new Object[((GeneratorState)paramObject).maxLocals]; 
    return ((GeneratorState)paramObject).localsState;
  }
  
  public static Object[] getGeneratorStackState(Object paramObject) {
    paramObject = paramObject;
    if (((GeneratorState)paramObject).stackState == null)
      ((GeneratorState)paramObject).stackState = new Object[((GeneratorState)paramObject).maxStack]; 
    return ((GeneratorState)paramObject).stackState;
  }
  
  public static void initFunction(NativeFunction paramNativeFunction, int paramInt, Scriptable paramScriptable, Context paramContext) {
    ScriptRuntime.initFunction(paramContext, paramScriptable, paramNativeFunction, paramInt, false);
  }
  
  public static void main(Script paramScript, String[] paramArrayOfString) {
    ContextFactory.getGlobal().call(paramContext -> {
          ScriptableObject scriptableObject = getGlobal(paramContext);
          Object[] arrayOfObject = new Object[paramArrayOfString.length];
          System.arraycopy(paramArrayOfString, 0, arrayOfObject, 0, paramArrayOfString.length);
          scriptableObject.defineProperty("arguments", paramContext.newArray((Scriptable)scriptableObject, arrayOfObject), 2);
          paramScript.exec(paramContext, (Scriptable)scriptableObject);
          return null;
        });
  }
  
  public static Scriptable newArrayLiteral(Object[] paramArrayOfObject, String paramString, int paramInt, Context paramContext, Scriptable paramScriptable) {
    return newArrayLiteral(paramArrayOfObject, decodeIntArray(paramString, paramInt), paramContext, paramScriptable);
  }
  
  public static Object newObjectSpecial(Context paramContext, Object paramObject, Object[] paramArrayOfObject, Scriptable paramScriptable1, Scriptable paramScriptable2, int paramInt) {
    return ScriptRuntime.newSpecial(paramContext, paramObject, paramArrayOfObject, paramScriptable1, paramInt);
  }
  
  public static Object[] padStart(Object[] paramArrayOfObject, int paramInt) {
    Object[] arrayOfObject = new Object[paramArrayOfObject.length + paramInt];
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject, paramInt, paramArrayOfObject.length);
    return arrayOfObject;
  }
  
  public static void throwStopIteration(Object paramObject) {
    throw new JavaScriptException(NativeIterator.getStopIterationObject((Scriptable)paramObject), "", 0);
  }
  
  public static Double wrapDouble(double paramDouble) {
    if (paramDouble == 0.0D) {
      if (1.0D / paramDouble > 0.0D)
        return zeroObj; 
    } else {
      if (paramDouble == 1.0D)
        return oneObj; 
      if (paramDouble == -1.0D)
        return minusOneObj; 
      if (paramDouble != paramDouble)
        return NaNobj; 
    } 
    return Double.valueOf(paramDouble);
  }
  
  public static class GeneratorState {
    static final String CLASS_NAME = "com/trendmicro/hippo/optimizer/OptRuntime$GeneratorState";
    
    static final String resumptionPoint_NAME = "resumptionPoint";
    
    static final String resumptionPoint_TYPE = "I";
    
    static final String thisObj_NAME = "thisObj";
    
    static final String thisObj_TYPE = "Lcom/trendmicro/hippo/Scriptable;";
    
    Object[] localsState;
    
    int maxLocals;
    
    int maxStack;
    
    public int resumptionPoint;
    
    Object[] stackState;
    
    public Scriptable thisObj;
    
    GeneratorState(Scriptable param1Scriptable, int param1Int1, int param1Int2) {
      this.thisObj = param1Scriptable;
      this.maxLocals = param1Int1;
      this.maxStack = param1Int2;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/OptRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */