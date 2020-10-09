package com.trendmicro.hippo.regexp;

class REProgState {
  final REBackTrackData backTrack;
  
  final int continuationOp;
  
  final int continuationPc;
  
  final int index;
  
  final int max;
  
  final int min;
  
  final REProgState previous;
  
  REProgState(REProgState paramREProgState, int paramInt1, int paramInt2, int paramInt3, REBackTrackData paramREBackTrackData, int paramInt4, int paramInt5) {
    this.previous = paramREProgState;
    this.min = paramInt1;
    this.max = paramInt2;
    this.index = paramInt3;
    this.continuationOp = paramInt4;
    this.continuationPc = paramInt5;
    this.backTrack = paramREBackTrackData;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/REProgState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */