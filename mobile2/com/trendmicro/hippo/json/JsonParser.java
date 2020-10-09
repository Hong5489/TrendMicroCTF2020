package com.trendmicro.hippo.json;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import java.util.ArrayList;

public class JsonParser {
  private Context cx;
  
  private int length;
  
  private int pos;
  
  private Scriptable scope;
  
  private String src;
  
  public JsonParser(Context paramContext, Scriptable paramScriptable) {
    this.cx = paramContext;
    this.scope = paramScriptable;
  }
  
  private void consume(char paramChar) throws ParseException {
    consumeWhitespace();
    int i = this.pos;
    if (i < this.length) {
      String str = this.src;
      this.pos = i + 1;
      char c = str.charAt(i);
      if (c == paramChar)
        return; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected ");
      stringBuilder1.append(paramChar);
      stringBuilder1.append(" found ");
      stringBuilder1.append(c);
      throw new ParseException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected ");
    stringBuilder.append(paramChar);
    stringBuilder.append(" but reached end of stream");
    throw new ParseException(stringBuilder.toString());
  }
  
  private void consumeWhitespace() {
    while (true) {
      int i = this.pos;
      if (i < this.length) {
        i = this.src.charAt(i);
        if (i != 9 && i != 10 && i != 13 && i != 32)
          return; 
        this.pos++;
        continue;
      } 
      break;
    } 
  }
  
  private int fromHex(char paramChar) {
    int i;
    if (paramChar >= '0' && paramChar <= '9') {
      paramChar -= '0';
    } else if (paramChar >= 'A' && paramChar <= 'F') {
      i = paramChar - 65 + 10;
    } else if (i >= 97 && i <= 102) {
      i = i - 97 + 10;
    } else {
      i = -1;
    } 
    return i;
  }
  
  private char nextOrNumberError(int paramInt) throws ParseException {
    int i = this.pos;
    int j = this.length;
    if (i < j) {
      String str = this.src;
      this.pos = i + 1;
      return str.charAt(i);
    } 
    throw numberError(paramInt, j);
  }
  
  private ParseException numberError(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unsupported number format: ");
    stringBuilder.append(this.src.substring(paramInt1, paramInt2));
    return new ParseException(stringBuilder.toString());
  }
  
  private Object readArray() throws ParseException {
    consumeWhitespace();
    int i = this.pos;
    if (i < this.length && this.src.charAt(i) == ']') {
      this.pos++;
      return this.cx.newArray(this.scope, 0);
    } 
    ArrayList<Object> arrayList = new ArrayList();
    i = 0;
    while (true) {
      int j = this.pos;
      if (j < this.length) {
        j = this.src.charAt(j);
        if (j != 44) {
          if (j != 93) {
            if (i == 0) {
              arrayList.add(readValue());
              i = 1;
            } else {
              throw new ParseException("Missing comma in array literal");
            } 
          } else {
            if (i != 0) {
              this.pos++;
              return this.cx.newArray(this.scope, arrayList.toArray());
            } 
            throw new ParseException("Unexpected comma in array literal");
          } 
        } else if (i != 0) {
          i = 0;
          this.pos++;
        } else {
          throw new ParseException("Unexpected comma in array literal");
        } 
        consumeWhitespace();
        continue;
      } 
      throw new ParseException("Unterminated array literal");
    } 
  }
  
  private void readDigits() {
    while (true) {
      int i = this.pos;
      if (i < this.length) {
        i = this.src.charAt(i);
        if (i < 48 || i > 57)
          break; 
        this.pos++;
        continue;
      } 
      break;
    } 
  }
  
  private Boolean readFalse() throws ParseException {
    int i = this.length;
    int j = this.pos;
    if (i - j >= 4 && this.src.charAt(j) == 'a' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 's' && this.src.charAt(this.pos + 3) == 'e') {
      this.pos += 4;
      return Boolean.FALSE;
    } 
    throw new ParseException("Unexpected token: f");
  }
  
  private Object readNull() throws ParseException {
    int i = this.length;
    int j = this.pos;
    if (i - j >= 3 && this.src.charAt(j) == 'u' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 'l') {
      this.pos += 3;
      return null;
    } 
    throw new ParseException("Unexpected token: n");
  }
  
  private Number readNumber(char paramChar) throws ParseException {
    // Byte code:
    //   0: aload_0
    //   1: getfield pos : I
    //   4: iconst_1
    //   5: isub
    //   6: istore_2
    //   7: iload_1
    //   8: istore_3
    //   9: iload_1
    //   10: bipush #45
    //   12: if_icmpne -> 46
    //   15: aload_0
    //   16: iload_2
    //   17: invokespecial nextOrNumberError : (I)C
    //   20: istore_3
    //   21: iload_3
    //   22: bipush #48
    //   24: if_icmplt -> 36
    //   27: iload_3
    //   28: bipush #57
    //   30: if_icmpgt -> 36
    //   33: goto -> 46
    //   36: aload_0
    //   37: iload_2
    //   38: aload_0
    //   39: getfield pos : I
    //   42: invokespecial numberError : (II)Lcom/trendmicro/hippo/json/JsonParser$ParseException;
    //   45: athrow
    //   46: iload_3
    //   47: bipush #48
    //   49: if_icmpeq -> 56
    //   52: aload_0
    //   53: invokespecial readDigits : ()V
    //   56: aload_0
    //   57: getfield pos : I
    //   60: istore_1
    //   61: iload_1
    //   62: aload_0
    //   63: getfield length : I
    //   66: if_icmpge -> 127
    //   69: aload_0
    //   70: getfield src : Ljava/lang/String;
    //   73: iload_1
    //   74: invokevirtual charAt : (I)C
    //   77: bipush #46
    //   79: if_icmpne -> 127
    //   82: aload_0
    //   83: aload_0
    //   84: getfield pos : I
    //   87: iconst_1
    //   88: iadd
    //   89: putfield pos : I
    //   92: aload_0
    //   93: iload_2
    //   94: invokespecial nextOrNumberError : (I)C
    //   97: istore_1
    //   98: iload_1
    //   99: bipush #48
    //   101: if_icmplt -> 117
    //   104: iload_1
    //   105: bipush #57
    //   107: if_icmpgt -> 117
    //   110: aload_0
    //   111: invokespecial readDigits : ()V
    //   114: goto -> 127
    //   117: aload_0
    //   118: iload_2
    //   119: aload_0
    //   120: getfield pos : I
    //   123: invokespecial numberError : (II)Lcom/trendmicro/hippo/json/JsonParser$ParseException;
    //   126: athrow
    //   127: aload_0
    //   128: getfield pos : I
    //   131: istore_1
    //   132: iload_1
    //   133: aload_0
    //   134: getfield length : I
    //   137: if_icmpge -> 226
    //   140: aload_0
    //   141: getfield src : Ljava/lang/String;
    //   144: iload_1
    //   145: invokevirtual charAt : (I)C
    //   148: istore_1
    //   149: iload_1
    //   150: bipush #101
    //   152: if_icmpeq -> 161
    //   155: iload_1
    //   156: bipush #69
    //   158: if_icmpne -> 226
    //   161: aload_0
    //   162: aload_0
    //   163: getfield pos : I
    //   166: iconst_1
    //   167: iadd
    //   168: putfield pos : I
    //   171: aload_0
    //   172: iload_2
    //   173: invokespecial nextOrNumberError : (I)C
    //   176: istore_3
    //   177: iload_3
    //   178: bipush #45
    //   180: if_icmpeq -> 191
    //   183: iload_3
    //   184: istore_1
    //   185: iload_3
    //   186: bipush #43
    //   188: if_icmpne -> 197
    //   191: aload_0
    //   192: iload_2
    //   193: invokespecial nextOrNumberError : (I)C
    //   196: istore_1
    //   197: iload_1
    //   198: bipush #48
    //   200: if_icmplt -> 216
    //   203: iload_1
    //   204: bipush #57
    //   206: if_icmpgt -> 216
    //   209: aload_0
    //   210: invokespecial readDigits : ()V
    //   213: goto -> 226
    //   216: aload_0
    //   217: iload_2
    //   218: aload_0
    //   219: getfield pos : I
    //   222: invokespecial numberError : (II)Lcom/trendmicro/hippo/json/JsonParser$ParseException;
    //   225: athrow
    //   226: aload_0
    //   227: getfield src : Ljava/lang/String;
    //   230: iload_2
    //   231: aload_0
    //   232: getfield pos : I
    //   235: invokevirtual substring : (II)Ljava/lang/String;
    //   238: invokestatic parseDouble : (Ljava/lang/String;)D
    //   241: dstore #4
    //   243: dload #4
    //   245: d2i
    //   246: istore_1
    //   247: iload_1
    //   248: i2d
    //   249: dload #4
    //   251: dcmpl
    //   252: ifne -> 260
    //   255: iload_1
    //   256: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   259: areturn
    //   260: dload #4
    //   262: invokestatic valueOf : (D)Ljava/lang/Double;
    //   265: areturn
  }
  
  private Object readObject() throws ParseException {
    consumeWhitespace();
    Scriptable scriptable = this.cx.newObject(this.scope);
    int i = this.pos;
    if (i < this.length && this.src.charAt(i) == '}') {
      this.pos++;
      return scriptable;
    } 
    i = 0;
    while (true) {
      int j = this.pos;
      if (j < this.length) {
        String str = this.src;
        this.pos = j + 1;
        j = str.charAt(j);
        if (j != 34) {
          if (j != 44) {
            if (j == 125) {
              if (i != 0)
                return scriptable; 
              throw new ParseException("Unexpected comma in object literal");
            } 
            throw new ParseException("Unexpected token in object literal");
          } 
          if (i != 0) {
            i = 0;
          } else {
            throw new ParseException("Unexpected comma in object literal");
          } 
        } else if (i == 0) {
          str = readString();
          consume(':');
          Object object = readValue();
          long l = ScriptRuntime.indexFromString(str);
          if (l < 0L) {
            scriptable.put(str, scriptable, object);
          } else {
            scriptable.put((int)l, scriptable, object);
          } 
          i = 1;
        } else {
          throw new ParseException("Missing comma in object literal");
        } 
        consumeWhitespace();
        continue;
      } 
      throw new ParseException("Unterminated object literal");
    } 
  }
  
  private String readString() throws ParseException {
    int i = this.pos;
    while (true) {
      int j = this.pos;
      if (j < this.length) {
        String str = this.src;
        this.pos = j + 1;
        j = str.charAt(j);
        if (j > 31) {
          if (j == 92)
            break; 
          if (j == 34)
            return this.src.substring(i, this.pos - 1); 
          continue;
        } 
        throw new ParseException("String contains control character");
      } 
      break;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    label68: while (true) {
      int j = this.pos;
      if (j < this.length) {
        stringBuilder.append(this.src, i, j - 1);
        i = this.pos;
        if (i < this.length) {
          String str = this.src;
          this.pos = i + 1;
          char c = str.charAt(i);
          if (c != '"') {
            if (c != '/') {
              if (c != '\\') {
                if (c != 'b') {
                  if (c != 'f') {
                    if (c != 'n') {
                      if (c != 'r') {
                        if (c != 't') {
                          if (c == 'u') {
                            i = this.length;
                            j = this.pos;
                            if (i - j >= 5) {
                              i = fromHex(this.src.charAt(j + 0)) << 12 | fromHex(this.src.charAt(this.pos + 1)) << 8 | fromHex(this.src.charAt(this.pos + 2)) << 4 | fromHex(this.src.charAt(this.pos + 3));
                              if (i >= 0) {
                                this.pos += 4;
                                stringBuilder.append((char)i);
                              } else {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Invalid character code: ");
                                str = this.src;
                                i = this.pos;
                                stringBuilder.append(str.substring(i, i + 4));
                                throw new ParseException(stringBuilder.toString());
                              } 
                            } else {
                              stringBuilder = new StringBuilder();
                              stringBuilder.append("Invalid character code: \\u");
                              stringBuilder.append(this.src.substring(this.pos));
                              throw new ParseException(stringBuilder.toString());
                            } 
                          } else {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Unexpected character in string: '\\");
                            stringBuilder.append(c);
                            stringBuilder.append("'");
                            throw new ParseException(stringBuilder.toString());
                          } 
                        } else {
                          stringBuilder.append('\t');
                        } 
                      } else {
                        stringBuilder.append('\r');
                      } 
                    } else {
                      stringBuilder.append('\n');
                    } 
                  } else {
                    stringBuilder.append('\f');
                  } 
                } else {
                  stringBuilder.append('\b');
                } 
              } else {
                stringBuilder.append('\\');
              } 
            } else {
              stringBuilder.append('/');
            } 
          } else {
            stringBuilder.append('"');
          } 
          i = this.pos;
          while (true) {
            j = this.pos;
            if (j < this.length) {
              str = this.src;
              this.pos = j + 1;
              j = str.charAt(j);
              if (j > 31) {
                if (j == 92)
                  continue label68; 
                if (j == 34) {
                  stringBuilder.append(this.src, i, this.pos - 1);
                  return stringBuilder.toString();
                } 
                continue;
              } 
              throw new ParseException("String contains control character");
            } 
            continue label68;
          } 
          break;
        } 
        throw new ParseException("Unterminated string");
      } 
      throw new ParseException("Unterminated string literal");
    } 
  }
  
  private Boolean readTrue() throws ParseException {
    int i = this.length;
    int j = this.pos;
    if (i - j >= 3 && this.src.charAt(j) == 'r' && this.src.charAt(this.pos + 1) == 'u' && this.src.charAt(this.pos + 2) == 'e') {
      this.pos += 3;
      return Boolean.TRUE;
    } 
    throw new ParseException("Unexpected token: t");
  }
  
  private Object readValue() throws ParseException {
    consumeWhitespace();
    int i = this.pos;
    if (i < this.length) {
      String str = this.src;
      this.pos = i + 1;
      char c = str.charAt(i);
      if (c != '"') {
        if (c != '-')
          if (c != '[') {
            if (c != 'f') {
              if (c != 'n') {
                if (c != 't') {
                  if (c != '{') {
                    StringBuilder stringBuilder;
                    switch (c) {
                      default:
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected token: ");
                        stringBuilder.append(c);
                        throw new ParseException(stringBuilder.toString());
                      case '0':
                      case '1':
                      case '2':
                      case '3':
                      case '4':
                      case '5':
                      case '6':
                      case '7':
                      case '8':
                      case '9':
                        break;
                    } 
                  } else {
                    return readObject();
                  } 
                } else {
                  return readTrue();
                } 
              } else {
                return readNull();
              } 
            } else {
              return readFalse();
            } 
          } else {
            return readArray();
          }  
        return readNumber(c);
      } 
      return readString();
    } 
    throw new ParseException("Empty JSON string");
  }
  
  public Object parseValue(String paramString) throws ParseException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 99
    //   6: aload_0
    //   7: iconst_0
    //   8: putfield pos : I
    //   11: aload_0
    //   12: aload_1
    //   13: invokevirtual length : ()I
    //   16: putfield length : I
    //   19: aload_0
    //   20: aload_1
    //   21: putfield src : Ljava/lang/String;
    //   24: aload_0
    //   25: invokespecial readValue : ()Ljava/lang/Object;
    //   28: astore_1
    //   29: aload_0
    //   30: invokespecial consumeWhitespace : ()V
    //   33: aload_0
    //   34: getfield pos : I
    //   37: istore_2
    //   38: aload_0
    //   39: getfield length : I
    //   42: istore_3
    //   43: iload_2
    //   44: iload_3
    //   45: if_icmplt -> 52
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: areturn
    //   52: new com/trendmicro/hippo/json/JsonParser$ParseException
    //   55: astore_1
    //   56: new java/lang/StringBuilder
    //   59: astore #4
    //   61: aload #4
    //   63: invokespecial <init> : ()V
    //   66: aload #4
    //   68: ldc 'Expected end of stream at char '
    //   70: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: aload #4
    //   76: aload_0
    //   77: getfield pos : I
    //   80: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_1
    //   85: aload #4
    //   87: invokevirtual toString : ()Ljava/lang/String;
    //   90: invokespecial <init> : (Ljava/lang/String;)V
    //   93: aload_1
    //   94: athrow
    //   95: astore_1
    //   96: goto -> 111
    //   99: new com/trendmicro/hippo/json/JsonParser$ParseException
    //   102: astore_1
    //   103: aload_1
    //   104: ldc 'Input string may not be null'
    //   106: invokespecial <init> : (Ljava/lang/String;)V
    //   109: aload_1
    //   110: athrow
    //   111: aload_0
    //   112: monitorexit
    //   113: aload_1
    //   114: athrow
    // Exception table:
    //   from	to	target	type
    //   6	43	95	finally
    //   52	95	95	finally
    //   99	111	95	finally
  }
  
  public static class ParseException extends Exception {
    private static final long serialVersionUID = 4804542791749920772L;
    
    ParseException(Exception param1Exception) {
      super(param1Exception);
    }
    
    ParseException(String param1String) {
      super(param1String);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/json/JsonParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */