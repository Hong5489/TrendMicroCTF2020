package com.trendmicro.classfile;

final class SuperBlock {
  private int end;
  
  private int index;
  
  private boolean isInQueue;
  
  private boolean isInitialized;
  
  private int[] locals;
  
  private int[] stack;
  
  private int start;
  
  SuperBlock(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint) {
    this.index = paramInt1;
    this.start = paramInt2;
    this.end = paramInt3;
    int[] arrayOfInt = new int[paramArrayOfint.length];
    this.locals = arrayOfInt;
    System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, paramArrayOfint.length);
    this.stack = new int[0];
    this.isInitialized = false;
    this.isInQueue = false;
  }
  
  private boolean mergeState(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt, ConstantPool paramConstantPool) {
    boolean bool = false;
    for (byte b = 0; b < paramInt; b++) {
      int i = paramArrayOfint1[b];
      paramArrayOfint1[b] = TypeInfo.merge(paramArrayOfint1[b], paramArrayOfint2[b], paramConstantPool);
      if (i != paramArrayOfint1[b])
        bool = true; 
    } 
    return bool;
  }
  
  int getEnd() {
    return this.end;
  }
  
  int getIndex() {
    return this.index;
  }
  
  int[] getLocals() {
    int[] arrayOfInt1 = this.locals;
    int[] arrayOfInt2 = new int[arrayOfInt1.length];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, arrayOfInt1.length);
    return arrayOfInt2;
  }
  
  int[] getStack() {
    int[] arrayOfInt1 = this.stack;
    int[] arrayOfInt2 = new int[arrayOfInt1.length];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, arrayOfInt1.length);
    return arrayOfInt2;
  }
  
  int getStart() {
    return this.start;
  }
  
  int[] getTrimmedLocals() {
    int i = this.locals.length - 1;
    while (i >= 0) {
      int[] arrayOfInt1 = this.locals;
      if (arrayOfInt1[i] == 0 && !TypeInfo.isTwoWords(arrayOfInt1[i - 1]))
        i--; 
    } 
    int j = i + 1;
    int k = j;
    i = 0;
    while (i < j) {
      int m = k;
      if (TypeInfo.isTwoWords(this.locals[i]))
        m = k - 1; 
      i++;
      k = m;
    } 
    int[] arrayOfInt = new int[k];
    byte b = 0;
    for (i = 0; b < k; i = j + 1) {
      int[] arrayOfInt1 = this.locals;
      arrayOfInt[b] = arrayOfInt1[i];
      j = i;
      if (TypeInfo.isTwoWords(arrayOfInt1[i]))
        j = i + 1; 
      b++;
    } 
    return arrayOfInt;
  }
  
  boolean isInQueue() {
    return this.isInQueue;
  }
  
  boolean isInitialized() {
    return this.isInitialized;
  }
  
  boolean merge(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, ConstantPool paramConstantPool) {
    boolean bool = this.isInitialized;
    boolean bool1 = true;
    if (!bool) {
      System.arraycopy(paramArrayOfint1, 0, this.locals, 0, paramInt1);
      paramArrayOfint1 = new int[paramInt2];
      this.stack = paramArrayOfint1;
      System.arraycopy(paramArrayOfint2, 0, paramArrayOfint1, 0, paramInt2);
      this.isInitialized = true;
      return true;
    } 
    int[] arrayOfInt = this.locals;
    if (arrayOfInt.length == paramInt1 && this.stack.length == paramInt2) {
      boolean bool2 = mergeState(arrayOfInt, paramArrayOfint1, paramInt1, paramConstantPool);
      boolean bool3 = mergeState(this.stack, paramArrayOfint2, paramInt2, paramConstantPool);
      bool = bool1;
      if (!bool2)
        if (bool3) {
          bool = bool1;
        } else {
          bool = false;
        }  
      return bool;
    } 
    throw new IllegalArgumentException("bad merge attempt");
  }
  
  void setInQueue(boolean paramBoolean) {
    this.isInQueue = paramBoolean;
  }
  
  void setInitialized(boolean paramBoolean) {
    this.isInitialized = paramBoolean;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("sb ");
    stringBuilder.append(this.index);
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/SuperBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */