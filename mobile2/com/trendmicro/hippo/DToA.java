package com.trendmicro.hippo;

import java.math.BigInteger;

class DToA {
  private static final int Bias = 1023;
  
  private static final int Bletch = 16;
  
  private static final int Bndry_mask = 1048575;
  
  static final int DTOSTR_EXPONENTIAL = 3;
  
  static final int DTOSTR_FIXED = 2;
  
  static final int DTOSTR_PRECISION = 4;
  
  static final int DTOSTR_STANDARD = 0;
  
  static final int DTOSTR_STANDARD_EXPONENTIAL = 1;
  
  private static final int Exp_11 = 1072693248;
  
  private static final int Exp_mask = 2146435072;
  
  private static final int Exp_mask_shifted = 2047;
  
  private static final int Exp_msk1 = 1048576;
  
  private static final long Exp_msk1L = 4503599627370496L;
  
  private static final int Exp_shift = 20;
  
  private static final int Exp_shift1 = 20;
  
  private static final int Exp_shiftL = 52;
  
  private static final int Frac_mask = 1048575;
  
  private static final int Frac_mask1 = 1048575;
  
  private static final long Frac_maskL = 4503599627370495L;
  
  private static final int Int_max = 14;
  
  private static final int Log2P = 1;
  
  private static final int P = 53;
  
  private static final int Quick_max = 14;
  
  private static final int Sign_bit = -2147483648;
  
  private static final int Ten_pmax = 22;
  
  private static final double[] bigtens;
  
  private static final int[] dtoaModes;
  
  private static final int n_bigtens = 5;
  
  private static final double[] tens = new double[] { 
      1.0D, 10.0D, 100.0D, 1000.0D, 10000.0D, 100000.0D, 1000000.0D, 1.0E7D, 1.0E8D, 1.0E9D, 
      1.0E10D, 1.0E11D, 1.0E12D, 1.0E13D, 1.0E14D, 1.0E15D, 1.0E16D, 1.0E17D, 1.0E18D, 1.0E19D, 
      1.0E20D, 1.0E21D, 1.0E22D };
  
  static {
    bigtens = new double[] { 1.0E16D, 1.0E32D, 1.0E64D, 1.0E128D, 1.0E256D };
    dtoaModes = new int[] { 0, 0, 3, 2, 2 };
  }
  
  private static char BASEDIGIT(int paramInt) {
    if (paramInt >= 10) {
      paramInt += 87;
    } else {
      paramInt += 48;
    } 
    return (char)paramInt;
  }
  
  static int JS_dtoa(double paramDouble, int paramInt1, boolean paramBoolean, int paramInt2, boolean[] paramArrayOfboolean, StringBuilder paramStringBuilder) {
    // Byte code:
    //   0: iload_2
    //   1: istore #7
    //   3: iconst_1
    //   4: newarray int
    //   6: astore #8
    //   8: iconst_1
    //   9: newarray int
    //   11: astore #9
    //   13: dload_0
    //   14: invokestatic word0 : (D)I
    //   17: ldc -2147483648
    //   19: iand
    //   20: ifeq -> 43
    //   23: aload #5
    //   25: iconst_0
    //   26: iconst_1
    //   27: bastore
    //   28: dload_0
    //   29: dload_0
    //   30: invokestatic word0 : (D)I
    //   33: ldc 2147483647
    //   35: iand
    //   36: invokestatic setWord0 : (DI)D
    //   39: dstore_0
    //   40: goto -> 48
    //   43: aload #5
    //   45: iconst_0
    //   46: iconst_0
    //   47: bastore
    //   48: dload_0
    //   49: invokestatic word0 : (D)I
    //   52: ldc 2146435072
    //   54: iand
    //   55: ldc 2146435072
    //   57: if_icmpne -> 100
    //   60: dload_0
    //   61: invokestatic word1 : (D)I
    //   64: ifne -> 84
    //   67: dload_0
    //   68: invokestatic word0 : (D)I
    //   71: ldc 1048575
    //   73: iand
    //   74: ifne -> 84
    //   77: ldc 'Infinity'
    //   79: astore #5
    //   81: goto -> 88
    //   84: ldc 'NaN'
    //   86: astore #5
    //   88: aload #6
    //   90: aload #5
    //   92: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: pop
    //   96: sipush #9999
    //   99: ireturn
    //   100: dload_0
    //   101: dconst_0
    //   102: dcmpl
    //   103: ifne -> 122
    //   106: aload #6
    //   108: iconst_0
    //   109: invokevirtual setLength : (I)V
    //   112: aload #6
    //   114: bipush #48
    //   116: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: iconst_1
    //   121: ireturn
    //   122: dload_0
    //   123: aload #8
    //   125: aload #9
    //   127: invokestatic d2b : (D[I[I)Ljava/math/BigInteger;
    //   130: astore #5
    //   132: dload_0
    //   133: invokestatic word0 : (D)I
    //   136: bipush #20
    //   138: iushr
    //   139: sipush #2047
    //   142: iand
    //   143: istore_2
    //   144: iload_2
    //   145: ifeq -> 177
    //   148: dload_0
    //   149: dload_0
    //   150: invokestatic word0 : (D)I
    //   153: ldc 1048575
    //   155: iand
    //   156: ldc 1072693248
    //   158: ior
    //   159: invokestatic setWord0 : (DI)D
    //   162: dstore #10
    //   164: iload_2
    //   165: sipush #1023
    //   168: isub
    //   169: istore #12
    //   171: iconst_0
    //   172: istore #13
    //   174: goto -> 266
    //   177: aload #9
    //   179: iconst_0
    //   180: iaload
    //   181: aload #8
    //   183: iconst_0
    //   184: iaload
    //   185: iadd
    //   186: sipush #1074
    //   189: iadd
    //   190: istore_2
    //   191: iload_2
    //   192: bipush #32
    //   194: if_icmple -> 227
    //   197: dload_0
    //   198: invokestatic word0 : (D)I
    //   201: i2l
    //   202: lstore #14
    //   204: dload_0
    //   205: invokestatic word1 : (D)I
    //   208: iload_2
    //   209: bipush #32
    //   211: isub
    //   212: iushr
    //   213: i2l
    //   214: lload #14
    //   216: bipush #64
    //   218: iload_2
    //   219: isub
    //   220: lshl
    //   221: lor
    //   222: lstore #14
    //   224: goto -> 239
    //   227: dload_0
    //   228: invokestatic word1 : (D)I
    //   231: i2l
    //   232: bipush #32
    //   234: iload_2
    //   235: isub
    //   236: lshl
    //   237: lstore #14
    //   239: lload #14
    //   241: l2d
    //   242: lload #14
    //   244: l2d
    //   245: invokestatic word0 : (D)I
    //   248: ldc 32505856
    //   250: isub
    //   251: invokestatic setWord0 : (DI)D
    //   254: dstore #10
    //   256: iload_2
    //   257: sipush #1075
    //   260: isub
    //   261: istore #12
    //   263: iconst_1
    //   264: istore #13
    //   266: dload #10
    //   268: ldc2_w 1.5
    //   271: dsub
    //   272: ldc2_w 0.289529654602168
    //   275: dmul
    //   276: ldc2_w 0.1760912590558
    //   279: dadd
    //   280: iload #12
    //   282: i2d
    //   283: ldc2_w 0.301029995663981
    //   286: dmul
    //   287: dadd
    //   288: dstore #16
    //   290: dload #16
    //   292: d2i
    //   293: istore #18
    //   295: iload #18
    //   297: istore_2
    //   298: dload #16
    //   300: dconst_0
    //   301: dcmpg
    //   302: ifge -> 322
    //   305: iload #18
    //   307: istore_2
    //   308: dload #16
    //   310: iload #18
    //   312: i2d
    //   313: dcmpl
    //   314: ifeq -> 322
    //   317: iload #18
    //   319: iconst_1
    //   320: isub
    //   321: istore_2
    //   322: iconst_1
    //   323: istore #18
    //   325: iload_2
    //   326: istore #19
    //   328: iload #18
    //   330: istore #20
    //   332: iload_2
    //   333: iflt -> 370
    //   336: iload_2
    //   337: istore #19
    //   339: iload #18
    //   341: istore #20
    //   343: iload_2
    //   344: bipush #22
    //   346: if_icmpgt -> 370
    //   349: iload_2
    //   350: istore #19
    //   352: dload_0
    //   353: getstatic com/trendmicro/hippo/DToA.tens : [D
    //   356: iload_2
    //   357: daload
    //   358: dcmpg
    //   359: ifge -> 367
    //   362: iload_2
    //   363: iconst_1
    //   364: isub
    //   365: istore #19
    //   367: iconst_0
    //   368: istore #20
    //   370: aload #9
    //   372: iconst_0
    //   373: iaload
    //   374: iload #12
    //   376: isub
    //   377: iconst_1
    //   378: isub
    //   379: istore #21
    //   381: iload #21
    //   383: iflt -> 392
    //   386: iconst_0
    //   387: istore #22
    //   389: goto -> 400
    //   392: iload #21
    //   394: ineg
    //   395: istore #22
    //   397: iconst_0
    //   398: istore #21
    //   400: iload #19
    //   402: iflt -> 422
    //   405: iload #21
    //   407: iload #19
    //   409: iadd
    //   410: istore #21
    //   412: iload #19
    //   414: istore #23
    //   416: iconst_0
    //   417: istore #18
    //   419: goto -> 437
    //   422: iload #22
    //   424: iload #19
    //   426: isub
    //   427: istore #22
    //   429: iload #19
    //   431: ineg
    //   432: istore #18
    //   434: iconst_0
    //   435: istore #23
    //   437: iload #7
    //   439: iflt -> 452
    //   442: iload #7
    //   444: istore_2
    //   445: iload #7
    //   447: bipush #9
    //   449: if_icmple -> 454
    //   452: iconst_0
    //   453: istore_2
    //   454: iload_2
    //   455: iconst_5
    //   456: if_icmple -> 470
    //   459: iload_2
    //   460: iconst_4
    //   461: isub
    //   462: istore #24
    //   464: iconst_0
    //   465: istore #25
    //   467: goto -> 476
    //   470: iconst_1
    //   471: istore #25
    //   473: iload_2
    //   474: istore #24
    //   476: iconst_1
    //   477: istore #26
    //   479: iconst_1
    //   480: istore #27
    //   482: iconst_1
    //   483: istore_2
    //   484: iconst_0
    //   485: istore #7
    //   487: iload #24
    //   489: ifeq -> 616
    //   492: iload #24
    //   494: iconst_1
    //   495: if_icmpeq -> 616
    //   498: iload #24
    //   500: iconst_2
    //   501: if_icmpeq -> 588
    //   504: iload #24
    //   506: iconst_3
    //   507: if_icmpeq -> 538
    //   510: iload #24
    //   512: iconst_4
    //   513: if_icmpeq -> 591
    //   516: iload_2
    //   517: istore #26
    //   519: iload #24
    //   521: iconst_5
    //   522: if_icmpeq -> 541
    //   525: iload #4
    //   527: istore_2
    //   528: iconst_0
    //   529: istore #4
    //   531: iload #27
    //   533: istore #26
    //   535: goto -> 632
    //   538: iconst_0
    //   539: istore #26
    //   541: iload #4
    //   543: iload #19
    //   545: iadd
    //   546: iconst_1
    //   547: iadd
    //   548: istore #12
    //   550: iload #12
    //   552: iconst_1
    //   553: isub
    //   554: istore #7
    //   556: iload #12
    //   558: ifgt -> 578
    //   561: iconst_1
    //   562: istore #27
    //   564: iload #4
    //   566: istore_2
    //   567: iload #12
    //   569: istore #4
    //   571: iload #27
    //   573: istore #12
    //   575: goto -> 632
    //   578: iload #4
    //   580: istore_2
    //   581: iload #12
    //   583: istore #4
    //   585: goto -> 632
    //   588: iconst_0
    //   589: istore #26
    //   591: iload #4
    //   593: ifgt -> 601
    //   596: iconst_1
    //   597: istore_2
    //   598: goto -> 604
    //   601: iload #4
    //   603: istore_2
    //   604: iload_2
    //   605: istore #12
    //   607: iload_2
    //   608: istore #7
    //   610: iload_2
    //   611: istore #4
    //   613: goto -> 632
    //   616: iconst_m1
    //   617: istore #7
    //   619: bipush #18
    //   621: istore #12
    //   623: iconst_0
    //   624: istore_2
    //   625: iconst_m1
    //   626: istore #4
    //   628: iload #27
    //   630: istore #26
    //   632: iconst_0
    //   633: istore #28
    //   635: iload #4
    //   637: iflt -> 1576
    //   640: iload #4
    //   642: bipush #14
    //   644: if_icmpgt -> 1576
    //   647: iload #25
    //   649: ifeq -> 1576
    //   652: iconst_0
    //   653: istore #27
    //   655: iconst_0
    //   656: istore #29
    //   658: dload_0
    //   659: dstore #10
    //   661: iload #19
    //   663: istore #25
    //   665: iconst_2
    //   666: istore #12
    //   668: iload #19
    //   670: ifle -> 805
    //   673: getstatic com/trendmicro/hippo/DToA.tens : [D
    //   676: iload #19
    //   678: bipush #15
    //   680: iand
    //   681: daload
    //   682: dstore #30
    //   684: iload #19
    //   686: iconst_4
    //   687: ishr
    //   688: istore #32
    //   690: iload #29
    //   692: istore #27
    //   694: iload #32
    //   696: istore #33
    //   698: dload_0
    //   699: dstore #34
    //   701: dload #30
    //   703: dstore #16
    //   705: iload #32
    //   707: bipush #16
    //   709: iand
    //   710: ifeq -> 742
    //   713: iload #32
    //   715: bipush #15
    //   717: iand
    //   718: istore #33
    //   720: dload_0
    //   721: getstatic com/trendmicro/hippo/DToA.bigtens : [D
    //   724: iconst_4
    //   725: daload
    //   726: ddiv
    //   727: dstore #34
    //   729: iconst_2
    //   730: iconst_1
    //   731: iadd
    //   732: istore #12
    //   734: dload #30
    //   736: dstore #16
    //   738: iload #29
    //   740: istore #27
    //   742: iload #33
    //   744: ifeq -> 796
    //   747: dload #16
    //   749: dstore_0
    //   750: iload #12
    //   752: istore #29
    //   754: iload #33
    //   756: iconst_1
    //   757: iand
    //   758: ifeq -> 777
    //   761: iload #12
    //   763: iconst_1
    //   764: iadd
    //   765: istore #29
    //   767: dload #16
    //   769: getstatic com/trendmicro/hippo/DToA.bigtens : [D
    //   772: iload #27
    //   774: daload
    //   775: dmul
    //   776: dstore_0
    //   777: iload #33
    //   779: iconst_1
    //   780: ishr
    //   781: istore #33
    //   783: iinc #27, 1
    //   786: dload_0
    //   787: dstore #16
    //   789: iload #29
    //   791: istore #12
    //   793: goto -> 742
    //   796: dload #34
    //   798: dload #16
    //   800: ddiv
    //   801: dstore_0
    //   802: goto -> 893
    //   805: iload #19
    //   807: ineg
    //   808: istore #33
    //   810: iload #33
    //   812: ifeq -> 890
    //   815: dload_0
    //   816: getstatic com/trendmicro/hippo/DToA.tens : [D
    //   819: iload #33
    //   821: bipush #15
    //   823: iand
    //   824: daload
    //   825: dmul
    //   826: dstore_0
    //   827: iload #33
    //   829: iconst_4
    //   830: ishr
    //   831: istore #29
    //   833: iload #29
    //   835: ifeq -> 887
    //   838: dload_0
    //   839: dstore #34
    //   841: iload #12
    //   843: istore #33
    //   845: iload #29
    //   847: iconst_1
    //   848: iand
    //   849: ifeq -> 868
    //   852: iload #12
    //   854: iconst_1
    //   855: iadd
    //   856: istore #33
    //   858: dload_0
    //   859: getstatic com/trendmicro/hippo/DToA.bigtens : [D
    //   862: iload #27
    //   864: daload
    //   865: dmul
    //   866: dstore #34
    //   868: iload #29
    //   870: iconst_1
    //   871: ishr
    //   872: istore #29
    //   874: iinc #27, 1
    //   877: dload #34
    //   879: dstore_0
    //   880: iload #33
    //   882: istore #12
    //   884: goto -> 833
    //   887: goto -> 893
    //   890: iconst_0
    //   891: istore #27
    //   893: iload #20
    //   895: ifeq -> 947
    //   898: dload_0
    //   899: dconst_1
    //   900: dcmpg
    //   901: ifge -> 947
    //   904: iload #4
    //   906: ifle -> 947
    //   909: iload #7
    //   911: ifgt -> 924
    //   914: iconst_1
    //   915: istore #29
    //   917: iload #4
    //   919: istore #33
    //   921: goto -> 955
    //   924: iload #7
    //   926: istore #33
    //   928: iinc #19, -1
    //   931: dload_0
    //   932: ldc2_w 10.0
    //   935: dmul
    //   936: dstore_0
    //   937: iinc #12, 1
    //   940: iload #28
    //   942: istore #29
    //   944: goto -> 955
    //   947: iload #28
    //   949: istore #29
    //   951: iload #4
    //   953: istore #33
    //   955: iload #12
    //   957: i2d
    //   958: dload_0
    //   959: dmul
    //   960: ldc2_w 7.0
    //   963: dadd
    //   964: dstore #16
    //   966: dload #16
    //   968: dload #16
    //   970: invokestatic word0 : (D)I
    //   973: ldc 54525952
    //   975: isub
    //   976: invokestatic setWord0 : (DI)D
    //   979: dstore #16
    //   981: iload #33
    //   983: ifne -> 1044
    //   986: dload_0
    //   987: ldc2_w 5.0
    //   990: dsub
    //   991: dstore_0
    //   992: dload_0
    //   993: dload #16
    //   995: dcmpl
    //   996: ifle -> 1014
    //   999: aload #6
    //   1001: bipush #49
    //   1003: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1006: pop
    //   1007: iload #19
    //   1009: iconst_1
    //   1010: iadd
    //   1011: iconst_1
    //   1012: iadd
    //   1013: ireturn
    //   1014: dload_0
    //   1015: dload #16
    //   1017: dneg
    //   1018: dcmpg
    //   1019: ifge -> 1038
    //   1022: aload #6
    //   1024: iconst_0
    //   1025: invokevirtual setLength : (I)V
    //   1028: aload #6
    //   1030: bipush #48
    //   1032: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1035: pop
    //   1036: iconst_1
    //   1037: ireturn
    //   1038: iconst_1
    //   1039: istore #29
    //   1041: goto -> 1044
    //   1044: iload #29
    //   1046: ifne -> 1468
    //   1049: iload #26
    //   1051: ifeq -> 1266
    //   1054: ldc2_w 0.5
    //   1057: getstatic com/trendmicro/hippo/DToA.tens : [D
    //   1060: iload #33
    //   1062: iconst_1
    //   1063: isub
    //   1064: daload
    //   1065: ddiv
    //   1066: dstore #34
    //   1068: iconst_0
    //   1069: istore #27
    //   1071: dload #34
    //   1073: dload #16
    //   1075: dsub
    //   1076: dstore #16
    //   1078: aload #9
    //   1080: astore #36
    //   1082: aload #5
    //   1084: astore #9
    //   1086: dload_0
    //   1087: d2l
    //   1088: lstore #14
    //   1090: iload #22
    //   1092: istore #12
    //   1094: iload #13
    //   1096: istore #22
    //   1098: dload_0
    //   1099: lload #14
    //   1101: l2d
    //   1102: dsub
    //   1103: dstore_0
    //   1104: aload #6
    //   1106: lload #14
    //   1108: ldc2_w 48
    //   1111: ladd
    //   1112: l2i
    //   1113: i2c
    //   1114: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1117: pop
    //   1118: dload_0
    //   1119: dload #16
    //   1121: dcmpg
    //   1122: ifge -> 1130
    //   1125: iload #19
    //   1127: iconst_1
    //   1128: iadd
    //   1129: ireturn
    //   1130: dconst_1
    //   1131: dload_0
    //   1132: dsub
    //   1133: dload #16
    //   1135: dcmpg
    //   1136: ifge -> 1205
    //   1139: aload #6
    //   1141: aload #6
    //   1143: invokevirtual length : ()I
    //   1146: iconst_1
    //   1147: isub
    //   1148: invokevirtual charAt : (I)C
    //   1151: istore_2
    //   1152: aload #6
    //   1154: aload #6
    //   1156: invokevirtual length : ()I
    //   1159: iconst_1
    //   1160: isub
    //   1161: invokevirtual setLength : (I)V
    //   1164: iload_2
    //   1165: bipush #57
    //   1167: if_icmpeq -> 1173
    //   1170: goto -> 1187
    //   1173: aload #6
    //   1175: invokevirtual length : ()I
    //   1178: ifne -> 1202
    //   1181: iinc #19, 1
    //   1184: bipush #48
    //   1186: istore_2
    //   1187: aload #6
    //   1189: iload_2
    //   1190: iconst_1
    //   1191: iadd
    //   1192: i2c
    //   1193: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1196: pop
    //   1197: iload #19
    //   1199: iconst_1
    //   1200: iadd
    //   1201: ireturn
    //   1202: goto -> 1139
    //   1205: iinc #27, 1
    //   1208: iload #27
    //   1210: iload #33
    //   1212: if_icmplt -> 1233
    //   1215: iconst_1
    //   1216: istore #29
    //   1218: iload #27
    //   1220: istore #13
    //   1222: iload #29
    //   1224: istore #27
    //   1226: aload #36
    //   1228: astore #5
    //   1230: goto -> 1500
    //   1233: dload #16
    //   1235: ldc2_w 10.0
    //   1238: dmul
    //   1239: dstore #16
    //   1241: dload_0
    //   1242: ldc2_w 10.0
    //   1245: dmul
    //   1246: dstore_0
    //   1247: aload #9
    //   1249: astore #5
    //   1251: iload #22
    //   1253: istore #13
    //   1255: iload #12
    //   1257: istore #22
    //   1259: aload #36
    //   1261: astore #9
    //   1263: goto -> 1078
    //   1266: aload #5
    //   1268: astore #36
    //   1270: iload #22
    //   1272: istore #12
    //   1274: iload #13
    //   1276: istore #22
    //   1278: getstatic com/trendmicro/hippo/DToA.tens : [D
    //   1281: iload #33
    //   1283: iconst_1
    //   1284: isub
    //   1285: daload
    //   1286: dload #16
    //   1288: dmul
    //   1289: dstore #16
    //   1291: iconst_1
    //   1292: istore #27
    //   1294: iload_2
    //   1295: istore #13
    //   1297: iload #27
    //   1299: istore_2
    //   1300: dload_0
    //   1301: d2l
    //   1302: lstore #14
    //   1304: dload_0
    //   1305: lload #14
    //   1307: l2d
    //   1308: dsub
    //   1309: dstore_0
    //   1310: aload #6
    //   1312: lload #14
    //   1314: ldc2_w 48
    //   1317: ladd
    //   1318: l2i
    //   1319: i2c
    //   1320: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1323: pop
    //   1324: iload_2
    //   1325: iload #33
    //   1327: if_icmpne -> 1456
    //   1330: dload_0
    //   1331: dload #16
    //   1333: ldc2_w 0.5
    //   1336: dadd
    //   1337: dcmpl
    //   1338: ifle -> 1407
    //   1341: aload #6
    //   1343: aload #6
    //   1345: invokevirtual length : ()I
    //   1348: iconst_1
    //   1349: isub
    //   1350: invokevirtual charAt : (I)C
    //   1353: istore_2
    //   1354: aload #6
    //   1356: aload #6
    //   1358: invokevirtual length : ()I
    //   1361: iconst_1
    //   1362: isub
    //   1363: invokevirtual setLength : (I)V
    //   1366: iload_2
    //   1367: bipush #57
    //   1369: if_icmpeq -> 1375
    //   1372: goto -> 1389
    //   1375: aload #6
    //   1377: invokevirtual length : ()I
    //   1380: ifne -> 1404
    //   1383: iinc #19, 1
    //   1386: bipush #48
    //   1388: istore_2
    //   1389: aload #6
    //   1391: iload_2
    //   1392: iconst_1
    //   1393: iadd
    //   1394: i2c
    //   1395: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1398: pop
    //   1399: iload #19
    //   1401: iconst_1
    //   1402: iadd
    //   1403: ireturn
    //   1404: goto -> 1341
    //   1407: dload_0
    //   1408: ldc2_w 0.5
    //   1411: dload #16
    //   1413: dsub
    //   1414: dcmpg
    //   1415: ifge -> 1428
    //   1418: aload #6
    //   1420: invokestatic stripTrailingZeroes : (Ljava/lang/StringBuilder;)V
    //   1423: iload #19
    //   1425: iconst_1
    //   1426: iadd
    //   1427: ireturn
    //   1428: iload_2
    //   1429: istore #27
    //   1431: iconst_1
    //   1432: istore #29
    //   1434: iload #13
    //   1436: istore_2
    //   1437: iload #27
    //   1439: istore #13
    //   1441: iload #29
    //   1443: istore #27
    //   1445: aload #9
    //   1447: astore #5
    //   1449: aload #36
    //   1451: astore #9
    //   1453: goto -> 1500
    //   1456: iinc #2, 1
    //   1459: dload_0
    //   1460: ldc2_w 10.0
    //   1463: dmul
    //   1464: dstore_0
    //   1465: goto -> 1300
    //   1468: aload #9
    //   1470: astore #36
    //   1472: iload #27
    //   1474: istore #28
    //   1476: iload #22
    //   1478: istore #12
    //   1480: iload #13
    //   1482: istore #22
    //   1484: aload #5
    //   1486: astore #9
    //   1488: aload #36
    //   1490: astore #5
    //   1492: iload #29
    //   1494: istore #27
    //   1496: iload #28
    //   1498: istore #13
    //   1500: iload #27
    //   1502: ifeq -> 1543
    //   1505: aload #6
    //   1507: iconst_0
    //   1508: invokevirtual setLength : (I)V
    //   1511: dload #10
    //   1513: dstore_0
    //   1514: iload_2
    //   1515: istore #33
    //   1517: iload #25
    //   1519: istore_2
    //   1520: iload #13
    //   1522: istore #25
    //   1524: aload #5
    //   1526: astore #36
    //   1528: aload #9
    //   1530: astore #5
    //   1532: iload #22
    //   1534: istore #27
    //   1536: iload #12
    //   1538: istore #13
    //   1540: goto -> 1598
    //   1543: iload #33
    //   1545: istore #4
    //   1547: iload_2
    //   1548: istore #33
    //   1550: iload #19
    //   1552: istore_2
    //   1553: iload #13
    //   1555: istore #25
    //   1557: aload #5
    //   1559: astore #36
    //   1561: aload #9
    //   1563: astore #5
    //   1565: iload #22
    //   1567: istore #27
    //   1569: iload #12
    //   1571: istore #13
    //   1573: goto -> 1598
    //   1576: iload #13
    //   1578: istore #27
    //   1580: iload_2
    //   1581: istore #33
    //   1583: iload #22
    //   1585: istore #13
    //   1587: aload #9
    //   1589: astore #36
    //   1591: iload #12
    //   1593: istore #25
    //   1595: iload #19
    //   1597: istore_2
    //   1598: aload #8
    //   1600: iconst_0
    //   1601: iaload
    //   1602: iflt -> 1863
    //   1605: iload_2
    //   1606: bipush #14
    //   1608: if_icmpgt -> 1863
    //   1611: getstatic com/trendmicro/hippo/DToA.tens : [D
    //   1614: iload_2
    //   1615: daload
    //   1616: dstore #10
    //   1618: iload #33
    //   1620: ifge -> 1692
    //   1623: iload #4
    //   1625: ifgt -> 1692
    //   1628: iload #4
    //   1630: iflt -> 1676
    //   1633: dload_0
    //   1634: dload #10
    //   1636: ldc2_w 5.0
    //   1639: dmul
    //   1640: dcmpg
    //   1641: iflt -> 1676
    //   1644: iload_3
    //   1645: ifne -> 1662
    //   1648: dload_0
    //   1649: ldc2_w 5.0
    //   1652: dload #10
    //   1654: dmul
    //   1655: dcmpl
    //   1656: ifne -> 1662
    //   1659: goto -> 1676
    //   1662: aload #6
    //   1664: bipush #49
    //   1666: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1669: pop
    //   1670: iload_2
    //   1671: iconst_1
    //   1672: iadd
    //   1673: iconst_1
    //   1674: iadd
    //   1675: ireturn
    //   1676: aload #6
    //   1678: iconst_0
    //   1679: invokevirtual setLength : (I)V
    //   1682: aload #6
    //   1684: bipush #48
    //   1686: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1689: pop
    //   1690: iconst_1
    //   1691: ireturn
    //   1692: iconst_1
    //   1693: istore #7
    //   1695: dload_0
    //   1696: dload #10
    //   1698: ddiv
    //   1699: d2l
    //   1700: lstore #14
    //   1702: dload_0
    //   1703: lload #14
    //   1705: l2d
    //   1706: dload #10
    //   1708: dmul
    //   1709: dsub
    //   1710: dstore_0
    //   1711: aload #6
    //   1713: lload #14
    //   1715: ldc2_w 48
    //   1718: ladd
    //   1719: l2i
    //   1720: i2c
    //   1721: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1724: pop
    //   1725: iload #7
    //   1727: iload #4
    //   1729: if_icmpne -> 1837
    //   1732: dload_0
    //   1733: dload_0
    //   1734: dadd
    //   1735: dstore_0
    //   1736: dload_0
    //   1737: dload #10
    //   1739: dcmpl
    //   1740: ifgt -> 1769
    //   1743: iload_2
    //   1744: istore #4
    //   1746: dload_0
    //   1747: dload #10
    //   1749: dcmpl
    //   1750: ifne -> 1852
    //   1753: lload #14
    //   1755: lconst_1
    //   1756: land
    //   1757: lconst_0
    //   1758: lcmp
    //   1759: ifne -> 1769
    //   1762: iload_2
    //   1763: istore #4
    //   1765: iload_3
    //   1766: ifeq -> 1852
    //   1769: aload #6
    //   1771: aload #6
    //   1773: invokevirtual length : ()I
    //   1776: iconst_1
    //   1777: isub
    //   1778: invokevirtual charAt : (I)C
    //   1781: istore #4
    //   1783: aload #6
    //   1785: aload #6
    //   1787: invokevirtual length : ()I
    //   1790: iconst_1
    //   1791: isub
    //   1792: invokevirtual setLength : (I)V
    //   1795: iload #4
    //   1797: bipush #57
    //   1799: if_icmpeq -> 1805
    //   1802: goto -> 1820
    //   1805: aload #6
    //   1807: invokevirtual length : ()I
    //   1810: ifne -> 1769
    //   1813: iinc #2, 1
    //   1816: bipush #48
    //   1818: istore #4
    //   1820: aload #6
    //   1822: iload #4
    //   1824: iconst_1
    //   1825: iadd
    //   1826: i2c
    //   1827: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1830: pop
    //   1831: iload_2
    //   1832: istore #4
    //   1834: goto -> 1852
    //   1837: dload_0
    //   1838: ldc2_w 10.0
    //   1841: dmul
    //   1842: dstore_0
    //   1843: dload_0
    //   1844: dconst_0
    //   1845: dcmpl
    //   1846: ifne -> 1857
    //   1849: iload_2
    //   1850: istore #4
    //   1852: iload #4
    //   1854: iconst_1
    //   1855: iadd
    //   1856: ireturn
    //   1857: iinc #7, 1
    //   1860: goto -> 1695
    //   1863: iload #13
    //   1865: istore #22
    //   1867: iload #18
    //   1869: istore #19
    //   1871: iconst_0
    //   1872: istore #37
    //   1874: iload #26
    //   1876: ifeq -> 2055
    //   1879: iload #24
    //   1881: iconst_2
    //   1882: if_icmpge -> 1927
    //   1885: iload #27
    //   1887: ifeq -> 1903
    //   1890: aload #8
    //   1892: iconst_0
    //   1893: iaload
    //   1894: sipush #1075
    //   1897: iadd
    //   1898: istore #12
    //   1900: goto -> 1912
    //   1903: bipush #54
    //   1905: aload #36
    //   1907: iconst_0
    //   1908: iaload
    //   1909: isub
    //   1910: istore #12
    //   1912: iload #12
    //   1914: istore #25
    //   1916: iload #19
    //   1918: istore #12
    //   1920: iload #25
    //   1922: istore #19
    //   1924: goto -> 2004
    //   1927: iload #4
    //   1929: iconst_1
    //   1930: isub
    //   1931: istore #25
    //   1933: iload #19
    //   1935: iload #25
    //   1937: if_icmplt -> 1954
    //   1940: iload #19
    //   1942: iload #25
    //   1944: isub
    //   1945: istore #12
    //   1947: iload #25
    //   1949: istore #19
    //   1951: goto -> 1982
    //   1954: iload #25
    //   1956: iload #19
    //   1958: isub
    //   1959: istore #12
    //   1961: iload #12
    //   1963: istore #19
    //   1965: iload #23
    //   1967: iload #12
    //   1969: iadd
    //   1970: istore #23
    //   1972: iload #18
    //   1974: iload #19
    //   1976: iadd
    //   1977: istore #18
    //   1979: iconst_0
    //   1980: istore #12
    //   1982: iload #4
    //   1984: ifge -> 2000
    //   1987: iload #22
    //   1989: iload #4
    //   1991: isub
    //   1992: istore #22
    //   1994: iconst_0
    //   1995: istore #19
    //   1997: goto -> 2004
    //   2000: iload #4
    //   2002: istore #19
    //   2004: iload #13
    //   2006: iload #19
    //   2008: iadd
    //   2009: istore #25
    //   2011: lconst_1
    //   2012: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   2015: astore #9
    //   2017: iload #21
    //   2019: iload #19
    //   2021: iadd
    //   2022: istore #13
    //   2024: iload #18
    //   2026: istore #29
    //   2028: iload #13
    //   2030: istore #18
    //   2032: iload #22
    //   2034: istore #13
    //   2036: iload #12
    //   2038: istore #28
    //   2040: iload #23
    //   2042: istore #33
    //   2044: iload #25
    //   2046: istore #12
    //   2048: iload #19
    //   2050: istore #27
    //   2052: goto -> 2086
    //   2055: aconst_null
    //   2056: astore #9
    //   2058: iload #25
    //   2060: istore #27
    //   2062: iload #13
    //   2064: istore #12
    //   2066: iload #23
    //   2068: istore #33
    //   2070: iload #18
    //   2072: istore #29
    //   2074: iload #19
    //   2076: istore #28
    //   2078: iload #22
    //   2080: istore #13
    //   2082: iload #21
    //   2084: istore #18
    //   2086: iload #18
    //   2088: istore #21
    //   2090: iload #13
    //   2092: istore #19
    //   2094: iload #12
    //   2096: istore #25
    //   2098: iload #27
    //   2100: istore #23
    //   2102: iload #13
    //   2104: ifle -> 2175
    //   2107: iload #18
    //   2109: istore #21
    //   2111: iload #13
    //   2113: istore #19
    //   2115: iload #12
    //   2117: istore #25
    //   2119: iload #27
    //   2121: istore #23
    //   2123: iload #18
    //   2125: ifle -> 2175
    //   2128: iload #13
    //   2130: iload #18
    //   2132: if_icmpge -> 2142
    //   2135: iload #13
    //   2137: istore #19
    //   2139: goto -> 2146
    //   2142: iload #18
    //   2144: istore #19
    //   2146: iload #12
    //   2148: iload #19
    //   2150: isub
    //   2151: istore #25
    //   2153: iload #13
    //   2155: iload #19
    //   2157: isub
    //   2158: istore #13
    //   2160: iload #18
    //   2162: iload #19
    //   2164: isub
    //   2165: istore #21
    //   2167: iload #19
    //   2169: istore #23
    //   2171: iload #13
    //   2173: istore #19
    //   2175: iload #29
    //   2177: ifle -> 2250
    //   2180: iload #26
    //   2182: ifeq -> 2238
    //   2185: iload #28
    //   2187: ifle -> 2211
    //   2190: aload #9
    //   2192: iload #28
    //   2194: invokestatic pow5mult : (Ljava/math/BigInteger;I)Ljava/math/BigInteger;
    //   2197: astore #9
    //   2199: aload #9
    //   2201: aload #5
    //   2203: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   2206: astore #5
    //   2208: goto -> 2211
    //   2211: iload #29
    //   2213: iload #28
    //   2215: isub
    //   2216: istore #18
    //   2218: iload #18
    //   2220: ifeq -> 2235
    //   2223: aload #5
    //   2225: iload #18
    //   2227: invokestatic pow5mult : (Ljava/math/BigInteger;I)Ljava/math/BigInteger;
    //   2230: astore #5
    //   2232: goto -> 2250
    //   2235: goto -> 2250
    //   2238: aload #5
    //   2240: iload #29
    //   2242: invokestatic pow5mult : (Ljava/math/BigInteger;I)Ljava/math/BigInteger;
    //   2245: astore #5
    //   2247: goto -> 2250
    //   2250: lconst_1
    //   2251: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   2254: astore #8
    //   2256: aload #8
    //   2258: astore #36
    //   2260: iload #33
    //   2262: ifle -> 2274
    //   2265: aload #8
    //   2267: iload #33
    //   2269: invokestatic pow5mult : (Ljava/math/BigInteger;I)Ljava/math/BigInteger;
    //   2272: astore #36
    //   2274: iconst_0
    //   2275: istore #18
    //   2277: iload #21
    //   2279: istore #22
    //   2281: iload #25
    //   2283: istore #12
    //   2285: iload #18
    //   2287: istore #27
    //   2289: iload #24
    //   2291: iconst_2
    //   2292: if_icmpge -> 2373
    //   2295: iload #21
    //   2297: istore #22
    //   2299: iload #25
    //   2301: istore #12
    //   2303: iload #18
    //   2305: istore #27
    //   2307: dload_0
    //   2308: invokestatic word1 : (D)I
    //   2311: ifne -> 2373
    //   2314: iload #21
    //   2316: istore #22
    //   2318: iload #25
    //   2320: istore #12
    //   2322: iload #18
    //   2324: istore #27
    //   2326: dload_0
    //   2327: invokestatic word0 : (D)I
    //   2330: ldc 1048575
    //   2332: iand
    //   2333: ifne -> 2373
    //   2336: iload #21
    //   2338: istore #22
    //   2340: iload #25
    //   2342: istore #12
    //   2344: iload #18
    //   2346: istore #27
    //   2348: dload_0
    //   2349: invokestatic word0 : (D)I
    //   2352: ldc 2145386496
    //   2354: iand
    //   2355: ifeq -> 2373
    //   2358: iload #25
    //   2360: iconst_1
    //   2361: iadd
    //   2362: istore #12
    //   2364: iload #21
    //   2366: iconst_1
    //   2367: iadd
    //   2368: istore #22
    //   2370: iconst_1
    //   2371: istore #27
    //   2373: aload #36
    //   2375: invokevirtual toByteArray : ()[B
    //   2378: astore #8
    //   2380: iconst_0
    //   2381: istore #18
    //   2383: iconst_0
    //   2384: istore #13
    //   2386: iload #13
    //   2388: iconst_4
    //   2389: if_icmpge -> 2430
    //   2392: iload #18
    //   2394: bipush #8
    //   2396: ishl
    //   2397: istore #18
    //   2399: iload #13
    //   2401: aload #8
    //   2403: arraylength
    //   2404: if_icmpge -> 2424
    //   2407: iload #18
    //   2409: aload #8
    //   2411: iload #13
    //   2413: baload
    //   2414: sipush #255
    //   2417: iand
    //   2418: ior
    //   2419: istore #18
    //   2421: goto -> 2424
    //   2424: iinc #13, 1
    //   2427: goto -> 2386
    //   2430: iload #33
    //   2432: ifeq -> 2448
    //   2435: bipush #32
    //   2437: iload #18
    //   2439: invokestatic hi0bits : (I)I
    //   2442: isub
    //   2443: istore #18
    //   2445: goto -> 2451
    //   2448: iconst_1
    //   2449: istore #18
    //   2451: iload #18
    //   2453: iload #22
    //   2455: iadd
    //   2456: bipush #31
    //   2458: iand
    //   2459: istore #13
    //   2461: iload #13
    //   2463: istore #18
    //   2465: iload #18
    //   2467: istore #25
    //   2469: iload #13
    //   2471: ifeq -> 2481
    //   2474: bipush #32
    //   2476: iload #18
    //   2478: isub
    //   2479: istore #25
    //   2481: iload #25
    //   2483: iconst_4
    //   2484: if_icmple -> 2517
    //   2487: iload #25
    //   2489: iconst_4
    //   2490: isub
    //   2491: istore #18
    //   2493: iload #12
    //   2495: iload #18
    //   2497: iadd
    //   2498: istore #23
    //   2500: iload #19
    //   2502: iload #18
    //   2504: iadd
    //   2505: istore #13
    //   2507: iload #22
    //   2509: iload #18
    //   2511: iadd
    //   2512: istore #18
    //   2514: goto -> 2563
    //   2517: iload #22
    //   2519: istore #18
    //   2521: iload #19
    //   2523: istore #13
    //   2525: iload #12
    //   2527: istore #23
    //   2529: iload #25
    //   2531: iconst_4
    //   2532: if_icmpge -> 2563
    //   2535: iload #25
    //   2537: bipush #28
    //   2539: iadd
    //   2540: istore #18
    //   2542: iload #12
    //   2544: iload #18
    //   2546: iadd
    //   2547: istore #23
    //   2549: iload #19
    //   2551: iload #18
    //   2553: iadd
    //   2554: istore #13
    //   2556: iload #22
    //   2558: iload #18
    //   2560: iadd
    //   2561: istore #18
    //   2563: aload #5
    //   2565: astore #8
    //   2567: iload #23
    //   2569: ifle -> 2581
    //   2572: aload #5
    //   2574: iload #23
    //   2576: invokevirtual shiftLeft : (I)Ljava/math/BigInteger;
    //   2579: astore #8
    //   2581: aload #36
    //   2583: astore #38
    //   2585: iload #18
    //   2587: ifle -> 2599
    //   2590: aload #36
    //   2592: iload #18
    //   2594: invokevirtual shiftLeft : (I)Ljava/math/BigInteger;
    //   2597: astore #38
    //   2599: iload #20
    //   2601: ifeq -> 2667
    //   2604: aload #8
    //   2606: aload #38
    //   2608: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   2611: ifge -> 2667
    //   2614: aload #8
    //   2616: ldc2_w 10
    //   2619: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   2622: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   2625: astore #36
    //   2627: aload #9
    //   2629: astore #5
    //   2631: iload #26
    //   2633: ifeq -> 2649
    //   2636: aload #9
    //   2638: ldc2_w 10
    //   2641: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   2644: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   2647: astore #5
    //   2649: aload #5
    //   2651: astore #9
    //   2653: iinc #2, -1
    //   2656: iload #7
    //   2658: istore #4
    //   2660: aload #36
    //   2662: astore #5
    //   2664: goto -> 2671
    //   2667: aload #8
    //   2669: astore #5
    //   2671: iload #4
    //   2673: ifgt -> 2752
    //   2676: iload #24
    //   2678: iconst_2
    //   2679: if_icmple -> 2752
    //   2682: iload #4
    //   2684: iflt -> 2736
    //   2687: aload #5
    //   2689: aload #38
    //   2691: ldc2_w 5
    //   2694: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   2697: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   2700: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   2703: istore #4
    //   2705: iload #4
    //   2707: iflt -> 2736
    //   2710: iload #4
    //   2712: ifne -> 2722
    //   2715: iload_3
    //   2716: ifne -> 2722
    //   2719: goto -> 2736
    //   2722: aload #6
    //   2724: bipush #49
    //   2726: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2729: pop
    //   2730: iload_2
    //   2731: iconst_1
    //   2732: iadd
    //   2733: iconst_1
    //   2734: iadd
    //   2735: ireturn
    //   2736: aload #6
    //   2738: iconst_0
    //   2739: invokevirtual setLength : (I)V
    //   2742: aload #6
    //   2744: bipush #48
    //   2746: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2749: pop
    //   2750: iconst_1
    //   2751: ireturn
    //   2752: iload #26
    //   2754: ifeq -> 3283
    //   2757: aload #9
    //   2759: astore #36
    //   2761: iload #13
    //   2763: ifle -> 2775
    //   2766: aload #9
    //   2768: iload #13
    //   2770: invokevirtual shiftLeft : (I)Ljava/math/BigInteger;
    //   2773: astore #36
    //   2775: iload #27
    //   2777: ifeq -> 2791
    //   2780: aload #36
    //   2782: iconst_1
    //   2783: invokevirtual shiftLeft : (I)Ljava/math/BigInteger;
    //   2786: astore #9
    //   2788: goto -> 2795
    //   2791: aload #36
    //   2793: astore #9
    //   2795: iconst_1
    //   2796: istore #7
    //   2798: aload #5
    //   2800: astore #8
    //   2802: aload #8
    //   2804: aload #38
    //   2806: invokevirtual divideAndRemainder : (Ljava/math/BigInteger;)[Ljava/math/BigInteger;
    //   2809: astore #8
    //   2811: aload #8
    //   2813: iconst_1
    //   2814: aaload
    //   2815: astore #5
    //   2817: aload #8
    //   2819: iconst_0
    //   2820: aaload
    //   2821: invokevirtual intValue : ()I
    //   2824: bipush #48
    //   2826: iadd
    //   2827: i2c
    //   2828: istore #39
    //   2830: aload #5
    //   2832: aload #36
    //   2834: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   2837: istore #22
    //   2839: aload #38
    //   2841: aload #9
    //   2843: invokevirtual subtract : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   2846: astore #8
    //   2848: aload #8
    //   2850: invokevirtual signum : ()I
    //   2853: ifgt -> 2862
    //   2856: iconst_1
    //   2857: istore #19
    //   2859: goto -> 2871
    //   2862: aload #5
    //   2864: aload #8
    //   2866: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   2869: istore #19
    //   2871: iload #19
    //   2873: ifne -> 2966
    //   2876: iload #24
    //   2878: ifne -> 2966
    //   2881: dload_0
    //   2882: invokestatic word1 : (D)I
    //   2885: iconst_1
    //   2886: iand
    //   2887: ifne -> 2966
    //   2890: iload #39
    //   2892: bipush #57
    //   2894: if_icmpne -> 2934
    //   2897: aload #6
    //   2899: bipush #57
    //   2901: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2904: pop
    //   2905: iload_2
    //   2906: istore #4
    //   2908: aload #6
    //   2910: invokestatic roundOff : (Ljava/lang/StringBuilder;)Z
    //   2913: ifeq -> 2929
    //   2916: iload_2
    //   2917: iconst_1
    //   2918: iadd
    //   2919: istore #4
    //   2921: aload #6
    //   2923: bipush #49
    //   2925: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2928: pop
    //   2929: iload #4
    //   2931: iconst_1
    //   2932: iadd
    //   2933: ireturn
    //   2934: iload #39
    //   2936: istore #40
    //   2938: iload #22
    //   2940: ifle -> 2954
    //   2943: iload #39
    //   2945: iconst_1
    //   2946: iadd
    //   2947: i2c
    //   2948: istore #4
    //   2950: iload #4
    //   2952: istore #40
    //   2954: aload #6
    //   2956: iload #40
    //   2958: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   2961: pop
    //   2962: iload_2
    //   2963: iconst_1
    //   2964: iadd
    //   2965: ireturn
    //   2966: iload #22
    //   2968: iflt -> 3155
    //   2971: iload #22
    //   2973: ifne -> 2993
    //   2976: iload #24
    //   2978: ifne -> 2993
    //   2981: dload_0
    //   2982: invokestatic word1 : (D)I
    //   2985: iconst_1
    //   2986: iand
    //   2987: ifne -> 2993
    //   2990: goto -> 3155
    //   2993: iload #19
    //   2995: ifle -> 3057
    //   2998: iload #39
    //   3000: bipush #57
    //   3002: if_icmpne -> 3042
    //   3005: aload #6
    //   3007: bipush #57
    //   3009: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3012: pop
    //   3013: iload_2
    //   3014: istore #4
    //   3016: aload #6
    //   3018: invokestatic roundOff : (Ljava/lang/StringBuilder;)Z
    //   3021: ifeq -> 3037
    //   3024: iload_2
    //   3025: iconst_1
    //   3026: iadd
    //   3027: istore #4
    //   3029: aload #6
    //   3031: bipush #49
    //   3033: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3036: pop
    //   3037: iload #4
    //   3039: iconst_1
    //   3040: iadd
    //   3041: ireturn
    //   3042: aload #6
    //   3044: iload #39
    //   3046: iconst_1
    //   3047: iadd
    //   3048: i2c
    //   3049: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3052: pop
    //   3053: iload_2
    //   3054: iconst_1
    //   3055: iadd
    //   3056: ireturn
    //   3057: aload #6
    //   3059: iload #39
    //   3061: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3064: pop
    //   3065: iload #7
    //   3067: iload #4
    //   3069: if_icmpne -> 3079
    //   3072: iload #39
    //   3074: istore #4
    //   3076: goto -> 3333
    //   3079: aload #5
    //   3081: ldc2_w 10
    //   3084: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   3087: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   3090: astore #8
    //   3092: aload #36
    //   3094: aload #9
    //   3096: if_acmpne -> 3119
    //   3099: aload #9
    //   3101: ldc2_w 10
    //   3104: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   3107: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   3110: astore #5
    //   3112: aload #5
    //   3114: astore #9
    //   3116: goto -> 3145
    //   3119: aload #36
    //   3121: ldc2_w 10
    //   3124: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   3127: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   3130: astore #5
    //   3132: aload #9
    //   3134: ldc2_w 10
    //   3137: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   3140: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   3143: astore #9
    //   3145: iinc #7, 1
    //   3148: aload #5
    //   3150: astore #36
    //   3152: goto -> 2802
    //   3155: iload #19
    //   3157: ifle -> 3267
    //   3160: aload #5
    //   3162: iconst_1
    //   3163: invokevirtual shiftLeft : (I)Ljava/math/BigInteger;
    //   3166: aload #38
    //   3168: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   3171: istore #4
    //   3173: iload #4
    //   3175: ifgt -> 3209
    //   3178: iload #4
    //   3180: ifne -> 3202
    //   3183: iload #39
    //   3185: iconst_1
    //   3186: iand
    //   3187: iconst_1
    //   3188: if_icmpeq -> 3209
    //   3191: iload #39
    //   3193: istore #40
    //   3195: iload_3
    //   3196: ifeq -> 3271
    //   3199: goto -> 3209
    //   3202: iload #39
    //   3204: istore #40
    //   3206: goto -> 3271
    //   3209: iload #39
    //   3211: iconst_1
    //   3212: iadd
    //   3213: i2c
    //   3214: istore #4
    //   3216: iload #39
    //   3218: bipush #57
    //   3220: if_icmpne -> 3260
    //   3223: aload #6
    //   3225: bipush #57
    //   3227: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3230: pop
    //   3231: iload_2
    //   3232: istore #4
    //   3234: aload #6
    //   3236: invokestatic roundOff : (Ljava/lang/StringBuilder;)Z
    //   3239: ifeq -> 3255
    //   3242: iload_2
    //   3243: iconst_1
    //   3244: iadd
    //   3245: istore #4
    //   3247: aload #6
    //   3249: bipush #49
    //   3251: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3254: pop
    //   3255: iload #4
    //   3257: iconst_1
    //   3258: iadd
    //   3259: ireturn
    //   3260: iload #4
    //   3262: istore #40
    //   3264: goto -> 3271
    //   3267: iload #39
    //   3269: istore #40
    //   3271: aload #6
    //   3273: iload #40
    //   3275: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3278: pop
    //   3279: iload_2
    //   3280: iconst_1
    //   3281: iadd
    //   3282: ireturn
    //   3283: iconst_1
    //   3284: istore #7
    //   3286: aload #5
    //   3288: aload #38
    //   3290: invokevirtual divideAndRemainder : (Ljava/math/BigInteger;)[Ljava/math/BigInteger;
    //   3293: astore #36
    //   3295: aload #36
    //   3297: iconst_1
    //   3298: aaload
    //   3299: astore #5
    //   3301: aload #36
    //   3303: iconst_0
    //   3304: aaload
    //   3305: invokevirtual intValue : ()I
    //   3308: bipush #48
    //   3310: iadd
    //   3311: i2c
    //   3312: istore #39
    //   3314: aload #6
    //   3316: iload #39
    //   3318: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3321: pop
    //   3322: iload #7
    //   3324: iload #4
    //   3326: if_icmplt -> 3405
    //   3329: iload #39
    //   3331: istore #4
    //   3333: aload #5
    //   3335: iconst_1
    //   3336: invokevirtual shiftLeft : (I)Ljava/math/BigInteger;
    //   3339: aload #38
    //   3341: invokevirtual compareTo : (Ljava/math/BigInteger;)I
    //   3344: istore #7
    //   3346: iload #7
    //   3348: ifgt -> 3379
    //   3351: iload #7
    //   3353: ifne -> 3371
    //   3356: iload #4
    //   3358: iconst_1
    //   3359: iand
    //   3360: iconst_1
    //   3361: if_icmpeq -> 3379
    //   3364: iload_3
    //   3365: ifeq -> 3371
    //   3368: goto -> 3379
    //   3371: aload #6
    //   3373: invokestatic stripTrailingZeroes : (Ljava/lang/StringBuilder;)V
    //   3376: goto -> 3401
    //   3379: aload #6
    //   3381: invokestatic roundOff : (Ljava/lang/StringBuilder;)Z
    //   3384: ifeq -> 3401
    //   3387: aload #6
    //   3389: bipush #49
    //   3391: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   3394: pop
    //   3395: iload_2
    //   3396: iconst_1
    //   3397: iadd
    //   3398: iconst_1
    //   3399: iadd
    //   3400: ireturn
    //   3401: iload_2
    //   3402: iconst_1
    //   3403: iadd
    //   3404: ireturn
    //   3405: aload #5
    //   3407: ldc2_w 10
    //   3410: invokestatic valueOf : (J)Ljava/math/BigInteger;
    //   3413: invokevirtual multiply : (Ljava/math/BigInteger;)Ljava/math/BigInteger;
    //   3416: astore #5
    //   3418: iinc #7, 1
    //   3421: goto -> 3286
  }
  
  static String JS_dtobasestr(int paramInt, double paramDouble) {
    if (2 <= paramInt && paramInt <= 36) {
      String str;
      int i;
      if (Double.isNaN(paramDouble))
        return "NaN"; 
      if (Double.isInfinite(paramDouble)) {
        if (paramDouble > 0.0D) {
          str = "Infinity";
        } else {
          str = "-Infinity";
        } 
        return str;
      } 
      if (paramDouble == 0.0D)
        return "0"; 
      if (paramDouble >= 0.0D) {
        i = 0;
      } else {
        paramDouble = -paramDouble;
        i = 1;
      } 
      double d = Math.floor(paramDouble);
      long l = (long)d;
      if (l == d) {
        if (i)
          l = -l; 
        str = Long.toString(l, paramInt);
      } else {
        BigInteger bigInteger6;
        l = Double.doubleToLongBits(d);
        int i1 = (int)(l >> 52L) & 0x7FF;
        if (i1 == 0) {
          l = (0xFFFFFFFFFFFFFL & l) << 1L;
        } else {
          l = 0xFFFFFFFFFFFFFL & l | 0x10000000000000L;
        } 
        long l1 = l;
        if (i)
          l1 = -l; 
        i1 -= 1075;
        BigInteger bigInteger7 = BigInteger.valueOf(l1);
        if (i1 > 0) {
          bigInteger6 = bigInteger7.shiftLeft(i1);
        } else {
          bigInteger6 = bigInteger7;
          if (i1 < 0)
            bigInteger6 = bigInteger7.shiftRight(-i1); 
        } 
        str = bigInteger6.toString(paramInt);
      } 
      if (paramDouble == d)
        return str; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append('.');
      l = Double.doubleToLongBits(paramDouble);
      int k = (int)(l >> 32L);
      int m = (int)l;
      int[] arrayOfInt = new int[1];
      BigInteger bigInteger4 = d2b(paramDouble - d, arrayOfInt, new int[1]);
      int n = -(k >>> 20 & 0x7FF);
      int j = n;
      if (n == 0)
        j = -1; 
      n = j + 1076;
      BigInteger bigInteger2 = BigInteger.valueOf(1L);
      BigInteger bigInteger5 = bigInteger2;
      j = n;
      BigInteger bigInteger1 = bigInteger5;
      if (m == 0) {
        j = n;
        bigInteger1 = bigInteger5;
        if ((k & 0xFFFFF) == 0) {
          j = n;
          bigInteger1 = bigInteger5;
          if ((k & 0x7FE00000) != 0) {
            j = n + 1;
            bigInteger1 = BigInteger.valueOf(2L);
          } 
        } 
      } 
      bigInteger5 = bigInteger4.shiftLeft(arrayOfInt[0] + j);
      BigInteger bigInteger3 = BigInteger.valueOf(1L).shiftLeft(j);
      bigInteger4 = BigInteger.valueOf(paramInt);
      n = 0;
      j = i;
      while (true) {
        BigInteger[] arrayOfBigInteger = bigInteger5.multiply(bigInteger4).divideAndRemainder(bigInteger3);
        bigInteger5 = arrayOfBigInteger[1];
        paramInt = (char)arrayOfBigInteger[0].intValue();
        if (bigInteger2 == bigInteger1) {
          bigInteger2 = bigInteger2.multiply(bigInteger4);
          bigInteger1 = bigInteger2;
        } else {
          bigInteger2 = bigInteger2.multiply(bigInteger4);
          bigInteger1 = bigInteger1.multiply(bigInteger4);
        } 
        k = bigInteger5.compareTo(bigInteger2);
        BigInteger bigInteger = bigInteger3.subtract(bigInteger1);
        if (bigInteger.signum() <= 0) {
          i = 1;
        } else {
          i = bigInteger5.compareTo(bigInteger);
        } 
        if (i == 0 && (m & 0x1) == 0) {
          i = paramInt;
          if (k > 0)
            i = paramInt + 1; 
          n = 1;
          paramInt = i;
          i = n;
        } else if (k < 0 || (k == 0 && (m & 0x1) == 0)) {
          if (i > 0) {
            bigInteger5 = bigInteger5.shiftLeft(1);
            if (bigInteger5.compareTo(bigInteger3) > 0)
              paramInt++; 
          } 
          i = 1;
        } else if (i > 0) {
          paramInt++;
          i = 1;
        } else {
          i = n;
        } 
        stringBuilder1.append(BASEDIGIT(paramInt));
        if (i != 0)
          return stringBuilder1.toString(); 
        n = i;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad base: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  static void JS_dtostr(StringBuilder paramStringBuilder, int paramInt1, int paramInt2, double paramDouble) {
    // Byte code:
    //   0: iconst_1
    //   1: newarray boolean
    //   3: astore #5
    //   5: iload_1
    //   6: istore #6
    //   8: iload_1
    //   9: iconst_2
    //   10: if_icmpne -> 35
    //   13: dload_3
    //   14: ldc2_w 1.0E21
    //   17: dcmpl
    //   18: ifge -> 32
    //   21: iload_1
    //   22: istore #6
    //   24: dload_3
    //   25: ldc2_w -1.0E21
    //   28: dcmpg
    //   29: ifgt -> 35
    //   32: iconst_0
    //   33: istore #6
    //   35: getstatic com/trendmicro/hippo/DToA.dtoaModes : [I
    //   38: iload #6
    //   40: iaload
    //   41: istore_1
    //   42: iload #6
    //   44: iconst_2
    //   45: if_icmplt -> 54
    //   48: iconst_1
    //   49: istore #7
    //   51: goto -> 57
    //   54: iconst_0
    //   55: istore #7
    //   57: dload_3
    //   58: iload_1
    //   59: iload #7
    //   61: iload_2
    //   62: aload #5
    //   64: aload_0
    //   65: invokestatic JS_dtoa : (DIZI[ZLjava/lang/StringBuilder;)I
    //   68: istore #8
    //   70: aload_0
    //   71: invokevirtual length : ()I
    //   74: istore #9
    //   76: iload #8
    //   78: sipush #9999
    //   81: if_icmpeq -> 365
    //   84: iconst_0
    //   85: istore #10
    //   87: iconst_0
    //   88: istore #11
    //   90: iconst_0
    //   91: istore_1
    //   92: iload #6
    //   94: ifeq -> 200
    //   97: iload #6
    //   99: iconst_1
    //   100: if_icmpeq -> 194
    //   103: iload #6
    //   105: iconst_2
    //   106: if_icmpeq -> 168
    //   109: iload #6
    //   111: iconst_3
    //   112: if_icmpeq -> 163
    //   115: iload #6
    //   117: iconst_4
    //   118: if_icmpeq -> 131
    //   121: iload #10
    //   123: istore #6
    //   125: iload #11
    //   127: istore_1
    //   128: goto -> 233
    //   131: iload_2
    //   132: istore #11
    //   134: iload #8
    //   136: bipush #-5
    //   138: if_icmplt -> 154
    //   141: iload #10
    //   143: istore #6
    //   145: iload #11
    //   147: istore_1
    //   148: iload #8
    //   150: iload_2
    //   151: if_icmple -> 233
    //   154: iconst_1
    //   155: istore #6
    //   157: iload #11
    //   159: istore_1
    //   160: goto -> 233
    //   163: iload_2
    //   164: istore_1
    //   165: goto -> 194
    //   168: iload_2
    //   169: iflt -> 184
    //   172: iload #8
    //   174: iload_2
    //   175: iadd
    //   176: istore_1
    //   177: iload #10
    //   179: istore #6
    //   181: goto -> 233
    //   184: iload #8
    //   186: istore_1
    //   187: iload #10
    //   189: istore #6
    //   191: goto -> 233
    //   194: iconst_1
    //   195: istore #6
    //   197: goto -> 233
    //   200: iload #8
    //   202: bipush #-5
    //   204: if_icmplt -> 227
    //   207: iload #8
    //   209: bipush #21
    //   211: if_icmple -> 217
    //   214: goto -> 227
    //   217: iload #8
    //   219: istore_1
    //   220: iload #10
    //   222: istore #6
    //   224: goto -> 233
    //   227: iconst_1
    //   228: istore #6
    //   230: iload #11
    //   232: istore_1
    //   233: iload #9
    //   235: istore_2
    //   236: iload #9
    //   238: iload_1
    //   239: if_icmpge -> 259
    //   242: aload_0
    //   243: bipush #48
    //   245: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   248: pop
    //   249: aload_0
    //   250: invokevirtual length : ()I
    //   253: iload_1
    //   254: if_icmpne -> 242
    //   257: iload_1
    //   258: istore_2
    //   259: iload #6
    //   261: ifeq -> 310
    //   264: iload_2
    //   265: iconst_1
    //   266: if_icmpeq -> 277
    //   269: aload_0
    //   270: iconst_1
    //   271: bipush #46
    //   273: invokevirtual insert : (IC)Ljava/lang/StringBuilder;
    //   276: pop
    //   277: aload_0
    //   278: bipush #101
    //   280: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   283: pop
    //   284: iload #8
    //   286: iconst_1
    //   287: isub
    //   288: iflt -> 298
    //   291: aload_0
    //   292: bipush #43
    //   294: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   297: pop
    //   298: aload_0
    //   299: iload #8
    //   301: iconst_1
    //   302: isub
    //   303: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   306: pop
    //   307: goto -> 365
    //   310: iload #8
    //   312: iload_2
    //   313: if_icmpeq -> 365
    //   316: iload #8
    //   318: ifle -> 333
    //   321: aload_0
    //   322: iload #8
    //   324: bipush #46
    //   326: invokevirtual insert : (IC)Ljava/lang/StringBuilder;
    //   329: pop
    //   330: goto -> 365
    //   333: iconst_0
    //   334: istore_1
    //   335: iload_1
    //   336: iconst_1
    //   337: iload #8
    //   339: isub
    //   340: if_icmpge -> 357
    //   343: aload_0
    //   344: iconst_0
    //   345: bipush #48
    //   347: invokevirtual insert : (IC)Ljava/lang/StringBuilder;
    //   350: pop
    //   351: iinc #1, 1
    //   354: goto -> 335
    //   357: aload_0
    //   358: iconst_1
    //   359: bipush #46
    //   361: invokevirtual insert : (IC)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: aload #5
    //   367: iconst_0
    //   368: baload
    //   369: ifeq -> 425
    //   372: dload_3
    //   373: invokestatic word0 : (D)I
    //   376: ldc -2147483648
    //   378: if_icmpne -> 388
    //   381: dload_3
    //   382: invokestatic word1 : (D)I
    //   385: ifeq -> 425
    //   388: dload_3
    //   389: invokestatic word0 : (D)I
    //   392: ldc 2146435072
    //   394: iand
    //   395: ldc 2146435072
    //   397: if_icmpne -> 417
    //   400: dload_3
    //   401: invokestatic word1 : (D)I
    //   404: ifne -> 425
    //   407: dload_3
    //   408: invokestatic word0 : (D)I
    //   411: ldc 1048575
    //   413: iand
    //   414: ifne -> 425
    //   417: aload_0
    //   418: iconst_0
    //   419: bipush #45
    //   421: invokevirtual insert : (IC)Ljava/lang/StringBuilder;
    //   424: pop
    //   425: return
  }
  
  private static BigInteger d2b(double paramDouble, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    byte[] arrayOfByte;
    int n;
    long l = Double.doubleToLongBits(paramDouble);
    int i = (int)(l >>> 32L);
    int j = (int)l;
    int k = 0xFFFFF & i;
    int m = (i & Integer.MAX_VALUE) >>> 20;
    i = k;
    if (m != 0)
      i = k | 0x100000; 
    if (j != 0) {
      arrayOfByte = new byte[8];
      n = lo0bits(j);
      k = j >>> n;
      if (n != 0) {
        stuffBits(arrayOfByte, 4, i << 32 - n | k);
        i >>= n;
      } else {
        stuffBits(arrayOfByte, 4, k);
      } 
      stuffBits(arrayOfByte, 0, i);
      if (i != 0) {
        k = 2;
      } else {
        k = 1;
      } 
    } else {
      arrayOfByte = new byte[4];
      k = lo0bits(i);
      i >>>= k;
      stuffBits(arrayOfByte, 0, i);
      n = k + 32;
      k = 1;
    } 
    if (m != 0) {
      paramArrayOfint1[0] = m - 1023 - 52 + n;
      paramArrayOfint2[0] = 53 - n;
    } else {
      paramArrayOfint1[0] = m - 1023 - 52 + 1 + n;
      paramArrayOfint2[0] = k * 32 - hi0bits(i);
    } 
    return new BigInteger(arrayOfByte);
  }
  
  private static int hi0bits(int paramInt) {
    int i = 0;
    int j = paramInt;
    if ((0xFFFF0000 & paramInt) == 0) {
      i = 16;
      j = paramInt << 16;
    } 
    int k = i;
    paramInt = j;
    if ((0xFF000000 & j) == 0) {
      k = i + 8;
      paramInt = j << 8;
    } 
    i = k;
    j = paramInt;
    if ((0xF0000000 & paramInt) == 0) {
      i = k + 4;
      j = paramInt << 4;
    } 
    paramInt = i;
    k = j;
    if ((0xC0000000 & j) == 0) {
      paramInt = i + 2;
      k = j << 2;
    } 
    j = paramInt;
    if ((Integer.MIN_VALUE & k) == 0) {
      j = paramInt + 1;
      if ((0x40000000 & k) == 0)
        return 32; 
    } 
    return j;
  }
  
  private static int lo0bits(int paramInt) {
    int i = paramInt;
    if ((i & 0x7) != 0)
      return ((i & 0x1) != 0) ? 0 : (((i & 0x2) != 0) ? 1 : 2); 
    int j = 0;
    paramInt = i;
    if ((0xFFFF & i) == 0) {
      j = 16;
      paramInt = i >>> 16;
    } 
    int k = paramInt;
    i = j;
    if ((paramInt & 0xFF) == 0) {
      i = j + 8;
      k = paramInt >>> 8;
    } 
    paramInt = k;
    j = i;
    if ((k & 0xF) == 0) {
      j = i + 4;
      paramInt = k >>> 4;
    } 
    k = paramInt;
    i = j;
    if ((paramInt & 0x3) == 0) {
      i = j + 2;
      k = paramInt >>> 2;
    } 
    paramInt = i;
    if ((k & 0x1) == 0) {
      paramInt = i + 1;
      if ((k >>> 1 & 0x1) == 0)
        return 32; 
    } 
    return paramInt;
  }
  
  static BigInteger pow5mult(BigInteger paramBigInteger, int paramInt) {
    return paramBigInteger.multiply(BigInteger.valueOf(5L).pow(paramInt));
  }
  
  static boolean roundOff(StringBuilder paramStringBuilder) {
    int i = paramStringBuilder.length();
    while (i != 0) {
      char c = paramStringBuilder.charAt(--i);
      if (c != '9') {
        paramStringBuilder.setCharAt(i, (char)(c + 1));
        paramStringBuilder.setLength(i + 1);
        return false;
      } 
    } 
    paramStringBuilder.setLength(0);
    return true;
  }
  
  static double setWord0(double paramDouble, int paramInt) {
    long l = Double.doubleToLongBits(paramDouble);
    return Double.longBitsToDouble(paramInt << 32L | 0xFFFFFFFFL & l);
  }
  
  private static void stripTrailingZeroes(StringBuilder paramStringBuilder) {
    int j;
    int i = paramStringBuilder.length();
    while (true) {
      j = i - 1;
      if (i > 0 && paramStringBuilder.charAt(j) == '0') {
        i = j;
        continue;
      } 
      break;
    } 
    paramStringBuilder.setLength(j + 1);
  }
  
  private static void stuffBits(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    paramArrayOfbyte[paramInt1] = (byte)(byte)(paramInt2 >> 24);
    paramArrayOfbyte[paramInt1 + 1] = (byte)(byte)(paramInt2 >> 16);
    paramArrayOfbyte[paramInt1 + 2] = (byte)(byte)(paramInt2 >> 8);
    paramArrayOfbyte[paramInt1 + 3] = (byte)(byte)paramInt2;
  }
  
  static int word0(double paramDouble) {
    return (int)(Double.doubleToLongBits(paramDouble) >> 32L);
  }
  
  static int word1(double paramDouble) {
    return (int)Double.doubleToLongBits(paramDouble);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/DToA.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */