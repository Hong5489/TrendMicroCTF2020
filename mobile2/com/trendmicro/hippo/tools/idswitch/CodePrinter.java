package com.trendmicro.hippo.tools.idswitch;

class CodePrinter {
  private static final int LITERAL_CHAR_MAX_SIZE = 6;
  
  private char[] buffer = new char[4096];
  
  private int indentStep = 4;
  
  private int indentTabSize = 8;
  
  private String lineTerminator = System.lineSeparator();
  
  private int offset;
  
  private int add_area(int paramInt) {
    int i = ensure_area(paramInt);
    this.offset = i + paramInt;
    return i;
  }
  
  private static char digit_to_hex_letter(int paramInt) {
    if (paramInt < 10) {
      paramInt += 48;
    } else {
      paramInt += 55;
    } 
    return (char)paramInt;
  }
  
  private int ensure_area(int paramInt) {
    int i = this.offset;
    int j = i + paramInt;
    char[] arrayOfChar = this.buffer;
    if (j > arrayOfChar.length) {
      int k = arrayOfChar.length * 2;
      paramInt = k;
      if (j > k)
        paramInt = j; 
      arrayOfChar = new char[paramInt];
      System.arraycopy(this.buffer, 0, arrayOfChar, 0, i);
      this.buffer = arrayOfChar;
    } 
    return i;
  }
  
  private int put_string_literal_char(int paramInt1, int paramInt2, boolean paramBoolean) {
    boolean bool = true;
    if (paramInt2 != 12) {
      if (paramInt2 != 13) {
        if (paramInt2 != 34)
          if (paramInt2 != 39) {
            switch (paramInt2) {
              default:
                paramBoolean = false;
                break;
              case 10:
                paramInt2 = 110;
                paramBoolean = bool;
                break;
              case 9:
                paramInt2 = 116;
                paramBoolean = bool;
                break;
              case 8:
                paramInt2 = 98;
                paramBoolean = bool;
                break;
            } 
          } else {
            int i = paramBoolean ^ true;
          }  
      } else {
        paramInt2 = 114;
        paramBoolean = bool;
      } 
    } else {
      paramInt2 = 102;
      paramBoolean = bool;
    } 
    if (paramBoolean) {
      char[] arrayOfChar = this.buffer;
      arrayOfChar[paramInt1] = (char)'\\';
      arrayOfChar[paramInt1 + 1] = (char)(char)paramInt2;
      paramInt1 += 2;
    } else if (32 <= paramInt2 && paramInt2 <= 126) {
      this.buffer[paramInt1] = (char)(char)paramInt2;
      paramInt1++;
    } else {
      char[] arrayOfChar = this.buffer;
      arrayOfChar[paramInt1] = (char)'\\';
      arrayOfChar[paramInt1 + 1] = (char)'u';
      arrayOfChar[paramInt1 + 2] = digit_to_hex_letter(paramInt2 >> 12 & 0xF);
      this.buffer[paramInt1 + 3] = digit_to_hex_letter(paramInt2 >> 8 & 0xF);
      this.buffer[paramInt1 + 4] = digit_to_hex_letter(paramInt2 >> 4 & 0xF);
      this.buffer[paramInt1 + 5] = digit_to_hex_letter(paramInt2 & 0xF);
      paramInt1 += 6;
    } 
    return paramInt1;
  }
  
  public void clear() {
    this.offset = 0;
  }
  
  public void erase(int paramInt1, int paramInt2) {
    char[] arrayOfChar = this.buffer;
    System.arraycopy(arrayOfChar, paramInt2, arrayOfChar, paramInt1, this.offset - paramInt2);
    this.offset -= paramInt2 - paramInt1;
  }
  
  public int getIndentStep() {
    return this.indentStep;
  }
  
  public int getIndentTabSize() {
    return this.indentTabSize;
  }
  
  public int getLastChar() {
    int i = this.offset;
    if (i == 0) {
      i = -1;
    } else {
      i = this.buffer[i - 1];
    } 
    return i;
  }
  
  public String getLineTerminator() {
    return this.lineTerminator;
  }
  
  public int getOffset() {
    return this.offset;
  }
  
  public void indent(int paramInt) {
    int j;
    int i = this.indentStep * paramInt;
    paramInt = this.indentTabSize;
    if (paramInt <= 0) {
      j = 0;
    } else {
      j = i / paramInt;
      i = i % paramInt + j;
    } 
    int k = add_area(i);
    paramInt = k;
    while (true) {
      int m = paramInt;
      paramInt = m;
      if (m != k + j) {
        this.buffer[m] = (char)'\t';
        paramInt = m + 1;
        continue;
      } 
      break;
    } 
    while (paramInt != k + i) {
      this.buffer[paramInt] = (char)' ';
      paramInt++;
    } 
  }
  
  public void line(int paramInt, String paramString) {
    indent(paramInt);
    p(paramString);
    nl();
  }
  
  public void nl() {
    p(getLineTerminator());
  }
  
  public void p(char paramChar) {
    int i = add_area(1);
    this.buffer[i] = (char)paramChar;
  }
  
  public void p(int paramInt) {
    p(Integer.toString(paramInt));
  }
  
  public void p(String paramString) {
    int i = paramString.length();
    int j = add_area(i);
    paramString.getChars(0, i, this.buffer, j);
  }
  
  public final void p(char[] paramArrayOfchar) {
    p(paramArrayOfchar, 0, paramArrayOfchar.length);
  }
  
  public void p(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    int i = paramInt2 - paramInt1;
    paramInt2 = add_area(i);
    System.arraycopy(paramArrayOfchar, paramInt1, this.buffer, paramInt2, i);
  }
  
  public void qchar(int paramInt) {
    int i = ensure_area(8);
    this.buffer[i] = (char)'\'';
    paramInt = put_string_literal_char(i + 1, paramInt, false);
    this.buffer[paramInt] = (char)'\'';
    this.offset = paramInt + 1;
  }
  
  public void qstring(String paramString) {
    int i = paramString.length();
    int j = ensure_area(i * 6 + 2);
    this.buffer[j] = (char)'"';
    int k = j + 1;
    for (j = 0; j != i; j++)
      k = put_string_literal_char(k, paramString.charAt(j), true); 
    this.buffer[k] = (char)'"';
    this.offset = k + 1;
  }
  
  public void setIndentStep(int paramInt) {
    this.indentStep = paramInt;
  }
  
  public void setIndentTabSize(int paramInt) {
    this.indentTabSize = paramInt;
  }
  
  public void setLineTerminator(String paramString) {
    this.lineTerminator = paramString;
  }
  
  public String toString() {
    return new String(this.buffer, 0, this.offset);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/idswitch/CodePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */