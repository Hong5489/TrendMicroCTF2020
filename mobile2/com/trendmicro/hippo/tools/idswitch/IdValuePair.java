package com.trendmicro.hippo.tools.idswitch;

public class IdValuePair {
  public final String id;
  
  public final int idLength;
  
  private int lineNumber;
  
  public final String value;
  
  public IdValuePair(String paramString1, String paramString2) {
    this.idLength = paramString1.length();
    this.id = paramString1;
    this.value = paramString2;
  }
  
  public int getLineNumber() {
    return this.lineNumber;
  }
  
  public void setLineNumber(int paramInt) {
    this.lineNumber = paramInt;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/idswitch/IdValuePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */