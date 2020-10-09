package com.trendmicro.hippo.v8dtoa;

public class DoubleHelper {
  private static final int kDenormalExponent = -1074;
  
  private static final int kExponentBias = 1075;
  
  static final long kExponentMask = 9218868437227405312L;
  
  static final long kHiddenBit = 4503599627370496L;
  
  static final long kSignMask = -9223372036854775808L;
  
  static final long kSignificandMask = 4503599627370495L;
  
  private static final int kSignificandSize = 52;
  
  static DiyFp asDiyFp(long paramLong) {
    return new DiyFp(significand(paramLong), exponent(paramLong));
  }
  
  static DiyFp asNormalizedDiyFp(long paramLong) {
    long l = significand(paramLong);
    int i = exponent(paramLong);
    paramLong = l;
    while ((0x10000000000000L & paramLong) == 0L) {
      paramLong <<= 1L;
      i--;
    } 
    return new DiyFp(paramLong << 11L, i - 11);
  }
  
  static int exponent(long paramLong) {
    return isDenormal(paramLong) ? -1074 : ((int)((0x7FF0000000000000L & paramLong) >>> 52L & 0xFFFFFFFFL) - 1075);
  }
  
  static boolean isDenormal(long paramLong) {
    boolean bool;
    if ((0x7FF0000000000000L & paramLong) == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isInfinite(long paramLong) {
    boolean bool;
    if ((paramLong & 0x7FF0000000000000L) == 9218868437227405312L && (0xFFFFFFFFFFFFFL & paramLong) == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isNan(long paramLong) {
    boolean bool;
    if ((paramLong & 0x7FF0000000000000L) == 9218868437227405312L && (0xFFFFFFFFFFFFFL & paramLong) != 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isSpecial(long paramLong) {
    boolean bool;
    if ((paramLong & 0x7FF0000000000000L) == 9218868437227405312L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static void normalizedBoundaries(long paramLong, DiyFp paramDiyFp1, DiyFp paramDiyFp2) {
    boolean bool;
    DiyFp diyFp = asDiyFp(paramLong);
    if (diyFp.f() == 4503599627370496L) {
      bool = true;
    } else {
      bool = false;
    } 
    paramDiyFp2.setF((diyFp.f() << 1L) + 1L);
    paramDiyFp2.setE(diyFp.e() - 1);
    paramDiyFp2.normalize();
    if (bool && diyFp.e() != -1074) {
      paramDiyFp1.setF((diyFp.f() << 2L) - 1L);
      paramDiyFp1.setE(diyFp.e() - 2);
    } else {
      paramDiyFp1.setF((diyFp.f() << 1L) - 1L);
      paramDiyFp1.setE(diyFp.e() - 1);
    } 
    paramDiyFp1.setF(paramDiyFp1.f() << paramDiyFp1.e() - paramDiyFp2.e());
    paramDiyFp1.setE(paramDiyFp2.e());
  }
  
  static int sign(long paramLong) {
    byte b;
    if ((Long.MIN_VALUE & paramLong) == 0L) {
      b = 1;
    } else {
      b = -1;
    } 
    return b;
  }
  
  static long significand(long paramLong) {
    long l = 0xFFFFFFFFFFFFFL & paramLong;
    return !isDenormal(paramLong) ? (4503599627370496L + l) : l;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/v8dtoa/DoubleHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */