package com.trendmicro.classfile;

final class ClassFileMethod {
  private byte[] itsCodeAttribute;
  
  private short itsFlags;
  
  private String itsName;
  
  private short itsNameIndex;
  
  private String itsType;
  
  private short itsTypeIndex;
  
  ClassFileMethod(String paramString1, short paramShort1, String paramString2, short paramShort2, short paramShort3) {
    this.itsName = paramString1;
    this.itsNameIndex = (short)paramShort1;
    this.itsType = paramString2;
    this.itsTypeIndex = (short)paramShort2;
    this.itsFlags = (short)paramShort3;
  }
  
  short getFlags() {
    return this.itsFlags;
  }
  
  String getName() {
    return this.itsName;
  }
  
  String getType() {
    return this.itsType;
  }
  
  int getWriteSize() {
    return this.itsCodeAttribute.length + 8;
  }
  
  void setCodeAttribute(byte[] paramArrayOfbyte) {
    this.itsCodeAttribute = paramArrayOfbyte;
  }
  
  int write(byte[] paramArrayOfbyte, int paramInt) {
    paramInt = ClassFileWriter.putInt16(this.itsFlags, paramArrayOfbyte, paramInt);
    paramInt = ClassFileWriter.putInt16(this.itsNameIndex, paramArrayOfbyte, paramInt);
    paramInt = ClassFileWriter.putInt16(1, paramArrayOfbyte, ClassFileWriter.putInt16(this.itsTypeIndex, paramArrayOfbyte, paramInt));
    byte[] arrayOfByte = this.itsCodeAttribute;
    System.arraycopy(arrayOfByte, 0, paramArrayOfbyte, paramInt, arrayOfByte.length);
    return paramInt + this.itsCodeAttribute.length;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/ClassFileMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */