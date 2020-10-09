package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.WeakHashMap;

public class NativeWeakMap extends IdScriptableObject {
  private static final int Id_constructor = 1;
  
  private static final int Id_delete = 2;
  
  private static final int Id_get = 3;
  
  private static final int Id_has = 4;
  
  private static final int Id_set = 5;
  
  private static final Object MAP_TAG = "WeakMap";
  
  private static final int MAX_PROTOTYPE_ID = 6;
  
  private static final Object NULL_VALUE = new Object();
  
  private static final int SymbolId_toStringTag = 6;
  
  private boolean instanceOfWeakMap = false;
  
  private transient WeakHashMap<Scriptable, Object> map = new WeakHashMap<>();
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeWeakMap()).exportAsJSClass(6, paramScriptable, paramBoolean);
  }
  
  private Object js_delete(Object paramObject) {
    boolean bool = ScriptRuntime.isObject(paramObject);
    boolean bool1 = false;
    if (!bool)
      return Boolean.valueOf(false); 
    if (this.map.remove(paramObject) != null)
      bool1 = true; 
    return Boolean.valueOf(bool1);
  }
  
  private Object js_get(Object paramObject) {
    if (!ScriptRuntime.isObject(paramObject))
      return Undefined.instance; 
    paramObject = this.map.get(paramObject);
    return (paramObject == null) ? Undefined.instance : ((paramObject == NULL_VALUE) ? null : paramObject);
  }
  
  private Object js_has(Object paramObject) {
    return !ScriptRuntime.isObject(paramObject) ? Boolean.valueOf(false) : Boolean.valueOf(this.map.containsKey(paramObject));
  }
  
  private Object js_set(Object paramObject1, Object paramObject2) {
    if (ScriptRuntime.isObject(paramObject1)) {
      if (paramObject2 == null)
        paramObject2 = NULL_VALUE; 
      this.map.put((Scriptable)paramObject1, paramObject2);
      return this;
    } 
    throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(paramObject1));
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    this.map = new WeakHashMap<>();
  }
  
  private NativeWeakMap realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable != null)
      try {
        paramScriptable = paramScriptable;
        if (((NativeWeakMap)paramScriptable).instanceOfWeakMap)
          return (NativeWeakMap)paramScriptable; 
        throw incompatibleCallError(paramIdFunctionObject);
      } catch (ClassCastException classCastException) {
        throw incompatibleCallError(paramIdFunctionObject);
      }  
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    NativeWeakMap nativeWeakMap;
    if (!paramIdFunctionObject.hasTag(MAP_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != 1) {
      Object object;
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i == 5) {
              Object object1;
              paramScriptable1 = realThis(paramScriptable2, paramIdFunctionObject);
              if (paramArrayOfObject.length > 0) {
                object = paramArrayOfObject[0];
              } else {
                object = Undefined.instance;
              } 
              if (paramArrayOfObject.length > 1) {
                object1 = paramArrayOfObject[1];
              } else {
                object1 = Undefined.instance;
              } 
              return paramScriptable1.js_set(object, object1);
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("WeakMap.prototype has no method: ");
            stringBuilder.append(object.getFunctionName());
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
          NativeWeakMap nativeWeakMap2 = realThis(paramScriptable2, (IdFunctionObject)object);
          if (paramArrayOfObject.length > 0) {
            object = paramArrayOfObject[0];
          } else {
            object = Undefined.instance;
          } 
          return nativeWeakMap2.js_has(object);
        } 
        NativeWeakMap nativeWeakMap1 = realThis(paramScriptable2, (IdFunctionObject)object);
        if (paramArrayOfObject.length > 0) {
          object = paramArrayOfObject[0];
        } else {
          object = Undefined.instance;
        } 
        return nativeWeakMap1.js_get(object);
      } 
      nativeWeakMap = realThis(paramScriptable2, (IdFunctionObject)object);
      if (paramArrayOfObject.length > 0) {
        object = paramArrayOfObject[0];
      } else {
        object = Undefined.instance;
      } 
      return nativeWeakMap.js_delete(object);
    } 
    if (paramScriptable2 == null) {
      NativeWeakMap nativeWeakMap1 = new NativeWeakMap();
      nativeWeakMap1.instanceOfWeakMap = true;
      if (paramArrayOfObject.length > 0)
        NativeMap.loadFromIterable((Context)nativeWeakMap, paramScriptable1, nativeWeakMap1, paramArrayOfObject[0]); 
      return nativeWeakMap1;
    } 
    throw ScriptRuntime.typeError1("msg.no.new", "WeakMap");
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.TO_STRING_TAG.equals(paramSymbol) ? 6 : 0;
  }
  
  protected int findPrototypeId(String paramString) {
    byte b2;
    String str2;
    byte b1 = 0;
    String str1 = null;
    int i = paramString.length();
    if (i == 3) {
      i = paramString.charAt(0);
      if (i == 103) {
        b2 = b1;
        str2 = str1;
        if (paramString.charAt(2) == 't') {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(1) == 'e')
            return 3; 
        } 
      } else if (i == 104) {
        b2 = b1;
        str2 = str1;
        if (paramString.charAt(2) == 's') {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(1) == 'a')
            return 4; 
        } 
      } else {
        b2 = b1;
        str2 = str1;
        if (i == 115) {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(2) == 't') {
            b2 = b1;
            str2 = str1;
            if (paramString.charAt(1) == 'e')
              return 5; 
          } 
        } 
      } 
    } else if (i == 6) {
      str2 = "delete";
      b2 = 2;
    } else {
      b2 = b1;
      str2 = str1;
      if (i == 11) {
        str2 = "constructor";
        b2 = 1;
      } 
    } 
    b1 = b2;
    if (str2 != null) {
      b1 = b2;
      if (str2 != paramString) {
        b1 = b2;
        if (!str2.equals(paramString))
          b1 = 0; 
      } 
    } 
    return b1;
  }
  
  public String getClassName() {
    return "WeakMap";
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt == 6) {
      initPrototypeValue(6, SymbolKey.TO_STRING_TAG, getClassName(), 3);
      return;
    } 
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt != 4) {
            if (paramInt == 5) {
              bool = true;
              str = "set";
            } else {
              throw new IllegalArgumentException(String.valueOf(paramInt));
            } 
          } else {
            bool = true;
            str = "has";
          } 
        } else {
          bool = true;
          str = "get";
        } 
      } else {
        bool = true;
        str = "delete";
      } 
    } else {
      bool = false;
      str = "constructor";
    } 
    initPrototypeMethod(MAP_TAG, paramInt, str, (String)null, bool);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeWeakMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */