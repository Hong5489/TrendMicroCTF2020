package com.trendmicro.hippo.xmlimpl;

import com.trendmicro.hippo.Callable;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.ScriptRuntime;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import com.trendmicro.hippo.Undefined;
import com.trendmicro.hippo.xml.XMLObject;
import java.util.ArrayList;

class XMLList extends XMLObjectImpl implements Function {
  static final long serialVersionUID = -4543618751670781135L;
  
  private XmlNode.InternalList _annos = new XmlNode.InternalList();
  
  private XMLObjectImpl targetObject = null;
  
  private XmlNode.QName targetProperty = null;
  
  XMLList(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject) {
    super(paramXMLLibImpl, paramScriptable, paramXMLObject);
  }
  
  private Object applyOrCall(boolean paramBoolean, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    String str;
    if (paramBoolean) {
      str = "apply";
    } else {
      str = "call";
    } 
    if (paramScriptable2 instanceof XMLList && ((XMLList)paramScriptable2).targetProperty != null)
      return ScriptRuntime.applyOrCall(paramBoolean, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
    throw ScriptRuntime.typeError1("msg.isnt.function", str);
  }
  
  private XMLList getPropertyList(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    XmlNode.QName qName1 = null;
    XmlNode.QName qName2 = qName1;
    if (!paramXMLName.isDescendants()) {
      qName2 = qName1;
      if (!paramXMLName.isAttributeName())
        qName2 = paramXMLName.toQname(); 
    } 
    xMLList.setTargets(this, qName2);
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).getPropertyList(paramXMLName)); 
    return xMLList;
  }
  
  private XML getXML(XmlNode.InternalList paramInternalList, int paramInt) {
    return (paramInt >= 0 && paramInt < length()) ? xmlFromNode(paramInternalList.item(paramInt)) : null;
  }
  
  private XML getXmlFromAnnotation(int paramInt) {
    return getXML(this._annos, paramInt);
  }
  
  private void insert(int paramInt, XML paramXML) {
    if (paramInt < length()) {
      XmlNode.InternalList internalList = new XmlNode.InternalList();
      internalList.add(this._annos, 0, paramInt);
      internalList.add(paramXML);
      internalList.add(this._annos, paramInt, length());
      this._annos = internalList;
    } 
  }
  
  private void internalRemoveFromList(int paramInt) {
    this._annos.remove(paramInt);
  }
  
  private void replaceNode(XML paramXML1, XML paramXML2) {
    paramXML1.replaceWith(paramXML2);
  }
  
  private void setAttribute(XMLName paramXMLName, Object paramObject) {
    for (byte b = 0; b < length(); b++)
      getXmlFromAnnotation(b).setAttribute(paramXMLName, paramObject); 
  }
  
  void addMatches(XMLList paramXMLList, XMLName paramXMLName) {
    for (byte b = 0; b < length(); b++)
      getXmlFromAnnotation(b).addMatches(paramXMLList, paramXMLName); 
  }
  
  void addToList(Object paramObject) {
    this._annos.addToList(paramObject);
  }
  
  public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject) {
    XmlNode.QName qName = this.targetProperty;
    if (qName != null) {
      String str = qName.getLocalName();
      boolean bool = str.equals("apply");
      if (bool || str.equals("call"))
        return applyOrCall(bool, paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject); 
      if (paramScriptable2 instanceof XMLObject) {
        Object object;
        Scriptable scriptable2 = null;
        Scriptable scriptable3 = paramScriptable2;
        Scriptable scriptable1 = paramScriptable2;
        paramScriptable2 = scriptable2;
        while (scriptable3 instanceof XMLObject) {
          object = scriptable3;
          Object object1 = object.getFunctionProperty(paramContext, str);
          if (object1 != Scriptable.NOT_FOUND) {
            object = object1;
            break;
          } 
          scriptable3 = object.getExtraMethodSource(paramContext);
          object = object1;
          if (scriptable3 != null) {
            Scriptable scriptable = scriptable3;
            object = object1;
            scriptable1 = scriptable;
            if (!(scriptable3 instanceof XMLObject)) {
              object = ScriptableObject.getProperty(scriptable3, str);
              scriptable1 = scriptable;
            } 
          } 
        } 
        if (object instanceof Callable)
          return ((Callable)object).call(paramContext, paramScriptable1, scriptable1, paramArrayOfObject); 
        throw ScriptRuntime.notFunctionError(scriptable1, object, str);
      } 
      throw ScriptRuntime.typeError1("msg.incompat.call", str);
    } 
    throw ScriptRuntime.notFunctionError(this);
  }
  
  XMLList child(int paramInt) {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).child(paramInt)); 
    return xMLList;
  }
  
  XMLList child(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).child(paramXMLName)); 
    return xMLList;
  }
  
  XMLList children() {
    ArrayList<XML> arrayList = new ArrayList();
    byte b;
    for (b = 0; b < length(); b++) {
      XML xML = getXmlFromAnnotation(b);
      if (xML != null) {
        XMLList xMLList1 = xML.children();
        int j = xMLList1.length();
        for (byte b1 = 0; b1 < j; b1++)
          arrayList.add(xMLList1.item(b1)); 
      } 
    } 
    XMLList xMLList = newXMLList();
    int i = arrayList.size();
    for (b = 0; b < i; b++)
      xMLList.addToList(arrayList.get(b)); 
    return xMLList;
  }
  
  XMLList comments() {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).comments()); 
    return xMLList;
  }
  
  public Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject) {
    throw ScriptRuntime.typeError1("msg.not.ctor", "XMLList");
  }
  
  boolean contains(Object paramObject) {
    boolean bool2;
    boolean bool1 = false;
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < length()) {
        if (getXmlFromAnnotation(b).equivalentXml(paramObject)) {
          bool2 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return bool2;
  }
  
  XMLObjectImpl copy() {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).copy()); 
    return xMLList;
  }
  
  public void delete(int paramInt) {
    if (paramInt >= 0 && paramInt < length()) {
      getXmlFromAnnotation(paramInt).remove();
      internalRemoveFromList(paramInt);
    } 
  }
  
  void deleteXMLProperty(XMLName paramXMLName) {
    for (byte b = 0; b < length(); b++) {
      XML xML = getXmlFromAnnotation(b);
      if (xML.isElement())
        xML.deleteXMLProperty(paramXMLName); 
    } 
  }
  
  XMLList elements(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).elements(paramXMLName)); 
    return xMLList;
  }
  
  boolean equivalentXml(Object paramObject) {
    boolean bool2;
    boolean bool1 = false;
    if (paramObject instanceof Undefined && length() == 0) {
      bool2 = true;
    } else if (length() == 1) {
      bool2 = getXmlFromAnnotation(0).equivalentXml(paramObject);
    } else {
      bool2 = bool1;
      if (paramObject instanceof XMLList) {
        paramObject = paramObject;
        bool2 = bool1;
        if (paramObject.length() == length()) {
          bool1 = true;
          byte b = 0;
          while (true) {
            bool2 = bool1;
            if (b < length()) {
              if (!getXmlFromAnnotation(b).equivalentXml(paramObject.getXmlFromAnnotation(b))) {
                bool2 = false;
                break;
              } 
              b++;
              continue;
            } 
            break;
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public Object get(int paramInt, Scriptable paramScriptable) {
    return (paramInt >= 0 && paramInt < length()) ? getXmlFromAnnotation(paramInt) : Scriptable.NOT_FOUND;
  }
  
  public String getClassName() {
    return "XMLList";
  }
  
  public Scriptable getExtraMethodSource(Context paramContext) {
    return (Scriptable)((length() == 1) ? getXmlFromAnnotation(0) : null);
  }
  
  public Object[] getIds() {
    Object[] arrayOfObject;
    if (isPrototype()) {
      arrayOfObject = new Object[0];
    } else {
      Object[] arrayOfObject1 = new Object[length()];
      byte b = 0;
      while (true) {
        arrayOfObject = arrayOfObject1;
        if (b < arrayOfObject1.length) {
          arrayOfObject1[b] = Integer.valueOf(b);
          b++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfObject;
  }
  
  public Object[] getIdsForDebug() {
    return getIds();
  }
  
  XmlNode.InternalList getNodeList() {
    return this._annos;
  }
  
  XML getXML() {
    return (length() == 1) ? getXmlFromAnnotation(0) : null;
  }
  
  Object getXMLProperty(XMLName paramXMLName) {
    return getPropertyList(paramXMLName);
  }
  
  public boolean has(int paramInt, Scriptable paramScriptable) {
    boolean bool;
    if (paramInt >= 0 && paramInt < length()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  boolean hasComplexContent() {
    boolean bool;
    int i = length();
    if (i == 0) {
      bool = false;
    } else if (i == 1) {
      bool = getXmlFromAnnotation(0).hasComplexContent();
    } else {
      boolean bool1 = false;
      byte b = 0;
      while (true) {
        bool = bool1;
        if (b < i) {
          if (getXmlFromAnnotation(b).isElement()) {
            bool = true;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
    } 
    return bool;
  }
  
  boolean hasOwnProperty(XMLName paramXMLName) {
    boolean bool = isPrototype();
    boolean bool1 = true;
    boolean bool2 = true;
    if (bool) {
      if (findPrototypeId(paramXMLName.localName()) == 0)
        bool2 = false; 
      return bool2;
    } 
    if (getPropertyList(paramXMLName).length() > 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  boolean hasSimpleContent() {
    if (length() == 0)
      return true; 
    if (length() == 1)
      return getXmlFromAnnotation(0).hasSimpleContent(); 
    for (byte b = 0; b < length(); b++) {
      if (getXmlFromAnnotation(b).isElement())
        return false; 
    } 
    return true;
  }
  
  boolean hasXMLProperty(XMLName paramXMLName) {
    boolean bool;
    if (getPropertyList(paramXMLName).length() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  XML item(int paramInt) {
    XML xML;
    if (this._annos != null) {
      xML = getXmlFromAnnotation(paramInt);
    } else {
      xML = createEmptyXML();
    } 
    return xML;
  }
  
  protected Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject) {
    if (paramArrayOfObject.length == 0)
      return newXMLList(); 
    Object object = paramArrayOfObject[0];
    return (!paramBoolean && object instanceof XMLList) ? object : newXMLListFrom(object);
  }
  
  int length() {
    int i = 0;
    XmlNode.InternalList internalList = this._annos;
    if (internalList != null)
      i = internalList.length(); 
    return i;
  }
  
  void normalize() {
    for (byte b = 0; b < length(); b++)
      getXmlFromAnnotation(b).normalize(); 
  }
  
  Object parent() {
    if (length() == 0)
      return Undefined.instance; 
    Object object = null;
    for (byte b = 0; b < length(); b++) {
      Object object1 = getXmlFromAnnotation(b).parent();
      if (!(object1 instanceof XML))
        return Undefined.instance; 
      object1 = object1;
      if (b == 0) {
        object = object1;
      } else if (!object.is((XML)object1)) {
        return Undefined.instance;
      } 
    } 
    return object;
  }
  
  XMLList processingInstructions(XMLName paramXMLName) {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).processingInstructions(paramXMLName)); 
    return xMLList;
  }
  
  boolean propertyIsEnumerable(Object paramObject) {
    long l;
    boolean bool = paramObject instanceof Integer;
    boolean bool1 = false;
    if (bool) {
      l = ((Integer)paramObject).intValue();
    } else if (paramObject instanceof Number) {
      double d = ((Number)paramObject).doubleValue();
      l = (long)d;
      if (l != d)
        return false; 
      if (l == 0L && 1.0D / d < 0.0D)
        return false; 
    } else {
      l = ScriptRuntime.testUint32String(ScriptRuntime.toString(paramObject));
    } 
    bool = bool1;
    if (0L <= l) {
      bool = bool1;
      if (l < length())
        bool = true; 
    } 
    return bool;
  }
  
  public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
    Object object = Undefined.instance;
    if (paramObject == null) {
      object = "null";
    } else {
      object = paramObject;
      if (paramObject instanceof Undefined)
        object = "undefined"; 
    } 
    if (object instanceof XMLObject) {
      object = object;
    } else if (this.targetProperty == null) {
      object = newXMLFromJs(object.toString());
    } else {
      XML xML = item(paramInt);
      paramObject = xML;
      if (xML == null) {
        paramObject = item(0);
        if (paramObject == null) {
          paramObject = newTextElementXML(null, this.targetProperty, null);
        } else {
          paramObject = paramObject.copy();
        } 
      } 
      ((XML)paramObject).setChildren(object);
      object = paramObject;
    } 
    if (paramInt < length()) {
      paramObject = item(paramInt).parent();
    } else if (length() == 0) {
      paramObject = this.targetObject;
      if (paramObject != null) {
        paramObject = paramObject.getXML();
      } else {
        paramObject = parent();
      } 
    } else {
      paramObject = parent();
    } 
    if (paramObject instanceof XML) {
      paramObject = paramObject;
      if (paramInt < length()) {
        XML xML = getXmlFromAnnotation(paramInt);
        if (object instanceof XML) {
          replaceNode(xML, (XML)object);
          replace(paramInt, xML);
        } else if (object instanceof XMLList) {
          object = object;
          if (object.length() > 0) {
            int i = xML.childIndex();
            replaceNode(xML, object.item(0));
            replace(paramInt, object.item(0));
            for (byte b = 1; b < object.length(); b++) {
              paramObject.insertChildAfter(paramObject.getXmlChild(i), object.item(b));
              i++;
              insert(paramInt + b, object.item(b));
            } 
          } 
        } 
      } else {
        paramObject.appendChild(object);
        addToList(paramObject.getLastXmlChild());
      } 
    } else if (paramInt < length()) {
      paramObject = getXML(this._annos, paramInt);
      if (object instanceof XML) {
        replaceNode((XML)paramObject, (XML)object);
        replace(paramInt, (XML)paramObject);
      } else if (object instanceof XMLList) {
        object = object;
        if (object.length() > 0) {
          replaceNode((XML)paramObject, object.item(0));
          replace(paramInt, object.item(0));
          for (byte b = 1; b < object.length(); b++)
            insert(paramInt + b, object.item(b)); 
        } 
      } 
    } else {
      addToList(object);
    } 
  }
  
  void putXMLProperty(XMLName paramXMLName, Object paramObject) {
    Object object;
    if (paramObject == null) {
      object = "null";
    } else {
      object = paramObject;
      if (paramObject instanceof Undefined)
        object = "undefined"; 
    } 
    if (length() <= 1) {
      if (length() == 0) {
        if (this.targetObject != null) {
          paramObject = this.targetProperty;
          if (paramObject != null && paramObject.getLocalName() != null && this.targetProperty.getLocalName().length() > 0) {
            addToList(newTextElementXML(null, this.targetProperty, null));
            if (paramXMLName.isAttributeName()) {
              setAttribute(paramXMLName, object);
            } else {
              item(0).putXMLProperty(paramXMLName, object);
              replace(0, item(0));
            } 
            paramXMLName = XMLName.formProperty(this.targetProperty.getNamespace().getUri(), this.targetProperty.getLocalName());
            this.targetObject.putXMLProperty(paramXMLName, this);
            replace(0, this.targetObject.getXML().getLastXmlChild());
          } else {
            throw ScriptRuntime.typeError("Assignment to empty XMLList without targets not supported");
          } 
        } else {
          throw ScriptRuntime.typeError("Assignment to empty XMLList without targets not supported");
        } 
      } else if (paramXMLName.isAttributeName()) {
        setAttribute(paramXMLName, object);
      } else {
        item(0).putXMLProperty(paramXMLName, object);
        replace(0, item(0));
      } 
      return;
    } 
    throw ScriptRuntime.typeError("Assignment to lists with more than one item is not supported");
  }
  
  void remove() {
    for (int i = length() - 1; i >= 0; i--) {
      XML xML = getXmlFromAnnotation(i);
      if (xML != null) {
        xML.remove();
        internalRemoveFromList(i);
      } 
    } 
  }
  
  void replace(int paramInt, XML paramXML) {
    if (paramInt < length()) {
      XmlNode.InternalList internalList = new XmlNode.InternalList();
      internalList.add(this._annos, 0, paramInt);
      internalList.add(paramXML);
      internalList.add(this._annos, paramInt + 1, length());
      this._annos = internalList;
    } 
  }
  
  void setTargets(XMLObjectImpl paramXMLObjectImpl, XmlNode.QName paramQName) {
    this.targetObject = paramXMLObjectImpl;
    this.targetProperty = paramQName;
  }
  
  XMLList text() {
    XMLList xMLList = newXMLList();
    for (byte b = 0; b < length(); b++)
      xMLList.addToList(getXmlFromAnnotation(b).text()); 
    return xMLList;
  }
  
  String toSource(int paramInt) {
    return toXMLString();
  }
  
  public String toString() {
    if (hasSimpleContent()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (byte b = 0; b < length(); b++) {
        XML xML = getXmlFromAnnotation(b);
        if (!xML.isComment() && !xML.isProcessingInstruction())
          stringBuilder.append(xML.toString()); 
      } 
      return stringBuilder.toString();
    } 
    return toXMLString();
  }
  
  String toXMLString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < length(); b++) {
      if (getProcessor().isPrettyPrinting() && b != 0)
        stringBuilder.append('\n'); 
      stringBuilder.append(getXmlFromAnnotation(b).toXMLString());
    } 
    return stringBuilder.toString();
  }
  
  Object valueOf() {
    return this;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/xmlimpl/XMLList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */