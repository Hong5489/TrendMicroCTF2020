package com.trendmicro.hippo.regexp;

import com.trendmicro.hippo.Context;

class CompilerState {
  int backReferenceLimit;
  
  int classCount;
  
  int cp;
  
  char[] cpbegin;
  
  int cpend;
  
  Context cx;
  
  int flags;
  
  int maxBackReference;
  
  int parenCount;
  
  int parenNesting;
  
  int progLength;
  
  RENode result;
  
  CompilerState(Context paramContext, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    this.cx = paramContext;
    this.cpbegin = paramArrayOfchar;
    this.cp = 0;
    this.cpend = paramInt1;
    this.flags = paramInt2;
    this.backReferenceLimit = Integer.MAX_VALUE;
    this.maxBackReference = 0;
    this.parenCount = 0;
    this.classCount = 0;
    this.progLength = 0;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/CompilerState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */