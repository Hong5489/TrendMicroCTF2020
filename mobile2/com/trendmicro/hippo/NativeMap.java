package com.trendmicro.hippo;

import java.util.Iterator;

public class NativeMap extends IdScriptableObject {
  static final String ITERATOR_TAG = "Map Iterator";
  
  private static final int Id_clear = 6;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_delete = 4;
  
  private static final int Id_entries = 9;
  
  private static final int Id_forEach = 10;
  
  private static final int Id_get = 3;
  
  private static final int Id_has = 5;
  
  private static final int Id_keys = 7;
  
  private static final int Id_set = 2;
  
  private static final int Id_values = 8;
  
  private static final Object MAP_TAG = "Map";
  
  private static final int MAX_PROTOTYPE_ID = 12;
  
  private static final Object NULL_VALUE = new Object();
  
  private static final int SymbolId_getSize = 11;
  
  private static final int SymbolId_toStringTag = 12;
  
  private final Hashtable entries = new Hashtable();
  
  private boolean instanceOfMap = false;
  
  static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    NativeMap nativeMap = new NativeMap();
    nativeMap.exportAsJSClass(12, paramScriptable, false);
    paramScriptable = paramContext.newObject(paramScriptable);
    paramScriptable.put("enumerable", paramScriptable, Boolean.valueOf(false));
    paramScriptable.put("configurable", paramScriptable, Boolean.valueOf(true));
    paramScriptable.put("get", paramScriptable, nativeMap.get(NativeSet.GETSIZE, nativeMap));
    nativeMap.defineOwnProperty(paramContext, "size", (ScriptableObject)paramScriptable);
    if (paramBoolean)
      nativeMap.sealObject(); 
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
        Object object1 = paramObject1;
        if (paramObject1 == null)
          object1 = Undefined.SCRIPTABLE_UNDEFINED; 
        Hashtable.Entry entry = iterator.next();
        Object object2 = entry.value;
        paramObject1 = object2;
        if (object2 == NULL_VALUE)
          paramObject1 = null; 
        callable.call(paramContext, paramScriptable, (Scriptable)object1, new Object[] { paramObject1, entry.key, this });
      } 
      return Undefined.instance;
    } 
    throw ScriptRuntime.typeError2("msg.isnt.function", paramObject1, ScriptRuntime.typeof(paramObject1));
  }
  
  private Object js_get(Object paramObject) {
    paramObject = this.entries.get(paramObject);
    return (paramObject == null) ? Undefined.instance : ((paramObject == NULL_VALUE) ? null : paramObject);
  }
  
  private Object js_getSize() {
    return Integer.valueOf(this.entries.size());
  }
  
  private Object js_has(Object paramObject) {
    return Boolean.valueOf(this.entries.has(paramObject));
  }
  
  private Object js_iterator(Scriptable paramScriptable, NativeCollectionIterator.Type paramType) {
    return new NativeCollectionIterator(paramScriptable, "Map Iterator", paramType, this.entries.iterator());
  }
  
  private Object js_set(Object paramObject1, Object paramObject2) {
    if (paramObject2 == null)
      paramObject2 = NULL_VALUE; 
    Object object = paramObject1;
    paramObject1 = object;
    if (object instanceof Number) {
      paramObject1 = object;
      if (((Number)object).doubleValue() == ScriptRuntime.negativeZero)
        paramObject1 = Double.valueOf(0.0D); 
    } 
    this.entries.put(paramObject1, paramObject2);
    return this;
  }
  
  static void loadFromIterable(Context paramContext, Scriptable paramScriptable, ScriptableObject paramScriptableObject, Object paramObject) {
    if (paramObject == null || Undefined.instance.equals(paramObject))
      return; 
    paramObject = ScriptRuntime.callIterator(paramObject, paramContext, paramScriptable);
    if (Undefined.instance.equals(paramObject))
      return; 
    ScriptableObject scriptableObject = ensureScriptableObject(paramContext.newObject(paramScriptable, paramScriptableObject.getClassName()));
    Callable callable = ScriptRuntime.getPropFunctionAndThis(scriptableObject.getPrototype(), "set", paramContext, paramScriptable);
    ScriptRuntime.lastStoredScriptable(paramContext);
    IteratorLikeIterable iteratorLikeIterable = new IteratorLikeIterable(paramContext, paramScriptable, paramObject);
    try {
      IteratorLikeIterable.Itr itr = iteratorLikeIterable.iterator();
      while (itr.hasNext()) {
        Object object = ScriptableObject.ensureScriptable(itr.next());
        if (!(object instanceof Symbol)) {
          Object object1 = object.get(0, (Scriptable)object);
          paramObject = object1;
          if (object1 == NOT_FOUND)
            paramObject = Undefined.instance; 
          object = object.get(1, (Scriptable)object);
          object1 = object;
          if (object == NOT_FOUND)
            object1 = Undefined.instance; 
          callable.call(paramContext, paramScriptable, paramScriptableObject, new Object[] { paramObject, object1 });
          continue;
        } 
        throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(object));
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
  
  private NativeMap realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable != null)
      try {
        paramScriptable = paramScriptable;
        if (((NativeMap)paramScriptable).instanceOfMap)
          return (NativeMap)paramScriptable; 
        throw incompatibleCallError(paramIdFunctionObject);
      } catch (ClassCastException classCastException) {
        throw incompatibleCallError(paramIdFunctionObject);
      }  
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object1;
    StringBuilder stringBuilder;
    Object object2;
    Object object3;
    NativeMap nativeMap;
    if (!paramIdFunctionObject.hasTag(MAP_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    switch (paramIdFunctionObject.methodId()) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Map.prototype has no method: ");
        stringBuilder.append(paramIdFunctionObject.getFunctionName());
        throw new IllegalArgumentException(stringBuilder.toString());
      case 11:
        return realThis(paramScriptable2, paramIdFunctionObject).js_getSize();
      case 10:
        nativeMap = realThis(paramScriptable2, paramIdFunctionObject);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        if (paramArrayOfObject.length > 1) {
          object3 = paramArrayOfObject[1];
        } else {
          object3 = Undefined.instance;
        } 
        return nativeMap.js_forEach((Context)stringBuilder, paramScriptable1, object1, object3);
      case 9:
        return realThis((Scriptable)object3, (IdFunctionObject)object1).js_iterator(paramScriptable1, NativeCollectionIterator.Type.BOTH);
      case 8:
        return realThis((Scriptable)object3, (IdFunctionObject)object1).js_iterator(paramScriptable1, NativeCollectionIterator.Type.VALUES);
      case 7:
        return realThis((Scriptable)object3, (IdFunctionObject)object1).js_iterator(paramScriptable1, NativeCollectionIterator.Type.KEYS);
      case 6:
        return realThis((Scriptable)object3, (IdFunctionObject)object1).js_clear();
      case 5:
        object2 = realThis((Scriptable)object3, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        return object2.js_has(object1);
      case 4:
        object2 = realThis((Scriptable)object3, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        return object2.js_delete(object1);
      case 3:
        object2 = realThis((Scriptable)object3, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        return object2.js_get(object1);
      case 2:
        paramScriptable1 = realThis((Scriptable)object3, (IdFunctionObject)object1);
        if (paramArrayOfObject.length > 0) {
          object1 = paramArrayOfObject[0];
        } else {
          object1 = Undefined.instance;
        } 
        if (paramArrayOfObject.length > 1) {
          Object object = paramArrayOfObject[1];
        } else {
          object2 = Undefined.instance;
        } 
        return paramScriptable1.js_set(object1, object2);
      case 1:
        break;
    } 
    if (object3 == null) {
      object1 = new NativeMap();
      ((NativeMap)object1).instanceOfMap = true;
      if (paramArrayOfObject.length > 0)
        loadFromIterable((Context)object2, paramScriptable1, (ScriptableObject)object1, paramArrayOfObject[0]); 
      return object1;
    } 
    throw ScriptRuntime.typeError1("msg.no.new", "Map");
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return NativeSet.GETSIZE.equals(paramSymbol) ? 11 : (SymbolKey.ITERATOR.equals(paramSymbol) ? 9 : (SymbolKey.TO_STRING_TAG.equals(paramSymbol) ? 12 : 0));
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
                j = 9;
              } else {
                j = i;
                str2 = str1;
                if (c == 'f') {
                  str2 = "forEach";
                  j = 10;
                } 
              } 
            } 
          } else {
            char c = paramString.charAt(0);
            if (c == 'd') {
              str2 = "delete";
              j = 4;
            } else {
              j = i;
              str2 = str1;
              if (c == 'v') {
                str2 = "values";
                j = 8;
              } 
            } 
          } 
        } else {
          str2 = "clear";
          j = 6;
        } 
      } else {
        str2 = "keys";
        j = 7;
      } 
    } else {
      char c = paramString.charAt(0);
      if (c == 'g') {
        j = i;
        str2 = str1;
        if (paramString.charAt(2) == 't') {
          j = i;
          str2 = str1;
          if (paramString.charAt(1) == 'e')
            return 3; 
        } 
      } else if (c == 'h') {
        j = i;
        str2 = str1;
        if (paramString.charAt(2) == 's') {
          j = i;
          str2 = str1;
          if (paramString.charAt(1) == 'a')
            return 5; 
        } 
      } else {
        j = i;
        str2 = str1;
        if (c == 's') {
          j = i;
          str2 = str1;
          if (paramString.charAt(2) == 't') {
            j = i;
            str2 = str1;
            if (paramString.charAt(1) == 'e')
              return 2; 
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
    return "Map";
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt != 11) {
      if (paramInt != 12) {
        byte b;
        String str;
        switch (paramInt) {
          default:
            throw new IllegalArgumentException(String.valueOf(paramInt));
          case 10:
            b = 1;
            str = "forEach";
            break;
          case 9:
            b = 0;
            str = "entries";
            break;
          case 8:
            b = 0;
            str = "values";
            break;
          case 7:
            b = 0;
            str = "keys";
            break;
          case 6:
            b = 0;
            str = "clear";
            break;
          case 5:
            b = 1;
            str = "has";
            break;
          case 4:
            b = 1;
            str = "delete";
            break;
          case 3:
            b = 1;
            str = "get";
            break;
          case 2:
            b = 2;
            str = "set";
            break;
          case 1:
            b = 0;
            str = "constructor";
            break;
        } 
        initPrototypeMethod(MAP_TAG, paramInt, str, (String)null, b);
        return;
      } 
      initPrototypeValue(12, SymbolKey.TO_STRING_TAG, getClassName(), 3);
      return;
    } 
    initPrototypeMethod(MAP_TAG, paramInt, NativeSet.GETSIZE, "get size", 0);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */