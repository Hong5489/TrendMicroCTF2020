package com.trendmicro.hippo.tools.idswitch;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class FileBody {
  private char[] buffer = new char[16384];
  
  private int bufferEnd;
  
  ReplaceItem firstReplace;
  
  ReplaceItem lastReplace;
  
  private int lineBegin;
  
  private int lineEnd;
  
  private int lineNumber;
  
  private int nextLineStart;
  
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
  
  public char[] getBuffer() {
    return this.buffer;
  }
  
  public int getLineBegin() {
    return this.lineBegin;
  }
  
  public int getLineEnd() {
    return this.lineEnd;
  }
  
  public int getLineNumber() {
    return this.lineNumber;
  }
  
  public boolean nextLine() {
    if (this.nextLineStart == this.bufferEnd) {
      this.lineNumber = 0;
      return false;
    } 
    char c = Character.MIN_VALUE;
    int i = this.nextLineStart;
    while (i != this.bufferEnd) {
      char c1 = this.buffer[i];
      c = c1;
      if (c1 != '\n') {
        if (c1 == '\r') {
          c = c1;
          break;
        } 
        i++;
        c = c1;
      } 
    } 
    this.lineBegin = this.nextLineStart;
    this.lineEnd = i;
    int j = this.bufferEnd;
    if (i == j) {
      this.nextLineStart = i;
    } else if (c == '\r' && i + 1 != j && this.buffer[i + 1] == '\n') {
      this.nextLineStart = i + 2;
    } else {
      this.nextLineStart = i + 1;
    } 
    this.lineNumber++;
    return true;
  }
  
  public void readData(Reader paramReader) throws IOException {
    int i = this.buffer.length;
    for (int j = 0;; j = k) {
      int k = paramReader.read(this.buffer, j, i - j);
      if (k < 0) {
        this.bufferEnd = j;
        return;
      } 
      k = j + k;
      j = i;
      if (i == k) {
        j = i * 2;
        char[] arrayOfChar = new char[j];
        System.arraycopy(this.buffer, 0, arrayOfChar, 0, k);
        this.buffer = arrayOfChar;
      } 
      i = j;
    } 
  }
  
  public boolean setReplacement(int paramInt1, int paramInt2, String paramString) {
    if (equals(paramString, this.buffer, paramInt1, paramInt2))
      return false; 
    ReplaceItem replaceItem2 = new ReplaceItem(paramInt1, paramInt2, paramString);
    ReplaceItem replaceItem1 = this.firstReplace;
    if (replaceItem1 == null) {
      this.lastReplace = replaceItem2;
      this.firstReplace = replaceItem2;
    } else if (paramInt1 < replaceItem1.begin) {
      replaceItem2.next = this.firstReplace;
      this.firstReplace = replaceItem2;
    } else {
      ReplaceItem replaceItem = this.firstReplace;
      for (replaceItem1 = replaceItem.next; replaceItem1 != null; replaceItem1 = replaceItem1.next) {
        if (paramInt1 < replaceItem1.begin) {
          replaceItem2.next = replaceItem1;
          replaceItem.next = replaceItem2;
          break;
        } 
        replaceItem = replaceItem1;
      } 
      if (replaceItem1 == null)
        this.lastReplace.next = replaceItem2; 
    } 
    return true;
  }
  
  public void startLineLoop() {
    this.lineNumber = 0;
    this.nextLineStart = 0;
    this.lineEnd = 0;
    this.lineBegin = 0;
  }
  
  public boolean wasModified() {
    boolean bool;
    if (this.firstReplace != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void writeData(Writer paramWriter) throws IOException {
    int i = 0;
    for (ReplaceItem replaceItem = this.firstReplace; replaceItem != null; replaceItem = replaceItem.next) {
      int k = replaceItem.begin - i;
      if (k > 0)
        paramWriter.write(this.buffer, i, k); 
      paramWriter.write(replaceItem.replacement);
      i = replaceItem.end;
    } 
    int j = this.bufferEnd - i;
    if (j != 0)
      paramWriter.write(this.buffer, i, j); 
  }
  
  public void writeInitialData(Writer paramWriter) throws IOException {
    paramWriter.write(this.buffer, 0, this.bufferEnd);
  }
  
  private static class ReplaceItem {
    int begin;
    
    int end;
    
    ReplaceItem next;
    
    String replacement;
    
    ReplaceItem(int param1Int1, int param1Int2, String param1String) {
      this.begin = param1Int1;
      this.end = param1Int2;
      this.replacement = param1String;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/idswitch/FileBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */