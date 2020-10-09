package com.trendmicro.hippo.typedarrays;

import com.trendmicro.hippo.ScriptRuntime;

public class Conversions {
  public static int toInt16(Object paramObject) {
    return (short)ScriptRuntime.toInt32(paramObject);
  }
  
  public static int toInt32(Object paramObject) {
    return ScriptRuntime.toInt32(paramObject);
  }
  
  public static int toInt8(Object paramObject) {
    return (byte)ScriptRuntime.toInt32(paramObject);
  }
  
  public static int toUint16(Object paramObject) {
    return ScriptRuntime.toInt32(paramObject) & 0xFFFF;
  }
  
  public static long toUint32(Object paramObject) {
    return ScriptRuntime.toUint32(paramObject);
  }
  
  public static int toUint8(Object paramObject) {
    return ScriptRuntime.toInt32(paramObject) & 0xFF;
  }
  
  public static int toUint8Clamp(Object paramObject) {
    double d1 = ScriptRuntime.toNumber(paramObject);
    if (d1 <= 0.0D)
      return 0; 
    if (d1 >= 255.0D)
      return 255; 
    double d2 = Math.floor(d1);
    return (d2 + 0.5D < d1) ? (int)(1.0D + d2) : ((d1 < 0.5D + d2) ? (int)d2 : (((int)d2 % 2 != 0) ? ((int)d2 + 1) : (int)d2));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/Conversions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */