package com.trendmicro.classfile;

import com.trendmicro.hippo.ObjToIntMap;
import com.trendmicro.hippo.UintMap;

final class ConstantPool {
  static final byte CONSTANT_Class = 7;
  
  static final byte CONSTANT_Double = 6;
  
  static final byte CONSTANT_Fieldref = 9;
  
  static final byte CONSTANT_Float = 4;
  
  static final byte CONSTANT_Integer = 3;
  
  static final byte CONSTANT_InterfaceMethodref = 11;
  
  static final byte CONSTANT_InvokeDynamic = 18;
  
  static final byte CONSTANT_Long = 5;
  
  static final byte CONSTANT_MethodHandle = 15;
  
  static final byte CONSTANT_MethodType = 16;
  
  static final byte CONSTANT_Methodref = 10;
  
  static final byte CONSTANT_NameAndType = 12;
  
  static final byte CONSTANT_String = 8;
  
  static final byte CONSTANT_Utf8 = 1;
  
  private static final int ConstantPoolSize = 256;
  
  private static final int MAX_UTF_ENCODING_SIZE = 65535;
  
  private ClassFileWriter cfw;
  
  private ObjToIntMap itsClassHash = new ObjToIntMap();
  
  private UintMap itsConstantData = new UintMap();
  
  private ObjToIntMap itsConstantHash = new ObjToIntMap();
  
  private ObjToIntMap itsFieldRefHash = new ObjToIntMap();
  
  private ObjToIntMap itsMethodRefHash = new ObjToIntMap();
  
  private byte[] itsPool;
  
  private UintMap itsPoolTypes = new UintMap();
  
  private UintMap itsStringConstHash = new UintMap();
  
  private int itsTop;
  
  private int itsTopIndex;
  
  private ObjToIntMap itsUtf8Hash = new ObjToIntMap();
  
  ConstantPool(ClassFileWriter paramClassFileWriter) {
    this.cfw = paramClassFileWriter;
    this.itsTopIndex = 1;
    this.itsPool = new byte[256];
    this.itsTop = 0;
  }
  
  private short addNameAndType(String paramString1, String paramString2) {
    short s1 = addUtf8(paramString1);
    short s2 = addUtf8(paramString2);
    ensure(5);
    byte[] arrayOfByte = this.itsPool;
    int k = this.itsTop;
    int m = k + 1;
    this.itsTop = m;
    arrayOfByte[k] = (byte)12;
    int i = ClassFileWriter.putInt16(s1, arrayOfByte, m);
    this.itsTop = i;
    this.itsTop = ClassFileWriter.putInt16(s2, this.itsPool, i);
    this.itsPoolTypes.put(this.itsTopIndex, 12);
    int j = this.itsTopIndex;
    this.itsTopIndex = j + 1;
    return (short)j;
  }
  
  private void ensure(int paramInt) {
    int i = this.itsTop;
    byte[] arrayOfByte = this.itsPool;
    if (i + paramInt > arrayOfByte.length) {
      int j = arrayOfByte.length * 2;
      int k = j;
      if (i + paramInt > j)
        k = i + paramInt; 
      arrayOfByte = new byte[k];
      System.arraycopy(this.itsPool, 0, arrayOfByte, 0, this.itsTop);
      this.itsPool = arrayOfByte;
    } 
  }
  
  short addClass(String paramString) {
    int i = this.itsClassHash.get(paramString, -1);
    int j = i;
    if (i == -1) {
      String str = paramString;
      if (paramString.indexOf('.') > 0) {
        String str1 = ClassFileWriter.getSlashedForm(paramString);
        j = this.itsClassHash.get(str1, -1);
        i = j;
        str = str1;
        if (j != -1) {
          this.itsClassHash.put(paramString, j);
          str = str1;
          i = j;
        } 
      } 
      j = i;
      if (i == -1) {
        short s = addUtf8(str);
        ensure(3);
        byte[] arrayOfByte = this.itsPool;
        j = this.itsTop;
        i = j + 1;
        this.itsTop = i;
        arrayOfByte[j] = (byte)7;
        this.itsTop = ClassFileWriter.putInt16(s, arrayOfByte, i);
        i = this.itsTopIndex;
        this.itsTopIndex = i + 1;
        this.itsClassHash.put(str, i);
        j = i;
        if (!paramString.equals(str)) {
          this.itsClassHash.put(paramString, i);
          j = i;
        } 
      } 
    } 
    setConstantData(j, paramString);
    this.itsPoolTypes.put(j, 7);
    return (short)j;
  }
  
  int addConstant(double paramDouble) {
    ensure(9);
    byte[] arrayOfByte = this.itsPool;
    int i = this.itsTop;
    this.itsTop = i + 1;
    arrayOfByte[i] = (byte)6;
    this.itsTop = ClassFileWriter.putInt64(Double.doubleToLongBits(paramDouble), this.itsPool, this.itsTop);
    i = this.itsTopIndex;
    this.itsTopIndex += 2;
    this.itsPoolTypes.put(i, 6);
    return i;
  }
  
  int addConstant(float paramFloat) {
    ensure(5);
    byte[] arrayOfByte = this.itsPool;
    int i = this.itsTop;
    this.itsTop = i + 1;
    arrayOfByte[i] = (byte)4;
    this.itsTop = ClassFileWriter.putInt32(Float.floatToIntBits(paramFloat), this.itsPool, this.itsTop);
    this.itsPoolTypes.put(this.itsTopIndex, 4);
    i = this.itsTopIndex;
    this.itsTopIndex = i + 1;
    return i;
  }
  
  int addConstant(int paramInt) {
    ensure(5);
    byte[] arrayOfByte = this.itsPool;
    int i = this.itsTop;
    int j = i + 1;
    this.itsTop = j;
    arrayOfByte[i] = (byte)3;
    this.itsTop = ClassFileWriter.putInt32(paramInt, arrayOfByte, j);
    this.itsPoolTypes.put(this.itsTopIndex, 3);
    paramInt = this.itsTopIndex;
    this.itsTopIndex = paramInt + 1;
    return (short)paramInt;
  }
  
  int addConstant(long paramLong) {
    ensure(9);
    byte[] arrayOfByte = this.itsPool;
    int i = this.itsTop;
    int j = i + 1;
    this.itsTop = j;
    arrayOfByte[i] = (byte)5;
    this.itsTop = ClassFileWriter.putInt64(paramLong, arrayOfByte, j);
    j = this.itsTopIndex;
    this.itsTopIndex += 2;
    this.itsPoolTypes.put(j, 5);
    return j;
  }
  
  int addConstant(Object paramObject) {
    if (paramObject instanceof Integer || paramObject instanceof Byte || paramObject instanceof Short)
      return addConstant(((Number)paramObject).intValue()); 
    if (paramObject instanceof Character)
      return addConstant(((Character)paramObject).charValue()); 
    if (paramObject instanceof Boolean)
      return addConstant(((Boolean)paramObject).booleanValue()); 
    if (paramObject instanceof Float)
      return addConstant(((Float)paramObject).floatValue()); 
    if (paramObject instanceof Long)
      return addConstant(((Long)paramObject).longValue()); 
    if (paramObject instanceof Double)
      return addConstant(((Double)paramObject).doubleValue()); 
    if (paramObject instanceof String)
      return addConstant((String)paramObject); 
    if (paramObject instanceof ClassFileWriter.MHandle)
      return addMethodHandle((ClassFileWriter.MHandle)paramObject); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("value ");
    stringBuilder.append(paramObject);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  int addConstant(String paramString) {
    int i = addUtf8(paramString) & 0xFFFF;
    int j = this.itsStringConstHash.getInt(i, -1);
    int k = j;
    if (j == -1) {
      k = this.itsTopIndex;
      this.itsTopIndex = k + 1;
      ensure(3);
      byte[] arrayOfByte = this.itsPool;
      j = this.itsTop;
      int m = j + 1;
      this.itsTop = m;
      arrayOfByte[j] = (byte)8;
      this.itsTop = ClassFileWriter.putInt16(i, arrayOfByte, m);
      this.itsStringConstHash.put(i, k);
    } 
    this.itsPoolTypes.put(k, 8);
    return k;
  }
  
  short addFieldRef(String paramString1, String paramString2, String paramString3) {
    FieldOrMethodRef fieldOrMethodRef = new FieldOrMethodRef(paramString1, paramString2, paramString3);
    int i = this.itsFieldRefHash.get(fieldOrMethodRef, -1);
    int j = i;
    if (i == -1) {
      j = addNameAndType(paramString2, paramString3);
      short s = addClass(paramString1);
      ensure(5);
      byte[] arrayOfByte = this.itsPool;
      i = this.itsTop;
      int k = i + 1;
      this.itsTop = k;
      arrayOfByte[i] = (byte)9;
      i = ClassFileWriter.putInt16(s, arrayOfByte, k);
      this.itsTop = i;
      this.itsTop = ClassFileWriter.putInt16(j, this.itsPool, i);
      j = this.itsTopIndex;
      this.itsTopIndex = j + 1;
      this.itsFieldRefHash.put(fieldOrMethodRef, j);
    } 
    setConstantData(j, fieldOrMethodRef);
    this.itsPoolTypes.put(j, 9);
    return (short)j;
  }
  
  short addInterfaceMethodRef(String paramString1, String paramString2, String paramString3) {
    short s1 = addNameAndType(paramString2, paramString3);
    short s2 = addClass(paramString1);
    ensure(5);
    byte[] arrayOfByte = this.itsPool;
    int j = this.itsTop;
    int k = j + 1;
    this.itsTop = k;
    arrayOfByte[j] = (byte)11;
    k = ClassFileWriter.putInt16(s2, arrayOfByte, k);
    this.itsTop = k;
    this.itsTop = ClassFileWriter.putInt16(s1, this.itsPool, k);
    FieldOrMethodRef fieldOrMethodRef = new FieldOrMethodRef(paramString1, paramString2, paramString3);
    setConstantData(this.itsTopIndex, fieldOrMethodRef);
    this.itsPoolTypes.put(this.itsTopIndex, 11);
    int i = this.itsTopIndex;
    this.itsTopIndex = i + 1;
    return (short)i;
  }
  
  short addInvokeDynamic(String paramString1, String paramString2, int paramInt) {
    ConstantEntry constantEntry = new ConstantEntry(18, paramInt, paramString1, paramString2);
    int i = this.itsConstantHash.get(constantEntry, -1);
    int j = i;
    if (i == -1) {
      j = addNameAndType(paramString1, paramString2);
      ensure(5);
      byte[] arrayOfByte = this.itsPool;
      int k = this.itsTop;
      i = k + 1;
      this.itsTop = i;
      arrayOfByte[k] = (byte)18;
      paramInt = ClassFileWriter.putInt16(paramInt, arrayOfByte, i);
      this.itsTop = paramInt;
      this.itsTop = ClassFileWriter.putInt16(j, this.itsPool, paramInt);
      j = this.itsTopIndex;
      this.itsTopIndex = j + 1;
      this.itsConstantHash.put(constantEntry, j);
      setConstantData(j, paramString2);
      this.itsPoolTypes.put(j, 18);
    } 
    return (short)j;
  }
  
  short addMethodHandle(ClassFileWriter.MHandle paramMHandle) {
    int i = this.itsConstantHash.get(paramMHandle, -1);
    int j = i;
    if (i == -1) {
      if (paramMHandle.tag <= 4) {
        j = addFieldRef(paramMHandle.owner, paramMHandle.name, paramMHandle.desc);
      } else if (paramMHandle.tag == 9) {
        j = addInterfaceMethodRef(paramMHandle.owner, paramMHandle.name, paramMHandle.desc);
      } else {
        j = addMethodRef(paramMHandle.owner, paramMHandle.name, paramMHandle.desc);
      } 
      ensure(4);
      byte[] arrayOfByte = this.itsPool;
      i = this.itsTop;
      int k = i + 1;
      this.itsTop = k;
      arrayOfByte[i] = (byte)15;
      this.itsTop = k + 1;
      arrayOfByte[k] = (byte)paramMHandle.tag;
      this.itsTop = ClassFileWriter.putInt16(j, this.itsPool, this.itsTop);
      j = this.itsTopIndex;
      this.itsTopIndex = j + 1;
      this.itsConstantHash.put(paramMHandle, j);
      this.itsPoolTypes.put(j, 15);
    } 
    return (short)j;
  }
  
  short addMethodRef(String paramString1, String paramString2, String paramString3) {
    FieldOrMethodRef fieldOrMethodRef = new FieldOrMethodRef(paramString1, paramString2, paramString3);
    int i = this.itsMethodRefHash.get(fieldOrMethodRef, -1);
    int j = i;
    if (i == -1) {
      j = addNameAndType(paramString2, paramString3);
      short s = addClass(paramString1);
      ensure(5);
      byte[] arrayOfByte = this.itsPool;
      i = this.itsTop;
      int k = i + 1;
      this.itsTop = k;
      arrayOfByte[i] = (byte)10;
      i = ClassFileWriter.putInt16(s, arrayOfByte, k);
      this.itsTop = i;
      this.itsTop = ClassFileWriter.putInt16(j, this.itsPool, i);
      j = this.itsTopIndex;
      this.itsTopIndex = j + 1;
      this.itsMethodRefHash.put(fieldOrMethodRef, j);
    } 
    setConstantData(j, fieldOrMethodRef);
    this.itsPoolTypes.put(j, 10);
    return (short)j;
  }
  
  short addUtf8(String paramString) {
    int i = this.itsUtf8Hash.get(paramString, -1);
    int j = i;
    if (i == -1) {
      int k = paramString.length();
      if (k > 65535) {
        boolean bool = true;
        j = i;
        i = bool;
      } else {
        ensure(k * 3 + 3);
        j = this.itsTop;
        this.itsPool[j] = (byte)1;
        j = j + 1 + 2;
        char[] arrayOfChar = this.cfw.getCharBuffer(k);
        paramString.getChars(0, k, arrayOfChar, 0);
        int m;
        for (m = 0; m != k; m++) {
          char c = arrayOfChar[m];
          if (c != '\000' && c <= '') {
            this.itsPool[j] = (byte)(byte)c;
            j++;
          } else if (c > '߿') {
            byte[] arrayOfByte = this.itsPool;
            int n = j + 1;
            arrayOfByte[j] = (byte)(byte)(c >> 12 | 0xE0);
            j = n + 1;
            arrayOfByte[n] = (byte)(byte)(c >> 6 & 0x3F | 0x80);
            arrayOfByte[j] = (byte)(byte)(c & 0x3F | 0x80);
            j++;
          } else {
            byte[] arrayOfByte = this.itsPool;
            int n = j + 1;
            arrayOfByte[j] = (byte)(byte)(c >> 6 | 0xC0);
            j = n + 1;
            arrayOfByte[n] = (byte)(byte)(c & 0x3F | 0x80);
          } 
        } 
        m = this.itsTop;
        k = j - m + 1 + 2;
        if (k > 65535) {
          m = 1;
          j = i;
          i = m;
        } else {
          byte[] arrayOfByte = this.itsPool;
          arrayOfByte[m + 1] = (byte)(byte)(k >>> 8);
          arrayOfByte[m + 2] = (byte)(byte)k;
          this.itsTop = j;
          j = this.itsTopIndex;
          this.itsTopIndex = j + 1;
          this.itsUtf8Hash.put(paramString, j);
          i = 0;
        } 
      } 
      if (i != 0)
        throw new IllegalArgumentException("Too big string"); 
    } 
    setConstantData(j, paramString);
    this.itsPoolTypes.put(j, 1);
    return (short)j;
  }
  
  Object getConstantData(int paramInt) {
    return this.itsConstantData.getObject(paramInt);
  }
  
  byte getConstantType(int paramInt) {
    return (byte)this.itsPoolTypes.getInt(paramInt, 0);
  }
  
  int getUtfEncodingLimit(String paramString, int paramInt1, int paramInt2) {
    if ((paramInt2 - paramInt1) * 3 <= 65535)
      return paramInt2; 
    char c = '￿';
    int i = paramInt1;
    paramInt1 = c;
    while (i != paramInt2) {
      c = paramString.charAt(i);
      if (c != '\000' && c <= '') {
        paramInt1--;
      } else if (c < '߿') {
        paramInt1 -= 2;
      } else {
        paramInt1 -= 3;
      } 
      if (paramInt1 < 0)
        return i; 
      i++;
    } 
    return paramInt2;
  }
  
  int getWriteSize() {
    return this.itsTop + 2;
  }
  
  boolean isUnderUtfEncodingLimit(String paramString) {
    int i = paramString.length();
    boolean bool = true;
    if (i * 3 <= 65535)
      return true; 
    if (i > 65535)
      return false; 
    if (i != getUtfEncodingLimit(paramString, 0, i))
      bool = false; 
    return bool;
  }
  
  void setConstantData(int paramInt, Object paramObject) {
    this.itsConstantData.put(paramInt, paramObject);
  }
  
  int write(byte[] paramArrayOfbyte, int paramInt) {
    paramInt = ClassFileWriter.putInt16((short)this.itsTopIndex, paramArrayOfbyte, paramInt);
    System.arraycopy(this.itsPool, 0, paramArrayOfbyte, paramInt, this.itsTop);
    return paramInt + this.itsTop;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/ConstantPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */