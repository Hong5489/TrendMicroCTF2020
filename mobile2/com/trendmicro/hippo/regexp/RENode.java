package com.trendmicro.hippo.regexp;

class RENode {
  int bmsize;
  
  char chr;
  
  int flatIndex;
  
  boolean greedy;
  
  int index;
  
  RENode kid;
  
  RENode kid2;
  
  int kidlen;
  
  int length;
  
  int max;
  
  int min;
  
  RENode next;
  
  byte op;
  
  int parenCount;
  
  int parenIndex;
  
  boolean sense;
  
  int startIndex;
  
  RENode(byte paramByte) {
    this.op = (byte)paramByte;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/RENode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */