package com.trendmicro.hippo.optimizer;

import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.ObjArray;
import com.trendmicro.hippo.ast.ScriptNode;

class Optimizer {
  static final int AnyType = 3;
  
  static final int NoType = 0;
  
  static final int NumberType = 1;
  
  private boolean inDirectCallFunction;
  
  private boolean parameterUsedInNumberContext;
  
  OptFunctionNode theFunction;
  
  private static void buildStatementList_r(Node paramNode, ObjArray paramObjArray) {
    int i = paramNode.getType();
    if (i == 130 || i == 142 || i == 133 || i == 110) {
      for (paramNode = paramNode.getFirstChild(); paramNode != null; paramNode = paramNode.getNext())
        buildStatementList_r(paramNode, paramObjArray); 
      return;
    } 
    paramObjArray.add(paramNode);
  }
  
  private boolean convertParameter(Node paramNode) {
    if (this.inDirectCallFunction && paramNode.getType() == 55) {
      int i = this.theFunction.getVarIndex(paramNode);
      if (this.theFunction.isParameter(i)) {
        paramNode.removeProp(8);
        return true;
      } 
    } 
    return false;
  }
  
  private void markDCPNumberContext(Node paramNode) {
    if (this.inDirectCallFunction && paramNode.getType() == 55) {
      int i = this.theFunction.getVarIndex(paramNode);
      if (this.theFunction.isParameter(i))
        this.parameterUsedInNumberContext = true; 
    } 
  }
  
  private void optimizeFunction(OptFunctionNode paramOptFunctionNode) {
    if (paramOptFunctionNode.fnode.requiresActivation())
      return; 
    this.inDirectCallFunction = paramOptFunctionNode.isTargetOfDirectCall();
    this.theFunction = paramOptFunctionNode;
    ObjArray objArray = new ObjArray();
    buildStatementList_r((Node)paramOptFunctionNode.fnode, objArray);
    Node[] arrayOfNode = new Node[objArray.size()];
    objArray.toArray((Object[])arrayOfNode);
    Block.runFlowAnalyzes(paramOptFunctionNode, arrayOfNode);
    if (!paramOptFunctionNode.fnode.requiresActivation()) {
      byte b = 0;
      this.parameterUsedInNumberContext = false;
      int i = arrayOfNode.length;
      while (b < i) {
        rewriteForNumberVariables(arrayOfNode[b], 1);
        b++;
      } 
      paramOptFunctionNode.setParameterNumberContext(this.parameterUsedInNumberContext);
    } 
  }
  
  private void rewriteAsObjectChildren(Node paramNode1, Node paramNode2) {
    while (paramNode2 != null) {
      Node node = paramNode2.getNext();
      if (rewriteForNumberVariables(paramNode2, 0) == 1 && !convertParameter(paramNode2)) {
        paramNode1.removeChild(paramNode2);
        paramNode2 = new Node(150, paramNode2);
        if (node == null) {
          paramNode1.addChildToBack(paramNode2);
        } else {
          paramNode1.addChildBefore(paramNode2, node);
        } 
      } 
      paramNode2 = node;
    } 
  }
  
  private int rewriteForNumberVariables(Node paramNode, int paramInt) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getType : ()I
    //   4: istore_3
    //   5: iload_3
    //   6: bipush #40
    //   8: if_icmpeq -> 1317
    //   11: iload_3
    //   12: sipush #134
    //   15: if_icmpeq -> 1295
    //   18: iload_3
    //   19: sipush #141
    //   22: if_icmpeq -> 1162
    //   25: iload_3
    //   26: sipush #157
    //   29: if_icmpeq -> 994
    //   32: iload_3
    //   33: bipush #55
    //   35: if_icmpeq -> 931
    //   38: iload_3
    //   39: bipush #56
    //   41: if_icmpeq -> 994
    //   44: iload_3
    //   45: bipush #107
    //   47: if_icmpeq -> 849
    //   50: iload_3
    //   51: bipush #108
    //   53: if_icmpeq -> 849
    //   56: iload_3
    //   57: tableswitch default -> 84, 9 -> 633, 10 -> 633, 11 -> 633
    //   84: iload_3
    //   85: tableswitch default -> 124, 14 -> 489, 15 -> 489, 16 -> 489, 17 -> 489, 18 -> 633, 19 -> 633
    //   124: iload_3
    //   125: tableswitch default -> 160, 21 -> 358, 22 -> 633, 23 -> 633, 24 -> 633, 25 -> 633
    //   160: iload_3
    //   161: tableswitch default -> 188, 36 -> 274, 37 -> 1162, 38 -> 199
    //   188: aload_0
    //   189: aload_1
    //   190: aload_1
    //   191: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   194: invokespecial rewriteAsObjectChildren : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   197: iconst_0
    //   198: ireturn
    //   199: aload_1
    //   200: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   203: astore #4
    //   205: aload_0
    //   206: aload #4
    //   208: aload #4
    //   210: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   213: invokespecial rewriteAsObjectChildren : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   216: aload #4
    //   218: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   221: astore #4
    //   223: aload_1
    //   224: bipush #9
    //   226: invokevirtual getProp : (I)Ljava/lang/Object;
    //   229: checkcast com/trendmicro/hippo/optimizer/OptFunctionNode
    //   232: ifnull -> 265
    //   235: aload #4
    //   237: astore_1
    //   238: aload_1
    //   239: ifnull -> 272
    //   242: aload_0
    //   243: aload_1
    //   244: iconst_1
    //   245: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   248: iconst_1
    //   249: if_icmpne -> 257
    //   252: aload_0
    //   253: aload_1
    //   254: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   257: aload_1
    //   258: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   261: astore_1
    //   262: goto -> 238
    //   265: aload_0
    //   266: aload_1
    //   267: aload #4
    //   269: invokespecial rewriteAsObjectChildren : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   272: iconst_0
    //   273: ireturn
    //   274: aload_1
    //   275: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   278: astore #4
    //   280: aload #4
    //   282: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   285: astore #5
    //   287: aload_0
    //   288: aload #4
    //   290: iconst_1
    //   291: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   294: iconst_1
    //   295: if_icmpne -> 329
    //   298: aload_0
    //   299: aload #4
    //   301: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   304: ifne -> 329
    //   307: aload_1
    //   308: aload #4
    //   310: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   313: aload_1
    //   314: new com/trendmicro/hippo/Node
    //   317: dup
    //   318: sipush #150
    //   321: aload #4
    //   323: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   326: invokevirtual addChildToFront : (Lcom/trendmicro/hippo/Node;)V
    //   329: aload_0
    //   330: aload #5
    //   332: iconst_1
    //   333: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   336: iconst_1
    //   337: if_icmpne -> 356
    //   340: aload_0
    //   341: aload #5
    //   343: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   346: ifne -> 356
    //   349: aload_1
    //   350: bipush #8
    //   352: iconst_2
    //   353: invokevirtual putIntProp : (II)V
    //   356: iconst_0
    //   357: ireturn
    //   358: aload_1
    //   359: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   362: astore #4
    //   364: aload #4
    //   366: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   369: astore #5
    //   371: aload_0
    //   372: aload #4
    //   374: iconst_1
    //   375: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   378: istore_3
    //   379: aload_0
    //   380: aload #5
    //   382: iconst_1
    //   383: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   386: istore_2
    //   387: aload_0
    //   388: aload #4
    //   390: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   393: ifeq -> 422
    //   396: aload_0
    //   397: aload #5
    //   399: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   402: ifeq -> 407
    //   405: iconst_0
    //   406: ireturn
    //   407: iload_2
    //   408: iconst_1
    //   409: if_icmpne -> 487
    //   412: aload_1
    //   413: bipush #8
    //   415: iconst_2
    //   416: invokevirtual putIntProp : (II)V
    //   419: goto -> 487
    //   422: aload_0
    //   423: aload #5
    //   425: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   428: ifeq -> 446
    //   431: iload_3
    //   432: iconst_1
    //   433: if_icmpne -> 487
    //   436: aload_1
    //   437: bipush #8
    //   439: iconst_1
    //   440: invokevirtual putIntProp : (II)V
    //   443: goto -> 487
    //   446: iload_3
    //   447: iconst_1
    //   448: if_icmpne -> 475
    //   451: iload_2
    //   452: iconst_1
    //   453: if_icmpne -> 465
    //   456: aload_1
    //   457: bipush #8
    //   459: iconst_0
    //   460: invokevirtual putIntProp : (II)V
    //   463: iconst_1
    //   464: ireturn
    //   465: aload_1
    //   466: bipush #8
    //   468: iconst_1
    //   469: invokevirtual putIntProp : (II)V
    //   472: goto -> 487
    //   475: iload_2
    //   476: iconst_1
    //   477: if_icmpne -> 487
    //   480: aload_1
    //   481: bipush #8
    //   483: iconst_2
    //   484: invokevirtual putIntProp : (II)V
    //   487: iconst_0
    //   488: ireturn
    //   489: aload_1
    //   490: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   493: astore #5
    //   495: aload #5
    //   497: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   500: astore #4
    //   502: aload_0
    //   503: aload #5
    //   505: iconst_1
    //   506: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   509: istore_2
    //   510: aload_0
    //   511: aload #4
    //   513: iconst_1
    //   514: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   517: istore_3
    //   518: aload_0
    //   519: aload #5
    //   521: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   524: aload_0
    //   525: aload #4
    //   527: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   530: aload_0
    //   531: aload #5
    //   533: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   536: ifeq -> 565
    //   539: aload_0
    //   540: aload #4
    //   542: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   545: ifeq -> 550
    //   548: iconst_0
    //   549: ireturn
    //   550: iload_3
    //   551: iconst_1
    //   552: if_icmpne -> 631
    //   555: aload_1
    //   556: bipush #8
    //   558: iconst_2
    //   559: invokevirtual putIntProp : (II)V
    //   562: goto -> 631
    //   565: aload_0
    //   566: aload #4
    //   568: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   571: ifeq -> 589
    //   574: iload_2
    //   575: iconst_1
    //   576: if_icmpne -> 631
    //   579: aload_1
    //   580: bipush #8
    //   582: iconst_1
    //   583: invokevirtual putIntProp : (II)V
    //   586: goto -> 631
    //   589: iload_2
    //   590: iconst_1
    //   591: if_icmpne -> 619
    //   594: iload_3
    //   595: iconst_1
    //   596: if_icmpne -> 609
    //   599: aload_1
    //   600: bipush #8
    //   602: iconst_0
    //   603: invokevirtual putIntProp : (II)V
    //   606: goto -> 631
    //   609: aload_1
    //   610: bipush #8
    //   612: iconst_1
    //   613: invokevirtual putIntProp : (II)V
    //   616: goto -> 631
    //   619: iload_3
    //   620: iconst_1
    //   621: if_icmpne -> 631
    //   624: aload_1
    //   625: bipush #8
    //   627: iconst_2
    //   628: invokevirtual putIntProp : (II)V
    //   631: iconst_0
    //   632: ireturn
    //   633: aload_1
    //   634: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   637: astore #5
    //   639: aload #5
    //   641: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   644: astore #4
    //   646: aload_0
    //   647: aload #5
    //   649: iconst_1
    //   650: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   653: istore_3
    //   654: aload_0
    //   655: aload #4
    //   657: iconst_1
    //   658: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   661: istore_2
    //   662: aload_0
    //   663: aload #5
    //   665: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   668: aload_0
    //   669: aload #4
    //   671: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   674: iload_3
    //   675: iconst_1
    //   676: if_icmpne -> 733
    //   679: iload_2
    //   680: iconst_1
    //   681: if_icmpne -> 693
    //   684: aload_1
    //   685: bipush #8
    //   687: iconst_0
    //   688: invokevirtual putIntProp : (II)V
    //   691: iconst_1
    //   692: ireturn
    //   693: aload_0
    //   694: aload #4
    //   696: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   699: ifne -> 731
    //   702: aload_1
    //   703: aload #4
    //   705: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   708: aload_1
    //   709: new com/trendmicro/hippo/Node
    //   712: dup
    //   713: sipush #151
    //   716: aload #4
    //   718: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   721: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   724: aload_1
    //   725: bipush #8
    //   727: iconst_0
    //   728: invokevirtual putIntProp : (II)V
    //   731: iconst_1
    //   732: ireturn
    //   733: iload_2
    //   734: iconst_1
    //   735: if_icmpne -> 778
    //   738: aload_0
    //   739: aload #5
    //   741: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   744: ifne -> 776
    //   747: aload_1
    //   748: aload #5
    //   750: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   753: aload_1
    //   754: new com/trendmicro/hippo/Node
    //   757: dup
    //   758: sipush #151
    //   761: aload #5
    //   763: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   766: invokevirtual addChildToFront : (Lcom/trendmicro/hippo/Node;)V
    //   769: aload_1
    //   770: bipush #8
    //   772: iconst_0
    //   773: invokevirtual putIntProp : (II)V
    //   776: iconst_1
    //   777: ireturn
    //   778: aload_0
    //   779: aload #5
    //   781: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   784: ifne -> 809
    //   787: aload_1
    //   788: aload #5
    //   790: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   793: aload_1
    //   794: new com/trendmicro/hippo/Node
    //   797: dup
    //   798: sipush #151
    //   801: aload #5
    //   803: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   806: invokevirtual addChildToFront : (Lcom/trendmicro/hippo/Node;)V
    //   809: aload_0
    //   810: aload #4
    //   812: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   815: ifne -> 840
    //   818: aload_1
    //   819: aload #4
    //   821: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   824: aload_1
    //   825: new com/trendmicro/hippo/Node
    //   828: dup
    //   829: sipush #151
    //   832: aload #4
    //   834: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   837: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   840: aload_1
    //   841: bipush #8
    //   843: iconst_0
    //   844: invokevirtual putIntProp : (II)V
    //   847: iconst_1
    //   848: ireturn
    //   849: aload_1
    //   850: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   853: astore #4
    //   855: aload_0
    //   856: aload #4
    //   858: iconst_1
    //   859: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   862: istore_2
    //   863: aload #4
    //   865: invokevirtual getType : ()I
    //   868: bipush #55
    //   870: if_icmpne -> 904
    //   873: iload_2
    //   874: iconst_1
    //   875: if_icmpne -> 902
    //   878: aload_0
    //   879: aload #4
    //   881: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   884: ifne -> 902
    //   887: aload_1
    //   888: bipush #8
    //   890: iconst_0
    //   891: invokevirtual putIntProp : (II)V
    //   894: aload_0
    //   895: aload #4
    //   897: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   900: iconst_1
    //   901: ireturn
    //   902: iconst_0
    //   903: ireturn
    //   904: aload #4
    //   906: invokevirtual getType : ()I
    //   909: bipush #36
    //   911: if_icmpeq -> 929
    //   914: aload #4
    //   916: invokevirtual getType : ()I
    //   919: bipush #33
    //   921: if_icmpne -> 927
    //   924: goto -> 929
    //   927: iconst_0
    //   928: ireturn
    //   929: iload_2
    //   930: ireturn
    //   931: aload_0
    //   932: getfield theFunction : Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   935: aload_1
    //   936: invokevirtual getVarIndex : (Lcom/trendmicro/hippo/Node;)I
    //   939: istore_3
    //   940: aload_0
    //   941: getfield inDirectCallFunction : Z
    //   944: ifeq -> 972
    //   947: aload_0
    //   948: getfield theFunction : Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   951: iload_3
    //   952: invokevirtual isParameter : (I)Z
    //   955: ifeq -> 972
    //   958: iload_2
    //   959: iconst_1
    //   960: if_icmpne -> 972
    //   963: aload_1
    //   964: bipush #8
    //   966: iconst_0
    //   967: invokevirtual putIntProp : (II)V
    //   970: iconst_1
    //   971: ireturn
    //   972: aload_0
    //   973: getfield theFunction : Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   976: iload_3
    //   977: invokevirtual isNumberVar : (I)Z
    //   980: ifeq -> 992
    //   983: aload_1
    //   984: bipush #8
    //   986: iconst_0
    //   987: invokevirtual putIntProp : (II)V
    //   990: iconst_1
    //   991: ireturn
    //   992: iconst_0
    //   993: ireturn
    //   994: aload_1
    //   995: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   998: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1001: astore #4
    //   1003: aload_0
    //   1004: aload #4
    //   1006: iconst_1
    //   1007: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   1010: istore_3
    //   1011: aload_0
    //   1012: getfield theFunction : Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   1015: aload_1
    //   1016: invokevirtual getVarIndex : (Lcom/trendmicro/hippo/Node;)I
    //   1019: istore_2
    //   1020: aload_0
    //   1021: getfield inDirectCallFunction : Z
    //   1024: ifeq -> 1071
    //   1027: aload_0
    //   1028: getfield theFunction : Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   1031: iload_2
    //   1032: invokevirtual isParameter : (I)Z
    //   1035: ifeq -> 1071
    //   1038: iload_3
    //   1039: iconst_1
    //   1040: if_icmpne -> 1069
    //   1043: aload_0
    //   1044: aload #4
    //   1046: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   1049: ifne -> 1061
    //   1052: aload_1
    //   1053: bipush #8
    //   1055: iconst_0
    //   1056: invokevirtual putIntProp : (II)V
    //   1059: iconst_1
    //   1060: ireturn
    //   1061: aload_0
    //   1062: aload #4
    //   1064: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   1067: iconst_0
    //   1068: ireturn
    //   1069: iload_3
    //   1070: ireturn
    //   1071: aload_0
    //   1072: getfield theFunction : Lcom/trendmicro/hippo/optimizer/OptFunctionNode;
    //   1075: iload_2
    //   1076: invokevirtual isNumberVar : (I)Z
    //   1079: ifeq -> 1124
    //   1082: iload_3
    //   1083: iconst_1
    //   1084: if_icmpeq -> 1109
    //   1087: aload_1
    //   1088: aload #4
    //   1090: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   1093: aload_1
    //   1094: new com/trendmicro/hippo/Node
    //   1097: dup
    //   1098: sipush #151
    //   1101: aload #4
    //   1103: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   1106: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   1109: aload_1
    //   1110: bipush #8
    //   1112: iconst_0
    //   1113: invokevirtual putIntProp : (II)V
    //   1116: aload_0
    //   1117: aload #4
    //   1119: invokespecial markDCPNumberContext : (Lcom/trendmicro/hippo/Node;)V
    //   1122: iconst_1
    //   1123: ireturn
    //   1124: iload_3
    //   1125: iconst_1
    //   1126: if_icmpne -> 1160
    //   1129: aload_0
    //   1130: aload #4
    //   1132: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   1135: ifne -> 1160
    //   1138: aload_1
    //   1139: aload #4
    //   1141: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   1144: aload_1
    //   1145: new com/trendmicro/hippo/Node
    //   1148: dup
    //   1149: sipush #150
    //   1152: aload #4
    //   1154: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   1157: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   1160: iconst_0
    //   1161: ireturn
    //   1162: aload_1
    //   1163: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1166: astore #5
    //   1168: aload #5
    //   1170: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1173: astore #4
    //   1175: aload #4
    //   1177: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   1180: astore #6
    //   1182: aload_0
    //   1183: aload #5
    //   1185: iconst_1
    //   1186: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   1189: iconst_1
    //   1190: if_icmpne -> 1224
    //   1193: aload_0
    //   1194: aload #5
    //   1196: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   1199: ifne -> 1224
    //   1202: aload_1
    //   1203: aload #5
    //   1205: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   1208: aload_1
    //   1209: new com/trendmicro/hippo/Node
    //   1212: dup
    //   1213: sipush #150
    //   1216: aload #5
    //   1218: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   1221: invokevirtual addChildToFront : (Lcom/trendmicro/hippo/Node;)V
    //   1224: aload_0
    //   1225: aload #4
    //   1227: iconst_1
    //   1228: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   1231: iconst_1
    //   1232: if_icmpne -> 1251
    //   1235: aload_0
    //   1236: aload #4
    //   1238: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   1241: ifne -> 1251
    //   1244: aload_1
    //   1245: bipush #8
    //   1247: iconst_1
    //   1248: invokevirtual putIntProp : (II)V
    //   1251: aload_0
    //   1252: aload #6
    //   1254: iconst_1
    //   1255: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   1258: iconst_1
    //   1259: if_icmpne -> 1293
    //   1262: aload_0
    //   1263: aload #6
    //   1265: invokespecial convertParameter : (Lcom/trendmicro/hippo/Node;)Z
    //   1268: ifne -> 1293
    //   1271: aload_1
    //   1272: aload #6
    //   1274: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   1277: aload_1
    //   1278: new com/trendmicro/hippo/Node
    //   1281: dup
    //   1282: sipush #150
    //   1285: aload #6
    //   1287: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   1290: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   1293: iconst_0
    //   1294: ireturn
    //   1295: aload_0
    //   1296: aload_1
    //   1297: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1300: iconst_1
    //   1301: invokespecial rewriteForNumberVariables : (Lcom/trendmicro/hippo/Node;I)I
    //   1304: iconst_1
    //   1305: if_icmpne -> 1315
    //   1308: aload_1
    //   1309: bipush #8
    //   1311: iconst_0
    //   1312: invokevirtual putIntProp : (II)V
    //   1315: iconst_0
    //   1316: ireturn
    //   1317: aload_1
    //   1318: bipush #8
    //   1320: iconst_0
    //   1321: invokevirtual putIntProp : (II)V
    //   1324: iconst_1
    //   1325: ireturn
  }
  
  void optimize(ScriptNode paramScriptNode) {
    int i = paramScriptNode.getFunctionCount();
    for (int j = 0; j != i; j++)
      optimizeFunction(OptFunctionNode.get(paramScriptNode, j)); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/Optimizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */