package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.IdFunctionCall;
import com.trendmicro.hippo.IdFunctionObject;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.Undefined;

class XMLCtor extends IdFunctionObject {
  private static final int Id_defaultSettings = 1;
  
  private static final int Id_ignoreComments = 1;
  
  private static final int Id_ignoreProcessingInstructions = 2;
  
  private static final int Id_ignoreWhitespace = 3;
  
  private static final int Id_prettyIndent = 4;
  
  private static final int Id_prettyPrinting = 5;
  
  private static final int Id_setSettings = 3;
  
  private static final int Id_settings = 2;
  
  private static final int MAX_FUNCTION_ID = 3;
  
  private static final int MAX_INSTANCE_ID = 5;
  
  private static final Object XMLCTOR_TAG = "XMLCtor";
  
  static final long serialVersionUID = -8708195078359817341L;
  
  private XmlProcessor options;
  
  XMLCtor(XML paramXML, Object paramObject, int paramInt1, int paramInt2) {
    super((IdFunctionCall)paramXML, paramObject, paramInt1, paramInt2);
    this.options = paramXML.getProcessor();
    activatePrototypeMap(3);
  }
  
  private void readSettings(Scriptable paramScriptable) {
    for (byte b = 1;; b++) {
      int i;
      Object object;
      if (b <= 5) {
        i = super.getMaxInstanceId() + b;
        object = ScriptableObject.getProperty(paramScriptable, getInstanceIdName(i));
        if (object == Scriptable.NOT_FOUND)
          continue; 
        if (b != 1 && b != 2 && b != 3)
          if (b != 4) {
            if (b != 5)
              throw new IllegalStateException(); 
          } else {
            if (!(object instanceof Number))
              continue; 
            setInstanceIdValue(i, object);
          }  
        if (!(object instanceof Boolean))
          continue; 
      } else {
        break;
      } 
      setInstanceIdValue(i, object);
    } 
  }
  
  private void writeSetting(Scriptable paramScriptable) {
    for (byte b = 1; b <= 5; b++) {
      int i = super.getMaxInstanceId() + b;
      ScriptableObject.putProperty(paramScriptable, getInstanceIdName(i), getInstanceIdValue(i));
    } 
  }
  
  public Object execIdCall(IdFunctionObject paramIdFunctionObject, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    if (!paramIdFunctionObject.hasTag(XMLCTOR_TAG))
      return super.execIdCall(paramIdFunctionObject, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    int i = paramIdFunctionObject.methodId();
    if (i != 1) {
      if (i != 2) {
        if (i == 3) {
          if (paramArrayOfObject.length == 0 || paramArrayOfObject[0] == null || paramArrayOfObject[0] == Undefined.instance) {
            this.options.setDefault();
            return Undefined.instance;
          } 
          if (paramArrayOfObject[0] instanceof Scriptable)
            readSettings((Scriptable)paramArrayOfObject[0]); 
          return Undefined.instance;
        } 
        throw new IllegalArgumentException(String.valueOf(i));
      } 
      Scriptable scriptable1 = paramContext.newObject(paramScriptable1);
      writeSetting(scriptable1);
      return scriptable1;
    } 
    this.options.setDefault();
    Scriptable scriptable = paramContext.newObject(paramScriptable1);
    writeSetting(scriptable);
    return scriptable;
  }
  
  protected int findInstanceIdInfo(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i != 12) {
      if (i != 14) {
        if (i != 16) {
          if (i == 28) {
            str = "ignoreProcessingInstructions";
            b = 2;
          } 
        } else {
          str = "ignoreWhitespace";
          b = 3;
        } 
      } else {
        i = paramString.charAt(0);
        if (i == 105) {
          str = "ignoreComments";
          b = 1;
        } else if (i == 112) {
          str = "prettyPrinting";
          b = 5;
        } 
      } 
    } else {
      str = "prettyIndent";
      b = 4;
    } 
    i = b;
    if (str != null) {
      i = b;
      if (str != paramString) {
        i = b;
        if (!str.equals(paramString))
          i = 0; 
      } 
    } 
    if (i == 0)
      return super.findInstanceIdInfo(paramString); 
    if (i == 1 || i == 2 || i == 3 || i == 4 || i == 5)
      return instanceIdInfo(6, super.getMaxInstanceId() + i); 
    throw new IllegalStateException();
  }
  
  protected int findPrototypeId(String paramString) {
    byte b = 0;
    String str = null;
    int i = paramString.length();
    if (i == 8) {
      str = "settings";
      b = 2;
    } else if (i == 11) {
      str = "setSettings";
      b = 3;
    } else if (i == 15) {
      str = "defaultSettings";
      b = 1;
    } 
    i = b;
    if (str != null) {
      i = b;
      if (str != paramString) {
        i = b;
        if (!str.equals(paramString))
          i = 0; 
      } 
    } 
    return i;
  }
  
  protected String getInstanceIdName(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? super.getInstanceIdName(paramInt) : "prettyPrinting") : "prettyIndent") : "ignoreWhitespace") : "ignoreProcessingInstructions") : "ignoreComments";
  }
  
  protected Object getInstanceIdValue(int paramInt) {
    int i = paramInt - super.getMaxInstanceId();
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? super.getInstanceIdValue(paramInt) : ScriptRuntime.wrapBoolean(this.options.isPrettyPrinting())) : ScriptRuntime.wrapInt(this.options.getPrettyIndent())) : ScriptRuntime.wrapBoolean(this.options.isIgnoreWhitespace())) : ScriptRuntime.wrapBoolean(this.options.isIgnoreProcessingInstructions())) : ScriptRuntime.wrapBoolean(this.options.isIgnoreComments());
  }
  
  protected int getMaxInstanceId() {
    return super.getMaxInstanceId() + 5;
  }
  
  public boolean hasInstance(Scriptable paramScriptable) {
    return (paramScriptable instanceof XML || paramScriptable instanceof XMLList);
  }
  
  protected void initPrototypeId(int paramInt) {
    boolean bool;
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt == 3) {
          bool = true;
          str = "setSettings";
        } else {
          throw new IllegalArgumentException(String.valueOf(paramInt));
        } 
      } else {
        bool = false;
        str = "settings";
      } 
    } else {
      bool = false;
      str = "defaultSettings";
    } 
    initPrototypeMethod(XMLCTOR_TAG, paramInt, str, bool);
  }
  
  protected void setInstanceIdValue(int paramInt, Object paramObject) {
    int i = paramInt - super.getMaxInstanceId();
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i != 5) {
              super.setInstanceIdValue(paramInt, paramObject);
              return;
            } 
            this.options.setPrettyPrinting(ScriptRuntime.toBoolean(paramObject));
            return;
          } 
          this.options.setPrettyIndent(ScriptRuntime.toInt32(paramObject));
          return;
        } 
        this.options.setIgnoreWhitespace(ScriptRuntime.toBoolean(paramObject));
        return;
      } 
      this.options.setIgnoreProcessingInstructions(ScriptRuntime.toBoolean(paramObject));
      return;
    } 
    this.options.setIgnoreComments(ScriptRuntime.toBoolean(paramObject));
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XMLCtor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */