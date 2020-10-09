package com.trendmicro.hippo.regexp;

class REBackTrackData {
  final int continuationOp;
  
  final int continuationPc;
  
  final int cp;
  
  final int op;
  
  final long[] parens;
  
  final int pc;
  
  final REBackTrackData previous;
  
  final REProgState stateStackTop;
  
  REBackTrackData(REGlobalData paramREGlobalData, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    this.previous = paramREGlobalData.backTrackStackTop;
    this.op = paramInt1;
    this.pc = paramInt2;
    this.cp = paramInt3;
    this.continuationOp = paramInt4;
    this.continuationPc = paramInt5;
    this.parens = paramREGlobalData.parens;
    this.stateStackTop = paramREGlobalData.stateStackTop;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/REBackTrackData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */