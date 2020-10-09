package com.trendmicro.hippo;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Map;

public class NativeJavaClass extends NativeJavaObject implements Function {
  static final String javaClassPropertyName = "__javaObject__";
  
  private static final long serialVersionUID = -6460763940409461664L;
  
  private Map<String, FieldAndMethods> staticFieldAndMethods;
  
  public NativeJavaClass() {}
  
  public NativeJavaClass(Scriptable paramScriptable, Class<?> paramClass) {
    this(paramScriptable, paramClass, false);
  }
  
  public NativeJavaClass(Scriptable paramScriptable, Class<?> paramClass, boolean paramBoolean) {
    super(paramScriptable, paramClass, (Class<?>)null, paramBoolean);
  }
  
  static Object constructInternal(Object[] paramArrayOfObject, MemberBox paramMemberBox) {
    Object object1;
    Object object2;
    Class<?>[] arrayOfClass = paramMemberBox.argTypes;
    if (paramMemberBox.vararg) {
      object2 = new Object[arrayOfClass.length];
      byte b;
      for (b = 0; b < arrayOfClass.length - 1; b++)
        object2[b] = Context.jsToJava(paramArrayOfObject[b], arrayOfClass[b]); 
      if (paramArrayOfObject.length == arrayOfClass.length && (paramArrayOfObject[paramArrayOfObject.length - 1] == null || paramArrayOfObject[paramArrayOfObject.length - 1] instanceof NativeArray || paramArrayOfObject[paramArrayOfObject.length - 1] instanceof NativeJavaArray)) {
        object1 = Context.jsToJava(paramArrayOfObject[paramArrayOfObject.length - 1], arrayOfClass[arrayOfClass.length - 1]);
      } else {
        Class<?> clazz = arrayOfClass[arrayOfClass.length - 1].getComponentType();
        Object object = Array.newInstance(clazz, object1.length - arrayOfClass.length + 1);
        for (b = 0; b < Array.getLength(object); b++)
          Array.set(object, b, Context.jsToJava(object1[arrayOfClass.length - 1 + b], clazz)); 
        object1 = object;
      } 
      object2[arrayOfClass.length - 1] = object1;
    } else {
      byte b = 0;
      Object object = object1;
      while (true) {
        object2 = object;
        if (b < object.length) {
          Object object4 = object[b];
          Object object3 = Context.jsToJava(object4, arrayOfClass[b]);
          object2 = object;
          if (object3 != object4) {
            object2 = object;
            if (object == object1)
              object2 = object1.clone(); 
            object2[b] = object3;
          } 
          b++;
          object = object2;
          continue;
        } 
        break;
      } 
    } 
    return paramMemberBox.newInstance((Object[])object2);
  }
  
  static Scriptable constructSpecific(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, MemberBox paramMemberBox) {
    Object object = constructInternal(paramArrayOfObject, paramMemberBox);
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    return paramContext.getWrapFactory().wrapNewObject(paramContext, paramScriptable, object);
  }
  
  private static Class<?> findNestedClass(Class<?> paramClass, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramClass.getName());
    stringBuilder.append('$');
    stringBuilder.append(paramString);
    paramString = stringBuilder.toString();
    ClassLoader classLoader = paramClass.getClassLoader();
    return (classLoader == null) ? Kit.classOrNull(paramString) : Kit.classOrNull(classLoader, paramString);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length == 1 && paramArrayOfObject[0] instanceof Scriptable) {
      Scriptable scriptable;
      Class<?> clazz = getClassObject();
      paramScriptable2 = (Scriptable)paramArrayOfObject[0];
      do {
        if (paramScriptable2 instanceof Wrapper && clazz.isInstance(((Wrapper)paramScriptable2).unwrap()))
          return paramScriptable2; 
        scriptable = paramScriptable2.getPrototype();
        paramScriptable2 = scriptable;
      } while (scriptable != null);
    } 
    return construct(paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    String str;
    Class<?> clazz = getClassObject();
    int i = clazz.getModifiers();
    if (!Modifier.isInterface(i) && !Modifier.isAbstract(i)) {
      NativeJavaMethod nativeJavaMethod = this.members.ctors;
      i = nativeJavaMethod.findCachedFunction(paramContext, paramArrayOfObject);
      if (i >= 0)
        return constructSpecific(paramContext, paramScriptable, paramArrayOfObject, nativeJavaMethod.methods[i]); 
      str = NativeJavaMethod.scriptSignature(paramArrayOfObject);
      throw Context.reportRuntimeError2("msg.no.java.ctor", clazz.getName(), str);
    } 
    if (paramArrayOfObject.length != 0) {
      String str1;
      Scriptable scriptable = ScriptableObject.getTopLevelScope(this);
      String str2 = "";
      try {
        Object object2;
        if ("Dalvik".equals(System.getProperty("java.vm.name")) && clazz.isInterface()) {
          object2 = createInterfaceAdapter(clazz, ScriptableObject.ensureScriptableObject(paramArrayOfObject[0]));
          return str.getWrapFactory().wrapAsJavaObject((Context)str, paramScriptable, object2, null);
        } 
        Object object1 = scriptable.get("JavaAdapter", scriptable);
        if (object1 != NOT_FOUND)
          return ((Function)object1).construct((Context)str, scriptable, new Object[] { this, object2[0] }); 
        str = str2;
      } catch (Exception exception) {
        String str3 = exception.getMessage();
        str1 = str2;
        if (str3 != null)
          str1 = str3; 
      } 
      throw Context.reportRuntimeError2("msg.cant.instantiate", str1, clazz.getName());
    } 
    throw Context.reportRuntimeError0("msg.adapter.zero.args");
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    Scriptable scriptable1;
    if (paramString.equals("prototype"))
      return null; 
    Map<String, FieldAndMethods> map = this.staticFieldAndMethods;
    if (map != null) {
      map = (Map<String, FieldAndMethods>)map.get(paramString);
      if (map != null)
        return map; 
    } 
    if (this.members.has(paramString, true))
      return this.members.get(this, paramString, this.javaObject, true); 
    Context context = Context.getContext();
    Scriptable scriptable2 = ScriptableObject.getTopLevelScope(paramScriptable);
    WrapFactory wrapFactory = context.getWrapFactory();
    if ("__javaObject__".equals(paramString))
      return wrapFactory.wrap(context, scriptable2, this.javaObject, ScriptRuntime.ClassClass); 
    Class<?> clazz = findNestedClass(getClassObject(), paramString);
    if (clazz != null) {
      scriptable1 = wrapFactory.wrapJavaClass(context, scriptable2, clazz);
      scriptable1.setParentScope(this);
      return scriptable1;
    } 
    throw this.members.reportMemberNotFound(scriptable1);
  }
  
  public String getClassName() {
    return "JavaClass";
  }
  
  public Class<?> getClassObject() {
    return (Class)unwrap();
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    return (paramClass == null || paramClass == ScriptRuntime.StringClass) ? toString() : ((paramClass == ScriptRuntime.BooleanClass) ? Boolean.TRUE : ((paramClass == ScriptRuntime.NumberClass) ? ScriptRuntime.NaNobj : this));
  }
  
  public Object[] getIds() {
    return this.members.getIds(true);
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    JavaMembers javaMembers = this.members;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (!javaMembers.has(paramString, true))
      if ("__javaObject__".equals(paramString)) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }  
    return bool2;
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    if (paramScriptable instanceof Wrapper && !(paramScriptable instanceof NativeJavaClass)) {
      Object object = ((Wrapper)paramScriptable).unwrap();
      return getClassObject().isInstance(object);
    } 
    return false;
  }
  
  protected void initMembers() {
    Class<?> clazz = (Class)this.javaObject;
    this.members = JavaMembers.lookupClass(this.parent, clazz, clazz, this.isAdapter);
    this.staticFieldAndMethods = this.members.getFieldAndMethodsObjects(this, clazz, true);
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    this.members.put(this, paramString, this.javaObject, paramObject, true);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[JavaClass ");
    stringBuilder.append(getClassObject().getName());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */