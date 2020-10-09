package com.trendmicro.hippo;

import java.util.HashMap;
import java.util.Map;

public class NativeSymbol extends IdScriptableObject implements Symbol {
  public static final String CLASS_NAME = "Symbol";
  
  private static final Object CONSTRUCTOR_SLOT;
  
  private static final int ConstructorId_for = -1;
  
  private static final int ConstructorId_keyFor = -2;
  
  private static final Object GLOBAL_TABLE_KEY = new Object();
  
  private static final int Id_constructor = 1;
  
  private static final int Id_toString = 2;
  
  private static final int Id_valueOf = 4;
  
  private static final int MAX_PROTOTYPE_ID = 5;
  
  private static final int SymbolId_toPrimitive = 5;
  
  private static final int SymbolId_toStringTag = 3;
  
  public static final String TYPE_NAME = "symbol";
  
  private static final long serialVersionUID = -589539749749830003L;
  
  private final SymbolKey key;
  
  private final NativeSymbol symbolData;
  
  static {
    CONSTRUCTOR_SLOT = new Object();
  }
  
  public NativeSymbol(NativeSymbol paramNativeSymbol) {
    this.key = paramNativeSymbol.key;
    this.symbolData = paramNativeSymbol.symbolData;
  }
  
  private NativeSymbol(SymbolKey paramSymbolKey) {
    this.key = paramSymbolKey;
    this.symbolData = this;
  }
  
  private NativeSymbol(String paramString) {
    this.key = new SymbolKey(paramString);
    this.symbolData = null;
  }
  
  public static NativeSymbol construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    paramContext.putThreadLocal(CONSTRUCTOR_SLOT, Boolean.TRUE);
    try {
      paramScriptable = paramContext.newObject(paramScriptable, "Symbol", paramArrayOfObject);
      return (NativeSymbol)paramScriptable;
    } finally {
      paramContext.removeThreadLocal(CONSTRUCTOR_SLOT);
    } 
  }
  
  private static void createStandardSymbol(Context paramContext, Scriptable paramScriptable, ScriptableObject paramScriptableObject, String paramString, SymbolKey paramSymbolKey) {
    paramScriptableObject.defineProperty(paramString, paramContext.newObject(paramScriptable, "Symbol", new Object[] { paramString, paramSymbolKey }), 7);
  }
  
  private Map<String, NativeSymbol> getGlobalMap() {
    ScriptableObject scriptableObject = (ScriptableObject)getTopLevelScope(this);
    Map<Object, Object> map1 = (Map)scriptableObject.getAssociatedValue(GLOBAL_TABLE_KEY);
    Map<Object, Object> map2 = map1;
    if (map1 == null) {
      map2 = new HashMap<>();
      scriptableObject.associateValue(GLOBAL_TABLE_KEY, map2);
    } 
    return (Map)map2;
  }
  
  private static NativeSymbol getSelf(Object paramObject) {
    try {
      return (NativeSymbol)paramObject;
    } catch (ClassCastException classCastException) {
      throw ScriptRuntime.typeError1("msg.invalid.type", paramObject.getClass().getName());
    } 
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    IdFunctionObject idFunctionObject = (new NativeSymbol("")).exportAsJSClass(5, paramScriptable, false);
    paramContext.putThreadLocal(CONSTRUCTOR_SLOT, Boolean.TRUE);
    try {
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "iterator", SymbolKey.ITERATOR);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "species", SymbolKey.SPECIES);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "toStringTag", SymbolKey.TO_STRING_TAG);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "hasInstance", SymbolKey.HAS_INSTANCE);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "isConcatSpreadable", SymbolKey.IS_CONCAT_SPREADABLE);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "isRegExp", SymbolKey.IS_REGEXP);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "toPrimitive", SymbolKey.TO_PRIMITIVE);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "match", SymbolKey.MATCH);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "replace", SymbolKey.REPLACE);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "search", SymbolKey.SEARCH);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "split", SymbolKey.SPLIT);
      createStandardSymbol(paramContext, paramScriptable, idFunctionObject, "unscopables", SymbolKey.UNSCOPABLES);
      paramContext.removeThreadLocal(CONSTRUCTOR_SLOT);
      return;
    } finally {
      paramContext.removeThreadLocal(CONSTRUCTOR_SLOT);
    } 
  }
  
  private boolean isStrictMode() {
    boolean bool;
    Context context = Context.getCurrentContext();
    if (context != null && context.isStrictMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static NativeSymbol js_constructor(Object[] paramArrayOfObject) {
    String str;
    if (paramArrayOfObject.length > 0) {
      if (Undefined.instance.equals(paramArrayOfObject[0])) {
        str = "";
      } else {
        str = ScriptRuntime.toString(paramArrayOfObject[0]);
      } 
    } else {
      str = "";
    } 
    return (paramArrayOfObject.length > 1) ? new NativeSymbol((SymbolKey)paramArrayOfObject[1]) : new NativeSymbol(new SymbolKey(str));
  }
  
  private Object js_for(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length > 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    String str = ScriptRuntime.toString(object);
    Map<String, NativeSymbol> map = getGlobalMap();
    NativeSymbol nativeSymbol = map.get(str);
    Object object = nativeSymbol;
    if (nativeSymbol == null) {
      object = construct(paramContext, paramScriptable, new Object[] { str });
      map.put(str, object);
    } 
    return object;
  }
  
  private Object js_keyFor(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    Object object;
    if (paramArrayOfObject.length > 0) {
      object = paramArrayOfObject[0];
    } else {
      object = Undefined.instance;
    } 
    if (object instanceof NativeSymbol) {
      object = object;
      for (Map.Entry<String, NativeSymbol> entry : getGlobalMap().entrySet()) {
        if (((NativeSymbol)entry.getValue()).key == ((NativeSymbol)object).key)
          return entry.getKey(); 
      } 
      return Undefined.instance;
    } 
    throw ScriptRuntime.throwCustomError(paramContext, entry, "TypeError", "Not a Symbol");
  }
  
  private Object js_valueOf() {
    return this.symbolData;
  }
  
  public boolean equals(Object paramObject) {
    return this.key.equals(paramObject);
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag("Symbol"))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != -2) {
      if (i != -1) {
        if (i != 1)
          return (i != 2) ? ((i != 4 && i != 5) ? super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject) : getSelf(paramScriptable2).js_valueOf()) : getSelf(paramScriptable2).toString(); 
        if (paramScriptable2 == null) {
          if (paramContext.getThreadLocal(CONSTRUCTOR_SLOT) != null)
            return js_constructor(paramArrayOfObject); 
          throw ScriptRuntime.typeError0("msg.no.symbol.new");
        } 
        return construct(paramContext, paramScriptable1, paramArrayOfObject);
      } 
      return js_for(paramContext, paramScriptable1, paramArrayOfObject);
    } 
    return js_keyFor(paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    super.fillConstructorProperties(paramIdFunctionObject);
    addIdFunctionProperty(paramIdFunctionObject, "Symbol", -1, "for", 1);
    addIdFunctionProperty(paramIdFunctionObject, "Symbol", -2, "keyFor", 1);
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.TO_STRING_TAG.equals(paramSymbol) ? 3 : (SymbolKey.TO_PRIMITIVE.equals(paramSymbol) ? 5 : 0);
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 7) {
      str = "valueOf";
      b = 4;
    } else if (i == 8) {
      str = "toString";
      b = 2;
    } else if (i == 11) {
      str = "constructor";
      b = 1;
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
    return "Symbol";
  }
  
  SymbolKey getKey() {
    return this.key;
  }
  
  public String getTypeOf() {
    String str;
    if (isSymbol()) {
      str = "symbol";
    } else {
      str = super.getTypeOf();
    } 
    return str;
  }
  
  public int hashCode() {
    return this.key.hashCode();
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt != 4) {
            if (paramInt != 5) {
              super.initPrototypeId(paramInt);
            } else {
              initPrototypeMethod("Symbol", paramInt, SymbolKey.TO_PRIMITIVE, "Symbol.toPrimitive", 1);
            } 
          } else {
            initPrototypeMethod("Symbol", paramInt, "valueOf", 0);
          } 
        } else {
          initPrototypeValue(paramInt, SymbolKey.TO_STRING_TAG, "Symbol", 3);
        } 
      } else {
        initPrototypeMethod("Symbol", paramInt, "toString", 0);
      } 
    } else {
      initPrototypeMethod("Symbol", paramInt, "constructor", 1);
    } 
  }
  
  public boolean isSymbol() {
    boolean bool;
    if (this.symbolData == this) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    if (!isSymbol()) {
      super.put(paramInt, paramScriptable, paramObject);
    } else if (isStrictMode()) {
      throw ScriptRuntime.typeError0("msg.no.assign.symbol.strict");
    } 
  }
  
  public void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject) {
    if (!isSymbol()) {
      super.put(paramSymbol, paramScriptable, paramObject);
    } else if (isStrictMode()) {
      throw ScriptRuntime.typeError0("msg.no.assign.symbol.strict");
    } 
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    if (!isSymbol()) {
      super.put(paramString, paramScriptable, paramObject);
    } else if (isStrictMode()) {
      throw ScriptRuntime.typeError0("msg.no.assign.symbol.strict");
    } 
  }
  
  public String toString() {
    return this.key.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeSymbol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */