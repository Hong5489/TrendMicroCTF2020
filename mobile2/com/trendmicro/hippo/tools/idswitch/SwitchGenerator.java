package com.trendmicro.hippo.tools.idswitch;

import com.trendmicro.hippo.EvaluatorException;
import com.trendmicro.hippo.tools.ToolErrorReporter;

public class SwitchGenerator {
  private CodePrinter P;
  
  private ToolErrorReporter R;
  
  private boolean c_was_defined;
  
  int char_tail_test_threshold = 2;
  
  private int[] columns;
  
  private String default_value;
  
  private IdValuePair[] pairs;
  
  private String source_file;
  
  int use_if_threshold = 3;
  
  String v_c = "c";
  
  String v_guess = "X";
  
  String v_id = "id";
  
  String v_label = "L";
  
  String v_length_suffix = "_length";
  
  String v_s = "s";
  
  String v_switch_label = "L0";
  
  private static boolean bigger(IdValuePair paramIdValuePair1, IdValuePair paramIdValuePair2, int paramInt) {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    if (paramInt < 0) {
      paramInt = paramIdValuePair1.idLength - paramIdValuePair2.idLength;
      if (paramInt != 0) {
        if (paramInt <= 0)
          bool3 = false; 
        return bool3;
      } 
      if (paramIdValuePair1.id.compareTo(paramIdValuePair2.id) > 0) {
        bool3 = bool1;
      } else {
        bool3 = false;
      } 
      return bool3;
    } 
    if (paramIdValuePair1.id.charAt(paramInt) > paramIdValuePair2.id.charAt(paramInt)) {
      bool3 = bool2;
    } else {
      bool3 = false;
    } 
    return bool3;
  }
  
  private void check_all_is_different(int paramInt1, int paramInt2) {
    if (paramInt1 != paramInt2) {
      IdValuePair idValuePair = this.pairs[paramInt1];
      while (++paramInt1 != paramInt2) {
        IdValuePair idValuePair1 = this.pairs[paramInt1];
        if (!idValuePair.id.equals(idValuePair1.id)) {
          idValuePair = idValuePair1;
          continue;
        } 
        throw on_same_pair_fail(idValuePair, idValuePair1);
      } 
    } 
  }
  
  private int count_different_chars(int paramInt1, int paramInt2, int paramInt3) {
    int i = 0;
    int j;
    for (j = -1; paramInt1 != paramInt2; j = m) {
      char c = (this.pairs[paramInt1]).id.charAt(paramInt3);
      int k = i;
      int m = j;
      if (c != j) {
        k = i + 1;
        m = c;
      } 
      paramInt1++;
      i = k;
    } 
    return i;
  }
  
  private int count_different_lengths(int paramInt1, int paramInt2) {
    int i = 0;
    int j;
    for (j = -1; paramInt1 != paramInt2; j = n) {
      int k = (this.pairs[paramInt1]).idLength;
      int m = i;
      int n = j;
      if (j != k) {
        m = i + 1;
        n = k;
      } 
      paramInt1++;
      i = m;
    } 
    return i;
  }
  
  private int find_max_different_column(int paramInt1, int paramInt2, int paramInt3) {
    int i = 0;
    int j = 0;
    int k = 0;
    while (k != paramInt3) {
      int m = this.columns[k];
      sort_pairs(paramInt1, paramInt2, m);
      int n = count_different_chars(paramInt1, paramInt2, m);
      if (n == paramInt2 - paramInt1)
        return k; 
      m = i;
      if (i < n) {
        m = n;
        j = k;
      } 
      k++;
      i = m;
    } 
    if (j != paramInt3 - 1)
      sort_pairs(paramInt1, paramInt2, this.columns[j]); 
    return j;
  }
  
  private void generate_body(int paramInt1, int paramInt2, int paramInt3) {
    this.P.indent(paramInt3);
    this.P.p(this.v_switch_label);
    this.P.p(": { ");
    this.P.p(this.v_id);
    this.P.p(" = ");
    this.P.p(this.default_value);
    this.P.p("; String ");
    this.P.p(this.v_guess);
    this.P.p(" = null;");
    this.c_was_defined = false;
    int i = this.P.getOffset();
    this.P.p(" int ");
    this.P.p(this.v_c);
    this.P.p(';');
    int j = this.P.getOffset();
    this.P.nl();
    generate_length_switch(paramInt1, paramInt2, paramInt3 + 1);
    if (!this.c_was_defined)
      this.P.erase(i, j); 
    this.P.indent(paramInt3 + 1);
    this.P.p("if (");
    this.P.p(this.v_guess);
    this.P.p("!=null && ");
    this.P.p(this.v_guess);
    this.P.p("!=");
    this.P.p(this.v_s);
    this.P.p(" && !");
    this.P.p(this.v_guess);
    this.P.p(".equals(");
    this.P.p(this.v_s);
    this.P.p(")) ");
    this.P.p(this.v_id);
    this.P.p(" = ");
    this.P.p(this.default_value);
    this.P.p(";");
    this.P.nl();
    this.P.indent(paramInt3 + 1);
    this.P.p("break ");
    this.P.p(this.v_switch_label);
    this.P.p(";");
    this.P.nl();
    this.P.line(paramInt3, "}");
  }
  
  private void generate_length_switch(int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: iload_2
    //   3: iconst_m1
    //   4: invokespecial sort_pairs : (III)V
    //   7: aload_0
    //   8: iload_1
    //   9: iload_2
    //   10: invokespecial check_all_is_different : (II)V
    //   13: aload_0
    //   14: iload_1
    //   15: iload_2
    //   16: invokespecial count_different_lengths : (II)I
    //   19: istore #4
    //   21: aload_0
    //   22: aload_0
    //   23: getfield pairs : [Lcom/trendmicro/hippo/tools/idswitch/IdValuePair;
    //   26: iload_2
    //   27: iconst_1
    //   28: isub
    //   29: aaload
    //   30: getfield idLength : I
    //   33: newarray int
    //   35: putfield columns : [I
    //   38: iload #4
    //   40: aload_0
    //   41: getfield use_if_threshold : I
    //   44: if_icmpgt -> 134
    //   47: iload #4
    //   49: iconst_1
    //   50: if_icmpeq -> 128
    //   53: aload_0
    //   54: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   57: iload_3
    //   58: invokevirtual indent : (I)V
    //   61: aload_0
    //   62: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   65: ldc 'int '
    //   67: invokevirtual p : (Ljava/lang/String;)V
    //   70: aload_0
    //   71: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   74: aload_0
    //   75: getfield v_s : Ljava/lang/String;
    //   78: invokevirtual p : (Ljava/lang/String;)V
    //   81: aload_0
    //   82: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   85: aload_0
    //   86: getfield v_length_suffix : Ljava/lang/String;
    //   89: invokevirtual p : (Ljava/lang/String;)V
    //   92: aload_0
    //   93: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   96: ldc ' = '
    //   98: invokevirtual p : (Ljava/lang/String;)V
    //   101: aload_0
    //   102: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   105: aload_0
    //   106: getfield v_s : Ljava/lang/String;
    //   109: invokevirtual p : (Ljava/lang/String;)V
    //   112: aload_0
    //   113: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   116: ldc '.length();'
    //   118: invokevirtual p : (Ljava/lang/String;)V
    //   121: aload_0
    //   122: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   125: invokevirtual nl : ()V
    //   128: iconst_1
    //   129: istore #5
    //   131: goto -> 192
    //   134: aload_0
    //   135: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   138: iload_3
    //   139: invokevirtual indent : (I)V
    //   142: aload_0
    //   143: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   146: aload_0
    //   147: getfield v_label : Ljava/lang/String;
    //   150: invokevirtual p : (Ljava/lang/String;)V
    //   153: aload_0
    //   154: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   157: ldc ': switch ('
    //   159: invokevirtual p : (Ljava/lang/String;)V
    //   162: aload_0
    //   163: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   166: aload_0
    //   167: getfield v_s : Ljava/lang/String;
    //   170: invokevirtual p : (Ljava/lang/String;)V
    //   173: aload_0
    //   174: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   177: ldc '.length()) {'
    //   179: invokevirtual p : (Ljava/lang/String;)V
    //   182: aload_0
    //   183: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   186: invokevirtual nl : ()V
    //   189: iconst_0
    //   190: istore #5
    //   192: aload_0
    //   193: getfield pairs : [Lcom/trendmicro/hippo/tools/idswitch/IdValuePair;
    //   196: iload_1
    //   197: aaload
    //   198: getfield idLength : I
    //   201: istore #6
    //   203: iconst_0
    //   204: istore #7
    //   206: iload_1
    //   207: istore #8
    //   209: iload_1
    //   210: istore #9
    //   212: iinc #8, 1
    //   215: iload #8
    //   217: iload_2
    //   218: if_icmpeq -> 250
    //   221: aload_0
    //   222: getfield pairs : [Lcom/trendmicro/hippo/tools/idswitch/IdValuePair;
    //   225: iload #8
    //   227: aaload
    //   228: getfield idLength : I
    //   231: istore #10
    //   233: iload #10
    //   235: istore #7
    //   237: iload #10
    //   239: iload #6
    //   241: if_icmpeq -> 247
    //   244: goto -> 250
    //   247: goto -> 212
    //   250: iload #5
    //   252: ifeq -> 373
    //   255: aload_0
    //   256: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   259: iload_3
    //   260: invokevirtual indent : (I)V
    //   263: iload #9
    //   265: iload_1
    //   266: if_icmpeq -> 278
    //   269: aload_0
    //   270: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   273: ldc 'else '
    //   275: invokevirtual p : (Ljava/lang/String;)V
    //   278: aload_0
    //   279: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   282: ldc 'if ('
    //   284: invokevirtual p : (Ljava/lang/String;)V
    //   287: iload #4
    //   289: iconst_1
    //   290: if_icmpne -> 316
    //   293: aload_0
    //   294: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   297: aload_0
    //   298: getfield v_s : Ljava/lang/String;
    //   301: invokevirtual p : (Ljava/lang/String;)V
    //   304: aload_0
    //   305: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   308: ldc '.length()=='
    //   310: invokevirtual p : (Ljava/lang/String;)V
    //   313: goto -> 347
    //   316: aload_0
    //   317: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   320: aload_0
    //   321: getfield v_s : Ljava/lang/String;
    //   324: invokevirtual p : (Ljava/lang/String;)V
    //   327: aload_0
    //   328: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   331: aload_0
    //   332: getfield v_length_suffix : Ljava/lang/String;
    //   335: invokevirtual p : (Ljava/lang/String;)V
    //   338: aload_0
    //   339: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   342: ldc '=='
    //   344: invokevirtual p : (Ljava/lang/String;)V
    //   347: aload_0
    //   348: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   351: iload #6
    //   353: invokevirtual p : (I)V
    //   356: aload_0
    //   357: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   360: ldc ') {'
    //   362: invokevirtual p : (Ljava/lang/String;)V
    //   365: iload_3
    //   366: iconst_1
    //   367: iadd
    //   368: istore #6
    //   370: goto -> 413
    //   373: aload_0
    //   374: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   377: iload_3
    //   378: invokevirtual indent : (I)V
    //   381: aload_0
    //   382: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   385: ldc 'case '
    //   387: invokevirtual p : (Ljava/lang/String;)V
    //   390: aload_0
    //   391: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   394: iload #6
    //   396: invokevirtual p : (I)V
    //   399: aload_0
    //   400: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   403: ldc ':'
    //   405: invokevirtual p : (Ljava/lang/String;)V
    //   408: iload_3
    //   409: iconst_1
    //   410: iadd
    //   411: istore #6
    //   413: aload_0
    //   414: iload #9
    //   416: iload #8
    //   418: iload #6
    //   420: iload #5
    //   422: iconst_1
    //   423: ixor
    //   424: iload #5
    //   426: invokespecial generate_letter_switch : (IIIZZ)V
    //   429: iload #5
    //   431: ifeq -> 453
    //   434: aload_0
    //   435: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   438: ldc '}'
    //   440: invokevirtual p : (Ljava/lang/String;)V
    //   443: aload_0
    //   444: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   447: invokevirtual nl : ()V
    //   450: goto -> 489
    //   453: aload_0
    //   454: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   457: ldc 'break '
    //   459: invokevirtual p : (Ljava/lang/String;)V
    //   462: aload_0
    //   463: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   466: aload_0
    //   467: getfield v_label : Ljava/lang/String;
    //   470: invokevirtual p : (Ljava/lang/String;)V
    //   473: aload_0
    //   474: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   477: ldc ';'
    //   479: invokevirtual p : (Ljava/lang/String;)V
    //   482: aload_0
    //   483: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   486: invokevirtual nl : ()V
    //   489: iload #8
    //   491: iload_2
    //   492: if_icmpne -> 525
    //   495: iload #5
    //   497: ifne -> 524
    //   500: aload_0
    //   501: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   504: iload_3
    //   505: invokevirtual indent : (I)V
    //   508: aload_0
    //   509: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   512: ldc '}'
    //   514: invokevirtual p : (Ljava/lang/String;)V
    //   517: aload_0
    //   518: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   521: invokevirtual nl : ()V
    //   524: return
    //   525: iload #8
    //   527: istore #9
    //   529: iload #7
    //   531: istore #6
    //   533: goto -> 212
  }
  
  private void generate_letter_switch(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2) {
    int i = (this.pairs[paramInt1]).idLength;
    for (int j = 0; j != i; j++)
      this.columns[j] = j; 
    generate_letter_switch_r(paramInt1, paramInt2, i, paramInt3, paramBoolean1, paramBoolean2);
  }
  
  private boolean generate_letter_switch_r(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: iload_1
    //   1: istore #7
    //   3: iconst_0
    //   4: istore #8
    //   6: iload #7
    //   8: iconst_1
    //   9: iadd
    //   10: iload_2
    //   11: if_icmpne -> 437
    //   14: aload_0
    //   15: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   18: bipush #32
    //   20: invokevirtual p : (C)V
    //   23: aload_0
    //   24: getfield pairs : [Lcom/trendmicro/hippo/tools/idswitch/IdValuePair;
    //   27: iload #7
    //   29: aaload
    //   30: astore #9
    //   32: iload_3
    //   33: aload_0
    //   34: getfield char_tail_test_threshold : I
    //   37: if_icmple -> 129
    //   40: aload_0
    //   41: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   44: aload_0
    //   45: getfield v_guess : Ljava/lang/String;
    //   48: invokevirtual p : (Ljava/lang/String;)V
    //   51: aload_0
    //   52: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   55: ldc '='
    //   57: invokevirtual p : (Ljava/lang/String;)V
    //   60: aload_0
    //   61: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   64: aload #9
    //   66: getfield id : Ljava/lang/String;
    //   69: invokevirtual qstring : (Ljava/lang/String;)V
    //   72: aload_0
    //   73: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   76: ldc ';'
    //   78: invokevirtual p : (Ljava/lang/String;)V
    //   81: aload_0
    //   82: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   85: aload_0
    //   86: getfield v_id : Ljava/lang/String;
    //   89: invokevirtual p : (Ljava/lang/String;)V
    //   92: aload_0
    //   93: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   96: ldc '='
    //   98: invokevirtual p : (Ljava/lang/String;)V
    //   101: aload_0
    //   102: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   105: aload #9
    //   107: getfield value : Ljava/lang/String;
    //   110: invokevirtual p : (Ljava/lang/String;)V
    //   113: aload_0
    //   114: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   117: ldc ';'
    //   119: invokevirtual p : (Ljava/lang/String;)V
    //   122: iload #8
    //   124: istore #5
    //   126: goto -> 425
    //   129: iload_3
    //   130: ifne -> 200
    //   133: iconst_1
    //   134: istore #5
    //   136: aload_0
    //   137: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   140: aload_0
    //   141: getfield v_id : Ljava/lang/String;
    //   144: invokevirtual p : (Ljava/lang/String;)V
    //   147: aload_0
    //   148: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   151: ldc '='
    //   153: invokevirtual p : (Ljava/lang/String;)V
    //   156: aload_0
    //   157: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   160: aload #9
    //   162: getfield value : Ljava/lang/String;
    //   165: invokevirtual p : (Ljava/lang/String;)V
    //   168: aload_0
    //   169: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   172: ldc '; break '
    //   174: invokevirtual p : (Ljava/lang/String;)V
    //   177: aload_0
    //   178: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   181: aload_0
    //   182: getfield v_switch_label : Ljava/lang/String;
    //   185: invokevirtual p : (Ljava/lang/String;)V
    //   188: aload_0
    //   189: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   192: ldc ';'
    //   194: invokevirtual p : (Ljava/lang/String;)V
    //   197: goto -> 425
    //   200: aload_0
    //   201: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   204: ldc 'if ('
    //   206: invokevirtual p : (Ljava/lang/String;)V
    //   209: aload_0
    //   210: getfield columns : [I
    //   213: iconst_0
    //   214: iaload
    //   215: istore_1
    //   216: aload_0
    //   217: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   220: aload_0
    //   221: getfield v_s : Ljava/lang/String;
    //   224: invokevirtual p : (Ljava/lang/String;)V
    //   227: aload_0
    //   228: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   231: ldc '.charAt('
    //   233: invokevirtual p : (Ljava/lang/String;)V
    //   236: aload_0
    //   237: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   240: iload_1
    //   241: invokevirtual p : (I)V
    //   244: aload_0
    //   245: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   248: ldc ')=='
    //   250: invokevirtual p : (Ljava/lang/String;)V
    //   253: aload_0
    //   254: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   257: aload #9
    //   259: getfield id : Ljava/lang/String;
    //   262: iload_1
    //   263: invokevirtual charAt : (I)C
    //   266: invokevirtual qchar : (I)V
    //   269: iconst_1
    //   270: istore_1
    //   271: iload_1
    //   272: iload_3
    //   273: if_icmpeq -> 351
    //   276: aload_0
    //   277: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   280: ldc ' && '
    //   282: invokevirtual p : (Ljava/lang/String;)V
    //   285: aload_0
    //   286: getfield columns : [I
    //   289: iload_1
    //   290: iaload
    //   291: istore_2
    //   292: aload_0
    //   293: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   296: aload_0
    //   297: getfield v_s : Ljava/lang/String;
    //   300: invokevirtual p : (Ljava/lang/String;)V
    //   303: aload_0
    //   304: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   307: ldc '.charAt('
    //   309: invokevirtual p : (Ljava/lang/String;)V
    //   312: aload_0
    //   313: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   316: iload_2
    //   317: invokevirtual p : (I)V
    //   320: aload_0
    //   321: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   324: ldc ')=='
    //   326: invokevirtual p : (Ljava/lang/String;)V
    //   329: aload_0
    //   330: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   333: aload #9
    //   335: getfield id : Ljava/lang/String;
    //   338: iload_2
    //   339: invokevirtual charAt : (I)C
    //   342: invokevirtual qchar : (I)V
    //   345: iinc #1, 1
    //   348: goto -> 271
    //   351: aload_0
    //   352: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   355: ldc ') {'
    //   357: invokevirtual p : (Ljava/lang/String;)V
    //   360: aload_0
    //   361: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   364: aload_0
    //   365: getfield v_id : Ljava/lang/String;
    //   368: invokevirtual p : (Ljava/lang/String;)V
    //   371: aload_0
    //   372: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   375: ldc '='
    //   377: invokevirtual p : (Ljava/lang/String;)V
    //   380: aload_0
    //   381: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   384: aload #9
    //   386: getfield value : Ljava/lang/String;
    //   389: invokevirtual p : (Ljava/lang/String;)V
    //   392: aload_0
    //   393: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   396: ldc '; break '
    //   398: invokevirtual p : (Ljava/lang/String;)V
    //   401: aload_0
    //   402: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   405: aload_0
    //   406: getfield v_switch_label : Ljava/lang/String;
    //   409: invokevirtual p : (Ljava/lang/String;)V
    //   412: aload_0
    //   413: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   416: ldc ';}'
    //   418: invokevirtual p : (Ljava/lang/String;)V
    //   421: iload #8
    //   423: istore #5
    //   425: aload_0
    //   426: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   429: bipush #32
    //   431: invokevirtual p : (C)V
    //   434: iload #5
    //   436: ireturn
    //   437: aload_0
    //   438: iload_1
    //   439: iload_2
    //   440: iload_3
    //   441: invokespecial find_max_different_column : (III)I
    //   444: istore #10
    //   446: aload_0
    //   447: getfield columns : [I
    //   450: iload #10
    //   452: iaload
    //   453: istore #11
    //   455: aload_0
    //   456: iload #7
    //   458: iload_2
    //   459: iload #11
    //   461: invokespecial count_different_chars : (III)I
    //   464: istore #12
    //   466: aload_0
    //   467: getfield columns : [I
    //   470: astore #9
    //   472: aload #9
    //   474: iload #10
    //   476: aload #9
    //   478: iload_3
    //   479: iconst_1
    //   480: isub
    //   481: iaload
    //   482: iastore
    //   483: iload #6
    //   485: ifeq -> 507
    //   488: aload_0
    //   489: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   492: invokevirtual nl : ()V
    //   495: aload_0
    //   496: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   499: iload #4
    //   501: invokevirtual indent : (I)V
    //   504: goto -> 516
    //   507: aload_0
    //   508: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   511: bipush #32
    //   513: invokevirtual p : (C)V
    //   516: iload #12
    //   518: aload_0
    //   519: getfield use_if_threshold : I
    //   522: if_icmpgt -> 602
    //   525: aload_0
    //   526: iconst_1
    //   527: putfield c_was_defined : Z
    //   530: aload_0
    //   531: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   534: aload_0
    //   535: getfield v_c : Ljava/lang/String;
    //   538: invokevirtual p : (Ljava/lang/String;)V
    //   541: aload_0
    //   542: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   545: ldc '='
    //   547: invokevirtual p : (Ljava/lang/String;)V
    //   550: aload_0
    //   551: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   554: aload_0
    //   555: getfield v_s : Ljava/lang/String;
    //   558: invokevirtual p : (Ljava/lang/String;)V
    //   561: aload_0
    //   562: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   565: ldc '.charAt('
    //   567: invokevirtual p : (Ljava/lang/String;)V
    //   570: aload_0
    //   571: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   574: iload #11
    //   576: invokevirtual p : (I)V
    //   579: aload_0
    //   580: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   583: ldc ');'
    //   585: invokevirtual p : (Ljava/lang/String;)V
    //   588: iconst_1
    //   589: istore #13
    //   591: iload #5
    //   593: istore #8
    //   595: iload #13
    //   597: istore #5
    //   599: goto -> 691
    //   602: iload #5
    //   604: ifne -> 633
    //   607: iconst_1
    //   608: istore #5
    //   610: aload_0
    //   611: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   614: aload_0
    //   615: getfield v_label : Ljava/lang/String;
    //   618: invokevirtual p : (Ljava/lang/String;)V
    //   621: aload_0
    //   622: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   625: ldc ': '
    //   627: invokevirtual p : (Ljava/lang/String;)V
    //   630: goto -> 633
    //   633: aload_0
    //   634: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   637: ldc 'switch ('
    //   639: invokevirtual p : (Ljava/lang/String;)V
    //   642: aload_0
    //   643: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   646: aload_0
    //   647: getfield v_s : Ljava/lang/String;
    //   650: invokevirtual p : (Ljava/lang/String;)V
    //   653: aload_0
    //   654: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   657: ldc '.charAt('
    //   659: invokevirtual p : (Ljava/lang/String;)V
    //   662: aload_0
    //   663: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   666: iload #11
    //   668: invokevirtual p : (I)V
    //   671: aload_0
    //   672: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   675: ldc ')) {'
    //   677: invokevirtual p : (Ljava/lang/String;)V
    //   680: iconst_0
    //   681: istore #13
    //   683: iload #5
    //   685: istore #8
    //   687: iload #13
    //   689: istore #5
    //   691: aload_0
    //   692: getfield pairs : [Lcom/trendmicro/hippo/tools/idswitch/IdValuePair;
    //   695: iload #7
    //   697: aaload
    //   698: getfield id : Ljava/lang/String;
    //   701: iload #11
    //   703: invokevirtual charAt : (I)C
    //   706: istore #14
    //   708: iconst_0
    //   709: istore #7
    //   711: iload_1
    //   712: istore #15
    //   714: iload_1
    //   715: istore #16
    //   717: iinc #16, 1
    //   720: iload #16
    //   722: iload_2
    //   723: if_icmpeq -> 760
    //   726: aload_0
    //   727: getfield pairs : [Lcom/trendmicro/hippo/tools/idswitch/IdValuePair;
    //   730: iload #16
    //   732: aaload
    //   733: getfield id : Ljava/lang/String;
    //   736: iload #11
    //   738: invokevirtual charAt : (I)C
    //   741: istore #17
    //   743: iload #17
    //   745: istore #7
    //   747: iload #17
    //   749: iload #14
    //   751: if_icmpeq -> 757
    //   754: goto -> 760
    //   757: goto -> 717
    //   760: iload #5
    //   762: ifeq -> 852
    //   765: aload_0
    //   766: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   769: invokevirtual nl : ()V
    //   772: aload_0
    //   773: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   776: iload #4
    //   778: invokevirtual indent : (I)V
    //   781: iload #15
    //   783: iload_1
    //   784: if_icmpeq -> 796
    //   787: aload_0
    //   788: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   791: ldc 'else '
    //   793: invokevirtual p : (Ljava/lang/String;)V
    //   796: aload_0
    //   797: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   800: ldc 'if ('
    //   802: invokevirtual p : (Ljava/lang/String;)V
    //   805: aload_0
    //   806: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   809: aload_0
    //   810: getfield v_c : Ljava/lang/String;
    //   813: invokevirtual p : (Ljava/lang/String;)V
    //   816: aload_0
    //   817: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   820: ldc '=='
    //   822: invokevirtual p : (Ljava/lang/String;)V
    //   825: aload_0
    //   826: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   829: iload #14
    //   831: invokevirtual qchar : (I)V
    //   834: aload_0
    //   835: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   838: ldc ') {'
    //   840: invokevirtual p : (Ljava/lang/String;)V
    //   843: iload #4
    //   845: iconst_1
    //   846: iadd
    //   847: istore #14
    //   849: goto -> 901
    //   852: aload_0
    //   853: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   856: invokevirtual nl : ()V
    //   859: aload_0
    //   860: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   863: iload #4
    //   865: invokevirtual indent : (I)V
    //   868: aload_0
    //   869: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   872: ldc 'case '
    //   874: invokevirtual p : (Ljava/lang/String;)V
    //   877: aload_0
    //   878: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   881: iload #14
    //   883: invokevirtual qchar : (I)V
    //   886: aload_0
    //   887: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   890: ldc ':'
    //   892: invokevirtual p : (Ljava/lang/String;)V
    //   895: iload #4
    //   897: iconst_1
    //   898: iadd
    //   899: istore #14
    //   901: aload_0
    //   902: iload #15
    //   904: iload #16
    //   906: iload_3
    //   907: iconst_1
    //   908: isub
    //   909: iload #14
    //   911: iload #8
    //   913: iload #5
    //   915: invokespecial generate_letter_switch_r : (IIIIZZ)Z
    //   918: istore #13
    //   920: iload #5
    //   922: ifeq -> 937
    //   925: aload_0
    //   926: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   929: ldc '}'
    //   931: invokevirtual p : (Ljava/lang/String;)V
    //   934: goto -> 971
    //   937: iload #13
    //   939: ifne -> 971
    //   942: aload_0
    //   943: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   946: ldc 'break '
    //   948: invokevirtual p : (Ljava/lang/String;)V
    //   951: aload_0
    //   952: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   955: aload_0
    //   956: getfield v_label : Ljava/lang/String;
    //   959: invokevirtual p : (Ljava/lang/String;)V
    //   962: aload_0
    //   963: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   966: ldc ';'
    //   968: invokevirtual p : (Ljava/lang/String;)V
    //   971: iload #16
    //   973: iload_2
    //   974: if_icmpne -> 1091
    //   977: iload #5
    //   979: ifeq -> 1020
    //   982: aload_0
    //   983: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   986: invokevirtual nl : ()V
    //   989: iload #6
    //   991: ifeq -> 1008
    //   994: aload_0
    //   995: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   998: iload #4
    //   1000: iconst_1
    //   1001: isub
    //   1002: invokevirtual indent : (I)V
    //   1005: goto -> 1080
    //   1008: aload_0
    //   1009: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1012: iload #4
    //   1014: invokevirtual indent : (I)V
    //   1017: goto -> 1080
    //   1020: aload_0
    //   1021: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1024: invokevirtual nl : ()V
    //   1027: aload_0
    //   1028: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1031: iload #4
    //   1033: invokevirtual indent : (I)V
    //   1036: aload_0
    //   1037: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1040: ldc '}'
    //   1042: invokevirtual p : (Ljava/lang/String;)V
    //   1045: iload #6
    //   1047: ifeq -> 1071
    //   1050: aload_0
    //   1051: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1054: invokevirtual nl : ()V
    //   1057: aload_0
    //   1058: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1061: iload #4
    //   1063: iconst_1
    //   1064: isub
    //   1065: invokevirtual indent : (I)V
    //   1068: goto -> 1080
    //   1071: aload_0
    //   1072: getfield P : Lcom/trendmicro/hippo/tools/idswitch/CodePrinter;
    //   1075: bipush #32
    //   1077: invokevirtual p : (C)V
    //   1080: aload_0
    //   1081: getfield columns : [I
    //   1084: iload #10
    //   1086: iload #11
    //   1088: iastore
    //   1089: iconst_0
    //   1090: ireturn
    //   1091: iload #16
    //   1093: istore #15
    //   1095: iload #7
    //   1097: istore #14
    //   1099: goto -> 717
  }
  
  private static void heap4Sort(IdValuePair[] paramArrayOfIdValuePair, int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt2 <= 1)
      return; 
    makeHeap4(paramArrayOfIdValuePair, paramInt1, paramInt2, paramInt3);
    while (paramInt2 > 1) {
      IdValuePair idValuePair = paramArrayOfIdValuePair[paramInt1 + --paramInt2];
      paramArrayOfIdValuePair[paramInt1 + paramInt2] = paramArrayOfIdValuePair[paramInt1 + 0];
      paramArrayOfIdValuePair[paramInt1 + 0] = idValuePair;
      heapify4(paramArrayOfIdValuePair, paramInt1, paramInt2, 0, paramInt3);
    } 
  }
  
  private static void heapify4(IdValuePair[] paramArrayOfIdValuePair, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    IdValuePair idValuePair = paramArrayOfIdValuePair[paramInt1 + paramInt3];
    int i;
    for (i = paramInt3;; i = paramInt3) {
      int j = i << 2;
      paramInt3 = j | 0x1;
      int k = j | 0x2;
      int m = j | 0x3;
      int n = j + 4;
      if (n >= paramInt2) {
        if (paramInt3 < paramInt2) {
          IdValuePair idValuePair6 = paramArrayOfIdValuePair[paramInt1 + paramInt3];
          IdValuePair idValuePair7 = idValuePair6;
          j = paramInt3;
          if (k != paramInt2) {
            idValuePair7 = paramArrayOfIdValuePair[paramInt1 + k];
            IdValuePair idValuePair8 = idValuePair6;
            if (bigger(idValuePair7, idValuePair6, paramInt4)) {
              idValuePair8 = idValuePair7;
              paramInt3 = k;
            } 
            idValuePair7 = idValuePair8;
            j = paramInt3;
            if (m != paramInt2) {
              idValuePair6 = paramArrayOfIdValuePair[paramInt1 + m];
              idValuePair7 = idValuePair8;
              j = paramInt3;
              if (bigger(idValuePair6, idValuePair8, paramInt4)) {
                idValuePair7 = idValuePair6;
                j = m;
              } 
            } 
          } 
          if (bigger(idValuePair7, idValuePair, paramInt4)) {
            paramArrayOfIdValuePair[paramInt1 + i] = idValuePair7;
            paramArrayOfIdValuePair[paramInt1 + j] = idValuePair;
          } 
        } 
        return;
      } 
      IdValuePair idValuePair4 = paramArrayOfIdValuePair[paramInt1 + paramInt3];
      IdValuePair idValuePair2 = paramArrayOfIdValuePair[paramInt1 + k];
      IdValuePair idValuePair5 = paramArrayOfIdValuePair[paramInt1 + m];
      IdValuePair idValuePair1 = paramArrayOfIdValuePair[paramInt1 + n];
      IdValuePair idValuePair3 = idValuePair4;
      if (bigger(idValuePair2, idValuePair4, paramInt4)) {
        idValuePair3 = idValuePair2;
        paramInt3 = k;
      } 
      j = m;
      idValuePair2 = idValuePair5;
      if (bigger(idValuePair1, idValuePair5, paramInt4)) {
        idValuePair2 = idValuePair1;
        j = n;
      } 
      idValuePair1 = idValuePair3;
      if (bigger(idValuePair2, idValuePair3, paramInt4)) {
        paramInt3 = j;
        idValuePair1 = idValuePair2;
      } 
      if (bigger(idValuePair, idValuePair1, paramInt4))
        return; 
      paramArrayOfIdValuePair[paramInt1 + i] = idValuePair1;
      paramArrayOfIdValuePair[paramInt1 + paramInt3] = idValuePair;
    } 
  }
  
  private static void makeHeap4(IdValuePair[] paramArrayOfIdValuePair, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt2 + 2 >> 2;
    while (i != 0)
      heapify4(paramArrayOfIdValuePair, paramInt1, paramInt2, --i, paramInt3); 
  }
  
  private EvaluatorException on_same_pair_fail(IdValuePair paramIdValuePair1, IdValuePair paramIdValuePair2) {
    int i = paramIdValuePair1.getLineNumber();
    int j = paramIdValuePair2.getLineNumber();
    int k = i;
    int m = j;
    if (j > i) {
      m = i;
      k = j;
    } 
    String str = ToolErrorReporter.getMessage("msg.idswitch.same_string", paramIdValuePair1.id, new Integer(m));
    return this.R.runtimeError(str, this.source_file, k, null, 0);
  }
  
  private void sort_pairs(int paramInt1, int paramInt2, int paramInt3) {
    heap4Sort(this.pairs, paramInt1, paramInt2 - paramInt1, paramInt3);
  }
  
  public void generateSwitch(IdValuePair[] paramArrayOfIdValuePair, String paramString) {
    int i = paramArrayOfIdValuePair.length;
    if (i == 0)
      return; 
    this.pairs = paramArrayOfIdValuePair;
    this.default_value = paramString;
    generate_body(0, i, 2);
  }
  
  public void generateSwitch(String[] paramArrayOfString, String paramString) {
    int i = paramArrayOfString.length / 2;
    IdValuePair[] arrayOfIdValuePair = new IdValuePair[i];
    for (int j = 0; j != i; j++)
      arrayOfIdValuePair[j] = new IdValuePair(paramArrayOfString[j * 2], paramArrayOfString[j * 2 + 1]); 
    generateSwitch(arrayOfIdValuePair, paramString);
  }
  
  public CodePrinter getCodePrinter() {
    return this.P;
  }
  
  public ToolErrorReporter getReporter() {
    return this.R;
  }
  
  public String getSourceFileName() {
    return this.source_file;
  }
  
  public void setCodePrinter(CodePrinter paramCodePrinter) {
    this.P = paramCodePrinter;
  }
  
  public void setReporter(ToolErrorReporter paramToolErrorReporter) {
    this.R = paramToolErrorReporter;
  }
  
  public void setSourceFileName(String paramString) {
    this.source_file = paramString;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/idswitch/SwitchGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */