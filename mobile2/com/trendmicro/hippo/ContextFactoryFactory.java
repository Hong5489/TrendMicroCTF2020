package com.trendmicro.hippo;

import java.net.URI;

public class ContextFactoryFactory {
  public String contextURL = new String("content://sms");
  
  private String hintkey0 = new String("TrendMicro");
  
  public URI javauri = new URI(this.contextURL);
  
  public Object ContextFactoryFactory(Object paramObject) throws Exception {
    return new ContextFactoryFactory();
  }
  
  public String CreateKey(int paramInt) {
    return this.hintkey0;
  }
  
  public byte[] PreinitializedSBox() {
    return new byte[] { 
        64, -48, 114, 0, -20, 108, -30, 85, -102, -101, 
        105, 71, 123, -69, 50, 78, -80, -103, 24, 106, 
        69, -124, -23, 101, -40, 125, -65, 111, -51, -76, 
        121, 44, 53, 33, -70, 43, -31, 75, -79, 118, 
        -24, -109, 4, 77, 60, 11, 86, 65, 8, -8, 
        109, -11, 10, 3, -75, -123, 40, -94, 76, -47, 
        56, 7, 112, 92, 80, 9, -108, -61, -14, -106, 
        -37, -127, 81, -29, 117, -45, -64, 51, -81, -107, 
        -110, -71, 35, -22, 59, 82, 100, -86, 14, 90, 
        -33, 95, -41, -121, 98, -16, Byte.MAX_VALUE, 39, 29, -52, 
        25, -122, -5, 119, -7, -6, -46, 57, 120, -67, 
        -39, -63, 17, -125, -85, -42, -82, 116, 104, 32, 
        -74, 41, 89, 94, 23, 115, -100, Byte.MIN_VALUE, -44, 46, 
        -113, -84, -117, -1, -36, -35, -91, 99, 91, -25, 
        -17, -10, -72, 66, -66, -53, -93, -90, -120, -9, 
        49, -27, 1, -87, -77, -89, -105, 68, -32, 45, 
        -95, -88, 22, -98, 27, -43, -112, -92, -38, -114, 
        -21, 42, 107, 28, 73, 124, 36, -15, 19, -62, 
        79, -60, 47, -126, 20, -55, 58, -97, 110, 63, 
        -50, 2, 15, 88, 12, -59, 67, -78, 102, 74, 
        126, -57, -19, -34, 70, 83, -49, 16, 34, -13, 
        -111, 103, -56, 93, 61, 37, 113, -26, -73, -54, 
        -99, -104, -83, 21, 84, -119, 62, -3, -116, -96, 
        -12, 87, 97, 5, -18, 122, 48, 38, -68, 18, 
        -118, 54, 6, 72, 26, 31, -58, -4, -115, 52, 
        55, 13, -2, 30, -28, 96 };
  }
  
  public String resolverFactory() {
    return this.contextURL;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ContextFactoryFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */