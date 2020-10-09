package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.WeakHashMap;

public class NativeWeakSet extends IdScriptableObject {
  private static final int Id_add = 2;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_delete = 3;
  
  private static final int Id_has = 4;
  
  private static final Object MAP_TAG = "WeakSet";
  
  private static final int MAX_PROTOTYPE_ID = 5;
  
  private static final int SymbolId_toStringTag = 5;
  
  private boolean instanceOfWeakSet = false;
  
  private transient WeakHashMap<Scriptable, Boolean> map = new WeakHashMap<>();
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeWeakSet()).exportAsJSClass(5, paramScriptable, paramBoolean);
  }
  
  private Object js_add(Object paramObject) {
    if (ScriptRuntime.isObject(paramObject)) {
      this.map.put((Scriptable)paramObject, Boolean.TRUE);
      return this;
    } 
    throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(paramObject));
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
  
  private Object js_has(Object paramObject) {
    return !ScriptRuntime.isObject(paramObject) ? Boolean.valueOf(false) : Boolean.valueOf(this.map.containsKey(paramObject));
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    this.map = new WeakHashMap<>();
  }
  
  private NativeWeakSet realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable != null)
      try {
        paramScriptable = paramScriptable;
        if (((NativeWeakSet)paramScriptable).instanceOfWeakSet)
          return (NativeWeakSet)paramScriptable; 
        throw incompatibleCallError(paramIdFunctionObject);
      } catch (ClassCastException classCastException) {
        throw incompatibleCallError(paramIdFunctionObject);
      }  
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    NativeWeakSet nativeWeakSet;
    if (!paramIdFunctionObject.hasTag(MAP_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != 1) {
      Object object;
      if (i != 2) {
        if (i != 3) {
          if (i == 4) {
            NativeWeakSet nativeWeakSet2 = realThis(paramScriptable2, paramIdFunctionObject);
            if (paramArrayOfObject.length > 0) {
              object = paramArrayOfObject[0];
            } else {
              object = Undefined.instance;
            } 
            return nativeWeakSet2.js_has(object);
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("WeakMap.prototype has no method: ");
          stringBuilder.append(object.getFunctionName());
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
        NativeWeakSet nativeWeakSet1 = realThis(paramScriptable2, (IdFunctionObject)object);
        if (paramArrayOfObject.length > 0) {
          object = paramArrayOfObject[0];
        } else {
          object = Undefined.instance;
        } 
        return nativeWeakSet1.js_delete(object);
      } 
      nativeWeakSet = realThis(paramScriptable2, (IdFunctionObject)object);
      if (paramArrayOfObject.length > 0) {
        object = paramArrayOfObject[0];
      } else {
        object = Undefined.instance;
      } 
      return nativeWeakSet.js_add(object);
    } 
    if (paramScriptable2 == null) {
      NativeWeakSet nativeWeakSet1 = new NativeWeakSet();
      nativeWeakSet1.instanceOfWeakSet = true;
      if (paramArrayOfObject.length > 0)
        NativeSet.loadFromIterable((Context)nativeWeakSet, paramScriptable1, nativeWeakSet1, paramArrayOfObject[0]); 
      return nativeWeakSet1;
    } 
    throw ScriptRuntime.typeError1("msg.no.new", "WeakSet");
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.TO_STRING_TAG.equals(paramSymbol) ? 5 : 0;
  }
  
  protected int findPrototypeId(String paramString) {
    byte b2;
    String str2;
    byte b1 = 0;
    String str1 = null;
    int i = paramString.length();
    if (i == 3) {
      i = paramString.charAt(0);
      if (i == 97) {
        b2 = b1;
        str2 = str1;
        if (paramString.charAt(2) == 'd') {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(1) == 'd')
            return 2; 
        } 
      } else {
        b2 = b1;
        str2 = str1;
        if (i == 104) {
          b2 = b1;
          str2 = str1;
          if (paramString.charAt(2) == 's') {
            b2 = b1;
            str2 = str1;
            if (paramString.charAt(1) == 'a')
              return 4; 
          } 
        } 
      } 
    } else if (i == 6) {
      str2 = "delete";
      b2 = 3;
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
    return "WeakSet";
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt == 5) {
      initPrototypeValue(5, SymbolKey.TO_STRING_TAG, getClassName(), 3);
      return;
    } 
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt == 4) {
            bool = true;
            str = "has";
          } else {
            throw new IllegalArgumentException(String.valueOf(paramInt));
          } 
        } else {
          bool = true;
          str = "delete";
        } 
      } else {
        bool = true;
        str = "add";
      } 
    } else {
      bool = false;
      str = "constructor";
    } 
    initPrototypeMethod(MAP_TAG, paramInt, str, (String)null, bool);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeWeakSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */