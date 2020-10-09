package com.trendmicro.hippo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class MemberBox implements Serializable {
  private static final Class<?>[] primitives = new Class[] { boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, void.class };
  
  private static final long serialVersionUID = 6358550398665688245L;
  
  transient Class<?>[] argTypes;
  
  transient Object delegateTo;
  
  private transient Member memberObject;
  
  transient boolean vararg;
  
  MemberBox(Constructor<?> paramConstructor) {
    init(paramConstructor);
  }
  
  MemberBox(Method paramMethod) {
    init(paramMethod);
  }
  
  private void init(Constructor<?> paramConstructor) {
    this.memberObject = paramConstructor;
    this.argTypes = paramConstructor.getParameterTypes();
    this.vararg = paramConstructor.isVarArgs();
  }
  
  private void init(Method paramMethod) {
    this.memberObject = paramMethod;
    this.argTypes = paramMethod.getParameterTypes();
    this.vararg = paramMethod.isVarArgs();
  }
  
  private static Member readMember(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    StringBuilder stringBuilder;
    if (!paramObjectInputStream.readBoolean())
      return null; 
    boolean bool = paramObjectInputStream.readBoolean();
    String str = (String)paramObjectInputStream.readObject();
    Class clazz = (Class)paramObjectInputStream.readObject();
    Class[] arrayOfClass = readParameters(paramObjectInputStream);
    if (bool)
      try {
        return clazz.getMethod(str, arrayOfClass);
      } catch (NoSuchMethodException noSuchMethodException) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot find member: ");
        stringBuilder.append(noSuchMethodException);
        throw new IOException(stringBuilder.toString());
      }  
    return noSuchMethodException.getConstructor((Class<?>[])stringBuilder);
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    Member member = readMember(paramObjectInputStream);
    if (member instanceof Method) {
      init((Method)member);
    } else {
      init((Constructor)member);
    } 
  }
  
  private static Class<?>[] readParameters(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    Class[] arrayOfClass = new Class[paramObjectInputStream.readShort()];
    for (byte b = 0; b < arrayOfClass.length; b++) {
      if (!paramObjectInputStream.readBoolean()) {
        arrayOfClass[b] = (Class)paramObjectInputStream.readObject();
      } else {
        arrayOfClass[b] = primitives[paramObjectInputStream.readByte()];
      } 
    } 
    return arrayOfClass;
  }
  
  private static Method searchAccessibleMethod(Method paramMethod, Class<?>[] paramArrayOfClass) {
    int i = paramMethod.getModifiers();
    if (Modifier.isPublic(i) && !Modifier.isStatic(i)) {
      Class<?> clazz = paramMethod.getDeclaringClass();
      if (!Modifier.isPublic(clazz.getModifiers())) {
        String str = paramMethod.getName();
        Class[] arrayOfClass = clazz.getInterfaces();
        i = 0;
        int j = arrayOfClass.length;
        while (true) {
          Class<?> clazz1 = clazz;
          if (i != j) {
            clazz1 = arrayOfClass[i];
            if (Modifier.isPublic(clazz1.getModifiers()))
              try {
                return clazz1.getMethod(str, paramArrayOfClass);
              } catch (NoSuchMethodException noSuchMethodException) {
              
              } catch (SecurityException securityException) {} 
            i++;
            continue;
          } 
          break;
        } 
        while (true) {
          clazz = securityException.getSuperclass();
          if (clazz == null)
            break; 
          Class<?> clazz1 = clazz;
          if (Modifier.isPublic(clazz.getModifiers())) {
            try {
              Method method = clazz.getMethod(str, paramArrayOfClass);
              i = method.getModifiers();
              if (Modifier.isPublic(i)) {
                boolean bool = Modifier.isStatic(i);
                if (!bool)
                  return method; 
              } 
            } catch (NoSuchMethodException noSuchMethodException) {
            
            } catch (SecurityException securityException1) {}
            clazz1 = clazz;
          } 
        } 
      } 
    } 
    return null;
  }
  
  private static void writeMember(ObjectOutputStream paramObjectOutputStream, Member paramMember) throws IOException {
    if (paramMember == null) {
      paramObjectOutputStream.writeBoolean(false);
      return;
    } 
    paramObjectOutputStream.writeBoolean(true);
    if (paramMember instanceof Method || paramMember instanceof Constructor) {
      paramObjectOutputStream.writeBoolean(paramMember instanceof Method);
      paramObjectOutputStream.writeObject(paramMember.getName());
      paramObjectOutputStream.writeObject(paramMember.getDeclaringClass());
      if (paramMember instanceof Method) {
        writeParameters(paramObjectOutputStream, ((Method)paramMember).getParameterTypes());
      } else {
        writeParameters(paramObjectOutputStream, ((Constructor)paramMember).getParameterTypes());
      } 
      return;
    } 
    throw new IllegalArgumentException("not Method or Constructor");
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    writeMember(paramObjectOutputStream, this.memberObject);
  }
  
  private static void writeParameters(ObjectOutputStream paramObjectOutputStream, Class<?>[] paramArrayOfClass) throws IOException {
    paramObjectOutputStream.writeShort(paramArrayOfClass.length);
    byte b = 0;
    label20: while (b < paramArrayOfClass.length) {
      Class<?> clazz = paramArrayOfClass[b];
      boolean bool = clazz.isPrimitive();
      paramObjectOutputStream.writeBoolean(bool);
      if (!bool) {
        paramObjectOutputStream.writeObject(clazz);
        continue;
      } 
      byte b1 = 0;
      while (true) {
        Class<?>[] arrayOfClass = primitives;
        if (b1 < arrayOfClass.length) {
          if (clazz.equals(arrayOfClass[b1])) {
            paramObjectOutputStream.writeByte(b1);
            b++;
            continue label20;
          } 
          b1++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Primitive ");
        stringBuilder.append(clazz);
        stringBuilder.append(" not found");
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
  }
  
  Constructor<?> ctor() {
    return (Constructor)this.memberObject;
  }
  
  Class<?> getDeclaringClass() {
    return this.memberObject.getDeclaringClass();
  }
  
  String getName() {
    return this.memberObject.getName();
  }
  
  Object invoke(Object paramObject, Object[] paramArrayOfObject) {
    Method method = method();
    try {
      return method.invoke(paramObject, paramArrayOfObject);
    } catch (IllegalAccessException illegalAccessException) {
      Method method1 = searchAccessibleMethod(method, this.argTypes);
      if (method1 != null) {
        this.memberObject = method1;
        method = method1;
      } else if (!VMBridge.instance.tryToMakeAccessible(method)) {
        throw Context.throwAsScriptRuntimeEx(illegalAccessException);
      } 
      return method.invoke(paramObject, paramArrayOfObject);
    } catch (InvocationTargetException null) {
    
    } catch (Exception exception) {}
    while (true) {
      Throwable throwable = ((InvocationTargetException)exception).getTargetException();
      if (!(throwable instanceof InvocationTargetException)) {
        if (throwable instanceof ContinuationPending)
          throw (ContinuationPending)throwable; 
        throw Context.throwAsScriptRuntimeEx(throwable);
      } 
    } 
  }
  
  boolean isCtor() {
    return this.memberObject instanceof Constructor;
  }
  
  boolean isMethod() {
    return this.memberObject instanceof Method;
  }
  
  boolean isPublic() {
    return Modifier.isPublic(this.memberObject.getModifiers());
  }
  
  boolean isStatic() {
    return Modifier.isStatic(this.memberObject.getModifiers());
  }
  
  Member member() {
    return this.memberObject;
  }
  
  Method method() {
    return (Method)this.memberObject;
  }
  
  Object newInstance(Object[] paramArrayOfObject) {
    Constructor<?> constructor = ctor();
    try {
      return constructor.newInstance(paramArrayOfObject);
    } catch (IllegalAccessException illegalAccessException) {
      if (VMBridge.instance.tryToMakeAccessible(constructor))
        return constructor.newInstance(paramArrayOfObject); 
      throw Context.throwAsScriptRuntimeEx(illegalAccessException);
    } catch (Exception exception) {}
    throw Context.throwAsScriptRuntimeEx(exception);
  }
  
  String toJavaDeclaration() {
    StringBuilder stringBuilder = new StringBuilder();
    if (isMethod()) {
      Method method = method();
      stringBuilder.append(method.getReturnType());
      stringBuilder.append(' ');
      stringBuilder.append(method.getName());
    } else {
      String str2 = ctor().getDeclaringClass().getName();
      int i = str2.lastIndexOf('.');
      String str1 = str2;
      if (i >= 0)
        str1 = str2.substring(i + 1); 
      stringBuilder.append(str1);
    } 
    stringBuilder.append(JavaMembers.liveConnectSignature(this.argTypes));
    return stringBuilder.toString();
  }
  
  public String toString() {
    return this.memberObject.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/MemberBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */