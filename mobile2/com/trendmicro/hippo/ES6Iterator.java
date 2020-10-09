package com.trendmicro.hippo;

public abstract class ES6Iterator extends IdScriptableObject {
  public static final String DONE_PROPERTY = "done";
  
  private static final int Id_next = 1;
  
  private static final int MAX_PROTOTYPE_ID = 3;
  
  public static final String NEXT_METHOD = "next";
  
  private static final int SymbolId_iterator = 2;
  
  private static final int SymbolId_toStringTag = 3;
  
  public static final String VALUE_PROPERTY = "value";
  
  protected boolean exhausted = false;
  
  private String tag;
  
  ES6Iterator() {}
  
  ES6Iterator(Scriptable paramScriptable, String paramString) {
    this.tag = paramString;
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    setParentScope(paramScriptable);
    setPrototype((IdScriptableObject)ScriptableObject.getTopScopeValue(paramScriptable, paramString));
  }
  
  static void init(ScriptableObject paramScriptableObject, boolean paramBoolean, IdScriptableObject paramIdScriptableObject, String paramString) {
    if (paramScriptableObject != null) {
      paramIdScriptableObject.setParentScope(paramScriptableObject);
      paramIdScriptableObject.setPrototype(getObjectPrototype(paramScriptableObject));
    } 
    paramIdScriptableObject.activatePrototypeMap(3);
    if (paramBoolean)
      paramIdScriptableObject.sealObject(); 
    if (paramScriptableObject != null)
      paramScriptableObject.associateValue(paramString, paramIdScriptableObject); 
  }
  
  private Scriptable makeIteratorResult(Context paramContext, Scriptable paramScriptable, boolean paramBoolean, Object paramObject) {
    Scriptable scriptable = paramContext.newObject(paramScriptable);
    ScriptableObject.putProperty(scriptable, "value", paramObject);
    ScriptableObject.putProperty(scriptable, "done", Boolean.valueOf(paramBoolean));
    return scriptable;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    ES6Iterator eS6Iterator;
    if (!paramIdFunctionObject.hasTag(getTag()))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (paramScriptable2 instanceof ES6Iterator) {
      eS6Iterator = (ES6Iterator)paramScriptable2;
      if (i != 1) {
        if (i == 2)
          return eS6Iterator; 
        throw new IllegalArgumentException(String.valueOf(i));
      } 
      return eS6Iterator.next(paramContext, paramScriptable1);
    } 
    throw incompatibleCallError(eS6Iterator);
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.ITERATOR.equals(paramSymbol) ? 2 : (SymbolKey.TO_STRING_TAG.equals(paramSymbol) ? 3 : 0);
  }
  
  protected int findPrototypeId(String paramString) {
    return "next".equals(paramString) ? 1 : 0;
  }
  
  protected String getTag() {
    return this.tag;
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          initPrototypeValue(3, SymbolKey.TO_STRING_TAG, getClassName(), 3);
          return;
        } 
        throw new IllegalArgumentException(String.valueOf(paramInt));
      } 
      initPrototypeMethod(getTag(), paramInt, SymbolKey.ITERATOR, "[Symbol.iterator]", 3);
      return;
    } 
    initPrototypeMethod(getTag(), paramInt, "next", 0);
  }
  
  protected abstract boolean isDone(Context paramContext, Scriptable paramScriptable);
  
  protected Object next(Context paramContext, Scriptable paramScriptable) {
    boolean bool;
    Object object = Undefined.instance;
    if (isDone(paramContext, paramScriptable) || this.exhausted) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!bool) {
      object = nextValue(paramContext, paramScriptable);
    } else {
      this.exhausted = true;
    } 
    return makeIteratorResult(paramContext, paramScriptable, bool, object);
  }
  
  protected abstract Object nextValue(Context paramContext, Scriptable paramScriptable);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ES6Iterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */