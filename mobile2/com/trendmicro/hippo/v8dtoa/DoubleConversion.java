package com.trendmicro.hippo.v8dtoa;

public final class DoubleConversion {
  private static final int kDenormalExponent = -1074;
  
  private static final int kExponentBias = 1075;
  
  private static final long kExponentMask = 9218868437227405312L;
  
  private static final long kHiddenBit = 4503599627370496L;
  
  private static final int kPhysicalSignificandSize = 52;
  
  private static final long kSignMask = -9223372036854775808L;
  
  private static final long kSignificandMask = 4503599627370495L;
  
  private static final int kSignificandSize = 53;
  
  public static int doubleToInt32(double paramDouble) {
    int i = (int)paramDouble;
    if (i == paramDouble)
      return i; 
    long l1 = Double.doubleToLongBits(paramDouble);
    i = exponent(l1);
    if (i <= -53 || i > 31)
      return 0; 
    long l2 = significand(l1);
    int j = sign(l1);
    if (i < 0) {
      l1 = l2 >> -i;
    } else {
      l1 = l2 << i;
    } 
    return j * (int)l1;
  }
  
  private static int exponent(long paramLong) {
    return isDenormal(paramLong) ? -1074 : ((int)((0x7FF0000000000000L & paramLong) >> 52L) - 1075);
  }
  
  private static boolean isDenormal(long paramLong) {
    boolean bool;
    if ((0x7FF0000000000000L & paramLong) == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static int sign(long paramLong) {
    byte b;
    if ((Long.MIN_VALUE & paramLong) == 0L) {
      b = 1;
    } else {
      b = -1;
    } 
    return b;
  }
  
  private static long significand(long paramLong) {
    long l = 0xFFFFFFFFFFFFFL & paramLong;
    return !isDenormal(paramLong) ? (4503599627370496L + l) : l;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/v8dtoa/DoubleConversion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */