package com.trendmicro.hippo.xml;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdScriptableObject;
import com.trendmicro.hippo.NativeWith;
import com.trendmicro.hippo.Ref;
import com.trendmicro.hippo.Scriptable;

public abstract class XMLObject extends IdScriptableObject {
  private static final long serialVersionUID = 8455156490438576500L;
  
  public XMLObject() {}
  
  public XMLObject(Scriptable paramScriptable1, Scriptable paramScriptable2) {
    super(paramScriptable1, paramScriptable2);
  }
  
  public Object addValues(Context paramContext, boolean paramBoolean, Object paramObject) {
    return Scriptable.NOT_FOUND;
  }
  
  public abstract boolean delete(Context paramContext, Object paramObject);
  
  public abstract NativeWith enterDotQuery(Scriptable paramScriptable);
  
  public abstract NativeWith enterWith(Scriptable paramScriptable);
  
  public abstract Object get(Context paramContext, Object paramObject);
  
  public abstract Scriptable getExtraMethodSource(Context paramContext);
  
  public abstract Object getFunctionProperty(Context paramContext, int paramInt);
  
  public abstract Object getFunctionProperty(Context paramContext, String paramString);
  
  public String getTypeOf() {
    String str;
    if (avoidObjectDetection()) {
      str = "undefined";
    } else {
      str = "xml";
    } 
    return str;
  }
  
  public abstract boolean has(Context paramContext, Object paramObject);
  
  public abstract Ref memberRef(Context paramContext, Object paramObject, int paramInt);
  
  public abstract Ref memberRef(Context paramContext, Object paramObject1, Object paramObject2, int paramInt);
  
  public abstract void put(Context paramContext, Object paramObject1, Object paramObject2);
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xml/XMLObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */