package com.trendmicro.hippo.tools.idswitch;

import com.trendmicro.hippo.EvaluatorException;
import com.trendmicro.hippo.tools.ToolErrorReporter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
  private static final int GENERATED_TAG = 2;
  
  private static final String GENERATED_TAG_STR = "generated";
  
  private static final int NORMAL_LINE = 0;
  
  private static final int STRING_TAG = 3;
  
  private static final String STRING_TAG_STR = "string";
  
  private static final int SWITCH_TAG = 1;
  
  private static final String SWITCH_TAG_STR = "string_id_map";
  
  private CodePrinter P;
  
  private ToolErrorReporter R;
  
  private final List<IdValuePair> all_pairs = new ArrayList<>();
  
  private FileBody body;
  
  private String source_file;
  
  private int tag_definition_end;
  
  private int tag_value_end;
  
  private int tag_value_start;
  
  private void add_id(char[] paramArrayOfchar, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    IdValuePair idValuePair = new IdValuePair(new String(paramArrayOfchar, paramInt3, paramInt4 - paramInt3), new String(paramArrayOfchar, paramInt1, paramInt2 - paramInt1));
    idValuePair.setLineNumber(this.body.getLineNumber());
    this.all_pairs.add(idValuePair);
  }
  
  private static boolean equals(String paramString, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    if (paramString.length() == paramInt2 - paramInt1) {
      int i = paramInt1;
      for (paramInt1 = 0; i != paramInt2; paramInt1++) {
        if (paramArrayOfchar[i] != paramString.charAt(paramInt1))
          return false; 
        i++;
      } 
      return true;
    } 
    return false;
  }
  
  private int exec(String[] paramArrayOfString) {
    this.R = new ToolErrorReporter(true, System.err);
    int i = process_options(paramArrayOfString);
    if (i == 0) {
      option_error(ToolErrorReporter.getMessage("msg.idswitch.no_file_argument"));
      return -1;
    } 
    if (i > 1) {
      option_error(ToolErrorReporter.getMessage("msg.idswitch.too_many_arguments"));
      return -1;
    } 
    CodePrinter codePrinter = new CodePrinter();
    this.P = codePrinter;
    codePrinter.setIndentStep(4);
    this.P.setIndentTabSize(0);
    try {
      process_file(paramArrayOfString[0]);
      return 0;
    } catch (IOException iOException) {
      print_error(ToolErrorReporter.getMessage("msg.idswitch.io_error", iOException.toString()));
      return -1;
    } catch (EvaluatorException evaluatorException) {
      return -1;
    } 
  }
  
  private int extract_line_tag_id(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: aload_1
    //   4: iload_2
    //   5: iload_3
    //   6: invokestatic skip_white_space : ([CII)I
    //   9: istore #5
    //   11: aload_0
    //   12: aload_1
    //   13: iload #5
    //   15: iload_3
    //   16: invokespecial look_for_slash_slash : ([CII)I
    //   19: istore_2
    //   20: iload #4
    //   22: istore #6
    //   24: iload_2
    //   25: iload_3
    //   26: if_icmpeq -> 387
    //   29: iload #5
    //   31: iconst_2
    //   32: iadd
    //   33: iload_2
    //   34: if_icmpne -> 43
    //   37: iconst_1
    //   38: istore #7
    //   40: goto -> 46
    //   43: iconst_0
    //   44: istore #7
    //   46: aload_1
    //   47: iload_2
    //   48: iload_3
    //   49: invokestatic skip_white_space : ([CII)I
    //   52: istore_2
    //   53: iload #4
    //   55: istore #6
    //   57: iload_2
    //   58: iload_3
    //   59: if_icmpeq -> 387
    //   62: iload #4
    //   64: istore #6
    //   66: aload_1
    //   67: iload_2
    //   68: caload
    //   69: bipush #35
    //   71: if_icmpne -> 387
    //   74: iload_2
    //   75: iconst_1
    //   76: iadd
    //   77: istore #6
    //   79: iconst_0
    //   80: istore #8
    //   82: iload #6
    //   84: istore_2
    //   85: iload #8
    //   87: istore #5
    //   89: iload #6
    //   91: iload_3
    //   92: if_icmpeq -> 119
    //   95: iload #6
    //   97: istore_2
    //   98: iload #8
    //   100: istore #5
    //   102: aload_1
    //   103: iload #6
    //   105: caload
    //   106: bipush #47
    //   108: if_icmpne -> 119
    //   111: iload #6
    //   113: iconst_1
    //   114: iadd
    //   115: istore_2
    //   116: iconst_1
    //   117: istore #5
    //   119: iload_2
    //   120: istore #6
    //   122: iload #6
    //   124: istore #8
    //   126: iload #8
    //   128: iload_3
    //   129: if_icmpeq -> 172
    //   132: aload_1
    //   133: iload #8
    //   135: caload
    //   136: istore #6
    //   138: iload #6
    //   140: bipush #35
    //   142: if_icmpeq -> 172
    //   145: iload #6
    //   147: bipush #61
    //   149: if_icmpeq -> 172
    //   152: iload #6
    //   154: invokestatic is_white_space : (I)Z
    //   157: ifeq -> 163
    //   160: goto -> 172
    //   163: iload #8
    //   165: iconst_1
    //   166: iadd
    //   167: istore #6
    //   169: goto -> 122
    //   172: iload #4
    //   174: istore #6
    //   176: iload #8
    //   178: iload_3
    //   179: if_icmpeq -> 387
    //   182: aload_1
    //   183: iload #8
    //   185: iload_3
    //   186: invokestatic skip_white_space : ([CII)I
    //   189: istore #9
    //   191: iload #4
    //   193: istore #6
    //   195: iload #9
    //   197: iload_3
    //   198: if_icmpeq -> 387
    //   201: aload_1
    //   202: iload #9
    //   204: caload
    //   205: istore #10
    //   207: iload #10
    //   209: bipush #61
    //   211: if_icmpeq -> 225
    //   214: iload #4
    //   216: istore #6
    //   218: iload #10
    //   220: bipush #35
    //   222: if_icmpne -> 387
    //   225: aload_0
    //   226: aload_1
    //   227: iload_2
    //   228: iload #8
    //   230: iload #7
    //   232: invokespecial get_tag_id : ([CIIZ)I
    //   235: istore #4
    //   237: iload #4
    //   239: istore #6
    //   241: iload #4
    //   243: ifeq -> 387
    //   246: aconst_null
    //   247: astore #11
    //   249: aconst_null
    //   250: astore #12
    //   252: iload #10
    //   254: bipush #35
    //   256: if_icmpne -> 302
    //   259: iload #4
    //   261: istore_2
    //   262: aload #12
    //   264: astore_1
    //   265: iload #5
    //   267: ifeq -> 291
    //   270: iload #4
    //   272: ineg
    //   273: istore_3
    //   274: iload_3
    //   275: istore_2
    //   276: aload #12
    //   278: astore_1
    //   279: iload_3
    //   280: invokestatic is_value_type : (I)Z
    //   283: ifeq -> 291
    //   286: ldc 'msg.idswitch.no_end_usage'
    //   288: astore_1
    //   289: iload_3
    //   290: istore_2
    //   291: aload_0
    //   292: iload #9
    //   294: iconst_1
    //   295: iadd
    //   296: putfield tag_definition_end : I
    //   299: goto -> 346
    //   302: iload #5
    //   304: ifeq -> 314
    //   307: ldc 'msg.idswitch.no_end_with_value'
    //   309: astore #12
    //   311: goto -> 330
    //   314: aload #11
    //   316: astore #12
    //   318: iload #4
    //   320: invokestatic is_value_type : (I)Z
    //   323: ifne -> 330
    //   326: ldc 'msg.idswitch.no_value_allowed'
    //   328: astore #12
    //   330: aload_0
    //   331: aload_1
    //   332: iload #9
    //   334: iconst_1
    //   335: iadd
    //   336: iload_3
    //   337: iload #4
    //   339: invokespecial extract_tag_value : ([CIII)I
    //   342: istore_2
    //   343: aload #12
    //   345: astore_1
    //   346: aload_1
    //   347: ifnonnull -> 356
    //   350: iload_2
    //   351: istore #6
    //   353: goto -> 387
    //   356: aload_1
    //   357: iload_2
    //   358: invokestatic tag_name : (I)Ljava/lang/String;
    //   361: invokestatic getMessage : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   364: astore_1
    //   365: aload_0
    //   366: getfield R : Lcom/trendmicro/hippo/tools/ToolErrorReporter;
    //   369: aload_1
    //   370: aload_0
    //   371: getfield source_file : Ljava/lang/String;
    //   374: aload_0
    //   375: getfield body : Lcom/trendmicro/hippo/tools/idswitch/FileBody;
    //   378: invokevirtual getLineNumber : ()I
    //   381: aconst_null
    //   382: iconst_0
    //   383: invokevirtual runtimeError : (Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;I)Lcom/trendmicro/hippo/EvaluatorException;
    //   386: athrow
    //   387: iload #6
    //   389: ireturn
  }
  
  private int extract_tag_value(char[] paramArrayOfchar, int paramInt1, int paramInt2, int paramInt3) {
    boolean bool = false;
    int i = skip_white_space(paramArrayOfchar, paramInt1, paramInt2);
    paramInt1 = bool;
    if (i != paramInt2) {
      int k;
      int m;
      int j = i;
      paramInt1 = i;
      while (true) {
        k = j;
        m = paramInt1;
        if (paramInt1 != paramInt2) {
          k = paramArrayOfchar[paramInt1];
          if (is_white_space(k)) {
            m = skip_white_space(paramArrayOfchar, paramInt1 + 1, paramInt2);
            if (m != paramInt2 && paramArrayOfchar[m] == '#') {
              k = paramInt1;
              break;
            } 
            paramInt1 = m + 1;
            continue;
          } 
          if (k == 35) {
            k = paramInt1;
            m = paramInt1;
            break;
          } 
          paramInt1++;
          continue;
        } 
        break;
      } 
      paramInt1 = bool;
      if (m != paramInt2) {
        paramInt1 = 1;
        this.tag_value_start = i;
        this.tag_value_end = k;
        this.tag_definition_end = m + 1;
      } 
    } 
    if (paramInt1 != 0) {
      paramInt1 = paramInt3;
    } else {
      paramInt1 = 0;
    } 
    return paramInt1;
  }
  
  private void generate_java_code() {
    this.P.clear();
    IdValuePair[] arrayOfIdValuePair = new IdValuePair[this.all_pairs.size()];
    this.all_pairs.toArray(arrayOfIdValuePair);
    SwitchGenerator switchGenerator = new SwitchGenerator();
    switchGenerator.char_tail_test_threshold = 2;
    switchGenerator.setReporter(this.R);
    switchGenerator.setCodePrinter(this.P);
    switchGenerator.generateSwitch(arrayOfIdValuePair, "0");
  }
  
  private int get_tag_id(char[] paramArrayOfchar, int paramInt1, int paramInt2, boolean paramBoolean) {
    if (paramBoolean) {
      if (equals("string_id_map", paramArrayOfchar, paramInt1, paramInt2))
        return 1; 
      if (equals("generated", paramArrayOfchar, paramInt1, paramInt2))
        return 2; 
    } 
    return equals("string", paramArrayOfchar, paramInt1, paramInt2) ? 3 : 0;
  }
  
  private String get_time_stamp() {
    return (new SimpleDateFormat(" 'Last update:' yyyy-MM-dd HH:mm:ss z")).format(new Date());
  }
  
  private static boolean is_value_type(int paramInt) {
    return (paramInt == 3);
  }
  
  private static boolean is_white_space(int paramInt) {
    return (paramInt == 32 || paramInt == 9);
  }
  
  private void look_for_id_definitions(char[] paramArrayOfchar, int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = skip_white_space(paramArrayOfchar, paramInt1, paramInt2);
    paramInt1 = skip_matched_prefix("Id_", paramArrayOfchar, i, paramInt2);
    if (paramInt1 >= 0) {
      int j = skip_name_char(paramArrayOfchar, paramInt1, paramInt2);
      if (paramInt1 != j) {
        int k = skip_white_space(paramArrayOfchar, j, paramInt2);
        if (k != paramInt2 && paramArrayOfchar[k] == '=') {
          if (paramBoolean) {
            paramInt2 = this.tag_value_start;
            paramInt1 = this.tag_value_end;
          } else {
            paramInt2 = paramInt1;
            paramInt1 = j;
          } 
          add_id(paramArrayOfchar, i, j, paramInt2, paramInt1);
        } 
      } 
    } 
  }
  
  private int look_for_slash_slash(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    while (paramInt1 + 2 <= paramInt2) {
      int i = paramInt1 + 1;
      if (paramArrayOfchar[paramInt1] == '/') {
        paramInt1 = i + 1;
        if (paramArrayOfchar[i] == '/')
          return paramInt1; 
        continue;
      } 
      paramInt1 = i;
    } 
    return paramInt2;
  }
  
  public static void main(String[] paramArrayOfString) {
    System.exit((new Main()).exec(paramArrayOfString));
  }
  
  private void option_error(String paramString) {
    print_error(ToolErrorReporter.getMessage("msg.idswitch.bad_invocation", paramString));
  }
  
  private void print_error(String paramString) {
    System.err.println(paramString);
  }
  
  private void process_file() {
    boolean bool = false;
    char[] arrayOfChar = this.body.getBuffer();
    byte b1 = -1;
    byte b2 = -1;
    byte b3 = -1;
    int i = -1;
    this.body.startLineLoop();
    while (this.body.nextLine()) {
      boolean bool2;
      byte b4;
      byte b5;
      byte b6;
      boolean bool3;
      int j = this.body.getLineBegin();
      int k = this.body.getLineEnd();
      int m = extract_line_tag_id(arrayOfChar, j, k);
      boolean bool1 = false;
      if (bool) {
        if (bool != true) {
          if (bool != 2) {
            bool2 = bool;
            b4 = b1;
            b5 = b2;
            b6 = b3;
            k = i;
            bool3 = bool1;
          } else if (m == 0) {
            bool2 = bool;
            b4 = b1;
            b5 = b2;
            b6 = b3;
            k = i;
            bool3 = bool1;
            if (b1 < 0) {
              b4 = j;
              bool2 = bool;
              b5 = b2;
              b6 = b3;
              k = i;
              bool3 = bool1;
            } 
          } else if (m == -2) {
            b4 = b1;
            if (b1 < 0)
              b4 = j; 
            bool2 = true;
            b5 = j;
            b6 = b3;
            k = i;
            bool3 = bool1;
          } else {
            bool3 = true;
            bool2 = bool;
            b4 = b1;
            b5 = b2;
            b6 = b3;
            k = i;
          } 
        } else if (m == 0) {
          look_for_id_definitions(arrayOfChar, j, k, false);
          bool2 = bool;
          b4 = b1;
          b5 = b2;
          b6 = b3;
          k = i;
          bool3 = bool1;
        } else if (m == 3) {
          look_for_id_definitions(arrayOfChar, j, k, true);
          bool2 = bool;
          b4 = b1;
          b5 = b2;
          b6 = b3;
          k = i;
          bool3 = bool1;
        } else if (m == 2) {
          if (b1 >= 0) {
            bool3 = true;
            bool2 = bool;
            b4 = b1;
            b5 = b2;
            b6 = b3;
            k = i;
          } else {
            bool2 = true;
            b6 = this.tag_definition_end;
            b4 = b1;
            b5 = b2;
            bool3 = bool1;
          } 
        } else if (m == -1) {
          j = 0;
          bool2 = j;
          b4 = b1;
          b5 = b2;
          b6 = b3;
          k = i;
          bool3 = bool1;
          if (b1 >= 0) {
            bool2 = j;
            b4 = b1;
            b5 = b2;
            b6 = b3;
            k = i;
            bool3 = bool1;
            if (!this.all_pairs.isEmpty()) {
              generate_java_code();
              String str2 = this.P.toString();
              if (this.body.setReplacement(b1, b2, str2)) {
                str2 = get_time_stamp();
                this.body.setReplacement(b3, i, str2);
              } 
              bool2 = j;
              b4 = b1;
              b5 = b2;
              b6 = b3;
              k = i;
              bool3 = bool1;
            } 
          } 
        } else {
          bool3 = true;
          bool2 = bool;
          b4 = b1;
          b5 = b2;
          b6 = b3;
          k = i;
        } 
      } else if (m == 1) {
        bool2 = true;
        this.all_pairs.clear();
        b4 = -1;
        b5 = b2;
        b6 = b3;
        k = i;
        bool3 = bool1;
      } else {
        bool2 = bool;
        b4 = b1;
        b5 = b2;
        b6 = b3;
        k = i;
        bool3 = bool1;
        if (m == -1) {
          bool3 = true;
          k = i;
          b6 = b3;
          b5 = b2;
          b4 = b1;
          bool2 = bool;
        } 
      } 
      if (!bool3) {
        bool = bool2;
        b1 = b4;
        b2 = b5;
        b3 = b6;
        i = k;
        continue;
      } 
      String str1 = ToolErrorReporter.getMessage("msg.idswitch.bad_tag_order", tag_name(m));
      throw this.R.runtimeError(str1, this.source_file, this.body.getLineNumber(), null, 0);
    } 
    if (!bool)
      return; 
    String str = ToolErrorReporter.getMessage("msg.idswitch.file_end_in_switch", tag_name(bool));
    throw this.R.runtimeError(str, this.source_file, this.body.getLineNumber(), null, 0);
  }
  
  private int process_options(String[] paramArrayOfString) {
    byte b4;
    byte b5;
    byte b1 = 1;
    byte b2 = 0;
    byte b3 = 0;
    int i = paramArrayOfString.length;
    int j = 0;
    while (true) {
      b4 = b1;
      b5 = b2;
      if (j != i) {
        String str = paramArrayOfString[j];
        int k = str.length();
        b4 = b2;
        b5 = b3;
        if (k >= 2) {
          b4 = b2;
          b5 = b3;
          if (str.charAt(0) == '-') {
            if (str.charAt(1) == '-') {
              if (k == 2) {
                paramArrayOfString[j] = null;
                b4 = b1;
                b5 = b2;
                break;
              } 
              if (str.equals("--help")) {
                b4 = 1;
                b5 = b3;
                continue;
              } 
              if (str.equals("--version")) {
                b5 = 1;
                b4 = b2;
                continue;
              } 
              option_error(ToolErrorReporter.getMessage("msg.idswitch.bad_option", str));
              b4 = -1;
              b5 = b2;
              break;
            } 
            int m = 1;
            label42: while (true) {
              b4 = b2;
              b5 = b3;
              if (m != k) {
                char c = str.charAt(m);
                if (c != 'h') {
                  option_error(ToolErrorReporter.getMessage("msg.idswitch.bad_option_char", String.valueOf(c)));
                  b4 = -1;
                  b5 = b2;
                  break;
                } 
                b2 = 1;
                m++;
                continue;
              } 
              paramArrayOfString[j] = null;
              break label42;
            } 
            break;
          } 
        } 
        j++;
        b2 = b4;
        b3 = b5;
        continue;
      } 
      break;
    } 
    b2 = b4;
    if (b4 == 1) {
      b2 = b4;
      if (b5 != 0) {
        show_usage();
        b2 = 0;
      } 
      if (b3 != 0) {
        show_version();
        b2 = 0;
      } 
    } 
    if (b2 != 1)
      System.exit(b2); 
    return remove_nulls(paramArrayOfString);
  }
  
  private int remove_nulls(String[] paramArrayOfString) {
    int i = paramArrayOfString.length;
    int j;
    for (j = 0; j != i && paramArrayOfString[j] != null; j++);
    int k = j;
    int m = k;
    if (j != i) {
      j++;
      while (true) {
        m = k;
        if (j != i) {
          String str = paramArrayOfString[j];
          m = k;
          if (str != null) {
            paramArrayOfString[k] = str;
            m = k + 1;
          } 
          j++;
          k = m;
          continue;
        } 
        break;
      } 
    } 
    return m;
  }
  
  private void show_usage() {
    System.out.println(ToolErrorReporter.getMessage("msg.idswitch.usage"));
    System.out.println();
  }
  
  private void show_version() {
    System.out.println(ToolErrorReporter.getMessage("msg.idswitch.version"));
  }
  
  private static int skip_matched_prefix(String paramString, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    int i = -1;
    int j = paramString.length();
    if (j <= paramInt2 - paramInt1) {
      paramInt2 = 0;
      while (true) {
        i = paramInt1;
        if (paramInt2 != j) {
          if (paramString.charAt(paramInt2) != paramArrayOfchar[paramInt1]) {
            i = -1;
            break;
          } 
          paramInt2++;
          paramInt1++;
          continue;
        } 
        break;
      } 
    } 
    return i;
  }
  
  private static int skip_name_char(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    while (paramInt1 != paramInt2) {
      char c = paramArrayOfchar[paramInt1];
      if (('a' > c || c > 'z') && ('A' > c || c > 'Z') && ('0' > c || c > '9') && c != '_')
        break; 
      paramInt1++;
    } 
    return paramInt1;
  }
  
  private static int skip_white_space(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    while (paramInt1 != paramInt2 && is_white_space(paramArrayOfchar[paramInt1]))
      paramInt1++; 
    return paramInt1;
  }
  
  private static String tag_name(int paramInt) {
    return (paramInt != -2) ? ((paramInt != -1) ? ((paramInt != 1) ? ((paramInt != 2) ? "" : "generated") : "string_id_map") : "/string_id_map") : "/generated";
  }
  
  void process_file(String paramString) throws IOException {
    InputStream inputStream;
    this.source_file = paramString;
    this.body = new FileBody();
    if (paramString.equals("-")) {
      inputStream = System.in;
    } else {
      inputStream = new FileInputStream(paramString);
    } 
    try {
      InputStreamReader inputStreamReader = new InputStreamReader();
      this(inputStream, "ASCII");
      this.body.readData(inputStreamReader);
      inputStream.close();
      process_file();
      return;
    } finally {
      inputStream.close();
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/idswitch/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */