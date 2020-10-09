package com.trendmicro.hippo;

import java.text.Collator;
import java.text.Normalizer;

final class NativeString extends IdScriptableObject {
  private static final int ConstructorId_charAt = -5;
  
  private static final int ConstructorId_charCodeAt = -6;
  
  private static final int ConstructorId_concat = -14;
  
  private static final int ConstructorId_equalsIgnoreCase = -30;
  
  private static final int ConstructorId_fromCharCode = -1;
  
  private static final int ConstructorId_indexOf = -7;
  
  private static final int ConstructorId_lastIndexOf = -8;
  
  private static final int ConstructorId_localeCompare = -34;
  
  private static final int ConstructorId_match = -31;
  
  private static final int ConstructorId_replace = -33;
  
  private static final int ConstructorId_search = -32;
  
  private static final int ConstructorId_slice = -15;
  
  private static final int ConstructorId_split = -9;
  
  private static final int ConstructorId_substr = -13;
  
  private static final int ConstructorId_substring = -10;
  
  private static final int ConstructorId_toLocaleLowerCase = -35;
  
  private static final int ConstructorId_toLowerCase = -11;
  
  private static final int ConstructorId_toUpperCase = -12;
  
  private static final int Id_anchor = 28;
  
  private static final int Id_big = 21;
  
  private static final int Id_blink = 22;
  
  private static final int Id_bold = 16;
  
  private static final int Id_charAt = 5;
  
  private static final int Id_charCodeAt = 6;
  
  private static final int Id_codePointAt = 45;
  
  private static final int Id_concat = 14;
  
  private static final int Id_constructor = 1;
  
  private static final int Id_endsWith = 42;
  
  private static final int Id_equals = 29;
  
  private static final int Id_equalsIgnoreCase = 30;
  
  private static final int Id_fixed = 18;
  
  private static final int Id_fontcolor = 26;
  
  private static final int Id_fontsize = 25;
  
  private static final int Id_includes = 40;
  
  private static final int Id_indexOf = 7;
  
  private static final int Id_italics = 17;
  
  private static final int Id_lastIndexOf = 8;
  
  private static final int Id_length = 1;
  
  private static final int Id_link = 27;
  
  private static final int Id_localeCompare = 34;
  
  private static final int Id_match = 31;
  
  private static final int Id_normalize = 43;
  
  private static final int Id_padEnd = 47;
  
  private static final int Id_padStart = 46;
  
  private static final int Id_repeat = 44;
  
  private static final int Id_replace = 33;
  
  private static final int Id_search = 32;
  
  private static final int Id_slice = 15;
  
  private static final int Id_small = 20;
  
  private static final int Id_split = 9;
  
  private static final int Id_startsWith = 41;
  
  private static final int Id_strike = 19;
  
  private static final int Id_sub = 24;
  
  private static final int Id_substr = 13;
  
  private static final int Id_substring = 10;
  
  private static final int Id_sup = 23;
  
  private static final int Id_toLocaleLowerCase = 35;
  
  private static final int Id_toLocaleUpperCase = 36;
  
  private static final int Id_toLowerCase = 11;
  
  private static final int Id_toSource = 3;
  
  private static final int Id_toString = 2;
  
  private static final int Id_toUpperCase = 12;
  
  private static final int Id_trim = 37;
  
  private static final int Id_trimLeft = 38;
  
  private static final int Id_trimRight = 39;
  
  private static final int Id_valueOf = 4;
  
  private static final int MAX_INSTANCE_ID = 1;
  
  private static final int MAX_PROTOTYPE_ID = 48;
  
  private static final Object STRING_TAG = "String";
  
  private static final int SymbolId_iterator = 48;
  
  private static final long serialVersionUID = 920268368584188687L;
  
  private CharSequence string;
  
  NativeString(CharSequence paramCharSequence) {
    this.string = paramCharSequence;
  }
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    (new NativeString("")).exportAsJSClass(48, paramScriptable, paramBoolean);
  }
  
  private static String js_concat(String paramString, Object[] paramArrayOfObject) {
    int i = paramArrayOfObject.length;
    if (i == 0)
      return paramString; 
    if (i == 1)
      return paramString.concat(ScriptRuntime.toString(paramArrayOfObject[0])); 
    int j = paramString.length();
    String[] arrayOfString = new String[i];
    int k;
    for (k = 0; k != i; k++) {
      String str = ScriptRuntime.toString(paramArrayOfObject[k]);
      arrayOfString[k] = str;
      j += str.length();
    } 
    StringBuilder stringBuilder = new StringBuilder(j);
    stringBuilder.append(paramString);
    for (k = 0; k != i; k++)
      stringBuilder.append(arrayOfString[k]); 
    return stringBuilder.toString();
  }
  
  private static int js_indexOf(int paramInt, String paramString, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: aload_2
    //   6: iconst_0
    //   7: invokestatic toString : ([Ljava/lang/Object;I)Ljava/lang/String;
    //   10: astore #5
    //   12: aload_2
    //   13: iconst_1
    //   14: invokestatic toInteger : ([Ljava/lang/Object;I)D
    //   17: dstore #6
    //   19: dload #6
    //   21: aload_1
    //   22: invokevirtual length : ()I
    //   25: i2d
    //   26: dcmpl
    //   27: ifle -> 44
    //   30: iload_0
    //   31: bipush #41
    //   33: if_icmpeq -> 44
    //   36: iload_0
    //   37: bipush #42
    //   39: if_icmpeq -> 44
    //   42: iconst_m1
    //   43: ireturn
    //   44: dload #6
    //   46: dconst_0
    //   47: dcmpg
    //   48: ifge -> 57
    //   51: dconst_0
    //   52: dstore #8
    //   54: goto -> 118
    //   57: dload #6
    //   59: aload_1
    //   60: invokevirtual length : ()I
    //   63: i2d
    //   64: dcmpl
    //   65: ifle -> 78
    //   68: aload_1
    //   69: invokevirtual length : ()I
    //   72: i2d
    //   73: dstore #8
    //   75: goto -> 118
    //   78: dload #6
    //   80: dstore #8
    //   82: iload_0
    //   83: bipush #42
    //   85: if_icmpne -> 118
    //   88: dload #6
    //   90: dload #6
    //   92: dcmpl
    //   93: ifne -> 111
    //   96: dload #6
    //   98: dstore #8
    //   100: dload #6
    //   102: aload_1
    //   103: invokevirtual length : ()I
    //   106: i2d
    //   107: dcmpl
    //   108: ifle -> 118
    //   111: aload_1
    //   112: invokevirtual length : ()I
    //   115: i2d
    //   116: dstore #8
    //   118: bipush #42
    //   120: iload_0
    //   121: if_icmpne -> 191
    //   124: aload_2
    //   125: arraylength
    //   126: ifeq -> 158
    //   129: aload_2
    //   130: arraylength
    //   131: iconst_1
    //   132: if_icmpeq -> 158
    //   135: dload #8
    //   137: dstore #6
    //   139: aload_2
    //   140: arraylength
    //   141: iconst_2
    //   142: if_icmpne -> 165
    //   145: dload #8
    //   147: dstore #6
    //   149: aload_2
    //   150: iconst_1
    //   151: aaload
    //   152: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   155: if_acmpne -> 165
    //   158: aload_1
    //   159: invokevirtual length : ()I
    //   162: i2d
    //   163: dstore #6
    //   165: aload_1
    //   166: iconst_0
    //   167: dload #6
    //   169: d2i
    //   170: invokevirtual substring : (II)Ljava/lang/String;
    //   173: aload #5
    //   175: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   178: ifeq -> 187
    //   181: iload #4
    //   183: istore_0
    //   184: goto -> 189
    //   187: iconst_m1
    //   188: istore_0
    //   189: iload_0
    //   190: ireturn
    //   191: iload_0
    //   192: bipush #41
    //   194: if_icmpne -> 219
    //   197: aload_1
    //   198: aload #5
    //   200: dload #8
    //   202: d2i
    //   203: invokevirtual startsWith : (Ljava/lang/String;I)Z
    //   206: ifeq -> 214
    //   209: iload_3
    //   210: istore_0
    //   211: goto -> 229
    //   214: iconst_m1
    //   215: istore_0
    //   216: goto -> 229
    //   219: aload_1
    //   220: aload #5
    //   222: dload #8
    //   224: d2i
    //   225: invokevirtual indexOf : (Ljava/lang/String;I)I
    //   228: istore_0
    //   229: iload_0
    //   230: ireturn
  }
  
  private static int js_lastIndexOf(String paramString, Object[] paramArrayOfObject) {
    String str = ScriptRuntime.toString(paramArrayOfObject, 0);
    double d1 = ScriptRuntime.toNumber(paramArrayOfObject, 1);
    if (d1 != d1 || d1 > paramString.length()) {
      double d = paramString.length();
      return paramString.lastIndexOf(str, (int)d);
    } 
    double d2 = d1;
    if (d1 < 0.0D)
      d2 = 0.0D; 
    return paramString.lastIndexOf(str, (int)d2);
  }
  
  private static String js_pad(Context paramContext, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject, Object[] paramArrayOfObject, Boolean paramBoolean) {
    String str3 = ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible(paramContext, paramScriptable, paramIdFunctionObject));
    long l = ScriptRuntime.toLength(paramArrayOfObject, 0);
    if (l <= str3.length())
      return str3; 
    String str2 = " ";
    String str1 = str2;
    if (paramArrayOfObject.length >= 2) {
      str1 = str2;
      if (!Undefined.isUndefined(paramArrayOfObject[1])) {
        str2 = ScriptRuntime.toString(paramArrayOfObject[1]);
        str1 = str2;
        if (str2.length() < 1)
          return str3; 
      } 
    } 
    int i = (int)(l - str3.length());
    StringBuilder stringBuilder = new StringBuilder();
    while (true) {
      stringBuilder.append(str1);
      if (stringBuilder.length() >= i) {
        stringBuilder.setLength(i);
        if (paramBoolean.booleanValue()) {
          stringBuilder.append(str3);
          return stringBuilder.toString();
        } 
        return stringBuilder.insert(0, str3).toString();
      } 
    } 
  }
  
  private static String js_repeat(Context paramContext, Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject, Object[] paramArrayOfObject) {
    String str = ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible(paramContext, paramScriptable, paramIdFunctionObject));
    double d = ScriptRuntime.toInteger(paramArrayOfObject, 0);
    if (d >= 0.0D && d != Double.POSITIVE_INFINITY) {
      if (d == 0.0D || str.length() == 0)
        return ""; 
      long l = str.length() * (long)d;
      if (d <= 2.147483647E9D && l <= 2147483647L) {
        StringBuilder stringBuilder = new StringBuilder((int)l);
        stringBuilder.append(str);
        int i = 1;
        int j = (int)d;
        while (i <= j / 2) {
          stringBuilder.append(stringBuilder);
          i *= 2;
        } 
        if (i < j)
          stringBuilder.append(stringBuilder.substring(0, str.length() * (j - i))); 
        return stringBuilder.toString();
      } 
      throw ScriptRuntime.rangeError("Invalid size or count value");
    } 
    throw ScriptRuntime.rangeError("Invalid count value");
  }
  
  private static CharSequence js_slice(CharSequence paramCharSequence, Object[] paramArrayOfObject) {
    double d1;
    double d2;
    if (paramArrayOfObject.length < 1) {
      d1 = 0.0D;
    } else {
      d1 = ScriptRuntime.toInteger(paramArrayOfObject[0]);
    } 
    int i = paramCharSequence.length();
    if (d1 < 0.0D) {
      d1 += i;
      d2 = d1;
      if (d1 < 0.0D)
        d2 = 0.0D; 
    } else {
      d2 = d1;
      if (d1 > i)
        d2 = i; 
    } 
    if (paramArrayOfObject.length < 2 || paramArrayOfObject[1] == Undefined.instance) {
      double d = i;
      return paramCharSequence.subSequence((int)d2, (int)d);
    } 
    double d3 = ScriptRuntime.toInteger(paramArrayOfObject[1]);
    if (d3 < 0.0D) {
      d3 += i;
      d1 = d3;
      if (d3 < 0.0D)
        d1 = 0.0D; 
    } else {
      d1 = d3;
      if (d3 > i)
        d1 = i; 
    } 
    d3 = d1;
    if (d1 < d2)
      d3 = d2; 
    return paramCharSequence.subSequence((int)d2, (int)d3);
  }
  
  private static CharSequence js_substr(CharSequence paramCharSequence, Object[] paramArrayOfObject) {
    double d2;
    if (paramArrayOfObject.length < 1)
      return paramCharSequence; 
    double d1 = ScriptRuntime.toInteger(paramArrayOfObject[0]);
    int i = paramCharSequence.length();
    if (d1 < 0.0D) {
      d1 += i;
      d2 = d1;
      if (d1 < 0.0D)
        d2 = 0.0D; 
    } else {
      d2 = d1;
      if (d1 > i)
        d2 = i; 
    } 
    if (paramArrayOfObject.length == 1) {
      d1 = i;
    } else {
      double d = ScriptRuntime.toInteger(paramArrayOfObject[1]);
      d1 = d;
      if (d < 0.0D)
        d1 = 0.0D; 
      d = d1 + d2;
      d1 = d;
      if (d > i)
        d1 = i; 
    } 
    return paramCharSequence.subSequence((int)d2, (int)d1);
  }
  
  private static CharSequence js_substring(Context paramContext, CharSequence paramCharSequence, Object[] paramArrayOfObject) {
    double d2;
    int i = paramCharSequence.length();
    double d1 = ScriptRuntime.toInteger(paramArrayOfObject, 0);
    if (d1 < 0.0D) {
      d2 = 0.0D;
    } else {
      d2 = d1;
      if (d1 > i)
        d2 = i; 
    } 
    if (paramArrayOfObject.length <= 1 || paramArrayOfObject[1] == Undefined.instance) {
      double d5 = i;
      double d6 = d2;
      return paramCharSequence.subSequence((int)d6, (int)d5);
    } 
    double d3 = ScriptRuntime.toInteger(paramArrayOfObject[1]);
    if (d3 < 0.0D) {
      d1 = 0.0D;
    } else {
      d1 = d3;
      if (d3 > i)
        d1 = i; 
    } 
    double d4 = d2;
    d3 = d1;
    if (d1 < d2)
      if (paramContext.getLanguageVersion() != 120) {
        d4 = d1;
        d3 = d2;
      } else {
        d3 = d2;
        d4 = d2;
      }  
    return paramCharSequence.subSequence((int)d4, (int)d3);
  }
  
  private static NativeString realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject) {
    if (paramScriptable instanceof NativeString)
      return (NativeString)paramScriptable; 
    throw incompatibleCallError(paramIdFunctionObject);
  }
  
  private static String tagify(Object paramObject, String paramString1, String paramString2, Object[] paramArrayOfObject) {
    String str = ScriptRuntime.toString(paramObject);
    paramObject = new StringBuilder();
    paramObject.append('<');
    paramObject.append(paramString1);
    if (paramString2 != null) {
      paramObject.append(' ');
      paramObject.append(paramString2);
      paramObject.append("=\"");
      paramObject.append(ScriptRuntime.toString(paramArrayOfObject, 0));
      paramObject.append('"');
    } 
    paramObject.append('>');
    paramObject.append(str);
    paramObject.append("</");
    paramObject.append(paramString1);
    paramObject.append('>');
    return paramObject.toString();
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(STRING_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    while (true) {
      boolean bool1 = true;
      boolean bool2 = true;
      boolean bool3 = true;
      boolean bool4 = true;
      if (i != -1) {
        String str3;
        char[] arrayOfChar1;
        String str2;
        Collator collator;
        String str1;
        CharSequence charSequence1;
        StringBuilder stringBuilder1;
        String str5;
        char[] arrayOfChar2;
        String str4;
        CharSequence charSequence2;
        String str7;
        Normalizer.Form form;
        String str6;
        double d;
        int k;
        char c;
        switch (i) {
          default:
            switch (i) {
              default:
                switch (i) {
                  default:
                    stringBuilder1 = new StringBuilder();
                    stringBuilder1.append("String.prototype has no method: ");
                    stringBuilder1.append(paramIdFunctionObject.getFunctionName());
                    throw new IllegalArgumentException(stringBuilder1.toString());
                  case 48:
                    return new NativeStringIterator(paramScriptable1, paramScriptable2);
                  case 45:
                    str3 = ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible((Context)stringBuilder1, paramScriptable2, paramIdFunctionObject));
                    d = ScriptRuntime.toInteger(paramArrayOfObject, 0);
                    return (d < 0.0D || d >= str3.length()) ? Undefined.instance : Integer.valueOf(str3.codePointAt((int)d));
                  case 44:
                    return js_repeat((Context)stringBuilder1, paramScriptable2, (IdFunctionObject)str3, paramArrayOfObject);
                  case 43:
                    str7 = ScriptRuntime.toString(paramArrayOfObject, 0);
                    if (Normalizer.Form.NFD.name().equals(str7)) {
                      form = Normalizer.Form.NFD;
                    } else if (Normalizer.Form.NFKC.name().equals(form)) {
                      form = Normalizer.Form.NFKC;
                    } else if (Normalizer.Form.NFKD.name().equals(form)) {
                      form = Normalizer.Form.NFKD;
                    } else {
                      if (Normalizer.Form.NFC.name().equals(form) || paramArrayOfObject.length == 0) {
                        form = Normalizer.Form.NFC;
                        return Normalizer.normalize(ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible((Context)stringBuilder1, paramScriptable2, (IdFunctionObject)str3)), form);
                      } 
                      throw ScriptRuntime.rangeError("The normalization form should be one of NFC, NFD, NFKC, NFKD");
                    } 
                    return Normalizer.normalize(ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible((Context)stringBuilder1, paramScriptable2, (IdFunctionObject)str3)), form);
                  case 40:
                  case 41:
                  case 42:
                    str6 = ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible((Context)stringBuilder1, paramScriptable2, (IdFunctionObject)str3));
                    if (paramArrayOfObject.length <= 0 || !(paramArrayOfObject[0] instanceof com.trendmicro.hippo.regexp.NativeRegExp)) {
                      int m = js_indexOf(i, str6, paramArrayOfObject);
                      if (i == 40) {
                        if (m == -1)
                          bool4 = false; 
                        return Boolean.valueOf(bool4);
                      } 
                      if (i == 41) {
                        if (m == 0) {
                          bool4 = bool1;
                        } else {
                          bool4 = false;
                        } 
                        return Boolean.valueOf(bool4);
                      } 
                      if (i == 42) {
                        if (m != -1) {
                          bool4 = bool2;
                        } else {
                          bool4 = false;
                        } 
                        return Boolean.valueOf(bool4);
                      } 
                    } else {
                      throw ScriptRuntime.typeError2("msg.first.arg.not.regexp", String.class.getSimpleName(), str3.getFunctionName());
                    } 
                  case 46:
                  case 47:
                    if (i == 46) {
                      bool4 = bool3;
                    } else {
                      bool4 = false;
                    } 
                    return js_pad((Context)stringBuilder1, paramScriptable2, (IdFunctionObject)str3, paramArrayOfObject, Boolean.valueOf(bool4));
                  case 39:
                    str5 = ScriptRuntime.toString(paramScriptable2);
                    arrayOfChar1 = str5.toCharArray();
                    for (i = arrayOfChar1.length; i > 0 && ScriptRuntime.isJSWhitespaceOrLineTerminator(arrayOfChar1[i - 1]); i--);
                    return str5.substring(0, i);
                  case 38:
                    str2 = ScriptRuntime.toString(paramScriptable2);
                    arrayOfChar2 = str2.toCharArray();
                    for (i = 0; i < arrayOfChar2.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(arrayOfChar2[i]); i++);
                    return str2.substring(i, arrayOfChar2.length);
                  case 37:
                    str2 = ScriptRuntime.toString(ScriptRuntimeES6.requireObjectCoercible((Context)arrayOfChar2, paramScriptable2, (IdFunctionObject)str2));
                    arrayOfChar2 = str2.toCharArray();
                    for (i = 0; i < arrayOfChar2.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(arrayOfChar2[i]); i++);
                    for (k = arrayOfChar2.length; k > i && ScriptRuntime.isJSWhitespaceOrLineTerminator(arrayOfChar2[k - 1]); k--);
                    return str2.substring(i, k);
                  case 36:
                    return ScriptRuntime.toString(paramScriptable2).toUpperCase(arrayOfChar2.getLocale());
                  case 35:
                    return ScriptRuntime.toString(paramScriptable2).toLowerCase(arrayOfChar2.getLocale());
                  case 34:
                    collator = Collator.getInstance(arrayOfChar2.getLocale());
                    collator.setStrength(3);
                    collator.setDecomposition(1);
                    return ScriptRuntime.wrapNumber(collator.compare(ScriptRuntime.toString(paramScriptable2), ScriptRuntime.toString(paramArrayOfObject, 0)));
                  case 31:
                  case 32:
                  case 33:
                    if (i == 31) {
                      i = 1;
                    } else if (i == 32) {
                      i = 3;
                    } else {
                      i = 2;
                    } 
                    return ScriptRuntime.checkRegExpProxy((Context)arrayOfChar2).action((Context)arrayOfChar2, (Scriptable)str6, paramScriptable2, paramArrayOfObject, i);
                  case 29:
                  case 30:
                    str1 = ScriptRuntime.toString(paramScriptable2);
                    str4 = ScriptRuntime.toString(paramArrayOfObject, 0);
                    if (i == 29) {
                      bool4 = str1.equals(str4);
                    } else {
                      bool4 = str1.equalsIgnoreCase(str4);
                    } 
                    return ScriptRuntime.wrapBoolean(bool4);
                  case 28:
                    return tagify(paramScriptable2, "a", "name", paramArrayOfObject);
                  case 27:
                    return tagify(paramScriptable2, "a", "href", paramArrayOfObject);
                  case 26:
                    return tagify(paramScriptable2, "font", "color", paramArrayOfObject);
                  case 25:
                    return tagify(paramScriptable2, "font", "size", paramArrayOfObject);
                  case 24:
                    return tagify(paramScriptable2, "sub", (String)null, (Object[])null);
                  case 23:
                    return tagify(paramScriptable2, "sup", (String)null, (Object[])null);
                  case 22:
                    return tagify(paramScriptable2, "blink", (String)null, (Object[])null);
                  case 21:
                    return tagify(paramScriptable2, "big", (String)null, (Object[])null);
                  case 20:
                    return tagify(paramScriptable2, "small", (String)null, (Object[])null);
                  case 19:
                    return tagify(paramScriptable2, "strike", (String)null, (Object[])null);
                  case 18:
                    return tagify(paramScriptable2, "tt", (String)null, (Object[])null);
                  case 17:
                    return tagify(paramScriptable2, "i", (String)null, (Object[])null);
                  case 16:
                    return tagify(paramScriptable2, "b", (String)null, (Object[])null);
                  case 15:
                    return js_slice(ScriptRuntime.toCharSequence(paramScriptable2), paramArrayOfObject);
                  case 14:
                    return js_concat(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
                  case 13:
                    return js_substr(ScriptRuntime.toCharSequence(paramScriptable2), paramArrayOfObject);
                  case 12:
                    return ScriptRuntime.toString(paramScriptable2).toUpperCase(ScriptRuntime.ROOT_LOCALE);
                  case 11:
                    return ScriptRuntime.toString(paramScriptable2).toLowerCase(ScriptRuntime.ROOT_LOCALE);
                  case 10:
                    return js_substring((Context)str4, ScriptRuntime.toCharSequence(paramScriptable2), paramArrayOfObject);
                  case 9:
                    return ScriptRuntime.checkRegExpProxy((Context)str4).js_split((Context)str4, (Scriptable)str6, ScriptRuntime.toString(paramScriptable2), paramArrayOfObject);
                  case 8:
                    return ScriptRuntime.wrapInt(js_lastIndexOf(ScriptRuntime.toString(paramScriptable2), paramArrayOfObject));
                  case 7:
                    return ScriptRuntime.wrapInt(js_indexOf(7, ScriptRuntime.toString(paramScriptable2), paramArrayOfObject));
                  case 5:
                  case 6:
                    charSequence1 = ScriptRuntime.toCharSequence(paramScriptable2);
                    d = ScriptRuntime.toInteger(paramArrayOfObject, 0);
                    if (d < 0.0D || d >= charSequence1.length())
                      return (i == 5) ? "" : ScriptRuntime.NaNobj; 
                    c = charSequence1.charAt((int)d);
                    return (i == 5) ? String.valueOf(c) : ScriptRuntime.wrapInt(c);
                  case 3:
                    charSequence2 = (realThis(paramScriptable2, (IdFunctionObject)charSequence1)).string;
                    charSequence1 = new StringBuilder();
                    charSequence1.append("(new String(\"");
                    charSequence1.append(ScriptRuntime.escapeString(charSequence2.toString()));
                    charSequence1.append("\"))");
                    return charSequence1.toString();
                  case 2:
                  case 4:
                    charSequence1 = (realThis(paramScriptable2, (IdFunctionObject)charSequence1)).string;
                    if (!(charSequence1 instanceof String))
                      charSequence1 = charSequence1.toString(); 
                    return charSequence1;
                  case 1:
                    break;
                } 
                if (paramArrayOfObject.length == 0) {
                  charSequence1 = "";
                } else if (ScriptRuntime.isSymbol(paramArrayOfObject[0]) && paramScriptable2 != null) {
                  charSequence1 = paramArrayOfObject[0].toString();
                } else {
                  charSequence1 = ScriptRuntime.toCharSequence(paramArrayOfObject[0]);
                } 
                if (paramScriptable2 == null)
                  return new NativeString(charSequence1); 
                if (!(charSequence1 instanceof String))
                  charSequence1 = charSequence1.toString(); 
                return charSequence1;
              case -15:
              case -14:
              case -13:
              case -12:
              case -11:
              case -10:
              case -9:
              case -8:
              case -7:
              case -6:
              case -5:
                break;
            } 
            break;
          case -35:
          case -34:
          case -33:
          case -32:
          case -31:
          case -30:
            break;
        } 
        if (paramArrayOfObject.length > 0) {
          paramScriptable2 = ScriptRuntime.toObject((Context)charSequence2, (Scriptable)str6, ScriptRuntime.toCharSequence(paramArrayOfObject[0]));
          Object[] arrayOfObject = new Object[paramArrayOfObject.length - 1];
          for (k = 0; k < arrayOfObject.length; k++)
            arrayOfObject[k] = paramArrayOfObject[k + 1]; 
          paramArrayOfObject = arrayOfObject;
        } else {
          paramScriptable2 = ScriptRuntime.toObject((Context)charSequence2, (Scriptable)str6, ScriptRuntime.toCharSequence(paramScriptable2));
        } 
        i = -i;
        continue;
      } 
      int j = paramArrayOfObject.length;
      if (j < 1)
        return ""; 
      StringBuilder stringBuilder = new StringBuilder(j);
      for (i = 0; i != j; i++)
        stringBuilder.append(ScriptRuntime.toUint16(paramArrayOfObject[i])); 
      return stringBuilder.toString();
    } 
  }
  
  protected void fillConstructorProperties(IdFunctionObject paramIdFunctionObject) {
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -1, "fromCharCode", 1);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -5, "charAt", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -6, "charCodeAt", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -7, "indexOf", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -8, "lastIndexOf", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -9, "split", 3);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -10, "substring", 3);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -11, "toLowerCase", 1);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -12, "toUpperCase", 1);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -13, "substr", 3);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -14, "concat", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -15, "slice", 3);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -30, "equalsIgnoreCase", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -31, "match", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -32, "search", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -33, "replace", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -34, "localeCompare", 2);
    addIdFunctionProperty(paramIdFunctionObject, STRING_TAG, -35, "toLocaleLowerCase", 1);
    super.fillConstructorProperties(paramIdFunctionObject);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    return paramString.equals("length") ? instanceIdInfo(7, 1) : super.findInstanceIdInfo(paramString);
  }
  
  protected int findPrototypeId(Symbol paramSymbol) {
    return SymbolKey.ITERATOR.equals(paramSymbol) ? 48 : 0;
  }
  
  protected int findPrototypeId(String paramString) {
    char c2;
    String str2;
    char c;
    char c1 = Character.MIN_VALUE;
    String str1 = null;
    switch (paramString.length()) {
      default:
        c2 = c1;
        str2 = str1;
        break;
      case 17:
        c = paramString.charAt(8);
        if (c == 'L') {
          str2 = "toLocaleLowerCase";
          c2 = '#';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'U') {
          str2 = "toLocaleUpperCase";
          c2 = '$';
        } 
        break;
      case 16:
        str2 = "equalsIgnoreCase";
        c2 = '\036';
        break;
      case 13:
        str2 = "localeCompare";
        c2 = '"';
        break;
      case 11:
        c2 = paramString.charAt(2);
        if (c2 != 'L') {
          if (c2 != 'U') {
            if (c2 != 'd') {
              if (c2 != 'n') {
                if (c2 != 's') {
                  c2 = c1;
                  str2 = str1;
                  break;
                } 
                str2 = "lastIndexOf";
                c2 = '\b';
                break;
              } 
              str2 = "constructor";
              c2 = '\001';
              break;
            } 
            str2 = "codePointAt";
            c2 = '-';
            break;
          } 
          str2 = "toUpperCase";
          c2 = '\f';
          break;
        } 
        str2 = "toLowerCase";
        c2 = '\013';
        break;
      case 10:
        c = paramString.charAt(0);
        if (c == 'c') {
          str2 = "charCodeAt";
          c2 = '\006';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 's') {
          str2 = "startsWith";
          c2 = ')';
        } 
        break;
      case 9:
        c2 = paramString.charAt(0);
        if (c2 != 'f') {
          if (c2 != 'n') {
            if (c2 != 's') {
              if (c2 != 't') {
                c2 = c1;
                str2 = str1;
                break;
              } 
              str2 = "trimRight";
              c2 = '\'';
              break;
            } 
            str2 = "substring";
            c2 = '\n';
            break;
          } 
          str2 = "normalize";
          c2 = '+';
          break;
        } 
        str2 = "fontcolor";
        c2 = '\032';
        break;
      case 8:
        c2 = paramString.charAt(6);
        if (c2 != 'c') {
          if (c2 != 'n') {
            if (c2 != 'r') {
              if (c2 != 't') {
                if (c2 != 'z') {
                  if (c2 != 'e') {
                    if (c2 != 'f') {
                      c2 = c1;
                      str2 = str1;
                      break;
                    } 
                    str2 = "trimLeft";
                    c2 = '&';
                    break;
                  } 
                  str2 = "includes";
                  c2 = '(';
                  break;
                } 
                str2 = "fontsize";
                c2 = '\031';
                break;
              } 
              str2 = "endsWith";
              c2 = '*';
              break;
            } 
            str2 = "padStart";
            c2 = '.';
            break;
          } 
          str2 = "toString";
          c2 = '\002';
          break;
        } 
        str2 = "toSource";
        c2 = '\003';
        break;
      case 7:
        c2 = paramString.charAt(1);
        if (c2 != 'a') {
          if (c2 != 'e') {
            if (c2 != 'n') {
              if (c2 != 't') {
                c2 = c1;
                str2 = str1;
                break;
              } 
              str2 = "italics";
              c2 = '\021';
              break;
            } 
            str2 = "indexOf";
            c2 = '\007';
            break;
          } 
          str2 = "replace";
          c2 = '!';
          break;
        } 
        str2 = "valueOf";
        c2 = '\004';
        break;
      case 6:
        c2 = paramString.charAt(1);
        if (c2 != 'a') {
          if (c2 != 'e') {
            if (c2 != 'h') {
              if (c2 != 'q') {
                if (c2 != 'n') {
                  if (c2 != 'o') {
                    if (c2 != 't') {
                      if (c2 != 'u') {
                        c2 = c1;
                        str2 = str1;
                        break;
                      } 
                      str2 = "substr";
                      c2 = '\r';
                      break;
                    } 
                    str2 = "strike";
                    c2 = '\023';
                    break;
                  } 
                  str2 = "concat";
                  c2 = '\016';
                  break;
                } 
                str2 = "anchor";
                c2 = '\034';
                break;
              } 
              str2 = "equals";
              c2 = '\035';
              break;
            } 
            str2 = "charAt";
            c2 = '\005';
            break;
          } 
          c = paramString.charAt(0);
          if (c == 'r') {
            str2 = "repeat";
            c2 = ',';
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (c == 's') {
            str2 = "search";
            c2 = ' ';
          } 
          break;
        } 
        str2 = "padEnd";
        c2 = '/';
        break;
      case 5:
        c2 = paramString.charAt(4);
        if (c2 != 'd') {
          if (c2 != 'e') {
            if (c2 != 'h') {
              if (c2 != 't') {
                if (c2 != 'k') {
                  if (c2 != 'l') {
                    c2 = c1;
                    str2 = str1;
                    break;
                  } 
                  str2 = "small";
                  c2 = '\024';
                  break;
                } 
                str2 = "blink";
                c2 = '\026';
                break;
              } 
              str2 = "split";
              c2 = '\t';
              break;
            } 
            str2 = "match";
            c2 = '\037';
            break;
          } 
          str2 = "slice";
          c2 = '\017';
          break;
        } 
        str2 = "fixed";
        c2 = '\022';
        break;
      case 4:
        c = paramString.charAt(0);
        if (c == 'b') {
          str2 = "bold";
          c2 = '\020';
          break;
        } 
        if (c == 'l') {
          str2 = "link";
          c2 = '\033';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 't') {
          str2 = "trim";
          c2 = '%';
        } 
        break;
      case 3:
        c = paramString.charAt(2);
        if (c == 'b') {
          c2 = c1;
          str2 = str1;
          if (paramString.charAt(0) == 's') {
            c2 = c1;
            str2 = str1;
            if (paramString.charAt(1) == 'u')
              return 24; 
          } 
          break;
        } 
        if (c == 'g') {
          c2 = c1;
          str2 = str1;
          if (paramString.charAt(0) == 'b') {
            c2 = c1;
            str2 = str1;
            if (paramString.charAt(1) == 'i')
              return 21; 
          } 
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'p') {
          c2 = c1;
          str2 = str1;
          if (paramString.charAt(0) == 's') {
            c2 = c1;
            str2 = str1;
            if (paramString.charAt(1) == 'u')
              return 23; 
          } 
        } 
        break;
    } 
    c1 = c2;
    if (str2 != null) {
      c1 = c2;
      if (str2 != paramString) {
        c1 = c2;
        if (!str2.equals(paramString))
          c1 = Character.MIN_VALUE; 
      } 
    } 
    return c1;
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    return (paramInt >= 0 && paramInt < this.string.length()) ? String.valueOf(this.string.charAt(paramInt)) : super.get(paramInt, paramScriptable);
  }
  
  public String getClassName() {
    return "String";
  }
  
  protected Object[] getIds(boolean paramBoolean1, boolean paramBoolean2) {
    Context context = Context.getCurrentContext();
    if (context != null && context.getLanguageVersion() >= 200) {
      Object[] arrayOfObject1 = super.getIds(paramBoolean1, paramBoolean2);
      Object[] arrayOfObject2 = new Object[arrayOfObject1.length + this.string.length()];
      byte b;
      for (b = 0; b < this.string.length(); b++)
        arrayOfObject2[b] = Integer.valueOf(b); 
      System.arraycopy(arrayOfObject1, 0, arrayOfObject2, b, arrayOfObject1.length);
      return arrayOfObject2;
    } 
    return super.getIds(paramBoolean1, paramBoolean2);
  }
  
  protected String getInstanceIdName(int paramInt) {
    return (paramInt == 1) ? "length" : super.getInstanceIdName(paramInt);
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt == 1) ? ScriptRuntime.wrapInt(this.string.length()) : super.getInstanceIdValue(paramInt);
  }
  
  int getLength() {
    return this.string.length();
  }
  
  protected int getMaxInstanceId() {
    return 1;
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    return (paramInt >= 0 && paramInt < this.string.length()) ? true : super.has(paramInt, paramScriptable);
  }
  
  protected void initPrototypeId(int paramInt) {
    byte b;
    String str;
    if (paramInt == 48) {
      initPrototypeMethod(STRING_TAG, paramInt, SymbolKey.ITERATOR, "[Symbol.iterator]", 0);
      return;
    } 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException(String.valueOf(paramInt));
      case 47:
        b = 1;
        str = "padEnd";
        break;
      case 46:
        b = 1;
        str = "padStart";
        break;
      case 45:
        b = 1;
        str = "codePointAt";
        break;
      case 44:
        b = 1;
        str = "repeat";
        break;
      case 43:
        b = 0;
        str = "normalize";
        break;
      case 42:
        b = 1;
        str = "endsWith";
        break;
      case 41:
        b = 1;
        str = "startsWith";
        break;
      case 40:
        b = 1;
        str = "includes";
        break;
      case 39:
        b = 0;
        str = "trimRight";
        break;
      case 38:
        b = 0;
        str = "trimLeft";
        break;
      case 37:
        b = 0;
        str = "trim";
        break;
      case 36:
        b = 0;
        str = "toLocaleUpperCase";
        break;
      case 35:
        b = 0;
        str = "toLocaleLowerCase";
        break;
      case 34:
        b = 1;
        str = "localeCompare";
        break;
      case 33:
        b = 2;
        str = "replace";
        break;
      case 32:
        b = 1;
        str = "search";
        break;
      case 31:
        b = 1;
        str = "match";
        break;
      case 30:
        b = 1;
        str = "equalsIgnoreCase";
        break;
      case 29:
        b = 1;
        str = "equals";
        break;
      case 28:
        b = 0;
        str = "anchor";
        break;
      case 27:
        b = 0;
        str = "link";
        break;
      case 26:
        b = 0;
        str = "fontcolor";
        break;
      case 25:
        b = 0;
        str = "fontsize";
        break;
      case 24:
        b = 0;
        str = "sub";
        break;
      case 23:
        b = 0;
        str = "sup";
        break;
      case 22:
        b = 0;
        str = "blink";
        break;
      case 21:
        b = 0;
        str = "big";
        break;
      case 20:
        b = 0;
        str = "small";
        break;
      case 19:
        b = 0;
        str = "strike";
        break;
      case 18:
        b = 0;
        str = "fixed";
        break;
      case 17:
        b = 0;
        str = "italics";
        break;
      case 16:
        b = 0;
        str = "bold";
        break;
      case 15:
        b = 2;
        str = "slice";
        break;
      case 14:
        b = 1;
        str = "concat";
        break;
      case 13:
        b = 2;
        str = "substr";
        break;
      case 12:
        b = 0;
        str = "toUpperCase";
        break;
      case 11:
        b = 0;
        str = "toLowerCase";
        break;
      case 10:
        b = 2;
        str = "substring";
        break;
      case 9:
        b = 2;
        str = "split";
        break;
      case 8:
        b = 1;
        str = "lastIndexOf";
        break;
      case 7:
        b = 1;
        str = "indexOf";
        break;
      case 6:
        b = 1;
        str = "charCodeAt";
        break;
      case 5:
        b = 1;
        str = "charAt";
        break;
      case 4:
        b = 0;
        str = "valueOf";
        break;
      case 3:
        b = 0;
        str = "toSource";
        break;
      case 2:
        b = 0;
        str = "toString";
        break;
      case 1:
        b = 1;
        str = "constructor";
        break;
    } 
    initPrototypeMethod(STRING_TAG, paramInt, str, (String)null, b);
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    if (paramInt >= 0 && paramInt < this.string.length())
      return; 
    super.put(paramInt, paramScriptable, paramObject);
  }
  
  public CharSequence toCharSequence() {
    return this.string;
  }
  
  public String toString() {
    CharSequence charSequence = this.string;
    if (charSequence instanceof String) {
      charSequence = charSequence;
    } else {
      charSequence = charSequence.toString();
    } 
    return (String)charSequence;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */