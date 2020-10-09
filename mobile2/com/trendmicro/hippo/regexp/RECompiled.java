package com.trendmicro.hippo.regexp;

import java.io.Serializable;

class RECompiled implements Serializable {
  private static final long serialVersionUID = -6144956577595844213L;
  
  int anchorCh = -1;
  
  int classCount;
  
  RECharSet[] classList;
  
  int flags;
  
  int parenCount;
  
  byte[] program;
  
  final char[] source;
  
  RECompiled(String paramString) {
    this.source = paramString.toCharArray();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/RECompiled.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */