package com.trendmicro.hippo;

import java.io.IOException;
import java.io.Reader;

class TokenStream {
  private static final char BYTE_ORDER_MARK = '﻿';
  
  private static final int EOF_CHAR = -1;
  
  private ObjToIntMap allStrings = new ObjToIntMap(50);
  
  private int commentCursor = -1;
  
  private String commentPrefix = "";
  
  Token.CommentType commentType;
  
  int cursor;
  
  private boolean dirtyLine;
  
  private boolean hitEOF = false;
  
  private boolean isBinary;
  
  private boolean isHex;
  
  private boolean isOctal;
  
  private boolean isOldOctal;
  
  private int lineEndChar = -1;
  
  private int lineStart = 0;
  
  int lineno;
  
  private double number;
  
  private Parser parser;
  
  private int quoteChar;
  
  String regExpFlags;
  
  private char[] sourceBuffer;
  
  int sourceCursor;
  
  private int sourceEnd;
  
  private Reader sourceReader;
  
  private String sourceString;
  
  private String string = "";
  
  private char[] stringBuffer = new char[128];
  
  private int stringBufferTop;
  
  int tokenBeg;
  
  int tokenEnd;
  
  private final int[] ungetBuffer = new int[3];
  
  private int ungetCursor;
  
  private boolean xmlIsAttribute;
  
  private boolean xmlIsTagContent;
  
  private int xmlOpenTagsCount;
  
  TokenStream(Parser paramParser, Reader paramReader, String paramString, int paramInt) {
    this.parser = paramParser;
    this.lineno = paramInt;
    if (paramReader != null) {
      if (paramString != null)
        Kit.codeBug(); 
      this.sourceReader = paramReader;
      this.sourceBuffer = new char[512];
      this.sourceEnd = 0;
    } else {
      if (paramString == null)
        Kit.codeBug(); 
      this.sourceString = paramString;
      this.sourceEnd = paramString.length();
    } 
    this.cursor = 0;
    this.sourceCursor = 0;
  }
  
  private void addToString(int paramInt) {
    int i = this.stringBufferTop;
    char[] arrayOfChar = this.stringBuffer;
    if (i == arrayOfChar.length) {
      char[] arrayOfChar1 = new char[arrayOfChar.length * 2];
      System.arraycopy(arrayOfChar, 0, arrayOfChar1, 0, i);
      this.stringBuffer = arrayOfChar1;
    } 
    this.stringBuffer[i] = (char)(char)paramInt;
    this.stringBufferTop = i + 1;
  }
  
  private boolean canUngetChar() {
    int i = this.ungetCursor;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 0)
      if (this.ungetBuffer[i - 1] != 10) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }  
    return bool2;
  }
  
  private final int charAt(int paramInt) {
    if (paramInt < 0)
      return -1; 
    String str = this.sourceString;
    if (str != null)
      return (paramInt >= this.sourceEnd) ? -1 : str.charAt(paramInt); 
    int i = paramInt;
    if (paramInt >= this.sourceEnd) {
      i = this.sourceCursor;
      try {
        boolean bool = fillSourceBuffer();
        if (!bool)
          return -1; 
        i = paramInt - i - this.sourceCursor;
      } catch (IOException iOException) {
        return -1;
      } 
    } 
    return this.sourceBuffer[i];
  }
  
  private String convertLastCharToHex(String paramString) {
    int i = paramString.length() - 1;
    StringBuilder stringBuilder = new StringBuilder(paramString.substring(0, i));
    stringBuilder.append("\\u");
    paramString = Integer.toHexString(paramString.charAt(i));
    for (i = 0; i < 4 - paramString.length(); i++)
      stringBuilder.append('0'); 
    stringBuilder.append(paramString);
    return stringBuilder.toString();
  }
  
  private boolean fillSourceBuffer() throws IOException {
    if (this.sourceString != null)
      Kit.codeBug(); 
    if (this.sourceEnd == this.sourceBuffer.length)
      if (this.lineStart != 0 && !isMarkingComment()) {
        char[] arrayOfChar1 = this.sourceBuffer;
        int j = this.lineStart;
        System.arraycopy(arrayOfChar1, j, arrayOfChar1, 0, this.sourceEnd - j);
        j = this.sourceEnd;
        int k = this.lineStart;
        this.sourceEnd = j - k;
        this.sourceCursor -= k;
        this.lineStart = 0;
      } else {
        char[] arrayOfChar2 = this.sourceBuffer;
        char[] arrayOfChar1 = new char[arrayOfChar2.length * 2];
        System.arraycopy(arrayOfChar2, 0, arrayOfChar1, 0, this.sourceEnd);
        this.sourceBuffer = arrayOfChar1;
      }  
    Reader reader = this.sourceReader;
    char[] arrayOfChar = this.sourceBuffer;
    int i = this.sourceEnd;
    i = reader.read(arrayOfChar, i, arrayOfChar.length - i);
    if (i < 0)
      return false; 
    this.sourceEnd += i;
    return true;
  }
  
  private int getChar() throws IOException {
    return getChar(true);
  }
  
  private int getChar(boolean paramBoolean) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield ungetCursor : I
    //   4: istore_2
    //   5: iload_2
    //   6: ifeq -> 36
    //   9: aload_0
    //   10: aload_0
    //   11: getfield cursor : I
    //   14: iconst_1
    //   15: iadd
    //   16: putfield cursor : I
    //   19: aload_0
    //   20: getfield ungetBuffer : [I
    //   23: astore_3
    //   24: iinc #2, -1
    //   27: aload_0
    //   28: iload_2
    //   29: putfield ungetCursor : I
    //   32: aload_3
    //   33: iload_2
    //   34: iaload
    //   35: ireturn
    //   36: aload_0
    //   37: getfield sourceString : Ljava/lang/String;
    //   40: astore_3
    //   41: aload_3
    //   42: ifnull -> 91
    //   45: aload_0
    //   46: getfield sourceCursor : I
    //   49: istore_2
    //   50: iload_2
    //   51: aload_0
    //   52: getfield sourceEnd : I
    //   55: if_icmpne -> 65
    //   58: aload_0
    //   59: iconst_1
    //   60: putfield hitEOF : Z
    //   63: iconst_m1
    //   64: ireturn
    //   65: aload_0
    //   66: aload_0
    //   67: getfield cursor : I
    //   70: iconst_1
    //   71: iadd
    //   72: putfield cursor : I
    //   75: aload_0
    //   76: iload_2
    //   77: iconst_1
    //   78: iadd
    //   79: putfield sourceCursor : I
    //   82: aload_3
    //   83: iload_2
    //   84: invokevirtual charAt : (I)C
    //   87: istore_2
    //   88: goto -> 147
    //   91: aload_0
    //   92: getfield sourceCursor : I
    //   95: aload_0
    //   96: getfield sourceEnd : I
    //   99: if_icmpne -> 116
    //   102: aload_0
    //   103: invokespecial fillSourceBuffer : ()Z
    //   106: ifne -> 116
    //   109: aload_0
    //   110: iconst_1
    //   111: putfield hitEOF : Z
    //   114: iconst_m1
    //   115: ireturn
    //   116: aload_0
    //   117: aload_0
    //   118: getfield cursor : I
    //   121: iconst_1
    //   122: iadd
    //   123: putfield cursor : I
    //   126: aload_0
    //   127: getfield sourceBuffer : [C
    //   130: astore_3
    //   131: aload_0
    //   132: getfield sourceCursor : I
    //   135: istore_2
    //   136: aload_0
    //   137: iload_2
    //   138: iconst_1
    //   139: iadd
    //   140: putfield sourceCursor : I
    //   143: aload_3
    //   144: iload_2
    //   145: caload
    //   146: istore_2
    //   147: aload_0
    //   148: getfield lineEndChar : I
    //   151: istore #4
    //   153: iload #4
    //   155: iflt -> 205
    //   158: iload #4
    //   160: bipush #13
    //   162: if_icmpne -> 180
    //   165: iload_2
    //   166: bipush #10
    //   168: if_icmpne -> 180
    //   171: aload_0
    //   172: bipush #10
    //   174: putfield lineEndChar : I
    //   177: goto -> 36
    //   180: aload_0
    //   181: iconst_m1
    //   182: putfield lineEndChar : I
    //   185: aload_0
    //   186: aload_0
    //   187: getfield sourceCursor : I
    //   190: iconst_1
    //   191: isub
    //   192: putfield lineStart : I
    //   195: aload_0
    //   196: aload_0
    //   197: getfield lineno : I
    //   200: iconst_1
    //   201: iadd
    //   202: putfield lineno : I
    //   205: iload_2
    //   206: bipush #127
    //   208: if_icmpgt -> 238
    //   211: iload_2
    //   212: bipush #10
    //   214: if_icmpeq -> 226
    //   217: iload_2
    //   218: istore #4
    //   220: iload_2
    //   221: bipush #13
    //   223: if_icmpne -> 279
    //   226: aload_0
    //   227: iload_2
    //   228: putfield lineEndChar : I
    //   231: bipush #10
    //   233: istore #4
    //   235: goto -> 279
    //   238: iload_2
    //   239: ldc 65279
    //   241: if_icmpne -> 246
    //   244: iload_2
    //   245: ireturn
    //   246: iload_1
    //   247: ifeq -> 260
    //   250: iload_2
    //   251: invokestatic isJSFormatChar : (I)Z
    //   254: ifeq -> 260
    //   257: goto -> 36
    //   260: iload_2
    //   261: istore #4
    //   263: iload_2
    //   264: invokestatic isJSLineTerminator : (I)Z
    //   267: ifeq -> 279
    //   270: aload_0
    //   271: iload_2
    //   272: putfield lineEndChar : I
    //   275: bipush #10
    //   277: istore #4
    //   279: iload #4
    //   281: ireturn
  }
  
  private int getCharIgnoreLineEnd() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield ungetCursor : I
    //   4: istore_1
    //   5: iload_1
    //   6: ifeq -> 36
    //   9: aload_0
    //   10: aload_0
    //   11: getfield cursor : I
    //   14: iconst_1
    //   15: iadd
    //   16: putfield cursor : I
    //   19: aload_0
    //   20: getfield ungetBuffer : [I
    //   23: astore_2
    //   24: iinc #1, -1
    //   27: aload_0
    //   28: iload_1
    //   29: putfield ungetCursor : I
    //   32: aload_2
    //   33: iload_1
    //   34: iaload
    //   35: ireturn
    //   36: aload_0
    //   37: getfield sourceString : Ljava/lang/String;
    //   40: astore_2
    //   41: aload_2
    //   42: ifnull -> 91
    //   45: aload_0
    //   46: getfield sourceCursor : I
    //   49: istore_1
    //   50: iload_1
    //   51: aload_0
    //   52: getfield sourceEnd : I
    //   55: if_icmpne -> 65
    //   58: aload_0
    //   59: iconst_1
    //   60: putfield hitEOF : Z
    //   63: iconst_m1
    //   64: ireturn
    //   65: aload_0
    //   66: aload_0
    //   67: getfield cursor : I
    //   70: iconst_1
    //   71: iadd
    //   72: putfield cursor : I
    //   75: aload_0
    //   76: iload_1
    //   77: iconst_1
    //   78: iadd
    //   79: putfield sourceCursor : I
    //   82: aload_2
    //   83: iload_1
    //   84: invokevirtual charAt : (I)C
    //   87: istore_1
    //   88: goto -> 147
    //   91: aload_0
    //   92: getfield sourceCursor : I
    //   95: aload_0
    //   96: getfield sourceEnd : I
    //   99: if_icmpne -> 116
    //   102: aload_0
    //   103: invokespecial fillSourceBuffer : ()Z
    //   106: ifne -> 116
    //   109: aload_0
    //   110: iconst_1
    //   111: putfield hitEOF : Z
    //   114: iconst_m1
    //   115: ireturn
    //   116: aload_0
    //   117: aload_0
    //   118: getfield cursor : I
    //   121: iconst_1
    //   122: iadd
    //   123: putfield cursor : I
    //   126: aload_0
    //   127: getfield sourceBuffer : [C
    //   130: astore_2
    //   131: aload_0
    //   132: getfield sourceCursor : I
    //   135: istore_1
    //   136: aload_0
    //   137: iload_1
    //   138: iconst_1
    //   139: iadd
    //   140: putfield sourceCursor : I
    //   143: aload_2
    //   144: iload_1
    //   145: caload
    //   146: istore_1
    //   147: iload_1
    //   148: bipush #127
    //   150: if_icmpgt -> 178
    //   153: iload_1
    //   154: bipush #10
    //   156: if_icmpeq -> 167
    //   159: iload_1
    //   160: istore_3
    //   161: iload_1
    //   162: bipush #13
    //   164: if_icmpne -> 213
    //   167: aload_0
    //   168: iload_1
    //   169: putfield lineEndChar : I
    //   172: bipush #10
    //   174: istore_3
    //   175: goto -> 213
    //   178: iload_1
    //   179: ldc 65279
    //   181: if_icmpne -> 186
    //   184: iload_1
    //   185: ireturn
    //   186: iload_1
    //   187: invokestatic isJSFormatChar : (I)Z
    //   190: ifeq -> 196
    //   193: goto -> 36
    //   196: iload_1
    //   197: istore_3
    //   198: iload_1
    //   199: invokestatic isJSLineTerminator : (I)Z
    //   202: ifeq -> 213
    //   205: aload_0
    //   206: iload_1
    //   207: putfield lineEndChar : I
    //   210: bipush #10
    //   212: istore_3
    //   213: iload_3
    //   214: ireturn
  }
  
  private String getStringFromBuffer() {
    this.tokenEnd = this.cursor;
    return new String(this.stringBuffer, 0, this.stringBufferTop);
  }
  
  private static boolean isAlpha(int paramInt) {
    boolean bool1 = true;
    boolean bool2 = true;
    if (paramInt <= 90) {
      if (65 > paramInt)
        bool2 = false; 
      return bool2;
    } 
    if (97 <= paramInt && paramInt <= 122) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  static boolean isDigit(int paramInt) {
    boolean bool;
    if (48 <= paramInt && paramInt <= 57) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static boolean isJSFormatChar(int paramInt) {
    boolean bool;
    if (paramInt > 127 && Character.getType((char)paramInt) == 16) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isJSSpace(int paramInt) {
    boolean bool = false;
    null = false;
    if (paramInt <= 127) {
      if (paramInt == 32 || paramInt == 9 || paramInt == 12 || paramInt == 11)
        null = true; 
      return null;
    } 
    if (paramInt != 160 && paramInt != 65279) {
      null = bool;
      return (Character.getType((char)paramInt) == 12) ? true : null;
    } 
    return true;
  }
  
  static boolean isKeyword(String paramString, int paramInt, boolean paramBoolean) {
    if (stringToKeyword(paramString, paramInt, paramBoolean) != 0) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    return paramBoolean;
  }
  
  private boolean isMarkingComment() {
    boolean bool;
    if (this.commentCursor != -1) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void markCommentStart() {
    markCommentStart("");
  }
  
  private void markCommentStart(String paramString) {
    if (this.parser.compilerEnv.isRecordingComments() && this.sourceReader != null) {
      this.commentPrefix = paramString;
      this.commentCursor = this.sourceCursor - 1;
    } 
  }
  
  private boolean matchChar(int paramInt) throws IOException {
    int i = getCharIgnoreLineEnd();
    if (i == paramInt) {
      this.tokenEnd = this.cursor;
      return true;
    } 
    ungetCharIgnoreLineEnd(i);
    return false;
  }
  
  private int peekChar() throws IOException {
    int i = getChar();
    ungetChar(i);
    return i;
  }
  
  private boolean readCDATA() throws IOException {
    for (int i = getChar(); i != -1; i = getChar()) {
      addToString(i);
      if (i == 93 && peekChar() == 93) {
        i = getChar();
        addToString(i);
        if (peekChar() == 62) {
          addToString(getChar());
          return true;
        } 
        continue;
      } 
    } 
    this.stringBufferTop = 0;
    this.string = null;
    this.parser.addError("msg.XML.bad.form");
    return false;
  }
  
  private boolean readEntity() throws IOException {
    int i = 1;
    for (int j = getChar(); j != -1; j = getChar()) {
      addToString(j);
      if (j != 60) {
        if (j == 62) {
          j = i - 1;
          i = j;
          if (j == 0)
            return true; 
        } 
      } else {
        i++;
      } 
    } 
    this.stringBufferTop = 0;
    this.string = null;
    this.parser.addError("msg.XML.bad.form");
    return false;
  }
  
  private boolean readPI() throws IOException {
    for (int i = getChar(); i != -1; i = getChar()) {
      addToString(i);
      if (i == 63 && peekChar() == 62) {
        addToString(getChar());
        return true;
      } 
    } 
    this.stringBufferTop = 0;
    this.string = null;
    this.parser.addError("msg.XML.bad.form");
    return false;
  }
  
  private boolean readQuotedString(int paramInt) throws IOException {
    for (int i = getChar(); i != -1; i = getChar()) {
      addToString(i);
      if (i == paramInt)
        return true; 
    } 
    this.stringBufferTop = 0;
    this.string = null;
    this.parser.addError("msg.XML.bad.form");
    return false;
  }
  
  private boolean readXmlComment() throws IOException {
    for (int i = getChar(); i != -1; i = getChar()) {
      addToString(i);
      if (i == 45 && peekChar() == 45) {
        i = getChar();
        addToString(i);
        if (peekChar() == 62) {
          addToString(getChar());
          return true;
        } 
        continue;
      } 
    } 
    this.stringBufferTop = 0;
    this.string = null;
    this.parser.addError("msg.XML.bad.form");
    return false;
  }
  
  private void skipLine() throws IOException {
    int i;
    while (true) {
      i = getChar();
      if (i != -1 && i != 10)
        continue; 
      break;
    } 
    ungetChar(i);
    this.tokenEnd = this.cursor;
  }
  
  private static int stringToKeyword(String paramString, int paramInt, boolean paramBoolean) {
    return (paramInt < 200) ? stringToKeywordForJS(paramString) : stringToKeywordForES(paramString, paramBoolean);
  }
  
  private static int stringToKeywordForES(String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: aload_0
    //   3: invokevirtual length : ()I
    //   6: tableswitch default -> 56, 2 -> 1217, 3 -> 1018, 4 -> 731, 5 -> 512, 6 -> 326, 7 -> 221, 8 -> 160, 9 -> 107, 10 -> 59
    //   56: goto -> 1299
    //   59: aload_0
    //   60: iconst_1
    //   61: invokevirtual charAt : (I)C
    //   64: istore_3
    //   65: iload_3
    //   66: bipush #109
    //   68: if_icmpne -> 87
    //   71: iload_1
    //   72: ifeq -> 87
    //   75: sipush #128
    //   78: istore_2
    //   79: ldc_w 'implements'
    //   82: astore #4
    //   84: goto -> 1302
    //   87: iload_3
    //   88: bipush #110
    //   90: if_icmpne -> 104
    //   93: bipush #53
    //   95: istore_2
    //   96: ldc_w 'instanceof'
    //   99: astore #4
    //   101: goto -> 1302
    //   104: goto -> 1299
    //   107: aload_0
    //   108: iconst_0
    //   109: invokevirtual charAt : (I)C
    //   112: istore_3
    //   113: iload_3
    //   114: bipush #105
    //   116: if_icmpne -> 135
    //   119: iload_1
    //   120: ifeq -> 135
    //   123: sipush #128
    //   126: istore_2
    //   127: ldc_w 'interface'
    //   130: astore #4
    //   132: goto -> 1302
    //   135: iload_3
    //   136: bipush #112
    //   138: if_icmpne -> 157
    //   141: iload_1
    //   142: ifeq -> 157
    //   145: sipush #128
    //   148: istore_2
    //   149: ldc_w 'protected'
    //   152: astore #4
    //   154: goto -> 1302
    //   157: goto -> 1299
    //   160: aload_0
    //   161: iconst_0
    //   162: invokevirtual charAt : (I)C
    //   165: istore_3
    //   166: iload_3
    //   167: bipush #99
    //   169: if_icmpeq -> 210
    //   172: iload_3
    //   173: bipush #100
    //   175: if_icmpeq -> 198
    //   178: iload_3
    //   179: bipush #102
    //   181: if_icmpeq -> 187
    //   184: goto -> 1299
    //   187: bipush #110
    //   189: istore_2
    //   190: ldc_w 'function'
    //   193: astore #4
    //   195: goto -> 1302
    //   198: sipush #161
    //   201: istore_2
    //   202: ldc_w 'debugger'
    //   205: astore #4
    //   207: goto -> 1302
    //   210: bipush #122
    //   212: istore_2
    //   213: ldc_w 'continue'
    //   216: astore #4
    //   218: goto -> 1302
    //   221: aload_0
    //   222: iconst_1
    //   223: invokevirtual charAt : (I)C
    //   226: istore_3
    //   227: iload_3
    //   228: bipush #97
    //   230: if_icmpeq -> 299
    //   233: iload_3
    //   234: bipush #101
    //   236: if_icmpeq -> 315
    //   239: iload_3
    //   240: bipush #105
    //   242: if_icmpeq -> 288
    //   245: iload_3
    //   246: bipush #114
    //   248: if_icmpeq -> 260
    //   251: iload_3
    //   252: bipush #120
    //   254: if_icmpeq -> 276
    //   257: goto -> 1299
    //   260: iload_1
    //   261: ifeq -> 276
    //   264: sipush #128
    //   267: istore_2
    //   268: ldc_w 'private'
    //   271: astore #4
    //   273: goto -> 1302
    //   276: sipush #128
    //   279: istore_2
    //   280: ldc_w 'extends'
    //   283: astore #4
    //   285: goto -> 1302
    //   288: bipush #126
    //   290: istore_2
    //   291: ldc_w 'finally'
    //   294: astore #4
    //   296: goto -> 1302
    //   299: iload_1
    //   300: ifeq -> 315
    //   303: sipush #128
    //   306: istore_2
    //   307: ldc_w 'package'
    //   310: astore #4
    //   312: goto -> 1302
    //   315: bipush #117
    //   317: istore_2
    //   318: ldc_w 'default'
    //   321: astore #4
    //   323: goto -> 1302
    //   326: aload_0
    //   327: astore #4
    //   329: aload #4
    //   331: iconst_1
    //   332: invokevirtual charAt : (I)C
    //   335: istore_3
    //   336: iload_3
    //   337: bipush #101
    //   339: if_icmpeq -> 469
    //   342: iload_3
    //   343: bipush #109
    //   345: if_icmpeq -> 457
    //   348: iload_3
    //   349: bipush #116
    //   351: if_icmpeq -> 414
    //   354: iload_3
    //   355: bipush #117
    //   357: if_icmpeq -> 430
    //   360: iload_3
    //   361: tableswitch default -> 388, 119 -> 446, 120 -> 402, 121 -> 391
    //   388: goto -> 1299
    //   391: bipush #32
    //   393: istore_2
    //   394: ldc_w 'typeof'
    //   397: astore #4
    //   399: goto -> 1302
    //   402: sipush #128
    //   405: istore_2
    //   406: ldc_w 'export'
    //   409: astore #4
    //   411: goto -> 1302
    //   414: iload_1
    //   415: ifeq -> 430
    //   418: sipush #128
    //   421: istore_2
    //   422: ldc_w 'static'
    //   425: astore #4
    //   427: goto -> 1302
    //   430: iload_1
    //   431: ifeq -> 446
    //   434: sipush #128
    //   437: istore_2
    //   438: ldc_w 'public'
    //   441: astore #4
    //   443: goto -> 1302
    //   446: bipush #115
    //   448: istore_2
    //   449: ldc_w 'switch'
    //   452: astore #4
    //   454: goto -> 1302
    //   457: sipush #128
    //   460: istore_2
    //   461: ldc_w 'import'
    //   464: astore #4
    //   466: goto -> 1302
    //   469: aload #4
    //   471: iconst_0
    //   472: invokevirtual charAt : (I)C
    //   475: istore_3
    //   476: iload_3
    //   477: bipush #100
    //   479: if_icmpne -> 493
    //   482: bipush #31
    //   484: istore_2
    //   485: ldc_w 'delete'
    //   488: astore #4
    //   490: goto -> 1302
    //   493: iload_3
    //   494: bipush #114
    //   496: if_icmpne -> 509
    //   499: iconst_4
    //   500: istore_2
    //   501: ldc_w 'return'
    //   504: astore #4
    //   506: goto -> 1302
    //   509: goto -> 1299
    //   512: aload_0
    //   513: astore #4
    //   515: aload #4
    //   517: iconst_2
    //   518: invokevirtual charAt : (I)C
    //   521: istore_3
    //   522: iload_3
    //   523: bipush #97
    //   525: if_icmpeq -> 685
    //   528: iload_3
    //   529: bipush #101
    //   531: if_icmpeq -> 641
    //   534: iload_3
    //   535: bipush #105
    //   537: if_icmpeq -> 630
    //   540: iload_3
    //   541: bipush #108
    //   543: if_icmpeq -> 619
    //   546: iload_3
    //   547: bipush #110
    //   549: if_icmpeq -> 607
    //   552: iload_3
    //   553: bipush #112
    //   555: if_icmpeq -> 595
    //   558: iload_3
    //   559: bipush #114
    //   561: if_icmpeq -> 584
    //   564: iload_3
    //   565: bipush #116
    //   567: if_icmpeq -> 573
    //   570: goto -> 1299
    //   573: bipush #125
    //   575: istore_2
    //   576: ldc_w 'catch'
    //   579: astore #4
    //   581: goto -> 1302
    //   584: bipush #50
    //   586: istore_2
    //   587: ldc_w 'throw'
    //   590: astore #4
    //   592: goto -> 1302
    //   595: sipush #128
    //   598: istore_2
    //   599: ldc_w 'super'
    //   602: astore #4
    //   604: goto -> 1302
    //   607: sipush #155
    //   610: istore_2
    //   611: ldc_w 'const'
    //   614: astore #4
    //   616: goto -> 1302
    //   619: bipush #44
    //   621: istore_2
    //   622: ldc_w 'false'
    //   625: astore #4
    //   627: goto -> 1302
    //   630: bipush #118
    //   632: istore_2
    //   633: ldc_w 'while'
    //   636: astore #4
    //   638: goto -> 1302
    //   641: aload #4
    //   643: iconst_0
    //   644: invokevirtual charAt : (I)C
    //   647: istore_3
    //   648: iload_3
    //   649: bipush #98
    //   651: if_icmpne -> 665
    //   654: bipush #121
    //   656: istore_2
    //   657: ldc_w 'break'
    //   660: astore #4
    //   662: goto -> 1302
    //   665: iload_3
    //   666: bipush #121
    //   668: if_icmpne -> 682
    //   671: bipush #73
    //   673: istore_2
    //   674: ldc_w 'yield'
    //   677: astore #4
    //   679: goto -> 1302
    //   682: goto -> 1299
    //   685: aload #4
    //   687: iconst_0
    //   688: invokevirtual charAt : (I)C
    //   691: istore_3
    //   692: iload_3
    //   693: bipush #99
    //   695: if_icmpne -> 710
    //   698: sipush #128
    //   701: istore_2
    //   702: ldc_w 'class'
    //   705: astore #4
    //   707: goto -> 1302
    //   710: iload_3
    //   711: bipush #97
    //   713: if_icmpne -> 728
    //   716: sipush #128
    //   719: istore_2
    //   720: ldc_w 'await'
    //   723: astore #4
    //   725: goto -> 1302
    //   728: goto -> 1299
    //   731: aload_0
    //   732: astore #4
    //   734: aload #4
    //   736: iconst_0
    //   737: invokevirtual charAt : (I)C
    //   740: istore_3
    //   741: iload_3
    //   742: bipush #99
    //   744: if_icmpeq -> 976
    //   747: iload_3
    //   748: bipush #101
    //   750: if_icmpeq -> 894
    //   753: iload_3
    //   754: bipush #110
    //   756: if_icmpeq -> 883
    //   759: iload_3
    //   760: bipush #116
    //   762: if_icmpeq -> 802
    //   765: iload_3
    //   766: bipush #118
    //   768: if_icmpeq -> 791
    //   771: iload_3
    //   772: bipush #119
    //   774: if_icmpeq -> 780
    //   777: goto -> 1299
    //   780: bipush #124
    //   782: istore_2
    //   783: ldc_w 'with'
    //   786: astore #4
    //   788: goto -> 1302
    //   791: bipush #127
    //   793: istore_2
    //   794: ldc_w 'void'
    //   797: astore #4
    //   799: goto -> 1302
    //   802: aload #4
    //   804: iconst_3
    //   805: invokevirtual charAt : (I)C
    //   808: istore_3
    //   809: iload_3
    //   810: bipush #101
    //   812: if_icmpne -> 846
    //   815: aload #4
    //   817: iconst_2
    //   818: invokevirtual charAt : (I)C
    //   821: bipush #117
    //   823: if_icmpne -> 843
    //   826: aload #4
    //   828: iconst_1
    //   829: invokevirtual charAt : (I)C
    //   832: bipush #114
    //   834: if_icmpne -> 843
    //   837: bipush #45
    //   839: istore_2
    //   840: goto -> 1327
    //   843: goto -> 1299
    //   846: iload_3
    //   847: bipush #115
    //   849: if_icmpne -> 880
    //   852: aload #4
    //   854: iconst_2
    //   855: invokevirtual charAt : (I)C
    //   858: bipush #105
    //   860: if_icmpne -> 880
    //   863: aload #4
    //   865: iconst_1
    //   866: invokevirtual charAt : (I)C
    //   869: bipush #104
    //   871: if_icmpne -> 880
    //   874: bipush #43
    //   876: istore_2
    //   877: goto -> 1327
    //   880: goto -> 1299
    //   883: bipush #42
    //   885: istore_2
    //   886: ldc_w 'null'
    //   889: astore #4
    //   891: goto -> 1302
    //   894: aload #4
    //   896: iconst_3
    //   897: invokevirtual charAt : (I)C
    //   900: istore_3
    //   901: iload_3
    //   902: bipush #101
    //   904: if_icmpne -> 938
    //   907: aload #4
    //   909: iconst_2
    //   910: invokevirtual charAt : (I)C
    //   913: bipush #115
    //   915: if_icmpne -> 935
    //   918: aload #4
    //   920: iconst_1
    //   921: invokevirtual charAt : (I)C
    //   924: bipush #108
    //   926: if_icmpne -> 935
    //   929: bipush #114
    //   931: istore_2
    //   932: goto -> 1327
    //   935: goto -> 1299
    //   938: iload_3
    //   939: bipush #109
    //   941: if_icmpne -> 973
    //   944: aload #4
    //   946: iconst_2
    //   947: invokevirtual charAt : (I)C
    //   950: bipush #117
    //   952: if_icmpne -> 973
    //   955: aload #4
    //   957: iconst_1
    //   958: invokevirtual charAt : (I)C
    //   961: bipush #110
    //   963: if_icmpne -> 973
    //   966: sipush #128
    //   969: istore_2
    //   970: goto -> 1327
    //   973: goto -> 1299
    //   976: aload #4
    //   978: iconst_3
    //   979: invokevirtual charAt : (I)C
    //   982: bipush #101
    //   984: if_icmpne -> 1015
    //   987: aload #4
    //   989: iconst_2
    //   990: invokevirtual charAt : (I)C
    //   993: bipush #115
    //   995: if_icmpne -> 1015
    //   998: aload #4
    //   1000: iconst_1
    //   1001: invokevirtual charAt : (I)C
    //   1004: bipush #97
    //   1006: if_icmpne -> 1015
    //   1009: bipush #116
    //   1011: istore_2
    //   1012: goto -> 1327
    //   1015: goto -> 1299
    //   1018: aload_0
    //   1019: astore #4
    //   1021: aload #4
    //   1023: iconst_0
    //   1024: invokevirtual charAt : (I)C
    //   1027: istore_3
    //   1028: iload_3
    //   1029: bipush #102
    //   1031: if_icmpeq -> 1186
    //   1034: iload_3
    //   1035: bipush #108
    //   1037: if_icmpeq -> 1154
    //   1040: iload_3
    //   1041: bipush #110
    //   1043: if_icmpeq -> 1123
    //   1046: iload_3
    //   1047: bipush #116
    //   1049: if_icmpeq -> 1092
    //   1052: iload_3
    //   1053: bipush #118
    //   1055: if_icmpeq -> 1061
    //   1058: goto -> 1299
    //   1061: aload #4
    //   1063: iconst_2
    //   1064: invokevirtual charAt : (I)C
    //   1067: bipush #114
    //   1069: if_icmpne -> 1089
    //   1072: aload #4
    //   1074: iconst_1
    //   1075: invokevirtual charAt : (I)C
    //   1078: bipush #97
    //   1080: if_icmpne -> 1089
    //   1083: bipush #123
    //   1085: istore_2
    //   1086: goto -> 1327
    //   1089: goto -> 1299
    //   1092: aload #4
    //   1094: iconst_2
    //   1095: invokevirtual charAt : (I)C
    //   1098: bipush #121
    //   1100: if_icmpne -> 1120
    //   1103: aload #4
    //   1105: iconst_1
    //   1106: invokevirtual charAt : (I)C
    //   1109: bipush #114
    //   1111: if_icmpne -> 1120
    //   1114: bipush #82
    //   1116: istore_2
    //   1117: goto -> 1327
    //   1120: goto -> 1299
    //   1123: aload #4
    //   1125: iconst_2
    //   1126: invokevirtual charAt : (I)C
    //   1129: bipush #119
    //   1131: if_icmpne -> 1151
    //   1134: aload #4
    //   1136: iconst_1
    //   1137: invokevirtual charAt : (I)C
    //   1140: bipush #101
    //   1142: if_icmpne -> 1151
    //   1145: bipush #30
    //   1147: istore_2
    //   1148: goto -> 1327
    //   1151: goto -> 1299
    //   1154: aload #4
    //   1156: iconst_2
    //   1157: invokevirtual charAt : (I)C
    //   1160: bipush #116
    //   1162: if_icmpne -> 1183
    //   1165: aload #4
    //   1167: iconst_1
    //   1168: invokevirtual charAt : (I)C
    //   1171: bipush #101
    //   1173: if_icmpne -> 1183
    //   1176: sipush #154
    //   1179: istore_2
    //   1180: goto -> 1327
    //   1183: goto -> 1299
    //   1186: aload #4
    //   1188: iconst_2
    //   1189: invokevirtual charAt : (I)C
    //   1192: bipush #114
    //   1194: if_icmpne -> 1214
    //   1197: aload #4
    //   1199: iconst_1
    //   1200: invokevirtual charAt : (I)C
    //   1203: bipush #111
    //   1205: if_icmpne -> 1214
    //   1208: bipush #120
    //   1210: istore_2
    //   1211: goto -> 1327
    //   1214: goto -> 1299
    //   1217: aload_0
    //   1218: astore #4
    //   1220: aload #4
    //   1222: iconst_1
    //   1223: invokevirtual charAt : (I)C
    //   1226: istore_3
    //   1227: iload_3
    //   1228: bipush #102
    //   1230: if_icmpne -> 1253
    //   1233: aload #4
    //   1235: iconst_0
    //   1236: invokevirtual charAt : (I)C
    //   1239: bipush #105
    //   1241: if_icmpne -> 1250
    //   1244: bipush #113
    //   1246: istore_2
    //   1247: goto -> 1327
    //   1250: goto -> 1299
    //   1253: iload_3
    //   1254: bipush #110
    //   1256: if_icmpne -> 1276
    //   1259: aload #4
    //   1261: iconst_0
    //   1262: invokevirtual charAt : (I)C
    //   1265: bipush #105
    //   1267: if_icmpne -> 1299
    //   1270: bipush #52
    //   1272: istore_2
    //   1273: goto -> 1327
    //   1276: iload_3
    //   1277: bipush #111
    //   1279: if_icmpne -> 1299
    //   1282: aload #4
    //   1284: iconst_0
    //   1285: invokevirtual charAt : (I)C
    //   1288: bipush #100
    //   1290: if_icmpne -> 1299
    //   1293: bipush #119
    //   1295: istore_2
    //   1296: goto -> 1327
    //   1299: aconst_null
    //   1300: astore #4
    //   1302: aload #4
    //   1304: ifnull -> 1327
    //   1307: aload #4
    //   1309: aload_0
    //   1310: if_acmpeq -> 1327
    //   1313: aload #4
    //   1315: aload_0
    //   1316: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1319: ifne -> 1327
    //   1322: iconst_0
    //   1323: istore_2
    //   1324: goto -> 1327
    //   1327: iload_2
    //   1328: ifne -> 1333
    //   1331: iconst_0
    //   1332: ireturn
    //   1333: iload_2
    //   1334: sipush #255
    //   1337: iand
    //   1338: ireturn
  }
  
  private static int stringToKeywordForJS(String paramString) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: invokevirtual length : ()I
    //   6: tableswitch default -> 64, 2 -> 1445, 3 -> 1222, 4 -> 864, 5 -> 600, 6 -> 381, 7 -> 272, 8 -> 180, 9 -> 120, 10 -> 78, 11 -> 64, 12 -> 67
    //   64: goto -> 1522
    //   67: sipush #128
    //   70: istore_1
    //   71: ldc_w 'synchronized'
    //   74: astore_2
    //   75: goto -> 1524
    //   78: aload_0
    //   79: iconst_1
    //   80: invokevirtual charAt : (I)C
    //   83: istore_3
    //   84: iload_3
    //   85: bipush #109
    //   87: if_icmpne -> 101
    //   90: sipush #128
    //   93: istore_1
    //   94: ldc_w 'implements'
    //   97: astore_2
    //   98: goto -> 1524
    //   101: iload_3
    //   102: bipush #110
    //   104: if_icmpne -> 117
    //   107: bipush #53
    //   109: istore_1
    //   110: ldc_w 'instanceof'
    //   113: astore_2
    //   114: goto -> 1524
    //   117: goto -> 1522
    //   120: aload_0
    //   121: iconst_0
    //   122: invokevirtual charAt : (I)C
    //   125: istore_3
    //   126: iload_3
    //   127: bipush #105
    //   129: if_icmpne -> 143
    //   132: sipush #128
    //   135: istore_1
    //   136: ldc_w 'interface'
    //   139: astore_2
    //   140: goto -> 1524
    //   143: iload_3
    //   144: bipush #112
    //   146: if_icmpne -> 160
    //   149: sipush #128
    //   152: istore_1
    //   153: ldc_w 'protected'
    //   156: astore_2
    //   157: goto -> 1524
    //   160: iload_3
    //   161: bipush #116
    //   163: if_icmpne -> 177
    //   166: sipush #128
    //   169: istore_1
    //   170: ldc_w 'transient'
    //   173: astore_2
    //   174: goto -> 1524
    //   177: goto -> 1522
    //   180: aload_0
    //   181: iconst_0
    //   182: invokevirtual charAt : (I)C
    //   185: istore_3
    //   186: iload_3
    //   187: bipush #97
    //   189: if_icmpeq -> 261
    //   192: iload_3
    //   193: bipush #102
    //   195: if_icmpeq -> 251
    //   198: iload_3
    //   199: bipush #118
    //   201: if_icmpeq -> 240
    //   204: iload_3
    //   205: bipush #99
    //   207: if_icmpeq -> 230
    //   210: iload_3
    //   211: bipush #100
    //   213: if_icmpeq -> 219
    //   216: goto -> 1522
    //   219: sipush #161
    //   222: istore_1
    //   223: ldc_w 'debugger'
    //   226: astore_2
    //   227: goto -> 1524
    //   230: bipush #122
    //   232: istore_1
    //   233: ldc_w 'continue'
    //   236: astore_2
    //   237: goto -> 1524
    //   240: sipush #128
    //   243: istore_1
    //   244: ldc_w 'volatile'
    //   247: astore_2
    //   248: goto -> 1524
    //   251: bipush #110
    //   253: istore_1
    //   254: ldc_w 'function'
    //   257: astore_2
    //   258: goto -> 1524
    //   261: sipush #128
    //   264: istore_1
    //   265: ldc_w 'abstract'
    //   268: astore_2
    //   269: goto -> 1524
    //   272: aload_0
    //   273: iconst_1
    //   274: invokevirtual charAt : (I)C
    //   277: istore_3
    //   278: iload_3
    //   279: bipush #97
    //   281: if_icmpeq -> 370
    //   284: iload_3
    //   285: bipush #101
    //   287: if_icmpeq -> 360
    //   290: iload_3
    //   291: bipush #105
    //   293: if_icmpeq -> 350
    //   296: iload_3
    //   297: bipush #111
    //   299: if_icmpeq -> 339
    //   302: iload_3
    //   303: bipush #114
    //   305: if_icmpeq -> 328
    //   308: iload_3
    //   309: bipush #120
    //   311: if_icmpeq -> 317
    //   314: goto -> 1522
    //   317: sipush #128
    //   320: istore_1
    //   321: ldc_w 'extends'
    //   324: astore_2
    //   325: goto -> 1524
    //   328: sipush #128
    //   331: istore_1
    //   332: ldc_w 'private'
    //   335: astore_2
    //   336: goto -> 1524
    //   339: sipush #128
    //   342: istore_1
    //   343: ldc_w 'boolean'
    //   346: astore_2
    //   347: goto -> 1524
    //   350: bipush #126
    //   352: istore_1
    //   353: ldc_w 'finally'
    //   356: astore_2
    //   357: goto -> 1524
    //   360: bipush #117
    //   362: istore_1
    //   363: ldc_w 'default'
    //   366: astore_2
    //   367: goto -> 1524
    //   370: sipush #128
    //   373: istore_1
    //   374: ldc_w 'package'
    //   377: astore_2
    //   378: goto -> 1524
    //   381: aload_0
    //   382: astore_2
    //   383: aload_2
    //   384: iconst_1
    //   385: invokevirtual charAt : (I)C
    //   388: istore_3
    //   389: iload_3
    //   390: bipush #97
    //   392: if_icmpeq -> 589
    //   395: iload_3
    //   396: bipush #101
    //   398: if_icmpeq -> 549
    //   401: iload_3
    //   402: bipush #104
    //   404: if_icmpeq -> 538
    //   407: iload_3
    //   408: bipush #109
    //   410: if_icmpeq -> 527
    //   413: iload_3
    //   414: bipush #111
    //   416: if_icmpeq -> 516
    //   419: iload_3
    //   420: bipush #116
    //   422: if_icmpeq -> 505
    //   425: iload_3
    //   426: bipush #117
    //   428: if_icmpeq -> 494
    //   431: iload_3
    //   432: tableswitch default -> 460, 119 -> 484, 120 -> 473, 121 -> 463
    //   460: goto -> 1522
    //   463: bipush #32
    //   465: istore_1
    //   466: ldc_w 'typeof'
    //   469: astore_2
    //   470: goto -> 1524
    //   473: sipush #128
    //   476: istore_1
    //   477: ldc_w 'export'
    //   480: astore_2
    //   481: goto -> 1524
    //   484: bipush #115
    //   486: istore_1
    //   487: ldc_w 'switch'
    //   490: astore_2
    //   491: goto -> 1524
    //   494: sipush #128
    //   497: istore_1
    //   498: ldc_w 'public'
    //   501: astore_2
    //   502: goto -> 1524
    //   505: sipush #128
    //   508: istore_1
    //   509: ldc_w 'static'
    //   512: astore_2
    //   513: goto -> 1524
    //   516: sipush #128
    //   519: istore_1
    //   520: ldc_w 'double'
    //   523: astore_2
    //   524: goto -> 1524
    //   527: sipush #128
    //   530: istore_1
    //   531: ldc_w 'import'
    //   534: astore_2
    //   535: goto -> 1524
    //   538: sipush #128
    //   541: istore_1
    //   542: ldc_w 'throws'
    //   545: astore_2
    //   546: goto -> 1524
    //   549: aload_2
    //   550: iconst_0
    //   551: invokevirtual charAt : (I)C
    //   554: istore_3
    //   555: iload_3
    //   556: bipush #100
    //   558: if_icmpne -> 571
    //   561: bipush #31
    //   563: istore_1
    //   564: ldc_w 'delete'
    //   567: astore_2
    //   568: goto -> 1524
    //   571: iload_3
    //   572: bipush #114
    //   574: if_icmpne -> 586
    //   577: iconst_4
    //   578: istore_1
    //   579: ldc_w 'return'
    //   582: astore_2
    //   583: goto -> 1524
    //   586: goto -> 1522
    //   589: sipush #128
    //   592: istore_1
    //   593: ldc_w 'native'
    //   596: astore_2
    //   597: goto -> 1524
    //   600: aload_0
    //   601: astore_2
    //   602: aload_2
    //   603: iconst_2
    //   604: invokevirtual charAt : (I)C
    //   607: istore_3
    //   608: iload_3
    //   609: bipush #97
    //   611: if_icmpeq -> 853
    //   614: iload_3
    //   615: bipush #101
    //   617: if_icmpeq -> 812
    //   620: iload_3
    //   621: bipush #105
    //   623: if_icmpeq -> 802
    //   626: iload_3
    //   627: bipush #108
    //   629: if_icmpeq -> 792
    //   632: iload_3
    //   633: bipush #114
    //   635: if_icmpeq -> 782
    //   638: iload_3
    //   639: bipush #116
    //   641: if_icmpeq -> 772
    //   644: iload_3
    //   645: tableswitch default -> 672, 110 -> 729, 111 -> 686, 112 -> 675
    //   672: goto -> 1522
    //   675: sipush #128
    //   678: istore_1
    //   679: ldc_w 'super'
    //   682: astore_2
    //   683: goto -> 1524
    //   686: aload_2
    //   687: iconst_0
    //   688: invokevirtual charAt : (I)C
    //   691: istore_3
    //   692: iload_3
    //   693: bipush #102
    //   695: if_icmpne -> 709
    //   698: sipush #128
    //   701: istore_1
    //   702: ldc_w 'float'
    //   705: astore_2
    //   706: goto -> 1524
    //   709: iload_3
    //   710: bipush #115
    //   712: if_icmpne -> 726
    //   715: sipush #128
    //   718: istore_1
    //   719: ldc_w 'short'
    //   722: astore_2
    //   723: goto -> 1524
    //   726: goto -> 1522
    //   729: aload_2
    //   730: iconst_0
    //   731: invokevirtual charAt : (I)C
    //   734: istore_3
    //   735: iload_3
    //   736: bipush #99
    //   738: if_icmpne -> 752
    //   741: sipush #155
    //   744: istore_1
    //   745: ldc_w 'const'
    //   748: astore_2
    //   749: goto -> 1524
    //   752: iload_3
    //   753: bipush #102
    //   755: if_icmpne -> 769
    //   758: sipush #128
    //   761: istore_1
    //   762: ldc_w 'final'
    //   765: astore_2
    //   766: goto -> 1524
    //   769: goto -> 1522
    //   772: bipush #125
    //   774: istore_1
    //   775: ldc_w 'catch'
    //   778: astore_2
    //   779: goto -> 1524
    //   782: bipush #50
    //   784: istore_1
    //   785: ldc_w 'throw'
    //   788: astore_2
    //   789: goto -> 1524
    //   792: bipush #44
    //   794: istore_1
    //   795: ldc_w 'false'
    //   798: astore_2
    //   799: goto -> 1524
    //   802: bipush #118
    //   804: istore_1
    //   805: ldc_w 'while'
    //   808: astore_2
    //   809: goto -> 1524
    //   812: aload_2
    //   813: iconst_0
    //   814: invokevirtual charAt : (I)C
    //   817: istore_3
    //   818: iload_3
    //   819: bipush #98
    //   821: if_icmpne -> 834
    //   824: bipush #121
    //   826: istore_1
    //   827: ldc_w 'break'
    //   830: astore_2
    //   831: goto -> 1524
    //   834: iload_3
    //   835: bipush #121
    //   837: if_icmpne -> 850
    //   840: bipush #73
    //   842: istore_1
    //   843: ldc_w 'yield'
    //   846: astore_2
    //   847: goto -> 1524
    //   850: goto -> 1522
    //   853: sipush #128
    //   856: istore_1
    //   857: ldc_w 'class'
    //   860: astore_2
    //   861: goto -> 1524
    //   864: aload_0
    //   865: astore_2
    //   866: aload_2
    //   867: iconst_0
    //   868: invokevirtual charAt : (I)C
    //   871: istore_3
    //   872: iload_3
    //   873: bipush #98
    //   875: if_icmpeq -> 1211
    //   878: iload_3
    //   879: bipush #99
    //   881: if_icmpeq -> 1134
    //   884: iload_3
    //   885: bipush #101
    //   887: if_icmpeq -> 1057
    //   890: iload_3
    //   891: bipush #103
    //   893: if_icmpeq -> 1046
    //   896: iload_3
    //   897: bipush #108
    //   899: if_icmpeq -> 1035
    //   902: iload_3
    //   903: bipush #110
    //   905: if_icmpeq -> 1025
    //   908: iload_3
    //   909: bipush #116
    //   911: if_icmpeq -> 949
    //   914: iload_3
    //   915: bipush #118
    //   917: if_icmpeq -> 939
    //   920: iload_3
    //   921: bipush #119
    //   923: if_icmpeq -> 929
    //   926: goto -> 1522
    //   929: bipush #124
    //   931: istore_1
    //   932: ldc_w 'with'
    //   935: astore_2
    //   936: goto -> 1524
    //   939: bipush #127
    //   941: istore_1
    //   942: ldc_w 'void'
    //   945: astore_2
    //   946: goto -> 1524
    //   949: aload_2
    //   950: iconst_3
    //   951: invokevirtual charAt : (I)C
    //   954: istore_3
    //   955: iload_3
    //   956: bipush #101
    //   958: if_icmpne -> 990
    //   961: aload_2
    //   962: iconst_2
    //   963: invokevirtual charAt : (I)C
    //   966: bipush #117
    //   968: if_icmpne -> 987
    //   971: aload_2
    //   972: iconst_1
    //   973: invokevirtual charAt : (I)C
    //   976: bipush #114
    //   978: if_icmpne -> 987
    //   981: bipush #45
    //   983: istore_1
    //   984: goto -> 1546
    //   987: goto -> 1522
    //   990: iload_3
    //   991: bipush #115
    //   993: if_icmpne -> 1022
    //   996: aload_2
    //   997: iconst_2
    //   998: invokevirtual charAt : (I)C
    //   1001: bipush #105
    //   1003: if_icmpne -> 1022
    //   1006: aload_2
    //   1007: iconst_1
    //   1008: invokevirtual charAt : (I)C
    //   1011: bipush #104
    //   1013: if_icmpne -> 1022
    //   1016: bipush #43
    //   1018: istore_1
    //   1019: goto -> 1546
    //   1022: goto -> 1522
    //   1025: bipush #42
    //   1027: istore_1
    //   1028: ldc_w 'null'
    //   1031: astore_2
    //   1032: goto -> 1524
    //   1035: sipush #128
    //   1038: istore_1
    //   1039: ldc_w 'long'
    //   1042: astore_2
    //   1043: goto -> 1524
    //   1046: sipush #128
    //   1049: istore_1
    //   1050: ldc_w 'goto'
    //   1053: astore_2
    //   1054: goto -> 1524
    //   1057: aload_2
    //   1058: iconst_3
    //   1059: invokevirtual charAt : (I)C
    //   1062: istore_3
    //   1063: iload_3
    //   1064: bipush #101
    //   1066: if_icmpne -> 1098
    //   1069: aload_2
    //   1070: iconst_2
    //   1071: invokevirtual charAt : (I)C
    //   1074: bipush #115
    //   1076: if_icmpne -> 1095
    //   1079: aload_2
    //   1080: iconst_1
    //   1081: invokevirtual charAt : (I)C
    //   1084: bipush #108
    //   1086: if_icmpne -> 1095
    //   1089: bipush #114
    //   1091: istore_1
    //   1092: goto -> 1546
    //   1095: goto -> 1522
    //   1098: iload_3
    //   1099: bipush #109
    //   1101: if_icmpne -> 1131
    //   1104: aload_2
    //   1105: iconst_2
    //   1106: invokevirtual charAt : (I)C
    //   1109: bipush #117
    //   1111: if_icmpne -> 1131
    //   1114: aload_2
    //   1115: iconst_1
    //   1116: invokevirtual charAt : (I)C
    //   1119: bipush #110
    //   1121: if_icmpne -> 1131
    //   1124: sipush #128
    //   1127: istore_1
    //   1128: goto -> 1546
    //   1131: goto -> 1522
    //   1134: aload_2
    //   1135: iconst_3
    //   1136: invokevirtual charAt : (I)C
    //   1139: istore_3
    //   1140: iload_3
    //   1141: bipush #101
    //   1143: if_icmpne -> 1175
    //   1146: aload_2
    //   1147: iconst_2
    //   1148: invokevirtual charAt : (I)C
    //   1151: bipush #115
    //   1153: if_icmpne -> 1172
    //   1156: aload_2
    //   1157: iconst_1
    //   1158: invokevirtual charAt : (I)C
    //   1161: bipush #97
    //   1163: if_icmpne -> 1172
    //   1166: bipush #116
    //   1168: istore_1
    //   1169: goto -> 1546
    //   1172: goto -> 1522
    //   1175: iload_3
    //   1176: bipush #114
    //   1178: if_icmpne -> 1208
    //   1181: aload_2
    //   1182: iconst_2
    //   1183: invokevirtual charAt : (I)C
    //   1186: bipush #97
    //   1188: if_icmpne -> 1208
    //   1191: aload_2
    //   1192: iconst_1
    //   1193: invokevirtual charAt : (I)C
    //   1196: bipush #104
    //   1198: if_icmpne -> 1208
    //   1201: sipush #128
    //   1204: istore_1
    //   1205: goto -> 1546
    //   1208: goto -> 1522
    //   1211: sipush #128
    //   1214: istore_1
    //   1215: ldc_w 'byte'
    //   1218: astore_2
    //   1219: goto -> 1524
    //   1222: aload_0
    //   1223: astore_2
    //   1224: aload_2
    //   1225: iconst_0
    //   1226: invokevirtual charAt : (I)C
    //   1229: istore_3
    //   1230: iload_3
    //   1231: bipush #102
    //   1233: if_icmpeq -> 1416
    //   1236: iload_3
    //   1237: bipush #105
    //   1239: if_icmpeq -> 1386
    //   1242: iload_3
    //   1243: bipush #108
    //   1245: if_icmpeq -> 1356
    //   1248: iload_3
    //   1249: bipush #110
    //   1251: if_icmpeq -> 1327
    //   1254: iload_3
    //   1255: bipush #116
    //   1257: if_icmpeq -> 1298
    //   1260: iload_3
    //   1261: bipush #118
    //   1263: if_icmpeq -> 1269
    //   1266: goto -> 1522
    //   1269: aload_2
    //   1270: iconst_2
    //   1271: invokevirtual charAt : (I)C
    //   1274: bipush #114
    //   1276: if_icmpne -> 1295
    //   1279: aload_2
    //   1280: iconst_1
    //   1281: invokevirtual charAt : (I)C
    //   1284: bipush #97
    //   1286: if_icmpne -> 1295
    //   1289: bipush #123
    //   1291: istore_1
    //   1292: goto -> 1546
    //   1295: goto -> 1522
    //   1298: aload_2
    //   1299: iconst_2
    //   1300: invokevirtual charAt : (I)C
    //   1303: bipush #121
    //   1305: if_icmpne -> 1324
    //   1308: aload_2
    //   1309: iconst_1
    //   1310: invokevirtual charAt : (I)C
    //   1313: bipush #114
    //   1315: if_icmpne -> 1324
    //   1318: bipush #82
    //   1320: istore_1
    //   1321: goto -> 1546
    //   1324: goto -> 1522
    //   1327: aload_2
    //   1328: iconst_2
    //   1329: invokevirtual charAt : (I)C
    //   1332: bipush #119
    //   1334: if_icmpne -> 1353
    //   1337: aload_2
    //   1338: iconst_1
    //   1339: invokevirtual charAt : (I)C
    //   1342: bipush #101
    //   1344: if_icmpne -> 1353
    //   1347: bipush #30
    //   1349: istore_1
    //   1350: goto -> 1546
    //   1353: goto -> 1522
    //   1356: aload_2
    //   1357: iconst_2
    //   1358: invokevirtual charAt : (I)C
    //   1361: bipush #116
    //   1363: if_icmpne -> 1383
    //   1366: aload_2
    //   1367: iconst_1
    //   1368: invokevirtual charAt : (I)C
    //   1371: bipush #101
    //   1373: if_icmpne -> 1383
    //   1376: sipush #154
    //   1379: istore_1
    //   1380: goto -> 1546
    //   1383: goto -> 1522
    //   1386: aload_2
    //   1387: iconst_2
    //   1388: invokevirtual charAt : (I)C
    //   1391: bipush #116
    //   1393: if_icmpne -> 1413
    //   1396: aload_2
    //   1397: iconst_1
    //   1398: invokevirtual charAt : (I)C
    //   1401: bipush #110
    //   1403: if_icmpne -> 1413
    //   1406: sipush #128
    //   1409: istore_1
    //   1410: goto -> 1546
    //   1413: goto -> 1522
    //   1416: aload_2
    //   1417: iconst_2
    //   1418: invokevirtual charAt : (I)C
    //   1421: bipush #114
    //   1423: if_icmpne -> 1442
    //   1426: aload_2
    //   1427: iconst_1
    //   1428: invokevirtual charAt : (I)C
    //   1431: bipush #111
    //   1433: if_icmpne -> 1442
    //   1436: bipush #120
    //   1438: istore_1
    //   1439: goto -> 1546
    //   1442: goto -> 1522
    //   1445: aload_0
    //   1446: astore_2
    //   1447: aload_2
    //   1448: iconst_1
    //   1449: invokevirtual charAt : (I)C
    //   1452: istore_3
    //   1453: iload_3
    //   1454: bipush #102
    //   1456: if_icmpne -> 1478
    //   1459: aload_2
    //   1460: iconst_0
    //   1461: invokevirtual charAt : (I)C
    //   1464: bipush #105
    //   1466: if_icmpne -> 1475
    //   1469: bipush #113
    //   1471: istore_1
    //   1472: goto -> 1546
    //   1475: goto -> 1522
    //   1478: iload_3
    //   1479: bipush #110
    //   1481: if_icmpne -> 1500
    //   1484: aload_2
    //   1485: iconst_0
    //   1486: invokevirtual charAt : (I)C
    //   1489: bipush #105
    //   1491: if_icmpne -> 1522
    //   1494: bipush #52
    //   1496: istore_1
    //   1497: goto -> 1546
    //   1500: iload_3
    //   1501: bipush #111
    //   1503: if_icmpne -> 1522
    //   1506: aload_2
    //   1507: iconst_0
    //   1508: invokevirtual charAt : (I)C
    //   1511: bipush #100
    //   1513: if_icmpne -> 1522
    //   1516: bipush #119
    //   1518: istore_1
    //   1519: goto -> 1546
    //   1522: aconst_null
    //   1523: astore_2
    //   1524: aload_2
    //   1525: ifnull -> 1546
    //   1528: aload_2
    //   1529: aload_0
    //   1530: if_acmpeq -> 1546
    //   1533: aload_2
    //   1534: aload_0
    //   1535: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1538: ifne -> 1546
    //   1541: iconst_0
    //   1542: istore_1
    //   1543: goto -> 1546
    //   1546: iload_1
    //   1547: ifne -> 1552
    //   1550: iconst_0
    //   1551: ireturn
    //   1552: iload_1
    //   1553: sipush #255
    //   1556: iand
    //   1557: ireturn
  }
  
  private final String substring(int paramInt1, int paramInt2) {
    String str = this.sourceString;
    return (str != null) ? str.substring(paramInt1, paramInt2) : new String(this.sourceBuffer, paramInt1, paramInt2 - paramInt1);
  }
  
  private void ungetChar(int paramInt) {
    int i = this.ungetCursor;
    if (i != 0 && this.ungetBuffer[i - 1] == 10)
      Kit.codeBug(); 
    int[] arrayOfInt = this.ungetBuffer;
    i = this.ungetCursor;
    this.ungetCursor = i + 1;
    arrayOfInt[i] = paramInt;
    this.cursor--;
  }
  
  private void ungetCharIgnoreLineEnd(int paramInt) {
    int[] arrayOfInt = this.ungetBuffer;
    int i = this.ungetCursor;
    this.ungetCursor = i + 1;
    arrayOfInt[i] = paramInt;
    this.cursor--;
  }
  
  final boolean eof() {
    return this.hitEOF;
  }
  
  final String getAndResetCurrentComment() {
    if (this.sourceString != null) {
      if (isMarkingComment())
        Kit.codeBug(); 
      return this.sourceString.substring(this.tokenBeg, this.tokenEnd);
    } 
    if (!isMarkingComment())
      Kit.codeBug(); 
    StringBuilder stringBuilder = new StringBuilder(this.commentPrefix);
    stringBuilder.append(this.sourceBuffer, this.commentCursor, getTokenLength() - this.commentPrefix.length());
    this.commentCursor = -1;
    return stringBuilder.toString();
  }
  
  public Token.CommentType getCommentType() {
    return this.commentType;
  }
  
  public int getCursor() {
    return this.cursor;
  }
  
  int getFirstXMLToken() throws IOException {
    this.xmlOpenTagsCount = 0;
    this.xmlIsAttribute = false;
    this.xmlIsTagContent = false;
    if (!canUngetChar())
      return -1; 
    ungetChar(60);
    return getNextXMLToken();
  }
  
  final String getLine() {
    int i = this.sourceCursor;
    int j = this.lineEndChar;
    if (j >= 0) {
      int k = i - 1;
      i = k;
      if (j == 10) {
        i = k;
        if (charAt(k - 1) == 13)
          i = k - 1; 
      } 
    } else {
      for (i -= this.lineStart;; i++) {
        int k = charAt(this.lineStart + i);
        if (k == -1 || ScriptRuntime.isJSLineTerminator(k))
          break; 
      } 
      i = this.lineStart + i;
    } 
    return substring(this.lineStart, i);
  }
  
  final String getLine(int paramInt, int[] paramArrayOfint) {
    int m;
    int i = this.cursor + this.ungetCursor - paramInt;
    paramInt = this.sourceCursor;
    if (i > paramInt)
      return null; 
    int j = 0;
    int k;
    for (k = 0; i > 0; k = i4) {
      int i1 = charAt(paramInt - 1);
      m = i;
      int i2 = paramInt;
      int i3 = j;
      int i4 = k;
      if (ScriptRuntime.isJSLineTerminator(i1)) {
        m = i;
        j = paramInt;
        if (i1 == 10) {
          m = i;
          j = paramInt;
          if (charAt(paramInt - 2) == 13) {
            m = i - 1;
            j = paramInt - 1;
          } 
        } 
        i4 = k + 1;
        i3 = j - 1;
        i2 = j;
      } 
      i = m - 1;
      paramInt = i2 - 1;
      j = i3;
    } 
    int n = 0;
    i = 0;
    while (true) {
      m = n;
      if (paramInt > 0) {
        if (ScriptRuntime.isJSLineTerminator(charAt(paramInt - 1))) {
          m = paramInt;
          break;
        } 
        paramInt--;
        i++;
        continue;
      } 
      break;
    } 
    n = this.lineno;
    if (this.lineEndChar >= 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramArrayOfint[0] = n - k + paramInt;
    paramArrayOfint[1] = i;
    return (k == 0) ? getLine() : substring(m, j);
  }
  
  final int getLineno() {
    return this.lineno;
  }
  
  int getNextXMLToken() throws IOException {
    this.tokenBeg = this.cursor;
    this.stringBufferTop = 0;
    for (int i = getChar(); i != -1; i = getChar()) {
      if (this.xmlIsTagContent) {
        if (i != 9 && i != 10 && i != 13 && i != 32) {
          if (i != 34 && i != 39) {
            if (i != 47) {
              if (i != 123) {
                if (i != 61) {
                  if (i != 62) {
                    addToString(i);
                    this.xmlIsAttribute = false;
                  } else {
                    addToString(i);
                    this.xmlIsTagContent = false;
                    this.xmlIsAttribute = false;
                  } 
                } else {
                  addToString(i);
                  this.xmlIsAttribute = true;
                } 
              } else {
                ungetChar(i);
                this.string = getStringFromBuffer();
                return 146;
              } 
            } else {
              addToString(i);
              if (peekChar() == 62) {
                addToString(getChar());
                this.xmlIsTagContent = false;
                this.xmlOpenTagsCount--;
              } 
            } 
          } else {
            addToString(i);
            if (!readQuotedString(i))
              return -1; 
          } 
        } else {
          addToString(i);
        } 
        if (!this.xmlIsTagContent && this.xmlOpenTagsCount == 0) {
          this.string = getStringFromBuffer();
          return 149;
        } 
      } else if (i != 60) {
        if (i != 123) {
          addToString(i);
        } else {
          ungetChar(i);
          this.string = getStringFromBuffer();
          return 146;
        } 
      } else {
        addToString(i);
        i = peekChar();
        if (i != 33) {
          if (i != 47) {
            if (i != 63) {
              this.xmlIsTagContent = true;
              this.xmlOpenTagsCount++;
            } else {
              addToString(getChar());
              if (!readPI())
                return -1; 
            } 
          } else {
            addToString(getChar());
            i = this.xmlOpenTagsCount;
            if (i == 0) {
              this.stringBufferTop = 0;
              this.string = null;
              this.parser.addError("msg.XML.bad.form");
              return -1;
            } 
            this.xmlIsTagContent = true;
            this.xmlOpenTagsCount = i - 1;
          } 
        } else {
          addToString(getChar());
          i = peekChar();
          if (i != 45) {
            if (i != 91) {
              if (!readEntity())
                return -1; 
            } else {
              addToString(getChar());
              if (getChar() == 67 && getChar() == 68 && getChar() == 65 && getChar() == 84 && getChar() == 65 && getChar() == 91) {
                addToString(67);
                addToString(68);
                addToString(65);
                addToString(84);
                addToString(65);
                addToString(91);
                if (!readCDATA())
                  return -1; 
              } else {
                this.stringBufferTop = 0;
                this.string = null;
                this.parser.addError("msg.XML.bad.form");
                return -1;
              } 
            } 
          } else {
            addToString(getChar());
            i = getChar();
            if (i == 45) {
              addToString(i);
              if (!readXmlComment())
                return -1; 
            } else {
              this.stringBufferTop = 0;
              this.string = null;
              this.parser.addError("msg.XML.bad.form");
              return -1;
            } 
          } 
        } 
      } 
    } 
    this.tokenEnd = this.cursor;
    this.stringBufferTop = 0;
    this.string = null;
    this.parser.addError("msg.XML.bad.form");
    return -1;
  }
  
  final double getNumber() {
    return this.number;
  }
  
  final int getOffset() {
    int i = this.sourceCursor - this.lineStart;
    int j = i;
    if (this.lineEndChar >= 0)
      j = i - 1; 
    return j;
  }
  
  final char getQuoteChar() {
    return (char)this.quoteChar;
  }
  
  final String getSourceString() {
    return this.sourceString;
  }
  
  final String getString() {
    return this.string;
  }
  
  final int getToken() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial getChar : ()I
    //   4: istore_1
    //   5: iload_1
    //   6: iconst_m1
    //   7: if_icmpne -> 29
    //   10: aload_0
    //   11: getfield cursor : I
    //   14: istore_2
    //   15: aload_0
    //   16: iload_2
    //   17: iconst_1
    //   18: isub
    //   19: putfield tokenBeg : I
    //   22: aload_0
    //   23: iload_2
    //   24: putfield tokenEnd : I
    //   27: iconst_0
    //   28: ireturn
    //   29: iload_1
    //   30: bipush #10
    //   32: if_icmpne -> 59
    //   35: aload_0
    //   36: iconst_0
    //   37: putfield dirtyLine : Z
    //   40: aload_0
    //   41: getfield cursor : I
    //   44: istore_2
    //   45: aload_0
    //   46: iload_2
    //   47: iconst_1
    //   48: isub
    //   49: putfield tokenBeg : I
    //   52: aload_0
    //   53: iload_2
    //   54: putfield tokenEnd : I
    //   57: iconst_1
    //   58: ireturn
    //   59: iload_1
    //   60: invokestatic isJSSpace : (I)Z
    //   63: ifne -> 2638
    //   66: iload_1
    //   67: bipush #45
    //   69: if_icmpeq -> 77
    //   72: aload_0
    //   73: iconst_1
    //   74: putfield dirtyLine : Z
    //   77: aload_0
    //   78: getfield cursor : I
    //   81: istore_2
    //   82: aload_0
    //   83: iload_2
    //   84: iconst_1
    //   85: isub
    //   86: putfield tokenBeg : I
    //   89: aload_0
    //   90: iload_2
    //   91: putfield tokenEnd : I
    //   94: iload_1
    //   95: bipush #64
    //   97: if_icmpne -> 104
    //   100: sipush #148
    //   103: ireturn
    //   104: iconst_0
    //   105: istore_3
    //   106: iload_1
    //   107: bipush #92
    //   109: if_icmpne -> 156
    //   112: aload_0
    //   113: invokespecial getChar : ()I
    //   116: istore #4
    //   118: iload #4
    //   120: bipush #117
    //   122: if_icmpne -> 138
    //   125: iconst_1
    //   126: istore #5
    //   128: iconst_1
    //   129: istore_2
    //   130: aload_0
    //   131: iconst_0
    //   132: putfield stringBufferTop : I
    //   135: goto -> 196
    //   138: iconst_0
    //   139: istore #5
    //   141: aload_0
    //   142: iload #4
    //   144: invokespecial ungetChar : (I)V
    //   147: bipush #92
    //   149: istore #4
    //   151: iload_3
    //   152: istore_2
    //   153: goto -> 196
    //   156: iload_1
    //   157: i2c
    //   158: invokestatic isJavaIdentifierStart : (C)Z
    //   161: istore #6
    //   163: iload_1
    //   164: istore #4
    //   166: iload_3
    //   167: istore_2
    //   168: iload #6
    //   170: istore #5
    //   172: iload #6
    //   174: ifeq -> 196
    //   177: aload_0
    //   178: iconst_0
    //   179: putfield stringBufferTop : I
    //   182: aload_0
    //   183: iload_1
    //   184: invokespecial addToString : (I)V
    //   187: iload #6
    //   189: istore #5
    //   191: iload_3
    //   192: istore_2
    //   193: iload_1
    //   194: istore #4
    //   196: iload #5
    //   198: ifeq -> 570
    //   201: iload_2
    //   202: istore_3
    //   203: iload_2
    //   204: ifeq -> 270
    //   207: iconst_0
    //   208: istore_2
    //   209: iconst_0
    //   210: istore #4
    //   212: iload_2
    //   213: istore_1
    //   214: iload #4
    //   216: iconst_4
    //   217: if_icmpeq -> 244
    //   220: aload_0
    //   221: invokespecial getChar : ()I
    //   224: iload_2
    //   225: invokestatic xDigitToInt : (II)I
    //   228: istore_2
    //   229: iload_2
    //   230: ifge -> 238
    //   233: iload_2
    //   234: istore_1
    //   235: goto -> 244
    //   238: iinc #4, 1
    //   241: goto -> 212
    //   244: iload_1
    //   245: ifge -> 260
    //   248: aload_0
    //   249: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   252: ldc_w 'msg.invalid.escape'
    //   255: invokevirtual addError : (Ljava/lang/String;)V
    //   258: iconst_m1
    //   259: ireturn
    //   260: aload_0
    //   261: iload_1
    //   262: invokespecial addToString : (I)V
    //   265: iconst_0
    //   266: istore_2
    //   267: goto -> 203
    //   270: aload_0
    //   271: invokespecial getChar : ()I
    //   274: istore #4
    //   276: iload #4
    //   278: bipush #92
    //   280: if_icmpne -> 314
    //   283: aload_0
    //   284: invokespecial getChar : ()I
    //   287: istore_2
    //   288: iload_2
    //   289: bipush #117
    //   291: if_icmpne -> 301
    //   294: iconst_1
    //   295: istore_2
    //   296: iconst_1
    //   297: istore_3
    //   298: goto -> 203
    //   301: aload_0
    //   302: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   305: ldc_w 'msg.illegal.character'
    //   308: iload_2
    //   309: invokevirtual addError : (Ljava/lang/String;I)V
    //   312: iconst_m1
    //   313: ireturn
    //   314: iload #4
    //   316: iconst_m1
    //   317: if_icmpeq -> 348
    //   320: iload #4
    //   322: ldc 65279
    //   324: if_icmpeq -> 348
    //   327: iload #4
    //   329: i2c
    //   330: invokestatic isJavaIdentifierPart : (C)Z
    //   333: ifne -> 339
    //   336: goto -> 348
    //   339: aload_0
    //   340: iload #4
    //   342: invokespecial addToString : (I)V
    //   345: goto -> 203
    //   348: aload_0
    //   349: iload #4
    //   351: invokespecial ungetChar : (I)V
    //   354: aload_0
    //   355: invokespecial getStringFromBuffer : ()Ljava/lang/String;
    //   358: astore #7
    //   360: iload_3
    //   361: ifne -> 511
    //   364: aload #7
    //   366: aload_0
    //   367: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   370: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   373: invokevirtual getLanguageVersion : ()I
    //   376: aload_0
    //   377: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   380: invokevirtual inUseStrictDirective : ()Z
    //   383: invokestatic stringToKeyword : (Ljava/lang/String;IZ)I
    //   386: istore_3
    //   387: iload_3
    //   388: ifeq -> 547
    //   391: iload_3
    //   392: sipush #154
    //   395: if_icmpeq -> 406
    //   398: iload_3
    //   399: istore_2
    //   400: iload_3
    //   401: bipush #73
    //   403: if_icmpne -> 453
    //   406: iload_3
    //   407: istore_2
    //   408: aload_0
    //   409: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   412: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   415: invokevirtual getLanguageVersion : ()I
    //   418: sipush #170
    //   421: if_icmpge -> 453
    //   424: iload_3
    //   425: sipush #154
    //   428: if_icmpne -> 439
    //   431: ldc_w 'let'
    //   434: astore #8
    //   436: goto -> 444
    //   439: ldc_w 'yield'
    //   442: astore #8
    //   444: aload_0
    //   445: aload #8
    //   447: putfield string : Ljava/lang/String;
    //   450: bipush #39
    //   452: istore_2
    //   453: aload_0
    //   454: aload_0
    //   455: getfield allStrings : Lcom/trendmicro/hippo/ObjToIntMap;
    //   458: aload #7
    //   460: invokevirtual intern : (Ljava/lang/Object;)Ljava/lang/Object;
    //   463: checkcast java/lang/String
    //   466: putfield string : Ljava/lang/String;
    //   469: iload_2
    //   470: sipush #128
    //   473: if_icmpeq -> 478
    //   476: iload_2
    //   477: ireturn
    //   478: aload_0
    //   479: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   482: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   485: invokevirtual getLanguageVersion : ()I
    //   488: sipush #200
    //   491: if_icmplt -> 496
    //   494: iload_2
    //   495: ireturn
    //   496: aload_0
    //   497: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   500: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   503: invokevirtual isReservedKeywordAsIdentifier : ()Z
    //   506: ifne -> 547
    //   509: iload_2
    //   510: ireturn
    //   511: aload #7
    //   513: aload_0
    //   514: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   517: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   520: invokevirtual getLanguageVersion : ()I
    //   523: aload_0
    //   524: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   527: invokevirtual inUseStrictDirective : ()Z
    //   530: invokestatic isKeyword : (Ljava/lang/String;IZ)Z
    //   533: ifeq -> 547
    //   536: aload_0
    //   537: aload #7
    //   539: invokespecial convertLastCharToHex : (Ljava/lang/String;)Ljava/lang/String;
    //   542: astore #8
    //   544: goto -> 551
    //   547: aload #7
    //   549: astore #8
    //   551: aload_0
    //   552: aload_0
    //   553: getfield allStrings : Lcom/trendmicro/hippo/ObjToIntMap;
    //   556: aload #8
    //   558: invokevirtual intern : (Ljava/lang/Object;)Ljava/lang/Object;
    //   561: checkcast java/lang/String
    //   564: putfield string : Ljava/lang/String;
    //   567: bipush #39
    //   569: ireturn
    //   570: iload #4
    //   572: invokestatic isDigit : (I)Z
    //   575: ifne -> 1965
    //   578: iload #4
    //   580: bipush #46
    //   582: if_icmpne -> 598
    //   585: aload_0
    //   586: invokespecial peekChar : ()I
    //   589: invokestatic isDigit : (I)Z
    //   592: ifeq -> 598
    //   595: goto -> 1965
    //   598: iload #4
    //   600: bipush #34
    //   602: if_icmpeq -> 1492
    //   605: iload #4
    //   607: bipush #39
    //   609: if_icmpeq -> 1492
    //   612: iload #4
    //   614: bipush #96
    //   616: if_icmpne -> 622
    //   619: goto -> 1492
    //   622: iload #4
    //   624: bipush #33
    //   626: if_icmpeq -> 1465
    //   629: iload #4
    //   631: bipush #91
    //   633: if_icmpeq -> 1462
    //   636: iload #4
    //   638: bipush #37
    //   640: if_icmpeq -> 1447
    //   643: iload #4
    //   645: bipush #38
    //   647: if_icmpeq -> 1420
    //   650: iload #4
    //   652: bipush #93
    //   654: if_icmpeq -> 1417
    //   657: iload #4
    //   659: bipush #94
    //   661: if_icmpeq -> 1402
    //   664: iload #4
    //   666: tableswitch default -> 712, 40 -> 1399, 41 -> 1396, 42 -> 1381, 43 -> 1354, 44 -> 1351, 45 -> 1273, 46 -> 1244, 47 -> 1062
    //   712: iload #4
    //   714: tableswitch default -> 752, 58 -> 1046, 59 -> 1043, 60 -> 940, 61 -> 900, 62 -> 837, 63 -> 834
    //   752: iload #4
    //   754: tableswitch default -> 784, 123 -> 831, 124 -> 804, 125 -> 801, 126 -> 798
    //   784: aload_0
    //   785: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   788: ldc_w 'msg.illegal.character'
    //   791: iload #4
    //   793: invokevirtual addError : (Ljava/lang/String;I)V
    //   796: iconst_m1
    //   797: ireturn
    //   798: bipush #27
    //   800: ireturn
    //   801: bipush #87
    //   803: ireturn
    //   804: aload_0
    //   805: bipush #124
    //   807: invokespecial matchChar : (I)Z
    //   810: ifeq -> 816
    //   813: bipush #105
    //   815: ireturn
    //   816: aload_0
    //   817: bipush #61
    //   819: invokespecial matchChar : (I)Z
    //   822: ifeq -> 828
    //   825: bipush #92
    //   827: ireturn
    //   828: bipush #9
    //   830: ireturn
    //   831: bipush #86
    //   833: ireturn
    //   834: bipush #103
    //   836: ireturn
    //   837: aload_0
    //   838: bipush #62
    //   840: invokespecial matchChar : (I)Z
    //   843: ifeq -> 885
    //   846: aload_0
    //   847: bipush #62
    //   849: invokespecial matchChar : (I)Z
    //   852: ifeq -> 870
    //   855: aload_0
    //   856: bipush #61
    //   858: invokespecial matchChar : (I)Z
    //   861: ifeq -> 867
    //   864: bipush #97
    //   866: ireturn
    //   867: bipush #20
    //   869: ireturn
    //   870: aload_0
    //   871: bipush #61
    //   873: invokespecial matchChar : (I)Z
    //   876: ifeq -> 882
    //   879: bipush #96
    //   881: ireturn
    //   882: bipush #19
    //   884: ireturn
    //   885: aload_0
    //   886: bipush #61
    //   888: invokespecial matchChar : (I)Z
    //   891: ifeq -> 897
    //   894: bipush #17
    //   896: ireturn
    //   897: bipush #16
    //   899: ireturn
    //   900: aload_0
    //   901: bipush #61
    //   903: invokespecial matchChar : (I)Z
    //   906: ifeq -> 924
    //   909: aload_0
    //   910: bipush #61
    //   912: invokespecial matchChar : (I)Z
    //   915: ifeq -> 921
    //   918: bipush #46
    //   920: ireturn
    //   921: bipush #12
    //   923: ireturn
    //   924: aload_0
    //   925: bipush #62
    //   927: invokespecial matchChar : (I)Z
    //   930: ifeq -> 937
    //   933: sipush #165
    //   936: ireturn
    //   937: bipush #91
    //   939: ireturn
    //   940: aload_0
    //   941: bipush #33
    //   943: invokespecial matchChar : (I)Z
    //   946: ifeq -> 1004
    //   949: aload_0
    //   950: bipush #45
    //   952: invokespecial matchChar : (I)Z
    //   955: ifeq -> 998
    //   958: aload_0
    //   959: bipush #45
    //   961: invokespecial matchChar : (I)Z
    //   964: ifeq -> 992
    //   967: aload_0
    //   968: aload_0
    //   969: getfield cursor : I
    //   972: iconst_4
    //   973: isub
    //   974: putfield tokenBeg : I
    //   977: aload_0
    //   978: invokespecial skipLine : ()V
    //   981: aload_0
    //   982: getstatic com/trendmicro/hippo/Token$CommentType.HTML : Lcom/trendmicro/hippo/Token$CommentType;
    //   985: putfield commentType : Lcom/trendmicro/hippo/Token$CommentType;
    //   988: sipush #162
    //   991: ireturn
    //   992: aload_0
    //   993: bipush #45
    //   995: invokespecial ungetCharIgnoreLineEnd : (I)V
    //   998: aload_0
    //   999: bipush #33
    //   1001: invokespecial ungetCharIgnoreLineEnd : (I)V
    //   1004: aload_0
    //   1005: bipush #60
    //   1007: invokespecial matchChar : (I)Z
    //   1010: ifeq -> 1028
    //   1013: aload_0
    //   1014: bipush #61
    //   1016: invokespecial matchChar : (I)Z
    //   1019: ifeq -> 1025
    //   1022: bipush #95
    //   1024: ireturn
    //   1025: bipush #18
    //   1027: ireturn
    //   1028: aload_0
    //   1029: bipush #61
    //   1031: invokespecial matchChar : (I)Z
    //   1034: ifeq -> 1040
    //   1037: bipush #15
    //   1039: ireturn
    //   1040: bipush #14
    //   1042: ireturn
    //   1043: bipush #83
    //   1045: ireturn
    //   1046: aload_0
    //   1047: bipush #58
    //   1049: invokespecial matchChar : (I)Z
    //   1052: ifeq -> 1059
    //   1055: sipush #145
    //   1058: ireturn
    //   1059: bipush #104
    //   1061: ireturn
    //   1062: aload_0
    //   1063: invokespecial markCommentStart : ()V
    //   1066: aload_0
    //   1067: bipush #47
    //   1069: invokespecial matchChar : (I)Z
    //   1072: ifeq -> 1100
    //   1075: aload_0
    //   1076: aload_0
    //   1077: getfield cursor : I
    //   1080: iconst_2
    //   1081: isub
    //   1082: putfield tokenBeg : I
    //   1085: aload_0
    //   1086: invokespecial skipLine : ()V
    //   1089: aload_0
    //   1090: getstatic com/trendmicro/hippo/Token$CommentType.LINE : Lcom/trendmicro/hippo/Token$CommentType;
    //   1093: putfield commentType : Lcom/trendmicro/hippo/Token$CommentType;
    //   1096: sipush #162
    //   1099: ireturn
    //   1100: aload_0
    //   1101: bipush #42
    //   1103: invokespecial matchChar : (I)Z
    //   1106: ifeq -> 1229
    //   1109: iconst_0
    //   1110: istore_2
    //   1111: aload_0
    //   1112: aload_0
    //   1113: getfield cursor : I
    //   1116: iconst_2
    //   1117: isub
    //   1118: putfield tokenBeg : I
    //   1121: aload_0
    //   1122: bipush #42
    //   1124: invokespecial matchChar : (I)Z
    //   1127: ifeq -> 1142
    //   1130: iconst_1
    //   1131: istore_2
    //   1132: aload_0
    //   1133: getstatic com/trendmicro/hippo/Token$CommentType.JSDOC : Lcom/trendmicro/hippo/Token$CommentType;
    //   1136: putfield commentType : Lcom/trendmicro/hippo/Token$CommentType;
    //   1139: goto -> 1149
    //   1142: aload_0
    //   1143: getstatic com/trendmicro/hippo/Token$CommentType.BLOCK_COMMENT : Lcom/trendmicro/hippo/Token$CommentType;
    //   1146: putfield commentType : Lcom/trendmicro/hippo/Token$CommentType;
    //   1149: aload_0
    //   1150: invokespecial getChar : ()I
    //   1153: istore_3
    //   1154: iload_3
    //   1155: iconst_m1
    //   1156: if_icmpne -> 1183
    //   1159: aload_0
    //   1160: aload_0
    //   1161: getfield cursor : I
    //   1164: iconst_1
    //   1165: isub
    //   1166: putfield tokenEnd : I
    //   1169: aload_0
    //   1170: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   1173: ldc_w 'msg.unterminated.comment'
    //   1176: invokevirtual addError : (Ljava/lang/String;)V
    //   1179: sipush #162
    //   1182: ireturn
    //   1183: iload_3
    //   1184: bipush #42
    //   1186: if_icmpne -> 1194
    //   1189: iconst_1
    //   1190: istore_2
    //   1191: goto -> 1149
    //   1194: iload_3
    //   1195: bipush #47
    //   1197: if_icmpne -> 1216
    //   1200: iload_2
    //   1201: ifeq -> 1149
    //   1204: aload_0
    //   1205: aload_0
    //   1206: getfield cursor : I
    //   1209: putfield tokenEnd : I
    //   1212: sipush #162
    //   1215: ireturn
    //   1216: iconst_0
    //   1217: istore_2
    //   1218: aload_0
    //   1219: aload_0
    //   1220: getfield cursor : I
    //   1223: putfield tokenEnd : I
    //   1226: goto -> 1149
    //   1229: aload_0
    //   1230: bipush #61
    //   1232: invokespecial matchChar : (I)Z
    //   1235: ifeq -> 1241
    //   1238: bipush #101
    //   1240: ireturn
    //   1241: bipush #24
    //   1243: ireturn
    //   1244: aload_0
    //   1245: bipush #46
    //   1247: invokespecial matchChar : (I)Z
    //   1250: ifeq -> 1257
    //   1253: sipush #144
    //   1256: ireturn
    //   1257: aload_0
    //   1258: bipush #40
    //   1260: invokespecial matchChar : (I)Z
    //   1263: ifeq -> 1270
    //   1266: sipush #147
    //   1269: ireturn
    //   1270: bipush #109
    //   1272: ireturn
    //   1273: aload_0
    //   1274: bipush #61
    //   1276: invokespecial matchChar : (I)Z
    //   1279: ifeq -> 1288
    //   1282: bipush #99
    //   1284: istore_2
    //   1285: goto -> 1344
    //   1288: aload_0
    //   1289: bipush #45
    //   1291: invokespecial matchChar : (I)Z
    //   1294: ifeq -> 1341
    //   1297: aload_0
    //   1298: getfield dirtyLine : Z
    //   1301: ifne -> 1335
    //   1304: aload_0
    //   1305: bipush #62
    //   1307: invokespecial matchChar : (I)Z
    //   1310: ifeq -> 1335
    //   1313: aload_0
    //   1314: ldc_w '--'
    //   1317: invokespecial markCommentStart : (Ljava/lang/String;)V
    //   1320: aload_0
    //   1321: invokespecial skipLine : ()V
    //   1324: aload_0
    //   1325: getstatic com/trendmicro/hippo/Token$CommentType.HTML : Lcom/trendmicro/hippo/Token$CommentType;
    //   1328: putfield commentType : Lcom/trendmicro/hippo/Token$CommentType;
    //   1331: sipush #162
    //   1334: ireturn
    //   1335: bipush #108
    //   1337: istore_2
    //   1338: goto -> 1344
    //   1341: bipush #22
    //   1343: istore_2
    //   1344: aload_0
    //   1345: iconst_1
    //   1346: putfield dirtyLine : Z
    //   1349: iload_2
    //   1350: ireturn
    //   1351: bipush #90
    //   1353: ireturn
    //   1354: aload_0
    //   1355: bipush #61
    //   1357: invokespecial matchChar : (I)Z
    //   1360: ifeq -> 1366
    //   1363: bipush #98
    //   1365: ireturn
    //   1366: aload_0
    //   1367: bipush #43
    //   1369: invokespecial matchChar : (I)Z
    //   1372: ifeq -> 1378
    //   1375: bipush #107
    //   1377: ireturn
    //   1378: bipush #21
    //   1380: ireturn
    //   1381: aload_0
    //   1382: bipush #61
    //   1384: invokespecial matchChar : (I)Z
    //   1387: ifeq -> 1393
    //   1390: bipush #100
    //   1392: ireturn
    //   1393: bipush #23
    //   1395: ireturn
    //   1396: bipush #89
    //   1398: ireturn
    //   1399: bipush #88
    //   1401: ireturn
    //   1402: aload_0
    //   1403: bipush #61
    //   1405: invokespecial matchChar : (I)Z
    //   1408: ifeq -> 1414
    //   1411: bipush #93
    //   1413: ireturn
    //   1414: bipush #10
    //   1416: ireturn
    //   1417: bipush #85
    //   1419: ireturn
    //   1420: aload_0
    //   1421: bipush #38
    //   1423: invokespecial matchChar : (I)Z
    //   1426: ifeq -> 1432
    //   1429: bipush #106
    //   1431: ireturn
    //   1432: aload_0
    //   1433: bipush #61
    //   1435: invokespecial matchChar : (I)Z
    //   1438: ifeq -> 1444
    //   1441: bipush #94
    //   1443: ireturn
    //   1444: bipush #11
    //   1446: ireturn
    //   1447: aload_0
    //   1448: bipush #61
    //   1450: invokespecial matchChar : (I)Z
    //   1453: ifeq -> 1459
    //   1456: bipush #102
    //   1458: ireturn
    //   1459: bipush #25
    //   1461: ireturn
    //   1462: bipush #84
    //   1464: ireturn
    //   1465: aload_0
    //   1466: bipush #61
    //   1468: invokespecial matchChar : (I)Z
    //   1471: ifeq -> 1489
    //   1474: aload_0
    //   1475: bipush #61
    //   1477: invokespecial matchChar : (I)Z
    //   1480: ifeq -> 1486
    //   1483: bipush #47
    //   1485: ireturn
    //   1486: bipush #13
    //   1488: ireturn
    //   1489: bipush #26
    //   1491: ireturn
    //   1492: aload_0
    //   1493: iload #4
    //   1495: putfield quoteChar : I
    //   1498: aload_0
    //   1499: iconst_0
    //   1500: putfield stringBufferTop : I
    //   1503: aload_0
    //   1504: iconst_0
    //   1505: invokespecial getChar : (Z)I
    //   1508: istore_2
    //   1509: iload_2
    //   1510: aload_0
    //   1511: getfield quoteChar : I
    //   1514: if_icmpeq -> 1940
    //   1517: iload_2
    //   1518: bipush #10
    //   1520: if_icmpeq -> 1915
    //   1523: iload_2
    //   1524: iconst_m1
    //   1525: if_icmpne -> 1531
    //   1528: goto -> 1915
    //   1531: iload_2
    //   1532: bipush #92
    //   1534: if_icmpne -> 1901
    //   1537: aload_0
    //   1538: invokespecial getChar : ()I
    //   1541: istore_2
    //   1542: iload_2
    //   1543: bipush #10
    //   1545: if_icmpeq -> 1893
    //   1548: iload_2
    //   1549: bipush #98
    //   1551: if_icmpeq -> 1887
    //   1554: iload_2
    //   1555: bipush #102
    //   1557: if_icmpeq -> 1881
    //   1560: iload_2
    //   1561: bipush #110
    //   1563: if_icmpeq -> 1875
    //   1566: iload_2
    //   1567: bipush #114
    //   1569: if_icmpeq -> 1869
    //   1572: iload_2
    //   1573: bipush #120
    //   1575: if_icmpeq -> 1803
    //   1578: iload_2
    //   1579: tableswitch default -> 1604, 116 -> 1797, 117 -> 1734, 118 -> 1728
    //   1604: bipush #48
    //   1606: iload_2
    //   1607: if_icmpgt -> 1725
    //   1610: iload_2
    //   1611: bipush #56
    //   1613: if_icmpge -> 1725
    //   1616: iload_2
    //   1617: bipush #48
    //   1619: isub
    //   1620: istore #4
    //   1622: aload_0
    //   1623: invokespecial getChar : ()I
    //   1626: istore_1
    //   1627: iload_1
    //   1628: istore_3
    //   1629: iload #4
    //   1631: istore_2
    //   1632: bipush #48
    //   1634: iload_1
    //   1635: if_icmpgt -> 1717
    //   1638: iload_1
    //   1639: istore_3
    //   1640: iload #4
    //   1642: istore_2
    //   1643: iload_1
    //   1644: bipush #56
    //   1646: if_icmpge -> 1717
    //   1649: iload #4
    //   1651: bipush #8
    //   1653: imul
    //   1654: iload_1
    //   1655: iadd
    //   1656: bipush #48
    //   1658: isub
    //   1659: istore_1
    //   1660: aload_0
    //   1661: invokespecial getChar : ()I
    //   1664: istore #4
    //   1666: iload #4
    //   1668: istore_3
    //   1669: iload_1
    //   1670: istore_2
    //   1671: bipush #48
    //   1673: iload #4
    //   1675: if_icmpgt -> 1717
    //   1678: iload #4
    //   1680: istore_3
    //   1681: iload_1
    //   1682: istore_2
    //   1683: iload #4
    //   1685: bipush #56
    //   1687: if_icmpge -> 1717
    //   1690: iload #4
    //   1692: istore_3
    //   1693: iload_1
    //   1694: istore_2
    //   1695: iload_1
    //   1696: bipush #31
    //   1698: if_icmpgt -> 1717
    //   1701: iload_1
    //   1702: bipush #8
    //   1704: imul
    //   1705: iload #4
    //   1707: iadd
    //   1708: bipush #48
    //   1710: isub
    //   1711: istore_2
    //   1712: aload_0
    //   1713: invokespecial getChar : ()I
    //   1716: istore_3
    //   1717: aload_0
    //   1718: iload_3
    //   1719: invokespecial ungetChar : (I)V
    //   1722: goto -> 1901
    //   1725: goto -> 1901
    //   1728: bipush #11
    //   1730: istore_2
    //   1731: goto -> 1901
    //   1734: aload_0
    //   1735: getfield stringBufferTop : I
    //   1738: istore_1
    //   1739: aload_0
    //   1740: bipush #117
    //   1742: invokespecial addToString : (I)V
    //   1745: iconst_0
    //   1746: istore_2
    //   1747: iconst_0
    //   1748: istore_3
    //   1749: iload_3
    //   1750: iconst_4
    //   1751: if_icmpeq -> 1789
    //   1754: aload_0
    //   1755: invokespecial getChar : ()I
    //   1758: istore #4
    //   1760: iload #4
    //   1762: iload_2
    //   1763: invokestatic xDigitToInt : (II)I
    //   1766: istore_2
    //   1767: iload_2
    //   1768: ifge -> 1777
    //   1771: iload #4
    //   1773: istore_2
    //   1774: goto -> 1509
    //   1777: aload_0
    //   1778: iload #4
    //   1780: invokespecial addToString : (I)V
    //   1783: iinc #3, 1
    //   1786: goto -> 1749
    //   1789: aload_0
    //   1790: iload_1
    //   1791: putfield stringBufferTop : I
    //   1794: goto -> 1901
    //   1797: bipush #9
    //   1799: istore_2
    //   1800: goto -> 1901
    //   1803: aload_0
    //   1804: invokespecial getChar : ()I
    //   1807: istore_2
    //   1808: iload_2
    //   1809: iconst_0
    //   1810: invokestatic xDigitToInt : (II)I
    //   1813: istore #4
    //   1815: iload #4
    //   1817: ifge -> 1829
    //   1820: aload_0
    //   1821: bipush #120
    //   1823: invokespecial addToString : (I)V
    //   1826: goto -> 1509
    //   1829: aload_0
    //   1830: invokespecial getChar : ()I
    //   1833: istore_3
    //   1834: iload_3
    //   1835: iload #4
    //   1837: invokestatic xDigitToInt : (II)I
    //   1840: istore #4
    //   1842: iload #4
    //   1844: ifge -> 1863
    //   1847: aload_0
    //   1848: bipush #120
    //   1850: invokespecial addToString : (I)V
    //   1853: aload_0
    //   1854: iload_2
    //   1855: invokespecial addToString : (I)V
    //   1858: iload_3
    //   1859: istore_2
    //   1860: goto -> 1509
    //   1863: iload #4
    //   1865: istore_2
    //   1866: goto -> 1901
    //   1869: bipush #13
    //   1871: istore_2
    //   1872: goto -> 1901
    //   1875: bipush #10
    //   1877: istore_2
    //   1878: goto -> 1901
    //   1881: bipush #12
    //   1883: istore_2
    //   1884: goto -> 1901
    //   1887: bipush #8
    //   1889: istore_2
    //   1890: goto -> 1901
    //   1893: aload_0
    //   1894: invokespecial getChar : ()I
    //   1897: istore_2
    //   1898: goto -> 1509
    //   1901: aload_0
    //   1902: iload_2
    //   1903: invokespecial addToString : (I)V
    //   1906: aload_0
    //   1907: iconst_0
    //   1908: invokespecial getChar : (Z)I
    //   1911: istore_2
    //   1912: goto -> 1509
    //   1915: aload_0
    //   1916: iload_2
    //   1917: invokespecial ungetChar : (I)V
    //   1920: aload_0
    //   1921: aload_0
    //   1922: getfield cursor : I
    //   1925: putfield tokenEnd : I
    //   1928: aload_0
    //   1929: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   1932: ldc_w 'msg.unterminated.string.lit'
    //   1935: invokevirtual addError : (Ljava/lang/String;)V
    //   1938: iconst_m1
    //   1939: ireturn
    //   1940: aload_0
    //   1941: invokespecial getStringFromBuffer : ()Ljava/lang/String;
    //   1944: astore #8
    //   1946: aload_0
    //   1947: aload_0
    //   1948: getfield allStrings : Lcom/trendmicro/hippo/ObjToIntMap;
    //   1951: aload #8
    //   1953: invokevirtual intern : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1956: checkcast java/lang/String
    //   1959: putfield string : Ljava/lang/String;
    //   1962: bipush #41
    //   1964: ireturn
    //   1965: aload_0
    //   1966: iconst_0
    //   1967: putfield stringBufferTop : I
    //   1970: bipush #10
    //   1972: istore #9
    //   1974: aload_0
    //   1975: iconst_0
    //   1976: putfield isBinary : Z
    //   1979: aload_0
    //   1980: iconst_0
    //   1981: putfield isOctal : Z
    //   1984: aload_0
    //   1985: iconst_0
    //   1986: putfield isOldOctal : Z
    //   1989: aload_0
    //   1990: iconst_0
    //   1991: putfield isHex : Z
    //   1994: aload_0
    //   1995: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   1998: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   2001: invokevirtual getLanguageVersion : ()I
    //   2004: sipush #200
    //   2007: if_icmplt -> 2015
    //   2010: iconst_1
    //   2011: istore_1
    //   2012: goto -> 2017
    //   2015: iconst_0
    //   2016: istore_1
    //   2017: iload #4
    //   2019: istore_3
    //   2020: iload #9
    //   2022: istore_2
    //   2023: iload #4
    //   2025: bipush #48
    //   2027: if_icmpne -> 2156
    //   2030: aload_0
    //   2031: invokespecial getChar : ()I
    //   2034: istore_3
    //   2035: iload_3
    //   2036: bipush #120
    //   2038: if_icmpeq -> 2143
    //   2041: iload_3
    //   2042: bipush #88
    //   2044: if_icmpne -> 2050
    //   2047: goto -> 2143
    //   2050: iload_1
    //   2051: ifeq -> 2082
    //   2054: iload_3
    //   2055: bipush #111
    //   2057: if_icmpeq -> 2066
    //   2060: iload_3
    //   2061: bipush #79
    //   2063: if_icmpne -> 2082
    //   2066: bipush #8
    //   2068: istore_2
    //   2069: aload_0
    //   2070: iconst_1
    //   2071: putfield isOctal : Z
    //   2074: aload_0
    //   2075: invokespecial getChar : ()I
    //   2078: istore_3
    //   2079: goto -> 2156
    //   2082: iload_1
    //   2083: ifeq -> 2113
    //   2086: iload_3
    //   2087: bipush #98
    //   2089: if_icmpeq -> 2098
    //   2092: iload_3
    //   2093: bipush #66
    //   2095: if_icmpne -> 2113
    //   2098: iconst_2
    //   2099: istore_2
    //   2100: aload_0
    //   2101: iconst_1
    //   2102: putfield isBinary : Z
    //   2105: aload_0
    //   2106: invokespecial getChar : ()I
    //   2109: istore_3
    //   2110: goto -> 2156
    //   2113: iload_3
    //   2114: invokestatic isDigit : (I)Z
    //   2117: ifeq -> 2131
    //   2120: bipush #8
    //   2122: istore_2
    //   2123: aload_0
    //   2124: iconst_1
    //   2125: putfield isOldOctal : Z
    //   2128: goto -> 2156
    //   2131: aload_0
    //   2132: bipush #48
    //   2134: invokespecial addToString : (I)V
    //   2137: iload #9
    //   2139: istore_2
    //   2140: goto -> 2156
    //   2143: bipush #16
    //   2145: istore_2
    //   2146: aload_0
    //   2147: iconst_1
    //   2148: putfield isHex : Z
    //   2151: aload_0
    //   2152: invokespecial getChar : ()I
    //   2155: istore_3
    //   2156: iconst_1
    //   2157: istore #9
    //   2159: iconst_1
    //   2160: istore #10
    //   2162: iload_3
    //   2163: istore #4
    //   2165: iload_2
    //   2166: istore_1
    //   2167: iload_2
    //   2168: bipush #16
    //   2170: if_icmpne -> 2214
    //   2173: iload #10
    //   2175: istore_1
    //   2176: iload_3
    //   2177: istore #4
    //   2179: iload #4
    //   2181: istore_3
    //   2182: iload_2
    //   2183: istore #10
    //   2185: iload_1
    //   2186: istore #11
    //   2188: iload #4
    //   2190: iconst_0
    //   2191: invokestatic xDigitToInt : (II)I
    //   2194: iflt -> 2370
    //   2197: aload_0
    //   2198: iload #4
    //   2200: invokespecial addToString : (I)V
    //   2203: aload_0
    //   2204: invokespecial getChar : ()I
    //   2207: istore #4
    //   2209: iconst_0
    //   2210: istore_1
    //   2211: goto -> 2179
    //   2214: iload #4
    //   2216: istore_3
    //   2217: iload_1
    //   2218: istore #10
    //   2220: iload #9
    //   2222: istore #11
    //   2224: bipush #48
    //   2226: iload #4
    //   2228: if_icmpgt -> 2370
    //   2231: iload #4
    //   2233: istore_3
    //   2234: iload_1
    //   2235: istore #10
    //   2237: iload #9
    //   2239: istore #11
    //   2241: iload #4
    //   2243: bipush #57
    //   2245: if_icmpgt -> 2370
    //   2248: iload_1
    //   2249: bipush #8
    //   2251: if_icmpne -> 2322
    //   2254: iload #4
    //   2256: bipush #56
    //   2258: if_icmplt -> 2322
    //   2261: aload_0
    //   2262: getfield isOldOctal : Z
    //   2265: ifeq -> 2310
    //   2268: aload_0
    //   2269: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   2272: astore #7
    //   2274: iload #4
    //   2276: bipush #56
    //   2278: if_icmpne -> 2289
    //   2281: ldc_w '8'
    //   2284: astore #8
    //   2286: goto -> 2294
    //   2289: ldc_w '9'
    //   2292: astore #8
    //   2294: aload #7
    //   2296: ldc_w 'msg.bad.octal.literal'
    //   2299: aload #8
    //   2301: invokevirtual addWarning : (Ljava/lang/String;Ljava/lang/String;)V
    //   2304: bipush #10
    //   2306: istore_2
    //   2307: goto -> 2350
    //   2310: aload_0
    //   2311: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   2314: ldc_w 'msg.caught.nfe'
    //   2317: invokevirtual addError : (Ljava/lang/String;)V
    //   2320: iconst_m1
    //   2321: ireturn
    //   2322: iload_1
    //   2323: istore_2
    //   2324: iload_1
    //   2325: iconst_2
    //   2326: if_icmpne -> 2350
    //   2329: iload_1
    //   2330: istore_2
    //   2331: iload #4
    //   2333: bipush #50
    //   2335: if_icmplt -> 2350
    //   2338: aload_0
    //   2339: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   2342: ldc_w 'msg.caught.nfe'
    //   2345: invokevirtual addError : (Ljava/lang/String;)V
    //   2348: iconst_m1
    //   2349: ireturn
    //   2350: aload_0
    //   2351: iload #4
    //   2353: invokespecial addToString : (I)V
    //   2356: aload_0
    //   2357: invokespecial getChar : ()I
    //   2360: istore #4
    //   2362: iconst_0
    //   2363: istore #9
    //   2365: iload_2
    //   2366: istore_1
    //   2367: goto -> 2214
    //   2370: iload #11
    //   2372: ifeq -> 2408
    //   2375: aload_0
    //   2376: getfield isBinary : Z
    //   2379: ifne -> 2396
    //   2382: aload_0
    //   2383: getfield isOctal : Z
    //   2386: ifne -> 2396
    //   2389: aload_0
    //   2390: getfield isHex : Z
    //   2393: ifeq -> 2408
    //   2396: aload_0
    //   2397: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   2400: ldc_w 'msg.caught.nfe'
    //   2403: invokevirtual addError : (Ljava/lang/String;)V
    //   2406: iconst_m1
    //   2407: ireturn
    //   2408: iconst_1
    //   2409: istore #4
    //   2411: iload #10
    //   2413: bipush #10
    //   2415: if_icmpne -> 2562
    //   2418: iload_3
    //   2419: bipush #46
    //   2421: if_icmpeq -> 2436
    //   2424: iload_3
    //   2425: bipush #101
    //   2427: if_icmpeq -> 2436
    //   2430: iload_3
    //   2431: bipush #69
    //   2433: if_icmpne -> 2562
    //   2436: iload_3
    //   2437: istore_2
    //   2438: iload_3
    //   2439: bipush #46
    //   2441: if_icmpne -> 2463
    //   2444: aload_0
    //   2445: iload_3
    //   2446: invokespecial addToString : (I)V
    //   2449: aload_0
    //   2450: invokespecial getChar : ()I
    //   2453: istore_2
    //   2454: iload_2
    //   2455: istore_3
    //   2456: iload_2
    //   2457: invokestatic isDigit : (I)Z
    //   2460: ifne -> 2444
    //   2463: iload_2
    //   2464: bipush #101
    //   2466: if_icmpeq -> 2483
    //   2469: iload_2
    //   2470: bipush #69
    //   2472: if_icmpne -> 2478
    //   2475: goto -> 2483
    //   2478: iconst_0
    //   2479: istore_3
    //   2480: goto -> 2567
    //   2483: aload_0
    //   2484: iload_2
    //   2485: invokespecial addToString : (I)V
    //   2488: aload_0
    //   2489: invokespecial getChar : ()I
    //   2492: istore_3
    //   2493: iload_3
    //   2494: bipush #43
    //   2496: if_icmpeq -> 2507
    //   2499: iload_3
    //   2500: istore_2
    //   2501: iload_3
    //   2502: bipush #45
    //   2504: if_icmpne -> 2517
    //   2507: aload_0
    //   2508: iload_3
    //   2509: invokespecial addToString : (I)V
    //   2512: aload_0
    //   2513: invokespecial getChar : ()I
    //   2516: istore_2
    //   2517: iload_2
    //   2518: istore_3
    //   2519: iload_2
    //   2520: invokestatic isDigit : (I)Z
    //   2523: ifne -> 2538
    //   2526: aload_0
    //   2527: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   2530: ldc_w 'msg.missing.exponent'
    //   2533: invokevirtual addError : (Ljava/lang/String;)V
    //   2536: iconst_m1
    //   2537: ireturn
    //   2538: aload_0
    //   2539: iload_3
    //   2540: invokespecial addToString : (I)V
    //   2543: aload_0
    //   2544: invokespecial getChar : ()I
    //   2547: istore_2
    //   2548: iload_2
    //   2549: istore_3
    //   2550: iload_2
    //   2551: invokestatic isDigit : (I)Z
    //   2554: ifne -> 2538
    //   2557: iconst_0
    //   2558: istore_3
    //   2559: goto -> 2567
    //   2562: iload_3
    //   2563: istore_2
    //   2564: iload #4
    //   2566: istore_3
    //   2567: aload_0
    //   2568: iload_2
    //   2569: invokespecial ungetChar : (I)V
    //   2572: aload_0
    //   2573: invokespecial getStringFromBuffer : ()Ljava/lang/String;
    //   2576: astore #8
    //   2578: aload_0
    //   2579: aload #8
    //   2581: putfield string : Ljava/lang/String;
    //   2584: iload #10
    //   2586: bipush #10
    //   2588: if_icmpne -> 2619
    //   2591: iload_3
    //   2592: ifne -> 2619
    //   2595: aload #8
    //   2597: invokestatic parseDouble : (Ljava/lang/String;)D
    //   2600: dstore #12
    //   2602: goto -> 2629
    //   2605: astore #8
    //   2607: aload_0
    //   2608: getfield parser : Lcom/trendmicro/hippo/Parser;
    //   2611: ldc_w 'msg.caught.nfe'
    //   2614: invokevirtual addError : (Ljava/lang/String;)V
    //   2617: iconst_m1
    //   2618: ireturn
    //   2619: aload #8
    //   2621: iconst_0
    //   2622: iload #10
    //   2624: invokestatic stringPrefixToNumber : (Ljava/lang/String;II)D
    //   2627: dstore #12
    //   2629: aload_0
    //   2630: dload #12
    //   2632: putfield number : D
    //   2635: bipush #40
    //   2637: ireturn
    //   2638: goto -> 0
    // Exception table:
    //   from	to	target	type
    //   2595	2602	2605	java/lang/NumberFormatException
  }
  
  public int getTokenBeg() {
    return this.tokenBeg;
  }
  
  public int getTokenEnd() {
    return this.tokenEnd;
  }
  
  public int getTokenLength() {
    return this.tokenEnd - this.tokenBeg;
  }
  
  final boolean isNumberBinary() {
    return this.isBinary;
  }
  
  final boolean isNumberHex() {
    return this.isHex;
  }
  
  final boolean isNumberOctal() {
    return this.isOctal;
  }
  
  final boolean isNumberOldOctal() {
    return this.isOldOctal;
  }
  
  boolean isXMLAttribute() {
    return this.xmlIsAttribute;
  }
  
  String readAndClearRegExpFlags() {
    String str = this.regExpFlags;
    this.regExpFlags = null;
    return str;
  }
  
  void readRegExp(int paramInt) throws IOException {
    int j;
    int i = this.tokenBeg;
    this.stringBufferTop = 0;
    if (paramInt == 101) {
      addToString(61);
    } else if (paramInt != 24) {
      Kit.codeBug();
    } 
    paramInt = 0;
    while (true) {
      int k = getChar();
      j = k;
      if (k != 47 || paramInt != 0) {
        if (j == 10 || j == -1)
          break; 
        if (j == 92) {
          addToString(j);
          k = getChar();
        } else if (j == 91) {
          paramInt = 1;
          k = j;
        } else {
          k = j;
          if (j == 93) {
            paramInt = 0;
            k = j;
          } 
        } 
        addToString(k);
        continue;
      } 
      paramInt = this.stringBufferTop;
      while (true) {
        while (matchChar(103))
          addToString(103); 
        if (matchChar(105)) {
          addToString(105);
          continue;
        } 
        if (matchChar(109)) {
          addToString(109);
          continue;
        } 
        if (matchChar(121)) {
          addToString(121);
          continue;
        } 
        this.tokenEnd = this.stringBufferTop + i + 2;
        if (isAlpha(peekChar()))
          this.parser.reportError("msg.invalid.re.flag"); 
        this.string = new String(this.stringBuffer, 0, paramInt);
        this.regExpFlags = new String(this.stringBuffer, paramInt, this.stringBufferTop - paramInt);
        return;
      } 
    } 
    ungetChar(j);
    this.tokenEnd = this.cursor - 1;
    this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
    this.parser.reportError("msg.unterminated.re.lit");
  }
  
  String tokenToString(int paramInt) {
    return "";
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/TokenStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */