package com.trendmicro.hippo;

class SpecialRef extends Ref {
  private static final int SPECIAL_NONE = 0;
  
  private static final int SPECIAL_PARENT = 2;
  
  private static final int SPECIAL_PROTO = 1;
  
  private static final long serialVersionUID = -7521596632456797847L;
  
  private String name;
  
  private Scriptable target;
  
  private int type;
  
  private SpecialRef(Scriptable paramScriptable, int paramInt, String paramString) {
    this.target = paramScriptable;
    this.type = paramInt;
    this.name = paramString;
  }
  
  static Ref createSpecial(Context paramContext, Scriptable paramScriptable, Object paramObject, String paramString) {
    paramScriptable = ScriptRuntime.toObjectOrNull(paramContext, paramObject, paramScriptable);
    if (paramScriptable != null) {
      boolean bool;
      if (paramString.equals("__proto__")) {
        bool = true;
      } else if (paramString.equals("__parent__")) {
        bool = true;
      } else {
        throw new IllegalArgumentException(paramString);
      } 
      if (!paramContext.hasFeature(5))
        bool = false; 
      return new SpecialRef(paramScriptable, bool, paramString);
    } 
    throw ScriptRuntime.undefReadError(paramObject, paramString);
  }
  
  public boolean delete(Context paramContext) {
    return (this.type == 0) ? ScriptRuntime.deleteObjectElem(this.target, this.name, paramContext) : false;
  }
  
  public Object get(Context paramContext) {
    int i = this.type;
    if (i != 0) {
      if (i != 1) {
        if (i == 2)
          return this.target.getParentScope(); 
        throw Kit.codeBug();
      } 
      return this.target.getPrototype();
    } 
    return ScriptRuntime.getObjectProp(this.target, this.name, paramContext);
  }
  
  public boolean has(Context paramContext) {
    return (this.type == 0) ? ScriptRuntime.hasObjectElem(this.target, this.name, paramContext) : true;
  }
  
  public Object set(Context paramContext, Scriptable paramScriptable, Object paramObject) {
    Scriptable scriptable;
    int i = this.type;
    if (i != 0) {
      if (i == 1 || i == 2) {
        paramObject = ScriptRuntime.toObjectOrNull(paramContext, paramObject, paramScriptable);
        if (paramObject != null) {
          Object object = paramObject;
          while (true) {
            if (object != this.target) {
              if (this.type == 1) {
                scriptable = object.getPrototype();
              } else {
                scriptable = object.getParentScope();
              } 
              object = scriptable;
              if (scriptable == null)
                break; 
              continue;
            } 
            throw Context.reportRuntimeError1("msg.cyclic.value", this.name);
          } 
        } 
        if (this.type == 1) {
          this.target.setPrototype((Scriptable)paramObject);
        } else {
          this.target.setParentScope((Scriptable)paramObject);
        } 
        return paramObject;
      } 
      throw Kit.codeBug();
    } 
    return ScriptRuntime.setObjectProp(this.target, this.name, paramObject, (Context)scriptable);
  }
  
  @Deprecated
  public Object set(Context paramContext, Object paramObject) {
    throw new IllegalStateException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/SpecialRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */