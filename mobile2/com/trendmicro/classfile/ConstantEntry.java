package com.trendmicro.classfile;

final class ConstantEntry {
  private int hashcode;
  
  private int intval;
  
  private long longval;
  
  private String str1;
  
  private String str2;
  
  private int type;
  
  ConstantEntry(int paramInt1, int paramInt2, String paramString1, String paramString2) {
    this.type = paramInt1;
    this.intval = paramInt2;
    this.str1 = paramString1;
    this.str2 = paramString2;
    this.hashcode = paramString1.hashCode() * paramString2.hashCode() + paramInt2 ^ paramInt1;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof ConstantEntry;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    int i = this.type;
    if (i != ((ConstantEntry)paramObject).type)
      return false; 
    if (i != 3 && i != 4) {
      if (i != 5 && i != 6) {
        if (i != 12) {
          if (i == 18) {
            if (this.intval == ((ConstantEntry)paramObject).intval && this.str1.equals(((ConstantEntry)paramObject).str1) && this.str2.equals(((ConstantEntry)paramObject).str2))
              bool4 = true; 
            return bool4;
          } 
          throw new RuntimeException("unsupported constant type");
        } 
        bool4 = bool1;
        if (this.str1.equals(((ConstantEntry)paramObject).str1)) {
          bool4 = bool1;
          if (this.str2.equals(((ConstantEntry)paramObject).str2))
            bool4 = true; 
        } 
        return bool4;
      } 
      bool4 = bool2;
      if (this.longval == ((ConstantEntry)paramObject).longval)
        bool4 = true; 
      return bool4;
    } 
    bool4 = bool3;
    if (this.intval == ((ConstantEntry)paramObject).intval)
      bool4 = true; 
    return bool4;
  }
  
  public int hashCode() {
    return this.hashcode;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/ConstantEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */