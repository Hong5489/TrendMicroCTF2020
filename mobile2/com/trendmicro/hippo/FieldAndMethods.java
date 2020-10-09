package com.trendmicro.hippo;

import java.lang.reflect.Field;

class FieldAndMethods extends NativeJavaMethod {
  private static final long serialVersionUID = -9222428244284796755L;
  
  Field field;
  
  Object javaObject;
  
  FieldAndMethods(Scriptable paramScriptable, MemberBox[] paramArrayOfMemberBox, Field paramField) {
    super(paramArrayOfMemberBox);
    this.field = paramField;
    setParentScope(paramScriptable);
    setPrototype(ScriptableObject.getFunctionPrototype(paramScriptable));
  }
  
  public Object getDefaultValue(Class<?> paramClass) {
    if (paramClass == ScriptRuntime.FunctionClass)
      return this; 
    try {
      Object object1 = this.field.get(this.javaObject);
      Class<?> clazz = this.field.getType();
      Context context = Context.getContext();
      Object object2 = context.getWrapFactory().wrap(context, this, object1, clazz);
      Object object3 = object2;
      if (object2 instanceof Scriptable)
        object3 = ((Scriptable)object2).getDefaultValue(paramClass); 
      return object3;
    } catch (IllegalAccessException illegalAccessException) {
      throw Context.reportRuntimeError1("msg.java.internal.private", this.field.getName());
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/FieldAndMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */