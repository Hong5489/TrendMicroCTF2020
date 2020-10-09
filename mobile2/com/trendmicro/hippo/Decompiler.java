package com.trendmicro.hippo;

public class Decompiler {
  public static final int CASE_GAP_PROP = 3;
  
  private static final int FUNCTION_END = 167;
  
  public static final int INDENT_GAP_PROP = 2;
  
  public static final int INITIAL_INDENT_PROP = 1;
  
  public static final int ONLY_BODY_FLAG = 1;
  
  public static final int TO_SOURCE_FLAG = 2;
  
  private static final boolean printSource = false;
  
  private char[] sourceBuffer = new char[128];
  
  private int sourceTop;
  
  private void append(char paramChar) {
    int i = this.sourceTop;
    if (i == this.sourceBuffer.length)
      increaseSourceCapacity(i + 1); 
    char[] arrayOfChar = this.sourceBuffer;
    i = this.sourceTop;
    arrayOfChar[i] = (char)paramChar;
    this.sourceTop = i + 1;
  }
  
  private void appendString(String paramString) {
    int i = paramString.length();
    int j = 1;
    if (i >= 32768)
      j = 2; 
    j = this.sourceTop + j + i;
    if (j > this.sourceBuffer.length)
      increaseSourceCapacity(j); 
    if (i >= 32768) {
      char[] arrayOfChar1 = this.sourceBuffer;
      int m = this.sourceTop;
      arrayOfChar1[m] = (char)(char)(0x8000 | i >>> 16);
      this.sourceTop = m + 1;
    } 
    char[] arrayOfChar = this.sourceBuffer;
    int k = this.sourceTop;
    arrayOfChar[k] = (char)(char)i;
    this.sourceTop = ++k;
    paramString.getChars(0, i, arrayOfChar, k);
    this.sourceTop = j;
  }
  
  public static String decompile(String paramString, int paramInt, UintMap paramUintMap) {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: aload_0
    //   3: invokevirtual length : ()I
    //   6: istore #4
    //   8: iload #4
    //   10: ifne -> 16
    //   13: ldc ''
    //   15: areturn
    //   16: aload_2
    //   17: iconst_1
    //   18: iconst_0
    //   19: invokevirtual getInt : (II)I
    //   22: istore #5
    //   24: iload #5
    //   26: iflt -> 2418
    //   29: aload_2
    //   30: iconst_2
    //   31: iconst_4
    //   32: invokevirtual getInt : (II)I
    //   35: istore #6
    //   37: iload #6
    //   39: iflt -> 2410
    //   42: aload_2
    //   43: iconst_3
    //   44: iconst_2
    //   45: invokevirtual getInt : (II)I
    //   48: istore #7
    //   50: iload #7
    //   52: iflt -> 2402
    //   55: new java/lang/StringBuilder
    //   58: dup
    //   59: invokespecial <init> : ()V
    //   62: astore #8
    //   64: iload_1
    //   65: iconst_1
    //   66: iand
    //   67: ifeq -> 76
    //   70: iconst_1
    //   71: istore #9
    //   73: goto -> 79
    //   76: iconst_0
    //   77: istore #9
    //   79: iload_1
    //   80: iconst_2
    //   81: iand
    //   82: ifeq -> 91
    //   85: iconst_1
    //   86: istore #10
    //   88: goto -> 94
    //   91: iconst_0
    //   92: istore #10
    //   94: iconst_0
    //   95: istore #11
    //   97: iconst_0
    //   98: istore #12
    //   100: iconst_0
    //   101: istore #13
    //   103: aload_3
    //   104: iconst_0
    //   105: invokevirtual charAt : (I)C
    //   108: sipush #137
    //   111: if_icmpne -> 125
    //   114: iconst_0
    //   115: iconst_1
    //   116: iadd
    //   117: istore #13
    //   119: iconst_m1
    //   120: istore #14
    //   122: goto -> 134
    //   125: aload_3
    //   126: iconst_0
    //   127: iconst_1
    //   128: iadd
    //   129: invokevirtual charAt : (I)C
    //   132: istore #14
    //   134: iload #10
    //   136: ifne -> 191
    //   139: aload #8
    //   141: bipush #10
    //   143: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: iconst_0
    //   148: istore_1
    //   149: iload_1
    //   150: iload #5
    //   152: if_icmpge -> 169
    //   155: aload #8
    //   157: bipush #32
    //   159: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: iinc #1, 1
    //   166: goto -> 149
    //   169: iload #4
    //   171: istore #15
    //   173: iload #5
    //   175: istore #16
    //   177: iload #11
    //   179: istore #17
    //   181: iload #12
    //   183: istore #18
    //   185: iload #13
    //   187: istore_1
    //   188: goto -> 243
    //   191: iload #4
    //   193: istore #15
    //   195: iload #5
    //   197: istore #16
    //   199: iload #11
    //   201: istore #17
    //   203: iload #12
    //   205: istore #18
    //   207: iload #13
    //   209: istore_1
    //   210: iload #14
    //   212: iconst_2
    //   213: if_icmpne -> 243
    //   216: aload #8
    //   218: bipush #40
    //   220: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   223: pop
    //   224: iload #13
    //   226: istore_1
    //   227: iload #12
    //   229: istore #18
    //   231: iload #11
    //   233: istore #17
    //   235: iload #5
    //   237: istore #16
    //   239: iload #4
    //   241: istore #15
    //   243: aload_0
    //   244: astore_2
    //   245: iload_1
    //   246: iload #15
    //   248: if_icmpge -> 2361
    //   251: aload_2
    //   252: iload_1
    //   253: invokevirtual charAt : (I)C
    //   256: istore #13
    //   258: iload #13
    //   260: iconst_1
    //   261: if_icmpeq -> 2153
    //   264: iload #13
    //   266: iconst_4
    //   267: if_icmpeq -> 2122
    //   270: iload #13
    //   272: bipush #50
    //   274: if_icmpeq -> 2111
    //   277: iload #13
    //   279: bipush #67
    //   281: if_icmpeq -> 2100
    //   284: iload #13
    //   286: bipush #73
    //   288: if_icmpeq -> 2089
    //   291: iload #13
    //   293: sipush #161
    //   296: if_icmpeq -> 2078
    //   299: iload #13
    //   301: sipush #167
    //   304: if_icmpeq -> 2075
    //   307: iload #13
    //   309: bipush #52
    //   311: if_icmpeq -> 2064
    //   314: iload #13
    //   316: bipush #53
    //   318: if_icmpeq -> 2053
    //   321: iload #13
    //   323: sipush #144
    //   326: if_icmpeq -> 2042
    //   329: iload #13
    //   331: sipush #145
    //   334: if_icmpeq -> 2031
    //   337: iload #13
    //   339: sipush #147
    //   342: if_icmpeq -> 2020
    //   345: iload #13
    //   347: sipush #148
    //   350: if_icmpeq -> 2009
    //   353: iload #13
    //   355: sipush #164
    //   358: if_icmpeq -> 838
    //   361: iload #13
    //   363: sipush #165
    //   366: if_icmpeq -> 1935
    //   369: iload #13
    //   371: tableswitch default -> 480, 9 -> 1924, 10 -> 1913, 11 -> 1902, 12 -> 1891, 13 -> 1880, 14 -> 1869, 15 -> 1858, 16 -> 1847, 17 -> 1836, 18 -> 1825, 19 -> 1814, 20 -> 1803, 21 -> 1792, 22 -> 1781, 23 -> 1770, 24 -> 1759, 25 -> 1748, 26 -> 1737, 27 -> 1726, 28 -> 1715, 29 -> 1704, 30 -> 1693, 31 -> 1682, 32 -> 1671
    //   480: iload #13
    //   482: tableswitch default -> 536, 39 -> 1657, 40 -> 1644, 41 -> 1630, 42 -> 1619, 43 -> 1608, 44 -> 1597, 45 -> 1586, 46 -> 1575, 47 -> 1564, 48 -> 1657
    //   536: iload #13
    //   538: tableswitch default -> 668, 82 -> 1553, 83 -> 1523, 84 -> 1512, 85 -> 1501, 86 -> 1457, 87 -> 1357, 88 -> 1346, 89 -> 1312, 90 -> 1301, 91 -> 1290, 92 -> 1279, 93 -> 1268, 94 -> 1257, 95 -> 1246, 96 -> 1235, 97 -> 1224, 98 -> 1213, 99 -> 1202, 100 -> 1191, 101 -> 1180, 102 -> 1169, 103 -> 1158, 104 -> 1125, 105 -> 1114, 106 -> 1103, 107 -> 1092, 108 -> 1081, 109 -> 1070, 110 -> 1052
    //   668: iload #13
    //   670: tableswitch default -> 744, 113 -> 1041, 114 -> 1030, 115 -> 1019, 116 -> 1008, 117 -> 997, 118 -> 986, 119 -> 975, 120 -> 964, 121 -> 930, 122 -> 896, 123 -> 885, 124 -> 874, 125 -> 863, 126 -> 852, 127 -> 841
    //   744: iload #13
    //   746: tableswitch default -> 776, 152 -> 838, 153 -> 838, 154 -> 827, 155 -> 816
    //   776: new java/lang/StringBuilder
    //   779: dup
    //   780: invokespecial <init> : ()V
    //   783: astore_0
    //   784: aload_0
    //   785: ldc 'Token: '
    //   787: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   790: pop
    //   791: aload_0
    //   792: aload_2
    //   793: iload_1
    //   794: invokevirtual charAt : (I)C
    //   797: invokestatic name : (I)Ljava/lang/String;
    //   800: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   803: pop
    //   804: new java/lang/RuntimeException
    //   807: dup
    //   808: aload_0
    //   809: invokevirtual toString : ()Ljava/lang/String;
    //   812: invokespecial <init> : (Ljava/lang/String;)V
    //   815: athrow
    //   816: aload #8
    //   818: ldc 'const '
    //   820: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   823: pop
    //   824: goto -> 2158
    //   827: aload #8
    //   829: ldc 'let '
    //   831: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   834: pop
    //   835: goto -> 2158
    //   838: goto -> 1946
    //   841: aload #8
    //   843: ldc 'void '
    //   845: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   848: pop
    //   849: goto -> 2158
    //   852: aload #8
    //   854: ldc 'finally '
    //   856: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   859: pop
    //   860: goto -> 2158
    //   863: aload #8
    //   865: ldc 'catch '
    //   867: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   870: pop
    //   871: goto -> 2158
    //   874: aload #8
    //   876: ldc 'with '
    //   878: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   881: pop
    //   882: goto -> 2158
    //   885: aload #8
    //   887: ldc 'var '
    //   889: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   892: pop
    //   893: goto -> 2158
    //   896: aload #8
    //   898: ldc 'continue'
    //   900: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   903: pop
    //   904: bipush #39
    //   906: aload_2
    //   907: iload #15
    //   909: iload_1
    //   910: invokestatic getNext : (Ljava/lang/String;II)I
    //   913: if_icmpne -> 927
    //   916: aload #8
    //   918: bipush #32
    //   920: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   923: pop
    //   924: goto -> 2158
    //   927: goto -> 2158
    //   930: aload #8
    //   932: ldc 'break'
    //   934: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   937: pop
    //   938: bipush #39
    //   940: aload_2
    //   941: iload #15
    //   943: iload_1
    //   944: invokestatic getNext : (Ljava/lang/String;II)I
    //   947: if_icmpne -> 961
    //   950: aload #8
    //   952: bipush #32
    //   954: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   957: pop
    //   958: goto -> 2158
    //   961: goto -> 2158
    //   964: aload #8
    //   966: ldc 'for '
    //   968: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   971: pop
    //   972: goto -> 2158
    //   975: aload #8
    //   977: ldc 'do '
    //   979: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   982: pop
    //   983: goto -> 2158
    //   986: aload #8
    //   988: ldc 'while '
    //   990: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   993: pop
    //   994: goto -> 2158
    //   997: aload #8
    //   999: ldc 'default'
    //   1001: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1004: pop
    //   1005: goto -> 2158
    //   1008: aload #8
    //   1010: ldc 'case '
    //   1012: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1015: pop
    //   1016: goto -> 2158
    //   1019: aload #8
    //   1021: ldc 'switch '
    //   1023: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1026: pop
    //   1027: goto -> 2158
    //   1030: aload #8
    //   1032: ldc 'else '
    //   1034: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1037: pop
    //   1038: goto -> 2158
    //   1041: aload #8
    //   1043: ldc 'if '
    //   1045: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1048: pop
    //   1049: goto -> 2158
    //   1052: iinc #1, 1
    //   1055: aload #8
    //   1057: ldc 'function '
    //   1059: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1062: pop
    //   1063: iload #18
    //   1065: istore #13
    //   1067: goto -> 2351
    //   1070: aload #8
    //   1072: bipush #46
    //   1074: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1077: pop
    //   1078: goto -> 2158
    //   1081: aload #8
    //   1083: ldc '--'
    //   1085: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1088: pop
    //   1089: goto -> 2158
    //   1092: aload #8
    //   1094: ldc '++'
    //   1096: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1099: pop
    //   1100: goto -> 2158
    //   1103: aload #8
    //   1105: ldc ' && '
    //   1107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1110: pop
    //   1111: goto -> 2158
    //   1114: aload #8
    //   1116: ldc ' || '
    //   1118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1121: pop
    //   1122: goto -> 2158
    //   1125: iconst_1
    //   1126: aload_2
    //   1127: iload #15
    //   1129: iload_1
    //   1130: invokestatic getNext : (Ljava/lang/String;II)I
    //   1133: if_icmpne -> 1147
    //   1136: aload #8
    //   1138: bipush #58
    //   1140: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1143: pop
    //   1144: goto -> 2158
    //   1147: aload #8
    //   1149: ldc ' : '
    //   1151: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1154: pop
    //   1155: goto -> 2158
    //   1158: aload #8
    //   1160: ldc ' ? '
    //   1162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1165: pop
    //   1166: goto -> 2158
    //   1169: aload #8
    //   1171: ldc ' %= '
    //   1173: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1176: pop
    //   1177: goto -> 2158
    //   1180: aload #8
    //   1182: ldc ' /= '
    //   1184: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1187: pop
    //   1188: goto -> 2158
    //   1191: aload #8
    //   1193: ldc ' *= '
    //   1195: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1198: pop
    //   1199: goto -> 2158
    //   1202: aload #8
    //   1204: ldc ' -= '
    //   1206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1209: pop
    //   1210: goto -> 2158
    //   1213: aload #8
    //   1215: ldc ' += '
    //   1217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1220: pop
    //   1221: goto -> 2158
    //   1224: aload #8
    //   1226: ldc ' >>>= '
    //   1228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1231: pop
    //   1232: goto -> 2158
    //   1235: aload #8
    //   1237: ldc ' >>= '
    //   1239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1242: pop
    //   1243: goto -> 2158
    //   1246: aload #8
    //   1248: ldc ' <<= '
    //   1250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1253: pop
    //   1254: goto -> 2158
    //   1257: aload #8
    //   1259: ldc ' &= '
    //   1261: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1264: pop
    //   1265: goto -> 2158
    //   1268: aload #8
    //   1270: ldc ' ^= '
    //   1272: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1275: pop
    //   1276: goto -> 2158
    //   1279: aload #8
    //   1281: ldc ' |= '
    //   1283: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1286: pop
    //   1287: goto -> 2158
    //   1290: aload #8
    //   1292: ldc ' = '
    //   1294: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1297: pop
    //   1298: goto -> 2158
    //   1301: aload #8
    //   1303: ldc ', '
    //   1305: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1308: pop
    //   1309: goto -> 2158
    //   1312: aload #8
    //   1314: bipush #41
    //   1316: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1319: pop
    //   1320: bipush #86
    //   1322: aload_2
    //   1323: iload #15
    //   1325: iload_1
    //   1326: invokestatic getNext : (Ljava/lang/String;II)I
    //   1329: if_icmpne -> 1343
    //   1332: aload #8
    //   1334: bipush #32
    //   1336: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1339: pop
    //   1340: goto -> 2158
    //   1343: goto -> 2158
    //   1346: aload #8
    //   1348: bipush #40
    //   1350: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1353: pop
    //   1354: goto -> 2158
    //   1357: iinc #17, -1
    //   1360: iload #9
    //   1362: ifeq -> 1377
    //   1365: iload #17
    //   1367: ifne -> 1377
    //   1370: iload #18
    //   1372: istore #13
    //   1374: goto -> 2351
    //   1377: aload #8
    //   1379: bipush #125
    //   1381: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1384: pop
    //   1385: aload_2
    //   1386: iload #15
    //   1388: iload_1
    //   1389: invokestatic getNext : (Ljava/lang/String;II)I
    //   1392: istore #13
    //   1394: iload #13
    //   1396: iconst_1
    //   1397: if_icmpeq -> 1443
    //   1400: iload #13
    //   1402: bipush #114
    //   1404: if_icmpeq -> 1425
    //   1407: iload #13
    //   1409: bipush #118
    //   1411: if_icmpeq -> 1425
    //   1414: iload #13
    //   1416: sipush #167
    //   1419: if_icmpeq -> 1443
    //   1422: goto -> 1450
    //   1425: iload #16
    //   1427: iload #6
    //   1429: isub
    //   1430: istore #16
    //   1432: aload #8
    //   1434: bipush #32
    //   1436: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1439: pop
    //   1440: goto -> 1450
    //   1443: iload #16
    //   1445: iload #6
    //   1447: isub
    //   1448: istore #16
    //   1450: iload #18
    //   1452: istore #13
    //   1454: goto -> 2351
    //   1457: iinc #17, 1
    //   1460: iload #16
    //   1462: istore #13
    //   1464: iconst_1
    //   1465: aload_2
    //   1466: iload #15
    //   1468: iload_1
    //   1469: invokestatic getNext : (Ljava/lang/String;II)I
    //   1472: if_icmpne -> 1482
    //   1475: iload #16
    //   1477: iload #6
    //   1479: iadd
    //   1480: istore #13
    //   1482: aload #8
    //   1484: bipush #123
    //   1486: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1489: pop
    //   1490: iload #13
    //   1492: istore #16
    //   1494: iload #18
    //   1496: istore #13
    //   1498: goto -> 2351
    //   1501: aload #8
    //   1503: bipush #93
    //   1505: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1508: pop
    //   1509: goto -> 2158
    //   1512: aload #8
    //   1514: bipush #91
    //   1516: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1519: pop
    //   1520: goto -> 2158
    //   1523: aload #8
    //   1525: bipush #59
    //   1527: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1530: pop
    //   1531: iconst_1
    //   1532: aload_2
    //   1533: iload #15
    //   1535: iload_1
    //   1536: invokestatic getNext : (Ljava/lang/String;II)I
    //   1539: if_icmpeq -> 2158
    //   1542: aload #8
    //   1544: bipush #32
    //   1546: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1549: pop
    //   1550: goto -> 2158
    //   1553: aload #8
    //   1555: ldc 'try '
    //   1557: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1560: pop
    //   1561: goto -> 2158
    //   1564: aload #8
    //   1566: ldc ' !== '
    //   1568: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1571: pop
    //   1572: goto -> 2158
    //   1575: aload #8
    //   1577: ldc ' === '
    //   1579: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1582: pop
    //   1583: goto -> 2158
    //   1586: aload #8
    //   1588: ldc 'true'
    //   1590: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1593: pop
    //   1594: goto -> 2158
    //   1597: aload #8
    //   1599: ldc 'false'
    //   1601: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1604: pop
    //   1605: goto -> 2158
    //   1608: aload #8
    //   1610: ldc 'this'
    //   1612: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1615: pop
    //   1616: goto -> 2158
    //   1619: aload #8
    //   1621: ldc 'null'
    //   1623: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1626: pop
    //   1627: goto -> 2158
    //   1630: aload_2
    //   1631: iload_1
    //   1632: iconst_1
    //   1633: iadd
    //   1634: iconst_1
    //   1635: aload #8
    //   1637: invokestatic printSourceString : (Ljava/lang/String;IZLjava/lang/StringBuilder;)I
    //   1640: istore_1
    //   1641: goto -> 243
    //   1644: aload_2
    //   1645: iload_1
    //   1646: iconst_1
    //   1647: iadd
    //   1648: aload #8
    //   1650: invokestatic printSourceNumber : (Ljava/lang/String;ILjava/lang/StringBuilder;)I
    //   1653: istore_1
    //   1654: goto -> 243
    //   1657: aload_2
    //   1658: iload_1
    //   1659: iconst_1
    //   1660: iadd
    //   1661: iconst_0
    //   1662: aload #8
    //   1664: invokestatic printSourceString : (Ljava/lang/String;IZLjava/lang/StringBuilder;)I
    //   1667: istore_1
    //   1668: goto -> 243
    //   1671: aload #8
    //   1673: ldc 'typeof '
    //   1675: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1678: pop
    //   1679: goto -> 2158
    //   1682: aload #8
    //   1684: ldc 'delete '
    //   1686: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1689: pop
    //   1690: goto -> 2158
    //   1693: aload #8
    //   1695: ldc 'new '
    //   1697: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1700: pop
    //   1701: goto -> 2158
    //   1704: aload #8
    //   1706: bipush #45
    //   1708: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1711: pop
    //   1712: goto -> 2158
    //   1715: aload #8
    //   1717: bipush #43
    //   1719: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1722: pop
    //   1723: goto -> 2158
    //   1726: aload #8
    //   1728: bipush #126
    //   1730: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1733: pop
    //   1734: goto -> 2158
    //   1737: aload #8
    //   1739: bipush #33
    //   1741: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1744: pop
    //   1745: goto -> 2158
    //   1748: aload #8
    //   1750: ldc ' % '
    //   1752: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1755: pop
    //   1756: goto -> 2158
    //   1759: aload #8
    //   1761: ldc ' / '
    //   1763: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1766: pop
    //   1767: goto -> 2158
    //   1770: aload #8
    //   1772: ldc ' * '
    //   1774: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1777: pop
    //   1778: goto -> 2158
    //   1781: aload #8
    //   1783: ldc ' - '
    //   1785: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1788: pop
    //   1789: goto -> 2158
    //   1792: aload #8
    //   1794: ldc ' + '
    //   1796: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1799: pop
    //   1800: goto -> 2158
    //   1803: aload #8
    //   1805: ldc ' >>> '
    //   1807: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1810: pop
    //   1811: goto -> 2158
    //   1814: aload #8
    //   1816: ldc ' >> '
    //   1818: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1821: pop
    //   1822: goto -> 2158
    //   1825: aload #8
    //   1827: ldc ' << '
    //   1829: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1832: pop
    //   1833: goto -> 2158
    //   1836: aload #8
    //   1838: ldc ' >= '
    //   1840: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1843: pop
    //   1844: goto -> 2158
    //   1847: aload #8
    //   1849: ldc ' > '
    //   1851: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1854: pop
    //   1855: goto -> 2158
    //   1858: aload #8
    //   1860: ldc ' <= '
    //   1862: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1865: pop
    //   1866: goto -> 2158
    //   1869: aload #8
    //   1871: ldc ' < '
    //   1873: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1876: pop
    //   1877: goto -> 2158
    //   1880: aload #8
    //   1882: ldc ' != '
    //   1884: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1887: pop
    //   1888: goto -> 2158
    //   1891: aload #8
    //   1893: ldc ' == '
    //   1895: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1898: pop
    //   1899: goto -> 2158
    //   1902: aload #8
    //   1904: ldc ' & '
    //   1906: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1909: pop
    //   1910: goto -> 2158
    //   1913: aload #8
    //   1915: ldc ' ^ '
    //   1917: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1920: pop
    //   1921: goto -> 2158
    //   1924: aload #8
    //   1926: ldc ' | '
    //   1928: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1931: pop
    //   1932: goto -> 2158
    //   1935: aload #8
    //   1937: ldc ' => '
    //   1939: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1942: pop
    //   1943: goto -> 2158
    //   1946: aload_2
    //   1947: iload_1
    //   1948: invokevirtual charAt : (I)C
    //   1951: sipush #152
    //   1954: if_icmpne -> 1968
    //   1957: aload #8
    //   1959: ldc 'get '
    //   1961: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1964: pop
    //   1965: goto -> 1987
    //   1968: aload_2
    //   1969: iload_1
    //   1970: invokevirtual charAt : (I)C
    //   1973: sipush #153
    //   1976: if_icmpne -> 1987
    //   1979: aload #8
    //   1981: ldc 'set '
    //   1983: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1986: pop
    //   1987: aload_2
    //   1988: iload_1
    //   1989: iconst_1
    //   1990: iadd
    //   1991: iconst_1
    //   1992: iadd
    //   1993: iconst_0
    //   1994: aload #8
    //   1996: invokestatic printSourceString : (Ljava/lang/String;IZLjava/lang/StringBuilder;)I
    //   1999: iconst_1
    //   2000: iadd
    //   2001: istore_1
    //   2002: iload #18
    //   2004: istore #13
    //   2006: goto -> 2351
    //   2009: aload #8
    //   2011: bipush #64
    //   2013: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2016: pop
    //   2017: goto -> 2158
    //   2020: aload #8
    //   2022: ldc '.('
    //   2024: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2027: pop
    //   2028: goto -> 2158
    //   2031: aload #8
    //   2033: ldc '::'
    //   2035: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2038: pop
    //   2039: goto -> 2158
    //   2042: aload #8
    //   2044: ldc '..'
    //   2046: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2049: pop
    //   2050: goto -> 2158
    //   2053: aload #8
    //   2055: ldc ' instanceof '
    //   2057: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2060: pop
    //   2061: goto -> 2158
    //   2064: aload #8
    //   2066: ldc ' in '
    //   2068: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2071: pop
    //   2072: goto -> 2158
    //   2075: goto -> 2158
    //   2078: aload #8
    //   2080: ldc 'debugger;\\n'
    //   2082: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2085: pop
    //   2086: goto -> 2158
    //   2089: aload #8
    //   2091: ldc 'yield '
    //   2093: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2096: pop
    //   2097: goto -> 2158
    //   2100: aload #8
    //   2102: ldc ': '
    //   2104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2107: pop
    //   2108: goto -> 2158
    //   2111: aload #8
    //   2113: ldc 'throw '
    //   2115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2118: pop
    //   2119: goto -> 2158
    //   2122: aload #8
    //   2124: ldc 'return'
    //   2126: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2129: pop
    //   2130: bipush #83
    //   2132: aload_2
    //   2133: iload #15
    //   2135: iload_1
    //   2136: invokestatic getNext : (Ljava/lang/String;II)I
    //   2139: if_icmpeq -> 2158
    //   2142: aload #8
    //   2144: bipush #32
    //   2146: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2149: pop
    //   2150: goto -> 2158
    //   2153: iload #10
    //   2155: ifeq -> 2165
    //   2158: iload #18
    //   2160: istore #13
    //   2162: goto -> 2351
    //   2165: iconst_1
    //   2166: istore #5
    //   2168: iload #18
    //   2170: ifne -> 2207
    //   2173: iconst_1
    //   2174: istore #13
    //   2176: iload #9
    //   2178: ifeq -> 2200
    //   2181: aload #8
    //   2183: iconst_0
    //   2184: invokevirtual setLength : (I)V
    //   2187: iload #16
    //   2189: iload #6
    //   2191: isub
    //   2192: istore #18
    //   2194: iconst_0
    //   2195: istore #5
    //   2197: goto -> 2215
    //   2200: iload #16
    //   2202: istore #18
    //   2204: goto -> 2215
    //   2207: iload #18
    //   2209: istore #13
    //   2211: iload #16
    //   2213: istore #18
    //   2215: iload #5
    //   2217: ifeq -> 2228
    //   2220: aload #8
    //   2222: bipush #10
    //   2224: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2227: pop
    //   2228: iload_1
    //   2229: iconst_1
    //   2230: iadd
    //   2231: iload #15
    //   2233: if_icmpge -> 2347
    //   2236: iconst_0
    //   2237: istore #16
    //   2239: aload_2
    //   2240: iload_1
    //   2241: iconst_1
    //   2242: iadd
    //   2243: invokevirtual charAt : (I)C
    //   2246: istore #5
    //   2248: iload #5
    //   2250: bipush #116
    //   2252: if_icmpeq -> 2312
    //   2255: iload #5
    //   2257: bipush #117
    //   2259: if_icmpne -> 2265
    //   2262: goto -> 2312
    //   2265: iload #5
    //   2267: bipush #87
    //   2269: if_icmpne -> 2279
    //   2272: iload #6
    //   2274: istore #16
    //   2276: goto -> 2319
    //   2279: iload #5
    //   2281: bipush #39
    //   2283: if_icmpne -> 2309
    //   2286: aload_2
    //   2287: aload_2
    //   2288: iload_1
    //   2289: iconst_2
    //   2290: iadd
    //   2291: invokestatic getSourceStringEnd : (Ljava/lang/String;I)I
    //   2294: invokevirtual charAt : (I)C
    //   2297: bipush #104
    //   2299: if_icmpne -> 2319
    //   2302: iload #6
    //   2304: istore #16
    //   2306: goto -> 2319
    //   2309: goto -> 2319
    //   2312: iload #6
    //   2314: iload #7
    //   2316: isub
    //   2317: istore #16
    //   2319: iload #16
    //   2321: iload #18
    //   2323: if_icmpge -> 2340
    //   2326: aload #8
    //   2328: bipush #32
    //   2330: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2333: pop
    //   2334: iinc #16, 1
    //   2337: goto -> 2319
    //   2340: iload #18
    //   2342: istore #16
    //   2344: goto -> 2351
    //   2347: iload #18
    //   2349: istore #16
    //   2351: iinc #1, 1
    //   2354: iload #13
    //   2356: istore #18
    //   2358: goto -> 243
    //   2361: iload #10
    //   2363: ifne -> 2382
    //   2366: iload #9
    //   2368: ifne -> 2396
    //   2371: aload #8
    //   2373: bipush #10
    //   2375: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2378: pop
    //   2379: goto -> 2396
    //   2382: iload #14
    //   2384: iconst_2
    //   2385: if_icmpne -> 2396
    //   2388: aload #8
    //   2390: bipush #41
    //   2392: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2395: pop
    //   2396: aload #8
    //   2398: invokevirtual toString : ()Ljava/lang/String;
    //   2401: areturn
    //   2402: new java/lang/IllegalArgumentException
    //   2405: dup
    //   2406: invokespecial <init> : ()V
    //   2409: athrow
    //   2410: new java/lang/IllegalArgumentException
    //   2413: dup
    //   2414: invokespecial <init> : ()V
    //   2417: athrow
    //   2418: new java/lang/IllegalArgumentException
    //   2421: dup
    //   2422: invokespecial <init> : ()V
    //   2425: athrow
  }
  
  private static int getNext(String paramString, int paramInt1, int paramInt2) {
    if (paramInt2 + 1 < paramInt1) {
      paramInt1 = paramString.charAt(paramInt2 + 1);
    } else {
      paramInt1 = 0;
    } 
    return paramInt1;
  }
  
  private static int getSourceStringEnd(String paramString, int paramInt) {
    return printSourceString(paramString, paramInt, false, null);
  }
  
  private void increaseSourceCapacity(int paramInt) {
    if (paramInt <= this.sourceBuffer.length)
      Kit.codeBug(); 
    int i = this.sourceBuffer.length * 2;
    int j = i;
    if (i < paramInt)
      j = paramInt; 
    char[] arrayOfChar = new char[j];
    System.arraycopy(this.sourceBuffer, 0, arrayOfChar, 0, this.sourceTop);
    this.sourceBuffer = arrayOfChar;
  }
  
  private static int printSourceNumber(String paramString, int paramInt, StringBuilder paramStringBuilder) {
    double d = 0.0D;
    char c = paramString.charAt(paramInt);
    paramInt++;
    if (c == 'S') {
      if (paramStringBuilder != null)
        d = paramString.charAt(paramInt); 
      paramInt++;
    } else if (c == 'J' || c == 'D') {
      if (paramStringBuilder != null) {
        long l = paramString.charAt(paramInt) << 48L | paramString.charAt(paramInt + 1) << 32L | paramString.charAt(paramInt + 2) << 16L | paramString.charAt(paramInt + 3);
        if (c == 'J') {
          d = l;
        } else {
          d = Double.longBitsToDouble(l);
        } 
      } 
      paramInt += 4;
    } else {
      throw new RuntimeException();
    } 
    if (paramStringBuilder != null)
      paramStringBuilder.append(ScriptRuntime.numberToString(d, 10)); 
    return paramInt;
  }
  
  private static int printSourceString(String paramString, int paramInt, boolean paramBoolean, StringBuilder paramStringBuilder) {
    char c = paramString.charAt(paramInt);
    int i = paramInt + 1;
    int j = c;
    paramInt = i;
    if ((0x8000 & c) != 0) {
      j = (c & 0x7FFF) << 16 | paramString.charAt(i);
      paramInt = i + 1;
    } 
    if (paramStringBuilder != null) {
      paramString = paramString.substring(paramInt, paramInt + j);
      if (!paramBoolean) {
        paramStringBuilder.append(paramString);
      } else {
        paramStringBuilder.append('"');
        paramStringBuilder.append(ScriptRuntime.escapeString(paramString));
        paramStringBuilder.append('"');
      } 
    } 
    return paramInt + j;
  }
  
  private String sourceToString(int paramInt) {
    if (paramInt < 0 || this.sourceTop < paramInt)
      Kit.codeBug(); 
    return new String(this.sourceBuffer, paramInt, this.sourceTop - paramInt);
  }
  
  void addEOL(int paramInt) {
    if (paramInt >= 0 && paramInt <= 166) {
      append((char)paramInt);
      append('\001');
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  void addName(String paramString) {
    addToken(39);
    appendString(paramString);
  }
  
  void addNumber(double paramDouble) {
    addToken(40);
    long l = (long)paramDouble;
    if (l != paramDouble) {
      l = Double.doubleToLongBits(paramDouble);
      append('D');
      append((char)(int)(l >> 48L));
      append((char)(int)(l >> 32L));
      append((char)(int)(l >> 16L));
      append((char)(int)l);
    } else {
      if (l < 0L)
        Kit.codeBug(); 
      if (l <= 65535L) {
        append('S');
        append((char)(int)l);
      } else {
        append('J');
        append((char)(int)(l >> 48L));
        append((char)(int)(l >> 32L));
        append((char)(int)(l >> 16L));
        append((char)(int)l);
      } 
    } 
  }
  
  void addRegexp(String paramString1, String paramString2) {
    addToken(48);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('/');
    stringBuilder.append(paramString1);
    stringBuilder.append('/');
    stringBuilder.append(paramString2);
    appendString(stringBuilder.toString());
  }
  
  void addString(String paramString) {
    addToken(41);
    appendString(paramString);
  }
  
  void addToken(int paramInt) {
    if (paramInt >= 0 && paramInt <= 166) {
      append((char)paramInt);
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  int getCurrentOffset() {
    return this.sourceTop;
  }
  
  String getEncodedSource() {
    return sourceToString(0);
  }
  
  int markFunctionEnd(int paramInt) {
    paramInt = getCurrentOffset();
    append('§');
    return paramInt;
  }
  
  int markFunctionStart(int paramInt) {
    int i = getCurrentOffset();
    if (paramInt != 4) {
      addToken(110);
      append((char)paramInt);
    } 
    return i;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Decompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */