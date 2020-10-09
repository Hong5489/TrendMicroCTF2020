package com.trendmicro.hippo;

public enum StackStyle {
  HIPPO, TRENDMICRO, V8;
  
  static {
    StackStyle stackStyle = new StackStyle("V8", 2);
    V8 = stackStyle;
    $VALUES = new StackStyle[] { HIPPO, TRENDMICRO, stackStyle };
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/StackStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */