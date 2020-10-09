package com.trendmicro.hippo;

import java.util.Iterator;

public class NativeSet extends IdScriptableObject {
  static final SymbolKey GETSIZE;
  
  static final String ITERATOR_TAG = "Set Iterator";
  
  private static final int Id_add = 2;
  
  private static final int Id_clear = 5;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_delete = 3;
  
  private static final int Id_entries = 7;
  
  private static final int Id_forEach = 8;
  
  private static final int Id_has = 4;
  
  private static final int Id_keys = 6;
  
  private static final int Id_values = 6;
  
  private static final int MAX_PROTOTYPE_ID = 10;
  
  private static final Object SET_TAG = "Set";
  
  private static final int SymbolId_getSize = 9;
  
  private static final int SymbolId_toStringTag = 10;
  
  private final Hashtable entries = new Hashtable();
  
  private boolean instanceOfSet = false;
  
  static {
    GETSIZE = new SymbolKey("[Symbol.getSize]");
  }
  
  static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    NativeSet nativeSet = new NativeSet();
    nativeSet.exportAsJSClass(10, paramScriptable, false);
    paramScriptable = paramContext.newObject(paramScriptable);
    paramScriptable.put("enumerable", paramScriptable, Boolean.valueOf(false));
    paramScriptable.put("configurable", paramScriptable, Boolean.valueOf(true));
    paramScriptable.put("get", paramScriptable, nativeSet.get(GETSIZE, nativeSet));
    nativeSet.defineOwnProperty(paramContext, "size", (ScriptableObject)paramScriptable);
    if (paramBoolean)
      nativeSet.sealObject(); 
  }
  
  private Object js_add(Object paramObject) {
    Object object = paramObject;
    if (paramObject instanceof Number) {
      object = paramObject;
      if (((Number)paramObject).doubleValue() == ScriptRuntime.negativeZero)
        object = Double.valueOf(0.0D); 
    } 
    this.entries.put(object, object);
    return this;
  }
  
  private Object js_clear() {
    this.entries.clear();
    return Undefined.instance;
  }
  
  private Object js_delete(Object paramObject) {
    boolean bool;
    if (this.entries.delete(paramObject) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return Boolean.valueOf(bool);
  }
  
  private Object js_forEach(Context paramContext, Scriptable paramScriptable, Object paramObject1, Object paramObject2) {
    if (paramObject1 instanceof Callable) {
      Callable callable = (Callable)paramObject1;
      boolean bool = paramContext.isStrictMode();
      Iterator<Hashtable.Entry> iterator = this.entries.iterator();
      while (iterator.hasNext()) {
        Scriptable scriptable = ScriptRuntime.toObjectOrNull(paramContext, paramObject2, paramScriptable);
        paramObject1 = scriptable;
        if (scriptable == null) {
          paramObject1 = scriptable;
          if (!bool)
            paramObject1 = paramScriptable; 
        } 
        Object object = paramObject1;
        if (paramObject1 == null)
          object = Undefined.SCRIPTABLE_UNDEFINED; 
        paramObject1 = iterator.next();
        callable.call(paramContext, paramScriptable, (Scriptable)object, new Object[] { ((Hashtable.Entry)paramObject1).value, ((Hashtable.Entry)paramObject1).value, this });
      } 
      return Undefined.instance;
    } 
    throw ScriptRuntime.notFunctionError(paramObject1);
  }
  
  private Object js_getSize() {
    return Integer.valueOf(this.entries.size());
  }
  
  private Object js_has(Object paramObject) {
    return Boolean.valueOf(this.entries.has(paramObject));
  }
  
  private Object js_iterator(Scriptable paramScriptable, NativeCollectionIterator.Type paramType) {
    return new NativeCollectionIterator(paramScriptable, "Set Iterator", paramType, this.entries.iterator());
  }
  
  static void loadFromIterable(Context paramContext, Scriptable paramScriptable, ScriptableObject paramScriptableObject, Object paramObject) {
    if (paramObject == null || Undefined.instance.equals(paramObject))
      return; 
    paramObject = ScriptRuntime.callIterator(paramObject, paramContext, paramScriptable);
    if (Undefined.instance.equals(paramObject))
      return; 
    ScriptableObject scriptableObject = ensureScriptableObject(paramContext.newObject(paramScriptable, paramScriptableObject.getClassName()));
    Callable callable = ScriptRuntime.getPropFunctionAndThis(scriptableObject.getPrototype(), "add", paramContext, paramScriptable);
    ScriptRuntime.lastStoredScriptable(paramContext);
    IteratorLikeIterable iteratorLikeIterable = new IteratorLikeIterable(paramContext, paramScriptable, paramObject);
    try {
      IteratorLikeIterable.Itr itr = iteratorLikeIterable.iterator();
      while (itr.hasNext()) {
        paramObject = itr.next();
        if (paramObject == Scriptable.NOT_FOUND)
          paramObject = Undefined.instance; 
        callable.call(paramContext, paramScriptable, paramScriptableObject, new Object[] { paramObject });
      } 
      return;
    } finally {
      try {
        iteratorLikeIterable.close();
      } finally {
        paramScriptable = null;
      } 
    } 
  }
  
  private NativeSet realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable != null)
      try {
        paramScriptable = paramScriptable;
        if (((NativeSet)paramScriptable).instanceOfSet)
          return (NativeSet)paramScriptable; 
        throw incompatibleCallError(paramIdFunctionObject);
      } catch (ClassCastException classCastException) {
        throw incompatibleCallError(paramIdFunctionObject);
      }  
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object1;
    StringBuilder stringBuilder;
    NativeSet nativeSet1;
    Object object2;
    NativeSet nativeSet2;
    if (!paramIdFunctionObject.hasTag(SET_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    switch (paramIdFunctionObject.methodId()) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Set.prototype has no method: ");
        stringBuilder.append(paramIdFunctionObject.getFunctionName());
        throw new IllegalArgumentException(stringBuilder.toString());
      case 9:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getSize();
      case 8:
        nativeSet2 = realThis(paramScriptable2, paramIdFunctionObject);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        if (paramArrayOfObject.length > 1) {
          object2 = paramArrayOfObject[1];
        } else {
          object2 = Undefined.instance;
        } 
        return nativeSet2.js_forEach((Context)stringBuilder, paramScriptable1, object1, object2);
      case 7:
        return realThis((Scriptable)object2, (IdFunctionObject)object1).js_iterator(paramScriptable1, NativeCollectionIterator.Type.BOTH);
      case 6:
        return realThis((Scriptable)object2, (IdFunctionObject)object1).js_iterator(paramScriptable1, NativeCollectionIterator.Type.VALUES);
      case 5:
        return realThis((Scriptable)object2, (IdFunctionObject)object1).js_clear();
      case 4:
        nativeSet1 = realThis((Scriptable)object2, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        return nativeSet1.js_has(object1);
      case 3:
        nativeSet1 = realThis((Scriptable)object2, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        return nativeSet1.js_delete(object1);
      case 2:
        nativeSet1 = realThis((Scriptable)object2, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        return nativeSet1.js_add(object1);
      case 1:
        break;
    } 
    if (object2 == null) {
      object1 = new NativeSet();
      ((NativeSet)object1).instanceOfSet = true;
      if (paramArrayOfObject.length > 0)
        loadFromIterable((Context)nativeSet1, paramScriptable1, (ScriptableObject)object1, paramArrayOfObject[0]); 
      return object1;
    } 
    throw ScriptRuntime.typeError1("msg.no.new", "Set");
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return GETSIZE.equals(paramSymbol) ? 9 : (SymbolKey.ITERATOR.equals(paramSymbol) ? 6 : (SymbolKey.TO_STRING_TAG.equals(paramSymbol) ? 10 : 0));
  }
  
  protected int findPrototypeId(String paramString) {
    String str2;
    int i = 0;
    String str1 = null;
    int j = paramString.length();
    if (j != 3) {
      if (j != 4) {
        if (j != 5) {
          if (j != 6) {
            if (j != 7) {
              if (j != 11) {
                j = i;
                str2 = str1;
              } else {
                str2 = "constructor";
                j = 1;
              } 
            } else {
              char c = paramString.charAt(0);
              if (c == 'e') {
                str2 = "entries";
                j = 7;
              } else {
                j = i;
                str2 = str1;
                if (c == 'f') {
                  str2 = "forEach";
                  j = 8;
                } 
              } 
            } 
          } else {
            char c = paramString.charAt(0);
            if (c == 'd') {
              str2 = "delete";
              j = 3;
            } else {
              j = i;
              str2 = str1;
              if (c == 'v') {
                str2 = "values";
                j = 6;
              } 
            } 
          } 
        } else {
          str2 = "clear";
          j = 5;
        } 
      } else {
        str2 = "keys";
        j = 6;
      } 
    } else {
      char c = paramString.charAt(0);
      if (c == 'a') {
        j = i;
        str2 = str1;
        if (paramString.charAt(2) == 'd') {
          j = i;
          str2 = str1;
          if (paramString.charAt(1) == 'd')
            return 2; 
        } 
      } else {
        j = i;
        str2 = str1;
        if (c == 'h') {
          j = i;
          str2 = str1;
          if (paramString.charAt(2) == 's') {
            j = i;
            str2 = str1;
            if (paramString.charAt(1) == 'a')
              return 4; 
          } 
        } 
      } 
    } 
    i = j;
    if (str2 != null) {
      i = j;
      if (str2 != paramString) {
        i = j;
        if (!str2.equals(paramString))
          i = 0; 
      } 
    } 
    return i;
  }
  
  public String getClassName() {
    return "Set";
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt != 9) {
      if (paramInt != 10) {
        boolean bool;
        String str;
        switch (paramInt) {
          default:
            throw new IllegalArgumentException(String.valueOf(paramInt));
          case 8:
            bool = true;
            str = "forEach";
            break;
          case 7:
            bool = false;
            str = "entries";
            break;
          case 6:
            bool = false;
            str = "values";
            break;
          case 5:
            bool = false;
            str = "clear";
            break;
          case 4:
            bool = true;
            str = "has";
            break;
          case 3:
            bool = true;
            str = "delete";
            break;
          case 2:
            bool = true;
            str = "add";
            break;
          case 1:
            bool = false;
            str = "constructor";
            break;
        } 
        initPrototypeMethod(SET_TAG, paramInt, str, (String)null, bool);
        return;
      } 
      initPrototypeValue(10, SymbolKey.TO_STRING_TAG, getClassName(), 3);
      return;
    } 
    initPrototypeMethod(SET_TAG, paramInt, GETSIZE, "get size", 0);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */