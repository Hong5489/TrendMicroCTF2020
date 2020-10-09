package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

class Chain {
  private static final boolean DEBUG = false;
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt) {
    byte b1;
    int i;
    ChainHead[] arrayOfChainHead;
    if (paramInt == 0) {
      b1 = 0;
      i = paramConstraintWidgetContainer.mHorizontalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mHorizontalChainsArray;
    } else {
      b1 = 2;
      i = paramConstraintWidgetContainer.mVerticalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mVerticalChainsArray;
    } 
    for (byte b2 = 0; b2 < i; b2++) {
      ChainHead chainHead = arrayOfChainHead[b2];
      chainHead.define();
      if (paramConstraintWidgetContainer.optimizeFor(4)) {
        if (!Optimizer.applyChainOptimized(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead))
          applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead); 
      } else {
        applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead);
      } 
    } 
  }
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   5: astore #5
    //   7: aload #4
    //   9: getfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   12: astore #6
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   19: astore #7
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   26: astore #8
    //   28: aload #4
    //   30: getfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   33: astore #9
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #10
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   47: astore #11
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   54: astore #12
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   65: if_acmpne -> 74
    //   68: iconst_1
    //   69: istore #13
    //   71: goto -> 77
    //   74: iconst_0
    //   75: istore #13
    //   77: iload_2
    //   78: ifne -> 164
    //   81: aload #9
    //   83: getfield mHorizontalChainStyle : I
    //   86: ifne -> 95
    //   89: iconst_1
    //   90: istore #14
    //   92: goto -> 98
    //   95: iconst_0
    //   96: istore #14
    //   98: aload #9
    //   100: getfield mHorizontalChainStyle : I
    //   103: istore #15
    //   105: iload #14
    //   107: istore #16
    //   109: iload #15
    //   111: iconst_1
    //   112: if_icmpne -> 121
    //   115: iconst_1
    //   116: istore #14
    //   118: goto -> 124
    //   121: iconst_0
    //   122: istore #14
    //   124: aload #9
    //   126: getfield mHorizontalChainStyle : I
    //   129: iconst_2
    //   130: if_icmpne -> 139
    //   133: iconst_1
    //   134: istore #15
    //   136: goto -> 142
    //   139: iconst_0
    //   140: istore #15
    //   142: iconst_0
    //   143: istore #17
    //   145: aload #5
    //   147: astore #18
    //   149: iload #14
    //   151: istore #19
    //   153: iload #17
    //   155: istore #14
    //   157: iload #15
    //   159: istore #17
    //   161: goto -> 244
    //   164: aload #9
    //   166: getfield mVerticalChainStyle : I
    //   169: ifne -> 178
    //   172: iconst_1
    //   173: istore #14
    //   175: goto -> 181
    //   178: iconst_0
    //   179: istore #14
    //   181: aload #9
    //   183: getfield mVerticalChainStyle : I
    //   186: istore #15
    //   188: iload #14
    //   190: istore #16
    //   192: iload #15
    //   194: iconst_1
    //   195: if_icmpne -> 204
    //   198: iconst_1
    //   199: istore #14
    //   201: goto -> 207
    //   204: iconst_0
    //   205: istore #14
    //   207: aload #9
    //   209: getfield mVerticalChainStyle : I
    //   212: iconst_2
    //   213: if_icmpne -> 222
    //   216: iconst_1
    //   217: istore #15
    //   219: goto -> 225
    //   222: iconst_0
    //   223: istore #15
    //   225: iconst_0
    //   226: istore #20
    //   228: aload #5
    //   230: astore #18
    //   232: iload #14
    //   234: istore #19
    //   236: iload #15
    //   238: istore #17
    //   240: iload #20
    //   242: istore #14
    //   244: iload #14
    //   246: ifne -> 616
    //   249: aload #18
    //   251: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   254: iload_3
    //   255: aaload
    //   256: astore #21
    //   258: iconst_4
    //   259: istore #15
    //   261: iload #13
    //   263: ifne -> 271
    //   266: iload #17
    //   268: ifeq -> 274
    //   271: iconst_1
    //   272: istore #15
    //   274: aload #21
    //   276: invokevirtual getMargin : ()I
    //   279: istore #20
    //   281: aload #21
    //   283: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   286: ifnull -> 312
    //   289: aload #18
    //   291: aload #5
    //   293: if_acmpeq -> 312
    //   296: iload #20
    //   298: aload #21
    //   300: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   303: invokevirtual getMargin : ()I
    //   306: iadd
    //   307: istore #20
    //   309: goto -> 312
    //   312: iload #17
    //   314: ifeq -> 338
    //   317: aload #18
    //   319: aload #5
    //   321: if_acmpeq -> 338
    //   324: aload #18
    //   326: aload #7
    //   328: if_acmpeq -> 338
    //   331: bipush #6
    //   333: istore #15
    //   335: goto -> 354
    //   338: iload #16
    //   340: ifeq -> 354
    //   343: iload #13
    //   345: ifeq -> 354
    //   348: iconst_4
    //   349: istore #15
    //   351: goto -> 354
    //   354: aload #21
    //   356: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   359: ifnull -> 438
    //   362: aload #18
    //   364: aload #7
    //   366: if_acmpne -> 392
    //   369: aload_1
    //   370: aload #21
    //   372: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   375: aload #21
    //   377: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   380: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   383: iload #20
    //   385: iconst_5
    //   386: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   389: goto -> 413
    //   392: aload_1
    //   393: aload #21
    //   395: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   398: aload #21
    //   400: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   403: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   406: iload #20
    //   408: bipush #6
    //   410: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   413: aload_1
    //   414: aload #21
    //   416: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   419: aload #21
    //   421: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   424: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   427: iload #20
    //   429: iload #15
    //   431: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   434: pop
    //   435: goto -> 438
    //   438: iload #13
    //   440: ifeq -> 526
    //   443: aload #18
    //   445: invokevirtual getVisibility : ()I
    //   448: bipush #8
    //   450: if_icmpeq -> 497
    //   453: aload #18
    //   455: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   458: iload_2
    //   459: aaload
    //   460: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   463: if_acmpne -> 497
    //   466: aload_1
    //   467: aload #18
    //   469: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   472: iload_3
    //   473: iconst_1
    //   474: iadd
    //   475: aaload
    //   476: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   479: aload #18
    //   481: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   484: iload_3
    //   485: aaload
    //   486: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   489: iconst_0
    //   490: iconst_5
    //   491: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   494: goto -> 497
    //   497: aload_1
    //   498: aload #18
    //   500: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   503: iload_3
    //   504: aaload
    //   505: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   508: aload_0
    //   509: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   512: iload_3
    //   513: aaload
    //   514: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   517: iconst_0
    //   518: bipush #6
    //   520: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   523: goto -> 526
    //   526: aload #18
    //   528: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   531: iload_3
    //   532: iconst_1
    //   533: iadd
    //   534: aaload
    //   535: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   538: astore #21
    //   540: aload #21
    //   542: ifnull -> 595
    //   545: aload #21
    //   547: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   550: astore #21
    //   552: aload #21
    //   554: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   557: iload_3
    //   558: aaload
    //   559: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   562: ifnull -> 589
    //   565: aload #21
    //   567: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   570: iload_3
    //   571: aaload
    //   572: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   575: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   578: aload #18
    //   580: if_acmpeq -> 586
    //   583: goto -> 589
    //   586: goto -> 598
    //   589: aconst_null
    //   590: astore #21
    //   592: goto -> 598
    //   595: aconst_null
    //   596: astore #21
    //   598: aload #21
    //   600: ifnull -> 610
    //   603: aload #21
    //   605: astore #18
    //   607: goto -> 613
    //   610: iconst_1
    //   611: istore #14
    //   613: goto -> 244
    //   616: aload #8
    //   618: ifnull -> 681
    //   621: aload #6
    //   623: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   626: iload_3
    //   627: iconst_1
    //   628: iadd
    //   629: aaload
    //   630: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   633: ifnull -> 681
    //   636: aload #8
    //   638: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   641: iload_3
    //   642: iconst_1
    //   643: iadd
    //   644: aaload
    //   645: astore #21
    //   647: aload_1
    //   648: aload #21
    //   650: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   653: aload #6
    //   655: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   658: iload_3
    //   659: iconst_1
    //   660: iadd
    //   661: aaload
    //   662: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   665: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   668: aload #21
    //   670: invokevirtual getMargin : ()I
    //   673: ineg
    //   674: iconst_5
    //   675: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   678: goto -> 681
    //   681: iload #13
    //   683: ifeq -> 727
    //   686: aload_1
    //   687: aload_0
    //   688: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   691: iload_3
    //   692: iconst_1
    //   693: iadd
    //   694: aaload
    //   695: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   698: aload #6
    //   700: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   703: iload_3
    //   704: iconst_1
    //   705: iadd
    //   706: aaload
    //   707: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   710: aload #6
    //   712: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   715: iload_3
    //   716: iconst_1
    //   717: iadd
    //   718: aaload
    //   719: invokevirtual getMargin : ()I
    //   722: bipush #6
    //   724: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   727: aload #4
    //   729: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   732: astore_0
    //   733: aload_0
    //   734: ifnull -> 1049
    //   737: aload_0
    //   738: invokevirtual size : ()I
    //   741: istore #15
    //   743: iload #15
    //   745: iconst_1
    //   746: if_icmple -> 1040
    //   749: aconst_null
    //   750: astore #22
    //   752: fconst_0
    //   753: fstore #23
    //   755: aload #4
    //   757: getfield mHasUndefinedWeights : Z
    //   760: ifeq -> 782
    //   763: aload #4
    //   765: getfield mHasComplexMatchWeights : Z
    //   768: ifne -> 782
    //   771: aload #4
    //   773: getfield mWidgetsMatchCount : I
    //   776: i2f
    //   777: fstore #24
    //   779: goto -> 786
    //   782: fload #10
    //   784: fstore #24
    //   786: iconst_0
    //   787: istore #14
    //   789: aload #11
    //   791: astore #21
    //   793: aload #22
    //   795: astore #11
    //   797: iload #14
    //   799: iload #15
    //   801: if_icmpge -> 1031
    //   804: aload_0
    //   805: iload #14
    //   807: invokevirtual get : (I)Ljava/lang/Object;
    //   810: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   813: astore #22
    //   815: aload #22
    //   817: getfield mWeight : [F
    //   820: iload_2
    //   821: faload
    //   822: fstore #10
    //   824: fload #10
    //   826: fconst_0
    //   827: fcmpg
    //   828: ifge -> 881
    //   831: aload #4
    //   833: getfield mHasComplexMatchWeights : Z
    //   836: ifeq -> 875
    //   839: aload_1
    //   840: aload #22
    //   842: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   845: iload_3
    //   846: iconst_1
    //   847: iadd
    //   848: aaload
    //   849: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   852: aload #22
    //   854: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   857: iload_3
    //   858: aaload
    //   859: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   862: iconst_0
    //   863: iconst_4
    //   864: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   867: pop
    //   868: fload #23
    //   870: fstore #10
    //   872: goto -> 1021
    //   875: fconst_1
    //   876: fstore #10
    //   878: goto -> 881
    //   881: fload #10
    //   883: fconst_0
    //   884: fcmpl
    //   885: ifne -> 925
    //   888: aload_1
    //   889: aload #22
    //   891: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   894: iload_3
    //   895: iconst_1
    //   896: iadd
    //   897: aaload
    //   898: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   901: aload #22
    //   903: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   906: iload_3
    //   907: aaload
    //   908: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   911: iconst_0
    //   912: bipush #6
    //   914: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   917: pop
    //   918: fload #23
    //   920: fstore #10
    //   922: goto -> 1021
    //   925: aload #11
    //   927: ifnull -> 1017
    //   930: aload #11
    //   932: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   935: iload_3
    //   936: aaload
    //   937: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   940: astore #25
    //   942: aload #11
    //   944: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   947: iload_3
    //   948: iconst_1
    //   949: iadd
    //   950: aaload
    //   951: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   954: astore #11
    //   956: aload #22
    //   958: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   961: iload_3
    //   962: aaload
    //   963: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   966: astore #26
    //   968: aload #22
    //   970: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   973: iload_3
    //   974: iconst_1
    //   975: iadd
    //   976: aaload
    //   977: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   980: astore #27
    //   982: aload_1
    //   983: invokevirtual createRow : ()Landroid/support/constraint/solver/ArrayRow;
    //   986: astore #28
    //   988: aload #28
    //   990: fload #23
    //   992: fload #24
    //   994: fload #10
    //   996: aload #25
    //   998: aload #11
    //   1000: aload #26
    //   1002: aload #27
    //   1004: invokevirtual createRowEqualMatchDimensions : (FFFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;)Landroid/support/constraint/solver/ArrayRow;
    //   1007: pop
    //   1008: aload_1
    //   1009: aload #28
    //   1011: invokevirtual addConstraint : (Landroid/support/constraint/solver/ArrayRow;)V
    //   1014: goto -> 1017
    //   1017: aload #22
    //   1019: astore #11
    //   1021: iinc #14, 1
    //   1024: fload #10
    //   1026: fstore #23
    //   1028: goto -> 797
    //   1031: aload #18
    //   1033: astore_0
    //   1034: aload #12
    //   1036: astore_0
    //   1037: goto -> 1061
    //   1040: aload #18
    //   1042: astore_0
    //   1043: aload #12
    //   1045: astore_0
    //   1046: goto -> 1061
    //   1049: aload_0
    //   1050: astore #21
    //   1052: aload #12
    //   1054: astore_0
    //   1055: aload #18
    //   1057: astore_0
    //   1058: aload #21
    //   1060: astore_0
    //   1061: aload #7
    //   1063: ifnull -> 1284
    //   1066: aload #7
    //   1068: aload #8
    //   1070: if_acmpeq -> 1084
    //   1073: iload #17
    //   1075: ifeq -> 1081
    //   1078: goto -> 1084
    //   1081: goto -> 1284
    //   1084: aload #5
    //   1086: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1089: iload_3
    //   1090: aaload
    //   1091: astore #21
    //   1093: aload #6
    //   1095: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1098: iload_3
    //   1099: iconst_1
    //   1100: iadd
    //   1101: aaload
    //   1102: astore #18
    //   1104: aload #5
    //   1106: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1109: iload_3
    //   1110: aaload
    //   1111: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1114: ifnull -> 1134
    //   1117: aload #5
    //   1119: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1122: iload_3
    //   1123: aaload
    //   1124: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1127: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1130: astore_0
    //   1131: goto -> 1136
    //   1134: aconst_null
    //   1135: astore_0
    //   1136: aload #6
    //   1138: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1141: iload_3
    //   1142: iconst_1
    //   1143: iadd
    //   1144: aaload
    //   1145: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1148: ifnull -> 1171
    //   1151: aload #6
    //   1153: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1156: iload_3
    //   1157: iconst_1
    //   1158: iadd
    //   1159: aaload
    //   1160: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1163: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1166: astore #4
    //   1168: goto -> 1174
    //   1171: aconst_null
    //   1172: astore #4
    //   1174: aload #7
    //   1176: aload #8
    //   1178: if_acmpne -> 1208
    //   1181: aload #7
    //   1183: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1186: iload_3
    //   1187: aaload
    //   1188: astore #18
    //   1190: aload #7
    //   1192: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1195: iload_3
    //   1196: iconst_1
    //   1197: iadd
    //   1198: aaload
    //   1199: astore #12
    //   1201: aload #18
    //   1203: astore #21
    //   1205: goto -> 1212
    //   1208: aload #18
    //   1210: astore #12
    //   1212: aload_0
    //   1213: ifnull -> 1281
    //   1216: aload #4
    //   1218: ifnull -> 1281
    //   1221: iload_2
    //   1222: ifne -> 1235
    //   1225: aload #9
    //   1227: getfield mHorizontalBiasPercent : F
    //   1230: fstore #10
    //   1232: goto -> 1242
    //   1235: aload #9
    //   1237: getfield mVerticalBiasPercent : F
    //   1240: fstore #10
    //   1242: aload #21
    //   1244: invokevirtual getMargin : ()I
    //   1247: istore #14
    //   1249: aload #12
    //   1251: invokevirtual getMargin : ()I
    //   1254: istore_2
    //   1255: aload_1
    //   1256: aload #21
    //   1258: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1261: aload_0
    //   1262: iload #14
    //   1264: fload #10
    //   1266: aload #4
    //   1268: aload #12
    //   1270: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1273: iload_2
    //   1274: iconst_5
    //   1275: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1278: goto -> 1281
    //   1281: goto -> 2392
    //   1284: iload #16
    //   1286: ifeq -> 1811
    //   1289: aload #7
    //   1291: ifnull -> 1811
    //   1294: aload #4
    //   1296: getfield mWidgetsMatchCount : I
    //   1299: ifle -> 1321
    //   1302: aload #4
    //   1304: getfield mWidgetsCount : I
    //   1307: aload #4
    //   1309: getfield mWidgetsMatchCount : I
    //   1312: if_icmpne -> 1321
    //   1315: iconst_1
    //   1316: istore #13
    //   1318: goto -> 1324
    //   1321: iconst_0
    //   1322: istore #13
    //   1324: aload #7
    //   1326: astore #18
    //   1328: aload #7
    //   1330: astore #4
    //   1332: aload #18
    //   1334: ifnull -> 1808
    //   1337: aload #18
    //   1339: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1342: iload_2
    //   1343: aaload
    //   1344: astore_0
    //   1345: aload_0
    //   1346: ifnull -> 1368
    //   1349: aload_0
    //   1350: invokevirtual getVisibility : ()I
    //   1353: bipush #8
    //   1355: if_icmpne -> 1368
    //   1358: aload_0
    //   1359: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1362: iload_2
    //   1363: aaload
    //   1364: astore_0
    //   1365: goto -> 1345
    //   1368: aload_0
    //   1369: ifnonnull -> 1389
    //   1372: aload #18
    //   1374: aload #8
    //   1376: if_acmpne -> 1382
    //   1379: goto -> 1389
    //   1382: bipush #8
    //   1384: istore #14
    //   1386: goto -> 1785
    //   1389: aload #18
    //   1391: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1394: iload_3
    //   1395: aaload
    //   1396: astore #11
    //   1398: aload #11
    //   1400: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1403: astore #25
    //   1405: aload #11
    //   1407: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1410: ifnull -> 1426
    //   1413: aload #11
    //   1415: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1418: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1421: astore #21
    //   1423: goto -> 1429
    //   1426: aconst_null
    //   1427: astore #21
    //   1429: aload #4
    //   1431: aload #18
    //   1433: if_acmpeq -> 1453
    //   1436: aload #4
    //   1438: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1441: iload_3
    //   1442: iconst_1
    //   1443: iadd
    //   1444: aaload
    //   1445: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1448: astore #21
    //   1450: goto -> 1504
    //   1453: aload #18
    //   1455: aload #7
    //   1457: if_acmpne -> 1504
    //   1460: aload #4
    //   1462: aload #18
    //   1464: if_acmpne -> 1504
    //   1467: aload #5
    //   1469: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1472: iload_3
    //   1473: aaload
    //   1474: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1477: ifnull -> 1498
    //   1480: aload #5
    //   1482: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1485: iload_3
    //   1486: aaload
    //   1487: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1490: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1493: astore #21
    //   1495: goto -> 1501
    //   1498: aconst_null
    //   1499: astore #21
    //   1501: goto -> 1504
    //   1504: aconst_null
    //   1505: astore #12
    //   1507: aload #11
    //   1509: invokevirtual getMargin : ()I
    //   1512: istore #17
    //   1514: aload #18
    //   1516: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1519: iload_3
    //   1520: iconst_1
    //   1521: iadd
    //   1522: aaload
    //   1523: invokevirtual getMargin : ()I
    //   1526: istore #15
    //   1528: aload_0
    //   1529: ifnull -> 1572
    //   1532: aload_0
    //   1533: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1536: iload_3
    //   1537: aaload
    //   1538: astore #12
    //   1540: aload #12
    //   1542: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1545: astore #22
    //   1547: aload #18
    //   1549: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1552: iload_3
    //   1553: iconst_1
    //   1554: iadd
    //   1555: aaload
    //   1556: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1559: astore #11
    //   1561: aload #12
    //   1563: astore #9
    //   1565: aload #11
    //   1567: astore #12
    //   1569: goto -> 1624
    //   1572: aload #6
    //   1574: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1577: iload_3
    //   1578: iconst_1
    //   1579: iadd
    //   1580: aaload
    //   1581: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1584: astore #11
    //   1586: aload #11
    //   1588: ifnull -> 1598
    //   1591: aload #11
    //   1593: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1596: astore #12
    //   1598: aload #18
    //   1600: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1603: iload_3
    //   1604: iconst_1
    //   1605: iadd
    //   1606: aaload
    //   1607: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1610: astore #9
    //   1612: aload #12
    //   1614: astore #22
    //   1616: aload #9
    //   1618: astore #12
    //   1620: aload #11
    //   1622: astore #9
    //   1624: iload #15
    //   1626: istore #14
    //   1628: aload #9
    //   1630: ifnull -> 1643
    //   1633: iload #15
    //   1635: aload #9
    //   1637: invokevirtual getMargin : ()I
    //   1640: iadd
    //   1641: istore #14
    //   1643: iload #17
    //   1645: istore #15
    //   1647: aload #4
    //   1649: ifnull -> 1669
    //   1652: iload #17
    //   1654: aload #4
    //   1656: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1659: iload_3
    //   1660: iconst_1
    //   1661: iadd
    //   1662: aaload
    //   1663: invokevirtual getMargin : ()I
    //   1666: iadd
    //   1667: istore #15
    //   1669: aload #25
    //   1671: ifnull -> 1781
    //   1674: aload #21
    //   1676: ifnull -> 1781
    //   1679: aload #22
    //   1681: ifnull -> 1781
    //   1684: aload #12
    //   1686: ifnull -> 1781
    //   1689: aload #18
    //   1691: aload #7
    //   1693: if_acmpne -> 1711
    //   1696: aload #7
    //   1698: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1701: iload_3
    //   1702: aaload
    //   1703: invokevirtual getMargin : ()I
    //   1706: istore #15
    //   1708: goto -> 1711
    //   1711: aload #18
    //   1713: aload #8
    //   1715: if_acmpne -> 1735
    //   1718: aload #8
    //   1720: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1723: iload_3
    //   1724: iconst_1
    //   1725: iadd
    //   1726: aaload
    //   1727: invokevirtual getMargin : ()I
    //   1730: istore #14
    //   1732: goto -> 1735
    //   1735: iload #13
    //   1737: ifeq -> 1747
    //   1740: bipush #6
    //   1742: istore #17
    //   1744: goto -> 1750
    //   1747: iconst_4
    //   1748: istore #17
    //   1750: bipush #8
    //   1752: istore #20
    //   1754: aload_1
    //   1755: aload #25
    //   1757: aload #21
    //   1759: iload #15
    //   1761: ldc 0.5
    //   1763: aload #22
    //   1765: aload #12
    //   1767: iload #14
    //   1769: iload #17
    //   1771: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1774: iload #20
    //   1776: istore #14
    //   1778: goto -> 1785
    //   1781: bipush #8
    //   1783: istore #14
    //   1785: aload #18
    //   1787: invokevirtual getVisibility : ()I
    //   1790: iload #14
    //   1792: if_icmpeq -> 1802
    //   1795: aload #18
    //   1797: astore #4
    //   1799: goto -> 1802
    //   1802: aload_0
    //   1803: astore #18
    //   1805: goto -> 1332
    //   1808: goto -> 2392
    //   1811: iload #19
    //   1813: ifeq -> 2392
    //   1816: aload #7
    //   1818: ifnull -> 2392
    //   1821: aload #4
    //   1823: getfield mWidgetsMatchCount : I
    //   1826: ifle -> 1848
    //   1829: aload #4
    //   1831: getfield mWidgetsCount : I
    //   1834: aload #4
    //   1836: getfield mWidgetsMatchCount : I
    //   1839: if_icmpne -> 1848
    //   1842: iconst_1
    //   1843: istore #14
    //   1845: goto -> 1851
    //   1848: iconst_0
    //   1849: istore #14
    //   1851: aload #7
    //   1853: astore #18
    //   1855: aload #7
    //   1857: astore #4
    //   1859: aload #18
    //   1861: ifnull -> 2230
    //   1864: aload #18
    //   1866: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1869: iload_2
    //   1870: aaload
    //   1871: astore_0
    //   1872: aload_0
    //   1873: ifnull -> 1895
    //   1876: aload_0
    //   1877: invokevirtual getVisibility : ()I
    //   1880: bipush #8
    //   1882: if_icmpne -> 1895
    //   1885: aload_0
    //   1886: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1889: iload_2
    //   1890: aaload
    //   1891: astore_0
    //   1892: goto -> 1872
    //   1895: aload #18
    //   1897: aload #7
    //   1899: if_acmpeq -> 2207
    //   1902: aload #18
    //   1904: aload #8
    //   1906: if_acmpeq -> 2207
    //   1909: aload_0
    //   1910: ifnull -> 2207
    //   1913: aload_0
    //   1914: aload #8
    //   1916: if_acmpne -> 1924
    //   1919: aconst_null
    //   1920: astore_0
    //   1921: goto -> 1924
    //   1924: aload #18
    //   1926: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1929: iload_3
    //   1930: aaload
    //   1931: astore #12
    //   1933: aload #12
    //   1935: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1938: astore #9
    //   1940: aload #12
    //   1942: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1945: ifnull -> 1961
    //   1948: aload #12
    //   1950: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1953: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1956: astore #21
    //   1958: goto -> 1961
    //   1961: aload #4
    //   1963: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1966: iload_3
    //   1967: iconst_1
    //   1968: iadd
    //   1969: aaload
    //   1970: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1973: astore #25
    //   1975: aconst_null
    //   1976: astore #21
    //   1978: aload #12
    //   1980: invokevirtual getMargin : ()I
    //   1983: istore #17
    //   1985: aload #18
    //   1987: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1990: iload_3
    //   1991: iconst_1
    //   1992: iadd
    //   1993: aaload
    //   1994: invokevirtual getMargin : ()I
    //   1997: istore #13
    //   1999: aload_0
    //   2000: ifnull -> 2057
    //   2003: aload_0
    //   2004: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2007: iload_3
    //   2008: aaload
    //   2009: astore #22
    //   2011: aload #22
    //   2013: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2016: astore #11
    //   2018: aload #22
    //   2020: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2023: ifnull -> 2039
    //   2026: aload #22
    //   2028: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2031: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2034: astore #21
    //   2036: goto -> 2042
    //   2039: aconst_null
    //   2040: astore #21
    //   2042: aload #21
    //   2044: astore #12
    //   2046: aload #11
    //   2048: astore #21
    //   2050: aload #12
    //   2052: astore #11
    //   2054: goto -> 2101
    //   2057: aload #18
    //   2059: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2062: iload_3
    //   2063: iconst_1
    //   2064: iadd
    //   2065: aaload
    //   2066: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2069: astore #12
    //   2071: aload #12
    //   2073: ifnull -> 2083
    //   2076: aload #12
    //   2078: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2081: astore #21
    //   2083: aload #18
    //   2085: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2088: iload_3
    //   2089: iconst_1
    //   2090: iadd
    //   2091: aaload
    //   2092: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2095: astore #11
    //   2097: aload #12
    //   2099: astore #22
    //   2101: iload #13
    //   2103: istore #15
    //   2105: aload #22
    //   2107: ifnull -> 2120
    //   2110: iload #13
    //   2112: aload #22
    //   2114: invokevirtual getMargin : ()I
    //   2117: iadd
    //   2118: istore #15
    //   2120: iload #17
    //   2122: istore #13
    //   2124: aload #4
    //   2126: ifnull -> 2146
    //   2129: iload #17
    //   2131: aload #4
    //   2133: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2136: iload_3
    //   2137: iconst_1
    //   2138: iadd
    //   2139: aaload
    //   2140: invokevirtual getMargin : ()I
    //   2143: iadd
    //   2144: istore #13
    //   2146: iload #14
    //   2148: ifeq -> 2158
    //   2151: bipush #6
    //   2153: istore #17
    //   2155: goto -> 2161
    //   2158: iconst_4
    //   2159: istore #17
    //   2161: aload #9
    //   2163: ifnull -> 2204
    //   2166: aload #25
    //   2168: ifnull -> 2204
    //   2171: aload #21
    //   2173: ifnull -> 2204
    //   2176: aload #11
    //   2178: ifnull -> 2204
    //   2181: aload_1
    //   2182: aload #9
    //   2184: aload #25
    //   2186: iload #13
    //   2188: ldc 0.5
    //   2190: aload #21
    //   2192: aload #11
    //   2194: iload #15
    //   2196: iload #17
    //   2198: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2201: goto -> 2204
    //   2204: goto -> 2207
    //   2207: aload #18
    //   2209: invokevirtual getVisibility : ()I
    //   2212: bipush #8
    //   2214: if_icmpeq -> 2224
    //   2217: aload #18
    //   2219: astore #4
    //   2221: goto -> 2224
    //   2224: aload_0
    //   2225: astore #18
    //   2227: goto -> 1859
    //   2230: aload #7
    //   2232: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2235: iload_3
    //   2236: aaload
    //   2237: astore #18
    //   2239: aload #5
    //   2241: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2244: iload_3
    //   2245: aaload
    //   2246: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2249: astore #21
    //   2251: aload #8
    //   2253: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2256: iload_3
    //   2257: iconst_1
    //   2258: iadd
    //   2259: aaload
    //   2260: astore #4
    //   2262: aload #6
    //   2264: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2267: iload_3
    //   2268: iconst_1
    //   2269: iadd
    //   2270: aaload
    //   2271: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2274: astore_0
    //   2275: aload #21
    //   2277: ifnull -> 2357
    //   2280: aload #7
    //   2282: aload #8
    //   2284: if_acmpeq -> 2311
    //   2287: aload_1
    //   2288: aload #18
    //   2290: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2293: aload #21
    //   2295: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2298: aload #18
    //   2300: invokevirtual getMargin : ()I
    //   2303: iconst_5
    //   2304: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   2307: pop
    //   2308: goto -> 2357
    //   2311: aload_0
    //   2312: ifnull -> 2354
    //   2315: aload_1
    //   2316: aload #18
    //   2318: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2321: aload #21
    //   2323: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2326: aload #18
    //   2328: invokevirtual getMargin : ()I
    //   2331: ldc 0.5
    //   2333: aload #4
    //   2335: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2338: aload_0
    //   2339: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2342: aload #4
    //   2344: invokevirtual getMargin : ()I
    //   2347: iconst_5
    //   2348: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2351: goto -> 2357
    //   2354: goto -> 2357
    //   2357: aload_0
    //   2358: ifnull -> 2392
    //   2361: aload #7
    //   2363: aload #8
    //   2365: if_acmpeq -> 2392
    //   2368: aload_1
    //   2369: aload #4
    //   2371: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2374: aload_0
    //   2375: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2378: aload #4
    //   2380: invokevirtual getMargin : ()I
    //   2383: ineg
    //   2384: iconst_5
    //   2385: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   2388: pop
    //   2389: goto -> 2392
    //   2392: iload #16
    //   2394: ifne -> 2402
    //   2397: iload #19
    //   2399: ifeq -> 2617
    //   2402: aload #7
    //   2404: ifnull -> 2617
    //   2407: aload #7
    //   2409: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2412: iload_3
    //   2413: aaload
    //   2414: astore #21
    //   2416: aload #8
    //   2418: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2421: iload_3
    //   2422: iconst_1
    //   2423: iadd
    //   2424: aaload
    //   2425: astore #18
    //   2427: aload #21
    //   2429: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2432: ifnull -> 2448
    //   2435: aload #21
    //   2437: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2440: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2443: astore #4
    //   2445: goto -> 2451
    //   2448: aconst_null
    //   2449: astore #4
    //   2451: aload #18
    //   2453: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2456: ifnull -> 2471
    //   2459: aload #18
    //   2461: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2464: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2467: astore_0
    //   2468: goto -> 2473
    //   2471: aconst_null
    //   2472: astore_0
    //   2473: aload #6
    //   2475: aload #8
    //   2477: if_acmpeq -> 2513
    //   2480: aload #6
    //   2482: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2485: iload_3
    //   2486: iconst_1
    //   2487: iadd
    //   2488: aaload
    //   2489: astore_0
    //   2490: aload_0
    //   2491: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2494: ifnull -> 2508
    //   2497: aload_0
    //   2498: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2501: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2504: astore_0
    //   2505: goto -> 2510
    //   2508: aconst_null
    //   2509: astore_0
    //   2510: goto -> 2513
    //   2513: aload #7
    //   2515: aload #8
    //   2517: if_acmpne -> 2547
    //   2520: aload #7
    //   2522: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2525: iload_3
    //   2526: aaload
    //   2527: astore #21
    //   2529: aload #7
    //   2531: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2534: iload_3
    //   2535: iconst_1
    //   2536: iadd
    //   2537: aaload
    //   2538: astore #18
    //   2540: aload #21
    //   2542: astore #7
    //   2544: goto -> 2551
    //   2547: aload #21
    //   2549: astore #7
    //   2551: aload #4
    //   2553: ifnull -> 2617
    //   2556: aload_0
    //   2557: ifnull -> 2617
    //   2560: aload #7
    //   2562: invokevirtual getMargin : ()I
    //   2565: istore_2
    //   2566: aload #8
    //   2568: astore #21
    //   2570: aload #8
    //   2572: ifnonnull -> 2579
    //   2575: aload #6
    //   2577: astore #21
    //   2579: aload #21
    //   2581: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2584: iload_3
    //   2585: iconst_1
    //   2586: iadd
    //   2587: aaload
    //   2588: invokevirtual getMargin : ()I
    //   2591: istore_3
    //   2592: aload_1
    //   2593: aload #7
    //   2595: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2598: aload #4
    //   2600: iload_2
    //   2601: ldc 0.5
    //   2603: aload_0
    //   2604: aload #18
    //   2606: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2609: iload_3
    //   2610: iconst_5
    //   2611: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2614: goto -> 2617
    //   2617: return
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/Chain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */