package com.trendmicro.hippo.regexp;

class REGlobalData {
  REBackTrackData backTrackStackTop;
  
  int cp;
  
  boolean multiline;
  
  long[] parens;
  
  RECompiled regexp;
  
  int skipped;
  
  REProgState stateStackTop;
  
  int parensIndex(int paramInt) {
    return (int)this.parens[paramInt];
  }
  
  int parensLength(int paramInt) {
    return (int)(this.parens[paramInt] >>> 32L);
  }
  
  void setParens(int paramInt1, int paramInt2, int paramInt3) {
    REBackTrackData rEBackTrackData = this.backTrackStackTop;
    if (rEBackTrackData != null) {
      long[] arrayOfLong1 = rEBackTrackData.parens;
      long[] arrayOfLong2 = this.parens;
      if (arrayOfLong1 == arrayOfLong2)
        this.parens = (long[])arrayOfLong2.clone(); 
    } 
    this.parens[paramInt1] = paramInt2 & 0xFFFFFFFFL | paramInt3 << 32L;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/REGlobalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */