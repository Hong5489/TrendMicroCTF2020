package com.trendmicro.hippo.regexp;

import java.io.Serializable;

final class RECharSet implements Serializable {
  private static final long serialVersionUID = 7931787979395898394L;
  
  volatile transient byte[] bits;
  
  volatile transient boolean converted;
  
  final int length;
  
  final boolean sense;
  
  final int startIndex;
  
  final int strlength;
  
  RECharSet(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    this.length = paramInt1;
    this.startIndex = paramInt2;
    this.strlength = paramInt3;
    this.sense = paramBoolean;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/RECharSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */