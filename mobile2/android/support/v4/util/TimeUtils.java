package android.support.v4.util;

import java.io.PrintWriter;

public final class TimeUtils {
  public static final int HUNDRED_DAY_FIELD_LEN = 19;
  
  private static final int SECONDS_PER_DAY = 86400;
  
  private static final int SECONDS_PER_HOUR = 3600;
  
  private static final int SECONDS_PER_MINUTE = 60;
  
  private static char[] sFormatStr;
  
  private static final Object sFormatSync = new Object();
  
  static {
    sFormatStr = new char[24];
  }
  
  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3) {
    return (paramInt1 > 99 || (paramBoolean && paramInt3 >= 3)) ? (paramInt2 + 3) : ((paramInt1 > 9 || (paramBoolean && paramInt3 >= 2)) ? (paramInt2 + 2) : ((paramBoolean || paramInt1 > 0) ? (paramInt2 + 1) : 0));
  }
  
  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter) {
    if (paramLong1 == 0L) {
      paramPrintWriter.print("--");
      return;
    } 
    formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter) {
    formatDuration(paramLong, paramPrintWriter, 0);
  }
  
  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt) {
    synchronized (sFormatSync) {
      paramInt = formatDurationLocked(paramLong, paramInt);
      String str = new String();
      this(sFormatStr, 0, paramInt);
      paramPrintWriter.print(str);
      return;
    } 
  }
  
  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder) {
    synchronized (sFormatSync) {
      int i = formatDurationLocked(paramLong, 0);
      paramStringBuilder.append(sFormatStr, 0, i);
      return;
    } 
  }
  
  private static int formatDurationLocked(long paramLong, int paramInt) {
    boolean bool1;
    boolean bool4;
    if (sFormatStr.length < paramInt)
      sFormatStr = new char[paramInt]; 
    char[] arrayOfChar = sFormatStr;
    if (paramLong == 0L) {
      while (paramInt - 1 < 0)
        arrayOfChar[0] = (char)' '; 
      arrayOfChar[0] = (char)'0';
      return 0 + 1;
    } 
    if (paramLong > 0L) {
      i = 43;
    } else {
      paramLong = -paramLong;
      i = 45;
    } 
    int j = (int)(paramLong % 1000L);
    int k = (int)Math.floor((paramLong / 1000L));
    if (k > 86400) {
      m = k / 86400;
      k -= 86400 * m;
    } else {
      m = 0;
    } 
    if (k > 3600) {
      n = k / 3600;
      k -= n * 3600;
    } else {
      n = 0;
    } 
    if (k > 60) {
      bool1 = k / 60;
      i1 = k - bool1 * 60;
    } else {
      bool1 = false;
      i1 = k;
    } 
    int i2 = 0;
    boolean bool2 = false;
    byte b = 3;
    boolean bool3 = false;
    if (paramInt != 0) {
      k = accumField(m, 1, false, 0);
      if (k > 0)
        bool3 = true; 
      k += accumField(n, 1, bool3, 2);
      if (k > 0) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      k += accumField(bool1, 1, bool3, 2);
      if (k > 0) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      int i4 = k + accumField(i1, 1, bool3, 2);
      if (i4 > 0) {
        k = 3;
      } else {
        k = 0;
      } 
      i4 += accumField(j, 2, true, k) + 1;
      k = bool2;
      while (true) {
        i2 = k;
        if (i4 < paramInt) {
          arrayOfChar[k] = (char)' ';
          k++;
          i4++;
          continue;
        } 
        break;
      } 
    } 
    arrayOfChar[i2] = (char)i;
    int i = i2 + 1;
    if (paramInt != 0) {
      k = 1;
    } else {
      k = 0;
    } 
    bool3 = true;
    paramInt = 2;
    int i3 = printField(arrayOfChar, m, 'd', i, false, 0);
    if (i3 != i) {
      bool4 = bool3;
    } else {
      bool4 = false;
    } 
    if (k != 0) {
      m = paramInt;
    } else {
      m = 0;
    } 
    int n = printField(arrayOfChar, n, 'h', i3, bool4, m);
    if (n != i) {
      bool4 = bool3;
    } else {
      bool4 = false;
    } 
    if (k != 0) {
      m = paramInt;
    } else {
      m = 0;
    } 
    int m = printField(arrayOfChar, bool1, 'm', n, bool4, m);
    if (m == i)
      bool3 = false; 
    if (k == 0)
      paramInt = 0; 
    int i1 = printField(arrayOfChar, i1, 's', m, bool3, paramInt);
    if (k != 0 && i1 != i) {
      paramInt = b;
    } else {
      paramInt = 0;
    } 
    paramInt = printField(arrayOfChar, j, 'm', i1, true, paramInt);
    arrayOfChar[paramInt] = (char)'s';
    return paramInt + 1;
  }
  
  private static int printField(char[] paramArrayOfchar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3) {
    // Byte code:
    //   0: iload #4
    //   2: ifne -> 12
    //   5: iload_3
    //   6: istore #6
    //   8: iload_1
    //   9: ifle -> 149
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: iload #5
    //   19: iconst_3
    //   20: if_icmpge -> 35
    //   23: iload_1
    //   24: istore #6
    //   26: iload_3
    //   27: istore #7
    //   29: iload_1
    //   30: bipush #99
    //   32: if_icmple -> 65
    //   35: iload_1
    //   36: bipush #100
    //   38: idiv
    //   39: istore #6
    //   41: aload_0
    //   42: iload_3
    //   43: iload #6
    //   45: bipush #48
    //   47: iadd
    //   48: i2c
    //   49: i2c
    //   50: castore
    //   51: iload_3
    //   52: iconst_1
    //   53: iadd
    //   54: istore #7
    //   56: iload_1
    //   57: iload #6
    //   59: bipush #100
    //   61: imul
    //   62: isub
    //   63: istore #6
    //   65: iload #4
    //   67: ifeq -> 76
    //   70: iload #5
    //   72: iconst_2
    //   73: if_icmpge -> 96
    //   76: iload #6
    //   78: bipush #9
    //   80: if_icmpgt -> 96
    //   83: iload #6
    //   85: istore #5
    //   87: iload #7
    //   89: istore_1
    //   90: iload_3
    //   91: iload #7
    //   93: if_icmpeq -> 126
    //   96: iload #6
    //   98: bipush #10
    //   100: idiv
    //   101: istore_3
    //   102: aload_0
    //   103: iload #7
    //   105: iload_3
    //   106: bipush #48
    //   108: iadd
    //   109: i2c
    //   110: i2c
    //   111: castore
    //   112: iload #7
    //   114: iconst_1
    //   115: iadd
    //   116: istore_1
    //   117: iload #6
    //   119: iload_3
    //   120: bipush #10
    //   122: imul
    //   123: isub
    //   124: istore #5
    //   126: aload_0
    //   127: iload_1
    //   128: iload #5
    //   130: bipush #48
    //   132: iadd
    //   133: i2c
    //   134: i2c
    //   135: castore
    //   136: iinc #1, 1
    //   139: aload_0
    //   140: iload_1
    //   141: iload_2
    //   142: i2c
    //   143: castore
    //   144: iload_1
    //   145: iconst_1
    //   146: iadd
    //   147: istore #6
    //   149: iload #6
    //   151: ireturn
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/util/TimeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */