package com.trendmicro.hippo.xml;

import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Ref;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;

public abstract class XMLLib {
  private static final Object XML_LIB_KEY = new Object();
  
  public static XMLLib extractFromScope(Scriptable paramScriptable) {
    XMLLib xMLLib = extractFromScopeOrNull(paramScriptable);
    if (xMLLib != null)
      return xMLLib; 
    throw Context.reportRuntimeError(ScriptRuntime.getMessage0("msg.XML.not.available"));
  }
  
  public static XMLLib extractFromScopeOrNull(Scriptable paramScriptable) {
    ScriptableObject scriptableObject = ScriptRuntime.getLibraryScopeOrNull(paramScriptable);
    if (scriptableObject == null)
      return null; 
    ScriptableObject.getProperty((Scriptable)scriptableObject, "XML");
    return (XMLLib)scriptableObject.getAssociatedValue(XML_LIB_KEY);
  }
  
  protected final XMLLib bindToScope(Scriptable paramScriptable) {
    ScriptableObject scriptableObject = ScriptRuntime.getLibraryScopeOrNull(paramScriptable);
    if (scriptableObject != null)
      return (XMLLib)scriptableObject.associateValue(XML_LIB_KEY, this); 
    throw new IllegalStateException();
  }
  
  public abstract String escapeAttributeValue(Object paramObject);
  
  public abstract String escapeTextValue(Object paramObject);
  
  public int getPrettyIndent() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isIgnoreComments() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isIgnoreProcessingInstructions() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isIgnoreWhitespace() {
    throw new UnsupportedOperationException();
  }
  
  public boolean isPrettyPrinting() {
    throw new UnsupportedOperationException();
  }
  
  public abstract boolean isXMLName(Context paramContext, Object paramObject);
  
  public abstract Ref nameRef(Context paramContext, Object paramObject, Scriptable paramScriptable, int paramInt);
  
  public abstract Ref nameRef(Context paramContext, Object paramObject1, Object paramObject2, Scriptable paramScriptable, int paramInt);
  
  public void setIgnoreComments(boolean paramBoolean) {
    throw new UnsupportedOperationException();
  }
  
  public void setIgnoreProcessingInstructions(boolean paramBoolean) {
    throw new UnsupportedOperationException();
  }
  
  public void setIgnoreWhitespace(boolean paramBoolean) {
    throw new UnsupportedOperationException();
  }
  
  public void setPrettyIndent(int paramInt) {
    throw new UnsupportedOperationException();
  }
  
  public void setPrettyPrinting(boolean paramBoolean) {
    throw new UnsupportedOperationException();
  }
  
  public abstract Object toDefaultXmlNamespace(Context paramContext, Object paramObject);
  
  public static abstract class Factory {
    public static Factory create(final String className) {
      return new Factory() {
          public String getImplementationClassName() {
            return className;
          }
        };
    }
    
    public abstract String getImplementationClassName();
  }
  
  class null extends Factory {
    public String getImplementationClassName() {
      return className;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xml/XMLLib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */