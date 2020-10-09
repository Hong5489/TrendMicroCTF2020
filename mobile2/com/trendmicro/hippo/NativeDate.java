package com.trendmicro.hippo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

final class NativeDate extends IdScriptableObject {
  private static final int ConstructorId_UTC = -1;
  
  private static final int ConstructorId_now = -3;
  
  private static final int ConstructorId_parse = -2;
  
  private static final Object DATE_TAG = "Date";
  
  private static final double HalfTimeDomain = 8.64E15D;
  
  private static final double HoursPerDay = 24.0D;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_getDate = 17;
  
  private static final int Id_getDay = 19;
  
  private static final int Id_getFullYear = 13;
  
  private static final int Id_getHours = 21;
  
  private static final int Id_getMilliseconds = 27;
  
  private static final int Id_getMinutes = 23;
  
  private static final int Id_getMonth = 15;
  
  private static final int Id_getSeconds = 25;
  
  private static final int Id_getTime = 11;
  
  private static final int Id_getTimezoneOffset = 29;
  
  private static final int Id_getUTCDate = 18;
  
  private static final int Id_getUTCDay = 20;
  
  private static final int Id_getUTCFullYear = 14;
  
  private static final int Id_getUTCHours = 22;
  
  private static final int Id_getUTCMilliseconds = 28;
  
  private static final int Id_getUTCMinutes = 24;
  
  private static final int Id_getUTCMonth = 16;
  
  private static final int Id_getUTCSeconds = 26;
  
  private static final int Id_getYear = 12;
  
  private static final int Id_setDate = 39;
  
  private static final int Id_setFullYear = 43;
  
  private static final int Id_setHours = 37;
  
  private static final int Id_setMilliseconds = 31;
  
  private static final int Id_setMinutes = 35;
  
  private static final int Id_setMonth = 41;
  
  private static final int Id_setSeconds = 33;
  
  private static final int Id_setTime = 30;
  
  private static final int Id_setUTCDate = 40;
  
  private static final int Id_setUTCFullYear = 44;
  
  private static final int Id_setUTCHours = 38;
  
  private static final int Id_setUTCMilliseconds = 32;
  
  private static final int Id_setUTCMinutes = 36;
  
  private static final int Id_setUTCMonth = 42;
  
  private static final int Id_setUTCSeconds = 34;
  
  private static final int Id_setYear = 45;
  
  private static final int Id_toDateString = 4;
  
  private static final int Id_toGMTString = 8;
  
  private static final int Id_toISOString = 46;
  
  private static final int Id_toJSON = 47;
  
  private static final int Id_toLocaleDateString = 7;
  
  private static final int Id_toLocaleString = 5;
  
  private static final int Id_toLocaleTimeString = 6;
  
  private static final int Id_toSource = 9;
  
  private static final int Id_toString = 2;
  
  private static final int Id_toTimeString = 3;
  
  private static final int Id_toUTCString = 8;
  
  private static final int Id_valueOf = 10;
  
  private static double LocalTZA = 0.0D;
  
  private static final int MAXARGS = 7;
  
  private static final int MAX_PROTOTYPE_ID = 47;
  
  private static final double MinutesPerDay = 1440.0D;
  
  private static final double MinutesPerHour = 60.0D;
  
  private static final double SecondsPerDay = 86400.0D;
  
  private static final double SecondsPerHour = 3600.0D;
  
  private static final double SecondsPerMinute = 60.0D;
  
  private static final String js_NaN_date_str = "Invalid Date";
  
  private static DateFormat localeDateFormatter;
  
  private static DateFormat localeDateTimeFormatter;
  
  private static DateFormat localeTimeFormatter;
  
  private static final double msPerDay = 8.64E7D;
  
  private static final double msPerHour = 3600000.0D;
  
  private static final double msPerMinute = 60000.0D;
  
  private static final double msPerSecond = 1000.0D;
  
  private static final long serialVersionUID = -8307438915861678966L;
  
  private static TimeZone thisTimeZone;
  
  private static DateFormat timeZoneFormatter;
  
  private double date;
  
  private NativeDate() {
    if (thisTimeZone == null) {
      TimeZone timeZone = TimeZone.getDefault();
      thisTimeZone = timeZone;
      LocalTZA = timeZone.getRawOffset();
    } 
  }
  
  private static int DateFromTime(double paramDouble) {
    // Byte code:
    //   0: dload_0
    //   1: invokestatic YearFromTime : (D)I
    //   4: istore_2
    //   5: dload_0
    //   6: invokestatic Day : (D)D
    //   9: iload_2
    //   10: i2d
    //   11: invokestatic DayFromYear : (D)D
    //   14: dsub
    //   15: d2i
    //   16: bipush #59
    //   18: isub
    //   19: istore_3
    //   20: iload_3
    //   21: ifge -> 48
    //   24: iload_3
    //   25: bipush #-28
    //   27: if_icmpge -> 41
    //   30: iload_3
    //   31: bipush #31
    //   33: iadd
    //   34: bipush #28
    //   36: iadd
    //   37: istore_3
    //   38: goto -> 44
    //   41: iinc #3, 28
    //   44: iload_3
    //   45: iconst_1
    //   46: iadd
    //   47: ireturn
    //   48: iload_3
    //   49: istore #4
    //   51: iload_2
    //   52: invokestatic IsLeapYear : (I)Z
    //   55: ifeq -> 70
    //   58: iload_3
    //   59: ifne -> 65
    //   62: bipush #29
    //   64: ireturn
    //   65: iload_3
    //   66: iconst_1
    //   67: isub
    //   68: istore #4
    //   70: iload #4
    //   72: bipush #30
    //   74: idiv
    //   75: i2f
    //   76: invokestatic round : (F)I
    //   79: tableswitch default -> 136, 0 -> 255, 1 -> 226, 2 -> 217, 3 -> 208, 4 -> 199, 5 -> 189, 6 -> 179, 7 -> 169, 8 -> 159, 9 -> 149, 10 -> 140
    //   136: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   139: athrow
    //   140: iload #4
    //   142: sipush #275
    //   145: isub
    //   146: iconst_1
    //   147: iadd
    //   148: ireturn
    //   149: bipush #30
    //   151: istore_3
    //   152: sipush #275
    //   155: istore_2
    //   156: goto -> 232
    //   159: bipush #31
    //   161: istore_3
    //   162: sipush #245
    //   165: istore_2
    //   166: goto -> 232
    //   169: bipush #30
    //   171: istore_3
    //   172: sipush #214
    //   175: istore_2
    //   176: goto -> 232
    //   179: bipush #31
    //   181: istore_3
    //   182: sipush #184
    //   185: istore_2
    //   186: goto -> 232
    //   189: bipush #31
    //   191: istore_3
    //   192: sipush #153
    //   195: istore_2
    //   196: goto -> 232
    //   199: bipush #30
    //   201: istore_3
    //   202: bipush #122
    //   204: istore_2
    //   205: goto -> 232
    //   208: bipush #31
    //   210: istore_3
    //   211: bipush #92
    //   213: istore_2
    //   214: goto -> 232
    //   217: bipush #30
    //   219: istore_3
    //   220: bipush #61
    //   222: istore_2
    //   223: goto -> 232
    //   226: bipush #31
    //   228: istore_3
    //   229: bipush #31
    //   231: istore_2
    //   232: iload #4
    //   234: iload_2
    //   235: isub
    //   236: istore #4
    //   238: iload #4
    //   240: istore_2
    //   241: iload #4
    //   243: ifge -> 251
    //   246: iload #4
    //   248: iload_3
    //   249: iadd
    //   250: istore_2
    //   251: iload_2
    //   252: iconst_1
    //   253: iadd
    //   254: ireturn
    //   255: iload #4
    //   257: iconst_1
    //   258: iadd
    //   259: ireturn
  }
  
  private static double Day(double paramDouble) {
    return Math.floor(paramDouble / 8.64E7D);
  }
  
  private static double DayFromMonth(int paramInt1, int paramInt2) {
    int i = paramInt1 * 30;
    if (paramInt1 >= 7) {
      i += paramInt1 / 2 - 1;
    } else if (paramInt1 >= 2) {
      i += (paramInt1 - 1) / 2 - 1;
    } else {
      i += paramInt1;
    } 
    int j = i;
    if (paramInt1 >= 2) {
      j = i;
      if (IsLeapYear(paramInt2))
        j = i + 1; 
    } 
    return j;
  }
  
  private static double DayFromYear(double paramDouble) {
    return (paramDouble - 1970.0D) * 365.0D + Math.floor((paramDouble - 1969.0D) / 4.0D) - Math.floor((paramDouble - 1901.0D) / 100.0D) + Math.floor((paramDouble - 1601.0D) / 400.0D);
  }
  
  private static double DaylightSavingTA(double paramDouble) {
    double d = paramDouble;
    if (paramDouble < 0.0D)
      d = MakeDate(MakeDay(EquivalentYear(YearFromTime(paramDouble)), MonthFromTime(paramDouble), DateFromTime(paramDouble)), TimeWithinDay(paramDouble)); 
    Date date = new Date((long)d);
    return thisTimeZone.inDaylightTime(date) ? 3600000.0D : 0.0D;
  }
  
  private static int DaysInMonth(int paramInt1, int paramInt2) {
    if (paramInt2 == 2) {
      if (IsLeapYear(paramInt1)) {
        paramInt1 = 29;
      } else {
        paramInt1 = 28;
      } 
      return paramInt1;
    } 
    if (paramInt2 >= 8) {
      paramInt1 = 31 - (paramInt2 & 0x1);
    } else {
      paramInt1 = (paramInt2 & 0x1) + 30;
    } 
    return paramInt1;
  }
  
  private static double DaysInYear(double paramDouble) {
    if (Double.isInfinite(paramDouble) || Double.isNaN(paramDouble))
      return ScriptRuntime.NaN; 
    if (IsLeapYear((int)paramDouble)) {
      paramDouble = 366.0D;
    } else {
      paramDouble = 365.0D;
    } 
    return paramDouble;
  }
  
  private static int EquivalentYear(int paramInt) {
    int i = ((int)DayFromYear(paramInt) + 4) % 7;
    int j = i;
    if (i < 0)
      j = i + 7; 
    if (IsLeapYear(paramInt)) {
      switch (j) {
        default:
          throw Kit.codeBug();
        case 6:
          return 1972;
        case 5:
          return 1988;
        case 4:
          return 1976;
        case 3:
          return 1992;
        case 2:
          return 1980;
        case 1:
          return 1996;
        case 0:
          break;
      } 
      return 1984;
    } 
    switch (j) {
      default:
      
      case 6:
        return 1977;
      case 5:
        return 1971;
      case 4:
        return 1981;
      case 3:
        return 1986;
      case 2:
        return 1985;
      case 1:
        return 1973;
      case 0:
        break;
    } 
    return 1978;
  }
  
  private static int HourFromTime(double paramDouble) {
    double d = Math.floor(paramDouble / 3600000.0D) % 24.0D;
    paramDouble = d;
    if (d < 0.0D)
      paramDouble = d + 24.0D; 
    return (int)paramDouble;
  }
  
  private static boolean IsLeapYear(int paramInt) {
    boolean bool;
    if (paramInt % 4 == 0 && (paramInt % 100 != 0 || paramInt % 400 == 0)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static double LocalTime(double paramDouble) {
    return LocalTZA + paramDouble + DaylightSavingTA(paramDouble);
  }
  
  private static double MakeDate(double paramDouble1, double paramDouble2) {
    return 8.64E7D * paramDouble1 + paramDouble2;
  }
  
  private static double MakeDay(double paramDouble1, double paramDouble2, double paramDouble3) {
    double d = paramDouble1 + Math.floor(paramDouble2 / 12.0D);
    paramDouble2 %= 12.0D;
    paramDouble1 = paramDouble2;
    if (paramDouble2 < 0.0D)
      paramDouble1 = paramDouble2 + 12.0D; 
    return Math.floor(TimeFromYear(d) / 8.64E7D) + DayFromMonth((int)paramDouble1, (int)d) + paramDouble3 - 1.0D;
  }
  
  private static double MakeTime(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    return ((paramDouble1 * 60.0D + paramDouble2) * 60.0D + paramDouble3) * 1000.0D + paramDouble4;
  }
  
  private static int MinFromTime(double paramDouble) {
    double d = Math.floor(paramDouble / 60000.0D) % 60.0D;
    paramDouble = d;
    if (d < 0.0D)
      paramDouble = d + 60.0D; 
    return (int)paramDouble;
  }
  
  private static int MonthFromTime(double paramDouble) {
    // Byte code:
    //   0: dload_0
    //   1: invokestatic YearFromTime : (D)I
    //   4: istore_2
    //   5: dload_0
    //   6: invokestatic Day : (D)D
    //   9: iload_2
    //   10: i2d
    //   11: invokestatic DayFromYear : (D)D
    //   14: dsub
    //   15: d2i
    //   16: bipush #59
    //   18: isub
    //   19: istore_3
    //   20: iconst_1
    //   21: istore #4
    //   23: iload_3
    //   24: ifge -> 39
    //   27: iload_3
    //   28: bipush #-28
    //   30: if_icmpge -> 36
    //   33: iconst_0
    //   34: istore #4
    //   36: iload #4
    //   38: ireturn
    //   39: iload_3
    //   40: istore #5
    //   42: iload_2
    //   43: invokestatic IsLeapYear : (I)Z
    //   46: ifeq -> 60
    //   49: iload_3
    //   50: ifne -> 55
    //   53: iconst_1
    //   54: ireturn
    //   55: iload_3
    //   56: iconst_1
    //   57: isub
    //   58: istore #5
    //   60: iload #5
    //   62: bipush #30
    //   64: idiv
    //   65: istore_3
    //   66: iload_3
    //   67: tableswitch default -> 124, 0 -> 219, 1 -> 192, 2 -> 185, 3 -> 178, 4 -> 171, 5 -> 163, 6 -> 155, 7 -> 147, 8 -> 139, 9 -> 131, 10 -> 128
    //   124: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   127: athrow
    //   128: bipush #11
    //   130: ireturn
    //   131: sipush #275
    //   134: istore #4
    //   136: goto -> 196
    //   139: sipush #245
    //   142: istore #4
    //   144: goto -> 196
    //   147: sipush #214
    //   150: istore #4
    //   152: goto -> 196
    //   155: sipush #184
    //   158: istore #4
    //   160: goto -> 196
    //   163: sipush #153
    //   166: istore #4
    //   168: goto -> 196
    //   171: bipush #122
    //   173: istore #4
    //   175: goto -> 196
    //   178: bipush #92
    //   180: istore #4
    //   182: goto -> 196
    //   185: bipush #61
    //   187: istore #4
    //   189: goto -> 196
    //   192: bipush #31
    //   194: istore #4
    //   196: iload #5
    //   198: iload #4
    //   200: if_icmplt -> 211
    //   203: iload_3
    //   204: iconst_2
    //   205: iadd
    //   206: istore #4
    //   208: goto -> 216
    //   211: iload_3
    //   212: iconst_1
    //   213: iadd
    //   214: istore #4
    //   216: iload #4
    //   218: ireturn
    //   219: iconst_2
    //   220: ireturn
  }
  
  private static int SecFromTime(double paramDouble) {
    double d = Math.floor(paramDouble / 1000.0D) % 60.0D;
    paramDouble = d;
    if (d < 0.0D)
      paramDouble = d + 60.0D; 
    return (int)paramDouble;
  }
  
  private static double TimeClip(double paramDouble) {
    return (paramDouble != paramDouble || paramDouble == Double.POSITIVE_INFINITY || paramDouble == Double.NEGATIVE_INFINITY || Math.abs(paramDouble) > 8.64E15D) ? ScriptRuntime.NaN : ((paramDouble > 0.0D) ? Math.floor(0.0D + paramDouble) : Math.ceil(0.0D + paramDouble));
  }
  
  private static double TimeFromYear(double paramDouble) {
    return DayFromYear(paramDouble) * 8.64E7D;
  }
  
  private static double TimeWithinDay(double paramDouble) {
    double d = paramDouble % 8.64E7D;
    paramDouble = d;
    if (d < 0.0D)
      paramDouble = d + 8.64E7D; 
    return paramDouble;
  }
  
  private static int WeekDay(double paramDouble) {
    double d = (Day(paramDouble) + 4.0D) % 7.0D;
    paramDouble = d;
    if (d < 0.0D)
      paramDouble = d + 7.0D; 
    return (int)paramDouble;
  }
  
  private static int YearFromTime(double paramDouble) {
    double d3;
    if (Double.isInfinite(paramDouble) || Double.isNaN(paramDouble))
      return 0; 
    double d1 = Math.floor(paramDouble / 3.1556952E10D) + 1970.0D;
    double d2 = TimeFromYear(d1);
    if (d2 > paramDouble) {
      d3 = d1 - 1.0D;
    } else {
      d3 = d1;
      if (DaysInYear(d1) * 8.64E7D + d2 <= paramDouble)
        d3 = d1 + 1.0D; 
    } 
    return (int)d3;
  }
  
  private static void append0PaddedUint(StringBuilder paramStringBuilder, int paramInt1, int paramInt2) {
    if (paramInt1 < 0)
      Kit.codeBug(); 
    int i = 1;
    int j = 1;
    int k = paramInt2 - 1;
    paramInt2 = i;
    i = k;
    if (paramInt1 >= 10)
      if (paramInt1 < 1000000000) {
        i = k;
        for (paramInt2 = j;; paramInt2 = k) {
          k = paramInt2 * 10;
          if (paramInt1 < k)
            break; 
          i--;
        } 
      } else {
        i = k - 9;
        paramInt2 = 1000000000;
      }  
    while (true) {
      k = paramInt2;
      j = paramInt1;
      if (i > 0) {
        paramStringBuilder.append('0');
        i--;
        continue;
      } 
      break;
    } 
    while (k != 1) {
      paramStringBuilder.append((char)(j / k + 48));
      j %= k;
      k /= 10;
    } 
    paramStringBuilder.append((char)(j + 48));
  }
  
  private static void appendMonthName(StringBuilder paramStringBuilder, int paramInt) {
    for (byte b = 0; b != 3; b++)
      paramStringBuilder.append("JanFebMarAprMayJunJulAugSepOctNovDec".charAt(paramInt * 3 + b)); 
  }
  
  private static void appendWeekDayName(StringBuilder paramStringBuilder, int paramInt) {
    for (byte b = 0; b != 3; b++)
      paramStringBuilder.append("SunMonTueWedThuFriSat".charAt(paramInt * 3 + b)); 
  }
  
  private static String date_format(double paramDouble, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(60);
    double d = LocalTime(paramDouble);
    if (paramInt != 3) {
      appendWeekDayName(stringBuilder, WeekDay(d));
      stringBuilder.append(' ');
      appendMonthName(stringBuilder, MonthFromTime(d));
      stringBuilder.append(' ');
      append0PaddedUint(stringBuilder, DateFromTime(d), 2);
      stringBuilder.append(' ');
      int i = YearFromTime(d);
      int j = i;
      if (i < 0) {
        stringBuilder.append('-');
        j = -i;
      } 
      append0PaddedUint(stringBuilder, j, 4);
      if (paramInt != 4)
        stringBuilder.append(' '); 
    } 
    if (paramInt != 4) {
      append0PaddedUint(stringBuilder, HourFromTime(d), 2);
      stringBuilder.append(':');
      append0PaddedUint(stringBuilder, MinFromTime(d), 2);
      stringBuilder.append(':');
      append0PaddedUint(stringBuilder, SecFromTime(d), 2);
      paramInt = (int)Math.floor((LocalTZA + DaylightSavingTA(paramDouble)) / 60000.0D);
      paramInt = paramInt / 60 * 100 + paramInt % 60;
      if (paramInt > 0) {
        stringBuilder.append(" GMT+");
      } else {
        stringBuilder.append(" GMT-");
        paramInt = -paramInt;
      } 
      append0PaddedUint(stringBuilder, paramInt, 4);
      if (timeZoneFormatter == null)
        timeZoneFormatter = new SimpleDateFormat("zzz"); 
      if (paramDouble < 0.0D)
        paramDouble = MakeDate(MakeDay(EquivalentYear(YearFromTime(d)), MonthFromTime(paramDouble), DateFromTime(paramDouble)), TimeWithinDay(paramDouble)); 
      stringBuilder.append(" (");
      null = new Date((long)paramDouble);
      synchronized (timeZoneFormatter) {
        stringBuilder.append(timeZoneFormatter.format(null));
        stringBuilder.append(')');
      } 
    } 
    return stringBuilder.toString();
  }
  
  private static double date_msecFromArgs(Object[] paramArrayOfObject) {
    double[] arrayOfDouble = new double[7];
    for (byte b = 0; b < 7; b++) {
      if (b < paramArrayOfObject.length) {
        double d = ScriptRuntime.toNumber(paramArrayOfObject[b]);
        if (d != d || Double.isInfinite(d))
          return ScriptRuntime.NaN; 
        arrayOfDouble[b] = ScriptRuntime.toInteger(paramArrayOfObject[b]);
      } else if (b == 2) {
        arrayOfDouble[b] = 1.0D;
      } else {
        arrayOfDouble[b] = 0.0D;
      } 
    } 
    if (arrayOfDouble[0] >= 0.0D && arrayOfDouble[0] <= 99.0D)
      arrayOfDouble[0] = arrayOfDouble[0] + 1900.0D; 
    return date_msecFromDate(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2], arrayOfDouble[3], arrayOfDouble[4], arrayOfDouble[5], arrayOfDouble[6]);
  }
  
  private static double date_msecFromDate(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7) {
    return MakeDate(MakeDay(paramDouble1, paramDouble2, paramDouble3), MakeTime(paramDouble4, paramDouble5, paramDouble6, paramDouble7));
  }
  
  private static double date_parseString(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic parseISOString : (Ljava/lang/String;)D
    //   4: dstore_1
    //   5: dload_1
    //   6: dload_1
    //   7: dcmpl
    //   8: ifne -> 13
    //   11: dload_1
    //   12: dreturn
    //   13: aload_0
    //   14: invokevirtual length : ()I
    //   17: istore_3
    //   18: iconst_0
    //   19: istore #4
    //   21: iconst_m1
    //   22: istore #5
    //   24: iconst_m1
    //   25: istore #6
    //   27: iconst_0
    //   28: istore #7
    //   30: iconst_m1
    //   31: istore #8
    //   33: iconst_0
    //   34: istore #9
    //   36: iconst_m1
    //   37: istore #10
    //   39: iconst_m1
    //   40: istore #11
    //   42: iconst_0
    //   43: istore #12
    //   45: iconst_0
    //   46: istore #13
    //   48: iconst_m1
    //   49: istore #14
    //   51: ldc2_w -1.0
    //   54: dstore #15
    //   56: iload #9
    //   58: iload_3
    //   59: if_icmpge -> 1462
    //   62: aload_0
    //   63: iload #9
    //   65: invokevirtual charAt : (I)C
    //   68: istore #17
    //   70: iinc #9, 1
    //   73: iload #17
    //   75: bipush #32
    //   77: if_icmple -> 1402
    //   80: iload #17
    //   82: bipush #44
    //   84: if_icmpeq -> 1402
    //   87: iload #17
    //   89: bipush #45
    //   91: if_icmpne -> 97
    //   94: goto -> 1402
    //   97: iload #17
    //   99: bipush #40
    //   101: if_icmpne -> 188
    //   104: iconst_1
    //   105: istore #18
    //   107: iload #9
    //   109: istore #19
    //   111: iload #17
    //   113: istore #7
    //   115: iload #19
    //   117: istore #9
    //   119: iload #19
    //   121: iload_3
    //   122: if_icmpge -> 56
    //   125: aload_0
    //   126: iload #19
    //   128: invokevirtual charAt : (I)C
    //   131: istore #7
    //   133: iload #19
    //   135: iconst_1
    //   136: iadd
    //   137: istore #9
    //   139: iload #7
    //   141: bipush #40
    //   143: if_icmpne -> 156
    //   146: iinc #18, 1
    //   149: iload #9
    //   151: istore #19
    //   153: goto -> 115
    //   156: iload #7
    //   158: bipush #41
    //   160: if_icmpne -> 181
    //   163: iinc #18, -1
    //   166: iload #18
    //   168: ifgt -> 174
    //   171: goto -> 56
    //   174: iload #9
    //   176: istore #19
    //   178: goto -> 115
    //   181: iload #9
    //   183: istore #19
    //   185: goto -> 115
    //   188: bipush #48
    //   190: iload #17
    //   192: if_icmpgt -> 750
    //   195: iload #17
    //   197: bipush #57
    //   199: if_icmpgt -> 750
    //   202: iload #17
    //   204: bipush #48
    //   206: isub
    //   207: istore #7
    //   209: iload #9
    //   211: iload_3
    //   212: if_icmpge -> 272
    //   215: aload_0
    //   216: iload #9
    //   218: invokevirtual charAt : (I)C
    //   221: istore #19
    //   223: iload #19
    //   225: istore #18
    //   227: iload #18
    //   229: istore #17
    //   231: bipush #48
    //   233: iload #19
    //   235: if_icmpgt -> 272
    //   238: iload #18
    //   240: istore #17
    //   242: iload #18
    //   244: bipush #57
    //   246: if_icmpgt -> 272
    //   249: iload #7
    //   251: bipush #10
    //   253: imul
    //   254: iload #18
    //   256: iadd
    //   257: bipush #48
    //   259: isub
    //   260: istore #7
    //   262: iinc #9, 1
    //   265: iload #18
    //   267: istore #17
    //   269: goto -> 209
    //   272: iload #12
    //   274: bipush #43
    //   276: if_icmpeq -> 651
    //   279: iload #12
    //   281: bipush #45
    //   283: if_icmpne -> 289
    //   286: goto -> 651
    //   289: iload #7
    //   291: bipush #70
    //   293: if_icmpge -> 571
    //   296: iload #12
    //   298: bipush #47
    //   300: if_icmpne -> 321
    //   303: iload #14
    //   305: iflt -> 321
    //   308: iload #8
    //   310: iflt -> 321
    //   313: iload #6
    //   315: ifge -> 321
    //   318: goto -> 571
    //   321: iload #17
    //   323: bipush #58
    //   325: if_icmpne -> 372
    //   328: iload #5
    //   330: ifge -> 348
    //   333: iload #8
    //   335: istore #18
    //   337: iload #7
    //   339: istore #5
    //   341: dload #15
    //   343: dstore #20
    //   345: goto -> 732
    //   348: iload #10
    //   350: ifge -> 368
    //   353: iload #8
    //   355: istore #18
    //   357: iload #7
    //   359: istore #10
    //   361: dload #15
    //   363: dstore #20
    //   365: goto -> 732
    //   368: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   371: dreturn
    //   372: iload #17
    //   374: bipush #47
    //   376: if_icmpne -> 421
    //   379: iload #14
    //   381: ifge -> 401
    //   384: iload #7
    //   386: iconst_1
    //   387: isub
    //   388: istore #14
    //   390: iload #8
    //   392: istore #18
    //   394: dload #15
    //   396: dstore #20
    //   398: goto -> 732
    //   401: iload #8
    //   403: ifge -> 417
    //   406: iload #7
    //   408: istore #18
    //   410: dload #15
    //   412: dstore #20
    //   414: goto -> 732
    //   417: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   420: dreturn
    //   421: iload #9
    //   423: iload_3
    //   424: if_icmpge -> 452
    //   427: iload #17
    //   429: bipush #44
    //   431: if_icmpeq -> 452
    //   434: iload #17
    //   436: bipush #32
    //   438: if_icmple -> 452
    //   441: iload #17
    //   443: bipush #45
    //   445: if_icmpeq -> 452
    //   448: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   451: dreturn
    //   452: iload #4
    //   454: ifeq -> 501
    //   457: iload #7
    //   459: bipush #60
    //   461: if_icmpge -> 501
    //   464: dload #15
    //   466: dconst_0
    //   467: dcmpg
    //   468: ifge -> 486
    //   471: dload #15
    //   473: iload #7
    //   475: i2d
    //   476: dsub
    //   477: dstore #20
    //   479: iload #8
    //   481: istore #18
    //   483: goto -> 732
    //   486: dload #15
    //   488: iload #7
    //   490: i2d
    //   491: dadd
    //   492: dstore #20
    //   494: iload #8
    //   496: istore #18
    //   498: goto -> 732
    //   501: iload #5
    //   503: iflt -> 526
    //   506: iload #10
    //   508: ifge -> 526
    //   511: iload #8
    //   513: istore #18
    //   515: iload #7
    //   517: istore #10
    //   519: dload #15
    //   521: dstore #20
    //   523: goto -> 732
    //   526: iload #10
    //   528: iflt -> 551
    //   531: iload #11
    //   533: ifge -> 551
    //   536: iload #8
    //   538: istore #18
    //   540: iload #7
    //   542: istore #11
    //   544: dload #15
    //   546: dstore #20
    //   548: goto -> 732
    //   551: iload #8
    //   553: ifge -> 567
    //   556: iload #7
    //   558: istore #18
    //   560: dload #15
    //   562: dstore #20
    //   564: goto -> 732
    //   567: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   570: dreturn
    //   571: iload #6
    //   573: iflt -> 580
    //   576: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   579: dreturn
    //   580: iload #17
    //   582: bipush #32
    //   584: if_icmple -> 614
    //   587: iload #17
    //   589: bipush #44
    //   591: if_icmpeq -> 614
    //   594: iload #17
    //   596: bipush #47
    //   598: if_icmpeq -> 614
    //   601: iload #9
    //   603: iload_3
    //   604: if_icmplt -> 610
    //   607: goto -> 614
    //   610: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   613: dreturn
    //   614: iload #7
    //   616: bipush #100
    //   618: if_icmpge -> 632
    //   621: iload #7
    //   623: sipush #1900
    //   626: iadd
    //   627: istore #12
    //   629: goto -> 636
    //   632: iload #7
    //   634: istore #12
    //   636: iload #12
    //   638: istore #6
    //   640: iload #8
    //   642: istore #18
    //   644: dload #15
    //   646: dstore #20
    //   648: goto -> 732
    //   651: iload #7
    //   653: bipush #24
    //   655: if_icmpge -> 668
    //   658: iload #7
    //   660: bipush #60
    //   662: imul
    //   663: istore #7
    //   665: goto -> 684
    //   668: iload #7
    //   670: bipush #100
    //   672: irem
    //   673: iload #7
    //   675: bipush #100
    //   677: idiv
    //   678: bipush #60
    //   680: imul
    //   681: iadd
    //   682: istore #7
    //   684: iload #7
    //   686: istore #18
    //   688: iload #12
    //   690: bipush #43
    //   692: if_icmpne -> 700
    //   695: iload #7
    //   697: ineg
    //   698: istore #18
    //   700: dload #15
    //   702: dconst_0
    //   703: dcmpl
    //   704: ifeq -> 720
    //   707: dload #15
    //   709: ldc2_w -1.0
    //   712: dcmpl
    //   713: ifeq -> 720
    //   716: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   719: dreturn
    //   720: iload #18
    //   722: i2d
    //   723: dstore #20
    //   725: iconst_1
    //   726: istore #4
    //   728: iload #8
    //   730: istore #18
    //   732: iconst_0
    //   733: istore #12
    //   735: iload #17
    //   737: istore #7
    //   739: iload #18
    //   741: istore #8
    //   743: dload #20
    //   745: dstore #15
    //   747: goto -> 56
    //   750: iload #12
    //   752: istore #18
    //   754: iload #17
    //   756: bipush #47
    //   758: if_icmpeq -> 1391
    //   761: iload #17
    //   763: bipush #58
    //   765: if_icmpeq -> 1391
    //   768: iload #17
    //   770: bipush #43
    //   772: if_icmpeq -> 1391
    //   775: iload #17
    //   777: bipush #45
    //   779: if_icmpne -> 785
    //   782: goto -> 1391
    //   785: iload #9
    //   787: iconst_1
    //   788: isub
    //   789: istore #22
    //   791: iload #9
    //   793: istore #7
    //   795: iload #7
    //   797: iload_3
    //   798: if_icmpge -> 853
    //   801: aload_0
    //   802: iload #7
    //   804: invokevirtual charAt : (I)C
    //   807: istore #17
    //   809: bipush #65
    //   811: iload #17
    //   813: if_icmpgt -> 823
    //   816: iload #17
    //   818: bipush #90
    //   820: if_icmple -> 840
    //   823: bipush #97
    //   825: iload #17
    //   827: if_icmpgt -> 846
    //   830: iload #17
    //   832: bipush #122
    //   834: if_icmple -> 840
    //   837: goto -> 846
    //   840: iinc #7, 1
    //   843: goto -> 795
    //   846: iload #17
    //   848: istore #12
    //   850: goto -> 857
    //   853: iload #17
    //   855: istore #12
    //   857: iload #7
    //   859: iload #22
    //   861: isub
    //   862: istore #19
    //   864: iload #19
    //   866: iconst_2
    //   867: if_icmpge -> 874
    //   870: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   873: dreturn
    //   874: ldc_w 'am;pm;monday;tuesday;wednesday;thursday;friday;saturday;sunday;january;february;march;april;may;june;july;august;september;october;november;december;gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;'
    //   877: astore #23
    //   879: iconst_0
    //   880: istore #24
    //   882: iconst_0
    //   883: istore #9
    //   885: iload_3
    //   886: istore #17
    //   888: iload #18
    //   890: istore_3
    //   891: iload #24
    //   893: istore #18
    //   895: aload #23
    //   897: bipush #59
    //   899: iload #9
    //   901: invokevirtual indexOf : (II)I
    //   904: istore #24
    //   906: iload #24
    //   908: ifge -> 915
    //   911: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   914: dreturn
    //   915: aload #23
    //   917: iconst_1
    //   918: iload #9
    //   920: aload_0
    //   921: iload #22
    //   923: iload #19
    //   925: invokevirtual regionMatches : (ZILjava/lang/String;II)Z
    //   928: ifeq -> 1379
    //   931: iload #18
    //   933: iconst_2
    //   934: if_icmpge -> 1031
    //   937: iload #5
    //   939: bipush #12
    //   941: if_icmpgt -> 1027
    //   944: iload #5
    //   946: ifge -> 952
    //   949: goto -> 1027
    //   952: iload #18
    //   954: ifne -> 990
    //   957: iload #14
    //   959: istore #19
    //   961: iload #5
    //   963: istore #18
    //   965: dload #15
    //   967: dstore #20
    //   969: iload #5
    //   971: bipush #12
    //   973: if_icmpne -> 1342
    //   976: iconst_0
    //   977: istore #18
    //   979: iload #14
    //   981: istore #19
    //   983: dload #15
    //   985: dstore #20
    //   987: goto -> 1342
    //   990: iload #14
    //   992: istore #19
    //   994: iload #5
    //   996: istore #18
    //   998: dload #15
    //   1000: dstore #20
    //   1002: iload #5
    //   1004: bipush #12
    //   1006: if_icmpeq -> 1342
    //   1009: iload #5
    //   1011: bipush #12
    //   1013: iadd
    //   1014: istore #18
    //   1016: iload #14
    //   1018: istore #19
    //   1020: dload #15
    //   1022: dstore #20
    //   1024: goto -> 1342
    //   1027: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   1030: dreturn
    //   1031: iload #18
    //   1033: iconst_2
    //   1034: isub
    //   1035: istore #9
    //   1037: iload #9
    //   1039: bipush #7
    //   1041: if_icmpge -> 1059
    //   1044: iload #14
    //   1046: istore #19
    //   1048: iload #5
    //   1050: istore #18
    //   1052: dload #15
    //   1054: dstore #20
    //   1056: goto -> 1342
    //   1059: iload #9
    //   1061: bipush #7
    //   1063: isub
    //   1064: istore #19
    //   1066: iload #19
    //   1068: bipush #12
    //   1070: if_icmpge -> 1093
    //   1073: iload #14
    //   1075: ifge -> 1089
    //   1078: iload #5
    //   1080: istore #18
    //   1082: dload #15
    //   1084: dstore #20
    //   1086: goto -> 1342
    //   1089: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   1092: dreturn
    //   1093: iload #19
    //   1095: bipush #12
    //   1097: isub
    //   1098: tableswitch default -> 1156, 0 -> 1331, 1 -> 1317, 2 -> 1303, 3 -> 1287, 4 -> 1271, 5 -> 1255, 6 -> 1239, 7 -> 1223, 8 -> 1207, 9 -> 1191, 10 -> 1175
    //   1156: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   1159: pop
    //   1160: iload #14
    //   1162: istore #19
    //   1164: iload #5
    //   1166: istore #18
    //   1168: dload #15
    //   1170: dstore #20
    //   1172: goto -> 1342
    //   1175: ldc2_w 420.0
    //   1178: dstore #20
    //   1180: iload #14
    //   1182: istore #19
    //   1184: iload #5
    //   1186: istore #18
    //   1188: goto -> 1342
    //   1191: ldc2_w 480.0
    //   1194: dstore #20
    //   1196: iload #14
    //   1198: istore #19
    //   1200: iload #5
    //   1202: istore #18
    //   1204: goto -> 1342
    //   1207: ldc2_w 360.0
    //   1210: dstore #20
    //   1212: iload #14
    //   1214: istore #19
    //   1216: iload #5
    //   1218: istore #18
    //   1220: goto -> 1342
    //   1223: ldc2_w 420.0
    //   1226: dstore #20
    //   1228: iload #14
    //   1230: istore #19
    //   1232: iload #5
    //   1234: istore #18
    //   1236: goto -> 1342
    //   1239: ldc2_w 300.0
    //   1242: dstore #20
    //   1244: iload #14
    //   1246: istore #19
    //   1248: iload #5
    //   1250: istore #18
    //   1252: goto -> 1342
    //   1255: ldc2_w 360.0
    //   1258: dstore #20
    //   1260: iload #14
    //   1262: istore #19
    //   1264: iload #5
    //   1266: istore #18
    //   1268: goto -> 1342
    //   1271: ldc2_w 240.0
    //   1274: dstore #20
    //   1276: iload #14
    //   1278: istore #19
    //   1280: iload #5
    //   1282: istore #18
    //   1284: goto -> 1342
    //   1287: ldc2_w 300.0
    //   1290: dstore #20
    //   1292: iload #14
    //   1294: istore #19
    //   1296: iload #5
    //   1298: istore #18
    //   1300: goto -> 1342
    //   1303: dconst_0
    //   1304: dstore #20
    //   1306: iload #14
    //   1308: istore #19
    //   1310: iload #5
    //   1312: istore #18
    //   1314: goto -> 1342
    //   1317: dconst_0
    //   1318: dstore #20
    //   1320: iload #14
    //   1322: istore #19
    //   1324: iload #5
    //   1326: istore #18
    //   1328: goto -> 1342
    //   1331: dconst_0
    //   1332: dstore #20
    //   1334: iload #5
    //   1336: istore #18
    //   1338: iload #14
    //   1340: istore #19
    //   1342: iload #12
    //   1344: istore #9
    //   1346: iload_3
    //   1347: istore #12
    //   1349: iload #7
    //   1351: istore #5
    //   1353: iload #9
    //   1355: istore #7
    //   1357: iload #17
    //   1359: istore_3
    //   1360: iload #5
    //   1362: istore #9
    //   1364: iload #19
    //   1366: istore #14
    //   1368: iload #18
    //   1370: istore #5
    //   1372: dload #20
    //   1374: dstore #15
    //   1376: goto -> 56
    //   1379: iload #24
    //   1381: iconst_1
    //   1382: iadd
    //   1383: istore #9
    //   1385: iinc #18, 1
    //   1388: goto -> 895
    //   1391: iload #17
    //   1393: istore #12
    //   1395: iload #17
    //   1397: istore #7
    //   1399: goto -> 56
    //   1402: iload #9
    //   1404: iload_3
    //   1405: if_icmpge -> 1455
    //   1408: aload_0
    //   1409: iload #9
    //   1411: invokevirtual charAt : (I)C
    //   1414: istore #13
    //   1416: iload #17
    //   1418: bipush #45
    //   1420: if_icmpne -> 1448
    //   1423: bipush #48
    //   1425: iload #13
    //   1427: if_icmpgt -> 1448
    //   1430: iload #13
    //   1432: bipush #57
    //   1434: if_icmpgt -> 1448
    //   1437: iload #17
    //   1439: istore #12
    //   1441: iload #17
    //   1443: istore #7
    //   1445: goto -> 56
    //   1448: iload #17
    //   1450: istore #7
    //   1452: goto -> 56
    //   1455: iload #17
    //   1457: istore #7
    //   1459: goto -> 56
    //   1462: iload #6
    //   1464: iflt -> 1562
    //   1467: iload #14
    //   1469: iflt -> 1562
    //   1472: iload #8
    //   1474: ifge -> 1480
    //   1477: goto -> 1562
    //   1480: iload #11
    //   1482: istore #12
    //   1484: iload #11
    //   1486: ifge -> 1492
    //   1489: iconst_0
    //   1490: istore #12
    //   1492: iload #10
    //   1494: istore #7
    //   1496: iload #10
    //   1498: ifge -> 1504
    //   1501: iconst_0
    //   1502: istore #7
    //   1504: iload #5
    //   1506: istore_3
    //   1507: iload #5
    //   1509: ifge -> 1514
    //   1512: iconst_0
    //   1513: istore_3
    //   1514: iload #6
    //   1516: i2d
    //   1517: iload #14
    //   1519: i2d
    //   1520: iload #8
    //   1522: i2d
    //   1523: iload_3
    //   1524: i2d
    //   1525: iload #7
    //   1527: i2d
    //   1528: iload #12
    //   1530: i2d
    //   1531: dconst_0
    //   1532: invokestatic date_msecFromDate : (DDDDDDD)D
    //   1535: dstore #20
    //   1537: dload #15
    //   1539: ldc2_w -1.0
    //   1542: dcmpl
    //   1543: ifne -> 1552
    //   1546: dload #20
    //   1548: invokestatic internalUTC : (D)D
    //   1551: dreturn
    //   1552: ldc2_w 60000.0
    //   1555: dload #15
    //   1557: dmul
    //   1558: dload #20
    //   1560: dadd
    //   1561: dreturn
    //   1562: getstatic com/trendmicro/hippo/ScriptRuntime.NaN : D
    //   1565: dreturn
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    NativeDate nativeDate = new NativeDate();
    nativeDate.date = ScriptRuntime.NaN;
    nativeDate.exportAsJSClass(47, paramScriptable, paramBoolean);
  }
  
  private static double internalUTC(double paramDouble) {
    double d = LocalTZA;
    return paramDouble - d - DaylightSavingTA(paramDouble - d);
  }
  
  private static Object jsConstructor(Object[] paramArrayOfObject) {
    Object object;
    NativeDate nativeDate = new NativeDate();
    if (paramArrayOfObject.length == 0) {
      nativeDate.date = now();
      return nativeDate;
    } 
    if (paramArrayOfObject.length == 1) {
      double d;
      Object object1 = paramArrayOfObject[0];
      if (object1 instanceof NativeDate) {
        nativeDate.date = ((NativeDate)object1).date;
        return nativeDate;
      } 
      object = object1;
      if (object1 instanceof Scriptable)
        object = ((Scriptable)object1).getDefaultValue(null); 
      if (object instanceof CharSequence) {
        d = date_parseString(object.toString());
      } else {
        d = ScriptRuntime.toNumber(object);
      } 
      nativeDate.date = TimeClip(d);
      return nativeDate;
    } 
    double d2 = date_msecFromArgs((Object[])object);
    double d1 = d2;
    if (!Double.isNaN(d2)) {
      d1 = d2;
      if (!Double.isInfinite(d2))
        d1 = TimeClip(internalUTC(d2)); 
    } 
    nativeDate.date = d1;
    return nativeDate;
  }
  
  private static double jsStaticFunction_UTC(Object[] paramArrayOfObject) {
    return (paramArrayOfObject.length == 0) ? ScriptRuntime.NaN : TimeClip(date_msecFromArgs(paramArrayOfObject));
  }
  
  private static String js_toISOString(double paramDouble) {
    StringBuilder stringBuilder = new StringBuilder(27);
    int i = YearFromTime(paramDouble);
    if (i < 0) {
      stringBuilder.append('-');
      append0PaddedUint(stringBuilder, -i, 6);
    } else if (i > 9999) {
      append0PaddedUint(stringBuilder, i, 6);
    } else {
      append0PaddedUint(stringBuilder, i, 4);
    } 
    stringBuilder.append('-');
    append0PaddedUint(stringBuilder, MonthFromTime(paramDouble) + 1, 2);
    stringBuilder.append('-');
    append0PaddedUint(stringBuilder, DateFromTime(paramDouble), 2);
    stringBuilder.append('T');
    append0PaddedUint(stringBuilder, HourFromTime(paramDouble), 2);
    stringBuilder.append(':');
    append0PaddedUint(stringBuilder, MinFromTime(paramDouble), 2);
    stringBuilder.append(':');
    append0PaddedUint(stringBuilder, SecFromTime(paramDouble), 2);
    stringBuilder.append('.');
    append0PaddedUint(stringBuilder, msFromTime(paramDouble), 3);
    stringBuilder.append('Z');
    return stringBuilder.toString();
  }
  
  private static String js_toUTCString(double paramDouble) {
    StringBuilder stringBuilder = new StringBuilder(60);
    appendWeekDayName(stringBuilder, WeekDay(paramDouble));
    stringBuilder.append(", ");
    append0PaddedUint(stringBuilder, DateFromTime(paramDouble), 2);
    stringBuilder.append(' ');
    appendMonthName(stringBuilder, MonthFromTime(paramDouble));
    stringBuilder.append(' ');
    int i = YearFromTime(paramDouble);
    int j = i;
    if (i < 0) {
      stringBuilder.append('-');
      j = -i;
    } 
    append0PaddedUint(stringBuilder, j, 4);
    stringBuilder.append(' ');
    append0PaddedUint(stringBuilder, HourFromTime(paramDouble), 2);
    stringBuilder.append(':');
    append0PaddedUint(stringBuilder, MinFromTime(paramDouble), 2);
    stringBuilder.append(':');
    append0PaddedUint(stringBuilder, SecFromTime(paramDouble), 2);
    stringBuilder.append(" GMT");
    return stringBuilder.toString();
  }
  
  private static double makeDate(double paramDouble, Object[] paramArrayOfObject, int paramInt) {
    double d2;
    double d3;
    if (paramArrayOfObject.length == 0)
      return ScriptRuntime.NaN; 
    int i = 1;
    int j = 1;
    int k = 1;
    switch (paramInt) {
      default:
        throw Kit.codeBug();
      case 44:
        k = 0;
      case 43:
        paramInt = 3;
        break;
      case 42:
        i = 0;
      case 41:
        paramInt = 2;
        k = i;
        break;
      case 40:
        j = 0;
      case 39:
        paramInt = 1;
        k = j;
        break;
    } 
    boolean bool = false;
    if (paramArrayOfObject.length < paramInt) {
      j = paramArrayOfObject.length;
    } else {
      j = paramInt;
    } 
    double[] arrayOfDouble = new double[3];
    for (i = 0; i < j; i++) {
      d1 = ScriptRuntime.toNumber(paramArrayOfObject[i]);
      if (d1 != d1 || Double.isInfinite(d1)) {
        bool = true;
      } else {
        arrayOfDouble[i] = ScriptRuntime.toInteger(d1);
      } 
    } 
    if (bool)
      return ScriptRuntime.NaN; 
    i = 0;
    if (paramDouble != paramDouble) {
      if (paramInt < 3)
        return ScriptRuntime.NaN; 
      paramDouble = 0.0D;
    } else if (k != 0) {
      paramDouble = LocalTime(paramDouble);
    } 
    if (paramInt >= 3 && j < 0) {
      d1 = arrayOfDouble[0];
      i = 0 + 1;
    } else {
      d1 = YearFromTime(paramDouble);
    } 
    if (paramInt >= 2 && i < j) {
      d2 = arrayOfDouble[i];
      i++;
    } else {
      d2 = MonthFromTime(paramDouble);
    } 
    if (paramInt >= 1 && i < j) {
      d3 = arrayOfDouble[i];
    } else {
      d3 = DateFromTime(paramDouble);
    } 
    double d1 = MakeDate(MakeDay(d1, d2, d3), TimeWithinDay(paramDouble));
    paramDouble = d1;
    if (k != 0)
      paramDouble = internalUTC(d1); 
    return TimeClip(paramDouble);
  }
  
  private static double makeTime(double paramDouble, Object[] paramArrayOfObject, int paramInt) {
    double d2;
    double d3;
    double d4;
    if (paramArrayOfObject.length == 0)
      return ScriptRuntime.NaN; 
    int i = 1;
    byte b = 1;
    int j = 1;
    int k = 1;
    switch (paramInt) {
      default:
        throw Kit.codeBug();
      case 38:
        k = 0;
      case 37:
        paramInt = 4;
        break;
      case 36:
        i = 0;
      case 35:
        paramInt = 3;
        k = i;
        break;
      case 34:
        b = 0;
      case 33:
        paramInt = 2;
        k = b;
        break;
      case 32:
        j = 0;
      case 31:
        paramInt = 1;
        k = j;
        break;
    } 
    b = 0;
    if (paramArrayOfObject.length < paramInt) {
      j = paramArrayOfObject.length;
    } else {
      j = paramInt;
    } 
    double[] arrayOfDouble = new double[4];
    for (i = 0; i < j; i++) {
      d1 = ScriptRuntime.toNumber(paramArrayOfObject[i]);
      if (d1 != d1 || Double.isInfinite(d1)) {
        b = 1;
      } else {
        arrayOfDouble[i] = ScriptRuntime.toInteger(d1);
      } 
    } 
    if (b != 0 || paramDouble != paramDouble)
      return ScriptRuntime.NaN; 
    i = 0;
    if (k != 0)
      paramDouble = LocalTime(paramDouble); 
    if (paramInt >= 4 && j < 0) {
      d1 = arrayOfDouble[0];
      i = 0 + 1;
    } else {
      d1 = HourFromTime(paramDouble);
    } 
    if (paramInt >= 3 && i < j) {
      d2 = arrayOfDouble[i];
      i++;
    } else {
      d2 = MinFromTime(paramDouble);
    } 
    if (paramInt >= 2 && i < j) {
      d3 = arrayOfDouble[i];
      i++;
    } else {
      d3 = SecFromTime(paramDouble);
    } 
    if (paramInt >= 1 && i < j) {
      d4 = arrayOfDouble[i];
    } else {
      d4 = msFromTime(paramDouble);
    } 
    double d1 = MakeTime(d1, d2, d3, d4);
    d1 = MakeDate(Day(paramDouble), d1);
    paramDouble = d1;
    if (k != 0)
      paramDouble = internalUTC(d1); 
    return TimeClip(paramDouble);
  }
  
  private static int msFromTime(double paramDouble) {
    double d = paramDouble % 1000.0D;
    paramDouble = d;
    if (d < 0.0D)
      paramDouble = d + 1000.0D; 
    return (int)paramDouble;
  }
  
  private static double now() {
    return System.currentTimeMillis();
  }
  
  private static double parseISOString(String paramString) {
    int i3;
    int i4;
    int i5;
    int i = 2;
    int j = 0;
    int[] arrayOfInt = new int[9];
    arrayOfInt[0] = 1970;
    arrayOfInt[1] = 1;
    arrayOfInt[2] = 1;
    arrayOfInt[3] = 0;
    arrayOfInt[4] = 0;
    arrayOfInt[5] = 0;
    arrayOfInt[6] = 0;
    arrayOfInt[7] = -1;
    arrayOfInt[8] = -1;
    int k = 4;
    int m = 1;
    int n = 1;
    int i1 = 0;
    int i2 = paramString.length();
    if (i2 != 0) {
      char c = paramString.charAt(0);
      if (c == '+' || c == '-') {
        m = 0 + 1;
        i3 = 6;
        if (c == '-') {
          i1 = -1;
        } else {
          i1 = 1;
        } 
        int i6 = i;
        i4 = i1;
        i5 = n;
        i1 = m;
      } else {
        int i6 = i;
        i3 = k;
        i4 = m;
        i5 = n;
        if (c == 'T') {
          i1 = 0 + 1;
          j = 3;
          i6 = i;
          i3 = k;
          i4 = m;
          i5 = n;
        } 
      } 
    } else {
      i5 = n;
      i4 = m;
      i3 = k;
      int i6 = i;
    } 
    label155: while (j != -1) {
      if (j == 0) {
        i = i3;
      } else if (j == 6) {
        i = 3;
      } else {
        i = 2;
      } 
      i = i1 + i;
      if (i > i2) {
        j = -1;
        break;
      } 
      n = 0;
      while (i1 < i) {
        m = paramString.charAt(i1);
        if (m < 48 || m > 57) {
          j = -1;
          break label155;
        } 
        n = n * 10 + m - 48;
        i1++;
      } 
      arrayOfInt[j] = n;
      if (i1 == i2) {
        if (j != 3 && j != 7)
          break; 
        j = -1;
        break;
      } 
      i = i1 + 1;
      n = paramString.charAt(i1);
      if (n == 90) {
        arrayOfInt[7] = 0;
        arrayOfInt[8] = 0;
        if (j != 4 && j != 5 && j != 6) {
          j = -1;
          i1 = i;
          break;
        } 
        i1 = i;
        break;
      } 
      switch (j) {
        default:
          i1 = j;
          break;
        case 8:
          i1 = -1;
          break;
        case 7:
          j = i;
          if (n != 58)
            j = i - 1; 
          i1 = 8;
          i = j;
          break;
        case 6:
          if (n == 43 || n == 45) {
            i1 = 7;
            break;
          } 
          i1 = -1;
          break;
        case 5:
          if (n == 46) {
            i1 = 6;
            break;
          } 
          if (n == 43 || n == 45) {
            i1 = 7;
            break;
          } 
          i1 = -1;
          break;
        case 4:
          if (n == 58) {
            i1 = 5;
            break;
          } 
          if (n == 43 || n == 45) {
            i1 = 7;
            break;
          } 
          i1 = -1;
          break;
        case 3:
          if (n == 58) {
            i1 = 4;
            break;
          } 
          i1 = -1;
          break;
        case 2:
          if (n == 84) {
            i1 = 3;
            break;
          } 
          i1 = -1;
          break;
        case 0:
        case 1:
          if (n == 45) {
            i1 = j + 1;
            break;
          } 
          if (n == 84) {
            i1 = 3;
            break;
          } 
          i1 = -1;
          break;
      } 
      if (i1 == 7) {
        if (n == 45) {
          j = -1;
        } else {
          j = 1;
        } 
        i5 = j;
      } 
      j = i1;
      i1 = i;
    } 
    if (j != -1 && i1 == i2) {
      k = arrayOfInt[0];
      m = arrayOfInt[1];
      j = arrayOfInt[2];
      i2 = arrayOfInt[3];
      i = arrayOfInt[4];
      n = arrayOfInt[5];
      i1 = arrayOfInt[6];
      int i6 = arrayOfInt[7];
      i3 = arrayOfInt[8];
      if (k <= 275943 && m >= 1 && m <= 12 && j >= 1 && j <= DaysInMonth(k, m) && i2 <= 24 && (i2 != 24 || (i <= 0 && n <= 0 && i1 <= 0)) && i <= 59 && n <= 59 && i6 <= 23 && i3 <= 59) {
        double d = date_msecFromDate((k * i4), (m - 1), j, i2, i, n, i1);
        if (i6 != -1)
          d -= (i6 * 60 + i3) * 60000.0D * i5; 
        if (d >= -8.64E15D && d <= 8.64E15D)
          return d; 
      } 
    } 
    return ScriptRuntime.NaN;
  }
  
  private static String toLocale_helper(double paramDouble, int paramInt) {
    // Byte code:
    //   0: iload_2
    //   1: iconst_5
    //   2: if_icmpeq -> 65
    //   5: iload_2
    //   6: bipush #6
    //   8: if_icmpeq -> 45
    //   11: iload_2
    //   12: bipush #7
    //   14: if_icmpne -> 37
    //   17: getstatic com/trendmicro/hippo/NativeDate.localeDateFormatter : Ljava/text/DateFormat;
    //   20: ifnonnull -> 30
    //   23: iconst_1
    //   24: invokestatic getDateInstance : (I)Ljava/text/DateFormat;
    //   27: putstatic com/trendmicro/hippo/NativeDate.localeDateFormatter : Ljava/text/DateFormat;
    //   30: getstatic com/trendmicro/hippo/NativeDate.localeDateFormatter : Ljava/text/DateFormat;
    //   33: astore_3
    //   34: goto -> 83
    //   37: new java/lang/AssertionError
    //   40: dup
    //   41: invokespecial <init> : ()V
    //   44: athrow
    //   45: getstatic com/trendmicro/hippo/NativeDate.localeTimeFormatter : Ljava/text/DateFormat;
    //   48: ifnonnull -> 58
    //   51: iconst_1
    //   52: invokestatic getTimeInstance : (I)Ljava/text/DateFormat;
    //   55: putstatic com/trendmicro/hippo/NativeDate.localeTimeFormatter : Ljava/text/DateFormat;
    //   58: getstatic com/trendmicro/hippo/NativeDate.localeTimeFormatter : Ljava/text/DateFormat;
    //   61: astore_3
    //   62: goto -> 83
    //   65: getstatic com/trendmicro/hippo/NativeDate.localeDateTimeFormatter : Ljava/text/DateFormat;
    //   68: ifnonnull -> 79
    //   71: iconst_1
    //   72: iconst_1
    //   73: invokestatic getDateTimeInstance : (II)Ljava/text/DateFormat;
    //   76: putstatic com/trendmicro/hippo/NativeDate.localeDateTimeFormatter : Ljava/text/DateFormat;
    //   79: getstatic com/trendmicro/hippo/NativeDate.localeDateTimeFormatter : Ljava/text/DateFormat;
    //   82: astore_3
    //   83: aload_3
    //   84: monitorenter
    //   85: new java/util/Date
    //   88: astore #4
    //   90: aload #4
    //   92: dload_0
    //   93: d2l
    //   94: invokespecial <init> : (J)V
    //   97: aload_3
    //   98: aload #4
    //   100: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   103: astore #4
    //   105: aload_3
    //   106: monitorexit
    //   107: aload #4
    //   109: areturn
    //   110: astore #4
    //   112: aload_3
    //   113: monitorexit
    //   114: aload #4
    //   116: athrow
    // Exception table:
    //   from	to	target	type
    //   85	107	110	finally
    //   112	114	110	finally
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(DATE_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != -3) {
      if (i != -2) {
        if (i != -1) {
          Object object;
          if (i != 1) {
            if (i != 47) {
              StringBuilder stringBuilder;
              if (paramScriptable2 instanceof NativeDate) {
                double d2;
                double d3;
                NativeDate nativeDate = (NativeDate)paramScriptable2;
                double d1 = nativeDate.date;
                switch (i) {
                  default:
                    throw new IllegalArgumentException(String.valueOf(i));
                  case 46:
                    if (d1 == d1)
                      return js_toISOString(d1); 
                    throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.invalid.date"));
                  case 45:
                    d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
                    if (d2 != d2 || Double.isInfinite(d2)) {
                      d1 = ScriptRuntime.NaN;
                      nativeDate.date = d1;
                      return ScriptRuntime.wrapNumber(d1);
                    } 
                    if (d1 != d1) {
                      d1 = 0.0D;
                    } else {
                      d1 = LocalTime(d1);
                    } 
                    d3 = d2;
                    if (d2 >= 0.0D) {
                      d3 = d2;
                      if (d2 <= 99.0D)
                        d3 = d2 + 1900.0D; 
                    } 
                    d1 = TimeClip(internalUTC(MakeDate(MakeDay(d3, MonthFromTime(d1), DateFromTime(d1)), TimeWithinDay(d1))));
                    nativeDate.date = d1;
                    return ScriptRuntime.wrapNumber(d1);
                  case 39:
                  case 40:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                    d1 = makeDate(d1, paramArrayOfObject, i);
                    nativeDate.date = d1;
                    return ScriptRuntime.wrapNumber(d1);
                  case 31:
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                    d1 = makeTime(d1, paramArrayOfObject, i);
                    nativeDate.date = d1;
                    return ScriptRuntime.wrapNumber(d1);
                  case 30:
                    d1 = TimeClip(ScriptRuntime.toNumber(paramArrayOfObject, 0));
                    nativeDate.date = d1;
                    return ScriptRuntime.wrapNumber(d1);
                  case 29:
                    d3 = d1;
                    if (d1 == d1)
                      d3 = (d1 - LocalTime(d1)) / 60000.0D; 
                    return ScriptRuntime.wrapNumber(d3);
                  case 27:
                  case 28:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 27)
                        d3 = LocalTime(d1); 
                      d3 = msFromTime(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 25:
                  case 26:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 25)
                        d3 = LocalTime(d1); 
                      d3 = SecFromTime(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 23:
                  case 24:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 23)
                        d3 = LocalTime(d1); 
                      d3 = MinFromTime(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 21:
                  case 22:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 21)
                        d3 = LocalTime(d1); 
                      d3 = HourFromTime(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 19:
                  case 20:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 19)
                        d3 = LocalTime(d1); 
                      d3 = WeekDay(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 17:
                  case 18:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 17)
                        d3 = LocalTime(d1); 
                      d3 = DateFromTime(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 15:
                  case 16:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i == 15)
                        d3 = LocalTime(d1); 
                      d3 = MonthFromTime(d3);
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 12:
                  case 13:
                  case 14:
                    d3 = d1;
                    if (d1 == d1) {
                      d3 = d1;
                      if (i != 14)
                        d3 = LocalTime(d1); 
                      d1 = YearFromTime(d3);
                      d3 = d1;
                      if (i == 12)
                        if (paramContext.hasFeature(1)) {
                          d3 = d1;
                          if (1900.0D <= d1) {
                            d3 = d1;
                            if (d1 < 2000.0D)
                              d3 = d1 - 1900.0D; 
                          } 
                        } else {
                          d3 = d1 - 1900.0D;
                        }  
                    } 
                    return ScriptRuntime.wrapNumber(d3);
                  case 10:
                  case 11:
                    return ScriptRuntime.wrapNumber(d1);
                  case 9:
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("(new Date(");
                    stringBuilder.append(ScriptRuntime.toString(d1));
                    stringBuilder.append("))");
                    return stringBuilder.toString();
                  case 8:
                    return (d1 == d1) ? js_toUTCString(d1) : "Invalid Date";
                  case 5:
                  case 6:
                  case 7:
                    return (d1 == d1) ? toLocale_helper(d1, i) : "Invalid Date";
                  case 2:
                  case 3:
                  case 4:
                    break;
                } 
                return (d1 == d1) ? date_format(d1, i) : "Invalid Date";
              } 
              throw incompatibleCallError(stringBuilder);
            } 
            Object object1 = ScriptRuntime.toObject(paramContext, paramScriptable1, paramScriptable2);
            object = ScriptRuntime.toPrimitive(object1, ScriptRuntime.NumberClass);
            if (object instanceof Number) {
              double d = ((Number)object).doubleValue();
              if (d != d || Double.isInfinite(d))
                return null; 
            } 
            object = ScriptableObject.getProperty((Scriptable)object1, "toISOString");
            if (object != NOT_FOUND) {
              if (object instanceof Callable) {
                object1 = ((Callable)object).call(paramContext, paramScriptable1, (Scriptable)object1, ScriptRuntime.emptyArgs);
                if (ScriptRuntime.isPrimitive(object1))
                  return object1; 
                throw ScriptRuntime.typeError1("msg.toisostring.must.return.primitive", ScriptRuntime.toString(object1));
              } 
              throw ScriptRuntime.typeError3("msg.isnt.function.in", "toISOString", ScriptRuntime.toString(object1), ScriptRuntime.toString(object));
            } 
            throw ScriptRuntime.typeError2("msg.function.not.found.in", "toISOString", ScriptRuntime.toString(object1));
          } 
          return (object != null) ? date_format(now(), 2) : jsConstructor(paramArrayOfObject);
        } 
        return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(paramArrayOfObject));
      } 
      return ScriptRuntime.wrapNumber(date_parseString(ScriptRuntime.toString(paramArrayOfObject, 0)));
    } 
    return ScriptRuntime.wrapNumber(now());
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    addIdFunctionProperty(paramIdFunctionObject, DATE_TAG, -3, "now", 0);
    addIdFunctionProperty(paramIdFunctionObject, DATE_TAG, -2, "parse", 1);
    addIdFunctionProperty(paramIdFunctionObject, DATE_TAG, -1, "UTC", 7);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findPrototypeId(String paramString) {
    char c2;
    String str2;
    char c;
    char c1 = Character.MIN_VALUE;
    String str1 = null;
    switch (paramString.length()) {
      default:
        c2 = c1;
        str2 = str1;
        break;
      case 18:
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getUTCMilliseconds";
          c2 = '\034';
          break;
        } 
        if (c == 's') {
          str2 = "setUTCMilliseconds";
          c2 = ' ';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 't') {
          c = paramString.charAt(8);
          if (c == 'D') {
            str2 = "toLocaleDateString";
            c2 = '\007';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 'T') {
            str2 = "toLocaleTimeString";
            c2 = '\006';
          } 
        } 
        break;
      case 17:
        str2 = "getTimezoneOffset";
        c2 = '\035';
        break;
      case 15:
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getMilliseconds";
          c2 = '\033';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          str2 = "setMilliseconds";
          c2 = '\037';
        } 
        break;
      case 14:
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getUTCFullYear";
          c2 = '\016';
          break;
        } 
        if (c == 's') {
          str2 = "setUTCFullYear";
          c2 = ',';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 't') {
          str2 = "toLocaleString";
          c2 = '\005';
        } 
        break;
      case 13:
        c = paramString.charAt(0);
        if (c == 'g') {
          c = paramString.charAt(6);
          if (c == 'M') {
            str2 = "getUTCMinutes";
            c2 = '\030';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 'S') {
            str2 = "getUTCSeconds";
            c2 = '\032';
          } 
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          c = paramString.charAt(6);
          if (c == 'M') {
            str2 = "setUTCMinutes";
            c2 = '$';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 'S') {
            str2 = "setUTCSeconds";
            c2 = '"';
          } 
        } 
        break;
      case 12:
        c = paramString.charAt(2);
        if (c == 'D') {
          str2 = "toDateString";
          c2 = '\004';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'T') {
          str2 = "toTimeString";
          c2 = '\003';
        } 
        break;
      case 11:
        c2 = paramString.charAt(3);
        if (c2 != 'F') {
          if (c2 != 'M') {
            if (c2 != 's') {
              switch (c2) {
                default:
                  c2 = c1;
                  str2 = str1;
                  break;
                case 'U':
                  c = paramString.charAt(0);
                  if (c == 'g') {
                    c = paramString.charAt(9);
                    if (c == 'r') {
                      str2 = "getUTCHours";
                      c2 = '\026';
                      break;
                    } 
                    c2 = c1;
                    str2 = str1;
                    if (c == 't') {
                      str2 = "getUTCMonth";
                      c2 = '\020';
                    } 
                    break;
                  } 
                  c2 = c1;
                  str2 = str1;
                  if (c == 's') {
                    c = paramString.charAt(9);
                    if (c == 'r') {
                      str2 = "setUTCHours";
                      c2 = '&';
                      break;
                    } 
                    c2 = c1;
                    str2 = str1;
                    if (c == 't') {
                      str2 = "setUTCMonth";
                      c2 = '*';
                    } 
                  } 
                  break;
                case 'T':
                  str2 = "toUTCString";
                  c2 = '\b';
                  break;
                case 'S':
                  break;
              } 
              str2 = "toISOString";
              c2 = '.';
              break;
            } 
            str2 = "constructor";
            c2 = '\001';
            break;
          } 
          str2 = "toGMTString";
          c2 = '\b';
          break;
        } 
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getFullYear";
          c2 = '\r';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          str2 = "setFullYear";
          c2 = '+';
        } 
        break;
      case 10:
        c = paramString.charAt(3);
        if (c == 'M') {
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getMinutes";
            c2 = '\027';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 's') {
            str2 = "setMinutes";
            c2 = '#';
          } 
          break;
        } 
        if (c == 'S') {
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getSeconds";
            c2 = '\031';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 's') {
            str2 = "setSeconds";
            c2 = '!';
          } 
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'U') {
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getUTCDate";
            c2 = '\022';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 's') {
            str2 = "setUTCDate";
            c2 = '(';
          } 
        } 
        break;
      case 9:
        str2 = "getUTCDay";
        c2 = '\024';
        break;
      case 8:
        c2 = paramString.charAt(3);
        if (c2 != 'H') {
          if (c2 != 'M') {
            if (c2 != 'o') {
              if (c2 != 't') {
                c2 = c1;
                str2 = str1;
                break;
              } 
              str2 = "toString";
              c2 = '\002';
              break;
            } 
            str2 = "toSource";
            c2 = '\t';
            break;
          } 
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getMonth";
            c2 = '\017';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 's') {
            str2 = "setMonth";
            c2 = ')';
          } 
          break;
        } 
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getHours";
          c2 = '\025';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          str2 = "setHours";
          c2 = '%';
        } 
        break;
      case 7:
        c2 = paramString.charAt(3);
        if (c2 != 'D') {
          if (c2 != 'T') {
            if (c2 != 'Y') {
              if (c2 != 'u') {
                c2 = c1;
                str2 = str1;
                break;
              } 
              str2 = "valueOf";
              c2 = '\n';
              break;
            } 
            c = paramString.charAt(0);
            if (c == 'g') {
              str2 = "getYear";
              c2 = '\f';
              break;
            } 
            c2 = c1;
            str2 = str1;
            if (c == 's') {
              str2 = "setYear";
              c2 = '-';
            } 
            break;
          } 
          c = paramString.charAt(0);
          if (c == 'g') {
            str2 = "getTime";
            c2 = '\013';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 's') {
            str2 = "setTime";
            c2 = '\036';
          } 
          break;
        } 
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getDate";
          c2 = '\021';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          str2 = "setDate";
          c2 = '\'';
        } 
        break;
      case 6:
        c = paramString.charAt(0);
        if (c == 'g') {
          str2 = "getDay";
          c2 = '\023';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 't') {
          str2 = "toJSON";
          c2 = '/';
        } 
        break;
    } 
    c1 = c2;
    if (str2 != null) {
      c1 = c2;
      if (str2 != paramString) {
        c1 = c2;
        if (!str2.equals(paramString))
          c1 = Character.MIN_VALUE; 
      } 
    } 
    return c1;
  }
  
  public String getClassName() {
    return "Date";
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    Class<?> clazz = paramClass;
    if (paramClass == null)
      clazz = ScriptRuntime.StringClass; 
    return super.getDefaultValue(clazz);
  }
  
  double getJSTimeValue() {
    return this.date;
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 47:
        b = 1;
        str = "toJSON";
        break;
      case 46:
        b = 0;
        str = "toISOString";
        break;
      case 45:
        b = 1;
        str = "setYear";
        break;
      case 44:
        b = 3;
        str = "setUTCFullYear";
        break;
      case 43:
        b = 3;
        str = "setFullYear";
        break;
      case 42:
        b = 2;
        str = "setUTCMonth";
        break;
      case 41:
        b = 2;
        str = "setMonth";
        break;
      case 40:
        b = 1;
        str = "setUTCDate";
        break;
      case 39:
        b = 1;
        str = "setDate";
        break;
      case 38:
        b = 4;
        str = "setUTCHours";
        break;
      case 37:
        b = 4;
        str = "setHours";
        break;
      case 36:
        b = 3;
        str = "setUTCMinutes";
        break;
      case 35:
        b = 3;
        str = "setMinutes";
        break;
      case 34:
        b = 2;
        str = "setUTCSeconds";
        break;
      case 33:
        b = 2;
        str = "setSeconds";
        break;
      case 32:
        b = 1;
        str = "setUTCMilliseconds";
        break;
      case 31:
        b = 1;
        str = "setMilliseconds";
        break;
      case 30:
        b = 1;
        str = "setTime";
        break;
      case 29:
        b = 0;
        str = "getTimezoneOffset";
        break;
      case 28:
        b = 0;
        str = "getUTCMilliseconds";
        break;
      case 27:
        b = 0;
        str = "getMilliseconds";
        break;
      case 26:
        b = 0;
        str = "getUTCSeconds";
        break;
      case 25:
        b = 0;
        str = "getSeconds";
        break;
      case 24:
        b = 0;
        str = "getUTCMinutes";
        break;
      case 23:
        b = 0;
        str = "getMinutes";
        break;
      case 22:
        b = 0;
        str = "getUTCHours";
        break;
      case 21:
        b = 0;
        str = "getHours";
        break;
      case 20:
        b = 0;
        str = "getUTCDay";
        break;
      case 19:
        b = 0;
        str = "getDay";
        break;
      case 18:
        b = 0;
        str = "getUTCDate";
        break;
      case 17:
        b = 0;
        str = "getDate";
        break;
      case 16:
        b = 0;
        str = "getUTCMonth";
        break;
      case 15:
        b = 0;
        str = "getMonth";
        break;
      case 14:
        b = 0;
        str = "getUTCFullYear";
        break;
      case 13:
        b = 0;
        str = "getFullYear";
        break;
      case 12:
        b = 0;
        str = "getYear";
        break;
      case 11:
        b = 0;
        str = "getTime";
        break;
      case 10:
        b = 0;
        str = "valueOf";
        break;
      case 9:
        b = 0;
        str = "toSource";
        break;
      case 8:
        b = 0;
        str = "toUTCString";
        break;
      case 7:
        b = 0;
        str = "toLocaleDateString";
        break;
      case 6:
        b = 0;
        str = "toLocaleTimeString";
        break;
      case 5:
        b = 0;
        str = "toLocaleString";
        break;
      case 4:
        b = 0;
        str = "toDateString";
        break;
      case 3:
        b = 0;
        str = "toTimeString";
        break;
      case 2:
        b = 0;
        str = "toString";
        break;
      case 1:
        b = 7;
        str = "constructor";
        break;
    } 
    initPrototypeMethod(DATE_TAG, paramInt, str, b);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */