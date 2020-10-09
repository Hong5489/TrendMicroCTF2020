package com.trendmicro.hippo.regexp;

import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.Scriptable;

final class GlobData {
  Scriptable arrayobj;
  
  StringBuilder charBuf;
  
  int dollar = -1;
  
  boolean global;
  
  Function lambda;
  
  int leftIndex;
  
  int mode;
  
  String repstr;
  
  String str;
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/regexp/GlobData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */