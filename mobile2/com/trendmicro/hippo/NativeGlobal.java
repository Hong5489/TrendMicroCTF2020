package com.trendmicro.hippo;

import com.trendmicro.hippo.xml.XMLLib;
import java.io.Serializable;

public class NativeGlobal implements Serializable, IdFunctionCall {
  private static final Object FTAG = "Global";
  
  private static final int INVALID_UTF8 = 2147483647;
  
  private static final int Id_decodeURI = 1;
  
  private static final int Id_decodeURIComponent = 2;
  
  private static final int Id_encodeURI = 3;
  
  private static final int Id_encodeURIComponent = 4;
  
  private static final int Id_escape = 5;
  
  private static final int Id_eval = 6;
  
  private static final int Id_isFinite = 7;
  
  private static final int Id_isNaN = 8;
  
  private static final int Id_isXMLName = 9;
  
  private static final int Id_new_CommonError = 14;
  
  private static final int Id_parseFloat = 10;
  
  private static final int Id_parseInt = 11;
  
  private static final int Id_unescape = 12;
  
  private static final int Id_uneval = 13;
  
  private static final int LAST_SCOPE_FUNCTION_ID = 13;
  
  private static final String URI_DECODE_RESERVED = ";/?:@&=+$,#";
  
  static final long serialVersionUID = 6080442165748707530L;
  
  @Deprecated
  public static EcmaError constructError(Context paramContext, String paramString1, String paramString2, Scriptable paramScriptable) {
    return ScriptRuntime.constructError(paramString1, paramString2);
  }
  
  @Deprecated
  public static EcmaError constructError(Context paramContext, String paramString1, String paramString2, Scriptable paramScriptable, String paramString3, int paramInt1, int paramInt2, String paramString4) {
    return ScriptRuntime.constructError(paramString1, paramString2, paramString3, paramInt1, paramString4, paramInt2);
  }
  
  private static String decode(String paramString, boolean paramBoolean) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: iconst_0
    //   3: istore_3
    //   4: iconst_0
    //   5: istore #4
    //   7: aload_0
    //   8: invokevirtual length : ()I
    //   11: istore #5
    //   13: iload #4
    //   15: iload #5
    //   17: if_icmpeq -> 599
    //   20: aload_0
    //   21: iload #4
    //   23: invokevirtual charAt : (I)C
    //   26: istore #6
    //   28: iload #6
    //   30: bipush #37
    //   32: if_icmpeq -> 72
    //   35: iload_3
    //   36: istore #7
    //   38: aload_2
    //   39: ifnull -> 53
    //   42: aload_2
    //   43: iload_3
    //   44: iload #6
    //   46: i2c
    //   47: castore
    //   48: iload_3
    //   49: iconst_1
    //   50: iadd
    //   51: istore #7
    //   53: iload #4
    //   55: iconst_1
    //   56: iadd
    //   57: istore #6
    //   59: aload_2
    //   60: astore #8
    //   62: iload #7
    //   64: istore_3
    //   65: iload #6
    //   67: istore #7
    //   69: goto -> 569
    //   72: aload_2
    //   73: astore #8
    //   75: iload_3
    //   76: istore #9
    //   78: aload_2
    //   79: ifnonnull -> 102
    //   82: iload #5
    //   84: newarray char
    //   86: astore #8
    //   88: aload_0
    //   89: iconst_0
    //   90: iload #4
    //   92: aload #8
    //   94: iconst_0
    //   95: invokevirtual getChars : (II[CI)V
    //   98: iload #4
    //   100: istore #9
    //   102: iload #4
    //   104: iconst_3
    //   105: iadd
    //   106: iload #5
    //   108: if_icmpgt -> 595
    //   111: aload_0
    //   112: iload #4
    //   114: iconst_1
    //   115: iadd
    //   116: invokevirtual charAt : (I)C
    //   119: aload_0
    //   120: iload #4
    //   122: iconst_2
    //   123: iadd
    //   124: invokevirtual charAt : (I)C
    //   127: invokestatic unHex : (CC)I
    //   130: istore_3
    //   131: iload_3
    //   132: iflt -> 591
    //   135: iload #4
    //   137: iconst_3
    //   138: iadd
    //   139: istore #10
    //   141: iload_3
    //   142: sipush #128
    //   145: iand
    //   146: ifne -> 160
    //   149: iload_3
    //   150: i2c
    //   151: istore #6
    //   153: iload #10
    //   155: istore #7
    //   157: goto -> 506
    //   160: iload_3
    //   161: sipush #192
    //   164: iand
    //   165: sipush #128
    //   168: if_icmpeq -> 587
    //   171: iload_3
    //   172: bipush #32
    //   174: iand
    //   175: ifne -> 194
    //   178: iconst_1
    //   179: istore #7
    //   181: iload_3
    //   182: bipush #31
    //   184: iand
    //   185: istore_3
    //   186: sipush #128
    //   189: istore #6
    //   191: goto -> 276
    //   194: iload_3
    //   195: bipush #16
    //   197: iand
    //   198: ifne -> 217
    //   201: iconst_2
    //   202: istore #7
    //   204: iload_3
    //   205: bipush #15
    //   207: iand
    //   208: istore_3
    //   209: sipush #2048
    //   212: istore #6
    //   214: goto -> 276
    //   217: iload_3
    //   218: bipush #8
    //   220: iand
    //   221: ifne -> 239
    //   224: iconst_3
    //   225: istore #7
    //   227: iload_3
    //   228: bipush #7
    //   230: iand
    //   231: istore_3
    //   232: ldc 65536
    //   234: istore #6
    //   236: goto -> 276
    //   239: iload_3
    //   240: iconst_4
    //   241: iand
    //   242: ifne -> 259
    //   245: iconst_4
    //   246: istore #7
    //   248: iload_3
    //   249: iconst_3
    //   250: iand
    //   251: istore_3
    //   252: ldc 2097152
    //   254: istore #6
    //   256: goto -> 276
    //   259: iload_3
    //   260: iconst_2
    //   261: iand
    //   262: ifne -> 583
    //   265: iconst_5
    //   266: istore #7
    //   268: iload_3
    //   269: iconst_1
    //   270: iand
    //   271: istore_3
    //   272: ldc 67108864
    //   274: istore #6
    //   276: iload #7
    //   278: iconst_3
    //   279: imul
    //   280: iload #10
    //   282: iadd
    //   283: iload #5
    //   285: if_icmpgt -> 579
    //   288: iconst_0
    //   289: istore #11
    //   291: iload_3
    //   292: istore #12
    //   294: iload #10
    //   296: istore_3
    //   297: iload #11
    //   299: iload #7
    //   301: if_icmpeq -> 380
    //   304: aload_0
    //   305: iload_3
    //   306: invokevirtual charAt : (I)C
    //   309: bipush #37
    //   311: if_icmpne -> 376
    //   314: aload_0
    //   315: iload_3
    //   316: iconst_1
    //   317: iadd
    //   318: invokevirtual charAt : (I)C
    //   321: aload_0
    //   322: iload_3
    //   323: iconst_2
    //   324: iadd
    //   325: invokevirtual charAt : (I)C
    //   328: invokestatic unHex : (CC)I
    //   331: istore #10
    //   333: iload #10
    //   335: iflt -> 372
    //   338: iload #10
    //   340: sipush #192
    //   343: iand
    //   344: sipush #128
    //   347: if_icmpne -> 372
    //   350: iload #12
    //   352: bipush #6
    //   354: ishl
    //   355: iload #10
    //   357: bipush #63
    //   359: iand
    //   360: ior
    //   361: istore #12
    //   363: iinc #3, 3
    //   366: iinc #11, 1
    //   369: goto -> 297
    //   372: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   375: athrow
    //   376: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   379: athrow
    //   380: iload #12
    //   382: iload #6
    //   384: if_icmplt -> 429
    //   387: iload #12
    //   389: ldc 55296
    //   391: if_icmplt -> 404
    //   394: iload #12
    //   396: ldc 57343
    //   398: if_icmpgt -> 404
    //   401: goto -> 429
    //   404: iload #12
    //   406: ldc 65534
    //   408: if_icmpeq -> 422
    //   411: iload #12
    //   413: istore #7
    //   415: iload #12
    //   417: ldc 65535
    //   419: if_icmpne -> 433
    //   422: ldc 65533
    //   424: istore #7
    //   426: goto -> 433
    //   429: ldc 2147483647
    //   431: istore #7
    //   433: iload #7
    //   435: ldc 65536
    //   437: if_icmplt -> 498
    //   440: iload #7
    //   442: ldc 65536
    //   444: isub
    //   445: istore #6
    //   447: iload #6
    //   449: ldc 1048575
    //   451: if_icmpgt -> 494
    //   454: iload #6
    //   456: bipush #10
    //   458: iushr
    //   459: ldc 55296
    //   461: iadd
    //   462: i2c
    //   463: istore #7
    //   465: iload #6
    //   467: sipush #1023
    //   470: iand
    //   471: ldc 56320
    //   473: iadd
    //   474: i2c
    //   475: istore #6
    //   477: aload #8
    //   479: iload #9
    //   481: iload #7
    //   483: i2c
    //   484: castore
    //   485: iinc #9, 1
    //   488: iload_3
    //   489: istore #7
    //   491: goto -> 506
    //   494: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   497: athrow
    //   498: iload #7
    //   500: i2c
    //   501: istore #6
    //   503: iload_3
    //   504: istore #7
    //   506: iload_1
    //   507: ifeq -> 556
    //   510: ldc ';/?:@&=+$,#'
    //   512: iload #6
    //   514: invokevirtual indexOf : (I)I
    //   517: iflt -> 556
    //   520: iload #4
    //   522: istore #6
    //   524: iload #9
    //   526: istore_3
    //   527: iload #6
    //   529: iload #7
    //   531: if_icmpeq -> 553
    //   534: aload #8
    //   536: iload_3
    //   537: aload_0
    //   538: iload #6
    //   540: invokevirtual charAt : (I)C
    //   543: castore
    //   544: iinc #6, 1
    //   547: iinc #3, 1
    //   550: goto -> 527
    //   553: goto -> 569
    //   556: aload #8
    //   558: iload #9
    //   560: iload #6
    //   562: i2c
    //   563: castore
    //   564: iload #9
    //   566: iconst_1
    //   567: iadd
    //   568: istore_3
    //   569: aload #8
    //   571: astore_2
    //   572: iload #7
    //   574: istore #4
    //   576: goto -> 13
    //   579: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   582: athrow
    //   583: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   586: athrow
    //   587: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   590: athrow
    //   591: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   594: athrow
    //   595: invokestatic uriError : ()Lcom/trendmicro/hippo/EcmaError;
    //   598: athrow
    //   599: aload_2
    //   600: ifnonnull -> 606
    //   603: goto -> 617
    //   606: new java/lang/String
    //   609: dup
    //   610: aload_2
    //   611: iconst_0
    //   612: iload_3
    //   613: invokespecial <init> : ([CII)V
    //   616: astore_0
    //   617: aload_0
    //   618: areturn
  }
  
  private static String encode(String paramString, boolean paramBoolean) {
    byte[] arrayOfByte = null;
    StringBuilder stringBuilder = null;
    int i = 0;
    int j = paramString.length();
    while (i != j) {
      byte[] arrayOfByte1;
      StringBuilder stringBuilder1;
      int k;
      char c = paramString.charAt(i);
      if (encodeUnescaped(c, paramBoolean)) {
        arrayOfByte1 = arrayOfByte;
        stringBuilder1 = stringBuilder;
        k = i;
        if (stringBuilder != null) {
          stringBuilder.append(c);
          arrayOfByte1 = arrayOfByte;
          stringBuilder1 = stringBuilder;
          k = i;
        } 
      } else {
        StringBuilder stringBuilder2 = stringBuilder;
        if (stringBuilder == null) {
          stringBuilder2 = new StringBuilder(j + 3);
          stringBuilder2.append(paramString);
          stringBuilder2.setLength(i);
          arrayOfByte = new byte[6];
        } 
        if ('?' > c || c > '?') {
          if (c < '?' || '?' < c) {
            m = c;
          } else if (++i != j) {
            m = paramString.charAt(i);
            if (56320 <= m && m <= 57343) {
              m = (c - 55296 << 10) + m - 56320 + 65536;
            } else {
              throw uriError();
            } 
          } else {
            throw uriError();
          } 
          int n = oneUcs4ToUtf8Char(arrayOfByte, m);
          int m = 0;
          while (true) {
            arrayOfByte1 = arrayOfByte;
            stringBuilder1 = stringBuilder2;
            k = i;
            if (m < n) {
              k = arrayOfByte[m] & 0xFF;
              stringBuilder2.append('%');
              stringBuilder2.append(toHexChar(k >>> 4));
              stringBuilder2.append(toHexChar(k & 0xF));
              m++;
              continue;
            } 
            break;
          } 
        } else {
          throw uriError();
        } 
      } 
      i = k + 1;
      arrayOfByte = arrayOfByte1;
      stringBuilder = stringBuilder1;
    } 
    if (stringBuilder != null)
      paramString = stringBuilder.toString(); 
    return paramString;
  }
  
  private static boolean encodeUnescaped(char paramChar, boolean paramBoolean) {
    boolean bool = true;
    if (('A' <= paramChar && paramChar <= 'Z') || ('a' <= paramChar && paramChar <= 'z') || ('0' <= paramChar && paramChar <= '9'))
      return true; 
    if ("-_.!~*'()".indexOf(paramChar) >= 0)
      return true; 
    if (paramBoolean) {
      if (";/?:@&=+$,#".indexOf(paramChar) >= 0) {
        paramBoolean = bool;
      } else {
        paramBoolean = false;
      } 
      return paramBoolean;
    } 
    return false;
  }
  
  public static void init(Context paramContext, Scriptable paramScriptable, boolean paramBoolean) {
    NativeGlobal nativeGlobal = new NativeGlobal();
    for (null = 1; null <= 13; null++) {
      byte b;
      String str;
      switch (null) {
        default:
          throw Kit.codeBug();
        case 13:
          b = 1;
          str = "uneval";
          break;
        case 12:
          b = 1;
          str = "unescape";
          break;
        case 11:
          b = 2;
          str = "parseInt";
          break;
        case 10:
          b = 1;
          str = "parseFloat";
          break;
        case 9:
          b = 1;
          str = "isXMLName";
          break;
        case 8:
          b = 1;
          str = "isNaN";
          break;
        case 7:
          b = 1;
          str = "isFinite";
          break;
        case 6:
          b = 1;
          str = "eval";
          break;
        case 5:
          b = 1;
          str = "escape";
          break;
        case 4:
          b = 1;
          str = "encodeURIComponent";
          break;
        case 3:
          b = 1;
          str = "encodeURI";
          break;
        case 2:
          b = 1;
          str = "decodeURIComponent";
          break;
        case 1:
          b = 1;
          str = "decodeURI";
          break;
      } 
      IdFunctionObject idFunctionObject = new IdFunctionObject(nativeGlobal, FTAG, null, str, b, paramScriptable);
      if (paramBoolean)
        idFunctionObject.sealObject(); 
      idFunctionObject.exportAsScopeProperty();
    } 
    ScriptableObject.defineProperty(paramScriptable, "NaN", ScriptRuntime.NaNobj, 7);
    ScriptableObject.defineProperty(paramScriptable, "Infinity", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
    ScriptableObject.defineProperty(paramScriptable, "undefined", Undefined.instance, 7);
    for (TopLevel.NativeErrors nativeErrors : TopLevel.NativeErrors.values()) {
      if (nativeErrors != TopLevel.NativeErrors.Error) {
        String str = nativeErrors.name();
        ScriptableObject scriptableObject = (ScriptableObject)ScriptRuntime.newBuiltinObject(paramContext, paramScriptable, TopLevel.Builtins.Error, ScriptRuntime.emptyArgs);
        scriptableObject.put("name", scriptableObject, str);
        scriptableObject.put("message", scriptableObject, "");
        IdFunctionObject idFunctionObject = new IdFunctionObject(nativeGlobal, FTAG, 14, str, 1, paramScriptable);
        idFunctionObject.markAsConstructor(scriptableObject);
        scriptableObject.put("constructor", scriptableObject, idFunctionObject);
        scriptableObject.setAttributes("constructor", 2);
        if (paramBoolean) {
          scriptableObject.sealObject();
          idFunctionObject.sealObject();
        } 
        idFunctionObject.exportAsScopeProperty();
      } 
    } 
  }
  
  static boolean isEvalFunction(Object paramObject) {
    if (paramObject instanceof IdFunctionObject) {
      paramObject = paramObject;
      if (paramObject.hasTag(FTAG) && paramObject.methodId() == 6)
        return true; 
    } 
    return false;
  }
  
  private Object js_escape(Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_1
    //   1: iconst_0
    //   2: invokestatic toString : ([Ljava/lang/Object;I)Ljava/lang/String;
    //   5: astore_2
    //   6: bipush #7
    //   8: istore_3
    //   9: aload_1
    //   10: arraylength
    //   11: iconst_1
    //   12: if_icmple -> 65
    //   15: aload_1
    //   16: iconst_1
    //   17: aaload
    //   18: invokestatic toNumber : (Ljava/lang/Object;)D
    //   21: dstore #4
    //   23: dload #4
    //   25: dload #4
    //   27: dcmpl
    //   28: ifne -> 58
    //   31: dload #4
    //   33: d2i
    //   34: istore #6
    //   36: iload #6
    //   38: istore_3
    //   39: iload #6
    //   41: i2d
    //   42: dload #4
    //   44: dcmpl
    //   45: ifne -> 58
    //   48: iload_3
    //   49: bipush #-8
    //   51: iand
    //   52: ifne -> 58
    //   55: goto -> 65
    //   58: ldc_w 'msg.bad.esc.mask'
    //   61: invokestatic reportRuntimeError0 : (Ljava/lang/String;)Lcom/trendmicro/hippo/EvaluatorException;
    //   64: athrow
    //   65: aconst_null
    //   66: astore #7
    //   68: iconst_0
    //   69: istore #6
    //   71: aload_2
    //   72: invokevirtual length : ()I
    //   75: istore #8
    //   77: iload #6
    //   79: iload #8
    //   81: if_icmpeq -> 379
    //   84: aload_2
    //   85: iload #6
    //   87: invokevirtual charAt : (I)C
    //   90: istore #9
    //   92: iload_3
    //   93: ifeq -> 218
    //   96: iload #9
    //   98: bipush #48
    //   100: if_icmplt -> 110
    //   103: iload #9
    //   105: bipush #57
    //   107: if_icmple -> 193
    //   110: iload #9
    //   112: bipush #65
    //   114: if_icmplt -> 124
    //   117: iload #9
    //   119: bipush #90
    //   121: if_icmple -> 193
    //   124: iload #9
    //   126: bipush #97
    //   128: if_icmplt -> 138
    //   131: iload #9
    //   133: bipush #122
    //   135: if_icmple -> 193
    //   138: iload #9
    //   140: bipush #64
    //   142: if_icmpeq -> 193
    //   145: iload #9
    //   147: bipush #42
    //   149: if_icmpeq -> 193
    //   152: iload #9
    //   154: bipush #95
    //   156: if_icmpeq -> 193
    //   159: iload #9
    //   161: bipush #45
    //   163: if_icmpeq -> 193
    //   166: iload #9
    //   168: bipush #46
    //   170: if_icmpeq -> 193
    //   173: iload_3
    //   174: iconst_4
    //   175: iand
    //   176: ifeq -> 218
    //   179: iload #9
    //   181: bipush #47
    //   183: if_icmpeq -> 193
    //   186: iload #9
    //   188: bipush #43
    //   190: if_icmpne -> 218
    //   193: aload #7
    //   195: astore #10
    //   197: aload #7
    //   199: ifnull -> 369
    //   202: aload #7
    //   204: iload #9
    //   206: i2c
    //   207: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload #7
    //   213: astore #10
    //   215: goto -> 369
    //   218: aload #7
    //   220: astore_1
    //   221: aload #7
    //   223: ifnonnull -> 250
    //   226: new java/lang/StringBuilder
    //   229: dup
    //   230: iload #8
    //   232: iconst_3
    //   233: iadd
    //   234: invokespecial <init> : (I)V
    //   237: astore_1
    //   238: aload_1
    //   239: aload_2
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: pop
    //   244: aload_1
    //   245: iload #6
    //   247: invokevirtual setLength : (I)V
    //   250: iload #9
    //   252: sipush #256
    //   255: if_icmpge -> 296
    //   258: iload #9
    //   260: bipush #32
    //   262: if_icmpne -> 283
    //   265: iload_3
    //   266: iconst_2
    //   267: if_icmpne -> 283
    //   270: aload_1
    //   271: bipush #43
    //   273: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   276: pop
    //   277: aload_1
    //   278: astore #10
    //   280: goto -> 369
    //   283: aload_1
    //   284: bipush #37
    //   286: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: iconst_2
    //   291: istore #11
    //   293: goto -> 313
    //   296: aload_1
    //   297: bipush #37
    //   299: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   302: pop
    //   303: aload_1
    //   304: bipush #117
    //   306: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   309: pop
    //   310: iconst_4
    //   311: istore #11
    //   313: iload #11
    //   315: iconst_1
    //   316: isub
    //   317: iconst_4
    //   318: imul
    //   319: istore #11
    //   321: aload_1
    //   322: astore #10
    //   324: iload #11
    //   326: iflt -> 369
    //   329: iload #9
    //   331: iload #11
    //   333: ishr
    //   334: bipush #15
    //   336: iand
    //   337: istore #12
    //   339: iload #12
    //   341: bipush #10
    //   343: if_icmpge -> 352
    //   346: iinc #12, 48
    //   349: goto -> 355
    //   352: iinc #12, 55
    //   355: aload_1
    //   356: iload #12
    //   358: i2c
    //   359: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   362: pop
    //   363: iinc #11, -4
    //   366: goto -> 321
    //   369: iinc #6, 1
    //   372: aload #10
    //   374: astore #7
    //   376: goto -> 77
    //   379: aload #7
    //   381: ifnonnull -> 389
    //   384: aload_2
    //   385: astore_1
    //   386: goto -> 395
    //   389: aload #7
    //   391: invokevirtual toString : ()Ljava/lang/String;
    //   394: astore_1
    //   395: aload_1
    //   396: areturn
  }
  
  private Object js_eval(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    return ScriptRuntime.evalSpecial(paramContext, paramScriptable, paramScriptable, paramArrayOfObject, "eval code", 1);
  }
  
  static Object js_parseFloat(Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: iconst_1
    //   3: if_icmpge -> 10
    //   6: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   9: areturn
    //   10: aload_0
    //   11: iconst_0
    //   12: aaload
    //   13: invokestatic toString : (Ljava/lang/Object;)Ljava/lang/String;
    //   16: astore_0
    //   17: aload_0
    //   18: invokevirtual length : ()I
    //   21: istore_1
    //   22: iconst_0
    //   23: istore_2
    //   24: iload_2
    //   25: iload_1
    //   26: if_icmpne -> 33
    //   29: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   32: areturn
    //   33: aload_0
    //   34: iload_2
    //   35: invokevirtual charAt : (I)C
    //   38: istore_3
    //   39: iload_3
    //   40: invokestatic isStrWhiteSpaceChar : (I)Z
    //   43: ifne -> 482
    //   46: iload_2
    //   47: istore #4
    //   49: iload_3
    //   50: bipush #43
    //   52: if_icmpeq -> 68
    //   55: iload_3
    //   56: istore #5
    //   58: iload #4
    //   60: istore #6
    //   62: iload_3
    //   63: bipush #45
    //   65: if_icmpne -> 92
    //   68: iload #4
    //   70: iconst_1
    //   71: iadd
    //   72: istore #6
    //   74: iload #6
    //   76: iload_1
    //   77: if_icmpne -> 84
    //   80: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   83: areturn
    //   84: aload_0
    //   85: iload #6
    //   87: invokevirtual charAt : (I)C
    //   90: istore #5
    //   92: iload #5
    //   94: bipush #73
    //   96: if_icmpne -> 155
    //   99: iload #6
    //   101: bipush #8
    //   103: iadd
    //   104: iload_1
    //   105: if_icmpgt -> 151
    //   108: aload_0
    //   109: iload #6
    //   111: ldc 'Infinity'
    //   113: iconst_0
    //   114: bipush #8
    //   116: invokevirtual regionMatches : (ILjava/lang/String;II)Z
    //   119: ifeq -> 151
    //   122: aload_0
    //   123: iload_2
    //   124: invokevirtual charAt : (I)C
    //   127: bipush #45
    //   129: if_icmpne -> 140
    //   132: ldc2_w -Infinity
    //   135: dstore #7
    //   137: goto -> 145
    //   140: ldc2_w Infinity
    //   143: dstore #7
    //   145: dload #7
    //   147: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   150: areturn
    //   151: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   154: areturn
    //   155: iconst_m1
    //   156: istore #9
    //   158: iconst_m1
    //   159: istore #4
    //   161: iconst_0
    //   162: istore_3
    //   163: iload #6
    //   165: istore #5
    //   167: iload #6
    //   169: iload_1
    //   170: if_icmpge -> 440
    //   173: aload_0
    //   174: iload #6
    //   176: invokevirtual charAt : (I)C
    //   179: istore #5
    //   181: iload #5
    //   183: bipush #43
    //   185: if_icmpeq -> 379
    //   188: iload #5
    //   190: bipush #69
    //   192: if_icmpeq -> 337
    //   195: iload #5
    //   197: bipush #101
    //   199: if_icmpeq -> 337
    //   202: iload #5
    //   204: bipush #45
    //   206: if_icmpeq -> 379
    //   209: iload #5
    //   211: bipush #46
    //   213: if_icmpeq -> 310
    //   216: iload #5
    //   218: tableswitch default -> 272, 48 -> 279, 49 -> 279, 50 -> 279, 51 -> 279, 52 -> 279, 53 -> 279, 54 -> 279, 55 -> 279, 56 -> 279, 57 -> 279
    //   272: iload #6
    //   274: istore #5
    //   276: goto -> 440
    //   279: iload #9
    //   281: istore #5
    //   283: iload #4
    //   285: istore #10
    //   287: iload_3
    //   288: istore #11
    //   290: iload #4
    //   292: iconst_m1
    //   293: if_icmpeq -> 423
    //   296: iconst_1
    //   297: istore #11
    //   299: iload #9
    //   301: istore #5
    //   303: iload #4
    //   305: istore #10
    //   307: goto -> 423
    //   310: iload #9
    //   312: iconst_m1
    //   313: if_icmpeq -> 323
    //   316: iload #6
    //   318: istore #5
    //   320: goto -> 440
    //   323: iload #6
    //   325: istore #5
    //   327: iload #4
    //   329: istore #10
    //   331: iload_3
    //   332: istore #11
    //   334: goto -> 423
    //   337: iload #4
    //   339: iconst_m1
    //   340: if_icmpeq -> 350
    //   343: iload #6
    //   345: istore #5
    //   347: goto -> 440
    //   350: iload #6
    //   352: iload_1
    //   353: iconst_1
    //   354: isub
    //   355: if_icmpne -> 365
    //   358: iload #6
    //   360: istore #5
    //   362: goto -> 440
    //   365: iload #6
    //   367: istore #10
    //   369: iload #9
    //   371: istore #5
    //   373: iload_3
    //   374: istore #11
    //   376: goto -> 423
    //   379: iload #4
    //   381: iload #6
    //   383: iconst_1
    //   384: isub
    //   385: if_icmpeq -> 395
    //   388: iload #6
    //   390: istore #5
    //   392: goto -> 440
    //   395: iload #9
    //   397: istore #5
    //   399: iload #4
    //   401: istore #10
    //   403: iload_3
    //   404: istore #11
    //   406: iload #6
    //   408: iload_1
    //   409: iconst_1
    //   410: isub
    //   411: if_icmpne -> 423
    //   414: iload #6
    //   416: iconst_1
    //   417: isub
    //   418: istore #5
    //   420: goto -> 440
    //   423: iinc #6, 1
    //   426: iload #5
    //   428: istore #9
    //   430: iload #10
    //   432: istore #4
    //   434: iload #11
    //   436: istore_3
    //   437: goto -> 163
    //   440: iload #5
    //   442: istore #6
    //   444: iload #4
    //   446: iconst_m1
    //   447: if_icmpeq -> 462
    //   450: iload #5
    //   452: istore #6
    //   454: iload_3
    //   455: ifne -> 462
    //   458: iload #4
    //   460: istore #6
    //   462: aload_0
    //   463: iload_2
    //   464: iload #6
    //   466: invokevirtual substring : (II)Ljava/lang/String;
    //   469: astore_0
    //   470: aload_0
    //   471: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Double;
    //   474: astore_0
    //   475: aload_0
    //   476: areturn
    //   477: astore_0
    //   478: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   481: areturn
    //   482: iinc #2, 1
    //   485: goto -> 24
    // Exception table:
    //   from	to	target	type
    //   470	475	477	java/lang/NumberFormatException
  }
  
  static Object js_parseInt(Object[] paramArrayOfObject) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: iconst_0
    //   4: invokestatic toString : ([Ljava/lang/Object;I)Ljava/lang/String;
    //   7: astore_2
    //   8: aload_0
    //   9: iconst_1
    //   10: invokestatic toInt32 : ([Ljava/lang/Object;I)I
    //   13: istore_3
    //   14: aload_2
    //   15: invokevirtual length : ()I
    //   18: istore #4
    //   20: iload #4
    //   22: ifne -> 29
    //   25: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   28: areturn
    //   29: iconst_0
    //   30: istore #5
    //   32: iconst_0
    //   33: istore #6
    //   35: aload_2
    //   36: iload #6
    //   38: invokevirtual charAt : (I)C
    //   41: istore #7
    //   43: iload #7
    //   45: invokestatic isStrWhiteSpaceChar : (I)Z
    //   48: ifne -> 54
    //   51: goto -> 75
    //   54: iload #6
    //   56: iconst_1
    //   57: iadd
    //   58: istore #8
    //   60: iload #8
    //   62: istore #6
    //   64: iload #8
    //   66: iload #4
    //   68: if_icmplt -> 35
    //   71: iload #8
    //   73: istore #6
    //   75: iload #7
    //   77: bipush #43
    //   79: if_icmpeq -> 113
    //   82: iload_1
    //   83: istore #5
    //   85: iload #7
    //   87: bipush #45
    //   89: if_icmpne -> 95
    //   92: iconst_1
    //   93: istore #5
    //   95: iload #5
    //   97: istore_1
    //   98: iload_1
    //   99: istore #7
    //   101: iload #6
    //   103: istore #8
    //   105: iload #5
    //   107: ifeq -> 123
    //   110: iload_1
    //   111: istore #5
    //   113: iload #6
    //   115: iconst_1
    //   116: iadd
    //   117: istore #8
    //   119: iload #5
    //   121: istore #7
    //   123: iload_3
    //   124: ifne -> 136
    //   127: iconst_m1
    //   128: istore_1
    //   129: iload #8
    //   131: istore #6
    //   133: goto -> 232
    //   136: iload_3
    //   137: iconst_2
    //   138: if_icmplt -> 383
    //   141: iload_3
    //   142: bipush #36
    //   144: if_icmple -> 150
    //   147: goto -> 383
    //   150: iload_3
    //   151: istore_1
    //   152: iload #8
    //   154: istore #6
    //   156: iload_3
    //   157: bipush #16
    //   159: if_icmpne -> 232
    //   162: iload_3
    //   163: istore_1
    //   164: iload #8
    //   166: istore #6
    //   168: iload #4
    //   170: iload #8
    //   172: isub
    //   173: iconst_1
    //   174: if_icmple -> 232
    //   177: iload_3
    //   178: istore_1
    //   179: iload #8
    //   181: istore #6
    //   183: aload_2
    //   184: iload #8
    //   186: invokevirtual charAt : (I)C
    //   189: bipush #48
    //   191: if_icmpne -> 232
    //   194: aload_2
    //   195: iload #8
    //   197: iconst_1
    //   198: iadd
    //   199: invokevirtual charAt : (I)C
    //   202: istore #5
    //   204: iload #5
    //   206: bipush #120
    //   208: if_icmpeq -> 224
    //   211: iload_3
    //   212: istore_1
    //   213: iload #8
    //   215: istore #6
    //   217: iload #5
    //   219: bipush #88
    //   221: if_icmpne -> 232
    //   224: iload #8
    //   226: iconst_2
    //   227: iadd
    //   228: istore #6
    //   230: iload_3
    //   231: istore_1
    //   232: iload_1
    //   233: istore #8
    //   235: iload #6
    //   237: istore #5
    //   239: iload_1
    //   240: iconst_m1
    //   241: if_icmpne -> 354
    //   244: bipush #10
    //   246: istore_1
    //   247: iload_1
    //   248: istore #8
    //   250: iload #6
    //   252: istore #5
    //   254: iload #4
    //   256: iload #6
    //   258: isub
    //   259: iconst_1
    //   260: if_icmple -> 354
    //   263: iload_1
    //   264: istore #8
    //   266: iload #6
    //   268: istore #5
    //   270: aload_2
    //   271: iload #6
    //   273: invokevirtual charAt : (I)C
    //   276: bipush #48
    //   278: if_icmpne -> 354
    //   281: aload_2
    //   282: iload #6
    //   284: iconst_1
    //   285: iadd
    //   286: invokevirtual charAt : (I)C
    //   289: istore_3
    //   290: iload_3
    //   291: bipush #120
    //   293: if_icmpeq -> 344
    //   296: iload_3
    //   297: bipush #88
    //   299: if_icmpne -> 305
    //   302: goto -> 344
    //   305: iload_1
    //   306: istore #8
    //   308: iload #6
    //   310: istore #5
    //   312: bipush #48
    //   314: iload_3
    //   315: if_icmpgt -> 354
    //   318: iload_1
    //   319: istore #8
    //   321: iload #6
    //   323: istore #5
    //   325: iload_3
    //   326: bipush #57
    //   328: if_icmpgt -> 354
    //   331: bipush #8
    //   333: istore #8
    //   335: iload #6
    //   337: iconst_1
    //   338: iadd
    //   339: istore #5
    //   341: goto -> 354
    //   344: bipush #16
    //   346: istore #8
    //   348: iload #6
    //   350: iconst_2
    //   351: iadd
    //   352: istore #5
    //   354: aload_2
    //   355: iload #5
    //   357: iload #8
    //   359: invokestatic stringPrefixToNumber : (Ljava/lang/String;II)D
    //   362: dstore #9
    //   364: iload #7
    //   366: ifeq -> 377
    //   369: dload #9
    //   371: dneg
    //   372: dstore #9
    //   374: goto -> 377
    //   377: dload #9
    //   379: invokestatic wrapNumber : (D)Ljava/lang/Number;
    //   382: areturn
    //   383: getstatic com/trendmicro/hippo/ScriptRuntime.NaNobj : Ljava/lang/Double;
    //   386: areturn
  }
  
  private Object js_unescape(Object[] paramArrayOfObject) {
    String str2 = ScriptRuntime.toString(paramArrayOfObject, 0);
    int i = str2.indexOf('%');
    String str1 = str2;
    if (i >= 0) {
      int j = str2.length();
      char[] arrayOfChar = str2.toCharArray();
      int k;
      for (k = i; i != j; k++) {
        char c = arrayOfChar[i];
        int m = i + 1;
        i = m;
        char c1 = c;
        if (c == '%') {
          i = m;
          c1 = c;
          if (m != j) {
            int n;
            int i1;
            if (arrayOfChar[m] == 'u') {
              n = m + 1;
              i1 = m + 5;
            } else {
              n = m;
              i1 = m + 2;
            } 
            i = m;
            c1 = c;
            if (i1 <= j) {
              c1 = Character.MIN_VALUE;
              i = n;
              n = c1;
              while (i != i1) {
                n = Kit.xDigitToInt(arrayOfChar[i], n);
                i++;
              } 
              i = m;
              c1 = c;
              if (n >= 0) {
                c1 = (char)n;
                i = i1;
              } 
            } 
          } 
        } 
        arrayOfChar[k] = (char)c1;
      } 
      str1 = new String(arrayOfChar, 0, k);
    } 
    return str1;
  }
  
  private static int oneUcs4ToUtf8Char(byte[] paramArrayOfbyte, int paramInt) {
    byte b = 1;
    if ((paramInt & 0xFFFFFF80) == 0) {
      paramArrayOfbyte[0] = (byte)(byte)paramInt;
    } else {
      int i = paramInt >>> 11;
      for (b = 2; i != 0; b++)
        i >>>= 5; 
      i = b;
      while (--i > 0) {
        paramArrayOfbyte[i] = (byte)(byte)(paramInt & 0x3F | 0x80);
        paramInt >>>= 6;
      } 
      paramArrayOfbyte[0] = (byte)(byte)(256 - (1 << 8 - b) + paramInt);
    } 
    return b;
  }
  
  private static char toHexChar(int paramInt) {
    if (paramInt >> 4 != 0)
      Kit.codeBug(); 
    if (paramInt < 10) {
      paramInt += 48;
    } else {
      paramInt = paramInt - 10 + 65;
    } 
    return (char)paramInt;
  }
  
  private static int unHex(char paramChar) {
    return ('A' <= paramChar && paramChar <= 'F') ? (paramChar - 65 + 10) : (('a' <= paramChar && paramChar <= 'f') ? (paramChar - 97 + 10) : (('0' <= paramChar && paramChar <= '9') ? (paramChar - 48) : -1));
  }
  
  private static int unHex(char paramChar1, char paramChar2) {
    int i = unHex(paramChar1);
    int j = unHex(paramChar2);
    return (i >= 0 && j >= 0) ? (i << 4 | j) : -1;
  }
  
  private static EcmaError uriError() {
    return ScriptRuntime.constructError("URIError", ScriptRuntime.getMessage0("msg.bad.uri"));
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (paramIdFunctionObject.hasTag(FTAG)) {
      XMLLib xMLLib;
      int i = paramIdFunctionObject.methodId();
      boolean bool1 = true;
      boolean bool2 = true;
      boolean bool3 = true;
      switch (i) {
        default:
          throw paramIdFunctionObject.unknown();
        case 14:
          return NativeError.make(paramContext, paramScriptable1, paramIdFunctionObject, paramArrayOfObject);
        case 13:
          if (paramArrayOfObject.length != 0) {
            object = paramArrayOfObject[0];
          } else {
            object = Undefined.instance;
          } 
          return ScriptRuntime.uneval(paramContext, paramScriptable1, object);
        case 12:
          return js_unescape(paramArrayOfObject);
        case 11:
          return js_parseInt(paramArrayOfObject);
        case 10:
          return js_parseFloat(paramArrayOfObject);
        case 9:
          if (paramArrayOfObject.length == 0) {
            object = Undefined.instance;
          } else {
            object = paramArrayOfObject[0];
          } 
          xMLLib = XMLLib.extractFromScope(paramScriptable1);
          return ScriptRuntime.wrapBoolean(xMLLib.isXMLName(paramContext, object));
        case 8:
          if (paramArrayOfObject.length < 1) {
            bool3 = true;
          } else {
            double d = ScriptRuntime.toNumber(paramArrayOfObject[0]);
            if (d == d)
              bool3 = false; 
          } 
          return ScriptRuntime.wrapBoolean(bool3);
        case 7:
          return (paramArrayOfObject.length < 1) ? Boolean.FALSE : NativeNumber.isFinite(paramArrayOfObject[0]);
        case 6:
          return js_eval(paramContext, (Scriptable)xMLLib, paramArrayOfObject);
        case 5:
          return js_escape(paramArrayOfObject);
        case 3:
        case 4:
          object = ScriptRuntime.toString(paramArrayOfObject, 0);
          if (i == 3) {
            bool3 = bool1;
          } else {
            bool3 = false;
          } 
          return encode((String)object, bool3);
        case 1:
        case 2:
          break;
      } 
      Object object = ScriptRuntime.toString(paramArrayOfObject, 0);
      if (i == 1) {
        bool3 = bool2;
      } else {
        bool3 = false;
      } 
      return decode((String)object, bool3);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */