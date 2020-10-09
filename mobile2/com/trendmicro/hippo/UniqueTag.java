package com.trendmicro.hippo;

import java.io.Serializable;

public final class UniqueTag implements Serializable {
  public static final UniqueTag DOUBLE_MARK;
  
  private static final int ID_DOUBLE_MARK = 3;
  
  private static final int ID_NOT_FOUND = 1;
  
  private static final int ID_NULL_VALUE = 2;
  
  public static final UniqueTag NOT_FOUND = new UniqueTag(1);
  
  public static final UniqueTag NULL_VALUE = new UniqueTag(2);
  
  private static final long serialVersionUID = -4320556826714577259L;
  
  private final int tagId;
  
  static {
    DOUBLE_MARK = new UniqueTag(3);
  }
  
  private UniqueTag(int paramInt) {
    this.tagId = paramInt;
  }
  
  public Object readResolve() {
    int i = this.tagId;
    if (i != 1) {
      if (i != 2) {
        if (i == 3)
          return DOUBLE_MARK; 
        throw new IllegalStateException(String.valueOf(this.tagId));
      } 
      return NULL_VALUE;
    } 
    return NOT_FOUND;
  }
  
  public String toString() {
    String str;
    int i = this.tagId;
    if (i != 1) {
      if (i != 2) {
        if (i == 3) {
          str = "DOUBLE_MARK";
        } else {
          throw Kit.codeBug();
        } 
      } else {
        str = "NULL_VALUE";
      } 
    } else {
      str = "NOT_FOUND";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append(": ");
    stringBuilder.append(str);
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/UniqueTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */