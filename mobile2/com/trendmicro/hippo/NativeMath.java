package com.trendmicro.hippo;

final class NativeMath extends IdScriptableObject {
  private static final int Id_E = 37;
  
  private static final int Id_LN10 = 39;
  
  private static final int Id_LN2 = 40;
  
  private static final int Id_LOG10E = 42;
  
  private static final int Id_LOG2E = 41;
  
  private static final int Id_PI = 38;
  
  private static final int Id_SQRT1_2 = 43;
  
  private static final int Id_SQRT2 = 44;
  
  private static final int Id_abs = 2;
  
  private static final int Id_acos = 3;
  
  private static final int Id_acosh = 30;
  
  private static final int Id_asin = 4;
  
  private static final int Id_asinh = 31;
  
  private static final int Id_atan = 5;
  
  private static final int Id_atan2 = 6;
  
  private static final int Id_atanh = 32;
  
  private static final int Id_cbrt = 20;
  
  private static final int Id_ceil = 7;
  
  private static final int Id_clz32 = 36;
  
  private static final int Id_cos = 8;
  
  private static final int Id_cosh = 21;
  
  private static final int Id_exp = 9;
  
  private static final int Id_expm1 = 22;
  
  private static final int Id_floor = 10;
  
  private static final int Id_fround = 35;
  
  private static final int Id_hypot = 23;
  
  private static final int Id_imul = 28;
  
  private static final int Id_log = 11;
  
  private static final int Id_log10 = 25;
  
  private static final int Id_log1p = 24;
  
  private static final int Id_log2 = 34;
  
  private static final int Id_max = 12;
  
  private static final int Id_min = 13;
  
  private static final int Id_pow = 14;
  
  private static final int Id_random = 15;
  
  private static final int Id_round = 16;
  
  private static final int Id_sign = 33;
  
  private static final int Id_sin = 17;
  
  private static final int Id_sinh = 26;
  
  private static final int Id_sqrt = 18;
  
  private static final int Id_tan = 19;
  
  private static final int Id_tanh = 27;
  
  private static final int Id_toSource = 1;
  
  private static final int Id_trunc = 29;
  
  private static final int LAST_METHOD_ID = 36;
  
  private static final double LOG2E = 1.4426950408889634D;
  
  private static final Object MATH_TAG = "Math";
  
  private static final int MAX_ID = 44;
  
  private static final long serialVersionUID = -8838847185801131569L;
  
  static void init(Scriptable paramScriptable, boolean paramBoolean) {
    NativeMath nativeMath = new NativeMath();
    nativeMath.activatePrototypeMap(44);
    nativeMath.setPrototype(getObjectPrototype(paramScriptable));
    nativeMath.setParentScope(paramScriptable);
    if (paramBoolean)
      nativeMath.sealObject(); 
    ScriptableObject.defineProperty(paramScriptable, "Math", nativeMath, 2);
  }
  
  private static double js_hypot(Object[] paramArrayOfObject) {
    if (paramArrayOfObject == null)
      return 0.0D; 
    double d = 0.0D;
    int i = paramArrayOfObject.length;
    for (byte b = 0; b < i; b++) {
      double d1 = ScriptRuntime.toNumber(paramArrayOfObject[b]);
      if (d1 == ScriptRuntime.NaN)
        return d1; 
      if (d1 == Double.POSITIVE_INFINITY || d1 == Double.NEGATIVE_INFINITY)
        return Double.POSITIVE_INFINITY; 
      d += d1 * d1;
    } 
    return Math.sqrt(d);
  }
  
  private static int js_imul(Object[] paramArrayOfObject) {
    return (paramArrayOfObject == null) ? 0 : (ScriptRuntime.toInt32(paramArrayOfObject, 0) * ScriptRuntime.toInt32(paramArrayOfObject, 1));
  }
  
  private static double js_pow(double paramDouble1, double paramDouble2) {
    if (paramDouble2 != paramDouble2) {
      paramDouble1 = paramDouble2;
    } else {
      double d = 0.0D;
      if (paramDouble2 == 0.0D) {
        paramDouble1 = 1.0D;
      } else if (paramDouble1 == 0.0D) {
        if (1.0D / paramDouble1 > 0.0D) {
          if (paramDouble2 > 0.0D) {
            paramDouble1 = d;
          } else {
            paramDouble1 = Double.POSITIVE_INFINITY;
          } 
        } else {
          long l = (long)paramDouble2;
          if (l == paramDouble2 && (l & 0x1L) != 0L) {
            if (paramDouble2 > 0.0D) {
              paramDouble1 = 0.0D;
            } else {
              paramDouble1 = Double.NEGATIVE_INFINITY;
            } 
          } else if (paramDouble2 > 0.0D) {
            paramDouble1 = d;
          } else {
            paramDouble1 = Double.POSITIVE_INFINITY;
          } 
        } 
      } else {
        double d1 = Math.pow(paramDouble1, paramDouble2);
        if (d1 != d1)
          if (paramDouble2 == Double.POSITIVE_INFINITY) {
            if (paramDouble1 < -1.0D || 1.0D < paramDouble1)
              return Double.POSITIVE_INFINITY; 
            if (-1.0D < paramDouble1 && paramDouble1 < 1.0D)
              return 0.0D; 
          } else if (paramDouble2 == Double.NEGATIVE_INFINITY) {
            if (paramDouble1 < -1.0D || 1.0D < paramDouble1)
              return 0.0D; 
            if (-1.0D < paramDouble1 && paramDouble1 < 1.0D)
              return Double.POSITIVE_INFINITY; 
          } else {
            if (paramDouble1 == Double.POSITIVE_INFINITY) {
              paramDouble1 = d;
              if (paramDouble2 > 0.0D)
                paramDouble1 = Double.POSITIVE_INFINITY; 
            } else {
              if (paramDouble1 == Double.NEGATIVE_INFINITY) {
                long l = (long)paramDouble2;
                if (l == paramDouble2 && (0x1L & l) != 0L) {
                  if (paramDouble2 > 0.0D) {
                    paramDouble1 = Double.NEGATIVE_INFINITY;
                  } else {
                    paramDouble1 = 0.0D;
                  } 
                } else {
                  paramDouble1 = d;
                  if (paramDouble2 > 0.0D)
                    paramDouble1 = Double.POSITIVE_INFINITY; 
                } 
                return paramDouble1;
              } 
              paramDouble1 = d1;
            } 
            return paramDouble1;
          }  
        paramDouble1 = d1;
      } 
    } 
    return paramDouble1;
  }
  
  private static double js_trunc(double paramDouble) {
    if (paramDouble < 0.0D) {
      paramDouble = Math.ceil(paramDouble);
    } else {
      paramDouble = Math.floor(paramDouble);
    } 
    return paramDouble;
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    long l;
    byte b;
    if (!paramIdFunctionObject.hasTag(MATH_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    double d1 = Double.NEGATIVE_INFINITY;
    double d2 = Double.NaN;
    double d3 = 0.0D;
    switch (i) {
      default:
        throw new IllegalStateException(String.valueOf(i));
      case 36:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d2 == 0.0D || d2 != d2 || d2 == Double.POSITIVE_INFINITY || d2 == Double.NEGATIVE_INFINITY)
          return Integer.valueOf(32); 
        l = ScriptRuntime.toUint32(d2);
        return (l == 0L) ? Integer.valueOf(32) : Double.valueOf(31.0D - Math.floor(Math.log((l >>> 0L)) * 1.4426950408889634D));
      case 35:
        d2 = (float)ScriptRuntime.toNumber(paramArrayOfObject, 0);
        return ScriptRuntime.wrapNumber(d2);
      case 34:
        d3 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d3 >= 0.0D)
          d2 = Math.log(d3) * 1.4426950408889634D; 
        return ScriptRuntime.wrapNumber(d2);
      case 33:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        return (d2 == d2) ? ((d2 == 0.0D) ? ((1.0D / d2 > 0.0D) ? Double.valueOf(0.0D) : Double.valueOf(0.0D)) : Double.valueOf(Math.signum(d2))) : Double.valueOf(Double.NaN);
      case 32:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        return (d2 == d2 && -1.0D <= d2 && d2 <= 1.0D) ? ((d2 == 0.0D) ? ((1.0D / d2 > 0.0D) ? Double.valueOf(0.0D) : Double.valueOf(0.0D)) : Double.valueOf(Math.log((d2 + 1.0D) / (d2 - 1.0D)) * 0.5D)) : Double.valueOf(Double.NaN);
      case 31:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        return (d2 == Double.POSITIVE_INFINITY || d2 == Double.NEGATIVE_INFINITY) ? Double.valueOf(d2) : ((d2 == d2) ? ((d2 == 0.0D) ? ((1.0D / d2 > 0.0D) ? Double.valueOf(0.0D) : Double.valueOf(0.0D)) : Double.valueOf(Math.log(Math.sqrt(d2 * d2 + 1.0D) + d2))) : Double.valueOf(Double.NaN));
      case 30:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        return (d2 == d2) ? Double.valueOf(Math.log(Math.sqrt(d2 * d2 - 1.0D) + d2)) : Double.valueOf(Double.NaN);
      case 29:
        d2 = js_trunc(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 28:
        return Integer.valueOf(js_imul(paramArrayOfObject));
      case 27:
        d2 = Math.tanh(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 26:
        d2 = Math.sinh(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 25:
        d2 = Math.log10(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 24:
        d2 = Math.log1p(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 23:
        d2 = js_hypot(paramArrayOfObject);
        return ScriptRuntime.wrapNumber(d2);
      case 22:
        d2 = Math.expm1(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 21:
        d2 = Math.cosh(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 20:
        d2 = Math.cbrt(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 19:
        d2 = Math.tan(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 18:
        d2 = Math.sqrt(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 17:
        d3 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d3 != Double.POSITIVE_INFINITY && d3 != Double.NEGATIVE_INFINITY)
          d2 = Math.sin(d3); 
        return ScriptRuntime.wrapNumber(d2);
      case 16:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d2 == d2 && d2 != Double.POSITIVE_INFINITY && d2 != Double.NEGATIVE_INFINITY) {
          l = Math.round(d2);
          if (l != 0L) {
            d2 = l;
          } else if (d2 < 0.0D) {
            d2 = ScriptRuntime.negativeZero;
          } else if (d2 != 0.0D) {
            d2 = 0.0D;
          } 
        } 
        return ScriptRuntime.wrapNumber(d2);
      case 15:
        d2 = Math.random();
        return ScriptRuntime.wrapNumber(d2);
      case 14:
        d2 = js_pow(ScriptRuntime.toNumber(paramArrayOfObject, 0), ScriptRuntime.toNumber(paramArrayOfObject, 1));
        return ScriptRuntime.wrapNumber(d2);
      case 12:
      case 13:
        if (i == 12) {
          d2 = d1;
        } else {
          d2 = Double.POSITIVE_INFINITY;
        } 
        for (b = 0; b != paramArrayOfObject.length; b++) {
          d3 = ScriptRuntime.toNumber(paramArrayOfObject[b]);
          if (d3 != d3) {
            d2 = d3;
            break;
          } 
          if (i == 12) {
            d2 = Math.max(d2, d3);
          } else {
            d2 = Math.min(d2, d3);
          } 
        } 
        return ScriptRuntime.wrapNumber(d2);
      case 11:
        d3 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d3 >= 0.0D)
          d2 = Math.log(d3); 
        return ScriptRuntime.wrapNumber(d2);
      case 10:
        d2 = Math.floor(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 9:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d2 != Double.POSITIVE_INFINITY)
          if (d2 == Double.NEGATIVE_INFINITY) {
            d2 = d3;
          } else {
            d2 = Math.exp(d2);
          }  
        return ScriptRuntime.wrapNumber(d2);
      case 8:
        d3 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d3 != Double.POSITIVE_INFINITY && d3 != Double.NEGATIVE_INFINITY)
          d2 = Math.cos(d3); 
        return ScriptRuntime.wrapNumber(d2);
      case 7:
        d2 = Math.ceil(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 6:
        d2 = Math.atan2(ScriptRuntime.toNumber(paramArrayOfObject, 0), ScriptRuntime.toNumber(paramArrayOfObject, 1));
        return ScriptRuntime.wrapNumber(d2);
      case 5:
        d2 = Math.atan(ScriptRuntime.toNumber(paramArrayOfObject, 0));
        return ScriptRuntime.wrapNumber(d2);
      case 3:
      case 4:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d2 == d2 && -1.0D <= d2 && d2 <= 1.0D) {
          if (i == 3) {
            d2 = Math.acos(d2);
          } else {
            d2 = Math.asin(d2);
          } 
        } else {
          d2 = Double.NaN;
        } 
        return ScriptRuntime.wrapNumber(d2);
      case 2:
        d2 = ScriptRuntime.toNumber(paramArrayOfObject, 0);
        if (d2 == 0.0D) {
          d2 = d3;
        } else if (d2 < 0.0D) {
          d2 = -d2;
        } 
        return ScriptRuntime.wrapNumber(d2);
      case 1:
        break;
    } 
    return "Math";
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
      case 8:
        str2 = "toSource";
        c2 = '\001';
        break;
      case 7:
        str2 = "SQRT1_2";
        c2 = '+';
        break;
      case 6:
        c = paramString.charAt(0);
        if (c == 'L') {
          str2 = "LOG10E";
          c2 = '*';
          break;
        } 
        if (c == 'f') {
          str2 = "fround";
          c2 = '#';
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (c == 'r') {
          str2 = "random";
          c2 = '\017';
        } 
        break;
      case 5:
        c2 = paramString.charAt(0);
        if (c2 != 'L') {
          if (c2 != 'S') {
            if (c2 != 'a') {
              if (c2 != 'c') {
                if (c2 != 'h') {
                  if (c2 != 'l') {
                    if (c2 != 'r') {
                      if (c2 != 't') {
                        if (c2 != 'e') {
                          if (c2 != 'f') {
                            c2 = c1;
                            str2 = str1;
                            break;
                          } 
                          str2 = "floor";
                          c2 = '\n';
                          break;
                        } 
                        str2 = "expm1";
                        c2 = '\026';
                        break;
                      } 
                      str2 = "trunc";
                      c2 = '\035';
                      break;
                    } 
                    str2 = "round";
                    c2 = '\020';
                    break;
                  } 
                  c = paramString.charAt(4);
                  if (c == '0') {
                    str2 = "log10";
                    c2 = '\031';
                    break;
                  } 
                  c2 = c1;
                  str2 = str1;
                  if (c == 'p') {
                    str2 = "log1p";
                    c2 = '\030';
                  } 
                  break;
                } 
                str2 = "hypot";
                c2 = '\027';
                break;
              } 
              str2 = "clz32";
              c2 = '$';
              break;
            } 
            c = paramString.charAt(1);
            if (c == 'c') {
              str2 = "acosh";
              c2 = '\036';
              break;
            } 
            if (c == 's') {
              str2 = "asinh";
              c2 = '\037';
              break;
            } 
            c2 = c1;
            str2 = str1;
            if (c == 't') {
              c = paramString.charAt(4);
              if (c == '2') {
                c2 = c1;
                str2 = str1;
                if (paramString.charAt(2) == 'a') {
                  c2 = c1;
                  str2 = str1;
                  if (paramString.charAt(3) == 'n')
                    return 6; 
                } 
                break;
              } 
              c2 = c1;
              str2 = str1;
              if (c == 'h') {
                c2 = c1;
                str2 = str1;
                if (paramString.charAt(2) == 'a') {
                  c2 = c1;
                  str2 = str1;
                  if (paramString.charAt(3) == 'n')
                    return 32; 
                } 
              } 
            } 
            break;
          } 
          str2 = "SQRT2";
          c2 = ',';
          break;
        } 
        str2 = "LOG2E";
        c2 = ')';
        break;
      case 4:
        c2 = paramString.charAt(1);
        if (c2 != 'N') {
          if (c2 != 'e') {
            if (c2 != 'i') {
              if (c2 != 'm') {
                if (c2 != 'o') {
                  if (c2 != 'q') {
                    if (c2 != 's') {
                      if (c2 != 't') {
                        switch (c2) {
                          default:
                            c2 = c1;
                            str2 = str1;
                            break;
                          case 'c':
                            str2 = "acos";
                            c2 = '\003';
                            break;
                          case 'b':
                            str2 = "cbrt";
                            c2 = '\024';
                            break;
                          case 'a':
                            break;
                        } 
                        str2 = "tanh";
                        c2 = '\033';
                        break;
                      } 
                      str2 = "atan";
                      c2 = '\005';
                      break;
                    } 
                    str2 = "asin";
                    c2 = '\004';
                    break;
                  } 
                  str2 = "sqrt";
                  c2 = '\022';
                  break;
                } 
                c = paramString.charAt(0);
                if (c == 'c') {
                  c2 = c1;
                  str2 = str1;
                  if (paramString.charAt(2) == 's') {
                    c2 = c1;
                    str2 = str1;
                    if (paramString.charAt(3) == 'h')
                      return 21; 
                  } 
                  break;
                } 
                c2 = c1;
                str2 = str1;
                if (c == 'l') {
                  c2 = c1;
                  str2 = str1;
                  if (paramString.charAt(2) == 'g') {
                    c2 = c1;
                    str2 = str1;
                    if (paramString.charAt(3) == '2')
                      return 34; 
                  } 
                } 
                break;
              } 
              str2 = "imul";
              c2 = '\034';
              break;
            } 
            c = paramString.charAt(3);
            if (c == 'h') {
              c2 = c1;
              str2 = str1;
              if (paramString.charAt(0) == 's') {
                c2 = c1;
                str2 = str1;
                if (paramString.charAt(2) == 'n')
                  return 26; 
              } 
              break;
            } 
            c2 = c1;
            str2 = str1;
            if (c == 'n') {
              c2 = c1;
              str2 = str1;
              if (paramString.charAt(0) == 's') {
                c2 = c1;
                str2 = str1;
                if (paramString.charAt(2) == 'g')
                  return 33; 
              } 
            } 
            break;
          } 
          str2 = "ceil";
          c2 = '\007';
          break;
        } 
        str2 = "LN10";
        c2 = '\'';
        break;
      case 3:
        c2 = paramString.charAt(0);
        if (c2 != 'L') {
          if (c2 != 'a') {
            if (c2 != 'c') {
              if (c2 != 'e') {
                if (c2 != 'p') {
                  if (c2 != 'l') {
                    if (c2 != 'm') {
                      if (c2 != 's') {
                        if (c2 != 't') {
                          c2 = c1;
                          str2 = str1;
                          break;
                        } 
                        c2 = c1;
                        str2 = str1;
                        if (paramString.charAt(2) == 'n') {
                          c2 = c1;
                          str2 = str1;
                          if (paramString.charAt(1) == 'a')
                            return 19; 
                        } 
                        break;
                      } 
                      c2 = c1;
                      str2 = str1;
                      if (paramString.charAt(2) == 'n') {
                        c2 = c1;
                        str2 = str1;
                        if (paramString.charAt(1) == 'i')
                          return 17; 
                      } 
                      break;
                    } 
                    c = paramString.charAt(2);
                    if (c == 'n') {
                      c2 = c1;
                      str2 = str1;
                      if (paramString.charAt(1) == 'i')
                        return 13; 
                      break;
                    } 
                    c2 = c1;
                    str2 = str1;
                    if (c == 'x') {
                      c2 = c1;
                      str2 = str1;
                      if (paramString.charAt(1) == 'a')
                        return 12; 
                    } 
                    break;
                  } 
                  c2 = c1;
                  str2 = str1;
                  if (paramString.charAt(2) == 'g') {
                    c2 = c1;
                    str2 = str1;
                    if (paramString.charAt(1) == 'o')
                      return 11; 
                  } 
                  break;
                } 
                c2 = c1;
                str2 = str1;
                if (paramString.charAt(2) == 'w') {
                  c2 = c1;
                  str2 = str1;
                  if (paramString.charAt(1) == 'o')
                    return 14; 
                } 
                break;
              } 
              c2 = c1;
              str2 = str1;
              if (paramString.charAt(2) == 'p') {
                c2 = c1;
                str2 = str1;
                if (paramString.charAt(1) == 'x')
                  return 9; 
              } 
              break;
            } 
            c2 = c1;
            str2 = str1;
            if (paramString.charAt(2) == 's') {
              c2 = c1;
              str2 = str1;
              if (paramString.charAt(1) == 'o')
                return 8; 
            } 
            break;
          } 
          c2 = c1;
          str2 = str1;
          if (paramString.charAt(2) == 's') {
            c2 = c1;
            str2 = str1;
            if (paramString.charAt(1) == 'b')
              return 2; 
          } 
          break;
        } 
        c2 = c1;
        str2 = str1;
        if (paramString.charAt(2) == '2') {
          c2 = c1;
          str2 = str1;
          if (paramString.charAt(1) == 'N')
            return 40; 
        } 
        break;
      case 2:
        c2 = c1;
        str2 = str1;
        if (paramString.charAt(0) == 'P') {
          c2 = c1;
          str2 = str1;
          if (paramString.charAt(1) == 'I')
            return 38; 
        } 
        break;
      case 1:
        c2 = c1;
        str2 = str1;
        if (paramString.charAt(0) == 'E')
          return 37; 
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
  
  public String getClassName() {
    return "Math";
  }
  
  protected void initPrototypeId(int paramInt) {
    if (paramInt <= 36) {
      byte b;
      String str;
      switch (paramInt) {
        default:
          throw new IllegalStateException(String.valueOf(paramInt));
        case 36:
          b = 1;
          str = "clz32";
          break;
        case 35:
          b = 1;
          str = "fround";
          break;
        case 34:
          b = 1;
          str = "log2";
          break;
        case 33:
          b = 1;
          str = "sign";
          break;
        case 32:
          b = 1;
          str = "atanh";
          break;
        case 31:
          b = 1;
          str = "asinh";
          break;
        case 30:
          b = 1;
          str = "acosh";
          break;
        case 29:
          b = 1;
          str = "trunc";
          break;
        case 28:
          b = 2;
          str = "imul";
          break;
        case 27:
          b = 1;
          str = "tanh";
          break;
        case 26:
          b = 1;
          str = "sinh";
          break;
        case 25:
          b = 1;
          str = "log10";
          break;
        case 24:
          b = 1;
          str = "log1p";
          break;
        case 23:
          b = 2;
          str = "hypot";
          break;
        case 22:
          b = 1;
          str = "expm1";
          break;
        case 21:
          b = 1;
          str = "cosh";
          break;
        case 20:
          b = 1;
          str = "cbrt";
          break;
        case 19:
          b = 1;
          str = "tan";
          break;
        case 18:
          b = 1;
          str = "sqrt";
          break;
        case 17:
          b = 1;
          str = "sin";
          break;
        case 16:
          b = 1;
          str = "round";
          break;
        case 15:
          b = 0;
          str = "random";
          break;
        case 14:
          b = 2;
          str = "pow";
          break;
        case 13:
          b = 2;
          str = "min";
          break;
        case 12:
          b = 2;
          str = "max";
          break;
        case 11:
          b = 1;
          str = "log";
          break;
        case 10:
          b = 1;
          str = "floor";
          break;
        case 9:
          b = 1;
          str = "exp";
          break;
        case 8:
          b = 1;
          str = "cos";
          break;
        case 7:
          b = 1;
          str = "ceil";
          break;
        case 6:
          b = 2;
          str = "atan2";
          break;
        case 5:
          b = 1;
          str = "atan";
          break;
        case 4:
          b = 1;
          str = "asin";
          break;
        case 3:
          b = 1;
          str = "acos";
          break;
        case 2:
          b = 1;
          str = "abs";
          break;
        case 1:
          b = 0;
          str = "toSource";
          break;
      } 
      initPrototypeMethod(MATH_TAG, paramInt, str, b);
    } else {
      String str;
      double d;
      switch (paramInt) {
        default:
          throw new IllegalStateException(String.valueOf(paramInt));
        case 44:
          d = 1.4142135623730951D;
          str = "SQRT2";
          break;
        case 43:
          d = 0.7071067811865476D;
          str = "SQRT1_2";
          break;
        case 42:
          d = 0.4342944819032518D;
          str = "LOG10E";
          break;
        case 41:
          d = 1.4426950408889634D;
          str = "LOG2E";
          break;
        case 40:
          d = 0.6931471805599453D;
          str = "LN2";
          break;
        case 39:
          d = 2.302585092994046D;
          str = "LN10";
          break;
        case 38:
          d = Math.PI;
          str = "PI";
          break;
        case 37:
          d = Math.E;
          str = "E";
          break;
      } 
      initPrototypeValue(paramInt, str, ScriptRuntime.wrapNumber(d), 7);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */