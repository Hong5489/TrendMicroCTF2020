package com.trendmicro.hippo;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class NativeObject extends IdScriptableObject implements Map {
  private static final int ConstructorId_assign = -15;
  
  private static final int ConstructorId_create = -9;
  
  private static final int ConstructorId_defineProperties = -8;
  
  private static final int ConstructorId_defineProperty = -5;
  
  private static final int ConstructorId_freeze = -13;
  
  private static final int ConstructorId_getOwnPropertyDescriptor = -4;
  
  private static final int ConstructorId_getOwnPropertyNames = -3;
  
  private static final int ConstructorId_getOwnPropertySymbols = -14;
  
  private static final int ConstructorId_getPrototypeOf = -1;
  
  private static final int ConstructorId_is = -16;
  
  private static final int ConstructorId_isExtensible = -6;
  
  private static final int ConstructorId_isFrozen = -11;
  
  private static final int ConstructorId_isSealed = -10;
  
  private static final int ConstructorId_keys = -2;
  
  private static final int ConstructorId_preventExtensions = -7;
  
  private static final int ConstructorId_seal = -12;
  
  private static final int Id___defineGetter__ = 9;
  
  private static final int Id___defineSetter__ = 10;
  
  private static final int Id___lookupGetter__ = 11;
  
  private static final int Id___lookupSetter__ = 12;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_hasOwnProperty = 5;
  
  private static final int Id_isPrototypeOf = 7;
  
  private static final int Id_propertyIsEnumerable = 6;
  
  private static final int Id_toLocaleString = 3;
  
  private static final int Id_toSource = 8;
  
  private static final int Id_toString = 2;
  
  private static final int Id_valueOf = 4;
  
  private static final int MAX_PROTOTYPE_ID = 12;
  
  private static final Object OBJECT_TAG = "Object";
  
  private static final long serialVersionUID = -6345305608474346996L;
  
  private static Scriptable getCompatibleObject(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    return (paramContext.getLanguageVersion() >= 200) ? ensureScriptable(ScriptRuntime.toObject(paramContext, paramScriptable, paramObject)) : ensureScriptable(paramObject);
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeObject()).exportAsJSClass(12, paramScriptable, paramBoolean);
  }
  
  public void clear() {
    throw new UnsupportedOperationException();
  }
  
  public boolean containsKey(Object paramObject) {
    return (paramObject instanceof String) ? has((String)paramObject, this) : ((paramObject instanceof Number) ? has(((Number)paramObject).intValue(), this) : false);
  }
  
  public boolean containsValue(Object paramObject) {
    for (Object object : values()) {
      if (paramObject == object || (paramObject != null && paramObject.equals(object)))
        return true; 
    } 
    return false;
  }
  
  public Set<Map.Entry<Object, Object>> entrySet() {
    return new EntrySet();
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object1;
    String str2;
    ScriptableObject scriptableObject;
    String str1;
    Object object3;
    Object[] arrayOfObject;
    byte b;
    if (!paramIdFunctionObject.hasTag(OBJECT_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool = false;
    null = 0;
    null = 0;
    boolean bool3 = false;
    boolean bool4 = true;
    switch (i) {
      default:
        throw new IllegalArgumentException(String.valueOf(i));
      case 11:
      case 12:
        if (paramArrayOfObject.length < 1 || !(paramScriptable2 instanceof ScriptableObject))
          return Undefined.instance; 
        null = paramScriptable2;
        str2 = ScriptRuntime.toStringIdOrIndex(paramContext, paramArrayOfObject[0]);
        if (str2 != null) {
          null = 0;
        } else {
          null = ScriptRuntime.lastIndexResult(paramContext);
        } 
        if (i == 12)
          bool3 = true; 
        while (true) {
          object2 = null.getGetterOrSetter(str2, null, bool3);
          if (object2 != null)
            break; 
          object1 = null.getPrototype();
          if (object1 != null && object1 instanceof ScriptableObject) {
            object1 = object1;
            continue;
          } 
          break;
        } 
        return (object2 != null) ? object2 : Undefined.instance;
      case 9:
      case 10:
        if (paramArrayOfObject.length < 2 || !(paramArrayOfObject[1] instanceof Callable)) {
          if (paramArrayOfObject.length >= 2) {
            object1 = paramArrayOfObject[1];
          } else {
            object1 = Undefined.instance;
          } 
          throw ScriptRuntime.notFunctionError(object1);
        } 
        if (!(paramScriptable2 instanceof ScriptableObject)) {
          if (paramScriptable2 == null) {
            object1 = "null";
          } else {
            object1 = paramScriptable2.getClass().getName();
          } 
          throw Context.reportRuntimeError2("msg.extend.scriptable", object1, String.valueOf(paramArrayOfObject[0]));
        } 
        scriptableObject = (ScriptableObject)paramScriptable2;
        null = ScriptRuntime.toStringIdOrIndex((Context)object2, paramArrayOfObject[0]);
        if (null != null) {
          null = 0;
        } else {
          null = ScriptRuntime.lastIndexResult((Context)object2);
        } 
        object2 = paramArrayOfObject[1];
        if (i == 10) {
          bool3 = bool4;
        } else {
          bool3 = false;
        } 
        scriptableObject.setGetterOrSetter((String)null, null, (Callable)object2, bool3);
        if (scriptableObject instanceof NativeArray)
          ((NativeArray)scriptableObject).setDenseOnly(false); 
        return Undefined.instance;
      case 8:
        return ScriptRuntime.defaultObjectToSource((Context)object2, scriptableObject, paramScriptable2, paramArrayOfObject);
      case 7:
        bool1 = false;
        bool3 = bool1;
        if (paramArrayOfObject.length != 0) {
          bool3 = bool1;
          if (paramArrayOfObject[0] instanceof Scriptable) {
            object1 = paramArrayOfObject[0];
            while (true) {
              object2 = object1.getPrototype();
              if (object2 == paramScriptable2) {
                bool3 = true;
                break;
              } 
              Object object = object2;
              if (object2 == null) {
                bool3 = bool1;
                break;
              } 
            } 
          } 
        } 
        return ScriptRuntime.wrapBoolean(bool3);
      case 6:
        if (paramArrayOfObject.length < 1) {
          object1 = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (null instanceof Symbol) {
          bool2 = ((SymbolScriptable)paramScriptable2).has((Symbol)null, paramScriptable2);
          bool3 = bool2;
          if (bool2) {
            bool3 = bool2;
            if (paramScriptable2 instanceof ScriptableObject) {
              bool3 = bool1;
              if ((((ScriptableObject)paramScriptable2).getAttributes((Symbol)null) & 0x2) == 0)
                bool3 = true; 
            } 
          } 
        } else {
          str1 = ScriptRuntime.toStringIdOrIndex((Context)object2, null);
          if (str1 == null) {
            null = str1;
            try {
              null = ScriptRuntime.lastIndexResult((Context)object2);
              null = str1;
              bool1 = paramScriptable2.has(null, paramScriptable2);
              null = str1;
              object2 = Integer.toString(null);
              bool3 = bool1;
              if (bool1) {
                bool3 = bool1;
                null = object2;
                if (paramScriptable2 instanceof ScriptableObject) {
                  bool3 = bool2;
                  null = object2;
                  if ((((ScriptableObject)paramScriptable2).getAttributes(null) & 0x2) == 0)
                    bool3 = true; 
                } 
              } 
            } catch (EvaluatorException object2) {
              if (object2.getMessage().startsWith(ScriptRuntime.getMessage1("msg.prop.not.found", null))) {
                bool3 = false;
                return ScriptRuntime.wrapBoolean(bool3);
              } 
            } 
          } else {
            null = str1;
            bool1 = paramScriptable2.has(str1, paramScriptable2);
            bool3 = bool1;
            if (bool1) {
              bool3 = bool1;
              null = str1;
              if (paramScriptable2 instanceof ScriptableObject) {
                null = str1;
                null = ((ScriptableObject)paramScriptable2).getAttributes(str1);
                bool3 = bool;
                if ((null & 0x2) == 0)
                  bool3 = true; 
              } 
            } 
          } 
        } 
        return ScriptRuntime.wrapBoolean(bool3);
      case 5:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (null instanceof Symbol) {
          bool3 = ensureSymbolScriptable(paramScriptable2).has((Symbol)null, paramScriptable2);
        } else {
          null = ScriptRuntime.toStringIdOrIndex((Context)object2, null);
          if (null == null) {
            bool3 = paramScriptable2.has(ScriptRuntime.lastIndexResult((Context)object2), paramScriptable2);
          } else {
            bool3 = paramScriptable2.has((String)null, paramScriptable2);
          } 
        } 
        return ScriptRuntime.wrapBoolean(bool3);
      case 4:
        return paramScriptable2;
      case 3:
        null = ScriptableObject.getProperty(paramScriptable2, "toString");
        if (null instanceof Callable)
          return ((Callable)null).call((Context)object2, (Scriptable)str1, paramScriptable2, ScriptRuntime.emptyArgs); 
        throw ScriptRuntime.notFunctionError(null);
      case 2:
        if (object2.hasFeature(4)) {
          object2 = ScriptRuntime.defaultObjectToSource((Context)object2, (Scriptable)str1, paramScriptable2, paramArrayOfObject);
          null = object2.length();
          null = object2;
          if (null != 0) {
            null = object2;
            if (object2.charAt(0) == '(') {
              null = object2;
              if (object2.charAt(null - 1) == ')')
                null = object2.substring(1, null - 1); 
            } 
          } 
          return null;
        } 
        return ScriptRuntime.defaultObjectToString(paramScriptable2);
      case 1:
        return (paramScriptable2 != null) ? null.construct((Context)object2, (Scriptable)str1, paramArrayOfObject) : ((paramArrayOfObject.length == 0 || paramArrayOfObject[0] == null || paramArrayOfObject[0] == Undefined.instance) ? new NativeObject() : ScriptRuntime.toObject((Context)object2, (Scriptable)str1, paramArrayOfObject[0]));
      case -1:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        return getCompatibleObject((Context)object2, (Scriptable)str1, null).getPrototype();
      case -2:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        null = getCompatibleObject((Context)object2, (Scriptable)str1, null).getIds();
        for (null = 0; null < null.length; null++)
          null[null] = ScriptRuntime.toString(null[null]); 
        return object2.newArray((Scriptable)str1, (Object[])null);
      case -3:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        null = ensureScriptableObject(getCompatibleObject((Context)object2, (Scriptable)str1, null)).getIds(true, false);
        for (null = 0; null < null.length; null++)
          null[null] = ScriptRuntime.toString(null[null]); 
        return object2.newArray((Scriptable)str1, (Object[])null);
      case -4:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        object3 = ensureScriptableObject(getCompatibleObject((Context)object2, (Scriptable)str1, null));
        if (paramArrayOfObject.length < 2) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[1];
        } 
        null = object3.getOwnPropertyDescriptor((Context)object2, null);
        if (null == null)
          null = Undefined.instance; 
        return null;
      case -5:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        paramScriptable2 = ensureScriptableObject(null);
        if (paramArrayOfObject.length < 2) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[1];
        } 
        if (paramArrayOfObject.length < 3) {
          Object object = Undefined.instance;
        } else {
          object3 = paramArrayOfObject[2];
        } 
        paramScriptable2.defineOwnProperty((Context)object2, null, ensureScriptableObject(object3));
        return paramScriptable2;
      case -6:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        return (object2.getLanguageVersion() >= 200 && !(null instanceof ScriptableObject)) ? Boolean.FALSE : Boolean.valueOf(ensureScriptableObject(null).isExtensible());
      case -7:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (object2.getLanguageVersion() >= 200 && !(null instanceof ScriptableObject))
          return null; 
        null = ensureScriptableObject(null);
        null.preventExtensions();
        return null;
      case -8:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        object3 = ensureScriptableObject(null);
        if (paramArrayOfObject.length < 2) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[1];
        } 
        object3.defineOwnProperties((Context)object2, ensureScriptableObject(Context.toObject(null, getParentScope())));
        return object3;
      case -9:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (null == null) {
          null = null;
        } else {
          null = ensureScriptable(null);
        } 
        object3 = new NativeObject();
        object3.setParentScope(getParentScope());
        object3.setPrototype((Scriptable)null);
        if (paramArrayOfObject.length > 1 && paramArrayOfObject[1] != Undefined.instance)
          object3.defineOwnProperties((Context)object2, ensureScriptableObject(Context.toObject(paramArrayOfObject[1], getParentScope()))); 
        return object3;
      case -10:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (object2.getLanguageVersion() >= 200 && !(null instanceof ScriptableObject))
          return Boolean.TRUE; 
        null = ensureScriptableObject(null);
        if (null.isExtensible())
          return Boolean.FALSE; 
        object3 = null.getAllIds();
        null = object3.length;
        while (null < null) {
          Object object = null.getOwnPropertyDescriptor((Context)object2, object3[null]).get("configurable");
          if (Boolean.TRUE.equals(object))
            return Boolean.FALSE; 
          null++;
        } 
        return Boolean.TRUE;
      case -11:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (object2.getLanguageVersion() >= 200 && !(null instanceof ScriptableObject))
          return Boolean.TRUE; 
        null = ensureScriptableObject(null);
        if (null.isExtensible())
          return Boolean.FALSE; 
        object3 = null.getAllIds();
        i = object3.length;
        for (null = null; null < i; null++) {
          paramScriptable2 = null.getOwnPropertyDescriptor((Context)object2, object3[null]);
          if (Boolean.TRUE.equals(paramScriptable2.get("configurable")))
            return Boolean.FALSE; 
          if (isDataDescriptor((ScriptableObject)paramScriptable2) && Boolean.TRUE.equals(paramScriptable2.get("writable")))
            return Boolean.FALSE; 
        } 
        return Boolean.TRUE;
      case -12:
        if (paramArrayOfObject.length < 1) {
          null = Undefined.instance;
        } else {
          null = paramArrayOfObject[0];
        } 
        if (object2.getLanguageVersion() >= 200 && !(null instanceof ScriptableObject))
          return null; 
        paramScriptable2 = ensureScriptableObject(null);
        for (Object object4 : paramScriptable2.getAllIds()) {
          object3 = paramScriptable2.getOwnPropertyDescriptor((Context)object2, object4);
          if (Boolean.TRUE.equals(object3.get("configurable"))) {
            object3.put("configurable", (Scriptable)object3, Boolean.FALSE);
            paramScriptable2.defineOwnProperty((Context)object2, object4, (ScriptableObject)object3, false);
          } 
        } 
        paramScriptable2.preventExtensions();
        return paramScriptable2;
      case -13:
        if (object4.length < 1) {
          object1 = Undefined.instance;
        } else {
          null = object4[0];
        } 
        if (object2.getLanguageVersion() >= 200 && !(null instanceof ScriptableObject))
          return null; 
        object3 = ensureScriptableObject(null);
        for (Object object4 : object3.getAllIds()) {
          paramScriptable2 = object3.getOwnPropertyDescriptor((Context)object2, object4);
          if (isDataDescriptor((ScriptableObject)paramScriptable2) && Boolean.TRUE.equals(paramScriptable2.get("writable")))
            paramScriptable2.put("writable", paramScriptable2, Boolean.FALSE); 
          if (Boolean.TRUE.equals(paramScriptable2.get("configurable")))
            paramScriptable2.put("configurable", paramScriptable2, Boolean.FALSE); 
          object3.defineOwnProperty((Context)object2, object4, (ScriptableObject)paramScriptable2, false);
        } 
        object3.preventExtensions();
        return object3;
      case -14:
        if (object4.length < 1) {
          object1 = Undefined.instance;
        } else {
          object1 = object4[0];
        } 
        arrayOfObject = ensureScriptableObject(getCompatibleObject((Context)object2, (Scriptable)object3, object1)).getIds(true, true);
        object1 = new ArrayList();
        for (b = 0; b < arrayOfObject.length; b++) {
          if (arrayOfObject[b] instanceof Symbol)
            object1.add(arrayOfObject[b]); 
        } 
        return object2.newArray((Scriptable)object3, object1.toArray());
      case -15:
        if (object4.length >= 1) {
          object3 = ScriptRuntime.toObject((Context)object2, (Scriptable)arrayOfObject, object4[0]);
          for (b = 1; b < object4.length; b++) {
            if (object4[b] != null && !Undefined.instance.equals(object4[b])) {
              Scriptable scriptable = ScriptRuntime.toObject((Context)object2, (Scriptable)arrayOfObject, object4[b]);
              for (Object object : scriptable.getIds()) {
                if (object instanceof String) {
                  Object object5 = scriptable.get((String)object, (Scriptable)object3);
                  if (object5 != Scriptable.NOT_FOUND && object5 != Undefined.instance)
                    object3.put((String)object, (Scriptable)object3, object5); 
                } else if (object instanceof Number) {
                  int j = ScriptRuntime.toInt32(object);
                  Object object5 = scriptable.get(j, (Scriptable)object3);
                  if (object5 != Scriptable.NOT_FOUND && object5 != Undefined.instance)
                    object3.put(j, (Scriptable)object3, object5); 
                } 
              } 
            } 
          } 
          return object3;
        } 
        throw ScriptRuntime.typeError1("msg.incompat.call", "assign");
      case -16:
        break;
    } 
    if (object4.length < 1) {
      object1 = Undefined.instance;
    } else {
      object1 = object4[0];
    } 
    if (object4.length < 2) {
      object2 = Undefined.instance;
    } else {
      object2 = object4[1];
    } 
    return ScriptRuntime.wrapBoolean(ScriptRuntime.same(object1, object2));
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -1, "getPrototypeOf", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -2, "keys", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -3, "getOwnPropertyNames", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -14, "getOwnPropertySymbols", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -4, "getOwnPropertyDescriptor", 2);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -5, "defineProperty", 3);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -6, "isExtensible", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -7, "preventExtensions", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -8, "defineProperties", 2);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -9, "create", 2);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -10, "isSealed", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -11, "isFrozen", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -12, "seal", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -13, "freeze", 1);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -15, "assign", 2);
    addIdFunctionProperty(paramIdFunctionObject, OBJECT_TAG, -16, "is", 2);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findPrototypeId(String paramString) {
    String str2;
    int i = 0;
    String str1 = null;
    int j = paramString.length();
    if (j != 7) {
      if (j != 8) {
        if (j != 11) {
          if (j != 16) {
            if (j != 20) {
              if (j != 13) {
                if (j != 14) {
                  j = i;
                  str2 = str1;
                } else {
                  char c = paramString.charAt(0);
                  if (c == 'h') {
                    str2 = "hasOwnProperty";
                    j = 5;
                  } else {
                    j = i;
                    str2 = str1;
                    if (c == 't') {
                      str2 = "toLocaleString";
                      j = 3;
                    } 
                  } 
                } 
              } else {
                str2 = "isPrototypeOf";
                j = 7;
              } 
            } else {
              str2 = "propertyIsEnumerable";
              j = 6;
            } 
          } else {
            char c = paramString.charAt(2);
            if (c == 'd') {
              c = paramString.charAt(8);
              if (c == 'G') {
                str2 = "__defineGetter__";
                j = 9;
              } else {
                j = i;
                str2 = str1;
                if (c == 'S') {
                  str2 = "__defineSetter__";
                  j = 10;
                } 
              } 
            } else {
              j = i;
              str2 = str1;
              if (c == 'l') {
                c = paramString.charAt(8);
                if (c == 'G') {
                  str2 = "__lookupGetter__";
                  j = 11;
                } else {
                  j = i;
                  str2 = str1;
                  if (c == 'S') {
                    str2 = "__lookupSetter__";
                    j = 12;
                  } 
                } 
              } 
            } 
          } 
        } else {
          str2 = "constructor";
          j = 1;
        } 
      } else {
        char c = paramString.charAt(3);
        if (c == 'o') {
          str2 = "toSource";
          j = 8;
        } else {
          j = i;
          str2 = str1;
          if (c == 't') {
            str2 = "toString";
            j = 2;
          } 
        } 
      } 
    } else {
      str2 = "valueOf";
      j = 4;
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
    return "Object";
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 12:
        b = 1;
        str = "__lookupSetter__";
        break;
      case 11:
        b = 1;
        str = "__lookupGetter__";
        break;
      case 10:
        b = 2;
        str = "__defineSetter__";
        break;
      case 9:
        b = 2;
        str = "__defineGetter__";
        break;
      case 8:
        b = 0;
        str = "toSource";
        break;
      case 7:
        b = 1;
        str = "isPrototypeOf";
        break;
      case 6:
        b = 1;
        str = "propertyIsEnumerable";
        break;
      case 5:
        b = 1;
        str = "hasOwnProperty";
        break;
      case 4:
        b = 0;
        str = "valueOf";
        break;
      case 3:
        b = 0;
        str = "toLocaleString";
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
    initPrototypeMethod(OBJECT_TAG, paramInt, str, b);
  }
  
  public Set<Object> keySet() {
    return new KeySet();
  }
  
  public Object put(Object paramObject1, Object paramObject2) {
    throw new UnsupportedOperationException();
  }
  
  public void putAll(Map paramMap) {
    throw new UnsupportedOperationException();
  }
  
  public Object remove(Object paramObject) {
    Object object = get(paramObject);
    if (paramObject instanceof String) {
      delete((String)paramObject);
    } else if (paramObject instanceof Number) {
      delete(((Number)paramObject).intValue());
    } 
    return object;
  }
  
  public String toString() {
    return ScriptRuntime.defaultObjectToString(this);
  }
  
  public Collection<Object> values() {
    return new ValueCollection();
  }
  
  class EntrySet extends AbstractSet<Map.Entry<Object, Object>> {
    public Iterator<Map.Entry<Object, Object>> iterator() {
      return new Iterator<Map.Entry<Object, Object>>() {
          Object[] ids = NativeObject.this.getIds();
          
          int index = 0;
          
          Object key = null;
          
          public boolean hasNext() {
            boolean bool;
            if (this.index < this.ids.length) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
          
          public Map.Entry<Object, Object> next() {
            Object[] arrayOfObject = this.ids;
            int i = this.index;
            this.index = i + 1;
            final Object ekey = arrayOfObject[i];
            this.key = object;
            return new Map.Entry<Object, Object>() {
                public boolean equals(Object param3Object) {
                  boolean bool = param3Object instanceof Map.Entry;
                  boolean bool1 = false;
                  if (!bool)
                    return false; 
                  param3Object = param3Object;
                  Object object = ekey;
                  if ((object == null) ? (param3Object.getKey() == null) : object.equals(param3Object.getKey())) {
                    object = value;
                    if ((object == null) ? (param3Object.getValue() == null) : object.equals(param3Object.getValue()))
                      bool1 = true; 
                  } 
                  return bool1;
                }
                
                public Object getKey() {
                  return ekey;
                }
                
                public Object getValue() {
                  return value;
                }
                
                public int hashCode() {
                  int j;
                  Object object = ekey;
                  int i = 0;
                  if (object == null) {
                    j = 0;
                  } else {
                    j = object.hashCode();
                  } 
                  object = value;
                  if (object != null)
                    i = object.hashCode(); 
                  return j ^ i;
                }
                
                public Object setValue(Object param3Object) {
                  throw new UnsupportedOperationException();
                }
                
                public String toString() {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append(ekey);
                  stringBuilder.append("=");
                  stringBuilder.append(value);
                  return stringBuilder.toString();
                }
              };
          }
          
          public void remove() {
            if (this.key != null) {
              NativeObject.this.remove(this.key);
              this.key = null;
              return;
            } 
            throw new IllegalStateException();
          }
        };
    }
    
    public int size() {
      return NativeObject.this.size();
    }
  }
  
  class null implements Iterator<Map.Entry<Object, Object>> {
    Object[] ids = NativeObject.this.getIds();
    
    int index = 0;
    
    Object key = null;
    
    public boolean hasNext() {
      boolean bool;
      if (this.index < this.ids.length) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Map.Entry<Object, Object> next() {
      Object[] arrayOfObject = this.ids;
      int i = this.index;
      this.index = i + 1;
      final Object ekey = arrayOfObject[i];
      this.key = object;
      return new Map.Entry<Object, Object>() {
          public boolean equals(Object param3Object) {
            boolean bool = param3Object instanceof Map.Entry;
            boolean bool1 = false;
            if (!bool)
              return false; 
            param3Object = param3Object;
            Object object = ekey;
            if ((object == null) ? (param3Object.getKey() == null) : object.equals(param3Object.getKey())) {
              object = value;
              if ((object == null) ? (param3Object.getValue() == null) : object.equals(param3Object.getValue()))
                bool1 = true; 
            } 
            return bool1;
          }
          
          public Object getKey() {
            return ekey;
          }
          
          public Object getValue() {
            return value;
          }
          
          public int hashCode() {
            int j;
            Object object = ekey;
            int i = 0;
            if (object == null) {
              j = 0;
            } else {
              j = object.hashCode();
            } 
            object = value;
            if (object != null)
              i = object.hashCode(); 
            return j ^ i;
          }
          
          public Object setValue(Object param3Object) {
            throw new UnsupportedOperationException();
          }
          
          public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ekey);
            stringBuilder.append("=");
            stringBuilder.append(value);
            return stringBuilder.toString();
          }
        };
    }
    
    public void remove() {
      if (this.key != null) {
        NativeObject.this.remove(this.key);
        this.key = null;
        return;
      } 
      throw new IllegalStateException();
    }
  }
  
  class null implements Map.Entry<Object, Object> {
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Map.Entry;
      boolean bool1 = false;
      if (!bool)
        return false; 
      param1Object = param1Object;
      Object object = ekey;
      if ((object == null) ? (param1Object.getKey() == null) : object.equals(param1Object.getKey())) {
        object = value;
        if ((object == null) ? (param1Object.getValue() == null) : object.equals(param1Object.getValue()))
          bool1 = true; 
      } 
      return bool1;
    }
    
    public Object getKey() {
      return ekey;
    }
    
    public Object getValue() {
      return value;
    }
    
    public int hashCode() {
      int j;
      Object object = ekey;
      int i = 0;
      if (object == null) {
        j = 0;
      } else {
        j = object.hashCode();
      } 
      object = value;
      if (object != null)
        i = object.hashCode(); 
      return j ^ i;
    }
    
    public Object setValue(Object param1Object) {
      throw new UnsupportedOperationException();
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(ekey);
      stringBuilder.append("=");
      stringBuilder.append(value);
      return stringBuilder.toString();
    }
  }
  
  class KeySet extends AbstractSet<Object> {
    public boolean contains(Object param1Object) {
      return NativeObject.this.containsKey(param1Object);
    }
    
    public Iterator<Object> iterator() {
      return new Iterator() {
          Object[] ids = NativeObject.this.getIds();
          
          int index = 0;
          
          Object key;
          
          public boolean hasNext() {
            boolean bool;
            if (this.index < this.ids.length) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
          
          public Object next() {
            try {
              Object[] arrayOfObject = this.ids;
              int i = this.index;
              this.index = i + 1;
              Object object = arrayOfObject[i];
              this.key = object;
              return object;
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
              this.key = null;
              throw new NoSuchElementException();
            } 
          }
          
          public void remove() {
            if (this.key != null) {
              NativeObject.this.remove(this.key);
              this.key = null;
              return;
            } 
            throw new IllegalStateException();
          }
        };
    }
    
    public int size() {
      return NativeObject.this.size();
    }
  }
  
  class null implements Iterator<Object> {
    Object[] ids = NativeObject.this.getIds();
    
    int index = 0;
    
    Object key;
    
    public boolean hasNext() {
      boolean bool;
      if (this.index < this.ids.length) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Object next() {
      try {
        Object[] arrayOfObject = this.ids;
        int i = this.index;
        this.index = i + 1;
        Object object = arrayOfObject[i];
        this.key = object;
        return object;
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        this.key = null;
        throw new NoSuchElementException();
      } 
    }
    
    public void remove() {
      if (this.key != null) {
        NativeObject.this.remove(this.key);
        this.key = null;
        return;
      } 
      throw new IllegalStateException();
    }
  }
  
  class ValueCollection extends AbstractCollection<Object> {
    public Iterator<Object> iterator() {
      return new Iterator() {
          Object[] ids = NativeObject.this.getIds();
          
          int index = 0;
          
          Object key;
          
          public boolean hasNext() {
            boolean bool;
            if (this.index < this.ids.length) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
          
          public Object next() {
            NativeObject nativeObject = NativeObject.this;
            Object[] arrayOfObject = this.ids;
            int i = this.index;
            this.index = i + 1;
            Object object = arrayOfObject[i];
            this.key = object;
            return nativeObject.get(object);
          }
          
          public void remove() {
            if (this.key != null) {
              NativeObject.this.remove(this.key);
              this.key = null;
              return;
            } 
            throw new IllegalStateException();
          }
        };
    }
    
    public int size() {
      return NativeObject.this.size();
    }
  }
  
  class null implements Iterator<Object> {
    Object[] ids = NativeObject.this.getIds();
    
    int index = 0;
    
    Object key;
    
    public boolean hasNext() {
      boolean bool;
      if (this.index < this.ids.length) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Object next() {
      NativeObject nativeObject = NativeObject.this;
      Object[] arrayOfObject = this.ids;
      int i = this.index;
      this.index = i + 1;
      Object object = arrayOfObject[i];
      this.key = object;
      return nativeObject.get(object);
    }
    
    public void remove() {
      if (this.key != null) {
        NativeObject.this.remove(this.key);
        this.key = null;
        return;
      } 
      throw new IllegalStateException();
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */