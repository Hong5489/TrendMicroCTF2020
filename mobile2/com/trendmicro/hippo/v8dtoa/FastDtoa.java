package com.trendmicro.hippo.v8dtoa;

public class FastDtoa {
  static final int kFastDtoaMaximalLength = 17;
  
  static final int kTen4 = 10000;
  
  static final int kTen5 = 100000;
  
  static final int kTen6 = 1000000;
  
  static final int kTen7 = 10000000;
  
  static final int kTen8 = 100000000;
  
  static final int kTen9 = 1000000000;
  
  static final int maximal_target_exponent = -32;
  
  static final int minimal_target_exponent = -60;
  
  static long biggestPowerTen(int paramInt1, int paramInt2) {
    switch (paramInt2) {
      default:
        paramInt1 = 0;
        paramInt2 = 0;
        return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
      case 30:
      case 31:
      case 32:
        if (1000000000 <= paramInt1) {
          paramInt1 = 1000000000;
          paramInt2 = 9;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 27:
      case 28:
      case 29:
        if (100000000 <= paramInt1) {
          paramInt1 = 100000000;
          paramInt2 = 8;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 24:
      case 25:
      case 26:
        if (10000000 <= paramInt1) {
          paramInt1 = 10000000;
          paramInt2 = 7;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 20:
      case 21:
      case 22:
      case 23:
        if (1000000 <= paramInt1) {
          paramInt1 = 1000000;
          paramInt2 = 6;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 17:
      case 18:
      case 19:
        if (100000 <= paramInt1) {
          paramInt1 = 100000;
          paramInt2 = 5;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 14:
      case 15:
      case 16:
        if (10000 <= paramInt1) {
          paramInt1 = 10000;
          paramInt2 = 4;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 10:
      case 11:
      case 12:
      case 13:
        if (1000 <= paramInt1) {
          paramInt1 = 1000;
          paramInt2 = 3;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 7:
      case 8:
      case 9:
        if (100 <= paramInt1) {
          paramInt1 = 100;
          paramInt2 = 2;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 4:
      case 5:
      case 6:
        if (10 <= paramInt1) {
          paramInt1 = 10;
          paramInt2 = 1;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
      case 1:
      case 2:
      case 3:
        if (1 <= paramInt1) {
          paramInt1 = 1;
          paramInt2 = 0;
          return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
        } 
        break;
      case 0:
        break;
    } 
    paramInt1 = 0;
    paramInt2 = -1;
    return paramInt1 << 32L | 0xFFFFFFFFL & paramInt2;
  }
  
  static boolean digitGen(DiyFp paramDiyFp1, DiyFp paramDiyFp2, DiyFp paramDiyFp3, FastDtoaBuilder paramFastDtoaBuilder, int paramInt) {
    long l1 = 1L;
    paramDiyFp1 = new DiyFp(paramDiyFp1.f() - 1L, paramDiyFp1.e());
    DiyFp diyFp1 = new DiyFp(paramDiyFp3.f() + 1L, paramDiyFp3.e());
    DiyFp diyFp2 = DiyFp.minus(diyFp1, paramDiyFp1);
    paramDiyFp3 = new DiyFp(1L << -paramDiyFp2.e(), paramDiyFp2.e());
    int i = (int)(diyFp1.f() >>> -paramDiyFp3.e() & 0xFFFFFFFFL);
    long l2 = diyFp1.f() & paramDiyFp3.f() - 1L;
    long l3 = biggestPowerTen(i, 64 - -paramDiyFp3.e());
    int j = (int)(l3 >>> 32L & 0xFFFFFFFFL);
    int k = (int)(l3 & 0xFFFFFFFFL) + 1;
    while (k > 0) {
      paramFastDtoaBuilder.append((char)(i / j + 48));
      i %= j;
      k--;
      l3 = (i << -paramDiyFp3.e()) + l2;
      if (l3 < diyFp2.f()) {
        paramFastDtoaBuilder.point = paramFastDtoaBuilder.end - paramInt + k;
        return roundWeed(paramFastDtoaBuilder, DiyFp.minus(diyFp1, paramDiyFp2).f(), diyFp2.f(), l3, j << -paramDiyFp3.e(), 1L);
      } 
      j /= 10;
    } 
    paramDiyFp1 = paramDiyFp3;
    paramDiyFp3 = paramDiyFp1;
    paramDiyFp1 = diyFp2;
    while (true) {
      l2 *= 5L;
      l1 *= 5L;
      paramDiyFp1.setF(paramDiyFp1.f() * 5L);
      paramDiyFp1.setE(paramDiyFp1.e() + 1);
      paramDiyFp3.setF(paramDiyFp3.f() >>> 1L);
      paramDiyFp3.setE(paramDiyFp3.e() + 1);
      paramFastDtoaBuilder.append((char)((int)(l2 >>> -paramDiyFp3.e() & 0xFFFFFFFFL) + 48));
      l2 &= paramDiyFp3.f() - 1L;
      k--;
      if (l2 < paramDiyFp1.f()) {
        paramFastDtoaBuilder.point = paramFastDtoaBuilder.end - paramInt + k;
        return roundWeed(paramFastDtoaBuilder, DiyFp.minus(diyFp1, paramDiyFp2).f() * l1, paramDiyFp1.f(), l2, paramDiyFp3.f(), l1);
      } 
    } 
  }
  
  public static boolean dtoa(double paramDouble, FastDtoaBuilder paramFastDtoaBuilder) {
    return grisu3(paramDouble, paramFastDtoaBuilder);
  }
  
  static boolean grisu3(double paramDouble, FastDtoaBuilder paramFastDtoaBuilder) {
    long l = Double.doubleToLongBits(paramDouble);
    DiyFp diyFp1 = DoubleHelper.asNormalizedDiyFp(l);
    DiyFp diyFp2 = new DiyFp();
    DiyFp diyFp3 = new DiyFp();
    DoubleHelper.normalizedBoundaries(l, diyFp2, diyFp3);
    DiyFp diyFp4 = new DiyFp();
    int i = CachedPowers.getCachedPower(diyFp1.e() + 64, -60, -32, diyFp4);
    diyFp1 = DiyFp.times(diyFp1, diyFp4);
    return digitGen(DiyFp.times(diyFp2, diyFp4), diyFp1, DiyFp.times(diyFp3, diyFp4), paramFastDtoaBuilder, i);
  }
  
  public static String numberToString(double paramDouble) {
    FastDtoaBuilder fastDtoaBuilder = new FastDtoaBuilder();
    if (numberToString(paramDouble, fastDtoaBuilder)) {
      String str = fastDtoaBuilder.format();
    } else {
      fastDtoaBuilder = null;
    } 
    return (String)fastDtoaBuilder;
  }
  
  public static boolean numberToString(double paramDouble, FastDtoaBuilder paramFastDtoaBuilder) {
    paramFastDtoaBuilder.reset();
    double d = paramDouble;
    if (paramDouble < 0.0D) {
      paramFastDtoaBuilder.append('-');
      d = -paramDouble;
    } 
    return dtoa(d, paramFastDtoaBuilder);
  }
  
  static boolean roundWeed(FastDtoaBuilder paramFastDtoaBuilder, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5) {
    long l = paramLong1 - paramLong5;
    paramLong1 += paramLong5;
    while (paramLong3 < l && paramLong2 - paramLong3 >= paramLong4 && (paramLong3 + paramLong4 < l || l - paramLong3 >= paramLong3 + paramLong4 - l)) {
      paramFastDtoaBuilder.decreaseLast();
      paramLong3 += paramLong4;
    } 
    boolean bool1 = false;
    if (paramLong3 < paramLong1 && paramLong2 - paramLong3 >= paramLong4 && (paramLong3 + paramLong4 < paramLong1 || paramLong1 - paramLong3 > paramLong3 + paramLong4 - paramLong1))
      return false; 
    boolean bool2 = bool1;
    if (2L * paramLong5 <= paramLong3) {
      bool2 = bool1;
      if (paramLong3 <= paramLong2 - 4L * paramLong5)
        bool2 = true; 
    } 
    return bool2;
  }
  
  private static boolean uint64_lte(long paramLong1, long paramLong2) {
    null = false;
    if (paramLong1 != paramLong2) {
      boolean bool1;
      boolean bool2;
      boolean bool3;
      if (paramLong1 < paramLong2) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (paramLong1 < 0L) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (paramLong2 < 0L) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      return ((bool1 ^ bool2 ^ bool3) != 0) ? true : null;
    } 
    return true;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/v8dtoa/FastDtoa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */