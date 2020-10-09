package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.NativeWith;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.xml.XMLObject;

final class XMLWithScope extends NativeWith {
  private static final long serialVersionUID = -696429282095170887L;
  
  private int _currIndex;
  
  private XMLObject _dqPrototype;
  
  private XMLList _xmlList;
  
  private XMLLibImpl lib;
  
  XMLWithScope(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject) {
    super(paramScriptable, (Scriptable)paramXMLObject);
    this.lib = paramXMLLibImpl;
  }
  
  void initAsDotQuery() {
    XMLObject xMLObject = (XMLObject)getPrototype();
    this._currIndex = 0;
    this._dqPrototype = xMLObject;
    if (xMLObject instanceof XMLList) {
      xMLObject = xMLObject;
      if (xMLObject.length() > 0)
        setPrototype((Scriptable)xMLObject.get(0, null)); 
    } 
    this._xmlList = this.lib.newXMLList();
  }
  
  protected Object updateDotQuery(boolean paramBoolean) {
    XMLObject xMLObject = this._dqPrototype;
    XMLList xMLList = this._xmlList;
    if (xMLObject instanceof XMLList) {
      xMLObject = xMLObject;
      int i = this._currIndex;
      if (paramBoolean)
        xMLList.addToList(xMLObject.get(i, null)); 
      if (++i < xMLObject.length()) {
        this._currIndex = i;
        setPrototype((Scriptable)xMLObject.get(i, null));
        return null;
      } 
    } else if (paramBoolean) {
      xMLList.addToList(xMLObject);
    } 
    return xMLList;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XMLWithScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */