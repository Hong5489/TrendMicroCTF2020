package com.trendmicro.hippo.tools.jsc;

import com.trendmicro.hippo.CompilerEnvirons;
import com.trendmicro.hippo.ErrorReporter;
import com.trendmicro.hippo.optimizer.ClassCompiler;
import com.trendmicro.hippo.tools.SourceReader;
import com.trendmicro.hippo.tools.ToolErrorReporter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
  private String characterEncoding;
  
  private ClassCompiler compiler;
  
  private CompilerEnvirons compilerEnv;
  
  private String destinationDir;
  
  private boolean printHelp;
  
  private ToolErrorReporter reporter = new ToolErrorReporter(true);
  
  private String targetName;
  
  private String targetPackage;
  
  public Main() {
    CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
    this.compilerEnv = compilerEnvirons;
    compilerEnvirons.setErrorReporter((ErrorReporter)this.reporter);
    this.compiler = new ClassCompiler(this.compilerEnv);
  }
  
  private void addError(String paramString1, String paramString2) {
    if (paramString2 == null) {
      paramString1 = ToolErrorReporter.getMessage(paramString1);
    } else {
      paramString1 = ToolErrorReporter.getMessage(paramString1, paramString2);
    } 
    addFormatedError(paramString1);
  }
  
  private void addFormatedError(String paramString) {
    this.reporter.error(paramString, null, -1, null, -1);
  }
  
  private static void badUsage(String paramString) {
    System.err.println(ToolErrorReporter.getMessage("msg.jsc.bad.usage", Main.class.getName(), paramString));
  }
  
  private File getOutputFile(File paramFile, String paramString) {
    paramFile = new File(paramFile, paramString.replace('.', File.separatorChar).concat(".class"));
    paramString = paramFile.getParent();
    if (paramString != null) {
      File file = new File(paramString);
      if (!file.exists())
        file.mkdirs(); 
    } 
    return paramFile;
  }
  
  public static void main(String[] paramArrayOfString) {
    Main main = new Main();
    paramArrayOfString = main.processOptions(paramArrayOfString);
    if (paramArrayOfString == null) {
      if (main.printHelp) {
        System.out.println(ToolErrorReporter.getMessage("msg.jsc.usage", Main.class.getName()));
        System.exit(0);
      } 
      System.exit(1);
    } 
    if (!main.reporter.hasReportedError())
      main.processSource(paramArrayOfString); 
  }
  
  private static void p(String paramString) {
    System.out.println(paramString);
  }
  
  private String readSource(File paramFile) {
    String str = paramFile.getAbsolutePath();
    if (!paramFile.isFile()) {
      addError("msg.jsfile.not.found", str);
      return null;
    } 
    try {
      return (String)SourceReader.readFileOrUrl(str, true, this.characterEncoding);
    } catch (FileNotFoundException fileNotFoundException) {
      addError("msg.couldnt.open", str);
    } catch (IOException iOException) {
      addFormatedError(iOException.toString());
    } 
    return null;
  }
  
  String getClassName(String paramString) {
    char[] arrayOfChar = new char[paramString.length() + 1];
    int i = 0;
    if (!Character.isJavaIdentifierStart(paramString.charAt(0))) {
      arrayOfChar[0] = (char)'_';
      i = 0 + 1;
    } 
    byte b = 0;
    while (b < paramString.length()) {
      char c = paramString.charAt(b);
      if (Character.isJavaIdentifierPart(c)) {
        arrayOfChar[i] = c;
      } else {
        arrayOfChar[i] = (char)'_';
      } 
      b++;
      i++;
    } 
    return (new String(arrayOfChar)).trim();
  }
  
  public String[] processOptions(String[] paramArrayOfString) {
    // Byte code:
    //   0: aload_0
    //   1: ldc ''
    //   3: putfield targetPackage : Ljava/lang/String;
    //   6: aload_0
    //   7: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   10: iconst_0
    //   11: invokevirtual setGenerateDebugInfo : (Z)V
    //   14: iconst_0
    //   15: istore_2
    //   16: iload_2
    //   17: aload_1
    //   18: arraylength
    //   19: if_icmpge -> 1003
    //   22: aload_1
    //   23: iload_2
    //   24: aaload
    //   25: astore_3
    //   26: aload_3
    //   27: ldc '-'
    //   29: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   32: ifne -> 99
    //   35: aload_1
    //   36: arraylength
    //   37: iload_2
    //   38: isub
    //   39: istore #4
    //   41: aload_0
    //   42: getfield targetName : Ljava/lang/String;
    //   45: astore_3
    //   46: aload_3
    //   47: ifnull -> 65
    //   50: iload #4
    //   52: iconst_1
    //   53: if_icmple -> 65
    //   56: aload_0
    //   57: ldc 'msg.multiple.js.to.file'
    //   59: aload_3
    //   60: invokespecial addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   63: aconst_null
    //   64: areturn
    //   65: iload #4
    //   67: anewarray java/lang/String
    //   70: astore_3
    //   71: iconst_0
    //   72: istore #5
    //   74: iload #5
    //   76: iload #4
    //   78: if_icmpeq -> 97
    //   81: aload_3
    //   82: iload #5
    //   84: aload_1
    //   85: iload_2
    //   86: iload #5
    //   88: iadd
    //   89: aaload
    //   90: aastore
    //   91: iinc #5, 1
    //   94: goto -> 74
    //   97: aload_3
    //   98: areturn
    //   99: aload_3
    //   100: ldc '-help'
    //   102: invokevirtual equals : (Ljava/lang/Object;)Z
    //   105: ifne -> 996
    //   108: aload_3
    //   109: ldc '-h'
    //   111: invokevirtual equals : (Ljava/lang/Object;)Z
    //   114: ifne -> 996
    //   117: aload_3
    //   118: ldc '--help'
    //   120: invokevirtual equals : (Ljava/lang/Object;)Z
    //   123: ifeq -> 129
    //   126: goto -> 996
    //   129: iload_2
    //   130: istore #4
    //   132: iload_2
    //   133: istore #5
    //   135: aload_3
    //   136: ldc '-version'
    //   138: invokevirtual equals : (Ljava/lang/Object;)Z
    //   141: ifeq -> 188
    //   144: iinc #2, 1
    //   147: iload_2
    //   148: istore #4
    //   150: iload_2
    //   151: istore #5
    //   153: iload_2
    //   154: aload_1
    //   155: arraylength
    //   156: if_icmpge -> 188
    //   159: iload_2
    //   160: istore #5
    //   162: aload_1
    //   163: iload_2
    //   164: aaload
    //   165: invokestatic parseInt : (Ljava/lang/String;)I
    //   168: istore #4
    //   170: iload_2
    //   171: istore #5
    //   173: aload_0
    //   174: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   177: iload #4
    //   179: invokevirtual setLanguageVersion : (I)V
    //   182: iload_2
    //   183: istore #5
    //   185: goto -> 978
    //   188: iload #4
    //   190: istore #5
    //   192: aload_3
    //   193: ldc '-opt'
    //   195: invokevirtual equals : (Ljava/lang/Object;)Z
    //   198: ifne -> 217
    //   201: iload #4
    //   203: istore_2
    //   204: iload #4
    //   206: istore #5
    //   208: aload_3
    //   209: ldc '-O'
    //   211: invokevirtual equals : (Ljava/lang/Object;)Z
    //   214: ifeq -> 265
    //   217: iinc #4, 1
    //   220: iload #4
    //   222: istore_2
    //   223: iload #4
    //   225: istore #5
    //   227: iload #4
    //   229: aload_1
    //   230: arraylength
    //   231: if_icmpge -> 265
    //   234: iload #4
    //   236: istore #5
    //   238: aload_1
    //   239: iload #4
    //   241: aaload
    //   242: invokestatic parseInt : (Ljava/lang/String;)I
    //   245: istore_2
    //   246: iload #4
    //   248: istore #5
    //   250: aload_0
    //   251: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   254: iload_2
    //   255: invokevirtual setOptimizationLevel : (I)V
    //   258: iload #4
    //   260: istore #5
    //   262: goto -> 978
    //   265: aload_3
    //   266: ldc '-nosource'
    //   268: invokevirtual equals : (Ljava/lang/Object;)Z
    //   271: ifeq -> 288
    //   274: aload_0
    //   275: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   278: iconst_0
    //   279: invokevirtual setGeneratingSource : (Z)V
    //   282: iload_2
    //   283: istore #5
    //   285: goto -> 978
    //   288: aload_3
    //   289: ldc '-debug'
    //   291: invokevirtual equals : (Ljava/lang/Object;)Z
    //   294: ifne -> 967
    //   297: aload_3
    //   298: ldc '-g'
    //   300: invokevirtual equals : (Ljava/lang/Object;)Z
    //   303: ifeq -> 309
    //   306: goto -> 967
    //   309: iload_2
    //   310: istore #5
    //   312: aload_3
    //   313: ldc_w '-main-method-class'
    //   316: invokevirtual equals : (Ljava/lang/Object;)Z
    //   319: ifeq -> 350
    //   322: iinc #2, 1
    //   325: iload_2
    //   326: istore #5
    //   328: iload_2
    //   329: aload_1
    //   330: arraylength
    //   331: if_icmpge -> 350
    //   334: aload_0
    //   335: getfield compiler : Lcom/trendmicro/hippo/optimizer/ClassCompiler;
    //   338: aload_1
    //   339: iload_2
    //   340: aaload
    //   341: invokevirtual setMainMethodClass : (Ljava/lang/String;)V
    //   344: iload_2
    //   345: istore #5
    //   347: goto -> 978
    //   350: iload #5
    //   352: istore_2
    //   353: aload_3
    //   354: ldc_w '-encoding'
    //   357: invokevirtual equals : (Ljava/lang/Object;)Z
    //   360: ifeq -> 387
    //   363: iinc #5, 1
    //   366: iload #5
    //   368: istore_2
    //   369: iload #5
    //   371: aload_1
    //   372: arraylength
    //   373: if_icmpge -> 387
    //   376: aload_0
    //   377: aload_1
    //   378: iload #5
    //   380: aaload
    //   381: putfield characterEncoding : Ljava/lang/String;
    //   384: goto -> 978
    //   387: iload_2
    //   388: istore #5
    //   390: aload_3
    //   391: ldc_w '-o'
    //   394: invokevirtual equals : (Ljava/lang/Object;)Z
    //   397: ifeq -> 560
    //   400: iinc #2, 1
    //   403: iload_2
    //   404: istore #5
    //   406: iload_2
    //   407: aload_1
    //   408: arraylength
    //   409: if_icmpge -> 560
    //   412: aload_1
    //   413: iload_2
    //   414: aaload
    //   415: astore #6
    //   417: aload #6
    //   419: invokevirtual length : ()I
    //   422: istore #4
    //   424: iload #4
    //   426: ifeq -> 545
    //   429: aload #6
    //   431: iconst_0
    //   432: invokevirtual charAt : (I)C
    //   435: invokestatic isJavaIdentifierStart : (C)Z
    //   438: ifne -> 444
    //   441: goto -> 545
    //   444: iconst_1
    //   445: istore #5
    //   447: aload #6
    //   449: astore_3
    //   450: iload #5
    //   452: iload #4
    //   454: if_icmpge -> 534
    //   457: aload #6
    //   459: iload #5
    //   461: invokevirtual charAt : (I)C
    //   464: istore #7
    //   466: iload #7
    //   468: invokestatic isJavaIdentifierPart : (C)Z
    //   471: ifne -> 528
    //   474: iload #7
    //   476: bipush #46
    //   478: if_icmpne -> 513
    //   481: iload #5
    //   483: iload #4
    //   485: bipush #6
    //   487: isub
    //   488: if_icmpne -> 513
    //   491: aload #6
    //   493: ldc '.class'
    //   495: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   498: ifeq -> 513
    //   501: aload #6
    //   503: iconst_0
    //   504: iload #5
    //   506: invokevirtual substring : (II)Ljava/lang/String;
    //   509: astore_3
    //   510: goto -> 534
    //   513: aload_0
    //   514: ldc_w 'msg.invalid.classfile.name'
    //   517: aload #6
    //   519: invokespecial addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   522: aload #6
    //   524: astore_3
    //   525: goto -> 534
    //   528: iinc #5, 1
    //   531: goto -> 447
    //   534: aload_0
    //   535: aload_3
    //   536: putfield targetName : Ljava/lang/String;
    //   539: iload_2
    //   540: istore #5
    //   542: goto -> 978
    //   545: aload_0
    //   546: ldc_w 'msg.invalid.classfile.name'
    //   549: aload #6
    //   551: invokespecial addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   554: iload_2
    //   555: istore #5
    //   557: goto -> 978
    //   560: aload_3
    //   561: ldc_w '-observe-instruction-count'
    //   564: invokevirtual equals : (Ljava/lang/Object;)Z
    //   567: ifeq -> 578
    //   570: aload_0
    //   571: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   574: iconst_1
    //   575: invokevirtual setGenerateObserverCount : (Z)V
    //   578: iload #5
    //   580: istore_2
    //   581: aload_3
    //   582: ldc_w '-package'
    //   585: invokevirtual equals : (Ljava/lang/Object;)Z
    //   588: ifeq -> 742
    //   591: iload #5
    //   593: iconst_1
    //   594: iadd
    //   595: istore #4
    //   597: iload #4
    //   599: istore_2
    //   600: iload #4
    //   602: aload_1
    //   603: arraylength
    //   604: if_icmpge -> 742
    //   607: aload_1
    //   608: iload #4
    //   610: aaload
    //   611: astore_3
    //   612: aload_3
    //   613: invokevirtual length : ()I
    //   616: istore #8
    //   618: iconst_0
    //   619: istore #5
    //   621: iload #5
    //   623: iload #8
    //   625: if_icmpeq -> 730
    //   628: aload_3
    //   629: iload #5
    //   631: invokevirtual charAt : (I)C
    //   634: istore #7
    //   636: iload #7
    //   638: invokestatic isJavaIdentifierStart : (C)Z
    //   641: ifeq -> 717
    //   644: iload #7
    //   646: istore #9
    //   648: iload #5
    //   650: iconst_1
    //   651: iadd
    //   652: istore_2
    //   653: iload_2
    //   654: iload #8
    //   656: if_icmpeq -> 685
    //   659: aload_3
    //   660: iload_2
    //   661: invokevirtual charAt : (I)C
    //   664: istore #7
    //   666: iload_2
    //   667: istore #5
    //   669: iload #7
    //   671: istore #9
    //   673: iload #7
    //   675: invokestatic isJavaIdentifierPart : (C)Z
    //   678: ifne -> 648
    //   681: iload #7
    //   683: istore #9
    //   685: iload_2
    //   686: iload #8
    //   688: if_icmpne -> 694
    //   691: goto -> 730
    //   694: iload #9
    //   696: bipush #46
    //   698: if_icmpne -> 717
    //   701: iload_2
    //   702: iload #8
    //   704: iconst_1
    //   705: isub
    //   706: if_icmpeq -> 717
    //   709: iload_2
    //   710: iconst_1
    //   711: iadd
    //   712: istore #5
    //   714: goto -> 621
    //   717: aload_0
    //   718: ldc_w 'msg.package.name'
    //   721: aload_0
    //   722: getfield targetPackage : Ljava/lang/String;
    //   725: invokespecial addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   728: aconst_null
    //   729: areturn
    //   730: aload_0
    //   731: aload_3
    //   732: putfield targetPackage : Ljava/lang/String;
    //   735: iload #4
    //   737: istore #5
    //   739: goto -> 978
    //   742: iload_2
    //   743: istore #5
    //   745: aload_3
    //   746: ldc_w '-extends'
    //   749: invokevirtual equals : (Ljava/lang/Object;)Z
    //   752: ifeq -> 803
    //   755: iinc #2, 1
    //   758: iload_2
    //   759: istore #5
    //   761: iload_2
    //   762: aload_1
    //   763: arraylength
    //   764: if_icmpge -> 803
    //   767: aload_1
    //   768: iload_2
    //   769: aaload
    //   770: astore_3
    //   771: aload_3
    //   772: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   775: astore_3
    //   776: aload_0
    //   777: getfield compiler : Lcom/trendmicro/hippo/optimizer/ClassCompiler;
    //   780: aload_3
    //   781: invokevirtual setTargetExtends : (Ljava/lang/Class;)V
    //   784: iload_2
    //   785: istore #5
    //   787: goto -> 978
    //   790: astore_1
    //   791: new java/lang/Error
    //   794: dup
    //   795: aload_1
    //   796: invokevirtual toString : ()Ljava/lang/String;
    //   799: invokespecial <init> : (Ljava/lang/String;)V
    //   802: athrow
    //   803: iload #5
    //   805: istore_2
    //   806: aload_3
    //   807: ldc_w '-implements'
    //   810: invokevirtual equals : (Ljava/lang/Object;)Z
    //   813: ifeq -> 928
    //   816: iinc #5, 1
    //   819: iload #5
    //   821: istore_2
    //   822: iload #5
    //   824: aload_1
    //   825: arraylength
    //   826: if_icmpge -> 928
    //   829: new java/util/StringTokenizer
    //   832: dup
    //   833: aload_1
    //   834: iload #5
    //   836: aaload
    //   837: ldc_w ','
    //   840: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   843: astore #6
    //   845: new java/util/ArrayList
    //   848: dup
    //   849: invokespecial <init> : ()V
    //   852: astore #10
    //   854: aload #6
    //   856: invokevirtual hasMoreTokens : ()Z
    //   859: ifeq -> 896
    //   862: aload #6
    //   864: invokevirtual nextToken : ()Ljava/lang/String;
    //   867: astore_3
    //   868: aload #10
    //   870: aload_3
    //   871: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   874: invokeinterface add : (Ljava/lang/Object;)Z
    //   879: pop
    //   880: goto -> 854
    //   883: astore_1
    //   884: new java/lang/Error
    //   887: dup
    //   888: aload_1
    //   889: invokevirtual toString : ()Ljava/lang/String;
    //   892: invokespecial <init> : (Ljava/lang/String;)V
    //   895: athrow
    //   896: aload #10
    //   898: aload #10
    //   900: invokeinterface size : ()I
    //   905: anewarray java/lang/Class
    //   908: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   913: checkcast [Ljava/lang/Class;
    //   916: astore_3
    //   917: aload_0
    //   918: getfield compiler : Lcom/trendmicro/hippo/optimizer/ClassCompiler;
    //   921: aload_3
    //   922: invokevirtual setTargetImplements : ([Ljava/lang/Class;)V
    //   925: goto -> 978
    //   928: aload_3
    //   929: ldc_w '-d'
    //   932: invokevirtual equals : (Ljava/lang/Object;)Z
    //   935: ifeq -> 961
    //   938: iload_2
    //   939: iconst_1
    //   940: iadd
    //   941: istore #5
    //   943: iload #5
    //   945: aload_1
    //   946: arraylength
    //   947: if_icmpge -> 961
    //   950: aload_0
    //   951: aload_1
    //   952: iload #5
    //   954: aaload
    //   955: putfield destinationDir : Ljava/lang/String;
    //   958: goto -> 978
    //   961: aload_3
    //   962: invokestatic badUsage : (Ljava/lang/String;)V
    //   965: aconst_null
    //   966: areturn
    //   967: aload_0
    //   968: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   971: iconst_1
    //   972: invokevirtual setGenerateDebugInfo : (Z)V
    //   975: iload_2
    //   976: istore #5
    //   978: iload #5
    //   980: iconst_1
    //   981: iadd
    //   982: istore_2
    //   983: goto -> 16
    //   986: astore_3
    //   987: aload_1
    //   988: iload #5
    //   990: aaload
    //   991: invokestatic badUsage : (Ljava/lang/String;)V
    //   994: aconst_null
    //   995: areturn
    //   996: aload_0
    //   997: iconst_1
    //   998: putfield printHelp : Z
    //   1001: aconst_null
    //   1002: areturn
    //   1003: ldc_w 'msg.no.file'
    //   1006: invokestatic getMessage : (Ljava/lang/String;)Ljava/lang/String;
    //   1009: invokestatic p : (Ljava/lang/String;)V
    //   1012: aconst_null
    //   1013: areturn
    // Exception table:
    //   from	to	target	type
    //   135	144	986	java/lang/NumberFormatException
    //   153	159	986	java/lang/NumberFormatException
    //   162	170	986	java/lang/NumberFormatException
    //   173	182	986	java/lang/NumberFormatException
    //   192	201	986	java/lang/NumberFormatException
    //   208	217	986	java/lang/NumberFormatException
    //   227	234	986	java/lang/NumberFormatException
    //   238	246	986	java/lang/NumberFormatException
    //   250	258	986	java/lang/NumberFormatException
    //   771	776	790	java/lang/ClassNotFoundException
    //   868	880	883	java/lang/ClassNotFoundException
  }
  
  public void processSource(String[] paramArrayOfString) {
    for (byte b = 0; b != paramArrayOfString.length; b++) {
      File file2;
      String str1 = paramArrayOfString[b];
      if (!str1.endsWith(".js")) {
        addError("msg.extension.not.js", str1);
        return;
      } 
      File file1 = new File(str1);
      String str2 = readSource(file1);
      if (str2 == null)
        return; 
      String str3 = this.targetName;
      String str4 = str3;
      if (str3 == null) {
        str4 = file1.getName();
        str4 = getClassName(str4.substring(0, str4.length() - 3));
      } 
      str3 = str4;
      if (this.targetPackage.length() != 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.targetPackage);
        stringBuilder.append(".");
        stringBuilder.append(str4);
        str3 = stringBuilder.toString();
      } 
      Object[] arrayOfObject = this.compiler.compileToClassFiles(str2, str1, 1, str3);
      if (arrayOfObject == null || arrayOfObject.length == 0)
        return; 
      str4 = null;
      if (this.destinationDir != null) {
        file2 = new File(this.destinationDir);
      } else {
        String str = file1.getParent();
        if (str != null)
          file2 = new File(str); 
      } 
      for (byte b1 = 0; b1 != arrayOfObject.length; b1 += 2) {
        str2 = (String)arrayOfObject[b1];
        byte[] arrayOfByte = (byte[])arrayOfObject[b1 + 1];
        File file = getOutputFile(file2, str2);
        try {
          FileOutputStream fileOutputStream = new FileOutputStream();
          this(file);
          try {
            fileOutputStream.write(arrayOfByte);
          } finally {
            fileOutputStream.close();
          } 
        } catch (IOException iOException) {
          addFormatedError(iOException.toString());
        } 
      } 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/jsc/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */