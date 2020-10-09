package com.trendmicro.classfile;

final class TypeInfo {
  static final int DOUBLE = 3;
  
  static final int FLOAT = 2;
  
  static final int INTEGER = 1;
  
  static final int LONG = 4;
  
  static final int NULL = 5;
  
  static final int OBJECT_TAG = 7;
  
  static final int TOP = 0;
  
  static final int UNINITIALIZED_THIS = 6;
  
  static final int UNINITIALIZED_VAR_TAG = 8;
  
  static final int OBJECT(int paramInt) {
    return (0xFFFF & paramInt) << 8 | 0x7;
  }
  
  static final int OBJECT(String paramString, ConstantPool paramConstantPool) {
    return OBJECT(paramConstantPool.addClass(paramString));
  }
  
  static final int UNINITIALIZED_VARIABLE(int paramInt) {
    return (0xFFFF & paramInt) << 8 | 0x8;
  }
  
  static final int fromType(String paramString, ConstantPool paramConstantPool) {
    if (paramString.length() == 1) {
      char c = paramString.charAt(0);
      if (c != 'F') {
        if (c != 'S' && c != 'Z' && c != 'I')
          if (c != 'J') {
            switch (c) {
              default:
                throw new IllegalArgumentException("bad type");
              case 'D':
                return 3;
              case 'B':
              case 'C':
                break;
            } 
          } else {
            return 4;
          }  
        return 1;
      } 
      return 2;
    } 
    return OBJECT(paramString, paramConstantPool);
  }
  
  private static Class<?> getClassFromInternalName(String paramString) {
    try {
      return Class.forName(paramString.replace('/', '.'));
    } catch (ClassNotFoundException classNotFoundException) {
      throw new RuntimeException(classNotFoundException);
    } 
  }
  
  static final int getPayload(int paramInt) {
    return paramInt >>> 8;
  }
  
  static final String getPayloadAsType(int paramInt, ConstantPool paramConstantPool) {
    if (getTag(paramInt) == 7)
      return (String)paramConstantPool.getConstantData(getPayload(paramInt)); 
    throw new IllegalArgumentException("expecting object type");
  }
  
  static final int getTag(int paramInt) {
    return paramInt & 0xFF;
  }
  
  static boolean isTwoWords(int paramInt) {
    return (paramInt == 3 || paramInt == 4);
  }
  
  static int merge(int paramInt1, int paramInt2, ConstantPool paramConstantPool) {
    boolean bool2;
    int i = getTag(paramInt1);
    int j = getTag(paramInt2);
    boolean bool1 = true;
    if (i == 7) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (j != 7)
      bool1 = false; 
    if (paramInt1 == paramInt2 || (bool2 && paramInt2 == 5))
      return paramInt1; 
    if (i == 0 || j == 0)
      return 0; 
    if (paramInt1 == 5 && bool1)
      return paramInt2; 
    if (bool2 && bool1) {
      String str1 = getPayloadAsType(paramInt1, paramConstantPool);
      String str2 = getPayloadAsType(paramInt2, paramConstantPool);
      String str3 = (String)paramConstantPool.getConstantData(2);
      String str4 = (String)paramConstantPool.getConstantData(4);
      String str5 = str1;
      if (str1.equals(str3))
        str5 = str4; 
      str1 = str2;
      if (str2.equals(str3))
        str1 = str4; 
      Class<?> clazz2 = getClassFromInternalName(str5);
      Class<?> clazz1 = getClassFromInternalName(str1);
      if (clazz2.isAssignableFrom(clazz1))
        return paramInt1; 
      if (clazz1.isAssignableFrom(clazz2))
        return paramInt2; 
      if (clazz1.isInterface() || clazz2.isInterface())
        return OBJECT("java/lang/Object", paramConstantPool); 
      for (clazz1 = clazz1.getSuperclass(); clazz1 != null; clazz1 = clazz1.getSuperclass()) {
        if (clazz1.isAssignableFrom(clazz2))
          return OBJECT(ClassFileWriter.getSlashedForm(clazz1.getName()), paramConstantPool); 
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bad merge attempt between ");
    stringBuilder.append(toString(paramInt1, paramConstantPool));
    stringBuilder.append(" and ");
    stringBuilder.append(toString(paramInt2, paramConstantPool));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  static void print(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, ConstantPool paramConstantPool) {
    System.out.print("locals: ");
    System.out.println(toString(paramArrayOfint1, paramInt1, paramConstantPool));
    System.out.print("stack: ");
    System.out.println(toString(paramArrayOfint2, paramInt2, paramConstantPool));
    System.out.println();
  }
  
  static void print(int[] paramArrayOfint1, int[] paramArrayOfint2, ConstantPool paramConstantPool) {
    print(paramArrayOfint1, paramArrayOfint1.length, paramArrayOfint2, paramArrayOfint2.length, paramConstantPool);
  }
  
  static String toString(int paramInt, ConstantPool paramConstantPool) {
    int i = getTag(paramInt);
    switch (i) {
      default:
        if (i == 7)
          return getPayloadAsType(paramInt, paramConstantPool); 
        break;
      case 6:
        return "uninitialized_this";
      case 5:
        return "null";
      case 4:
        return "long";
      case 3:
        return "double";
      case 2:
        return "float";
      case 1:
        return "int";
      case 0:
        return "top";
    } 
    if (i == 8)
      return "uninitialized"; 
    throw new IllegalArgumentException("bad type");
  }
  
  private static String toString(int[] paramArrayOfint, int paramInt, ConstantPool paramConstantPool) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    for (byte b = 0; b < paramInt; b++) {
      if (b > 0)
        stringBuilder.append(", "); 
      stringBuilder.append(toString(paramArrayOfint[b], paramConstantPool));
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/TypeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */