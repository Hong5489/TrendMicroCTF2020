package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.Scope;
import com.trendmicro.hippo.ast.ScriptNode;
import java.util.ArrayList;
import java.util.List;

public class NodeTransformer {
  private boolean hasFinally;
  
  private ObjArray loopEnds;
  
  private ObjArray loops;
  
  private static Node addBeforeCurrent(Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4) {
    if (paramNode2 == null) {
      if (paramNode3 != paramNode1.getFirstChild())
        Kit.codeBug(); 
      paramNode1.addChildToFront(paramNode4);
    } else {
      if (paramNode3 != paramNode2.getNext())
        Kit.codeBug(); 
      paramNode1.addChildAfter(paramNode4, paramNode2);
    } 
    return paramNode4;
  }
  
  private static Node replaceCurrent(Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4) {
    if (paramNode2 == null) {
      if (paramNode3 != paramNode1.getFirstChild())
        Kit.codeBug(); 
      paramNode1.replaceChild(paramNode3, paramNode4);
    } else if (paramNode2.next == paramNode3) {
      paramNode1.replaceChildAfter(paramNode2, paramNode4);
    } else {
      paramNode1.replaceChild(paramNode3, paramNode4);
    } 
    return paramNode4;
  }
  
  private void transformCompilationUnit(ScriptNode paramScriptNode, boolean paramBoolean) {
    boolean bool2;
    this.loops = new ObjArray();
    this.loopEnds = new ObjArray();
    boolean bool1 = false;
    this.hasFinally = false;
    if (paramScriptNode.getType() != 110 || ((FunctionNode)paramScriptNode).requiresActivation()) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (!bool2)
      bool1 = true; 
    paramScriptNode.flattenSymbolTable(bool1);
    transformCompilationUnit_r(paramScriptNode, (Node)paramScriptNode, (Scope)paramScriptNode, bool2, paramBoolean);
  }
  
  private void transformCompilationUnit_r(ScriptNode paramScriptNode, Node paramNode, Scope paramScope, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #6
    //   3: aload #6
    //   5: ifnonnull -> 20
    //   8: aload_2
    //   9: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   12: astore #6
    //   14: aconst_null
    //   15: astore #7
    //   17: goto -> 35
    //   20: aload #6
    //   22: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   25: astore #8
    //   27: aload #6
    //   29: astore #7
    //   31: aload #8
    //   33: astore #6
    //   35: aload #6
    //   37: ifnonnull -> 41
    //   40: return
    //   41: aload #6
    //   43: invokevirtual getType : ()I
    //   46: istore #9
    //   48: iload #4
    //   50: ifeq -> 240
    //   53: iload #9
    //   55: sipush #130
    //   58: if_icmpeq -> 77
    //   61: iload #9
    //   63: sipush #133
    //   66: if_icmpeq -> 77
    //   69: iload #9
    //   71: sipush #158
    //   74: if_icmpne -> 240
    //   77: aload #6
    //   79: instanceof com/trendmicro/hippo/ast/Scope
    //   82: ifeq -> 240
    //   85: aload #6
    //   87: checkcast com/trendmicro/hippo/ast/Scope
    //   90: astore #8
    //   92: aload #8
    //   94: invokevirtual getSymbolTable : ()Ljava/util/Map;
    //   97: ifnull -> 240
    //   100: iload #9
    //   102: sipush #158
    //   105: if_icmpne -> 116
    //   108: sipush #159
    //   111: istore #9
    //   113: goto -> 121
    //   116: sipush #154
    //   119: istore #9
    //   121: new com/trendmicro/hippo/Node
    //   124: dup
    //   125: iload #9
    //   127: invokespecial <init> : (I)V
    //   130: astore #10
    //   132: new com/trendmicro/hippo/Node
    //   135: dup
    //   136: sipush #154
    //   139: invokespecial <init> : (I)V
    //   142: astore #11
    //   144: aload #10
    //   146: aload #11
    //   148: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   151: aload #8
    //   153: invokevirtual getSymbolTable : ()Ljava/util/Map;
    //   156: invokeinterface keySet : ()Ljava/util/Set;
    //   161: invokeinterface iterator : ()Ljava/util/Iterator;
    //   166: astore #12
    //   168: aload #12
    //   170: invokeinterface hasNext : ()Z
    //   175: ifeq -> 201
    //   178: aload #11
    //   180: bipush #39
    //   182: aload #12
    //   184: invokeinterface next : ()Ljava/lang/Object;
    //   189: checkcast java/lang/String
    //   192: invokestatic newString : (ILjava/lang/String;)Lcom/trendmicro/hippo/Node;
    //   195: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   198: goto -> 168
    //   201: aload #8
    //   203: aconst_null
    //   204: invokevirtual setSymbolTable : (Ljava/util/Map;)V
    //   207: aload_2
    //   208: aload #7
    //   210: aload #6
    //   212: aload #10
    //   214: invokestatic replaceCurrent : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   217: astore #8
    //   219: aload #8
    //   221: invokevirtual getType : ()I
    //   224: istore #9
    //   226: aload #10
    //   228: aload #6
    //   230: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   233: aload #8
    //   235: astore #6
    //   237: goto -> 240
    //   240: iload #9
    //   242: iconst_3
    //   243: if_icmpeq -> 1712
    //   246: iload #9
    //   248: iconst_4
    //   249: if_icmpeq -> 1417
    //   252: iload #9
    //   254: bipush #7
    //   256: if_icmpeq -> 1255
    //   259: iload #9
    //   261: bipush #8
    //   263: if_icmpeq -> 1036
    //   266: iload #9
    //   268: bipush #38
    //   270: if_icmpeq -> 1026
    //   273: iload #9
    //   275: bipush #39
    //   277: if_icmpeq -> 1049
    //   280: iload #9
    //   282: bipush #73
    //   284: if_icmpeq -> 1014
    //   287: iload #9
    //   289: bipush #82
    //   291: if_icmpeq -> 973
    //   294: iload #9
    //   296: bipush #115
    //   298: if_icmpeq -> 946
    //   301: iload #9
    //   303: sipush #138
    //   306: if_icmpeq -> 920
    //   309: iload #9
    //   311: sipush #159
    //   314: if_icmpeq -> 688
    //   317: iload #9
    //   319: tableswitch default -> 344, 30 -> 678, 31 -> 1049, 32 -> 1255
    //   344: iload #9
    //   346: tableswitch default -> 376, 121 -> 476, 122 -> 476, 123 -> 749, 124 -> 435
    //   376: iload #9
    //   378: tableswitch default -> 404, 131 -> 946, 132 -> 1712, 133 -> 946
    //   404: iload #9
    //   406: tableswitch default -> 432, 154 -> 688, 155 -> 749, 156 -> 1049
    //   432: goto -> 1750
    //   435: aload_0
    //   436: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   439: aload #6
    //   441: invokevirtual push : (Ljava/lang/Object;)V
    //   444: aload #6
    //   446: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   449: astore #7
    //   451: aload #7
    //   453: invokevirtual getType : ()I
    //   456: iconst_3
    //   457: if_icmpeq -> 464
    //   460: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   463: pop
    //   464: aload_0
    //   465: getfield loopEnds : Lcom/trendmicro/hippo/ObjArray;
    //   468: aload #7
    //   470: invokevirtual push : (Ljava/lang/Object;)V
    //   473: goto -> 1750
    //   476: aload #6
    //   478: checkcast com/trendmicro/hippo/ast/Jump
    //   481: astore #11
    //   483: aload #11
    //   485: invokevirtual getJumpStatement : ()Lcom/trendmicro/hippo/ast/Jump;
    //   488: astore #10
    //   490: aload #10
    //   492: ifnonnull -> 499
    //   495: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   498: pop
    //   499: aload_0
    //   500: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   503: invokevirtual size : ()I
    //   506: istore #13
    //   508: iload #13
    //   510: ifeq -> 674
    //   513: iinc #13, -1
    //   516: aload_0
    //   517: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   520: iload #13
    //   522: invokevirtual get : (I)Ljava/lang/Object;
    //   525: checkcast com/trendmicro/hippo/Node
    //   528: astore #12
    //   530: aload #12
    //   532: aload #10
    //   534: if_acmpne -> 577
    //   537: iload #9
    //   539: bipush #121
    //   541: if_icmpne -> 557
    //   544: aload #11
    //   546: aload #10
    //   548: getfield target : Lcom/trendmicro/hippo/Node;
    //   551: putfield target : Lcom/trendmicro/hippo/Node;
    //   554: goto -> 567
    //   557: aload #11
    //   559: aload #10
    //   561: invokevirtual getContinue : ()Lcom/trendmicro/hippo/Node;
    //   564: putfield target : Lcom/trendmicro/hippo/Node;
    //   567: aload #11
    //   569: iconst_5
    //   570: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   573: pop
    //   574: goto -> 1750
    //   577: aload #12
    //   579: invokevirtual getType : ()I
    //   582: istore #14
    //   584: iload #14
    //   586: bipush #124
    //   588: if_icmpne -> 612
    //   591: aload_2
    //   592: aload #7
    //   594: aload #6
    //   596: new com/trendmicro/hippo/Node
    //   599: dup
    //   600: iconst_3
    //   601: invokespecial <init> : (I)V
    //   604: invokestatic addBeforeCurrent : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   607: astore #8
    //   609: goto -> 667
    //   612: aload #7
    //   614: astore #8
    //   616: iload #14
    //   618: bipush #82
    //   620: if_icmpne -> 667
    //   623: aload #12
    //   625: checkcast com/trendmicro/hippo/ast/Jump
    //   628: astore #12
    //   630: new com/trendmicro/hippo/ast/Jump
    //   633: dup
    //   634: sipush #136
    //   637: invokespecial <init> : (I)V
    //   640: astore #8
    //   642: aload #8
    //   644: aload #12
    //   646: invokevirtual getFinally : ()Lcom/trendmicro/hippo/Node;
    //   649: putfield target : Lcom/trendmicro/hippo/Node;
    //   652: aload_2
    //   653: aload #7
    //   655: aload #6
    //   657: aload #8
    //   659: invokestatic addBeforeCurrent : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   662: astore #7
    //   664: goto -> 671
    //   667: aload #8
    //   669: astore #7
    //   671: goto -> 508
    //   674: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   677: athrow
    //   678: aload_0
    //   679: aload #6
    //   681: aload_1
    //   682: invokevirtual visitNew : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/ast/ScriptNode;)V
    //   685: goto -> 1750
    //   688: aload #6
    //   690: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   693: invokevirtual getType : ()I
    //   696: sipush #154
    //   699: if_icmpne -> 749
    //   702: aload_1
    //   703: invokevirtual getType : ()I
    //   706: bipush #110
    //   708: if_icmpne -> 730
    //   711: aload_1
    //   712: checkcast com/trendmicro/hippo/ast/FunctionNode
    //   715: invokevirtual requiresActivation : ()Z
    //   718: ifeq -> 724
    //   721: goto -> 730
    //   724: iconst_0
    //   725: istore #15
    //   727: goto -> 733
    //   730: iconst_1
    //   731: istore #15
    //   733: aload_0
    //   734: iload #15
    //   736: aload_2
    //   737: aload #7
    //   739: aload #6
    //   741: invokevirtual visitLet : (ZLcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   744: astore #6
    //   746: goto -> 1750
    //   749: new com/trendmicro/hippo/Node
    //   752: dup
    //   753: sipush #130
    //   756: invokespecial <init> : (I)V
    //   759: astore #11
    //   761: aload #6
    //   763: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   766: astore #8
    //   768: aload #8
    //   770: ifnull -> 905
    //   773: aload #8
    //   775: astore #10
    //   777: aload #8
    //   779: invokevirtual getNext : ()Lcom/trendmicro/hippo/Node;
    //   782: astore #8
    //   784: aload #10
    //   786: invokevirtual getType : ()I
    //   789: bipush #39
    //   791: if_icmpne -> 865
    //   794: aload #10
    //   796: invokevirtual hasChildren : ()Z
    //   799: ifne -> 805
    //   802: goto -> 768
    //   805: aload #10
    //   807: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   810: astore #12
    //   812: aload #10
    //   814: aload #12
    //   816: invokevirtual removeChild : (Lcom/trendmicro/hippo/Node;)V
    //   819: aload #10
    //   821: bipush #49
    //   823: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   826: pop
    //   827: iload #9
    //   829: sipush #155
    //   832: if_icmpne -> 843
    //   835: sipush #156
    //   838: istore #13
    //   840: goto -> 847
    //   843: bipush #8
    //   845: istore #13
    //   847: new com/trendmicro/hippo/Node
    //   850: dup
    //   851: iload #13
    //   853: aload #10
    //   855: aload #12
    //   857: invokespecial <init> : (ILcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)V
    //   860: astore #10
    //   862: goto -> 876
    //   865: aload #10
    //   867: invokevirtual getType : ()I
    //   870: sipush #159
    //   873: if_icmpne -> 901
    //   876: aload #11
    //   878: new com/trendmicro/hippo/Node
    //   881: dup
    //   882: sipush #134
    //   885: aload #10
    //   887: aload #6
    //   889: invokevirtual getLineno : ()I
    //   892: invokespecial <init> : (ILcom/trendmicro/hippo/Node;I)V
    //   895: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   898: goto -> 768
    //   901: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   904: athrow
    //   905: aload_2
    //   906: aload #7
    //   908: aload #6
    //   910: aload #11
    //   912: invokestatic replaceCurrent : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   915: astore #6
    //   917: goto -> 1750
    //   920: aload_3
    //   921: aload #6
    //   923: invokevirtual getString : ()Ljava/lang/String;
    //   926: invokevirtual getDefiningScope : (Ljava/lang/String;)Lcom/trendmicro/hippo/ast/Scope;
    //   929: astore #7
    //   931: aload #7
    //   933: ifnull -> 943
    //   936: aload #6
    //   938: aload #7
    //   940: invokevirtual setScope : (Lcom/trendmicro/hippo/ast/Scope;)V
    //   943: goto -> 1750
    //   946: aload_0
    //   947: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   950: aload #6
    //   952: invokevirtual push : (Ljava/lang/Object;)V
    //   955: aload_0
    //   956: getfield loopEnds : Lcom/trendmicro/hippo/ObjArray;
    //   959: aload #6
    //   961: checkcast com/trendmicro/hippo/ast/Jump
    //   964: getfield target : Lcom/trendmicro/hippo/Node;
    //   967: invokevirtual push : (Ljava/lang/Object;)V
    //   970: goto -> 1750
    //   973: aload #6
    //   975: checkcast com/trendmicro/hippo/ast/Jump
    //   978: invokevirtual getFinally : ()Lcom/trendmicro/hippo/Node;
    //   981: astore #7
    //   983: aload #7
    //   985: ifnull -> 1750
    //   988: aload_0
    //   989: iconst_1
    //   990: putfield hasFinally : Z
    //   993: aload_0
    //   994: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   997: aload #6
    //   999: invokevirtual push : (Ljava/lang/Object;)V
    //   1002: aload_0
    //   1003: getfield loopEnds : Lcom/trendmicro/hippo/ObjArray;
    //   1006: aload #7
    //   1008: invokevirtual push : (Ljava/lang/Object;)V
    //   1011: goto -> 1750
    //   1014: aload_1
    //   1015: checkcast com/trendmicro/hippo/ast/FunctionNode
    //   1018: aload #6
    //   1020: invokevirtual addResumptionPoint : (Lcom/trendmicro/hippo/Node;)V
    //   1023: goto -> 1750
    //   1026: aload_0
    //   1027: aload #6
    //   1029: aload_1
    //   1030: invokevirtual visitCall : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/ast/ScriptNode;)V
    //   1033: goto -> 1750
    //   1036: iload #5
    //   1038: ifeq -> 1049
    //   1041: aload #6
    //   1043: bipush #74
    //   1045: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1048: pop
    //   1049: iload #4
    //   1051: ifeq -> 1057
    //   1054: goto -> 1750
    //   1057: iload #9
    //   1059: bipush #39
    //   1061: if_icmpne -> 1071
    //   1064: aload #6
    //   1066: astore #8
    //   1068: goto -> 1106
    //   1071: aload #6
    //   1073: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1076: astore #10
    //   1078: aload #10
    //   1080: astore #8
    //   1082: aload #10
    //   1084: invokevirtual getType : ()I
    //   1087: bipush #49
    //   1089: if_icmpeq -> 1106
    //   1092: iload #9
    //   1094: bipush #31
    //   1096: if_icmpne -> 1102
    //   1099: goto -> 1750
    //   1102: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   1105: athrow
    //   1106: aload #8
    //   1108: invokevirtual getScope : ()Lcom/trendmicro/hippo/ast/Scope;
    //   1111: ifnull -> 1117
    //   1114: goto -> 1750
    //   1117: aload_3
    //   1118: aload #8
    //   1120: invokevirtual getString : ()Ljava/lang/String;
    //   1123: invokevirtual getDefiningScope : (Ljava/lang/String;)Lcom/trendmicro/hippo/ast/Scope;
    //   1126: astore #10
    //   1128: aload #10
    //   1130: ifnull -> 1750
    //   1133: aload #8
    //   1135: aload #10
    //   1137: invokevirtual setScope : (Lcom/trendmicro/hippo/ast/Scope;)V
    //   1140: iload #9
    //   1142: bipush #39
    //   1144: if_icmpne -> 1158
    //   1147: aload #6
    //   1149: bipush #55
    //   1151: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1154: pop
    //   1155: goto -> 1750
    //   1158: iload #9
    //   1160: bipush #8
    //   1162: if_icmpeq -> 1236
    //   1165: iload #9
    //   1167: bipush #74
    //   1169: if_icmpne -> 1175
    //   1172: goto -> 1236
    //   1175: iload #9
    //   1177: sipush #156
    //   1180: if_icmpne -> 1203
    //   1183: aload #6
    //   1185: sipush #157
    //   1188: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1191: pop
    //   1192: aload #8
    //   1194: bipush #41
    //   1196: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1199: pop
    //   1200: goto -> 1750
    //   1203: iload #9
    //   1205: bipush #31
    //   1207: if_icmpne -> 1232
    //   1210: aload_2
    //   1211: aload #7
    //   1213: aload #6
    //   1215: new com/trendmicro/hippo/Node
    //   1218: dup
    //   1219: bipush #44
    //   1221: invokespecial <init> : (I)V
    //   1224: invokestatic replaceCurrent : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   1227: astore #6
    //   1229: goto -> 1750
    //   1232: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   1235: athrow
    //   1236: aload #6
    //   1238: bipush #56
    //   1240: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1243: pop
    //   1244: aload #8
    //   1246: bipush #41
    //   1248: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1251: pop
    //   1252: goto -> 1750
    //   1255: aload #6
    //   1257: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1260: astore #8
    //   1262: aload #8
    //   1264: astore #7
    //   1266: iload #9
    //   1268: bipush #7
    //   1270: if_icmpne -> 1396
    //   1273: aload #8
    //   1275: invokevirtual getType : ()I
    //   1278: bipush #26
    //   1280: if_icmpne -> 1293
    //   1283: aload #8
    //   1285: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1288: astore #8
    //   1290: goto -> 1273
    //   1293: aload #8
    //   1295: invokevirtual getType : ()I
    //   1298: bipush #12
    //   1300: if_icmpeq -> 1317
    //   1303: aload #8
    //   1305: astore #7
    //   1307: aload #8
    //   1309: invokevirtual getType : ()I
    //   1312: bipush #13
    //   1314: if_icmpne -> 1396
    //   1317: aload #8
    //   1319: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1322: astore #10
    //   1324: aload #8
    //   1326: invokevirtual getLastChild : ()Lcom/trendmicro/hippo/Node;
    //   1329: astore #11
    //   1331: aload #10
    //   1333: invokevirtual getType : ()I
    //   1336: bipush #39
    //   1338: if_icmpne -> 1361
    //   1341: aload #10
    //   1343: invokevirtual getString : ()Ljava/lang/String;
    //   1346: ldc 'undefined'
    //   1348: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1351: ifeq -> 1361
    //   1354: aload #11
    //   1356: astore #7
    //   1358: goto -> 1396
    //   1361: aload #8
    //   1363: astore #7
    //   1365: aload #11
    //   1367: invokevirtual getType : ()I
    //   1370: bipush #39
    //   1372: if_icmpne -> 1396
    //   1375: aload #8
    //   1377: astore #7
    //   1379: aload #11
    //   1381: invokevirtual getString : ()Ljava/lang/String;
    //   1384: ldc 'undefined'
    //   1386: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1389: ifeq -> 1396
    //   1392: aload #10
    //   1394: astore #7
    //   1396: aload #7
    //   1398: invokevirtual getType : ()I
    //   1401: bipush #33
    //   1403: if_icmpne -> 1750
    //   1406: aload #7
    //   1408: bipush #34
    //   1410: invokevirtual setType : (I)Lcom/trendmicro/hippo/Node;
    //   1413: pop
    //   1414: goto -> 1750
    //   1417: aload_1
    //   1418: invokevirtual getType : ()I
    //   1421: bipush #110
    //   1423: if_icmpne -> 1442
    //   1426: aload_1
    //   1427: checkcast com/trendmicro/hippo/ast/FunctionNode
    //   1430: invokevirtual isGenerator : ()Z
    //   1433: ifeq -> 1442
    //   1436: iconst_1
    //   1437: istore #9
    //   1439: goto -> 1445
    //   1442: iconst_0
    //   1443: istore #9
    //   1445: iload #9
    //   1447: ifeq -> 1458
    //   1450: aload #6
    //   1452: bipush #20
    //   1454: iconst_1
    //   1455: invokevirtual putIntProp : (II)V
    //   1458: aload_0
    //   1459: getfield hasFinally : Z
    //   1462: ifne -> 1468
    //   1465: goto -> 1750
    //   1468: aload_0
    //   1469: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   1472: invokevirtual size : ()I
    //   1475: iconst_1
    //   1476: isub
    //   1477: istore #13
    //   1479: aconst_null
    //   1480: astore #8
    //   1482: iload #13
    //   1484: iflt -> 1611
    //   1487: aload_0
    //   1488: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   1491: iload #13
    //   1493: invokevirtual get : (I)Ljava/lang/Object;
    //   1496: checkcast com/trendmicro/hippo/Node
    //   1499: astore #11
    //   1501: aload #11
    //   1503: invokevirtual getType : ()I
    //   1506: istore #14
    //   1508: iload #14
    //   1510: bipush #82
    //   1512: if_icmpeq -> 1528
    //   1515: iload #14
    //   1517: bipush #124
    //   1519: if_icmpne -> 1525
    //   1522: goto -> 1528
    //   1525: goto -> 1605
    //   1528: iload #14
    //   1530: bipush #82
    //   1532: if_icmpne -> 1563
    //   1535: new com/trendmicro/hippo/ast/Jump
    //   1538: dup
    //   1539: sipush #136
    //   1542: invokespecial <init> : (I)V
    //   1545: astore #10
    //   1547: aload #10
    //   1549: aload #11
    //   1551: checkcast com/trendmicro/hippo/ast/Jump
    //   1554: invokevirtual getFinally : ()Lcom/trendmicro/hippo/Node;
    //   1557: putfield target : Lcom/trendmicro/hippo/Node;
    //   1560: goto -> 1573
    //   1563: new com/trendmicro/hippo/Node
    //   1566: dup
    //   1567: iconst_3
    //   1568: invokespecial <init> : (I)V
    //   1571: astore #10
    //   1573: aload #8
    //   1575: ifnonnull -> 1598
    //   1578: new com/trendmicro/hippo/Node
    //   1581: dup
    //   1582: sipush #130
    //   1585: aload #6
    //   1587: invokevirtual getLineno : ()I
    //   1590: invokespecial <init> : (II)V
    //   1593: astore #8
    //   1595: goto -> 1598
    //   1598: aload #8
    //   1600: aload #10
    //   1602: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   1605: iinc #13, -1
    //   1608: goto -> 1482
    //   1611: aload #8
    //   1613: ifnull -> 1750
    //   1616: aload #6
    //   1618: invokevirtual getFirstChild : ()Lcom/trendmicro/hippo/Node;
    //   1621: astore #10
    //   1623: aload_2
    //   1624: aload #7
    //   1626: aload #6
    //   1628: aload #8
    //   1630: invokestatic replaceCurrent : (Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/Node;)Lcom/trendmicro/hippo/Node;
    //   1633: astore #7
    //   1635: aload #10
    //   1637: ifnull -> 1698
    //   1640: iload #9
    //   1642: ifeq -> 1648
    //   1645: goto -> 1698
    //   1648: new com/trendmicro/hippo/Node
    //   1651: dup
    //   1652: sipush #135
    //   1655: aload #10
    //   1657: invokespecial <init> : (ILcom/trendmicro/hippo/Node;)V
    //   1660: astore #6
    //   1662: aload #8
    //   1664: aload #6
    //   1666: invokevirtual addChildToFront : (Lcom/trendmicro/hippo/Node;)V
    //   1669: aload #8
    //   1671: new com/trendmicro/hippo/Node
    //   1674: dup
    //   1675: bipush #65
    //   1677: invokespecial <init> : (I)V
    //   1680: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   1683: aload_0
    //   1684: aload_1
    //   1685: aload #6
    //   1687: aload_3
    //   1688: iload #4
    //   1690: iload #5
    //   1692: invokespecial transformCompilationUnit_r : (Lcom/trendmicro/hippo/ast/ScriptNode;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/ast/Scope;ZZ)V
    //   1695: goto -> 1705
    //   1698: aload #8
    //   1700: aload #6
    //   1702: invokevirtual addChildToBack : (Lcom/trendmicro/hippo/Node;)V
    //   1705: aload #7
    //   1707: astore #6
    //   1709: goto -> 3
    //   1712: aload_0
    //   1713: getfield loopEnds : Lcom/trendmicro/hippo/ObjArray;
    //   1716: invokevirtual isEmpty : ()Z
    //   1719: ifne -> 1750
    //   1722: aload_0
    //   1723: getfield loopEnds : Lcom/trendmicro/hippo/ObjArray;
    //   1726: invokevirtual peek : ()Ljava/lang/Object;
    //   1729: aload #6
    //   1731: if_acmpne -> 1750
    //   1734: aload_0
    //   1735: getfield loopEnds : Lcom/trendmicro/hippo/ObjArray;
    //   1738: invokevirtual pop : ()Ljava/lang/Object;
    //   1741: pop
    //   1742: aload_0
    //   1743: getfield loops : Lcom/trendmicro/hippo/ObjArray;
    //   1746: invokevirtual pop : ()Ljava/lang/Object;
    //   1749: pop
    //   1750: aload #6
    //   1752: instanceof com/trendmicro/hippo/ast/Scope
    //   1755: ifeq -> 1768
    //   1758: aload #6
    //   1760: checkcast com/trendmicro/hippo/ast/Scope
    //   1763: astore #7
    //   1765: goto -> 1771
    //   1768: aload_3
    //   1769: astore #7
    //   1771: aload_0
    //   1772: aload_1
    //   1773: aload #6
    //   1775: aload #7
    //   1777: iload #4
    //   1779: iload #5
    //   1781: invokespecial transformCompilationUnit_r : (Lcom/trendmicro/hippo/ast/ScriptNode;Lcom/trendmicro/hippo/Node;Lcom/trendmicro/hippo/ast/Scope;ZZ)V
    //   1784: goto -> 3
  }
  
  public final void transform(ScriptNode paramScriptNode, CompilerEnvirons paramCompilerEnvirons) {
    transform(paramScriptNode, false, paramCompilerEnvirons);
  }
  
  public final void transform(ScriptNode paramScriptNode, boolean paramBoolean, CompilerEnvirons paramCompilerEnvirons) {
    boolean bool = paramBoolean;
    paramBoolean = bool;
    if (paramCompilerEnvirons.getLanguageVersion() >= 200) {
      paramBoolean = bool;
      if (paramScriptNode.isInStrictMode())
        paramBoolean = true; 
    } 
    transformCompilationUnit(paramScriptNode, paramBoolean);
    for (byte b = 0; b != paramScriptNode.getFunctionCount(); b++)
      transform((ScriptNode)paramScriptNode.getFunctionNode(b), paramBoolean, paramCompilerEnvirons); 
  }
  
  protected void visitCall(Node paramNode, ScriptNode paramScriptNode) {}
  
  protected Node visitLet(boolean paramBoolean, Node paramNode1, Node paramNode2, Node paramNode3) {
    Node node1;
    boolean bool;
    Node node2 = paramNode3.getFirstChild();
    Node node3 = node2.getNext();
    paramNode3.removeChild(node2);
    paramNode3.removeChild(node3);
    if (paramNode3.getType() == 159) {
      bool = true;
    } else {
      bool = false;
    } 
    if (paramBoolean) {
      char c;
      if (bool) {
        c = ' ';
      } else {
        c = '';
      } 
      Node node4 = replaceCurrent(paramNode1, paramNode2, paramNode3, new Node(c));
      ArrayList<Object> arrayList = new ArrayList();
      Node node5 = new Node(67);
      paramNode1 = node2.getFirstChild();
      while (paramNode1 != null) {
        paramNode3 = paramNode1;
        paramNode2 = node3;
        Node node = paramNode3;
        if (paramNode3.getType() == 159) {
          List list = (List)paramNode3.getProp(22);
          paramNode3 = paramNode3.getFirstChild();
          if (paramNode3.getType() == 154) {
            if (bool) {
              paramNode2 = new Node(90, paramNode3.getNext(), node3);
            } else {
              paramNode2 = new Node(130, new Node(134, paramNode3.getNext()), node3);
            } 
            if (list != null) {
              arrayList.addAll(list);
              for (c = Character.MIN_VALUE; c < list.size(); c++)
                node5.addChildToBack(new Node(127, Node.newNumber(0.0D))); 
            } 
            node = paramNode3.getFirstChild();
          } else {
            throw Kit.codeBug();
          } 
        } 
        if (node.getType() == 39) {
          arrayList.add(ScriptRuntime.getIndexObject(node.getString()));
          node3 = node.getFirstChild();
          paramNode3 = node3;
          if (node3 == null)
            paramNode3 = new Node(127, Node.newNumber(0.0D)); 
          node5.addChildToBack(paramNode3);
          paramNode1 = paramNode1.getNext();
          node3 = paramNode2;
          continue;
        } 
        throw Kit.codeBug();
      } 
      node5.putProp(12, arrayList.toArray());
      node4.addChildToBack(new Node(2, node5));
      node4.addChildToBack(new Node(124, node3));
      node4.addChildToBack(new Node(3));
      paramNode1 = node4;
    } else {
      char c;
      if (bool) {
        c = 'Z';
      } else {
        c = '';
      } 
      Node node5 = replaceCurrent(paramNode1, paramNode2, paramNode3, new Node(c));
      Node node4 = new Node(90);
      paramNode1 = node2.getFirstChild();
      paramNode2 = node3;
      while (paramNode1 != null) {
        node3 = paramNode1;
        if (node3.getType() == 159) {
          Node node = node3.getFirstChild();
          if (node.getType() == 154) {
            if (bool) {
              paramNode2 = new Node(90, node.getNext(), paramNode2);
            } else {
              paramNode2 = new Node(130, new Node(134, node.getNext()), paramNode2);
            } 
            Scope.joinScopes((Scope)node3, (Scope)paramNode3);
            node3 = node.getFirstChild();
          } else {
            throw Kit.codeBug();
          } 
        } 
        if (node3.getType() == 39) {
          Node node = Node.newString(node3.getString());
          node.setScope((Scope)paramNode3);
          node3 = node3.getFirstChild();
          if (node3 == null)
            node3 = new Node(127, Node.newNumber(0.0D)); 
          node4.addChildToBack(new Node(56, node, node3));
          paramNode1 = paramNode1.getNext();
          continue;
        } 
        throw Kit.codeBug();
      } 
      if (bool) {
        node5.addChildToBack(node4);
        paramNode3.setType(90);
        node5.addChildToBack(paramNode3);
        paramNode3.addChildToBack(paramNode2);
        paramNode1 = node5;
        if (paramNode2 instanceof Scope) {
          Scope scope = ((Scope)paramNode2).getParentScope();
          ((Scope)paramNode2).setParentScope((Scope)paramNode3);
          ((Scope)paramNode3).setParentScope(scope);
          node1 = node5;
        } 
      } else {
        node5.addChildToBack(new Node(134, node4));
        paramNode3.setType(130);
        node5.addChildToBack(paramNode3);
        paramNode3.addChildrenToBack(paramNode2);
        paramNode1 = node5;
        if (paramNode2 instanceof Scope) {
          Scope scope = ((Scope)paramNode2).getParentScope();
          ((Scope)paramNode2).setParentScope((Scope)paramNode3);
          ((Scope)paramNode3).setParentScope(scope);
          node1 = node5;
        } 
      } 
    } 
    return node1;
  }
  
  protected void visitNew(Node paramNode, ScriptNode paramScriptNode) {}
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NodeTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */