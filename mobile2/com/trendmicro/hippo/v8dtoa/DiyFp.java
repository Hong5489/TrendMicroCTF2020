package com.trendmicro.hippo.v8dtoa;

class DiyFp {
  static final int kSignificandSize = 64;
  
  static final long kUint64MSB = -9223372036854775808L;
  
  private int e = 0;
  
  private long f = 0L;
  
  DiyFp() {}
  
  DiyFp(long paramLong, int paramInt) {}
  
  static DiyFp minus(DiyFp paramDiyFp1, DiyFp paramDiyFp2) {
    paramDiyFp1 = new DiyFp(paramDiyFp1.f, paramDiyFp1.e);
    paramDiyFp1.subtract(paramDiyFp2);
    return paramDiyFp1;
  }
  
  static DiyFp normalize(DiyFp paramDiyFp) {
    paramDiyFp = new DiyFp(paramDiyFp.f, paramDiyFp.e);
    paramDiyFp.normalize();
    return paramDiyFp;
  }
  
  static DiyFp times(DiyFp paramDiyFp1, DiyFp paramDiyFp2) {
    paramDiyFp1 = new DiyFp(paramDiyFp1.f, paramDiyFp1.e);
    paramDiyFp1.multiply(paramDiyFp2);
    return paramDiyFp1;
  }
  
  private static boolean uint64_gte(long paramLong1, long paramLong2) {
    null = false;
    if (paramLong1 != paramLong2) {
      boolean bool1;
      boolean bool2;
      boolean bool3;
      if (paramLong1 > paramLong2) {
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
  
  int e() {
    return this.e;
  }
  
  long f() {
    return this.f;
  }
  
  void multiply(DiyFp paramDiyFp) {
    long l1 = this.f;
    long l2 = l1 >>> 32L;
    l1 &= 0xFFFFFFFFL;
    long l3 = paramDiyFp.f;
    long l4 = l3 >>> 32L;
    l3 &= 0xFFFFFFFFL;
    long l5 = l1 * l4;
    long l6 = l2 * l3;
    this.e += paramDiyFp.e + 64;
    this.f = (l6 >>> 32L) + l2 * l4 + (l5 >>> 32L) + ((l1 * l3 >>> 32L) + (l6 & 0xFFFFFFFFL) + (l5 & 0xFFFFFFFFL) + 2147483648L >>> 32L);
  }
  
  void normalize() {
    long l2;
    int j;
    long l1 = this.f;
    int i = this.e;
    while (true) {
      l2 = l1;
      j = i;
      if ((0xFFC0000000000000L & l1) == 0L) {
        l1 <<= 10L;
        i -= 10;
        continue;
      } 
      break;
    } 
    while ((Long.MIN_VALUE & l2) == 0L) {
      l2 <<= 1L;
      j--;
    } 
    this.f = l2;
    this.e = j;
  }
  
  void setE(int paramInt) {
    this.e = paramInt;
  }
  
  void setF(long paramLong) {
    this.f = paramLong;
  }
  
  void subtract(DiyFp paramDiyFp) {
    this.f -= paramDiyFp.f;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[DiyFp f:");
    stringBuilder.append(this.f);
    stringBuilder.append(", e:");
    stringBuilder.append(this.e);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/v8dtoa/DiyFp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */