package com.trendmicro.hippo.v8dtoa;

import java.util.Arrays;

public class FastDtoaBuilder {
  static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
  
  final char[] chars = new char[25];
  
  int end = 0;
  
  boolean formatted = false;
  
  int point;
  
  private void toExponentialFormat(int paramInt1, int paramInt2) {
    int i = this.end;
    if (i - paramInt1 > 1) {
      paramInt1++;
      char[] arrayOfChar1 = this.chars;
      System.arraycopy(arrayOfChar1, paramInt1, arrayOfChar1, paramInt1 + 1, i - paramInt1);
      this.chars[paramInt1] = (char)'.';
      this.end++;
    } 
    char[] arrayOfChar = this.chars;
    paramInt1 = this.end;
    this.end = paramInt1 + 1;
    arrayOfChar[paramInt1] = (char)'e';
    paramInt1 = 43;
    i = paramInt2 - 1;
    paramInt2 = i;
    if (i < 0) {
      paramInt1 = 45;
      paramInt2 = -i;
    } 
    arrayOfChar = this.chars;
    int j = this.end;
    i = j + 1;
    this.end = i;
    arrayOfChar[j] = (char)paramInt1;
    if (paramInt2 > 99) {
      paramInt1 = i + 2;
    } else {
      paramInt1 = i;
      if (paramInt2 > 9)
        paramInt1 = i + 1; 
    } 
    this.end = paramInt1 + 1;
    while (true) {
      this.chars[paramInt1] = (char)digits[paramInt2 % 10];
      paramInt2 /= 10;
      if (paramInt2 == 0)
        return; 
      paramInt1--;
    } 
  }
  
  private void toFixedFormat(int paramInt1, int paramInt2) {
    int i = this.point;
    int j = this.end;
    if (i < j) {
      if (paramInt2 > 0) {
        char[] arrayOfChar = this.chars;
        System.arraycopy(arrayOfChar, i, arrayOfChar, i + 1, j - i);
        this.chars[this.point] = (char)'.';
        this.end++;
      } else {
        i = paramInt1 + 2 - paramInt2;
        char[] arrayOfChar = this.chars;
        System.arraycopy(arrayOfChar, paramInt1, arrayOfChar, i, j - paramInt1);
        arrayOfChar = this.chars;
        arrayOfChar[paramInt1] = (char)'0';
        arrayOfChar[paramInt1 + 1] = (char)'.';
        if (paramInt2 < 0)
          Arrays.fill(arrayOfChar, paramInt1 + 2, i, '0'); 
        this.end += 2 - paramInt2;
      } 
    } else if (i > j) {
      Arrays.fill(this.chars, j, i, '0');
      paramInt1 = this.end;
      this.end = paramInt1 + this.point - paramInt1;
    } 
  }
  
  void append(char paramChar) {
    char[] arrayOfChar = this.chars;
    int i = this.end;
    this.end = i + 1;
    arrayOfChar[i] = (char)paramChar;
  }
  
  void decreaseLast() {
    char[] arrayOfChar = this.chars;
    int i = this.end - 1;
    arrayOfChar[i] = (char)(char)(arrayOfChar[i] - 1);
  }
  
  public String format() {
    if (!this.formatted) {
      byte b;
      if (this.chars[0] == '-') {
        b = 1;
      } else {
        b = 0;
      } 
      int i = this.point - b;
      if (i < -5 || i > 21) {
        toExponentialFormat(b, i);
      } else {
        toFixedFormat(b, i);
      } 
      this.formatted = true;
    } 
    return new String(this.chars, 0, this.end);
  }
  
  public void reset() {
    this.end = 0;
    this.formatted = false;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[chars:");
    stringBuilder.append(new String(this.chars, 0, this.end));
    stringBuilder.append(", point:");
    stringBuilder.append(this.point);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/v8dtoa/FastDtoaBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */