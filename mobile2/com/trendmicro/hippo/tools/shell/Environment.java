package com.trendmicro.hippo.tools.shell;

import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;

public class Environment extends ScriptableObject {
  static final long serialVersionUID = -430727378460177065L;
  
  private Environment thePrototypeInstance = null;
  
  public Environment() {
    if (!false)
      this.thePrototypeInstance = this; 
  }
  
  public Environment(ScriptableObject paramScriptableObject) {
    setParentScope((Scriptable)paramScriptableObject);
    Object object = ScriptRuntime.getTopLevelProp((Scriptable)paramScriptableObject, "Environment");
    if (object != null && object instanceof Scriptable) {
      object = object;
      setPrototype((Scriptable)object.get("prototype", (Scriptable)object));
    } 
  }
  
  private Object[] collectIds() {
    return System.getProperties().keySet().toArray();
  }
  
  public static void defineClass(ScriptableObject paramScriptableObject) {
    try {
      ScriptableObject.defineClass((Scriptable)paramScriptableObject, Environment.class);
      return;
    } catch (Exception exception) {
      throw new Error(exception.getMessage());
    } 
  }
  
  public Object get(String paramString, Scriptable paramScriptable) {
    if (this == this.thePrototypeInstance)
      return super.get(paramString, paramScriptable); 
    paramString = System.getProperty(paramString);
    return (paramString != null) ? ScriptRuntime.toObject(getParentScope(), paramString) : Scriptable.NOT_FOUND;
  }
  
  public Object[] getAllIds() {
    return (this == this.thePrototypeInstance) ? super.getAllIds() : collectIds();
  }
  
  public String getClassName() {
    return "Environment";
  }
  
  public Object[] getIds() {
    return (this == this.thePrototypeInstance) ? super.getIds() : collectIds();
  }
  
  public boolean has(String paramString, Scriptable paramScriptable) {
    boolean bool;
    if (this == this.thePrototypeInstance)
      return super.has(paramString, paramScriptable); 
    if (System.getProperty(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void put(String paramString, Scriptable paramScriptable, Object paramObject) {
    if (this == this.thePrototypeInstance) {
      super.put(paramString, paramScriptable, paramObject);
    } else {
      System.getProperties().put(paramString, ScriptRuntime.toString(paramObject));
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/shell/Environment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */