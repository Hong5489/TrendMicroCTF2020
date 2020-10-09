package com.trendmicro.hippo.typedarrays;

public class ByteIo {
  private static short doReadInt16(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return paramBoolean ? (short)(paramArrayOfbyte[paramInt] & 0xFF | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 8) : (short)((paramArrayOfbyte[paramInt] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 1] & 0xFF);
  }
  
  private static void doWriteInt16(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramBoolean) {
      paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 & 0xFF);
      paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)(paramInt2 >>> 8 & 0xFF);
    } else {
      paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 >>> 8 & 0xFF);
      paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)(paramInt2 & 0xFF);
    } 
  }
  
  public static Object readFloat32(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return Float.valueOf(Float.intBitsToFloat((int)readUint32Primitive(paramArrayOfbyte, paramInt, paramBoolean)));
  }
  
  public static Object readFloat64(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return Double.valueOf(Double.longBitsToDouble(readUint64Primitive(paramArrayOfbyte, paramInt, paramBoolean)));
  }
  
  public static Object readInt16(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return Short.valueOf(doReadInt16(paramArrayOfbyte, paramInt, paramBoolean));
  }
  
  public static Object readInt32(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return paramBoolean ? Integer.valueOf(paramArrayOfbyte[paramInt] & 0xFF | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 8 | (paramArrayOfbyte[paramInt + 2] & 0xFF) << 16 | (paramArrayOfbyte[paramInt + 3] & 0xFF) << 24) : Integer.valueOf((paramArrayOfbyte[paramInt] & 0xFF) << 24 | (paramArrayOfbyte[paramInt + 1] & 0xFF) << 16 | (paramArrayOfbyte[paramInt + 2] & 0xFF) << 8 | paramArrayOfbyte[paramInt + 3] & 0xFF);
  }
  
  public static Object readInt8(byte[] paramArrayOfbyte, int paramInt) {
    return Byte.valueOf(paramArrayOfbyte[paramInt]);
  }
  
  public static Object readUint16(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return Integer.valueOf(doReadInt16(paramArrayOfbyte, paramInt, paramBoolean) & 0xFFFF);
  }
  
  public static Object readUint32(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    return Long.valueOf(readUint32Primitive(paramArrayOfbyte, paramInt, paramBoolean));
  }
  
  public static long readUint32Primitive(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    if (paramBoolean) {
      long l1 = paramArrayOfbyte[paramInt];
      long l2 = paramArrayOfbyte[paramInt + 1];
      return 0xFFFFFFFFL & ((paramArrayOfbyte[paramInt + 2] & 0xFFL) << 16L | l1 & 0xFFL | (l2 & 0xFFL) << 8L | (0xFFL & paramArrayOfbyte[paramInt + 3]) << 24L);
    } 
    long l = paramArrayOfbyte[paramInt];
    return 0xFFFFFFFFL & ((paramArrayOfbyte[paramInt + 1] & 0xFFL) << 16L | (l & 0xFFL) << 24L | (paramArrayOfbyte[paramInt + 2] & 0xFFL) << 8L | paramArrayOfbyte[paramInt + 3] & 0xFFL);
  }
  
  public static long readUint64Primitive(byte[] paramArrayOfbyte, int paramInt, boolean paramBoolean) {
    if (paramBoolean) {
      long l1 = paramArrayOfbyte[paramInt];
      long l2 = paramArrayOfbyte[paramInt + 1];
      return (paramArrayOfbyte[paramInt + 2] & 0xFFL) << 16L | l1 & 0xFFL | (l2 & 0xFFL) << 8L | (paramArrayOfbyte[paramInt + 3] & 0xFFL) << 24L | (paramArrayOfbyte[paramInt + 4] & 0xFFL) << 32L | (paramArrayOfbyte[paramInt + 5] & 0xFFL) << 40L | (paramArrayOfbyte[paramInt + 6] & 0xFFL) << 48L | (paramArrayOfbyte[paramInt + 7] & 0xFFL) << 56L;
    } 
    long l = paramArrayOfbyte[paramInt];
    return (paramArrayOfbyte[paramInt + 1] & 0xFFL) << 48L | (l & 0xFFL) << 56L | (paramArrayOfbyte[paramInt + 2] & 0xFFL) << 40L | (paramArrayOfbyte[paramInt + 3] & 0xFFL) << 32L | (paramArrayOfbyte[paramInt + 4] & 0xFFL) << 24L | (paramArrayOfbyte[paramInt + 5] & 0xFFL) << 16L | (paramArrayOfbyte[paramInt + 6] & 0xFFL) << 8L | (paramArrayOfbyte[paramInt + 7] & 0xFFL) << 0L;
  }
  
  public static Object readUint8(byte[] paramArrayOfbyte, int paramInt) {
    return Integer.valueOf(paramArrayOfbyte[paramInt] & 0xFF);
  }
  
  public static void writeFloat32(byte[] paramArrayOfbyte, int paramInt, double paramDouble, boolean paramBoolean) {
    writeUint32(paramArrayOfbyte, paramInt, Float.floatToIntBits((float)paramDouble), paramBoolean);
  }
  
  public static void writeFloat64(byte[] paramArrayOfbyte, int paramInt, double paramDouble, boolean paramBoolean) {
    writeUint64(paramArrayOfbyte, paramInt, Double.doubleToLongBits(paramDouble), paramBoolean);
  }
  
  public static void writeInt16(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean) {
    doWriteInt16(paramArrayOfbyte, paramInt1, paramInt2, paramBoolean);
  }
  
  public static void writeInt32(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramBoolean) {
      paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 & 0xFF);
      paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)(paramInt2 >>> 8 & 0xFF);
      paramArrayOfbyte[paramInt1 + 2] = (byte)(byte)(paramInt2 >>> 16 & 0xFF);
      paramArrayOfbyte[paramInt1 + 3] = (byte)(byte)(paramInt2 >>> 24 & 0xFF);
    } else {
      paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 >>> 24 & 0xFF);
      paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)(paramInt2 >>> 16 & 0xFF);
      paramArrayOfbyte[paramInt1 + 2] = (byte)(byte)(paramInt2 >>> 8 & 0xFF);
      paramArrayOfbyte[paramInt1 + 3] = (byte)(byte)(paramInt2 & 0xFF);
    } 
  }
  
  public static void writeInt8(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    paramArrayOfbyte[paramInt1] = (byte)(byte)paramInt2;
  }
  
  public static void writeUint16(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, boolean paramBoolean) {
    doWriteInt16(paramArrayOfbyte, paramInt1, 0xFFFF & paramInt2, paramBoolean);
  }
  
  public static void writeUint32(byte[] paramArrayOfbyte, int paramInt, long paramLong, boolean paramBoolean) {
    if (paramBoolean) {
      paramArrayOfbyte[paramInt] = (byte)(byte)(int)(paramLong & 0xFFL);
      paramArrayOfbyte[paramInt + 1] = (byte)(byte)(int)(paramLong >>> 8L & 0xFFL);
      paramArrayOfbyte[paramInt + 2] = (byte)(byte)(int)(paramLong >>> 16L & 0xFFL);
      paramArrayOfbyte[paramInt + 3] = (byte)(byte)(int)(paramLong >>> 24L & 0xFFL);
    } else {
      paramArrayOfbyte[paramInt] = (byte)(byte)(int)(paramLong >>> 24L & 0xFFL);
      paramArrayOfbyte[paramInt + 1] = (byte)(byte)(int)(paramLong >>> 16L & 0xFFL);
      paramArrayOfbyte[paramInt + 2] = (byte)(byte)(int)(paramLong >>> 8L & 0xFFL);
      paramArrayOfbyte[paramInt + 3] = (byte)(byte)(int)(paramLong & 0xFFL);
    } 
  }
  
  public static void writeUint64(byte[] paramArrayOfbyte, int paramInt, long paramLong, boolean paramBoolean) {
    if (paramBoolean) {
      paramArrayOfbyte[paramInt] = (byte)(byte)(int)(paramLong & 0xFFL);
      paramArrayOfbyte[paramInt + 1] = (byte)(byte)(int)(paramLong >>> 8L & 0xFFL);
      paramArrayOfbyte[paramInt + 2] = (byte)(byte)(int)(paramLong >>> 16L & 0xFFL);
      paramArrayOfbyte[paramInt + 3] = (byte)(byte)(int)(paramLong >>> 24L & 0xFFL);
      paramArrayOfbyte[paramInt + 4] = (byte)(byte)(int)(paramLong >>> 32L & 0xFFL);
      paramArrayOfbyte[paramInt + 5] = (byte)(byte)(int)(paramLong >>> 40L & 0xFFL);
      paramArrayOfbyte[paramInt + 6] = (byte)(byte)(int)(paramLong >>> 48L & 0xFFL);
      paramArrayOfbyte[paramInt + 7] = (byte)(byte)(int)(paramLong >>> 56L & 0xFFL);
    } else {
      paramArrayOfbyte[paramInt] = (byte)(byte)(int)(paramLong >>> 56L & 0xFFL);
      paramArrayOfbyte[paramInt + 1] = (byte)(byte)(int)(paramLong >>> 48L & 0xFFL);
      paramArrayOfbyte[paramInt + 2] = (byte)(byte)(int)(paramLong >>> 40L & 0xFFL);
      paramArrayOfbyte[paramInt + 3] = (byte)(byte)(int)(paramLong >>> 32L & 0xFFL);
      paramArrayOfbyte[paramInt + 4] = (byte)(byte)(int)(paramLong >>> 24L & 0xFFL);
      paramArrayOfbyte[paramInt + 5] = (byte)(byte)(int)(paramLong >>> 16L & 0xFFL);
      paramArrayOfbyte[paramInt + 6] = (byte)(byte)(int)(paramLong >>> 8L & 0xFFL);
      paramArrayOfbyte[paramInt + 7] = (byte)(byte)(int)(paramLong & 0xFFL);
    } 
  }
  
  public static void writeUint8(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 & 0xFF);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/typedarrays/ByteIo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */