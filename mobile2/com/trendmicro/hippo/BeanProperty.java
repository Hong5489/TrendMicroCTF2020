package com.trendmicro.hippo;

class BeanProperty {
  MemberBox getter;
  
  MemberBox setter;
  
  NativeJavaMethod setters;
  
  BeanProperty(MemberBox paramMemberBox1, MemberBox paramMemberBox2, NativeJavaMethod paramNativeJavaMethod) {
    this.getter = paramMemberBox1;
    this.setter = paramMemberBox2;
    this.setters = paramNativeJavaMethod;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/BeanProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */