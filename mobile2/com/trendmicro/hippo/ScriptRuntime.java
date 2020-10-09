package com.trendmicro.hippo;

import com.trendmicro.hippo.v8dtoa.DoubleConversion;
import com.trendmicro.hippo.v8dtoa.FastDtoa;
import com.trendmicro.hippo.xml.XMLLib;
import com.trendmicro.hippo.xml.XMLObject;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ScriptRuntime {
  public static final Class<?> BooleanClass = Kit.classOrNull("java.lang.Boolean");
  
  public static final Class<?> ByteClass = Kit.classOrNull("java.lang.Byte");
  
  public static final Class<?> CharacterClass = Kit.classOrNull("java.lang.Character");
  
  public static final Class<?> ClassClass = Kit.classOrNull("java.lang.Class");
  
  public static final Class<?> ContextClass;
  
  public static final Class<?> ContextFactoryClass;
  
  private static final String DEFAULT_NS_TAG = "__default_namespace__";
  
  public static final Class<?> DateClass;
  
  public static final Class<?> DoubleClass = Kit.classOrNull("java.lang.Double");
  
  public static final int ENUMERATE_ARRAY = 2;
  
  public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;
  
  public static final int ENUMERATE_KEYS = 0;
  
  public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;
  
  public static final int ENUMERATE_VALUES = 1;
  
  public static final int ENUMERATE_VALUES_IN_ORDER = 6;
  
  public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;
  
  public static final Class<?> FloatClass = Kit.classOrNull("java.lang.Float");
  
  public static final Class<?> FunctionClass;
  
  public static final Class<?> IntegerClass = Kit.classOrNull("java.lang.Integer");
  
  private static final Object LIBRARY_SCOPE_KEY;
  
  public static final Class<?> LongClass = Kit.classOrNull("java.lang.Long");
  
  public static final double NaN;
  
  public static final Double NaNobj;
  
  public static final Class<?> NumberClass = Kit.classOrNull("java.lang.Number");
  
  public static final Class<?> ObjectClass = Kit.classOrNull("java.lang.Object");
  
  public static Locale ROOT_LOCALE;
  
  public static final Class<Scriptable> ScriptableClass;
  
  public static final Class<?> ScriptableObjectClass;
  
  public static final Class<?> ShortClass = Kit.classOrNull("java.lang.Short");
  
  public static final Class<?> StringClass = Kit.classOrNull("java.lang.String");
  
  public static final Object[] emptyArgs;
  
  public static final String[] emptyStrings;
  
  public static MessageProvider messageProvider;
  
  public static final double negativeZero;
  
  static {
    DateClass = Kit.classOrNull("java.util.Date");
    ContextClass = Kit.classOrNull("com.trendmicro.hippo.Context");
    ContextFactoryClass = Kit.classOrNull("com.trendmicro.hippo.ContextFactory");
    FunctionClass = Kit.classOrNull("com.trendmicro.hippo.Function");
    ScriptableObjectClass = Kit.classOrNull("com.trendmicro.hippo.ScriptableObject");
    ScriptableClass = Scriptable.class;
    ROOT_LOCALE = new Locale("");
    LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";
    NaN = Double.longBitsToDouble(9221120237041090560L);
    negativeZero = Double.longBitsToDouble(Long.MIN_VALUE);
    NaNobj = new Double(NaN);
    messageProvider = new DefaultMessageProvider();
    emptyArgs = new Object[0];
    emptyStrings = new String[0];
  }
  
  public static CharSequence add(CharSequence paramCharSequence, Object paramObject) {
    return new ConsString(paramCharSequence, toCharSequence(paramObject));
  }
  
  public static CharSequence add(Object paramObject, CharSequence paramCharSequence) {
    return new ConsString(toCharSequence(paramObject), paramCharSequence);
  }
  
  public static Object add(Object paramObject1, Object paramObject2, Context paramContext) {
    if (paramObject1 instanceof Number && paramObject2 instanceof Number)
      return wrapNumber(((Number)paramObject1).doubleValue() + ((Number)paramObject2).doubleValue()); 
    if (paramObject1 instanceof XMLObject) {
      Object object = ((XMLObject)paramObject1).addValues(paramContext, true, paramObject2);
      if (object != Scriptable.NOT_FOUND)
        return object; 
    } 
    if (paramObject2 instanceof XMLObject) {
      Object object = ((XMLObject)paramObject2).addValues(paramContext, false, paramObject1);
      if (object != Scriptable.NOT_FOUND)
        return object; 
    } 
    if (!(paramObject1 instanceof Symbol) && !(paramObject2 instanceof Symbol)) {
      Object object = paramObject1;
      if (paramObject1 instanceof Scriptable)
        object = ((Scriptable)paramObject1).getDefaultValue(null); 
      paramObject1 = paramObject2;
      if (paramObject2 instanceof Scriptable)
        paramObject1 = ((Scriptable)paramObject2).getDefaultValue(null); 
      return (!(object instanceof CharSequence) && !(paramObject1 instanceof CharSequence)) ? ((object instanceof Number && paramObject1 instanceof Number) ? wrapNumber(((Number)object).doubleValue() + ((Number)paramObject1).doubleValue()) : wrapNumber(toNumber(object) + toNumber(paramObject1))) : new ConsString(toCharSequence(object), toCharSequence(paramObject1));
    } 
    throw typeError0("msg.not.a.number");
  }
  
  public static void addInstructionCount(Context paramContext, int paramInt) {
    paramContext.instructionCount += paramInt;
    if (paramContext.instructionCount > paramContext.instructionThreshold) {
      paramContext.observeInstructionCount(paramContext.instructionCount);
      paramContext.instructionCount = 0;
    } 
  }
  
  public static Object applyOrCall(boolean paramBoolean, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object[] arrayOfObject;
    int i = paramArrayOfObject.length;
    Callable callable = getCallable(paramScriptable2);
    paramScriptable2 = null;
    if (i != 0)
      if (paramContext.hasFeature(15)) {
        paramScriptable2 = toObjectOrNull(paramContext, paramArrayOfObject[0], paramScriptable1);
      } else if (paramArrayOfObject[0] == Undefined.instance) {
        paramScriptable2 = Undefined.SCRIPTABLE_UNDEFINED;
      } else {
        paramScriptable2 = toObjectOrNull(paramContext, paramArrayOfObject[0], paramScriptable1);
      }  
    Scriptable scriptable = paramScriptable2;
    if (paramScriptable2 == null) {
      scriptable = paramScriptable2;
      if (paramContext.hasFeature(15))
        scriptable = getTopCallScope(paramContext); 
    } 
    if (paramBoolean) {
      if (i <= 1) {
        arrayOfObject = emptyArgs;
      } else {
        arrayOfObject = getApplyArguments(paramContext, paramArrayOfObject[1]);
      } 
    } else if (i <= 1) {
      arrayOfObject = emptyArgs;
    } else {
      arrayOfObject = new Object[i - 1];
      System.arraycopy(paramArrayOfObject, 1, arrayOfObject, 0, i - 1);
    } 
    return callable.call(paramContext, paramScriptable1, scriptable, arrayOfObject);
  }
  
  public static Scriptable bind(Context paramContext, Scriptable paramScriptable, String paramString) {
    Scriptable scriptable1;
    XMLObject xMLObject2;
    XMLObject xMLObject3;
    Scriptable scriptable2 = null;
    Scriptable scriptable3 = paramScriptable.getParentScope();
    Scriptable scriptable4 = paramScriptable;
    if (scriptable3 != null) {
      scriptable4 = null;
      Scriptable scriptable = paramScriptable;
      paramScriptable = scriptable4;
      scriptable4 = scriptable3;
      while (true) {
        XMLObject xMLObject;
        scriptable2 = scriptable4;
        scriptable3 = scriptable;
        if (scriptable instanceof NativeWith) {
          scriptable2 = scriptable.getPrototype();
          if (scriptable2 instanceof XMLObject) {
            XMLObject xMLObject4 = (XMLObject)scriptable2;
            if (xMLObject4.has(paramContext, paramString))
              return (Scriptable)xMLObject4; 
            scriptable2 = paramScriptable;
            if (paramScriptable == null)
              xMLObject2 = xMLObject4; 
            xMLObject = xMLObject2;
          } else if (ScriptableObject.hasProperty((Scriptable)xMLObject2, paramString)) {
            return (Scriptable)xMLObject2;
          } 
          scriptable = scriptable4;
          scriptable4 = scriptable4.getParentScope();
          if (scriptable4 == null) {
            xMLObject2 = xMLObject;
            scriptable4 = scriptable;
            break;
          } 
          continue;
        } 
        while (true) {
          if (ScriptableObject.hasProperty(scriptable3, paramString))
            return scriptable3; 
          xMLObject3 = xMLObject2;
          scriptable = xMLObject2.getParentScope();
          Scriptable scriptable5 = scriptable;
          XMLObject xMLObject4 = xMLObject3;
          if (scriptable == null) {
            xMLObject2 = xMLObject;
            break;
          } 
        } 
        break;
      } 
    } 
    XMLObject xMLObject1 = xMLObject3;
    if (paramContext.useDynamicScope)
      scriptable1 = checkDynamicScope(paramContext.topCallScope, (Scriptable)xMLObject3); 
    return (Scriptable)(ScriptableObject.hasProperty(scriptable1, paramString) ? scriptable1 : xMLObject2);
  }
  
  @Deprecated
  public static Object call(Context paramContext, Object paramObject1, Object paramObject2, Object[] paramArrayOfObject, Scriptable paramScriptable) {
    if (paramObject1 instanceof Function) {
      paramObject1 = paramObject1;
      paramObject2 = toObjectOrNull(paramContext, paramObject2, paramScriptable);
      if (paramObject2 != null)
        return paramObject1.call(paramContext, paramScriptable, (Scriptable)paramObject2, paramArrayOfObject); 
      throw undefCallError(paramObject2, "function");
    } 
    throw notFunctionError(toString(paramObject1));
  }
  
  public static Object callIterator(Object paramObject, Context paramContext, Scriptable paramScriptable) {
    return getElemFunctionAndThis(paramObject, SymbolKey.ITERATOR, paramContext, paramScriptable).call(paramContext, paramScriptable, lastStoredScriptable(paramContext), emptyArgs);
  }
  
  public static Ref callRef(Callable paramCallable, Scriptable paramScriptable, Object[] paramArrayOfObject, Context paramContext) {
    if (paramCallable instanceof RefCallable) {
      paramCallable = paramCallable;
      Ref ref = paramCallable.refCall(paramContext, paramScriptable, paramArrayOfObject);
      if (ref != null)
        return ref; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramCallable.getClass().getName());
      stringBuilder.append(".refCall() returned null");
      throw new IllegalStateException(stringBuilder.toString());
    } 
    throw constructError("ReferenceError", getMessage1("msg.no.ref.from.function", toString(paramCallable)));
  }
  
  public static Object callSpecial(Context paramContext, Callable paramCallable, Scriptable paramScriptable1, Object[] paramArrayOfObject, Scriptable paramScriptable2, Scriptable paramScriptable3, int paramInt1, String paramString, int paramInt2) {
    if (paramInt1 == 1) {
      if (paramScriptable1.getParentScope() == null && NativeGlobal.isEvalFunction(paramCallable))
        return evalSpecial(paramContext, paramScriptable2, paramScriptable3, paramArrayOfObject, paramString, paramInt2); 
    } else {
      if (paramInt1 == 2) {
        if (NativeWith.isWithFunction(paramCallable))
          throw Context.reportRuntimeError1("msg.only.from.new", "With"); 
        return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, paramArrayOfObject);
      } 
      throw Kit.codeBug();
    } 
    return paramCallable.call(paramContext, paramScriptable2, paramScriptable1, paramArrayOfObject);
  }
  
  static void checkDeprecated(Context paramContext, String paramString) {
    String str;
    int i = paramContext.getLanguageVersion();
    if (i >= 140 || i == 0) {
      str = getMessage1("msg.deprec.ctor", paramString);
      if (i == 0) {
        Context.reportWarning(str);
        return;
      } 
    } else {
      return;
    } 
    throw Context.reportRuntimeError(str);
  }
  
  static Scriptable checkDynamicScope(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    if (paramScriptable1 == paramScriptable2)
      return paramScriptable1; 
    Scriptable scriptable = paramScriptable1;
    while (true) {
      scriptable = scriptable.getPrototype();
      if (scriptable == paramScriptable2)
        return paramScriptable1; 
      if (scriptable == null)
        return paramScriptable2; 
    } 
  }
  
  public static RegExpProxy checkRegExpProxy(Context paramContext) {
    RegExpProxy regExpProxy = getRegExpProxy(paramContext);
    if (regExpProxy != null)
      return regExpProxy; 
    throw Context.reportRuntimeError0("msg.no.regexp");
  }
  
  public static boolean cmp_LE(Object paramObject1, Object paramObject2) {
    double d1;
    double d2;
    boolean bool = paramObject1 instanceof Number;
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool && paramObject2 instanceof Number) {
      d1 = ((Number)paramObject1).doubleValue();
      d2 = ((Number)paramObject2).doubleValue();
    } else if (!(paramObject1 instanceof Symbol) && !(paramObject2 instanceof Symbol)) {
      Object object = paramObject1;
      if (paramObject1 instanceof Scriptable)
        object = ((Scriptable)paramObject1).getDefaultValue(NumberClass); 
      paramObject1 = paramObject2;
      if (paramObject2 instanceof Scriptable)
        paramObject1 = ((Scriptable)paramObject2).getDefaultValue(NumberClass); 
      if (object instanceof CharSequence && paramObject1 instanceof CharSequence) {
        if (object.toString().compareTo(paramObject1.toString()) > 0)
          bool2 = false; 
        return bool2;
      } 
      d1 = toNumber(object);
      d2 = toNumber(paramObject1);
    } else {
      throw typeError0("msg.compare.symbol");
    } 
    if (d1 <= d2) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  public static boolean cmp_LT(Object paramObject1, Object paramObject2) {
    double d1;
    double d2;
    boolean bool = paramObject1 instanceof Number;
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool && paramObject2 instanceof Number) {
      d1 = ((Number)paramObject1).doubleValue();
      d2 = ((Number)paramObject2).doubleValue();
    } else if (!(paramObject1 instanceof Symbol) && !(paramObject2 instanceof Symbol)) {
      Object object = paramObject1;
      if (paramObject1 instanceof Scriptable)
        object = ((Scriptable)paramObject1).getDefaultValue(NumberClass); 
      paramObject1 = paramObject2;
      if (paramObject2 instanceof Scriptable)
        paramObject1 = ((Scriptable)paramObject2).getDefaultValue(NumberClass); 
      if (object instanceof CharSequence && paramObject1 instanceof CharSequence) {
        if (object.toString().compareTo(paramObject1.toString()) >= 0)
          bool2 = false; 
        return bool2;
      } 
      d1 = toNumber(object);
      d2 = toNumber(paramObject1);
    } else {
      throw typeError0("msg.compare.symbol");
    } 
    if (d1 < d2) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  public static EcmaError constructError(String paramString1, String paramString2) {
    int[] arrayOfInt = new int[1];
    return constructError(paramString1, paramString2, Context.getSourcePositionFromStack(arrayOfInt), arrayOfInt[0], null, 0);
  }
  
  public static EcmaError constructError(String paramString1, String paramString2, int paramInt) {
    int[] arrayOfInt = new int[1];
    String str = Context.getSourcePositionFromStack(arrayOfInt);
    if (arrayOfInt[0] != 0)
      arrayOfInt[0] = arrayOfInt[0] + paramInt; 
    return constructError(paramString1, paramString2, str, arrayOfInt[0], null, 0);
  }
  
  public static EcmaError constructError(String paramString1, String paramString2, String paramString3, int paramInt1, String paramString4, int paramInt2) {
    return new EcmaError(paramString1, paramString2, paramString3, paramInt1, paramString4, paramInt2);
  }
  
  public static Scriptable createArrowFunctionActivation(NativeFunction paramNativeFunction, Scriptable paramScriptable, Object[] paramArrayOfObject, boolean paramBoolean) {
    return new NativeCall(paramNativeFunction, paramScriptable, paramArrayOfObject, true, paramBoolean);
  }
  
  @Deprecated
  public static Scriptable createFunctionActivation(NativeFunction paramNativeFunction, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    return createFunctionActivation(paramNativeFunction, paramScriptable, paramArrayOfObject, false);
  }
  
  public static Scriptable createFunctionActivation(NativeFunction paramNativeFunction, Scriptable paramScriptable, Object[] paramArrayOfObject, boolean paramBoolean) {
    return new NativeCall(paramNativeFunction, paramScriptable, paramArrayOfObject, false, paramBoolean);
  }
  
  private static XMLLib currentXMLLib(Context paramContext) {
    if (paramContext.topCallScope != null) {
      XMLLib xMLLib1 = paramContext.cachedXMLLib;
      XMLLib xMLLib2 = xMLLib1;
      if (xMLLib1 == null) {
        xMLLib2 = XMLLib.extractFromScope(paramContext.topCallScope);
        if (xMLLib2 != null) {
          paramContext.cachedXMLLib = xMLLib2;
        } else {
          throw new IllegalStateException();
        } 
      } 
      return xMLLib2;
    } 
    throw new IllegalStateException();
  }
  
  static String defaultObjectToSource(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    boolean bool;
    boolean bool1;
    if (paramContext.iterating == null) {
      bool = true;
      bool1 = false;
      paramContext.iterating = new ObjToIntMap(31);
    } else {
      bool = false;
      bool1 = paramContext.iterating.has(paramScriptable2);
    } 
    StringBuilder stringBuilder = new StringBuilder(128);
    if (bool)
      stringBuilder.append("("); 
    stringBuilder.append('{');
    if (!bool1)
      try {
        paramContext.iterating.intern(paramScriptable2);
        Object[] arrayOfObject = paramScriptable2.getIds();
      } finally {
        if (bool)
          paramContext.iterating = null; 
      }  
    if (bool)
      paramContext.iterating = null; 
    stringBuilder.append('}');
    if (bool)
      stringBuilder.append(')'); 
    return stringBuilder.toString();
  }
  
  static String defaultObjectToString(Scriptable paramScriptable) {
    if (paramScriptable == null)
      return "[object Null]"; 
    if (Undefined.isUndefined(paramScriptable))
      return "[object Undefined]"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[object ");
    stringBuilder.append(paramScriptable.getClassName());
    stringBuilder.append(']');
    return stringBuilder.toString();
  }
  
  @Deprecated
  public static Object delete(Object paramObject1, Object paramObject2, Context paramContext) {
    return delete(paramObject1, paramObject2, paramContext, false);
  }
  
  public static Object delete(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    paramScriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
    if (paramScriptable == null) {
      if (paramBoolean)
        return Boolean.TRUE; 
      throw undefDeleteError(paramObject1, paramObject2);
    } 
    return wrapBoolean(deleteObjectElem(paramScriptable, paramObject2, paramContext));
  }
  
  @Deprecated
  public static Object delete(Object paramObject1, Object paramObject2, Context paramContext, boolean paramBoolean) {
    return delete(paramObject1, paramObject2, paramContext, getTopCallScope(paramContext), paramBoolean);
  }
  
  public static boolean deleteObjectElem(Scriptable paramScriptable, Object paramObject, Context paramContext) {
    SymbolScriptable symbolScriptable;
    if (isSymbol(paramObject)) {
      symbolScriptable = ScriptableObject.ensureSymbolScriptable(paramScriptable);
      paramObject = paramObject;
      symbolScriptable.delete((Symbol)paramObject);
      return symbolScriptable.has((Symbol)paramObject, paramScriptable) ^ true;
    } 
    paramObject = toStringIdOrIndex((Context)symbolScriptable, paramObject);
    if (paramObject == null) {
      int i = lastIndexResult((Context)symbolScriptable);
      paramScriptable.delete(i);
      return paramScriptable.has(i, paramScriptable) ^ true;
    } 
    paramScriptable.delete((String)paramObject);
    return paramScriptable.has((String)paramObject, paramScriptable) ^ true;
  }
  
  private static Object doScriptableIncrDecr(Scriptable paramScriptable1, String paramString, Scriptable paramScriptable2, Object paramObject, int paramInt) {
    boolean bool;
    double d;
    if ((paramInt & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramObject instanceof Number) {
      d = ((Number)paramObject).doubleValue();
    } else {
      double d1 = toNumber(paramObject);
      d = d1;
      if (bool) {
        paramObject = wrapNumber(d1);
        d = d1;
      } 
    } 
    if ((paramInt & 0x1) == 0) {
      d++;
    } else {
      d--;
    } 
    Number number = wrapNumber(d);
    paramScriptable1.put(paramString, paramScriptable2, number);
    return bool ? paramObject : number;
  }
  
  @Deprecated
  public static Object doTopCall(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return doTopCall(paramCallable, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, paramContext.isTopLevelStrict);
  }
  
  public static Object doTopCall(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, boolean paramBoolean) {
    if (paramScriptable1 != null) {
      if (paramContext.topCallScope == null) {
        paramContext.topCallScope = ScriptableObject.getTopLevelScope(paramScriptable1);
        paramContext.useDynamicScope = paramContext.hasFeature(7);
        boolean bool = paramContext.isTopLevelStrict;
        paramContext.isTopLevelStrict = paramBoolean;
        ContextFactory contextFactory = paramContext.getFactory();
        try {
          Object object = contextFactory.doTopCall(paramCallable, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
          paramContext.topCallScope = null;
          paramContext.cachedXMLLib = null;
          paramContext.isTopLevelStrict = bool;
          if (paramContext.currentActivationCall == null)
            return object; 
          throw new IllegalStateException();
        } finally {
          paramContext.topCallScope = null;
          paramContext.cachedXMLLib = null;
          paramContext.isTopLevelStrict = bool;
          if (paramContext.currentActivationCall != null)
            throw new IllegalStateException(); 
        } 
      } 
      throw new IllegalStateException();
    } 
    throw new IllegalArgumentException();
  }
  
  @Deprecated
  public static Object elemIncrDecr(Object paramObject1, Object paramObject2, Context paramContext, int paramInt) {
    return elemIncrDecr(paramObject1, paramObject2, paramContext, getTopCallScope(paramContext), paramInt);
  }
  
  public static Object elemIncrDecr(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable, int paramInt) {
    boolean bool;
    double d;
    Object object = getObjectElem(paramObject1, paramObject2, paramContext, paramScriptable);
    if ((paramInt & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (object instanceof Number) {
      d = ((Number)object).doubleValue();
    } else {
      double d1 = toNumber(object);
      d = d1;
      if (bool) {
        object = wrapNumber(d1);
        d = d1;
      } 
    } 
    if ((paramInt & 0x1) == 0) {
      d++;
    } else {
      d--;
    } 
    Number number = wrapNumber(d);
    setObjectElem(paramObject1, paramObject2, number, paramContext, paramScriptable);
    return bool ? object : number;
  }
  
  public static void enterActivationFunction(Context paramContext, Scriptable paramScriptable) {
    if (paramContext.topCallScope != null) {
      paramScriptable = paramScriptable;
      ((NativeCall)paramScriptable).parentActivationCall = paramContext.currentActivationCall;
      paramContext.currentActivationCall = (NativeCall)paramScriptable;
      paramScriptable.defineAttributesForArguments();
      return;
    } 
    throw new IllegalStateException();
  }
  
  public static Scriptable enterDotQuery(Object paramObject, Scriptable paramScriptable) {
    if (paramObject instanceof XMLObject)
      return ((XMLObject)paramObject).enterDotQuery(paramScriptable); 
    throw notXmlError(paramObject);
  }
  
  public static Scriptable enterWith(Object paramObject, Context paramContext, Scriptable paramScriptable) {
    Scriptable scriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (scriptable != null)
      return (scriptable instanceof XMLObject) ? ((XMLObject)scriptable).enterWith(paramScriptable) : new NativeWith(paramScriptable, scriptable); 
    throw typeError1("msg.undef.with", toString(paramObject));
  }
  
  private static void enumChangeObject(IdEnumeration paramIdEnumeration) {
    Object[] arrayOfObject = null;
    while (paramIdEnumeration.obj != null) {
      arrayOfObject = paramIdEnumeration.obj.getIds();
      if (arrayOfObject.length != 0)
        break; 
      paramIdEnumeration.obj = paramIdEnumeration.obj.getPrototype();
    } 
    if (paramIdEnumeration.obj != null && paramIdEnumeration.ids != null) {
      Object[] arrayOfObject1 = paramIdEnumeration.ids;
      int i = arrayOfObject1.length;
      if (paramIdEnumeration.used == null)
        paramIdEnumeration.used = new ObjToIntMap(i); 
      for (int j = 0; j != i; j++)
        paramIdEnumeration.used.intern(arrayOfObject1[j]); 
    } 
    paramIdEnumeration.ids = arrayOfObject;
    paramIdEnumeration.index = 0;
  }
  
  public static Object enumId(Object paramObject, Context paramContext) {
    IdEnumeration idEnumeration = (IdEnumeration)paramObject;
    if (idEnumeration.iterator != null)
      return idEnumeration.currentId; 
    int i = idEnumeration.enumType;
    if (i != 0)
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 4) {
              if (i != 5)
                throw Kit.codeBug(); 
              Object object = idEnumeration.currentId;
              paramObject = enumValue(paramObject, paramContext);
              return paramContext.newArray(ScriptableObject.getTopLevelScope(idEnumeration.obj), new Object[] { object, paramObject });
            } 
            return enumValue(paramObject, paramContext);
          } 
        } else {
          Object object = idEnumeration.currentId;
          paramObject = enumValue(paramObject, paramContext);
          return paramContext.newArray(ScriptableObject.getTopLevelScope(idEnumeration.obj), new Object[] { object, paramObject });
        } 
      } else {
        return enumValue(paramObject, paramContext);
      }  
    return idEnumeration.currentId;
  }
  
  @Deprecated
  public static Object enumInit(Object paramObject, Context paramContext, int paramInt) {
    return enumInit(paramObject, paramContext, getTopCallScope(paramContext), paramInt);
  }
  
  public static Object enumInit(Object paramObject, Context paramContext, Scriptable paramScriptable, int paramInt) {
    IdEnumeration idEnumeration = new IdEnumeration();
    idEnumeration.obj = toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (paramInt == 6) {
      idEnumeration.enumType = paramInt;
      idEnumeration.iterator = null;
      return enumInitInOrder(paramContext, idEnumeration);
    } 
    if (idEnumeration.obj == null)
      return idEnumeration; 
    idEnumeration.enumType = paramInt;
    idEnumeration.iterator = null;
    if (paramInt != 3 && paramInt != 4 && paramInt != 5) {
      boolean bool;
      paramScriptable = idEnumeration.obj.getParentScope();
      paramObject = idEnumeration.obj;
      if (paramInt == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      idEnumeration.iterator = toIterator(paramContext, paramScriptable, (Scriptable)paramObject, bool);
    } 
    if (idEnumeration.iterator == null)
      enumChangeObject(idEnumeration); 
    return idEnumeration;
  }
  
  @Deprecated
  public static Object enumInit(Object paramObject, Context paramContext, boolean paramBoolean) {
    boolean bool;
    if (paramBoolean) {
      bool = true;
    } else {
      bool = false;
    } 
    return enumInit(paramObject, paramContext, bool);
  }
  
  private static Object enumInitInOrder(Context paramContext, IdEnumeration paramIdEnumeration) {
    if (paramIdEnumeration.obj instanceof ScriptableObject) {
      ScriptableObject scriptableObject = (ScriptableObject)paramIdEnumeration.obj;
      if (ScriptableObject.hasProperty(scriptableObject, SymbolKey.ITERATOR)) {
        Object object = ScriptableObject.getProperty(scriptableObject, SymbolKey.ITERATOR);
        if (object instanceof Callable) {
          Object object1 = ((Callable)object).call(paramContext, paramIdEnumeration.obj.getParentScope(), paramIdEnumeration.obj, new Object[0]);
          if (object1 instanceof Scriptable) {
            paramIdEnumeration.iterator = (Scriptable)object1;
            return paramIdEnumeration;
          } 
          throw typeError1("msg.not.iterable", toString(paramIdEnumeration.obj));
        } 
        throw typeError1("msg.not.iterable", toString(paramIdEnumeration.obj));
      } 
      throw typeError1("msg.not.iterable", toString(paramIdEnumeration.obj));
    } 
    throw typeError1("msg.not.iterable", toString(paramIdEnumeration.obj));
  }
  
  public static Boolean enumNext(Object paramObject) {
    IdEnumeration idEnumeration = (IdEnumeration)paramObject;
    if (idEnumeration.iterator != null) {
      if (idEnumeration.enumType == 6)
        return enumNextInOrder(idEnumeration); 
      paramObject = ScriptableObject.getProperty(idEnumeration.iterator, "next");
      if (!(paramObject instanceof Callable))
        return Boolean.FALSE; 
      Callable callable = (Callable)paramObject;
      paramObject = Context.getContext();
      try {
        idEnumeration.currentId = callable.call((Context)paramObject, idEnumeration.iterator.getParentScope(), idEnumeration.iterator, emptyArgs);
        return Boolean.TRUE;
      } catch (JavaScriptException javaScriptException) {
        if (javaScriptException.getValue() instanceof NativeIterator.StopIteration)
          return Boolean.FALSE; 
        throw javaScriptException;
      } 
    } 
    while (true) {
      if (idEnumeration.obj == null)
        return Boolean.FALSE; 
      if (idEnumeration.index == idEnumeration.ids.length) {
        idEnumeration.obj = idEnumeration.obj.getPrototype();
        enumChangeObject(idEnumeration);
        continue;
      } 
      paramObject = idEnumeration.ids;
      int i = idEnumeration.index;
      idEnumeration.index = i + 1;
      paramObject = paramObject[i];
      if ((idEnumeration.used != null && idEnumeration.used.has(paramObject)) || paramObject instanceof Symbol)
        continue; 
      if (paramObject instanceof String) {
        paramObject = paramObject;
        if (!idEnumeration.obj.has((String)paramObject, idEnumeration.obj))
          continue; 
        idEnumeration.currentId = paramObject;
        break;
      } 
      i = ((Number)paramObject).intValue();
      if (!idEnumeration.obj.has(i, idEnumeration.obj))
        continue; 
      if (idEnumeration.enumNumbers) {
        paramObject = Integer.valueOf(i);
      } else {
        paramObject = String.valueOf(i);
      } 
      idEnumeration.currentId = paramObject;
      break;
    } 
    return Boolean.TRUE;
  }
  
  private static Boolean enumNextInOrder(IdEnumeration paramIdEnumeration) {
    Object object = ScriptableObject.getProperty(paramIdEnumeration.iterator, "next");
    if (object instanceof Callable) {
      object = object;
      Context context = Context.getContext();
      Scriptable scriptable = paramIdEnumeration.iterator.getParentScope();
      object = toObject(context, scriptable, object.call(context, scriptable, paramIdEnumeration.iterator, emptyArgs));
      Object object1 = ScriptableObject.getProperty((Scriptable)object, "done");
      if (object1 != ScriptableObject.NOT_FOUND && toBoolean(object1))
        return Boolean.FALSE; 
      paramIdEnumeration.currentId = ScriptableObject.getProperty((Scriptable)object, "value");
      return Boolean.TRUE;
    } 
    throw notFunctionError(paramIdEnumeration.iterator, "next");
  }
  
  public static Object enumValue(Object paramObject, Context paramContext) {
    paramObject = paramObject;
    if (isSymbol(((IdEnumeration)paramObject).currentId)) {
      paramObject = ScriptableObject.ensureSymbolScriptable(((IdEnumeration)paramObject).obj).get((Symbol)((IdEnumeration)paramObject).currentId, ((IdEnumeration)paramObject).obj);
    } else {
      String str = toStringIdOrIndex(paramContext, ((IdEnumeration)paramObject).currentId);
      if (str == null) {
        int i = lastIndexResult(paramContext);
        paramObject = ((IdEnumeration)paramObject).obj.get(i, ((IdEnumeration)paramObject).obj);
      } else {
        paramObject = ((IdEnumeration)paramObject).obj.get(str, ((IdEnumeration)paramObject).obj);
      } 
    } 
    return paramObject;
  }
  
  public static boolean eq(Object paramObject1, Object paramObject2) {
    boolean bool1 = true;
    boolean bool = true;
    boolean bool2 = true;
    if (paramObject1 == null || paramObject1 == Undefined.instance) {
      if (paramObject2 == null || paramObject2 == Undefined.instance)
        return true; 
      if (paramObject2 instanceof ScriptableObject) {
        paramObject1 = ((ScriptableObject)paramObject2).equivalentValues(paramObject1);
        if (paramObject1 != Scriptable.NOT_FOUND)
          return ((Boolean)paramObject1).booleanValue(); 
      } 
      return false;
    } 
    if (paramObject1 instanceof Number)
      return eqNumber(((Number)paramObject1).doubleValue(), paramObject2); 
    if (paramObject1 == paramObject2)
      return true; 
    if (paramObject1 instanceof CharSequence)
      return eqString((CharSequence)paramObject1, paramObject2); 
    boolean bool3 = paramObject1 instanceof Boolean;
    double d = 1.0D;
    if (bool3) {
      bool1 = ((Boolean)paramObject1).booleanValue();
      if (paramObject2 instanceof Boolean) {
        if (bool1 != ((Boolean)paramObject2).booleanValue())
          bool2 = false; 
        return bool2;
      } 
      if (paramObject2 instanceof ScriptableObject) {
        paramObject1 = ((ScriptableObject)paramObject2).equivalentValues(paramObject1);
        if (paramObject1 != Scriptable.NOT_FOUND)
          return ((Boolean)paramObject1).booleanValue(); 
      } 
      if (!bool1)
        d = 0.0D; 
      return eqNumber(d, paramObject2);
    } 
    if (paramObject1 instanceof Scriptable) {
      if (paramObject2 instanceof Scriptable) {
        if (paramObject1 instanceof ScriptableObject) {
          Object object = ((ScriptableObject)paramObject1).equivalentValues(paramObject2);
          if (object != Scriptable.NOT_FOUND)
            return ((Boolean)object).booleanValue(); 
        } 
        if (paramObject2 instanceof ScriptableObject) {
          Object object = ((ScriptableObject)paramObject2).equivalentValues(paramObject1);
          if (object != Scriptable.NOT_FOUND)
            return ((Boolean)object).booleanValue(); 
        } 
        if (paramObject1 instanceof Wrapper && paramObject2 instanceof Wrapper) {
          paramObject1 = ((Wrapper)paramObject1).unwrap();
          paramObject2 = ((Wrapper)paramObject2).unwrap();
          return (paramObject1 == paramObject2 || (isPrimitive(paramObject1) && isPrimitive(paramObject2) && eq(paramObject1, paramObject2))) ? bool1 : false;
        } 
        return false;
      } 
      if (paramObject2 instanceof Boolean) {
        if (paramObject1 instanceof ScriptableObject) {
          Object object = ((ScriptableObject)paramObject1).equivalentValues(paramObject2);
          if (object != Scriptable.NOT_FOUND)
            return ((Boolean)object).booleanValue(); 
        } 
        if (!((Boolean)paramObject2).booleanValue())
          d = 0.0D; 
        return eqNumber(d, paramObject1);
      } 
      return (paramObject2 instanceof Number) ? eqNumber(((Number)paramObject2).doubleValue(), paramObject1) : ((paramObject2 instanceof CharSequence) ? eqString((CharSequence)paramObject2, paramObject1) : false);
    } 
    warnAboutNonJSObject(paramObject1);
    if (paramObject1 == paramObject2) {
      bool2 = bool;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  static boolean eqNumber(double paramDouble, Object paramObject) {
    while (true) {
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      if (paramObject == null || paramObject == Undefined.instance)
        break; 
      if (paramObject instanceof Number) {
        if (paramDouble == ((Number)paramObject).doubleValue())
          bool3 = true; 
        return bool3;
      } 
      if (paramObject instanceof CharSequence) {
        bool3 = bool1;
        if (paramDouble == toNumber(paramObject))
          bool3 = true; 
        return bool3;
      } 
      if (paramObject instanceof Boolean) {
        double d;
        if (((Boolean)paramObject).booleanValue()) {
          d = 1.0D;
        } else {
          d = 0.0D;
        } 
        bool3 = bool2;
        if (paramDouble == d)
          bool3 = true; 
        return bool3;
      } 
      if (isSymbol(paramObject))
        return false; 
      if (paramObject instanceof Scriptable) {
        if (paramObject instanceof ScriptableObject) {
          Number number = wrapNumber(paramDouble);
          Object object = ((ScriptableObject)paramObject).equivalentValues(number);
          if (object != Scriptable.NOT_FOUND)
            return ((Boolean)object).booleanValue(); 
        } 
        paramObject = toPrimitive(paramObject);
        continue;
      } 
      warnAboutNonJSObject(paramObject);
      return false;
    } 
    return false;
  }
  
  private static boolean eqString(CharSequence paramCharSequence, Object paramObject) {
    while (true) {
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      if (paramObject == null || paramObject == Undefined.instance)
        break; 
      if (paramObject instanceof CharSequence) {
        paramObject = paramObject;
        bool1 = bool3;
        if (paramCharSequence.length() == paramObject.length()) {
          bool1 = bool3;
          if (paramCharSequence.toString().equals(paramObject.toString()))
            bool1 = true; 
        } 
        return bool1;
      } 
      if (paramObject instanceof Number) {
        if (toNumber(paramCharSequence.toString()) == ((Number)paramObject).doubleValue())
          bool1 = true; 
        return bool1;
      } 
      if (paramObject instanceof Boolean) {
        double d2;
        double d1 = toNumber(paramCharSequence.toString());
        if (((Boolean)paramObject).booleanValue()) {
          d2 = 1.0D;
        } else {
          d2 = 0.0D;
        } 
        bool1 = bool2;
        if (d1 == d2)
          bool1 = true; 
        return bool1;
      } 
      if (isSymbol(paramObject))
        return false; 
      if (paramObject instanceof Scriptable) {
        if (paramObject instanceof ScriptableObject) {
          Object object = ((ScriptableObject)paramObject).equivalentValues(paramCharSequence.toString());
          if (object != Scriptable.NOT_FOUND)
            return ((Boolean)object).booleanValue(); 
        } 
        paramObject = toPrimitive(paramObject);
        continue;
      } 
      warnAboutNonJSObject(paramObject);
      return false;
    } 
    return false;
  }
  
  private static RuntimeException errorWithClassName(String paramString, Object paramObject) {
    return Context.reportRuntimeError1(paramString, paramObject.getClass().getName());
  }
  
  public static String escapeAttributeValue(Object paramObject, Context paramContext) {
    return currentXMLLib(paramContext).escapeAttributeValue(paramObject);
  }
  
  public static String escapeString(String paramString) {
    return escapeString(paramString, '"');
  }
  
  public static String escapeString(String paramString, char paramChar) {
    if (paramChar != '"' && paramChar != '\'' && paramChar != '`')
      Kit.codeBug(); 
    StringBuilder stringBuilder = null;
    int i = 0;
    int j = paramString.length();
    while (i != j) {
      StringBuilder stringBuilder1;
      char c = paramString.charAt(i);
      if (' ' <= c && c <= '~' && c != paramChar && c != '\\') {
        stringBuilder1 = stringBuilder;
        if (stringBuilder != null) {
          stringBuilder.append((char)c);
          stringBuilder1 = stringBuilder;
        } 
      } else {
        StringBuilder stringBuilder2 = stringBuilder;
        if (stringBuilder == null) {
          stringBuilder2 = new StringBuilder(j + 3);
          stringBuilder2.append(paramString);
          stringBuilder2.setLength(i);
        } 
        int k = -1;
        if (c != ' ') {
          if (c != '\\') {
            switch (c) {
              case '\r':
                k = 114;
                break;
              case '\f':
                k = 102;
                break;
              case '\013':
                k = 118;
                break;
              case '\n':
                k = 110;
                break;
              case '\t':
                k = 116;
                break;
              case '\b':
                k = 98;
                break;
            } 
          } else {
            k = 92;
          } 
        } else {
          k = 32;
        } 
        if (k >= 0) {
          stringBuilder2.append('\\');
          stringBuilder2.append((char)k);
          stringBuilder1 = stringBuilder2;
        } else if (c == paramChar) {
          stringBuilder2.append('\\');
          stringBuilder2.append(paramChar);
          stringBuilder1 = stringBuilder2;
        } else {
          if (c < 'Ā') {
            stringBuilder2.append("\\x");
            k = 2;
          } else {
            stringBuilder2.append("\\u");
            k = 4;
          } 
          k = (k - 1) * 4;
          while (true) {
            stringBuilder1 = stringBuilder2;
            if (k >= 0) {
              int m = c >> k & 0xF;
              if (m < 10) {
                m += 48;
              } else {
                m += 87;
              } 
              stringBuilder2.append((char)m);
              k -= 4;
              continue;
            } 
            break;
          } 
        } 
      } 
      i++;
      stringBuilder = stringBuilder1;
    } 
    if (stringBuilder != null)
      paramString = stringBuilder.toString(); 
    return paramString;
  }
  
  public static String escapeTextValue(Object paramObject, Context paramContext) {
    return currentXMLLib(paramContext).escapeTextValue(paramObject);
  }
  
  public static Object evalSpecial(Context paramContext, Scriptable paramScriptable, Object paramObject, Object[] paramArrayOfObject, String paramString, int paramInt) {
    int[] arrayOfInt1;
    Script script;
    int[] arrayOfInt2;
    if (paramArrayOfObject.length < 1)
      return Undefined.instance; 
    Object object = paramArrayOfObject[0];
    if (!(object instanceof CharSequence)) {
      if (!paramContext.hasFeature(11) && !paramContext.hasFeature(9)) {
        Context.reportWarning(getMessage0("msg.eval.nonstring"));
        return object;
      } 
      throw Context.reportRuntimeError0("msg.eval.nonstring.strict");
    } 
    if (paramString == null) {
      arrayOfInt2 = new int[1];
      String str1 = Context.getSourcePositionFromStack(arrayOfInt2);
      if (str1 != null) {
        paramInt = arrayOfInt2[0];
      } else {
        str1 = "";
      } 
    } else {
      arrayOfInt1 = arrayOfInt2;
    } 
    String str = makeUrlForGeneratedScript(true, (String)arrayOfInt1, paramInt);
    ErrorReporter errorReporter = DefaultErrorReporter.forEval(paramContext.getErrorReporter());
    Evaluator evaluator = Context.createInterpreter();
    if (evaluator != null) {
      script = paramContext.compileString(object.toString(), evaluator, errorReporter, str, 1, null);
      evaluator.setEvalScriptFlag(script);
      return ((Callable)script).call(paramContext, paramScriptable, (Scriptable)paramObject, emptyArgs);
    } 
    throw new JavaScriptException("Interpreter not present", script, paramInt);
  }
  
  public static void exitActivationFunction(Context paramContext) {
    NativeCall nativeCall = paramContext.currentActivationCall;
    paramContext.currentActivationCall = nativeCall.parentActivationCall;
    nativeCall.parentActivationCall = null;
  }
  
  static NativeCall findFunctionActivation(Context paramContext, Function paramFunction) {
    for (NativeCall nativeCall = paramContext.currentActivationCall; nativeCall != null; nativeCall = nativeCall.parentActivationCall) {
      if (nativeCall.function == paramFunction)
        return nativeCall; 
    } 
    return null;
  }
  
  static Object[] getApplyArguments(Context paramContext, Object paramObject) {
    if (paramObject == null || paramObject == Undefined.instance)
      return emptyArgs; 
    if (paramObject instanceof Scriptable && isArrayLike((Scriptable)paramObject))
      return paramContext.getElements((Scriptable)paramObject); 
    if (paramObject instanceof ScriptableObject)
      return emptyArgs; 
    throw typeError0("msg.arg.isnt.array");
  }
  
  public static Object[] getArrayElements(Scriptable paramScriptable) {
    long l = NativeArray.getLengthProperty(Context.getContext(), paramScriptable, false);
    if (l <= 2147483647L) {
      int i = (int)l;
      if (i == 0)
        return emptyArgs; 
      Object[] arrayOfObject = new Object[i];
      for (byte b = 0; b < i; b++) {
        Object object = ScriptableObject.getProperty(paramScriptable, b);
        if (object == Scriptable.NOT_FOUND)
          object = Undefined.instance; 
        arrayOfObject[b] = object;
      } 
      return arrayOfObject;
    } 
    throw new IllegalArgumentException();
  }
  
  static Callable getCallable(Scriptable paramScriptable) {
    Callable callable;
    if (paramScriptable instanceof Callable) {
      callable = (Callable)paramScriptable;
    } else {
      Object object = callable.getDefaultValue(FunctionClass);
      if (object instanceof Callable)
        return (Callable)object; 
      throw notFunctionError(object, callable);
    } 
    return callable;
  }
  
  @Deprecated
  public static Callable getElemFunctionAndThis(Object paramObject1, Object paramObject2, Context paramContext) {
    return getElemFunctionAndThis(paramObject1, paramObject2, paramContext, getTopCallScope(paramContext));
  }
  
  public static Callable getElemFunctionAndThis(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable) {
    Object object;
    if (isSymbol(paramObject2)) {
      Scriptable scriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
      if (scriptable != null) {
        object = ScriptableObject.getProperty(scriptable, (Symbol)paramObject2);
        paramObject1 = scriptable;
      } else {
        throw undefCallError(paramObject1, String.valueOf(paramObject2));
      } 
    } else {
      String str = toStringIdOrIndex(paramContext, paramObject2);
      if (str != null)
        return getPropFunctionAndThis(paramObject1, str, paramContext, (Scriptable)object); 
      int i = lastIndexResult(paramContext);
      Scriptable scriptable = toObjectOrNull(paramContext, paramObject1, (Scriptable)object);
      if (scriptable != null) {
        object = ScriptableObject.getProperty(scriptable, i);
        paramObject1 = scriptable;
      } else {
        throw undefCallError(paramObject1, String.valueOf(paramObject2));
      } 
    } 
    if (object instanceof Callable) {
      storeScriptable(paramContext, (Scriptable)paramObject1);
      return (Callable)object;
    } 
    throw notFunctionError(object, paramObject2);
  }
  
  static Function getExistingCtor(Context paramContext, Scriptable paramScriptable, String paramString) {
    Object object = ScriptableObject.getProperty(paramScriptable, paramString);
    if (object instanceof Function)
      return (Function)object; 
    if (object == Scriptable.NOT_FOUND)
      throw Context.reportRuntimeError1("msg.ctor.not.found", paramString); 
    throw Context.reportRuntimeError1("msg.not.ctor", paramString);
  }
  
  public static ScriptableObject getGlobal(Context paramContext) {
    Class<?> clazz = Kit.classOrNull("com.trendmicro.hippo.tools.shell.Global");
    if (clazz != null)
      try {
        return clazz.getConstructor(new Class[] { ContextClass }).newInstance(new Object[] { paramContext });
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
    return new ImporterTopLevel((Context)runtimeException);
  }
  
  static Object getIndexObject(double paramDouble) {
    int i = (int)paramDouble;
    return (i == paramDouble) ? Integer.valueOf(i) : toString(paramDouble);
  }
  
  static Object getIndexObject(String paramString) {
    long l = indexFromString(paramString);
    return (l >= 0L) ? Integer.valueOf((int)l) : paramString;
  }
  
  public static ScriptableObject getLibraryScopeOrNull(Scriptable paramScriptable) {
    return (ScriptableObject)ScriptableObject.getTopScopeValue(paramScriptable, LIBRARY_SCOPE_KEY);
  }
  
  public static String getMessage(String paramString, Object[] paramArrayOfObject) {
    return messageProvider.getMessage(paramString, paramArrayOfObject);
  }
  
  public static String getMessage0(String paramString) {
    return getMessage(paramString, null);
  }
  
  public static String getMessage1(String paramString, Object paramObject) {
    return getMessage(paramString, new Object[] { paramObject });
  }
  
  public static String getMessage2(String paramString, Object paramObject1, Object paramObject2) {
    return getMessage(paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public static String getMessage3(String paramString, Object paramObject1, Object paramObject2, Object paramObject3) {
    return getMessage(paramString, new Object[] { paramObject1, paramObject2, paramObject3 });
  }
  
  public static String getMessage4(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4) {
    return getMessage(paramString, new Object[] { paramObject1, paramObject2, paramObject3, paramObject4 });
  }
  
  public static Callable getNameFunctionAndThis(String paramString, Context paramContext, Scriptable paramScriptable) {
    Object object = paramScriptable.getParentScope();
    if (object == null) {
      object = topScopeName(paramContext, paramScriptable, paramString);
      if (!(object instanceof Callable)) {
        if (object == Scriptable.NOT_FOUND)
          throw notFoundError(paramScriptable, paramString); 
        throw notFunctionError(object, paramString);
      } 
      storeScriptable(paramContext, paramScriptable);
      return (Callable)object;
    } 
    return (Callable)nameOrFunction(paramContext, paramScriptable, (Scriptable)object, paramString, true);
  }
  
  public static Object getObjectElem(Scriptable paramScriptable, Object paramObject, Context paramContext) {
    Object object;
    if (paramScriptable instanceof XMLObject) {
      object = ((XMLObject)paramScriptable).get(paramContext, paramObject);
    } else if (isSymbol(paramObject)) {
      object = ScriptableObject.getProperty((Scriptable)object, (Symbol)paramObject);
    } else {
      paramObject = toStringIdOrIndex(paramContext, paramObject);
      if (paramObject == null) {
        object = ScriptableObject.getProperty((Scriptable)object, lastIndexResult(paramContext));
      } else {
        object = ScriptableObject.getProperty((Scriptable)object, (String)paramObject);
      } 
    } 
    paramObject = object;
    if (object == Scriptable.NOT_FOUND)
      paramObject = Undefined.instance; 
    return paramObject;
  }
  
  @Deprecated
  public static Object getObjectElem(Object paramObject1, Object paramObject2, Context paramContext) {
    return getObjectElem(paramObject1, paramObject2, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object getObjectElem(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable) {
    paramScriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
    if (paramScriptable != null)
      return getObjectElem(paramScriptable, paramObject2, paramContext); 
    throw undefReadError(paramObject1, paramObject2);
  }
  
  public static Object getObjectIndex(Scriptable paramScriptable, int paramInt, Context paramContext) {
    Object object2 = ScriptableObject.getProperty(paramScriptable, paramInt);
    Object object1 = object2;
    if (object2 == Scriptable.NOT_FOUND)
      object1 = Undefined.instance; 
    return object1;
  }
  
  @Deprecated
  public static Object getObjectIndex(Object paramObject, double paramDouble, Context paramContext) {
    return getObjectIndex(paramObject, paramDouble, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object getObjectIndex(Object paramObject, double paramDouble, Context paramContext, Scriptable paramScriptable) {
    paramScriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (paramScriptable != null) {
      int i = (int)paramDouble;
      return (i == paramDouble) ? getObjectIndex(paramScriptable, i, paramContext) : getObjectProp(paramScriptable, toString(paramDouble), paramContext);
    } 
    throw undefReadError(paramObject, toString(paramDouble));
  }
  
  public static Object getObjectProp(Scriptable paramScriptable, String paramString, Context paramContext) {
    Object object2 = ScriptableObject.getProperty(paramScriptable, paramString);
    Object object1 = object2;
    if (object2 == Scriptable.NOT_FOUND) {
      if (paramContext.hasFeature(11))
        Context.reportWarning(getMessage1("msg.ref.undefined.prop", paramString)); 
      object1 = Undefined.instance;
    } 
    return object1;
  }
  
  @Deprecated
  public static Object getObjectProp(Object paramObject, String paramString, Context paramContext) {
    return getObjectProp(paramObject, paramString, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object getObjectProp(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    paramScriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (paramScriptable != null)
      return getObjectProp(paramScriptable, paramString, paramContext); 
    throw undefReadError(paramObject, paramString);
  }
  
  @Deprecated
  public static Object getObjectPropNoWarn(Object paramObject, String paramString, Context paramContext) {
    return getObjectPropNoWarn(paramObject, paramString, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object getObjectPropNoWarn(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    Scriptable scriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (scriptable != null) {
      paramObject = ScriptableObject.getProperty(scriptable, paramString);
      return (paramObject == Scriptable.NOT_FOUND) ? Undefined.instance : paramObject;
    } 
    throw undefReadError(paramObject, paramString);
  }
  
  @Deprecated
  public static Callable getPropFunctionAndThis(Object paramObject, String paramString, Context paramContext) {
    return getPropFunctionAndThis(paramObject, paramString, paramContext, getTopCallScope(paramContext));
  }
  
  public static Callable getPropFunctionAndThis(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    return getPropFunctionAndThisHelper(paramObject, paramString, paramContext, toObjectOrNull(paramContext, paramObject, paramScriptable));
  }
  
  private static Callable getPropFunctionAndThisHelper(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    if (paramScriptable != null) {
      Object object = ScriptableObject.getProperty(paramScriptable, paramString);
      paramObject = object;
      if (!(object instanceof Callable)) {
        Object object1 = ScriptableObject.getProperty(paramScriptable, "__noSuchMethod__");
        paramObject = object;
        if (object1 instanceof Callable)
          paramObject = new NoSuchMethodShim((Callable)object1, paramString); 
      } 
      if (paramObject instanceof Callable) {
        storeScriptable(paramContext, paramScriptable);
        return (Callable)paramObject;
      } 
      throw notFunctionError(paramScriptable, paramObject, paramString);
    } 
    throw undefCallError(paramObject, paramString);
  }
  
  public static RegExpProxy getRegExpProxy(Context paramContext) {
    return paramContext.getRegExpProxy();
  }
  
  public static Scriptable getTopCallScope(Context paramContext) {
    Scriptable scriptable = paramContext.topCallScope;
    if (scriptable != null)
      return scriptable; 
    throw new IllegalStateException();
  }
  
  public static Object getTopLevelProp(Scriptable paramScriptable, String paramString) {
    return ScriptableObject.getProperty(ScriptableObject.getTopLevelScope(paramScriptable), paramString);
  }
  
  static String[] getTopPackageNames() {
    String[] arrayOfString;
    if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
      arrayOfString = new String[] { "java", "javax", "org", "com", "edu", "net", "android" };
    } else {
      arrayOfString = new String[] { "java", "javax", "org", "com", "edu", "net" };
    } 
    return arrayOfString;
  }
  
  public static Callable getValueFunctionAndThis(Object paramObject, Context paramContext) {
    if (paramObject instanceof Callable) {
      Callable callable = (Callable)paramObject;
      Scriptable scriptable = null;
      if (callable instanceof Scriptable)
        scriptable = ((Scriptable)callable).getParentScope(); 
      paramObject = scriptable;
      if (scriptable == null)
        if (paramContext.topCallScope != null) {
          paramObject = paramContext.topCallScope;
        } else {
          throw new IllegalStateException();
        }  
      Object object = paramObject;
      if (paramObject.getParentScope() != null)
        if (paramObject instanceof NativeWith) {
          object = paramObject;
        } else {
          object = paramObject;
          if (paramObject instanceof NativeCall)
            object = ScriptableObject.getTopLevelScope((Scriptable)paramObject); 
        }  
      storeScriptable(paramContext, (Scriptable)object);
      return callable;
    } 
    throw notFunctionError(paramObject);
  }
  
  public static boolean hasObjectElem(Scriptable paramScriptable, Object paramObject, Context paramContext) {
    boolean bool;
    if (isSymbol(paramObject)) {
      bool = ScriptableObject.hasProperty(paramScriptable, (Symbol)paramObject);
    } else {
      paramObject = toStringIdOrIndex(paramContext, paramObject);
      if (paramObject == null) {
        bool = ScriptableObject.hasProperty(paramScriptable, lastIndexResult(paramContext));
      } else {
        bool = ScriptableObject.hasProperty(paramScriptable, (String)paramObject);
      } 
    } 
    return bool;
  }
  
  public static boolean hasTopCall(Context paramContext) {
    boolean bool;
    if (paramContext.topCallScope != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean in(Object paramObject1, Object paramObject2, Context paramContext) {
    if (paramObject2 instanceof Scriptable)
      return hasObjectElem((Scriptable)paramObject2, paramObject1, paramContext); 
    throw typeError0("msg.in.not.object");
  }
  
  public static long indexFromString(String paramString) {
    int j;
    int k;
    int i = paramString.length();
    if (i > 0) {
      int m = 0;
      int n = 0;
      char c = paramString.charAt(0);
      int i1 = m;
      j = n;
      k = c;
      if (c == '-') {
        i1 = m;
        j = n;
        k = c;
        if (i > 1) {
          k = paramString.charAt(1);
          if (k == 48)
            return -1L; 
          i1 = 1;
          j = 1;
        } 
      } 
      n = k - 48;
      if (n >= 0 && n <= 9) {
        if (j != 0) {
          k = 11;
        } else {
          k = 10;
        } 
        if (i <= k) {
          int i3 = -n;
          int i4 = 0;
          m = 0;
          int i5 = i1 + 1;
          int i6 = i5;
          int i2 = n;
          k = i3;
          if (i3 != 0) {
            i1 = i3;
            i2 = n;
            while (true) {
              i6 = i5;
              k = i1;
              i4 = m;
              if (i5 != i) {
                i3 = paramString.charAt(i5) - 48;
                n = i3;
                i6 = i5;
                i2 = n;
                k = i1;
                i4 = m;
                if (i3 >= 0) {
                  i6 = i5;
                  i2 = n;
                  k = i1;
                  i4 = m;
                  if (n <= 9) {
                    m = i1;
                    i1 = i1 * 10 - n;
                    i5++;
                    i2 = n;
                    continue;
                  } 
                } 
              } 
              break;
            } 
          } 
          if (i6 == i) {
            if (i4 <= -214748364) {
              if (i4 == -214748364) {
                if (j != 0) {
                  i1 = 8;
                } else {
                  i1 = 7;
                } 
                if (i2 <= i1) {
                  if (j == 0)
                    k = -k; 
                  return 0xFFFFFFFFL & k;
                } 
              } 
              return -1L;
            } 
          } else {
            return -1L;
          } 
        } else {
          return -1L;
        } 
      } else {
        return -1L;
      } 
    } else {
      return -1L;
    } 
    if (j == 0)
      k = -k; 
    return 0xFFFFFFFFL & k;
  }
  
  public static void initFunction(Context paramContext, Scriptable paramScriptable, NativeFunction paramNativeFunction, int paramInt, boolean paramBoolean) {
    if (paramInt == 1) {
      String str = paramNativeFunction.getFunctionName();
      if (str != null && str.length() != 0)
        if (!paramBoolean) {
          ScriptableObject.defineProperty(paramScriptable, str, paramNativeFunction, 4);
        } else {
          paramScriptable.put(str, paramScriptable, paramNativeFunction);
        }  
    } else {
      if (paramInt == 3) {
        String str = paramNativeFunction.getFunctionName();
        if (str != null && str.length() != 0) {
          while (paramScriptable instanceof NativeWith)
            paramScriptable = paramScriptable.getParentScope(); 
          paramScriptable.put(str, paramScriptable, paramNativeFunction);
        } 
        return;
      } 
      throw Kit.codeBug();
    } 
  }
  
  public static ScriptableObject initSafeStandardObjects(Context paramContext, ScriptableObject paramScriptableObject, boolean paramBoolean) {
    boolean bool;
    ScriptableObject scriptableObject = paramScriptableObject;
    if (paramScriptableObject == null)
      scriptableObject = new NativeObject(); 
    scriptableObject.associateValue(LIBRARY_SCOPE_KEY, scriptableObject);
    (new ClassCache()).associate(scriptableObject);
    BaseFunction.init(scriptableObject, paramBoolean);
    NativeObject.init(scriptableObject, paramBoolean);
    Scriptable scriptable = ScriptableObject.getObjectPrototype(scriptableObject);
    ScriptableObject.getClassPrototype(scriptableObject, "Function").setPrototype(scriptable);
    if (scriptableObject.getPrototype() == null)
      scriptableObject.setPrototype(scriptable); 
    NativeError.init(scriptableObject, paramBoolean);
    NativeGlobal.init(paramContext, scriptableObject, paramBoolean);
    NativeArray.init(scriptableObject, paramBoolean);
    if (paramContext.getOptimizationLevel() > 0)
      NativeArray.setMaximumInitialCapacity(200000); 
    NativeString.init(scriptableObject, paramBoolean);
    NativeBoolean.init(scriptableObject, paramBoolean);
    NativeNumber.init(scriptableObject, paramBoolean);
    NativeDate.init(scriptableObject, paramBoolean);
    NativeMath.init(scriptableObject, paramBoolean);
    NativeJSON.init(scriptableObject, paramBoolean);
    NativeWith.init(scriptableObject, paramBoolean);
    NativeCall.init(scriptableObject, paramBoolean);
    NativeScript.init(scriptableObject, paramBoolean);
    NativeIterator.init(scriptableObject, paramBoolean);
    NativeArrayIterator.init(scriptableObject, paramBoolean);
    NativeStringIterator.init(scriptableObject, paramBoolean);
    if (paramContext.hasFeature(6) && paramContext.getE4xImplementationFactory() != null) {
      bool = true;
    } else {
      bool = false;
    } 
    new LazilyLoadedCtor(scriptableObject, "RegExp", "com.trendmicro.hippo.regexp.NativeRegExp", paramBoolean, true);
    new LazilyLoadedCtor(scriptableObject, "Continuation", "com.trendmicro.hippo.NativeContinuation", paramBoolean, true);
    if (bool) {
      String str = paramContext.getE4xImplementationFactory().getImplementationClassName();
      new LazilyLoadedCtor(scriptableObject, "XML", str, paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "XMLList", str, paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Namespace", str, paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "QName", str, paramBoolean, true);
    } 
    if ((paramContext.getLanguageVersion() >= 180 && paramContext.hasFeature(14)) || paramContext.getLanguageVersion() >= 200) {
      new LazilyLoadedCtor(scriptableObject, "ArrayBuffer", "com.trendmicro.hippo.typedarrays.NativeArrayBuffer", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Int8Array", "com.trendmicro.hippo.typedarrays.NativeInt8Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Uint8Array", "com.trendmicro.hippo.typedarrays.NativeUint8Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Uint8ClampedArray", "com.trendmicro.hippo.typedarrays.NativeUint8ClampedArray", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Int16Array", "com.trendmicro.hippo.typedarrays.NativeInt16Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Uint16Array", "com.trendmicro.hippo.typedarrays.NativeUint16Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Int32Array", "com.trendmicro.hippo.typedarrays.NativeInt32Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Uint32Array", "com.trendmicro.hippo.typedarrays.NativeUint32Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Float32Array", "com.trendmicro.hippo.typedarrays.NativeFloat32Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "Float64Array", "com.trendmicro.hippo.typedarrays.NativeFloat64Array", paramBoolean, true);
      new LazilyLoadedCtor(scriptableObject, "DataView", "com.trendmicro.hippo.typedarrays.NativeDataView", paramBoolean, true);
    } 
    if (paramContext.getLanguageVersion() >= 200) {
      NativeSymbol.init(paramContext, scriptableObject, paramBoolean);
      NativeCollectionIterator.init(scriptableObject, "Set Iterator", paramBoolean);
      NativeCollectionIterator.init(scriptableObject, "Map Iterator", paramBoolean);
      NativeMap.init(paramContext, scriptableObject, paramBoolean);
      NativeSet.init(paramContext, scriptableObject, paramBoolean);
      NativeWeakMap.init(scriptableObject, paramBoolean);
      NativeWeakSet.init(scriptableObject, paramBoolean);
    } 
    if (scriptableObject instanceof TopLevel)
      ((TopLevel)scriptableObject).cacheBuiltins(); 
    return scriptableObject;
  }
  
  public static void initScript(NativeFunction paramNativeFunction, Scriptable paramScriptable1, Context paramContext, Scriptable paramScriptable2, boolean paramBoolean) {
    if (paramContext.topCallScope != null) {
      int i = paramNativeFunction.getParamAndVarCount();
      if (i != 0) {
        for (paramScriptable1 = paramScriptable2; paramScriptable1 instanceof NativeWith; paramScriptable1 = paramScriptable1.getParentScope());
        while (true) {
          int j = i - 1;
          if (i != 0) {
            String str = paramNativeFunction.getParamOrVarName(j);
            boolean bool = paramNativeFunction.getParamOrVarConst(j);
            if (!ScriptableObject.hasProperty(paramScriptable2, str)) {
              if (bool) {
                ScriptableObject.defineConstProperty(paramScriptable1, str);
              } else if (!paramBoolean) {
                ScriptableObject.defineProperty(paramScriptable1, str, Undefined.instance, 4);
              } else {
                paramScriptable1.put(str, paramScriptable1, Undefined.instance);
              } 
            } else {
              ScriptableObject.redefineProperty(paramScriptable2, str, bool);
            } 
            i = j;
            continue;
          } 
          break;
        } 
      } 
      return;
    } 
    throw new IllegalStateException();
  }
  
  public static ScriptableObject initStandardObjects(Context paramContext, ScriptableObject paramScriptableObject, boolean paramBoolean) {
    ScriptableObject scriptableObject = initSafeStandardObjects(paramContext, paramScriptableObject, paramBoolean);
    new LazilyLoadedCtor(scriptableObject, "Packages", "com.trendmicro.hippo.NativeJavaTopPackage", paramBoolean, true);
    new LazilyLoadedCtor(scriptableObject, "getClass", "com.trendmicro.hippo.NativeJavaTopPackage", paramBoolean, true);
    new LazilyLoadedCtor(scriptableObject, "JavaAdapter", "com.trendmicro.hippo.JavaAdapter", paramBoolean, true);
    new LazilyLoadedCtor(scriptableObject, "JavaImporter", "com.trendmicro.hippo.ImporterTopLevel", paramBoolean, true);
    String[] arrayOfString = getTopPackageNames();
    int i = arrayOfString.length;
    for (byte b = 0; b < i; b++)
      new LazilyLoadedCtor(scriptableObject, arrayOfString[b], "com.trendmicro.hippo.NativeJavaTopPackage", paramBoolean, true); 
    return scriptableObject;
  }
  
  public static boolean instanceOf(Object paramObject1, Object paramObject2, Context paramContext) {
    if (paramObject2 instanceof Scriptable)
      return !(paramObject1 instanceof Scriptable) ? false : ((Scriptable)paramObject2).hasInstance((Scriptable)paramObject1); 
    throw typeError0("msg.instanceof.not.object");
  }
  
  private static boolean isArrayLike(Scriptable paramScriptable) {
    boolean bool;
    if (paramScriptable != null && (paramScriptable instanceof NativeArray || paramScriptable instanceof Arguments || ScriptableObject.hasProperty(paramScriptable, "length"))) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static boolean isArrayObject(Object paramObject) {
    return (paramObject instanceof NativeArray || paramObject instanceof Arguments);
  }
  
  static boolean isGeneratedScript(String paramString) {
    return (paramString.indexOf("(eval)") >= 0 || paramString.indexOf("(Function)") >= 0);
  }
  
  public static boolean isHippoRuntimeType(Class<?> paramClass) {
    boolean bool = paramClass.isPrimitive();
    boolean bool1 = false;
    null = false;
    if (bool) {
      if (paramClass != char.class)
        null = true; 
      return null;
    } 
    if (paramClass != StringClass && paramClass != BooleanClass && !NumberClass.isAssignableFrom(paramClass)) {
      null = bool1;
      return ScriptableClass.isAssignableFrom(paramClass) ? true : null;
    } 
    return true;
  }
  
  public static boolean isJSLineTerminator(int paramInt) {
    boolean bool = false;
    if ((0xDFD0 & paramInt) != 0)
      return false; 
    if (paramInt == 10 || paramInt == 13 || paramInt == 8232 || paramInt == 8233)
      bool = true; 
    return bool;
  }
  
  public static boolean isJSWhitespaceOrLineTerminator(int paramInt) {
    return (isStrWhiteSpaceChar(paramInt) || isJSLineTerminator(paramInt));
  }
  
  public static boolean isNaN(Object paramObject) {
    Double double_ = NaNobj;
    boolean bool1 = true;
    boolean bool2 = true;
    if (paramObject == double_)
      return true; 
    if (paramObject instanceof Double) {
      paramObject = paramObject;
      boolean bool = bool2;
      if (paramObject.doubleValue() != NaN)
        if (Double.isNaN(paramObject.doubleValue())) {
          bool = bool2;
        } else {
          bool = false;
        }  
      return bool;
    } 
    if (paramObject instanceof Float) {
      paramObject = paramObject;
      boolean bool = bool1;
      if (paramObject.floatValue() != NaN)
        if (Float.isNaN(paramObject.floatValue())) {
          bool = bool1;
        } else {
          bool = false;
        }  
      return bool;
    } 
    return false;
  }
  
  public static boolean isObject(Object paramObject) {
    boolean bool = false;
    if (paramObject == null)
      return false; 
    if (Undefined.instance.equals(paramObject))
      return false; 
    if (paramObject instanceof ScriptableObject) {
      paramObject = ((ScriptableObject)paramObject).getTypeOf();
      if ("object".equals(paramObject) || "function".equals(paramObject))
        bool = true; 
      return bool;
    } 
    return (paramObject instanceof Scriptable) ? (paramObject instanceof Callable ^ true) : false;
  }
  
  public static boolean isPrimitive(Object paramObject) {
    return (paramObject == null || paramObject == Undefined.instance || paramObject instanceof Number || paramObject instanceof String || paramObject instanceof Boolean);
  }
  
  static boolean isSpecialProperty(String paramString) {
    return (paramString.equals("__proto__") || paramString.equals("__parent__"));
  }
  
  static boolean isStrWhiteSpaceChar(int paramInt) {
    boolean bool = true;
    if (paramInt != 32 && paramInt != 160 && paramInt != 65279 && paramInt != 8232 && paramInt != 8233)
      switch (paramInt) {
        default:
          if (Character.getType(paramInt) != 12)
            bool = false; 
          return bool;
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
          break;
      }  
    return true;
  }
  
  static boolean isSymbol(Object paramObject) {
    boolean bool;
    if ((paramObject instanceof NativeSymbol && ((NativeSymbol)paramObject).isSymbol()) || paramObject instanceof SymbolKey) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isValidIdentifierName(String paramString, Context paramContext, boolean paramBoolean) {
    int i = paramString.length();
    if (i == 0)
      return false; 
    if (!Character.isJavaIdentifierStart(paramString.charAt(0)))
      return false; 
    for (int j = 1; j != i; j++) {
      if (!Character.isJavaIdentifierPart(paramString.charAt(j)))
        return false; 
    } 
    return TokenStream.isKeyword(paramString, paramContext.getLanguageVersion(), paramBoolean) ^ true;
  }
  
  private static boolean isVisible(Context paramContext, Object paramObject) {
    ClassShutter classShutter = paramContext.getClassShutter();
    return (classShutter == null || classShutter.visibleToScripts(paramObject.getClass().getName()));
  }
  
  public static boolean jsDelegatesTo(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    for (paramScriptable1 = paramScriptable1.getPrototype(); paramScriptable1 != null; paramScriptable1 = paramScriptable1.getPrototype()) {
      if (paramScriptable1.equals(paramScriptable2))
        return true; 
    } 
    return false;
  }
  
  static int lastIndexResult(Context paramContext) {
    return paramContext.scratchIndex;
  }
  
  public static Scriptable lastStoredScriptable(Context paramContext) {
    Scriptable scriptable = paramContext.scratchScriptable;
    paramContext.scratchScriptable = null;
    return scriptable;
  }
  
  public static long lastUint32Result(Context paramContext) {
    long l = paramContext.scratchUint32;
    if (l >>> 32L == 0L)
      return l; 
    throw new IllegalStateException();
  }
  
  public static Scriptable leaveDotQuery(Scriptable paramScriptable) {
    return ((NativeWith)paramScriptable).getParentScope();
  }
  
  public static Scriptable leaveWith(Scriptable paramScriptable) {
    return ((NativeWith)paramScriptable).getParentScope();
  }
  
  static String makeUrlForGeneratedScript(boolean paramBoolean, String paramString, int paramInt) {
    if (paramBoolean) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramString);
      stringBuilder1.append('#');
      stringBuilder1.append(paramInt);
      stringBuilder1.append("(eval)");
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append('#');
    stringBuilder.append(paramInt);
    stringBuilder.append("(Function)");
    return stringBuilder.toString();
  }
  
  public static Ref memberRef(Object paramObject1, Object paramObject2, Context paramContext, int paramInt) {
    if (paramObject1 instanceof XMLObject)
      return ((XMLObject)paramObject1).memberRef(paramContext, paramObject2, paramInt); 
    throw notXmlError(paramObject1);
  }
  
  public static Ref memberRef(Object paramObject1, Object paramObject2, Object paramObject3, Context paramContext, int paramInt) {
    if (paramObject1 instanceof XMLObject)
      return ((XMLObject)paramObject1).memberRef(paramContext, paramObject2, paramObject3, paramInt); 
    throw notXmlError(paramObject1);
  }
  
  public static Object name(Context paramContext, Scriptable paramScriptable, String paramString) {
    Object object;
    Scriptable scriptable = paramScriptable.getParentScope();
    if (scriptable == null) {
      object = topScopeName(paramContext, paramScriptable, paramString);
      if (object != Scriptable.NOT_FOUND)
        return object; 
      throw notFoundError(paramScriptable, paramString);
    } 
    return nameOrFunction((Context)object, paramScriptable, scriptable, paramString, false);
  }
  
  @Deprecated
  public static Object nameIncrDecr(Scriptable paramScriptable, String paramString, int paramInt) {
    return nameIncrDecr(paramScriptable, paramString, Context.getContext(), paramInt);
  }
  
  public static Object nameIncrDecr(Scriptable paramScriptable, String paramString, Context paramContext, int paramInt) {
    Scriptable scriptable = paramScriptable;
    while (true) {
      paramScriptable = scriptable;
      if (paramContext.useDynamicScope) {
        paramScriptable = scriptable;
        if (scriptable.getParentScope() == null)
          paramScriptable = checkDynamicScope(paramContext.topCallScope, scriptable); 
      } 
      scriptable = paramScriptable;
      while (!(scriptable instanceof NativeWith) || !(scriptable.getPrototype() instanceof XMLObject)) {
        Object object2 = scriptable.get(paramString, paramScriptable);
        if (object2 != Scriptable.NOT_FOUND)
          return doScriptableIncrDecr(scriptable, paramString, paramScriptable, object2, paramInt); 
        object2 = scriptable.getPrototype();
        Object object1 = object2;
        if (object2 == null)
          break; 
      } 
      scriptable = paramScriptable.getParentScope();
      if (scriptable != null)
        continue; 
      throw notFoundError(scriptable, paramString);
    } 
  }
  
  private static Object nameOrFunction(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, String paramString, boolean paramBoolean) {
    Object object1;
    Object object2;
    Scriptable scriptable1 = paramScriptable1;
    Scriptable scriptable2 = null;
    Scriptable scriptable3 = paramScriptable2;
    paramScriptable2 = paramScriptable1;
    paramScriptable1 = scriptable2;
    while (true) {
      if (paramScriptable2 instanceof NativeWith) {
        XMLObject xMLObject;
        scriptable2 = paramScriptable2.getPrototype();
        if (scriptable2 instanceof XMLObject) {
          xMLObject = (XMLObject)scriptable2;
          if (xMLObject.has(paramString, (Scriptable)xMLObject)) {
            object1 = xMLObject;
            Object object = xMLObject.get(paramString, (Scriptable)xMLObject);
            break;
          } 
          object2 = object1;
          if (object1 == null)
            object2 = xMLObject; 
          object1 = object2;
        } else {
          object2 = ScriptableObject.getProperty((Scriptable)xMLObject, paramString);
          if (object2 != Scriptable.NOT_FOUND) {
            object1 = xMLObject;
            break;
          } 
        } 
      } else if (object2 instanceof NativeCall) {
        Object object = object2.get(paramString, (Scriptable)object2);
        if (object != Scriptable.NOT_FOUND) {
          paramScriptable1 = scriptable1;
          object2 = object;
          if (paramBoolean) {
            paramScriptable1 = ScriptableObject.getTopLevelScope(scriptable3);
            object2 = object;
          } 
          break;
        } 
      } else {
        Object object = ScriptableObject.getProperty((Scriptable)object2, paramString);
        if (object != Scriptable.NOT_FOUND) {
          object1 = object2;
          object2 = object;
          break;
        } 
      } 
      object2 = scriptable3;
      scriptable3 = scriptable3.getParentScope();
      if (scriptable3 == null) {
        Object object = topScopeName(paramContext, (Scriptable)object2, paramString);
        if (object == Scriptable.NOT_FOUND) {
          if (object1 != null && !paramBoolean) {
            object1 = object1.get(paramString, (Scriptable)object1);
          } else {
            throw notFoundError(object2, paramString);
          } 
        } else {
          object1 = object;
        } 
        object = object2;
        object2 = object1;
        object1 = object;
        break;
      } 
    } 
    if (paramBoolean)
      if (object2 instanceof Callable) {
        storeScriptable(paramContext, (Scriptable)object1);
      } else {
        throw notFunctionError(object2, paramString);
      }  
    return object2;
  }
  
  public static Ref nameRef(Object paramObject, Context paramContext, Scriptable paramScriptable, int paramInt) {
    return currentXMLLib(paramContext).nameRef(paramContext, paramObject, paramScriptable, paramInt);
  }
  
  public static Ref nameRef(Object paramObject1, Object paramObject2, Context paramContext, Scriptable paramScriptable, int paramInt) {
    return currentXMLLib(paramContext).nameRef(paramContext, paramObject1, paramObject2, paramScriptable, paramInt);
  }
  
  public static Scriptable newArrayLiteral(Object[] paramArrayOfObject, int[] paramArrayOfint, Context paramContext, Scriptable paramScriptable) {
    int i = paramArrayOfObject.length;
    int j = 0;
    if (paramArrayOfint != null)
      j = paramArrayOfint.length; 
    int k = i + j;
    if (k > 1 && j * 2 < k) {
      Object[] arrayOfObject;
      if (j == 0) {
        arrayOfObject = paramArrayOfObject;
      } else {
        Object[] arrayOfObject1 = new Object[k];
        int n = 0;
        i = 0;
        byte b1 = 0;
        while (true) {
          arrayOfObject = arrayOfObject1;
          if (i != k) {
            if (n != j && paramArrayOfint[n] == i) {
              arrayOfObject1[i] = Scriptable.NOT_FOUND;
              n++;
            } else {
              arrayOfObject1[i] = paramArrayOfObject[b1];
              b1++;
            } 
            i++;
            continue;
          } 
          break;
        } 
      } 
      return paramContext.newArray(paramScriptable, arrayOfObject);
    } 
    Scriptable scriptable = paramContext.newArray(paramScriptable, k);
    int m = 0;
    i = 0;
    byte b = 0;
    while (i != k) {
      if (m != j && paramArrayOfint[m] == i) {
        m++;
      } else {
        scriptable.put(i, scriptable, paramArrayOfObject[b]);
        b++;
      } 
      i++;
    } 
    return scriptable;
  }
  
  public static Scriptable newBuiltinObject(Context paramContext, Scriptable paramScriptable, TopLevel.Builtins paramBuiltins, Object[] paramArrayOfObject) {
    Scriptable scriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    Function function = TopLevel.getBuiltinCtor(paramContext, scriptable, paramBuiltins);
    Object[] arrayOfObject = paramArrayOfObject;
    if (paramArrayOfObject == null)
      arrayOfObject = emptyArgs; 
    return function.construct(paramContext, scriptable, arrayOfObject);
  }
  
  public static Scriptable newCatchScope(Throwable paramThrowable, Scriptable paramScriptable1, String paramString, Context paramContext, Scriptable paramScriptable2) {
    Object object;
    boolean bool;
    if (paramThrowable instanceof JavaScriptException) {
      bool = false;
      object = ((JavaScriptException)paramThrowable).getValue();
    } else {
      bool = true;
      if (object != null) {
        object = ((NativeObject)object).getAssociatedValue(paramThrowable);
        if (object == null)
          Kit.codeBug(); 
      } else {
        String str1;
        Object[] arrayOfObject;
        TopLevel.NativeErrors nativeErrors;
        Throwable throwable = null;
        if (paramThrowable instanceof EcmaError) {
          EcmaError ecmaError = (EcmaError)paramThrowable;
          object = ecmaError;
          nativeErrors = TopLevel.NativeErrors.valueOf(ecmaError.getName());
          str1 = ecmaError.getErrorMessage();
        } else if (paramThrowable instanceof WrappedException) {
          WrappedException wrappedException = (WrappedException)paramThrowable;
          object = wrappedException;
          throwable = wrappedException.getWrappedException();
          nativeErrors = TopLevel.NativeErrors.JavaException;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(throwable.getClass().getName());
          stringBuilder.append(": ");
          stringBuilder.append(throwable.getMessage());
          str1 = stringBuilder.toString();
        } else if (paramThrowable instanceof EvaluatorException) {
          EvaluatorException evaluatorException = (EvaluatorException)paramThrowable;
          object = evaluatorException;
          nativeErrors = TopLevel.NativeErrors.InternalError;
          str1 = evaluatorException.getMessage();
        } else if (paramContext.hasFeature(13)) {
          object = new WrappedException(paramThrowable);
          nativeErrors = TopLevel.NativeErrors.JavaException;
          str1 = paramThrowable.toString();
        } else {
          throw Kit.codeBug();
        } 
        String str2 = object.sourceName();
        String str3 = str2;
        if (str2 == null)
          str3 = ""; 
        int i = object.lineNumber();
        if (i > 0) {
          arrayOfObject = new Object[] { str1, str3, Integer.valueOf(i) };
        } else {
          arrayOfObject = new Object[] { arrayOfObject, str3 };
        } 
        Scriptable scriptable = newNativeError(paramContext, paramScriptable2, nativeErrors, arrayOfObject);
        if (scriptable instanceof NativeError)
          ((NativeError)scriptable).setStackProvider((HippoException)object); 
        if (throwable != null && isVisible(paramContext, throwable))
          ScriptableObject.defineProperty(scriptable, "javaException", paramContext.getWrapFactory().wrap(paramContext, paramScriptable2, throwable, null), 7); 
        if (isVisible(paramContext, object))
          ScriptableObject.defineProperty(scriptable, "hippoException", paramContext.getWrapFactory().wrap(paramContext, paramScriptable2, object, null), 7); 
        object = scriptable;
      } 
    } 
    NativeObject nativeObject = new NativeObject();
    nativeObject.defineProperty(paramString, object, 4);
    if (isVisible(paramContext, paramThrowable))
      nativeObject.defineProperty("__exception__", Context.javaToJS(paramThrowable, paramScriptable2), 6); 
    if (bool)
      nativeObject.associateValue(paramThrowable, object); 
    return nativeObject;
  }
  
  static Scriptable newNativeError(Context paramContext, Scriptable paramScriptable, TopLevel.NativeErrors paramNativeErrors, Object[] paramArrayOfObject) {
    Scriptable scriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    Function function = TopLevel.getNativeErrorCtor(paramContext, scriptable, paramNativeErrors);
    Object[] arrayOfObject = paramArrayOfObject;
    if (paramArrayOfObject == null)
      arrayOfObject = emptyArgs; 
    return function.construct(paramContext, scriptable, arrayOfObject);
  }
  
  public static Scriptable newObject(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject) {
    Scriptable scriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    Function function = getExistingCtor(paramContext, scriptable, paramString);
    Object[] arrayOfObject = paramArrayOfObject;
    if (paramArrayOfObject == null)
      arrayOfObject = emptyArgs; 
    return function.construct(paramContext, scriptable, arrayOfObject);
  }
  
  public static Scriptable newObject(Object paramObject, Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    if (paramObject instanceof Function)
      return ((Function)paramObject).construct(paramContext, paramScriptable, paramArrayOfObject); 
    throw notFunctionError(paramObject);
  }
  
  @Deprecated
  public static Scriptable newObjectLiteral(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, Context paramContext, Scriptable paramScriptable) {
    return newObjectLiteral(paramArrayOfObject1, paramArrayOfObject2, null, paramContext, paramScriptable);
  }
  
  public static Scriptable newObjectLiteral(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int[] paramArrayOfint, Context paramContext, Scriptable paramScriptable) {
    Scriptable scriptable = paramContext.newObject(paramScriptable);
    int i = 0;
    int j = paramArrayOfObject1.length;
    while (i != j) {
      int k;
      Object object1 = paramArrayOfObject1[i];
      if (paramArrayOfint == null) {
        k = 0;
      } else {
        k = paramArrayOfint[i];
      } 
      Object object2 = paramArrayOfObject2[i];
      if (object1 instanceof String) {
        if (k == 0) {
          if (isSpecialProperty((String)object1)) {
            specialRef(scriptable, (String)object1, paramContext, paramScriptable).set(paramContext, paramScriptable, object2);
          } else {
            scriptable.put((String)object1, scriptable, object2);
          } 
        } else {
          ScriptableObject scriptableObject = (ScriptableObject)scriptable;
          object2 = object2;
          boolean bool = true;
          if (k != 1)
            bool = false; 
          scriptableObject.setGetterOrSetter((String)object1, 0, (Callable)object2, bool);
        } 
      } else {
        scriptable.put(((Integer)object1).intValue(), scriptable, object2);
      } 
      i++;
    } 
    return scriptable;
  }
  
  public static Object newSpecial(Context paramContext, Object paramObject, Object[] paramArrayOfObject, Scriptable paramScriptable, int paramInt) {
    if (paramInt == 1) {
      if (NativeGlobal.isEvalFunction(paramObject))
        throw typeError1("msg.not.ctor", "eval"); 
    } else {
      if (paramInt == 2)
        return NativeWith.isWithFunction(paramObject) ? NativeWith.newWithSpecial(paramContext, paramScriptable, paramArrayOfObject) : newObject(paramObject, paramContext, paramScriptable, paramArrayOfObject); 
      throw Kit.codeBug();
    } 
    return newObject(paramObject, paramContext, paramScriptable, paramArrayOfObject);
  }
  
  public static RuntimeException notFoundError(Scriptable paramScriptable, String paramString) {
    throw constructError("ReferenceError", getMessage1("msg.is.not.defined", paramString));
  }
  
  public static RuntimeException notFunctionError(Object paramObject) {
    return notFunctionError(paramObject, paramObject);
  }
  
  public static RuntimeException notFunctionError(Object paramObject1, Object paramObject2) {
    if (paramObject2 == null) {
      paramObject2 = "null";
    } else {
      paramObject2 = paramObject2.toString();
    } 
    return (paramObject1 == Scriptable.NOT_FOUND) ? typeError1("msg.function.not.found", paramObject2) : typeError2("msg.isnt.function", paramObject2, typeof(paramObject1));
  }
  
  public static RuntimeException notFunctionError(Object paramObject1, Object paramObject2, String paramString) {
    String str1 = toString(paramObject1);
    String str2 = str1;
    if (paramObject1 instanceof NativeFunction) {
      int i = str1.indexOf('{', str1.indexOf(')'));
      str2 = str1;
      if (i > -1) {
        paramObject1 = new StringBuilder();
        paramObject1.append(str1.substring(0, i + 1));
        paramObject1.append("...}");
        str2 = paramObject1.toString();
      } 
    } 
    return (paramObject2 == Scriptable.NOT_FOUND) ? typeError2("msg.function.not.found.in", paramString, str2) : typeError3("msg.isnt.function.in", paramString, str2, typeof(paramObject2));
  }
  
  private static RuntimeException notXmlError(Object paramObject) {
    throw typeError1("msg.isnt.xml.object", toString(paramObject));
  }
  
  public static String numberToString(double paramDouble, int paramInt) {
    if (paramInt >= 2 && paramInt <= 36) {
      if (paramDouble != paramDouble)
        return "NaN"; 
      if (paramDouble == Double.POSITIVE_INFINITY)
        return "Infinity"; 
      if (paramDouble == Double.NEGATIVE_INFINITY)
        return "-Infinity"; 
      if (paramDouble == 0.0D)
        return "0"; 
      if (paramInt != 10)
        return DToA.JS_dtobasestr(paramInt, paramDouble); 
      String str = FastDtoa.numberToString(paramDouble);
      if (str != null)
        return str; 
      StringBuilder stringBuilder = new StringBuilder();
      DToA.JS_dtostr(stringBuilder, 0, 0, paramDouble);
      return stringBuilder.toString();
    } 
    throw Context.reportRuntimeError1("msg.bad.radix", Integer.toString(paramInt));
  }
  
  public static Object[] padArguments(Object[] paramArrayOfObject, int paramInt) {
    byte b2;
    if (paramInt < paramArrayOfObject.length)
      return paramArrayOfObject; 
    Object[] arrayOfObject = new Object[paramInt];
    byte b1 = 0;
    while (true) {
      b2 = b1;
      if (b1 < paramArrayOfObject.length) {
        arrayOfObject[b1] = paramArrayOfObject[b1];
        b1++;
        continue;
      } 
      break;
    } 
    while (b2 < paramInt) {
      arrayOfObject[b2] = Undefined.instance;
      b2++;
    } 
    return arrayOfObject;
  }
  
  @Deprecated
  public static Object propIncrDecr(Object paramObject, String paramString, Context paramContext, int paramInt) {
    return propIncrDecr(paramObject, paramString, paramContext, getTopCallScope(paramContext), paramInt);
  }
  
  public static Object propIncrDecr(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable, int paramInt) {
    Scriptable scriptable = toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (scriptable != null) {
      paramObject = scriptable;
      while (true) {
        Object object = paramObject.get(paramString, scriptable);
        if (object != Scriptable.NOT_FOUND)
          return doScriptableIncrDecr((Scriptable)paramObject, paramString, scriptable, object, paramInt); 
        object = paramObject.getPrototype();
        paramObject = object;
        if (object == null) {
          scriptable.put(paramString, scriptable, NaNobj);
          return NaNobj;
        } 
      } 
    } 
    throw undefReadError(paramObject, paramString);
  }
  
  public static EcmaError rangeError(String paramString) {
    return constructError("RangeError", paramString);
  }
  
  public static Object refDel(Ref paramRef, Context paramContext) {
    return wrapBoolean(paramRef.delete(paramContext));
  }
  
  public static Object refGet(Ref paramRef, Context paramContext) {
    return paramRef.get(paramContext);
  }
  
  @Deprecated
  public static Object refIncrDecr(Ref paramRef, Context paramContext, int paramInt) {
    return refIncrDecr(paramRef, paramContext, getTopCallScope(paramContext), paramInt);
  }
  
  public static Object refIncrDecr(Ref paramRef, Context paramContext, Scriptable paramScriptable, int paramInt) {
    boolean bool;
    double d;
    Object object = paramRef.get(paramContext);
    if ((paramInt & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    if (object instanceof Number) {
      d = ((Number)object).doubleValue();
    } else {
      double d1 = toNumber(object);
      d = d1;
      if (bool) {
        object = wrapNumber(d1);
        d = d1;
      } 
    } 
    if ((paramInt & 0x1) == 0) {
      d++;
    } else {
      d--;
    } 
    Number number = wrapNumber(d);
    paramRef.set(paramContext, paramScriptable, number);
    return bool ? object : number;
  }
  
  @Deprecated
  public static Object refSet(Ref paramRef, Object paramObject, Context paramContext) {
    return refSet(paramRef, paramObject, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object refSet(Ref paramRef, Object paramObject, Context paramContext, Scriptable paramScriptable) {
    return paramRef.set(paramContext, paramScriptable, paramObject);
  }
  
  public static boolean same(Object paramObject1, Object paramObject2) {
    return !typeof(paramObject1).equals(typeof(paramObject2)) ? false : ((paramObject1 instanceof Number) ? ((isNaN(paramObject1) && isNaN(paramObject2)) ? true : paramObject1.equals(paramObject2)) : eq(paramObject1, paramObject2));
  }
  
  public static boolean sameZero(Object paramObject1, Object paramObject2) {
    if (!typeof(paramObject1).equals(typeof(paramObject2)))
      return false; 
    if (paramObject1 instanceof Number) {
      if (isNaN(paramObject1) && isNaN(paramObject2))
        return true; 
      double d = ((Number)paramObject1).doubleValue();
      if (paramObject2 instanceof Number) {
        double d1 = ((Number)paramObject2).doubleValue();
        if ((d == negativeZero && d1 == 0.0D) || (d == 0.0D && d1 == negativeZero))
          return true; 
      } 
      return eqNumber(d, paramObject2);
    } 
    return eq(paramObject1, paramObject2);
  }
  
  public static Object searchDefaultNamespace(Context paramContext) {
    NativeCall nativeCall = paramContext.currentActivationCall;
    Scriptable scriptable = nativeCall;
    if (nativeCall == null)
      scriptable = getTopCallScope(paramContext); 
    while (true) {
      Object object;
      Scriptable scriptable1 = scriptable.getParentScope();
      if (scriptable1 == null) {
        object = ScriptableObject.getProperty(scriptable, "__default_namespace__");
        Object object1 = object;
        if (object == Scriptable.NOT_FOUND)
          return null; 
      } else {
        object = object.get("__default_namespace__", (Scriptable)object);
        if (object != Scriptable.NOT_FOUND)
          return object; 
        object = scriptable1;
        continue;
      } 
      return scriptable1;
    } 
  }
  
  public static void setBuiltinProtoAndParent(ScriptableObject paramScriptableObject, Scriptable paramScriptable, TopLevel.Builtins paramBuiltins) {
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    paramScriptableObject.setParentScope(paramScriptable);
    paramScriptableObject.setPrototype(TopLevel.getBuiltinPrototype(paramScriptable, paramBuiltins));
  }
  
  public static Object setConst(Scriptable paramScriptable, Object paramObject, Context paramContext, String paramString) {
    if (paramScriptable instanceof XMLObject) {
      paramScriptable.put(paramString, paramScriptable, paramObject);
    } else {
      ScriptableObject.putConstProperty(paramScriptable, paramString, paramObject);
    } 
    return paramObject;
  }
  
  public static Object setDefaultNamespace(Object paramObject, Context paramContext) {
    NativeCall nativeCall = paramContext.currentActivationCall;
    Scriptable scriptable = nativeCall;
    if (nativeCall == null)
      scriptable = getTopCallScope(paramContext); 
    paramObject = currentXMLLib(paramContext).toDefaultXmlNamespace(paramContext, paramObject);
    if (!scriptable.has("__default_namespace__", scriptable)) {
      ScriptableObject.defineProperty(scriptable, "__default_namespace__", paramObject, 6);
    } else {
      scriptable.put("__default_namespace__", scriptable, paramObject);
    } 
    return Undefined.instance;
  }
  
  public static void setEnumNumbers(Object paramObject, boolean paramBoolean) {
    ((IdEnumeration)paramObject).enumNumbers = paramBoolean;
  }
  
  public static void setFunctionProtoAndParent(BaseFunction paramBaseFunction, Scriptable paramScriptable) {
    paramBaseFunction.setParentScope(paramScriptable);
    paramBaseFunction.setPrototype(ScriptableObject.getFunctionPrototype(paramScriptable));
  }
  
  public static Object setName(Scriptable paramScriptable1, Object paramObject, Context paramContext, Scriptable paramScriptable2, String paramString) {
    if (paramScriptable1 != null) {
      ScriptableObject.putProperty(paramScriptable1, paramString, paramObject);
    } else {
      if (paramContext.hasFeature(11) || paramContext.hasFeature(8))
        Context.reportWarning(getMessage1("msg.assn.create.strict", paramString)); 
      paramScriptable2 = ScriptableObject.getTopLevelScope(paramScriptable2);
      paramScriptable1 = paramScriptable2;
      if (paramContext.useDynamicScope)
        paramScriptable1 = checkDynamicScope(paramContext.topCallScope, paramScriptable2); 
      paramScriptable1.put(paramString, paramScriptable1, paramObject);
    } 
    return paramObject;
  }
  
  public static Object setObjectElem(Scriptable paramScriptable, Object paramObject1, Object paramObject2, Context paramContext) {
    if (paramScriptable instanceof XMLObject) {
      ((XMLObject)paramScriptable).put(paramContext, paramObject1, paramObject2);
    } else if (isSymbol(paramObject1)) {
      ScriptableObject.putProperty(paramScriptable, (Symbol)paramObject1, paramObject2);
    } else {
      paramObject1 = toStringIdOrIndex(paramContext, paramObject1);
      if (paramObject1 == null) {
        ScriptableObject.putProperty(paramScriptable, lastIndexResult(paramContext), paramObject2);
      } else {
        ScriptableObject.putProperty(paramScriptable, (String)paramObject1, paramObject2);
      } 
    } 
    return paramObject2;
  }
  
  @Deprecated
  public static Object setObjectElem(Object paramObject1, Object paramObject2, Object paramObject3, Context paramContext) {
    return setObjectElem(paramObject1, paramObject2, paramObject3, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object setObjectElem(Object paramObject1, Object paramObject2, Object paramObject3, Context paramContext, Scriptable paramScriptable) {
    paramScriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
    if (paramScriptable != null)
      return setObjectElem(paramScriptable, paramObject2, paramObject3, paramContext); 
    throw undefWriteError(paramObject1, paramObject2, paramObject3);
  }
  
  public static Object setObjectIndex(Scriptable paramScriptable, int paramInt, Object paramObject, Context paramContext) {
    ScriptableObject.putProperty(paramScriptable, paramInt, paramObject);
    return paramObject;
  }
  
  @Deprecated
  public static Object setObjectIndex(Object paramObject1, double paramDouble, Object paramObject2, Context paramContext) {
    return setObjectIndex(paramObject1, paramDouble, paramObject2, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object setObjectIndex(Object paramObject1, double paramDouble, Object paramObject2, Context paramContext, Scriptable paramScriptable) {
    paramScriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
    if (paramScriptable != null) {
      int i = (int)paramDouble;
      return (i == paramDouble) ? setObjectIndex(paramScriptable, i, paramObject2, paramContext) : setObjectProp(paramScriptable, toString(paramDouble), paramObject2, paramContext);
    } 
    throw undefWriteError(paramObject1, String.valueOf(paramDouble), paramObject2);
  }
  
  public static Object setObjectProp(Scriptable paramScriptable, String paramString, Object paramObject, Context paramContext) {
    ScriptableObject.putProperty(paramScriptable, paramString, paramObject);
    return paramObject;
  }
  
  @Deprecated
  public static Object setObjectProp(Object paramObject1, String paramString, Object paramObject2, Context paramContext) {
    return setObjectProp(paramObject1, paramString, paramObject2, paramContext, getTopCallScope(paramContext));
  }
  
  public static Object setObjectProp(Object paramObject1, String paramString, Object paramObject2, Context paramContext, Scriptable paramScriptable) {
    paramScriptable = toObjectOrNull(paramContext, paramObject1, paramScriptable);
    if (paramScriptable != null)
      return setObjectProp(paramScriptable, paramString, paramObject2, paramContext); 
    throw undefWriteError(paramObject1, paramString, paramObject2);
  }
  
  public static void setObjectProtoAndParent(ScriptableObject paramScriptableObject, Scriptable paramScriptable) {
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    paramScriptableObject.setParentScope(paramScriptable);
    paramScriptableObject.setPrototype(ScriptableObject.getClassPrototype(paramScriptable, paramScriptableObject.getClassName()));
  }
  
  public static void setRegExpProxy(Context paramContext, RegExpProxy paramRegExpProxy) {
    if (paramRegExpProxy != null) {
      paramContext.regExpProxy = paramRegExpProxy;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public static boolean shallowEq(Object paramObject1, Object paramObject2) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    if (paramObject1 == paramObject2) {
      if (!(paramObject1 instanceof Number))
        return true; 
      double d = ((Number)paramObject1).doubleValue();
      if (d == d)
        bool4 = true; 
      return bool4;
    } 
    if (paramObject1 == null || paramObject1 == Undefined.instance || paramObject1 == Undefined.SCRIPTABLE_UNDEFINED)
      return ((paramObject1 == Undefined.instance && paramObject2 == Undefined.SCRIPTABLE_UNDEFINED) || (paramObject1 == Undefined.SCRIPTABLE_UNDEFINED && paramObject2 == Undefined.instance)); 
    if (paramObject1 instanceof Number) {
      if (paramObject2 instanceof Number) {
        bool4 = bool1;
        if (((Number)paramObject1).doubleValue() == ((Number)paramObject2).doubleValue())
          bool4 = true; 
        return bool4;
      } 
    } else if (paramObject1 instanceof CharSequence) {
      if (paramObject2 instanceof CharSequence)
        return paramObject1.toString().equals(paramObject2.toString()); 
    } else if (paramObject1 instanceof Boolean) {
      if (paramObject2 instanceof Boolean)
        return paramObject1.equals(paramObject2); 
    } else {
      if (paramObject1 instanceof Scriptable) {
        if (paramObject1 instanceof Wrapper && paramObject2 instanceof Wrapper) {
          bool4 = bool2;
          if (((Wrapper)paramObject1).unwrap() == ((Wrapper)paramObject2).unwrap())
            bool4 = true; 
          return bool4;
        } 
        return false;
      } 
      warnAboutNonJSObject(paramObject1);
      bool4 = bool3;
      if (paramObject1 == paramObject2)
        bool4 = true; 
      return bool4;
    } 
    return false;
  }
  
  @Deprecated
  public static Ref specialRef(Object paramObject, String paramString, Context paramContext) {
    return specialRef(paramObject, paramString, paramContext, getTopCallScope(paramContext));
  }
  
  public static Ref specialRef(Object paramObject, String paramString, Context paramContext, Scriptable paramScriptable) {
    return SpecialRef.createSpecial(paramContext, paramScriptable, paramObject, paramString);
  }
  
  private static void storeIndexResult(Context paramContext, int paramInt) {
    paramContext.scratchIndex = paramInt;
  }
  
  private static void storeScriptable(Context paramContext, Scriptable paramScriptable) {
    if (paramContext.scratchScriptable == null) {
      paramContext.scratchScriptable = paramScriptable;
      return;
    } 
    throw new IllegalStateException();
  }
  
  public static void storeUint32Result(Context paramContext, long paramLong) {
    if (paramLong >>> 32L == 0L) {
      paramContext.scratchUint32 = paramLong;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public static Object strictSetName(Scriptable paramScriptable1, Object paramObject, Context paramContext, Scriptable paramScriptable2, String paramString) {
    if (paramScriptable1 != null) {
      ScriptableObject.putProperty(paramScriptable1, paramString, paramObject);
      return paramObject;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Assignment to undefined \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\" in strict mode");
    throw constructError("ReferenceError", stringBuilder.toString());
  }
  
  static double stringPrefixToNumber(String paramString, int paramInt1, int paramInt2) {
    return stringToNumber(paramString, paramInt1, paramString.length() - 1, paramInt2, true);
  }
  
  static double stringToNumber(String paramString, int paramInt1, int paramInt2, int paramInt3) {
    return stringToNumber(paramString, paramInt1, paramInt2, paramInt3, false);
  }
  
  private static double stringToNumber(String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: bipush #97
    //   2: istore #5
    //   4: bipush #65
    //   6: istore #6
    //   8: iload_3
    //   9: bipush #10
    //   11: if_icmpge -> 26
    //   14: iload_3
    //   15: bipush #48
    //   17: iadd
    //   18: iconst_1
    //   19: isub
    //   20: i2c
    //   21: istore #7
    //   23: goto -> 30
    //   26: bipush #57
    //   28: istore #7
    //   30: iload_3
    //   31: bipush #10
    //   33: if_icmple -> 56
    //   36: iload_3
    //   37: bipush #97
    //   39: iadd
    //   40: bipush #10
    //   42: isub
    //   43: i2c
    //   44: istore #5
    //   46: iload_3
    //   47: bipush #65
    //   49: iadd
    //   50: bipush #10
    //   52: isub
    //   53: i2c
    //   54: istore #6
    //   56: dconst_0
    //   57: dstore #8
    //   59: iload_1
    //   60: istore #10
    //   62: iload #5
    //   64: istore #11
    //   66: iload #10
    //   68: iload_2
    //   69: if_icmpgt -> 177
    //   72: aload_0
    //   73: iload #10
    //   75: invokevirtual charAt : (I)C
    //   78: istore #5
    //   80: bipush #48
    //   82: iload #5
    //   84: if_icmpgt -> 100
    //   87: iload #5
    //   89: iload #7
    //   91: if_icmpgt -> 100
    //   94: iinc #5, -48
    //   97: goto -> 151
    //   100: bipush #97
    //   102: iload #5
    //   104: if_icmpgt -> 127
    //   107: iload #5
    //   109: iload #11
    //   111: if_icmpge -> 127
    //   114: iload #5
    //   116: bipush #97
    //   118: isub
    //   119: bipush #10
    //   121: iadd
    //   122: istore #5
    //   124: goto -> 151
    //   127: bipush #65
    //   129: iload #5
    //   131: if_icmpgt -> 168
    //   134: iload #5
    //   136: iload #6
    //   138: if_icmpge -> 168
    //   141: iload #5
    //   143: bipush #65
    //   145: isub
    //   146: bipush #10
    //   148: iadd
    //   149: istore #5
    //   151: iload_3
    //   152: i2d
    //   153: dload #8
    //   155: dmul
    //   156: iload #5
    //   158: i2d
    //   159: dadd
    //   160: dstore #8
    //   162: iinc #10, 1
    //   165: goto -> 66
    //   168: iload #4
    //   170: ifne -> 177
    //   173: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   176: dreturn
    //   177: iload_1
    //   178: iload #10
    //   180: if_icmpne -> 187
    //   183: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   186: dreturn
    //   187: dload #8
    //   189: dstore #12
    //   191: dload #8
    //   193: ldc2_w 9.007199254740991E15
    //   196: dcmpl
    //   197: ifle -> 709
    //   200: iload_3
    //   201: bipush #10
    //   203: if_icmpne -> 226
    //   206: aload_0
    //   207: iload_1
    //   208: iload #10
    //   210: invokevirtual substring : (II)Ljava/lang/String;
    //   213: invokestatic parseDouble : (Ljava/lang/String;)D
    //   216: dstore #12
    //   218: dload #12
    //   220: dreturn
    //   221: astore_0
    //   222: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   225: dreturn
    //   226: iload_3
    //   227: iconst_2
    //   228: if_icmpeq -> 258
    //   231: iload_3
    //   232: iconst_4
    //   233: if_icmpeq -> 258
    //   236: iload_3
    //   237: bipush #8
    //   239: if_icmpeq -> 258
    //   242: iload_3
    //   243: bipush #16
    //   245: if_icmpeq -> 258
    //   248: dload #8
    //   250: dstore #12
    //   252: iload_3
    //   253: bipush #32
    //   255: if_icmpne -> 709
    //   258: iconst_1
    //   259: istore_2
    //   260: iconst_0
    //   261: istore #14
    //   263: bipush #53
    //   265: istore #15
    //   267: dconst_0
    //   268: dstore #16
    //   270: iconst_0
    //   271: istore #11
    //   273: iconst_0
    //   274: istore #7
    //   276: iconst_0
    //   277: istore #5
    //   279: dload #8
    //   281: dstore #12
    //   283: iload_1
    //   284: istore #18
    //   286: iload_2
    //   287: iconst_1
    //   288: if_icmpne -> 434
    //   291: iload #18
    //   293: iload #10
    //   295: if_icmpne -> 377
    //   298: iload #5
    //   300: ifeq -> 371
    //   303: iload #5
    //   305: iconst_3
    //   306: if_icmpeq -> 343
    //   309: iload #5
    //   311: iconst_4
    //   312: if_icmpeq -> 318
    //   315: goto -> 709
    //   318: dload #12
    //   320: dstore #8
    //   322: iload #7
    //   324: ifeq -> 333
    //   327: dload #12
    //   329: dconst_1
    //   330: dadd
    //   331: dstore #8
    //   333: dload #8
    //   335: dload #16
    //   337: dmul
    //   338: dstore #12
    //   340: goto -> 709
    //   343: dload #12
    //   345: dstore #8
    //   347: iload #7
    //   349: iload #11
    //   351: iand
    //   352: ifeq -> 361
    //   355: dload #12
    //   357: dconst_1
    //   358: dadd
    //   359: dstore #8
    //   361: dload #8
    //   363: dload #16
    //   365: dmul
    //   366: dstore #12
    //   368: goto -> 709
    //   371: dconst_0
    //   372: dstore #12
    //   374: goto -> 709
    //   377: aload_0
    //   378: iload #18
    //   380: invokevirtual charAt : (I)C
    //   383: istore_1
    //   384: bipush #48
    //   386: iload_1
    //   387: if_icmpgt -> 402
    //   390: iload_1
    //   391: bipush #57
    //   393: if_icmpgt -> 402
    //   396: iinc #1, -48
    //   399: goto -> 423
    //   402: bipush #97
    //   404: iload_1
    //   405: if_icmpgt -> 420
    //   408: iload_1
    //   409: bipush #122
    //   411: if_icmpgt -> 420
    //   414: iinc #1, -87
    //   417: goto -> 423
    //   420: iinc #1, -55
    //   423: iload_3
    //   424: istore_2
    //   425: iinc #18, 1
    //   428: iload_1
    //   429: istore #14
    //   431: goto -> 434
    //   434: iload_2
    //   435: iconst_1
    //   436: ishr
    //   437: istore #19
    //   439: iload #14
    //   441: iload #19
    //   443: iand
    //   444: ifeq -> 452
    //   447: iconst_1
    //   448: istore_2
    //   449: goto -> 454
    //   452: iconst_0
    //   453: istore_2
    //   454: iload #5
    //   456: ifeq -> 638
    //   459: iload #5
    //   461: iconst_1
    //   462: if_icmpeq -> 561
    //   465: iload #5
    //   467: iconst_2
    //   468: if_icmpeq -> 540
    //   471: iload #5
    //   473: iconst_3
    //   474: if_icmpeq -> 508
    //   477: iload #5
    //   479: istore_1
    //   480: iload #5
    //   482: iconst_4
    //   483: if_icmpeq -> 517
    //   486: iload #5
    //   488: istore_1
    //   489: iload #15
    //   491: istore #6
    //   493: dload #16
    //   495: dstore #8
    //   497: iload #11
    //   499: istore #20
    //   501: iload #7
    //   503: istore #21
    //   505: goto -> 684
    //   508: iload #5
    //   510: istore_1
    //   511: iload_2
    //   512: ifeq -> 517
    //   515: iconst_4
    //   516: istore_1
    //   517: dload #16
    //   519: ldc2_w 2.0
    //   522: dmul
    //   523: dstore #8
    //   525: iload #15
    //   527: istore #6
    //   529: iload #11
    //   531: istore #20
    //   533: iload #7
    //   535: istore #21
    //   537: goto -> 684
    //   540: ldc2_w 2.0
    //   543: dstore #8
    //   545: iconst_3
    //   546: istore_1
    //   547: iload #15
    //   549: istore #6
    //   551: iload #11
    //   553: istore #20
    //   555: iload_2
    //   556: istore #21
    //   558: goto -> 684
    //   561: dload #12
    //   563: ldc2_w 2.0
    //   566: dmul
    //   567: dstore #12
    //   569: dload #12
    //   571: dstore #22
    //   573: iload_2
    //   574: ifeq -> 583
    //   577: dload #12
    //   579: dconst_1
    //   580: dadd
    //   581: dstore #22
    //   583: iinc #15, -1
    //   586: dload #22
    //   588: dstore #12
    //   590: iload #5
    //   592: istore_1
    //   593: iload #15
    //   595: istore #6
    //   597: dload #16
    //   599: dstore #8
    //   601: iload #11
    //   603: istore #20
    //   605: iload #7
    //   607: istore #21
    //   609: iload #15
    //   611: ifne -> 684
    //   614: iconst_2
    //   615: istore_1
    //   616: dload #22
    //   618: dstore #12
    //   620: iload #15
    //   622: istore #6
    //   624: dload #16
    //   626: dstore #8
    //   628: iload_2
    //   629: istore #20
    //   631: iload #7
    //   633: istore #21
    //   635: goto -> 684
    //   638: iload #5
    //   640: istore_1
    //   641: iload #15
    //   643: istore #6
    //   645: dload #16
    //   647: dstore #8
    //   649: iload #11
    //   651: istore #20
    //   653: iload #7
    //   655: istore #21
    //   657: iload_2
    //   658: ifeq -> 684
    //   661: iload #15
    //   663: iconst_1
    //   664: isub
    //   665: istore #6
    //   667: dconst_1
    //   668: dstore #12
    //   670: iconst_1
    //   671: istore_1
    //   672: iload #7
    //   674: istore #21
    //   676: iload #11
    //   678: istore #20
    //   680: dload #16
    //   682: dstore #8
    //   684: iload #19
    //   686: istore_2
    //   687: iload_1
    //   688: istore #5
    //   690: iload #6
    //   692: istore #15
    //   694: dload #8
    //   696: dstore #16
    //   698: iload #20
    //   700: istore #11
    //   702: iload #21
    //   704: istore #7
    //   706: goto -> 286
    //   709: dload #12
    //   711: dreturn
    // Exception table:
    //   from	to	target	type
    //   206	218	221	java/lang/NumberFormatException
  }
  
  public static long testUint32String(String paramString) {
    int i = paramString.length();
    long l = -1L;
    if (1 <= i && i <= 10) {
      int j = paramString.charAt(0) - 48;
      if (j == 0) {
        if (i == 1)
          l = 0L; 
        return l;
      } 
      if (1 <= j && j <= 9) {
        l = j;
        for (j = 1; j != i; j++) {
          int k = paramString.charAt(j) - 48;
          if (k < 0 || k > 9)
            return -1L; 
          l = 10L * l + k;
        } 
        if (l >>> 32L == 0L)
          return l; 
      } 
    } 
    return -1L;
  }
  
  public static JavaScriptException throwCustomError(Context paramContext, Scriptable paramScriptable, String paramString1, String paramString2) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    String str = Context.getSourcePositionFromStack(arrayOfInt);
    return new JavaScriptException(paramContext.newObject(paramScriptable, paramString1, new Object[] { paramString2, str, Integer.valueOf(arrayOfInt[0]) }), str, arrayOfInt[0]);
  }
  
  public static JavaScriptException throwError(Context paramContext, Scriptable paramScriptable, String paramString) {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    String str = Context.getSourcePositionFromStack(arrayOfInt);
    return new JavaScriptException(newBuiltinObject(paramContext, paramScriptable, TopLevel.Builtins.Error, new Object[] { paramString, str, Integer.valueOf(arrayOfInt[0]) }), str, arrayOfInt[0]);
  }
  
  public static boolean toBoolean(Object paramObject) {
    while (true) {
      if (paramObject instanceof Boolean)
        return ((Boolean)paramObject).booleanValue(); 
      boolean bool1 = false;
      boolean bool2 = false;
      if (paramObject == null || paramObject == Undefined.instance)
        break; 
      if (paramObject instanceof CharSequence) {
        if (((CharSequence)paramObject).length() != 0)
          bool2 = true; 
        return bool2;
      } 
      if (paramObject instanceof Number) {
        double d = ((Number)paramObject).doubleValue();
        bool2 = bool1;
        if (d == d) {
          bool2 = bool1;
          if (d != 0.0D)
            bool2 = true; 
        } 
        return bool2;
      } 
      if (paramObject instanceof Scriptable) {
        if (paramObject instanceof ScriptableObject && ((ScriptableObject)paramObject).avoidObjectDetection())
          return false; 
        if (Context.getContext().isVersionECMA1())
          return true; 
        Object object = ((Scriptable)paramObject).getDefaultValue(BooleanClass);
        paramObject = object;
        if (object instanceof Scriptable) {
          if (isSymbol(object)) {
            paramObject = object;
            continue;
          } 
          throw errorWithClassName("msg.primitive.expected", object);
        } 
        continue;
      } 
      warnAboutNonJSObject(paramObject);
      return true;
    } 
    return false;
  }
  
  public static CharSequence toCharSequence(Object paramObject) {
    if (paramObject instanceof NativeString)
      return ((NativeString)paramObject).toCharSequence(); 
    if (paramObject instanceof CharSequence) {
      paramObject = paramObject;
    } else {
      paramObject = toString(paramObject);
    } 
    return (CharSequence)paramObject;
  }
  
  public static int toInt32(double paramDouble) {
    return DoubleConversion.doubleToInt32(paramDouble);
  }
  
  public static int toInt32(Object paramObject) {
    return (paramObject instanceof Integer) ? ((Integer)paramObject).intValue() : toInt32(toNumber(paramObject));
  }
  
  public static int toInt32(Object[] paramArrayOfObject, int paramInt) {
    if (paramInt < paramArrayOfObject.length) {
      paramInt = toInt32(paramArrayOfObject[paramInt]);
    } else {
      paramInt = 0;
    } 
    return paramInt;
  }
  
  public static double toInteger(double paramDouble) {
    return (paramDouble != paramDouble) ? 0.0D : ((paramDouble == 0.0D || paramDouble == Double.POSITIVE_INFINITY || paramDouble == Double.NEGATIVE_INFINITY) ? paramDouble : ((paramDouble > 0.0D) ? Math.floor(paramDouble) : Math.ceil(paramDouble)));
  }
  
  public static double toInteger(Object paramObject) {
    return toInteger(toNumber(paramObject));
  }
  
  public static double toInteger(Object[] paramArrayOfObject, int paramInt) {
    double d;
    if (paramInt < paramArrayOfObject.length) {
      d = toInteger(paramArrayOfObject[paramInt]);
    } else {
      d = 0.0D;
    } 
    return d;
  }
  
  public static Scriptable toIterator(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, boolean paramBoolean) {
    if (ScriptableObject.hasProperty(paramScriptable2, "__iterator__")) {
      Object object = ScriptableObject.getProperty(paramScriptable2, "__iterator__");
      if (object instanceof Callable) {
        Callable callable = (Callable)object;
        if (paramBoolean) {
          object = Boolean.TRUE;
        } else {
          object = Boolean.FALSE;
        } 
        Object object1 = callable.call(paramContext, paramScriptable1, paramScriptable2, new Object[] { object });
        if (object1 instanceof Scriptable)
          return (Scriptable)object1; 
        throw typeError0("msg.iterator.primitive");
      } 
      throw typeError0("msg.invalid.iterator");
    } 
    return null;
  }
  
  public static long toLength(Object[] paramArrayOfObject, int paramInt) {
    double d = toInteger(paramArrayOfObject, paramInt);
    return (d <= 0.0D) ? 0L : (long)Math.min(d, 9.007199254740991E15D);
  }
  
  public static double toNumber(Object paramObject) {
    while (true) {
      if (paramObject instanceof Number)
        return ((Number)paramObject).doubleValue(); 
      double d = 0.0D;
      if (paramObject == null)
        return 0.0D; 
      if (paramObject == Undefined.instance)
        return NaN; 
      if (paramObject instanceof String)
        return toNumber((String)paramObject); 
      if (paramObject instanceof CharSequence)
        return toNumber(paramObject.toString()); 
      if (paramObject instanceof Boolean) {
        if (((Boolean)paramObject).booleanValue())
          d = 1.0D; 
        return d;
      } 
      if (!(paramObject instanceof Symbol)) {
        if (paramObject instanceof Scriptable) {
          Object object = ((Scriptable)paramObject).getDefaultValue(NumberClass);
          paramObject = object;
          if (object instanceof Scriptable) {
            if (isSymbol(object)) {
              paramObject = object;
              continue;
            } 
            throw errorWithClassName("msg.primitive.expected", object);
          } 
          continue;
        } 
        warnAboutNonJSObject(paramObject);
        return NaN;
      } 
      throw typeError0("msg.not.a.number");
    } 
  }
  
  public static double toNumber(String paramString) {
    int i = paramString.length();
    for (byte b = 0;; b++) {
      if (b == i)
        return 0.0D; 
      char c = paramString.charAt(b);
      if (!isStrWhiteSpaceChar(c)) {
        for (int j = i - 1;; j--)
          char c1 = paramString.charAt(j); 
        boolean bool = isStrWhiteSpaceChar(c1);
        return NaN;
      } 
      continue;
    } 
  }
  
  public static double toNumber(Object[] paramArrayOfObject, int paramInt) {
    double d;
    if (paramInt < paramArrayOfObject.length) {
      d = toNumber(paramArrayOfObject[paramInt]);
    } else {
      d = NaN;
    } 
    return d;
  }
  
  public static Scriptable toObject(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    if (paramObject != null) {
      if (!Undefined.isUndefined(paramObject)) {
        NativeBoolean nativeBoolean;
        if (isSymbol(paramObject)) {
          NativeSymbol nativeSymbol = new NativeSymbol((NativeSymbol)paramObject);
          setBuiltinProtoAndParent(nativeSymbol, paramScriptable, TopLevel.Builtins.Symbol);
          return nativeSymbol;
        } 
        if (paramObject instanceof Scriptable)
          return (Scriptable)paramObject; 
        if (paramObject instanceof CharSequence) {
          NativeString nativeString = new NativeString((CharSequence)paramObject);
          setBuiltinProtoAndParent(nativeString, paramScriptable, TopLevel.Builtins.String);
          return nativeString;
        } 
        if (paramObject instanceof Number) {
          NativeNumber nativeNumber = new NativeNumber(((Number)paramObject).doubleValue());
          setBuiltinProtoAndParent(nativeNumber, paramScriptable, TopLevel.Builtins.Number);
          return nativeNumber;
        } 
        if (paramObject instanceof Boolean) {
          nativeBoolean = new NativeBoolean(((Boolean)paramObject).booleanValue());
          setBuiltinProtoAndParent(nativeBoolean, paramScriptable, TopLevel.Builtins.Boolean);
          return nativeBoolean;
        } 
        Object object = nativeBoolean.getWrapFactory().wrap((Context)nativeBoolean, paramScriptable, paramObject, null);
        if (object instanceof Scriptable)
          return (Scriptable)object; 
        throw errorWithClassName("msg.invalid.type", paramObject);
      } 
      throw typeError0("msg.undef.to.object");
    } 
    throw typeError0("msg.null.to.object");
  }
  
  @Deprecated
  public static Scriptable toObject(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    return toObject(paramContext, paramScriptable, paramObject);
  }
  
  public static Scriptable toObject(Scriptable paramScriptable, Object paramObject) {
    return (paramObject instanceof Scriptable) ? (Scriptable)paramObject : toObject(Context.getContext(), paramScriptable, paramObject);
  }
  
  @Deprecated
  public static Scriptable toObject(Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    return (paramObject instanceof Scriptable) ? (Scriptable)paramObject : toObject(Context.getContext(), paramScriptable, paramObject);
  }
  
  @Deprecated
  public static Scriptable toObjectOrNull(Context paramContext, Object paramObject) {
    return (paramObject instanceof Scriptable) ? (Scriptable)paramObject : ((paramObject != null && paramObject != Undefined.instance) ? toObject(paramContext, getTopCallScope(paramContext), paramObject) : null);
  }
  
  public static Scriptable toObjectOrNull(Context paramContext, Object paramObject, Scriptable paramScriptable) {
    return (paramObject instanceof Scriptable) ? (Scriptable)paramObject : ((paramObject != null && paramObject != Undefined.instance) ? toObject(paramContext, paramScriptable, paramObject) : null);
  }
  
  public static Object toPrimitive(Object paramObject) {
    return toPrimitive(paramObject, null);
  }
  
  public static Object toPrimitive(Object paramObject, Class<?> paramClass) {
    if (!(paramObject instanceof Scriptable))
      return paramObject; 
    paramObject = ((Scriptable)paramObject).getDefaultValue(paramClass);
    if (!(paramObject instanceof Scriptable) || isSymbol(paramObject))
      return paramObject; 
    throw typeError0("msg.bad.default.value");
  }
  
  public static String toString(double paramDouble) {
    return numberToString(paramDouble, 10);
  }
  
  public static String toString(Object paramObject) {
    while (true) {
      if (paramObject == null)
        return "null"; 
      if (paramObject == Undefined.instance || paramObject == Undefined.SCRIPTABLE_UNDEFINED)
        break; 
      if (paramObject instanceof String)
        return (String)paramObject; 
      if (paramObject instanceof CharSequence)
        return paramObject.toString(); 
      if (paramObject instanceof Number)
        return numberToString(((Number)paramObject).doubleValue(), 10); 
      if (!(paramObject instanceof Symbol)) {
        if (paramObject instanceof Scriptable) {
          Object object = ((Scriptable)paramObject).getDefaultValue(StringClass);
          paramObject = object;
          if (object instanceof Scriptable) {
            if (isSymbol(object)) {
              paramObject = object;
              continue;
            } 
            throw errorWithClassName("msg.primitive.expected", object);
          } 
          continue;
        } 
        return paramObject.toString();
      } 
      throw typeError0("msg.not.a.string");
    } 
    return "undefined";
  }
  
  public static String toString(Object[] paramArrayOfObject, int paramInt) {
    String str;
    if (paramInt < paramArrayOfObject.length) {
      str = toString(paramArrayOfObject[paramInt]);
    } else {
      str = "undefined";
    } 
    return str;
  }
  
  static String toStringIdOrIndex(Context paramContext, Object paramObject) {
    if (paramObject instanceof Number) {
      double d = ((Number)paramObject).doubleValue();
      int i = (int)d;
      if (i == d) {
        storeIndexResult(paramContext, i);
        return null;
      } 
      return toString(paramObject);
    } 
    if (paramObject instanceof String) {
      paramObject = paramObject;
    } else {
      paramObject = toString(paramObject);
    } 
    long l = indexFromString((String)paramObject);
    if (l >= 0L) {
      storeIndexResult(paramContext, (int)l);
      return null;
    } 
    return (String)paramObject;
  }
  
  public static char toUint16(Object paramObject) {
    return (char)DoubleConversion.doubleToInt32(toNumber(paramObject));
  }
  
  public static long toUint32(double paramDouble) {
    return DoubleConversion.doubleToInt32(paramDouble) & 0xFFFFFFFFL;
  }
  
  public static long toUint32(Object paramObject) {
    return toUint32(toNumber(paramObject));
  }
  
  private static Object topScopeName(Context paramContext, Scriptable paramScriptable, String paramString) {
    Scriptable scriptable = paramScriptable;
    if (paramContext.useDynamicScope)
      scriptable = checkDynamicScope(paramContext.topCallScope, paramScriptable); 
    return ScriptableObject.getProperty(scriptable, paramString);
  }
  
  public static EcmaError typeError(String paramString) {
    return constructError("TypeError", paramString);
  }
  
  public static EcmaError typeError0(String paramString) {
    return typeError(getMessage0(paramString));
  }
  
  public static EcmaError typeError1(String paramString, Object paramObject) {
    return typeError(getMessage1(paramString, paramObject));
  }
  
  public static EcmaError typeError2(String paramString, Object paramObject1, Object paramObject2) {
    return typeError(getMessage2(paramString, paramObject1, paramObject2));
  }
  
  public static EcmaError typeError3(String paramString1, String paramString2, String paramString3, String paramString4) {
    return typeError(getMessage3(paramString1, paramString2, paramString3, paramString4));
  }
  
  @Deprecated
  public static BaseFunction typeErrorThrower() {
    return typeErrorThrower(Context.getCurrentContext());
  }
  
  public static BaseFunction typeErrorThrower(Context paramContext) {
    if (paramContext.typeErrorThrower == null) {
      BaseFunction baseFunction = new BaseFunction() {
          private static final long serialVersionUID = -5891740962154902286L;
          
          public Object call(Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject) {
            throw ScriptRuntime.typeError0("msg.op.not.allowed");
          }
          
          public int getLength() {
            return 0;
          }
        };
      setFunctionProtoAndParent(baseFunction, paramContext.topCallScope);
      baseFunction.preventExtensions();
      paramContext.typeErrorThrower = baseFunction;
    } 
    return paramContext.typeErrorThrower;
  }
  
  public static String typeof(Object paramObject) {
    String str = "object";
    if (paramObject == null)
      return "object"; 
    if (paramObject == Undefined.instance)
      return "undefined"; 
    if (paramObject instanceof ScriptableObject)
      return ((ScriptableObject)paramObject).getTypeOf(); 
    if (paramObject instanceof Scriptable) {
      if (paramObject instanceof Callable)
        str = "function"; 
      return str;
    } 
    if (paramObject instanceof CharSequence)
      return "string"; 
    if (paramObject instanceof Number)
      return "number"; 
    if (paramObject instanceof Boolean)
      return "boolean"; 
    throw errorWithClassName("msg.invalid.type", paramObject);
  }
  
  public static String typeofName(Scriptable paramScriptable, String paramString) {
    Context context = Context.getContext();
    paramScriptable = bind(context, paramScriptable, paramString);
    return (paramScriptable == null) ? "undefined" : typeof(getObjectProp(paramScriptable, paramString, context));
  }
  
  public static RuntimeException undefCallError(Object paramObject1, Object paramObject2) {
    return typeError2("msg.undef.method.call", toString(paramObject1), toString(paramObject2));
  }
  
  private static RuntimeException undefDeleteError(Object paramObject1, Object paramObject2) {
    throw typeError2("msg.undef.prop.delete", toString(paramObject1), toString(paramObject2));
  }
  
  public static RuntimeException undefReadError(Object paramObject1, Object paramObject2) {
    return typeError2("msg.undef.prop.read", toString(paramObject1), toString(paramObject2));
  }
  
  public static RuntimeException undefWriteError(Object paramObject1, Object paramObject2, Object paramObject3) {
    return typeError3("msg.undef.prop.write", toString(paramObject1), toString(paramObject2), toString(paramObject3));
  }
  
  static String uneval(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    String str;
    StringBuilder stringBuilder;
    if (paramObject == null)
      return "null"; 
    if (paramObject == Undefined.instance)
      return "undefined"; 
    if (paramObject instanceof CharSequence) {
      str = escapeString(paramObject.toString());
      stringBuilder = new StringBuilder(str.length() + 2);
      stringBuilder.append('"');
      stringBuilder.append(str);
      stringBuilder.append('"');
      return stringBuilder.toString();
    } 
    if (paramObject instanceof Number) {
      double d = ((Number)paramObject).doubleValue();
      return (d == 0.0D && 1.0D / d < 0.0D) ? "-0" : toString(d);
    } 
    if (paramObject instanceof Boolean)
      return toString(paramObject); 
    if (paramObject instanceof Scriptable) {
      Scriptable scriptable = (Scriptable)paramObject;
      if (ScriptableObject.hasProperty(scriptable, "toSource")) {
        Object object = ScriptableObject.getProperty(scriptable, "toSource");
        if (object instanceof Function)
          return toString(((Function)object).call((Context)str, (Scriptable)stringBuilder, scriptable, emptyArgs)); 
      } 
      return toString(paramObject);
    } 
    warnAboutNonJSObject(paramObject);
    return paramObject.toString();
  }
  
  public static Object updateDotQuery(boolean paramBoolean, Scriptable paramScriptable) {
    return ((NativeWith)paramScriptable).updateDotQuery(paramBoolean);
  }
  
  private static void warnAboutNonJSObject(Object paramObject) {
    if (!"true".equals(getMessage0("params.omit.non.js.object.warning"))) {
      paramObject = getMessage2("msg.non.js.object.warning", paramObject, paramObject.getClass().getName());
      Context.reportWarning((String)paramObject);
      System.err.println((String)paramObject);
    } 
  }
  
  public static Boolean wrapBoolean(boolean paramBoolean) {
    Boolean bool;
    if (paramBoolean) {
      bool = Boolean.TRUE;
    } else {
      bool = Boolean.FALSE;
    } 
    return bool;
  }
  
  public static Scriptable wrapException(Throwable paramThrowable, Scriptable paramScriptable, Context paramContext) {
    String str1;
    Object[] arrayOfObject;
    String str2;
    Throwable throwable = null;
    if (paramThrowable instanceof EcmaError) {
      EcmaError ecmaError = (EcmaError)paramThrowable;
      paramThrowable = ecmaError;
      str2 = ecmaError.getName();
      str1 = ecmaError.getErrorMessage();
    } else if (paramThrowable instanceof WrappedException) {
      WrappedException wrappedException = (WrappedException)paramThrowable;
      paramThrowable = wrappedException;
      throwable = wrappedException.getWrappedException();
      str2 = "JavaException";
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(throwable.getClass().getName());
      stringBuilder.append(": ");
      stringBuilder.append(throwable.getMessage());
      str1 = stringBuilder.toString();
    } else if (paramThrowable instanceof EvaluatorException) {
      EvaluatorException evaluatorException = (EvaluatorException)paramThrowable;
      paramThrowable = evaluatorException;
      str2 = "InternalError";
      str1 = evaluatorException.getMessage();
    } else if (paramContext.hasFeature(13)) {
      WrappedException wrappedException = new WrappedException(paramThrowable);
      str2 = "JavaException";
      str1 = paramThrowable.toString();
      paramThrowable = wrappedException;
    } else {
      throw Kit.codeBug();
    } 
    String str4 = paramThrowable.sourceName();
    String str3 = str4;
    if (str4 == null)
      str3 = ""; 
    int i = paramThrowable.lineNumber();
    if (i > 0) {
      arrayOfObject = new Object[] { str1, str3, Integer.valueOf(i) };
    } else {
      arrayOfObject = new Object[] { arrayOfObject, str3 };
    } 
    Scriptable scriptable = paramContext.newObject(paramScriptable, str2, arrayOfObject);
    ScriptableObject.putProperty(scriptable, "name", str2);
    if (scriptable instanceof NativeError)
      ((NativeError)scriptable).setStackProvider((HippoException)paramThrowable); 
    if (throwable != null && isVisible(paramContext, throwable))
      ScriptableObject.defineProperty(scriptable, "javaException", paramContext.getWrapFactory().wrap(paramContext, paramScriptable, throwable, null), 7); 
    if (isVisible(paramContext, paramThrowable))
      ScriptableObject.defineProperty(scriptable, "hippoException", paramContext.getWrapFactory().wrap(paramContext, paramScriptable, paramThrowable, null), 7); 
    return scriptable;
  }
  
  public static Integer wrapInt(int paramInt) {
    return Integer.valueOf(paramInt);
  }
  
  public static Number wrapNumber(double paramDouble) {
    return (paramDouble != paramDouble) ? NaNobj : new Double(paramDouble);
  }
  
  public static Scriptable wrapRegExp(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    return paramContext.getRegExpProxy().wrapRegExp(paramContext, paramScriptable, paramObject);
  }
  
  private static class DefaultMessageProvider implements MessageProvider {
    private DefaultMessageProvider() {}
    
    public String getMessage(String param1String, Object[] param1ArrayOfObject) {
      Locale locale;
      Context context = Context.getCurrentContext();
      if (context != null) {
        locale = context.getLocale();
      } else {
        locale = Locale.getDefault();
      } 
      ResourceBundle resourceBundle = ResourceBundle.getBundle("com.trendmicro.hippo.resources.Messages", locale);
      try {
        String str = resourceBundle.getString(param1String);
        return (new MessageFormat(str)).format(param1ArrayOfObject);
      } catch (MissingResourceException missingResourceException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("no message resource found for message property ");
        stringBuilder.append(param1String);
        throw new RuntimeException(stringBuilder.toString());
      } 
    }
  }
  
  private static class IdEnumeration implements Serializable {
    private static final long serialVersionUID = 1L;
    
    Object currentId;
    
    boolean enumNumbers;
    
    int enumType;
    
    Object[] ids;
    
    int index;
    
    Scriptable iterator;
    
    Scriptable obj;
    
    ObjToIntMap used;
    
    private IdEnumeration() {}
  }
  
  public static interface MessageProvider {
    String getMessage(String param1String, Object[] param1ArrayOfObject);
  }
  
  static class NoSuchMethodShim implements Callable {
    String methodName;
    
    Callable noSuchMethodMethod;
    
    NoSuchMethodShim(Callable param1Callable, String param1String) {
      this.noSuchMethodMethod = param1Callable;
      this.methodName = param1String;
    }
    
    public Object call(Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject) {
      String str = this.methodName;
      Scriptable scriptable = ScriptRuntime.newArrayLiteral(param1ArrayOfObject, null, param1Context, param1Scriptable1);
      return this.noSuchMethodMethod.call(param1Context, param1Scriptable1, param1Scriptable2, new Object[] { str, scriptable });
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ScriptRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */