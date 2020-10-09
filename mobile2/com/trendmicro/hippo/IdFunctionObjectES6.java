package com.trendmicro.hippo;

public class IdFunctionObjectES6 extends IdFunctionObject {
  private static final int Id_length = 1;
  
  private static final int Id_name = 3;
  
  private boolean myLength = true;
  
  private boolean myName = true;
  
  public IdFunctionObjectES6(IdFunctionCall paramIdFunctionCall, Object paramObject, int paramInt1, String paramString, int paramInt2, Scriptable paramScriptable) {
    super(paramIdFunctionCall, paramObject, paramInt1, paramString, paramInt2, paramScriptable);
  }
  
  protected int findInstanceIdInfo(String paramString) {
    return paramString.equals("length") ? instanceIdInfo(3, 1) : (paramString.equals("name") ? instanceIdInfo(3, 3) : super.findInstanceIdInfo(paramString));
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    return (paramInt == 1 && !this.myLength) ? NOT_FOUND : ((paramInt == 3 && !this.myName) ? NOT_FOUND : super.getInstanceIdValue(paramInt));
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    if (paramInt == 1 && paramObject == NOT_FOUND) {
      this.myLength = false;
      return;
    } 
    if (paramInt == 3 && paramObject == NOT_FOUND) {
      this.myName = false;
      return;
    } 
    super.setInstanceIdValue(paramInt, paramObject);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/IdFunctionObjectES6.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */