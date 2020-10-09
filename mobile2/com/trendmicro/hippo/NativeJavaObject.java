package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class NativeJavaObject implements Scriptable, SymbolScriptable, Wrapper, Serializable {
  private static final Object COERCED_INTERFACE_KEY = "Coerced Interface";
  
  static final byte CONVERSION_NONE = 99;
  
  static final byte CONVERSION_NONTRIVIAL = 0;
  
  static final byte CONVERSION_TRIVIAL = 1;
  
  private static final int JSTYPE_BOOLEAN = 2;
  
  private static final int JSTYPE_JAVA_ARRAY = 7;
  
  private static final int JSTYPE_JAVA_CLASS = 5;
  
  private static final int JSTYPE_JAVA_OBJECT = 6;
  
  private static final int JSTYPE_NULL = 1;
  
  private static final int JSTYPE_NUMBER = 3;
  
  private static final int JSTYPE_OBJECT = 8;
  
  private static final int JSTYPE_STRING = 4;
  
  private static final int JSTYPE_UNDEFINED = 0;
  
  private static Method adapter_readAdapterObject;
  
  private static Method adapter_writeAdapterObject;
  
  private static final long serialVersionUID = -6948590651130498591L;
  
  private transient Map<String, FieldAndMethods> fieldAndMethods;
  
  protected transient boolean isAdapter;
  
  protected transient Object javaObject;
  
  protected transient JavaMembers members;
  
  protected Scriptable parent;
  
  protected Scriptable prototype;
  
  protected transient Class<?> staticType;
  
  static {
    Class[] arrayOfClass = new Class[2];
    Class<?> clazz = Kit.classOrNull("com.trendmicro.hippo.JavaAdapter");
    if (clazz != null)
      try {
        arrayOfClass[0] = ScriptRuntime.ObjectClass;
        arrayOfClass[1] = Kit.classOrNull("java.io.ObjectOutputStream");
        adapter_writeAdapterObject = clazz.getMethod("writeAdapterObject", arrayOfClass);
        arrayOfClass[0] = ScriptRuntime.ScriptableClass;
        arrayOfClass[1] = Kit.classOrNull("java.io.ObjectInputStream");
        adapter_readAdapterObject = clazz.getMethod("readAdapterObject", arrayOfClass);
      } catch (NoSuchMethodException noSuchMethodException) {
        adapter_writeAdapterObject = null;
        adapter_readAdapterObject = null;
      }  
  }
  
  public NativeJavaObject() {}
  
  public NativeJavaObject(Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    this(paramScriptable, paramObject, paramClass, false);
  }
  
  public NativeJavaObject(Scriptable paramScriptable, Object paramObject, Class<?> paramClass, boolean paramBoolean) {
    this.parent = paramScriptable;
    this.javaObject = paramObject;
    this.staticType = paramClass;
    this.isAdapter = paramBoolean;
    initMembers();
  }
  
  public static boolean canConvert(Object paramObject, Class<?> paramClass) {
    boolean bool;
    if (getConversionWeight(paramObject, paramClass) < 99) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static Object coerceToNumber(Class<?> paramClass, Object paramObject) {
    Object object;
    Class<?> clazz = paramObject.getClass();
    if (paramClass == char.class || paramClass == ScriptRuntime.CharacterClass)
      return (clazz == ScriptRuntime.CharacterClass) ? paramObject : Character.valueOf((char)(int)toInteger(paramObject, ScriptRuntime.CharacterClass, 0.0D, 65535.0D)); 
    if (paramClass == ScriptRuntime.ObjectClass || paramClass == ScriptRuntime.DoubleClass || paramClass == double.class) {
      if (clazz == ScriptRuntime.DoubleClass) {
        object = paramObject;
      } else {
        object = Double.valueOf(toDouble(paramObject));
      } 
      return object;
    } 
    if (object == ScriptRuntime.FloatClass || object == float.class) {
      if (clazz == ScriptRuntime.FloatClass)
        return paramObject; 
      double d1 = toDouble(paramObject);
      if (Double.isInfinite(d1) || Double.isNaN(d1) || d1 == 0.0D)
        return Float.valueOf((float)d1); 
      double d2 = Math.abs(d1);
      if (d2 < 1.401298464324817E-45D) {
        float f;
        if (d1 > 0.0D) {
          f = 0.0F;
        } else {
          f = 0.0F;
        } 
        return Float.valueOf(f);
      } 
      if (d2 > 3.4028234663852886E38D) {
        float f;
        if (d1 > 0.0D) {
          f = Float.POSITIVE_INFINITY;
        } else {
          f = Float.NEGATIVE_INFINITY;
        } 
        return Float.valueOf(f);
      } 
      return Float.valueOf((float)d1);
    } 
    if (object == ScriptRuntime.IntegerClass || object == int.class)
      return (clazz == ScriptRuntime.IntegerClass) ? paramObject : Integer.valueOf((int)toInteger(paramObject, ScriptRuntime.IntegerClass, -2.147483648E9D, 2.147483647E9D)); 
    if (object == ScriptRuntime.LongClass || object == long.class) {
      if (clazz == ScriptRuntime.LongClass)
        return paramObject; 
      double d1 = Double.longBitsToDouble(4890909195324358655L);
      double d2 = Double.longBitsToDouble(-4332462841530417152L);
      return Long.valueOf(toInteger(paramObject, ScriptRuntime.LongClass, d2, d1));
    } 
    return (object == ScriptRuntime.ShortClass || object == short.class) ? ((clazz == ScriptRuntime.ShortClass) ? paramObject : Short.valueOf((short)(int)toInteger(paramObject, ScriptRuntime.ShortClass, -32768.0D, 32767.0D))) : ((object == ScriptRuntime.ByteClass || object == byte.class) ? ((clazz == ScriptRuntime.ByteClass) ? paramObject : Byte.valueOf((byte)(int)toInteger(paramObject, ScriptRuntime.ByteClass, -128.0D, 127.0D))) : new Double(toDouble(paramObject)));
  }
  
  @Deprecated
  public static Object coerceType(Class<?> paramClass, Object paramObject) {
    return coerceTypeImpl(paramClass, paramObject);
  }
  
  static Object coerceTypeImpl(Class<?> paramClass, Object paramObject) {
    Context context;
    Object object;
    if (paramObject != null && paramObject.getClass() == paramClass)
      return paramObject; 
    switch (getJSTypeCode(paramObject)) {
      default:
        return paramObject;
      case 8:
        if (paramClass == ScriptRuntime.StringClass)
          return ScriptRuntime.toString(paramObject); 
        if (paramClass.isPrimitive()) {
          if (paramClass == boolean.class)
            reportConversionError(paramObject, paramClass); 
          return coerceToNumber(paramClass, paramObject);
        } 
        if (paramClass.isInstance(paramObject))
          return paramObject; 
        if (paramClass == ScriptRuntime.DateClass && paramObject instanceof NativeDate)
          return new Date((long)((NativeDate)paramObject).getJSTimeValue()); 
        if (paramClass.isArray() && paramObject instanceof NativeArray) {
          NativeArray nativeArray = (NativeArray)paramObject;
          long l = nativeArray.getLength();
          Class<?> clazz = paramClass.getComponentType();
          Object object1 = Array.newInstance(clazz, (int)l);
          for (byte b = 0; b < l; b++) {
            try {
              Array.set(object1, b, coerceTypeImpl(clazz, nativeArray.get(b, nativeArray)));
            } catch (EvaluatorException evaluatorException) {
              reportConversionError(paramObject, paramClass);
            } 
          } 
          return object1;
        } 
        if (paramObject instanceof Wrapper) {
          paramObject = ((Wrapper)paramObject).unwrap();
          if (paramClass.isInstance(paramObject))
            return paramObject; 
          reportConversionError(paramObject, paramClass);
        } else {
          if (paramClass.isInterface() && (paramObject instanceof NativeObject || paramObject instanceof NativeFunction))
            return createInterfaceAdapter(paramClass, (ScriptableObject)paramObject); 
          reportConversionError(paramObject, paramClass);
        } 
      case 6:
      case 7:
        object = paramObject;
        if (paramObject instanceof Wrapper)
          object = ((Wrapper)paramObject).unwrap(); 
        if (paramClass.isPrimitive()) {
          if (paramClass == boolean.class)
            reportConversionError(object, paramClass); 
          return coerceToNumber(paramClass, object);
        } 
        if (paramClass == ScriptRuntime.StringClass)
          return object.toString(); 
        if (paramClass.isInstance(object))
          return object; 
        reportConversionError(object, paramClass);
        paramObject = object;
      case 5:
        object = paramObject;
        if (paramObject instanceof Wrapper)
          object = ((Wrapper)paramObject).unwrap(); 
        if (paramClass == ScriptRuntime.ClassClass || paramClass == ScriptRuntime.ObjectClass)
          return object; 
        if (paramClass == ScriptRuntime.StringClass)
          return object.toString(); 
        reportConversionError(object, paramClass);
        paramObject = object;
      case 4:
        if (paramClass == ScriptRuntime.StringClass || paramClass.isInstance(paramObject))
          return paramObject.toString(); 
        if (paramClass == char.class || paramClass == ScriptRuntime.CharacterClass)
          return (((CharSequence)paramObject).length() == 1) ? Character.valueOf(((CharSequence)paramObject).charAt(0)) : coerceToNumber(paramClass, paramObject); 
        if ((paramClass.isPrimitive() && paramClass != boolean.class) || ScriptRuntime.NumberClass.isAssignableFrom(paramClass))
          return coerceToNumber(paramClass, paramObject); 
        reportConversionError(paramObject, paramClass);
      case 3:
        if (paramClass == ScriptRuntime.StringClass)
          return ScriptRuntime.toString(paramObject); 
        if (paramClass == ScriptRuntime.ObjectClass) {
          context = Context.getCurrentContext();
          return (context != null && context.hasFeature(18) && Math.round(toDouble(paramObject)) == toDouble(paramObject)) ? coerceToNumber(long.class, paramObject) : coerceToNumber(double.class, paramObject);
        } 
        if ((context.isPrimitive() && context != boolean.class) || ScriptRuntime.NumberClass.isAssignableFrom((Class<?>)context))
          return coerceToNumber((Class<?>)context, paramObject); 
        reportConversionError(paramObject, (Class<?>)context);
      case 2:
        if (context == boolean.class || context == ScriptRuntime.BooleanClass || context == ScriptRuntime.ObjectClass)
          return paramObject; 
        if (context == ScriptRuntime.StringClass)
          return paramObject.toString(); 
        reportConversionError(paramObject, (Class<?>)context);
      case 1:
        if (context.isPrimitive())
          reportConversionError(paramObject, (Class<?>)context); 
        return null;
      case 0:
        break;
    } 
    if (context == ScriptRuntime.StringClass || context == ScriptRuntime.ObjectClass)
      return "undefined"; 
    reportConversionError("undefined", (Class<?>)context);
  }
  
  protected static Object createInterfaceAdapter(Class<?> paramClass, ScriptableObject paramScriptableObject) {
    Object object1 = Kit.makeHashKeyFromPair(COERCED_INTERFACE_KEY, paramClass);
    Object object2 = paramScriptableObject.getAssociatedValue(object1);
    return (object2 != null) ? object2 : paramScriptableObject.associateValue(object1, InterfaceAdapter.create(Context.getContext(), paramClass, paramScriptableObject));
  }
  
  static int getConversionWeight(Object paramObject, Class<?> paramClass) {
    Object object;
    int i = getJSTypeCode(paramObject);
    int j = 99;
    switch (i) {
      default:
        return 99;
      case 8:
        if (paramClass != ScriptRuntime.ObjectClass && paramClass.isInstance(paramObject))
          return 1; 
        if (paramClass.isArray()) {
          if (paramObject instanceof NativeArray)
            return 2; 
        } else {
          if (paramClass == ScriptRuntime.ObjectClass)
            return 3; 
          if (paramClass == ScriptRuntime.StringClass)
            return 4; 
          if (paramClass == ScriptRuntime.DateClass) {
            if (paramObject instanceof NativeDate)
              return 1; 
          } else {
            if (paramClass.isInterface())
              return (paramObject instanceof NativeFunction) ? 1 : ((paramObject instanceof NativeObject) ? 2 : 12); 
            if (paramClass.isPrimitive() && paramClass != boolean.class)
              return getSizeRank(paramClass) + 4; 
          } 
        } 
      case 6:
      case 7:
        object = paramObject;
        paramObject = object;
        if (object instanceof Wrapper)
          paramObject = ((Wrapper)object).unwrap(); 
        if (paramClass.isInstance(paramObject))
          return 0; 
        if (paramClass == ScriptRuntime.StringClass)
          return 2; 
        if (paramClass.isPrimitive() && paramClass != boolean.class) {
          if (i != 7)
            j = getSizeRank(paramClass) + 2; 
          return j;
        } 
      case 5:
        if (paramClass == ScriptRuntime.ClassClass)
          return 1; 
        if (paramClass == ScriptRuntime.ObjectClass)
          return 3; 
        if (paramClass == ScriptRuntime.StringClass)
          return 4; 
      case 4:
        if (paramClass == ScriptRuntime.StringClass)
          return 1; 
        if (paramClass.isInstance(paramObject))
          return 2; 
        if (paramClass.isPrimitive()) {
          if (paramClass == char.class)
            return 3; 
          if (paramClass != boolean.class)
            return 4; 
        } 
      case 3:
        if (paramClass.isPrimitive()) {
          if (paramClass == double.class)
            return 1; 
          if (paramClass != boolean.class)
            return getSizeRank(paramClass) + 1; 
        } else {
          if (paramClass == ScriptRuntime.StringClass)
            return 9; 
          if (paramClass == ScriptRuntime.ObjectClass)
            return 10; 
          if (ScriptRuntime.NumberClass.isAssignableFrom(paramClass))
            return 2; 
        } 
      case 2:
        if (paramClass == boolean.class)
          return 1; 
        if (paramClass == ScriptRuntime.BooleanClass)
          return 2; 
        if (paramClass == ScriptRuntime.ObjectClass)
          return 3; 
        if (paramClass == ScriptRuntime.StringClass)
          return 4; 
      case 1:
        if (!paramClass.isPrimitive())
          return 1; 
      case 0:
        break;
    } 
    if (paramClass == ScriptRuntime.StringClass || paramClass == ScriptRuntime.ObjectClass)
      return 1; 
  }
  
  private static int getJSTypeCode(Object paramObject) {
    return (paramObject == null) ? 1 : ((paramObject == Undefined.instance) ? 0 : ((paramObject instanceof CharSequence) ? 4 : ((paramObject instanceof Number) ? 3 : ((paramObject instanceof Boolean) ? 2 : ((paramObject instanceof Scriptable) ? ((paramObject instanceof NativeJavaClass) ? 5 : ((paramObject instanceof NativeJavaArray) ? 7 : ((paramObject instanceof Wrapper) ? 6 : 8))) : ((paramObject instanceof Class) ? 5 : (paramObject.getClass().isArray() ? 7 : 6)))))));
  }
  
  static int getSizeRank(Class<?> paramClass) {
    return (paramClass == double.class) ? 1 : ((paramClass == float.class) ? 2 : ((paramClass == long.class) ? 3 : ((paramClass == int.class) ? 4 : ((paramClass == short.class) ? 5 : ((paramClass == char.class) ? 6 : ((paramClass == byte.class) ? 7 : ((paramClass == boolean.class) ? 99 : 8)))))));
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    boolean bool = paramObjectInputStream.readBoolean();
    this.isAdapter = bool;
    if (bool) {
      Method method = adapter_readAdapterObject;
      if (method != null) {
        try {
          this.javaObject = method.invoke(null, new Object[] { this, paramObjectInputStream });
        } catch (Exception exception) {
          throw new IOException();
        } 
      } else {
        throw new ClassNotFoundException();
      } 
    } else {
      this.javaObject = exception.readObject();
    } 
    String str = (String)exception.readObject();
    if (str != null) {
      this.staticType = Class.forName(str);
    } else {
      this.staticType = null;
    } 
    initMembers();
  }
  
  static void reportConversionError(Object paramObject, Class<?> paramClass) {
    throw Context.reportRuntimeError2("msg.conversion.not.allowed", String.valueOf(paramObject), JavaMembers.javaSignature(paramClass));
  }
  
  private static double toDouble(Object paramObject) {
    if (paramObject instanceof Number)
      return ((Number)paramObject).doubleValue(); 
    if (paramObject instanceof String)
      return ScriptRuntime.toNumber((String)paramObject); 
    if (paramObject instanceof Scriptable)
      return (paramObject instanceof Wrapper) ? toDouble(((Wrapper)paramObject).unwrap()) : ScriptRuntime.toNumber(paramObject); 
    try {
      Method method = paramObject.getClass().getMethod("doubleValue", (Class[])null);
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException = null;
    } catch (SecurityException securityException) {
      securityException = null;
    } 
    if (securityException != null)
      try {
        return ((Number)securityException.invoke(paramObject, (Object[])null)).doubleValue();
      } catch (IllegalAccessException illegalAccessException) {
        reportConversionError(paramObject, double.class);
      } catch (InvocationTargetException invocationTargetException) {
        reportConversionError(paramObject, double.class);
      }  
    return ScriptRuntime.toNumber(paramObject.toString());
  }
  
  private static long toInteger(Object paramObject, Class<?> paramClass, double paramDouble1, double paramDouble2) {
    double d = toDouble(paramObject);
    if (Double.isInfinite(d) || Double.isNaN(d))
      reportConversionError(ScriptRuntime.toString(paramObject), paramClass); 
    if (d > 0.0D) {
      d = Math.floor(d);
    } else {
      d = Math.ceil(d);
    } 
    if (d < paramDouble1 || d > paramDouble2)
      reportConversionError(ScriptRuntime.toString(paramObject), paramClass); 
    return (long)d;
  }
  
  @Deprecated
  public static Object wrap(Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    Context context = Context.getContext();
    return context.getWrapFactory().wrap(context, paramScriptable, paramObject, paramClass);
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeBoolean(this.isAdapter);
    if (this.isAdapter) {
      Method method = adapter_writeAdapterObject;
      if (method != null) {
        Object object = this.javaObject;
        try {
          method.invoke(null, new Object[] { object, paramObjectOutputStream });
        } catch (Exception exception) {
          throw new IOException();
        } 
      } else {
        throw new IOException();
      } 
    } else {
      exception.writeObject(this.javaObject);
    } 
    Class<?> clazz = this.staticType;
    if (clazz != null) {
      exception.writeObject(clazz.getClass().getName());
    } else {
      exception.writeObject(null);
    } 
  }
  
  public void delete(int paramInt) {}
  
  public void delete(Symbol paramSymbol) {}
  
  public void delete(String paramString) {}
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    throw this.members.reportMemberNotFound(Integer.toString(paramInt));
  }
  
  public Object get(Symbol paramSymbol, Scriptable paramScriptable) {
    return Scriptable.NOT_FOUND;
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Map<String, FieldAndMethods> map = this.fieldAndMethods;
    if (map != null) {
      map = (Map<String, FieldAndMethods>)map.get(paramString);
      if (map != null)
        return map; 
    } 
    return this.members.get(this, paramString, this.javaObject, false);
  }
  
  public String getClassName() {
    return "JavaObject";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    String str;
    Class<?> clazz = paramClass;
    if (paramClass == null) {
      clazz = paramClass;
      if (this.javaObject instanceof Boolean)
        clazz = ScriptRuntime.BooleanClass; 
    } 
    if (clazz == null || clazz == ScriptRuntime.StringClass)
      return this.javaObject.toString(); 
    if (clazz == ScriptRuntime.BooleanClass) {
      str = "booleanValue";
    } else if (clazz == ScriptRuntime.NumberClass) {
      str = "doubleValue";
    } else {
      throw Context.reportRuntimeError0("msg.default.value");
    } 
    Object object = get(str, this);
    if (object instanceof Function) {
      object = object;
      object = object.call(Context.getContext(), object.getParentScope(), this, ScriptRuntime.emptyArgs);
    } else {
      if (clazz == ScriptRuntime.NumberClass) {
        object = this.javaObject;
        if (object instanceof Boolean) {
          double d;
          if (((Boolean)object).booleanValue()) {
            d = 1.0D;
          } else {
            d = 0.0D;
          } 
          return ScriptRuntime.wrapNumber(d);
        } 
      } 
      object = this.javaObject.toString();
    } 
    return object;
  }
  
  public Object[] getIds() {
    return this.members.getIds(false);
  }
  
  public Scriptable getParentScope() {
    return this.parent;
  }
  
  public Scriptable getPrototype() {
    return (this.prototype == null && this.javaObject instanceof String) ? TopLevel.getBuiltinPrototype(ScriptableObject.getTopLevelScope(this.parent), TopLevel.Builtins.String) : this.prototype;
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    return false;
  }
  
  public boolean has(Symbol paramSymbol, Scriptable paramScriptable) {
    return false;
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    return this.members.has(paramString, false);
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    return false;
  }
  
  protected void initMembers() {
    Object<?> object = (Object<?>)this.javaObject;
    if (object != null) {
      object = (Object<?>)object.getClass();
    } else {
      object = (Object<?>)this.staticType;
    } 
    object = (Object<?>)JavaMembers.lookupClass(this.parent, (Class<?>)object, this.staticType, this.isAdapter);
    this.members = (JavaMembers)object;
    this.fieldAndMethods = object.getFieldAndMethodsObjects(this, this.javaObject, false);
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    throw this.members.reportMemberNotFound(Integer.toString(paramInt));
  }
  
  public void put(Symbol paramSymbol, Scriptable paramScriptable, Object paramObject) {
    String str = paramSymbol.toString();
    if (this.prototype == null || this.members.has(str, false)) {
      this.members.put(this, str, this.javaObject, paramObject, false);
      return;
    } 
    Scriptable scriptable = this.prototype;
    if (scriptable instanceof SymbolScriptable)
      ((SymbolScriptable)scriptable).put(paramSymbol, scriptable, paramObject); 
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    if (this.prototype == null || this.members.has(paramString, false)) {
      this.members.put(this, paramString, this.javaObject, paramObject, false);
      return;
    } 
    paramScriptable = this.prototype;
    paramScriptable.put(paramString, paramScriptable, paramObject);
  }
  
  public void setParentScope(Scriptable paramScriptable) {
    this.parent = paramScriptable;
  }
  
  public void setPrototype(Scriptable paramScriptable) {
    this.prototype = paramScriptable;
  }
  
  public Object unwrap() {
    return this.javaObject;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */