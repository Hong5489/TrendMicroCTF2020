package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FunctionObject extends BaseFunction {
  public static final int JAVA_BOOLEAN_TYPE = 3;
  
  public static final int JAVA_DOUBLE_TYPE = 4;
  
  public static final int JAVA_INT_TYPE = 2;
  
  public static final int JAVA_OBJECT_TYPE = 6;
  
  public static final int JAVA_SCRIPTABLE_TYPE = 5;
  
  public static final int JAVA_STRING_TYPE = 1;
  
  public static final int JAVA_UNSUPPORTED_TYPE = 0;
  
  private static final short VARARGS_CTOR = -2;
  
  private static final short VARARGS_METHOD = -1;
  
  private static boolean sawSecurityException = false;
  
  private static final long serialVersionUID = -5332312783643935019L;
  
  private String functionName;
  
  private transient boolean hasVoidReturn;
  
  private boolean isStatic;
  
  MemberBox member;
  
  private int parmsLength;
  
  private transient int returnTypeTag;
  
  private transient byte[] typeTags;
  
  public FunctionObject(String paramString, Member paramMember, Scriptable paramScriptable) {
    if (paramMember instanceof Constructor) {
      this.member = new MemberBox((Constructor)paramMember);
      this.isStatic = true;
    } else {
      MemberBox memberBox = new MemberBox((Method)paramMember);
      this.member = memberBox;
      this.isStatic = memberBox.isStatic();
    } 
    String str = this.member.getName();
    this.functionName = paramString;
    Class<?>[] arrayOfClass = this.member.argTypes;
    int i = arrayOfClass.length;
    if (i == 4 && (arrayOfClass[1].isArray() || arrayOfClass[2].isArray())) {
      if (arrayOfClass[1].isArray()) {
        if (this.isStatic && arrayOfClass[0] == ScriptRuntime.ContextClass && arrayOfClass[1].getComponentType() == ScriptRuntime.ObjectClass && arrayOfClass[2] == ScriptRuntime.FunctionClass && arrayOfClass[3] == boolean.class) {
          this.parmsLength = -2;
        } else {
          throw Context.reportRuntimeError1("msg.varargs.ctor", str);
        } 
      } else if (this.isStatic && arrayOfClass[0] == ScriptRuntime.ContextClass && arrayOfClass[1] == ScriptRuntime.ScriptableClass && arrayOfClass[2].getComponentType() == ScriptRuntime.ObjectClass && arrayOfClass[3] == ScriptRuntime.FunctionClass) {
        this.parmsLength = -1;
      } else {
        throw Context.reportRuntimeError1("msg.varargs.fun", str);
      } 
    } else {
      this.parmsLength = i;
      if (i > 0) {
        this.typeTags = new byte[i];
        int j = 0;
        while (j != i) {
          int k = getTypeTag(arrayOfClass[j]);
          if (k != 0) {
            this.typeTags[j] = (byte)(byte)k;
            j++;
            continue;
          } 
          throw Context.reportRuntimeError2("msg.bad.parms", arrayOfClass[j].getName(), str);
        } 
      } 
    } 
    if (this.member.isMethod()) {
      Class<?> clazz = this.member.method().getReturnType();
      if (clazz == void.class) {
        this.hasVoidReturn = true;
      } else {
        this.returnTypeTag = getTypeTag(clazz);
      } 
    } else {
      Class<?> clazz = this.member.getDeclaringClass();
      if (!ScriptRuntime.ScriptableClass.isAssignableFrom(clazz))
        throw Context.reportRuntimeError1("msg.bad.ctor.return", clazz.getName()); 
    } 
    ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable);
  }
  
  public static Object convertArg(Context paramContext, Scriptable paramScriptable, Object paramObject, int paramInt) {
    Boolean bool;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException();
      case 6:
        return paramObject;
      case 5:
        return ScriptRuntime.toObjectOrNull(paramContext, paramObject, paramScriptable);
      case 4:
        return (paramObject instanceof Double) ? paramObject : Double.valueOf(ScriptRuntime.toNumber(paramObject));
      case 3:
        if (paramObject instanceof Boolean)
          return paramObject; 
        if (ScriptRuntime.toBoolean(paramObject)) {
          bool = Boolean.TRUE;
        } else {
          bool = Boolean.FALSE;
        } 
        return bool;
      case 2:
        return (paramObject instanceof Integer) ? paramObject : Integer.valueOf(ScriptRuntime.toInt32(paramObject));
      case 1:
        break;
    } 
    return (paramObject instanceof String) ? paramObject : ScriptRuntime.toString(paramObject);
  }
  
  @Deprecated
  public static Object convertArg(Context paramContext, Scriptable paramScriptable, Object paramObject, Class<?> paramClass) {
    int i = getTypeTag(paramClass);
    if (i != 0)
      return convertArg(paramContext, paramScriptable, paramObject, i); 
    throw Context.reportRuntimeError1("msg.cant.convert", paramClass.getName());
  }
  
  static Method findSingleMethod(Method[] paramArrayOfMethod, String paramString) {
    Method method = null;
    int i = 0;
    int j = paramArrayOfMethod.length;
    while (i != j) {
      Method method1 = paramArrayOfMethod[i];
      Method method2 = method;
      if (method1 != null) {
        method2 = method;
        if (paramString.equals(method1.getName()))
          if (method == null) {
            method2 = method1;
          } else {
            throw Context.reportRuntimeError2("msg.no.overload", paramString, method1.getDeclaringClass().getName());
          }  
      } 
      i++;
      method = method2;
    } 
    return method;
  }
  
  static Method[] getMethodList(Class<?> paramClass) {
    Method[] arrayOfMethod2;
    SecurityException securityException1 = null;
    Method[] arrayOfMethod3 = null;
    try {
      if (!sawSecurityException)
        arrayOfMethod3 = paramClass.getDeclaredMethods(); 
    } catch (SecurityException securityException2) {
      sawSecurityException = true;
      securityException2 = securityException1;
    } 
    securityException1 = securityException2;
    if (securityException2 == null)
      arrayOfMethod2 = paramClass.getMethods(); 
    int i = 0;
    byte b;
    for (b = 0; b < arrayOfMethod2.length; b++) {
      if (sawSecurityException ? (arrayOfMethod2[b].getDeclaringClass() != paramClass) : !Modifier.isPublic(arrayOfMethod2[b].getModifiers())) {
        arrayOfMethod2[b] = null;
      } else {
        i++;
      } 
    } 
    Method[] arrayOfMethod1 = new Method[i];
    int j = 0;
    b = 0;
    while (b < arrayOfMethod2.length) {
      i = j;
      if (arrayOfMethod2[b] != null) {
        arrayOfMethod1[j] = arrayOfMethod2[b];
        i = j + 1;
      } 
      b++;
      j = i;
    } 
    return arrayOfMethod1;
  }
  
  public static int getTypeTag(Class<?> paramClass) {
    return (paramClass == ScriptRuntime.StringClass) ? 1 : ((paramClass == ScriptRuntime.IntegerClass || paramClass == int.class) ? 2 : ((paramClass == ScriptRuntime.BooleanClass || paramClass == boolean.class) ? 3 : ((paramClass == ScriptRuntime.DoubleClass || paramClass == double.class) ? 4 : (ScriptRuntime.ScriptableClass.isAssignableFrom(paramClass) ? 5 : ((paramClass == ScriptRuntime.ObjectClass) ? 6 : 0)))));
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    if (this.parmsLength > 0) {
      Class<?>[] arrayOfClass = this.member.argTypes;
      this.typeTags = new byte[this.parmsLength];
      for (byte b = 0; b != this.parmsLength; b++)
        this.typeTags[b] = (byte)(byte)getTypeTag(arrayOfClass[b]); 
    } 
    if (this.member.isMethod()) {
      Class<?> clazz = this.member.method().getReturnType();
      if (clazz == void.class) {
        this.hasVoidReturn = true;
      } else {
        this.returnTypeTag = getTypeTag(clazz);
      } 
    } 
  }
  
  public void addAsConstructor(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    initAsConstructor(paramScriptable1, paramScriptable2);
    defineProperty(paramScriptable1, paramScriptable2.getClassName(), this, 2);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    Object object1;
    boolean bool = false;
    int i = paramArrayOfObject.length;
    int j;
    for (j = 0; j < i; j++) {
      if (paramArrayOfObject[j] instanceof ConsString)
        paramArrayOfObject[j] = paramArrayOfObject[j].toString(); 
    } 
    j = this.parmsLength;
    if (j < 0) {
      if (j == -1) {
        object1 = this.member.invoke(null, new Object[] { paramContext, paramScriptable2, paramArrayOfObject, this });
        j = 1;
      } else {
        if (object1 == null) {
          j = 1;
        } else {
          j = 0;
        } 
        if (j != 0) {
          object1 = Boolean.TRUE;
        } else {
          object1 = Boolean.FALSE;
        } 
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = paramContext;
        arrayOfObject[1] = paramArrayOfObject;
        arrayOfObject[2] = this;
        arrayOfObject[3] = object1;
        if (this.member.isCtor()) {
          object1 = this.member.newInstance(arrayOfObject);
        } else {
          object1 = this.member.invoke(null, arrayOfObject);
        } 
        j = bool;
      } 
    } else {
      Object object = object1;
      if (!this.isStatic) {
        Class<?> clazz = this.member.getDeclaringClass();
        object = object1;
        if (!clazz.isInstance(object1)) {
          boolean bool1 = false;
          boolean bool2 = bool1;
          Object object3 = object1;
          if (object1 == paramScriptable1) {
            object = getParentScope();
            bool2 = bool1;
            object3 = object1;
            if (paramScriptable1 != object) {
              bool1 = clazz.isInstance(object);
              bool2 = bool1;
              object3 = object1;
              if (bool1) {
                object3 = object;
                bool2 = bool1;
              } 
            } 
          } 
          if (bool2) {
            object = object3;
          } else {
            throw ScriptRuntime.typeError1("msg.incompat.call", this.functionName);
          } 
        } 
      } 
      j = this.parmsLength;
      if (j == i) {
        object1 = paramArrayOfObject;
        j = 0;
        while (j != this.parmsLength) {
          Object object5 = paramArrayOfObject[j];
          Object object4 = convertArg(paramContext, paramScriptable1, object5, this.typeTags[j]);
          Object object3 = object1;
          if (object5 != object4) {
            object3 = object1;
            if (object1 == paramArrayOfObject)
              object3 = paramArrayOfObject.clone(); 
            object3[j] = object4;
          } 
          j++;
          object1 = object3;
        } 
      } else if (j == 0) {
        object1 = ScriptRuntime.emptyArgs;
      } else {
        Object[] arrayOfObject = new Object[j];
        j = 0;
        while (true) {
          object1 = arrayOfObject;
          if (j != this.parmsLength) {
            if (j < i) {
              object1 = paramArrayOfObject[j];
            } else {
              object1 = Undefined.instance;
            } 
            arrayOfObject[j] = convertArg(paramContext, paramScriptable1, object1, this.typeTags[j]);
            j++;
            continue;
          } 
          break;
        } 
      } 
      if (this.member.isMethod()) {
        object1 = this.member.invoke(object, (Object[])object1);
        j = 1;
      } else {
        object1 = this.member.newInstance((Object[])object1);
        j = bool;
      } 
    } 
    Object object2 = object1;
    if (j != 0)
      if (this.hasVoidReturn) {
        object2 = Undefined.instance;
      } else {
        object2 = object1;
        if (this.returnTypeTag == 0)
          object2 = paramContext.getWrapFactory().wrap(paramContext, paramScriptable1, object1, null); 
      }  
    return object2;
  }
  
  public Scriptable createObject(Context paramContext, Scriptable paramScriptable) {
    if (this.member.isCtor() || this.parmsLength == -2)
      return null; 
    try {
      Scriptable scriptable = (Scriptable)this.member.getDeclaringClass().newInstance();
      scriptable.setPrototype(getClassPrototype());
      scriptable.setParentScope(getParentScope());
      return scriptable;
    } catch (Exception exception) {
      throw Context.throwAsScriptRuntimeEx(exception);
    } 
  }
  
  public int getArity() {
    int i = this.parmsLength;
    int j = i;
    if (i < 0)
      j = 1; 
    return j;
  }
  
  public String getFunctionName() {
    String str1 = this.functionName;
    String str2 = str1;
    if (str1 == null)
      str2 = ""; 
    return str2;
  }
  
  public int getLength() {
    return getArity();
  }
  
  public Member getMethodOrConstructor() {
    return (Member)(this.member.isMethod() ? this.member.method() : this.member.ctor());
  }
  
  void initAsConstructor(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    ScriptRuntime.setFunctionProtoAndParent(this, paramScriptable1);
    setImmunePrototypeProperty(paramScriptable2);
    paramScriptable2.setParentScope(this);
    defineProperty(paramScriptable2, "constructor", this, 7);
    setParentScope(paramScriptable1);
  }
  
  boolean isVarArgsConstructor() {
    boolean bool;
    if (this.parmsLength == -2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean isVarArgsMethod() {
    boolean bool;
    if (this.parmsLength == -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/FunctionObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */