package com.trendmicro.hippo;

import java.util.Iterator;

public final class NativeIterator extends IdScriptableObject {
  public static final String ITERATOR_PROPERTY_NAME = "__iterator__";
  
  private static final Object ITERATOR_TAG = "Iterator";
  
  private static final int Id___iterator__ = 3;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_next = 2;
  
  private static final int MAX_PROTOTYPE_ID = 3;
  
  private static final String STOP_ITERATION = "StopIteration";
  
  private static final long serialVersionUID = -4136968203581667681L;
  
  private Object objectIterator;
  
  private NativeIterator() {}
  
  private NativeIterator(Object paramObject) {
    this.objectIterator = paramObject;
  }
  
  private static Iterator<?> getJavaIterator(Object paramObject) {
    if (paramObject instanceof Wrapper) {
      Object object = ((Wrapper)paramObject).unwrap();
      paramObject = null;
      if (object instanceof Iterator)
        paramObject = object; 
      if (object instanceof Iterable)
        paramObject = ((Iterable)object).iterator(); 
      return (Iterator<?>)paramObject;
    } 
    return null;
  }
  
  public static Object getStopIterationObject(Scriptable paramScriptable) {
    return ScriptableObject.getTopScopeValue(ScriptableObject.getTopLevelScope(paramScriptable), ITERATOR_TAG);
  }
  
  static void init(ScriptableObject paramScriptableObject, boolean paramBoolean) {
    (new NativeIterator()).exportAsJSClass(3, paramScriptableObject, paramBoolean);
    NativeGenerator.init(paramScriptableObject, paramBoolean);
    StopIteration stopIteration = new StopIteration();
    stopIteration.setPrototype(getObjectPrototype(paramScriptableObject));
    stopIteration.setParentScope(paramScriptableObject);
    if (paramBoolean)
      stopIteration.sealObject(); 
    ScriptableObject.defineProperty(paramScriptableObject, "StopIteration", stopIteration, 2);
    paramScriptableObject.associateValue(ITERATOR_TAG, stopIteration);
  }
  
  private static Object jsConstructor(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    int i = paramArrayOfObject.length;
    boolean bool1 = false;
    if (i == 0 || paramArrayOfObject[0] == null || paramArrayOfObject[0] == Undefined.instance) {
      if (paramArrayOfObject.length == 0) {
        object = Undefined.instance;
      } else {
        object = paramArrayOfObject[0];
      } 
      throw ScriptRuntime.typeError1("msg.no.properties", ScriptRuntime.toString(object));
    } 
    Scriptable scriptable = ScriptRuntime.toObject((Context)object, paramScriptable1, paramArrayOfObject[0]);
    boolean bool2 = bool1;
    if (paramArrayOfObject.length > 1) {
      bool2 = bool1;
      if (ScriptRuntime.toBoolean(paramArrayOfObject[1]))
        bool2 = true; 
    } 
    if (paramScriptable2 != null) {
      Iterator<?> iterator = getJavaIterator(scriptable);
      if (iterator != null) {
        paramScriptable1 = ScriptableObject.getTopLevelScope(paramScriptable1);
        return object.getWrapFactory().wrap((Context)object, paramScriptable1, new WrappedJavaIterator(iterator, paramScriptable1), WrappedJavaIterator.class);
      } 
      Scriptable scriptable1 = ScriptRuntime.toIterator((Context)object, paramScriptable1, scriptable, bool2);
      if (scriptable1 != null)
        return scriptable1; 
    } 
    if (bool2) {
      i = 3;
    } else {
      i = 5;
    } 
    Object object = ScriptRuntime.enumInit(scriptable, (Context)object, paramScriptable1, i);
    ScriptRuntime.setEnumNumbers(object, true);
    object = new NativeIterator(object);
    object.setPrototype(ScriptableObject.getClassPrototype(paramScriptable1, object.getClassName()));
    object.setParentScope(paramScriptable1);
    return object;
  }
  
  private Object next(Context paramContext, Scriptable paramScriptable) {
    if (ScriptRuntime.enumNext(this.objectIterator).booleanValue())
      return ScriptRuntime.enumId(this.objectIterator, paramContext); 
    throw new JavaScriptException(getStopIterationObject(paramScriptable), null, 0);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    NativeIterator nativeIterator;
    if (!paramIdFunctionObject.hasTag(ITERATOR_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i == 1)
      return jsConstructor(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    if (paramScriptable2 instanceof NativeIterator) {
      nativeIterator = (NativeIterator)paramScriptable2;
      if (i != 2) {
        if (i == 3)
          return paramScriptable2; 
        throw new IllegalArgumentException(String.valueOf(i));
      } 
      return nativeIterator.next(paramContext, paramScriptable1);
    } 
    throw incompatibleCallError(nativeIterator);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 4) {
      str = "next";
      b = 2;
    } else if (i == 11) {
      str = "constructor";
      b = 1;
    } else if (i == 12) {
      str = "__iterator__";
      b = 3;
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
    return "Iterator";
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          b = 1;
          str = "__iterator__";
        } else {
          throw new IllegalArgumentException(String.valueOf(paramInt));
        } 
      } else {
        b = 0;
        str = "next";
      } 
    } else {
      b = 2;
      str = "constructor";
    } 
    initPrototypeMethod(ITERATOR_TAG, paramInt, str, b);
  }
  
  static class StopIteration extends NativeObject {
    private static final long serialVersionUID = 2485151085722377663L;
    
    public String getClassName() {
      return "StopIteration";
    }
    
    public boolean hasInstance(Scriptable param1Scriptable) {
      return param1Scriptable instanceof StopIteration;
    }
  }
  
  public static class WrappedJavaIterator {
    private Iterator<?> iterator;
    
    private Scriptable scope;
    
    WrappedJavaIterator(Iterator<?> param1Iterator, Scriptable param1Scriptable) {
      this.iterator = param1Iterator;
      this.scope = param1Scriptable;
    }
    
    public Object __iterator__(boolean param1Boolean) {
      return this;
    }
    
    public Object next() {
      if (this.iterator.hasNext())
        return this.iterator.next(); 
      throw new JavaScriptException(NativeIterator.getStopIterationObject(this.scope), null, 0);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */