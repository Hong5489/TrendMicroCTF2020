package com.trendmicro.hippo.regexp;

public class SubString {
  int index;
  
  int length;
  
  String str;
  
  public SubString() {}
  
  public SubString(String paramString) {
    this.str = paramString;
    this.index = 0;
    this.length = paramString.length();
  }
  
  public SubString(String paramString, int paramInt1, int paramInt2) {
    this.str = paramString;
    this.index = paramInt1;
    this.length = paramInt2;
  }
  
  public String toString() {
    String str = this.str;
    if (str == null) {
      str = "";
    } else {
      int i = this.index;
      str = str.substring(i, this.length + i);
    } 
    return str;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/SubString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */