package com.trendmicro.hippo;

public class NativeJavaConstructor extends BaseFunction {
  private static final long serialVersionUID = -8149253217482668463L;
  
  MemberBox ctor;
  
  public NativeJavaConstructor(MemberBox paramMemberBox) {
    this.ctor = paramMemberBox;
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    return NativeJavaClass.constructSpecific(paramContext, paramScriptable1, paramArrayOfObject, this.ctor);
  }
  
  public String getFunctionName() {
    return "<init>".concat(JavaMembers.liveConnectSignature(this.ctor.argTypes));
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[JavaConstructor ");
    stringBuilder.append(this.ctor.getName());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/NativeJavaConstructor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */