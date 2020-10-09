package com.trendmicro.classfile;

final class ExceptionTableEntry {
  short itsCatchType;
  
  int itsEndLabel;
  
  int itsHandlerLabel;
  
  int itsStartLabel;
  
  ExceptionTableEntry(int paramInt1, int paramInt2, int paramInt3, short paramShort) {
    this.itsStartLabel = paramInt1;
    this.itsEndLabel = paramInt2;
    this.itsHandlerLabel = paramInt3;
    this.itsCatchType = (short)paramShort;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/ExceptionTableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */