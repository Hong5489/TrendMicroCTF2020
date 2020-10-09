package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Map;

public class MethodCallsLogger {
  private Map<String, Integer> mCalledMethods = new HashMap<>();
  
  public boolean approveCall(String paramString, int paramInt) {
    int i;
    boolean bool2;
    Integer integer = this.mCalledMethods.get(paramString);
    boolean bool1 = false;
    if (integer != null) {
      i = integer.intValue();
    } else {
      i = 0;
    } 
    if ((i & paramInt) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    this.mCalledMethods.put(paramString, Integer.valueOf(i | paramInt));
    if (!bool2)
      bool1 = true; 
    return bool1;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/arch/lifecycle/MethodCallsLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */