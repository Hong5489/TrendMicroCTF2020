package com.trendmicro.classfile;

final class ClassFileField {
  private short itsAttr1;
  
  private short itsAttr2;
  
  private short itsAttr3;
  
  private short itsFlags;
  
  private boolean itsHasAttributes;
  
  private int itsIndex;
  
  private short itsNameIndex;
  
  private short itsTypeIndex;
  
  ClassFileField(short paramShort1, short paramShort2, short paramShort3) {
    this.itsNameIndex = (short)paramShort1;
    this.itsTypeIndex = (short)paramShort2;
    this.itsFlags = (short)paramShort3;
    this.itsHasAttributes = false;
  }
  
  int getWriteSize() {
    int i;
    if (!this.itsHasAttributes) {
      i = 6 + 2;
    } else {
      i = 6 + 10;
    } 
    return i;
  }
  
  void setAttributes(short paramShort1, short paramShort2, short paramShort3, int paramInt) {
    this.itsHasAttributes = true;
    this.itsAttr1 = (short)paramShort1;
    this.itsAttr2 = (short)paramShort2;
    this.itsAttr3 = (short)paramShort3;
    this.itsIndex = paramInt;
  }
  
  int write(byte[] paramArrayOfbyte, int paramInt) {
    paramInt = ClassFileWriter.putInt16(this.itsFlags, paramArrayOfbyte, paramInt);
    paramInt = ClassFileWriter.putInt16(this.itsNameIndex, paramArrayOfbyte, paramInt);
    paramInt = ClassFileWriter.putInt16(this.itsTypeIndex, paramArrayOfbyte, paramInt);
    if (!this.itsHasAttributes) {
      paramInt = ClassFileWriter.putInt16(0, paramArrayOfbyte, paramInt);
    } else {
      paramInt = ClassFileWriter.putInt16(1, paramArrayOfbyte, paramInt);
      paramInt = ClassFileWriter.putInt16(this.itsAttr1, paramArrayOfbyte, paramInt);
      paramInt = ClassFileWriter.putInt16(this.itsAttr2, paramArrayOfbyte, paramInt);
      paramInt = ClassFileWriter.putInt16(this.itsAttr3, paramArrayOfbyte, paramInt);
      paramInt = ClassFileWriter.putInt16(this.itsIndex, paramArrayOfbyte, paramInt);
    } 
    return paramInt;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/ClassFileField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */