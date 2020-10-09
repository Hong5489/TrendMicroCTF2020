package com.trendmicro.keybox;

public class Oracle {
  private final byte[] S = new byte[256];
  
  private final byte[] T = new byte[256];
  
  private final int keylen;
  
  public Oracle(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length >= 1 && paramArrayOfbyte.length <= 256) {
      this.keylen = paramArrayOfbyte.length;
      byte b;
      for (b = 0; b < 'Ā'; b++) {
        this.S[b] = (byte)(byte)b;
        this.T[b] = (byte)paramArrayOfbyte[b % this.keylen];
      } 
      int i = 0;
      for (b = 0; b < 'Ā'; b++) {
        paramArrayOfbyte = this.S;
        i = paramArrayOfbyte[b] + i + this.T[b] & 0xFF;
        byte b1 = paramArrayOfbyte[i];
        paramArrayOfbyte[i] = (byte)paramArrayOfbyte[b];
        paramArrayOfbyte[b] = (byte)b1;
      } 
      return;
    } 
    throw new IllegalArgumentException("key exception");
  }
  
  public byte[] process(byte[] paramArrayOfbyte) {
    byte[] arrayOfByte = new byte[paramArrayOfbyte.length];
    int i = 0;
    int j = 0;
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      i = i + 1 & 0xFF;
      byte[] arrayOfByte1 = this.S;
      j = arrayOfByte1[i] + j & 0xFF;
      byte b1 = arrayOfByte1[j];
      arrayOfByte1[j] = (byte)arrayOfByte1[i];
      arrayOfByte1[i] = (byte)b1;
      b1 = arrayOfByte1[arrayOfByte1[i] + arrayOfByte1[j] & 0xFF];
      arrayOfByte[b] = (byte)(byte)(paramArrayOfbyte[b] ^ b1);
    } 
    return arrayOfByte;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/Oracle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */