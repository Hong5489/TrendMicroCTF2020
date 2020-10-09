package com.trendmicro.hippo.regexp;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.RegExpProxy;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.Undefined;

public class RegExpImpl implements RegExpProxy {
  protected String input;
  
  protected SubString lastMatch;
  
  protected SubString lastParen;
  
  protected SubString leftContext;
  
  protected boolean multiline;
  
  protected SubString[] parens;
  
  protected SubString rightContext;
  
  private static NativeRegExp createRegExp(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject, int paramInt, boolean paramBoolean) {
    NativeRegExp nativeRegExp;
    Scriptable scriptable = ScriptableObject.getTopLevelScope(paramScriptable);
    if (paramArrayOfObject.length == 0 || paramArrayOfObject[0] == Undefined.instance)
      return new NativeRegExp(scriptable, NativeRegExp.compileRE(paramContext, "", "", false)); 
    if (paramArrayOfObject[0] instanceof NativeRegExp) {
      nativeRegExp = (NativeRegExp)paramArrayOfObject[0];
    } else {
      String str = ScriptRuntime.toString(paramArrayOfObject[0]);
      if (paramInt < paramArrayOfObject.length) {
        paramArrayOfObject[0] = str;
        String str1 = ScriptRuntime.toString(paramArrayOfObject[paramInt]);
      } else {
        paramScriptable = null;
      } 
      nativeRegExp = new NativeRegExp(scriptable, NativeRegExp.compileRE((Context)nativeRegExp, str, (String)paramScriptable, paramBoolean));
    } 
    return nativeRegExp;
  }
  
  private static void do_replace(GlobData paramGlobData, Context paramContext, RegExpImpl paramRegExpImpl) {
    StringBuilder stringBuilder = paramGlobData.charBuf;
    int i = 0;
    int j = 0;
    String str = paramGlobData.repstr;
    int k = paramGlobData.dollar;
    if (k != -1) {
      int m;
      int[] arrayOfInt = new int[1];
      do {
        stringBuilder.append(str.substring(j, k));
        i = k;
        SubString subString = interpretDollar(paramContext, paramRegExpImpl, str, k, arrayOfInt);
        if (subString != null) {
          j = subString.length;
          if (j > 0)
            stringBuilder.append(subString.str, subString.index, subString.index + j); 
          i += arrayOfInt[0];
          k += arrayOfInt[0];
        } else {
          k++;
        } 
        m = str.indexOf('$', k);
        j = i;
        k = m;
      } while (m >= 0);
    } 
    k = str.length();
    if (k > i)
      stringBuilder.append(str.substring(i, k)); 
  }
  
  private static int find_split(Context paramContext, Scriptable paramScriptable1, String paramString1, String paramString2, int paramInt, RegExpProxy paramRegExpProxy, Scriptable paramScriptable2, int[] paramArrayOfint1, int[] paramArrayOfint2, boolean[] paramArrayOfboolean, String[][] paramArrayOfString) {
    int i = paramArrayOfint1[0];
    int j = paramString1.length();
    int k = -1;
    if (paramInt == 120 && paramScriptable2 == null && paramString2.length() == 1 && paramString2.charAt(0) == ' ') {
      k = i;
      if (i == 0) {
        for (k = i; k < j && Character.isWhitespace(paramString1.charAt(k)); k++);
        paramArrayOfint1[0] = k;
      } 
      paramInt = k;
      if (k == j)
        return -1; 
      while (paramInt < j && !Character.isWhitespace(paramString1.charAt(paramInt)))
        paramInt++; 
      for (k = paramInt; k < j && Character.isWhitespace(paramString1.charAt(k)); k++);
      paramArrayOfint2[0] = k - paramInt;
      return paramInt;
    } 
    if (i > j)
      return -1; 
    if (paramScriptable2 != null)
      return paramRegExpProxy.find_split(paramContext, paramScriptable1, paramString1, paramString2, paramScriptable2, paramArrayOfint1, paramArrayOfint2, paramArrayOfboolean, paramArrayOfString); 
    if (paramInt != 0 && paramInt < 130 && j == 0)
      return -1; 
    if (paramString2.length() == 0) {
      if (paramInt == 120) {
        if (i == j) {
          paramArrayOfint2[0] = 1;
          return i;
        } 
        return i + 1;
      } 
      if (i == j) {
        paramInt = k;
      } else {
        paramInt = i + 1;
      } 
      return paramInt;
    } 
    if (paramArrayOfint1[0] >= j)
      return j; 
    paramInt = paramString1.indexOf(paramString2, paramArrayOfint1[0]);
    if (paramInt == -1)
      paramInt = j; 
    return paramInt;
  }
  
  private static SubString interpretDollar(Context paramContext, RegExpImpl paramRegExpImpl, String paramString, int paramInt, int[] paramArrayOfint) {
    if (paramString.charAt(paramInt) != '$')
      Kit.codeBug(); 
    int i = paramContext.getLanguageVersion();
    if (i != 0 && i <= 140 && paramInt > 0 && paramString.charAt(paramInt - 1) == '\\')
      return null; 
    int j = paramString.length();
    if (paramInt + 1 >= j)
      return null; 
    char c = paramString.charAt(paramInt + 1);
    if (NativeRegExp.isDigit(c)) {
      int k;
      int m;
      if (i != 0 && i <= 140) {
        if (c == '0')
          return null; 
        i = 0;
        int n = paramInt;
        while (true) {
          n++;
          k = i;
          m = n;
          if (n < j) {
            c = paramString.charAt(n);
            k = i;
            m = n;
            if (NativeRegExp.isDigit(c)) {
              m = i * 10 + c - 48;
              if (m < i) {
                k = i;
                m = n;
                break;
              } 
              i = m;
              continue;
            } 
          } 
          break;
        } 
      } else {
        SubString[] arrayOfSubString = paramRegExpImpl.parens;
        if (arrayOfSubString == null) {
          m = 0;
        } else {
          m = arrayOfSubString.length;
        } 
        int i1 = c - 48;
        if (i1 > m)
          return null; 
        int i2 = paramInt + 2;
        int n = i1;
        i = i2;
        if (paramInt + 2 < j) {
          c = paramString.charAt(paramInt + 2);
          n = i1;
          i = i2;
          if (NativeRegExp.isDigit(c)) {
            int i3 = i1 * 10 + c - 48;
            n = i1;
            i = i2;
            if (i3 <= m) {
              i = i2 + 1;
              n = i3;
            } 
          } 
        } 
        if (n == 0)
          return null; 
        m = i;
        k = n;
      } 
      paramArrayOfint[0] = m - paramInt;
      return paramRegExpImpl.getParenSubString(k - 1);
    } 
    paramArrayOfint[0] = 2;
    if (c != '$') {
      if (c != '+') {
        if (c != '`')
          return (c != '&') ? ((c != '\'') ? null : paramRegExpImpl.rightContext) : paramRegExpImpl.lastMatch; 
        if (i == 120) {
          paramRegExpImpl.leftContext.index = 0;
          paramRegExpImpl.leftContext.length = paramRegExpImpl.lastMatch.index;
        } 
        return paramRegExpImpl.leftContext;
      } 
      return paramRegExpImpl.lastParen;
    } 
    return new SubString("$");
  }
  
  private static Object matchOrReplace(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, RegExpImpl paramRegExpImpl, GlobData paramGlobData, NativeRegExp paramNativeRegExp) {
    // Byte code:
    //   0: aload #5
    //   2: getfield str : Ljava/lang/String;
    //   5: astore_3
    //   6: aload #6
    //   8: invokevirtual getFlags : ()I
    //   11: iconst_1
    //   12: iand
    //   13: ifeq -> 22
    //   16: iconst_1
    //   17: istore #7
    //   19: goto -> 25
    //   22: iconst_0
    //   23: istore #7
    //   25: aload #5
    //   27: iload #7
    //   29: putfield global : Z
    //   32: iconst_1
    //   33: newarray int
    //   35: astore #8
    //   37: aload #8
    //   39: iconst_0
    //   40: iconst_0
    //   41: iastore
    //   42: aload #5
    //   44: getfield mode : I
    //   47: iconst_3
    //   48: if_icmpne -> 102
    //   51: aload #6
    //   53: aload_0
    //   54: aload_1
    //   55: aload #4
    //   57: aload_3
    //   58: aload #8
    //   60: iconst_0
    //   61: invokevirtual executeRegExp : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/regexp/RegExpImpl;Ljava/lang/String;[II)Ljava/lang/Object;
    //   64: astore_0
    //   65: aload_0
    //   66: ifnull -> 94
    //   69: aload_0
    //   70: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   73: invokevirtual equals : (Ljava/lang/Object;)Z
    //   76: ifeq -> 94
    //   79: aload #4
    //   81: getfield leftContext : Lcom/trendmicro/hippo/regexp/SubString;
    //   84: getfield length : I
    //   87: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   90: astore_0
    //   91: goto -> 339
    //   94: iconst_m1
    //   95: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   98: astore_0
    //   99: goto -> 339
    //   102: aload #5
    //   104: getfield global : Z
    //   107: ifeq -> 306
    //   110: aload #6
    //   112: dconst_0
    //   113: invokestatic valueOf : (D)Ljava/lang/Double;
    //   116: putfield lastIndex : Ljava/lang/Object;
    //   119: aconst_null
    //   120: astore_2
    //   121: iconst_0
    //   122: istore #9
    //   124: aload #8
    //   126: iconst_0
    //   127: iaload
    //   128: aload_3
    //   129: invokevirtual length : ()I
    //   132: if_icmpgt -> 301
    //   135: aload #6
    //   137: aload_0
    //   138: aload_1
    //   139: aload #4
    //   141: aload_3
    //   142: aload #8
    //   144: iconst_0
    //   145: invokevirtual executeRegExp : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/regexp/RegExpImpl;Ljava/lang/String;[II)Ljava/lang/Object;
    //   148: astore_2
    //   149: aload_2
    //   150: ifnull -> 296
    //   153: aload_2
    //   154: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   157: invokevirtual equals : (Ljava/lang/Object;)Z
    //   160: ifne -> 166
    //   163: goto -> 296
    //   166: aload #5
    //   168: getfield mode : I
    //   171: iconst_1
    //   172: if_icmpne -> 189
    //   175: aload #5
    //   177: aload_0
    //   178: aload_1
    //   179: iload #9
    //   181: aload #4
    //   183: invokestatic match_glob : (Lcom/trendmicro/hippo/regexp/GlobData;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;ILcom/trendmicro/hippo/regexp/RegExpImpl;)V
    //   186: goto -> 255
    //   189: aload #5
    //   191: getfield mode : I
    //   194: iconst_2
    //   195: if_icmpeq -> 202
    //   198: invokestatic codeBug : ()Ljava/lang/RuntimeException;
    //   201: pop
    //   202: aload #4
    //   204: getfield lastMatch : Lcom/trendmicro/hippo/regexp/SubString;
    //   207: astore #10
    //   209: aload #5
    //   211: getfield leftIndex : I
    //   214: istore #11
    //   216: aload #10
    //   218: getfield index : I
    //   221: istore #12
    //   223: aload #5
    //   225: aload #10
    //   227: getfield index : I
    //   230: aload #10
    //   232: getfield length : I
    //   235: iadd
    //   236: putfield leftIndex : I
    //   239: aload #5
    //   241: aload_0
    //   242: aload_1
    //   243: aload #4
    //   245: iload #11
    //   247: iload #12
    //   249: iload #11
    //   251: isub
    //   252: invokestatic replace_glob : (Lcom/trendmicro/hippo/regexp/GlobData;Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/regexp/RegExpImpl;II)V
    //   255: aload #4
    //   257: getfield lastMatch : Lcom/trendmicro/hippo/regexp/SubString;
    //   260: getfield length : I
    //   263: ifne -> 290
    //   266: aload #8
    //   268: iconst_0
    //   269: iaload
    //   270: aload_3
    //   271: invokevirtual length : ()I
    //   274: if_icmpne -> 280
    //   277: goto -> 296
    //   280: aload #8
    //   282: iconst_0
    //   283: aload #8
    //   285: iconst_0
    //   286: iaload
    //   287: iconst_1
    //   288: iadd
    //   289: iastore
    //   290: iinc #9, 1
    //   293: goto -> 124
    //   296: aload_2
    //   297: astore_0
    //   298: goto -> 339
    //   301: aload_2
    //   302: astore_0
    //   303: goto -> 339
    //   306: aload #5
    //   308: getfield mode : I
    //   311: iconst_2
    //   312: if_icmpne -> 321
    //   315: iconst_0
    //   316: istore #9
    //   318: goto -> 324
    //   321: iconst_1
    //   322: istore #9
    //   324: aload #6
    //   326: aload_0
    //   327: aload_1
    //   328: aload #4
    //   330: aload_3
    //   331: aload #8
    //   333: iload #9
    //   335: invokevirtual executeRegExp : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Lcom/trendmicro/hippo/regexp/RegExpImpl;Ljava/lang/String;[II)Ljava/lang/Object;
    //   338: astore_0
    //   339: aload_0
    //   340: areturn
  }
  
  private static void match_glob(GlobData paramGlobData, Context paramContext, Scriptable paramScriptable, int paramInt, RegExpImpl paramRegExpImpl) {
    if (paramGlobData.arrayobj == null)
      paramGlobData.arrayobj = paramContext.newArray(paramScriptable, 0); 
    String str = paramRegExpImpl.lastMatch.toString();
    paramGlobData.arrayobj.put(paramInt, paramGlobData.arrayobj, str);
  }
  
  private static void replace_glob(GlobData paramGlobData, Context paramContext, Scriptable paramScriptable, RegExpImpl paramRegExpImpl, int paramInt1, int paramInt2) {
    Scriptable scriptable;
    if (paramGlobData.lambda != null) {
      SubString[] arrayOfSubString = paramRegExpImpl.parens;
      if (arrayOfSubString == null) {
        i = 0;
      } else {
        i = arrayOfSubString.length;
      } 
      Object[] arrayOfObject = new Object[i + 3];
      arrayOfObject[0] = paramRegExpImpl.lastMatch.toString();
      for (byte b = 0; b < i; b++) {
        SubString subString = arrayOfSubString[b];
        if (subString != null) {
          arrayOfObject[b + 1] = subString.toString();
        } else {
          arrayOfObject[b + 1] = Undefined.instance;
        } 
      } 
      arrayOfObject[i + 1] = Integer.valueOf(paramRegExpImpl.leftContext.length);
      arrayOfObject[i + 2] = paramGlobData.str;
      if (paramRegExpImpl != ScriptRuntime.getRegExpProxy(paramContext))
        Kit.codeBug(); 
      RegExpImpl regExpImpl = new RegExpImpl();
      regExpImpl.multiline = paramRegExpImpl.multiline;
      regExpImpl.input = paramRegExpImpl.input;
      ScriptRuntime.setRegExpProxy(paramContext, regExpImpl);
      try {
        paramScriptable = ScriptableObject.getTopLevelScope(paramScriptable);
        String str = ScriptRuntime.toString(paramGlobData.lambda.call(paramContext, paramScriptable, paramScriptable, arrayOfObject));
        ScriptRuntime.setRegExpProxy(paramContext, paramRegExpImpl);
      } finally {
        ScriptRuntime.setRegExpProxy(paramContext, paramRegExpImpl);
      } 
    } else {
      Scriptable scriptable1 = null;
      int j = paramGlobData.repstr.length();
      i = j;
      paramScriptable = scriptable1;
      if (paramGlobData.dollar >= 0) {
        int[] arrayOfInt = new int[1];
        int k = paramGlobData.dollar;
        while (true) {
          SubString subString = interpretDollar(paramContext, paramRegExpImpl, paramGlobData.repstr, k, arrayOfInt);
          if (subString != null) {
            i = j + subString.length - arrayOfInt[0];
            k += arrayOfInt[0];
          } else {
            k++;
            i = j;
          } 
          int m = paramGlobData.repstr.indexOf('$', k);
          j = i;
          k = m;
          if (m < 0) {
            scriptable = scriptable1;
            break;
          } 
        } 
      } 
    } 
    int i = paramInt2 + i + paramRegExpImpl.rightContext.length;
    StringBuilder stringBuilder = paramGlobData.charBuf;
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder(i);
      paramGlobData.charBuf = stringBuilder;
    } else {
      stringBuilder.ensureCapacity(paramGlobData.charBuf.length() + i);
    } 
    stringBuilder.append(paramRegExpImpl.leftContext.str, paramInt1, paramInt1 + paramInt2);
    if (paramGlobData.lambda != null) {
      stringBuilder.append((String)scriptable);
    } else {
      do_replace(paramGlobData, paramContext, paramRegExpImpl);
    } 
  }
  
  public Object action(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject, int paramInt) {
    SubString subString;
    Object object2;
    GlobData globData = new GlobData();
    globData.mode = paramInt;
    globData.str = ScriptRuntime.toString(paramScriptable2);
    if (paramInt != 1) {
      NativeRegExp nativeRegExp;
      Object object3;
      Object object4;
      Function function;
      int i;
      if (paramInt != 2) {
        if (paramInt == 3)
          return matchOrReplace(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, this, globData, createRegExp(paramContext, paramScriptable1, paramArrayOfObject, 1, false)); 
        throw Kit.codeBug();
      } 
      if ((paramArrayOfObject.length > 0 && paramArrayOfObject[0] instanceof NativeRegExp) || paramArrayOfObject.length > 2) {
        paramInt = 1;
      } else {
        paramInt = 0;
      } 
      if (paramInt != 0) {
        nativeRegExp = createRegExp(paramContext, paramScriptable1, paramArrayOfObject, 2, true);
        object3 = null;
      } else {
        if (paramArrayOfObject.length < 1) {
          object3 = Undefined.instance;
        } else {
          object3 = paramArrayOfObject[0];
        } 
        object3 = ScriptRuntime.toString(object3);
        nativeRegExp = null;
      } 
      if (paramArrayOfObject.length < 2) {
        object4 = Undefined.instance;
      } else {
        object4 = paramArrayOfObject[1];
      } 
      if (object4 instanceof Function) {
        function = (Function)object4;
        object4 = null;
      } else {
        object4 = ScriptRuntime.toString(object4);
        function = null;
      } 
      globData.lambda = function;
      globData.repstr = (String)object4;
      if (object4 == null) {
        i = -1;
      } else {
        i = object4.indexOf('$');
      } 
      globData.dollar = i;
      globData.charBuf = null;
      globData.leftIndex = 0;
      if (paramInt != 0) {
        object2 = matchOrReplace(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject, this, globData, nativeRegExp);
      } else {
        object2 = globData.str;
        i = object2.indexOf((String)object3);
        if (i >= 0) {
          paramInt = object3.length();
          this.lastParen = null;
          this.leftContext = new SubString((String)object2, 0, i);
          this.lastMatch = new SubString((String)object2, i, paramInt);
          this.rightContext = new SubString((String)object2, i + paramInt, object2.length() - i - paramInt);
          Boolean bool = Boolean.TRUE;
        } else {
          object2 = Boolean.FALSE;
        } 
      } 
      if (globData.charBuf == null) {
        if (globData.global || object2 == null || !object2.equals(Boolean.TRUE))
          return globData.str; 
        object2 = this.leftContext;
        replace_glob(globData, paramContext, paramScriptable1, this, ((SubString)object2).index, ((SubString)object2).length);
      } 
      subString = this.rightContext;
      globData.charBuf.append(subString.str, subString.index, subString.index + subString.length);
      return globData.charBuf.toString();
    } 
    Object object1 = matchOrReplace((Context)subString, paramScriptable1, (Scriptable)object2, paramArrayOfObject, this, globData, createRegExp((Context)subString, paramScriptable1, paramArrayOfObject, 1, false));
    if (globData.arrayobj != null)
      object1 = globData.arrayobj; 
    return object1;
  }
  
  public Object compileRegExp(Context paramContext, String paramString1, String paramString2) {
    return NativeRegExp.compileRE(paramContext, paramString1, paramString2, false);
  }
  
  public int find_split(Context paramContext, Scriptable paramScriptable1, String paramString1, String paramString2, Scriptable paramScriptable2, int[] paramArrayOfint1, int[] paramArrayOfint2, boolean[] paramArrayOfboolean, String[][] paramArrayOfString) {
    int i = paramArrayOfint1[0];
    int j = paramString1.length();
    int k = paramContext.getLanguageVersion();
    NativeRegExp nativeRegExp = (NativeRegExp)paramScriptable2;
    while (true) {
      int m = paramArrayOfint1[0];
      paramArrayOfint1[0] = i;
      if (nativeRegExp.executeRegExp(paramContext, paramScriptable1, this, paramString1, paramArrayOfint1, 0) != Boolean.TRUE) {
        paramArrayOfint1[0] = m;
        paramArrayOfint2[0] = 1;
        paramArrayOfboolean[0] = false;
        return j;
      } 
      i = paramArrayOfint1[0];
      paramArrayOfint1[0] = m;
      paramArrayOfboolean[0] = true;
      paramArrayOfint2[0] = this.lastMatch.length;
      if (paramArrayOfint2[0] == 0 && i == paramArrayOfint1[0]) {
        if (i == j) {
          if (k == 120) {
            paramArrayOfint2[0] = 1;
            break;
          } 
          i = -1;
          break;
        } 
        i++;
        continue;
      } 
      i -= paramArrayOfint2[0];
      break;
    } 
    SubString[] arrayOfSubString = this.parens;
    if (arrayOfSubString == null) {
      k = 0;
    } else {
      k = arrayOfSubString.length;
    } 
    paramArrayOfString[0] = new String[k];
    for (j = 0; j < k; j++) {
      SubString subString = getParenSubString(j);
      paramArrayOfString[0][j] = subString.toString();
    } 
    return i;
  }
  
  SubString getParenSubString(int paramInt) {
    SubString[] arrayOfSubString = this.parens;
    if (arrayOfSubString != null && paramInt < arrayOfSubString.length) {
      SubString subString = arrayOfSubString[paramInt];
      if (subString != null)
        return subString; 
    } 
    return new SubString();
  }
  
  public boolean isRegExp(Scriptable paramScriptable) {
    return paramScriptable instanceof NativeRegExp;
  }
  
  public Object js_split(Context paramContext, Scriptable paramScriptable, String paramString, Object[] paramArrayOfObject) {
    // Byte code:
    //   0: aload_1
    //   1: aload_2
    //   2: iconst_0
    //   3: invokevirtual newArray : (Lcom/trendmicro/hippo/Scriptable;I)Lcom/trendmicro/hippo/Scriptable;
    //   6: astore #5
    //   8: aload #5
    //   10: astore #6
    //   12: aload #4
    //   14: arraylength
    //   15: iconst_1
    //   16: if_icmple -> 35
    //   19: aload #4
    //   21: iconst_1
    //   22: aaload
    //   23: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   26: if_acmpeq -> 35
    //   29: iconst_1
    //   30: istore #7
    //   32: goto -> 38
    //   35: iconst_0
    //   36: istore #7
    //   38: iload #7
    //   40: ifeq -> 78
    //   43: aload #4
    //   45: iconst_1
    //   46: aaload
    //   47: invokestatic toUint32 : (Ljava/lang/Object;)J
    //   50: lstore #8
    //   52: lload #8
    //   54: aload_3
    //   55: invokevirtual length : ()I
    //   58: i2l
    //   59: lcmp
    //   60: ifle -> 75
    //   63: aload_3
    //   64: invokevirtual length : ()I
    //   67: iconst_1
    //   68: iadd
    //   69: i2l
    //   70: lstore #8
    //   72: goto -> 81
    //   75: goto -> 81
    //   78: lconst_0
    //   79: lstore #8
    //   81: aload #4
    //   83: arraylength
    //   84: iconst_1
    //   85: if_icmplt -> 491
    //   88: aload #4
    //   90: iconst_0
    //   91: aaload
    //   92: getstatic com/trendmicro/hippo/Undefined.instance : Ljava/lang/Object;
    //   95: if_acmpne -> 101
    //   98: goto -> 491
    //   101: iconst_1
    //   102: newarray int
    //   104: astore #10
    //   106: aload #4
    //   108: iconst_0
    //   109: aaload
    //   110: instanceof com/trendmicro/hippo/Scriptable
    //   113: ifeq -> 177
    //   116: aload_1
    //   117: invokestatic getRegExpProxy : (Lcom/trendmicro/hippo/Context;)Lcom/trendmicro/hippo/RegExpProxy;
    //   120: astore #5
    //   122: aload #5
    //   124: ifnull -> 163
    //   127: aload #4
    //   129: iconst_0
    //   130: aaload
    //   131: checkcast com/trendmicro/hippo/Scriptable
    //   134: astore #11
    //   136: aload #5
    //   138: aload #11
    //   140: invokeinterface isRegExp : (Lcom/trendmicro/hippo/Scriptable;)Z
    //   145: ifeq -> 163
    //   148: aload #5
    //   150: astore #12
    //   152: aload #11
    //   154: astore #5
    //   156: aload #12
    //   158: astore #11
    //   160: goto -> 183
    //   163: aconst_null
    //   164: astore #12
    //   166: aload #5
    //   168: astore #11
    //   170: aload #12
    //   172: astore #5
    //   174: goto -> 183
    //   177: aconst_null
    //   178: astore #5
    //   180: aconst_null
    //   181: astore #11
    //   183: aload #5
    //   185: ifnonnull -> 209
    //   188: aload #4
    //   190: iconst_0
    //   191: aaload
    //   192: invokestatic toString : (Ljava/lang/Object;)Ljava/lang/String;
    //   195: astore #4
    //   197: aload #10
    //   199: iconst_0
    //   200: aload #4
    //   202: invokevirtual length : ()I
    //   205: iastore
    //   206: goto -> 212
    //   209: aconst_null
    //   210: astore #4
    //   212: iconst_1
    //   213: newarray int
    //   215: astore #13
    //   217: aload #13
    //   219: iconst_0
    //   220: iconst_0
    //   221: iastore
    //   222: iconst_1
    //   223: newarray boolean
    //   225: astore #14
    //   227: aload #14
    //   229: iconst_0
    //   230: iconst_0
    //   231: bastore
    //   232: iconst_1
    //   233: anewarray [Ljava/lang/String;
    //   236: astore #15
    //   238: aload #15
    //   240: iconst_0
    //   241: aconst_null
    //   242: aastore
    //   243: aload_1
    //   244: invokevirtual getLanguageVersion : ()I
    //   247: istore #16
    //   249: iconst_0
    //   250: istore #17
    //   252: aload #10
    //   254: astore #12
    //   256: aload_1
    //   257: aload_2
    //   258: aload_3
    //   259: aload #4
    //   261: iload #16
    //   263: aload #11
    //   265: aload #5
    //   267: aload #13
    //   269: aload #12
    //   271: aload #14
    //   273: aload #15
    //   275: invokestatic find_split : (Lcom/trendmicro/hippo/Context;Lcom/trendmicro/hippo/Scriptable;Ljava/lang/String;Ljava/lang/String;ILcom/trendmicro/hippo/RegExpProxy;Lcom/trendmicro/hippo/Scriptable;[I[I[Z[[Ljava/lang/String;)I
    //   278: istore #18
    //   280: iload #18
    //   282: iflt -> 488
    //   285: iload #7
    //   287: ifeq -> 299
    //   290: iload #17
    //   292: i2l
    //   293: lload #8
    //   295: lcmp
    //   296: ifge -> 488
    //   299: iload #18
    //   301: aload_3
    //   302: invokevirtual length : ()I
    //   305: if_icmple -> 311
    //   308: goto -> 488
    //   311: aload_3
    //   312: invokevirtual length : ()I
    //   315: ifne -> 324
    //   318: aload_3
    //   319: astore #10
    //   321: goto -> 336
    //   324: aload_3
    //   325: aload #13
    //   327: iconst_0
    //   328: iaload
    //   329: iload #18
    //   331: invokevirtual substring : (II)Ljava/lang/String;
    //   334: astore #10
    //   336: aload #6
    //   338: iload #17
    //   340: aload #6
    //   342: aload #10
    //   344: invokeinterface put : (ILcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
    //   349: iinc #17, 1
    //   352: aload #5
    //   354: ifnull -> 436
    //   357: aload #14
    //   359: iconst_0
    //   360: baload
    //   361: ifeq -> 436
    //   364: aload #15
    //   366: iconst_0
    //   367: aaload
    //   368: arraylength
    //   369: istore #19
    //   371: iconst_0
    //   372: istore #20
    //   374: iload #20
    //   376: iload #19
    //   378: if_icmpge -> 425
    //   381: iload #7
    //   383: ifeq -> 398
    //   386: iload #17
    //   388: i2l
    //   389: lload #8
    //   391: lcmp
    //   392: iflt -> 398
    //   395: goto -> 425
    //   398: aload #6
    //   400: iload #17
    //   402: aload #6
    //   404: aload #15
    //   406: iconst_0
    //   407: aaload
    //   408: iload #20
    //   410: aaload
    //   411: invokeinterface put : (ILcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
    //   416: iinc #17, 1
    //   419: iinc #20, 1
    //   422: goto -> 374
    //   425: iconst_0
    //   426: istore #20
    //   428: aload #14
    //   430: iconst_0
    //   431: iconst_0
    //   432: bastore
    //   433: goto -> 439
    //   436: iconst_0
    //   437: istore #20
    //   439: aload #13
    //   441: iload #20
    //   443: aload #12
    //   445: iload #20
    //   447: iaload
    //   448: iload #18
    //   450: iadd
    //   451: iastore
    //   452: iload #16
    //   454: sipush #130
    //   457: if_icmpge -> 485
    //   460: iload #16
    //   462: ifeq -> 485
    //   465: iload #7
    //   467: ifne -> 485
    //   470: aload #13
    //   472: iload #20
    //   474: iaload
    //   475: aload_3
    //   476: invokevirtual length : ()I
    //   479: if_icmpne -> 485
    //   482: goto -> 488
    //   485: goto -> 256
    //   488: aload #6
    //   490: areturn
    //   491: aload #5
    //   493: iconst_0
    //   494: aload #5
    //   496: aload_3
    //   497: invokeinterface put : (ILcom/trendmicro/hippo/Scriptable;Ljava/lang/Object;)V
    //   502: aload #5
    //   504: areturn
  }
  
  public Scriptable wrapRegExp(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    return (Scriptable)new NativeRegExp(paramScriptable, (RECompiled)paramObject);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/RegExpImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */