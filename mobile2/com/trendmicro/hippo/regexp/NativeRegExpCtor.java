package com.trendmicro.hippo.regexp;

import com.trendmicro.hippo.BaseFunction;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.TopLevel;
import com.trendmicro.hippo.Undefined;

class NativeRegExpCtor extends BaseFunction {
  private static final int DOLLAR_ID_BASE = 12;
  
  private static final int Id_AMPERSAND = 6;
  
  private static final int Id_BACK_QUOTE = 10;
  
  private static final int Id_DOLLAR_1 = 13;
  
  private static final int Id_DOLLAR_2 = 14;
  
  private static final int Id_DOLLAR_3 = 15;
  
  private static final int Id_DOLLAR_4 = 16;
  
  private static final int Id_DOLLAR_5 = 17;
  
  private static final int Id_DOLLAR_6 = 18;
  
  private static final int Id_DOLLAR_7 = 19;
  
  private static final int Id_DOLLAR_8 = 20;
  
  private static final int Id_DOLLAR_9 = 21;
  
  private static final int Id_PLUS = 8;
  
  private static final int Id_QUOTE = 12;
  
  private static final int Id_STAR = 2;
  
  private static final int Id_UNDERSCORE = 4;
  
  private static final int Id_input = 3;
  
  private static final int Id_lastMatch = 5;
  
  private static final int Id_lastParen = 7;
  
  private static final int Id_leftContext = 9;
  
  private static final int Id_multiline = 1;
  
  private static final int Id_rightContext = 11;
  
  private static final int MAX_INSTANCE_ID = 21;
  
  private static final long serialVersionUID = -5733330028285400526L;
  
  private int inputAttr = 4;
  
  private int multilineAttr = 4;
  
  private int starAttr = 4;
  
  private int underscoreAttr = 4;
  
  private static RegExpImpl getImpl() {
    return (RegExpImpl)ScriptRuntime.getRegExpProxy(Context.getCurrentContext());
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return (paramArrayOfObject.length > 0 && paramArrayOfObject[0] instanceof NativeRegExp && (paramArrayOfObject.length == 1 || paramArrayOfObject[1] == Undefined.instance)) ? paramArrayOfObject[0] : construct(paramContext, paramScriptable1, paramArrayOfObject);
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    NativeRegExp nativeRegExp = new NativeRegExp();
    nativeRegExp.compile(paramContext, paramScriptable, paramArrayOfObject);
    ScriptRuntime.setBuiltinProtoAndParent((ScriptableObject)nativeRegExp, paramScriptable, TopLevel.Builtins.RegExp);
    return (Scriptable)nativeRegExp;
  }
  
  protected int findInstanceIdInfo(String paramString) {
    int i = 0;
    String str = null;
    int j = paramString.length();
    if (j != 2)
      if (j != 5) {
        if (j != 9) {
          if (j != 11) {
            if (j == 12) {
              str = "rightContext";
              i = 11;
            } 
          } else {
            str = "leftContext";
            i = 9;
          } 
        } else {
          j = paramString.charAt(4);
          if (j == 77) {
            str = "lastMatch";
            i = 5;
          } else if (j == 80) {
            str = "lastParen";
            i = 7;
          } else if (j == 105) {
            str = "multiline";
            i = 1;
          } 
        } 
      } else {
        str = "input";
        i = 3;
      }  
    j = paramString.charAt(1);
    if (j != 38) {
      if (j != 39) {
        if (j != 42) {
          if (j != 43) {
            if (j != 95) {
              if (j != 96) {
                switch (j) {
                  default:
                    j = i;
                    if (str != null) {
                      j = i;
                      if (str != paramString) {
                        j = i;
                        if (!str.equals(paramString))
                          j = 0; 
                      } 
                    } 
                    break;
                  case 57:
                    if (paramString.charAt(0) == '$') {
                      j = 21;
                      break;
                    } 
                  case 56:
                    if (paramString.charAt(0) == '$') {
                      j = 20;
                      break;
                    } 
                  case 55:
                    if (paramString.charAt(0) == '$') {
                      j = 19;
                      break;
                    } 
                  case 54:
                    if (paramString.charAt(0) == '$') {
                      j = 18;
                      break;
                    } 
                  case 53:
                    if (paramString.charAt(0) == '$') {
                      j = 17;
                      break;
                    } 
                  case 52:
                    if (paramString.charAt(0) == '$') {
                      j = 16;
                      break;
                    } 
                  case 51:
                    if (paramString.charAt(0) == '$') {
                      j = 15;
                      break;
                    } 
                  case 50:
                    if (paramString.charAt(0) == '$') {
                      j = 14;
                      break;
                    } 
                  case 49:
                    if (paramString.charAt(0) == '$') {
                      j = 13;
                      break;
                    } 
                } 
              } else if (paramString.charAt(0) == '$') {
                j = 10;
              } else {
              
              } 
            } else if (paramString.charAt(0) == '$') {
              j = 4;
            } else {
            
            } 
          } else if (paramString.charAt(0) == '$') {
            j = 8;
          } else {
          
          } 
        } else if (paramString.charAt(0) == '$') {
          j = 2;
        } else {
        
        } 
      } else if (paramString.charAt(0) == '$') {
        j = 12;
      } else {
      
      } 
    } else if (paramString.charAt(0) == '$') {
      j = 6;
    } else {
    
    } 
    if (j == 0)
      return super.findInstanceIdInfo(paramString); 
    if (j != 1) {
      if (j != 2) {
        if (j != 3) {
          if (j != 4) {
            i = 5;
          } else {
            i = this.underscoreAttr;
          } 
        } else {
          i = this.inputAttr;
        } 
      } else {
        i = this.starAttr;
      } 
    } else {
      i = this.multilineAttr;
    } 
    return instanceIdInfo(i, super.getMaxInstanceId() + j);
  }
  
  public int getArity() {
    return 2;
  }
  
  public String getFunctionName() {
    return "RegExp";
  }
  
  protected String getInstanceIdName(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    if (1 <= i && i <= 21) {
      switch (i) {
        default:
          return new String(new char[] { '$', (char)(i - 12 - 1 + 49) });
        case 12:
          return "$'";
        case 11:
          return "rightContext";
        case 10:
          return "$`";
        case 9:
          return "leftContext";
        case 8:
          return "$+";
        case 7:
          return "lastParen";
        case 6:
          return "$&";
        case 5:
          return "lastMatch";
        case 4:
          return "$_";
        case 3:
          return "input";
        case 2:
          return "$*";
        case 1:
          break;
      } 
      return "multiline";
    } 
    return super.getInstanceIdName(paramInt);
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    if (1 <= i && i <= 21) {
      SubString subString;
      String str;
      RegExpImpl regExpImpl = getImpl();
      switch (i) {
        default:
          subString = regExpImpl.getParenSubString(i - 12 - 1);
          break;
        case 11:
        case 12:
          subString = ((RegExpImpl)subString).rightContext;
          break;
        case 9:
        case 10:
          subString = ((RegExpImpl)subString).leftContext;
          break;
        case 7:
        case 8:
          subString = ((RegExpImpl)subString).lastParen;
          break;
        case 5:
        case 6:
          subString = ((RegExpImpl)subString).lastMatch;
          break;
        case 3:
        case 4:
          str = ((RegExpImpl)subString).input;
          break;
        case 1:
        case 2:
          return ScriptRuntime.wrapBoolean(((RegExpImpl)str).multiline);
      } 
      if (str == null) {
        str = "";
      } else {
        str = str.toString();
      } 
      return str;
    } 
    return super.getInstanceIdValue(paramInt);
  }
  
  public int getLength() {
    return 2;
  }
  
  protected int getMaxInstanceId() {
    return super.getMaxInstanceId() + 21;
  }
  
  protected void setInstanceIdAttributes(int paramInt1, int paramInt2) {
    int i = paramInt1 - super.getMaxInstanceId();
    switch (i) {
      default:
        i = i - 12 - 1;
        if (i >= 0 && i <= 8)
          return; 
        break;
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
        return;
      case 4:
        this.underscoreAttr = paramInt2;
        return;
      case 3:
        this.inputAttr = paramInt2;
        return;
      case 2:
        this.starAttr = paramInt2;
        return;
      case 1:
        this.multilineAttr = paramInt2;
        return;
    } 
    super.setInstanceIdAttributes(paramInt1, paramInt2);
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    int i = paramInt - super.getMaxInstanceId();
    switch (i) {
      default:
        i = i - 12 - 1;
        if (i >= 0 && i <= 8)
          return; 
        break;
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
        return;
      case 3:
      case 4:
        (getImpl()).input = ScriptRuntime.toString(paramObject);
        return;
      case 1:
      case 2:
        (getImpl()).multiline = ScriptRuntime.toBoolean(paramObject);
        return;
    } 
    super.setInstanceIdValue(paramInt, paramObject);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/NativeRegExpCtor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */